package jp.co.nicovideo.eka2513.commentviewerj.util;

public class CommentUtil {
	/**
	 * vposを計算します
	 * @param baseTime
	 * @param time
	 * @return
	 */
	public static String calcVpos(String baseTime, String time) {
		return String.valueOf((Long.valueOf(time) - Long.valueOf(baseTime))*100);
	}

}
