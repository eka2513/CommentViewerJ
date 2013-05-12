package jp.co.nicovideo.eka2513.commentviewerj.main.settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

import jp.co.nicovideo.eka2513.commentviewerj.constants.CommentViewerConstants;
import jp.co.nicovideo.eka2513.commentviewerj.dto.ThreadMessage;
import jp.co.nicovideo.eka2513.commentviewerj.event.CommentEvent;
import jp.co.nicovideo.eka2513.commentviewerj.eventlistener.CommentEventListener;
import jp.co.nicovideo.eka2513.commentviewerj.exception.CommentViewerException;
import jp.co.nicovideo.eka2513.commentviewerj.util.CommentUtil;
import jp.co.nicovideo.eka2513.commentviewerj.util.NicoRequestUtil;
import jp.nicovideo.eka2513.cookiegetter4j.util.StringUtil;

public class CommentThread extends Thread implements CommentViewerConstants {

//	private Map<String, String> params;

	private Socket sock;
	private String threadXml;
	private String addr;
	private String port;
	private String threadId;

	private boolean stop;

	private CommentEventListener commentEventListener;

	private PrintWriter out;

//	private List<AbstractCommentViewerPlugin> plugins;


	/**
	 * コンストラクター
	 * @param addr addr
	 * @param port port
	 * @param threadId threadId
	 */
	public CommentThread(String addr, String port, String threadId) {
		try {
			this.stop = false;
			this.addr = addr;
			this.port = port;
			this.threadId = threadId;
			threadXml = String.format(COMMENT_THREAD_XML, threadId);
		} catch (Exception e) {
			throw new CommentViewerException(e);
		}
	}

	/**
	 * コメントを送信します
	 * @param mail mail
	 * @param comment comment
	 * @param cookie cookie
	 * @param lastCommentNo lastCommentNo
	 * @param playerstatus playerstatus
	 * @param threadMessage threadMessage
	 */
	public void sendComment(String mail, String comment, String cookie, Integer lastCommentNo, Map<String, String> playerstatus, ThreadMessage threadMessage) {
		NicoRequestUtil util = new NicoRequestUtil();
		util.setCookieString(cookie);
		Integer block = lastCommentNo / 100;
		String postKey = util.getPostKey(threadId, block.toString());
		Integer now = Integer.valueOf(String.valueOf(System.currentTimeMillis() / 1000));
		String vpos = calcVpos(playerstatus.get(BASE_TIME), now.toString());
		String chatXml =
				String.format(
						"<chat thread=\"%s\" ticket=\"%s\" vpos=\"%s\" postkey=\"%s\" user_id=\"%s\" premium=\"%s\" mail=\"%s\">%s</chat>\0",
						threadId,
						threadMessage.getTicket(),
						vpos,
						postKey,
						playerstatus.get(USER_ID),
						playerstatus.get(PREMIUM),
						mail,
						StringUtil.htmlescape(comment)
				);
		out.write(chatXml);
        out.flush();
	}


	@Override
	public void run() {
		InputStream in = null;
		BufferedReader br = null;
		try {
			sock = new Socket(addr, StringUtil.inull2Val(port));
			out = new PrintWriter(sock.getOutputStream());
			in = sock.getInputStream();
			br = new BufferedReader(new InputStreamReader(in, "utf-8"));
			out.write(threadXml);
	        out.flush();

	        String xml = null;
			while (true) {
				if (stop)
					break;
				if (in.available() > 0) {
					char[] data = new char[in.available()];
					br.read(data, 0, in.available());
					xml = new String(data);
					String[] tags = xml.split("\0");
					for (String tag : tags) {
					    commentEventListener.comReceived(new CommentEvent(this, tag));
					}
				}
				Thread.sleep(100);
			}
		} catch (IOException e) {
			System.err.println("parameters");
			System.err.println(String.format("addr=%s, port=%s, thread=%s", addr, port, threadId));
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			stop = true;
			try {
				if (out != null)
					out.close();
				if (br != null)
					br.close();
				if (in != null)
					in.close();
				if (sock != null)
					sock.close();
				out = null;
				sock = null;
			} catch (IOException ignore) {
			}
		}
	}

	private String calcVpos(String startTime, String time) {
		return CommentUtil.calcVpos(startTime, time);
	}

	public void exit() {
		this.stop = true;
	}

	public boolean isThreadWorking() {
		return !stop;
	}

	/**
	 * addrを取得します。
	 * @return addr
	 */
	public String getAddr() {
	    return addr;
	}

	/**
	 * addrを設定します。
	 * @param addr addr
	 */
	public void setAddr(String addr) {
	    this.addr = addr;
	}

	/**
	 * portを取得します。
	 * @return port
	 */
	public String getPort() {
	    return port;
	}

	/**
	 * portを設定します。
	 * @param port port
	 */
	public void setPort(String port) {
	    this.port = port;
	}

	/**
	 * threadIdを取得します。
	 * @return threadId
	 */
	public String getThreadId() {
	    return threadId;
	}

	/**
	 * threadIdを設定します。
	 * @param threadId threadId
	 */
	public void setThreadId(String threadId) {
	    this.threadId = threadId;
	}

	/**
	 * commentEventListenerを設定します。
	 * @param listener commentEventListener
	 */
	public void setCommentEventListener(CommentEventListener listener) {
	    this.commentEventListener = listener;
	}

	public void removeCommnetEventListener() {
	    this.commentEventListener = null;
	}

}