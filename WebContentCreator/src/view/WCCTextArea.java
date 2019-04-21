/**
 * 0 AD: Jesus of Nazareth born
 * 1492: Culumbus sails the ocean blue
 * 2019-04-12: Leo Köberlein creates this file
 */
package view;

import java.awt.event.ActionEvent;

import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import contoller.WCCController;

/**
 * 
 * 
 * @author Leo Köberlein
 */
@SuppressWarnings({"serial", "unused"})
public class WCCTextArea extends JTextArea {

	public WCCTextArea() {
		this(null, 0, 0);
	}

	public WCCTextArea(String text) {
		this(text, 0, 0);
	}

	public WCCTextArea(int rows, int columns) {
		this(null, rows, columns);
	}

	public WCCTextArea(String text, int rows, int columns) {
		super(text, rows, columns);
		WCCTextArea selfref = this;
		getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				update(e);
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				update(e);
			}
			public void update(DocumentEvent e) {
				WCCController.pageChangeContent.actionPerformed(new ActionEvent(selfref, 0, getText()));
			}
			@Override public void changedUpdate(DocumentEvent e) {}
		});
		addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				WCCController.pageChangeCaretPosition.actionPerformed(new ActionEvent(selfref, 0, ""+e.getDot()));
			}
		});
	}

	private WCCTextArea(Document doc) {
		super(doc);
	}
	private WCCTextArea(Document doc, String text, int rows, int columns) {
		super(doc, text, rows, columns);
	}

}
