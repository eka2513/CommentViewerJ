package jp.co.nicovideo.eka2513.commentviewerj.exception;

public class CommentServerException extends Throwable {

	private static final long serialVersionUID = 3064617267261282764L;

	public CommentServerException() {
		super();
	}

	public CommentServerException(String message) {
		super(message);
	}

	public CommentServerException(Throwable cause) {
		super(cause);
	}

	public CommentServerException(String message, Throwable cause) {
		super(message, cause);
	}

}
