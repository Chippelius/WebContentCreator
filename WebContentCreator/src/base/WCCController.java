package base;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import data.*;

/*
 * Control part of WebContentCreator (by concept of ModelViewControl)
 * 
 * Created by Leo Köberlein on 09.07.2017
 */
@SuppressWarnings("serial")
public class WCCController {

	private static final String VERSION = "0.2";

	//Variables state, if action is performed directly (without asking anything)
	private static boolean doFileExport, doFileExit, doPageNew, doPageChangeData, doPageDelete = false; //TODO: rest


	public static void main(String[] args) {
		WCCModel.init();
		WCCView.init();
	}

	/* Optional functionality for later:
	 * public static final String fileNew = "fileNew";
	 * public static final String fileOpen = "fileOpen";
	 */

	public static final AbstractAction fileSave = new AbstractAction(WCCView.fileSaveText, WCCView.fileSaveIcon) {
		@Override
		public void actionPerformed(ActionEvent a) {
			WCCModel.saveDataStorage();
		}
	};

	//TODO: Rest in AbstractActions umwandeln

	/* Optional functionality for later:
	 * public static final String fileSaveAs = "fileSaveAs";
	 */

	public static final AbstractAction fileExport = new AbstractAction(WCCView.fileExportText, WCCView.fileExportIcon) {
		@Override
		public void actionPerformed(ActionEvent a) {
			if(!doFileExport) {
				WCCView.askForExportDestination();
			} else {
				doFileExport = false;
				WCCModel.export(WCCView.tmpExportLocation);
				WCCView.tmpExportLocation = "";
			}
		}
	};

	public static final AbstractAction fileExit = new AbstractAction(WCCView.fileExitText) {
		@Override
		public void actionPerformed(ActionEvent a) {
			if(!doFileExit && WCCModel.getDataStorage().isEditedSinceLastSave()) {
				WCCView.askForSaveBeforeExit();
			} else {
				doFileExit = false;
				WCCView.fetchSettings();
				WCCModel.saveSettings();
				System.exit(0);
			}
		}
	};

	public static final AbstractAction pageNew = new AbstractAction(WCCView.pageNewText, WCCView.pageNewIcon) {
		@Override
		public void actionPerformed(ActionEvent a) {
			if(!doPageNew) {
				WCCView.tmpPageFilename = "";
				WCCView.tmpPageName = "";
				WCCView.askForNewPageData();
			} else {
				doPageNew = false;
				if(WCCModel.getDataStorage().isValidFilename(WCCView.tmpPageFilename)) {
					WCCModel.getDataStorage().createPage(WCCView.tmpPageFilename, WCCView.tmpPageName);
					WCCView.tmpPageFilename = "";
					WCCView.tmpPageName = "";
				} else {
					WCCView.showMessage("Der Dateiname \""+WCCView.tmpPageFilename+"\" ist ungültig oder schon vergeben! \n"
							+ "Bitte suchen Sie einen anderen aus. \n"
							+ "Ein Dateiname muss eine Endung besitzen (z.B. '.html'), darf aber sonst keine Sonderzeichen enthalten", 
							"Dateiname ungültig", JOptionPane.ERROR_MESSAGE);
					WCCView.askForNewPageData();
				}
			}
		}
	};

	public static final AbstractAction pageChangeData = new AbstractAction(WCCView.pageChangeDataText) {
		@Override
		public void actionPerformed(ActionEvent a) {
			if(!doPageChangeData) {
				WCCView.requestChangePageData();
			} else {
				doPageChangeData = false;
				Page p = WCCModel.getDataStorage().get(WCCView.getSelectedPage());
				p.setFilename(WCCView.tmpPageFilename);
				WCCView.tmpPageFilename = "";
				p.setName(WCCView.tmpPageName);
				WCCView.tmpPageName = "";
			}
		}
	};

	public static final AbstractAction pageMoveTop = new AbstractAction(WCCView.pageMoveTopText) {
		@Override
		public void actionPerformed(ActionEvent a) {
			WCCModel.getDataStorage().add(0, WCCModel.getDataStorage().remove(WCCView.getSelectedPage()));
		}
	};

	public static final AbstractAction pageMoveBottom = new AbstractAction(WCCView.pageMoveBottomText) {
		@Override
		public void actionPerformed(ActionEvent a) {
			WCCModel.getDataStorage().add(WCCModel.getDataStorage().remove(WCCView.getSelectedPage()));
		}
	};

