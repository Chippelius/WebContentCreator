package view;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import contoller.WCCController;

@SuppressWarnings({ "unused", "serial" })
public class WCCToolbar extends JToolBar {
	
	protected final Dimension SPACER_SIZE = new Dimension(32, 32); 

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
		add(new WCCButton(WCCController.pageMoveTop));
		add(new WCCButton(WCCController.pageMoveUp));
		add(new WCCButton(WCCController.pageMoveDown));
		add(new WCCButton(WCCController.pageMoveBottom));
		addSpacer();
		addSeparator();
		add(new WCCButton(WCCController.elementNewHeader));
		add(new WCCButton(WCCController.elementNewSubheader));
		add(new WCCButton(WCCController.elementNewText));
		add(new WCCButton(WCCController.elementNewImage));
		add(new JLabel("   "));
		add(new WCCButton(WCCController.elementMoveTop));
		add(new WCCButton(WCCController.elementMoveUp));
		add(new WCCButton(WCCController.elementMoveDown));
		add(new WCCButton(WCCController.elementMoveBottom));
		addSpacer();
	}
	
	/**
	 * Appends an invisible spacer of dynamic size to the end of the tool bar.
	 */
	protected void addSpacer() {
		add(new JPanel());
	}
	
}
