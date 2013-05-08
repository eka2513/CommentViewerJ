package jp.co.nicovideo.eka2513.commentviewerj.plugin;

import jp.co.nicovideo.eka2513.commentviewerj.event.PluginCommentEvent;
import jp.co.nicovideo.eka2513.commentviewerj.event.PluginThreadEvent;
import jp.co.nicovideo.eka2513.commentviewerj.event.TimerPluginEvent;
import jp.co.nicovideo.eka2513.commentviewerj.main.CommentViewerBase;

public interface CommetViewerPlugin {
	public void threadReceived(CommentViewerBase source, PluginThreadEvent e);
	public void commentReceived(CommentViewerBase source, PluginCommentEvent e);
	public void commentResultReceived(CommentViewerBase source, PluginCommentEvent e);
	public void tick(CommentViewerBase source, TimerPluginEvent e);
}
