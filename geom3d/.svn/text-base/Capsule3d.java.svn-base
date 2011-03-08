package geom3d;

import j3dScene.J3DScene;
import geom2d.Circle2d;
import geom2d.Point2d;
import matrix.Matrix3x3;


/**
 * A capsule (also known as a line-swept-sphere or sometimes 'cigar') class. The capsule is 
 * represented by two endpoints and a radius, and is a cylinder capped with hemispheres. 
 * Distance calculations (and thereby collision checks) can be performed very fast, but 
 * finding the minimum capsule bounding a set of points can be somewhat time-consuming and 
 * no well-documented methods exist for doing this. For a heuristic see Christer Ericson's
 * "Real-time Collision Detection". 
 * @author R. Fonseca
 */
public class Capsule3d implements Volume3d{
	public Segment3d segment;
	//public Point3d p1, p2;
	public double rad;

	/**Construct a capsule using two endpoints (center of hemispheres) and a radius (used 
	 * both for hemispheres and cylinder shape). 
	 */
	public Capsule3d(Point3d p1, Point3d p2, double r){
		this.segment = new Segment3d(p1,p2);
		this.rad = r;
	}

	public static Capsule3d createBoundingCapsule(PointSet3d points){
		return createBoundingCapsule_CovarianceFit(points);
	}

	public static Capsule3d createBoundingCapsule_CovarianceFit(PointSet3d points){
		if(points.getSize()<=0)	throw new Error("Cannot create capsule enclosing 0 points");
		if(points.getSize()==1)	return new Capsule3d(points.get(0).clone(), points.get(0).clone(), 0);
		if(points.getSize()==2)	return new Capsule3d(points.get(0).clone(), points.get(1).clone(), 0);

		Matrix3x3 covMatr = Matrix3x3.createCovarianceMatrix(points);//points.getCoVariance();//Matrix.createCovarianceMatrix(points);
		Vector3d[] eigenVecs = covMatr.getEigenvectors();
		
		Vector3d dir = eigenVecs[0];
		if(eigenVecs[1].length()>dir.length()) dir = eigenVecs[1];
		if(eigenVecs[2].length()>dir.length()) dir = eigenVecs[2];

		InfCylinder3d iCyl = InfCylinder3d.createMinRadCylinderFromDirection(points, dir);
		Capsule3d ret = iCyl.capWithHalfSpheres(points);
		return ret;
	}

	public static void main(String[] args){
		Capsule3d c1 = new Capsule3d(new Point3d(-4.802348,-16.155228,-12.992208), new Point3d(2.139424,-23.890044,-10.976707), 3.477805);
		Capsule3d c2 = new Capsule3d(new Point3d(-16.302076,1.470395,-14.510630),new Point3d(-6.953424,-12.463785,-12.937377), 5.776234);
		Capsule3d c = createBoundingCapsule_MaxDist(c1, c2);
		Capsule3d cPrim = new Capsule3d(new Point3d(-16.222689,1.178946,-14.466623),new Point3d(0.684934,-21.004854,-12.155378),5.887734);

		J3DScene scene = J3DScene.createJ3DSceneInFrame();
		scene.addShape(c1);
		scene.addShape(c2);
		scene.addShape(c, new java.awt.Color(100,100,200,100));
		scene.addShape(cPrim,new java.awt.Color(200,10,10,50));
	}
	
	/* 284HOps */
	public static Capsule3d createBoundingCapsule_MaxDist(Capsule3d v1, Capsule3d v2) {
		double[] rads = {v1.rad, v1.rad, v2.rad, v2.rad};
		Point3d[] points = {v1.segment.a, v1.segment.b, v2.segment.a, v2.segment.b};

//		double[][] distMap = new double[4][4];
		//4*6 = 24HOps
//		for(int i=0;i<4;i++) for(int j=i+1;j<4;j++) distMap[i][j] = points[i].distance(points[j])+rads[i]+rads[j];
//		
//		int m1=0, m2=1;
//		for(int i=0;i<4;i++){
//			for(int j=i+1;j<4;j++){
//				if(distMap[i][j]>distMap[m1][m2]){
//					m1 = i;
//					m2 = j;
//				}
//			}
//		}

		
		int m1 = 0, m2 = 1;
		double best = v1.segment.getLength() + v1.rad+v1.rad;//4HOps
		double dist = v2.segment.getLength() + v2.rad+v2.rad;//4HOps
		if (dist > best) { best = dist;	m1 = 2;	m2 = 3; }
		
		double sumOfRads = v1.rad + v2.rad;
		for (int i=0; i<2; i++) {
			for (int j=2; j<4; j++) {
				dist = points[i].getDistance(points[j]) + sumOfRads;//4HOps
				if (dist > best) { best = dist;	m1 = i;	m2 = j; }
			}
		}

		Vector3d dir = points[m1].vectorTo(points[m2]).scaleToLength(1);//8HOps
		//dir = new Vector3d(1,0,0);
		int exclude = 0;
		//if( m1<2 && m2<2 ) ;//A circle enclosing only 0,2 and 3 must be created
		if(m1>1 && m2>1) exclude = 2;//A circle enclosing only 0,1 and 3 must be created
		else {
			if(rads[m1]>rads[m2]) exclude = m2;//A circle enclosing all but m2 must be created
			else exclude = m1;//A circle enclosing all but m1 must be created;
		}
		//32HOps so far.
		
		InfCylinder3d iCyl = createCylinderFromDirAndThreeSpheres(dir,rads,points,exclude);//126HOps
		Capsule3d ret = iCyl.capWithHalfSpheres(v1, v2); //74HOps
		return ret;
	}
	
