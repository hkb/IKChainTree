package geom2d;

import misc.*;

public class Vector2d {
	protected double x;
	protected double y;
		
	public Vector2d(double x, double y) { this.x = x; this.y = y; }
	public Vector2d(Point2d p, Point2d q) { x = q.x - p.x; y = q.y - p.y; }
	public Vector2d(Point2d p) { x = p.x; y = p.y; }
		
	public double getX() { return x; }
	public double getY() { return y; }
		
	public double getSquaredLength() { return x*x + y*y; }
		
	public double getLength() { return Math.sqrt(getSquaredLength()); }
	
	public double getSlope() {
		if (x == 0) return 9999.0; else return y/x;
	}

	public void negative() { scale(-1); }
	public Vector2d scale(double a) { x=a*x; y=a*y; return this; }
	public void rotate(double a) {
		double cosA = Math.cos(a);
		double sinA = Math.sin(a);
		double xOld = x;
		x = cosA*x - sinA*y;
		y = sinA*xOld + cosA*y;
	}
	public void rotate90() { 
		double xOld = x;
		x = -y; 
		y = xOld;
	}
	public void makeUnitVector() { scale(1/getLength()); }
	public void addVector(Vector2d v) { x=x+v.x; y=y+v.y; }
	public void subtractVector(Vector2d v) { x=x-v.x; y=y-v.y; }
	
	public Vector2d createNegativeVector2d() { return createScaledVector2d(-1); }
	public Vector2d createScaledVector2d(double a) { return new Vector2d(a*x, a*y); }
	public Vector2d createUnitVector() { return createScaledVector2d(1/getLength()); }
	public Vector2d createRotatedVector(double a) {
		return new Vector2d(Math.cos(a)*x - Math.sin(a)*y, Math.sin(a)*x + Math.cos(a)*y);
	}
	public Vector2d createRotatedVector90() { return new Vector2d(-y,x); }
	public Point2d toPoint() { return new Point2d(x,y); }
	
	public static Vector2d createSum(Vector2d  u, Vector2d v) { return new Vector2d(u.x+v.x, u.y+v.y); }
	public static Vector2d createDiff(Vector2d u, Vector2d v) { return new Vector2d(u.x-v.x, u.y-v.y); }
	
	public static double crossProduct(Vector2d u, Vector2d v) { return u.x*v.y - u.y*v.x; }
	public static double crossProduct(Point2d u, Point2d v) { return u.x*v.y - u.y*v.x; }
	public static double dotProduct(Vector2d u, Vector2d v) { return u.x*v.x + u.y*v.y; }
	public static double dotProduct(Vector2d u, Point2d p)  { return Vector2d.dotProduct(u,new Vector2d(p)); }
	public static double dotProduct(Point2d  p, Vector2d u) { return Vector2d.dotProduct(u, p); }
	public static double dotProduct(Point2d  p, Point2d q)  { return Vector2d.dotProduct(new Vector2d(p), new Vector2d(q)); }
	
	public static boolean leftTurn(Vector2d u, Vector2d v)  { return Vector2d.crossProduct(u, v) >  0.0; }
	public static boolean rightTurn(Vector2d u, Vector2d v) { return Vector2d.crossProduct(u, v) <= 0.0; }

	
	@Override
	public String toString() { return "[" + String.valueOf(x) + "," + String.valueOf(y) + "]"; }
	public String toString(int k) { return "[" + Functions.toString(x,k) + "," + Functions.toString(y,k) + "]"; }
	public void toConsole() { System.out.println(toString());
	
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
	public Vector2d scaleToLength(double l) {
		double l2 = this.getLength();
		return new Vector2d(l*x/l2, l*y/l2);
	}
	public double dot(Vector2d v) {
		return x*v.x + y*v.y;
	}

}

