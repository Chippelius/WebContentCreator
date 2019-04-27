package contoller;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;

import model.Element;
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
	private static int selectedPage, selectedElement = -1;

	/**
	 * Entry point for the program.
	 * 
	 * @param args Not currently used.
	 */
	public static void main(String[] args) {
		enablePageDependentActions(false);
		enableElementDependentActions(false);
		WCCView.init();
		WCCModel.loadSettings();
		WCCView.applySettings(WCCModel.getSettings());
		if(WCCModel.getSettings().getCurrentlyOpenedFile()==null||WCCModel.getSettings().getCurrentlyOpenedFile().equals("")) {
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
	 * Refreshes the view.
	 */
	public static void refreshView() {
		refreshSavedStateView();
		refreshProjectView();
	}

	/**
	 * Refreshes the part of the view that indicates whether the current state of the project is saved or not.
	 */
	public static void refreshSavedStateView() {
		WCCView.setSavedState(WCCModel.getDataStorage().isEditedSinceLastSave());
		enableSaveDependentActions(WCCModel.getDataStorage().isEditedSinceLastSave());
	}

	/**
	 * Refreshes the pagelist. <br>
	 * Deselects all pages and resets selectedPage to -1.
	 */
	public static void refreshProjectView() {
		setSelectedPage(-1);
		WCCView.clearPageList();
		for(Page p : WCCModel.getDataStorage()) {
			WCCView.addPageListItem(p.getFilename(), p.getName());
		}
	}

	/**
	 * Selects the page with the given index and shows the contained elements.
	 * 
	 * @param selectedPage The index of the page to be selected
	 */
	public static void setSelectedPage(int selectedPage) {
		if(selectedPage>=WCCModel.getDataStorage().size()) {
			return;
		}
		WCCController.selectedPage = selectedPage;
		if(selectedPage>=0) {
			WCCView.setSelectedPage(selectedPage);
			WCCView.clearElementList();
			for(Element e : WCCModel.getDataStorage().get(selectedPage)) {
				WCCView.addElementListItem(e.getType(), e.getValue());
			}
			enablePageDependentActions(true);
		} else {
			enablePageDependentActions(false);
		}
		WCCController.selectedElement = -1;
		enableElementDependentActions(false);
	}

	/**
	 * Selects the element with the given index.
	 * 
	 * @param selectedElement The index of the element to be selected
	 */
	public static void setSelectedElement(int selectedElement) {
		if(selectedPage==-1||selectedElement>=WCCModel.getDataStorage().get(selectedPage).size()) {
			return;
		}
		WCCController.selectedElement = selectedElement;
		if(selectedElement>=0) {
			WCCView.setSelectedElement(selectedElement);
			enableElementDependentActions(true);
		} else {
			enableElementDependentActions(false);
		}
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
					WCCModel.getSettings().setCurrentlyOpenedFile(null);
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
					refreshSavedStateView();
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
						refreshSavedStateView();
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
			while(!(WCCModel.getDataStorage().isValidFilename(res[0]) && WCCModel.getDataStorage().isUnusedFilename(res[0]))) {
				WCCView.showErrorInvalidFilename(res[0]);
				res = WCCView.requestNewPageData(res[0], res[1]);
				if(res==null)
					return;
			}
			WCCModel.getDataStorage().add(new Page(res[0], res[1]));
			refreshView();
			setSelectedPage(WCCModel.getDataStorage().size()-1);
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
			while(!(WCCModel.getDataStorage().isValidFilename(res[0]) && (WCCModel.getDataStorage().isUnusedFilename(res[0]) || WCCModel.getDataStorage().get(selectedPage).getFilename().equals(res[0])))) {
				WCCView.showErrorInvalidFilename(res[0]);
				res = WCCView.requestNewPageData(res[0], res[1]);
				if(res==null)
					return;
			}
			WCCModel.getDataStorage().get(selectedPage).setFilename(res[0]);
			WCCModel.getDataStorage().get(selectedPage).setName(res[1]);
			int tmp = selectedPage;
			refreshView();
			setSelectedPage(tmp);
		}
	};

	/**
	 * The action to be fired when the currently selected page should be moved to the top of the pagelist.
	 */
	public static final AbstractAction pageMoveTop = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent a) {
			if(selectedPage==-1) {
				return;
			}
			WCCModel.getDataStorage().add(0, WCCModel.getDataStorage().remove(selectedPage));
			refreshView();
			setSelectedPage(0);
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
			refreshView();
			setSelectedPage(WCCModel.getDataStorage().size()-1);
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
			int tmp = selectedPage;
			refreshView();
			setSelectedPage(tmp-1);
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
			int tmp = selectedPage;
			refreshView();
			setSelectedPage(tmp+1);
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
			refreshView();
		}
	};

	/**
	 * The action to be fired when a new header should be created at the end of the currently selected page. <br>
	 * Will ask the user for required data.
	 */
	public static final AbstractAction elementNewHeader = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent a) {
			if(selectedPage==-1) {
				return;
			}
			String res = WCCView.requestNewHeaderData(null);
			if(res == null)
				return;
			WCCModel.getDataStorage().get(selectedPage).add(new Element(Element.HEADER, res));
			int tmp = selectedPage;
			refreshView();
			setSelectedPage(tmp);
		}
	};

	/**
	 * The action to be fired when a new subheader should be created at the end of the currently selected page. <br>
	 * Will ask the user for required data.
	 */
	public static final AbstractAction elementNewSubheader = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent a) {
			if(selectedPage==-1) {
				return;
			}
			String res = WCCView.requestNewSubheaderData(null);
			if(res == null)
				return;
			WCCModel.getDataStorage().get(selectedPage).add(new Element(Element.SUBHEADER, res));
			int tmp = selectedPage;
			refreshView();
			setSelectedPage(tmp);
		}
	};

	/**
	 * The action to be fired when a new text should be created at the end of the currently selected page. <br>
	 * Will ask the user for required data.
	 */
	public static final AbstractAction elementNewText = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent a) {
			if(selectedPage==-1) {
				return;
			}
			String res = WCCView.requestNewTextData(null);
			if(res == null)
				return;
			WCCModel.getDataStorage().get(selectedPage).add(new Element(Element.TEXT, res));
			int tmp = selectedPage;
			refreshView();
			setSelectedPage(tmp);
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
			String res = WCCView.requestNewImageData(WCCModel.getSettings().getImageChooseLocation());
			if(res == null)
				return;
			WCCModel.getSettings().setImageChooseLocation(new File(res).getParent());
			WCCModel.getDataStorage().get(selectedPage).add(new Element(Element.IMAGE, res));
			int tmp = selectedPage;
			refreshView();
			setSelectedPage(tmp);
		}
	};

	/**
	 * The action to be fired when the type of the currently selected element should be changed into header.
	 */
	public static final AbstractAction elementChangeToHeader = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent a) {
			if(selectedPage==-1||selectedElement==-1) {
				return;
			}
			WCCModel.getDataStorage().get(selectedPage).get(selectedElement).setType(Element.HEADER);
			int tmp = selectedPage;
			int tmp1 = selectedElement;
			refreshView();
			setSelectedPage(tmp);
			setSelectedElement(tmp1);
		}
	};

	/**
	 * The action to be fired when the type of the currently selected element should be changed into subheader.
	 */
	public static final AbstractAction elementChangeToSubheader = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent a) {
			if(selectedPage==-1||selectedElement==-1) {
				return;
			}
			WCCModel.getDataStorage().get(selectedPage).get(selectedElement).setType(Element.SUBHEADER);
			int tmp = selectedPage;
			int tmp1 = selectedElement;
			refreshView();
			setSelectedPage(tmp);
			setSelectedElement(tmp1);
		}
	};

	/**
	 * The action to be fired when the type of the currently selected element should be changed into text.
	 */
	public static final AbstractAction elementChangeToText = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent a) {
			if(selectedPage==-1||selectedElement==-1) {
				return;
			}
			WCCModel.getDataStorage().get(selectedPage).get(selectedElement).setType(Element.TEXT);
			int tmp = selectedPage;
			int tmp1 = selectedElement;
			refreshView();
			setSelectedPage(tmp);
			setSelectedElement(tmp1);
		}
	};

	/**
	 * The action to be fired when the type of the currently selected element should be changed into image.
	 */
	public static final AbstractAction elementChangeToImage = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent a) {
			if(selectedPage==-1||selectedElement==-1) {
				return;
			}
			WCCModel.getDataStorage().get(selectedPage).get(selectedElement).setType(Element.IMAGE);
			int tmp = selectedPage;
			int tmp1 = selectedElement;
			refreshView();
			setSelectedPage(tmp);
			setSelectedElement(tmp1);
		}
	};

	/**
	 * The action to be fired when the value of the currently selected element should be altered. <br>
	 * Will ask the user for new data.
	 */
	public static final AbstractAction elementChangeValue = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent a) {
			if(selectedPage==-1||selectedElement==-1) {
				return;
			}
			String res = null;
			switch(WCCModel.getDataStorage().get(selectedPage).get(selectedElement).getType()) {
			case Element.HEADER:
				res = WCCView.requestNewHeaderData(WCCModel.getDataStorage().get(selectedPage).get(selectedElement).getValue());
				break;
			case Element.SUBHEADER:
				res = WCCView.requestNewSubheaderData(WCCModel.getDataStorage().get(selectedPage).get(selectedElement).getValue());
				break;
			case Element.TEXT:
				res = WCCView.requestNewTextData(WCCModel.getDataStorage().get(selectedPage).get(selectedElement).getValue());
				break;
			case Element.IMAGE:
				//String value = WCCModel.getDataStorage().get(selectedPage).get(selectedElement).getValue();
				//res = WCCView.requestNewImageData(value.substring(0, value.indexOf("\n")));
				System.out.println(WCCModel.getDataStorage().get(selectedPage).get(selectedElement).getValue());
				res = WCCView.requestNewImageData(WCCModel.getDataStorage().get(selectedPage).get(selectedElement).getValue());
				break;
			}
			if(res==null)
				return;
			WCCModel.getDataStorage().get(selectedPage).get(selectedElement).setValue(res);
			int tmp = selectedPage;
			int tmp1 = selectedElement;
			refreshView();
			setSelectedPage(tmp);
			setSelectedElement(tmp1);
		}
	};

	/**
	 * The action to be fired when the currently selected element should be moved to the top of the page.
	 */
	public static final AbstractAction elementMoveTop = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent a) {
			if(selectedPage==-1||selectedElement==-1) {
				return;
			}
			WCCModel.getDataStorage().get(selectedPage).add(0, WCCModel.getDataStorage().get(selectedPage).remove(selectedElement));
			int tmp = selectedPage;
			refreshView();
			setSelectedPage(tmp);
			setSelectedElement(0);
		}
	};

	/**
	 * The action to be fired when the currently selected element should be moved to the bottom of the page.
	 */
	public static final AbstractAction elementMoveBottom = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent a) {
			if(selectedPage==-1||selectedElement==-1) {
				return;
			}
			WCCModel.getDataStorage().get(selectedPage).add(WCCModel.getDataStorage().get(selectedPage).remove(selectedElement));
			int tmp = selectedPage;
			refreshView();
			setSelectedPage(tmp);
			setSelectedElement(WCCModel.getDataStorage().get(selectedPage).size()-1);
		}
	};

	/**
	 * The action to be fired when the currently selected element should be moved up in the page.
	 */
	public static final AbstractAction elementMoveUp = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent a) {
			if(selectedPage==-1||selectedElement==-1||selectedElement==0) {
				return;
			}
			WCCModel.getDataStorage().get(selectedPage).add(selectedElement-1, WCCModel.getDataStorage().get(selectedPage).remove(selectedElement));
			int tmp = selectedPage;
			int tmp1 = selectedElement;
			refreshView();
			setSelectedPage(tmp);
			setSelectedElement(tmp1-1);
		}
	};

	/**
	 * The action to be fired when the currently selected element should be moved down in the page.
	 */
	public static final AbstractAction elementMoveDown = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent a) {
			if(selectedPage==-1||selectedElement==-1||selectedElement==WCCModel.getDataStorage().get(selectedPage).size()-1) {
				return;
			}
			WCCModel.getDataStorage().get(selectedPage).add(selectedElement+1, WCCModel.getDataStorage().get(selectedPage).remove(selectedElement));
			int tmp = selectedPage;
			int tmp1 = selectedElement;
			refreshView();
			setSelectedPage(tmp);
			setSelectedElement(tmp1+1);
		}
	};

	/**
	 * The action to be fired when the currently selected element should be deleted.
	 */
	public static final AbstractAction elementDelete = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent a) {
			if(selectedPage==-1||selectedElement==-1||!WCCView.confirmDeleteElement(WCCModel.getDataStorage().get(selectedPage).getFilename(), 
					WCCModel.getDataStorage().get(selectedPage).getName(), WCCModel.getDataStorage().get(selectedPage).get(selectedElement).getValue())) {
				return;
			}
			WCCModel.getDataStorage().get(selectedPage).remove(selectedElement);
			int tmp = selectedPage;
			refreshView();
			setSelectedPage(tmp);
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
			WCCModel.getSettings().setDividerLocation((WCCView.getWidth()-10)/2);
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
				java.awt.Desktop.getDesktop().open(WCCModel.openReadmeFile());
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





	/**
	 * Enables or disables actions regarding saving.
	 * 
	 * @param value whether save-dependent actions should be enabled or not
	 */
	public static void enableSaveDependentActions(boolean value) {
		fileSave.setEnabled(value);
	}

	/**
	 * Enables or disables actions regarding exporting.
	 * 
	 * @param value whether export-dependent actions should be enabled or not
	 */
	public static void enableExportActions(boolean value) {
		fileExport.setEnabled(value);
	}

	/**
	 * Enables or disables actions regarding qr-code generation.
	 * 
	 * @param value whether qr-code-dependent actions should be enabled or not
	 */
	public static void enableQRActions(boolean value) {
		fileGenerateQRCodes.setEnabled(value);
	}

	private static final AbstractAction[] pageDependentActions = new AbstractAction[] {
			pageChangeData,
			pageMoveTop,
			pageMoveBottom,
			pageMoveUp,
			pageMoveDown,
			pageDelete,
			elementNewHeader,
			elementNewSubheader,
			elementNewText,
			elementNewImage
	};

	/**
	 * Enables or disables actions regarding a specific page.
	 * 
	 * @param value whether page-dependent actions should be enabled or not
	 */
	public static void enablePageDependentActions(boolean value) {
		for(AbstractAction a : pageDependentActions) {
			a.setEnabled(value);
		}
	}

	private static final AbstractAction[] elementDependentActions = new AbstractAction[] {
			elementChangeToHeader,
			elementChangeToSubheader,
			elementChangeToText,
			elementChangeToImage,
			elementChangeValue,
			elementMoveTop,
			elementMoveBottom,
			elementMoveUp,
			elementMoveDown,
			elementDelete
	};

	/**
	 * Enables or disables actions regarding a specific element.
	 * 
	 * @param value whether element-dependent actions should be enabled or not
	 */
	public static void enableElementDependentActions(boolean value) {
		for(AbstractAction a : elementDependentActions) {
			a.setEnabled(value);
		}
	}

}
