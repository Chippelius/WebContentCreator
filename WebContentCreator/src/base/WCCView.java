package base;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

import data.Element;
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
	private static String selectedPage;
	private static int selectedElement;
	
	
	public static void init() {
		//initiate variables
		selectedPage = "";
		selectedElement = -1;
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
		
		//Make data visible for first time
		update(WCCModel.getDataStorage(), WCCModel.getDataStorage());
	}
	
	public static void setSelectedPage(String filename) {
		selectedPage = filename;
	}
	
	public static String getSelectedPage() {
		return selectedPage;
	}
	
	public static void setSelectedElement(int index) {
		selectedElement = index;
	}
	
	public static int getSelectedElement() {
		return selectedElement;
	}
	
	public static void update(Observable o, Object arg) {
		//TODO
		if(WCCModel.getDataStorage().isEditedSinceLastSave()) {
			f.setTitle("*" + title);
		} else  {
			f.setTitle(title);
		}
		fetchSettings();
		pageList = new JPanel();
		pageList.setLayout(new BoxLayout(pageList, BoxLayout.Y_AXIS));
		pageList.setBackground(backgroundColor);
		JScrollPane pagepane = new JScrollPane(pageList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pagepane.getVerticalScrollBar().setUnitIncrement(16);
		elementList = new JPanel();
		elementList.setLayout(new BoxLayout(elementList, BoxLayout.Y_AXIS));
		elementList.setBackground(backgroundColor);
		JScrollPane elementpane = new JScrollPane(elementList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		elementpane.getVerticalScrollBar().setUnitIncrement(16);
		mainPanel.setLeftComponent(pagepane);
		mainPanel.setRightComponent(elementpane);
		for(Page p : WCCModel.getDataStorage()) {
			pageList.add(createPageListItem(p));
		}
		if(arg instanceof Page) {
			selectPage(WCCModel.getDataStorage().indexOf(arg));
		} else {
			selectedPage = -1;
			for(Component c : pageDependentButtons) {
				c.setEnabled(false);
			}
		}
		applySettings();
	}
	
	public static void applySettings() {
		//TODO
		f.setLocation(WCCModel.getSettings().getLocation());
		f.setSize(WCCModel.getSettings().getSize());
		if(WCCModel.getSettings().isFullscreen()) {
			f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		} else { 
			f.setExtendedState(JFrame.NORMAL);
		}
		mainPanel.setDividerLocation(WCCModel.getSettings().getDividerLocation());
	}
	
	public static void fetchSettings() {
		//TODO
		WCCModel.getSettings().setLocation(f.getLocation());
		WCCModel.getSettings().setSize(f.getSize());
		WCCModel.getSettings().setFullscreen(f.getExtendedState() == JFrame.MAXIMIZED_BOTH);
		WCCModel.getSettings().setDividerLocation(mainPanel.getDividerLocation());
	}
	
	public void setVisible(boolean b) {
		f.setVisible(b);
	}
	
	public static String requestExportDestination() {
		// TODO Auto-generated method stub
		
	}

	public static int requestSaveBeforeExit() {
		//TODO
		switch(JOptionPane.showConfirmDialog(f, "Es gibt nicht gespeicherte Änderungen. \nVor dem Schließen speichern?")) {
		case JOptionPane.YES_OPTION:
			controller.actionPerformed(new ActionEvent(f, ActionEvent.ACTION_PERFORMED, WCCController.fileSave));
			controller.actionPerformed(new ActionEvent(f, ActionEvent.ACTION_PERFORMED, WCCController.fileExit));
			break;
		case JOptionPane.NO_OPTION:
			controller.actionPerformed(new ActionEvent(f, ActionEvent.ACTION_PERFORMED, WCCController.fileExit+":true"));
			break;
		default:
			//Do nothing
		}
	}

	public static String[] requestNewPageData() {
		//TODO
		JPanel myPanel = new JPanel(new GridLayout(0, 2));
		myPanel.add(new JLabel("Name:"));
		JTextField nameField = new JTextField(tmpPageName, 20);
		myPanel.add(nameField);
		myPanel.add(new JLabel("Dateiname:"));
		JTextField filenameField = new JTextField(tmpPageFilename, 20);
		myPanel.add(filenameField);
		
		int result = JOptionPane.showConfirmDialog(f, myPanel, "Bitte einen Dateinamen und einen Namen für die Seite angeben:", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			controller.actionPerformed(new ActionEvent(f, ActionEvent.ACTION_PERFORMED, WCCController.pageNewPage+":"+filenameField.getText()+":"+nameField.getText()));
		}
	}
	
	public static String[] requestChangePageData() {
		//TODO
		JPanel myPanel = new JPanel(new GridLayout(0, 2));
		myPanel.add(new JLabel("Name:"));
		JTextField nameField = new JTextField(oldName, 20);
		myPanel.add(nameField);
		myPanel.add(new JLabel("Dateiname:"));
		JTextField filenameField = new JTextField(oldFilename, 20);
		myPanel.add(filenameField);
		
		int result = JOptionPane.showConfirmDialog(f, myPanel, "Neuen Namen und Dateinamen angeben:", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			controller.actionPerformed(new ActionEvent(f, ActionEvent.ACTION_PERFORMED, WCCController.pageChangeData+":true:"+oldFilename+":"+oldName+":"+filenameField.getText()+":"+nameField.getText()));
		}
	}

	public static boolean requestDeletePage() {
		//TODO
		int result = JOptionPane.showConfirmDialog(f, "Sind Sie sicher, dass sie die Seite '"+selectedPage+"' löschen wollen?", "Seite löschen?", JOptionPane.YES_NO_OPTION);
		if(result == JOptionPane.YES_OPTION) {
			controller.actionPerformed(new ActionEvent(f, ActionEvent.ACTION_PERFORMED, WCCController.pageDelete+":true:"+selectedPage));
		}
	}
	
	public static String[] requestNewElementData(String type) {
		switch (type) {
		case Element.HEADER:
			return new String[] {Element.HEADER, requestNewHeaderData()};
		case Element.SUBHEADER:
			return new String[] {Element.SUBHEADER, requestNewSubheaderData()};
		case Element.TEXT:
			return new String[] {Element.TEXT, requestNewTextData()};
		case Element.IMAGE:
			return new String[] {Element.IMAGE, requestNewImageData()};
		default:
			return null;
		}
	}
	
	public static String requestNewHeaderData() {
		//TODO
		String result = JOptionPane.showInputDialog(f, "Neue Überschrift eingeben:", "Neue Überschrift", JOptionPane.QUESTION_MESSAGE);
		if(result != null && !result.equals("")) {
			controller.actionPerformed(new ActionEvent(f, ActionEvent.ACTION_PERFORMED, WCCController.pageNewHeader+":"+parentFilename+":"+result));
		}
	}

	public static String requestNewSubheaderData() {
		//TODO
		String result = JOptionPane.showInputDialog(f, "Neue Unterüberschrift eingeben:", "Neue Unterüberschrift", JOptionPane.QUESTION_MESSAGE);
		if(result != null && !result.equals("")) {
			controller.actionPerformed(new ActionEvent(f, ActionEvent.ACTION_PERFORMED, WCCController.pageNewSubheader+":"+parentFilename+":"+result));
		}
	}

	public static String requestNewTextData() {
		//TODO
		JPanel myPanel = new JPanel(new FlowLayout());
		JTextArea textarea = new JTextArea(10, 60);
		textarea.setLineWrap(true);
		textarea.setWrapStyleWord(true);
		myPanel.add(new JScrollPane(textarea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		int result = JOptionPane.showConfirmDialog(f, myPanel, "Neuer Textinhalt", JOptionPane.OK_CANCEL_OPTION);
		if(result == JOptionPane.OK_OPTION) {
			controller.actionPerformed(new ActionEvent(f, ActionEvent.ACTION_PERFORMED, WCCController.pageNewText+":"+parentFilename+":"+textarea.getText().replaceAll("\n", "<br>\n")));
		}
	}

	public static String requestNewImageData() {
		//TODO
		JPanel myPanel = new JPanel(new FlowLayout());
		JFileChooser filechooser = new JFileChooser(model.getSettings().getImageChooseLocation());
		filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		filechooser.setControlButtonsAreShown(false);
		filechooser.setDialogType(JFileChooser.OPEN_DIALOG);
		filechooser.setMultiSelectionEnabled(false);
		myPanel.add(filechooser);
		int result = JOptionPane.showConfirmDialog(f, myPanel, "Neues Bild", JOptionPane.OK_CANCEL_OPTION);
		if(result == JOptionPane.OK_OPTION) {
			if(filechooser.getSelectedFile() != null && filechooser.getSelectedFile().exists()) {
				model.getSettings().setImageChooseLocation((filechooser.getSelectedFile().isDirectory())?filechooser.getSelectedFile().getAbsolutePath():filechooser.getSelectedFile().getParent());
				controller.actionPerformed(new ActionEvent(f, ActionEvent.ACTION_PERFORMED, WCCController.pageNewImage+":"+parentFilename+":"+filechooser.getSelectedFile().getAbsolutePath()));
			} else {
				System.out.println("Datei existiert nicht");
				JOptionPane.showInternalMessageDialog(f, "Die ausgewählte Datei existiert nicht!", "Datei existiert nicht", JOptionPane.WARNING_MESSAGE);
				requestNewImageData(parentFilename);
			}
		}
	}

	public static String requestChangeElementData() {
		//TODO
		Page parent = model.getDataStorage().get(model.getDataStorage().indexOf(parentFilename));
		Element e = parent.get(elementIndex);
		int result;
		
		JPanel myPanel = new JPanel(new BorderLayout());
		myPanel.add(new JLabel("Neue Daten für Element angeben:"), BorderLayout.NORTH);
		JPanel centerPanel = new JPanel(new FlowLayout());
		myPanel.add(centerPanel, BorderLayout.CENTER);
		
		switch(e.getType()) {
		case Element.HEADER:
			JTextField headerField = new JTextField(e.getValue(), 30);
			centerPanel.add(headerField);
			result = JOptionPane.showConfirmDialog(f, myPanel, "Neue Daten für Element", JOptionPane.OK_CANCEL_OPTION);
			if(result == JOptionPane.OK_OPTION) {
				controller.actionPerformed(new ActionEvent(f, ActionEvent.ACTION_PERFORMED, WCCController.elementChangeData+":"+parentFilename+":"+elementIndex+":"+headerField.getText()));
			}
			break;
		case Element.SUBHEADER:
			JTextField subheaderField = new JTextField(e.getValue(), 30);
			centerPanel.add(subheaderField);
			result = JOptionPane.showConfirmDialog(f, myPanel, "Neue Daten für Element", JOptionPane.OK_CANCEL_OPTION);
			if(result == JOptionPane.OK_OPTION) {
				controller.actionPerformed(new ActionEvent(f, ActionEvent.ACTION_PERFORMED, WCCController.elementChangeData+":"+parentFilename+":"+elementIndex+":"+subheaderField.getText()));
			}
			break;
		case Element.TEXT:
			JTextArea textarea = new JTextArea(e.getValue(), 10, 60);
			textarea.setLineWrap(true);
			textarea.setWrapStyleWord(true);
			centerPanel.add(new JScrollPane(textarea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
			result = JOptionPane.showConfirmDialog(f, myPanel, "Neue Daten für Element", JOptionPane.OK_CANCEL_OPTION);
			if(result == JOptionPane.OK_OPTION) {
				controller.actionPerformed(new ActionEvent(f, ActionEvent.ACTION_PERFORMED, WCCController.elementChangeData+":"+parentFilename+":"+elementIndex+":"+textarea.getText().replaceAll("\n", "<br>\n")));
			}
			break;
		case Element.IMAGE:
			JFileChooser filechooser = new JFileChooser(model.getSettings().getImageChooseLocation());
			filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			filechooser.setControlButtonsAreShown(false);
			filechooser.setDialogType(JFileChooser.OPEN_DIALOG);
			filechooser.setMultiSelectionEnabled(false);
			centerPanel.add(filechooser);
			result = JOptionPane.showConfirmDialog(f, myPanel, "Neues Bild", JOptionPane.OK_CANCEL_OPTION);
			if(result == JOptionPane.OK_OPTION) {
				System.out.println("OK gewählt");
				if(filechooser.getSelectedFile() != null && filechooser.getSelectedFile().exists()) {
					System.out.println("Datei existiert: "+filechooser.getSelectedFile().toString());
					model.getSettings().setImageChooseLocation((filechooser.getSelectedFile().isDirectory())?filechooser.getSelectedFile().getAbsolutePath():filechooser.getSelectedFile().getParent());
					controller.actionPerformed(new ActionEvent(f, ActionEvent.ACTION_PERFORMED, WCCController.elementChangeData+":"+parentFilename+":"+elementIndex+":"+filechooser.getSelectedFile().getAbsolutePath()));
				} else {
					System.out.println("Datei existiert nicht");
					JOptionPane.showMessageDialog(f, "Die ausgewählte Datei existiert nicht!", "Datei existiert nicht", JOptionPane.WARNING_MESSAGE);
					requestNewImageData(parentFilename);
				}
			}
			break;
		}
	}

	public static boolean requestDeleteElement() {
		//TODO
		int result = JOptionPane.showConfirmDialog(f, "Sind Sie sicher, dass Sie Element "+elementIndex+" der Seite '"+filename+"' löschen wollen?", "Element löschen?", JOptionPane.YES_NO_OPTION);
		if(result == JOptionPane.YES_OPTION) {
			controller.actionPerformed(new ActionEvent(f, ActionEvent.ACTION_PERFORMED, WCCController.elementDelete+":true:"+filename+":"+elementIndex));
		}
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
				+ "Ein Dateiname muss eine Endung besitzen (z.B. '.html'), darf aber sonst keine Sonderzeichen enthalten", 
				"Dateiname ungültig", JOptionPane.WARNING_MESSAGE);
	}
	
}
