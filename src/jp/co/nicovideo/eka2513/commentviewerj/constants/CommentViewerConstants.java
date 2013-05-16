package jp.co.nicovideo.eka2513.commentviewerj.constants;
/**
 * 定数クラス.
 * @author eka2513
 *
 */
public interface CommentViewerConstants {

	/** STATUS */
	public static final String STATUS = "status";
	/** TOKEN */
	public static final String TOKEN = "token";
	/** PRESSCAST_TOKEN */
	public static final String PRESSCAST_TOKEN = "presscast_token";

	/** ADDR */
	public static final String ADDR = "addr";
	/** PORT */
	public static final String PORT = "port";
	/** PORT */
	public static final String THREAD = "therad";

	/** USER_ID */
	public static final String USER_ID = "user_id";
	/** PREMIUM */
	public static final String PREMIUM = "is_premium";
	/** TITLE */
	public static final String LIVE_TITLE = "title";
	/** 来場者 */
	public static final String WATCH_COUNT = "watch_count";
	/** コメント数 */
	public static final String COMMENT_COUNT = "comment_count";
	/** コミュニティ */
	public static final String DEFAULT_COMMUNITY = "default_community";
	/** ROOM LABEL */
	public static final String ROOM_LABEL = "room_label";
	/** SEAT_NO */
	public static final String SEET_NO = "room_seetno";
	/** TIME */
	public static final String TIME = "time";
	/** START_TIME */
	public static final String START_TIME = "start_time";
	/** BASE_TIME */
	public static final String BASE_TIME = "base_time";
	/** BASE_TIME */
	public static final String END_TIME = "end_time";
	/** COMMENT_THREAD_XML */
	public static final String COMMENT_THREAD_XML = "<thread thread=\"%s\" scores=\"1\" version=\"20061206\" res_from=\"-200\"/>\0";

	/////////// for api ///////////
	/** getplayerstatusのurl */
	public static final String GETPLAYERSTATUS   = "http://live.nicovideo.jp/api/getplayerstatus?v=%s";
	/** getpublishstatusのurl */
	public static final String GETPUBLISHSTATUS  = "http://live.nicovideo.jp/api/getpublishstatus?v=%s";
	/** presscasttokenのurl */
	public static final String GETPREECASTTOKEN  = "http://live.nicovideo.jp/watch/%s";

	/** プラグイン格納ディレクトリ */
	public static final String PLUGIN_XML_DIR = "plugins/";
	/** プラグイン設定格納ディレクトリ */
	public static final String PLUGIN_DAT_DIR = PLUGIN_XML_DIR + "data/";
	/** コンフィグ格納ディレクトリ */
	public static final String CONFIG_DIR = "conf/";
	/** コテハン設定ファイル */
	public static final String HANDLE_NAME_DB = CONFIG_DIR + "kotehan.dat";
	/** グローバル設定ファイル */
	public static final String GLOBAL_SETTING_DB = CONFIG_DIR + "setting.dat";

}
