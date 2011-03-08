package geom3d;

import j3dScene.J3DScene;

import java.awt.Color;
import java.util.Arrays;

import matrix.Matrix3x3;

/** 
 * Implementation of a Rectangular Swept Sphere that supports overlap check and construction from 
 * a point-set and two RSS'.
 * @author Rasmus
 */
public class RSS3d implements Volume3d{
	public Rectangle3d rectangle;
	public double radius;

	public RSS3d(Point3d center, Vector3d[] bases, double radius){
		this.rectangle = new Rectangle3d(center, bases);
		this.radius = radius;
	}

	public boolean overlaps(Volume3d vol) {
		if(vol instanceof RSS3d) return overlaps((RSS3d)vol);
		throw new Error("Unimplemented ("+vol.getClass().getSimpleName()+")");
	}

	/**
	 * 674HOps 
	 */
	public boolean overlaps(RSS3d rss){
		double sqRads= (radius+rss.radius); sqRads = sqRads*sqRads; //1HOp
		double sqCenterDist = rectangle.center.getSquaredDistance(rss.rectangle.center);//3HOps
		if(sqCenterDist<=sqRads) return true;
		return rectangle.distance(rss.rectangle)<=radius+rss.radius; //670HOps
	}

	public double volume() {
		double boxVol = rectangle.bases[0].length()*rectangle.bases[1].length()*radius*8;
		double cylVol = 2*(rectangle.bases[0].length()+rectangle.bases[1].length())*(float)Math.PI*radius*radius;
		double sphereVol = (float)Math.PI*radius*radius*radius*4f/3;
		return boxVol+cylVol+sphereVol;
	}

	public Point3d getCenter() {
		return rectangle.center;
	}

	public String toString(){
		return String.format("RSS[center:%s,bases[%s,%s],radius:%f]", rectangle.center, rectangle.bases[0], rectangle.bases[1],radius);
	}

	static J3DScene scene;
	public static void main(String[] args){
		PointSet3d points = new PointSet3d();
		points.insert(new Point3d(0.000000,0.000000,0.000000 ));
		points.insert(new Point3d(1.252000,-0.632000,0.582000));
		points.insert(new Point3d(2.442000,-0.053000,0.353000));
		points.insert(new Point3d(3.682000,-0.600000,0.910000));
		scene = J3DScene.createJ3DSceneInFrame();
		for(Point3d p: points) scene.addShape(new Sphere3d(p,0.02),java.awt.Color.black);
		
		RSS3d rss = createBoundingRSS_covariance(points);
//		rss.radius+=0.2;
		scene.addShape(rss, new java.awt.Color(200,100,100,100));
		
	}

	public static RSS3d createBoundingRSS_covariance(PointSet3d points){
		RSS3d ret;
		Matrix3x3 covMatr = Matrix3x3.createCovarianceMatrix(points);
		Vector3d[] eigenVecs = covMatr.getEigenvectors();
		Vector3d tmp;
		if(eigenVecs[0].length()<eigenVecs[1].length()) {tmp = eigenVecs[0]; eigenVecs[0] = eigenVecs[1]; eigenVecs[1] = tmp; }
		if(eigenVecs[0].length()<eigenVecs[2].length()) {tmp = eigenVecs[0]; eigenVecs[0] = eigenVecs[2]; eigenVecs[2] = tmp; }
		if(eigenVecs[1].length()<eigenVecs[2].length()) {tmp = eigenVecs[1]; eigenVecs[1] = eigenVecs[2]; eigenVecs[2] = tmp; }
		
		eigenVecs[0].normIn();
		eigenVecs[1].normIn();
//		eigenVecs[2].normIn();
		eigenVecs[2] = eigenVecs[0].cross(eigenVecs[1]);
//		scene.addShape(new Capsule3d(new Point3d(0,0,0),eigenVecs[0].toPoint(),0.05),Color.red);
//		scene.addShape(new Capsule3d(new Point3d(0,0,0),eigenVecs[1].toPoint(),0.05),Color.green);
//		scene.addShape(new Capsule3d(new Point3d(0,0,0),eigenVecs[2].toPoint(),0.05),Color.blue);
		ret = createBoundingRSSFromBases(eigenVecs, points);

		return ret;
	}

	private static RSS3d createBoundingRSSFromBases(Vector3d[] bases, PointSet3d points){
		//Find radius along the third base
		double lowestDot = Double.POSITIVE_INFINITY, highestDot = Double.NEGATIVE_INFINITY;
		for(Point3d p: points){
			double dot = bases[2].dot(p.toVector());
			if(dot<lowestDot){	lowestDot = dot;	}
			if(dot>highestDot){	highestDot = dot;	}
		}
		double radius = (highestDot-lowestDot)/2;
		if(radius<0.0001) radius = 0.0001;
		ParametricPlane3d P = new ParametricPlane3d(bases[2].multiply((highestDot+lowestDot)/2).toPoint(), bases[0], bases[1]);
		//		System.out.printf("Plane center %s, bases: [%s,%s], radius: %f\n",P.p, P.v1, P.v2, radius);

		//Slide half-cylinder caps along projection to the bases[0] and bases[1] planes.
		//dots contains the min and max along bases[0] and min and max along bases[1]
		double[] dots = {Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, 
				Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY};
		for(Point3d p: points){
			double[] proj = P.projectPoint(p);
			double delta = Math.sqrt(radius*radius-proj[2]*proj[2]);
			if(radius*radius<proj[2]*proj[2]) delta = 0;
//			System.out.printf("Point: %s, projection: [%f , %f , %f], delta: %f .. %f %f\n", p,proj[0],proj[1],proj[2],delta, proj[0]+delta, proj[0]-delta);
			if(proj[0]+delta<dots[0]){	dots[0] = proj[0]+delta;	}
			if(proj[0]-delta>dots[1]){	dots[1] = proj[0]-delta;	}
			if(proj[1]+delta<dots[2]){	dots[2] = proj[1]+delta;	}
			if(proj[1]-delta>dots[3]){	dots[3] = proj[1]-delta;	}
		}
		//		System.out.printf("Final dots: %s\n",Arrays.toString(dots));
		//TODO: Fix corners


		double[] pars = { (dots[0]+dots[1])/2, (dots[2]+dots[3])/2 };
		double[] dim = { (dots[1]-dots[0])/2, (dots[3]-dots[2])/2 };
		Point3d center = new Point3d(P.getP(pars));
		Vector3d[] rssBases = {bases[0].multiply(dim[0]), bases[1].multiply(dim[1])};

		if(Double.isNaN(center.x)) throw new Error(" nana "+Arrays.toString(dots));
		
		return new RSS3d(center, rssBases, radius);
	}


	public static RSS3d createBoundingRSS_covariance(RSS3d s1, RSS3d s2) {
		PointSet3d points = new PointSet3d();
		for(Point3d p: s1.rectangle.getCorners()) points.insert(p); 
		for(Point3d p: s2.rectangle.getCorners()) points.insert(p);
		RSS3d ret = createBoundingRSS_covariance(points);
		ret.radius+=Math.max(s1.radius, s2.radius);
		return ret;
	}

	public RSS3d clone(){
		Point3d p = rectangle.center.clone();
		Vector3d[] bases = {rectangle.bases[0].clone(), rectangle.bases[1].clone()};
		return new RSS3d(p,bases,radius);
	}

}
