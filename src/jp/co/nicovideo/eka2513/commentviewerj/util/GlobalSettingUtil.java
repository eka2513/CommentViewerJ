package jp.co.nicovideo.eka2513.commentviewerj.util;

import jp.co.nicovideo.eka2513.commentviewerj.constants.CommentViewerConstants;
import jp.co.nicovideo.eka2513.commentviewerj.main.settings.GlobalSetting;

public class GlobalSettingUtil {

	/**
	 * グローバル設定を保存します
	 * @param setting グローバル設定
	 */
	public static void save(GlobalSetting setting) {
		new SerializerUtil<GlobalSetting>()
			.save(CommentViewerConstants.GLOBAL_SETTING_DB, setting);
	}

	/**
	 * グローバル設定をロードします
	 * @return グローバル設定
	 */
	public static GlobalSetting load() {
		return new SerializerUtil<GlobalSetting>()
				.load(CommentViewerConstants.GLOBAL_SETTING_DB);
	}
}
