package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import base.WCCView;
import data.Element;
import data.Page;

@SuppressWarnings({ "unused", "serial" })
public abstract class WCCListItem extends JPanel {

	private boolean selected;

	private WCCListItem() {}
	private WCCListItem(LayoutManager arg0) {}
	private WCCListItem(boolean arg0) {}
	private WCCListItem(LayoutManager arg0, boolean arg1) {}

	public WCCListItem(String topText, String bottomText, String sideText) {
		super(new BorderLayout());
		selected = false;
		setBackground(WCCView.backgroundColor);
		setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		//setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
		addMouseListener(hoverListener);
		//setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 0));
		createContent(topText, bottomText, sideText);
	}

	private MouseListener hoverListener = new MouseListener() {
		@Override
		public void mouseReleased(MouseEvent e) {}
		@Override
		public void mousePressed(MouseEvent e) {}
		
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
	
	private void createContent(String topText, String bottomText, String sideText) {
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(WCCView.transparentColor);
			JPanel topTextContainer = new JPanel(new FlowLayout());
			topTextContainer.setBackground(WCCView.transparentColor);
				topTextContainer.add(new JLabel(topText));
		topPanel.add(topTextContainer, BorderLayout.CENTER);
		if(sideText != null && !sideText.equals(""))
			topPanel.add(new JLabel(sideText), BorderLayout.EAST);
		add(topPanel, BorderLayout.NORTH);
		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setBackground(WCCView.transparentColor);
			JPanel bottomTextContainer = new JPanel(new FlowLayout());
			bottomTextContainer.setBackground(WCCView.transparentColor);
				bottomTextContainer.add(new JLabel(bottomText));
		bottomPanel.add(bottomTextContainer, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.CENTER);
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
	
}