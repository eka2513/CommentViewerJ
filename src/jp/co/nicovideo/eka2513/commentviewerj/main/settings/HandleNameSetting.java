package jp.co.nicovideo.eka2513.commentviewerj.main.settings;

import java.io.Serializable;

public class HandleNameSetting implements Serializable {

	private static final long serialVersionUID = -5032737605120407061L;

	/** ユーザ情報からコテハン自動取得 */
	private boolean autoNamingFromUserInfo;

	/** コテハン上書き */
	private boolean overwrite;

	/**
	 * ユーザ情報からコテハン自動取得を取得します。
	 * @return ユーザ情報からコテハン自動取得
	 */
	public boolean isAutoNamingFromUserInfo() {
	    return autoNamingFromUserInfo;
	}

	/**
	 * ユーザ情報からコテハン自動取得を設定します。
	 * @param autoNamingFromUserInfo ユーザ情報からコテハン自動取得
	 */
	public void setAutoNamingFromUserInfo(boolean autoNamingFromUserInfo) {
	    this.autoNamingFromUserInfo = autoNamingFromUserInfo;
	}

	/**
	 * コテハン上書きを取得します。
	 * @return コテハン上書き
	 */
	public boolean isOverwrite() {
	    return overwrite;
	}

	/**
	 * コテハン上書きを設定します。
	 * @param overwrite コテハン上書き
	 */
	public void setOverwrite(boolean overwrite) {
	    this.overwrite = overwrite;
	}

}
