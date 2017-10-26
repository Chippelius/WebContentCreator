package base;

import java.awt.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import data.*;
import gui.*;

/*
 * View part of WebContentCreator (by concept of ModelViewController)
 * 
 * Created by Leo Köberlein on 09.07.2017
 */
public class WCCView {

	//View configs
	private static final String title = "Web Content Creator";
	public static final Color backgroundColor = new Color(255, 255, 255);
	public static final Color hoverColor = new Color(235, 235, 235);
	public static final Color selectedColor = new Color(210, 210, 210);
	public static final Color transparentColor = new Color(0, 0, 0, 0);

	//Runtime variables
	private static WCCWindow f;
	private static Observer modelObserver;
	private static ArrayList<PageListItem> pageListItems;
	private static Page selectedPage;
	private static ArrayList<ElementListItem> elementListItems;
	private static Element selectedElement;


	public static void init() {
		//initiate variables
		pageListItems = new ArrayList<>();
		selectedPage = null;
		elementListItems = new ArrayList<>();
		selectedElement = null;
		modelObserver = new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				WCCView.update(o, arg);
			}
		};
		WCCModel.link(modelObserver);

		//Create window
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {e.printStackTrace();}
		f = new WCCWindow(title);

		//Apply settings to window and content
		applySettings();
		
		f.setVisible(true);

		//Make data visible for first time
		update(WCCModel.getDataStorage(), WCCModel.getDataStorage());
	}

	public static void setSelectedPage(Page page) {
		selectedPage = page;
		//set everything unselected
		for(PageListItem item : pageListItems) {
			item.setSelected(false);
		}
		//update element-list
		setSelectedElement(null);
		elementListItems = new ArrayList<>();
		f.clearElementList();
		if(page != null) {
			//set current page selected
			pageListItems.get(WCCModel.getDataStorage().indexOf(page)).setSelected(true);
			
			for(int i=0; i<page.size(); ++i) {
				ElementListItem item = new ElementListItem(page.get(i));
				elementListItems.add(item);
				f.addElementListItem(item);
			}
			//update page-dependent actions
			Actions.enablePageDependentActions(true);
		} else {
			//update page-dependent actions
			Actions.enablePageDependentActions(false);
		}
	}

	public static Page getSelectedPage() {
		return selectedPage;
	}

	public static void setSelectedElement(Element element) {
		selectedElement = element;
		//set everything unselected
		for(ElementListItem item : elementListItems) {
			item.setSelected(false);
		}
		if(element != null) {
			//set current element selected
			elementListItems.get(getSelectedPage().indexOf(element)).setSelected(true);
			//update element-dependent actions
			Actions.enableElementDependentActions(true);
		} else {
			//update element-dependent actions
			Actions.enableElementDependentActions(false);
		}
	}

	public static Element getSelectedElement() {
		return selectedElement;
	}

	public static void update(Observable o, Object arg) {
		if(WCCModel.getDataStorage().isEditedSinceLastSave()) {
			f.setTitle("*" + title);
		} else  {
			f.setTitle(title);
		}
		fetchSettings();
		if(arg instanceof Page) {
			pageListItems = new ArrayList<>();
			f.clearPageList();
			for(Page p : WCCModel.getDataStorage()) {
				PageListItem item = new PageListItem(p);
				pageListItems.add(item);
				f.addPageListItem(item);
			}
			setSelectedPage((Page) arg);
		} else if(arg instanceof Element) {
			setSelectedPage(getSelectedPage());
			setSelectedElement((Element) arg);
		} else {
			pageListItems = new ArrayList<>();
			f.clearPageList();
			for(Page p : WCCModel.getDataStorage()) {
				PageListItem item = new PageListItem(p);
				pageListItems.add(item);
				f.addPageListItem(item);
			}
			setSelectedPage(null);
		}
		applySettings();
	}

	public static void applySettings() {
		f.setLocation(WCCModel.getSettings().getLocation());
		f.setSize(WCCModel.getSettings().getSize());
		if(WCCModel.getSettings().isFullscreen()) {
			f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		} else { 
			f.setExtendedState(JFrame.NORMAL);
		}
		f.setDividerLocation(WCCModel.getSettings().getDividerLocation());
	}

	public static void fetchSettings() {
		WCCModel.getSettings().setLocation(f.getLocation());
		WCCModel.getSettings().setSize(f.getSize());
		WCCModel.getSettings().setFullscreen(f.getExtendedState() == JFrame.MAXIMIZED_BOTH);
		WCCModel.getSettings().setDividerLocation(f.getDividerLocation());
	}
	
	public static Dimension getMainPanelSize() {
		return f.getMainPanelSize();
	}

	public void setVisible(boolean b) {
		f.setVisible(b);
	}

	public static String requestExportDestination() {
		// TODO Auto-generated method stub
		return null;
	}

	public static int requestSaveBeforeExit() {
		return JOptionPane.showConfirmDialog(f, "Es gibt nicht gespeicherte Änderungen. \nVor dem Schließen speichern?");
	}

	public static String[] requestNewPageData(String oldFilename, String oldName) {
		JPanel myPanel = new JPanel(new GridLayout(0, 2));
		myPanel.add(new JLabel("Name:"));
		JTextField nameField = new JTextField(oldName, 20);
		nameField.addAncestorListener(createFocusListener());
		myPanel.add(nameField);
		myPanel.add(new JLabel("Dateiname:"));
		JTextField filenameField = new JTextField(oldFilename, 20);
		myPanel.add(filenameField);

		while(true) {
			int result = JOptionPane.showConfirmDialog(f, myPanel, "Bitte einen Dateinamen und einen Namen für die Seite angeben", JOptionPane.OK_CANCEL_OPTION);
			if(result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION)
				return null;
			if(WCCModel.getDataStorage().isValidFilename(filenameField.getText())) {
				return new String[] {filenameField.getText(), nameField.getText()};
			} else {
				WCCView.showIllegalFilenameWarning(filenameField.getText());
			}
		}
	}

	public static boolean requestDeletePage() {
		int result = JOptionPane.showConfirmDialog(f, "Sind Sie sicher, dass sie die Seite '"+selectedPage.getFilename()+"' löschen wollen?", "Seite löschen?", JOptionPane.YES_NO_OPTION);
		return result == JOptionPane.OK_OPTION;
	}

	public static String requestNewElementData(String type, String oldValue) {
		switch (type) {
		case Element.HEADER:
			return  requestNewHeaderData(oldValue);
		case Element.SUBHEADER:
			return requestNewSubheaderData(oldValue);
		case Element.TEXT:
			return requestNewTextData(oldValue);
		case Element.IMAGE:
			return requestNewImageData(oldValue);
		default:
			return null;
		}
	}

	public static String requestNewHeaderData(String oldValue) {
		JPanel myPanel = new JPanel(new GridLayout(0, 1));
		myPanel.add(new JLabel("Bitte geben Sie die neue Überschrift an:"));
		JTextField headerField = new JTextField(oldValue, 30);
		headerField.addAncestorListener(createFocusListener());
		myPanel.add(headerField);
		int result = JOptionPane.showConfirmDialog(f, myPanel, "Neue Daten für Element", JOptionPane.OK_CANCEL_OPTION);
		if(result == JOptionPane.OK_OPTION) {
			return headerField.getText();
		} else {
			return null;
		}
	}

	public static String requestNewSubheaderData(String oldValue) {
		JPanel myPanel = new JPanel(new GridLayout(0, 1));
		myPanel.add(new JLabel("Bitte geben Sie die neue Unterüberschrift an:"));
		JTextField subheaderField = new JTextField(oldValue, 30);
		subheaderField.addAncestorListener(createFocusListener());
		myPanel.add(subheaderField);
		int result = JOptionPane.showConfirmDialog(f, myPanel, "Neue Daten für Element", JOptionPane.OK_CANCEL_OPTION);
		if(result == JOptionPane.OK_OPTION) {
			return subheaderField.getText();
		} else {
			return null;
		}
	}

	public static String requestNewTextData(String oldValue) {
		JPanel myPanel = new JPanel(new BorderLayout());
		JLabel descriptionLabel = new JLabel("Bitte geben Sie den neuen Textinhalt an:");
		myPanel.add(descriptionLabel, BorderLayout.NORTH);
		JTextArea textarea = new JTextArea(oldValue, 10, 40);
		textarea.setLineWrap(true);
		textarea.setWrapStyleWord(true);
		textarea.addAncestorListener(createFocusListener());
		myPanel.add(new JScrollPane(textarea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
		int result = JOptionPane.showConfirmDialog(f, myPanel, "Neue Daten für Element", JOptionPane.OK_CANCEL_OPTION);
		if(result == JOptionPane.OK_OPTION) {
			return textarea.getText();
		} else {
			return null;
		}
	}

	public static String requestNewImageData(String oldValue) {
		JPanel myPanel = new JPanel(new BorderLayout());
		JLabel descriptionLabel = new JLabel("Bitte geben Sie das neue Bild an:", JLabel.LEFT);
		myPanel.add(descriptionLabel, BorderLayout.NORTH);
		JFileChooser filechooser = new JFileChooser(oldValue);
		filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		filechooser.setControlButtonsAreShown(false);
		filechooser.setDialogType(JFileChooser.OPEN_DIALOG);
		filechooser.setMultiSelectionEnabled(false);
		myPanel.add(filechooser, BorderLayout.CENTER);
		while(true) {
			int result = JOptionPane.showConfirmDialog(f, myPanel, "Neues Bild", JOptionPane.OK_CANCEL_OPTION);
			if(result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) {
				return null;
			}
			if(filechooser.getSelectedFile() != null && filechooser.getSelectedFile().exists()) {
				return filechooser.getSelectedFile().getAbsolutePath();
			} else {
				showError("Die ausgewählte Datei existiert nicht!", "Datei existiert nicht");
			}
		}
	}

	public static boolean requestDeleteElement() {
		//TODO
		int result = JOptionPane.showConfirmDialog(f, "Sind Sie sicher, dass Sie Element "+getSelectedPage().indexOf(getSelectedElement())+" \nder Seite '"+getSelectedPage().getFilename()+"' löschen wollen?", "Element löschen?", JOptionPane.YES_NO_OPTION);
		return result == JOptionPane.OK_OPTION;
	}

	public static void showMessage(String message, String title) {
		//TODO
		JOptionPane.showMessageDialog(f, message, title, JOptionPane.INFORMATION_MESSAGE);
	}

	public static void showWarning(String message, String title) {
		//TODO
		JOptionPane.showMessageDialog(f, message, title, JOptionPane.WARNING_MESSAGE);
	}

	public static void showError(String message, String title) {
		//TODO
		JOptionPane.showMessageDialog(f, message, title, JOptionPane.ERROR_MESSAGE);
	}

	public static void showIllegalFilenameWarning(String filename) {
		JOptionPane.showMessageDialog(f, "Der Dateiname \""+filename+"\" ist ungültig oder schon vergeben! \n"
				+ "Bitte suchen Sie einen anderen aus. \n"
				+ "Ein Dateiname darf keine Umlaute oder Sonderzeichen (außer Punkten) enthalten.", 
				"Dateiname ungültig", JOptionPane.WARNING_MESSAGE);
	}
	
	private static AncestorListener createFocusListener() {
		return new AncestorListener() {
			@Override public void ancestorRemoved(AncestorEvent event) {}
			@Override public void ancestorMoved(AncestorEvent event) {}
			@Override
			public void ancestorAdded(AncestorEvent event) {
				JComponent component = event.getComponent();
				component.requestFocusInWindow();
				component.removeAncestorListener( this );
			}
		};
	}

}
