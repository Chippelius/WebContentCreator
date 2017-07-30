import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.Serializable;

/*
 * Represents the program's settings
 * 
 * Created by Leo Köberlein on 10.07.2017
 */
public class Settings implements Serializable {

	private static final long serialVersionUID = 1L;

	private Point windowLocation;
	private Dimension windowSize;
	private boolean maximized;
	private int dividerLocation;
	
	public Settings () {
		refresh();
	}
	
	//To be called after being loaded to fill empty variables (i.e. new attributes after an update)
	public void refresh() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		if(windowLocation == null)
			windowLocation = new Point(toolkit.getScreenSize().width/4, toolkit.getScreenSize().height/4);
		if(windowSize == null) 
			windowSize = new Dimension(toolkit.getScreenSize().width/2, toolkit.getScreenSize().height/2);
		if(dividerLocation == 0)
			dividerLocation = (windowSize.width - 5)/2;
		
	}

	
	public void setLocation(Point location) {
		windowLocation = location;
	}
	public Point getLocation() {
		return windowLocation;
	}
	
	public void setSize(Dimension size) {
		windowSize = size;
	}
	public Dimension getSize() {
		return windowSize;
	}
	
	public void setFullscreen(boolean fullscreen) {
		this.maximized = fullscreen;
	}
	public boolean isFullscreen() {
		return maximized;
	}

	public void setDividerLocation(int location) {
		dividerLocation = location;
	}
	public int getDividerLocation() {
		return dividerLocation;
	}
	
}
