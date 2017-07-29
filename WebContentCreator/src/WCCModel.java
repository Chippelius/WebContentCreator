import java.io.File;
import java.net.URISyntaxException;
import java.util.Observable;
import java.util.Observer;

/*
 * Model part of WebContentCreator (by concept of ModelViewControl)
 * 
 * Created by Leo Köberlein on 09.07.2017
 */
public class WCCModel extends Observable implements Observer {

	private File programWorkspace;
	private Settings settings;
	private DataStorage dataStorage;
	private Observer observer;
	
	public WCCModel() {
		try {
			programWorkspace = new File(WCCModel.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		} catch (URISyntaxException e) {e.printStackTrace();}
		loadSettings();
		loadDataStorage();
	}
	
	public void setObserver(Observer o) {
		observer = o;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		observer.update(o, arg);
	}
	
	//Load existing settings or create new ones
	private void loadSettings() {
		//TODO: implement correctly
		settings = new Settings();
	}
	
	public Settings getSettings() {
		return settings;
	}
	
	//Save settings in settings file
	public void saveSettings() {
		//TODO: implement
	}
	
	
	//Load project data or create new ones
	private void loadDataStorage() {
		//TODO: implement correctly (don't forget to call setObserver(this) at the end!)
		dataStorage = new DataStorage(this);
	}
	
	public DataStorage getDataStorage() {
		return dataStorage;
	}
	
	//Save project data
	public void saveDataStorage() {
		//TODO: implement (don't forget to call setObserver(this) at the end!)
		update(null, null);
	}

}
