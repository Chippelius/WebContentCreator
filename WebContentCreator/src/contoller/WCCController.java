package contoller;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import model.Page;
import model.WCCModel;
import view.WCCView;

/**
 * Controller part of WebContentCreator (by concept of ModelViewController) <br>
 * <br>
 * Created by Leo Köberlein on 09.07.2017
 * 
 * @author Leo Köberlein
 * @see {@link model.WCCModel}, {@link view.WCCView}
 */
@SuppressWarnings("serial")
public class WCCController {

	//public static final double VERSION = 0.3;

	/**
	 * The currently selected page/element.<br>
	 * If no page/element is currently selected, the value is -1.
	 */
	private static int selectedPage = -1;
	private static volatile boolean enableContentListeners = true;
	
	/**
	 * Entry point for the program.
	 * 
	 * @param args Not currently used.
	 */
	public static void main(String[] args) {
		enablePageDependentActions(false);
		WCCModel.loadSettings();
		WCCView.applySettings(WCCModel.getSettings());
		if(WCCModel.getSettings().getCurrentlyOpenedFile()==null
				||WCCModel.getSettings().getCurrentlyOpenedFile().equals("")) {
			WCCModel.newDataStorage();
		} else {
			try {
				WCCModel.loadDataStorage(new File(WCCModel.getSettings().getCurrentlyOpenedFile()));
			} catch (Exception e) {
				WCCView.showErrorMessage("Unable to reload last session: \n\n" + e.getMessage());
				WCCModel.newDataStorage();
			}
		}
		refreshView();
		WCCView.setVisible(true);
	}
	







