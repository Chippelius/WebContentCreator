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
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		addMouseListener(hoverListener);
		createContent(topText, bottomText, sideText);
	}

	private void createContent(String topText, String bottomText, String sideText) {
		setBackground(WCCView.backgroundColor);
		setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
	
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(WCCView.transparentColor);
		topPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
			JPanel topTextContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
			topTextContainer.setBackground(WCCView.transparentColor);
				JLabel topTextLabel = new JLabel(topText, JLabel.LEFT);
				topTextLabel.setBackground(WCCView.transparentColor);
			topTextContainer.add(topTextLabel);
		topPanel.add(topTextContainer, BorderLayout.CENTER);
		if(sideText != null && !sideText.equals(""))
			topPanel.add(new JLabel(sideText), BorderLayout.EAST);
		add(topPanel, BorderLayout.NORTH);
		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setBackground(WCCView.transparentColor);
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 10));
			JPanel bottomTextContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
			bottomTextContainer.setBackground(WCCView.transparentColor);
				JLabel bottomTextLabel = new JLabel(bottomText, JLabel.LEFT);
				bottomTextLabel.setBackground(WCCView.transparentColor);
			bottomTextContainer.add(bottomTextLabel);
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
	
}