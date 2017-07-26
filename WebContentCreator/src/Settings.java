import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.Serializable;

/*
 * Represents the program's settings
 * 
 * Created by Leo Köberlein on 10.07.2017
 */
public class Settings implements Serializable, SettingsInterface {

	private static final long serialVersionUID = 1L;
	
	private Point windowLocation;
	private Dimension windowSize;
	private boolean fullscreen;
	private int dividerLocation;
	
	public Settings () {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		windowLocation = new Point(toolkit.getScreenSize().width/4, toolkit.getScreenSize().height/4);
		windowSize = new Dimension(toolkit.getScreenSize().width/2, toolkit.getScreenSize().height/2);
		fullscreen = false;
		dividerLocation = windowSize.width/2;
	}

	@Override
	public void setLocation(Point location) {
		windowLocation = location;
	}

	@Override
	public Point getLocation() {
		return windowLocation;
	}

	@Override
	public void setSize(Dimension size) {
		windowSize = size;
	}

	@Override
	public Dimension getSize() {
		return windowSize;
	}

	@Override
	public void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
	}

	@Override
	public boolean isFullscreen() {
		return fullscreen;
	}

	@Override
	public void setDividerLocation(int location) {
		dividerLocation = location;
	}

	@Override
	public int getDividerLocation() {
		return dividerLocation;
	}
	
}
