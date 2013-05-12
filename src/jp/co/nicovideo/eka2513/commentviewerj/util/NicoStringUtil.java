package jp.co.nicovideo.eka2513.commentviewerj.util;

import jp.nicovideo.eka2513.cookiegetter4j.util.StringUtil;

public class NicoStringUtil {

	public static String getLvFromUrl(String lvUrl) {
		return StringUtil.null2Val(StringUtil.groupMatchFirst("(lv[0-9]+)", lvUrl));
	}

}
