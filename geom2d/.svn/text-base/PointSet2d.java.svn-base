package geom2d;

import dataStructures.Set;
import dataStructures.Stack;
import gui2d.PolygonDrawer;
import java.util.Random;
import java.io.*;

public class PointSet2d extends Set {
	
	public PointSet2d() { this(0); }
	
	/*
	 * creates a point set consisting of n uniformly distributed points in the unit square
	 */
	public PointSet2d(int n) {
		super(n);
		Random randGen = new Random();
		for (int i = 0; i < n; i++) {
			double x = new Double(randGen.nextDouble());
			double y = new Double(randGen.nextDouble());
			insert(new Point2d(x, y));
		}
	}
	
	/*
	 * reads x- and y-coordinates of points from a specified input file (one point pr. line) 
	 */
	public PointSet2d(String inputFileName) {
		this(0);
		double x, y;
		try {
			FileReader rd = new FileReader(inputFileName);
			StreamTokenizer st = new StreamTokenizer(rd);
			st.parseNumbers();
			
			int token = st.nextToken();
			while (token != StreamTokenizer.TT_EOF) {
				switch (token) {
				case StreamTokenizer.TT_NUMBER:
					x = st.nval;
					token = st.nextToken();
					y = st.nval;
					insert(new Point2d(x,y));
					break;
				}
				token = st.nextToken();
			}
			rd.close();
		} 
		catch (IOException e) {
		}
	}
	
	public PointSet2d(PointSet2d points) { super(points); }
	
	public PointSet2d(Point2d [] elements) { super(elements); }
	
	/*
	 * translates and scales points into unit square preserving distance relations
	 */
	public void normalize() {
		double minX = getMinXCoordinate();
		double maxX = getMaxXCoordinate();
		double minY = getMinYCoordinate();
		double maxY = getMaxYCoordinate();
		System.out.println(minX + ", " + minY + ", " + maxX + ", " + maxY);
		
		double mx = 1/Math.max(minX-maxX, minY-maxY);
		int size = getSize();
		for (int i = 0; i < size; i++) {
			((Point2d)get(i)).translate(-maxX, -maxY);
			((Point2d)get(i)).scale(mx);
		}
	}
	
	/*
	 * returns the greatest x-coordinate of the point set.
	 */
	public double getMaxXCoordinate() { return getExtremeRight().x; }
	
	/*
	 * returns the greatest y-coordinate of the point set.
	 */
	public double getMaxYCoordinate() { return getExtremeTop().y; }
	
	/*
	 * returns the smallest x-coordinate of the point set.
	 */
	public double getMinXCoordinate() { return getExtremeLeft().x; }
	
	/*
	 * returns the smallest y-coordinate of the point set.
	 */
	public double getMinYCoordinate() { return getExtremeBottom().y; }
	
	/*
	 * returns the greatest absolute coordinate.
	 */
	public double getAbsMaxCoordinate() {
		double maxAbs = getMaxXCoordinate();
		double z = getMaxYCoordinate();
		if (z > maxAbs) maxAbs = z;
		z = -getMinXCoordinate();
		if (z > maxAbs) maxAbs = z;
		z = -getMinYCoordinate();
		if (z > maxAbs) maxAbs = z;
		return maxAbs;
	}
	
	/*
	 * return the centroid point of the set of points
	 */
	public Point2d getCentroid() {
		Point2d q = new Point2d();
		for (int i = 0; i < n; i++) q.translate((Point2d)get(i));
		q.scale(1/n);
		return  q;
	}

	/*
	 * returns variance of the point set
	 */
	public double getStandardDeviation() { return Math.sqrt(getVariance()); }
	
	/*
	 * returns standard variation of the point set
	 */
	public double getVariance() {
		Point2d c = getCentroid();
		double sum = 0.0;
		for (int i=0; i < n; i++) sum = sum + c.getSquaredDistance((Point2d)get(i));
		return sum/(n-1);
	}

	
	/*
	 * returns the index of the rightmost point (in case of ties the index of the top rightmost point is returned).
	 */
	public int getExtremeIndex(int ix, int iy, boolean up) {
		int indx = 0;
		Point2d q = (Point2d)get(0);
		Point2d p;
		for (int i = 1; i < n; i++) {
			p = (Point2d)get(i);
			if (up) {
				if (q.dominates(p, ix, iy)) { indx = i; q = p; }
			}
			else {
				if (p.dominates(q, ix, iy)) { indx = i; q = p; }
			}
		}
		return indx;
	}
	
