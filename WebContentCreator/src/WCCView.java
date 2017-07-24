import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;

import javax.swing.*;

/*
 * View part of WebContentCreator (by concept of ModelViewControl)
 * 
 * Created by Leo Köberlein on 09.07.2017
 */
public class WCCView {
	
	protected JFrame f;
	protected Toolkit toolkit;
	
	//Constructor to initiate needed variables and create the window
	public WCCView(String title, ActionListener actionListener, WindowListener windowListener) {
		
		//Window creation and settings
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {e.printStackTrace();}
		toolkit = Toolkit.getDefaultToolkit();
		f = new JFrame(title);
		f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		f.setSize(toolkit.getScreenSize().width/2, toolkit.getScreenSize().height);
		f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		f.addWindowListener(windowListener);
		f.getContentPane().setLayout(new BorderLayout());
		f.setJMenuBar(createMenuBar(actionListener));
		f.getContentPane().add(createToolBar(actionListener), BorderLayout.NORTH);
	}
	
	protected JMenuBar createMenuBar(ActionListener actionListener) {
		JMenuBar menubar = new JMenuBar();
		JMenu menuFile = new JMenu("Datei  ");
			JMenu menuFileNew = new JMenu("Neu");
			JMenuItem menuFileNewProject = new JMenuItem("Neues Projekt");
			menuFileNewProject.addActionListener(actionListener);
			menuFileNewProject.setActionCommand(WCCControl.fileNew);
			menuFileNew.add(menuFileNewProject);
			menuFileNew.addSeparator();
			JMenuItem menuFileNewPage = new JMenuItem("Neue Seite");
			menuFileNewPage.addActionListener(actionListener);
			menuFileNewPage.setActionCommand(WCCControl.pageNewPage);
			menuFileNew.add(menuFileNewPage);
			menuFileNew.addSeparator();
			JMenuItem menuFileNewHeader = new JMenuItem("Neue Überschrift");
			menuFileNewHeader.addActionListener(actionListener);
			menuFileNewHeader.setActionCommand(WCCControl.pageNewHeader);
			menuFileNew.add(menuFileNewHeader);
			JMenuItem menuFileNewSubheader = new JMenuItem("Neue Unterüberschrift");
			menuFileNewSubheader.addActionListener(actionListener);
			menuFileNewSubheader.setActionCommand(WCCControl.pageNewSubheader);
			menuFileNew.add(menuFileNewSubheader);
			JMenuItem menuFileNewText = new JMenuItem("Neuer Textinhalt");
			menuFileNewText.addActionListener(actionListener);
			menuFileNewText.setActionCommand(WCCControl.pageNewText);
			menuFileNew.add(menuFileNewText);
			JMenuItem menuFileNewImage = new JMenuItem("Neues Bild");
			menuFileNewImage.addActionListener(actionListener);
			menuFileNewImage.setActionCommand(WCCControl.pageNewImage);
			menuFileNew.add(menuFileNewImage);
			menuFile.add(menuFileNew);
		JMenuItem menuFileOpen = new JMenuItem("Öffnen");
		menuFileOpen.addActionListener(actionListener);
		menuFileOpen.setActionCommand(WCCControl.fileOpen);
		menuFile.add(menuFileOpen);
		menuFile.addSeparator();
		JMenuItem menuFileSave = new JMenuItem("Speichern");
		menuFileSave.addActionListener(actionListener);
		menuFileSave.setActionCommand(WCCControl.fileSave);
		menuFile.add(menuFileSave);
		JMenuItem menuFileSaveAs = new JMenuItem("Speichern unter");
		menuFileSaveAs.addActionListener(actionListener);
		menuFileSaveAs.setActionCommand(WCCControl.fileSaveAs);
		menuFile.add(menuFileSaveAs);
		menuFile.addSeparator();
		JMenuItem menuFileExport = new JMenuItem("Exportieren");
		menuFileExport.addActionListener(actionListener);
		menuFileExport.setActionCommand(WCCControl.fileExport);
		menuFile.add(menuFileExport);
		menuFile.addSeparator();
		JMenuItem menuFileExit = new JMenuItem("Schließen");
		menuFileExit.addActionListener(actionListener);
		menuFileExit.setActionCommand(WCCControl.fileExit);
		menuFile.add(menuFileExit);
		menubar.add(menuFile);
		
		JMenu menuPage = new JMenu("Seite  ");
		JMenuItem menuPageNewPage = new JMenuItem("Neue Seite");
		menuPageNewPage.addActionListener(actionListener);
		menuPageNewPage.setActionCommand(WCCControl.pageNewPage);
		menuPage.add(menuPageNewPage);
		menuPage.addSeparator();
			JMenu menuPageNew = new JMenu("Neues Element");
			JMenuItem menuPageNewHeader = new JMenuItem("Neue Überschrift");
			menuPageNewHeader.addActionListener(actionListener);
			menuPageNewHeader.setActionCommand(WCCControl.pageNewHeader);
			menuPageNew.add(menuPageNewHeader);
			JMenuItem menuPageNewSubheader = new JMenuItem("Neue Unterüberschrift");
			menuPageNewSubheader.addActionListener(actionListener);
			menuPageNewSubheader.setActionCommand(WCCControl.pageNewSubheader);
			menuPageNew.add(menuPageNewSubheader);
			JMenuItem menuPageNewText = new JMenuItem("Neuer Textinhalt");
			menuPageNewText.addActionListener(actionListener);
			menuPageNewText.setActionCommand(WCCControl.pageNewText);
			menuPageNew.add(menuPageNewText);
			JMenuItem menuPageNewImage = new JMenuItem("Neues Bild");
			menuPageNewImage.addActionListener(actionListener);
			menuPageNewImage.setActionCommand(WCCControl.pageNewImage);
			menuPageNew.add(menuPageNewImage);
			menuPage.add(menuPageNew);
		menuPage.addSeparator();
		JMenuItem menuPageDelete = new JMenuItem("Seite löschen");
		menuPageDelete.addActionListener(actionListener);
		menuPageDelete.setActionCommand(WCCControl.pageDelete);
		menuPage.add(menuPageDelete);
		menubar.add(menuPage);
		
		JMenu menuHelp = new JMenu("Hilfe  ");
		JMenuItem menuHelpInfo = new JMenuItem("Info");
		menuHelpInfo.addActionListener(actionListener);
		menuHelpInfo.setActionCommand(WCCControl.helpInfo);
		menuHelp.add(menuHelpInfo);
		JMenuItem menuHelpCheckForUpdates = new JMenuItem("Nach Updates suchen");
		menuHelpCheckForUpdates.addActionListener(actionListener);
		menuHelpCheckForUpdates.setActionCommand(WCCControl.helpCheckForUpdates);
		menuHelp.add(menuHelpCheckForUpdates);
		menubar.add(menuHelp);
		
		return menubar;
	}
	
