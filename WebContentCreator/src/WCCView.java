import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

/*
 * View part of WebContentCreator (by concept of ModelViewController)
 * 
 * Created by Leo Köberlein on 09.07.2017
 */
public class WCCView implements Observer {
	
	private WCCModel model;
	private ActionListener actionListener;
	private JFrame f;
	private JSplitPane mainPanel;
	private JList<String> pageList;
	private JList<String> elementList;
	
	//Constructor to initiate needed variables and create the window
	public WCCView(WCCModel m, ActionListener actionListener, WindowListener windowListener) {
		this.model = m;
		this.model.setObserver(this);
		this.actionListener = actionListener;
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {e.printStackTrace();}
		
		//Create window
		f = new JFrame("Web Content Creator");
		f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		f.addWindowListener(windowListener);
		
		//Charge window with content
		f.getContentPane().setLayout(new BorderLayout());
		f.setJMenuBar(createMenuBar());
		f.getContentPane().add(createToolBar(), BorderLayout.NORTH);
		f.getContentPane().add(createMainPanel(), BorderLayout.CENTER);
		
		//Apply settings to window and content
		applySettings();
		
		//Make data visible for first time
		update(model.getDataStorage().getPages()[0], model.getDataStorage().getPages()[0].getFilename());
		SwingUtilities.updateComponentTreeUI(f);
	}
	
