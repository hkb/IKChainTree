package geom3d;

import matrix.Matrix3x3;
import j3dScene.J3DScene;

import java.awt.Color;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.Random;

import geom2d.*;
import dataStructures.Set;
import dataStructures.Heap;

import sorting.SortToolSegment3d;

public class PointSet3d extends Set<Point3d> {

	public PointSet3d() { super(); }
	
	/*
	 * creates a point set consisting of n uniformly distributed random points in the unit cube
	 */
	public PointSet3d(int n) {
		super(n);
		Random randGen = new Random();
		for (int i = 0; i < n; i++) insert(new Point3d(randGen));
	}
	
	/*
	 * creates a point set consisting of n uniformly distributed random points in the unit sphere with given seed
	 */
	public PointSet3d(int n, int seed) {
		super(n);
		Random randGen = new Random(seed);
		for (int i = 0; i < n; i++) insert(new Point3d(randGen));
	}
	
	/* 
	 * creates a point set on the surface of a unit sphere
	 */
	public PointSet3d(int n, Sphere3d sphere) {
		super(n);
		double r = sphere.getRadius();
		Random randGen = new Random();
		double theta, phi, cosTheta;
		for (int i = 0; i < n; i++) { 
			theta = 180*randGen.nextDouble() - 90;
			cosTheta = Math.cos(theta);
			phi = 360*randGen.nextDouble();
			insert(new Point3d(r*cosTheta*Math.cos(phi), r*cosTheta*Math.sin(phi), r*Math.sin(theta)));
		}
	}
	
	public PointSet3d(int n, Circle3d circle) {
		super(n);
		Vector3d nv = circle.getNormalVector();
		Vector3d uv, vv;
		if (Math.abs(nv.x) >= Math.abs(nv.y)) {
			double invLength = 1/Math.sqrt(nv.x*nv.x + nv.y*nv.y);
			uv = new Vector3d(-nv.z*invLength, 0, nv.x*invLength);
			vv = new Vector3d(nv.y*uv.z, nv.z*uv.x - nv.x*uv.z, -nv.y*uv.x);
		}
		else {
			double invLength = 1/Math.sqrt(nv.y*nv.y + nv.z*nv.z);
			uv = new Vector3d(0, nv.z*invLength, -nv.y*invLength);
			vv = new Vector3d(nv.y*uv.z - nv.z*uv.y, -nv.x*uv.z, nv.x*uv.y);
		}
		double r = circle.getRadius();
		Point3d c = circle.getCenter();
		double cos, sin, alpha;
		Random randGen = new Random();
		for (int i = 0; i < n; i++) { 
			alpha = 360*randGen.nextDouble();
			sin = Math.sin(alpha); cos = Math.cos(alpha);
			insert(new Point3d(c.x +r*(cos*uv.x + sin*vv.x), c.y + r*(cos*uv.y + sin*vv.y), c.z+ r*(cos*uv.z + sin*vv.z)));
		}


		
	}
	
	/*
	 * creates a point set from an array
	 */
	public PointSet3d(Point3d [] elements) { super(elements); }
	
	/*
	 * creates a point set that is a copy of itself
	 */
	public PointSet3d(PointSet3d set) { super(set); }
	
	/*
	 * reads x-, y- and z-coordinates of points from a specified input file (one point pr. line) 
	 */
	public PointSet3d(String inputFileName) {
		this(0);
		double x, y, z;
		try {
			FileReader rd = new FileReader(inputFileName);
			System.out.flush();
			
			StreamTokenizer st = new StreamTokenizer(rd);
			st.parseNumbers();

			int token = st.nextToken();
			while (token != StreamTokenizer.TT_EOF) {
				switch (token) {
				case StreamTokenizer.TT_NUMBER:
					x = st.nval;
					token = st.nextToken();
					y = st.nval;
					token = st.nextToken();
					z = st.nval;
					insert(new Point3d(x,y,z));
					System.out.println(x + " " + y + " " + z);
					break;
				}
				token = st.nextToken();
			}
			rd.close();
		} 
		catch (IOException e) {
		}
	}
	
	public Point3d get(int i) { return (Point3d)super.get(i); }
	
	/*
	 * returns i-th coordinate of k-th point
	 */
	public double getCoord(int k, int i) { return get(k).getCoord(i); }
	
	/*
	 * return the centroid point of the set of points
	 */
	public Point3d getCentroid() {
		double x = 0.0, y = 0.0, z = 0.0;
		Point3d p;
		for (int i = 0; i < n; i++) { 
			p = get(i);
			x += p.x; y += p.y; z += p.z;
		}
		return  new Point3d(x/n, y/n, z/n);
	}
	
