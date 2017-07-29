import java.io.Serializable;
import java.util.Arrays;
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
	private Element[] elements;
	private Observer observer;
	
	public Page(String filename, String name, Observer o) {
		for(char c : filename.toLowerCase().toCharArray()) {
			if(!"abcdefghijklmnopqrstuvwxyzäöüß.".contains("" + c)) {
				throw new IllegalArgumentException("filename must not contain special characters!");
			}
		}
		this.filename = filename;
		this.name = name;
		version = 0;
		editedSinceLastExport = true;
		elements = new Element[0];
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
	
	public void setFilename(String s) {
		for(char c :s.toLowerCase().toCharArray()) {
			if(!"abcdefghijklmnopqrstuvwxyzäöüß".contains("" + c)) {
				throw new IllegalArgumentException("filename must not contain special characters!");
			}
		}
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
		elements = Arrays.copyOf(elements, elements.length+1);
		elements[elements.length-1] = new Element(type, value, this);
		update(elements[elements.length-1], null);
	}
	
	public void deleteElement(Element element) {
		for(int i=0; i<elements.length; ++i) {
			if(elements[i] == element) {
				deleteElement(i);
				return;
			}
		}
	}
	
	public void deleteElement(int index) {
		Element[] res = new Element[elements.length-1];
		if(index > 0) {
			System.arraycopy(elements, 0, res, 0, index);
		}
		if(index < res.length) {
			System.arraycopy(elements, index+1, res, index, res.length-index);
		}
		elements = res;
		update(this, null);
	}
	
	public void setElements(Element[] e) {
		elements = e;
		update(this, null);
	}
	
	public Element[] getElements() {
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
