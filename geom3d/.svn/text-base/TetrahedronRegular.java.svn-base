package geom3d;

public class TetrahedronRegular extends Tetrahedron {
	
	public TetrahedronRegular(Point3d a, Point3d b, Point3d c, Point3d d) { super(a,b,c,d); }

	/**
	 * returns the length of the side of this regular tetrahedron
	 * @return
	 */
	public double getSideLength() { return points[0].getDistance(points[1]); }
	
	public double volume() {
		double a = getSideLength();
		return Math.sqrt(2)*a*a*a/12;
	}
	
	public double surface() {
		double a = getSideLength();
		return Math.sqrt(3)*a*a;
	}
	
	public double height() { return Math.sqrt(2/3)*getSideLength(); }
}
