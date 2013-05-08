package jp.co.nicovideo.eka2513.commentviewerj.eventlistener;

import java.util.EventListener;

import jp.co.nicovideo.eka2513.commentviewerj.event.PluginSendEvent;
import jp.co.nicovideo.eka2513.commentviewerj.exception.CommentNotSendException;

/**
 * プラグインからコメント送信時に本体側に送る際に使用するリスナ
 * @author eka2513
 *
 */
public interface PluginSendEventListener extends EventListener {

	/**
	 * コメント送信
	 * @param event PluginSendEvent
	 * @throws CommentNotSendException CommentNotSendException
	 */
	public void sendComment(PluginSendEvent event) throws CommentNotSendException;

	/**
	 * 運営コメント送信
	 * @param event PluginSendEvent
	 * @throws CommentNotSendException CommentNotSendException
	 */
	public void sendUneiComment(PluginSendEvent event) throws CommentNotSendException;

	/**
	 * なんちゃってBSPコメ送信
	 * @param event PluginSendEvent
	 * @throws CommentNotSendException CommentNotSendException
	 */
	public void sendUneiBSPComment(PluginSendEvent event) throws CommentNotSendException;

	/**
	 * BSPコメント送信
	 * @param event PluginSendEvent
	 * @throws CommentNotSendException CommentNotSendException
	 */
	public void sendBSPComment(PluginSendEvent event) throws CommentNotSendException;
}
