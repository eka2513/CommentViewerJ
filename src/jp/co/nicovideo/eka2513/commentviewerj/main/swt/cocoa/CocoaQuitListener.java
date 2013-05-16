package jp.co.nicovideo.eka2513.commentviewerj.main.swt.cocoa;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class CocoaQuitListener implements Listener {

	public CocoaQuitListener() {
	}

	@Override
	public void handleEvent(Event arg0) {
		System.exit(0);
	}

}
