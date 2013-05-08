package jp.co.nicovideo.eka2513.commentviewerj.util;

public class CommentUtil {
	/**
	 * vposを計算します
	 * @param baseTime playerstatusから取得したbase_time
	 * @param time unixtime(秒)
	 * @return
	 */
	public static String calcVpos(String baseTime, String time) {
		return String.valueOf((Long.valueOf(time) - Long.valueOf(baseTime))*100);
	}

}
