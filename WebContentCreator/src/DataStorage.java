import java.io.Serializable;

/*
 * Represents a storage file and its content.
 * 
 * Created by Leo Köberlein on 10.07.2017
 */
public class DataStorage implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Page[] pages;
	
	public void setPages(Page[] p) {
		pages = p;
	}
	
	public Page[] getPages() {
		return pages;
	}

}
