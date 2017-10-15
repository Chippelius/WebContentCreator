package base;

import data.*;

/*
 * Control part of WebContentCreator (by concept of ModelViewControl)
 * 
 * Created by Leo Köberlein on 09.07.2017
 */
public class WCCController {

	public static void main(String[] args) {
		WCCModel.init();
		WCCView.init();
	}

	/* Optional functionality for later:
	 * public static final String fileNew = "fileNew";
	 * public static final String fileOpen = "fileOpen";
	 */

	public static void fileSave() {
		WCCModel.saveDataStorage();
	}

	/* Optional functionality for later:
	 * public static final String fileSaveAs = "fileSaveAs";
	 */

	public static void fileExport() {
		String location = WCCView.requestExportDestination();
		if(location != null) {
			WCCModel.export(location);
		}
	}

	public static void fileExit() {
		if(WCCModel.getDataStorage().isEditedSinceLastSave()) {
			int res = WCCView.requestSaveBeforeExit();
			if(res == 2)
				return;
			else if(res == 0)
				WCCModel.saveDataStorage();
		}
		WCCView.fetchSettings();
		WCCModel.saveSettings();
		System.exit(0);
	}

	public static void pageNew() {
		while(true) {
			String[] res = WCCView.requestNewPageData();
			if(res == null)
				return;

			if(WCCModel.getDataStorage().isValidFilename(res[0])) {
				WCCModel.getDataStorage().createPage(res[0], res[1]);
				return;
			} else {
				WCCView.showIllegalFilenameWarning(res[0]);
			}
		}
	}

	public static void pageChangeData() {
		while(true) {
			String[] res = WCCView.requestChangePageData();
			if(res == null)
				return;
			if(WCCModel.getDataStorage().isValidFilename(res[0])) {
				Page p = WCCModel.getDataStorage().get(WCCView.getSelectedPage());
				p.setFilename(res[0]);
				p.setName(res[1]);
				return;
			} else {
				WCCView.showIllegalFilenameWarning(res[0]);
			}
		}
	}

	public static void pageMoveTop() {
		WCCModel.getDataStorage().add(0, WCCModel.getDataStorage().remove(WCCView.getSelectedPage()));
	}

	public static void pageMoveBottom() {
		WCCModel.getDataStorage().add(WCCModel.getDataStorage().remove(WCCView.getSelectedPage()));
	}

	public static void pageMoveUp() {
		int index = WCCModel.getDataStorage().indexOf(WCCView.getSelectedPage());
		if(index > 0) {
			WCCModel.getDataStorage().set(index-1, WCCModel.getDataStorage().set(index, WCCModel.getDataStorage().get(index-1)));
		}
	}

	public static void pageMoveDown() {
		int index = WCCModel.getDataStorage().indexOf(WCCView.getSelectedPage());
		if(index < WCCModel.getDataStorage().size()-1) {
			WCCModel.getDataStorage().set(index+1, WCCModel.getDataStorage().set(index, WCCModel.getDataStorage().get(index+1)));
		}
	}

	public static void pageDelete() {
		if(WCCView.requestDeletePage()) {
			WCCModel.getDataStorage().remove(WCCView.getSelectedPage());
		}
	}

	public static void elementNew(String type) {
		String[] res = WCCView.requestNewElementData(type);
		if(res != null) {
			WCCModel.getDataStorage().get(WCCView.getSelectedPage()).createElement(res[0], res[1]);
		}
	}

	public static void elementChangeType(String newType) {
		WCCModel.getDataStorage().get(WCCView.getSelectedPage()).get(WCCView.getSelectedElement()).setType(newType);
	}

	public static void elementChangeValue() {
		String res = WCCView.requestChangeElementData();
		if(res != null) {
			WCCModel.getDataStorage().get(WCCView.getSelectedPage()).get(WCCView.getSelectedElement()).setValue(res);
		}
	}

	public static void elementMoveTop() {
		Page p =  WCCModel.getDataStorage().get(WCCView.getSelectedPage());
		p.add(0, p.remove(WCCView.getSelectedElement()));
	}

	public static void elementMoveBottom() {
		Page p =  WCCModel.getDataStorage().get(WCCView.getSelectedPage());
		p.add(p.remove(WCCView.getSelectedElement()));
	}

	public static void elementMoveUp() {
		Page p =  WCCModel.getDataStorage().get(WCCView.getSelectedPage());
		int elementIndex = WCCView.getSelectedElement();
		if(elementIndex > 0) {
			p.set(elementIndex-1, p.set(elementIndex, p.get(elementIndex-1)));
		}
	}

	public static void elementMoveDown() {
		Page p =  WCCModel.getDataStorage().get(WCCView.getSelectedPage());
		int elementIndex = WCCView.getSelectedElement();
		if(elementIndex < p.size()-1) {
			p.set(elementIndex+1, p.set(elementIndex, p.get(elementIndex+1)));
		}
	}

	public static void elementDelete() {
		if(WCCView.requestDeleteElement()) {
			WCCModel.getDataStorage().get(WCCView.getSelectedPage()).remove(WCCView.getSelectedElement());
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
		WCCView.showMessage(WCCModel.getReadmeText(), "Info");
	}

	public static void helpCheckForUpdates() {
		WCCView.showError("Die automatische Update-Funktion ist noch nicht verfügbar.\n"
				+ "Ihre Version ist: "+WCCModel.VERSION+"\n"
				+ "Wenn auf www.github.com/Chippelius/WebContentCreator/ eine neuere Version verfügbar ist,\n"
				+ "laden Sie diese bitte manuell herunter.", "Noch nicht verfügbar.");
	}

}
