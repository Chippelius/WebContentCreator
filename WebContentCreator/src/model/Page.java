package model;

import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/*
 * Represents a page and its elements.
 * 
 * Created by Leo Köberlein on 26.07.2017
 */
public class Page implements List<Element>{

	//Unique name to identify a page. Only letters and dots ('.') allowed (No whitespaces, no special characters).
	private String filename;
	//Name that shows up in the menu. May contain whitespaces and special characters.
	private String name;
	private ArrayList<Element> elements;
	private boolean editedSinceLastSave;
	
	public Page(String filename, String name) {
		this.filename = filename;
		this.name = name;
		elements = new ArrayList<>();
		editedSinceLastSave = true;
	}
	
	public boolean isEditedSinceLastSave() {
		if(editedSinceLastSave)
			return true;
		for(Element e : elements) {
			if(e.isEditedSinceLastSave())
				return true;
		}
		return false;
	}

	public void setEditedSinceLastSave(boolean editedSinceLastSave) {
		this.editedSinceLastSave = editedSinceLastSave;
		for(Element e : elements) {
			e.setEditedSinceLastSave(editedSinceLastSave);
		}
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
	
	@Override
	public boolean add(Element e) {
		editedSinceLastSave = true;
		return elements.add(e);
	}

	@Override
	public void add(int index, Element element) {
		editedSinceLastSave = true;
		elements.add(index, element);
	}

	@Override
	public boolean addAll(Collection<? extends Element> c) {
		editedSinceLastSave = true;
		return elements.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends Element> c) {
		editedSinceLastSave = true;
		return elements.addAll(index, c);
	}

	@Override
	public void clear() {
		editedSinceLastSave = true;
		elements.clear();
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
		editedSinceLastSave = true;
		return elements.remove(o);
	}

	@Override
	public Element remove(int index) {
		editedSinceLastSave = true;
		return elements.remove(index);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		editedSinceLastSave = true;
		return elements.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		editedSinceLastSave = true;
		return elements.retainAll(c);
	}

	@Override
	public Element set(int index, Element element) {
		editedSinceLastSave = true;
		return elements.set(index, element);
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
