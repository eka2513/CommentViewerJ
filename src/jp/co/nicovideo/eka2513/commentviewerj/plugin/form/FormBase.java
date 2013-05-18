package jp.co.nicovideo.eka2513.commentviewerj.plugin.form;

import jp.co.nicovideo.eka2513.commentviewerj.util.SerializerUtil;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * <pre>
 * フォーム使用のプラグイン用のフォーム基底クラス。
 * フォームを閉じるときに設定をセーブするので、ウィジェットのイベントでsettingを更新しておくこと。
 * いわゆる OK Cancelボタンがある画面は個人的に嫌いです
 * </pre>
 * @author eka2513
 */
public abstract class FormBase<S extends FormPluginSetting> extends Shell {

	/** 設定 */
	protected S setting;

	/** 保存ファイル名 */
	private String filename;

	/**
	 * プライベートコンストラクタ
	 */
	@SuppressWarnings("unused")
	private FormBase() {
	}

	/**
	 * コンストラクタ
	 * FormPluginBaseから呼ばれます。
	 * @param setting 設定
	 * @param filename ファイル名
	 */
	public FormBase(Display display, S setting, String filename) {
		super(display, SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		this.setting = setting;
		this.filename = filename;
		addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent event) {
				save();
			}
		});
		createContents();
	}

	/**
	 * ウィジェットを配置します。
	 * 画面サイズとかウィジェットの配置は自由に
	 */
	protected abstract void createContents();

	/**
	 * 設定を保存します。
	 * 画面を閉じるときに必ず呼ばれます
	 * 任意のタイミングでユーザが保存してもOK
	 */
	protected final void save() {
		new SerializerUtil<S>().save(filename, setting);
	}

	/**
	 * 設定を取得します。
	 * @return 設定
	 */
	public S getSetting() {
	    return setting;
	}

	/**
	 * 設定を設定します。
	 * @param setting 設定
	 */
	public void setSetting(S setting) {
	    this.setting = setting;
	}

	@Override
	protected final void checkSubclass() {
	}
}
