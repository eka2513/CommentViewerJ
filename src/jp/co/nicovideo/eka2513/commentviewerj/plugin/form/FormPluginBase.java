package jp.co.nicovideo.eka2513.commentviewerj.plugin.form;

import java.lang.reflect.Constructor;

import jp.co.nicovideo.eka2513.commentviewerj.constants.CommentViewerConstants;
import jp.co.nicovideo.eka2513.commentviewerj.exception.CommentViewerException;
import jp.co.nicovideo.eka2513.commentviewerj.main.swt.constants.GUIConstants;
import jp.co.nicovideo.eka2513.commentviewerj.plugin.PluginBase;
import jp.co.nicovideo.eka2513.commentviewerj.util.SerializerUtil;

/**
 * <pre>
 * フォームを使用するプラグインの基底クラス
 * </pre>
 * @author eka2513
 *
 * @param <F> FormBaseクラスを継承した設定画面のインスタンス
 * @param <S> FormPluginSettingを継承した設定Beanのインスタンス
 */
public abstract class FormPluginBase<F extends FormBase<S>, S extends FormPluginSetting> extends PluginBase {

	private static final long serialVersionUID = 5426184514191232447L;
	/** 設定保存用のbean */
	private S setting;
	/** 設定保存用のbeanクラス */
	private Class<S> clazzS;
	/** フォームのクラス */
	private Class<F> clazzF;

	/**
	 * コンストラクタ
	 */
	@SuppressWarnings("unchecked")
	public FormPluginBase() {
		try {
			clazzF = (Class<F>) Class.forName(this.getClass().getName() + "Form");
			clazzS = (Class<S>) Class.forName(this.getClass().getName() + "Setting");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		setting = new SerializerUtil<S>().load(getSettingFilename());
		if (setting == null) {
			createSetting();
		}
	}

	public final String getSettingFilename() {
		return GUIConstants.PLUGIN_DAT_DIR + clazzS.getName() + ".dat";
	}

	/**
	 * コンストラクタ。コメビュ本体から呼ばれます。
	 * @param clazzF
	 * @param clazzS
	 */
	public FormPluginBase(Class<F> clazzF, Class<S> clazzS) {
		this.clazzS = clazzS;
		this.clazzF = clazzF;
		setting = new SerializerUtil<S>().load(getSettingFilename());
		if (setting == null) {
			createSetting();
		}
	}

	/**
	 * 設定クラスのインスタンス生成
	 */
	private final void createSetting() {
		try {
			setting = (S) clazzS.newInstance();
		} catch (InstantiationException e) {
			throw new CommentViewerException(clazzS.getName() + "のインスタンス生成に失敗しました");
		} catch (IllegalAccessException e) {
			throw new CommentViewerException(clazzS.getName() + "のインスタンス生成に失敗しました");
		}
	}

	/**
	 * フォームクラスのインスタンス生成
	 */
	private final F createForm() {
		try {
			Class<?>[] types = new Class<?>[]{clazzS, String.class};
			Constructor<F> constructor = clazzF.getConstructor(types);
			Object[] args = new Object[]{setting, GUIConstants.PLUGIN_DAT_DIR + clazzS.getName() + ".dat"};
			return (F) constructor.newInstance(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public final String createFilename() {
		return CommentViewerConstants.PLUGIN_XML_DIR + this.getClass().getName() + ".xml";
	}

	/**
	 * フォームを開きます。コメビュ本体から呼ばれます。
	 */
	public final void formOpen() {
		F form = createForm();
		if (form != null)
			form.open();
	}

	/**
	 * 設定保存用のbeanを取得します。
	 * @return 設定保存用のbean
	 */
	public S getSetting() {
	    return setting;
	}
}
