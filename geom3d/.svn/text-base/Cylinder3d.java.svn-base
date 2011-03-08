package geom3d;

import matrix.Matrix3x3;
import misc.Functions;

public class Cylinder3d implements Volume3d{
	protected double r;
	protected Point3d anchor;
	protected Segment3d sgm;
	
	public Cylinder3d() {
	}

	public Cylinder3d(Point3d p1, Point3d p2, double r) {
		this(r, new Segment3d(p1, p2));
	}
	public Cylinder3d(double r, Segment3d sgm) {
		this.r = r;
		this.anchor = new Point3d(0, 0, 0);
		this.sgm = sgm;
	}

	
	public Cylinder3d(double r, Point3d anchor, Segment3d sgm) {
		this.r = r;
		this.anchor = anchor;
		this.sgm = sgm;
	}
	
	/*
	 * returns minimum radius cylinder with given three points on the surface
	 */
	public Cylinder3d(Point3d p0, Point3d p1, Point3d p2) {
		Point3d a = p0, b = p1, c = p2;
		if (a.getSquaredDistance(b) < a.getSquaredDistance(c)) { b = p2; c = p1; }
		if (a.getSquaredDistance(b) < b.getSquaredDistance(c)) { a = c;  c = p0; } 
		a.toConsole(3); b.toConsole(3); c.toConsole();
		sgm = new Segment3d(a, b);
		Segment3d ab = new Segment3d(a, b);
		ab.getClosestPoint(c).toConsole(3);
		Segment3d cd = new Segment3d(c, ab.getClosestPoint(c));
		anchor = cd.getMidPoint();
		r = cd.getLength()/2;
	}
	
	public Cylinder3d(Circle3d c, double length) {
		r = c.radius;
		anchor = c.center;
		Vector3d v = c.normalVector.createScaledToLengthVector3d(length/2);
		Point3d a = anchor.clone();
		Point3d b = anchor.clone();
		sgm = new Segment3d(a.subIn(v), b.addIn(v));
	}
	
	
	public Cylinder3d(PointSet3d points) {
		int n = points.getSize();
		Point3d a = points.get(0); Point3d b = points.get(n-1);
		Line3d line = new Line3d(a, new Vector3d(a, b));
		sgm = line.orthogonalProjection(points);
		r = line.getMaxDistance(points);
		anchor = sgm.getMidPoint();
	}
	
	public Point3d getAnchor() { return anchor; }
	public Segment3d getSegment() { return sgm; }
	public double getLength() { return 2*sgm.getLength(); }
	public double getRadius() { return r; }
	public double volume() { return Math.PI * r * r * getLength(); }
	public double getSurfaceArea() { return 2*Math.PI * r * (r + getLength()); }
	
	public boolean inCylinder(Point3d p) { return sgm.getSquaredDistance(p) < r; }
	
	public String toString(int dec) {
		return "Anchor: " + anchor.toString(dec) + ", Radius: " + Functions.toString(r,dec) + 
		       ", Direction: " + sgm.toString(dec);
	}

	
	public void toConsole(int dec) { System.out.println(toString(dec)) ; }
		
	
	public static void main(String[] args) {
		//fix three points
		Point3d p0 = new Point3d(-1,0,0);
		Point3d p1 = new Point3d(3,1,0);
		Point3d p2 = new Point3d(-2,0,0);
		Cylinder3d cylinder = new Cylinder3d(p0, p1, p2);
		cylinder.toConsole(3);
	}

	public Point3d getCenter() {
		return sgm.getMidPoint();
	}

	public static Cylinder3d createBoundingCylinder_CovarianceFit(PointSet3d points) {
		if(points.getSize()<=0)	throw new Error("Cannot create cylinder enclosing 0 points");
		if(points.getSize()==1)	return new Cylinder3d(points.get(0).clone(), points.get(0).clone(), 0);
		if(points.getSize()==2)	return new Cylinder3d(points.get(0).clone(), points.get(1).clone(), 0);

		Matrix3x3 covMatr = Matrix3x3.createCovarianceMatrix(points);//points.getCoVariance();//Matrix.createCovarianceMatrix(points);
		Vector3d[] eigenVecs = covMatr.getEigenvectors();
		
		Vector3d dir = eigenVecs[0];
		if(eigenVecs[1].length()>dir.length()) dir = eigenVecs[1];
		if(eigenVecs[2].length()>dir.length()) dir = eigenVecs[2];

		InfCylinder3d iCyl = InfCylinder3d.createMinRadCylinderFromDirection(points, dir);
		Cylinder3d ret = iCyl.capWithDiscs(points);
		return ret;
	}

	public boolean overlaps(Volume3d vol) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public Cylinder3d clone(){ throw new Error("Not implemented"); }

}
