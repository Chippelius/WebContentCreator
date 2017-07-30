import java.io.Serializable;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

/*
 * Represents a storage file's content.
 * 
 * Created by Leo Köberlein on 10.07.2017
 */
public class DataStorage extends Observable implements Serializable, Observer {

	private static final long serialVersionUID = 1L;
	
	private LinkedList<Page> pages;
	private boolean editedSinceLastSave;
	private Observer observer;
	
	public DataStorage(Observer o) {
		pages = new LinkedList<>();
		pages.add(new Page("startpage.html", "Home", this));
		editedSinceLastSave = true;
		observer = o;
	}
	
	//To be called after being saved or loaded to re-link observer.
	public void setObserver(Observer o) {
		this.observer = o;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if(!editedSinceLastSave) {
			editedSinceLastSave = true;
		}
		observer.update(o, arg);
	}
	
	public boolean createPage(String filename, String name) {
		//TODO: check for name duplication and validity (i.e. not 'index')
		if(Page.isValidFilename(filename))
			return false;
		Page p = new Page(filename, name, this);
		pages.add(p);
		update(p, filename);
		return true;
	}
	
	public void deletePage(Page p) {
		pages.remove(p);
		if(pages.size() == 0)
			pages.add(new Page("startpage.html", "Home", this));
		update(this, null);
	}
	
	public void deletePage(String name) {
		for(int i=0; i<pages.size(); ++i) {
			if(pages.get(i).getFilename().equals(name)) {
				pages.remove(i);
				if(pages.size() == 0)
					pages.add(new Page("startpage.html", "Home", this));
				update(this, null);
				return;
			}
		}
	}
	
	public void deletePage(int index) {
		pages.remove(index);
		if(pages.size() == 0)
			pages.add(new Page("startpage.html", "Home", this));
		update(this, null);
	}
	
	public void setPages(LinkedList<Page> p) {
		pages = new LinkedList<>(p);
		if(pages.size() == 0) 
			pages.add(new Page("startpage.html", "Home", this));
		update(this, null);
	}
	
	public LinkedList<Page> getPages() {
		return pages;
	}
	
	/* 
	 * Mark as saved and cut all connections to objects outside the data to be stored 
	 * (so nothing gets accidentally stored with the data).
	 */
	public void save() {
		editedSinceLastSave = false;
		observer = null;
	}
	
	public boolean isEditedSinceLastSave() {
		return editedSinceLastSave;
	}
	
	//Returns a hash of all page-versions
	public String export() {
		//TODO: implement using the export-method of the pages
		return null;
	}
	
}
