package geom2d;

import java.awt.*;
import java.util.List;

import misc.*;

public class Circle2d {
	protected Point2d center;
	protected double radius;

	/*
	 * creates circle with a given center and radius
	 */
	public Circle2d(Point2d center, double radius) {
		this.center = center;
		this.radius = radius;
	}

	/** 
	 * @author Rasmus
	 */
	public Circle2d(Point2d p1, Point2d p2){
		this( Point2d.getMidPoint(p1, p2), p1.getDistance(p2)/2 );
	}

	/**
	 * creates circle through 3 given points
	 */
	public Circle2d(Point2d a, Point2d b, Point2d c) {
		Line2d ab = Point2d.getBisector(a, b);
		Line2d bc = Point2d.getBisector(b, c);
		if(a.sameCoordinates(b) || b.sameCoordinates(c)){
			center = Point2d.getMidPoint(a, c);
			radius = center.getDistance(a);
		}else if(a.sameCoordinates(c)){
			center = Point2d.getMidPoint(a, b);
			radius = center.getDistance(a);
		}else if(ab.isParallelWith(bc)){
			Point2d p1 = a;
			Point2d p2 = b;
			if(a.getDistance(b)<a.getDistance(c)) p2 = c;
			if(b.getDistance(c)>p1.getDistance(p2)) p1 = b;
			center = Point2d.getMidPoint(p1, p2);
			radius = p1.getDistance(p2);
		}else{
			center = Line2d.getIntersection(ab, bc);
			if(center==null) throw new Error(a+" "+b+" "+c);
			radius = center.getDistance(c);
		}
	}
	
	/**
	 * Creates circle through 2 given points and with center on given line
	 */
	public Circle2d(Line2d ab, Point2d b, Point2d c) {
		Line2d bc = Point2d.getBisector(b, c);
		center = Line2d.getIntersection(ab, bc);
		radius = center.getDistance(c);
	}
  
	/**
	 * Creates smallest circle containing two given circles
	 * @param c1 
	 * @param c2
	 */
	public Circle2d(Circle2d c1, Circle2d c2){
		if(c1.contains(c2)){
			center = c1.center.clone();
			radius = c1.radius;
		}else if(c2.contains(c1)){
			center = c2.center.clone();
			radius = c2.radius;
		}else if(c1.center.equals(c2.center)){
			center = c1.center.clone();
			radius = Math.max(c1.radius, c2.radius);
		}else{
			Line2d l = new Line2d(c1.center, c2.center);
			center = l.getPoint(0.5*c1.center.getDistance(c2.center)-c1.radius/2+c2.radius/2);
			radius = center.getDistance(c1.center)+c1.radius;
		}
	}
	
	public Circle2d(Circle2d circle1, Circle2d circle2, Circle2d circle3) {
		Circle2d c1 = circle1;
		Circle2d c2 = circle2;
		Circle2d c3 = circle3;

		Circle2d tmp;
		if( (tmp=new Circle2d(c1,c2)).contains(c3)) { center = tmp.center; radius = tmp.radius; return;}
		if( (tmp=new Circle2d(c1,c3)).contains(c2)) { center = tmp.center; radius = tmp.radius; return;}
		if( (tmp=new Circle2d(c2,c3)).contains(c1)) { center = tmp.center; radius = tmp.radius; return;}

		Circle2d mec = ApolloniusSolver.solveApollonius(c1, c2, c3, 1, 1, 1);//68HOps
		this.center = mec.center;
		this.radius = mec.radius;
	}


	public Point2d getCenter() { return center; }
	public double getRadius() { return radius; }


	/**
	 * returns true if none of the given points is in the circle
	 */
	public boolean isEmpty(PointSet2d points) {
		double rr = radius*radius;
		int size = points.getSize(); 
		int i = 0;
		while (i < size) if (((Point2d)points.get(i++)).getSquaredDistance(center) < rr - 0.000000001) return false;
		return true;
	}

	/**
	 * returns true if none of the given points except points a, b and c is in the circle
	 */
	public boolean isEmpty(PointSet2d points, Point2d a, Point2d b, Point2d c) {
		double rr = radius*radius;
		Point2d p;
		int size = points.getSize(); 
		int i = 0;
		while (i < size) {
			p = (Point2d)points.get(i++);
			if ((p != a) && (p != b) && (p != c) && (p.getSquaredDistance(center) < rr - 0.000000001)) return false;
		}
		return true;
	}


	/**
	 * returns the secant of the circle on a given line
	 * @param line
	 * @return
	 */
	public Segment2d getIntersection(Line2d line) {
		Point2d p1 = line.p;
		Point2d p2 = line.getPoint(1.0);
//		Point2d p3 = center;
		double dx = p2.x - p1.x;
		double dy = p2.y - p1.y;
		double ex = p1.x - center.x;
		double ey = p1.y - center.y;
		double a = dx*dx + dy*dy;
		double b = 2*(dx*ex + dy*ey);
		double c = center.x*center.x + center.y*center.y + p1.x*p1.x + p1.y*p1.y - 2*(center.x*p1.x + center.y*p1.y) - radius*radius;
		double delta = b*b - 4*a*c; 
		if (delta < 0) return null;
		double u1, u2;
		if (delta == 0) u1 = u2 = -b/(2*a);
		else {
			double sqr = Math.sqrt(delta)/(2*a);
			u1 = -b + sqr;
			u2 = -b - sqr;
		}
		return new Segment2d(new Point2d(p1.x + u1*dx, p1.y + u1*dy),
							 new Point2d(p1.x + u2*dx, p1.y + u2*dy));
	}
	
