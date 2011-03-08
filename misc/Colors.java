package misc;

import java.awt.*;
import javax.vecmath.Color3f;

public class Colors {
	public static Color3f red =    new Color3f(1.0f, 0.0f, 0.0f);
	public static Color3f yellow = new Color3f(1.0f, 1.0f, 0.0f);
	public static Color3f magenta= new Color3f(1.0f, 0.0f, 1.0f);
	public static Color3f cyan =   new Color3f(0.0f, 1.0f, 1.0f);
	public static Color3f green =  new Color3f(0.0f, 1.0f, 0.0f);
	public static Color3f blue  =  new Color3f(0.0f, 0.0f, 1.0f);
	public static Color3f white =  new Color3f(1.0f, 1.0f, 1.0f);
	public static Color3f gray  =  new Color3f(0.4f, 0.4f, 0.4f);
	public static Color3f black =  new Color3f(0.0f, 0.0f, 0.0f);

	private static Color[] spectrum = { Color.black, Color.gray, Color.blue, Color.green, Color.cyan, Color.yellow, Color.magenta, Color.red }; 
	
	
	public static Color getColor(int i, int max)  { 
		System.out.println(i + " " + (max+1) + " " + i*spectrum.length/(max+1)); 
		return spectrum[i* spectrum.length/(max+1)]; 
	}
}

