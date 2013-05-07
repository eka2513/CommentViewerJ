package jp.co.nicovideo.eka2513.commentviewerj.exception;

public class CommentViewerException extends RuntimeException {

	private static final long serialVersionUID = -6780381720199513623L;

	public CommentViewerException(String message) {
		super(message);
	}

	public CommentViewerException(Throwable cause) {
		super(cause);
	}

	public CommentViewerException(String message, Throwable cause) {
		super(message, cause);
	}

}
