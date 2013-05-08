package jp.co.nicovideo.eka2513.commentviewerj.event;

import java.util.EventObject;

import jp.co.nicovideo.eka2513.commentviewerj.dto.ChatMessage;
import jp.co.nicovideo.eka2513.commentviewerj.dto.ChatResultMessage;

public class PluginCommentEvent extends EventObject {

	private static final long serialVersionUID = -6031446571923422360L;

	private ChatMessage message;
	private ChatResultMessage result;

	private Integer active;

	public PluginCommentEvent(Object source, ChatMessage message, Integer active) {
		super(source);
		this.active = active;
		this.message = message;
	}

	public PluginCommentEvent(Object source, ChatMessage message) {
		super(source);
		this.message = message;
	}

	public PluginCommentEvent(Object source, ChatResultMessage message) {
		super(source);
		this.result = message;
	}

	/**
	 * messageを取得します。
	 * @return message
	 */
	public ChatMessage getMessage() {
	    return message;
	}

	/**
	 * resultを取得します。
	 * @return result
	 */
	public ChatResultMessage getResult() {
	    return result;
	}

	/**
	 * activeを取得します。
	 * @return active
	 */
	public Integer getActive() {
	    return active;
	}
}
