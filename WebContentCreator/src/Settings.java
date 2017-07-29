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
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		windowLocation = new Point(toolkit.getScreenSize().width/4, toolkit.getScreenSize().height/4);
		windowSize = new Dimension(toolkit.getScreenSize().width/2, toolkit.getScreenSize().height/2);
		maximized = false;
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
