package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import base.*;

public class MainWindow extends JFrame {

	private static JSplitPane mainPanel;
	private static JPanel pageList, elementList;
	private static int selectedPage;
	private static ArrayList<AbstractButton> pageDependentButtons;
	
	public MainWindow(String title) {
		super(title);
		
		selectedPage = -1;
		pageDependentButtons = new ArrayList<>();
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowListener() {
			@Override
			public void windowActivated(WindowEvent arg0) {}
			@Override
			public void windowClosed(WindowEvent arg0) {}
			@Override
			public void windowClosing(WindowEvent arg0) {
				WCCController.fileExit(false);
			}
			@Override
			public void windowDeactivated(WindowEvent arg0) {}
			@Override
			public void windowDeiconified(WindowEvent arg0) {}
			@Override
			public void windowIconified(WindowEvent arg0) {}
			@Override
			public void windowOpened(WindowEvent arg0) {}

		});
		setBackground(WCCView.backgroundColor);
		
		//Charge window with content
		getContentPane().setLayout(new BorderLayout());
		setJMenuBar(new WCCMenubar());
		getContentPane().add(createToolBar(), BorderLayout.NORTH);
		getContentPane().add(createMainPanel(), BorderLayout.CENTER);
		
	}

	//Method to create the window's menubar
	private static JMenuBar createMenuBar() {
			JMenu menuFileNew = createMenu("Neu", new ImageIcon("icons/plusIcon.png"));
			/* Optional functionality for later:
			 * menuFileNew.add(createMenuItem("Neues Projekt", new ImageIcon("icons/plusIcon.png"), WCCController.fileNew));
			 * menuFileNew.addSeparator();
			 */
			menuFileNew.add(createMenuItem("Neue Seite", new ImageIcon("icons/newPageIcon.png"), new ActionListener() {@Override public void actionPerformed(ActionEvent e) {WCCController.pageNew(null, null);}}));
			menuFileNew.addSeparator();
			menuFileNew.add(createPageDependentMenuItem("Neue Überschrift", new ImageIcon("icons/headerIcon.png"), new ActionListener() {@Override public void actionPerformed(ActionEvent e) {WCCController.elementNew(null, Element.HEADER, null);}}));
			menuFileNew.add(createPageDependentMenuItem("Neue Unterüberschrift", new ImageIcon("icons/subheaderIcon.png"), new ActionListener() {@Override public void actionPerformed(ActionEvent e) {WCCController.elementNew(null, Element.SUBHEADER, null);}}));
			menuFileNew.add(createPageDependentMenuItem("Neuer Textinhalt", new ImageIcon("icons/textIcon.png"), new ActionListener() {@Override public void actionPerformed(ActionEvent e) {WCCController.elementNew(null, Element.TEXT, null);}}));
			menuFileNew.add(createPageDependentMenuItem("Neues Bild", new ImageIcon("icons/imageIcon.png"), new ActionListener() {@Override public void actionPerformed(ActionEvent e) {WCCController.elementNew(null, Element.IMAGE, null);}}));
			menuFile.add(menuFileNew);
			menuFile.addSeparator();
		//Optional functionality for later:
		//menuFile.add(createMenuItem("Öffnen", new ImageIcon("icons/openIcon.png"), WCCController.fileOpen));
		//menuFile.addSeparator();
		menuFile.add(createMenuItem("Speichern", new ImageIcon("icons/saveIcon.png"), WCCController.fileSave));
		//Optional functionality for later:
		//menuFile.add(new JMenuItem("Speichern unter", new ImageIcon("icons/saveIcon.png"), WCCController.fileSaveAs));
		menuFile.addSeparator();
		menuFile.add(createMenuItem("Exportieren", new ImageIcon("icons/exportIcon.png"), WCCController.fileExport));
		menuFile.addSeparator();
		menuFile.add(createMenuItem("Schließen", null, WCCController.fileExit));
		menubar.add(menuFile);
		
		JMenu menuPage = createMenu("Seite  ", null);
		menuPage.add(createMenuItem("Neue Seite", new ImageIcon("icons/newPageIcon.png"), WCCController.pageNewPage));
		menuPage.addSeparator();
			JMenu menuPageNew = createMenu("Neues Element", new ImageIcon("icons/plusIcon.png"));
			menuPageNew.add(createPageDependentMenuItem("Neue Überschrift", new ImageIcon("icons/headerIcon.png"), WCCController.pageNewHeader+":"));
			menuPageNew.add(createPageDependentMenuItem("Neue Unterüberschrift", new ImageIcon("icons/subheaderIcon.png"), WCCController.pageNewSubheader+":"));
			menuPageNew.add(createPageDependentMenuItem("Neuer Textinhalt", new ImageIcon("icons/textIcon.png"), WCCController.pageNewText+":"));
			menuPageNew.add(createPageDependentMenuItem("Neues Bild", new ImageIcon("icons/imageIcon.png"), WCCController.pageNewImage+":"));
			menuPage.add(menuPageNew);
		menuPage.addSeparator();
		menuPage.add(createPageDependentMenuItem("Seite löschen", new ImageIcon("icons/deletePageIcon.png"), WCCController.pageDelete+":false:"));
		menubar.add(menuPage);
		
		JMenu menuWindow = createMenu("Ansicht  ", null);
		menuWindow.add(createMenuItem("Maximieren/Minimieren", null, WCCController.windowToggleMaximized));
		menuWindow.add(createMenuItem("Trennstrich zentrieren", null, WCCController.windowCenterDivider));
		menubar.add(menuWindow);
		
		JMenu menuHelp = createMenu("Hilfe  ", null);
		menuHelp.add(createMenuItem("Info", null, WCCController.helpInfo));
		menuHelp.add(createMenuItem("Nach Updates suchen", null, WCCController.helpCheckForUpdates));
		menubar.add(menuHelp);
		
		return menubar;
	}


}
