package geom3d;

import geom2d.Line2d;
import geom2d.Point2d;
import geom2d.Vector2d;
import j3dScene.J3DScene;

import java.awt.Color;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Random;

import misc.*;


public class Point3d {//extends javax.vecmath.Point3d {
	private static final long serialVersionUID = 1L;
	public double x,y,z;
	
	public Point3d() { x = y = z = 0.0; }
	
	public Point3d(double x, double y, double z) { this.x = x; this.y = y; this.z = z; }
	
	/*
	 * creates a clone of the given point
	 */
	public Point3d(Point3d p) { x = p.x; y = p.y; z = p.z; } 
	
	public Point3d(Vector3d v) { x = v.x; y = v.y; z = v.z; }
	
	public Point3d(Random randGen) {
		x = new Double(randGen.nextDouble());
		y = new Double(randGen.nextDouble());
		z = new Double(randGen.nextDouble());
	}
	
	public Point3d(String s) throws NumberFormatException{
		StringTokenizer st = new StringTokenizer(s,"[,]");
		try{
			st.nextToken(); //get rid of leading v
			x = Double.valueOf(st.nextToken()).doubleValue();
			y = Double.valueOf(st.nextToken()).doubleValue();
			z = Double.valueOf(st.nextToken()).doubleValue();
		} 
		catch (NoSuchElementException e){ throw new NumberFormatException(); }
	}
	
	/*
	 * creates the midpoint of two points
	 */
	public Point3d(Point3d p, Point3d q) {
		x = (p.x + q.x)/2;
		y = (p.y + q.y)/2;
		z = (p.z + q.z)/2;		
	}
 
	/*
	 * creates the midpoint of three points
	 */
	public Point3d(Point3d p, Point3d q, Point3d r) {
		x = (p.x + q.x + r.x)/3;
		y = (p.y + q.y + r.y)/3;
		z = (p.z + q.z + r.z)/3;		
	}
 
	// GET METHODS
	
	/*
	 * returns the i-th coordinate
	 */
	public double getCoord(int i) { if (i == 0) return x; else return (i==1)? y : z; }
	
	public Vector3d vectorTo(Point3d p){ return new Vector3d(p.x-x, p.y-y, p.z-z); }
	
	/*
	 * returns TRUE if three points are on the same line
	 */
	public static boolean colinear(Point3d p0, Point3d p1, Point3d p2) {
		Vector3d v1v0 = new Vector3d(p1, p0);
		Vector3d v1v2 = new Vector3d(p1, p2);
		return Vector3d.crossProduct(v1v0,v1v2).isZeroVector();
	}
	
	/*
	 * returns true if four specified points are in the same plane
	 */
	public static boolean coplanar(Point3d p0, Point3d p1, Point3d p2, Point3d p3) {
		double ax = p0.x; double ay = p0.y; double az = p0.z;
		double bx = p1.x; double by = p1.y; double bz = p1.z;
		double cx = p2.x; double cy = p2.y; double cz = p2.z;
		double dx = p3.x; double dy = p3.y; double dz = p3.z;
		return   -az*by*cx + ay*bz*cx + az*bx*cy - ax*bz*cy
			     -ay*bx*cz + ax*by*cz + az*by*dx - ay*bz*dx
			     -az*cy*dx + bz*cy*dx + ay*cz*dx - by*cz*dx
			     -az*bx*dy + ax*bz*dy + az*cx*dy - bz*cx*dy
			     -ax*cz*dy + bx*cz*dy + ay*bx*dz - ax*by*dz
			     -ay*cx*dz + by*cx*dz + ax*cy*dz - bx*cy*dz == 0.0; 
	}
	
	/*
	 * returns TRUE if the half-plane with normal vector nv and point p0 is beind p
	 */
	public static boolean isBehind(Point3d p, Point3d p0, Vector3d nv) {
		Vector3d v = new Vector3d(p, p0);
		return Vector3d.dotProduct(nv, v) < 0.0;
	}
	
	/*
	 * returns TRUE if the half-plane through p0, p1, p2 (forming a counterclockwise triangle) is behind p
	 */
	public static boolean isBehind(Point3d p, Point3d p0, Point3d p1, Point3d p2) {
		return Vector3d.dotProduct(Point3d.getNormal(p0, p1, p2), new Vector3d(p0,p)) < 0.0;
	}
	
