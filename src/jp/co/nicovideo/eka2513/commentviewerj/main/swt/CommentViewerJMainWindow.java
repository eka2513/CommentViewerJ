package jp.co.nicovideo.eka2513.commentviewerj.main.swt;

import java.util.List;

import jp.co.nicovideo.eka2513.commentviewerj.dto.ChatMessage;
import jp.co.nicovideo.eka2513.commentviewerj.dto.ChatResultMessage;
import jp.co.nicovideo.eka2513.commentviewerj.dto.ThreadMessage;
import jp.co.nicovideo.eka2513.commentviewerj.event.PluginCommentEvent;
import jp.co.nicovideo.eka2513.commentviewerj.event.PluginSendEvent;
import jp.co.nicovideo.eka2513.commentviewerj.event.PluginThreadEvent;
import jp.co.nicovideo.eka2513.commentviewerj.event.TimerPluginEvent;
import jp.co.nicovideo.eka2513.commentviewerj.exception.CommentNotSendException;
import jp.co.nicovideo.eka2513.commentviewerj.main.CommentViewer;
import jp.co.nicovideo.eka2513.commentviewerj.main.eventlistener.GUIEventListener;
import jp.co.nicovideo.eka2513.commentviewerj.main.swt.constants.GUIConstants;
import jp.co.nicovideo.eka2513.commentviewerj.util.GlobalSettingUtil;
import jp.co.nicovideo.eka2513.commentviewerj.util.NicoRequestUtil;
import jp.co.nicovideo.eka2513.commentviewerj.util.NicoStringUtil;
import jp.nicovideo.eka2513.cookiegetter4j.cookie.NicoCookieManagerFactory;
import jp.nicovideo.eka2513.cookiegetter4j.util.PropertyUtil;
import jp.nicovideo.eka2513.cookiegetter4j.util.StringUtil;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class CommentViewerJMainWindow extends Shell implements GUIConstants,
		GUIEventListener {

	private CommentViewer commentViewer;

	private Combo cmbBrowsers;
	private Text txtLv;
	private Table table;
	private Text comment;

	@Override
	protected void checkSubclass() {
	}

	public CommentViewerJMainWindow(Display display) {
		super(display, SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		createContents();
	}

	private void createContents() {
		setSize(550, 420);
		setLayout(new GridLayout(10, true));

		GridData gd = new GridData();
		gd.horizontalSpan = 1;
		Button btnBroadcasting = new Button(this, SWT.NONE);
		btnBroadcasting.setLayoutData(gd);
		btnBroadcasting.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 放送中URLを取得
				final String cookie = NicoCookieManagerFactory
						.getInstance(cmbBrowsers.getText()).getSessionCookie()
						.toCookieString();
				final String lv = NicoStringUtil
						.getLvFromUrl(new NicoRequestUtil(cookie)
								.getBroadcatingLv());
				commentViewer.setCookie(cookie);
				commentViewer.setLv(lv);
				txtLv.setText(lv);
				if (lv.length() == 0) {
					// TODO error処理
					return;
				}
				commentViewer.setBrowser(cmbBrowsers.getText());
				commentViewer.connect();
			}
		});
		btnBroadcasting.setText("放送中");

		gd = new GridData();
		gd.horizontalSpan = 3;
		cmbBrowsers = new Combo(this, SWT.NONE);
		cmbBrowsers.setLayoutData(gd);
		cmbBrowsers.setItems(PropertyUtil.getSupportBrowsers());
		cmbBrowsers.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent selectionevent) {
				// グローバルセッティングに保存
				commentViewer.setBrowser(((Combo) selectionevent.getSource())
						.getText());
				commentViewer.getGlobalSetting().getGeneralSetting()
						.setBrowser(commentViewer.getBrowser());
				GlobalSettingUtil.save(commentViewer.getGlobalSetting());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent selectionevent) {
			}
		});
		gd = new GridData();
		gd.horizontalSpan = 2;
		Label lblLv = new Label(this, SWT.NONE);
		lblLv.setLayoutData(gd);
		lblLv.setText("lv or URL");

		txtLv = new Text(this, SWT.BORDER);
		gd = new GridData();
		gd.horizontalSpan = 3;
		gd.widthHint = 200;
		txtLv.setLayoutData(gd);

		gd = new GridData();
		gd.horizontalSpan = 1;
		Button btnConnect = new Button(this, SWT.NONE);
		btnConnect.setLayoutData(gd);
		btnConnect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (((Button) e.getSource()).getText().equals("接続")) {
					final String lv = NicoStringUtil.getLvFromUrl(txtLv.getText());
					final String cookie = NicoCookieManagerFactory
							.getInstance(cmbBrowsers.getText())
							.getSessionCookie().toCookieString();
					commentViewer.setBrowser(cmbBrowsers.getText());
					commentViewer.setLv(lv);
					commentViewer.setCookie(cookie);
					commentViewer.connect();
					((Button) e.getSource()).setText("切断");
				} else {
					commentViewer.disconnect();
					((Button) e.getSource()).setText("接続");
				}
			}
		});
		btnConnect.setText("接続");
		commentViewer = new CommentViewer();
		cmbBrowsers.setText(commentViewer.getGlobalSetting()
				.getGeneralSetting().getBrowser());
		commentViewer.addListener(this);

		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 10;
		table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(gd);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 10;
		comment = new Text(this, SWT.BORDER);
		comment.setLayoutData(gd);
		comment.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent keyevent) {
				if (keyevent.character == SWT.CR) {
					PluginSendEvent e = new PluginSendEvent(this, "yellow big", comment.getText().trim(), "");
					try {
						commentViewer.sendComment(e);
						comment.setText("");
					} catch (CommentNotSendException e1) {
						e1.printStackTrace();
					}
				}
			}

			@Override
			public void keyPressed(KeyEvent keyevent) {
			}
		});
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		 // ヘッダを設定image no comment handle 184 premium username userid
	    String[] cols = {"サムネ","NO","コメント","ハンドル","184","タイプ","ユーザ名","ユーザID"};
	    for(int i=0;i<cols.length;i++){
	      TableColumn col = new TableColumn(table, SWT.LEFT);
	      col.setText(cols[i]);
	      col.setWidth(50);
	    }
	}

	private void setTableData(ChatMessage message) {
		getDisplay().asyncExec(new ChatMessageRunnable(message));
	}

	@Override
	public void threadReceived(PluginThreadEvent e) {
		System.out.println("threadReceived");
		ThreadMessage message = e.getMessage();
		// do nothing
	}

	@Override
	public void commentReceived(PluginCommentEvent e) {
		ChatMessage message = e.getMessage();
		System.out.println("commentReceived:" + StringUtil.null2Val(message.getText()));
		setTableData(message);
		// set comment to table
	}

	@Override
	public void commentResultReceived(PluginCommentEvent e) {
		System.out.println("commentResultReceived");
		ChatResultMessage message = e.getResult();
		// error handling
	}

	@Override
	public void disconnectReceived() {
		System.out.println("disconnected");
		if (commentViewer != null)
			commentViewer.disconnect();
	}

	@Override
	public void connectReceived() {
		System.out.println("disconnected");
	}

	@Override
	public void ticked(TimerPluginEvent e) {
//		System.out.println("ticked");
	}

	class ChatMessageRunnable implements Runnable {

		private ChatMessage message;

		ChatMessageRunnable(ChatMessage message) {
			this.message = message;
		}

		@Override
		public void run() {
			TableItem item = new TableItem(table, SWT.NULL);
			String[] data = new String[] {
					"",// TODO サムネ
					StringUtil.null2Val(message.getNo()),
					StringUtil.null2Val(message.getText()).trim(),
					StringUtil.null2Val(message.getHandleName()),
					StringUtil.null2Val(message.getMail()),
					StringUtil.null2Val(message.getPremium()), "",// TODO ユーザ名
					StringUtil.null2Val(message.getUser_id()), };
			item.setText(data);
		}

	}


}