	/* 126 HOps */
	private static final InfCylinder3d createCylinderFromDirAndThreeSpheres(Vector3d dir, double[] rads, Point3d[] points, int exclude){
		Plane3d p = new Plane3d(dir, new Point3d(0,0,0));
		
		//Rand is guaranteed not to be parallel with dir
		Vector3d rand = new Vector3d(1,(dir.x==-1||dir.x==1)?1:0,0);
		
		Vector3d x = rand.cross(dir).scaleToLength(1);	//10HOps
		Vector3d y = x.cross(dir);						//6HOps
		Circle2d[] cArr = new Circle2d[3];
		int c=0;
		for(int i=0;i<3;i++){
			if(exclude==c) c++;
			Vector3d proj = p.projectPoint(points[c]).toVector();					//6HOps
			cArr[i] = new Circle2d(new Point2d(x.dot(proj), y.dot(proj)), rads[c]);	//6HOps
			c++;
		}
		//10+6+12*3=52HOps so far

		Circle2d mec = new Circle2d( cArr[0],cArr[1],cArr[2]);//68HOps
		Point3d linePoint = x.multiply(mec.getCenter().getX()).add(y.multiply(mec.getCenter().getY())).toPoint();//6HOps
		return new InfCylinder3d(new Line3d(linePoint,dir), mec.getRadius());
	}
	
	private static double clamp(double s){
		if(s<0) return 0;
		if(s>1) return 1;
		return s;
	}

	public double distanceToPoint(Point3d point){
		Vector3d d = segment.getAToB();
		double t = clamp( -(point.vectorTo(segment.a).dot(d))/(d.dot(d)) );
		return ( segment.a.addIn(d.multiplyIn(t)).subtractIn(point) ).toVector().getLength();
	}
	public boolean overlaps(Capsule3d capsule){
		double minDist = closestSegmentPoint(capsule); 
		return minDist<=(rad+capsule.rad);
	}

	public double closestSegmentPoint(Capsule3d capsule){
		Point3d startPoint1 = segment.a;
		Point3d startPoint2 = capsule.segment.a;

		Vector3d dir1 = segment.getAToB();
		Vector3d dir2 = capsule.segment.getAToB();
		
		if(segment.lengthSq<0.000001 && capsule.segment.lengthSq<0.00001 )
			return startPoint1.getDistance(startPoint2);
		if(segment.lengthSq<0.000001) return closestSegmentPoint(startPoint2, capsule.segment.b, startPoint1);
		if(capsule.segment.lengthSq<0.000001) return closestSegmentPoint(startPoint1, segment.b, startPoint2);
		//System.out.println("len1 "+d1.length()+" .. len2 "+d2.length());
		
		Vector3d r = startPoint2.vectorTo(startPoint1);
		double a = segment.lengthSq;//dir1.dot(dir1);//|S1| squared       .. 3HOp
		double e = capsule.segment.lengthSq;//dir2.dot(dir2);//|S2| squared       .. 3HOp
		double f = dir2.dot(r);//                    .. 3HOp
		double c = dir1.dot(r);//                    .. 3HOp
		double b = dir1.dot(dir2);//                   .. 3HOp
		double denom = a*e-b*b;//                  .. 2HOp

		//If segments not parallel, compute closest point on L1 and L2
		//and clamp to S1
		double s, t;
		if(denom!=0.0f)  s = clamp( (b*f-c*e)/denom );//      .. 3HOp
		else             s = 0.0f;

		//Compute point on L2 closest to S1(S)
		double tnom = b*s+f;//                     .. 1HOp

		//If t in [0,1] done. Else clamp t and recompute and clamp s
		//.. 1 HOp
		if(tnom<0.0f){
			t = 0.0f;
			s = clamp(-c/a);
		}else if(tnom>e){
			t = 1.0f;
			s = clamp( (b-c)/a );
		}else{
			t = tnom/e;
		}

		Point3d c1 = startPoint1.add(dir1.multiplyIn(s));//      vec-scalar mult  .. 3HOp
		Point3d c2 = startPoint2.add(dir2.multiplyIn(t));//      vec-scalar mult  .. 3HOp
		return c1.getDistance(c2);	                //          .. 3HOp
	}
	
	public static double closestSegmentPoint(Point3d p11, Point3d p12, Point3d p2){
		Line3d l = new Line3d(p11, p11.vectorTo(p12));
		double t = l.orthogonalProjectionParameter(p2);
		t = clamp(t)*p11.getDistance(p12);
		return l.getPoint(t).getDistance(p2);
	}


	public boolean overlaps(Volume3d vol) {
		if(vol instanceof Capsule3d) return overlaps((Capsule3d)vol);
		throw new Error("Unimplemented");
	}

	public boolean contains(Point3d p){
		Line3d l = new Line3d(segment);
		double t = l.orthogonalProjectionParameter(p);
		if(t>1) t=1; else if(t<0) t=0;
		return l.getPoint(t).getDistance(p)<=this.rad;
	}


	public double volume() {
		double sphereVols = (4d/3d)*Math.PI*rad*rad*rad;
		double cylVol = Math.PI*rad*rad*segment.getLength();
		return sphereVols+cylVol;
	}

	public Capsule3d clone(){
		return new Capsule3d(segment.a.clone(), segment.b.clone(), rad);
	}
	public Point3d getCenter() {
		return segment.getMidPoint();
	}

	public String toString(){
		return String.format("Capsule3d[%s->%s,r=%f]", segment.a, segment.b, rad);
	}

}
