package jp.co.nicovideo.eka2513.commentviewerj.plugin.sample;

import java.util.Random;

import jp.co.nicovideo.eka2513.commentviewerj.dto.ThreadMessage;
import jp.co.nicovideo.eka2513.commentviewerj.event.PluginCommentEvent;
import jp.co.nicovideo.eka2513.commentviewerj.event.PluginThreadEvent;
import jp.co.nicovideo.eka2513.commentviewerj.event.TimerPluginEvent;
import jp.co.nicovideo.eka2513.commentviewerj.exception.CommentNotSendException;
import jp.co.nicovideo.eka2513.commentviewerj.main.CommentViewerBase;
import jp.co.nicovideo.eka2513.commentviewerj.plugin.PluginBase;
import jp.co.nicovideo.eka2513.commentviewerj.util.PluginSettingUtil;

public class ChukeiRainbowPlugin extends PluginBase {

	private static final long serialVersionUID = 7857644682114297983L;

	public static void main(String[] args) {
		ChukeiRainbowPlugin plugin = new ChukeiRainbowPlugin();
		plugin.setLoopCount(20);
		new PluginSettingUtil<ChukeiRainbowPlugin>().save(plugin);
	}
	private ThreadMessage threadMessage;

	private Integer loopCount = 10;

	public ChukeiRainbowPlugin() {
	}

	@Override
	public void threadReceived(CommentViewerBase source, PluginThreadEvent e) {
		//do nothing
		threadMessage = e.getMessage();
	}

	@Override
	public void commentReceived(CommentViewerBase source, PluginCommentEvent e) {
		//主コメ送信不可なら無視
		if (!e.isBroadcaster())
			return;
		//threadタグを受信していなければ無視
		if (threadMessage == null)
			return;
		//NGコメント分のオブジェクトは無視
		if (e.getMessage().isNgComment())
			return;
		// /(スラッシュ)で始まるコメントは中継しない
		if (e.getMessage().getText().startsWith("/"))
			return;
		Integer lastNo = Integer.valueOf(threadMessage.getLast_res());
		if (!e.getMessage().getPremium().equals("3") && Integer.valueOf(e.getMessage().getNo()) > lastNo) {
			try {
				for (int i=0; i<loopCount; i++) {
					sendUneiComment("184", String.format("/chukei %s23 %s", Integer.toHexString(new Random().nextInt(4096)), e.getMessage().getText()), "");
				}
			} catch (CommentNotSendException ignore) {
				ignore.printStackTrace();
			}
		}
	}

	@Override
	public void commentResultReceived(CommentViewerBase source, PluginCommentEvent e) {
		System.out.println(String.format("=== no%s chatresult is %s", e.getResult().getNo(), e.getResult().getStatus()));
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

	@Override
	public String getName() {
		return "中継プラグイン";
	}
	@Override
	public String getVersion() {
		return "0.0.0";
	}

	/**
	 * loopCountを取得します。
	 * @return loopCount
	 */
	public Integer getLoopCount() {
	    return loopCount;
	}

	/**
	 * loopCountを設定します。
	 * @param loopCount loopCount
	 */
	public void setLoopCount(Integer loopCount) {
	    this.loopCount = loopCount;
	}

}
