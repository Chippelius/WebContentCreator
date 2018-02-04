package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/*
 * Model part of WebContentCreator (by concept of ModelViewController)
 * 
 * Created by Leo Köberlein on 09.07.2017
 */
public class WCCModel {

	//Only used during development:
	public static final File programWorkspace = new File("");
	//For release use:
	//public static final File programWorkspace = new File(WCCModel.class.getProtectionDomain().getCodeSource().getLocation().toURI());
	public static final File templateFolder = new File(programWorkspace.getAbsolutePath()+"\\template");
	public static final File exportFolder = new File(programWorkspace.getAbsolutePath()+"\\export");
	public static final File qrFolder = new File(programWorkspace.getAbsolutePath()+"\\qr");
	
	private static final File settingsFile = new File(programWorkspace.getAbsolutePath() + "\\settings.txt");
	
	private static Settings settings = null;
	private static DataStorage dataStorage = null;
	
	//Load existing settings or create new ones
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
	
	public static Settings getSettings() {
		return settings;
	}
	
	public static void setSettings(Settings s) {
		settings = s;
	}
	
	//Save settings in settings file
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
	
	public static void newDataStorage() {
		settings.setCurrentlyOpenedFile(null);
		dataStorage = new DataStorage();
	}
	
	public static void loadDataStorage(File file) throws Exception {
		settings.setCurrentlyOpenedFile(file.getAbsolutePath());
		dataStorage = XMLHandler.loadDataStorage(file);
	}
	
	public static DataStorage getDataStorage() {
		return dataStorage;
	}
	
	//Save project data
	public static void saveDataStorage(File file) throws Exception {
		settings.setSaveLocation(file.getParent());
		settings.setCurrentlyOpenedFile(file.getAbsolutePath());
		dataStorage.setEditedSinceLastSave(false);
		XMLHandler.saveDataStorage(file, dataStorage);
	}
	
	public static File getReadmeFile() {
		return new File(programWorkspace.getAbsolutePath()+"\\readme.html");
	}
	
}
