package gui;

import javax.swing.JToolBar;

@SuppressWarnings("serial")
public class WCCToolbar extends JToolBar {

	private WCCToolbar(int arg0) {}
	private WCCToolbar(String arg0) {}
	private WCCToolbar(String arg0, int arg1) {}

	public WCCToolbar() {
		super("WCC Toolbar");
		
		add(new WCCButton(Actions.fileSave));
		addSeparator();
		add(new WCCButton(Actions.fileExport));
		//TODO
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
			popupButtonNew.add(createPageDependentMenuItem("Neue Überschrift", new ImageIcon("icons/headerIcon.png"), WCCController.pageNewHeader+":"));
			popupButtonNew.add(createPageDependentMenuItem("Neue Unterüberschrift", new ImageIcon("icons/subheaderIcon.png"), WCCController.pageNewSubheader+":"));
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
		toolbar.add(createPageDependentToolbarButton(new ImageIcon("icons/headerIcon.png"), WCCController.pageNewHeader+":", "Neue Überschrift"));
		toolbar.add(createPageDependentToolbarButton(new ImageIcon("icons/subheaderIcon.png"), WCCController.pageNewSubheader+":", "Neue Unterüberschrift"));
		toolbar.add(createPageDependentToolbarButton(new ImageIcon("icons/textIcon.png"), WCCController.pageNewText+":", "Neuer Textinhalt"));
		toolbar.add(createPageDependentToolbarButton(new ImageIcon("icons/imageIcon.png"), WCCController.pageNewImage+":", "Neues Bild"));
		
		return toolbar;
	}
	
}