	//Method to create the window's menubar
	private JMenuBar createMenuBar() {
		JMenuBar menubar = new JMenuBar();
		JMenu menuFile = new JMenu("Datei  ");
			JMenu menuFileNew = new JMenu("Neu");
			menuFileNew.setIcon(new ImageIcon("icons/plusIcon.png"));
			/*
			 * Optional functionality for later:
			 * JMenuItem menuFileNewProject = new JMenuItem("Neues Projekt", new ImageIcon("icons/plusIcon.png"));
			 * menuFileNewProject.addActionListener(actionListener);
			 * menuFileNewProject.setActionCommand(WCCController.fileNew);
			 * menuFileNew.add(menuFileNewProject);
			 * menuFileNew.addSeparator();
			 */
			JMenuItem menuFileNewPage = new JMenuItem("Neue Seite", new ImageIcon("icons/newPageIcon.png"));
			menuFileNewPage.addActionListener(actionListener);
			menuFileNewPage.setActionCommand(WCCController.pageNewPage);
			menuFileNew.add(menuFileNewPage);
			menuFileNew.addSeparator();
			JMenuItem menuFileNewHeader = new JMenuItem("Neue Überschrift", new ImageIcon("icons/headerIcon.png"));
			menuFileNewHeader.addActionListener(actionListener);
			menuFileNewHeader.setActionCommand(WCCController.pageNewHeader);
			menuFileNew.add(menuFileNewHeader);
			JMenuItem menuFileNewSubheader = new JMenuItem("Neue Unterüberschrift", new ImageIcon("icons/subheaderIcon.png"));
			menuFileNewSubheader.addActionListener(actionListener);
			menuFileNewSubheader.setActionCommand(WCCController.pageNewSubheader);
			menuFileNew.add(menuFileNewSubheader);
			JMenuItem menuFileNewText = new JMenuItem("Neuer Textinhalt", new ImageIcon("icons/textIcon.png"));
			menuFileNewText.addActionListener(actionListener);
			menuFileNewText.setActionCommand(WCCController.pageNewText);
			menuFileNew.add(menuFileNewText);
			JMenuItem menuFileNewImage = new JMenuItem("Neues Bild", new ImageIcon("icons/imageIcon.png"));
			menuFileNewImage.addActionListener(actionListener);
			menuFileNewImage.setActionCommand(WCCController.pageNewImage);
			menuFileNew.add(menuFileNewImage);
			menuFile.add(menuFileNew);
			menuFile.addSeparator();
		/*
		 * Optional functionality for later:
		 * JMenuItem menuFileOpen = new JMenuItem("Öffnen", new ImageIcon("icons/openIcon.png"));
		 * menuFileOpen.addActionListener(actionListener);
		 * menuFileOpen.setActionCommand(WCCController.fileOpen);
		 * menuFile.add(menuFileOpen);
		 * menuFile.addSeparator();
		 */
		JMenuItem menuFileSave = new JMenuItem("Speichern", new ImageIcon("icons/saveIcon.png"));
		menuFileSave.addActionListener(actionListener);
		menuFileSave.setActionCommand(WCCController.fileSave);
		menuFile.add(menuFileSave);
		/*
		 * Optional functionality for later:
		 * JMenuItem menuFileSaveAs = new JMenuItem("Speichern unter", new ImageIcon("icons/saveIcon.png"));
		 * menuFileSaveAs.addActionListener(actionListener);
		 * menuFileSaveAs.setActionCommand(WCCController.fileSaveAs);
		 * menuFile.add(menuFileSaveAs);
		 */
		menuFile.addSeparator();
		JMenuItem menuFileExport = new JMenuItem("Exportieren", new ImageIcon("icons/exportIcon.png"));
		menuFileExport.addActionListener(actionListener);
		menuFileExport.setActionCommand(WCCController.fileExport);
		menuFile.add(menuFileExport);
		menuFile.addSeparator();
		JMenuItem menuFileExit = new JMenuItem("Schließen");
		menuFileExit.addActionListener(actionListener);
		menuFileExit.setActionCommand(WCCController.fileExit);
		menuFile.add(menuFileExit);
		menubar.add(menuFile);
		
		JMenu menuPage = new JMenu("Seite  ");
		JMenuItem menuPageNewPage = new JMenuItem("Neue Seite", new ImageIcon("icons/newPageIcon.png"));
		menuPageNewPage.addActionListener(actionListener);
		menuPageNewPage.setActionCommand(WCCController.pageNewPage);
		menuPage.add(menuPageNewPage);
		menuPage.addSeparator();
			JMenu menuPageNew = new JMenu("Neues Element");
			menuPageNew.setIcon(new ImageIcon("icons/plusIcon.png"));
			JMenuItem menuPageNewHeader = new JMenuItem("Neue Überschrift", new ImageIcon("icons/headerIcon.png"));
			menuPageNewHeader.addActionListener(actionListener);
			menuPageNewHeader.setActionCommand(WCCController.pageNewHeader);
			menuPageNew.add(menuPageNewHeader);
			JMenuItem menuPageNewSubheader = new JMenuItem("Neue Unterüberschrift", new ImageIcon("icons/subheaderIcon.png"));
			menuPageNewSubheader.addActionListener(actionListener);
			menuPageNewSubheader.setActionCommand(WCCController.pageNewSubheader);
			menuPageNew.add(menuPageNewSubheader);
			JMenuItem menuPageNewText = new JMenuItem("Neuer Textinhalt", new ImageIcon("icons/textIcon.png"));
			menuPageNewText.addActionListener(actionListener);
			menuPageNewText.setActionCommand(WCCController.pageNewText);
			menuPageNew.add(menuPageNewText);
			JMenuItem menuPageNewImage = new JMenuItem("Neues Bild", new ImageIcon("icons/imageIcon.png"));
			menuPageNewImage.addActionListener(actionListener);
			menuPageNewImage.setActionCommand(WCCController.pageNewImage);
			menuPageNew.add(menuPageNewImage);
			menuPage.add(menuPageNew);
		menuPage.addSeparator();
		JMenuItem menuPageDelete = new JMenuItem("Seite löschen", new ImageIcon("icons/deletePageIcon.png"));
		menuPageDelete.addActionListener(actionListener);
		menuPageDelete.setActionCommand(WCCController.pageDelete);
		menuPage.add(menuPageDelete);
		menubar.add(menuPage);
		
		JMenu menuWindow = new JMenu("Ansicht  ");
		JMenuItem menuWindowMaximized = new JMenuItem("Maximieren/Minimieren");
		menuWindowMaximized.addActionListener(actionListener);
		menuWindowMaximized.setActionCommand(WCCController.windowToggleMaximized);
		menuWindow.add(menuWindowMaximized);
		JMenuItem menuWindowCenterDivider = new JMenuItem("Trennstrich zentrieren");
		menuWindowCenterDivider.addActionListener(actionListener);
		menuWindowCenterDivider.setActionCommand(WCCController.windowCenterDivider);
		menuWindow.add(menuWindowCenterDivider);
		menubar.add(menuWindow);
		
		JMenu menuHelp = new JMenu("Hilfe  ");
		JMenuItem menuHelpInfo = new JMenuItem("Info");
		menuHelpInfo.addActionListener(actionListener);
		menuHelpInfo.setActionCommand(WCCController.helpInfo);
		menuHelp.add(menuHelpInfo);
		JMenuItem menuHelpCheckForUpdates = new JMenuItem("Nach Updates suchen");
		menuHelpCheckForUpdates.addActionListener(actionListener);
		menuHelpCheckForUpdates.setActionCommand(WCCController.helpCheckForUpdates);
		menuHelp.add(menuHelpCheckForUpdates);
		menubar.add(menuHelp);
		
		return menubar;
	}
	
