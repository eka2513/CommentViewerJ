package jp.co.nicovideo.eka2513.commentviewerj.dto;

import java.io.Serializable;

/**
 * コメント情報クラス.基本的にはchatタグ。
 * NGコメントの場合は、ngComment=trueとNoしかセットされないので注意
 * @author eka2513
 */
public class ChatMessage implements Serializable {

	private static final long serialVersionUID = -5381832803254738959L;

	private String thread;
	private String no;
	private String vpos;
	private String date;
	private String date_usec;
	private String mail;
	private String user_id;
	private String anonymity;
	private String locale;
	private String score;
	private String premium;
	/** 初コメ */
	private boolean firstComment;
	/** NGコメ */
	private boolean ngComment;
	/** コテハン */
	private String handleName;
	/** コメント本文 */
	private String text;

	/**
	 * vposをhh:mi:ss形式で返します
	 * @return vpos
	 */
	public String getPrintableVpos() {
		Integer pos = Integer.valueOf(Integer.valueOf(vpos)/100);
		if (pos > 3600) {
			return String.format("%d:%02d:%02d", Integer.valueOf(pos / 3600), Integer.valueOf((pos % 3600)/60), pos % 60);
		} else {
			return String.format("%02d:%02d", Integer.valueOf((pos % 3600)/60), pos % 60);
		}
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
	 * vposを取得します。
	 * @return vpos
	 */
	public String getVpos() {
	    return vpos;
	}
	/**
	 * vposを設定します。
	 * @param vpos vpos
	 */
	public void setVpos(String vpos) {
	    this.vpos = vpos;
	}
	/**
	 * dateを取得します。
	 * @return date
	 */
	public String getDate() {
	    return date;
	}
	/**
	 * dateを設定します。
	 * @param date date
	 */
	public void setDate(String date) {
	    this.date = date;
	}
	/**
	 * date_usecを取得します。
	 * @return date_usec
	 */
	public String getDate_usec() {
	    return date_usec;
	}
	/**
	 * date_usecを設定します。
	 * @param date_usec date_usec
	 */
	public void setDate_usec(String date_usec) {
	    this.date_usec = date_usec;
	}
	/**
	 * mailを取得します。
	 * @return mail
	 */
	public String getMail() {
	    return mail;
	}
	/**
	 * mailを設定します。
	 * @param mail mail
	 */
	public void setMail(String mail) {
	    this.mail = mail;
	}
	/**
	 * user_idを取得します。
	 * @return user_id
	 */
	public String getUser_id() {
	    return user_id;
	}
	/**
	 * user_idを設定します。
	 * @param user_id user_id
	 */
	public void setUser_id(String user_id) {
	    this.user_id = user_id;
	}
	/**
	 * anonymityを取得します。
	 * @return anonymity
	 */
	public String getAnonymity() {
	    return anonymity;
	}
	/**
	 * anonymityを設定します。
	 * @param anonymity anonymity
	 */
	public void setAnonymity(String anonymity) {
	    this.anonymity = anonymity;
	}
	/**
	 * localeを取得します。
	 * @return locale
	 */
	public String getLocale() {
	    return locale;
	}
	/**
	 * localeを設定します。
	 * @param locale locale
	 */
	public void setLocale(String locale) {
	    this.locale = locale;
	}
	/**
	 * scoreを取得します。
	 * @return score
	 */
	public String getScore() {
	    return score;
	}
	/**
	 * scoreを設定します。
	 * @param score score
	 */
	public void setScore(String score) {
	    this.score = score;
	}
	/**
	 * premiumを取得します。
	 * @return premium
	 */
	public String getPremium() {
	    return premium;
	}
	/**
	 * premiumを設定します。
	 * @param premium premium
	 */
	public void setPremium(String premium) {
	    this.premium = premium;
	}
	/**
	 * 初コメを取得します。
	 * @return 初コメ
	 */
	public boolean isFirstComment() {
	    return firstComment;
	}

	/**
	 * 初コメを設定します。
	 * @param firstComment 初コメ
	 */
	public void setFirstComment(boolean firstComment) {
	    this.firstComment = firstComment;
	}

	/**
	 * NGコメを取得します。
	 * @return NGコメ
	 */
	public boolean isNgComment() {
	    return ngComment;
	}

	/**
	 * NGコメを設定します。
	 * @param ngComment NGコメ
	 */
	public void setNgComment(boolean ngComment) {
	    this.ngComment = ngComment;
	}

	/**
	 * コテハンを取得します。
	 * @return コテハン
	 */
	public String getHandleName() {
	    return handleName;
	}
	/**
	 * コテハンを設定します。
	 * @param handleName コテハン
	 */
	public void setHandleName(String handleName) {
	    this.handleName = handleName;
	}
	/**
	 * コメント本文を取得します。
	 * @return コメント本文
	 */
	public String getText() {
	    return text;
	}
	/**
	 * コメント本文を設定します。
	 * @param text コメント本文
	 */
	public void setText(String text) {
	    this.text = text;
	}
}
