package jp.co.nicovideo.eka2513.commentviewerj.eventlistener;

import java.util.EventListener;

import jp.co.nicovideo.eka2513.commentviewerj.event.CommentEvent;

/**
 * コメント受信リスナ
 * @author eka2513
 *
 */
public interface CommentEventListener extends EventListener {
	/**
	 * コメント受信
	 * @param e CommentEvent
	 */
	public void comReceived(CommentEvent e);
}
