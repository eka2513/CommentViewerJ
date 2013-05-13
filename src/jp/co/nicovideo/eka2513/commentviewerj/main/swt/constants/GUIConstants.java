package jp.co.nicovideo.eka2513.commentviewerj.main.swt.constants;

import jp.co.nicovideo.eka2513.commentviewerj.constants.CommentViewerConstants;

public interface GUIConstants extends CommentViewerConstants {

	public static final String APPLICATION_NAME = "CommentViweerJ";
	public static final String VERSION = "0.0.1";

	public static final String COMUNAME_CACHE = "COMUNAME_CACHE";
	public static final String SETTING = "SETTING";

	public static final String IMAGE_CACHE_FILE = CONFIG_DIR + "thumb.cache";
	public static final String USERNAME_CACHE_FILE = CONFIG_DIR + "username.dat";

	public static final String MATCH_URL =
			  "(https?:\\/\\/[-_.!~*\\'()a-zA-Z0-9;\\/?:\\@&=+\\$,%#]+)";

	public static final String[] POSITIONS = new String[] {
		"default", "ue", "shita",
	};

	public static final String[] LETTER_SIZES = new String[] {
		"default", "small", "big",
	};

	public static final String[] BSP_COLORS = new String[]{
		"white",
		"red",
		"green",
		"blue",
		"cyan",
		"yellow",
		"purple",
		"pink",
		"orange",
		"niconicowhite",
	};
	public static final String[] LISTENER_COLORS = new String[]{
		"default",
		"red",
		"pink",
		"orange",
		"yellow",
		"green",
		"cyan",
		"blue",
		"purple",
		"black",
		"white2",
		"red2",
		"pink2",
		"orange2",
		"yellow2",
		"green2",
		"cyan2",
	};
}
