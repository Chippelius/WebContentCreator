import java.awt.Dimension;
import java.awt.Point;

public interface SettingsInterface {

	public void setLocation(Point location);
	public Point getLocation();
	
	public void setSize(Dimension size);
	public Dimension getSize();
	
	public void setFullscreen(boolean fullscreen);
	public boolean isFullscreen();
	
	public void setDividerLocation(int location);
	public int getDividerLocation();
	
}
