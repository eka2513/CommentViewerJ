package jp.co.nicovideo.eka2513.commentviewerj.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.nicovideo.eka2513.commentviewerj.constants.CommentViewerConstants;
import jp.co.nicovideo.eka2513.commentviewerj.dto.ChatMessage;
import jp.co.nicovideo.eka2513.commentviewerj.dto.ThreadMessage;
import jp.co.nicovideo.eka2513.commentviewerj.event.CommentEvent;
import jp.co.nicovideo.eka2513.commentviewerj.event.PluginCommentEvent;
import jp.co.nicovideo.eka2513.commentviewerj.event.PluginSendEvent;
import jp.co.nicovideo.eka2513.commentviewerj.event.PluginThreadEvent;
import jp.co.nicovideo.eka2513.commentviewerj.eventlistener.CommentEventListener;
import jp.co.nicovideo.eka2513.commentviewerj.eventlistener.PluginSendEventListener;
import jp.co.nicovideo.eka2513.commentviewerj.exception.CommentNotSendException;
import jp.co.nicovideo.eka2513.commentviewerj.plugin.AbstractCommentViewerPlugin;
import jp.co.nicovideo.eka2513.commentviewerj.util.CommentThread;
import jp.co.nicovideo.eka2513.commentviewerj.util.NicoRequestUtil;
import jp.co.nicovideo.eka2513.commentviewerj.util.PluginUtil;
import jp.co.nicovideo.eka2513.commentviewerj.util.SerializerUtil;
import jp.co.nicovideo.eka2513.commentviewerj.util.XMLUtil;
import jp.nicovideo.eka2513.cookiegetter4j.util.StringUtil;

public abstract class AbstractCommentViewer implements CommentEventListener, PluginSendEventListener {

	private String cookie;
	private String browser;
	private String lv;

	private Integer lastCommentNo;

	private CommentThread comThread;
	private Map<String, Long> activeCache;
	private HashMap<String, String> handleNameCache;

	private List<AbstractCommentViewerPlugin> plugins;

	private ThreadMessage threadMessage;
	private Map<String, String> playerstatus;
	private Map<String, String> publishstatus;
	private String presscastToken;

	protected void connect() {
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
		for (AbstractCommentViewerPlugin p : plugins) {
			p.addListener(this);
		}
		comThread = new CommentThread(
				playerstatus.get(CommentViewerConstants.ADDR),
				playerstatus.get(CommentViewerConstants.PORT),
				playerstatus.get(CommentViewerConstants.THREAD));
		comThread.setCommentEventListener(this);
		comThread.start();
	}

	public void disconnect() {
		comThread.exit();
	}

	private StringBuffer buf = new StringBuffer();

	private Integer calcActive() {
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
	 * コメントサーバ接続のスレッドからxml受信時に呼ばれるメソッド
	 */
	@Override
	public void comReceived(CommentEvent e) {
		String xml = e.getXml();
		String[] tags = xml.split("\0");
		String handleName = null;
		for (String tag : tags) {
			if (tag.startsWith("<thread")) {
				if (!tag.endsWith("/>")) {
					buf.append(tag);
					continue;
				}
				ThreadMessage message = XMLUtil.getThreadMessage(tag);
				threadMessage = message;
				PluginThreadEvent event = new PluginThreadEvent(this, message);
				for (AbstractCommentViewerPlugin p : plugins) {
					p.threadReceived(event);
				}
			} else if (tag.startsWith("<chat_result")) {
				System.out.println(tag);
			} else {
				if (!tag.endsWith("</chat>")) {
					buf.append(tag);
					continue;
				}
				tag = buf.append(tag).toString();
				buf.setLength(0);
				ChatMessage message = XMLUtil.getChatMessage(tag);
				activeCache.put(message.getUser_id(), System.currentTimeMillis());
				handleName = StringUtil.groupMatchFirst("[＠@]([^\\s　]+)", message.getText());
				if (handleName != null && handleName.length() > 0) {
					handleNameCache.put(message.getUser_id(), handleName);
					new SerializerUtil<HashMap<String, String>>().save(CommentViewerConstants.HANDLE_NAME_DB, handleNameCache);
				}
				if (handleNameCache.containsKey(message.getUser_id())) {
					message.setHandleName(handleNameCache.get(message.getUser_id()));
				}
				lastCommentNo = StringUtil.inull2Val(message.getNo());
				PluginCommentEvent event = new PluginCommentEvent(this, message, calcActive());
				for (AbstractCommentViewerPlugin p : plugins) {
					p.commentReceived(event);
				}
			}
//			System.out.println(tag);
		}
	}

	/**
	 * プラグインからコールされるコメント送信メソッド。
	 * @param PluginSendEvent
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
	 * @param PluginSendEvent
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
	 * @param PluginSendEvent
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
	 * @param PluginSendEvent
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
	 * browserを取得します。
	 * @return browser
	 */
	public String getBrowser() {
	    return browser;
	}

	/**
	 * browserを設定します。
	 * @param browser browser
	 */
	public void setBrowser(String browser) {
	    this.browser = browser;
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
}
