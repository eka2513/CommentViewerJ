package jp.co.nicovideo.eka2513.commentviewerj.plugin.form.sample;

import java.util.Random;

import jp.co.nicovideo.eka2513.commentviewerj.event.PluginCommentEvent;
import jp.co.nicovideo.eka2513.commentviewerj.event.PluginThreadEvent;
import jp.co.nicovideo.eka2513.commentviewerj.event.TimerPluginEvent;
import jp.co.nicovideo.eka2513.commentviewerj.exception.CommentNotSendException;
import jp.co.nicovideo.eka2513.commentviewerj.main.CommentViewerBase;
import jp.co.nicovideo.eka2513.commentviewerj.plugin.form.FormPluginBase;
import jp.co.nicovideo.eka2513.commentviewerj.util.PluginSettingUtil;
import jp.co.nicovideo.eka2513.commentviewerj.util.SerializerUtil;

import org.eclipse.swt.widgets.Display;

public class ChukeiPlugin extends FormPluginBase<ChukeiPluginForm, ChukeiPluginSetting> {

	private static final long serialVersionUID = 2625243439269616176L;

	public static void main(String[] args) {
		ChukeiPlugin plugin = new ChukeiPlugin(new Display(), ChukeiPluginForm.class, ChukeiPluginSetting.class);
		new PluginSettingUtil<ChukeiPlugin>().save(plugin);
		ChukeiPluginSetting setting = plugin.getSetting();
		new SerializerUtil<ChukeiPluginSetting>().save(plugin.getSettingFilename(), setting);
	}

	/**
	 * コンストラクタ必ず作成する
	 */
	public ChukeiPlugin() {
		super();
	}

	/**
	 * コンストラクタ必ず作成する
	 */
	public ChukeiPlugin(Display display, Class<ChukeiPluginForm> clazzF, Class<ChukeiPluginSetting> clazzS) {
		super(display, clazzF, clazzS);
	}

	@Override
	public void threadReceived(CommentViewerBase source, PluginThreadEvent e) {
	}

	@Override
	public void commentReceived(CommentViewerBase source, PluginCommentEvent e) {
		//threadmessageとchatmessageがeventに入っている

		//主コメ送信不可なら無視
		if (!e.isBroadcaster())
			return;
		//NGコメント分のオブジェクトは無視
		if (e.getMessage().isNgComment())
			return;
		// /(スラッシュ)で始まるコメントは中継しない
		if (e.getMessage().getText().startsWith("/"))
			return;
		Integer lastNo = (e.getThreadMessage() == null) ? 0 : Integer.valueOf(e.getThreadMessage().getLast_res());
		if (!e.getMessage().getPremium().equals("3") && Integer.valueOf(e.getMessage().getNo()) > lastNo) {
			try {
				for (int i=0; i<setting.getLoop(); i++) {
					sendUneiComment("184", String.format("/chukei %s23 %s", Integer.toHexString(new Random().nextInt(4096)), e.getMessage().getText()), "");
				}
			} catch (CommentNotSendException ignore) {
				ignore.printStackTrace();
			}
		}
	}

	@Override
	public void commentResultReceived(CommentViewerBase source, PluginCommentEvent e) {
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
