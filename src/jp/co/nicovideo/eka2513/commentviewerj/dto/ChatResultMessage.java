package jp.co.nicovideo.eka2513.commentviewerj.dto;

import java.io.Serializable;

/**
 * コメント送信後に帰ってくるchat_result格納クラス.
 * @author eka2513
 *
 */
public class ChatResultMessage implements Serializable {

	private static final long serialVersionUID = 2746829917751817328L;

	private String thread;
	private String no;
	private String status;
	/**
	 * threadを取得します。
	 * @return thread
	 */
	public String getThread() {
	    return thread;
	}
	/**
	 * threadを設定します。
	 * @param thread thread
	 */
	public void setThread(String thread) {
	    this.thread = thread;
	}
	/**
	 * noを取得します。
	 * @return no
	 */
	public String getNo() {
	    return no;
	}
	/**
	 * noを設定します。
	 * @param no no
	 */
	public void setNo(String no) {
	    this.no = no;
	}
	/**
	 * statusを取得します。
	 * @return status
	 */
	public String getStatus() {
	    return status;
	}
	/**
	 * statusを設定します。
	 * @param status status
	 */
	public void setStatus(String status) {
	    this.status = status;
	}
}
