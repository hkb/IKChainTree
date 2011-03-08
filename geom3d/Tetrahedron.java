package geom3d;

import java.awt.Color;
import dataStructures.Set;
import j3dScene.J3DScene;

public class Tetrahedron implements Volume3d{
	protected Point3d points[] = new Point3d[4];
	
	public Tetrahedron(Point3d a, Point3d b, Point3d c, Point3d d) {
		if (Point3d.coplanar(a, b, c, d))
				System.out.println("Creation of tetrahedron impossible, points are coplanar.");
		else {
			if (Point3d.isBehind(a, b, c, d)) {
				points[0] = a;
				points[1] = c;
				points[2] = b;
				points[3] = d;

			}
			else {
				points[0] = a;
				points[1] = b;
				points[2] = c;
				points[3] = d;
			}
		}
	}
	
	public Tetrahedron(PointSet3d points) {
		if (points.getSize() < 4) System.out.println("Not enough points to create a tetrahedron");
		else {
			if (Point3d.coplanar(points.get(0), points.get(1), points.get(2), points.get(3)))
				System.out.println("Creation of tetrahedron impossible, points are coplanar.");
			else for (int i = 0; i < 4; i++) { 
				this.points[i] = points.get(i);
				this.points[i].scale(10);
			}
		}
	}
	
	/*
	 * returns the i-th point of the tetrahedron
	 */
	public Point3d getPoint(int i) { return points[i]; }
	
	/*
	 * returns true if a point is a corner of the tetrahedron
	 */
	public boolean hasPoint(Point3d p) {
		int i = 0;
		while ((i < 4) && (points[i] != p)) i++;
		return i != 4;
	}
	
	/*
	 * creates a segment between vertices i and j
	 */
	public Segment3d getSegment(int i, int j) { return new Segment3d(points[i], points[j]);	}
	
	/*
	 * returns true if a segment is an edge of the tetrahedron
	 */
	public boolean hasSegment(Segment3d sgm) {
		return hasPoint(sgm.a) && hasPoint(sgm.b);
	}
	
	/*
	 * creates a segment opposite to the segment between vertices i and j
	 */
	public Segment3d getOppositeSegment(int i, int j) {
		int k = 0;
		while ((k == i) || (k == j)) k++;
		int l = k+1;
		while ((l == i) || (l == j)) l++;
		return getSegment(k,l);
	}
	
	/* 
	 * creates a segment opposite to a given segment
	 */
	public Segment3d getOppositeSegment(Segment3d sgm) {
		Point3d a = sgm.a;
		Point3d b = sgm.b;
		int i = 0;
		while (points[i].equals(a) || points[i].equals(b)) i++;
//		System.out.println("i = " + i);
		int j = i+1;
		while (points[j].equals(a) || points[j].equals(b)) j++;
//		System.out.println("j = " + j);
		return getSegment(i,j);
	}

	/*
	 * creates a plane perpendicular to the segment through vertices i and j and containing the midpoint of the opposite segment 
	 */
	public Plane3d getMongePlane(int i, int j) {
		Point3d midPoint = getOppositeSegment(getSegment(i,j)).getMidPoint();
		return new Plane3d(new Vector3d(points[i], points[j]), midPoint);
	}
	
	public Point3d getMongePoint() {
		return Plane3d.getIntersection(getMongePlane(0,1), getMongePlane(0,2), getMongePlane(1,3));
	}
	
	/*
	 * translates the tetrahedron by vector v
	 */
	public Sphere3d getCircumSphere() { return new Sphere3d(this); }
	
	public void translate(Vector3d v) { for (int i = 0; i < 4; i++) this.points[i].translate(v); }
	
	/*
	 * translates the tetrahedron so that its 0 vertex end in the origo
	 */
	public void translateToOrigin() { translateToOrigin(0); }
	
	/*
	 * translates the tetrahedron so that its i-th vertex ends in the origo.
	 */
	public void translateToOrigin(int i) { translate(new Vector3d(-points[i].x, -points[i].y, -points[i].z)); }
	
