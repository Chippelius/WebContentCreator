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
public class WCCController implements ActionListener, WindowListener {
	
	public static final double VERSION = 1.0;
	
	private static WCCModel model;
	private static WCCView view;
	@SuppressWarnings("unused")
	private static WCCController controler;
	
	/*
	 * Optional functionality for later:
	 * public static final String fileNew = "fileNew";
	 * public static final String fileOpen = "fileOpen";
	 */
	public static final String fileSave = "fileSave";
	/*
	 * Optional functionality for later:
	 * public static final String fileSaveAs = "fileSaveAs";
	 */
	public static final String fileExport = "fileExport";
	public static final String fileExit = "fileExit";
	public static final String pageNewPage = "pageNewPage";
	public static final String pageSelect = "pageSelect";
	public static final String pageChangeData = "pageChangeData";
	public static final String pageMoveTop = "pageMoveTop";
	public static final String pageMoveBottom = "pageMoveBottom";
	public static final String pageMoveUp = "pageMoveUp";
	public static final String pageMoveDown = "pageMoveDown";
	public static final String pageDelete = "pageDelete";
	public static final String pageNewHeader = "pageNewHeader";
	public static final String pageNewSubheader = "pageNewSubheader";
	public static final String pageNewText = "pageNewText";
	public static final String pageNewImage = "pageNewImage";
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

	public static void main(String[] args) {
		controler = new WCCController();
	}
	
	public WCCController() {
		//Initialize components
		model = new WCCModel();
		view = new WCCView(model, this);
		view.setVisible(true);
	}
	
	public void shutdown(boolean forced) {
		view.fetchSettings();
		model.saveSettings();
		if(!forced && model.getDataStorage().isEditedSinceLastSave()) {
			view.askForSaveBeforeExit();
		} else {
			System.exit(0);
		}
	}
	
	public void checkForUpdates() {
		view.showMessage("Die automatische Update-Funktion ist noch nicht verfügbar.\n"
				+ "Ihre Version ist: "+VERSION+"\n"
				+ "Wenn auf www.github.com/Chippelius/WebContentCreator/ eine neuere Version verfügbar ist,\n"
				+ "laden Sie diese bitte manuell herunter.", "Noch nicht verfügbar.", JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void windowActivated(WindowEvent arg0) {}
	@Override
	public void windowClosed(WindowEvent arg0) {}
	@Override
	public void windowClosing(WindowEvent arg0) {
		shutdown(false);
	}
	@Override
	public void windowDeactivated(WindowEvent arg0) {}
	@Override
	public void windowDeiconified(WindowEvent arg0) {}
	@Override
	public void windowIconified(WindowEvent arg0) {}
	@Override
	public void windowOpened(WindowEvent arg0) {}

	@Override
	public void actionPerformed(ActionEvent ae) {
		//TODO: implement rest
		String[] commandParts = ae.getActionCommand().split(":");
		Page p;
		int index;
		Element e;
		switch(commandParts[0]) {
		/*
		 * Optional functionality for later:
		 * case fileNew:
		 * 	break;
		 * case fileOpen:
		 * 	break;
		 */
		case fileSave:
			model.saveDataStorage();
			break;
		/*
		 * Optional functionality for later:
		 * case fileSaveAs:
		 * 	break;
		 */
		case fileExport:
			//TODO: ask for location to export into
			break;
		case fileExit:
			if(commandParts.length > 1) {
				shutdown(Boolean.parseBoolean(commandParts[1]));
			} else {
				shutdown(false);
			}
			break;
		case pageNewPage:
			if(commandParts.length < 3) {
				view.requestNewPageData(null, null);
			} else {
				if(model.getDataStorage().isValidFilename(commandParts[1])) {
					model.getDataStorage().createPage(commandParts[1], commandParts[2]);
				} else {
					view.showMessage("Der Dateiname \""+commandParts[1]+"\" ist ungültig oder schon vergeben! \n"
							+ "Bitte suchen Sie einen anderen aus. \n"
							+ "Ein Dateiname muss eine Endung besitzen (z.B. '.html'), darf aber sonst keine Sonderzeichen enthalten", 
							"Dateiname ungültig", JOptionPane.ERROR_MESSAGE);
					view.requestNewPageData(commandParts[1], commandParts[2]);
				}
			}
			break;
		case pageSelect:
			view.selectPage(Integer.parseInt(commandParts[1]));
			break;
		case pageChangeData:
			if(Boolean.parseBoolean(commandParts[1])) {
				p = model.getDataStorage().get(model.getDataStorage().indexOf(commandParts[2]));
				p.setFilename(commandParts[4]);
				p.setName(commandParts[5]);
			} else {
				view.requestChangePageData(commandParts[2], commandParts[3]);
			}
			break;
		case pageMoveTop:
			p = model.getDataStorage().remove(model.getDataStorage().indexOf(commandParts[1]));
			model.getDataStorage().add(0, p);
			break;
		case pageMoveBottom:
			p = model.getDataStorage().remove(model.getDataStorage().indexOf(commandParts[1]));
			model.getDataStorage().add(p);
			break;
		case pageMoveUp:
			index = model.getDataStorage().indexOf(commandParts[1]);
			if(index > 0) {
				p = model.getDataStorage().set(index, model.getDataStorage().get(index-1));
				model.getDataStorage().set(index-1, p);
			}
			break;
		case pageMoveDown:
			index = model.getDataStorage().indexOf(commandParts[1]);
			if(index < model.getDataStorage().size()-1) {
				p = model.getDataStorage().set(index, model.getDataStorage().get(index+1));
				model.getDataStorage().set(index+1, p);
			}
			break;
		case pageDelete:
			if(Boolean.parseBoolean(commandParts[1])) {
				model.getDataStorage().remove(model.getDataStorage().indexOf(commandParts[2]));
			} else { 
				view.askForDeletePage(commandParts[2]);
			}
			break;
		case pageNewHeader:
			if(commandParts.length < 3) {
				view.requestNewHeaderData(commandParts[1]);
			} else {
				model.getDataStorage().get(model.getDataStorage().indexOf(commandParts[1])).createElement(Element.HEADER, commandParts[2]);
			}
			break;
		case pageNewSubheader:
			if(commandParts.length < 3) {
				view.requestNewSubheaderData(commandParts[1]);
			} else {
				model.getDataStorage().get(model.getDataStorage().indexOf(commandParts[1])).createElement(Element.SUBHEADER, commandParts[2]);
			}
			break;
		case pageNewText:
			if(commandParts.length < 3) {
				view.requestNewTextData(commandParts[1]);
			} else {
				model.getDataStorage().get(model.getDataStorage().indexOf(commandParts[1])).createElement(Element.TEXT, commandParts[2]);
			}
			break;
		case pageNewImage:
			if(commandParts.length < 3) {
				view.requestNewImageData(commandParts[1]);
			} else {
				model.getDataStorage().get(model.getDataStorage().indexOf(commandParts[1])).createElement(Element.IMAGE, commandParts[2]+":"+commandParts[3]);
			}
			break;
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
				model.getDataStorage().get(model.getDataStorage().indexOf(commandParts[1])).get(Integer.parseInt(commandParts[2])).setValue(commandParts[3]);
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