	/**
	 * The action to be fired when a new project should be created.
	 */
	public static final AbstractAction fileNew = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent a) {
			if(WCCModel.getDataStorage().isEditedSinceLastSave()) {
				switch (WCCView.requestSaveBeforeExit()) {
				case WCCView.CANCEL_OPTION:
					return;
				case WCCView.YES_OPTION:
					fileSave.actionPerformed(null);
				}
			}
			WCCModel.newDataStorage();
			refreshView();
		}
	};

	/**
	 * The action to be fired when an existing project should be opened.
	 */
	public static final AbstractAction fileOpen = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent a) {
			if(WCCModel.getDataStorage().isEditedSinceLastSave()) {
				switch (WCCView.requestSaveBeforeExit()) {
				case WCCView.CANCEL_OPTION:
					return;
				case WCCView.YES_OPTION:
					fileSave.actionPerformed(null);
				}
			}
			File fileToOpen = WCCView.requestFileToOpen(WCCModel.getSettings().getOpenLocation());
			if(fileToOpen!=null&&!fileToOpen.getAbsolutePath().equals("")) {
				try {
					WCCModel.getSettings().setOpenLocation(fileToOpen.getParent());
					WCCModel.loadDataStorage(fileToOpen);
					refreshView();
				} catch (Exception e) {
					e.printStackTrace();
					WCCView.showErrorMessage("Unable to load file "+fileToOpen.getAbsolutePath()+": \n\n" + e.getMessage());
				}
			}
		}
	};

	/**
	 * The action to be fired when the current state of the project should be saved in the associated file. <br>
	 * Calls fileSaveAs if this project has no file associated with it (if it hasn't been saved before).
	 */
	public static final AbstractAction fileSave = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent a) {
			if(WCCModel.getSettings().getCurrentlyOpenedFile()==null) {
				fileSaveAs.actionPerformed(null);
			} else {
				try {
					WCCModel.saveDataStorage(new File(WCCModel.getSettings().getCurrentlyOpenedFile()));
					refreshView();
				} catch (Exception e) {
					e.printStackTrace();
					WCCView.showErrorMessage("Error occurred while saving: \n\n" + e.getMessage());
				}
			}
		}
	};

	/**
	 * The action to be fired when the current state of the project should be saved in a specific file. 
	 * (Other than the one associated with it.) <br>
	 * Will ask the user for the destination-file.
	 */
	public static final AbstractAction fileSaveAs = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			File fileToSave = WCCView.requestFileToSave(WCCModel.getSettings().getSaveLocation());
			if(fileToSave!=null&&!fileToSave.getAbsolutePath().equals("")) {
				if(!fileToSave.getName().endsWith(".xml"))
					fileToSave = new File(fileToSave.toString()+".xml");
				if((!fileToSave.exists())||WCCView.confirmFileOverride(fileToSave)) {
					try {
						WCCModel.saveDataStorage(fileToSave);
						refreshView();
					} catch (Exception e1) {
						e1.printStackTrace();
						WCCView.showErrorMessage("Error occurred while saving: \n\n" + e1.getMessage());
					}
				}
			}
		}
	};

	/**
	 * The action to be fired when the current state of the project shoud be exportet into html code.
	 */
	public static final AbstractAction fileExport = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent a) {
			try {
				enableExportActions(false);
				new Thread(new ExportController()).start();
			} catch(Exception e) {
				e.printStackTrace();
				WCCView.showErrorMessage("Error occurred during export: \n\n" + e.getMessage());
			}
		}
	};

	/**
	 * The action to be fired when the qr codes linking to the pages should be created.
	 */
	public static final AbstractAction fileGenerateQRCodes = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				enableQRActions(false);
				String s = WCCView.requestQRBaseUrl(WCCModel.getSettings().getQRCodeBaseUrl());
				if(s!=null) {
					WCCModel.getSettings().setQRCodeBaseUrl(s);
					QRController.generateQRCodes(WCCModel.qrFolder, s, 750);
					WCCView.showInformationMessage("QR-Codes erfolgreich generiert.");
					Desktop.getDesktop().open(WCCModel.qrFolder);
				}
			} catch(Exception e1) {
				e1.printStackTrace();
				WCCView.showErrorMessage("Error occurred while generating qr-codes: \n\n" + e1.getMessage());
			} finally {
				enableQRActions(true);
			}
		}
	};

	/**
	 * The action to be fired when the current project should be closed. <br>
	 * Closes the whole program if the project is closed.
	 */
	public static final AbstractAction fileExit = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent a) {
			if(WCCModel.getDataStorage().isEditedSinceLastSave()) {
				switch (WCCView.requestSaveBeforeExit()) {
				case WCCView.CANCEL_OPTION:
					return;
				case WCCView.YES_OPTION:
					fileSave.actionPerformed(null);
				}
			}
			WCCView.fetchSettings(WCCModel.getSettings());
			WCCModel.saveSettings();
			System.exit(0);
		}
	};

	/**
	 * The action to be fired when a new page should be created. <br>
	 * Will ask the user for required data.
	 */
	public static final AbstractAction pageNew = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent a) {
			String[] res = WCCView.requestNewPageData(null, null);
			if(res==null)
				return;
			while(!WCCModel.getDataStorage().isValidFilename(res[0])) {
				WCCView.showErrorInvalidFilename(res[0]);
				res = WCCView.requestNewPageData(res[0], res[1]);
				if(res==null)
					return;
			}
			WCCModel.getDataStorage().add(new Page(res[0], res[1]));
			selectedPage = WCCModel.getDataStorage().size()-1;
			refreshView();
		}
	};
	
	/**
	 * The action to be fired when a page should be selected.<br>
	 * The page-number is passed via the action command. <br>
	 * If the passed number is -1, every page will be deselected.
	 */
	public static final AbstractAction pageSelect = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent a) {
			try {
				int pageNr = Integer.parseInt(a.getActionCommand());
				selectedPage = pageNr;
				refreshView();
			} catch(Exception e) {
				e.printStackTrace();
				WCCView.showErrorMessage("Error occurred while trying to select a page: \n\n" + e.getMessage());
			}
		}
	};

	/**
	 * The action to be fired when the attributes of the currently selected page should be modified. <br>
	 * Will ask the user for new data.
	 */
	public static final AbstractAction pageChangeData = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent a) {
			if(selectedPage == -1) {
				return;
			}
			String[] res = WCCView.requestNewPageData(WCCModel.getDataStorage().get(selectedPage).getFilename(), 
					WCCModel.getDataStorage().get(selectedPage).getName());
			if(res==null)
				return;
			while(!WCCModel.getDataStorage().isValidFilename(res[0])) {
				WCCView.showErrorInvalidFilename(res[0]);
				res = WCCView.requestNewPageData(res[0], res[1]);
				if(res==null)
					return;
			}
			WCCModel.getDataStorage().get(selectedPage).setFilename(res[0]);
			WCCModel.getDataStorage().get(selectedPage).setName(res[1]);
			refreshView();
		}
	};

	/**
	 * The action to be fired when the currently selected page should be moved to the top of the pagelist.
	 */
	public static final AbstractAction pageMoveTop = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent a) {
			if(selectedPage<0) {
				return;
			}
			WCCModel.getDataStorage().add(0, WCCModel.getDataStorage().remove(selectedPage));
			selectedPage = 0;
			refreshView();
		}
	};

	/**
	 * The action to be fired when the currently selected page should be moved to the bottom of the pagelist.
	 */
	public static final AbstractAction pageMoveBottom = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent a) {
			if(selectedPage==-1) {
				return;
			}
			WCCModel.getDataStorage().add(WCCModel.getDataStorage().remove(selectedPage));
			selectedPage = WCCModel.getDataStorage().size()-1;
			refreshView();
		}
	};

	/**
	 * The action to be fired when the currently selected page should be moved up in the pagelist.
	 */
	public static final AbstractAction pageMoveUp = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent a) {
			if(selectedPage==-1||selectedPage==0) {
				return;
			}
			WCCModel.getDataStorage().add(selectedPage-1, WCCModel.getDataStorage().remove(selectedPage));
			selectedPage--;
			refreshView();
		}
	};

	/**
	 * The action to be fired when the currently selected page should be moved down in the pagelist.
	 */
	public static final AbstractAction pageMoveDown = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent a) {
			if(selectedPage==-1||selectedPage==(WCCModel.getDataStorage().size()-1)) {
				return;
			}
			WCCModel.getDataStorage().add(selectedPage+1, WCCModel.getDataStorage().remove(selectedPage));
			selectedPage++;
			refreshView();
		}
	};

	/**
	 * The action to be fired when the currently selected page should be deleted.
	 */
	public static final AbstractAction pageDelete = new AbstractAction() { 
		@Override
		public void actionPerformed(ActionEvent a) {
			if(selectedPage==-1||!WCCView.confirmDeletePage(WCCModel.getDataStorage().get(selectedPage).getFilename(), 
					WCCModel.getDataStorage().get(selectedPage).getName())) {
				return;
			}
			WCCModel.getDataStorage().remove(selectedPage);
			selectedPage = -1;
			refreshView();
		}
	};
	
	/**
	 * The action to be fired when the content of the currently selected page changes.
	 */
	public static final AbstractAction pageChangeContent = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent a) {
			if(enableContentListeners && selectedPage != -1) {
				WCCModel.getDataStorage().get(selectedPage).setContent(a.getActionCommand());
			}
		}
	};
	/**
	 * The action to be fired when the caret position of the currently selected page changes.
	 */
	public static final AbstractAction pageChangeCaretPosition = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent a) {
			if(enableContentListeners && selectedPage != -1) {
				WCCModel.getDataStorage().get(selectedPage).setCaretPosition(Integer.parseInt(a.getActionCommand()));
			}
		}
	};

	/**
	 * The action to be fired when a new image should be created at the end of the currently selected page. <br>
	 * Will ask the user for required data.
	 */
	public static final AbstractAction elementNewImage = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent a) {
			if(selectedPage==-1) {
				return;
			}
			WCCView.insertContent("![Grafik]()", WCCModel.getDataStorage().get(selectedPage).getCaretPosition());
			WCCView.setCaretPosition(WCCModel.getDataStorage().get(selectedPage).getCaretPosition()-1);
		}
	};

	/**
	 * The action to be fired when the window shoud be maximized.
	 */
	public static final AbstractAction windowToggleMaximized = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent a) {
			WCCModel.getSettings().setMaximized(!WCCModel.getSettings().isMaximized());
			WCCView.applySettings(WCCModel.getSettings());
		}
	};

	/**
	 * The action to be fired when the window should return to the state of the first launch.
	 */
	public static final AbstractAction windowRestoreDefaultState = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			WCCModel.getSettings().restoreDefaultWindowState();
			WCCView.applySettings(WCCModel.getSettings());
		}
	};

	/**
	 * The action to be fired when the divider between the pagelist and elementlist shoud be centered.
	 */
	public static final AbstractAction windowCenterDivider = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent a) {
			WCCModel.getSettings().setDividerLocation(WCCView.getWidth()/2);
			WCCView.applySettings(WCCModel.getSettings());
		}
	};

	/**
	 * The action to be fired when the readme file should be opened.
	 */
	public static final AbstractAction helpInfo = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent a) {
			try {
				java.awt.Desktop.getDesktop().open(WCCModel.readmeFile);
			} catch (IOException e) {e.printStackTrace();}
		}
	};

	/**
	 * The action to be fired when the program should check for updates.
	 */
	public static final AbstractAction helpCheckForUpdates = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent a) {
			WCCView.showErrorMessage("Die automatische Update-Funktion ist noch nicht verfügbar.\n\n"
					+ "Wenn auf www.github.com/Chippelius/WebContentCreator/ eine neuere Version verfügbar ist,\n"
					+ "laden Sie diese bitte manuell herunter.");
		}
	};





	
	public static DocumentListener documentlistener = new DocumentListener() {
		@Override
		public void removeUpdate(DocumentEvent e) {
			update(e);
		}
		@Override
		public void insertUpdate(DocumentEvent e) {
			update(e);
		}
		private void update(DocumentEvent e) {
			//TODO
			
		}
		@Override public void changedUpdate(DocumentEvent e) {}
	};
	public static CaretListener caretListener = new CaretListener() {
		@Override
		public void caretUpdate(CaretEvent e) {
			if(selectedPage != -1) {
				WCCModel.getDataStorage().get(selectedPage).setCaretPosition(e.getDot());
			}
			//TODO
		}
	};
	
	
	
	
	
	
	/**
	 * Enables or disables actions regarding saving.
	 * 
	 * @param value whether save-dependent actions should be enabled or not
	 */
	private static void enableSaveDependentActions(boolean value) {
		fileSave.setEnabled(value);
	}

	/**
	 * Enables or disables actions regarding exporting.
	 * 
	 * @param value whether export-dependent actions should be enabled or not
	 */
	static void enableExportActions(boolean value) {
		fileExport.setEnabled(value);
	}

	/**
	 * Enables or disables actions regarding qr-code generation.
	 * 
	 * @param value whether qr-code-dependent actions should be enabled or not
	 */
	private static void enableQRActions(boolean value) {
		fileGenerateQRCodes.setEnabled(value);
	}

	private static final AbstractAction[] pageDependentActions = new AbstractAction[] {
			pageChangeData,
			pageMoveTop,
			pageMoveBottom,
			pageMoveUp,
			pageMoveDown,
			pageDelete,
			elementNewImage
	};

	/**
	 * Enables or disables actions regarding a specific page.
	 * 
	 * @param value whether page-dependent actions should be enabled or not
	 */
	private static void enablePageDependentActions(boolean value) {
		for(AbstractAction a : pageDependentActions) {
			a.setEnabled(value);
		}
	}

	
	
	
	
	
	private static void refreshView() {
		WCCView.setSavedState(WCCModel.getDataStorage().isEditedSinceLastSave());
		enableSaveDependentActions(WCCModel.getDataStorage().isEditedSinceLastSave());
		
		WCCView.clearPageList();
		for(Page p : WCCModel.getDataStorage()) {
			WCCView.addPageListItem(p.getFilename(), p.getName());
		}
		enableContentListeners = false;
		if(selectedPage == -1) {
			WCCView.clearContentArea();
		} else {
			WCCView.setSelectedPage(selectedPage);
			WCCView.setContent(WCCModel.getDataStorage().get(selectedPage).getContent());
			WCCView.setCaretPosition(WCCModel.getDataStorage().get(selectedPage).getCaretPosition());
		}
		enableContentListeners = true;
	}
	
}
