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
public class WCCController {
	
	private static final String VERSION = "0.2";
	
	//Variables state, if action is performed directly (without asking anything)
	private static boolean doFileExport, doFileExit, doPageNew, doPageSelect, doPageChangeData; //TODO: rest
	
	
	public static void main(String[] args) {
		WCCModel.init();
		WCCView.init();
	}
	
	/* Optional functionality for later:
	 * public static final String fileNew = "fileNew";
	 * public static final String fileOpen = "fileOpen";
	 */
	
	public static final AbstractAction fileSave = new AbstractAction(WCCView.fileSaveText) {
		@Override
		public void actionPerformed(ActionEvent a) {
			WCCModel.saveDataStorage();
		}
	};
	
	//TODO: Rest in AbstractActions umwandeln
	
	/* Optional functionality for later:
	 * public static final String fileSaveAs = "fileSaveAs";
	 */
	
	public static void fileExport(String destination) {
		if(destination == null)
			WCCView.askForExportDestination();
		else
			WCCModel.export(destination);
	}
	
	public static void fileExit(boolean forced) {
		WCCView.fetchSettings();
		WCCModel.saveSettings();
		if(!forced && WCCModel.getDataStorage().isEditedSinceLastSave()) {
			WCCView.askForSaveBeforeExit();
		} else {
			System.exit(0);
		}
	}
	
	public static void pageNew(String filename, String name) {
		if(filename == null) {
			WCCView.askForNewPageData(null, null);
		} else {
			if(WCCModel.getDataStorage().isValidFilename(filename)) {
				WCCModel.getDataStorage().createPage(filename, name);
			} else {
				WCCView.showMessage("Der Dateiname \""+filename+"\" ist ungültig oder schon vergeben! \n"
						+ "Bitte suchen Sie einen anderen aus. \n"
						+ "Ein Dateiname muss eine Endung besitzen (z.B. '.html'), darf aber sonst keine Sonderzeichen enthalten", 
						"Dateiname ungültig", JOptionPane.ERROR_MESSAGE);
				WCCView.askForNewPageData(filename, name);
			}
		}
	}
	
	public static void pageChangeData(String filename, String name, String newFilename, String newName) {
		if(newFilename == null) {
			WCCView.requestChangePageData(filename, name);
		} else {
			Page p = WCCModel.getDataStorage().get(filename);
			p.setFilename(newFilename);
			p.setName(newName);
		}
	}
	
	public static void pageMoveTop(String filename) {
		Page p = WCCModel.getDataStorage().remove(WCCModel.getDataStorage().indexOf(filename));
		WCCModel.getDataStorage().add(0, p);
	}
	
	public static void pageMoveBottom(String filename) {
		Page p = WCCModel.getDataStorage().remove(WCCModel.getDataStorage().indexOf(filename));
		WCCModel.getDataStorage().add(p);
	}
	
	public static void pageMoveUp(String filename) {
		int index = WCCModel.getDataStorage().indexOf(filename);
		if(index > 0) {
			Page p = WCCModel.getDataStorage().set(index, WCCModel.getDataStorage().get(index-1));
			WCCModel.getDataStorage().set(index-1, p);
		}
	}
	
	public static void pageMoveDown(String filename) {
		int index = WCCModel.getDataStorage().indexOf(filename);
		if(index < WCCModel.getDataStorage().size()-1) {
			Page p = WCCModel.getDataStorage().set(index, WCCModel.getDataStorage().get(index+1));
			WCCModel.getDataStorage().set(index+1, p);
		}
	}
	
	public static void pageDelete(boolean forced, String filename) {
		if(forced) {
			WCCModel.getDataStorage().remove(filename);
		} else { 
			WCCView.askForDeletePage(filename);
		}
	}
	
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
