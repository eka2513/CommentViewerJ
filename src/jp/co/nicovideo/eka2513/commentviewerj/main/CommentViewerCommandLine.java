package jp.co.nicovideo.eka2513.commentviewerj.main;

import gnu.getopt.Getopt;
import jp.nicovideo.eka2513.cookiegetter4j.cookie.NicoCookieManager;
import jp.nicovideo.eka2513.cookiegetter4j.cookie.NicoCookieManagerFactory;

public class CommentViewerCommandLine extends CommentViewerBase {

	/**
	 * mainメソッド。パラメータの取得と#alert()の呼び出し
	 * @param args パラメータ
	 */
	public static void main(String[] args) {

		CommentViewerCommandLine cv = new CommentViewerCommandLine();
		//-b browser
		//-c cookiestr
		//-l lv
		Getopt options = new Getopt("CommentViewerCommandLine", args, "b:c:l:");
		int c;
		while ((c = options.getopt()) != -1) {
			switch (c) {
			case 'b':
				cv.setBrowser(options.getOptarg());
				NicoCookieManager manager =
						NicoCookieManagerFactory.getInstance(cv.getBrowser());
				if (manager.getSessionCookie() != null)
					cv.setCookie(manager.getSessionCookie().toCookieString());
				break;
			case 'c':
				cv.setCookie(options.getOptarg());
				break;
			case 'l':
				cv.setLv(options.getOptarg());
				break;
			default:
				break;
			}
		}
// めんどいからあとで
//		//uid pwd cookie commentがセットされてなかったらメッセージだして終了
//		if (alert.getCookie() == null || alert.getPasswd() == null ||
//				alert.getUser() == null || alert.getComment() == null) {
//			System.err.println("usage");
//			System.err.println("java -jar nicoalert.jar -u [userid] -p [password] -b [browser] -c [cookiestring] -s [commentstring]");
//			System.err.println("-u [userid]\t\t: ニコ生アラートで使用するユーザID");
//			System.err.println("-p [password]\t\t: ニコ生アラートで使用するユーザのパスワード");
//
//			if (PropertyUtil.isMac()) {
//				//BROWSER_CHROME, BROWSER_FIREFOX, BROWSER_SAFARI, BROWSER_SAFARI_50_UNDER
//				System.err.println("-b [browser]\t\t: コメント送信に使うIDでログインしているブラウザ Chrome Firefox Safari");
//			}
//			else if (PropertyUtil.isWindows()) {
//				//BROWSER_IE, BROWSER_CHROME, BROWSER_FIREFOX, BROWSER_SAFARI, BROWSER_SAFARI_50_UNDER
//				System.err.println("-b [browser]\t\t: コメント送信に使うIDでログインしているブラウザ InternetExplorer, Chrome, Firefox, Safari");
//			}
//			System.err.println("-c [cookiestring]\t: Cookie文字列を指定する場合はこちらから");
//			System.err.println("\t\t\tuser_session=user_session_999999_999999999999999999みたいな感じで指定");
//			System.err.println("-s [commentstring]\t: コメントを指定します。");
//			return;
//		}

		cv.connect();
	}
}
