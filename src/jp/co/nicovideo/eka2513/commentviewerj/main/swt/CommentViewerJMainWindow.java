package jp.co.nicovideo.eka2513.commentviewerj.main.swt;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import jp.co.nicovideo.eka2513.commentviewerj.constants.PremiumConstants;
import jp.co.nicovideo.eka2513.commentviewerj.dto.ChatMessage;
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
import jp.co.nicovideo.eka2513.commentviewerj.util.SWTUtil;
import jp.co.nicovideo.eka2513.commentviewerj.util.SerializerUtil;
import jp.co.nicovideo.eka2513.commentviewerj.util.XMLUtil;
import jp.nicovideo.eka2513.cookiegetter4j.cookie.NicoCookieManagerFactory;
import jp.nicovideo.eka2513.cookiegetter4j.util.PropertyUtil;
import jp.nicovideo.eka2513.cookiegetter4j.util.StringUtil;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class CommentViewerJMainWindow extends Shell implements GUIConstants, GUIEventListener {

	CommentViewer commentViewer;
	private HashMap<Integer, Image> imageCache;
	private HashMap<Integer, String> userNameCache;
	private Combo cmbBrowsers;
	private Text txtLv;
	private Button btnConnect;
	private Table table;
	private Text comment;

	private Button radioCommentTypeListener;
	private Button radioCommentTypeBSP;
	private Button radioCommentTypeUnei;

	private Button chk184;

	private Combo cmbCommentColor;
	private Combo cmbCommentPosition;
	private Combo cmbCommentSize;

	private Label lblTime;

	@Override
	protected void checkSubclass() {
	}

	public CommentViewerJMainWindow(Display display) {
		super(display, SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		createContents();
	}

	private void createContents() {
		setSize(800, 420);

		Group group = new Group(this, SWT.NONE | SWT.SINGLE);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        group.setLayoutData(gd);
        group.setLayout(new GridLayout(10, false));


		gd = new GridData();
		gd.horizontalSpan = 1;
		Button btnBroadcasting = new Button(group, SWT.NONE);
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
				btnConnect.setText("切断");
			}
		});
		btnBroadcasting.setText("放送中");

		gd = new GridData();
		gd.horizontalSpan = 3;
		cmbBrowsers = new Combo(group, SWT.READ_ONLY);
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
		Label lblLv = new Label(group, SWT.NONE);
		lblLv.setLayoutData(gd);
		lblLv.setText("lv or URL");

		txtLv = new Text(group, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		gd.widthHint = 200;
		txtLv.setLayoutData(gd);

		gd = new GridData();
		gd.horizontalSpan = 1;
		btnConnect = new Button(group, SWT.NONE);
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
					table.removeAll();
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

		gd = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		gd.horizontalSpan = 10;
		table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(gd);

		Group footer = new Group(this, SWT.NONE);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        footer.setLayoutData(gd);
        footer.setLayout(new GridLayout(20, false));

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 20;
		comment = new Text(footer, SWT.BORDER);
		comment.setLayoutData(gd);
		comment.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent keyevent) {
				if ((keyevent.stateMask & SWT.SHIFT) != 0 && keyevent.character == SWT.CR) {
					String iyayo    = (chk184.getSelection() ? "184" : "");
					String color    = cmbCommentColor.getText().replaceAll("default", "");
					String size     = cmbCommentSize.getText().replaceAll("default", "");
					String position = cmbCommentPosition.getText().replaceAll("default", "");
					try {
						if (radioCommentTypeListener.getSelection()) {
							String mail = String.format("%s %s %s %s", iyayo, color, position, size).trim();
							PluginSendEvent e = new PluginSendEvent(this, mail, comment.getText().trim(), "BSP");
							commentViewer.sendComment(e);
						} else if (radioCommentTypeBSP.getSelection()) {
							PluginSendEvent e = new PluginSendEvent(this, color, comment.getText().trim(), "BSP", color);
							if (commentViewer.isBroadcaster())
								commentViewer.sendUneiBSPComment(e);
							else
								commentViewer.sendBSPComment(e);
						} else if (radioCommentTypeUnei.getSelection()) {
							PluginSendEvent e = new PluginSendEvent(this, color, comment.getText().trim(), "");
							commentViewer.sendUneiComment(e);
						}
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

		gd = new GridData();
		gd.horizontalSpan = 2;
		radioCommentTypeListener = new Button(footer, SWT.RADIO);
		radioCommentTypeListener.setText("通常");
		radioCommentTypeListener.setSelection(true);
		radioCommentTypeListener.setLayoutData(gd);
		radioCommentTypeListener.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent selectionevent) {
				cmbCommentColor.removeAll();
				cmbCommentColor.setItems(LISTENER_COLORS);
				cmbCommentColor.select(0);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent selectionevent) {
			}
		});

		radioCommentTypeBSP = new Button(footer, SWT.RADIO);
		radioCommentTypeBSP.setText("BSP");
		radioCommentTypeBSP.setLayoutData(gd);
		radioCommentTypeBSP.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent selectionevent) {
				cmbCommentColor.removeAll();
				cmbCommentColor.setItems(BSP_COLORS);
				cmbCommentColor.select(0);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent selectionevent) {
			}
		});

		radioCommentTypeUnei = new Button(footer, SWT.RADIO);
		radioCommentTypeUnei.setText("運営");
		radioCommentTypeUnei.setLayoutData(gd);
		radioCommentTypeUnei.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent selectionevent) {
				cmbCommentColor.removeAll();
				cmbCommentColor.setItems(BSP_COLORS);
				cmbCommentColor.select(0);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent selectionevent) {
			}
		});

		chk184 = new Button(footer, SWT.CHECK);
		chk184.setText("184");
