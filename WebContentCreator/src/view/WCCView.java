package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import contoller.WCCController;
import model.Settings;

/**
 * View part of WebContentCreator (by concept of ModelViewController) <br>
 * <br>
 * Created by Leo Köberlein on 09.07.2017
 * 
 * @author Leo Köberlein
 * @see {@link model.WCCModel}, {@link contoller.WCCController}
 */
public class WCCView {

	/**
	 * Return value from class method if OK is chosen.
	 */
	public static final int OK_OPTION = JOptionPane.OK_OPTION;
	/**
	 * Return value from class method if YES is chosen.
	 */
	public static final int YES_OPTION = JOptionPane.YES_OPTION;
	/**
	 * Return value from class method if NO is chosen.
	 */
	public static final int NO_OPTION = JOptionPane.NO_OPTION;
	/**
	 * Return value from class method if CANCEL is chosen.
	 */
	public static final int CANCEL_OPTION = JOptionPane.CANCEL_OPTION;

	/**
	 * The background color used for most components.
	 */
	public static final Color backgroundColor = new Color(255, 255, 255);
	/**
	 * The color that the background of an hover-enabled element changes to when the cursor hovers over it. 
	 */
	public static final Color hoverColor = new Color(235, 235, 235);
	/**
	 * The background color of an element that is selected.
	 */
	public static final Color selectedColor = new Color(210, 210, 210);
	/**
	 * A transparent color. Duh.
	 */
	public static final Color transparentColor = new Color(0, 0, 0, 0);

	private static JFrame frame;
	private static JSplitPane splitPane;
	private static JPanel pageListContainer, pageList, elementListContainer, elementList;
	private static ArrayList<WCCListItem> pages, elements;

