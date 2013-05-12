package jp.co.nicovideo.eka2513.commentviewerj.main.settings;

import java.io.Serializable;

public class GlobalSetting implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 一般設定 */
	private GeneralSetting generalSetting = new GeneralSetting();;

	/** コテハン関連設定 */
	private HandleNameSetting handleNameSetting = new HandleNameSetting();

	/**
	 * 一般設定を取得します。
	 * @return 一般設定
	 */
	public GeneralSetting getGeneralSetting() {
	    return generalSetting;
	}

	/**
	 * 一般設定を設定します。
	 * @param generalSetting 一般設定
	 */
	public void setGeneralSetting(GeneralSetting generalSetting) {
	    this.generalSetting = generalSetting;
	}

	/**
	 * コテハン関連設定を取得します。
	 * @return コテハン関連設定
	 */
	public HandleNameSetting getHandleNameSetting() {
	    return handleNameSetting;
	}

	/**
	 * コテハン関連設定を設定します。
	 * @param handleNameSetting コテハン関連設定
	 */
	public void setHandleNameSetting(HandleNameSetting handleNameSetting) {
	    this.handleNameSetting = handleNameSetting;
	}

}
