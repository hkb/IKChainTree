package gui3d;

import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Geometry;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;

public class Point3dShape extends Shape3D {
//	private Geometry point3dGeometry;
	private Appearance point3dAppearance;
	public Point3dShape() {
//		point3dGeometry = createGeometry();
		point3dAppearance = createAppearance();
//		this.setGeometry(point3dGeometry);
		this.setAppearance(point3dAppearance);
	}
//	private Geometry createGeometry() {
//		Geometry geometry = new PointArray();
//		return geometry;
//	}
	private Appearance createAppearance() {
		Appearance appearance = new Appearance();
		appearance.setColoringAttributes(
				new ColoringAttributes(new Color3f(0.0f,0.0f,1.0f),ColoringAttributes.NICEST));
		return appearance;

	}
}

