package jp.co.nicovideo.eka2513.commentviewerj.dto;

import java.io.Serializable;

/**
 * コメントサーバから送られたthreadタグを保持します
 * @author eka2513
 */
public class ThreadMessage implements Serializable {

	private static final long serialVersionUID = 98378971841508114L;

	private String resultcode;
	private String thread;
	private String last_res;
	private String ticket;
	private String revision;
	private String server_time;
	/**
	 * resultcodeを取得します。
	 * @return resultcode
	 */
	public String getResultcode() {
	    return resultcode;
	}
	/**
	 * resultcodeを設定します。
	 * @param resultcode resultcode
	 */
	public void setResultcode(String resultcode) {
	    this.resultcode = resultcode;
	}
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
	 * last_resを取得します。
	 * @return last_res
	 */
	public String getLast_res() {
	    return last_res;
	}
	/**
	 * last_resを設定します。
	 * @param last_res last_res
	 */
	public void setLast_res(String last_res) {
	    this.last_res = last_res;
	}
	/**
	 * ticketを取得します。
	 * @return ticket
	 */
	public String getTicket() {
	    return ticket;
	}
	/**
	 * ticketを設定します。
	 * @param ticket ticket
	 */
	public void setTicket(String ticket) {
	    this.ticket = ticket;
	}
	/**
	 * revisionを取得します。
	 * @return revision
	 */
	public String getRevision() {
	    return revision;
	}
	/**
	 * revisionを設定します。
	 * @param revision revision
	 */
	public void setRevision(String revision) {
	    this.revision = revision;
	}
	/**
	 * server_timeを取得します。
	 * @return server_time
	 */
	public String getServer_time() {
	    return server_time;
	}
	/**
	 * server_timeを設定します。
	 * @param server_time server_time
	 */
	public void setServer_time(String server_time) {
	    this.server_time = server_time;
	}
}
