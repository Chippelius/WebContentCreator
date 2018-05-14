package view;

import javax.swing.JMenuBar;

import contoller.WCCController;

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
		menuFile.add(new WCCMenuItem(WCCController.fileNew));
		menuFile.addSeparator();
		menuFile.add(new WCCMenuItem(WCCController.fileOpen));
		menuFile.addSeparator();
		menuFile.add(new WCCMenuItem(WCCController.fileSave));
		menuFile.add(new WCCMenuItem(WCCController.fileSaveAs));
		menuFile.addSeparator();
		menuFile.add(new WCCMenuItem(WCCController.fileExport));
		menuFile.add(new WCCMenuItem(WCCController.fileGenerateQRCodes));
		menuFile.addSeparator();
		menuFile.add(new WCCMenuItem(WCCController.fileExit));
		return menuFile;
	}

	private static WCCMenu createPageMenu() {
		WCCMenu menuPage = new WCCMenu(Language.pageText+"  ");
		menuPage.add(new WCCMenuItem(WCCController.pageNew));
		menuPage.addSeparator();
		menuPage.add(new WCCMenuItem(WCCController.pageChangeData));
		menuPage.addSeparator();
		menuPage.add(new WCCMenuItem(WCCController.pageMoveTop));
		menuPage.add(new WCCMenuItem(WCCController.pageMoveUp));
		menuPage.add(new WCCMenuItem(WCCController.pageMoveDown));
		menuPage.add(new WCCMenuItem(WCCController.pageMoveBottom));
		menuPage.addSeparator();
		menuPage.add(new WCCMenuItem(WCCController.pageDelete));
		return menuPage;
	}

	private static WCCMenu createElementMenu() {
		WCCMenu menuElement = new WCCMenu(Language.elementText);
		menuElement.add(new WCCMenuItem(WCCController.elementNewHeader));
		menuElement.add(new WCCMenuItem(WCCController.elementNewSubheader));
		menuElement.add(new WCCMenuItem(WCCController.elementNewText));
		menuElement.add(new WCCMenuItem(WCCController.elementNewImage));
		menuElement.addSeparator();
		menuElement.add(new WCCMenuItem(WCCController.elementChangeValue));
		menuElement.add(new WCCMenuItem(WCCController.elementChangeToHeader));
		menuElement.add(new WCCMenuItem(WCCController.elementChangeToSubheader));
		menuElement.add(new WCCMenuItem(WCCController.elementChangeToText));
		menuElement.add(new WCCMenuItem(WCCController.elementChangeToImage));
		menuElement.addSeparator();
		menuElement.add(new WCCMenuItem(WCCController.elementMoveTop));
		menuElement.add(new WCCMenuItem(WCCController.elementMoveUp));
		menuElement.add(new WCCMenuItem(WCCController.elementMoveDown));
		menuElement.add(new WCCMenuItem(WCCController.elementMoveBottom));
		menuElement.addSeparator();
		menuElement.add(new WCCMenuItem(WCCController.elementDelete));
		return menuElement;
	}

	private static WCCMenu createWindowMenu() {
		WCCMenu menuWindow = new WCCMenu(Language.windowText);
		menuWindow.add(new WCCMenuItem(WCCController.windowToggleMaximized));
		menuWindow.add(new WCCMenuItem(WCCController.windowRestoreDefaultState));
		menuWindow.add(new WCCMenuItem(WCCController.windowCenterDivider));
		return menuWindow;
	}

	private static WCCMenu createHelpMenu() {
		WCCMenu menuHelp = new WCCMenu(Language.helpText);
		menuHelp.add(new WCCMenuItem(WCCController.helpInfo));
		menuHelp.add(new WCCMenuItem(WCCController.helpCheckForUpdates));
		return menuHelp;
	}

}
