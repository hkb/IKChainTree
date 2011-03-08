package geom3d;

public class Segment3d {
	Point3d a;
	Point3d b;
	double lengthSq=-1;

	public Segment3d() {
		a = new Point3d(0,0,0);
		b = new Point3d(0,0,0);
		this.lengthSq = 0; 
	}
	
	/*
	 * creates a segment between points a and b.
	 */
	public Segment3d(Point3d a, Point3d b) {
		this.a = a;
		this.b = b;
		lengthSq = a.getSquaredDistance(b);
	}
	
	/*
	 * creates a segment between a and the end of vector v starting at a. 
	 */
	public Segment3d(Point3d a, Vector3d v) {
		this.a = a;
		b = a;
		b.translate(v);
		lengthSq = a.getSquaredDistance(b);
	}

	public Point3d getA() { return a; } public void setA(Point3d a) { this.a = a; }
	public Point3d getB() { return b; } public void setB(Point3d b) { this.b = b; }
	public Vector3d getAToB(){ return a.vectorTo(b); }
	
	public double getLength() { return Math.sqrt(lengthSq); }
	public double getSquaredLength() { 
//		if(lengthSq<0) {
//			double bax = b.x - a.x, bay = b.y - a.y, baz = b.z - a.z;
//			lengthSq = bax*bax + bay*bay + baz*baz;
//		}
		return lengthSq;
	}
	
	/*
	 * returns point on the segment closest to a given point
	 */
	public Point3d getClosestPoint(Point3d q) {
		Vector3d dir = new Vector3d(a, b);
		Vector3d aq = new Vector3d(a, q);
		double t = Vector3d.dotProduct(dir, aq)/dir.getSquaredLength();
		if (t <= 0.0) return a; 
		else { 
			if (t >= 1.0) return b;
			else return new Point3d(a.x + t*dir.x, a.y + t*dir.y, a.z + t*dir.z);
		}
	}

	/*
	 * returns squared distance from a given point
	 */
	public double getSquaredDistance(Point3d q) { return q.getSquaredDistance(getClosestPoint(q)); }
	
	/*
	 * returns the midpoint of the segment
	 */
	public Point3d getMidPoint() { return new Point3d(a.x + (b.x - a.x)/2, a.y + (b.y - a.y)/2, a.z + (b.z - a.z)/2); }


	
	public String toString() { return "[" + a.toString() + "," + b.toString() + "]"; } 
	public String toString(int dec) { return "[" + a.toString(dec) + "," + b.toString(dec) + "]"; } 
	
	public void toConsole() { System.out.println(toString()); }
	public void toConsole(int dec) { System.out.println(toString(dec)); }
}
