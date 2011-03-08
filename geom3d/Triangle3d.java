package geom3d;

import dataStructures.Set;

public class Triangle3d implements Shape3d{
	public Point3d p0, p1, p2;
	
	public Triangle3d(Point3d p0, Point3d p1, Point3d p2){
		this.p0 = p0;
		this.p1 = p1;
		this.p2 = p2;
	}
	
	/*
	 * returns true if a point is a corner of the triangle
	 */
	public boolean hasPoint(Point3d p) { return ((p == p0) || (p == p1) || (p == p2)); }

	/*
	 * returns true if a segment is an edge of the triangle
	 */
	public boolean hasSegment(Segment3d sgm) { return hasPoint(sgm.a) && hasPoint(sgm.b); }

	public Point3d getCenter() {
		Point3d center = p0.clone();
		center.addIn(p1);
		center.addIn(p2);
		center.scale(1/3d);
		return center;
	}
	
	public Vector3d getNormal(){
		return Vector3d.crossProduct(p0.vectorTo(p1), p0.vectorTo(p2)).scaleToLength(1);
	}
	
	public Set<Segment3d> getMST() {
		PointSet3d pointset = new PointSet3d();
		pointset.insert(p0);
		pointset.insert(p1);
		pointset.insert(p2);
		return pointset.getMST();
	}
	
	/*
	 * returns subset of segments that are edges of the triangle
	 */
	public Set<Segment3d> coveringSegments(Set<Segment3d> set) {
		Segment3d sgm;
		Set<Segment3d> cover = new Set<Segment3d>();
		for (int i = 0; i < set.getSize(); i++) {
			sgm = set.get(i);
			if (hasSegment(sgm)) cover.insert(sgm);
		}
		return cover;
	}



}
