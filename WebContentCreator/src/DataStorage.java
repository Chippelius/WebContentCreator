import java.util.Arrays;
import java.util.Observable;

/*
 * Represents a storage file's content.
 * 
 * Created by Leo Köberlein on 10.07.2017
 */
public class DataStorage extends Observable implements SerializableObserver {

	private static final long serialVersionUID = 1L;
	
	private Page[] pages;
	private boolean editedSinceLastSave;
	private SerializableObserver observer;
	
	public DataStorage(SerializableObserver o) {
		pages = new Page[] {new Page("startpage.html", "Home", this)};
		editedSinceLastSave = true;
		observer = o;
	}
	
	public void setObserver(SerializableObserver o) {
		this.observer = o;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if(!editedSinceLastSave) {
			editedSinceLastSave = true;
		}
		observer.update(o, arg);
	}
	
	public void createPage(String filename, String name) {
		//TODO: check for name duplication and validity (i.e. not 'index')
		pages = Arrays.copyOf(pages, pages.length+1);
		pages[pages.length-1] = new Page(filename, name, this);
		update(pages[pages.length-1], filename);
	}
	
	public void deletePage(Page p) {
		for(int i=0; i<pages.length; ++i) {
			if(pages[i] == p) {
				deletePage(i);
				return;
			}
		}
	}
	
	public void deletePage(String name) {
		for(int i=0; i<pages.length; ++i) {
			if(pages[i].getFilename().equals(name)) {
				deletePage(i);
				return;
			}
		}
	}
	
	public void deletePage(int index) {
		Page[] res = new Page[pages.length-1];
		if(index > 0) {
			System.arraycopy(pages, 0, res, 0, index);
		}
		if(index < res.length) {
			System.arraycopy(pages, index+1, res, index, res.length-index);
		}
		pages = res;
		update(this, null);
	}
	
	public void setPages(Page[] p) {
		if(p.length > 0) {
			pages = p;
		} else {
			pages = new Page[] {new Page("startpage.html", "Home", this)};
		}
		update(this, null);
	}
	
	public Page[] getPages() {
		return pages;
	}
	
	public void save() {
		editedSinceLastSave = false;
	}
	
	//Returns a hash of all page-versions
	public String export() {
		//TODO: implement using the export-method of the pages
		return null;
	}
	
}
