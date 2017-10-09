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

/*
 * View part of WebContentCreator (by concept of ModelViewController)
 * 
 * Created by Leo K�berlein on 09.07.2017
 */
public class WCCView {
	
	private static Observer modelObserver;
	private static Color backgroundColor, hoverColor, selectedColor, transparentColor;
	private static String title = "Web Content Creator";
	private static JFrame f;
	private static JSplitPane mainPanel;
	private static JPanel pageList, elementList;
	private static int selectedPage;
	private static ArrayList<AbstractButton> pageDependentButtons;
	
	public static void init() {
		//initiate variables
		backgroundColor = new Color(255, 255, 255);
		hoverColor = new Color(235, 235, 235);
		selectedColor = new Color(210, 210, 210);
		transparentColor = new Color(0, 0, 0, 0);
		selectedPage = -1;
		pageDependentButtons = new ArrayList<>();
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
		f = new JFrame(title);
		f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		f.addWindowListener(new WindowListener() {
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
	private static JMenuBar createMenuBar() {
		JMenuBar menubar = new JMenuBar();
		menubar.setBackground(backgroundColor);
		JMenu menuFile = createMenu("Datei  ", null);
			JMenu menuFileNew = createMenu("Neu", new ImageIcon("icons/plusIcon.png"));
			//Optional functionality for later:
			//menuFileNew.add(createMenuItem("Neues Projekt", new ImageIcon("icons/plusIcon.png"), WCCController.fileNew));
			//menuFileNew.addSeparator();
			menuFileNew.add(createMenuItem("Neue Seite", new ImageIcon("icons/newPageIcon.png"), WCCController.pageNewPage));
			menuFileNew.addSeparator();
			menuFileNew.add(createPageDependentMenuItem("Neue �berschrift", new ImageIcon("icons/headerIcon.png"), WCCController.pageNewHeader+":"));
			menuFileNew.add(createPageDependentMenuItem("Neue Unter�berschrift", new ImageIcon("icons/subheaderIcon.png"), WCCController.pageNewSubheader+":"));
			menuFileNew.add(createPageDependentMenuItem("Neuer Textinhalt", new ImageIcon("icons/textIcon.png"), WCCController.pageNewText+":"));
			menuFileNew.add(createPageDependentMenuItem("Neues Bild", new ImageIcon("icons/imageIcon.png"), WCCController.pageNewImage+":"));
			menuFile.add(menuFileNew);
			menuFile.addSeparator();
		//Optional functionality for later:
		//menuFile.add(createMenuItem("�ffnen", new ImageIcon("icons/openIcon.png"), WCCController.fileOpen));
		//menuFile.addSeparator();
		menuFile.add(createMenuItem("Speichern", new ImageIcon("icons/saveIcon.png"), WCCController.fileSave));
		//Optional functionality for later:
		//menuFile.add(new JMenuItem("Speichern unter", new ImageIcon("icons/saveIcon.png"), WCCController.fileSaveAs));
		menuFile.addSeparator();
		menuFile.add(createMenuItem("Exportieren", new ImageIcon("icons/exportIcon.png"), WCCController.fileExport));
		menuFile.addSeparator();
		menuFile.add(createMenuItem("Schlie�en", null, WCCController.fileExit));
		menubar.add(menuFile);
		
		JMenu menuPage = createMenu("Seite  ", null);
		menuPage.add(createMenuItem("Neue Seite", new ImageIcon("icons/newPageIcon.png"), WCCController.pageNewPage));
		menuPage.addSeparator();
			JMenu menuPageNew = createMenu("Neues Element", new ImageIcon("icons/plusIcon.png"));
			menuPageNew.add(createPageDependentMenuItem("Neue �berschrift", new ImageIcon("icons/headerIcon.png"), WCCController.pageNewHeader+":"));
			menuPageNew.add(createPageDependentMenuItem("Neue Unter�berschrift", new ImageIcon("icons/subheaderIcon.png"), WCCController.pageNewSubheader+":"));
			menuPageNew.add(createPageDependentMenuItem("Neuer Textinhalt", new ImageIcon("icons/textIcon.png"), WCCController.pageNewText+":"));
			menuPageNew.add(createPageDependentMenuItem("Neues Bild", new ImageIcon("icons/imageIcon.png"), WCCController.pageNewImage+":"));
			menuPage.add(menuPageNew);
		menuPage.addSeparator();
		menuPage.add(createPageDependentMenuItem("Seite l�schen", new ImageIcon("icons/deletePageIcon.png"), WCCController.pageDelete+":false:"));
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
	
	private JMenuItem createPageDependentMenuItem(String title, Icon icon, String actionCommand) {
		JMenuItem item = createMenuItem(title, icon, actionCommand);
		pageDependentButtons.add(item);
		return item;
	}
	
	//Method to create the window's toolbar
	private static JToolBar createToolBar () {
		JToolBar toolbar = new JToolBar("Toolbar");
		toolbar.setFloatable(true);
		//toolbar.setBorder(BorderFactory.createRaisedBevelBorder());
		JButton buttonNew = createToolbarButton(new ImageIcon("icons/plusIcon.png"), "", "Neu...");
		buttonNew.removeActionListener(controller);
			JPopupMenu popupButtonNew = new JPopupMenu();
			popupButtonNew.add(createMenuItem("Neue Seite", new ImageIcon("icons/newPageIcon.png"), WCCController.pageNewPage));
			popupButtonNew.addSeparator();
			popupButtonNew.add(createPageDependentMenuItem("Neue �berschrift", new ImageIcon("icons/headerIcon.png"), WCCController.pageNewHeader+":"));
			popupButtonNew.add(createPageDependentMenuItem("Neue Unter�berschrift", new ImageIcon("icons/subheaderIcon.png"), WCCController.pageNewSubheader+":"));
			popupButtonNew.add(createPageDependentMenuItem("Neuer Textinhalt", new ImageIcon("icons/textIcon.png"), WCCController.pageNewText+":"));
			popupButtonNew.add(createPageDependentMenuItem("Neues Bild", new ImageIcon("icons/imageIcon.png"), WCCController.pageNewImage+":"));
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
		toolbar.add(createPageDependentToolbarButton(new ImageIcon("icons/headerIcon.png"), WCCController.pageNewHeader+":", "Neue �berschrift"));
		toolbar.add(createPageDependentToolbarButton(new ImageIcon("icons/subheaderIcon.png"), WCCController.pageNewSubheader+":", "Neue Unter�berschrift"));
		toolbar.add(createPageDependentToolbarButton(new ImageIcon("icons/textIcon.png"), WCCController.pageNewText+":", "Neuer Textinhalt"));
		toolbar.add(createPageDependentToolbarButton(new ImageIcon("icons/imageIcon.png"), WCCController.pageNewImage+":", "Neues Bild"));
		
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
	
	private JButton createPageDependentToolbarButton(Icon icon, String actionCommand, String tooltip) {
		JButton button = createToolbarButton(icon, actionCommand, tooltip);
		pageDependentButtons.add(button);
		return button;
	}
	
	//Method to create the window's content
	private static Component createMainPanel () {
		mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		mainPanel.setBackground(backgroundColor);
		mainPanel.setContinuousLayout(true);
		return mainPanel;
	}
	
	private Component createPageListItem(Page p) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(backgroundColor);
		panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
		panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
		
			JPanel labelPanel = new JPanel(new BorderLayout());
			labelPanel.setBackground(transparentColor);
			labelPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 0));
				JLabel nameLabel = new JLabel("<html><a style='font-size: 15px;'>"+p.getName()+"</a></html>");
				labelPanel.add(nameLabel, BorderLayout.CENTER);
				JLabel fileNameLabel = new JLabel("<html>"+p.getFilename()+"</html>");
				labelPanel.add(fileNameLabel, BorderLayout.SOUTH);
			panel.add(labelPanel, BorderLayout.CENTER);
			
			JPanel sidePanel = new JPanel(new BorderLayout());
			sidePanel.setBackground(transparentColor);
			sidePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
				JPanel deleteButtonPanel = new JPanel(new FlowLayout());
				deleteButtonPanel.setBackground(transparentColor);
					JButton deleteButton = new JButton("<html><a style='font-size: 15px;'>x</a></html>");
					deleteButton.addActionListener(controller);
					deleteButton.setActionCommand(WCCController.pageDelete + ":false:" + p.getFilename());
					deleteButton.setToolTipText("Seite l�schen");
					deleteButton.setFocusable(false);
					deleteButton.setContentAreaFilled(false);
					deleteButton.setOpaque(true);
					deleteButton.setBackground(transparentColor);
					deleteButton.setMargin(new Insets(0, 0, 0, 0));
					deleteButton.setPreferredSize(new Dimension(15, 15));
					deleteButtonPanel.add(deleteButton);
				sidePanel.add(deleteButtonPanel, BorderLayout.EAST);
				JLabel versionLabel = new JLabel("<html>&nbsp;&nbsp;Version: " + p.getVersion() + "&nbsp;&nbsp;</html>");
				sidePanel.add(versionLabel, BorderLayout.SOUTH);
			panel.add(sidePanel, BorderLayout.EAST);
		MouseListener hoverListener = new MouseListener() {
			@Override public void mouseReleased(MouseEvent e) {}
			@Override public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {
				if(selectedPage == -1 || (selectedPage != -1 && pageList.getComponent(selectedPage) != panel)) {
					panel.setBackground(backgroundColor);
				}
				panel.repaint();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				if(selectedPage == -1 || (selectedPage != -1 && pageList.getComponent(selectedPage) != panel)) {
					panel.setBackground(hoverColor);
				}
				panel.repaint();
			}
			@Override public void mouseClicked(MouseEvent e) {}
		};
		panel.addMouseListener(hoverListener);
		deleteButton.addMouseListener(hoverListener);
		JPopupMenu pageContextMenu = new JPopupMenu();
		pageContextMenu.add(createMenuItem("Daten �ndern", null, WCCController.pageChangeData+":false:"+p.getFilename()+":"+p.getName()));
		pageContextMenu.addSeparator();
		pageContextMenu.add(createMenuItem("an den Anfang bewegen", null, WCCController.pageMoveTop+":"+p.getFilename()));
		pageContextMenu.add(createMenuItem("Nach oben bewegen", null, WCCController.pageMoveUp+":"+p.getFilename()));
		pageContextMenu.add(createMenuItem("Nach unten bewegen", null, WCCController.pageMoveDown+":"+p.getFilename()));
		pageContextMenu.add(createMenuItem("An das Ende bewegen", null, WCCController.pageMoveBottom+":"+p.getFilename()));
		pageContextMenu.addSeparator();
		pageContextMenu.add(createMenuItem("Seite l�schen", null, WCCController.pageDelete+":false:"+p.getFilename()));
		MouseListener clickListener = new MouseListener() {
			@Override 
			public void mouseReleased(MouseEvent e) {
				if(e.isPopupTrigger())
					pageContextMenu.show(panel, e.getX(), e.getY());
			}
			@Override
			public void mousePressed(MouseEvent e) {
				controller.actionPerformed(new ActionEvent(panel, ActionEvent.ACTION_PERFORMED, WCCController.pageSelect+":"+model.getDataStorage().indexOf(p)));
				if(e.isPopupTrigger())
					pageContextMenu.show(panel, e.getX(), e.getY());
			}
			@Override public void mouseExited(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseClicked(MouseEvent e) {}
		};
		panel.addMouseListener(clickListener);
		return panel;
	}
	
	public static void selectPage(int id) {
		fetchSettings();
		selectedPage = id;
		for(int i=0; i<model.getDataStorage().size(); ++i) {
			pageList.getComponent(i).setBackground(backgroundColor);
		}
		pageList.getComponent(id).setBackground(selectedColor);
		elementList = new JPanel();
		elementList.setLayout(new BoxLayout(elementList, BoxLayout.Y_AXIS));
		elementList.setBackground(backgroundColor);
		JScrollPane elementpane = new JScrollPane(elementList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		elementpane.getVerticalScrollBar().setUnitIncrement(16);
		mainPanel.setRightComponent(elementpane);
		Page selected = model.getDataStorage().get(id);
		for(Element e : selected) {
			elementList.add(createElementListItem(selected, e));
		}
		elementList.repaint();
		for(AbstractButton a : pageDependentButtons) {
			a.setEnabled(true);
			a.setActionCommand(a.getActionCommand().substring(0, a.getActionCommand().lastIndexOf(":")+1)+model.getDataStorage().get(id).getFilename());
		}
		applySettings();
	}
	
	private Component createElementListItem(Page parent, Element e) {
		int elementIndex = parent.indexOf(e);
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(backgroundColor);
		panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
		panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
			JPanel labelPanel = new JPanel(new BorderLayout());
			labelPanel.setBackground(transparentColor);
			labelPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 0));
				JLabel typeLabel = new JLabel("<html><a style='font-size: 10px;'>"+e.getType()+"</a></html>");
				labelPanel.add(typeLabel, BorderLayout.NORTH);
				JLabel valueLabel = new JLabel();
				valueLabel.setText("<html><body style='width: 220px'><a style='font-size: "+((e.getType().equals(Element.HEADER) || e.getType().equals(Element.SUBHEADER))?20:12)+"px;'>"+e.getValue().replaceAll("\n", "<br>\n")+"</a></body></html>");
				labelPanel.add(valueLabel, BorderLayout.SOUTH);
			panel.add(labelPanel, BorderLayout.CENTER);
			
			JPanel sidePanel = new JPanel(new BorderLayout());
			sidePanel.setBackground(transparentColor);
			sidePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
				JPanel deleteButtonPanel = new JPanel(new FlowLayout());
				deleteButtonPanel.setBackground(transparentColor);
					JButton deleteButton = new JButton("<html><a style='font-size: 15px;'>x</a></html>");
					deleteButton.addActionListener(controller);
					deleteButton.setActionCommand(WCCController.elementDelete + ":false:" + parent.getFilename() + ":" + elementIndex);
					deleteButton.setToolTipText("Element l�schen");
					deleteButton.setFocusable(false);
					deleteButton.setContentAreaFilled(false);
					deleteButton.setOpaque(true);
					deleteButton.setBackground(transparentColor);
					deleteButton.setMargin(new Insets(0, 0, 0, 0));
					deleteButton.setPreferredSize(new Dimension(15, 15));
					deleteButtonPanel.add(deleteButton);
				sidePanel.add(deleteButtonPanel, BorderLayout.EAST);
			panel.add(sidePanel, BorderLayout.EAST);
		panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, labelPanel.getPreferredSize().height+10));
		
		
		MouseListener hoverListener = new MouseListener() {
			@Override public void mouseReleased(MouseEvent e) {}
			@Override public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {
				if(selectedPage == -1 || (selectedPage != -1 && pageList.getComponent(selectedPage) != panel)) {
					panel.setBackground(backgroundColor);
					valueLabel.setBackground(backgroundColor);
				}
				panel.repaint();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				if(selectedPage == -1 || (selectedPage != -1 && pageList.getComponent(selectedPage) != panel)) {
					panel.setBackground(hoverColor);
					valueLabel.setBackground(hoverColor);
				}
				panel.repaint();
			}
			@Override public void mouseClicked(MouseEvent e) {}
		};
		panel.addMouseListener(hoverListener);
		deleteButton.addMouseListener(hoverListener);
		JPopupMenu elementContextMenu = new JPopupMenu();
		JMenu changeTypeMenu = createMenu("Typ �ndern zu:", null);
			changeTypeMenu.add(createMenuItem("�berschrift", null, WCCController.elementChangeTypeToHeader+":"+parent.getFilename()+":"+elementIndex));
			changeTypeMenu.add(createMenuItem("Unter�berschrift", null, WCCController.elementChangeTypeToSubheader+":"+parent.getFilename()+":"+elementIndex));
			changeTypeMenu.add(createMenuItem("Textinhalt", null, WCCController.elementChangeTypeToText+":"+parent.getFilename()+":"+elementIndex));
			changeTypeMenu.add(createMenuItem("Bild", null, WCCController.elementChangeTypeToImage+":"+parent.getFilename()+":"+elementIndex));
		elementContextMenu.add(changeTypeMenu);
		elementContextMenu.add(createMenuItem("Daten �ndern", null, WCCController.elementChangeData+":"+parent.getFilename()+":"+elementIndex));
		elementContextMenu.addSeparator();
		elementContextMenu.add(createMenuItem("an den Anfang bewegen", null, WCCController.elementMoveTop+":"+parent.getFilename()+":"+elementIndex));
		elementContextMenu.add(createMenuItem("Nach oben bewegen", null, WCCController.elementMoveUp+":"+parent.getFilename()+":"+elementIndex));
		elementContextMenu.add(createMenuItem("Nach unten bewegen", null, WCCController.elementMoveDown+":"+parent.getFilename()+":"+elementIndex));
		elementContextMenu.add(createMenuItem("An das Ende bewegen", null, WCCController.elementMoveBottom+":"+parent.getFilename()+":"+elementIndex));
		elementContextMenu.addSeparator();
		elementContextMenu.add(createMenuItem("Element l�schen", null, WCCController.elementDelete+":false:"+parent.getFilename()+":"+elementIndex));
		MouseListener clickListener = new MouseListener() {
			@Override 
			public void mouseReleased(MouseEvent e) {
				if(e.isPopupTrigger())
					elementContextMenu.show(panel, e.getX(), e.getY());
			}
			@Override
			public void mousePressed(MouseEvent e) {
				if(e.isPopupTrigger())
					elementContextMenu.show(panel, e.getX(), e.getY());
			}
			@Override public void mouseExited(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseClicked(MouseEvent e) {}
		};
		panel.addMouseListener(clickListener);
		return panel;
	}
	
	public static void update(Observable o, Object arg) {
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
		WCCModel.getSettings().setLocation(f.getLocation());
		WCCModel.getSettings().setSize(f.getSize());
		WCCModel.getSettings().setFullscreen(f.getExtendedState() == JFrame.MAXIMIZED_BOTH);
		WCCModel.getSettings().setDividerLocation(mainPanel.getDividerLocation());
	}
	
	public void setVisible(boolean b) {
		f.setVisible(b);
	}
	
	public static void showMessage(String message, String title, int type) {
		JOptionPane.showMessageDialog(f, message, title, type);
	}
	
	public static void askForSaveBeforeExit() {
		switch(JOptionPane.showConfirmDialog(f, "Es gibt nicht gespeicherte �nderungen. \nVor dem Schlie�en speichern?")) {
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

	public static void askForNewPageData(String previousFilename, String previousName) {
		JPanel myPanel = new JPanel(new GridLayout(0, 2));
		myPanel.add(new JLabel("Name:"));
		JTextField nameField = new JTextField(previousName, 20);
		myPanel.add(nameField);
		myPanel.add(new JLabel("Dateiname:"));
		JTextField filenameField = new JTextField(previousFilename, 20);
		myPanel.add(filenameField);
		
		int result = JOptionPane.showConfirmDialog(f, myPanel, "Bitte einen Dateinamen und einen Namen f�r die Seite angeben:", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			controller.actionPerformed(new ActionEvent(f, ActionEvent.ACTION_PERFORMED, WCCController.pageNewPage+":"+filenameField.getText()+":"+nameField.getText()));
		}
	}
	
	public static void requestChangePageData(String oldFilename, String oldName) {
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

	public static void askForDeletePage(String filename) {
		int result = JOptionPane.showConfirmDialog(f, "Sind Sie sicher, dass sie die Seite '"+filename+"' l�schen wollen?", "Seite l�schen?", JOptionPane.YES_NO_OPTION);
		if(result == JOptionPane.YES_OPTION) {
			controller.actionPerformed(new ActionEvent(f, ActionEvent.ACTION_PERFORMED, WCCController.pageDelete+":true:"+filename));
		}
	}
	
	public void askForDeleteElement(String filename, int elementIndex) {
		int result = JOptionPane.showConfirmDialog(f, "Sind Sie sicher, dass Sie Element "+elementIndex+" der Seite '"+filename+"' l�schen wollen?", "Element l�schen?", JOptionPane.YES_NO_OPTION);
		if(result == JOptionPane.YES_OPTION) {
			controller.actionPerformed(new ActionEvent(f, ActionEvent.ACTION_PERFORMED, WCCController.elementDelete+":true:"+filename+":"+elementIndex));
		}
	}

	public static void requestNewHeaderData(String parentFilename) {
		String result = JOptionPane.showInputDialog(f, "Neue �berschrift eingeben:", "Neue �berschrift", JOptionPane.QUESTION_MESSAGE);
		if(result != null && !result.equals("")) {
			controller.actionPerformed(new ActionEvent(f, ActionEvent.ACTION_PERFORMED, WCCController.pageNewHeader+":"+parentFilename+":"+result));
		}
	}

	public static void requestNewSubheaderData(String parentFilename) {
		String result = JOptionPane.showInputDialog(f, "Neue Unter�berschrift eingeben:", "Neue Unter�berschrift", JOptionPane.QUESTION_MESSAGE);
		if(result != null && !result.equals("")) {
			controller.actionPerformed(new ActionEvent(f, ActionEvent.ACTION_PERFORMED, WCCController.pageNewSubheader+":"+parentFilename+":"+result));
		}
	}

	public static void requestNewTextData(String parentFilename) {
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

	public static void requestNewImageData(String parentFilename) {
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
				JOptionPane.showInternalMessageDialog(f, "Die ausgew�hlte Datei existiert nicht!", "Datei existiert nicht", JOptionPane.WARNING_MESSAGE);
				requestNewImageData(parentFilename);
			}
		}
	}

	public void requestChangeElementData(String parentFilename, int elementIndex) {
		Page parent = model.getDataStorage().get(model.getDataStorage().indexOf(parentFilename));
		Element e = parent.get(elementIndex);
		int result;
		
		JPanel myPanel = new JPanel(new BorderLayout());
		myPanel.add(new JLabel("Neue Daten f�r Element angeben:"), BorderLayout.NORTH);
		JPanel centerPanel = new JPanel(new FlowLayout());
		myPanel.add(centerPanel, BorderLayout.CENTER);
		
		switch(e.getType()) {
		case Element.HEADER:
			JTextField headerField = new JTextField(e.getValue(), 30);
			centerPanel.add(headerField);
			result = JOptionPane.showConfirmDialog(f, myPanel, "Neue Daten f�r Element", JOptionPane.OK_CANCEL_OPTION);
			if(result == JOptionPane.OK_OPTION) {
				controller.actionPerformed(new ActionEvent(f, ActionEvent.ACTION_PERFORMED, WCCController.elementChangeData+":"+parentFilename+":"+elementIndex+":"+headerField.getText()));
			}
			break;
		case Element.SUBHEADER:
			JTextField subheaderField = new JTextField(e.getValue(), 30);
			centerPanel.add(subheaderField);
			result = JOptionPane.showConfirmDialog(f, myPanel, "Neue Daten f�r Element", JOptionPane.OK_CANCEL_OPTION);
			if(result == JOptionPane.OK_OPTION) {
				controller.actionPerformed(new ActionEvent(f, ActionEvent.ACTION_PERFORMED, WCCController.elementChangeData+":"+parentFilename+":"+elementIndex+":"+subheaderField.getText()));
			}
			break;
		case Element.TEXT:
			JTextArea textarea = new JTextArea(e.getValue(), 10, 60);
			textarea.setLineWrap(true);
			textarea.setWrapStyleWord(true);
			centerPanel.add(new JScrollPane(textarea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
			result = JOptionPane.showConfirmDialog(f, myPanel, "Neue Daten f�r Element", JOptionPane.OK_CANCEL_OPTION);
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
				System.out.println("OK gew�hlt");
				if(filechooser.getSelectedFile() != null && filechooser.getSelectedFile().exists()) {
					System.out.println("Datei existiert: "+filechooser.getSelectedFile().toString());
					model.getSettings().setImageChooseLocation((filechooser.getSelectedFile().isDirectory())?filechooser.getSelectedFile().getAbsolutePath():filechooser.getSelectedFile().getParent());
					controller.actionPerformed(new ActionEvent(f, ActionEvent.ACTION_PERFORMED, WCCController.elementChangeData+":"+parentFilename+":"+elementIndex+":"+filechooser.getSelectedFile().getAbsolutePath()));
				} else {
					System.out.println("Datei existiert nicht");
					JOptionPane.showMessageDialog(f, "Die ausgew�hlte Datei existiert nicht!", "Datei existiert nicht", JOptionPane.WARNING_MESSAGE);
					requestNewImageData(parentFilename);
				}
			}
			break;
		}
	}

	public static void askForExportDestination() {
		// TODO Auto-generated method stub
		
	}

}