	/*
	 * points are translated by -p
	 */
	public void translate(Point3d p) {
		for (int i = 0; i < n; i++) (get(i)).translate(-p.x, -p.y, -p.z);
	}
	/*
	 * returns variance of the point set
	 */
	public double getVariance() {
		Point3d c = getCentroid();
		double sum = 0.0;
		for (int i = 0; i < n; i++) sum += c.getSquaredDistance(get(i));
		return sum/n;
	}
	
	/* 9HOps */
	public Matrix3x3 getCoVariance() {
		Matrix3x3 cov = new Matrix3x3();
		double cv, ci, cj;
		Point3d c = getCentroid();//3HOps
		Point3d p;
		for (int i = 0; i < 3; i++) {
			ci = c.getCoord(i);
			for (int j = i; j < 3; j++) {
				cj = c.getCoord(j);
				cv = 0.0;
				for (int k = 0; k < n; k++) {
					p = get(k);
					cv += (p.getCoord(i) - ci) * (p.getCoord(j) - cj);
				}
				cov.set(i,j,cv/n); 
				cov.set(j,i,cov.get(i,j));
			}
		}//6HOps
		return cov;
	}
	
	/*
	 * returns standard deviation of the point set
	 */
	public double getStandardDeviation() { return Math.sqrt(getVariance()); }
	
	/*
	 * returns the most extreme point in the direction specified by the unit vector
	 */
	public int getExtreme(Vector3d unitNormal) {
		double dot = get(0).dotProduct(unitNormal);
		double dotI;
		int indx = 0;
		for (int i = 1; i < n; i++) {
			dotI = get(i).dotProduct(unitNormal);
			if (dotI > dot) { dot = dotI; indx = i; }
		}
		return indx;	
	}
	
	/*
	 * returns the most extreme distance from the plane through origo, orthogonal to the specified unit vector
	 */
	public double getExtremeDist(Vector3d unitNormal) {
		double dot = get(0).dotProduct(unitNormal);
		double dotI;
		for (int i = 1; i < n; i++) {
			dotI = get(i).dotProduct(unitNormal);
			if (dotI > dot) dot = dotI;
		}
		return dot;	
	}

	
	public int getExtremeRightIndex() {
		int indx = 0;
		Point3d q = get(0);
		for (int i = 1; i < n; i++) {
			Point3d p = get(i);
			if (p.dominates(q)) { indx = i; q = p; }
		}
		return indx;
	}
	
	/*
	 * returns the index of the x-extreme point (in case of ties the index of the bottom leftmost point is returned).
	 * if HIGH = TRUE, the index of the point with highest x-coordinate is returned. Otherwise the index of the 
	 * point with lowest x-coordinate is returned.
	 */
	public int getExtremeIndex(int ix, int iy, int iz, boolean high) {
		int indx = 0;
		Point3d q = get(0);
		Point3d p;
		for (int i = 1; i < n; i++) {
			p = (Point3d)get(i);
			if (high) { 
				if (p.dominates(q, ix, iy, iz)) { indx = i; q = p; }
			}
			else {
				if (q.dominates(p, ix, iy, iz)) { indx = i; q = p; }
			}
		}
		return indx;
	}
	
	/*
	 * returns the rightmost point (in case of ties the top rightmost point is returned).
	 */
	public Point3d getExtremeRight() { return get(getExtremeIndex(0,1,2,true)); }

	/*
	 * returns the leftmost point (in case of ties the bottom lefttmost point is returned).
	 */
	public Point3d getExtremeLeft() { return get(getExtremeIndex(0,1,2,false)); }

	/*
	 * returns the highest point (in case of ties the left topmost point is returned).
	 */
	public Point3d getExtremeTop() { return (Point3d)get(getExtremeIndex(1,0,2,true)); }

	/*
	 * returns the lowest point (in case of ties the bottom rightmost point is returned).
	 */
	public Point3d getExtremeBottom() { return (Point3d)get(getExtremeIndex(1,0,2,false)); }

	/*
	 * returns the frontmost point (in case of ties the front rightmost point is returned).
	 */
	public Point3d getExtremeFront() { return (Point3d)get(getExtremeIndex(1,2,0,true)); }
	
	/*
	 * returns the deepest point (in case of ties the left topmost point is returned).
	 */
	public Point3d getExtremeBack() { return (Point3d)get(getExtremeIndex(1,2,0,false)); }
	
	/*
	 * returns the length of the diameter of the point set - trivial O(n^2) algorithm
	 */
	public Segment3d getDiameter() {
		Point3d p;
		Point3d q;
		Segment3d seg = new Segment3d();
		double pq; 
		double best = 0.0;
		for (int i = 0; i < n-1; i++) {
			p = (Point3d)get(i);
			for (int j = i+1; j < n; j++) {
				q = get(j);
				pq = p.getSquaredDistance(q);
				if (pq > best) { best = pq; seg.setA(p); seg.setB(q); }
			}
		}
		return seg;
	}
	
