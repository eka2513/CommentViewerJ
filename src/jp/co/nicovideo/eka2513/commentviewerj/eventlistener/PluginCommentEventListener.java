package jp.co.nicovideo.eka2513.commentviewerj.eventlistener;

import java.util.EventListener;

import jp.co.nicovideo.eka2513.commentviewerj.event.PluginCommentEvent;
import jp.co.nicovideo.eka2513.commentviewerj.event.PluginThreadEvent;

/**
 * プラグインコメント通知リスナ
 * @author eka2513
 *
 */
public interface PluginCommentEventListener extends EventListener {
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
}
