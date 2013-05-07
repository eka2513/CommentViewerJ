package jp.co.nicovideo.eka2513.commentviewerj.eventlistener;

import java.util.EventListener;

import jp.co.nicovideo.eka2513.commentviewerj.event.PluginCommentEvent;
import jp.co.nicovideo.eka2513.commentviewerj.event.PluginThreadEvent;

public interface PluginCommentEventListener extends EventListener {
	public void threadReceived(PluginThreadEvent e);
	public void commentReceived(PluginCommentEvent e);
	public void commentResultReceived(PluginCommentEvent e);
}
