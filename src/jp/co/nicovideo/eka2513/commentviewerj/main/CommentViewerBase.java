package jp.co.nicovideo.eka2513.commentviewerj.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import jp.co.nicovideo.eka2513.commentviewerj.constants.CommentViewerConstants;
import jp.co.nicovideo.eka2513.commentviewerj.constants.PremiumConstants;
import jp.co.nicovideo.eka2513.commentviewerj.dto.ChatMessage;
import jp.co.nicovideo.eka2513.commentviewerj.dto.ChatResultMessage;
import jp.co.nicovideo.eka2513.commentviewerj.dto.ThreadMessage;
import jp.co.nicovideo.eka2513.commentviewerj.event.CommentEvent;
import jp.co.nicovideo.eka2513.commentviewerj.event.PluginCommentEvent;
import jp.co.nicovideo.eka2513.commentviewerj.event.PluginSendEvent;
import jp.co.nicovideo.eka2513.commentviewerj.event.PluginThreadEvent;
import jp.co.nicovideo.eka2513.commentviewerj.event.TimerPluginEvent;
import jp.co.nicovideo.eka2513.commentviewerj.eventlistener.CommentEventListener;
import jp.co.nicovideo.eka2513.commentviewerj.eventlistener.PluginSendEventListener;
import jp.co.nicovideo.eka2513.commentviewerj.eventlistener.TimerPluginEventListener;
import jp.co.nicovideo.eka2513.commentviewerj.exception.CommentNotSendException;
import jp.co.nicovideo.eka2513.commentviewerj.main.settings.CommentThread;
import jp.co.nicovideo.eka2513.commentviewerj.main.settings.GlobalSetting;
import jp.co.nicovideo.eka2513.commentviewerj.main.thread.CommentReceivedRunnable;
import jp.co.nicovideo.eka2513.commentviewerj.main.thread.CommentResultReceivedRunnable;
import jp.co.nicovideo.eka2513.commentviewerj.main.thread.ConnectedRunnable;
import jp.co.nicovideo.eka2513.commentviewerj.main.thread.DisconnectedRunnable;
import jp.co.nicovideo.eka2513.commentviewerj.main.thread.ThreadReceivedRunnable;
import jp.co.nicovideo.eka2513.commentviewerj.main.thread.TimerTickRunnable;
import jp.co.nicovideo.eka2513.commentviewerj.plugin.CommentViewerPluginBase;
import jp.co.nicovideo.eka2513.commentviewerj.util.CommentUtil;
import jp.co.nicovideo.eka2513.commentviewerj.util.GlobalSettingUtil;
import jp.co.nicovideo.eka2513.commentviewerj.util.NicoRequestUtil;
import jp.co.nicovideo.eka2513.commentviewerj.util.PluginUtil;
import jp.co.nicovideo.eka2513.commentviewerj.util.SerializerUtil;
import jp.co.nicovideo.eka2513.commentviewerj.util.XMLUtil;
import jp.nicovideo.eka2513.cookiegetter4j.cookie.NicoCookieManagerFactory;
import jp.nicovideo.eka2513.cookiegetter4j.util.StringUtil;

public class CommentViewerBase implements CommentEventListener, PluginSendEventListener, TimerPluginEventListener {

	protected String cookie;
	protected String browser;
	protected String lv;

	protected Integer lastCommentNo = 0;

	protected CommentThread comThread;
	protected Map<String, Long> activeCache;
	protected HashMap<String, String> handleNameCache;

	protected List<CommentViewerPluginBase> plugins;

	protected Timer timer;

	protected ThreadMessage threadMessage;
	protected Map<String, String> playerstatus;
	protected Map<String, String> publishstatus;
	protected String presscastToken;

	protected GlobalSetting globalSetting;

	/**
	 * 放送主かどうかを返します
	 * @return
	 */
	public boolean isBroadcaster() {
		if (publishstatus.containsKey(CommentViewerConstants.STATUS))
			return !publishstatus.get(CommentViewerConstants.STATUS).equals("fail");
		return false;
	}

