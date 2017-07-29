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
	private Observer observer;
	
	public Element(int type, String value, Observer o) {
		this.type = type;
		this.value = value;
		observer = o;
	}
	
	public void update() {
		observer.update(this, null);
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
