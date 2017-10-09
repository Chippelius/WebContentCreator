import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JOptionPane;

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
	
	/*
	 * Optional functionality for later:
	 * public static final String fileNew = "fileNew";
	 * public static final String fileOpen = "fileOpen";
	 */
	
	public static void fileSave() {
		WCCModel.saveDataStorage();
	}
	/*
	 * Optional functionality for later:
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
	
	public static void pageNewPage(String filename, String name) {
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
	
	public static void pageSelect(int pageNr) {
		WCCView.selectPage(pageNr);
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
	
	public static void elementNewHeader(String parentFilename, String value) {
		if(value == null) {
			WCCView.requestNewHeaderData(parentFilename);
		} else {
			WCCModel.getDataStorage().get(parentFilename).createElement(Element.HEADER, value);
		}
	}
	
	public static void elementNewSubheader(String parentFilename, String value) {
		if(value == null) {
			WCCView.requestNewSubheaderData(parentFilename);
		} else {
			WCCModel.getDataStorage().get(parentFilename).createElement(Element.SUBHEADER, value);
		}
	}
	
	public static void elementNewText(String parentFilename, String value) {
		if(value == null) {
			WCCView.requestNewTextData(parentFilename);
		} else {
			WCCModel.getDataStorage().get(parentFilename).createElement(Element.TEXT, value);
		}
	}
	
	public static void elementNewImage(String parentFilename, String value) {
		if(value == null) {
			WCCView.requestNewImageData(parentFilename);
		} else {
			WCCModel.getDataStorage().get(parentFilename).createElement(Element.IMAGE, value);
		}
	}
	
	public static final String elementChangeTypeToHeader = "elementChangeTypeToHeader";
	public static final String elementChangeTypeToSubheader = "elementChangeTypeToSubheader";
	public static final String elementChangeTypeToText = "elementChangeTypeToText";
	public static final String elementChangeTypeToImage = "elementChangeTypeToImage";
	public static final String elementChangeData = "elementChangeData";
	public static final String elementMoveTop = "elementMoveTop";
	public static final String elementMoveBottom = "elementMoveBottom";
	public static final String elementMoveUp = "elementMoveUp";
	public static final String elementMoveDown = "elementMoveDown";
	public static final String elementDelete = "elementDelete";
	public static final String windowToggleMaximized = "windowToggleFullscreen";
	public static final String windowCenterDivider = "windowCenterDivider";
	public static final String helpInfo = "helpInfo";
	public static final String helpCheckForUpdates = "helpCheckForUpdates";

	public void checkForUpdates() {
		view.showMessage("Die automatische Update-Funktion ist noch nicht verfügbar.\n"
				+ "Ihre Version ist: "+VERSION+"\n"
				+ "Wenn auf www.github.com/Chippelius/WebContentCreator/ eine neuere Version verfügbar ist,\n"
				+ "laden Sie diese bitte manuell herunter.", "Noch nicht verfügbar.", JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		//TODO: implement rest
		String[] commandParts = ae.getActionCommand().split(":");
		Page p;
		int index;
		Element e;
		switch(commandParts[0]) {
		case elementChangeTypeToHeader:
			model.getDataStorage().get(model.getDataStorage().indexOf(commandParts[1])).get(Integer.parseInt(commandParts[2])).setType(Element.HEADER);
			break;
		case elementChangeTypeToSubheader:
			model.getDataStorage().get(model.getDataStorage().indexOf(commandParts[1])).get(Integer.parseInt(commandParts[2])).setType(Element.SUBHEADER);
			break;
		case elementChangeTypeToText:
			model.getDataStorage().get(model.getDataStorage().indexOf(commandParts[1])).get(Integer.parseInt(commandParts[2])).setType(Element.TEXT);
			break;
		case elementChangeTypeToImage:
			model.getDataStorage().get(model.getDataStorage().indexOf(commandParts[1])).get(Integer.parseInt(commandParts[2])).setType(Element.IMAGE);
			break;
		case elementChangeData:
			if(commandParts.length < 4) {
				view.requestChangeElementData(commandParts[1], Integer.parseInt(commandParts[2]));
			} else {
				model.getDataStorage().get(model.getDataStorage().indexOf(commandParts[1])).get(Integer.parseInt(commandParts[2])).setValue(commandParts[3]+(commandParts.length>4?":"+commandParts[4]:""));
			}
			break;
		case elementMoveTop:
			p =  model.getDataStorage().get(model.getDataStorage().indexOf(commandParts[1]));
			p.add(0, p.remove(Integer.parseInt(commandParts[2])));
			break;
		case elementMoveBottom:
			p =  model.getDataStorage().get(model.getDataStorage().indexOf(commandParts[1]));
			p.add(p.remove(Integer.parseInt(commandParts[2])));
			break;
		case elementMoveUp:
			p =  model.getDataStorage().get(model.getDataStorage().indexOf(commandParts[1]));
			index = Integer.parseInt(commandParts[2]);
			if(index > 0) {
				e = p.set(index, p.get(index-1));
				p.set(index-1, e);
			}
			break;
		case elementMoveDown:
			p =  model.getDataStorage().get(model.getDataStorage().indexOf(commandParts[1]));
			index = Integer.parseInt(commandParts[2]);
			if(index < p.size()-1) {
				e = p.set(index, p.get(index+1));
				p.set(index+1, e);
			}
			break;
		case elementDelete:
			if(Boolean.parseBoolean(commandParts[1])) {
				model.getDataStorage().get(model.getDataStorage().indexOf(commandParts[2])).remove(Integer.parseInt(commandParts[3]));
			} else {
				view.askForDeleteElement(commandParts[2], Integer.parseInt(commandParts[3]));
			}
			break;
		case windowToggleMaximized:
			view.fetchSettings();
			model.getSettings().setFullscreen(!model.getSettings().isFullscreen());
			view.applySettings();
			break;
		case windowCenterDivider:
			view.fetchSettings();
			model.getSettings().setDividerLocation((model.getSettings().getSize().width - 5)/2);
			view.applySettings();
			break;
		case helpInfo:
			view.showMessage(model.getReadmeText(), "Info", JOptionPane.INFORMATION_MESSAGE);
			break;
		case helpCheckForUpdates:
			checkForUpdates();
			break;
		default: 
		}
	}


}
