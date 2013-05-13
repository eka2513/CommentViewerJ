package jp.co.nicovideo.eka2513.commentviewerj.plugin;

import jp.co.nicovideo.eka2513.commentviewerj.event.PluginCommentEvent;
import jp.co.nicovideo.eka2513.commentviewerj.event.PluginThreadEvent;
import jp.co.nicovideo.eka2513.commentviewerj.event.TimerPluginEvent;
import jp.co.nicovideo.eka2513.commentviewerj.main.CommentViewerBase;
import jp.co.nicovideo.eka2513.commentviewerj.util.PluginSettingUtil;
import jp.nicovideo.eka2513.cookiegetter4j.util.PropertyUtil;

public class AquesTalkPlugin extends CommentViewerPluginBase {

	public static void main(String[] args) {
		new PluginSettingUtil<AquesTalkPlugin>().save(new AquesTalkPlugin());
//		System.out.println(System.getProperty("java.library.path"));
//		AquesTalkPlugin plugin = new AquesTalkPlugin();
//		int result = plugin.speech("あいうえおかきくけこ", 100, null);
//		System.out.println(System.getProperty("java.library.path"));
	}

	static {
		System.loadLibrary("Boyomi");
	}

	private Integer speed = 20;
	private String phont  = "aq_m4b.phont";

	public AquesTalkPlugin(){
	}
	public AquesTalkPlugin(Integer speed, String phont) {
		this.speed = speed;
		this.phont = phont;
	}

	@Override
	public void threadReceived(CommentViewerBase source, PluginThreadEvent e) {
		//do nothing
	}

	@Override
	public void commentReceived(CommentViewerBase source, PluginCommentEvent e) {
		//NGコメと/から始まるコメ以外を読む
		if (!e.getMessage().isNgComment() && !e.getMessage().getText().startsWith("/")) {
			if (PropertyUtil.isMac()) {
				speech("あいうえお", 100, null);
			}
		}
	}

	public native int speech(String comment, int speed, String phont);

	@Override
	public void commentResultReceived(CommentViewerBase source, PluginCommentEvent e) {
		//do nothing
	}

	@Override
	public void tick(CommentViewerBase source, TimerPluginEvent e) {
	}

	@Override
	public void connected(CommentViewerBase source) {
	}

	@Override
	public void disconnected(CommentViewerBase source) {
	}

	/**
	 * speedを取得します。
	 * @return speed
	 */
	public Integer getSpeed() {
	    return speed;
	}

	/**
	 * speedを設定します。
	 * @param speed speed
	 */
	public void setSpeed(Integer speed) {
	    this.speed = speed;
	}

	/**
	 * phontを取得します。
	 * @return phont
	 */
	public String getPhont() {
	    return phont;
	}

	/**
	 * phontを設定します。
	 * @param phont phont
	 */
	public void setPhont(String phont) {
	    this.phont = phont;
	}
}
