package jp.co.nicovideo.eka2513.commentviewerj.eventlistener;

import java.util.EventListener;

import jp.co.nicovideo.eka2513.commentviewerj.event.PluginSendEvent;
import jp.co.nicovideo.eka2513.commentviewerj.exception.CommentNotSendException;

public interface PluginSendEventListener extends EventListener {
	public void sendComment(PluginSendEvent event) throws CommentNotSendException;

	public void sendUneiComment(PluginSendEvent event) throws CommentNotSendException;

	public void sendUneiBSPComment(PluginSendEvent event) throws CommentNotSendException;

	public void sendBSPComment(PluginSendEvent event) throws CommentNotSendException;
}
