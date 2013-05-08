package jp.co.nicovideo.eka2513.commentviewerj.eventlistener;

import java.util.EventListener;

import jp.co.nicovideo.eka2513.commentviewerj.event.TimerPluginEvent;

/**
 * タイマスレッドからの本体へ送信時に使用するリスナ
 * @author eka2513
 *
 */
public interface TimerPluginEventListener extends EventListener {

	/**
	 * タイマーが動いた(1秒おきに実行)
	 * @param e TimerPluginEvent
	 */
	public void timerTicked(TimerPluginEvent e);
}
