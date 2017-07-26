import java.io.File;
import java.net.URISyntaxException;

/*
 * Model part of WebContentCreator (by concept of ModelViewControl)
 * 
 * Created by Leo Köberlein on 09.07.2017
 */
public class WCCModel {

	private File programWorkspace;
	private Settings settings;
	private DataStorage data;
	
	public WCCModel() {
		try {
			programWorkspace = new File(WCCControl.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		} catch (URISyntaxException e) {e.printStackTrace();}
		
	}

	//Load existing settings or create new ones and apply them
	public void loadSettings() {
		settings = new Settings();
		
		WCCControl.view.setLocation(settings.getLocation());
		WCCControl.view.setSize(settings.getSize());
		WCCControl.view.setFullscreen(settings.isFullscreen());
		WCCControl.view.setDividerLocation(settings.getDividerLocation());
	}
	
	//Save settings in settings file
	public void saveSettings() {
		settings.setLocation(WCCControl.view.getLocation());
		settings.setSize(WCCControl.view.getSize());
		settings.setFullscreen(WCCControl.view.isFullscreen());
		settings.setDividerLocation(WCCControl.view.getDividerLocation());
	}
	
	//Load project data
	public void loadDataStorage() {
		data = new DataStorage();
		
		WCCControl.view.setPages(data.getPages());
		WCCControl.view.setElements(data.getPages()[0].getElements());
	}
	
	public void shutdown() {
		saveSettings();
	}

}
