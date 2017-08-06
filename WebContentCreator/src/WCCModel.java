import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Observable;
import java.util.Observer;

/*
 * Model part of WebContentCreator (by concept of ModelViewControl)
 * 
 * Created by Leo Köberlein on 09.07.2017
 */
public class WCCModel extends Observable implements Observer {

	private File programWorkspace, settingsFile, dataStorageFile;
	private Settings settings;
	private DataStorage dataStorage;
	
	public WCCModel() {
		try {
			//To be used when exportet into jar file
			//programWorkspace = new File(WCCModel.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			
			//Only used during development
			programWorkspace = new File("");
			
			settingsFile = new File(programWorkspace.getAbsolutePath() + "/settings.dat");
			dataStorageFile = new File(programWorkspace.getAbsolutePath() + "/dataStorage.dat");
			loadSettings();
			loadDataStorage();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Load existing settings or create new ones
	private void loadSettings() {
		if(!settingsFile.exists()) {
			settings = new Settings();
		} else {
			try {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(settingsFile));
				settings = (Settings) ois.readObject();
				settings.refresh();
				ois.close();
			} catch (Exception e) {e.printStackTrace();}
		}
	}
	
	public Settings getSettings() {
		return settings;
	}
	
	//Save settings in settings file
	public void saveSettings() {
		try {
			if(!settingsFile.exists())
				settingsFile.createNewFile();
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(settingsFile, false));
			oos.writeObject(settings);
			oos.flush();
			oos.close();
		} catch (Exception e) {e.printStackTrace();}
	
	}
	
	
	//Load project data or create new ones
	private void loadDataStorage() {
		if(!dataStorageFile.exists()) {
			dataStorage = new DataStorage();
			saveDataStorage();
		} else {
			try {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataStorageFile));
				dataStorage = (DataStorage) ois.readObject();
				ois.close();
				dataStorage.relink(this);
				update(this, this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public DataStorage getDataStorage() {
		return dataStorage;
	}
	
	//Save project data
	public void saveDataStorage() {
		try {
			if(!dataStorageFile.exists())
				dataStorageFile.createNewFile();
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataStorageFile, false));
			dataStorage.save();
			oos.writeObject(dataStorage);
			oos.flush();
			oos.close();
			dataStorage.relink(this);
			update(this, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void update(Observable o, Object arg) {
		setChanged();
		notifyObservers(arg);
	}

	
	//Export project
	public void export() {
		/*
		 * TODO: implement:
		 * use the data-storages export()-method to get the current version-hash
		 * use the pages' getVersion()-method to get the versions
		 * create html, css and js files
		 * create QR-Codes for the pages
		 */
	}
	
}
