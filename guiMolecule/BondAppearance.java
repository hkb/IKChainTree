package guiMolecule;
import javax.media.j3d.*;

import misc.Colors;

public class BondAppearance extends Appearance {

	public static BondAppearance bond = new BondAppearance();
	
	public BondAppearance() {
	    setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_READ);
		setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);

		setLineAttributes(new LineAttributes(2, LineAttributes.PATTERN_SOLID, false));
		setColoringAttributes(new ColoringAttributes(Colors.blue, ColoringAttributes.NICEST));
		setPolygonAttributes(new PolygonAttributes(PolygonAttributes.POLYGON_LINE, PolygonAttributes.CULL_NONE,1.0f));


	}

}
