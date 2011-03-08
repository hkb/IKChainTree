package geom3d;

public class Sphere3d implements Volume3d{
	public Point3d center;
	public double radius;
	
	public Sphere3d() {
		radius = -1;
		center = null;
	}
	
	public Sphere3d(Point3d p) {
		center = p;
		radius = 0;
	}
	
	public Sphere3d(Point3d center, double radius) {
		this.center = center;
		this.radius = radius;
	}
	
	/**
	 * creates a sphere with the specified circle as equator
	 * @param c
	 */
	public Sphere3d(Circle3d c) {
		center = c.center;
		radius = c.radius;
	}
	
	/*
	 * creates smallest sphere through two given points
	 */
	public Sphere3d(Point3d p1, Point3d p2) {
		center = new Point3d(p1, p2);//3HOps
		radius = p1.getDistance(p2)/2;//5HOps
	}
	
	/*
	 * creates smallest sphere through three given points
	 */
	public Sphere3d(Point3d p1, Point3d p2, Point3d p3) {
		Circle3d circle = new Circle3d(p1, p2, p3);
		center = circle.center;
		radius = circle.radius;
	}

	
	/*
	 * creates a sphere circumscribing a specified tetrahedron
	 */
	public Sphere3d(Tetrahedron tetra) {
		computeSphere3d(tetra.getPoint(0), tetra.getPoint(1), tetra.getPoint(2), tetra.getPoint(3));
	}
	
	/*
	 * creates a sphere through array of 4 points
	 */
	public Sphere3d(Point3d[] points) {
		computeSphere3d(points[0], points[1], points[2], points[3]);
	}
	
	/* 
	 * creates a sphere through the first 4 points of the set
	 */
	public Sphere3d(PointSet3d points) {
		computeSphere3d((Point3d)points.get(0), (Point3d)points.get(1), (Point3d)points.get(2), (Point3d)points.get(3));
	}
	
	/*
	 * creates a sphere through 4 given points
	 */
	public Sphere3d(Point3d p0, Point3d p1, Point3d p2, Point3d p3) { computeSphere3d(p0, p1, p2, p3); }
	
	public static Sphere3d createSphere(PointSet3d points, int i, int j) {
		PointSet3d pointSubset = new PointSet3d();
		for (int k = i; k <= j; k++) pointSubset.insert(points.get(k));
//		pointSubset.toConsole(4);
		return getMinSphere(pointSubset);
	}

	
	
	/*
	 * returns the smallest sphere containing given set of points.
	 * Randomized, expected O(n) time algorithm
	 */
	public static Sphere3d getMinSphere(PointSet3d points) {
		points.randomPermutation();
		return getMinSphere(points, points.getSize(), new PointSet3d());
	}
	
	public static Sphere3d getMinSphere(PointSet3d points, int n, PointSet3d boundaryPoints) {
		Sphere3d sphere = null;
		int k = 0;
		switch (boundaryPoints.getSize()) {
			case 0: sphere = new Sphere3d(points.get(0), points.get(1)); k = 2; break;
			case 1: sphere = new Sphere3d(points.get(0), boundaryPoints.get(0)); k = 1; break;
			case 2: sphere = new Sphere3d(boundaryPoints.get(0), boundaryPoints.get(1)); break;
			case 3: sphere = new Sphere3d(boundaryPoints.get(0), boundaryPoints.get(1), boundaryPoints.get(2)); break;
		}
		
		for (int i = k; i < n + boundaryPoints.getSize(); i++) {
			Point3d p = (Point3d)points.get(i);
			if (!boundaryPoints.contains(p)) {
				if (!sphere.isInside(p)) {
					if (boundaryPoints.getSize() < 3) {
						boundaryPoints.insert(p);
						sphere = getMinSphere(points, i-1, boundaryPoints);
						boundaryPoints.delete(p);
					}
					else sphere = new Sphere3d(boundaryPoints.get(0), boundaryPoints.get(1), boundaryPoints.get(2), p);
				}
			}
		}
		return sphere;
	}
	
