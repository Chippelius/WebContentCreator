package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.event.ChangeListener;
import javax.swing.text.Caret;
import javax.swing.text.JTextComponent;

import contoller.WCCController;

@SuppressWarnings({ "unused", "serial" })
public class WCCListItem extends JPanel {

	private static final int primaryFontSize = 15;
	private static final int secondaryFontSize = 10;
	private static final int headerFontSize = 20;
	private static final int subheaderFontSize = 15;
	private static final int textFontSize = 12;
	private static final int imageFontSize = 12;
	
	private boolean selected;
	private boolean isPage;
	private int index;
	private JPopupMenu contextMenu;
	private WCCListItem selfRef;

	private WCCListItem() {}
	private WCCListItem(LayoutManager arg0) {}
	private WCCListItem(boolean arg0) {}
	private WCCListItem(LayoutManager arg0, boolean arg1) {}

	public WCCListItem(int ownIndex, String topText, String bottomText, boolean isPage) {
		super(new BorderLayout());
		this.isPage = isPage;
		selected = false;
		index = ownIndex;
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		selfRef = this;
		contextMenu = isPage?getPageContextMenu():getElementContextMenu();
		addMouseListener(listener);
		setBackground(WCCView.backgroundColor);
		setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
		JLabel textLabel = new JLabel();
		if(isPage) {
			textLabel.setText("<html><a style='font-size: "+secondaryFontSize+"px;'>"+topText+"</a><br>"
				+ "<a style='font-size: "+primaryFontSize+"px;'>"+bottomText+"</a></html>");
		} else {
			switch(topText) {
			case "Überschrift":
				textLabel.setText("<html><a style='font-size: "+secondaryFontSize+"px;'>"+topText+":</a><br>"
						+ "<a style='font-size: "+headerFontSize+"px;'>"+bottomText+"</a></html>");
				break;
			case "Unterüberschrift":
				textLabel.setText("<html><a style='font-size: "+secondaryFontSize+"px;'>"+topText+":</a><br>"
						+ "<a style='font-size: "+subheaderFontSize+"px;'>"+bottomText+"</a></html>");
				break;
			case "Textinhalt":
				textLabel.setText("<html><a style='font-size: "+secondaryFontSize+"px;'>"+topText+":</a><br>"
						+ "<a style='font-size: "+textFontSize+"px;'>"+bottomText.replaceAll("\n", "<br>")+"</a></html>");
				break;
			case "Bild":
				textLabel.setText("<html><a style='font-size: "+secondaryFontSize+"px;'>"+topText+":</a><br>"
						+ "<a style='font-size: "+imageFontSize+"px;'>"+bottomText.replaceAll("\n", "<br>")+"</a></html>");
				break;
			default:
				textLabel.setText("<html><a style='font-size: "+secondaryFontSize+"px;'>"+topText+":</a><br>"
						+ "<a style='font-size: "+primaryFontSize+"px;'>"+bottomText+"</a></html>");
			}
		}
		textLabel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
		add(textLabel);
	}

	public void setSelected(boolean newStatus) {
		if(newStatus) {
			selected = true;
			setBackground(WCCView.selectedColor);
		} else {
			selected = false;
			setBackground(WCCView.backgroundColor);
		}
	}
	
	private MouseListener listener = new MouseListener() {
		@Override
		public void mouseReleased(MouseEvent e) {
			if(e.isPopupTrigger())
				contextMenu.show(selfRef, e.getX(), e.getY());
		}
		@Override
		public void mousePressed(MouseEvent e) {
			if(isPage)
				WCCController.setSelectedPage(index);
			else
				WCCController.setSelectedElement(index);
			if(e.isPopupTrigger())
				contextMenu.show(selfRef, e.getX(), e.getY());
		}
		@Override
		public void mouseExited(MouseEvent e) {
			if(!selected) 
				setBackground(WCCView.backgroundColor);
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			if(!selected)
				setBackground(WCCView.hoverColor);
		}
		@Override
		public void mouseClicked(MouseEvent e) {}
	};

	private JPopupMenu getPageContextMenu() {
		JPopupMenu pageContextMenu = new JPopupMenu();
		pageContextMenu.add(new WCCMenuItem(WCCController.pageChangeData));
		pageContextMenu.addSeparator();
		pageContextMenu.add(new WCCMenuItem(WCCController.pageMoveTop));
		pageContextMenu.add(new WCCMenuItem(WCCController.pageMoveUp));
		pageContextMenu.add(new WCCMenuItem(WCCController.pageMoveDown));
		pageContextMenu.add(new WCCMenuItem(WCCController.pageMoveBottom));
		pageContextMenu.addSeparator();
		pageContextMenu.add(new WCCMenuItem(WCCController.pageDelete));
		return pageContextMenu;
	}

	private static JPopupMenu getElementContextMenu() {
		JPopupMenu elementContextMenu = new JPopupMenu();
		elementContextMenu.add(new WCCMenuItem(WCCController.elementChangeValue));
		WCCMenu changeTypeMenu = new WCCMenu("Elementtyp ändern");
		changeTypeMenu.add(new WCCMenuItem(WCCController.elementChangeToHeader));
		changeTypeMenu.add(new WCCMenuItem(WCCController.elementChangeToSubheader));
		changeTypeMenu.add(new WCCMenuItem(WCCController.elementChangeToText));
		changeTypeMenu.add(new WCCMenuItem(WCCController.elementChangeToImage));
		elementContextMenu.add(changeTypeMenu);
		elementContextMenu.addSeparator();
		elementContextMenu.add(new WCCMenuItem(WCCController.elementMoveTop));
		elementContextMenu.add(new WCCMenuItem(WCCController.elementMoveUp));
		elementContextMenu.add(new WCCMenuItem(WCCController.elementMoveDown));
		elementContextMenu.add(new WCCMenuItem(WCCController.elementMoveBottom));
		elementContextMenu.addSeparator();
		elementContextMenu.add(new WCCMenuItem(WCCController.elementDelete));
		return elementContextMenu;
	}
	
}