	/*
	 * returns TRUE if the point p is inside the tetrehedron p0,p1,p2,p3.
	 */
	public static boolean isInside(Point3d p, Point3d p0, Point3d p1, Point3d p2, Point3d p3) {
		return isBehind(p,p1,p3,p2) && isBehind(p,p0,p2,p3) && isBehind(p,p0,p3,p1) && isBehind(p,p0,p1,p2);
		
	}
	
	/*
	 * returns TRUE if the specified point p is inside cone with the specified apex
	 */
	public static boolean isInCone(Point3d p0, Point3d[] face, Point3d p) {
		if (Point3d.isBehind(p0, face[0], face[2], face[1])) {
			Point3d temp = face[1];
			face[1]= face[2];
			face[2] = temp;
		}
//		System.out.println("p0 = " + p0.toString(3));
//		System.out.println("Face: " + face[0] + " " + face[1] + " " + face[2]);
		for (int i = 0; i < 3 ; i++) {
//			System.out.println("p = " + p.toString(3));
			if (!Point3d.isBehind(p, face[i], p0, face[(i+1)%3])) return false;
		}
		return true;
	}

	
	/*
	 * returns vector orthogonal and to the triangle formed by p0, p1, p2 
	 */
	public static Vector3d getNormal(Point3d p0, Point3d p1, Point3d p2) {
		return Vector3d.crossProduct(new Vector3d(p0, p1), new Vector3d(p0, p2));
	}
	

	
	
	/**
	 * creates the equilateral point of a and b in the plane through c
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */
	public static Point3d getEquilateralPoint(Point3d a, Point3d b, Point3d c) {
		Point3d e = a.clone();
		Vector3d normal = Vector3d.crossProduct(new Vector3d(b,a), new Vector3d(b,c));
		normal.makeUnitVector();
		e.rotation(normal, -Math.PI/3,b);	
		return e;
	}
	
	public static Point3d getSteinerPoint(Point3d a, Point3d b, Point3d c) {
		// if the angle at a is 120 or more, return a. Similarly for b and c
		Vector3d ab = new Vector3d(a,b);
		Vector3d ba = new Vector3d(b,a);
		Vector3d bc = new Vector3d(b,c);
		Vector3d cb = new Vector3d(c,b);
		Vector3d ca = new Vector3d(c,a);
		Vector3d ac = new Vector3d(a,c);

		if (!ab.isSteinerAngle(ac)) return null;
		if (!ba.isSteinerAngle(bc)) return null;
		if (!ca.isSteinerAngle(cb)) return null;
		Point3d eab = Point3d.getEquilateralPoint(a, b, c);
		Sphere3d sphere = new Sphere3d(new Circle3d(a,b,eab));
		Segment3d sgm =  sphere.getIntersection(new Line3d(eab, c));
		if (sgm.a.getSquaredDistance(eab) > sgm.b.getSquaredDistance(eab)) return sgm.a; else return sgm.b;
	}
	
	public static Point3d getSteinerPoint(Point3d a, Point3d b, Point3d c, J3DScene j3ds) {
		// if the angle at a is 120 or more, return a. Similarly for b and c
		Vector3d ab = new Vector3d(a,b);
		Vector3d ba = new Vector3d(b,a);
		Vector3d bc = new Vector3d(b,c);
		Vector3d cb = new Vector3d(c,b);
		Vector3d ca = new Vector3d(c,a);
		Vector3d ac = new Vector3d(a,c);

		if (!ab.isSteinerAngle(ac)) return a;
		if (!ba.isSteinerAngle(bc)) return b;
		if (!ca.isSteinerAngle(cb)) return c;
		Point3d eab = Point3d.getEquilateralPoint(a, b, c);
		j3ds.addShape(new Sphere3d(eab, 0.06f), Color.GREEN);

		Sphere3d sphere = new Sphere3d(new Circle3d(a,b,eab));
		Segment3d sgm =  sphere.getIntersection(new Line3d(eab, c));
		if (sgm.a.getSquaredDistance(eab) > sgm.b.getSquaredDistance(eab)) return sgm.a; else return sgm.b;
	}

	
	/*
	 * translates this point by (x,y,z)
	 */
	public void translate(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
	}
	public void translate(Vector3d v) { translate(v.x, v.y, v.z); }
	public void translate(Point3d  p) { translate(p.x, p.y, p.z); }
	public Point3d addIn(Point3d p) { translate(p); return this;}
	public Point3d addIn(Vector3d p) { translate(p); return this; }
	public Point3d add(Vector3d p) { return new Point3d(x+p.x, y+p.y, z+p.z); }
	public Point3d subtractIn(Point3d p) { x-=p.x; y-=p.y; z-=p.z; return this;	}
	public Point3d subtractIn(Vector3d p) { x-=p.x; y-=p.y; z-=p.z; return this;	}
	public Point3d subtract(Vector3d p) {return new Point3d(x-p.x,y-p.y,z-p.z);	}
	public Point3d subtract(Point3d p) {return new Point3d(x-p.x,y-p.y,z-p.z);	}

