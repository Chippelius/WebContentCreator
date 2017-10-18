package gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPopupMenu;

import base.WCCView;
import data.Page;

@SuppressWarnings("serial")
public class PageListItem extends WCCListItem {

	private static JPopupMenu contextMenu = getContextMenu();
	
	//Reference to the page this item is representing
	private Page reference;

	public PageListItem(Page page) {
		super(createNameText(page.getName()), createFilenameText(page.getFilename()), page.getVersion()+"");
		reference = page;
		addMouseListener(clickListener);
	}

	private static String createNameText(String input) {
		return "<html><a style='font-size: 15px;'>"+input+"</a></html>";
	}

	private static String createFilenameText(String input) {
		return "<html>"+input+"</html>";
	}

	private MouseListener clickListener = new MouseListener() {
		@Override
		public void mouseReleased(MouseEvent e) {
			if(e.isPopupTrigger())
				contextMenu.show(getParent(), e.getX(), e.getY());
		}

		@Override
		public void mousePressed(MouseEvent e) {
			WCCView.setSelectedPage(reference);
			if(e.isPopupTrigger())
				contextMenu.show(getParent(), e.getX(), e.getY());
		}
		
		@Override
		public void mouseExited(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseClicked(MouseEvent e) {}
	};

	private static JPopupMenu getContextMenu() {
		JPopupMenu pageContextMenu = new JPopupMenu();
		pageContextMenu.add(new WCCMenuItem(Actions.pageChangeData));
		pageContextMenu.addSeparator();
		pageContextMenu.add(new WCCMenuItem(Actions.pageMoveTop));
		pageContextMenu.add(new WCCMenuItem(Actions.pageMoveUp));
		pageContextMenu.add(new WCCMenuItem(Actions.pageMoveDown));
		pageContextMenu.add(new WCCMenuItem(Actions.pageMoveBottom));
		pageContextMenu.addSeparator();
		pageContextMenu.add(new WCCMenuItem(Actions.pageDelete));
		return pageContextMenu;
	}

}
