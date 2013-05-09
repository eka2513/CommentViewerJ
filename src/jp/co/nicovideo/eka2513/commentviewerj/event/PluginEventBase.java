package jp.co.nicovideo.eka2513.commentviewerj.event;

import java.util.EventObject;

import jp.co.nicovideo.eka2513.commentviewerj.main.CommentViewerBase;

public class PluginEventBase extends EventObject {

	private static final long serialVersionUID = -7541293730688355802L;

	private boolean broadcaster;

	private boolean bsp;

	public PluginEventBase(Object source) {
		super(source);
		if (source instanceof CommentViewerBase) {
			this.broadcaster = ((CommentViewerBase) source).isBroadcaster();
			this.bsp = ((CommentViewerBase) source).isBSP();
		}
	}

	/**
	 * broadcasterを取得します。
	 * @return broadcaster
	 */
	public boolean isBroadcaster() {
	    return broadcaster;
	}

	/**
	 * bspを取得します。
	 * @return bsp
	 */
	public boolean isBsp() {
	    return bsp;
	}

}
