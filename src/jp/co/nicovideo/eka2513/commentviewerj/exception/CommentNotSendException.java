package jp.co.nicovideo.eka2513.commentviewerj.exception;

public class CommentNotSendException extends Throwable {

	private static final long serialVersionUID = -6780381720199513623L;

	public CommentNotSendException(String message) {
		super(message);
	}

	public CommentNotSendException(Throwable cause) {
		super(cause);
	}

	public CommentNotSendException(String message, Throwable cause) {
		super(message, cause);
	}

}
