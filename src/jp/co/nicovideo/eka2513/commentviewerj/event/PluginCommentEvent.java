package jp.co.nicovideo.eka2513.commentviewerj.event;

import jp.co.nicovideo.eka2513.commentviewerj.dto.ChatMessage;
import jp.co.nicovideo.eka2513.commentviewerj.dto.ChatResultMessage;

/**
 * プラグインコメントイベント
 * @author eka2513
 */
public class PluginCommentEvent extends PluginEventBase {

	private static final long serialVersionUID = -6031446571923422360L;

	private ChatMessage message;
	private ChatResultMessage result;

	private Integer active;

	/**
	 * コンストラクタ
	 * @param source source
	 * @param message message
	 * @param active active
	 */
	public PluginCommentEvent(Object source, ChatMessage message, Integer active) {
		super(source);
		this.active = active;
		this.message = message;
	}

	/**
	 * コンストラクタ
	 * @param source source
	 * @param message message
	 */
	public PluginCommentEvent(Object source, ChatMessage message) {
		super(source);
		this.message = message;
	}

	/**
	 * コンストラクタ
	 * @param source
	 * @param message
	 */
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
