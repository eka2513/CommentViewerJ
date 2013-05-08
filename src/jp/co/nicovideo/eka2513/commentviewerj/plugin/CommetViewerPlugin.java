package jp.co.nicovideo.eka2513.commentviewerj.plugin;

import jp.co.nicovideo.eka2513.commentviewerj.event.PluginCommentEvent;
import jp.co.nicovideo.eka2513.commentviewerj.event.PluginThreadEvent;
import jp.co.nicovideo.eka2513.commentviewerj.event.TimerPluginEvent;

public interface CommetViewerPlugin {
	public void threadReceived(PluginThreadEvent e);
	public void commentReceived(PluginCommentEvent e);
	public void commentResultReceived(PluginCommentEvent e);
	public void tick(TimerPluginEvent e);
}
