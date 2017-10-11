package data;

import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

/*
 * Represents an element and its content.
 * 
 * Created by Leo Köberlein on 26.07.2017
 */
public class Element extends Observable implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static final String HEADER = "Überschrift";
	public static final String SUBHEADER = "Unterüberschrift";
	public static final String TEXT = "Textinhalt";
	public static final String IMAGE = "Bild";
	public static final String[] TYPES = new String[] {HEADER, SUBHEADER, TEXT, IMAGE};
	
	private String type;
	private String value;
	
	public Element(String type, String value, Observer o) {
		this.type = type;
		this.value = value;
		addObserver(o);
	}
	
	public void update() {
		setChanged();
		notifyObservers(this);
	}
	
	public void save() {
		deleteObservers();
	}
	
	public void relink(Observer o) {
		addObserver(o);
	}
	
	public void setType(String newType) {
		type = newType;
		update();
	}
	
	public String getType() {
		return type;
	}
	
	public void setValue(String newValue) {
		value = newValue;
		update();
	}
	
	public String getValue() {
		return value;
	}

}