	/*
	 * returns TRUE if the specified point p is inside tetrahedron
	 */
	public boolean isInside(Point3d p) {
		Point3d[] facePoints = new Point3d[4];
		facePoints = points;
		for (int i = 0; i < 4 ; i++) {
			facePoints = points;
			facePoints[i] = facePoints[4];
			if (Point3d.isBehind(points[i], facePoints[0], facePoints[1], facePoints[2])) {
				if (!Point3d.isBehind(p,facePoints[0], facePoints[1], facePoints[2])) return false;
			}
			else {
				if (!Point3d.isBehind(p, facePoints[1], facePoints[0], facePoints[2])) return false;
			}
		}
		return true;
	}
	
	/*
	 * returns TRUE if the specified point p is inside cone with the specified apex
	 */
	public boolean isInCone(int apex, Point3d p) {
		Point3d[] facePoints = new Point3d[4];
		facePoints = points;
		for (int i = 0; i < 4 ; i++) {
			if (i != apex) {
				facePoints = points;
				facePoints[i] = facePoints[4];
				if (Point3d.isBehind(points[i], facePoints[0], facePoints[1], facePoints[2])) {
					if (!Point3d.isBehind(p,facePoints[0], facePoints[1], facePoints[2])) return false;
				}
				else {
					if (!Point3d.isBehind(p, facePoints[1], facePoints[0], facePoints[2])) return false;
				}
			}
		}
		return true;
	}
	
	/*
	 * returns the volume of the tetrahedron, see http://en.wikipedia.org/wiki/Tetrahedron#Volume
	 */
	public double volume() {
		Point3d p3 = points[3];
		return Math.abs(Vector3d.dotProduct(new Vector3d(p3,points[0]), 
				                                Vector3d.crossProduct(new Vector3d(p3,points[1]), 
				                                		              new Vector3d(p3,points[2]))))/6.0;
	}
	/**
	 * Returns the volume of the tetrahedron
	 */
	public double Volume() {
		Point3d p0 = points[0];
		Point3d p1 = points[1];
		Point3d p2 = points[2];
		Point3d p3 = points[3];
		double ax = p0.x; double ay = p0.y; double az = p0.z;
		double bx = p1.x; double by = p1.y; double bz = p1.z;
		double cx = p2.x; double cy = p2.y; double cz = p2.z;
		double dx = p3.x; double dy = p3.y; double dz = p3.z;
		return   -az*by*cx + ay*bz*cx + az*bx*cy - ax*bz*cy
			     -ay*bx*cz + ax*by*cz + az*by*dx - ay*bz*dx
			     -az*cy*dx + bz*cy*dx + ay*cz*dx - by*cz*dx
			     -az*bx*dy + ax*bz*dy + az*cx*dy - bz*cx*dy
			     -ax*cz*dy + bx*cz*dy + ay*bx*dz - ax*by*dz
			     -ay*cx*dz + by*cx*dz + ax*cy*dz - bx*cy*dz; 
	}

	/**
	 * returns the centroid of a tetrahedron
	 */
	public Point3d getCentroid() {
		Point3d center = points[0].clone();
		center.addIn(points[1]).addIn(points[2]).addIn(points[3]).scale(0.25);
		return center;
	}
	public Point3d getCenter() { return getCentroid(); }
	
