package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Model part of WebContentCreator (by concept of ModelViewController)<br>
 * <br>
 * Created by Leo Köberlein on 09.07.2017
 */
public class WCCModel {

	/**
	 * Points to the programs workspace folder.
	 */
	public static final File programWorkspace = new File("");
	/**
	 * Points to the programs template folder.
	 */
	public static final File templateFolder = new File(programWorkspace.getAbsolutePath()+"\\template");
	/**
	 * Points to the programs export folder.
	 */
	public static final File exportFolder = new File(programWorkspace.getAbsolutePath()+"\\export");
	/**
	 * Points to the programs qr folder.
	 */
	public static final File qrFolder = new File(programWorkspace.getAbsolutePath()+"\\qr");
	/**
	 * Points to the programs readme file.
	 */
	public static final File readmeFile = new File(programWorkspace.getAbsolutePath()+"\\readme.html");

	
	
	/**
	 * Points to the programs settings file.
	 */
	private static final File settingsFile = new File(programWorkspace.getAbsolutePath() + "\\settings.txt");
	
	private static Settings settings = null;
	private static DataStorage dataStorage = null;
	
	/**
	 * Loads existing settings or creates new ones.
	 */
	public static void loadSettings() {
		settings = new Settings();
		if(settingsFile.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(settingsFile));
				for(String s; ((s = br.readLine()) != null) && (!s.equals("")); settings.setByCommand(s));
				br.close();
			} catch (Exception e) {e.printStackTrace();}
		}
	}
	
	/**
	 * Returns the current settings.
	 * 
	 * @return the current settings
	 */
	public static Settings getSettings() {
		return settings;
	}
	
	/**
	 * Sets the current settings.
	 * 
	 * @param s the new settings
	 */
	public static void setSettings(Settings s) {
		settings = s;
	}
	
	/**
	 * Saves settings in the settings file.
	 */
	public static void saveSettings() {
		try {
			if(!settingsFile.exists()) {
				settingsFile.createNewFile();
			}
			FileWriter fw = new FileWriter(settingsFile, false);
			fw.write(settings.toString());
			fw.flush();
			fw.close();
		} catch (Exception e) {e.printStackTrace();}
	}
	
	/**
	 * Creates a new data storage.
	 */
	public static void newDataStorage() {
		settings.setCurrentlyOpenedFile(null);
		dataStorage = new DataStorage();
	}
	
	/**
	 * Loads a data storage from the given file.
	 * 
	 * @param file the file to load from
	 * @throws Exception
	 */
	public static void loadDataStorage(File file) throws Exception {
		dataStorage = XMLHandler.loadDataStorage(file);
		settings.setCurrentlyOpenedFile(file.getAbsolutePath());
	}
	
	/**
	 * Returns the currently opened project.
	 * 
	 * @return the currently opened project
	 */
	public static DataStorage getDataStorage() {
		return dataStorage;
	}
	
	/**
	 * Saves the project's current state into the given file.
	 * 
	 * @param file the file to write into
	 * @throws Exception
	 */
	public static void saveDataStorage(File file) throws Exception {
		settings.setSaveLocation(file.getParent());
		settings.setCurrentlyOpenedFile(file.getAbsolutePath());
		dataStorage.setEditedSinceLastSave(false);
		XMLHandler.saveDataStorage(file, dataStorage);
	}
	
}
