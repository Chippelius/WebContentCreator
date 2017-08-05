import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Observable;
import java.util.Observer;

/*
 * Represents a page and its elements.
 * 
 * Created by Leo Köberlein on 26.07.2017
 */
public class Page extends Observable  implements Serializable, List<Element>, Observer {

	private static final long serialVersionUID = 1L;
	
	//Unique name to identify a page. Ends with file extension (i.e. '.html'). Only letters and dots ('.') allowed (No whitespaces, no special characters).
	private String filename;
	//Name that shows up in the menu. May contain whitespaces and special characters.
	private String name;
	private long version;
	private boolean editedSinceLastExport;
	private ArrayList<Element> elements;
	
	public Page(String filename, String name, Observer o) {
		this.filename = filename;
		this.name = name;
		version = 0;
		editedSinceLastExport = true;
		elements = new ArrayList<>();
		addObserver(o);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if(!editedSinceLastExport) {
			editedSinceLastExport = true;
			version += 1;
		}
		setChanged();
		notifyObservers(arg);
	}
	
	public void save() {
		deleteObservers();
		for(Element e : elements) {
			e.save();
		}
	}
	
	public void relink(Observer o) {
		addObserver(o);
		for(Element e : elements) {
			e.relink(this);
		}
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

	public void setFilename(String s) {
		filename = s;
		update(this, this);
	}
	
	public String getFilename() {
		return filename;
	}
	
	public void setName(String s) {
		name = s;
		update(this, this);
	}
	
	public String getName() {
		return name;
	}
	
	public boolean createElement(int type, String value) {
		return add(new Element(type, value, this));
	}
	
	@Override
	public boolean add(Element e) {
		boolean b = elements.add(e);
		e.addObserver(this);
		update(this, this);
		return b;
	}

	@Override
	public void add(int index, Element element) {
		elements.add(index, element);
		element.addObserver(this);
		update(this, this);
	}

	@Override
	public boolean addAll(Collection<? extends Element> c) {
		boolean b = elements.addAll(c);
		c.forEach(x -> x.addObserver(this));
		update(this, this);
		return b;
	}

	@Override
	public boolean addAll(int index, Collection<? extends Element> c) {
		boolean b = elements.addAll(index, c);
		c.forEach(x -> x.addObserver(this));
		update(this, this);
		return b;
	}

	@Override
	public void clear() {
		elements.forEach(x -> x.deleteObserver(this));
		elements.clear();
		update(this, this);
	}

	@Override
	public boolean contains(Object o) {
		return elements.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return elements.containsAll(c);
	}

	@Override
	public Element get(int index) {
		return elements.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return elements.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return elements.isEmpty();
	}

	@Override
	public Iterator<Element> iterator() {
		return elements.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return elements.lastIndexOf(o);
	}

	@Override
	public ListIterator<Element> listIterator() {
		return elements.listIterator();
	}

	@Override
	public ListIterator<Element> listIterator(int index) {
		return elements.listIterator(index);
	}

	@Override
	public boolean remove(Object o) {
		if(elements.remove(o)) {
			((Element)o).deleteObserver(this);
			update(this, this);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Element remove(int index) {
		Element e = elements.remove(index);
		e.deleteObserver(this);
		update(this, this);
		return e;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean b = elements.removeAll(c);
		c.forEach(x -> ((Element)x).deleteObserver(this));
		update(this, this);
		return b;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean b = elements.retainAll(c);
		update(this, this);
		return b;
	}

	@Override
	public Element set(int index, Element element) {
		Element e = elements.set(index, element);
		element.addObserver(this);
		e.deleteObserver(this);
		update(this, this);
		return e;
	}

	@Override
	public int size() {
		return elements.size();
	}

	@Override
	public List<Element> subList(int fromIndex, int toIndex) {
		return elements.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return elements.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return elements.toArray(a);
	}
	
}
