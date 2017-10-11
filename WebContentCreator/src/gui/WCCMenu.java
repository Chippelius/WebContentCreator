package gui;

import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenu;

public class WCCMenu extends JMenu {

	private WCCMenu() {}
	private WCCMenu(String arg0, boolean arg1) {}
	
	private WCCMenu(String title) {
		super(title);
	}
	
	public WCCMenu(String title, ActionListener listener) {
		this(title);
		addActionListener(listener);
	}
	
	public WCCMenu(String title, Icon icon, ActionListener listener) {
		this(title, listener);
		setIcon(icon);
	}
	
	public WCCMenu(Action arg0) {
		super(arg0);
	}
	
	public WCCMenu(Action action, Icon icon) {
		
	}
}
