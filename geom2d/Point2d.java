package geom2d;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import misc.Functions;

public class Point2d {
	protected double x;
	protected double y;
	
	public Point2d() { x = y = 0.0; }
	
	public Point2d(Point2d p) { x = p.x; y = p.y; }
	
	public Point2d(double x, double y) { this.x = x; this.y = y; }
	
	public Point2d(Vector2d v) { x = v.x; y = v.y; }
	
	public Point2d(Point2d p, Vector2d v) { x = p.x + v.x; y = p.y + v.y; }
	
	public Point2d(String s) throws NumberFormatException {
		StringTokenizer st = new StringTokenizer(s,"[,]");
		try{
			st.nextToken(); //get rid of leading v
			x = Double.valueOf(st.nextToken()).doubleValue();
			y = Double.valueOf(st.nextToken()).doubleValue();
		} 
		catch (NoSuchElementException e){ throw new NumberFormatException(); }
	}
	
	public Point2d clone(){
		return new Point2d(x,y);
	}
	
	// GET METHODS
	
	public double getX() { return x; }
	public double getY() { return y; }
	
	/*
	 * creates the midpoint of two points
	 */
	public static Point2d getMidPoint(Point2d p, Point2d q) { return new Point2d((p.x + q.x)/2, (p.y + q.y)/2); }
	
	/*
	 * creates the equilateral point to the right of pq
	 */
	public Point2d getEquilateralPoint(Point2d p, Point2d q) {
		Point2d e = new Point2d(q);
		p.rotation(e, Math.PI/3.0);
		return e;
	}
	public static Point2d createEquilateralPoint(Point2d p, Point2d q) {
		Point2d e = new Point2d(q);
		p.rotation(e, Math.PI/3.0);
		return e;
	}


	
	/*
	 * translates this point by (x,y)
	 */
	public void translate(double x, double y) { this.x += x; this.y += y; }
	public void translate(Vector2d v) { translate(v.x, v.y); }
	public void translate(Point2d  p) { translate(p.x, p.y); }
		
	
	/*
	 * rotates the point around the origo (counterclockwise)
	 */
	public void rotation(double alpha) {
		double cosA = Math.cos(alpha);
		double sinA = Math.sin(alpha);
		double xNew =  cosA*x - sinA*y;
		y = cosA*y + sinA*x;
		x = xNew;
	}
	
	/*
	 * rotates point p around this point by angle
	 */
	public void rotation(Point2d p, double angle) {
		p.translate(-x, -y);
		p.rotation(angle);
		p.translate(x,y);
	}
	
	/*
	 * scales this point by the factor f
	 */
	public void scale(double f) { x = x*f; y = y*f; }
	
	// DISTANCE METHODS
	
	/*
	 * returns squared distance of this point to point q.
	 */
	public double getSquaredDistance(Point2d q) { return Math.pow(x-q.x, 2) + Math.pow(y-q.y, 2); }
	
	/*
	 * returns squared distance between points p and q.
	 */
	public static double getSquaredDistance(Point2d p, Point2d q) { return Math.pow(p.x-q.x,2) + Math.pow(p.y-q.y,2); }
	
	/*
	 * returns distance of this point to point q
	 */
	public double getDistance(Point2d q) {
		double dx = x-q.x;
		double dy = y-q.y;
		return Math.sqrt(dx*dx+dy*dy);
		//return Math.sqrt(getSquaredDistance(q)); 
	}
	
	/*
	 * returns distance between points p and q.
	 */
	public static double getDistance(Point2d p, Point2d q) { return Math.sqrt(getSquaredDistance(p,q)); }
	
	/*
	 * returns squared distance of this point to segment s 
	 */
	public double getSquaredDistance(Segment2d s) {
		double squaredSegmentLength = s.getSquaredLength();
		if (squaredSegmentLength == 0.0) return getSquaredDistance(s.a);
		double sx = s.b.x - s.a.x, sy = s.b.y - s.a.y;
		double t = ((s.a.x - x) * sx + (s.a.y - y) * sy)/squaredSegmentLength;
		if (t < 0) return getSquaredDistance(s.a);
		if (t > 1) return getSquaredDistance(s.b);
		return getSquaredDistance(new Point2d(s.a.x + t*sx, s.a.y + t*sy));	
	}
	
	/*
	 * creates a bisector between points p and q
	 */
	public static Line2d getBisector(Point2d p, Point2d q) {
		if (!p.sameCoordinates(q)) return new Line2d(getMidPoint(p,q), new Vector2d(p,q)); 
		return null;
	}
	