	public Point3d getClosest(Point3d q) {
		Point3d p = get(0);
		double sqDist = p.getSquaredDistance(q);
		double dist;
		for (int i = 1; i < getSize(); i++) {
			dist = get(i).getSquaredDistance(q);
			if (dist < sqDist) {
				sqDist = dist;
				p = get(i);
			}
		}
		return p;
	}
	
	public Point3d getClosest(double x, double y, double z) { return getClosest(new Point3d(x,y,z)); }
	
	public int getClosestIndex(Point3d q) {
		int j = 0;
		double sqDist = get(0).getSquaredDistance(q);
		double dist;
		for (int i = 1; i < getSize(); i++) {
			dist = get(i).getSquaredDistance(q);
			if (dist < sqDist) {
				sqDist = dist;
				j = i;
			}
		}
		return j;
	}

	/*
	 * returns a pair of points of the set making the sharpest edge at point q
	 */
	public PointSet3d getSharpestWedge(Point3d q) {
		int sz = getSize();
		if (sz < 2) return null;
		Point3d p, r;
		PointSet3d pair = new PointSet3d(2);
		double cos, cosMax = -2;
		for (int i = 0; i < sz-1; i++) {
			p = get(i);
			for (int j = i+1; j < sz; j++) {
				r = get(j);
				cos = Point3d.getCosAngle(p, q, r);
				if (cos > cosMax) {
					cosMax = cos;
					pair.set(0, p);
					pair.set(1, r);
				}
			}
		}
		return pair;
	}
	
	/*
	 * returns a segment seg between two points in the set such that diam/sqrt(3) <= seg
	 * Requires O(n) time.
	 */
	public Segment3d diameterSqrt3Approx() {
		Segment3d seg0 = new Segment3d(getExtremeLeft(), getExtremeRight());
		Segment3d seg1 = new Segment3d(getExtremeBottom(), getExtremeTop());
		Segment3d seg2 = new Segment3d(getExtremeFront(), getExtremeBack());
		double l0 = seg0.getSquaredLength();
		double l1 = seg1.getSquaredLength();
		double l2 = seg2.getSquaredLength();
		if (l0 < l1) return (l2 < l0)? seg2 : seg0;  else return (l2 < l1)?  seg2 :seg1; 
	}

	
	/*
	 * returns a convex hull segment through 2 points of the set
	 */
	public int[] getConvexHullSegment() {
		int[] indx = { getExtremeIndex(0,1,2,false), 0 };
		if (indx[0] == 0) indx[1] = 1; 
		Point3d[] p = { get(indx[0]), get(indx[1]) };
		Point3d q;
		for (int i = 0; i < n; i++) {
			if ((i != indx[0]) && (i != indx[1])) {
				q = get(i);				
				if ((q.x < p[1].x) || ((q.x == p[1].x) && 
						(p[0].getSquaredDistance(p[1]) < p[0].getSquaredDistance(q)))) {
					indx[1] = i;
					p[1] = q;
				}
			}
		}
		return indx;
	}
	
	/*
	 * return a triangle on the convex hull
	 */
	public int[] getConvexHullTriangle() {
		int[] indx2 = getConvexHullSegment();
		int[] indx = { indx2[0], indx2[1], 0 }; 
		Point3d p0 = get(indx[0]);
		Point3d p1 = get(indx[1]);
		if (indx[0] == 0) {
			indx[2] = 1;
			if (indx[1] == 1) indx[2] = 2;
		}
		else {
			if (indx[1] == 0) {
				indx[2] = 1;
				if (indx[0] == 1) indx[2] = 2;
			}
		}
		Point3d p2 = get(0);
		Point3d p;
		for (int i = 0; i < n; i++) {
			p = get(i);
			if ((indx[0] != i) && (indx[1] != i) && Point3d.isBehind(p0, p1, p, p2)) {
				indx[2] = i;
				p2 = p;
			}
		}
		return indx;
	}
	
	public int[] getExposedTetrahedron(int indx0, int indx1, int indx2) {
		int[] indx = { indx0, indx1, indx2, 0 }; 
		Point3d[] pts = { get(indx0), get(indx1), get(indx2), null} ;
		Point3d p;
		int i = 0; 
		boolean found = false;
		while ((i < n) && !found) {
			p = get(i);
			found = (i != indx0) && (i != indx1) && (i != indx2) && 
			         Point3d.isBehind(pts[0], pts[1], pts[2], p); 
			if (found) { 
				indx[3] = i;
				pts[3] = p;
			}
			i++;
		}
		if (!found) return null; 
		else {
			Sphere3d sphere = new Sphere3d(pts[0], pts[1], pts[2], pts[3]);   System.out.print("i = " + (i-1) +". " + pts[3].toString(3)); sphere.toConsole(".  Initial sphere: ", 3);
			while ( i < n) {
				p = get(i);                                          System.out.print("i = " + i +". " + p.toString(3)); sphere.toConsole(".  Current sphere: ", 3);
				if ((pts[0] != p) && (pts[1] != p) && (pts[2] != p)) {
					if (Point3d.isBehind(pts[0], pts[1], pts[2], p)) {
						if (sphere.isInside(p)) {
							indx[3] = i;
							pts[3] = p;
							sphere = new Sphere3d(pts[0], pts[1], pts[2], pts[3]);    System.out.println("Next sphere: " + sphere.toString(3) + ", 4. point: " + pts[3].toString(3));
						} else System.out.println("Not inside.");
					} else System.out.println("Wrong side.");
				} else System.out.println("Vertex taken.");
				i++;
			}
			return indx;
		}
	}
	
