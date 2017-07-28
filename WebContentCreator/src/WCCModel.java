import java.io.File;
import java.net.URISyntaxException;
import java.util.Observable;

/*
 * Model part of WebContentCreator (by concept of ModelViewControl)
 * 
 * Created by Leo Köberlein on 09.07.2017
 */
public class WCCModel implements SerializableObserver {

	private static final long serialVersionUID = 1L;
	
	private File programWorkspace;
	private SettingsInterface settings;
	private DataStorage dataStorage;
	
	public WCCModel() {
		try {
			programWorkspace = new File(WCCModel.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		} catch (URISyntaxException e) {e.printStackTrace();}
		loadSettings();
		loadDataStorage();
	}

	//Load existing settings or create new ones
	private void loadSettings() {
		//TODO: implement correctly
		settings = new Settings();
	}
	
	public SettingsInterface getSettings() {
		return settings;
	}
	
	//Save settings in settings file
	public void saveSettings() {
		//TODO: implement
	}
	
	
	//Load project data or create new ones
	private void loadDataStorage() {
		//TODO: implement correctly
		dataStorage = new DataStorage(this);
	}
	
	public DataStorage getDataStorage() {
		return dataStorage;
	}
	
	//Save project data
	public void saveDataStorage() {
		//TODO: implement
	}

	@Override
	public void update(Observable o, Object arg) {
		
	}
	
}
