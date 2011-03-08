package geom2d;

public class Rectangle2d {
	protected Point2d anchor;
	protected Vector2d baseRight;
	protected Vector2d baseUp;
	
	public Rectangle2d() {}
	
	public Rectangle2d(Point2d anchor, Vector2d baseRight, Vector2d baseUp) {
		this.anchor = anchor;
		this.baseRight = baseRight;
		this.baseUp = baseUp;
	}
	
	/*
	 * creates axis-aligned minimum area rectangle containing given set of points
	 */
	public Rectangle2d(PointSet2d set) {
		anchor = new Point2d(set.getExtremeLeft().x, set.getExtremeBottom().y);
		baseRight = new Vector2d(anchor, new Point2d(set.getExtremeRight().x, anchor.y));
		baseUp    = new Vector2d(anchor, new Point2d(anchor.x, set.getExtremeTop().y));
	}
 
	/*
	 * creates axis-aligned rectangle
	 */
	public Rectangle2d(double xMin, double yMax, double xMax, double yMin) {
		anchor = new Point2d(xMin, yMin);
		baseRight = new Vector2d(anchor, new Point2d(xMax,yMin));
		baseUp    = new Vector2d(anchor, new Point2d(xMin,yMax));
	}

	/*
	 * creates rectangle with vector v as a baseUp vector, and p0, p1, p2, p3 as points 
	 * on four sides
	 */
	public Rectangle2d(Vector2d v, Point2d[] p, int indx) { 
		Line2d l = new Line2d(p[indx], v.createRotatedVector90());
		anchor = l.projectPoint(p[(indx+1)%4]);
		baseRight = new Vector2d(anchor, l.projectPoint(p[(indx+3)%4]));
		l = new Line2d(p[(indx+1)%4],baseRight);
		baseUp = new Vector2d(anchor, l.projectPoint(p[(indx+2)%4]));
	}
	
	public Point2d getLowerRightCorner() { return new Point2d(anchor, baseRight); }
	public Point2d getUpperLeftCorner()  { return new Point2d(anchor, baseUp); }
    public Point2d getUpperRightCorner() { return new Point2d(anchor, Vector2d.createSum(baseUp,baseRight)); }
    
    public double getSquaredArea() { return baseRight.getSquaredLength()*baseUp.getSquaredLength(); }
    public double getArea() { return Math.sqrt(getSquaredArea()); }
    
    // MINIMUM AREA ENCLOSING RECTANGLE (ANY ORIENTATION)
    //
    // side of a minimum-area enclosing rectangle must be collinear with a side of the convex hull
    // the same aplies to the minimum perimeter enclosing rectangle
    // H. Freeman and R. Shapira, "Freeman Determining the Minimum-Area Encasing Rectangle for an Arbitrary Closed Curve", Comm. ACM, 1975, pp.409-413.
    // Godfried T. Toussaint, "Solving geometric problems with the rotating calipers," Proceedings of IEEE MELECON'83, Athens, Greece, May 1983.
    //   get convex hull, delete interior points, let h denote the remaining points on the convex hull
    //   identify "horizontal" antipodal pair and vertical antipodal pair and compute corresponding area (product of the lengths of the two diagonals).
    //   while number of moves is less then h
    //     find next antipodal pair and compute the corresponding area
    
    @Override
	public String toString() {
    	Point2d p1 = new Point2d(anchor, baseUp);
    	Point2d p2 = new Point2d(p1,     baseRight);
    	Point2d p3 = new Point2d(anchor, baseRight);
    	return "[" + anchor.toString() + ", " + p1.toString() + ", " + p2.toString() + ", " + p3.toString() + "]"; 
    }
	public String toString(int k) {
		Point2d p1 = new Point2d(anchor, baseUp);
		Point2d p2 = new Point2d(p1,     baseRight);
		Point2d p3 = new Point2d(anchor, baseRight);
		return "[" + anchor.toString(k) + ", " + p1.toString(k) + ", " + p2.toString(k) + ", " + p3.toString(k) + "]"; 
	}
}

