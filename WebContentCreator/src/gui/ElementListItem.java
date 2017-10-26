package gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPopupMenu;

import base.WCCView;
import data.Element;

@SuppressWarnings("serial")
public class ElementListItem extends WCCListItem {

	private static JPopupMenu contextMenu = getContextMenu();

	private Element reference;

	public ElementListItem(Element element) {
		super(createTypeText(element.getType()), createValueText(element.getValue(), element.getType()), "");
		reference = element;
		addMouseListener(clickListener);
	}

	private static String createTypeText(String input) {
		return "<html><a style='font-size: 10px;'>"+input+"</a></html>";
	}

	private static String createValueText(String input, String type) {
		switch (type) {
		case Element.HEADER:
			return "<html><body style='width: 220px'><a style='font-size: 20px;'>"+input.replaceAll("\n", "<br>\n")+"</a></body></html>";
		case Element.SUBHEADER:
			return "<html><body style='width: 220px'><a style='font-size: 16px;'>"+input.replaceAll("\n", "<br>\n")+"</a></body></html>";
		case Element.TEXT:
			return "<html><body style='width: 220px'><a style='font-size: 12px;'>"+input.replaceAll("\n", "<br>\n")+"</a></body></html>";
		case Element.IMAGE:
			return "<html><body style='width: 220px'><a style='font-size: 12px;'>"+input.replaceAll("\n", "<br>\n")+"</a></body></html>";
		default:
			return "";
		}
	}

	private MouseListener clickListener = new MouseListener() {
		@Override
		public void mouseReleased(MouseEvent e) {
			if(e.isPopupTrigger())
				contextMenu.show(null, e.getX(), e.getY());
		}

		@Override
		public void mousePressed(MouseEvent e) {
			WCCView.setSelectedElement(reference);
			if(e.isPopupTrigger())
				contextMenu.show(null, e.getX(), e.getY());
		}

		@Override
		public void mouseExited(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseClicked(MouseEvent e) {}
	};

	private static JPopupMenu getContextMenu() {
		JPopupMenu elementContextMenu = new JPopupMenu();
		elementContextMenu.add(new WCCMenuItem(Actions.elementChangeValue));
		WCCMenu changeTypeMenu = new WCCMenu("Elementtyp ändern");
		changeTypeMenu.add(new WCCMenuItem(Actions.elementChangeToHeader));
		changeTypeMenu.add(new WCCMenuItem(Actions.elementChangeToSubheader));
		changeTypeMenu.add(new WCCMenuItem(Actions.elementChangeToText));
		changeTypeMenu.add(new WCCMenuItem(Actions.elementChangeToImage));
		elementContextMenu.add(changeTypeMenu);
		elementContextMenu.addSeparator();
		elementContextMenu.add(new WCCMenuItem(Actions.elementMoveTop));
		elementContextMenu.add(new WCCMenuItem(Actions.elementMoveUp));
		elementContextMenu.add(new WCCMenuItem(Actions.elementMoveDown));
		elementContextMenu.add(new WCCMenuItem(Actions.elementMoveBottom));
		elementContextMenu.addSeparator();
		elementContextMenu.add(new WCCMenuItem(Actions.elementDelete));
		return elementContextMenu;
	}

}
