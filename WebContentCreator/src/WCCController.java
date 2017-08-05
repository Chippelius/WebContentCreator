import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/*
 * Control part of WebContentCreator (by concept of ModelViewControl)
 * 
 * Created by Leo Köberlein on 09.07.2017
 */
public class WCCController implements ActionListener, WindowListener {
	
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
	public static final String pageNewHeader = "pageNewHeader";
	public static final String pageNewSubheader = "pageNewSubheader";
	public static final String pageNewText = "pageNewText";
	public static final String pageNewImage = "pageNewImage";
	public static final String pageDelete = "pageDelete";
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
			break;
		case fileExit:
			if(commandParts.length > 1) {
				shutdown(Boolean.parseBoolean(commandParts[1]));
			} else {
				shutdown(false);
			}
			break;
		case pageNewPage:
			break;
		case pageNewHeader:
			break;
		case pageNewSubheader:
			break;
		case pageNewText:
			break;
		case pageNewImage:
			break;
		case pageDelete:
			System.out.println(ae.getActionCommand());
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
			break;
		case helpCheckForUpdates:
			break;
		default: 
		}
	}


}
