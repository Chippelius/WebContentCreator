
/*
 * Represents a page and its elements.
 * 
 * Created by Leo Köberlein on 26.07.2017
 */
public class Page {

	private String name;
	private Element[] elements;
	
	public Page(String s) {
		name = s;
		elements = null;
	}
	
	public void setName(String s) {
		name = s;
	}
	
	public void setElements(Element[] e) {
		elements = e;
	}
	
	public String getName() {
		return name;
	}
	
	public Element[] getElements() {
		return elements;
	}
	
}
