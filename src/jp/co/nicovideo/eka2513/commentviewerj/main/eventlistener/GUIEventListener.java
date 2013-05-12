package jp.co.nicovideo.eka2513.commentviewerj.main.eventlistener;

import java.util.EventListener;

import jp.co.nicovideo.eka2513.commentviewerj.event.PluginCommentEvent;
import jp.co.nicovideo.eka2513.commentviewerj.event.PluginThreadEvent;
import jp.co.nicovideo.eka2513.commentviewerj.event.TimerPluginEvent;

/**
 * コメント受信リスナ
 * @author eka2513
 *
 */
public interface GUIEventListener extends EventListener {
	/**
	 * threadタグ受信
	 * @param e PluginThreadEvent
	 */
	public void threadReceived(PluginThreadEvent e);

	/**
	 * chatタグ受信
	 * @param e PluginCommentEvent
	 */
	public void commentReceived(PluginCommentEvent e);

	/**
	 * chat_resultタグ受信
	 * @param e PluginCommentEvent
	 */
	public void commentResultReceived(PluginCommentEvent e);

	/**
	 * 放送終了受信
	 * @param e PluginCommentEvent
	 */
	public void disconnectReceived();

	/**
	 * 放送接続受信
	 */
	public void connectReceived();

	/**
	 * タイマーイベント受信
	 * @param e TimerPluginEvent
	 */
	public void ticked(TimerPluginEvent e);

}