	/**
	 * BSPかどうかを返します
	 * @return
	 */
	public boolean isBSP() {
		return (presscastToken != null);
	}


	/**
	 * アクティブ人数を返します
	 * @return アクティブ人数
	 */
	public Integer getActive() {
		return activeCache.size();
	}

	protected CommentViewerBase() {
		//グローバル設定ロード。(再ロードは保存時)
		globalSetting = GlobalSettingUtil.load();
		if (globalSetting != null && globalSetting.getGeneralSetting() != null) {
			this.cookie = NicoCookieManagerFactory
					.getInstance(globalSetting.getGeneralSetting().getBrowser())
						.getSessionCookie().toCookieString();
		} else {
			globalSetting = new GlobalSetting();
		}
	}

	public void connect() {
		//init active
		activeCache = new HashMap<String, Long>();
		//load cache
		handleNameCache = new SerializerUtil<HashMap<String, String>>().load(CommentViewerConstants.HANDLE_NAME_DB);
		if (handleNameCache == null)
			handleNameCache = new HashMap<String, String>();

		NicoRequestUtil util = new NicoRequestUtil();
		util.setCookieString(cookie);
		String xml = util.getPlayerStatus(String.format("%s", lv));
		playerstatus = XMLUtil.parsePlayerStatus(xml);
		xml = util.getPublishStatus(String.format("%s", lv));
		publishstatus = XMLUtil.parsePublishStatus(xml);
		presscastToken = util.getPresscastToken(lv);
//		plugins.add(this);
		plugins = PluginUtil.loadPlugins();
		//イベントリスナを登録
		for (CommentViewerPluginBase p : plugins) {
			p.addListener(this);
		}
		comThread = new CommentThread(
				playerstatus.get(CommentViewerConstants.ADDR),
				playerstatus.get(CommentViewerConstants.PORT),
				playerstatus.get(CommentViewerConstants.THREAD));
		comThread.setCommentEventListener(this);
		comThread.start();

		//タイマーの駆動
		if (timer != null)
			timer.cancel();
		timer = new Timer();
		TimerPluginTask timerPluginTask = new TimerPluginTask();
		timerPluginTask.addTimerPluginEventListener(this);
		timer.scheduleAtFixedRate(timerPluginTask, 1000, 1000);
	}

	public void disconnect() {
		if (comThread != null && comThread.isAlive())
			comThread.exit();
		if (timer != null)
			timer.cancel();
	}

	protected StringBuffer buf = new StringBuffer();

	protected Integer calcActive() {
		Long now = System.currentTimeMillis();
		Long tenminago = now - 60*10*1000;
		int active = 0;
		//10分以上経過したキャッシュ以外の件数を取得
		for (String key : activeCache.keySet()) {
			if (activeCache.get(key) >= tenminago) {
				active++;
			}
		}
		return active;
	}

