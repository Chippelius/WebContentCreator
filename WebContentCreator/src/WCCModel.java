import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Observer;

/*
 * Model part of WebContentCreator (by concept of ModelViewControl)
 * 
 * Created by Leo Köberlein on 09.07.2017
 */
public class WCCModel {

	private static File programWorkspace, settingsFile, dataStorageFile;
	private static Settings settings;
	private static DataStorage dataStorage;
	private static ArrayList<Observer> observers;
	
	public static void init() {
		try {
			//To be used when exportet into jar file
			//programWorkspace = new File(WCCModel.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			
			//Only used during development
			programWorkspace = new File("");
			
			settingsFile = new File(programWorkspace.getAbsolutePath() + "\\settings.dat");
			dataStorageFile = new File(programWorkspace.getAbsolutePath() + "\\dataStorage.dat");
		} catch (Exception e) {
			e.printStackTrace();
		}
		observers = new ArrayList<>();
		loadSettings();
		loadDataStorage();
	}
	
	//Load existing settings or create new ones
	private static void loadSettings() {
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
	
	public static Settings getSettings() {
		return settings;
	}
	
	//Save settings in settings file
	public static void saveSettings() {
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
	public static void loadDataStorage() {
		if(!dataStorageFile.exists()) {
			dataStorage = new DataStorage();
			saveDataStorage();
		} else {
			try {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataStorageFile));
				dataStorage = (DataStorage) ois.readObject();
				ois.close();
				for(Observer o : observers) {
					dataStorage.relink(o);
				}
				dataStorage.update(dataStorage, dataStorage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static DataStorage getDataStorage() {
		return dataStorage;
	}
	
	//Save project data
	public static void saveDataStorage() {
		try {
			if(!dataStorageFile.exists())
				dataStorageFile.createNewFile();
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataStorageFile, false));
			dataStorage.save();
			oos.writeObject(dataStorage);
			oos.flush();
			oos.close();
			for(Observer o : observers) {
				dataStorage.relink(o);
			}
			dataStorage.update(dataStorage, dataStorage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void link(Observer observer) {
		observers.add(observer);
		dataStorage.relink(observer);
		dataStorage.update(dataStorage, dataStorage);
	}
	
	
	//Export project
	public static void export(String location) {
		/*
		 * TODO: implement:
		 * use the data-storages export()-method to get the current version-hash
		 * use the pages' getVersion()-method to get the versions
		 * create html, css and js files
		 * create QR-Codes for the pages
		 */
		System.out.println("Export not yet implemented.");
		
		//(create and) clear directory
		File exportLocation = new File(location);
		if(!exportLocation.exists()) 
			exportLocation.mkdirs();
		
		
		//Versions-file
		File versionsFile = new File(location + "\\settings.dat");
		
		
		//index-file
		
		
		//page-files
		
		
		//css-files
		
		
		//js-files
		
		
		//images
		
	}

	public static String getReadmeText() {
		String res = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(programWorkspace.getAbsolutePath()+"\\readme.html"));
			for(String s=""; (s=br.readLine())!=null; res+=s);
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
}
