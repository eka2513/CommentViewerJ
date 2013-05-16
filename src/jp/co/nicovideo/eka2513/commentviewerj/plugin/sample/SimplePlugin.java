package jp.co.nicovideo.eka2513.commentviewerj.plugin.sample;

import jp.co.nicovideo.eka2513.commentviewerj.dto.ThreadMessage;
import jp.co.nicovideo.eka2513.commentviewerj.event.PluginCommentEvent;
import jp.co.nicovideo.eka2513.commentviewerj.event.PluginThreadEvent;
import jp.co.nicovideo.eka2513.commentviewerj.event.TimerPluginEvent;
import jp.co.nicovideo.eka2513.commentviewerj.main.CommentViewerBase;
import jp.co.nicovideo.eka2513.commentviewerj.plugin.PluginBase;
import jp.co.nicovideo.eka2513.commentviewerj.util.PluginSettingUtil;
import jp.nicovideo.eka2513.cookiegetter4j.util.StringUtil;

public class SimplePlugin extends PluginBase {

	private static final long serialVersionUID = -3802981718187680804L;

	@SuppressWarnings("unused")
	private ThreadMessage threadMessage;

	public static void main(String[] args) {
		new PluginSettingUtil<SimplePlugin>().save(new SimplePlugin());
	}

	public SimplePlugin(){
	}

	@Override
	public void threadReceived(CommentViewerBase source, PluginThreadEvent e) {
		threadMessage = e.getMessage();
		System.err.println(
				String.format("%s\t%s",
						e.getMessage().getThread(),
						e.getMessage().getTicket()
				)
		);
	}

	@Override
	public void commentReceived(CommentViewerBase source, PluginCommentEvent e) {
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
	}

	@Override
	public void commentResultReceived(CommentViewerBase source, PluginCommentEvent e) {
	}

	@Override
	public void tick(CommentViewerBase source, TimerPluginEvent e) {
		// do nothing
	}

	@Override
	public void connected(CommentViewerBase source) {
	}

	@Override
	public void disconnected(CommentViewerBase source) {
	}
	@Override
	public String getName() {
		return "シンプルプラグイン";
	}
	@Override
	public String getVersion() {
		return "0.0.0";
	}
}
