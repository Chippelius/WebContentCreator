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
	
	public static final int HEADER = 0;
	public static final int SUBHEADER = 1;
	public static final int TEXT = 2;
	public static final int IMAGE = 3;
	
	private int type;
	private String value;
	
	public Element(int type, String value, Observer o) {
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
	
	public void setType(int newType) {
		type = newType;
		update();
	}
	
	public int getType() {
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
