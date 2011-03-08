package geom3d;
import j3dScene.J3DScene;

import java.awt.Color;

import gui3d.GeoViewer;

import com.sun.j3d.utils.applet.MainFrame;

import misc.Constants;

public class Line3d {
	protected Point3d p;
	protected Vector3d dir;
	
	/*
	 * creates a line through the origo with direction vector v
	 */
	public Line3d(Vector3d v) {
		p = new Point3d(0, 0, 0);
		dir = v;
		dir.makeUnitVector();
	}
	
	/*
	 * creates a line through the point p with direction vector v
	 */
	public Line3d(Point3d p, Vector3d v) {
		this.p = p;
		dir = v;
		dir.makeUnitVector();	
	}
	
	/**
	 * creates a line through two given points
	 * @param s
	 */
	public Line3d(Point3d a, Point3d b) {
		this.p = a;
		dir = new Vector3d(a,b);
		dir.makeUnitVector();
	}
	
	/*
	 * creates a line through the segment s
	 */
	public Line3d(Segment3d s) {
		p = s.a;
		dir = new Vector3d(s);
		dir.makeUnitVector();
	}
	
	public Point3d getP()   { return p; }
	
	public Vector3d getDir() { return dir; }

	/*
	 * returns othogonal projection of a specified point q
	 */
	public Point3d orthogonalProjection(Point3d q) {
		Vector3d pq = new Vector3d(q, p);
		double t = Vector3d.dotProduct(dir, pq);
		return new Point3d(p.x + t*dir.x, p.y + t*dir.y, p.z + t*dir.z);
	}
	
	/*
	 * creates a segment through all orthogonol projections of a point set.
	 */
	public Segment3d orthogonalProjection(PointSet3d points) {
		return orthogonalProjection(points, 0, points.getSize());
	}
	
	public Segment3d orthogonalProjection(PointSet3d points, int i, int j) {
		double  t;
		Vector3d  pq = new Vector3d();
		Point3d q = points.get(i);
		pq.setX(q.x-p.x);  pq.setY(q.y-p.y);   pq.setZ(q.z-p.z);
		double minT = Vector3d.dotProduct(dir, pq);  double maxT = minT;
		for (int k = 1; k < j; k++) {
			q = points.get(k);
			pq.setX(q.x-p.x);  pq.setY(q.y-p.y);   pq.setZ(q.z-p.z);
			t = Vector3d.dotProduct(dir, pq);
			if (t < minT) minT = t;
			if (t > maxT) maxT = t;
		}
		Point3d a = new Point3d(p.x + minT*dir.x, p.y + minT*dir.y, p.z + minT*dir.z);
		Point3d b = new Point3d(p.x + maxT*dir.x, p.y + maxT*dir.y, p.z + maxT*dir.z);
		return new Segment3d(a, b); 
	}
	
	/** 
	 * @author Rasmus
	 */
	public double orthogonalProjectionParameter(Point3d q) {
		Vector3d pq = new Vector3d(q, p);
		double t = Vector3d.dotProduct(dir, pq);
		return t;
	}

	
	/*
	 * returns squared distance to a point
	 */
	public double getSquaredDistance(Point3d q) { return q.getSquaredDistance(orthogonalProjection(q)); }
	
	public double getMaxDistance(PointSet3d points) {
		int n = points.getSize();
		if (n == 0) return Double.MAX_VALUE;
		double dist2;
		double maxDist2 = getSquaredDistance(points.get(0));
		for (int i = 1; i< n; i++) {
			dist2 = getSquaredDistance(points.get(0));
			if (dist2 > maxDist2) maxDist2 = dist2;
		}
		return Math.sqrt(maxDist2);
	}
	
	/*
	 * returns squared distance to another line
	 */
	public double getSquaredDistance(Line3d l) {
		Vector3d vdir = l.getDir();
		Point3d q = l.getP();
		Vector3d w = new Vector3d(p, q);
		double a = Vector3d.dotProduct(dir, dir);
		double b = Vector3d.dotProduct(dir, vdir);
		double c = Vector3d.dotProduct(vdir, vdir);
		double d = Vector3d.dotProduct(dir, w);
		double e = Vector3d.dotProduct(vdir, w);
		double sc, tc;
		double denom = a*c -b*b;
		if (denom < Constants.epsilon) { sc = 0.0; tc = d/b; }
		else {
			sc = (b*e - c*d)/denom;
			tc = (a*e - b*d)/denom;
		}
		
		Vector3d uc = new Vector3d(sc*dir.x, sc*dir.y, sc*dir.z);
		Vector3d vc = new Vector3d(tc*vdir.x, tc*vdir.y, tc*vdir.z);
		Point3d pc = new Point3d(p.x + uc.x, p.y + uc.y, p.z + uc.z);
		Point3d qc = new Point3d(q.x + vc.x, q.y + vc.y, q.z + vc.z);		
		return Point3d.getSquaredDistance(pc, qc);
	}

	public Point3d getPoint(double t) {
		return new Point3d(p.x + t*dir.x, p.y + t*dir.y, p.z + t*dir.z);
	}

	public double projectOnto(Point3d p) {
		double nom = p.vectorTo(this.p).dot(this.getDir());
		double den = this.getDir().lengthSquared();
		return -nom/den;
	}
	

	public void toConsole(){
		System.out.println(String.format("Line3d[p:%s,dir:%s]", p, dir));
	}
	
	public void draw(J3DScene scene) {
		Point3d a = new Point3d(p.x - 100*dir.x, p.y - 100*dir.y, p.z - 100*dir.z);
		Point3d b = new Point3d(p.x + 100*dir.x, p.y + 100*dir.y, p.z + 100*dir.z);
		scene.addShape(new geom3d.Cylinder3d(a, b, 0.05), Color.BLACK);
	}

}

