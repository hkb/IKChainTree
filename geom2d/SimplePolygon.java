package geom2d;

public class SimplePolygon  extends Polygon {
	
	/*
	 * returns true if point p is inside the simple polygon
	 */
	public boolean isInside(Point2d p) {
		int count = 0;
		Point2d pi = (Point2d)get(0);
		Point2d pj;
		Segment2d sgm;
		int size = getSize();
		for (int i = 0; i < size; i++) {
			int j = (i == size-1)? 0 : i+1;
			pj = (Point2d)get(j);
			sgm = new Segment2d(pi, pj);
			if (sgm.isLeftYCovered(p)) count++;
			pi = pj;
		}
		return (count % 2 == 1);
	}
}

