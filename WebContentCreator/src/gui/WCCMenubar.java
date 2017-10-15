package gui;

import javax.swing.JMenuBar;

import base.WCCView;

@SuppressWarnings("serial")
public class WCCMenubar extends JMenuBar {

	public WCCMenubar() {
		super();
		setBackground(WCCView.backgroundColor);
		
		add(createFileMenu());
		add(createPageMenu());
		add(createElementMenu());
		add(createWindowMenu());
		add(createHelpMenu());
	}

	private static WCCMenu createFileMenu() {
		WCCMenu menuFile = new WCCMenu(Language.fileText+"  ");
		/* Optional functionality for later:
		 * menuFile.add(new WCCMenuItem(Actions.projectNew);
		 * menuFile.addSeparator();
		 * menuFile.add(new WCCMenuItem(Actions.projectOpen);
		 * menuFile.addSeparator();
		 */
		menuFile.add(new WCCMenuItem(Actions.fileSave));
		//Optional functionality for later:
		//menuFile.add(new WCCMenuItem(Actions.fileSaveAs));
		menuFile.addSeparator();
		menuFile.add(new WCCMenuItem(Actions.fileExport));
		menuFile.addSeparator();
		menuFile.add(new WCCMenuItem(Actions.fileExit));
		return menuFile;
	}

	private static WCCMenu createPageMenu() {
		WCCMenu menuPage = new WCCMenu(Language.pageText+"  ");
		menuPage.add(new WCCMenuItem(Actions.pageNew));
		menuPage.addSeparator();
		menuPage.add(new WCCMenuItem(Actions.pageChangeData));
		menuPage.addSeparator();
		menuPage.add(new WCCMenuItem(Actions.pageMoveTop));
		menuPage.add(new WCCMenuItem(Actions.pageMoveUp));
		menuPage.add(new WCCMenuItem(Actions.pageMoveDown));
		menuPage.add(new WCCMenuItem(Actions.pageMoveBottom));
		menuPage.addSeparator();
		menuPage.add(new WCCMenuItem(Actions.pageDelete));
		return menuPage;
	}

	private static WCCMenu createElementMenu() {
		WCCMenu menuElement = new WCCMenu(Language.elementText);
		menuElement.add(new WCCMenuItem(Actions.elementNewHeader));
		menuElement.add(new WCCMenuItem(Actions.elementNewSubheader));
		menuElement.add(new WCCMenuItem(Actions.elementNewText));
		menuElement.add(new WCCMenuItem(Actions.elementNewImage));
		menuElement.addSeparator();
		menuElement.add(new WCCMenuItem(Actions.elementChangeValue));
		menuElement.add(new WCCMenuItem(Actions.elementChangeToHeader));
		menuElement.add(new WCCMenuItem(Actions.elementChangeToSubheader));
		menuElement.add(new WCCMenuItem(Actions.elementChangeToText));
		menuElement.add(new WCCMenuItem(Actions.elementChangeToImage));
		menuElement.addSeparator();
		menuElement.add(new WCCMenuItem(Actions.elementMoveTop));
		menuElement.add(new WCCMenuItem(Actions.elementMoveUp));
		menuElement.add(new WCCMenuItem(Actions.elementMoveDown));
		menuElement.add(new WCCMenuItem(Actions.elementMoveBottom));
		menuElement.addSeparator();
		menuElement.add(new WCCMenuItem(Actions.elementDelete));
		return menuElement;
	}

	private static WCCMenu createWindowMenu() {
		WCCMenu menuWindow = new WCCMenu(Language.windowText);
		menuWindow.add(new WCCMenuItem(Actions.windowToggleMaximized));
		menuWindow.add(new WCCMenuItem(Actions.windowCenterDivider));
		return menuWindow;
	}

	private static WCCMenu createHelpMenu() {
		WCCMenu menuHelp = new WCCMenu(Language.helpText);
		menuHelp.add(new WCCMenuItem(Actions.helpInfo));
		menuHelp.add(new WCCMenuItem(Actions.helpCheckForUpdates));
		return menuHelp;
	}

}
