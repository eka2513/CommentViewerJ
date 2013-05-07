package jp.co.nicovideo.eka2513.commentviewerj.eventlistener;

import java.util.EventListener;

import jp.co.nicovideo.eka2513.commentviewerj.event.CommentEvent;

public interface CommentEventListener extends EventListener {
	public void comReceived(CommentEvent e);
}
