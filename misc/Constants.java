package misc;

import java.awt.*;
public class Constants {
	public static double infinty = Double.MAX_VALUE; 
	public static double epsilon = 0.0000001;
	public static double pi = Math.PI;

	// constants needed to draw within a given frame
	public static int getWidth(Frame f) { return  f.getWidth() - (int)(f.getWidth()/10); }
	public static int getHeight(Frame f) { return f.getHeight() - (int)(f.getHeight()/10); }
	public static int getOffsetWidth(Frame f) { return  (int)(f.getWidth()/20); }
	public static int getOffsetHeight(Frame f) { return  (int)(f.getHeight()/20); }

}
