/**
 * 0 AD: Jesus of Nazareth born
 * 1492: Culumbus sails the ocean blue
 * 2019-04-12: Leo Köberlein creates this file
 */
package view;

import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.Document;

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
	}

	private WCCTextArea(Document doc) {
		super(doc);
	}
	private WCCTextArea(Document doc, String text, int rows, int columns) {
		super(doc, text, rows, columns);
	}

}
