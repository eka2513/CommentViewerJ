package jp.co.nicovideo.eka2513.commentviewerj.util;

import info.growl.Growl;
import info.growl.GrowlException;
import info.growl.Notification;
import info.growl.NotificationType;

import java.util.LinkedList;
import java.util.List;

import jp.co.nicovideo.eka2513.commentviewerj.main.swt.constants.GUIConstants;

public class GrowlNotification {

	public static void main(String[] args) {
		try
		{
			List <NotificationType> notificationTypes = new LinkedList <NotificationType> ();
			notificationTypes.add (new NotificationType ("test notification 1",true));
			notificationTypes.add (new NotificationType ("test notification 2",false));

			Growl.registerApplication ("test java application",notificationTypes,"Finder.app");

			Notification notification = new Notification ("test java application","test notification 1","test","これは \"Java\" からの Growl のテストです。");
			notification.setSticky (true);
			Growl.notify (notification);

			notification = new Notification ("test java application","test notification 2","test","これは \"Java\" からの Growl のテストです。ただし、設定がオンになっていないと通知されません。");
			notification.setSticky (true);
			Growl.notify (notification);
		}
		catch (GrowlException e)
		{
			e.printStackTrace ();
		}
	}

	private static GrowlNotification instance = new GrowlNotification();

	private String appName;

	private GrowlNotification() {
		register(GUIConstants.APPLICATION_NAME, "CommentViewerJ.app");
	}

	public static GrowlNotification getInstance() {
		return instance;
	}

	private void register(String appName, String icon) {
		List<NotificationType> notificationTypes = new LinkedList<NotificationType>();
		notificationTypes.add(new NotificationType("default", true));
		notificationTypes.add(new NotificationType("other", false));

		try {
			Growl.registerApplication(appName, notificationTypes, icon);
		} catch (GrowlException e) {
			e.printStackTrace();
		}
	}

	public void notify(String title, String message) {
		Notification notification = new Notification(appName, "default", title, message);
		notification.setSticky(true);
		try {
			Growl.notify(notification);
		} catch (GrowlException e) {
			e.printStackTrace();
		}
	}
}
