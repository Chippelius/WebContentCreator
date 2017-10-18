package gui;

import javax.swing.JToolBar;

@SuppressWarnings({ "unused", "serial" })
public class WCCToolbar extends JToolBar {

	private WCCToolbar(int arg0) {}
	private WCCToolbar(String arg0) {}
	private WCCToolbar(String arg0, int arg1) {}

	public WCCToolbar() {
		super("WCC Toolbar");
		setFloatable(true);
		//setBorder(BorderFactory.createRaisedBevelBorder());
		
		add(new WCCButton(Actions.fileSave));
		addSeparator();
		add(new WCCButton(Actions.fileExport));
		addSeparator();
		add(new WCCButton(Actions.pageNew));
		addSeparator();
		add(new WCCButton(Actions.elementNewHeader));
		add(new WCCButton(Actions.elementNewSubheader));
		add(new WCCButton(Actions.elementNewText));
		add(new WCCButton(Actions.elementNewImage));
	}
	
}
