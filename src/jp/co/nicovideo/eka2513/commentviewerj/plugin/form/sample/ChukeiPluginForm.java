package jp.co.nicovideo.eka2513.commentviewerj.plugin.form.sample;


import jp.co.nicovideo.eka2513.commentviewerj.plugin.form.FormBase;
import jp.nicovideo.eka2513.cookiegetter4j.util.StringUtil;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class ChukeiPluginForm extends FormBase<ChukeiPluginSetting> {

	public ChukeiPluginForm(Display display, ChukeiPluginSetting setting,
			String filename) {
		super(display, setting, filename);
	}

	private Text text;

	/**
	 * フォームの内容を記述します。
	 * フォームは設定項目をsettingというインスタンスに保存してください
	 */
	@Override
	protected void createContents() {

		//フォームの内容を記述
		setLayout(new GridLayout(2, false));

		//ラベル
		Label label = new Label(this, SWT.NONE);
		label.setText("/chukeiコマンドを発行する回数");
		//ループ回数
		text = new Text(this, SWT.SINGLE | SWT.BORDER );
		text.setText(setting.getLoop().toString());
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		text.setLayoutData(gd);
		//数値のみ許可
		text.addVerifyListener(new VerifyListener() {
			@Override
			public void verifyText(VerifyEvent event) {
				switch (event.keyCode) {
				case SWT.BS: // Backspace
				case SWT.DEL: // Delete
				case SWT.HOME: // Home
				case SWT.END: // End
				case SWT.ARROW_LEFT: // Left arrow
				case SWT.ARROW_RIGHT: // Right arrow
					return;
				}

				if (!Character.isDigit(event.character)) {
					event.doit = false; // disallow the action
					return;
				}
				text.setText(StringUtil.inull2Val(text.getText()).toString());
			}
		});
		text.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent event) {
				//画面で設定した内容をsettingオブジェクトにセット(本来はリスナの中とかで)
				if (text.getText().length() > 0)
					setting.setLoop(Integer.valueOf(text.getText()));
			}
		});

		this.pack();

		//設定されたsettingオブジェクトはformを閉じるときに保存されます
		//明示的に保存したい場合はスーパークラスのsaveメソッドを呼びます
//		save();
	}
}