//		chk184.setLayoutData(gd);

		gd = new GridData();
		gd.horizontalSpan = 4;
		cmbCommentColor = new Combo(footer, SWT.READ_ONLY);
		cmbCommentColor.setItems(LISTENER_COLORS);
		cmbCommentColor.select(0);
		cmbCommentColor.setLayoutData(gd);

		cmbCommentPosition = new Combo(footer, SWT.READ_ONLY);
		cmbCommentPosition.setItems(POSITIONS);
		cmbCommentPosition.select(0);
		cmbCommentPosition.setLayoutData(gd);

		cmbCommentSize = new Combo(footer, SWT.READ_ONLY);
		cmbCommentSize.setItems(LETTER_SIZES);
		cmbCommentSize.select(0);
		cmbCommentSize.setLayoutData(gd);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 20;
		lblTime = new Label(footer, SWT.NONE);
		lblTime.setText("0:00:00 0:00:00");
		lblTime.setLayoutData(gd);

		/////////// テーブルの初期処理 ///////////
		 // ヘッダを設定image no comment handle 184 premium username userid
	    String[] cols = {"サムネ","NO","時間","コメント","ハンドル","184","タイプ","ユーザ名","ユーザID","スコア"};
	    Integer[] sizes = new Integer[] {40, 40, 60, 350, 80, 30, 40, 80, 80, 40};
	    for(int i=0;i<cols.length;i++){
	      TableColumn col = new TableColumn(table, SWT.LEFT);
	      col.setText(cols[i]);
	      col.setWidth(sizes[i]);
	    }
	    table.addMenuDetectListener(new MenuDetectListener() {
			@Override
			public void menuDetected(MenuDetectEvent event) {
				if (event.getSource().equals(table)) {
					event.doit = true;
				} else {
					event.doit = false;
				}
			}
		});

	    Menu menu = new Menu(this, SWT.POP_UP);
	    MenuItem item = new MenuItem(menu, SWT.PUSH);
	    item.setText("コメントをコピー");
	    item.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				TableItem[] items = table.getSelection();
				if (items != null && items.length > 0) {
					TableItem item = items[0];
					Clipboard clipboard = new Clipboard(getDisplay());
					clipboard.setContents(new Object[] { item.getText(3) },
							new Transfer[] { TextTransfer.getInstance() });
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent selectionevent) {
			}
		});
	    MenuItem itemUrlOpen = new MenuItem(menu, SWT.PUSH);
	    itemUrlOpen.setText("URLを開く");
	    itemUrlOpen.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				TableItem[] items = table.getSelection();
				if (items != null && items.length > 0) {
					TableItem item = items[0];
					String comment = item.getText(3);
					String urlString = StringUtil.groupMatchFirst(MATCH_URL,
							comment);
					if (urlString == null)
						return;
					try {
						Desktop.getDesktop().browse(new URI(urlString));
					} catch (IOException e) {
						e.printStackTrace();
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent selectionevent) {
			}
		});
	    MenuItem itemUserPageOpen = new MenuItem(menu, SWT.PUSH);
	    itemUserPageOpen.setText("ユーザページを開く");
	    itemUserPageOpen.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent selectionevent) {
				TableItem[] items = table.getSelection();
				if (items != null && items.length > 0) {
					TableItem item = items[0];
					String userid = item.getText(8);
					if (!StringUtil.inull2Val(userid).toString().equals(userid))
						return;
					String urlString = String.format("http://www.nicovideo.jp/user/%s", userid);
					try {
						Desktop.getDesktop().browse(new URI(urlString));
					} catch (IOException e) {
						e.printStackTrace();
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent selectionevent) {
			}
		});
	    table.setMenu(menu);
	    imageCache = new HashMap<Integer, Image>();
	    userNameCache = new HashMap<Integer, String>();
	}

	private void setTableData(ChatMessage message) {
		getDisplay().asyncExec(new ChatMessageRunnable(message));
	}

	@Override
	public void threadReceived(PluginThreadEvent e) {
		System.out.println("threadReceived");
		String title = commentViewer.getPlayerstatus().get(LIVE_TITLE);
		getDisplay().asyncExec(new ThreadMessageRunnable(e.getMessage(), title));
//		ThreadMessage message = e.getMessage();
//		// do nothing
	}

	@Override
	public void commentReceived(PluginCommentEvent e) {
		System.out.println("commentReceived");
		ChatMessage message = e.getMessage();
		setTableData(message);
	}

	@Override
	public void commentResultReceived(PluginCommentEvent e) {
		System.out.println("commentResultReceived");
		// set comment to table
//		ChatResultMessage message = e.getResult();
		// error handling
	}

	@Override
	public void disconnectReceived() {
		System.out.println("disconnected");
		getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				btnConnect.setText("接続");
			}
		});
	}

	@Override
	public void connectReceived() {
		System.out.println("connected");
	}

	@Override
	public void ticked(TimerPluginEvent e) {
		//30秒毎にplayerstatus更新
		if (e.getVpos() % (100*30) == 0)
			commentViewer.setPlayerstatus(XMLUtil.parsePlayerStatus(new NicoRequestUtil(commentViewer.getCookie()).getPlayerStatus(commentViewer.getLv())));
 		getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				Long endTime = Long.valueOf(commentViewer.getPlayerstatus().get(END_TIME));
				Long startTime = Long.valueOf(commentViewer.getPlayerstatus().get(START_TIME));
				Long now = System.currentTimeMillis() / 1000;
				//経過時間
		 		String pastTime = SWTUtil.vpos2Time(String.valueOf(100*(now - startTime)));
				//残り時間
		 		String elapsedTime = SWTUtil.vpos2Time(String.valueOf(100*(endTime - now)));
		 		lblTime.setText(String.format("経 %s 残 %s 来場者 %s コメント %s アクティブ %s",
		 				pastTime, elapsedTime,
		 				commentViewer.getPlayerstatus().get(WATCH_COUNT),
		 				commentViewer.getPlayerstatus().get(COMMENT_COUNT),
		 				commentViewer.getActive()));
			}
		});
