package jp.co.nicovideo.eka2513.commentviewerj.main.thread;

import jp.co.nicovideo.eka2513.commentviewerj.event.TimerPluginEvent;
import jp.co.nicovideo.eka2513.commentviewerj.main.CommentViewerBase;
import jp.co.nicovideo.eka2513.commentviewerj.plugin.CommentViewerPluginBase;

/**
 * pluginのtickメソッドを呼び出します
 * @author eka2513
 *
 */
public class TimerTickRunnable implements Runnable {

	private CommentViewerPluginBase plugin;
	private CommentViewerBase source;
	private TimerPluginEvent event;

	@SuppressWarnings("unused")
	private TimerTickRunnable() {
	}

	public TimerTickRunnable(CommentViewerPluginBase plugin, CommentViewerBase source, TimerPluginEvent event) {
		this.plugin = plugin;
		this.source = source;
		this.event = event;
	}

	@Override
	public void run() {
		plugin.tick(source, event);
	}

}