	//Method to create the window's toolbar
	private JToolBar createToolBar () {
		JToolBar toolbar = new JToolBar("Toolbar");
		toolbar.setFloatable(true);
		//toolbar.setBorder(BorderFactory.createRaisedBevelBorder());
		JButton buttonNew = new JButton(new ImageIcon("icons/plusIcon.png"));
		buttonNew.setToolTipText("Neu...");
		buttonNew.setFocusable(false);
			JPopupMenu popupButtonNew = new JPopupMenu();
			JMenuItem menuFileNewPage = new JMenuItem("Neue Seite", new ImageIcon("icons/newPageIcon.png"));
			menuFileNewPage.addActionListener(actionListener);
			menuFileNewPage.setActionCommand(WCCController.pageNewPage);
			popupButtonNew.add(menuFileNewPage);
			popupButtonNew.addSeparator();
			JMenuItem menuFileNewHeader = new JMenuItem("Neue Überschrift", new ImageIcon("icons/headerIcon.png"));
			menuFileNewHeader.addActionListener(actionListener);
			menuFileNewHeader.setActionCommand(WCCController.pageNewHeader);
			popupButtonNew.add(menuFileNewHeader);
			JMenuItem menuFileNewSubheader = new JMenuItem("Neue Unterüberschrift", new ImageIcon("icons/subheaderIcon.png"));
			menuFileNewSubheader.addActionListener(actionListener);
			menuFileNewSubheader.setActionCommand(WCCController.pageNewSubheader);
			popupButtonNew.add(menuFileNewSubheader);
			JMenuItem menuFileNewText = new JMenuItem("Neuer Textinhalt", new ImageIcon("icons/textIcon.png"));
			menuFileNewText.addActionListener(actionListener);
			menuFileNewText.setActionCommand(WCCController.pageNewText);
			popupButtonNew.add(menuFileNewText);
			JMenuItem menuFileNewImage = new JMenuItem("Neues Bild", new ImageIcon("icons/imageIcon.png"));
			menuFileNewImage.addActionListener(actionListener);
			menuFileNewImage.setActionCommand(WCCController.pageNewImage);
			popupButtonNew.add(menuFileNewImage);
		buttonNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				popupButtonNew.show(buttonNew, 0, buttonNew.getHeight());
			}
		});
		toolbar.add(buttonNew);
		toolbar.addSeparator();
		
		JButton buttonSave = new JButton(new ImageIcon("icons/saveIcon.png"));
		buttonSave.addActionListener(actionListener);
		buttonSave.setActionCommand(WCCController.fileSave);
		buttonSave.setToolTipText("Speichern");
		buttonSave.setFocusable(false);
		toolbar.add(buttonSave);
		toolbar.addSeparator();
		
		JButton buttonExport = new JButton(new ImageIcon("icons/exportIcon.png"));
		buttonExport.addActionListener(actionListener);
		buttonExport.setActionCommand(WCCController.fileExport);
		buttonExport.setToolTipText("Exportieren");
		buttonExport.setFocusable(false);
		toolbar.add(buttonExport);
		toolbar.addSeparator();
		
		JButton buttonNewPage = new JButton(new ImageIcon("icons/newPageIcon.png"));
		buttonNewPage.addActionListener(actionListener);
		buttonNewPage.setActionCommand(WCCController.pageNewPage);
		buttonNewPage.setToolTipText("Neue Seite");
		buttonNewPage.setFocusable(false);
		toolbar.add(buttonNewPage);
		toolbar.addSeparator();
		JButton buttonNewHeader = new JButton(new ImageIcon("icons/headerIcon.png"));
		buttonNewHeader.addActionListener(actionListener);
		buttonNewHeader.setActionCommand(WCCController.pageNewHeader);
		buttonNewHeader.setToolTipText("Neue Überschrift");
		buttonNewHeader.setFocusable(false);
		toolbar.add(buttonNewHeader);
		JButton buttonNewSubheader = new JButton(new ImageIcon("icons/subheaderIcon.png"));
		buttonNewSubheader.addActionListener(actionListener);
		buttonNewSubheader.setActionCommand(WCCController.pageNewSubheader);
		buttonNewSubheader.setToolTipText("Neue Unterüberschrift");
		buttonNewSubheader.setFocusable(false);
		toolbar.add(buttonNewSubheader);
		JButton buttonNewText = new JButton(new ImageIcon("icons/textIcon.png"));
		buttonNewText.addActionListener(actionListener);
		buttonNewText.setActionCommand(WCCController.pageNewText);
		buttonNewText.setToolTipText("Neuer Textinhalt");
		buttonNewText.setFocusable(false);
		toolbar.add(buttonNewText);
		JButton buttonNewImage = new JButton(new ImageIcon("icons/imageIcon.png"));
		buttonNewImage.addActionListener(actionListener);
		buttonNewImage.setActionCommand(WCCController.pageNewImage);
		buttonNewImage.setToolTipText("Neues Bild");
		buttonNewImage.setFocusable(false);
		toolbar.add(buttonNewImage);
		
		return toolbar;
	}
	
	//Method to create the window's content
	private Component createMainPanel () {
		mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		mainPanel.setContinuousLayout(true);
		//TODO: Set lists into scrollpane
		//TODO: Make lists D&D-enabled
		pageList = new JList<>(new String[]{"Testseite 1", "Testseite 2", "Testseite 3", "Testseite 4", "Testseite 5"});
		elementList = new JList<>(new String[]{"Testelement 1", "Testelement 2", "Testelement 3", "Testelement 4", "Testelement 5"});
		mainPanel.setLeftComponent(pageList);
		mainPanel.setRightComponent(elementList);
		return mainPanel;
	}

	public void applySettings() {
		f.setLocation(model.getSettings().getLocation());
		f.setSize(model.getSettings().getSize());
		if(model.getSettings().isFullscreen()) {
			f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		} else { 
			f.setExtendedState(JFrame.NORMAL);
		}
		mainPanel.setDividerLocation(model.getSettings().getDividerLocation());
	}
	
	public void fetchSettings() {
		model.getSettings().setLocation(f.getLocation());
		model.getSettings().setSize(f.getSize());
		model.getSettings().setFullscreen(f.getExtendedState() == JFrame.MAXIMIZED_BOTH);
		model.getSettings().setDividerLocation(mainPanel.getDividerLocation());
	}
	
	public void setVisible(boolean b) {
		f.setVisible(b);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if(model.getDataStorage().isEditedSinceLastSave() && (!f.getTitle().startsWith("*"))) {
			f.setTitle("*" + f.getTitle());
		} else if((!model.getDataStorage().isEditedSinceLastSave()) && f.getTitle().startsWith("*")) {
			f.setTitle(f.getTitle().substring(1));
		}
		//TODO: implement rest (get pages and elements and fill the lists with them, then select correct page and element)
	}
	
}