	private void computeSphere3d(Point3d p0, Point3d p1, Point3d p2, Point3d p3) {
		double x0 = p0.getX(); double y0 = p0.getY(); double z0 = p0.getZ();
		double x1 = p1.getX(); double y1 = p1.getY(); double z1 = p1.getZ();
		double x2 = p2.getX(); double y2 = p2.getY(); double z2 = p2.getZ();
		double x3 = p3.getX(); double y3 = p3.getY(); double z3 = p3.getZ();
		
		double xx0 = x0*x0 + y0*y0 + z0*z0, xx1 = x1*x1 + y1*y1 + z1*z1;
		double xx2 = x2*x2 + y2*y2 + z2*z2, xx3 = x3*x3 + y3*y3 + z3*z3;
		
		double x1y2 = x1*y2, x1y3 = x1*y3, x1z2 = x1*z2, x1z3 = x1*z3;
		double x2y1 = x2*y1, x2y3 = x2*y3, x2z1 = x2*z1, x2z3 = x2*z3; 
		double x3y2 = x3*y2, x3y1 = x3*y1, x3z2 = x3*z2, x3z1 = x3*z1;

		double y1z2 = y1*z2, y1z3 = y1*z3;
		double y2z1 = y2*z1, y2z3 = y2*z3;
		double y3z1 = y3*z1, y3z2 = y3*z2;
		
		
		double m11 =  x0*(y1z2 + y3z1 + y2z3 - y1z3 - y2z1 - y3z2)
		             -y0*(x1z2 + x3z1 + x2z3 - x1z3 - x2z1 - x3z2)
		             +z0*(x1y2 + x3y1 + x2y3 - x1y3 - x2y1 - x3y2)
		             -((x1y2-x2y1)*z3 + (x3y1-x1y3)*z2 + (x2y3-x3y2)*z1);
			
		if (m11 != 0.0) {
			
			double m12 =  xx0*(y1z2 + y3z1 + y2z3 - y1z3 - y2z1 - y3z2)
            -y0*(xx1*(z2-z3)     + xx3*(z1-z2)     + xx2*(z3-z1))
            +z0*(xx1*(y2-y3)     + xx3*(y1-y2)     + xx2*(y3-y1))
               -(xx1*(y2z3-y3z2) + xx3*(y1z2-y2z1) + xx2*(y3z1-y1z3));
		
			double m13 =  xx0*(x1z2 + x3z1 + x2z3 - x1z3 - x2z1 - x3z2)
			-x0*(xx1*(z2-z3)     + xx3*(z1-z2)     + xx2*(z3-z1))
            +z0*(xx1*(x2-x3)     + xx3*(x1-x2)     + xx2*(x3-x1))
               -(xx1*(x2z3-x3z2) + xx3*(x1z2-x2z1) + xx2*(x3z1-x1z3));

			double m14 =  xx0*(x1y2 + x3y1 + x2y3 - x1y3 - x2y1 - x3y2)
            -x0*(xx1*(y2-y3)     + xx3*(y1-y2)     + xx2*(y3-y1))
            +y0*(xx1*(x2-x3)     + xx3*(x1-x2)     + xx2*(x3-x1))
               -(xx1*(x2y3-x3y2) + xx3*(x1y2-x2y1) + xx2*(x3y1-x1y3));

			double m15 =  xx0*(z3*(x1y2-x2y1) + z2*(x3y1-x1y3) + z1*(x2y3-x3y2))
            -x0*(xx1*(y2z3-y3z2) + xx3*(y1z2-y2z1) + xx2*(y3z1-y1z3))
            +y0*(xx1*(x2z3-x3z2) + xx3*(x1z2-x2z1) + xx2*(x3z1-x1z3))
            -z0*(xx1*(x2y3-x3y2) + xx3*(x1y2-x2y1) + xx2*(x3y1-x1y3));

	
		    double x =  0.5*m12/m11;
		    double y = -0.5*m13/m11;
		    double z =  0.5*m14/m11;
		    center = new Point3d(x, y, z);
		    radius = Math.sqrt(x*x + y*y + z*z - m15/m11);
		}
		else System.out.println("Points are coplanar");
	}
	
	public static Sphere3d createBoundingSphere(Sphere3d s1, Sphere3d s2){
		Vector3d d = s1.center.vectorTo(s2.center);
		double dAbs = d.length();	//4HOps
		d.multiplyIn( 1/dAbs );		//4HOps

		Point3d p1 = s1.center.add(d.multiply(-s1.radius));//TODO: Optimize so object-allocations are spared
		Point3d p2 = s2.center.add(d.multiply(s2.radius));
		//20HOps so far.
		return new Sphere3d(p1,p2);//8HOps
	}

	public Point3d getCenter() { return center; }
	public double getRadius() { return radius; }
	public double getSquaredRadius() { return radius * radius; }
	public double getSurfaceArea() { return 4*Math.PI*radius*radius; }
	public double getVolume() { return getSurfaceArea()*radius/3; }
	public boolean isInside(Point3d p) { 
		if (center == null) return false;
		else return center.getSquaredDistance(p) < radius * radius; 
	}
	
