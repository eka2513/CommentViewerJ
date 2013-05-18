package jp.co.nicovideo.eka2513.commentviewerj.main.swt.cocoa;

import jp.co.nicovideo.eka2513.commentviewerj.main.swt.SettingWindow;

import org.eclipse.swt.widgets.Display;

public class PreferencesRunnable implements Runnable {

	private Display display;

	public PreferencesRunnable(Display display) {
		this.display = display;
	}

	@Override
	public void run() {
		SettingWindow settingWindow = new SettingWindow(display);
		settingWindow.open();
	}

}
