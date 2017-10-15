package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import base.*;
import data.Element;

@SuppressWarnings("serial")
public class WCCWindow extends JFrame {

	private static JSplitPane mainPanel;
	private static JPanel pageList, elementList;
	private static int selectedPage;
	private static ArrayList<AbstractButton> pageDependentButtons;
	
	public WCCWindow(String title) {
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
				WCCController.fileExit();
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
		getContentPane().add(new WCCToolbar(), BorderLayout.NORTH);
		getContentPane().add(createMainPanel(), BorderLayout.CENTER);
		
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
					deleteButton.setToolTipText("Seite löschen");
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
		pageContextMenu.add(createMenuItem("Daten ändern", null, WCCController.pageChangeData+":false:"+p.getFilename()+":"+p.getName()));
		pageContextMenu.addSeparator();
		pageContextMenu.add(createMenuItem("an den Anfang bewegen", null, WCCController.pageMoveTop+":"+p.getFilename()));
		pageContextMenu.add(createMenuItem("Nach oben bewegen", null, WCCController.pageMoveUp+":"+p.getFilename()));
		pageContextMenu.add(createMenuItem("Nach unten bewegen", null, WCCController.pageMoveDown+":"+p.getFilename()));
		pageContextMenu.add(createMenuItem("An das Ende bewegen", null, WCCController.pageMoveBottom+":"+p.getFilename()));
		pageContextMenu.addSeparator();
		pageContextMenu.add(createMenuItem("Seite löschen", null, WCCController.pageDelete+":false:"+p.getFilename()));
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
					deleteButton.setToolTipText("Element löschen");
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
		JMenu changeTypeMenu = createMenu("Typ ändern zu:", null);
			changeTypeMenu.add(createMenuItem("Überschrift", null, WCCController.elementChangeTypeToHeader+":"+parent.getFilename()+":"+elementIndex));
			changeTypeMenu.add(createMenuItem("Unterüberschrift", null, WCCController.elementChangeTypeToSubheader+":"+parent.getFilename()+":"+elementIndex));
			changeTypeMenu.add(createMenuItem("Textinhalt", null, WCCController.elementChangeTypeToText+":"+parent.getFilename()+":"+elementIndex));
			changeTypeMenu.add(createMenuItem("Bild", null, WCCController.elementChangeTypeToImage+":"+parent.getFilename()+":"+elementIndex));
		elementContextMenu.add(changeTypeMenu);
		elementContextMenu.add(createMenuItem("Daten ändern", null, WCCController.elementChangeData+":"+parent.getFilename()+":"+elementIndex));
		elementContextMenu.addSeparator();
		elementContextMenu.add(createMenuItem("an den Anfang bewegen", null, WCCController.elementMoveTop+":"+parent.getFilename()+":"+elementIndex));
		elementContextMenu.add(createMenuItem("Nach oben bewegen", null, WCCController.elementMoveUp+":"+parent.getFilename()+":"+elementIndex));
		elementContextMenu.add(createMenuItem("Nach unten bewegen", null, WCCController.elementMoveDown+":"+parent.getFilename()+":"+elementIndex));
		elementContextMenu.add(createMenuItem("An das Ende bewegen", null, WCCController.elementMoveBottom+":"+parent.getFilename()+":"+elementIndex));
		elementContextMenu.addSeparator();
		elementContextMenu.add(createMenuItem("Element löschen", null, WCCController.elementDelete+":false:"+parent.getFilename()+":"+elementIndex));
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
	

}
