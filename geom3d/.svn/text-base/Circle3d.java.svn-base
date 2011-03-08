package geom3d;
import geom2d.*;
import matrix.Matrix3x3;
import misc.*;

/*
 * If the circle has center c, radius r, and unit-length normal vector n, compute unit-length vectors u and v 
 * so that {u,v,n} are mutually orthogonal. The circle is then parameterized by P(t) = c + r*(cos(t)*u + sin(t)*v) 
 * for 0 <= t < 2*pi. 
 */


public class Circle3d {
	Point3d center;
	double radius;
	Vector3d normalVector;

	public Circle3d(Point3d center, double radius, Vector3d normalVector) {
		this.center = center;
		this.radius = radius;
		this.normalVector = normalVector;
	}
	
	/*
	 * Given three points p0, p1, p2 and a vector v, find the circle or just the radius of the circle through the 
	 * projection of the points onto the plane with v as its normal vector. Claim: radius is minimized when of the 
	 * plane goes through p0, p1, p2
	 */
	public Circle3d(Point3d p0, Point3d p1, Point3d p2, Vector3d v) {
		// create the plane through the origo with v as its normal vector
		Plane3d plane = new Plane3d(v, new Point3d(0,0,0));
		Point3d q0 = plane.projectPoint(p0);
		Point3d q1 = plane.projectPoint(p1);
		Point3d q2 = plane.projectPoint(p2);
		double a = q0.getSquaredDistance(q1);
		double b = q1.getSquaredDistance(q2);
		double c = q2.getSquaredDistance(q0);
		radius = Math.sqrt(a*b*c)/Math.sqrt(2*a*b + 2*b*c + 2*c*a - a*a - b*b - c*c);
		normalVector = Vector3d.crossProduct(new Vector3d(p0,p1), new Vector3d(p0,p2));
	}
	
	/*
	 * Circle in the plane through p0, p1, p2
	 */
/*	what is going on here? Pawe
 * public Circle3d(Point3d p0, Point3d p1, Point3d p2) {
		// create corner vectors
		Vector3d v0 = new Vector3d(p0);
		// create edge vectors
		Vector3d v01 = new Vector3d(p0, p1); v01.scale(0.5);
		Vector3d v02 = new Vector3d(p0, p2); v02.scale(0.5);
		// create mid vectors
		Vector3d m01 = Vector3d.createSum(v0, v01);
		Vector3d m02 = Vector3d.createSum(v0, v02);
		// create normal vector
		normalVector = Vector3d.crossProduct(v01, v02);
		// get center as the intersection of three planes: 
		// 1. through p0 with normal vector normalVector. 2. through m01 with v01 as normal vector.  3. through m02 with v02 as normal vector
		Matrix3x3 m = new Matrix3x3(v01.x, v01.y, v01.z, v02.x, v02.y, v02.z, normalVector.x, normalVector.y, normalVector.z );
//		m.toConsole(3);
		center = m.solve(new Point3d(Vector3d.dotProduct(v01, m01), Vector3d.dotProduct(v02, m02), Vector3d.dotProduct(normalVector, v0)));
		radius = p0.getDistance(center);
	}
*/	
	/**
	 * circle through p0, p1, p2
	 */
	public Circle3d(Point3d p0, Point3d p1, Point3d p2) {
		center = new Point3d((p0.x+p1.x+p2.x)/3, (p0.y+p1.y+p2.y)/3, (p0.z+p1.z+p2.z)/3);
		radius = p0.getDistance(center);
	}
		

	/*
	 * returns the radius of the circle through 3 given points (without creating the circle)
	 */
	public static double getRadius(Point3d p0, Point3d p1, Point3d p2) {

		// get the plane through q0, q1, q2
		
		Point3d q0 = new Point3d(0,0,0);
		Point3d q1 = new Point3d(p1.x-p0.x, p1.y-p0.y, p1.z-p0.z);
		Point3d q2 = new Point3d(p2.x-p0.x, p2.y-p0.y, p2.z-p0.z);
		
		// get the plane through q0, q1, q2
		
		Plane3d plane = new Plane3d(q0,q1,q2);
		Vector3d verticalVector = new Vector3d(0,0,1);
		double angle = Vector3d.getAngle(plane.n, verticalVector);
		Vector3d rotationVector = Vector3d.crossProduct(plane.n, verticalVector);
		rotationVector.makeUnitVector();
		q1.rotation(rotationVector, angle);
		q2.rotation(rotationVector, angle);
		Point2d r0 = new Point2d(0,0);
		Point2d r1 = new Point2d(q1.x, q1.y);
		Point2d r2 = new Point2d(q2.x, q2.y);
		Circle2d circle2 = new Circle2d(r0, r1, r2);
		return circle2.getRadius();
	}
	
	public Point3d getCenter() { return center; }
	public double getRadius() { return radius; }
	public Vector3d getNormalVector() { return normalVector; }

	
	public static Circle3d getEquilateralCircle(Point3d a, Point3d b) {
		Point3d center = new Point3d(a, b);
		Vector3d ab = new Vector3d(a, b);
		double radius = Math.sqrt(3)*ab.getLength()/2;
		return new Circle3d(center, radius, ab);
	}
	
	public String toString(int dec) {
		return "Center: " + center.toString(dec) + ", Radius: " + Functions.toString(radius,dec) + 
		       ", Normal: " + normalVector.toString(dec);
	}
	public void toConsole(int dec) { System.out.println(toString(dec)); }
 
	
	
	public static void main(String[] args) {
		Point3d p0 = new Point3d(0,0,0);
		Point3d p1 = new Point3d(-1,-0.5, -1);
		Point3d p2 = new Point3d(-1, 1, 12);
		Circle3d c = new Circle3d(p0,p1,p2);
		c.toConsole(3);
		System.out.println(c.center.getDistance(p0));
		System.out.println(c.center.getDistance(p1));
		System.out.println(c.center.getDistance(p2));
	}
}

