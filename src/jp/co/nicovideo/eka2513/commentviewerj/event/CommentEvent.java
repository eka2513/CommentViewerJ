package jp.co.nicovideo.eka2513.commentviewerj.event;

import java.util.EventObject;

public class CommentEvent extends EventObject {

	private static final long serialVersionUID = -6015456548879569723L;

	private String xml;

	public CommentEvent(Object source, String str) {
		super(source);
		xml = str;
	}

	/**
	 * xmlを取得します。
	 * @return xml
	 */
	public String getXml() {
	    return xml;
	}
}
