package jp.co.nicovideo.eka2513.commentviewerj.main.swt;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;

public class WindowMainStub {
	public static void main(String[] args) {
		try {
			Display display = Display.getDefault();
			CommentViewerJMainWindow shell = new CommentViewerJMainWindow(display);
		    shell.setLayout(new GridLayout(2,false));
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
