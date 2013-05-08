package jp.co.nicovideo.eka2513.commentviewerj.eventlistener;

import java.util.EventListener;

import jp.co.nicovideo.eka2513.commentviewerj.event.TimerPluginEvent;

public interface TimerPluginEventListener extends EventListener {
	public void timerTicked(TimerPluginEvent e);
}