	public Point3d subIn(Vector3d v) { x-=v.x; y-=v.y; z-=v.z; return this;	}
	public Point3d sub(Vector3d v) { return new Point3d(x-v.x, y-v.y, z-v.z); }
	public void scale(double s){ x*=s;y*=s;z*=s;} 
	/*
	 * reflects this point through origo
	 */
	public void reflectionOrigo() { scale(-1.0); }
	
	/*
	 * rotates this point around x-axis
	 */
	public void rotationX(double alpha) {
		double yNew =  Math.cos(alpha)*y - Math.sin(alpha)*z;
		z = Math.sin(alpha)*y + Math.cos(alpha)*z;
		y = yNew;
	}
	
	/*
	 * rotates the point around y-axis
	 */
	public void rotationY(double alpha) {
		double xNew = Math.cos(alpha)*x + Math.sin(alpha)*z;
		z = -Math.sin(alpha)*x + Math.cos(alpha)*z;
		x = xNew;
	}
	
	/*
	 * rotates the point around z-axis
	 */
	public void rotationZ(double alpha) {
		double xNew =  Math.cos(alpha)*x - Math.sin(alpha)*y;
		y = Math.sin(alpha)*x + Math.cos(alpha)*y;
		x = xNew;
	}
	
	/*
	 * rotates (clockwise) the point around the line through the  origo with the direction unit vector v.
	 * For counterclockwise rotation change signs within parentheses in non-diagonal terms.
	 */
	public void rotation(Vector3d v, double alpha) {
		double c = Math.cos(alpha);
		double d = 1.0-c;
		double s = Math.sin(alpha);
		double vxyd = v.x*v.y*d, vxzd = v.x*v.z*d, vyzd = v.y*v.z*d;
		double vxs = v.x*s, vys = v.y*s, vzs = v.z*s; 
		double xNew = (v.x*v.x*d+c)*x + (vxyd-vzs)*y    + (vxzd+vys)*z;
		double yNew = (vxyd+vzs)*x    + (v.y*v.y*d+c)*y + (vyzd-vxs)*z;
		z =           (vxzd-vys)*x    + (vyzd+vxs)*y    + (v.z*v.z*d+c)*z;
		x = xNew;
		y = yNew;
	}
	
	public void rotation(Vector3d v, double alpha, Point3d p) {
		this.translate(-p.x, -p.y, -p.z);
		rotation(v, alpha);
		this.translate(p);
	}
	
	/*
	 * scales this point by the factor f
	 */
//	public void scale(double f) { x = x*f; y = y*f; z = z*f; }
	
	// DISTANCE METHODS
	
	/*
	 * returns squared distance of this point to point q.
	 */
	public double getSquaredDistance(Point3d q) {
		double dx = x-q.x;
		double dy = y-q.y;
		double dz = z-q.z;
		return dx*dx+dy*dy+dz*dz;
	}
	
	/*
	 * returns squared distance of this point to origo.
	 */
	public double getSquaredDistance() { return x*x + y*y + z*z; }
	
	/*
	 * returns squared distance between points p and q.
	 */
	public static double getSquaredDistance(Point3d p, Point3d q) {
		return Math.pow(p.x-q.x,2) + Math.pow(p.y-q.y,2) + Math.pow(p.z-q.z,2);
	}
	
	
	/*
	 * returns  distance of this point to origo.
	 */
	public double getDistance() { return Math.sqrt(x*x + y*y + z*z); }
	
//  this is wrong!!!! use getDistance
//	public double distance(Point3d p) { return Math.sqrt(x*p.x + y*p.y + z*p.z); }
//  this is wrong !!! use getSquaredDistance
//	public double distanceSquared(Point3d p) { return (x*p.x + y*p.y + z*p.z); }
	
	/*
	 * returns distance of this point to point q
	 */
	public double getDistance(Point3d q) { return Math.sqrt(getSquaredDistance(q)); }
	
	/*
	 * returns distance between points p and q.
	 */
	public static double getDistance(Point3d p, Point3d q) { return Math.sqrt(getSquaredDistance(p,q)); }
	
