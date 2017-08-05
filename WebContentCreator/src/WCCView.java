import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
	private WCCController controller;
	private Color backgroundColor;
	private String title = "Web Content Creator";
	private JFrame f;
	private JSplitPane mainPanel;
	private JPanel leftList, rightList;
	
	//Constructor to initiate needed variables and create the window
	public WCCView(WCCModel m, WCCController c) {
		this.model = m;
		this.model.addObserver(this);
		this.controller = c;
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {e.printStackTrace();}
		backgroundColor = new Color(255, 255, 255);
		
		//Create window
		f = new JFrame(title);
		f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		f.addWindowListener(controller);
		f.setBackground(backgroundColor);
		
		//Charge window with content
		f.getContentPane().setLayout(new BorderLayout());
		f.setJMenuBar(createMenuBar());
		f.getContentPane().add(createToolBar(), BorderLayout.NORTH);
		f.getContentPane().add(createMainPanel(), BorderLayout.CENTER);
		
		//Apply settings to window and content
		applySettings();
		
		//Make data visible for first time
		update(model.getDataStorage(), model.getDataStorage());
	}
	
	//Method to create the window's menubar
	private JMenuBar createMenuBar() {
		JMenuBar menubar = new JMenuBar();
		menubar.setBackground(backgroundColor);
		JMenu menuFile = createMenu("Datei  ", null);
			JMenu menuFileNew = createMenu("Neu", new ImageIcon("icons/plusIcon.png"));
			//Optional functionality for later:
			//menuFileNew.add(createMenuItem("Neues Projekt", new ImageIcon("icons/plusIcon.png"), WCCController.fileNew));
			//menuFileNew.addSeparator();
			menuFileNew.add(createMenuItem("Neue Seite", new ImageIcon("icons/newPageIcon.png"), WCCController.pageNewPage));
			menuFileNew.addSeparator();
			menuFileNew.add(createMenuItem("Neue Überschrift", new ImageIcon("icons/headerIcon.png"), WCCController.pageNewHeader));
			menuFileNew.add(createMenuItem("Neue Unterüberschrift", new ImageIcon("icons/subheaderIcon.png"), WCCController.pageNewSubheader));
			menuFileNew.add(createMenuItem("Neuer Textinhalt", new ImageIcon("icons/textIcon.png"), WCCController.pageNewText));
			menuFileNew.add(createMenuItem("Neues Bild", new ImageIcon("icons/imageIcon.png"), WCCController.pageNewImage));
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
			menuPageNew.add(createMenuItem("Neue Überschrift", new ImageIcon("icons/headerIcon.png"), WCCController.pageNewHeader));
			menuPageNew.add(createMenuItem("Neue Unterüberschrift", new ImageIcon("icons/subheaderIcon.png"), WCCController.pageNewSubheader));
			menuPageNew.add(createMenuItem("Neuer Textinhalt", new ImageIcon("icons/textIcon.png"), WCCController.pageNewText));
			menuPageNew.add(createMenuItem("Neues Bild", new ImageIcon("icons/imageIcon.png"), WCCController.pageNewImage));
			menuPage.add(menuPageNew);
		menuPage.addSeparator();
		menuPage.add(createMenuItem("Seite löschen", new ImageIcon("icons/deletePageIcon.png"), WCCController.pageDelete));
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
	
	private JMenu createMenu(String title, Icon icon) {
		JMenu menu = new JMenu(title);
		if(icon != null)
			menu.setIcon(icon);
		return menu;
	}
	
	private JMenuItem createMenuItem(String title, Icon icon, String actionCommand) {
		JMenuItem item = new JMenuItem(title);
		if(icon != null)
			item.setIcon(icon);
		item.addActionListener(controller);
		item.setActionCommand(actionCommand);
		return item;
	}
	
	//Method to create the window's toolbar
	private JToolBar createToolBar () {
		JToolBar toolbar = new JToolBar("Toolbar");
		toolbar.setFloatable(true);
		//toolbar.setBorder(BorderFactory.createRaisedBevelBorder());
		JButton buttonNew = createToolbarButton(new ImageIcon("icons/plusIcon.png"), "", "Neu...");
		buttonNew.removeActionListener(controller);
			JPopupMenu popupButtonNew = new JPopupMenu();
			popupButtonNew.add(createMenuItem("Neue Seite", new ImageIcon("icons/newPageIcon.png"), WCCController.pageNewPage));
			popupButtonNew.addSeparator();
			popupButtonNew.add(createMenuItem("Neue Überschrift", new ImageIcon("icons/headerIcon.png"), WCCController.pageNewHeader));
			popupButtonNew.add(createMenuItem("Neue Unterüberschrift", new ImageIcon("icons/subheaderIcon.png"), WCCController.pageNewSubheader));
			popupButtonNew.add(createMenuItem("Neuer Textinhalt", new ImageIcon("icons/textIcon.png"), WCCController.pageNewText));
			popupButtonNew.add(createMenuItem("Neues Bild", new ImageIcon("icons/imageIcon.png"), WCCController.pageNewImage));
		buttonNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				popupButtonNew.show(buttonNew, 0, buttonNew.getHeight());
			}
		});
		toolbar.add(buttonNew);
		toolbar.addSeparator();
		toolbar.add(createToolbarButton(new ImageIcon("icons/saveIcon.png"), WCCController.fileSave, "Speichern"));
		toolbar.addSeparator();
		toolbar.add(createToolbarButton(new ImageIcon("icons/exportIcon.png"), WCCController.fileExport, "Exportieren"));
		toolbar.addSeparator();
		toolbar.add(createToolbarButton(new ImageIcon("icons/newPageIcon.png"), WCCController.pageNewPage, "Neue Seite"));
		toolbar.addSeparator();
		toolbar.add(createToolbarButton(new ImageIcon("icons/headerIcon.png"), WCCController.pageNewHeader, "Neue Überschrift"));
		toolbar.add(createToolbarButton(new ImageIcon("icons/subheaderIcon.png"), WCCController.pageNewSubheader, "Neue Unterüberschrift"));
		toolbar.add(createToolbarButton(new ImageIcon("icons/textIcon.png"), WCCController.pageNewText, "Neuer Textinhalt"));
		toolbar.add(createToolbarButton(new ImageIcon("icons/imageIcon.png"), WCCController.pageNewImage, "Neues Bild"));
		
		return toolbar;
	}
	
	private JButton createToolbarButton(Icon icon, String actionCommand, String tooltip) {
		JButton button = new JButton(icon);
		button.addActionListener(controller);
		button.setActionCommand(actionCommand);
		button.setToolTipText(tooltip);
		button.setFocusable(false);
		button.setContentAreaFilled(false);
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		return button;
	}
	
	//Method to create the window's content
	private Component createMainPanel () {
		mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		mainPanel.setBackground(backgroundColor);
		mainPanel.setContinuousLayout(true);
		leftList = new JPanel();
		leftList.setLayout(new BoxLayout(leftList, BoxLayout.Y_AXIS));
		leftList.setBackground(backgroundColor);
		rightList = new JPanel();
		rightList.setLayout(new BoxLayout(rightList, BoxLayout.Y_AXIS));
		rightList.setBackground(backgroundColor);
		mainPanel.setLeftComponent(new JScrollPane(leftList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		mainPanel.setRightComponent(new JScrollPane(rightList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		return mainPanel;
	}
	
	private Component createPageListItem(Page p) {
		JPanel itemPanel = new JPanel(new BorderLayout());
		itemPanel.setBackground(backgroundColor);
		itemPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		itemPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
			JPanel labelPanel = new JPanel(new BorderLayout());
			labelPanel.setBackground(backgroundColor);
			labelPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
				JLabel nameLabel = new JLabel("<html><a style='font-size: 20px;'>"+p.getName()+"</a></html>");
				labelPanel.add(nameLabel, BorderLayout.CENTER);
				JLabel fileNameLabel = new JLabel("<html>"+p.getFilename()+"</html>");
				labelPanel.add(fileNameLabel, BorderLayout.SOUTH);
			itemPanel.add(labelPanel, BorderLayout.CENTER);
			
			JPanel deleteButtonPanel = new JPanel(new FlowLayout());
			deleteButtonPanel.setBackground(backgroundColor);
				JButton deleteButton = new JButton("<html><a style='font-size: 15px;'>x</a></html>");
				deleteButton.addActionListener(controller);
				deleteButton.setActionCommand(WCCController.pageDelete + ":" + p.getFilename());
				deleteButton.setFocusable(false);
				deleteButton.setContentAreaFilled(false);
				deleteButton.setMargin(new Insets(0, 0, 0, 0));
				deleteButton.setPreferredSize(new Dimension(25, 25));
				deleteButton.setVisible(false);
				deleteButtonPanel.add(deleteButton);
			itemPanel.add(deleteButtonPanel, BorderLayout.EAST);
		itemPanel.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void mouseExited(MouseEvent e) {
				deleteButton.setVisible(false);
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				deleteButton.setVisible(true);
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		itemPanel.setMaximumSize(new Dimension(1920, nameLabel.getPreferredSize().height + fileNameLabel.getPreferredSize().height + 20));
		return itemPanel;
	}
	
	private Component createElementListItem(Element e) {
		//TODO: implement
		return null;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if(model.getDataStorage().isEditedSinceLastSave()) {
			f.setTitle("*" + title);
		} else  {
			f.setTitle(title);
		}
		//TODO: implement rest (get pages and elements and fill the lists with them, then select correct page and element
		for(Page p : model.getDataStorage()) {
			leftList.add(createPageListItem(p));
		}
		
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
	
	public void askForSaveBeforeExit() {
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
	
}