	protected JToolBar createToolBar (ActionListener actionListener) {
		JToolBar toolbar = new JToolBar("Toolbar");
		toolbar.setFloatable(true);
		toolbar.setBorder(BorderFactory.createRaisedBevelBorder());
		JButton buttonNew = new JButton(new ImageIcon("icons/plusIcon.png"));
			JPopupMenu popupButtonNew = new JPopupMenu();
			JMenuItem menuFileNewPage = new JMenuItem("Neue Seite");
			menuFileNewPage.addActionListener(actionListener);
			menuFileNewPage.setActionCommand(WCCControl.pageNewPage);
			popupButtonNew.add(menuFileNewPage);
			popupButtonNew.addSeparator();
			JMenuItem menuFileNewHeader = new JMenuItem("Neue Überschrift");
			menuFileNewHeader.addActionListener(actionListener);
			menuFileNewHeader.setActionCommand(WCCControl.pageNewHeader);
			popupButtonNew.add(menuFileNewHeader);
			JMenuItem menuFileNewSubheader = new JMenuItem("Neue Unterüberschrift");
			menuFileNewSubheader.addActionListener(actionListener);
			menuFileNewSubheader.setActionCommand(WCCControl.pageNewSubheader);
			popupButtonNew.add(menuFileNewSubheader);
			JMenuItem menuFileNewText = new JMenuItem("Neuer Textinhalt");
			menuFileNewText.addActionListener(actionListener);
			menuFileNewText.setActionCommand(WCCControl.pageNewText);
			popupButtonNew.add(menuFileNewText);
			JMenuItem menuFileNewImage = new JMenuItem("Neues Bild");
			menuFileNewImage.addActionListener(actionListener);
			menuFileNewImage.setActionCommand(WCCControl.pageNewImage);
			popupButtonNew.add(menuFileNewImage);
		buttonNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				popupButtonNew.show(buttonNew, 0, buttonNew.getHeight());
			}
		});
		buttonNew.setToolTipText("Neu...");
		toolbar.add(buttonNew);
		toolbar.addSeparator();
		
		JButton buttonSave = new JButton(new ImageIcon("icons/saveIcon.png"));
		buttonSave.addActionListener(actionListener);
		buttonSave.setActionCommand(WCCControl.fileSave);
		buttonSave.setToolTipText("Speichern");
		toolbar.add(buttonSave);
		toolbar.addSeparator();
		
		JButton buttonExport = new JButton(new ImageIcon("icons/exportIcon.png"));
		buttonExport.addActionListener(actionListener);
		buttonExport.setActionCommand(WCCControl.fileExport);
		buttonExport.setToolTipText("Exportieren");
		toolbar.add(buttonExport);
		toolbar.addSeparator();
		
		JButton buttonNewPage = new JButton(new ImageIcon("icons/newPageIcon.png"));
		buttonNewPage.addActionListener(actionListener);
		buttonNewPage.setActionCommand(WCCControl.pageNewPage);
		buttonNewPage.setToolTipText("Neue Seite");
		toolbar.add(buttonNewPage);
		
		return toolbar;
	}
	
	public void setVisible(boolean visible) {
		f.setVisible(visible);
	}

}
