package gui;

import java.awt.Cursor;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

public class WCCButton extends JButton {

	private WCCButton() {}
	private WCCButton(Icon arg0) {}
	private WCCButton(String arg0) {}
	private WCCButton(String arg0, Icon arg1) {}
	
	public WCCButton(Action a) {
		super(a);
		setText("");
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
	

}
