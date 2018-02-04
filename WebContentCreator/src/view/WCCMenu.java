package view;

import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenu;

@SuppressWarnings({ "unused", "serial" })
public class WCCMenu extends JMenu {

	private WCCMenu() {}
	private WCCMenu(String arg0, boolean arg1) {}
	private WCCMenu(Action action) {}
	
	public WCCMenu(String title) {
		super(title);
	}
	
	public WCCMenu(String title, Icon icon) {
		super(title);
		setIcon(icon);
	}
	
}
