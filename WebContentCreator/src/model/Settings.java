package model;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

/*
 * Represents the program's settings
 * 
 * Created by Leo Köberlein on 10.07.2017
 */
public class Settings {

	private static final char separator = '=';
	private static final String windowLocationXString = "WindowLocationX";
	private static final String windowLocationYString = "WindowLocationY";
	private Point windowLocation;
	private static final String windowSizeWidthString = "WindowWidth";
	private static final String windowSizeHeightString = "WindowHeight";
	private Dimension windowSize;
	private static final String maximizedString = "Maximized";
	private boolean maximized;
	private static final String dividerLocationString = "DividerLocation";
	private int dividerLocation;
	private static final String imageChooseLocationString = "ImageChooseLocation";
	private String imageChooseLocation;
	private static final String currentlyOpenedFileString = "CurrentlyOpenedFile";
	private String currentlyOpenedFile;
	private static final String saveLocationString = "SaveLocation";
	private String saveLocation;
	private static final String openLocationString = "OpenLocation";
	private String openLocation;
	private static final String qrCodeBaseUrlString = "QR-CodeBaseURL";
	private String qrCodeBaseUrl;

	public Settings () {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		windowLocation = new Point(toolkit.getScreenSize().width/4, toolkit.getScreenSize().height/4);
		windowSize = new Dimension(toolkit.getScreenSize().width/2, toolkit.getScreenSize().height/2);
		maximized = false;
		dividerLocation = (windowSize.width - 5)/2;
		imageChooseLocation = "";
		currentlyOpenedFile = "";
		saveLocation = "";
		openLocation = "";
		qrCodeBaseUrl = "";
	}
	
	public void restoreDefaultWindowState() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		windowLocation = new Point(toolkit.getScreenSize().width/4, toolkit.getScreenSize().height/4);
		windowSize = new Dimension(toolkit.getScreenSize().width/2, toolkit.getScreenSize().height/2);
		maximized = false;
	}
	
	public boolean setByCommand(String command) {
		int index = command.indexOf(separator);
		String name = command.substring(0, index);
		String value = command.substring(index+1);
		
		switch (name) {
		case windowLocationXString:
			windowLocation.setLocation(Integer.parseInt(value), windowLocation.getY());
			break;
		case windowLocationYString:
			windowLocation.setLocation(windowLocation.getX(), Integer.parseInt(value));
			break;
		case windowSizeWidthString:
			windowSize.setSize(Integer.parseInt(value), windowSize.getHeight());
			break;
		case windowSizeHeightString:
			windowSize.setSize(windowSize.getWidth(), Integer.parseInt(value));
			break;
		case maximizedString:
			maximized = Boolean.parseBoolean(value);
			break;
		case dividerLocationString:
			dividerLocation = Integer.parseInt(value);
			break;
		case imageChooseLocationString:
			imageChooseLocation = value;
			break;
		case currentlyOpenedFileString:
			currentlyOpenedFile = value;
			break;
		case saveLocationString:
			saveLocation = value;
			break;
		case openLocationString:
			openLocation = value;
			break;
		case qrCodeBaseUrlString:
			qrCodeBaseUrl = value;
			break;
		default:
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		String res = "";
		res += windowLocationXString+separator+windowLocation.x+"\n";
		res += windowLocationYString+separator+windowLocation.y+"\n";
		res += windowSizeWidthString+separator+windowSize.width+"\n";
		res += windowSizeHeightString+separator+windowSize.height+"\n";
		res += maximizedString+separator+maximized+"\n";
		res += dividerLocationString+separator+dividerLocation+"\n";
		res += imageChooseLocationString+separator+imageChooseLocation+"\n";
		res += currentlyOpenedFileString+separator+currentlyOpenedFile+"\n";
		res += saveLocationString+separator+saveLocation+"\n";
		res += openLocationString+separator+openLocation+"\n";
		res += qrCodeBaseUrlString+separator+qrCodeBaseUrl+"\n";
		return res;
	}


	public synchronized void setLocation(Point location) {
		windowLocation = location;
	}
	public synchronized Point getLocation() {
		return windowLocation;
	}

	
	public synchronized void setSize(Dimension size) {
		windowSize = size;
	}
	public synchronized Dimension getSize() {
		return windowSize;
	}

	
	public synchronized void setMaximized(boolean maximized) {
		this.maximized = maximized;
	}
	public synchronized boolean isMaximized() {
		return maximized;
	}

	
	public synchronized void setDividerLocation(int location) {
		dividerLocation = location;
	}
	public synchronized int getDividerLocation() {
		return dividerLocation;
	}

	
	public synchronized void setImageChooseLocation(String location) {
		imageChooseLocation = location;
	}
	public synchronized String getImageChooseLocation() {
		return imageChooseLocation;
	}

	
	public synchronized void setCurrentlyOpenedFile(String file) {
		currentlyOpenedFile = file;
	}
	public synchronized String getCurrentlyOpenedFile() {
		return currentlyOpenedFile;
	}

	
	public synchronized String getSaveLocation() {
		return saveLocation;
	}
	public synchronized void setSaveLocation(String saveLocation) {
		this.saveLocation = saveLocation;
	}
	
	
	public synchronized String getOpenLocation() {
		return openLocation;
	}
	public synchronized void setOpenLocation(String openLocation) {
		this.openLocation = openLocation;
	}

	public synchronized String getQRCodeBaseUrl() {
		return qrCodeBaseUrl;
	}
	public synchronized void setQRCodeBaseUrl(String baseUrl) {
		this.qrCodeBaseUrl = baseUrl;
	}
	
}
