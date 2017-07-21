import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.net.URISyntaxException;

/*
 * Control part of WebContentCreator (by concept of ModelViewControl)
 * 
 * Created by Leo Köberlein on 09.07.2017
 */
public class WCCControl implements ActionListener, WindowListener{
	
	protected static WCCControl control;
	public static final String fileNew = "fileNew";
	public static final String fileOpen = "fileOpen";
	public static final String fileSave = "fileSave";
	public static final String fileSaveAs = "fileSaveAs";
	public static final String fileExit = "fileExit";
	public static final String pageNewPage = "pageNewPage";
	public static final String pageNewHeader = "pageNewHeader";
	public static final String pageNewSubheader = "pageNewSubheader";
	public static final String pageNewText = "pageNewText";
	public static final String pageNewImage = "pageNewImage";
	public static final String pageDelete = "pageDelete";
	public static final String helpInfo = "helpInfo";
	public static final String helpCheckForUpdates = "helpCheckForUpdates";
	
	protected WCCModel model;
	protected WCCView view;
	protected File programWorkspace;

	public static void main(String[] args) {
		control = new WCCControl("WebContentCreator");
	}
	
	public WCCControl(String title) {
		try {
			programWorkspace = new File(WCCControl.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		} catch (URISyntaxException e) {e.printStackTrace();}
		model = new WCCModel();
		view = new WCCView(title, this, this);
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
		switch(ae.getActionCommand()) {
		case fileNew:
			break;
		case fileOpen:
			break;
		case fileSave:
			break;
		case fileSaveAs:
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
		default: 
		}
	}

}
