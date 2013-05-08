package jp.co.nicovideo.eka2513.commentviewerj.plugin;

import java.util.Random;

import jp.co.nicovideo.eka2513.commentviewerj.dto.ThreadMessage;
import jp.co.nicovideo.eka2513.commentviewerj.event.PluginCommentEvent;
import jp.co.nicovideo.eka2513.commentviewerj.event.PluginThreadEvent;
import jp.co.nicovideo.eka2513.commentviewerj.event.TimerPluginEvent;
import jp.co.nicovideo.eka2513.commentviewerj.exception.CommentNotSendException;

public class ChukeiRainbowPlugin extends AbstractCommentViewerPlugin {

	private ThreadMessage threadMessage;

	private Integer loopCount;

	public ChukeiRainbowPlugin() {
		loopCount = 20;
	}

	@Override
	public void threadReceived(PluginThreadEvent e) {
		//do nothing
		threadMessage = e.getMessage();
	}

	@Override
	public void commentReceived(PluginCommentEvent e) {
		if (threadMessage == null)
			return;
		Integer lastNo = Integer.valueOf(threadMessage.getLast_res());
		if (!e.getMessage().getPremium().equals("3") && Integer.valueOf(e.getMessage().getNo()) > lastNo) {
			try {
//				sendComment("184", "184でコメント" + System.currentTimeMillis());
//				sendComment("yellow big", "BIG文字でコメント");
//				sendComment("yellow big 184", "BIG文字でコメント");
//				sendBSPComment("184", "ああああああ", "BSPBSP", "niconicowhite");
//				sendUneiBSPComment("184", "な ん ち ゃ っ て B S P コ メ ン ト", "BSP", "niconicowhite");
				for (int i=0; i<loopCount; i++) {
					sendUneiComment("184", String.format("/chukei %s23 %s", Integer.toHexString(new Random().nextInt(4096)), e.getMessage().getText()), "");
				}
				sendUneiComment("184", "主は寝てますおもしろいのでコメントしてみてください", "");
				sendUneiComment("184", "/perm https://github.com/eka2513/CommentViewerJ", "");
			} catch (CommentNotSendException ignore) {
				ignore.printStackTrace();
			}
		}
	}

	@Override
	public void commentResultReceived(PluginCommentEvent e) {
		System.out.println(String.format("=== no%s chatresult is %s", e.getResult().getNo(), e.getResult().getStatus()));
	}

	@Override
	public void tick(TimerPluginEvent e) {
		// do nothing
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
