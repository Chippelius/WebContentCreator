package gui;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import base.WCCView;

public class WCCMenubar extends JMenuBar {

	public WCCMenubar() {
		super();
		setBackground(WCCView.backgroundColor);
		
		WCCMenu menuFile = new WCCMenu("Datei  ", null);
	}

}
