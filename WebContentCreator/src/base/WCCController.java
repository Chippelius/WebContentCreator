package base;

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
		String[] res = WCCView.requestNewPageData(null, null);
		if(res != null) {
			WCCModel.getDataStorage().createPage(res[0], res[1]);
		}
	}

	public static void pageChangeData() {
		String[] res = WCCView.requestNewPageData(WCCView.getSelectedPage().getFilename(), WCCView.getSelectedPage().getName());
		if(res != null) {
			WCCView.getSelectedPage().setFilename(res[0]);
			WCCView.getSelectedPage().setName(res[1]);
		}
	}

	public static void pageMoveTop() {
		WCCModel.getDataStorage().remove(WCCView.getSelectedPage());
		WCCModel.getDataStorage().add(0, WCCView.getSelectedPage());
	}

	public static void pageMoveBottom() {
		WCCModel.getDataStorage().remove(WCCView.getSelectedPage());
		WCCModel.getDataStorage().add(WCCView.getSelectedPage());
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
		String res = WCCView.requestNewElementData(type, null);
		if(res != null) {
			WCCView.getSelectedPage().createElement(type, res);
		}
	}

	public static void elementChangeType(String newType) {
		WCCView.getSelectedElement().setType(newType);
	}

	public static void elementChangeValue() {
		String res = WCCView.requestNewElementData(WCCView.getSelectedElement().getType(), WCCView.getSelectedElement().getValue());
		if(res != null) {
			WCCView.getSelectedElement().setValue(res);
		}
	}

	public static void elementMoveTop() {
		WCCView.getSelectedPage().remove(WCCView.getSelectedElement());
		WCCView.getSelectedPage().add(0, WCCView.getSelectedElement());
	}

	public static void elementMoveBottom() {
		WCCView.getSelectedPage().remove(WCCView.getSelectedElement());
		WCCView.getSelectedPage().add(WCCView.getSelectedElement());
	}

	public static void elementMoveUp() {
		int elementIndex = WCCView.getSelectedPage().indexOf(WCCView.getSelectedElement());
		if(elementIndex > 0) {
			WCCView.getSelectedPage().set(elementIndex-1, WCCView.getSelectedPage().set(elementIndex, WCCView.getSelectedPage().get(elementIndex-1)));
		}
	}

	public static void elementMoveDown() {
		int elementIndex = WCCView.getSelectedPage().indexOf(WCCView.getSelectedElement());
		if(elementIndex < WCCView.getSelectedPage().size()-1) {
			WCCView.getSelectedPage().set(elementIndex+1, WCCView.getSelectedPage().set(elementIndex, WCCView.getSelectedPage().get(elementIndex+1)));
		}
	}

	public static void elementDelete() {
		if(WCCView.requestDeleteElement()) {
			WCCView.getSelectedPage().remove(WCCView.getSelectedElement());
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
