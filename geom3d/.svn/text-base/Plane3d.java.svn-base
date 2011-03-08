package geom3d;
import j3dScene.J3DScene;

import java.awt.Color;

import misc.Constants;

public class Plane3d {

	protected Vector3d n; // normal vector of the plane - unit vector
	protected Point3d p;  // point in the plane
	protected double d;   // perpendicular distance from the origin
	
	/*
	 * creates a plane with the normal vector n and containing point p
	 */
	public Plane3d(Vector3d n, Point3d p) {
		this.n = n.createUnitVector();
		this.p = p;
		d = Vector3d.dotProduct(this.n, this.p);
	}
	
	/*
	 * creates a plane with the normal vector n and trough the tip of vector v 
	 */
	public Plane3d(Vector3d n, Vector3d v) {
		this.n = n.createUnitVector();
		this.p = new Point3d(v.x, v.y, v.z);
		d = Vector3d.dotProduct(this.n, this.p);
	}
	
	/*
	 * creates a plane through three distinct points
	 */
	public Plane3d(Point3d p, Point3d q, Point3d r) {
		n = Vector3d.crossProduct(new Vector3d(q,p),new Vector3d(q,r));
		n.makeUnitVector();
		this.p = q;
		d = Vector3d.dotProduct(this.n, this.p);
	}
	
	/*
	 * creates a bisecting plane between points p and q
	 */
	public Plane3d(Point3d p, Point3d q) {
		n = new Vector3d(p, q);
		n.makeUnitVector();
		this.p = new Point3d(p,q);
		d = Vector3d.dotProduct(n, this.p);
	}

	
	public void setNormal(Vector3d n) { this.n = n; }
	
	/*
	 * projects point p onto this plane
	 */
	public Point3d projectPoint(Point3d p) {
		double t = n.x*p.x + n.y*p.y + n.z*p.z + d;
		return new Point3d(p.x - n.x*t, p.y - n.y*t, p.z - n.z*t);
	}
	
	/*
	 * returns (1,0,-1) if point p is (above,on,below) this plane
	 */
	public int above(Point3d p) {
		double dotP = Vector3d.dotProduct(n,p);
		if (dotP < d) return 1;
		if (dotP > d) return -1; 
		return 0;
	}
	
	/*
	 * returns (1,0,-1) if point p is (below,on,above) this plane
	 */
	public int below(Point3d p) { return -above(p); }
	
	/*
	 * returns true if the plane pl is parallel to this plane
	 */
	public boolean parallel(Plane3d pl) { return Math.abs(n.dot(pl.n)) == 1;
	}
	
	/*
	 * returns the point on the plane closest to the point q
	 */
	public Point3d getClosestPoint(Point3d q) {
		double t = getSignedDistance(q);
		return new Point3d(q.x - t*n.x, q.y - t*n.y, q.z - t*n.z);
	}
	
	/*
	 * returns signed distance of the point q to the plane. If distance is positive, q is on the 
	 * same side of the plane as its normal vector
	 */
	public double getSignedDistance(Point3d q) { return Vector3d.dotProduct(n, new Vector3d(p, q)); }
	
	/*
	 * returns perpendicular distance of point p to this plane (recall that n is a unit vector)
	 */
	public double getDistance(Point3d q) { return Math.abs(getSignedDistance(q)); }
	
	/*
	 * returns the angle between two planes (no sign)
	 */
	public static double dihedralAngle(Plane3d h1, Plane3d h2) { return Math.acos(Vector3d.dotProduct(h1.n, h2.n)); } 
	
	
	/*
	 * returns true if the plane intersects a sphere 
	 */
	public boolean isIntersecting(Sphere3d sphere) {
		return Math.abs(Vector3d.dotProduct(sphere.center, n) - d) <= sphere.radius;
	}
	
	/*
	 * returns intersection of a line with the plane
	 */
	public Point3d getIntersection(Line3d line) {

		double denom = Vector3d.dotProduct(n, line.getDir());
		if (Math.abs(denom) < Constants.epsilon) return null;
		else {
			Point3d a = line.getP();
			Vector3d pa = new Vector3d(p, a);
			double u = Vector3d.dotProduct(n, pa)/denom;
			return new Point3d(a.x + u*line.dir.x, a.y + u*line.dir.y, a.z + u*line.dir.z);
		}
	}
	
	/*
	 * returns intersection of a segment with the plane
	 */
	public Point3d getIntersection(Segment3d sgm) {
		Vector3d dir = new Vector3d(sgm.a, sgm.b);
		double denom = Vector3d.dotProduct(n, dir);
		if (denom < Constants.epsilon) return null;
		else {
			Vector3d pa = new Vector3d(p, sgm.a);
			double u = Vector3d.dotProduct(n, pa)/denom;
			if ((u < 0) || (u > 1)) return null;
			else {
				return new Point3d(sgm.a.x + u*dir.x, sgm.a.y + u*dir.y, sgm.a.z + u*dir.z);}
		}
	}
	