	/*
	 * returns true if this sphere is intersected or touched by another sphere
	 */
	public boolean isIntersected (Sphere3d sphere) {
		return (center.getSquaredDistance(sphere.center) <= radius*radius + sphere.radius*sphere.radius);
	}
	
	/*
	 * returns true if this sphere intersects a pane 
	 */
	public boolean isIntersecting(Plane3d plane) {
		return Math.abs(Vector3d.dotProduct(center, plane.n) - plane.d) <= radius;
	}

	
	/**
	 * returns the secant of the circle on a given line
	 * @param line
	 * @return
	 */
	public Segment3d getIntersection(Line3d line) {
		Point3d p1 = line.p;
		Point3d p2 = line.getPoint(1.0);
		double dx = p2.x - p1.x;
		double dy = p2.y - p1.y;
		double dz = p2.z - p1.z;
		double ex = p1.x - center.x;
		double ey = p1.y - center.y;
		double ez = p1.z - center.z;
		double a = dx*dx + dy*dy + dz*dz;
		double b = 2*(dx*ex + dy*ey + dz*ez);
		double c = center.x*center.x + center.y*center.y + center.z*center.z + 
		           p1.x*p1.x + p1.y*p1.y + p1.z*p1.z - 
		           2*(center.x*p1.x + center.y*p1.y + center.z*p1.z) - radius*radius;
		double delta = b*b - 4*a*c; 
		if (delta < 0) return null;
		double u1, u2;
		if (delta == 0) u1 = u2 = - b/(2*a);
		else {
			double sqr = Math.sqrt(delta);
			u1 = (-b + sqr)/(2*a);
			u2 = (-b - sqr)/(2*a);
		}
		return new Segment3d(new Point3d(p1.x + u1*dx, p1.y + u1*dy, p1.z + u1*dz),
							 new Point3d(p1.x + u2*dx, p1.y + u2*dy, p1.z + u2*dz));
	}

	
	
	/*
	 * returns true if none of the given points is in the sphere
	 */
	public boolean isEmpty(PointSet3d points) {
		double rr = radius*radius;
		int size = points.getSize(); 
		int i = 0;
		while (i < size) if (((Point3d)points.get(i++)).getSquaredDistance(center) < rr - 0.000000001) return false;
		return true;
	}
	
	/*
	 * returns true if none of the given points except points a, b, c and d is in the sphere.
	 */
	public boolean isEmpty(PointSet3d points, Point3d[] pts) {
		System.out.print("Points on the sphere: "); for (int g=0; g < 4; g++) System.out.print(pts[g]); System.out.println();
		double rr = radius*radius;
		Point3d p;
		int size = points.getSize();
		for (int i = 0; i < size; i++) {
			p = (Point3d)points.get(i);
			System.out.println("Is " + p.toString(3) + " inside the sphere?");
			if ((pts[0] != p) && (pts[1] != p) && (pts[2] != p) && (pts[3] != p) && (p.getSquaredDistance(center) < rr - 0.000000001)) return false;
		}
		return true;
	}

	
	/*
	 * squared "distance" of a point from a sphere (negative if the point is inside the sphere)
	 */
	public double powerDistance(Point3d p) {
		return getCenter().getSquaredDistance(p) - radius*radius; 
	}
	
	public String toString() { return "center: " + center.toString() + ", radius: " + misc.Functions.toString(radius);}
	public String toString(int dec) { return "center: " + center.toString(dec) + ", radius: " + misc.Functions.toString(radius, dec);}

	public void toConsole() { System.out.println(toString()); }
	public void toConsole(int dec) { System.out.println(toString(dec)); }
	public void toConsole(String txt, int dec) { System.out.println(txt + toString(dec)); }

	public boolean overlaps(Volume3d vol) {
		if(vol instanceof Sphere3d) return ((Sphere3d)vol).center.getDistance(this.center)<=((Sphere3d)vol).radius+radius;
		throw new IllegalArgumentException();
	}

	public double volume() {
		return 3*Math.PI*radius*radius*radius/4;
	}

	public Sphere3d clone(){ 
		return new Sphere3d(center.clone(), radius);
	}
	
	/* 10HOps */
	public double[] intersectionParameters(Line3d line) {
		Vector3d l = line.getDir();//.norm();
		Vector3d c = line.getP().vectorTo(center);
		double lc = l.dot(c);
		double cc = c.dot(c);
		double rr = radius*radius;
		double tmp = lc*lc-cc+rr;
		if(tmp<0) return new double[0];
		else if(tmp==0) return new double[]{lc};
		else {
			double d1 = lc-Math.sqrt(tmp);
			double d2 = lc+Math.sqrt(tmp);
			return new double[]{d1, d2};
		}
	}

}

