package jp.co.nicovideo.eka2513.commentviewerj.plugin;

import jp.co.nicovideo.eka2513.commentviewerj.dto.ThreadMessage;
import jp.co.nicovideo.eka2513.commentviewerj.event.PluginCommentEvent;
import jp.co.nicovideo.eka2513.commentviewerj.event.PluginThreadEvent;
import jp.co.nicovideo.eka2513.commentviewerj.event.TimerPluginEvent;
import jp.nicovideo.eka2513.cookiegetter4j.util.StringUtil;

public class SimplePlugin extends AbstractCommentViewerPlugin {

	@SuppressWarnings("unused")
	private ThreadMessage threadMessage;

	public SimplePlugin(){

	}

	public void foo(String s) {
		System.out.println(s);
	}

	@Override
	public void threadReceived(PluginThreadEvent e) {
		threadMessage = e.getMessage();
		System.err.println(
				String.format("%s\t%s",
						e.getMessage().getThread(),
						e.getMessage().getTicket()
				)
		);
	}

	@Override
	public void commentReceived(PluginCommentEvent e) {
		if (!e.getMessage().isNgComment()) {
			if (e.getMessage().isFirstComment()) {
				System.err.print("初");
			}
			System.err.println(
					String.format("%s\t%s\t%s\thandle=%s\tactive=%d\tp=%s",
							e.getMessage().getNo(),
							e.getMessage().getPrintableVpos(),
							e.getMessage().getText(),
							StringUtil.null2Val(e.getMessage().getHandleName()),
							e.getActive(),
							e.getMessage().getPremium()
					)
			);
		} else {
			System.err.println(
					String.format("%s\t%s\t%s\thandle=%s\tactive=%d",
							e.getMessage().getNo(),
							"N/A",
							"NGコメントです",
							"NG",
							0
					)
			);
		}
		System.err.flush();
/*
		if (threadMessage == null)
			return;
		Integer lastNo = Integer.valueOf(threadMessage.getLast_res());
		if (Integer.valueOf(e.getMessage().getNo()) == lastNo) {
			try {
//				sendComment("184", "184でコメント" + System.currentTimeMillis());
//				sendComment("yellow big", "BIG文字でコメント");
//				sendComment("yellow big 184", "BIG文字でコメント");
//				sendBSPComment("184", "ああああああ", "BSPBSP", "niconicowhite");
//				sendUneiBSPComment("184", "な ん ち ゃ っ て B S P コ メ ン ト", "BSP", "niconicowhite");
//				sendUneiComment("184", "お<br>礼<br>は三行で", "えか");
			} catch (CommentNotSendException ignore) {
				ignore.printStackTrace();
			}
		}
*/
	}

	@Override
	public void commentResultReceived(PluginCommentEvent e) {
		System.out.println(String.format("=== no%s chatresult is %s", e.getResult().getNo(), e.getResult().getStatus()));
	}

	@Override
	public void tick(TimerPluginEvent e) {
		System.out.println(
				String.format(
						"timer ticked, vpos=%s currentTime=%d systemCurrentTime=%d",
						e.getVpos(),
						e.getCurrentTime(),
						System.currentTimeMillis()
				)
		);
		// do nothing
	}

}
