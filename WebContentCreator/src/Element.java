
/*
 * Represents an element and its content.
 * 
 * Created by Leo Köberlein on 26.07.2017
 */
public class Element {
	
	public static final int HEADER = 0;
	public static final int SUBHEADER = 1;
	public static final int TEXT = 2;
	public static final int IMAGE = 3;
	
	private int type;
	private String value;
	
	public Element(int type, String value) {
		this.type = type;
		this.value = value;
	}
	
	public void setType(int newType) {
		type = newType;
	}
	
	public int getType() {
		return type;
	}
	
	public void setValue(String newValue) {
		value = newValue;
	}
	
	public String getValue() {
		return value;
	}

}
