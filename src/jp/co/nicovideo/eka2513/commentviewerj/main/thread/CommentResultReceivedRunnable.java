package jp.co.nicovideo.eka2513.commentviewerj.main.thread;

import jp.co.nicovideo.eka2513.commentviewerj.event.PluginCommentEvent;
import jp.co.nicovideo.eka2513.commentviewerj.main.CommentViewerBase;
import jp.co.nicovideo.eka2513.commentviewerj.plugin.CommentViewerPluginBase;

/**
 * pluginのcommentReceivedメソッドを呼び出します
 * @author eka2513
 *
 */
public class CommentResultReceivedRunnable implements Runnable {

	private CommentViewerPluginBase plugin;
	private CommentViewerBase source;
	private PluginCommentEvent event;

	@SuppressWarnings("unused")
	private CommentResultReceivedRunnable() {
	}

	public CommentResultReceivedRunnable(CommentViewerPluginBase plugin, CommentViewerBase source, PluginCommentEvent event) {
		this.plugin = plugin;
		this.source = source;
		this.event = event;
	}

	@Override
	public void run() {
		plugin.commentResultReceived(source, event);
	}

}
