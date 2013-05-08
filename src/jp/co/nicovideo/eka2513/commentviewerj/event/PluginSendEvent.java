package jp.co.nicovideo.eka2513.commentviewerj.event;

import java.util.EventObject;

import jp.nicovideo.eka2513.cookiegetter4j.util.StringUtil;

/**
 * PluginSendEvent
 * @author eka2513
 *
 */
public class PluginSendEvent extends EventObject {

	private static final long serialVersionUID = 4187203001795808093L;

	private String comment;
	private String handle;
	private String mail;
	private String bspColor;

	/**
	 * コンストラクタ
	 * @param source source
	 * @param mail mail
	 * @param comment comment
	 * @param handle handle
	 */
	public PluginSendEvent(Object source, String mail, String comment, String handle) {
		super(source);
		this.bspColor = "niconicowhite";
		this.mail = StringUtil.null2Val(mail);
		this.handle = handle;
		this.comment = comment;
	}

	/**
	 * コンストラクタ
	 * @param source source
	 * @param mail mail
	 * @param comment comment
	 * @param handle handle
	 * @param bspColor bspColor
	 */
	public PluginSendEvent(Object source, String mail, String comment, String handle, String bspColor) {
		super(source);
		this.bspColor = bspColor;
		this.mail = StringUtil.null2Val(mail);
		this.handle = handle;
		this.comment = comment;
	}

	/**
	 * commentを取得します。
	 * @return comment
	 */
	public String getComment() {
	    return comment;
	}

	/**
	 * handleを取得します。
	 * @return handle
	 */
	public String getHandle() {
	    return handle;
	}

	/**
	 * mailを取得します。
	 * @return mail
	 */
	public String getMail() {
	    return mail;
	}

	/**
	 * bspColorを取得します。
	 * @return bspColor
	 */
	public String getBspColor() {
	    return bspColor;
	}
}
