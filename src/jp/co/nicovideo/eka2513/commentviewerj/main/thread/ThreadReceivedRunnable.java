package jp.co.nicovideo.eka2513.commentviewerj.main.thread;

import jp.co.nicovideo.eka2513.commentviewerj.event.PluginThreadEvent;
import jp.co.nicovideo.eka2513.commentviewerj.main.CommentViewerBase;
import jp.co.nicovideo.eka2513.commentviewerj.plugin.PluginBase;

/**
 * pluginのthreadReceivedメソッドを呼び出します
 * @author eka2513
 *
 */
public class ThreadReceivedRunnable implements Runnable {

	private PluginBase plugin;
	private CommentViewerBase source;
	private PluginThreadEvent event;

	@SuppressWarnings("unused")
	private ThreadReceivedRunnable() {
	}

	public ThreadReceivedRunnable(PluginBase plugin, CommentViewerBase source, PluginThreadEvent event) {
		this.plugin = plugin;
		this.source = source;
		this.event = event;
	}

	@Override
	public void run() {
		plugin.threadReceived(source, event);
	}

}
