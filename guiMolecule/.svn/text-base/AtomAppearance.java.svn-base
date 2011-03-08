package guiMolecule;

import javax.vecmath.Color3f;
import javax.media.j3d.*;
import misc.Colors;

public class AtomAppearance extends Appearance {

	public static AtomAppearance alphaCarbon = new AtomAppearance(Colors.gray);
	public static AtomAppearance carbon      = new AtomAppearance(Colors.black);
	public static AtomAppearance oxygen      = new AtomAppearance(Colors.red);
	public static AtomAppearance sulphur     = new AtomAppearance(Colors.yellow);
	public static AtomAppearance nitrogen    = new AtomAppearance(Colors.blue);
	public static AtomAppearance hydrogen    = new AtomAppearance(Colors.white);

	
	public AtomAppearance(Color3f color) {
		setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_READ);
		setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
		setColoringAttributes(new ColoringAttributes(color, ColoringAttributes.NICEST));
	}
}
