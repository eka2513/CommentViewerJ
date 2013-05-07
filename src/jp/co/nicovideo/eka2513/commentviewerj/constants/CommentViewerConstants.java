package jp.co.nicovideo.eka2513.commentviewerj.constants;

public interface CommentViewerConstants {

	public static final String STATUS = "status";
	public static final String TOKEN = "token";
	public static final String PRESSCAST_TOKEN = "presscast_token";

	public static final String ADDR = "addr";
	public static final String PORT = "port";
	public static final String THREAD = "therad";

	public static final String USER_ID = "user_id";
	public static final String PREMIUM = "is_premium";
	public static final String TIME = "time";
	public static final String START_TIME = "start_time";
	public static final String BASE_TIME = "base_time";
	public static final String COMMENT_THREAD_XML = "<thread thread=\"%s\" version=\"20061206\" res_from=\"-200\"/>\0";

	/////////// for api ///////////
	public static final String GETPLAYERSTATUS   = "http://live.nicovideo.jp/api/getplayerstatus?v=%s";
	public static final String GETPUBLISHSTATUS  = "http://live.nicovideo.jp/api/getpublishstatus?v=%s";
	public static final String GETPREECASTTOKEN  = "http://live.nicovideo.jp/watch/%s";

	public static final String PLUGIN_XML_DIR = "plugins/";
	public static final String CONFIG_DIR = "conf/";
	public static final String HANDLE_NAME_DB = CONFIG_DIR + "kotehan.dat";

}
