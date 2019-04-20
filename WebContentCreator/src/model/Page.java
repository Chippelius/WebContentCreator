package model;

/**
 * Represents a page and its elements.<br>
 * <br>
 * Created by Leo Köberlein on 26.07.2017
 */
public class Page {

	//Unique name to identify a page. Only letters and dots ('.') allowed (No whitespaces, no special characters).
	private String filename;
	//Name that shows up in the menu. May contain whitespaces and special characters.
	private String name;
	private String content;
	private int caretPosition;
	private boolean editedSinceLastSave;
	
	public Page(String filename, String name) {
		this.filename = filename;
		this.name = name;
		content = "";
		caretPosition = 0;
		editedSinceLastSave = true;
	}
	
	public boolean isEditedSinceLastSave() {
		if(editedSinceLastSave)
			return true;
		return false;
	}

	public void setEditedSinceLastSave(boolean editedSinceLastSave) {
		this.editedSinceLastSave = editedSinceLastSave;
	}
	
	public void setFilename(String s) {
		editedSinceLastSave = true;
		filename = s;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public void setName(String s) {
		editedSinceLastSave = true;
		name = s;
	}
	
	public String getName() {
		return name;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String s) {
		editedSinceLastSave = true;
		content = s;
	}
	
	public int getCaretPosition() {
		return caretPosition;
	}
	
	public void setCaretPosition(int i) {
		caretPosition = i;
	}
}
