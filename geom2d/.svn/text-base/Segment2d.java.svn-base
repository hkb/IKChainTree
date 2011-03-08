package geom2d;
import java.awt.*;

public class Segment2d {
	protected Point2d a;
	protected Point2d b;
	
	/*
	 * creates a segment between points a and b.
	 */
	public Segment2d(Point2d a, Point2d b) {
		this.a = a;
		this.b = b;
	}
	
	/*
	 * creates a segment between a and the end of vector v starting at a. 
	 */
	public Segment2d(Point2d a, Vector2d v) {
		this.a = a;
		b = a;
		b.translate(v);
	}
	
	/*
	 * creates a copy of specified segment
	 */
	public Segment2d(Segment2d segment) {
		a = segment.a;
		b = segment.b;
	}
	
	public Segment2d(double ax, double ay, double bx, double by) {
		a = new Point2d(ax, ay);
		b = new Point2d(bx, by);
	}
	public void setA(Point2d a) { this.a = a; }
	public void setB(Point2d b) { this.b = b; }
	
	@Override
	public String toString() { return "[" + a.toString() + b.toString() + "]"; }
	
	public double getLength() { return Math.sqrt(getSquaredLength()); }
	
	/*
	 * returns square distance this segment and segment s
	 */
	public double getSquaredDistance(Segment2d s) {
		if (intersects(s)) return 0.0; 
		else {
			double d = s.a.getSquaredDistance(this);
			double t = s.b.getSquaredDistance(this);
			if (t < d) d = t;
			t = a.getSquaredDistance(s);
			if (t < d) d = t;
			t = b.getSquaredDistance(s);
			if (t < d) d = t;
			return d;
		}
	}
	
	public double getSquaredLength() { 
		double bax = b.x - a.x;
		double bay = b.y - a.y;
		return bax*bax + bay*bay;
	}
	
	/*
	 * returns TRUE if point p is in the horizontal slab defined by the end-points of 
	 * the segment.
	 */
	public boolean isYCovered(Point2d p) { 
		return (((a.y <= p.y) && (p.y <= b.y)) || ((b.y <= p.y) && (p.y <= a.y))); }
				
	/*
	 * returns TRUE if point p is in the horizontal slab defined by the end-points of
	 * the segment and to the left of the undirected segment.
	 */
	public boolean isLeftYCovered(Point2d p) {
		if (isYCovered(p)) {
			if (a.isBelow(p)) return Point2d.rightTurn(a, b, p);
			else return Point2d.leftTurn(a, b, p);
		}
	else return false;
	}
	
	/*
	 * returns TRUE if point p is in the horizontal slab defined by the end-points of
	 * the segment and to the left of th undirected segment.
	 */
	public boolean isRightYCovered(Point2d p) {
		if (isYCovered(p)) {
			if (a.isBelow(p)) return Point2d.leftTurn(a, b, p);
			else return Point2d.rightTurn(a, b, p);
		}
	else return false;
	}
	
	/*
	 * returns TRUE if this segment properly intersects segment s.
	 */
	public boolean intersects(Segment2d s) {
		return (((Point2d.leftTurn(a,   b,   s.a) && Point2d.leftTurn(b,   a,   s.b)) || 
				 (Point2d.leftTurn(a,   b,   s.b) && Point2d.leftTurn(b,   a,   s.a))) &&
				((Point2d.leftTurn(s.a, s.b, a)   && Point2d.leftTurn(s.b, s.a, b)) || 
				 (Point2d.leftTurn(s.a, s.b, b)   && Point2d.leftTurn(s.b, s.a, a))));
	}
	
	
	public void draw(Graphics g) {
		g.drawLine((int)a.x, (int)a.y, (int)b.x, (int)b.y);		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}