	public static Circle2d minimumEnclosingCircle_Welzl(List<Point2d> points){
		Point2d[] b = new Point2d[3];
		return findMEC(points.size(), points,0,b);
	}
	
	private static Circle2d findMEC(int n, List<Point2d> points, int m, Point2d[] b){
		Circle2d mec = new Circle2d(new Point2d(0,0),0);
		
		// Compute the Smallest Enclosing Circle defined by B
		if(m == 1)			mec = new Circle2d(b[0],0);
		else if(m == 2)		mec = new Circle2d(b[0], b[1]);
		else if(m == 3)		return new Circle2d( b[0], b[1], b[2]);
	
		// Check if all the points in p are enclosed
		for(int i=0; i<n; i++)	{
			if(!mec.contains(points.get(i))){
				// Compute B <--- B union P[i].
				b[m] = points.get(i);	
				mec = findMEC(i,points, m+1, b);// Recurse
			}
		}
		
		return mec;
	}
	
	@Deprecated
	public static Circle2d minimumEnclosingCircle_bruteforce(List<Point2d> points){
		System.err.println("Warning: Please only use Circle2d.minimumEnclosingCircle_bruteforce for testing purposes!!");
		//System.out.println("MEC .. ");
		//for(Point2d p: points) System.out.println(p);

		//System.out.println("Checking pairs");
		double minRad = Double.POSITIVE_INFINITY;
		Circle2d minCircle = null;
		for(int i=0;i<points.size();i++){
			for(int j=i;j<points.size();j++){
				if(i==j) continue;
				Circle2d tmp = new Circle2d(points.get(i), points.get(j));
				boolean containsAll = true;
				for(Point2d p: points) if(!tmp.contains(p)) {
					containsAll=false;break;
				}
				//System.out.println(tmp+" .. contains all: "+containsAll);
				if(containsAll && tmp.radius<minRad){
					minRad = tmp.radius;
					minCircle = tmp;
				}
			}
		}

		for(int i=0;i<points.size();i++){
			for(int j=i;j<points.size();j++){
				for(int k=j;k<points.size();k++){
					if(i==j || i==k || j==k) continue;
					Circle2d tmp = new Circle2d(points.get(i), points.get(j), points.get(k));
					boolean containsAll = true;
					for(Point2d p: points) if(!tmp.contains(p)) {
						//System.out.println(" ! "+tmp.getCenter().getDistance(p)+" from "+p);
						containsAll=false;break;
					}
					if(containsAll && tmp.radius<minRad){
						minRad = tmp.radius;
						minCircle = tmp;
					}
					/*if(i==1 && j==2 && k==3){
						J3DScene scene = J3DScene.createJ3DSceneInFrame();
						scene.addShape(new Sphere3d(new Point3d(points.get(i).x, points.get(i).y, 0), 0.3), Color.BLACK);
						scene.addShape(new Sphere3d(new Point3d(points.get(j).x, points.get(j).y, 0), 0.3), Color.BLACK);
						scene.addShape(new Sphere3d(new Point3d(points.get(k).x, points.get(k).y, 0), 0.3), Color.BLACK);
						scene.addShape(new Sphere3d(new Point3d(points.get(0).x, points.get(0).y, 0), 0.3), Color.GRAY);
						scene.addShape(new Cylinder3d(new Point3d(tmp.center.x, tmp.center.y, 0), new Point3d(tmp.center.x, tmp.center.y, 0.1), tmp.radius), new Color(0,0,240, 100));
						scene.addShape(new Cylinder3d(new Point3d(minCircle.center.x, minCircle.center.y, 0), new Point3d(minCircle.center.x, minCircle.center.y, 0.1), minCircle.radius), new Color(0,0,240, 100));
					}*/
				}
			}

		}
		
		if(minCircle==null) throw new Error("minCircle not set .. "+points.size());
		return minCircle;
	}

	/** @author Rasmus */
	private boolean contains(Point2d p) {
		return center.getDistance(p)<=(radius+0.0001);
	}
	public boolean contains(Circle2d c){
		return radius>=center.getDistance(c.center)+c.radius-0.000001;
	}

	@Override
	public String toString() {
		return "Circle is centered at " + center.toString() + " and has radius " 
		+ String.valueOf(radius);
	}
	public String toString(int k) {
		return "Circle is centered at " + center.toString(k) + " and has radius " 
		+ Functions.toString(radius,k); //.valueOf(radius);
	}

	public void toConsole() { System.out.println(toString()); } 

	/*
	 * draws circle within specified frame
	 */
	public void draw(Graphics g, Frame f) {
		g.drawOval((int)((center.x - radius) * Constants.getWidth(f)) + Constants.getOffsetWidth(f), 
				(int)((1-center.y - radius) * Constants.getHeight(f)) + Constants.getOffsetHeight(f), 
				(int)(2 * radius * Constants.getWidth(f)), 
				(int)(2 * radius * Constants.getHeight(f)));

	}
}
