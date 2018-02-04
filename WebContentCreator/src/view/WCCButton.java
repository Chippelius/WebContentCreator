package view;

import java.awt.Cursor;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

@SuppressWarnings({ "unused", "serial" })
public class WCCButton extends JButton {

	private WCCButton() {}
	private WCCButton(Icon arg0) {}
	private WCCButton(String arg0) {}
	private WCCButton(String arg0, Icon arg1) {}
	
	public WCCButton(Action a) {
		super(a);
		setText("");
		setToolTipText((String) a.getValue(Action.NAME));
		setFocusable(false);
		setContentAreaFilled(false);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

}