	/*
	 * creates a bisecting plane between points p and q
	 */
	public static Plane3d getBisectingPlane(Point3d p, Point3d q) {
		if (!p.sameCoordinates(q)) return new Plane3d(new Vector3d(p,q), new Point3d(p,q)); 
		return null;
	}
	
	// ANGLE METHODS
	
	/**
	 * Returns angle at p2
	 */
	public static double getAngle(Point3d p1, Point3d p2, Point3d p3) {
		return Vector3d.getAngle(new Vector3d(p1,p2), new Vector3d(p2,p3));
	}
	
	/*
	 * returns cosinus of the angle between p1 and p3 at p2
	 */
	public static double getCosAngle(Point3d p1, Point3d p2, Point3d p3) { 
		Vector3d u = new Vector3d(p2, p1);
		Vector3d v = new Vector3d(p2, p3);
		return Vector3d.dotProduct(u,v)/(u.getLength()*v.getLength()); 
	}

	/*
	 * returns TRUE if the angle between p1 and p3 at p2 is less than 120 degrees
	 */
	public static boolean isSteinerAngle(Point3d p1, Point3d p2, Point3d p3) {
		return Vector3d.getCosAngle(new Vector3d(p2,p1), new Vector3d(p2,p3)) > -0.5;
	}
	
	/**
	 * Returns dihedral angle defined by 4 non-colinear points p1, p2, p3, p4.
	 */
	public static double getDihedralAngle(Point3d p1, Point3d p2, Point3d p3, Point3d p4) {
		return Vector3d.getDihedralAngle(new Vector3d(p1,p2), new Vector3d(p2,p3), new Vector3d(p3,p4));

	}
	
	// COMPARISON METHODS
	
	/**
	 * Returns true if this point dominates point q
	 */
	public boolean dominates(Point3d q) { 
		
		if (x > q.x) return true;
		else {
			if (x < q.x) return false;
			else {
				if (y > q.y) return true;
				else {
					if (y < q.y) return false; else return z > q.z;
				}
			}
		}
	}

	/**
	 * Returns true if this point dominates point q (i=0,1,2 is the most important coordinate,
	 * j=0,1,2 is the second most important coordinate and k=0,1,2 is the least important coordinate).
	 */
	public boolean dominates(Point3d q, int i, int j, int k) { 
		double[] pc = new double[3];
		double[] qc = new double[3];
		pc[i] = x;   pc[j] = y;   pc[k] = z;
		qc[i] = q.x; qc[j] = q.y; qc[k] = q.z;
		if (pc[0] > qc[0]) return true;
		else {
			if (pc[0] < qc[0]) return false;
			else {
				if (pc[1] > qc[1]) return true;
				else {
					if (pc[1] < qc[1]) return false; else return pc[2] > qc[2];
				}
			}
		}
	}

	public double dotProduct(Vector3d v) { return x*v.x + y*v.y + z*v.z; }
	
	/**
	 * Returns true iff this point and point p are overlapping
	 */
	public boolean sameCoordinates(Point3d p) { return x == p.x || y == p.y || z == p.z; }


	// TRANSFORMATION METHODS
	public Point3d clone(){ return new Point3d(x,y,z); }
	public Vector3d toVector() { return new Vector3d(x, y, z); }
	
	public double[] toDoubleArray() { 
		double[] a = {x, y, z}; 
		return a;
	}

	
	public String toString() { 
		return String.format("[%f,%f,%f]", x,y,z); 
	}
	
	public String toString(int dec) {
//		return "(" + Functions.toString(x,dec) + " , " + Functions.toString(y,dec) + " , " + Functions.toString(z,dec) + ")";
		return String.format("[%."+dec+"f,%."+dec+"f,%."+dec+"f]",x, y,z); 	
	}	
	
	public void toConsole() { System.out.println(toString()); }
	public void toConsole(int dec) { System.out.println(toString(dec)); }
	
	public static void main(String[] args) {
		Point3d p = new Point3d(0,1,0);
		Vector3d v= new Vector3d(0,0,1);
		p.rotation(v, Math.PI/2);
		System.out.println(p.toString());
	}

	public double get(int d) {
		switch(d){
		case 0: return x;
		case 1: return y;
		case 2: return z;
		}
		throw new Error("Trying to get invalid dimension");
	}
	public double getX(){ return x; }
	public double getY(){ return y; }
	public double getZ(){ return z; }
	
	public void set(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
		
	public void set(Point3d p){
		this.x = p.x;
		this.y = p.y;
		this.z = p.z;
	}
	public void setX(double s){ this.x = s;	}
	public void setY(double s){ this.y = s;	}
	public void setZ(double s){ this.z = s;	}
}



