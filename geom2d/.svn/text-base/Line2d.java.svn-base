
package geom2d;

import misc.Constants;



public class Line2d {
	protected Point2d p;     // point on the line
	protected Vector2d n;    // normal vector of the line, unit length
	
	/*
	 * creates a line through a given point and with a given normal vector
	 */
	public Line2d(Point2d p, Vector2d n) { 
		this.p = p; 
		this.n = n.createUnitVector(); 
	}
	
	/*
	 * creates a line through 2 given points 
	 */
	public Line2d(Point2d p, Point2d q) {
		this.p = p;
		n = new Vector2d(p.y-q.y, q.x-p.x);
		n.makeUnitVector();
	}
	
	/*
	 * creates a line ax + by + c = 0
	 */
	public Line2d(double a, double b, double c) {
		n = new Vector2d(a,b);
		n.makeUnitVector();
		if (b != 0.0) p = new Point2d(0.0,-c/b); else p = new Point2d(-c/a,0.0);
	}

	public Vector2d getDirection() { return new Vector2d(n.y,-n.x); }
	
	public double getSlope() { if (!isVertical()) return n.x/n.y; else return Double.MAX_VALUE;
	}
	
	public boolean isVertical() { return n.y == 0.0; }
	
	public boolean isParallelWith(Line2d l) { return Math.abs(Vector2d.crossProduct(n, l.n)) < Constants.epsilon; }
	
	public static boolean areParallel(Line2d l1, Line2d l2) {
		return Vector2d.crossProduct(l1.n, l2.n) == 0.0;
	}
	
	/*
	 * project point p onto this line
	 */
	public Point2d projectPoint(Point2d q) {
		double t = n.y*(q.x-p.x) - n.x*(q.y-p.y);
		return new Point2d(n.y*t+p.x, p.y-n.x*t);
	}
	
	/*
	 * returns the intersection of two lines using Cramer's rule, see Ericson, Section 3.1.5. 
	 * Runtime exception is thrown if the lines are parallel.
	 * Modified by Pawel on June 23, 2010
	 */
	public static Point2d getIntersection(Line2d l1, Line2d l2) {
		double denom = l1.n.x*l2.n.y - l1.n.y*l2.n.x;
		if (Math.abs(denom) < Constants.epsilon) throw new RuntimeException("Lines are parallel");
		else {
			double e = l1.n.x*l1.p.x + l1.n.y*l1.p.y;
			double f = l2.n.x*l2.p.x + l2.n.y*l2.p.y;
			return new Point2d((e*l2.n.y - f*l1.n.y)/denom, (f*l1.n.x - e*l2.n.x)/denom);
		}
	}
	
	public String toString(String name) {
		return "Line[" + name + ",point:" + p.toString() + ",normal:" + n.toString()+"]";
	}
	@Override
	public String toString() { return toString(""); }
	
	public void toConsole(String name) { System.out.println(toString(name)); }
	public void toConsole() { System.out.println(toString("")); }

	public Point2d getPoint(double d) {
		Vector2d dir = this.getDirection();
		return new Point2d(p.x+d*dir.x, p.y+d*dir.y);
	}

	public double intersectionParameter(Line2d l) {
		Vector2d dir = getDirection();
		Vector2d lDir = l.getDirection();
		double denom = lDir.getY()*dir.getX()-lDir.getX()*dir.getY();
		Vector2d c = l.p.vectorTo(p);
		double s = (lDir.getX()*c.getY()-lDir.getY()*c.getX())/denom;
		return s;
	}
	
}
