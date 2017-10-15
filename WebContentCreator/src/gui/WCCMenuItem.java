package gui;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenuItem;

@SuppressWarnings({ "unused", "serial" })
public class WCCMenuItem extends JMenuItem {

	private WCCMenuItem() {}
	private WCCMenuItem(Icon icon) {}
	private WCCMenuItem(String text) {}
	private WCCMenuItem(String text, Icon icon) {}
	private WCCMenuItem(String text, int mnemonic) {}

	public WCCMenuItem(Action a) {
		super(a);
	}
	
}