	/**
	 * Initiates the view: <br>
	 *  - sets the look and feel <br>
	 *  - initializes the main window <br>
	 *  - configures the main window
	 */
	public static void init() {
		prepareActions();
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {e.printStackTrace();}
		frame = new JFrame(Language.title);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowListener() {
			@Override public void windowOpened(WindowEvent e) {}
			@Override public void windowIconified(WindowEvent e) {}
			@Override public void windowDeiconified(WindowEvent e) {}
			@Override public void windowDeactivated(WindowEvent e) {}
			@Override public void windowClosing(WindowEvent e) {
				WCCController.fileExit.actionPerformed(null);
			}
			@Override public void windowClosed(WindowEvent e) {}
			@Override public void windowActivated(WindowEvent e) {}
		});
		frame.setBackground(backgroundColor);
		frame.setJMenuBar(new WCCMenubar());
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(new WCCToolbar(), BorderLayout.NORTH);
		frame.getContentPane().add(createMainPanel(), BorderLayout.CENTER);
	}

	/**
	 * Prepares the controller's actions. (Adds language-specific texts and icons.) 
	 * 
	 * @see contoller.WCCController
	 */
	private static void prepareActions() {
		WCCController.fileNew.putValue(Action.NAME, Language.fileNewText);
		WCCController.fileNew.putValue(Action.LARGE_ICON_KEY, Icons.plusIcon);
		WCCController.fileOpen.putValue(Action.NAME, Language.fileOpenText);
		WCCController.fileOpen.putValue(Action.LARGE_ICON_KEY, Icons.openIcon);
		WCCController.fileSave.putValue(Action.NAME, Language.fileSaveText);
		WCCController.fileSave.putValue(Action.LARGE_ICON_KEY, Icons.saveIcon);
		WCCController.fileSaveAs.putValue(Action.NAME, Language.fileSaveAsText);
		WCCController.fileSaveAs.putValue(Action.LARGE_ICON_KEY, Icons.saveAsIcon);
		WCCController.fileExport.putValue(Action.NAME, Language.fileExportText);
		WCCController.fileExport.putValue(Action.LARGE_ICON_KEY, Icons.exportIcon);
		WCCController.fileGenerateQRCodes.putValue(Action.NAME, Language.fileGenerateQRCodesText);
		WCCController.fileGenerateQRCodes.putValue(Action.LARGE_ICON_KEY, Icons.qrIcon);
		WCCController.fileExit.putValue(Action.NAME, Language.fileExitText);
		WCCController.pageNew.putValue(Action.NAME, Language.pageNewText);
		WCCController.pageNew.putValue(Action.LARGE_ICON_KEY, Icons.pageNewIcon);
		WCCController.pageChangeData.putValue(Action.NAME, Language.pageChangeDataText);
		WCCController.pageMoveTop.putValue(Action.NAME, Language.pageMoveTopText);
		WCCController.pageMoveTop.putValue(Action.LARGE_ICON_KEY, Icons.moveTopIcon);
		WCCController.pageMoveBottom.putValue(Action.NAME, Language.pageMoveBottomText);
		WCCController.pageMoveBottom.putValue(Action.LARGE_ICON_KEY, Icons.moveBottomIcon);
		WCCController.pageMoveUp.putValue(Action.NAME, Language.pageMoveUpText);
		WCCController.pageMoveUp.putValue(Action.LARGE_ICON_KEY, Icons.moveUpIcon);
		WCCController.pageMoveDown.putValue(Action.NAME, Language.pageMoveDownText);
		WCCController.pageMoveDown.putValue(Action.LARGE_ICON_KEY, Icons.moveDownIcon);
		WCCController.pageDelete.putValue(Action.NAME, Language.pageDeleteText);
		WCCController.pageDelete.putValue(Action.LARGE_ICON_KEY, Icons.pageDeleteIcon);
		WCCController.elementNewHeader.putValue(Action.NAME, Language.elementNewHeaderText);
		WCCController.elementNewHeader.putValue(Action.LARGE_ICON_KEY, Icons.headerIcon);
		WCCController.elementNewSubheader.putValue(Action.NAME, Language.elementNewSubheaderText);
		WCCController.elementNewSubheader.putValue(Action.LARGE_ICON_KEY, Icons.subheaderIcon);
		WCCController.elementNewText.putValue(Action.NAME, Language.elementNewTextText);
		WCCController.elementNewText.putValue(Action.LARGE_ICON_KEY, Icons.textIcon);
		WCCController.elementNewImage.putValue(Action.NAME, Language.elementNewImageText);
		WCCController.elementNewImage.putValue(Action.LARGE_ICON_KEY, Icons.imageIcon);
		WCCController.elementChangeToHeader.putValue(Action.NAME, Language.elementChangeToHeaderText);
		WCCController.elementChangeToSubheader.putValue(Action.NAME, Language.elementChangeToSubheaderText);
		WCCController.elementChangeToText.putValue(Action.NAME, Language.elementChangeToTextText);
		WCCController.elementChangeToImage.putValue(Action.NAME, Language.elementChangeToImageText);
		WCCController.elementChangeValue.putValue(Action.NAME, Language.elementChangeValueText);
		WCCController.elementMoveTop.putValue(Action.NAME, Language.elementMoveTopText);
		WCCController.elementMoveTop.putValue(Action.LARGE_ICON_KEY, Icons.moveTopIcon);
		WCCController.elementMoveBottom.putValue(Action.NAME, Language.elementMoveBottomText);
		WCCController.elementMoveBottom.putValue(Action.LARGE_ICON_KEY, Icons.moveBottomIcon);
		WCCController.elementMoveUp.putValue(Action.NAME, Language.elementMoveUpText);
		WCCController.elementMoveUp.putValue(Action.LARGE_ICON_KEY, Icons.moveUpIcon);
		WCCController.elementMoveDown.putValue(Action.NAME, Language.elementMoveDownText);
		WCCController.elementMoveDown.putValue(Action.LARGE_ICON_KEY, Icons.moveDownIcon);
		WCCController.elementDelete.putValue(Action.NAME, Language.elementDeleteText);
		WCCController.windowToggleMaximized.putValue(Action.NAME, Language.windowToggleMaximizedText);
		WCCController.windowRestoreDefaultState.putValue(Action.NAME, Language.windowRestoreDefaultState);
		WCCController.windowCenterDivider.putValue(Action.NAME, Language.windowCenterDividerText);
		WCCController.helpInfo.putValue(Action.NAME, Language.helpInfoText);
		WCCController.helpCheckForUpdates.putValue(Action.NAME, Language.helpCheckForUpdatesText);
	}

	/**
	 * Creates the main window's main panel.
	 * 
	 * @return the main window's main panel
	 */
	private static JSplitPane createMainPanel() {
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setBackground(backgroundColor);
		splitPane.setContinuousLayout(true);
		pageListContainer = new WCCListContainer(new BorderLayout());
		pageListContainer.setBackground(backgroundColor);
		pageListContainer.add(new JLabel(), BorderLayout.CENTER);
		JScrollPane scrollpane = new JScrollPane(pageListContainer, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollpane.getVerticalScrollBar().setUnitIncrement(50);
		scrollpane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener(){
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				frame.revalidate();
				frame.repaint();
			}
		});
		splitPane.setLeftComponent(scrollpane);
		clearPageList();
		elementListContainer = new WCCListContainer(new BorderLayout());
		elementListContainer.setBackground(backgroundColor);
		elementListContainer.add(new JLabel(), BorderLayout.CENTER);
		JScrollPane scrollpane2 = new JScrollPane(elementListContainer, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollpane2.getVerticalScrollBar().setUnitIncrement(50);
		scrollpane2.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener(){
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				frame.revalidate();
				frame.repaint();
			}
		});
		splitPane.setRightComponent(scrollpane2);
		clearElementList();
		return splitPane;
	}

	/**
	 * Shows or hides this Window depending on the value of parameter b. 
	 * 
	 * @param b if true, makes the Window visible, otherwise hides the Window.
	 */
	public static void setVisible(boolean b) {
		frame.setVisible(b);
	}

	/**
	 * Applies the provided settings to the main window.
	 * 
	 * @param s
	 */
	public static void applySettings(Settings s) {
		if(s.isMaximized()) {
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		} else {
			frame.setExtendedState(JFrame.NORMAL);
			frame.setLocation(s.getLocation());
			frame.setSize(s.getSize());
		}
		splitPane.setDividerLocation(s.getDividerLocation());
	}

	/**
	 * Fetches the main window's settings and writes them into the given reference.
	 * 
	 * @param s the reference to write the settings into
	 */
	public static void fetchSettings(Settings s) {
		s.setLocation(frame.getLocation());
		s.setSize(frame.getSize());
		s.setMaximized((frame.getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH);
		s.setDividerLocation(splitPane.getDividerLocation());
	}

	public static int requestSaveBeforeExit() {
		return JOptionPane.showConfirmDialog(frame, "Es gibt nicht gespeicherte Änderungen. \nVor dem Schließen speichern?");
	}

	public static File requestFileToOpen(String previousLocation) {
		JFileChooser jfc = new JFileChooser(previousLocation);
		jfc.setFileFilter(new FileNameExtensionFilter("xml files (*.xml)", "xml"));
		jfc.setMultiSelectionEnabled(false);
		jfc.showOpenDialog(frame);
		return jfc.getSelectedFile();
	}

	public static File requestFileToSave(String previousLocation) {
		JFileChooser jfc = new JFileChooser(previousLocation);
		jfc.setFileFilter(new FileNameExtensionFilter("xml files (*.xml)", "xml"));
		jfc.setMultiSelectionEnabled(false);
		jfc.showSaveDialog(frame);
		return jfc.getSelectedFile();
	}

	public static boolean confirmFileOverride(File fileToSave) {
		return JOptionPane.showConfirmDialog(frame, "Die Datei "+fileToSave.getAbsolutePath()+" existiert bereits.\nSoll sie überschrieben werden?", 
				"Datei überschreiben?", JOptionPane.YES_NO_OPTION)==YES_OPTION;
	}

	public static String requestQRBaseUrl(String previousURL) {
		return JOptionPane.showInputDialog(frame, "Basis-URL für die QR-Codes angeben:", previousURL);
	}

	public static void showInformationMessage(String message) {
		JOptionPane.showMessageDialog(frame, message, "Info", JOptionPane.INFORMATION_MESSAGE);
	}

	public static void showErrorMessage(String message) {
		JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
	}





	public static void clearPageList() {
		if(pageList!=null)
			pageListContainer.remove(pageList);
		pages = new ArrayList<>();
		pageList = new JPanel();
		pageList.setBackground(backgroundColor);
		pageList.setLayout(new BoxLayout(pageList, BoxLayout.Y_AXIS));
		pageListContainer.add(pageList, BorderLayout.NORTH);
		frame.revalidate();
		frame.repaint();
	}

	public static void addPageListItem(String filename, String name) {
		WCCListItem tmp = new WCCListItem(pages.size(), filename, name, true);
		pageList.add(tmp);
		pages.add(tmp);
		frame.revalidate();
		frame.repaint();
	}

	public static void setSelectedPage(int selectedPage) {
		for(WCCListItem item : pages) {
			item.setSelected(false);
		}
		pages.get(selectedPage).setSelected(true);
		frame.revalidate();
		frame.repaint();
	}

	public static void clearElementList() {
		if(elementList!=null)
			elementListContainer.remove(elementList);
		elements = new ArrayList<>();
		elementList = new JPanel();
		elementList.setBackground(backgroundColor);
		elementList.setLayout(new BoxLayout(elementList, BoxLayout.Y_AXIS));
		elementListContainer.add(elementList, BorderLayout.NORTH);
		frame.revalidate();
		frame.repaint();
	}

	public static void addElementListItem(String type, String value) {
		WCCListItem tmp = new WCCListItem(elements.size(), type, value, false);
		elementList.add(tmp);
		elements.add(tmp);
		frame.revalidate();
		frame.repaint();
	}

	public static void setSelectedElement(int selectedElement) {
		for(WCCListItem item : elements) {
			item.setSelected(false);
		}
		elements.get(selectedElement).setSelected(true);
		frame.revalidate();
		frame.repaint();
	}





	public static String[] requestNewPageData(String tmpFilename, String tmpName) {
		JPanel myPanel = new JPanel(new GridLayout(0, 2));
		myPanel.add(new JLabel("Name:"));
		JTextField nameField = new JTextField(tmpName, 20);
		nameField.addAncestorListener(createFocusListener());
		myPanel.add(nameField);
		myPanel.add(new JLabel("Dateiname:"));
		JTextField filenameField = new JTextField(tmpFilename, 20);
		myPanel.add(filenameField);

		int result = JOptionPane.showConfirmDialog(frame, myPanel, "Bitte einen Dateinamen und einen Namen für die Seite angeben", JOptionPane.OK_CANCEL_OPTION);
		if(result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) {
			return null;
		} else {
			return new String[] {filenameField.getText(), nameField.getText()};
		}
	}

	public static boolean confirmDeletePage(String filename, String name) {
		return JOptionPane.showConfirmDialog(frame, "Sind Sie sicher, dass sie die Seite \""+name+"\" ("+filename+") "
				+ "und alle Inhalte löschen wollen?", "Seite löschen?", 
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
	}

	public static void showErrorInvalidFilename(String string) {
		JOptionPane.showMessageDialog(frame, "Der Dateiname '"+string+"' ist ungültig oder schon vergeben!\n"
				+ "Bitte suchen Sie einen anderen aus.\n\nEin Dateiname darf keine Umlaute oder Sonderzeichen (außer Punkten) enthalten.",
				"Dateiname ungültig", JOptionPane.WARNING_MESSAGE);
	}

	public static String requestNewHeaderData(String oldValue) {
		JPanel myPanel = new JPanel(new GridLayout(0, 1));
		myPanel.add(new JLabel("Bitte geben Sie die neue Überschrift an:"));
		JTextField headerField = new JTextField(oldValue, 30);
		headerField.addAncestorListener(createFocusListener());
		myPanel.add(headerField);
		int result = JOptionPane.showConfirmDialog(frame, myPanel, "Neue Daten für Element", JOptionPane.OK_CANCEL_OPTION);
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
		int result = JOptionPane.showConfirmDialog(frame, myPanel, "Neue Daten für Element", JOptionPane.OK_CANCEL_OPTION);
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
		if(JOptionPane.showConfirmDialog(frame, myPanel, "Neue Daten für Element", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
			return textarea.getText();
		} else {
			return null;
		}
	}

	public static String requestNewImageData(String oldValue) {
		JFileChooser filechooser = new JFileChooser(oldValue);
		filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		filechooser.setAccessory(new ImagePreview(filechooser));
		filechooser.setMultiSelectionEnabled(false);
		int i = filechooser.showOpenDialog(frame);
		if(i!=JFileChooser.APPROVE_OPTION||filechooser.getSelectedFile()==null) {
			return null;
		} else {
			return filechooser.getSelectedFile().getAbsolutePath();
		}
	}


	public static boolean confirmDeleteElement(String filename, String name, String value) {
		return JOptionPane.showConfirmDialog(frame, "Sind Sie sicher, dass Sie das Element mit dem Inhalt\n\""
				+value+"\"\nvon der Seite \""+name+"\" ("+filename+") löschen wollen?", "Element löschen?", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION;
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




	public static int getWidth() {
		return frame.getContentPane().getWidth();
	}

	public static void setSavedState(boolean editedSinceLastSave) {
		frame.setTitle((editedSinceLastSave?"*":"")+Language.title);
	}
}