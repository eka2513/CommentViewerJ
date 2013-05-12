package jp.co.nicovideo.eka2513.commentviewerj.main.settings;

import java.io.Serializable;

import jp.nicovideo.eka2513.cookiegetter4j.constants.NicoCookieConstants;

public class GeneralSetting implements Serializable {

	private static final long serialVersionUID = 7982420983032704562L;

	/** ブラウザ */
	private String browser = NicoCookieConstants.BROWSER_CHROME;

	/** 放送終了通知を行うか */
	private boolean broadcastCloseNotification;

	/** 放送終了通知を何分前に行うか */
	private Integer broadcastCloseNotificationMin = 3;

	/**
	 * ブラウザを取得します。
	 * @return ブラウザ
	 */
	public String getBrowser() {
	    return browser;
	}

	/**
	 * ブラウザを設定します。
	 * @param browser ブラウザ
	 */
	public void setBrowser(String browser) {
	    this.browser = browser;
	}

	/**
	 * 放送終了通知を行うかを取得します。
	 * @return 放送終了通知を行うか
	 */
	public boolean isBroadcastCloseNotification() {
	    return broadcastCloseNotification;
	}

	/**
	 * 放送終了通知を行うかを設定します。
	 * @param broadcastCloseNotification 放送終了通知を行うか
	 */
	public void setBroadcastCloseNotification(boolean broadcastCloseNotification) {
	    this.broadcastCloseNotification = broadcastCloseNotification;
	}

	/**
	 * 放送終了通知を何分前に行うかを取得します。
	 * @return 放送終了通知を何分前に行うか
	 */
	public Integer getBroadcastCloseNotificationMin() {
	    return broadcastCloseNotificationMin;
	}

	/**
	 * 放送終了通知を何分前に行うかを設定します。
	 * @param broadcastCloseNotificationMin 放送終了通知を何分前に行うか
	 */
	public void setBroadcastCloseNotificationMin(Integer broadcastCloseNotificationMin) {
	    this.broadcastCloseNotificationMin = broadcastCloseNotificationMin;
	}
}
