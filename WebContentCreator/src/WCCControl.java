import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/*
 * Control part of WebContentCreator (by concept of ModelViewControl)
 * 
 * Created by Leo Köberlein on 09.07.2017
 */
public class WCCControl implements ActionListener, WindowListener {
	
	public static WCCModel model;
	public static WCCView view;
	public static WCCControl control;
	
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
	public static final String helpInfo = "helpInfo";
	public static final String helpCheckForUpdates = "helpCheckForUpdates";

	public static void main(String[] args) {
		control = new WCCControl("WebContentCreator");
	}
	
	public WCCControl(String title) {
		//Initialize components
		view = new WCCView(this, this);
		model = new WCCModel();
		
		//Load existing settings or create new ones and apply them
		model.loadSettings();
		//Load project data
		model.loadDataStorage();
		
		view.setVisible(true);
	}

	@Override
	public void windowActivated(WindowEvent arg0) {}
	@Override
	public void windowClosed(WindowEvent arg0) {}
	@Override
	public void windowClosing(WindowEvent arg0) {
		System.exit(0);
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
		switch(ae.getActionCommand().split(":")[0]) {
		/*
		 * Optional functionality for later:
		 * case fileNew:
		 * 	break;
		 * case fileOpen:
		 * 	break;
		 */
		case fileSave:
			break;
		/*
		 * Optional functionality for later:
		 * case fileSaveAs:
		 * 	break;
		 */
		case fileExport:
			break;
		case fileExit:
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
			break;
		case helpInfo:
			break;
		case helpCheckForUpdates:
			break;
		default: 
		}
	}

}