	/*
	 * returns the intersection line with another plane
	 */
	public Line3d getIntersection(Plane3d pl) {
//		this.toConsole();
//		pl.toConsole();
		Vector3d dir = Vector3d.crossProduct(n, pl.n);
//		dir.toConsole();
		if (dir.dot(dir) < Constants.epsilon) throw new RuntimeException("Two parallel planes"); 
		double d12 = n.dotProduct(pl.n); //System.out.println("d12 = " + d12);
		double denom = 1 - d12*d12; //System.out.println("denom = " + denom);
		double k1 = (d - pl.d*d12)/denom; //System.out.println("k1 = " + k1);
		double k2 = (pl.d - d*d12)/denom; //System.out.println("k2 = " + k2);
		Point3d q = new Point3d(k1*n.x + k2*pl.n.x, k1*n.y + k2*pl.n.y, k1*n.z + k2*pl.n.z);
//		q.toConsole();
		return new Line3d(q, dir);
	}

	/*
	 * returns the intersection point with two other planes using Crammer's rule, see Ericson, Section 3.1.5. 
	 * Runtime exception is thrown if at least two of the planes are parallel.
	 * Modified by Pawel on June 23, 2010
	 */
	public static Point3d getIntersection(Plane3d pl1, Plane3d pl2, Plane3d pl3) {
		double denom =  pl1.n.x*pl2.n.y*pl3.n.z + pl1.n.y*pl2.n.z*pl3.n.x + pl1.n.z*pl2.n.x*pl3.n.y -
		                pl1.n.x*pl2.n.z*pl3.n.y - pl1.n.y*pl2.n.x*pl3.n.z - pl1.n.z*pl2.n.y*pl3.n.x;
		if (denom < Constants.epsilon) throw new RuntimeException("At least two planes are parallel.");
		else {
			double d1 = pl1.n.x*pl1.p.x + pl1.n.y*pl1.p.y + pl1.n.z*pl1.p.z;
			double d2 = pl2.n.x*pl2.p.x + pl2.n.y*pl2.p.y + pl2.n.z*pl2.p.z;
			double d3 = pl3.n.x*pl3.p.x + pl3.n.y*pl3.p.y + pl3.n.z*pl3.p.z;
			double a =  d1*pl2.n.y*pl3.n.z + pl1.n.y*pl2.n.z*d3 + pl1.n.z*d2*pl3.n.y -
            			d1*pl2.n.z*pl3.n.y - pl1.n.y*d2*pl3.n.z - pl1.n.z*pl2.n.y*d3;
			double b =  pl1.n.x*d2*pl3.n.z + d1*pl2.n.z*pl3.n.x + pl1.n.z*pl2.n.x*d3 -
            			pl1.n.x*pl2.n.z*d3 - d1*pl2.n.x*pl3.n.z - pl1.n.z*d2*pl3.n.x;
			double c =  pl1.n.x*pl2.n.y*d3 + pl1.n.y*d2*pl3.n.x + d1*pl2.n.x*pl3.n.y -
            			pl1.n.x*d2*pl3.n.y - pl1.n.y*pl2.n.x*d3 - d1*pl2.n.y*pl3.n.x;
			return new Point3d(a/denom, b/denom, c/denom);
		}
	}
	/**
	 * @author Rasmus 
	 */
	public double intersectionParameter(Line3d line) {
		double denom = Vector3d.dotProduct(n, line.getDir());
		if (denom < Constants.epsilon) throw new RuntimeException("Line is parallel to plane");
		else {
			Point3d a = line.getP();
			Vector3d pa = new Vector3d(p, a);
			double u = Vector3d.dotProduct(n, pa)/denom;
			return u;
		}
	}
	
	public void toConsole() {
		System.out.println("Plane through the point " + p.toString() + " with the normal vector " + n.toString());
	}
	
	public void toConsole(int dec) {
		System.out.println("Plane through the point " + p.toString(dec) + " with the normal vector " + n.toString(dec));
	}

	public void draw(J3DScene scene) {
		Vector3d v1 = new Vector3d(n.x, n.y, n.z); 
		Vector3d v2 = new Vector3d(1.0, 11.0, 111.0).scaleToLength(1).cross(v1);
		Vector3d v3 = Vector3d.crossProduct(v1,v2);
		v1.scaleToLength(0.05);
		v2.scaleToLength(3);
		v3.scaleToLength(3);
		geom3d.Box3d box01 = new geom3d.Box3d(p, v1, v2, v3);
		scene.addShape(box01, new Color(50,50,50,50));
		scene.addShape(new Sphere3d(p, 0.12f), Color.YELLOW);
	}

	
	
	public static void main(String[] args) {
		Plane3d pl1 = new Plane3d(new Point3d(4,0,0), new Point3d(0,4,0), new Point3d(0,0,4));
		Plane3d pl2 = new Plane3d(new Point3d(0,0,0), new Point3d(4,0,0), new Point3d(0,0,4));
		Plane3d pl3 = new Plane3d(new Point3d(0,0,0), new Point3d(4,0,0), new Point3d(0,4,0));
		
		J3DScene j3ds = J3DScene.createJ3DSceneInFrame();
		j3ds.setBackgroundColor(Color.WHITE);
		j3ds.centerCamera();
		j3ds.autoZoom();
		pl1.draw(j3ds);
		pl2.draw(j3ds);
		pl3.draw(j3ds);
		Point3d p = Plane3d.getIntersection(pl1, pl2, pl3);
		j3ds.addShape(new Sphere3d(p, 0.12f), Color.YELLOW);
		
	}

}
