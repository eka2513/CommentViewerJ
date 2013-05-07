package jp.co.nicovideo.eka2513.commentviewerj.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import jp.co.nicovideo.eka2513.commentviewerj.constants.CommentViewerConstants;
import jp.nicovideo.eka2513.cookiegetter4j.util.StringUtil;

public class NicoRequestUtil extends RequestUtil {

	public NicoRequestUtil() {
		super();
	}

	public NicoRequestUtil(String cookieString) {
		super();
		setCookieString(cookieString);
	}

	/**
	 * 放送画面からBSP用のtokenを取得します
	 * @param lv
	 * @return
	 */
	public String getPresscastToken(String lv) {
		String html = get(String.format(CommentViewerConstants.GETPREECASTTOKEN, lv));

		return StringUtil.groupMatchFirst(
				"<input id=\"presscast_token\" type=\"hidden\" value=\"([^\"]+)\">", html);
	}

	/**
	 * getplayerstatusを呼んで必要な情報をMAPに詰めて返す
	 *
	 * @param lv
	 * @return
	 */
	public String getPlayerStatus(String lv) {
		String result = get(String.format(CommentViewerConstants.GETPLAYERSTATUS, lv));
		return result;
	}

	/**
	 * postKeyを取得します
	 * @param thread
	 * @param block
	 * @return
	 */
	public String getPostKey(String thread, String block) {
		final String url = "http://live.nicovideo.jp/api/getpostkey?thread=%s&block_no=%s";
		String result = get(String.format(url, thread, block));
		return StringUtil.groupMatchFirst("postkey=(.*)", result);
	}

	/**
	 * getPublishStatusを呼んで結果をmapに詰めて返す
	 *
	 * @param lv
	 * @return
	 */
	public String getPublishStatus(String lv) {
		String result = get(String
				.format(CommentViewerConstants.GETPUBLISHSTATUS, lv));
		return result;
	}

	/**
	 * 運営コメントを送信します
	 * @param lv
	 * @param mail
	 * @param comment
	 * @param handle
	 * @param token
	 * @return
	 */
	public boolean sendBroadcastComment(String lv, String mail, String comment, String handle, String token) {
		try {
			String url = String.format("http://live.nicovideo.jp/api/broadcast/%s?mail=%s&body=%s&name=%s&token=%s",
					lv,
					StringUtil.htmlescape(StringUtil.null2Val(mail).trim()),
					URLEncoder.encode(StringUtil.null2Val(comment).trim(), "UTF-8"),
					URLEncoder.encode(StringUtil.null2Val(handle).trim(), "UTF-8"),
					StringUtil.htmlescape(token)
					);
			String result = get(url);
			return result.split("=")[1].equals("ok");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * BSPコメントを送信します
	 * @param lv
	 * @param comment
	 * @param color
	 * @param handle
	 * @param presscastToken
	 * @return
	 */
	public boolean sendBspComment(String lv, String comment, String color, String handle, String presscastToken) {
		try {
			final String url = "http://live.nicovideo.jp/api/presscast";
			final String postData = String.format("v=%s&body=%s&color=%s&name=%s&mode=xml&token=%s",
				lv,
				URLEncoder.encode(StringUtil.null2Val(comment).trim(), "UTF-8"),
				StringUtil.null2Val(color),
				URLEncoder.encode(StringUtil.null2Val(handle).trim(), "UTF-8"),
				presscastToken
				);
			String result = post(url, postData);
			result = StringUtil.groupMatchFirst("<presscast status=\"([^\"]+)\"\\/>", result);
			if (result == null)
				return false;
			else if (result.equals("ok"))
				return true;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return false;
	}
}
