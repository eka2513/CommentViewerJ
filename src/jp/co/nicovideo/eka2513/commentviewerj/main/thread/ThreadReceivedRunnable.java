package jp.co.nicovideo.eka2513.commentviewerj.main.thread;

import jp.co.nicovideo.eka2513.commentviewerj.event.PluginThreadEvent;
import jp.co.nicovideo.eka2513.commentviewerj.main.CommentViewerBase;
import jp.co.nicovideo.eka2513.commentviewerj.plugin.CommentViewerPluginBase;

/**
 * pluginのthreadReceivedメソッドを呼び出します
 * @author eka2513
 *
 */
public class ThreadReceivedRunnable implements Runnable {

	private CommentViewerPluginBase plugin;
	private CommentViewerBase source;
	private PluginThreadEvent event;

	@SuppressWarnings("unused")
	private ThreadReceivedRunnable() {
	}

	public ThreadReceivedRunnable(CommentViewerPluginBase plugin, CommentViewerBase source, PluginThreadEvent event) {
		this.plugin = plugin;
		this.source = source;
		this.event = event;
	}

	@Override
	public void run() {
		plugin.threadReceived(source, event);
	}

}
