import java.io.Serializable;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

/*
 * Represents a page and its elements.
 * 
 * Created by Leo Köberlein on 26.07.2017
 */
public class Page extends Observable  implements Serializable, Observer {

	private static final long serialVersionUID = 1L;
	
	//Unique name to identify a page. Ends with file extension (i.e. '.html'). Only letters and dots ('.') allowed (No whitespaces, no special characters).
	private String filename;
	//Name that shows up in the menu. May contain whitespaces and special characters.
	private String name;
	private long version;
	private boolean editedSinceLastExport;
	private LinkedList<Element> elements;
	private Observer observer;
	
	public Page(String filename, String name, Observer o) {
		if(!isValidFilename(filename))
			throw new IllegalArgumentException("filename must not contain special characters!");
		this.filename = filename;
		this.name = name;
		version = 0;
		editedSinceLastExport = true;
		elements = new LinkedList<>();
		observer = o;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if(!editedSinceLastExport) {
			editedSinceLastExport = true;
			version += 1;
		}
		observer.update(o, this.filename);
	}
	
	public static boolean isValidFilename(String s) {
		if(s.equalsIgnoreCase("index.html"))
			return false;
		for(char c : s.toLowerCase().toCharArray()) {
			if(!"abcdefghijklmnopqrstuvwxyzäöüß.".contains("" + c)) {
				return false;
			}
		}
		return true;
	}
	
	public void setFilename(String s) {
		if(!isValidFilename(s))
			throw new IllegalArgumentException("filename must not contain special characters!");
		filename = s;
		update(this, null);
	}
	
	public String getFilename() {
		return filename;
	}
	
	public void setName(String s) {
		name = s;
		update(this, null);
	}
	
	public String getName() {
		return name;
	}
	
	public void createElement(int type, String value) {
		elements.add(new Element(type, value, this));
		update(elements.getLast(), null);
	}
	
	public void deleteElement(Element element) {
		elements.remove(element);
		update(this, null);
	}
	
	public void deleteElement(int index) {
		elements.remove(index);
		update(this, null);
	}
	
	public void setElements(LinkedList<Element> e) {
		elements = e;
		update(this, null);
	}
	
	public LinkedList<Element> getElements() {
		return elements;
	}
	
	public long export() {
		editedSinceLastExport = false;
		return version;
	}
	
	public boolean isEditedSinceLastExport() {
		return editedSinceLastExport;
	}
	
	public long getVersion() {
		return version;
	}
	
}