	/**
	 * コメントサーバ接続のスレッドからxml受信時に呼ばれる
	 * @param e CommentEvent
	 */
	@Override
	public synchronized void comReceived(CommentEvent e) {
		String xml = e.getXml();
		String[] tags = xml.split("\0");
		String handleName = null;
		for (String tag : tags) {
			if (tag.startsWith("<thread")) {
				//threadタグ
				if (!tag.endsWith("/>")) {
					buf.append(tag);
					continue;
				}
				ThreadMessage message = XMLUtil.getThreadMessage(tag);
				threadMessage = message;
				lastCommentNo = StringUtil.inull2Val(threadMessage.getLast_res());
				PluginThreadEvent event = new PluginThreadEvent(this, message);
				for (CommentViewerPluginBase p : plugins) {
					new Thread(new ThreadReceivedRunnable(p, this, event)).start();
				}
			} else if (tag.startsWith("<chat_result")) {
				//chat_resultタグ
				ChatResultMessage message = XMLUtil.getChatResultMessage(tag);
				PluginCommentEvent event = new PluginCommentEvent(this, message);
				for (CommentViewerPluginBase p : plugins) {
					new Thread(new ConnectedRunnable(p, this)).start();
					new Thread(new CommentResultReceivedRunnable(p, this, event)).start();
				}
			} else {
				//chatタグ
				if (!tag.endsWith("</chat>")) {
					buf.append(tag);
					continue;
				}
				tag = buf.append(tag).toString();
				buf.setLength(0);
				ChatMessage message = XMLUtil.getChatMessage(tag);
				int diff = StringUtil.inull2Val(playerstatus.get(CommentViewerConstants.BASE_TIME)) - StringUtil.inull2Val(playerstatus.get(CommentViewerConstants.START_TIME));
				message.setVposFromStartTime(String.valueOf(StringUtil.inull2Val(message.getVpos()) + diff));
				if (message.getText().startsWith("/disconnect")
						&& message.getPremium().equals(PremiumConstants.SYSTEM_UNEI.toString())) {
					//放送終了
					for (CommentViewerPluginBase p : plugins) {
						new Thread(new DisconnectedRunnable(p, this)).start();
					}
					disconnect();
				}
				//放送主・運営はアクティブと初コメの判定をしない
				if (!message.getPremium().equals(PremiumConstants.BROADCASTER.toString())
						&& !message.getPremium().equals(PremiumConstants.SYSTEM_UNEI.toString())) {
					if (!activeCache.containsKey(message.getUser_id())) {
						//初コメ
						message.setFirstComment(true);
					}
					activeCache.put(message.getUser_id(), System.currentTimeMillis());
				}

				//コテハン上書き
				handleName = StringUtil.groupMatchFirst("[＠@]([^\\s　]+)", message.getText());
				if (handleName != null && handleName.length() > 0) {
					if (handleNameCache.containsKey(message.getUser_id())) {
						if (globalSetting.getHandleNameSetting().isOverwrite()) {
							handleNameCache.put(message.getUser_id(), handleName);
							new SerializerUtil<HashMap<String, String>>().save(CommentViewerConstants.HANDLE_NAME_DB, handleNameCache);
						}
					} else {
						handleNameCache.put(message.getUser_id(), handleName);
						new SerializerUtil<HashMap<String, String>>().save(CommentViewerConstants.HANDLE_NAME_DB, handleNameCache);
					}
				}

				if (handleNameCache.containsKey(message.getUser_id())) {
					message.setHandleName(handleNameCache.get(message.getUser_id()));
				}

				//NG判定
				if (lastCommentNo+1 != StringUtil.inull2Val(message.getNo())) {
					for (int i=1; i<StringUtil.inull2Val(message.getNo())-lastCommentNo; i++) {
						ChatMessage m = new ChatMessage();
						m.setNo(String.valueOf(lastCommentNo+i));
						m.setNgComment(true);
						PluginCommentEvent event = new PluginCommentEvent(this, m);
						for (CommentViewerPluginBase p : plugins) {
							new Thread(new CommentReceivedRunnable(p, this, event)).start();
						}
					}
				}
				lastCommentNo = StringUtil.inull2Val(message.getNo());
				PluginCommentEvent event = new PluginCommentEvent(this, message, calcActive());
				for (CommentViewerPluginBase p : plugins) {
					new Thread(new CommentReceivedRunnable(p, this, event)).start();
				}
			}
		}
	}

	/**
	 * タイマーイベントリスナで駆動
	 * @param e タイマーイベント
	 */
	@Override
	public void timerTicked(TimerPluginEvent e) {
		String baseTime = playerstatus.get(CommentViewerConstants.BASE_TIME);
		String vpos = CommentUtil.calcVpos(baseTime, e.getCurrentTime().toString());
		e.setVpos(Integer.valueOf(vpos));
		//プラグインのtickメソッドを呼び出す
		for (CommentViewerPluginBase plugin : plugins) {
			new Thread(new TimerTickRunnable(plugin, this, e)).start();
		}
	}

	/**
	 * プラグインからコールされるコメント送信メソッド。
	 * @param event event
	 * @throws CommentNotSendException CommentNotSendException
	 */
	@Override
	public void sendComment(PluginSendEvent event) throws CommentNotSendException {
		if (comThread == null || !comThread.isAlive() || !comThread.isThreadWorking()) {
			throw new CommentNotSendException("cannot find socket thread");
		}
		if (event.getComment() == null || event.getComment().length() == 0) {
			return;
		}
		comThread.sendComment(event.getMail(), event.getComment(), cookie, lastCommentNo, playerstatus, threadMessage);
	}

