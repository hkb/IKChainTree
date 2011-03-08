package geom3d;

import java.util.ArrayList;
import java.util.List;


import geom2d.Circle2d;
import geom2d.Point2d;

public class InfCylinder3d {
	Line3d line;
	double radius;

	public InfCylinder3d(Line3d l, double r){
		line = l;
		radius = r;
	}

	public Capsule3d capWithHalfSpheres(PointSet3d enclosedPoints){
		double lowerT = Float.POSITIVE_INFINITY, upperT=Float.NEGATIVE_INFINITY;
		for (int i = 0; i < enclosedPoints.getSize(); i++) {
			Point3d p = enclosedPoints.get(i);
//		for(Point3d p: enclosedPoints){
			Sphere3d s = new Sphere3d(p, radius+0.00001f);
			double[] intersections = s.intersectionParameters(line);

			if(intersections.length<2){
				double intersection = line.orthogonalProjectionParameter(p);
				if(intersection>upperT) upperT = intersection;
				if(intersection<lowerT) lowerT = intersection;
			}else{
				if(intersections[0]>upperT) upperT = intersections[0];
				if(intersections[1]<lowerT) lowerT = intersections[1];
			}
		}
		return new Capsule3d(line.getPoint(lowerT), line.getPoint(upperT), radius);
	}

	public Cylinder3d capWithDiscs(PointSet3d enclosedPoints){
		double lowerT = Float.POSITIVE_INFINITY, upperT=Float.NEGATIVE_INFINITY;
		//System.out.println("capWithDiscs(..) .. "+line);
		for(Point3d p: enclosedPoints){
			Plane3d plane = new Plane3d(line.getDir(), p);
			double intersection = plane.intersectionParameter(line);
			//System.out.println("> Point "+p+" .. intersection "+intersection);
			if(intersection>upperT) upperT = intersection;
			if(intersection<lowerT) lowerT = intersection;
		}

		return new Cylinder3d(line.getPoint(lowerT), line.getPoint(upperT), radius);
	}


	public static InfCylinder3d createMinRadCylinderFromDirection(PointSet3d points, Vector3d dir){
		Plane3d p = new Plane3d(dir, new Point3d(0,0,0));
		List<Point2d> points2d = new ArrayList<Point2d>();
		Vector3d x = new Vector3d(dir.x, dir.y, dir.z+1);
		if(dir.x==0 && dir.y==0) x.x+=1;
		x = x.cross(dir).scaleToLength(1);

		Vector3d y = x.cross(dir).scaleToLength(1);
		for (int i = 0; i < points.getSize(); i++) {
			Point3d po = points.get(i);
			Point3d projected = p.projectPoint(po);
			Point2d p2d = new Point2d(x.dot(projected.toVector()), y.dot(projected.toVector()));
			points2d.add(p2d);
		}
		Circle2d mec = Circle2d.minimumEnclosingCircle_Welzl(points2d);//minimumEnclosingCircle_bruteforce(points2d);
		
		Point3d linePoint = x.multiply(mec.getCenter().getX()).addIn(y.multiply(mec.getCenter().getY())).toPoint();
		return new InfCylinder3d(new Line3d(linePoint, dir.clone()), mec.getRadius()+mec.getRadius()*0.001);
	}

	public String toString(){
		return String.format("InfCylinder[%s,%.2f]", line.toString(), radius);
	}

	/* 74HOps */
	public Capsule3d capWithHalfSpheres(Capsule3d c1, Capsule3d c2) {
		double lowerT = Double.POSITIVE_INFINITY, upperT=Double.NEGATIVE_INFINITY;
		Point3d[] centers = {c1.segment.a, c1.segment.b, c2.segment.a, c2.segment.b};
		double[] rads = {c1.rad, c1.rad, c2.rad, c2.rad};
		for(int i=0;i<4;i++){
			Point3d p = centers[i];
			double rad = rads[i];
			
			Sphere3d s = new Sphere3d(p, radius-rad+0.00001);
			double[] intersections = s.intersectionParameters(line);//10HOps
			
			if(intersections.length<2){
				double intersection = line.projectOnto(p);//7HOps
				if(intersection+rad>upperT) upperT = intersection+rad;
				if(intersection-rad<lowerT) lowerT = intersection-rad;
			}else{
				double upperIntersection = intersections[0];
				double lowerIntersection = intersections[1];
				if(upperIntersection>upperT) 	upperT = upperIntersection;
				if(lowerIntersection<lowerT) 	lowerT = lowerIntersection;
			}
		}//4*17=68HOps
		return new Capsule3d(line.getPoint(lowerT), line.getPoint(upperT), radius);//6HOps
	}
}
