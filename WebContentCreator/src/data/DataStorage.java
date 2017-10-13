package data;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Observable;
import java.util.Observer;

/*
 * Represents a storage file's content.
 * 
 * Created by Leo Köberlein on 10.07.2017
 */
public class DataStorage extends Observable implements Serializable, List<Page>, Observer {

	private static final long serialVersionUID = 1L;
	
	private ArrayList<Page> pages;
	private boolean editedSinceLastSave;
	private String lastImageChooseLocation;
	private String lastExportLocation;
	
	
	public DataStorage() {
		pages = new ArrayList<>();
		pages.add(new Page("startpage.html", "Home", this));
		editedSinceLastSave = true;
		lastImageChooseLocation = "";
		lastExportLocation = "";
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if(!editedSinceLastSave) {
			editedSinceLastSave = true;
		}
		if(pages.size() == 0) {
			pages.add(new Page("startpage.html", "Home", this));
		}
		setChanged();
		notifyObservers(arg);
	}
	
	/* 
	 * Mark as saved and cut all connections to objects outside the data to be stored 
	 * (so nothing gets accidentally stored with the data).
	 */
	public void save() {
		editedSinceLastSave = false;
		deleteObservers();
		for(Page p : pages) {
			p.save();
		}
	}
	
	//Relink observers after being saved or loaded
	public void relink(Observer o) {
		addObserver(o);
		for(Page p : pages) {
			p.relink(this);
		}
	}
	
	public boolean isEditedSinceLastSave() {
		return editedSinceLastSave;
	}
	
	//Returns a hash of all page-versions
	public String export(String location) {
		lastExportLocation = location;
		//TODO: implement using the export-method of the pages
		return null;
	}

	public boolean isValidFilename(String s) {
		if(s.equalsIgnoreCase("index"))
			return false;
		for(char c : s.toLowerCase().toCharArray()) {
			if(!"abcdefghijklmnopqrstuvwxyzäöüß.".contains("" + c)) {
				return false;
			}
		}
		if(pages.stream().filter(x -> x.getFilename().equalsIgnoreCase(s)).count() > 0)
			return false;
		
		return true;
	}
	
	public boolean createPage(String filename, String name) {
		return add(new Page(filename, name, this));
	}
	
	@Override
	public boolean add(Page p) {
		if(!isValidFilename(p.getFilename()))
			return false;
		boolean b = pages.add(p);
		p.addObserver(this);
		update(this, p);
		return b;
	}

	@Override
	public void add(int index, Page p) {
		if(!isValidFilename(p.getFilename()))
			throw new IllegalArgumentException("filename must not contain special characters!");
		pages.add(index, p);
		p.addObserver(this);
		update(this, p);
	}

	@Override
	public boolean addAll(Collection<? extends Page> c) {
		for(Page p : c) {
			if(!isValidFilename(p.getFilename()))
				return false;
		}
		boolean b = pages.addAll(c);
		c.forEach(x -> x.addObserver(this));
		update(this, this);
		return b;
	}

	@Override
	public boolean addAll(int index, Collection<? extends Page> c) {
		for(Page p : c) {
			if(!isValidFilename(p.getFilename()))
				return false;
		}
		boolean b = pages.addAll(index, c);
		c.forEach(x -> x.addObserver(this));
		update(this, this);
		return b;
	}

	@Override
	public void clear() {
		pages.forEach(x -> x.deleteObserver(this));
		pages.clear();
		update(this, this);
	}

	@Override
	public boolean contains(Object o) {
		return pages.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return pages.containsAll(c);
	}

	@Override
	public Page get(int index) {
		return pages.get(index);
	}
	
	public Page get(String filename) {
		return get(indexOf(filename));
	}

	@Override
	public int indexOf(Object o) {
		return pages.indexOf(o);
	}
	
	public int indexOf(String filename) {
		for(int i=0; i<size(); ++i) {
			if(get(i).getFilename().equalsIgnoreCase(filename))
				return i;
		}
		return -1;
	}

	@Override
	public boolean isEmpty() {
		return pages.isEmpty();
	}

	@Override
	public Iterator<Page> iterator() {
		return pages.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return pages.lastIndexOf(o);
	}

	@Override
	public ListIterator<Page> listIterator() {
		return pages.listIterator();
	}

	@Override
	public ListIterator<Page> listIterator(int index) {
		return pages.listIterator(index);
	}

	@Override
	public boolean remove(Object o) {
		boolean b = pages.remove(o);
		((Page)o).deleteObserver(this);
		update(this, null);
		return b;
	}

	@Override
	public Page remove(int index) {
		Page p = pages.remove(index);
		p.deleteObserver(this);
		update(this, this);
		return p;
	}
	
	public Page remove(String filename) {
		return remove(indexOf(filename));
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean b = pages.removeAll(c);
		c.forEach(x -> ((Page)x).deleteObserver(this));
		update(this, this);
		return b;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean b = pages.retainAll(c);
		update(this, this);
		return b;
	}

	@Override
	public Page set(int index, Page element) {
		Page p = pages.set(index, element);
		element.addObserver(this);
		p.deleteObserver(this);
		update(this, element);
		return p;
	}

	@Override
	public int size() {
		return pages.size();
	}

	@Override
	public List<Page> subList(int fromIndex, int toIndex) {
		return pages.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return pages.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return pages.toArray(a);
	}
	
	
	
	public void setLastImageChooseLocation(String location) {
		lastImageChooseLocation = location;
	}
	
	public String getLastImageChooseLocation() {
		return lastImageChooseLocation;
	}
	
	public String getLastExportLocation() {
		return lastExportLocation;
	}
}