	public static final AbstractAction pageMoveUp = new AbstractAction(WCCView.pageMoveUpText) {
		@Override
		public void actionPerformed(ActionEvent a) {
			int index = WCCModel.getDataStorage().indexOf(WCCView.getSelectedPage());
			if(index > 0) {
				WCCModel.getDataStorage().set(index-1, WCCModel.getDataStorage().set(index, WCCModel.getDataStorage().get(index-1)));
			}
		}
	};

	public static final AbstractAction pageMoveDown = new AbstractAction(WCCView.pageMoveDownText) {
		@Override
		public void actionPerformed(ActionEvent a) {
			int index = WCCModel.getDataStorage().indexOf(WCCView.getSelectedPage());
			if(index < WCCModel.getDataStorage().size()-1) {
				WCCModel.getDataStorage().set(index+1, WCCModel.getDataStorage().set(index, WCCModel.getDataStorage().get(index+1)));
			}
		}
	};

	public static final AbstractAction pageDelete = new AbstractAction(WCCView.pageDeleteText) {
		@Override
		public void actionPerformed(ActionEvent a) {
			if(!doPageDelete) {
				WCCView.askForDeletePage();
			} else { 
				WCCModel.getDataStorage().remove(WCCView.getSelectedPage());
			}
		}
	};

	public static void elementNew(String parentFilename, String type, String value) {
		if(value == null) {
			WCCView.askForNewElementData(parentFilename, type);
		} else {
			WCCModel.getDataStorage().get(parentFilename).createElement(type, value);
		}
	}

	public static void elementChangeType(String filename, int elementIndex, String newType) {
		WCCModel.getDataStorage().get(filename).get(elementIndex).setType(newType);
	}

	public static void elementChangeValue(String filename, int elementIndex, String newValue) {
		if(newValue == null) {
			WCCView.requestChangeElementData(filename, elementIndex);
		} else {
			WCCModel.getDataStorage().get(filename).get(elementIndex).setValue(newValue);
		}
	}

	public static void elementMoveTop(String filename, int elementIndex) {
		Page p =  WCCModel.getDataStorage().get(filename);
		p.add(0, p.remove(elementIndex));
	}

	public static void elementMoveBottom(String filename, int elementIndex) {
		Page p =  WCCModel.getDataStorage().get(filename);
		p.add(p.remove(elementIndex));
	}

	public static void elementMoveUp(String filename, int elementIndex) {
		Page p =  WCCModel.getDataStorage().get(filename);
		if(elementIndex > 0) {
			p.set(elementIndex-1, p.set(elementIndex, p.get(elementIndex-1)));
		}
	}

	public static void elementMoveDown(String filename, int elementIndex) {
		Page p =  WCCModel.getDataStorage().get(filename);
		if(elementIndex < p.size()-1) {
			p.set(elementIndex+1, p.set(elementIndex, p.get(elementIndex+1)));
		}
	}

	public static void elementDelete(boolean forced, String filename, int elementIndex) {
		if(forced) {
			WCCModel.getDataStorage().get(filename).remove(elementIndex);
		} else {
			WCCView.askForDeleteElement(filename, elementIndex);
		}
	}

	public static void windowToggleMaximized() {
		WCCView.fetchSettings();
		WCCModel.getSettings().setFullscreen(!WCCModel.getSettings().isFullscreen());
		WCCView.applySettings();
	}

	public static void windowCenterDivider() {
		WCCView.fetchSettings();
		WCCModel.getSettings().setDividerLocation((WCCModel.getSettings().getSize().width - 5)/2);
		WCCView.applySettings();
	}

	public static void helpInfo() {
		WCCView.showMessage(WCCModel.getReadmeText(), "Info", JOptionPane.INFORMATION_MESSAGE);
	}

	public static void helpCheckForUpdates() {
		WCCView.showMessage("Die automatische Update-Funktion ist noch nicht verfügbar.\n"
				+ "Ihre Version ist: "+VERSION+"\n"
				+ "Wenn auf www.github.com/Chippelius/WebContentCreator/ eine neuere Version verfügbar ist,\n"
				+ "laden Sie diese bitte manuell herunter.", "Noch nicht verfügbar.", JOptionPane.ERROR_MESSAGE);
	}

}
