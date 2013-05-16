package jp.co.nicovideo.eka2513.commentviewerj.main.thread;

import jp.co.nicovideo.eka2513.commentviewerj.event.PluginCommentEvent;
import jp.co.nicovideo.eka2513.commentviewerj.main.CommentViewerBase;
import jp.co.nicovideo.eka2513.commentviewerj.plugin.PluginBase;

/**
 * pluginのcommentReceivedメソッドを呼び出します
 * @author eka2513
 *
 */
public class CommentReceivedRunnable implements Runnable {

	private PluginBase plugin;
	private CommentViewerBase source;
	private PluginCommentEvent event;

	@SuppressWarnings("unused")
	private CommentReceivedRunnable() {
	}

	public CommentReceivedRunnable(PluginBase plugin, CommentViewerBase source, PluginCommentEvent event) {
		this.plugin = plugin;
		this.source = source;
		this.event = event;
	}

	@Override
	public void run() {
		plugin.commentReceived(source, event);
	}

}
