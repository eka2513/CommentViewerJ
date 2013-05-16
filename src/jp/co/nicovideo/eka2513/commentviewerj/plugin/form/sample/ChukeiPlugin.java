package jp.co.nicovideo.eka2513.commentviewerj.plugin.form.sample;

import jp.co.nicovideo.eka2513.commentviewerj.event.PluginCommentEvent;
import jp.co.nicovideo.eka2513.commentviewerj.event.PluginThreadEvent;
import jp.co.nicovideo.eka2513.commentviewerj.event.TimerPluginEvent;
import jp.co.nicovideo.eka2513.commentviewerj.main.CommentViewerBase;
import jp.co.nicovideo.eka2513.commentviewerj.plugin.form.FormPluginBase;
import jp.co.nicovideo.eka2513.commentviewerj.util.PluginSettingUtil;
import jp.co.nicovideo.eka2513.commentviewerj.util.SerializerUtil;

public class ChukeiPlugin extends FormPluginBase<ChukeiPluginForm, ChukeiPluginSetting> {

	private static final long serialVersionUID = 2625243439269616176L;

	public static void main(String[] args) {
		ChukeiPlugin plugin = new ChukeiPlugin(ChukeiPluginForm.class, ChukeiPluginSetting.class);
		new PluginSettingUtil<ChukeiPlugin>().save(plugin);
		ChukeiPluginSetting setting = plugin.getSetting();
		new SerializerUtil<ChukeiPluginSetting>().save(plugin.getSettingFilename(), setting);
	}

	public ChukeiPlugin(Class<ChukeiPluginForm> clazzF, Class<ChukeiPluginSetting> clazzS) {
		super(clazzF, clazzS);
	}

	@Override
	public void threadReceived(CommentViewerBase source, PluginThreadEvent e) {
	}

	@Override
	public void commentReceived(CommentViewerBase source, PluginCommentEvent e) {
	}

	@Override
	public void commentResultReceived(CommentViewerBase source,
			PluginCommentEvent e) {
	}

	@Override
	public void connected(CommentViewerBase source) {
	}

	@Override
	public void disconnected(CommentViewerBase source) {
	}

	@Override
	public void tick(CommentViewerBase source, TimerPluginEvent e) {
	}

	@Override
	public String getName() {
		return "chukeiプラグイン";
	}

	@Override
	public String getVersion() {
		return "1.0.0";
	}

}
