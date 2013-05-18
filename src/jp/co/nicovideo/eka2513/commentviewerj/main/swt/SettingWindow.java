package jp.co.nicovideo.eka2513.commentviewerj.main.swt;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.co.nicovideo.eka2513.commentviewerj.plugin.PluginBase;
import jp.co.nicovideo.eka2513.commentviewerj.plugin.form.FormPluginBase;
import jp.co.nicovideo.eka2513.commentviewerj.util.PluginUtil;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.reflections.Reflections;

public class SettingWindow extends Shell {

	@Override
	protected void checkSubclass() {
	}

	private Map<String, Class<? extends PluginBase>> pluginCache;
	private Table table;

	public SettingWindow(Display display) {
		super(display, SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
	    setLayout(new GridLayout(10, false));
		createContents();
	}

	private void createContents() {
		setSize(800, 400);
		CTabFolder tabFolder = new CTabFolder(this, SWT.NONE);
		tabFolder.setSimple(false);
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 10;
        tabFolder.setLayoutData(gd);

		// 最大化ボタンを表示
		tabFolder.setMaximizeVisible(false);
		// 最小化ボタンを表示
		tabFolder.setMinimizeVisible(false);
		// タブの高さを設定
		tabFolder.setTabHeight(24);
		// 選択タブの背景色にグラデーションを設定
		tabFolder.setSelectionBackground(
		  new Color[]{
		    getDisplay().getSystemColor(SWT.COLOR_TITLE_BACKGROUND),
		    getDisplay().getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT)},
		  new int[] {90},
		  true
		);
		// タブを作成
		final String[] tabIcons = new String[] {"images/setting.png", "images/plugin.png", "images/hn.png"};
		final String[] tabNames = new String[] {"全般設定", "プラグイン", "コテハン"};
		CTabItem tabItems[] = new CTabItem[tabNames.length];
		for (int i=0; i<tabNames.length; i++) {
			tabItems[i] = new CTabItem(tabFolder,SWT.NONE);
			tabItems[i].setText(tabNames[i]);
			tabItems[i].setImage(new Image(getDisplay(), tabIcons[i]));
		}
		//全般設定タブ設定
		createWholeTab(tabFolder, tabItems[0]);
		//プラグインタブ設定
		createPluginsTab(tabFolder, tabItems[1]);
		//コテハンタブ設定
		createHandleNameTab(tabFolder, tabItems[2]);
	}

	private void createHandleNameTab(CTabFolder tabFolder, CTabItem cTabItem) {
	}

	private void createWholeTab(CTabFolder tabFolder, CTabItem cTabItem) {
	}

	private void createPluginsTab(CTabFolder tabFolder, CTabItem cTabItem) {
		//クラスを検索
		List<Class<? extends PluginBase>> plugins = getPlugins();
		List<Class<? extends PluginBase>> availablePlugins = getAvailablePlugins();

		table = new Table(tabFolder, SWT.CHECK | SWT.BORDER | SWT.FULL_SELECTION | SWT.SINGLE);
		table.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				List<Class<? extends PluginBase>> list = new ArrayList<Class<? extends PluginBase>>();
				for (TableItem item : table.getItems()) {
					if (item.getChecked()) {
						list.add(pluginCache.get(item.getText(0)));
					}
				}
				PluginUtil.write(list);
			}
		});
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
	    String[] cols = {"プラグイン名", "設定"};
	    Integer[] sizes = new Integer[] {500, 50};
	    for(int i=0;i<cols.length;i++){
	      TableColumn col = new TableColumn(table, SWT.LEFT);
	      col.setText(cols[i]);
	      col.setWidth(sizes[i]);
	    }
	    PluginBase instance = null;
	    String pluginName = null;
	    pluginCache = new HashMap<String, Class<? extends PluginBase>>();
	    for (final Class<? extends PluginBase> plugin : plugins) {
	    	try {
				instance = plugin.newInstance();
				pluginName = String.format("%s ver.%s", instance.getName(), instance.getVersion());
				pluginCache.put(pluginName, plugin);
				TableItem item = new TableItem(table, SWT.MULTI | SWT.WRAP | SWT.BORDER);
				String[] data = new String[] {
					pluginName, "",
				};

				if (availablePlugins.contains(plugin)) {
					item.setChecked(true);
				}

				if (!(instance instanceof FormPluginBase)) {
					item.setText(data);
					continue;
				}

	    		TableEditor editor = new TableEditor(table);
	    		Button button = new Button(table, SWT.NONE);
				button.setText("設定");
				button.pack();
				editor.minimumWidth = button.getSize().x;
				editor.horizontalAlignment = SWT.CENTER;
				editor.setEditor(button, item, 1);
				item.setText(data);
				button.addSelectionListener(new SelectionListener() {
					@SuppressWarnings("rawtypes")
					@Override
					public void widgetSelected(SelectionEvent selectionevent) {
						//プラグイン設定画面の呼び出し
						try {
							Class<?> settingClass = Class.forName(plugin.getName() + "Setting");
							Class<?> formClass = Class.forName(plugin.getName() + "Form");
							Class<?> pluginClassBase = Class.forName(plugin.getName());
							Class<? extends FormPluginBase> pluginClass = pluginClassBase.asSubclass(FormPluginBase.class);
							Class<?>[] types = new Class<?>[]{Display.class, Class.class, Class.class};
							Constructor<? extends FormPluginBase> constructor = pluginClass.getConstructor(types);
							Object[] args = new Object[]{getDisplay(), formClass, settingClass};
							FormPluginBase formPlugin = constructor.newInstance(args);
							formPlugin.formOpen();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					@Override
					public void widgetDefaultSelected(SelectionEvent selectionevent) {
					}
				});
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
	    }
		cTabItem.setControl(table);
	}

	/**
	 *
	 */
	private List<Class<? extends PluginBase>> getAvailablePlugins() {
		return PluginUtil.loadPluginClasses();
	}
	/**
	 * プラグイン一覧
	 * @return
	 */
	private List<Class<? extends PluginBase>> getPlugins() {
		List<Class<? extends PluginBase>> list = new ArrayList<Class<? extends PluginBase>>();
		Reflections reflections = new Reflections("jp.co.nicovideo.eka2513.commentviewerj.plugin");
		Set<Class<? extends PluginBase>> plugins = reflections.getSubTypesOf(PluginBase.class);
		for (Class<? extends PluginBase> plugin : plugins) {
			if (!Modifier.isAbstract(plugin.getModifiers()) && !Modifier.isInterface(plugin.getModifiers())) {
				list.add(plugin);
			}
		}
		//sort
		Collections.sort(list, new Comparator<Class<? extends PluginBase>>() {
			@Override
			public int compare(Class<? extends PluginBase> c1, Class<? extends PluginBase> c2) {
				try {
					PluginBase i1 = c1.newInstance();
					PluginBase i2 = c2.newInstance();
					return (i1.getName() + i1.getVersion()).compareTo(i2.getName() + i2.getVersion());
				} catch (Exception e) {
					e.printStackTrace();
				}
				return 0;
			}
		});
		return list;
	}
}