	/**
	 * プラグインからコールされる運営コメント送信メソッド。
	 * @param event PluginSendEvent
	 * @throws CommentNotSendException CommentNotSendException
	 */
	@Override
	public void sendUneiComment(PluginSendEvent event) throws CommentNotSendException {
		if (publishstatus.get(CommentViewerConstants.STATUS).equals("fail")) {
			throw new CommentNotSendException("you has no authority for sending broadcast comment");
		}
		new NicoRequestUtil(cookie).sendBroadcastComment(lv, event.getMail(), event.getComment(), event.getHandle(), publishstatus.get(CommentViewerConstants.TOKEN));
	}

	/**
	 * プラグインからコールされるなんちゃってBSP送信メソッド。
	 * @param event PluginSendEvent
	 * @throws CommentNotSendException CommentNotSendException
	 */
	@Override
	public void sendUneiBSPComment(PluginSendEvent event) throws CommentNotSendException {
		if (publishstatus.get(CommentViewerConstants.STATUS).equals("fail")) {
			throw new CommentNotSendException("you has no authority for sending broadcast comment");
		}
		//半角SP対応
		String comment = event.getComment()
				.replaceAll("%5C", "%5C%5C")
				.replaceAll("%22", "%5C%22")
				.replace("%20", "+");
		comment = String.format("/press show %s \"%s\" %s", event.getBspColor(), comment, event.getHandle());
		boolean result = new NicoRequestUtil(cookie).sendBroadcastComment(lv, event.getMail(), comment, event.getHandle(), publishstatus.get(CommentViewerConstants.TOKEN));
		if (!result) {
			throw new CommentNotSendException("cannot send broadcast comment");
		}
	}

	/**
	 * プラグインからコールされるBSP送信メソッド。
	 * @param event PluginSendEvent
	 * @throws CommentNotSendException CommentNotSendException
	 */
	@Override
	public void sendBSPComment(PluginSendEvent event) throws CommentNotSendException {
		if (presscastToken == null || presscastToken.length() == 0) {
			throw new CommentNotSendException("you has no authority for sending bsp comment");
		}
		boolean result = new NicoRequestUtil(cookie).sendBspComment(lv, event.getComment(), event.getBspColor(), event.getHandle(), presscastToken);
		if (!result) {
			throw new CommentNotSendException("cannot send bsp comment");
		}
	}

	/**
	 * cookieを取得します。
	 * @return cookie
	 */
	public String getCookie() {
	    return cookie;
	}

	/**
	 * cookieを設定します。
	 * @param cookie cookie
	 */
	public void setCookie(String cookie) {
	    this.cookie = cookie;
	}

	/**
	 * browserを設定します。
	 * @param browser browser
	 */
	public void setBrowser(String browser) {
	    this.browser = browser;
	}

	/**
	 * browserを取得します。
	 * @return browser
	 */
	public String getBrowser() {
	    return browser;
	}

	/**
	 * lvを取得します。
	 * @return lv
	 */
	public String getLv() {
	    return lv;
	}

	/**
	 * lvを設定します。
	 * @param lv lv
	 */
	public void setLv(String lv) {
	    this.lv = lv;
	}

	/**
	 * handleNameCacheを取得します。
	 * @return handleNameCache
	 */
	public HashMap<String,String> getHandleNameCache() {
	    return handleNameCache;
	}

	/**
	 * playerstatusを取得します。
	 * @return playerstatus
	 */
	public Map<String,String> getPlayerstatus() {
	    return playerstatus;
	}

	/**
	 * playerstatusを設定します。
	 * @param playerstatus playerstatus
	 */
	public void setPlayerstatus(Map<String,String> playerstatus) {
	    this.playerstatus = playerstatus;
	}

	/**
	 * globalSettingを取得します。
	 * @return globalSetting
	 */
	public GlobalSetting getGlobalSetting() {
	    return globalSetting;
	}
}
