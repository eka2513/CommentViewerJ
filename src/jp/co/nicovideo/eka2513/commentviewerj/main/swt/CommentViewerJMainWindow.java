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
import jp.co.nicovideo.eka2513.commentviewerj.exception.CommentServerException;
import jp.co.nicovideo.eka2513.commentviewerj.main.CommentViewer;
import jp.co.nicovideo.eka2513.commentviewerj.main.eventlistener.GUIEventListener;
import jp.co.nicovideo.eka2513.commentviewerj.main.swt.cocoa.CocoaUIEnhancer;
import jp.co.nicovideo.eka2513.commentviewerj.main.swt.constants.GUIConstants;
import jp.co.nicovideo.eka2513.commentviewerj.main.swt.widgets.SearchText;
import jp.co.nicovideo.eka2513.commentviewerj.main.thread.UserNameRunnable;
import jp.co.nicovideo.eka2513.commentviewerj.util.GlobalSettingUtil;
import jp.co.nicovideo.eka2513.commentviewerj.util.NicoRequestUtil;
import jp.co.nicovideo.eka2513.commentviewerj.util.NicoStringUtil;
import jp.co.nicovideo.eka2513.commentviewerj.util.SWTUtil;
import jp.co.nicovideo.eka2513.commentviewerj.util.SerializerUtil;
import jp.nicovideo.eka2513.cookiegetter4j.cookie.NicoCookieManagerFactory;
import jp.nicovideo.eka2513.cookiegetter4j.util.PropertyUtil;
import jp.nicovideo.eka2513.cookiegetter4j.util.StringUtil;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class CommentViewerJMainWindow extends Shell implements GUIConstants, GUIEventListener {

	private CommentViewer commentViewer;
	private HashMap<Integer, Image> imageCache;
	private HashMap<Integer, String> userNameCache;
	private HashMap<String, RGB> colorCache;
	private ThreadMessage threadMessage;

	private Combo cmbBrowsers;
	private SearchText txtLv;
	private Button btnConnect;
	private Table table;
	private Text comment;

	private Button radioCommentTypeListener;
	private Button radioCommentTypeBSP;
	private Button radioCommentTypeUnei;

	private Text txtBspName;

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
		initialize();
	}

	/**
	 * オブジェクトをセット後に呼ばれる初期処理
	 */
	private void initialize() {
		//BSP名をユーザ名自動セット
		if (txtBspName.getText().length() == 0) {
			getDisplay().asyncExec(new UserNameRunnable(cmbBrowsers.getText(), txtBspName));
		}
	}

	private void createContents() {

		if (PropertyUtil.isMac()) {
			CocoaUIEnhancer enhancer = new CocoaUIEnhancer(this);
			enhancer.earlyStartup();
		} else {
			Menu menuBar = new Menu(this, SWT.BAR);
			MenuItem pluginMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
			pluginMenuHeader.setText("&設定");
			Menu pluginMenu = new Menu(this, SWT.DROP_DOWN);
			pluginMenuHeader.setMenu(pluginMenu);
			MenuItem pluginMenuItem = new MenuItem(pluginMenu, SWT.PUSH);
			pluginMenuItem.setText("&設定");
			pluginMenuItem.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetSelected(SelectionEvent selectionevent) {
			    	new SettingWindow(getShell()).open();
//			    	//プラグインの再ロード
//			    	commentViewer.setPlugins(PluginUtil.loadPlugins());
				}
				@Override
				public void widgetDefaultSelected(SelectionEvent selectionevent) {
				}
			});
		    setMenuBar(menuBar);
		}

		//widget disposed
		addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent disposeevent) {
				commentViewer.disconnect();
			}
		});

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

				if (commentViewer.isConnected()) {
					commentViewer.disconnect();
					table.removeAll();
				}
				try {
					commentViewer.connect();
				} catch (CommentServerException e1) {
					e1.printStackTrace();
					return;
				}
				table.removeAll();
				txtLv.setEnabled(false);
				btnConnect.setText(CONNECT_BTN_TEXT_OFF);
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

		txtLv = new SearchText(group, "lv or URL") {
			@Override
			public void enterKeyDetected() {
				if (btnConnect.getText().equals(CONNECT_BTN_TEXT_ON)) {
					final String lv = NicoStringUtil.getLvFromUrl(txtLv.getText());
					final String cookie = NicoCookieManagerFactory
							.getInstance(cmbBrowsers.getText())
							.getSessionCookie().toCookieString();
					commentViewer.setBrowser(cmbBrowsers.getText());
					commentViewer.setLv(lv);
					commentViewer.setCookie(cookie);
					try {
						commentViewer.connect();
					} catch (CommentServerException e) {
						e.printStackTrace();
						return;
					}
					table.removeAll();
					txtLv.setEnabled(false);
					btnConnect.setText(CONNECT_BTN_TEXT_OFF);
				} else {
					commentViewer.disconnect();
					txtLv.setEnabled(true);
					btnConnect.setText(CONNECT_BTN_TEXT_ON);
				}
			}
		};
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
				if (((Button) e.getSource()).getText().equals(CONNECT_BTN_TEXT_ON)) {
					final String lv = NicoStringUtil.getLvFromUrl(txtLv.getText());
					final String cookie = NicoCookieManagerFactory
							.getInstance(cmbBrowsers.getText())
							.getSessionCookie().toCookieString();
					commentViewer.setBrowser(cmbBrowsers.getText());
					commentViewer.setLv(lv);
					commentViewer.setCookie(cookie);
					if (commentViewer.isConnected())
						commentViewer.disconnect();

					try {
						commentViewer.connect();
					} catch (CommentServerException e1) {
						e1.printStackTrace();
						return;
					}
					table.removeAll();
					txtLv.setEnabled(false);
					((Button) e.getSource()).setText(CONNECT_BTN_TEXT_OFF);
				} else {
					commentViewer.disconnect();
					txtLv.setEnabled(true);
					((Button) e.getSource()).setText(CONNECT_BTN_TEXT_ON);
				}
			}
		});
		btnConnect.setText(CONNECT_BTN_TEXT_ON);
		commentViewer = new CommentViewer();
		cmbBrowsers.setText(commentViewer.getGlobalSetting()
				.getGeneralSetting().getBrowser());
		commentViewer.addListener(this);

		gd = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		gd.horizontalSpan = 10;
		table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION | SWT.SINGLE);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(gd);

		Group footer = new Group(this, SWT.NONE);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        footer.setLayoutData(gd);
        footer.setLayout(new GridLayout(8, false));

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 8;
		comment = new Text(footer, SWT.BORDER);
		comment.setLayoutData(gd);
		comment.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent keyevent) {
			}

			@Override
			public void keyPressed(KeyEvent keyevent) {
				if (keyevent.character == SWT.CR) {
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
							String name = txtBspName.getText().trim();
							if (name.length() == 0)
								name = "BSP";
							PluginSendEvent e = new PluginSendEvent(this, color, comment.getText().trim(), name, color);
							if (commentViewer.isBroadcaster())
								commentViewer.sendUneiBSPComment(e);
							else
								commentViewer.sendBSPComment(e);
						} else if (radioCommentTypeUnei.getSelection()) {
							String name = txtBspName.getText().trim();
							if (name.length() == 0)
								name = "";
							PluginSendEvent e = new PluginSendEvent(this, color, comment.getText().trim(), name);
							commentViewer.sendUneiComment(e);
						}
						comment.setText("");
					} catch (CommentNotSendException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		gd = new GridData();
//		gd.horizontalSpan = 3;
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
				cmbCommentPosition.setVisible(true);
				cmbCommentSize.setVisible(true);
				txtBspName.setVisible(false);
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
				cmbCommentPosition.setVisible(false);
				cmbCommentSize.setVisible(false);
				txtBspName.setVisible(true);
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
				cmbCommentPosition.setVisible(false);
				cmbCommentSize.setVisible(false);
				txtBspName.setVisible(true);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent selectionevent) {
			}
		});

		gd = new GridData(GridData.FILL_HORIZONTAL);
