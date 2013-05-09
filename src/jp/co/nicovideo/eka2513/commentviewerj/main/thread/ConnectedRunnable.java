package jp.co.nicovideo.eka2513.commentviewerj.main.thread;

import jp.co.nicovideo.eka2513.commentviewerj.main.CommentViewerBase;
import jp.co.nicovideo.eka2513.commentviewerj.plugin.CommentViewerPluginBase;

/**
 * pluginのconnectedメソッドを呼び出します
 * @author eka2513
 *
 */
public class ConnectedRunnable implements Runnable {

	private CommentViewerPluginBase plugin;
	private CommentViewerBase source;

	@SuppressWarnings("unused")
	private ConnectedRunnable() {
	}

	public ConnectedRunnable(CommentViewerPluginBase plugin, CommentViewerBase source) {
		this.plugin = plugin;
		this.source = source;
	}

	@Override
	public void run() {
		plugin.connected(source);
	}

}
