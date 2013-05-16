package jp.co.nicovideo.eka2513.commentviewerj.main.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public abstract class SearchText extends Text implements KeyListener {

	public SearchText(Composite parent, String placeholder) {
		super(parent, SWT.SEARCH | SWT.ICON_CANCEL | SWT.SINGLE | SWT.BORDER );
		setMessage(placeholder);
		addKeyListener(this);
//		addlisterner();
	}
	@Override
	public void keyReleased(KeyEvent event) {
	}
	@Override
	public void keyPressed(KeyEvent event) {
		if (event.character == SWT.CR) {
			enterKeyDetected();
		}
	}

	public abstract void enterKeyDetected();


	@Override
	protected void checkSubclass() {
		//hacker!
	}

	@Override
	protected void checkWidget() {
		//hacker!
	}

}