//		gd.horizontalSpan = 6;
		txtBspName = new Text(footer, SWT.BORDER | SWT.SINGLE);
		txtBspName.setText(StringUtil.null2Val(commentViewer.getGlobalSetting().getGeneralSetting().getBspName()));
		txtBspName.setLayoutData(gd);
		txtBspName.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent modifyevent) {
				commentViewer.getGlobalSetting().getGeneralSetting().setBspName(txtBspName.getText());
			}
		});
		txtBspName.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent focusevent) {
				commentViewer.getGlobalSetting().getGeneralSetting().setBspName(txtBspName.getText());
			}
			@Override
			public void focusGained(FocusEvent focusevent) {
			}
		});

		gd = new GridData();
//		gd.horizontalSpan = 3;
		chk184 = new Button(footer, SWT.CHECK);
		chk184.setText("184");
		chk184.setLayoutData(gd);

		gd = new GridData();
//		gd.horizontalSpan = 4;
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
		gd.horizontalSpan = 8;
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
	    table.addListener(SWT.PaintItem, new Listener() {
			@Override
			public void handleEvent(Event event) {
				TableItem item = (TableItem)event.item;
				//サムネとユーザ名を再セット(lazyload)
				if (StringUtil.inull2Val(item.getText(8)).toString().equals(item.getText(8))) {
					Integer userid = StringUtil.inull2Val(item.getText(8));
					if (imageCache.containsKey(userid)) {
						item.setImage(0, imageCache.get(userid));
					}
				}
				String userid = item.getText(8);
				if (userNameCache.containsKey(userid)) {
					item.setText(7, userNameCache.get(userid));
				}
				if  (colorCache.containsKey(userid)) {
					item.setBackground(new Color(getDisplay(), colorCache.get(userid)));
				}
				if (commentViewer.getHandleNameCache().containsKey(userid)) {
					item.setText(4, commentViewer.getHandleNameCache().get(userid));
				}
				//NGのライン引き
				Integer score = StringUtil.inull2Val(item.getText(9));
				if (score <= -1000) {
					Color color = getDisplay().getSystemColor(SWT.COLOR_CYAN);
					if (score <= -10000)
						color = getDisplay().getSystemColor(SWT.COLOR_RED);
					else if (score <= -4800)
						color = getDisplay().getSystemColor(SWT.COLOR_YELLOW);
					event.gc.setLineStyle(SWT.LINE_DOT);
					event.gc.setLineWidth(4);
					event.gc.setForeground(color);
					event.gc.drawLine(0, item.getBounds().y+item.getBounds().height, item.getBounds(9).x + item.getBounds(9).width, item.getBounds().y+item.getBounds().height);
				}
			}
		});

		// fake tooltip
	    table.setToolTipText("");
		final Listener labelListener = new Listener() {
			public void handleEvent(Event event) {
				Label label = (Label) event.widget;
				Shell shell = label.getShell();
				switch (event.type) {
				case SWT.MouseDown:
					Event e = new Event();
					e.item = (TableItem) label.getData("_TABLEITEM");
					// Assuming table is single select, set the selection as if
					// the mouse down event went through to the table
					table.setSelection(new TableItem[] { (TableItem) e.item });
					table.notifyListeners(SWT.Selection, e);
					// fall through
				case SWT.MouseExit:
					shell.dispose();
					break;
				}
			}
		};
		final Listener tableListener = new Listener() {
			Shell tip = null;
			Label label = null;

			public void handleEvent(Event event) {
				switch (event.type) {
				case SWT.Dispose:
				case SWT.KeyDown:
				case SWT.MouseMove: {
					if (tip == null)
						break;
					tip.dispose();
					tip = null;
					label = null;
					break;
				}
				case SWT.MouseHover: {
					TableItem item = table.getItem(new Point(event.x, event.y));
					if (item != null) {
						if (tip != null && !tip.isDisposed())
							tip.dispose();
						tip = new Shell(table.getDisplay(), SWT.ON_TOP | SWT.TOOL);
						tip.setLayout(new FillLayout());
						label = new Label(tip, SWT.NONE);
						label.setForeground(getDisplay().getSystemColor(SWT.COLOR_INFO_FOREGROUND));
						label.setBackground(getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
						label.setData("_TABLEITEM", item);
						label.setText(item.getText(3));
						label.addListener(SWT.MouseExit, labelListener);
						label.addListener(SWT.MouseDown, labelListener);
						Point size = tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);
						Rectangle rect = item.getBounds(0);
						Point pt = table.toDisplay(rect.x, rect.y);
						tip.setBounds(pt.x, pt.y, size.x, size.y);
						tip.setVisible(true);
					}
				}
				}
			}
		};
	    table.addListener(SWT.Dispose, tableListener);
	    table.addListener(SWT.KeyDown, tableListener);
	    table.addListener(SWT.MouseMove, tableListener);
	    table.addListener(SWT.MouseHover, tableListener);

	    //右クリックメニュー
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

	    MenuItem itemUserColor = new MenuItem(menu,  SWT.PUSH);
	    itemUserColor.setText("ユーザに色を付ける");
	    itemUserColor.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent selectionevent) {
				TableItem[] items = table.getSelection();
				if (items != null && items.length > 0) {
					ColorDialog dialog = new ColorDialog(getShell());
					RGB color = dialog.open();
					if (color == null)
						return;
					TableItem item = items[0];
					String userid = item.getText(8);
					colorCache.put(userid, color);
					new SerializerUtil<HashMap<String, RGB>>().save(USERCOLOR_CACHE_FILE, colorCache);
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent selectionevent) {
			}
		});

	    table.setMenu(menu);
	    imageCache = new HashMap<Integer, Image>();
	    userNameCache = new SerializerUtil<HashMap<Integer, String>>().load(USERNAME_CACHE_FILE);
	    if (userNameCache == null)
	    	userNameCache = new HashMap<Integer, String>();
	    colorCache = new SerializerUtil<HashMap<String, RGB>>().load(USERCOLOR_CACHE_FILE);
	    if (colorCache == null)
	    	colorCache = new HashMap<String, RGB>();
	}

	private void setTableData(ChatMessage message) {
		getDisplay().asyncExec(new ChatMessageRunnable(message));
	}

	@Override
	public void threadReceived(final PluginThreadEvent e) {
		String title = commentViewer.getPlayerstatus().get(LIVE_TITLE);
		threadMessage = e.getMessage();
		getDisplay().asyncExec(new ThreadMessageRunnable(threadMessage, title));
//		// do nothing
	}

	@Override
	public void commentReceived(PluginCommentEvent e) {
		ChatMessage message = e.getMessage();
		setTableData(message);
	}

	@Override
	public void commentResultReceived(PluginCommentEvent e) {
//		System.out.println("commentResultReceived");
		// set comment to table
//		ChatResultMessage message = e.getResult();
		// error handling
	}

	@Override
	public void disconnectReceived() {
//		System.out.println("disconnected");
		getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				txtLv.setEnabled(true);
				btnConnect.setText(CONNECT_BTN_TEXT_ON);
			}
		});
	}

	@Override
	public void connectReceived() {
		//TODO connected
//		System.out.println("connected");
	}

	@Override
	public void ticked(TimerPluginEvent event) {
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
		 		String room = commentViewer.getPlayerstatus().get(ROOM_LABEL);
		 		if (room.startsWith("co"))
		 			room = "アリーナ";
		 		lblTime.setText(String.format("%s %s:%s 経過時間:%s 残り時間:%s 来場者:%s コメント:%s アクティブ:%s",
		 				commentViewer.getPlayerstatus().get(DEFAULT_COMMUNITY),
		 				room,
		 				commentViewer.getPlayerstatus().get(SEET_NO),
		 				pastTime, elapsedTime,
		 				commentViewer.getPlayerstatus().get(WATCH_COUNT),
		 				commentViewer.getPlayerstatus().get(COMMENT_COUNT),
		 				commentViewer.getActive()));
			}
		});
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
			if (commentViewer.isBroadcaster()) {
				radioCommentTypeUnei.setEnabled(true);
				radioCommentTypeBSP.setEnabled(true);
				radioCommentTypeUnei.setSelection(true);
				radioCommentTypeBSP.setSelection(false);
				radioCommentTypeListener.setSelection(false);
				cmbCommentPosition.setVisible(false);
				cmbCommentSize.setVisible(false);
				cmbCommentColor.removeAll();
				cmbCommentColor.setItems(BSP_COLORS);
				cmbCommentColor.select(0);
				txtBspName.setVisible(true);
			} else if (commentViewer.isBSP()) {
				radioCommentTypeUnei.setEnabled(false);
				radioCommentTypeBSP.setEnabled(true);
				radioCommentTypeUnei.setSelection(false);
				radioCommentTypeBSP.setSelection(true);
				radioCommentTypeListener.setSelection(false);
				cmbCommentPosition.setVisible(false);
				cmbCommentSize.setVisible(false);
				cmbCommentColor.removeAll();
				cmbCommentColor.setItems(BSP_COLORS);
				cmbCommentColor.select(0);
				txtBspName.setVisible(true);
			} else {
				radioCommentTypeUnei.setEnabled(false);
				radioCommentTypeBSP.setEnabled(false);
				radioCommentTypeUnei.setSelection(false);
				radioCommentTypeBSP.setSelection(false);
				radioCommentTypeListener.setSelection(true);
				cmbCommentPosition.setVisible(true);
				cmbCommentSize.setVisible(true);
				cmbCommentColor.removeAll();
				cmbCommentColor.setItems(LISTENER_COLORS);
				cmbCommentColor.select(0);
				txtBspName.setVisible(false);
			}
		}
	}

	class ChatMessageRunnable implements Runnable {

		private ChatMessage message;

		ChatMessageRunnable(ChatMessage message) {
			this.message = message;
		}

		TableItem item = null;

		@Override
		public void run() {
			item = new TableItem(table, SWT.VIRTUAL | SWT.BORDER);
			String userName = "";
			if (StringUtil.inull2Val(message.getUser_id()).toString().equals(message.getUser_id())) {
				Integer userid = StringUtil.inull2Val(message.getUser_id());
				//ユーザ名
				if (userNameCache.containsKey(userid)) {
					userName = userNameCache.get(userid);
				} else {
					new Thread(new Runnable() {
						@Override
						public void run() {
							Integer userid = StringUtil.inull2Val(message.getUser_id());
							//<title>___さんのユーザーページ - niconico</title>
							String html = StringUtil.null2Val(new NicoRequestUtil(commentViewer.getCookie()).get(String.format("http://www.nicovideo.jp/user/%s", message.getUser_id())));
							String userName = StringUtil.null2Val(StringUtil.groupMatchFirst("<title>(.*?)さんのユーザーページ[^<]+<\\/title>", html));
							userNameCache.put(userid, userName);
							//ファイルキャッシュ
							new SerializerUtil<HashMap<Integer, String>>().save(USERNAME_CACHE_FILE, userNameCache);
						}
					}).start();
				}
				//サムネ
				if (imageCache.containsKey(userid)) {
					item.setImage(0, imageCache.get(userid));
				} else {
					new Thread(new Runnable() {
						@Override
						public void run() {
							Integer userid = StringUtil.inull2Val(message.getUser_id());
							Integer dir = userid / 10000;
							Image im = SWTUtil.getImage(getDisplay(),
									String.format("http://usericon.nimg.jp/usericon/%d/%d.jpg", dir, userid),
										getDisplay(), 30, 30);
							if (im != null) {
//								item.setImage(0, im);
								imageCache.put(userid, im);
							}
						}
					}).start();
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

//			//growl通知
//			if (PropertyUtil.isMac()) {
//				//TODO macでかつ、設定がONだったら
//				String title = message.getUser_id();
//				GrowlNotification.getInstance().notify(title, message.getText());
//			}

//			String[] links = StringUtil.groupMatch(MATCH_URL, StringUtil.null2Val(message.getText()).trim());
//			//TODO URL自動リンク
//			if (links.length > 0) {
//				Hyperlink link = new Hyperlink(table, SWT.WRAP);
//				link.setText(StringUtil.null2Val(message.getText()).trim().replaceAll(MATCH_URL, "$1"));
//				link.setUnderlined(true);
//				TableEditor editor = new TableEditor(table);
//				editor.grabHorizontal = true;
//				editor.setEditor(link, item, 3);
//			}

//			//最終コメが見えていて、初期ロードが終わってれば自動スクロール
//			boolean scrollFlag = (table.getItem(table.getItemCount()-1).getBounds().y == table.getClientArea().y + table.getClientArea().height);
			item.setText(data);
			table.setTopIndex(table.getItemCount()-1);
//			if ((StringUtil.inull2Val(threadMessage.getLast_res()) == StringUtil.inull2Val(message.getNo()))) {
//				//初期ロード後１回はスクロール
//				table.setTopIndex(table.getItemCount()-1);
//			} else if (scrollFlag && (StringUtil.inull2Val(message.getNo()) == 0 ||
//				//初期ロード後であればアイテムが見えてる時だけスクロール
//			}
		}
	}

	/**
	 * commentViewerを取得します。
	 * @return commentViewer
	 */
	public CommentViewer getCommentViewer() {
	    return commentViewer;
	}

	/**
	 * userNameCacheを取得します。
	 * @return userNameCache
	 */
	public HashMap<Integer,String> getUserNameCache() {
	    return userNameCache;
	}

}