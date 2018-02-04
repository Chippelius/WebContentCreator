package model;

/*
 * Represents an element and its content.
 * 
 * Created by Leo Köberlein on 26.07.2017
 */
public class Element {
	
	public static final String HEADER = "Überschrift";
	public static final String SUBHEADER = "Unterüberschrift";
	public static final String TEXT = "Textinhalt";
	public static final String IMAGE = "Bild";
	public static final String[] TYPES = new String[] {HEADER, SUBHEADER, TEXT, IMAGE};
	
	private String type;
	private String value;
	private boolean editedSinceLastSave;
	
	public Element(String type, String value) {
		this.type = type;
		this.value = value;
		editedSinceLastSave = true;
	}
	
	public boolean isEditedSinceLastSave() {
		return editedSinceLastSave;
	}

	public void setEditedSinceLastSave(boolean editedSinceLastSave) {
		this.editedSinceLastSave = editedSinceLastSave;
	}

	public void setType(String newType) {
		editedSinceLastSave = true;
		type = newType;
	}
	
	public String getType() {
		return type;
	}
	
	public boolean isHeader() {
		return type.equals(HEADER);
	}

	public boolean isSubheader() {
		return type.equals(SUBHEADER);
	}

	public boolean isText() {
		return type.equals(TEXT);
	}

	public boolean isImage() {
		return type.equals(IMAGE);
	}
	
	public void setValue(String newValue) {
		editedSinceLastSave = true;
		value = newValue;
	}
	
	public String getValue() {
		return value;
	}

}