	public Set<Segment3d> getMST() {
		PointSet3d pointset = new PointSet3d();
		for (int i = 0; i < 4; i++) pointset.insert(points[i]);
		return pointset.getMST();
	}

	
	/*
	 * returns subset of segments that are edges of the tetrahedron
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
	
	public Set<Segment3d> getSMT(J3DScene j3ds, boolean testing)
	{
		Point3d p0 = points[0];
		Point3d p1 = points[1];
		Point3d p2 = points[2];
		Point3d p3 = points[3];
		
		Set<Segment3d> bestTree = getMST();
		double bestTreeLength = 0.0;
		for (int i = 0; i < bestTree.getSize(); i++) bestTreeLength += ((Segment3d)bestTree.get(i)).getLength();
		double mstLength = bestTreeLength;
		if (testing) System.out.println("Length of the MST is " + mstLength);
		

		
		Plane3d pl01Start = new Plane3d(p0, p1, p2);
		Plane3d pl01End   = new Plane3d(p0, p1, p3);
		Point3d e01 = Point3d.getEquilateralPoint(p0, p1, p2);
		if (pl01Start.below(p3) == 1) { 
			e01 = Point3d.getEquilateralPoint(p0, p1, p3);
			Plane3d plTmp = pl01Start;
			pl01Start = pl01End;
			pl01End = plTmp;
		}
		Plane3d pl02Start = new Plane3d(p0, p2, p1);
		Plane3d pl02End   = new Plane3d(p0, p2, p3);
		Point3d e02 = Point3d.getEquilateralPoint(p0, p2, p1);
		if (pl02Start.below(p3) == 1) { 
			e02 = Point3d.getEquilateralPoint(p0, p2, p3);
			Plane3d plTmp = pl02Start;
			pl02Start = pl02End;
			pl02End = plTmp;
		}
		
		Plane3d pl03Start = new Plane3d(p0, p3, p1);
		Plane3d pl03End   = new Plane3d(p0, p3, p2);
		Point3d e03Start = Point3d.getEquilateralPoint(p0, p3, p1);
		Point3d e03End   = Point3d.getEquilateralPoint(p0, p3, p2);
		Point3d e03 = e03Start;
		if (pl03Start.below(p2) == 1) { 
			e03 = e03End;
			e03End = e03Start;
			Plane3d plTmp = pl03Start;
			pl03Start = pl03End;
			pl03End = plTmp;
		}
		Point3d mid03 = new Point3d(p0,p3);
		Vector3d v03 = new Vector3d(p0,p3);
		Vector3d v03u = v03.createUnitVector();
		Vector3d v03s = v03.createScaledToLengthVector3d(0.01);
		if (testing) {
			j3ds.addShape(new geom3d.Cylinder3d(mid03, mid03.add(v03s), Math.sqrt(3)*v03.getLength()/2), new Color(0, 0, 100, 50)); 
			j3ds.addShape(new Sphere3d(e03Start, 0.06f), Color.RED);
			j3ds.addShape(new Sphere3d(e03End, 0.06f), Color.CYAN);
		}
		Plane3d pl12Start = new Plane3d(p1, p2, p0);
		Plane3d pl12End   = new Plane3d(p1, p2, p3);
		Point3d e12Start = Point3d.getEquilateralPoint(p1, p2, p0);
		Point3d e12End   = Point3d.getEquilateralPoint(p1, p2, p3);
		Point3d e12 = e12Start;
		if (pl12Start.below(p3) == 1) { 
			e12 = e12End;
			e12End = e12Start;
			Plane3d plTmp = pl12Start;
			pl12Start = pl12End;
			pl12End = plTmp;
		}
		Point3d mid12 = new Point3d(p1,p2);
		Vector3d v12 = new Vector3d(p1,p2);
//		Vector3d v12u = v12.createUnitVector();
		Vector3d v12s = v12.createScaledToLengthVector3d(0.01);
		if (testing) j3ds.addShape(new geom3d.Cylinder3d(mid12, mid12.add(v12s), Math.sqrt(3)*v12.getLength()/2), new Color(0, 0, 100, 50)); 
		if (testing) j3ds.addShape(new Sphere3d(e12, 0.06f), Color.RED);
		if (testing) j3ds.addShape(new Sphere3d(e12End, 0.06f), Color.CYAN);
		if (testing) e12.toConsole(3);
		if (testing) e12End.toConsole(3);

		
		Vector3d v01 = new Vector3d(p0, p1);
		v01.makeUnitVector();
		Vector3d v02 = new Vector3d(p0, p2);
		v02.makeUnitVector();
		double dist;
		Point3d s01, s02, s03, s12, s13, s23, pk;
		
		Point3d s012 = Point3d.getSteinerPoint(p0, p1, p2);
		if (s012 != null) {
			dist = p0.getDistance(s012) + p1.getDistance(s012) + p2.getDistance(s012);
			pk = null;
			for (int i = 0; i < 3; i++) if (dist + points[i].getDistance(p3) < bestTreeLength) pk = points[i];
			if (pk != null) {
				dist = dist + pk.getDistance(p3);
				if (dist < bestTreeLength) {
					bestTreeLength = dist;
					bestTree.clear();
					bestTree.insert(new Segment3d(p0, s012));
					bestTree.insert(new Segment3d(p1, s012));
					bestTree.insert(new Segment3d(p2, s012));
					bestTree.insert(new Segment3d(p3, pk));
				}
			}
		}
		Point3d s013 = Point3d.getSteinerPoint(p0, p1, p3);
		if (s013 != null) {
			dist = p0.getDistance(s013) + p1.getDistance(s013) + p3.getDistance(s013);
			pk = null;
			if (dist + p0.getDistance(p2) < bestTreeLength) pk = p0;
			if (dist + p1.getDistance(p2) < bestTreeLength) pk = p1;
			if (dist + p3.getDistance(p2) < bestTreeLength) pk = p3;
			if (pk != null) {
				dist = dist + pk.getDistance(p2);
				if (dist < bestTreeLength) {
					bestTreeLength = dist;
					bestTree.clear();
					bestTree.insert(new Segment3d(p0, s013));
					bestTree.insert(new Segment3d(p1, s013));
					bestTree.insert(new Segment3d(p3, s013));
					bestTree.insert(new Segment3d(p2, pk));

				}
			}
		}
		Point3d s023 = Point3d.getSteinerPoint(p0, p2, p3);
		if (s023 != null) {
			dist = p0.getDistance(s023) + p2.getDistance(s023) + p3.getDistance(s023);
			pk = null;
			if (dist + p0.getDistance(p1) < bestTreeLength) pk = p0;
			if (dist + p2.getDistance(p1) < bestTreeLength) pk = p2;
			if (dist + p3.getDistance(p1) < bestTreeLength) pk = p3;
			if (pk != null) {
				dist = dist + pk.getDistance(p1);
				if ( dist < bestTreeLength) {
					bestTreeLength = dist;
					bestTree.clear();
					bestTree.insert(new Segment3d(p0, s023));
					bestTree.insert(new Segment3d(p2, s023));
					bestTree.insert(new Segment3d(p3, s023));
					bestTree.insert(new Segment3d(p1, pk));
				}
			}
		}
		Point3d s123 = Point3d.getSteinerPoint(p1, p2, p3);
		if (s123 != null) {
			dist = p1.getDistance(s123) + p2.getDistance(s123) + p3.getDistance(s123);
			pk = null;
			for (int i = 1; i < 4; i++) if (dist + points[i].getDistance(p0) < bestTreeLength) pk = points[i];
			if (pk != null) {
				dist = dist + pk.getDistance(p0);
				if (dist < bestTreeLength) {
					bestTreeLength = dist;
					bestTree.clear();
					bestTree.insert(new Segment3d(p1, s123));
					bestTree.insert(new Segment3d(p2, s123));
					bestTree.insert(new Segment3d(p3, s123));
					bestTree.insert(new Segment3d(p0, pk));
				}
			}
		}
		
		
		int nrIter = 143;
		int i1 = 0;
		while ((i1 == 0) || ((pl01Start.below(e01) == 1) && (pl01End.above(e01) == 1))) { 

//			j3ds.addShape(new Sphere3d(e01, 0.06f), Color.BLACK);
//			j3ds.addShape(new geom3d.Cylinder3d(e01, tetra.points[0], 0.01), Color.BLACK);
//			j3ds.addShape(new geom3d.Cylinder3d(e01, tetra.points[1], 0.01), Color.BLACK);
			s23 = Point3d.getSteinerPoint(p2, p3, e01);
			if ((s23 != null) && (pl01Start.above(s23) == 1) && (pl01End.below(s23) == 1)) { 

				s01 = Point3d.getSteinerPoint(p0, p1, s23);
				if (s01 != null) {
					dist = p0.getDistance(s01) + p1.getDistance(s01) + p2.getDistance(s23) + p3.getDistance(s23) + s01.getDistance(s23);
					if (testing) {
						j3ds.addShape(new geom3d.Cylinder3d(s01, p0, 0.01), Color.YELLOW);
						j3ds.addShape(new geom3d.Cylinder3d(s01, p1, 0.01), Color.YELLOW);
						j3ds.addShape(new geom3d.Cylinder3d(s23, p2, 0.01), Color.YELLOW);
						j3ds.addShape(new geom3d.Cylinder3d(s23, p3, 0.01), Color.YELLOW);
						j3ds.addShape(new geom3d.Cylinder3d(s01, s23, 0.01),Color.YELLOW);
					}
					if (bestTreeLength > dist) {
						if (testing) System.out.println(i1 + ". New best tree of type 1. Length = " + dist + " Ratio = " + (dist/mstLength));
						bestTreeLength = dist;
						bestTree.clear();
						bestTree.insert(new Segment3d(p0, s01));
						bestTree.insert(new Segment3d(p1, s01));
						bestTree.insert(new Segment3d(p2, s23));
						bestTree.insert(new Segment3d(p3, s23));
						bestTree.insert(new Segment3d(s01,s23));
					}
					else 
						if (testing) System.out.println(i1 + ". Tree of type 1. Length = " + dist + " Ratio = " + (dist/mstLength));
				}
			}
			i1++;
			e01.rotation(v01,Math.PI/((nrIter+1)/2),p0); 

		}
		int i2 = 0;
		while ((i2 == 0) || ((pl02Start.below(e02) == 1) && (pl02End.above(e02) == 1))) { 
//				j3ds.addShape(new Sphere3d(e02, 0.06f), Color.BLACK);
//				j3ds.addShape(new geom3d.Cylinder3d(e02, tetra.points[0], 0.01), Color.BLACK);
//				j3ds.addShape(new geom3d.Cylinder3d(e02, tetra.points[2], 0.01), Color.BLACK);
			s13 = Point3d.getSteinerPoint(p1, p3, e02);
			if ((s13 != null) && (pl02Start.above(s13) == 1) && (pl02End.below(s13) == 1)) { 
				s02 = Point3d.getSteinerPoint(p0, p2, s13);
				if (s02 != null) {
					dist = p0.getDistance(s02) + p2.getDistance(s02) + p1.getDistance(s13) + p3.getDistance(s13) + s02.getDistance(s13);
					if (testing) {
						j3ds.addShape(new geom3d.Cylinder3d(s02, p0, 0.01), Color.RED);
						j3ds.addShape(new geom3d.Cylinder3d(s02, p2, 0.01), Color.RED);
						j3ds.addShape(new geom3d.Cylinder3d(s13, p1, 0.01), Color.RED);
						j3ds.addShape(new geom3d.Cylinder3d(s13, p3, 0.01), Color.RED);
						j3ds.addShape(new geom3d.Cylinder3d(s02, s13,0.01), Color.RED);
					}
					if (bestTreeLength > dist) {
						if (testing) System.out.println(i2 + ". New best tree of type 2. Length = " + dist + " Ratio = " + (dist/mstLength));
						bestTreeLength = dist;
						bestTree.clear();
						bestTree.insert(new Segment3d(p0, s02));
						bestTree.insert(new Segment3d(p1, s13));
						bestTree.insert(new Segment3d(p2, s02));
						bestTree.insert(new Segment3d(p3, s13));
						bestTree.insert(new Segment3d(s02,s13));
					}
					else if (testing) System.out.println(i2 + ". Tree of type 2. Length = " + dist + " Ratio = " + (dist/mstLength));
				}
			}
			i2++;
			e02.rotation(v02,Math.PI/((nrIter+1)/2),p0);

		}

		
//		System.out.println("dihedral angle between start and end planes "+ Plane3d.dihedralAngle(pl03Start, pl03End));
//		Plane3d pl;

		int i3 = 0;
		while ((i3 == 0) || ((pl03Start.below(e03) == 1) && (pl03End.above(e03) == 1))) { 
		
//				j3ds.addShape(new Sphere3d(e03, 0.06f), Color.BLACK);
//				j3ds.addShape(new geom3d.Cylinder3d(e03, p0, 0.01), Color.BLACK);
//				j3ds.addShape(new geom3d.Cylinder3d(e03, p3, 0.01), Color.BLACK);
			
//			pl = new Plane3d(tetra.points[0], e03, tetra.points[3]);
//			System.out.println("dihedral angle between start and equilateral planes " + Plane3d.dihedralAngle(pl03Start, pl));

			s12 = Point3d.getSteinerPoint(p1, p2, e03);	
			if ((s12 != null) && (pl03Start.above(s12) == 1) && (pl03End.below(s12) == 1)) { 	
//				e12 = Point3d.getEquilateralPoint(p1, p2, s12);
//				if (i3 == 0) j3ds.addShape(new Sphere3d(s12, 0.06f), Color.BLACK);
				s03 = Point3d.getSteinerPoint(p0, p3, s12);
//				if (i3 == 0) j3ds.addShape(new Sphere3d(s03, 0.06f), Color.BLACK);
				if (s03 != null) {
					dist = p0.getDistance(s03) + p3.getDistance(s03) + p1.getDistance(s12) + p2.getDistance(s12) + s03.getDistance(s12);
					if (testing) {
						j3ds.addShape(new geom3d.Cylinder3d(s12, p1, 0.01), Color.GREEN);
						j3ds.addShape(new geom3d.Cylinder3d(s12, p2, 0.01), Color.GREEN);				
						j3ds.addShape(new geom3d.Cylinder3d(s03, p0, 0.01), Color.GREEN);
						j3ds.addShape(new geom3d.Cylinder3d(s03, p3, 0.01), Color.GREEN);
						j3ds.addShape(new geom3d.Cylinder3d(s03, s12,0.01), Color.GREEN);
					}
//					if (i3 == 0) j3ds.addShape(new geom3d.Cylinder3d(e03, e12,0.01), Color.GREEN);
//					e12.toConsole(3);
					if (bestTreeLength > dist) {
						if (testing) System.out.println(i3 + ". New best tree of type 3. Length = " + dist + " Ratio = " + (dist/mstLength));
						bestTreeLength = dist;
						bestTree.clear();
						bestTree.insert(new Segment3d(p0, s03));
						bestTree.insert(new Segment3d(p1, s12));
						bestTree.insert(new Segment3d(p2, s12));
						bestTree.insert(new Segment3d(p3, s03));
						bestTree.insert(new Segment3d(s12,s03));
					}
					else if (testing) System.out.println(i3 + ". Tree of type 3. Length = " + dist + " Ratio = " + (dist/mstLength));
				}
			}
			i3++;
			e03.rotation(v03u,Math.PI/((nrIter+1)/2),p0);
		}
		return bestTree;
	}

	public boolean overlaps(Volume3d vol) {
		// TODO Auto-generated method stub
		return false;
	}
	public Tetrahedron clone(){ throw new Error("Not implemented"); }
	
	public void toConsole(int dec) {
		for (int i = 0; i < 4; i++) points[i].toConsole(dec);
	}
	
	public void draw(J3DScene j3ds) {
		for (int i = 0; i < 3; i++)
			for (int j = i+1; j < 4; j++)
				j3ds.addShape(new geom3d.Cylinder3d(points[i], points[j], 0.06), Color.BLUE);
		for (int i = 0; i < 4; i++)  {
			j3ds.addShape(new Sphere3d(points[i], 0.12f), Color.RED);
			Point3d textPoint = new Point3d(points[i].x + 0.1, points[i].y + 0.1, points[i].z + 0.1);
			j3ds.addText((new Integer(i)).toString(), textPoint, 0.4);
		}
	}
	
	public void drawCorners(J3DScene j3ds) {
		for (int i = 0; i < 4; i++)  {
			j3ds.addShape(new Sphere3d(points[i], 0.12f), Color.RED);
			Point3d textPoint = new Point3d(points[i].x + 0.1, points[i].y + 0.1, points[i].z + 0.1);
			j3ds.addText((new Integer(i)).toString(), textPoint, 0.4);
		}
	}

	
	public void drawSegments(Set<Segment3d> set, double width, Color clr, J3DScene scene) {
		Segment3d sgm;
		for (int i = 0; i < set.getSize(); i++) {
			sgm = (Segment3d)set.get(i);
			scene.addShape(new geom3d.Cylinder3d(sgm.a, sgm.b, width), clr);
		}
	}
	
	public static void main(String[] args) {
		J3DScene j3ds = J3DScene.createJ3DSceneInFrame();
		j3ds.setBackgroundColor(Color.WHITE);

		PointSet3d points = new PointSet3d(4,2);
		Tetrahedron tetra = new Tetrahedron(points);
//		Tetrahedron tetra = new Tetrahedron(new Point3d(0,0,0), new Point3d(4,0,0), new Point3d(0,4,0), new Point3d(2,2,4));
		tetra.toConsole(3);
		tetra.draw(j3ds);
		j3ds.centerCamera();
		j3ds.autoZoom();

		Set<Segment3d> bestTree = tetra.getSMT(j3ds, false);
		double bestTreeLength = 0.0;
		Segment3d sgm;
		for (int i = 0; i < bestTree.getSize(); i++) {
			sgm = (Segment3d)bestTree.get(i);
			bestTreeLength += sgm.getLength();
			j3ds.addShape(new geom3d.Cylinder3d(sgm.a, sgm.b, 0.1), Color.YELLOW);	
		}
		System.out.println("Length of the SMT is " + bestTreeLength);
		
	}
}
