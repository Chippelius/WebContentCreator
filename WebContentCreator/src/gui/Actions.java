package gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import base.WCCController;
import data.Element;

@SuppressWarnings("serial")
public class Actions {

	/* Optional functionality for later:
	 * public static final String fileNew = "fileNew";
	 * public static final String fileOpen = "fileOpen";
	 */

	public static final AbstractAction fileSave = new AbstractAction(Language.fileSaveText, Icons.saveIcon) {
		@Override
		public void actionPerformed(ActionEvent a) {
			WCCController.fileSave();
		}
	};

	/* Optional functionality for later:
	 * public static final String fileSaveAs = "fileSaveAs";
	 */

	public static final AbstractAction fileExport = new AbstractAction(Language.fileExportText, Icons.exportIcon) {
		@Override
		public void actionPerformed(ActionEvent a) {
			WCCController.fileExport();
		}
	};

	public static final AbstractAction fileExit = new AbstractAction(Language.fileExitText) {
		@Override
		public void actionPerformed(ActionEvent a) {
			WCCController.fileExit();
		}
	};

	public static final AbstractAction pageNew = new AbstractAction(Language.pageNewText, Icons.pageNewIcon) {
		@Override
		public void actionPerformed(ActionEvent a) {
			WCCController.pageNew();
		}
	};

	public static final AbstractAction pageChangeData = new AbstractAction(Language.pageChangeDataText) {
		@Override
		public void actionPerformed(ActionEvent a) {
			WCCController.pageChangeData();
		}
	};

	public static final AbstractAction pageMoveTop = new AbstractAction(Language.pageMoveTopText) {
		@Override
		public void actionPerformed(ActionEvent a) {
			WCCController.pageMoveTop();
		}
	};

	public static final AbstractAction pageMoveBottom = new AbstractAction(Language.pageMoveBottomText) {
		@Override
		public void actionPerformed(ActionEvent a) {
			WCCController.pageMoveBottom();
		}
	};

	public static final AbstractAction pageMoveUp = new AbstractAction(Language.pageMoveUpText) {
		@Override
		public void actionPerformed(ActionEvent a) {
			WCCController.pageMoveUp();
		}
	};

	public static final AbstractAction pageMoveDown = new AbstractAction(Language.pageMoveDownText) {
		@Override
		public void actionPerformed(ActionEvent a) {
			WCCController.pageMoveDown();
		}
	};

	public static final AbstractAction pageDelete = new AbstractAction(Language.pageDeleteText, Icons.pageDeleteIcon) {
		@Override
		public void actionPerformed(ActionEvent a) {
			WCCController.pageDelete();
		}
	};

	public static final AbstractAction elementNewHeader = new AbstractAction(Language.elementNewHeaderText, Icons.headerIcon) {
		@Override
		public void actionPerformed(ActionEvent a) {
			WCCController.elementNew(Element.HEADER);
		}
	};

	public static final AbstractAction elementNewSubheader = new AbstractAction(Language.elementNewSubheaderText, Icons.subheaderIcon) {
		@Override
		public void actionPerformed(ActionEvent a) {
			WCCController.elementNew(Element.SUBHEADER);
		}
	};

	public static final AbstractAction elementNewText = new AbstractAction(Language.elementNewTextText, Icons.textIcon) {
		@Override
		public void actionPerformed(ActionEvent a) {
			WCCController.elementNew(Element.TEXT);
		}
	};

	public static final AbstractAction elementNewImage = new AbstractAction(Language.elementNewImageText, Icons.imageIcon) {
		@Override
		public void actionPerformed(ActionEvent a) {
			WCCController.elementNew(Element.IMAGE);
		}
	};

	public static final AbstractAction elementChangeToHeader = new AbstractAction(Language.elementChangeToHeaderText) {
		@Override
		public void actionPerformed(ActionEvent a) {
			WCCController.elementChangeType(Element.HEADER);
		}
	};

	public static final AbstractAction elementChangeToSubheader = new AbstractAction(Language.elementChangeToSubheaderText) {
		@Override
		public void actionPerformed(ActionEvent a) {
			WCCController.elementChangeType(Element.SUBHEADER);
		}
	};

	public static final AbstractAction elementChangeToText = new AbstractAction(Language.elementChangeToTextText) {
		@Override
		public void actionPerformed(ActionEvent a) {
			WCCController.elementChangeType(Element.TEXT);
		}
	};

	public static final AbstractAction elementChangeToImage = new AbstractAction(Language.elementChangeToImageText) {
		@Override
		public void actionPerformed(ActionEvent a) {
			WCCController.elementChangeType(Element.IMAGE);
		}
	};

	public static final AbstractAction elementChangeValue = new AbstractAction(Language.elementChangeValueText) {
		@Override
		public void actionPerformed(ActionEvent a) {
			WCCController.elementChangeValue();
		}
	};

	public static final AbstractAction elementMoveTop = new AbstractAction(Language.elementMoveTopText) {
		@Override
		public void actionPerformed(ActionEvent a) {
			WCCController.elementMoveTop();
		}
	};

	public static final AbstractAction elementMoveBottom = new AbstractAction(Language.elementMoveBottomText) {
		@Override
		public void actionPerformed(ActionEvent a) {
			WCCController.elementMoveBottom();
		}
	};

	public static final AbstractAction elementMoveUp = new AbstractAction(Language.elementMoveUpText) {
		@Override
		public void actionPerformed(ActionEvent a) {
			WCCController.elementMoveUp();
		}
	};

	public static final AbstractAction elementMoveDown = new AbstractAction(Language.elementMoveDownText) {
		@Override
		public void actionPerformed(ActionEvent a) {
			WCCController.elementMoveDown();
		}
	};

	public static final AbstractAction elementDelete = new AbstractAction(Language.elementDeleteText) {
		@Override
		public void actionPerformed(ActionEvent a) {
			WCCController.elementDelete();
		}
	};

	public static final AbstractAction windowToggleMaximized = new AbstractAction(Language.windowToggleMaximizedText) {
		@Override
		public void actionPerformed(ActionEvent a) {
			WCCController.windowToggleMaximized();
		}
	};

	public static final AbstractAction windowCenterDivider = new AbstractAction(Language.windowCenterDividerText) {
		@Override
		public void actionPerformed(ActionEvent a) {
			WCCController.windowCenterDivider();
		}
	};

	public static final AbstractAction helpInfo = new AbstractAction(Language.helpInfoText) {
		@Override
		public void actionPerformed(ActionEvent a) {
			WCCController.helpInfo();
		}
	};

	public static final AbstractAction helpCheckForUpdates = new AbstractAction(Language.helpCheckForUpdatesText) {
		@Override
		public void actionPerformed(ActionEvent a) {
			WCCController.helpCheckForUpdates();
		}
	};





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

	public static void enableElementDependentActions(boolean value) {
		for(AbstractAction a : elementDependentActions) {
			a.setEnabled(value);
		}
	}

}
