package jp.co.nicovideo.eka2513.commentviewerj.main.thread;

import jp.co.nicovideo.eka2513.commentviewerj.main.CommentViewerBase;
import jp.co.nicovideo.eka2513.commentviewerj.plugin.PluginBase;

/**
 * pluginのdisconnectedメソッドを呼び出します
 * @author eka2513
 *
 */
public class DisconnectedRunnable implements Runnable {

	private PluginBase plugin;
	private CommentViewerBase source;

	@SuppressWarnings("unused")
	private DisconnectedRunnable() {
	}

	public DisconnectedRunnable(PluginBase plugin, CommentViewerBase source) {
		this.plugin = plugin;
		this.source = source;
	}

	@Override
	public void run() {
		plugin.disconnected(source);
	}

}
