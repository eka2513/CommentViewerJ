package jp.co.nicovideo.eka2513.commentviewerj.main.swt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;

import jp.co.nicovideo.eka2513.commentviewerj.main.swt.constants.GUIConstants;
import jp.co.nicovideo.eka2513.commentviewerj.util.GlobalSettingUtil;
import jp.co.nicovideo.eka2513.commentviewerj.util.SerializerUtil;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;

public class WindowMainStub {
	public static void main(String[] args) throws FileNotFoundException {
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
			GlobalSettingUtil.save(shell.commentViewer.getGlobalSetting());
			new SerializerUtil<HashMap<String, String>>().save(GUIConstants.HANDLE_NAME_DB, shell.commentViewer.getHandleNameCache());
			new SerializerUtil<HashMap<Integer, String>>().save(GUIConstants.USERNAME_CACHE_FILE, shell.getUserNameCache());
		} catch (Exception e) {
			e.printStackTrace();
			PrintWriter out = new PrintWriter(new File("err.log"));
			StackTraceElement[] stackTraces = e.getStackTrace();
			for (StackTraceElement s : stackTraces)
				out.println(s.toString());
			out.flush();
			out.close();
		}
	}
}