	/*
	 * returns the rightmost point (in case of ties the top rightmost point is returned).
	 */
	public Point2d getExtremeRight() { return (Point2d)get(getExtremeIndex(0,1,true)); }

	
	/*
	 * returns the leftmost point (in case of ties the bottom lefttmost point is returned).
	 */
	public Point2d getExtremeLeft() { return (Point2d)get(getExtremeIndex(0,1,false)); }
	
	/*
	 * returns the topmost point (in case of ties the left topmost point is returned).
	 */
	public Point2d getExtremeTop() { return (Point2d)get(getExtremeIndex(1,0,true)); }
	
	/*
	 * returns the bottommost point (in case of ties the bottom rightmost point is returned).
	 */
	public Point2d getExtremeBottom() { return (Point2d)get(getExtremeIndex(1,0,false)); }

	
	/*
	 * returns a triangle unless all points are co-linear.
	 */
	public Triangle2d getTriangle() {
		Point2d a = (Point2d)get(0);
		Point2d b = (Point2d)get(1);
		for (int i = 2; i < n; i++) {
			Point2d c = (Point2d)get(i);
			if (!Point2d.colinear(a, b, c)) return new Triangle2d(a,b,c);
		}
		return null;
	}	
	
	/*
	 * returns next counterclockwise successor of point p on the convex hull 
	 * (used by Jarvis' march)
	 */
	public Point2d getNextCHPoint(Point2d p) {
		Point2d r = ((Point2d)get(0) != p)? (Point2d)get(0) : (Point2d)get(1);
		Point2d x;
		for (int i = 1; i < n; i++) {
			x = (Point2d)get(i);
			if ((x != p) && (x != r) && Point2d.rightTurn(p,r,x)) r = x;
		}
		return r;
	}
	
	/*
	 * Jarvis' march for convex hulls
	 */
	public ConvexPolygon jarvisMarch() {
		ConvexPolygon ch = new ConvexPolygon();
		Point2d p = getExtremeRight();
		ch.insert(p);
		Point2d r = getNextCHPoint(p);
		while (r != p) {
			ch.insert(r);
			r = getNextCHPoint(r);
		} 
		return ch;		
	}
	
	/*
	 * Graham's scan for convex hulls
	 */
	public ConvexPolygon grahamScan() {
		shift(findIndex(getExtremeRight()));
		Stack stack = new Stack();
		stack.push(0);
		int p = 1;
		int next = 2;
		while (p != 0) {
			if (Point2d.leftTurn((Point2d)get((Integer)stack.peek()), (Point2d)get(p), (Point2d)get(next))) {
				stack.push(p);
				p = next++;
				if (next == getSize()) next = 0;
			}
			else p = (Integer)stack.pop();
		}
		ConvexPolygon cp = new ConvexPolygon();
		while (!stack.isEmpty()) cp.insert(get((Integer)stack.pop()));
		return cp;
	}

	
	@Override
	public String toString() {
		String txt ="";
		for (int i = 0; i < n; i++) txt = txt + ((Point2d)get(i)).toString() + "  ";
		return txt;
	}
	
	public void toConsole() { System.out.print(toString()); }
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PointSet2d set = new PointSet2d(15);
/*		Point2d points[] = new Point2d[6];
		points[0] = new Point2d(0.1,0.3);
		points[1] = new Point2d(0.2,0.6);
		points[2] = new Point2d(0.5,0.7);
		points[3] = new Point2d(0.8,0.4);
		points[4] = new Point2d(0.6,0.0);
		points[5] = new Point2d(0.3,0.1);
		PointSet2d set = new PointSet2d(points);
*/
		ConvexPolygon ch = set.jarvisMarch();
		PolygonDrawer d = new PolygonDrawer(ch); 
		d.setSize(520,540);
		d.setVisible(true);

		Rectangle2d rect = ch.getMinRectangle();
		System.out.println("Rectangle " + rect.toString(4));

	}

}
