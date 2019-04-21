package model;

import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/*
 * Represents a storage file's content.
 * 
 * Created by Leo Köberlein on 10.07.2017
 */
public class DataStorage implements List<Page> {

	public static final int VERSION = 1;
	private ArrayList<Page> pages;
	private boolean editedSinceLastSave;

	public DataStorage() {
		pages = new ArrayList<>();
		editedSinceLastSave = false;
	}

	public boolean isValidFilename(String s) {
		if(s.equalsIgnoreCase("index"))
			return false;
		for(char c : s.toLowerCase().toCharArray()) {
			if(!"abcdefghijklmnopqrstuvwxyz.1234567890".contains("" + c)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isUnusedFilename(String s) {
		for(Page p : pages) {
			if(p.getFilename().equals(s))
				return false;
		}

		return true;
	}

	public boolean isEditedSinceLastSave() {
		if(editedSinceLastSave)
			return true;
		for(Page p : pages) {
			if(p.isEditedSinceLastSave())
				return true;
		}
		return false;
	}

	public void setEditedSinceLastSave(boolean editedSinceLastSave) {
		this.editedSinceLastSave = editedSinceLastSave;
		for(Page p : pages) {
			p.setEditedSinceLastSave(editedSinceLastSave);
		}
	}

	@Override
	public boolean add(Page p) {
		if(isValidFilename(p.getFilename()) && isUnusedFilename(p.getFilename())) {
			editedSinceLastSave = true;
			return pages.add(p);
		} else {
			return false;
		}
	}

	@Override
	public void add(int index, Page p) {
		if(!(isValidFilename(p.getFilename()) && isUnusedFilename(p.getFilename())))
			throw new IllegalArgumentException("filename must not contain special characters!");
		editedSinceLastSave = true;
		pages.add(index, p);
	}

	@Override
	public boolean addAll(Collection<? extends Page> c) {
		for(Page p : c) {
			if(!(isValidFilename(p.getFilename()) && isUnusedFilename(p.getFilename())))
				return false;
		}
		editedSinceLastSave = true;
		return pages.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends Page> c) {
		for(Page p : c) {
			if(!(isValidFilename(p.getFilename()) && isUnusedFilename(p.getFilename())))
				return false;
		}
		editedSinceLastSave = true;
		return pages.addAll(index, c);
	}

	@Override
	public void clear() {
		editedSinceLastSave = true;
		pages.clear();

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
		editedSinceLastSave = true;
		return pages.remove(o);
	}

	@Override
	public Page remove(int index) {
		editedSinceLastSave = true;
		return pages.remove(index);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		editedSinceLastSave = true;
		return pages.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		editedSinceLastSave = true;
		return pages.retainAll(c);
	}

	@Override
	public Page set(int index, Page element) {
		editedSinceLastSave = true;
		return pages.set(index, element);
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

}
