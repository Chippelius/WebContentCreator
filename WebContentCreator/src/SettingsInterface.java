import java.awt.Dimension;
import java.awt.Point;

public interface SettingsInterface {

	String windowsLookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
	String nimbusLookAndFeel = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
	String motifLookAndFeel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";

	void setLocation(Point location);

	Point getLocation();

	void setSize(Dimension size);

	Dimension getSize();

	void setFullscreen(boolean fullscreen);

	boolean isFullscreen();

	void setDividerLocation(int location);

	int getDividerLocation();

	void setLookAndFeel(String newLookAndFeel);

	String getLookAndFeel();

}