	/*
	 * returns signed double area of the triangle defined by the three points a, b and c (positive if counterclockwise)
	 */
	public static double area2(Point2d a, Point2d b, Point2d c) { return a.x*(b.y-c.y) + a.y*(c.x-b.x) + b.x*c.y - c.x*b.y; }
	
	// COMPARISON METHODS
	
	/*
	 * returns true if points a, b and c make a left turn at b
	 */
	public static boolean leftTurn(Point2d a, Point2d b, Point2d c ) { return area2(a,b,c) > 0.0; }

	/*
	 * returns true if points a, b and c make a right turn at b or are colinear
	 */
	public static boolean rightTurn(Point2d a, Point2d b, Point2d c ) { return area2(a,b,c) < 0.0; }

	/*
	 * returns true if points a, b and c are colinear
	 */
	public static boolean colinear(Point2d a, Point2d b, Point2d c ) { return area2(a,b,c) == 0.0; }

	/*
	 * returns true if this point dominates point q
	 */
	public boolean dominates(Point2d q) { return   x > q.x || ( x == q.x && y > q.y); }
	
	/*
	 * returns true if this point dominates point q (i=0,1 is the most important coordinate,
	 * and j=0,1 is the least important coordinate).
	 */
	public boolean dominates(Point2d q, int i, int j) { 
		double[] pc = new double[2], qc = new double[2];
		pc[i] = x;   pc[j] = y;  
		qc[i] = q.x; qc[j] = q.y;
		if (pc[0] > qc[0]) return true;
		if (pc[0] < qc[0]) return false; else return pc[1] > qc[1];
	}

	/*
	 * returns true if this point yx-dominates point q
	 */
	public boolean yxDominates(Point2d q) { return   y > q.y || ( y == q.y && x > q.x); }
	
	/*
	 * returns true if this point and point p are overlapping
	 */
	public boolean sameCoordinates(Point2d p) { return Math.abs(x-p.x)<0.000001 && Math.abs(y-p.y)<0.000001; }

	public boolean isBelow(Point2d p) { return y < p.y; }

	/*
	 * returns true if the point is inside the circle through specified 3 points
	 */
	public boolean inCircle(Point2d a, Point2d b, Point2d c) {
		double aa = a.x*a.x + a.y*a.y, bb = b.x*b.x + b.y*b.y, cc = c.x*c.x + c.y*c.y, dd = x*x + y*y;
		return a.x*(b.y-y)*cc + b.x*(c.y-a.y)*dd + c.x*(y-b.y)*aa + x*(a.y-c.y)*bb > 0.0;
	}
	
	/*
	 * returns true if the point is inside (counterclockwise) triangle with the specified corners
	 */
	public boolean inTriangle(Point2d a, Point2d b, Point2d c) {
		return (rightTurn(this,a,b) && rightTurn(this, b, c) && rightTurn(this, c, a)) ||
		       (leftTurn(this,a,b) && leftTurn(this, b,c) && leftTurn(this, c, a));
	}
	
	// TRANSFORMATION METHODS
	
	public Vector2d toVector() { return new Vector2d(x, y); }
	@Override
	public String toString() { return "(" + String.valueOf(x) + "," + String.valueOf(y) + ")"; }
	public String toString(int k) { return "(" + Functions.toString(x,k) + "," + Functions.toString(y,k) + ")"; }
	
	public void toConsole() { System.out.println(toString()); }
	public void toConsole(int k) { System.out.println(toString(k)); }
	
	public Vector2d vectorTo(Point2d p) {
		return new Vector2d(p.x-x, p.y-y);
	}

	
	public static void main(String[] args) {
		Point2d p = new Point2d(2.103, 1.195);
		Point2d q = new Point2d(1.52, 0.0);
		Vector2d v = new Vector2d(p,q);
		v.rotate(Functions.toRadians(-122));
		v.scale(1.45/1.33);
		p.translate(v);
	}


	public Line2d bisector(Point2d p) {
		return Point2d.getBisector(this, p);
		
		/*Vector2d dir = this.vectorTo(p);
		double t = dir.x;
		dir.x = -dir.y;
		dir.y = t;
		return new Line2d( 
				this.add(this.vectorTo(p).scale(0.5)),  
				new Vector2d(dir.getX(), dir.getY()) );
		*/
	}

	Point2d add(Vector2d v) {
		return new Point2d(x+v.x, y+v.y);
	}
}

//