//		System.out.println("ticked");
	}

	class ThreadMessageRunnable implements Runnable {
		private ThreadMessage message;
		private String title;
		ThreadMessageRunnable(ThreadMessage message, String title) {
			this.message = message;
			this.title = title;
		}
		@Override
		public void run() {
			if (message == null)
				return;
			setText(StringUtil.null2Val(title));
//			setText(title);
			if (commentViewer.isBroadcaster()) {
				radioCommentTypeUnei.setEnabled(true);
				radioCommentTypeBSP.setEnabled(true);
				radioCommentTypeUnei.setSelection(true);
				radioCommentTypeBSP.setSelection(false);
				radioCommentTypeListener.setSelection(false);
				cmbCommentColor.removeAll();
			} else if (commentViewer.isBSP()) {
				radioCommentTypeUnei.setEnabled(false);
				radioCommentTypeBSP.setEnabled(true);
				radioCommentTypeUnei.setSelection(false);
				radioCommentTypeBSP.setSelection(true);
				radioCommentTypeListener.setSelection(false);
				cmbCommentColor.removeAll();
				cmbCommentColor.setItems(BSP_COLORS);
			} else {
				radioCommentTypeUnei.setEnabled(false);
				radioCommentTypeBSP.setEnabled(false);
				radioCommentTypeUnei.setSelection(false);
				radioCommentTypeBSP.setSelection(false);
				radioCommentTypeListener.setSelection(true);
				cmbCommentColor.removeAll();
				cmbCommentColor.setItems(LISTENER_COLORS);
			}
		}
	}

	class ChatMessageRunnable implements Runnable {

		private ChatMessage message;

		ChatMessageRunnable(ChatMessage message) {
			this.message = message;
		}

		@Override
		public void run() {
			TableItem item = new TableItem(table, SWT.NULL);
			Image im = null;
			String userName = "";
			//TODO 別スレッドにする
			if (StringUtil.inull2Val(message.getUser_id()).toString().equals(message.getUser_id())) {
				Integer userid = StringUtil.inull2Val(message.getUser_id());
				//ユーザ名
				if (userNameCache.containsKey(userid)) {
					userName = userNameCache.get(userid);
				} else {
					//<title>uyuさんのユーザーページ - niconico</title>
					String html = StringUtil.null2Val(new NicoRequestUtil(commentViewer.getCookie()).get(String.format("http://www.nicovideo.jp/user/%s", message.getUser_id())));
					userName = StringUtil.null2Val(StringUtil.groupMatchFirst("<title>(.*?)さんのユーザーページ[^<]+<\\/title>", html));
					userNameCache.put(userid, userName);
					//ファイルキャッシュ
					new SerializerUtil<HashMap<Integer, String>>().save(USERNAME_CACHE_FILE, userNameCache);
				}
				//サムネ
				if (imageCache.containsKey(userid)) {
					item.setImage(0, imageCache.get(userid));
				} else {
					Integer dir = userid / 10000;
					im = SWTUtil.getImage(getDisplay(),
							String.format("http://usericon.nimg.jp/usericon/%d/%d.jpg", dir, userid),
								getDisplay(), 30, 30);
					if (im != null) {
						item.setImage(0, im);
						imageCache.put(userid, im);
						//TODO ファイルキャッシュは・・・やめとく？
//						new SerializerUtil<HashMap<Integer, Image>>().save(IMAGE_CACHE_FILE, imageCache);
					}
				}
			}
			if (message.isNgComment()) {
				Color color = new Color(getDisplay(), new RGB(128, 128, 128));
				item.setForeground(color);
			} else if (message.getPremium().equals(PremiumConstants.BROADCASTER.toString()) || message.getPremium().equals(PremiumConstants.SYSTEM_UNEI.toString())) {
				Color color = new Color(getDisplay(), new RGB(255, 0, 0));
				item.setForeground(color);
			} else if (message.isFirstComment()) {
				Color color = new Color(getDisplay(), new RGB(0, 255, 0));
				item.setForeground(1, color);
				item.setForeground(2, color);
				item.setForeground(3, color);
			}
			String[] data = new String[] {
					"",//サムネ
					StringUtil.null2Val(message.getNo()),
					StringUtil.null2Val(SWTUtil.vpos2Time(message.getVposFromStartTime())),
					StringUtil.null2Val(message.getText()).trim(),
					StringUtil.null2Val(message.getHandleName()),
					StringUtil.null2Val(message.getMail()),
					StringUtil.null2Val(message.getPremium()),
					userName,
					StringUtil.null2Val(message.getUser_id()),
					StringUtil.null2Val(message.getScore()),
			};
			item.setText(data);
			table.showItem(item);
		}

	}

	/**
	 * userNameCacheを取得します。
	 * @return userNameCache
	 */
	public HashMap<Integer,String> getUserNameCache() {
	    return userNameCache;
	}


}