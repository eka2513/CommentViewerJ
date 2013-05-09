package jp.co.nicovideo.eka2513.commentviewerj.main;

import java.util.TimerTask;

import jp.co.nicovideo.eka2513.commentviewerj.event.TimerPluginEvent;
import jp.co.nicovideo.eka2513.commentviewerj.eventlistener.TimerPluginEventListener;

public class TimerPluginTask extends TimerTask {

	private TimerPluginEventListener timerPluginEventListener;

	public TimerPluginTask() {
	}

	@Override
	public void run() {
		timerPluginEventListener.timerTicked(new TimerPluginEvent(this, System.currentTimeMillis()/1000));
	}

	/**
	 * timerPluginEventListenerを設定します。
	 * @param timerPluginEventListener timerPluginEventListener
	 */
	public void addTimerPluginEventListener(TimerPluginEventListener timerPluginEventListener) {
	    this.timerPluginEventListener = timerPluginEventListener;
	}
	/**
	 * timerPluginEventListenerを設定します。
	 * @param timerPluginEventListener timerPluginEventListener
	 */
	public void removeTimerPluginEventListener() {
	    this.timerPluginEventListener = null;
	}
}
