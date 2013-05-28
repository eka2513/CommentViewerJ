package jp.co.nicovideo.eka2513.commentviewerj.plugin;

import jp.co.nicovideo.eka2513.commentviewerj.event.PluginSendEvent;
import jp.co.nicovideo.eka2513.commentviewerj.eventlistener.PluginSendEventListener;
import jp.co.nicovideo.eka2513.commentviewerj.exception.CommentNotSendException;


public abstract class PluginBase implements CommentViewerPlugin {

	private static final long serialVersionUID = -1193003055044323376L;

	private PluginSendEventListener listener;

	/** プラグインの名称 */
	public abstract  String getName();

	/** プラグインの名称 */
	public abstract String getVersion();

	public PluginBase() {
	}

	/**
	 * コメントを送信します
	 * @param mail mail
	 * @param comment comment
	 * @throws CommentNotSendException CommentNotSendException
	 */
	protected final void sendComment(String mail, String comment) throws CommentNotSendException{
		listener.sendComment(new PluginSendEvent(this, mail, comment, null));
	}

	/**
	 * 運営コメントを送信します
	 * @param mail mail
	 * @param comment comment
	 * @param handle handle
	 * @throws CommentNotSendException CommentNotSendException
	 */
	protected final void sendUneiComment(String mail, String comment, String handle) throws CommentNotSendException {
		listener.sendUneiComment(new PluginSendEvent(this, mail, comment, null));
	}

	/**
	 * 運営コメントでなんちゃってBSPコメントを送信します
	 * @param mail mail
	 * @param comment comment
	 * @param handle handle
	 * @param bspColor bspColor
	 * @throws CommentNotSendException CommentNotSendException
	 */
	protected final void sendUneiBSPComment(String mail, String comment, String handle, String bspColor) throws CommentNotSendException {
		listener.sendUneiBSPComment(new PluginSendEvent(this, mail, comment, handle, bspColor));
	}

	/**
	 * BSPコメントを送信します
	 * @param mail mail
	 * @param comment comment
	 * @param handle handle
	 * @param bspColor bspColor
	 * @throws CommentNotSendException CommentNotSendException
	 */
	protected final void sendBSPComment(String mail, String comment, String handle, String bspColor) throws CommentNotSendException {
		listener.sendBSPComment(new PluginSendEvent(this, mail, comment, handle, bspColor));
	}

	/**
	 * listenerを設定します。
	 * @param listener listener
	 */
	public void addListener(PluginSendEventListener listener) {
	    this.listener = listener;
	}
}
