package view;

import javax.swing.JToolBar;

import contoller.WCCController;

@SuppressWarnings({ "unused", "serial" })
public class WCCToolbar extends JToolBar {

	private WCCToolbar(int arg0) {}
	private WCCToolbar(String arg0) {}
	private WCCToolbar(String arg0, int arg1) {}

	public WCCToolbar() {
		super("WCC Toolbar");
		setFloatable(true);
		//setBorder(BorderFactory.createRaisedBevelBorder());
		
		add(new WCCButton(WCCController.fileSave));
		addSeparator();
		add(new WCCButton(WCCController.fileExport));
		add(new WCCButton(WCCController.fileGenerateQRCodes));
		addSeparator();
		add(new WCCButton(WCCController.pageNew));
		addSeparator();
		add(new WCCButton(WCCController.elementNewImage));
	}
	
}
