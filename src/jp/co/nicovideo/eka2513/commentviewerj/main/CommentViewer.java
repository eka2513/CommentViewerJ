package jp.co.nicovideo.eka2513.commentviewerj.main;

import java.util.HashMap;

import jp.co.nicovideo.eka2513.commentviewerj.constants.CommentViewerConstants;
import jp.co.nicovideo.eka2513.commentviewerj.constants.PremiumConstants;
import jp.co.nicovideo.eka2513.commentviewerj.dto.ChatMessage;
import jp.co.nicovideo.eka2513.commentviewerj.dto.ChatResultMessage;
import jp.co.nicovideo.eka2513.commentviewerj.dto.ThreadMessage;
import jp.co.nicovideo.eka2513.commentviewerj.event.CommentEvent;
import jp.co.nicovideo.eka2513.commentviewerj.event.PluginCommentEvent;
import jp.co.nicovideo.eka2513.commentviewerj.event.PluginThreadEvent;
import jp.co.nicovideo.eka2513.commentviewerj.event.TimerPluginEvent;
import jp.co.nicovideo.eka2513.commentviewerj.main.eventlistener.GUIEventListener;
import jp.co.nicovideo.eka2513.commentviewerj.main.thread.CommentReceivedRunnable;
import jp.co.nicovideo.eka2513.commentviewerj.main.thread.CommentResultReceivedRunnable;
import jp.co.nicovideo.eka2513.commentviewerj.main.thread.ConnectedRunnable;
import jp.co.nicovideo.eka2513.commentviewerj.main.thread.DisconnectedRunnable;
import jp.co.nicovideo.eka2513.commentviewerj.main.thread.ThreadReceivedRunnable;
import jp.co.nicovideo.eka2513.commentviewerj.main.thread.TimerTickRunnable;
import jp.co.nicovideo.eka2513.commentviewerj.plugin.PluginBase;
import jp.co.nicovideo.eka2513.commentviewerj.util.CommentUtil;
import jp.co.nicovideo.eka2513.commentviewerj.util.SerializerUtil;
import jp.co.nicovideo.eka2513.commentviewerj.util.XMLUtil;
import jp.nicovideo.eka2513.cookiegetter4j.util.StringUtil;

public class CommentViewer extends CommentViewerBase {

	private GUIEventListener listener;

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
				listener.threadReceived(event);
				for (PluginBase p : plugins) {
					new Thread(new ThreadReceivedRunnable(p, this, event)).start();
				}
			} else if (tag.startsWith("<chat_result")) {
				//chat_resultタグ
				ChatResultMessage message = XMLUtil.getChatResultMessage(tag);
				PluginCommentEvent event = new PluginCommentEvent(this, message);
				listener.commentResultReceived(event);
				for (PluginBase p : plugins) {
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
						&& message.getPremium().equals(PremiumConstants.BROADCASTER.toString())) {
					//放送終了
					listener.disconnectReceived();
					for (PluginBase p : plugins) {
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
				if (StringUtil.inull2Val(message.getNo()) > 0 && lastCommentNo > 0 && lastCommentNo+1 != StringUtil.inull2Val(message.getNo())) {
					for (int i=1; i<StringUtil.inull2Val(message.getNo())-lastCommentNo; i++) {
						ChatMessage m = new ChatMessage();
						m.setNo(String.valueOf(lastCommentNo+i));
						m.setNgComment(true);
						m.setText("NGコメントです");
						PluginCommentEvent event = new PluginCommentEvent(this, m);
						listener.commentReceived(event);
						for (PluginBase p : plugins) {
							new Thread(new CommentReceivedRunnable(p, this, event)).start();
						}
					}
				}
				lastCommentNo = StringUtil.inull2Val(message.getNo());
				PluginCommentEvent event = new PluginCommentEvent(this, message, threadMessage, calcActive());
				listener.commentReceived(event);
				for (PluginBase p : plugins) {
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
		listener.ticked(e);
		for (PluginBase plugin : plugins) {
			new Thread(new TimerTickRunnable(plugin, this, e)).start();
		}
	}

	/**
	 * listenerを消去します
	 * @return listener
	 */
	public void removeListener() {
	    listener = null;
	}

	/**
	 * listenerを設定します。
	 * @param listener listener
	 */
	public void addListener(GUIEventListener listener) {
	    this.listener = listener;
	}
}
