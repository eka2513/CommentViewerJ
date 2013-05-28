package jp.co.nicovideo.eka2513.commentviewerj.main.thread;

import jp.co.nicovideo.eka2513.commentviewerj.util.NicoRequestUtil;
import jp.nicovideo.eka2513.cookiegetter4j.cookie.NicoCookieManagerFactory;
import jp.nicovideo.eka2513.cookiegetter4j.util.StringUtil;

import org.eclipse.swt.widgets.Text;

public class UserNameRunnable implements Runnable {

	private String browser;

	private Text field;

	public UserNameRunnable(String browser, Text field) {
		this.browser = browser;
		this.field = field;
	}

	@Override
	public void run() {
		final String cookie = NicoCookieManagerFactory.getInstance(browser)
				.getSessionCookie().toCookieString();
		NicoRequestUtil util = new NicoRequestUtil(cookie);
		String html = util.get("http://www.nicovideo.jp/my");
		String bspName = StringUtil
				.groupMatchFirst(
						"<span id=\"siteHeaderUserNickNameContainer\">(.*?) さん<\\/span>",
						html);
		if (bspName.length() > 0)
			field.setText(bspName);
	}
}
