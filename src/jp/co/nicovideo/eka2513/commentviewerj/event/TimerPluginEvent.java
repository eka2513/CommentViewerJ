package jp.co.nicovideo.eka2513.commentviewerj.event;

import java.util.EventObject;

/**
 * タイマー駆動用のイベント
 * @author eka2513
 */
public class TimerPluginEvent extends EventObject {

	private static final long serialVersionUID = -7055437277249840356L;

	/** イベント発生時刻 */
	private Long currentTime;

	/** vpos */
	private Integer vpos;

	/**
	 * コンストラクタ
	 * @param source source
	 * @param currentTime currentTime
	 */
	public TimerPluginEvent(Object source, Long currentTime) {
		super(source);
		this.currentTime = currentTime;
	}

	/**
	 * コンストラクタ
	 * @param source source
	 * @param vpos vpos
	 */
	public TimerPluginEvent(Object source, Integer vpos) {
		super(source);
		this.vpos = vpos;
	}

	/**
	 * コンストラクタ
	 * @param source source
	 * @param vpos vpos
	 * @param currentTime currentTime
 	 */
	public TimerPluginEvent(Object source, Integer vpos, Long currentTime) {
		super(source);
		this.vpos = vpos;
		this.currentTime = currentTime;
	}

	/**
	 * イベント発生時刻を取得します。
	 * @return イベント発生時刻
	 */
	public Long getCurrentTime() {
	    return currentTime;
	}

	/**
	 * イベント発生時刻を設定します。
	 * @param currentTime イベント発生時刻
	 */
	public void setCurrentTime(Long currentTime) {
	    this.currentTime = currentTime;
	}

	/**
	 * vposを取得します。
	 * @return vpos
	 */
	public Integer getVpos() {
	    return vpos;
	}

	/**
	 * vposを設定します。
	 * @param vpos vpos
	 */
	public void setVpos(Integer vpos) {
	    this.vpos = vpos;
	}
}