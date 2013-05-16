package jp.co.nicovideo.eka2513.commentviewerj.plugin.form.sample;

import jp.co.nicovideo.eka2513.commentviewerj.plugin.form.FormPluginSetting;

public class ChukeiPluginSetting implements FormPluginSetting {

	private static final long serialVersionUID = 6947851401138851060L;

	/** 中継コマンドをループさせる回数 */
	private Integer loop = 20;

	/**
	 * 中継コマンドをループさせる回数を取得します。
	 * @return 中継コマンドをループさせる回数
	 */
	public Integer getLoop() {
	    return loop;
	}

	/**
	 * 中継コマンドをループさせる回数を設定します。
	 * @param loop 中継コマンドをループさせる回数
	 */
	public void setLoop(Integer loop) {
	    this.loop = loop;
	}

}
