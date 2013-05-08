package jp.co.nicovideo.eka2513.commentviewerj.event;

import java.util.EventObject;

/**
 * コメントイベント
 * @author eka2513
 */
public class CommentEvent extends EventObject {

	private static final long serialVersionUID = -6015456548879569723L;

	/** コメントサーバから返されたxml */
	private String xml;

	/**
	 * コンストラクタ
	 * @param source source
	 * @param xml xml
	 */
	public CommentEvent(Object source, String xml) {
		super(source);
		this.xml = xml;
	}

	/**
	 * xmlを取得します。
	 * @return xml
	 */
	public String getXml() {
	    return xml;
	}
}
