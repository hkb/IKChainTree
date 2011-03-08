package geom2d;

public class Triangle2d {
	protected Point2d a;
	protected Point2d b;
	protected Point2d c;

	public Triangle2d(Point2d a, Point2d b, Point2d c) { 
		if (area2() != 0.0) { this.a = a; this.b = b; this.c = c; }
	}
	
	/*
	 * returns twice the signed area of the triangle (positive if counterclockwise, and negative if clockwise).
	 */
	public double area2() { return Point2d.area2(a, b, c); }
	
	/*
	 * return the are of the triangle
	 */
	public double area()  { return Math.abs(area2())/2; }
	
	public Circle2d getCircumscribingCircle() {
		Line2d bisectorAB = Point2d.getBisector(a,b);
		Line2d bisectorAC = Point2d.getBisector(a,c);
		Point2d d = Line2d.getIntersection(bisectorAB, bisectorAC);
		return new Circle2d(d, d.getDistance(a));
	}
}