	/*
	 * returns a tetrahedron unless all points are in the same plane.
	 */
	public Tetrahedron getTetrahedron() {
		Point3d a = get(0), b = get(1), c = get(2);
		int i = 2;
		while (Point3d.colinear(a, b, c)) c = get(++i);
		if (i < n) {
			Point3d d = get(++i);
			while (Point3d.coplanar(a, b, c, d)) d = get(++i);
			if (i < n) return new Tetrahedron(a, b, c, d);
		}
		return null;
	}
	
	public PointSet2d projectXYPlan() {
		int size = getSize();
		Point3d p;
		PointSet2d points = new PointSet2d(); 
		for (int i = 0; i < size; i++) {
			p = get(i);
			points.insert(new Point2d(p.x, p.y));
		}
		return points;
	}
	
	public PointSet2d projectXZPlan() {
		int size = getSize();
		Point3d p;
		PointSet2d points = new PointSet2d(); 
		for (int i = 0; i < size; i++) {
			p = get(i);
			points.insert(new Point2d(p.x, p.z));
		}
		return points;
	}
	
	public PointSet2d projectYZPlan() {
		int size = getSize();
		Point3d p;
		PointSet2d points = new PointSet2d(); 
		for (int i = 0; i < size; i++) {
			p = get(i);
			points.insert(new Point2d(p.y, p.z));
		}
		return points;
	}
	
	/*
	 * computes minimum spanning tree (as a set of segments) of the point set
	 * Points are placed on a min-heap with the distance to the tree as key.
	 * This is Prim's algorithm
	 */
	public Set<Segment3d> getMST() {
		Set<Segment3d> mstSegments = new Set<Segment3d>();
		int n = getSize();
		
		Heap heap = new Heap(n-1, new SortToolSegment3d());
		Segment3d pq, pr, qr; 
		Point3d p = (Point3d)get(0), q, r;
		Double qrDist;
		for (int i = 1; i < n; i++) {
			q = (Point3d)get(i);
			heap.insert(new Segment3d(p, q));
		}
		int k;
		while (!heap.isEmpty()) {
			// the segment to be added to the MST is deleted from the heap
			pq = (Segment3d)heap.extract();
			mstSegments.insert(pq);
			// q is now in the MST
			q = pq.b;
			//iterate through all heap items
			k = heap.getSize();
			for (int i = 0; i < k; i++) {
				pr = (Segment3d)heap.getItem(i);
				r = pr.b;
				qrDist = q.getSquaredDistance(r);
				if (qrDist < pr.getSquaredLength()) {
					qr = new Segment3d(q,r);
					heap.setItem(i, qr);
					heap.siftUp(i);
				}
			}
		}
		return mstSegments;
	}
	
	public void toConsole() {
		int size = getSize();
		for (int i = 0; i < size; i++) get(i).toConsole();
	}
	public void toConsole(int dec) {
		int size = getSize();
		for (int i = 0; i < size; i++) get(i).toConsole(dec);
	}
	
	public void draw(J3DScene j3ds, Color clr) { draw(j3ds, 0, n-1, clr); }

	public void draw(J3DScene j3ds, int startIndx, int endIndx, Color clr) {
		for (int i = startIndx; i <= endIndx; i++)  j3ds.addShape(new Sphere3d(get(i), 0.02f), clr);
	}

	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PointSet3d points = new PointSet3d(100);
//		points.toConsole();
//		System.out.println(points.getExtreme(new Vector3d(0,-1,0)));
		Set<Segment3d> mst = points.getMST();
		double mstLength = 0.0;
		for (int i = 0; i < mst.getSize(); i++) mstLength = mstLength + ((Segment3d)mst.get(i)).getLength();
		System.out.println("Length of the MST is " + mstLength);
//		GeoViewer viewer = new GeoViewer(new PointSet3d(100, new Circle3d(new Point3d(0, 0, -3), 1.0, new Vector3d(0,1,1))));
//		new MainFrame(viewer, 1280, 960);

		// TODO Auto-generated method stub

	}
}

