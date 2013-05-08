package jp.co.nicovideo.eka2513.commentviewerj.event;

import java.util.EventObject;

import jp.co.nicovideo.eka2513.commentviewerj.dto.ThreadMessage;

/**
 * threadタグ用のイベント
 * @author eka2513
 */
public class PluginThreadEvent extends EventObject {

	private static final long serialVersionUID = 3851925236511663402L;

	private ThreadMessage message;

	public PluginThreadEvent(Object source, ThreadMessage message) {
		super(source);
		this.message = message;
	}

	/**
	 * messageを取得します。
	 * @return message
	 */
	public ThreadMessage getMessage() {
	    return message;
	}
}
