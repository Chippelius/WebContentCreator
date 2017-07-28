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
	private String lookAndFeel;
	
	public Settings () {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		windowLocation = new Point(toolkit.getScreenSize().width/4, toolkit.getScreenSize().height/4);
		windowSize = new Dimension(toolkit.getScreenSize().width/2, toolkit.getScreenSize().height/2);
		fullscreen = false;
		dividerLocation = windowSize.width/2;
		lookAndFeel = windowsLookAndFeel;
	}

	/* (non-Javadoc)
	 * @see SettingsInterface#setLocation(java.awt.Point)
	 */
	@Override
	public void setLocation(Point location) {
		windowLocation = location;
	}
	/* (non-Javadoc)
	 * @see SettingsInterface#getLocation()
	 */
	@Override
	public Point getLocation() {
		return windowLocation;
	}

	/* (non-Javadoc)
	 * @see SettingsInterface#setSize(java.awt.Dimension)
	 */
	@Override
	public void setSize(Dimension size) {
		windowSize = size;
	}
	/* (non-Javadoc)
	 * @see SettingsInterface#getSize()
	 */
	@Override
	public Dimension getSize() {
		return windowSize;
	}

	/* (non-Javadoc)
	 * @see SettingsInterface#setFullscreen(boolean)
	 */
	@Override
	public void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
	}
	/* (non-Javadoc)
	 * @see SettingsInterface#isFullscreen()
	 */
	@Override
	public boolean isFullscreen() {
		return fullscreen;
	}

	/* (non-Javadoc)
	 * @see SettingsInterface#setDividerLocation(int)
	 */
	@Override
	public void setDividerLocation(int location) {
		dividerLocation = location;
	}
	/* (non-Javadoc)
	 * @see SettingsInterface#getDividerLocation()
	 */
	@Override
	public int getDividerLocation() {
		return dividerLocation;
	}
	
	/* (non-Javadoc)
	 * @see SettingsInterface#setLookAndFeel(java.lang.String)
	 */
	@Override
	public void setLookAndFeel(String newLookAndFeel) {
		lookAndFeel = newLookAndFeel;
	}
	/* (non-Javadoc)
	 * @see SettingsInterface#getLookAndFeel()
	 */
	@Override
	public String getLookAndFeel() {
		return lookAndFeel;
	}
	
}
