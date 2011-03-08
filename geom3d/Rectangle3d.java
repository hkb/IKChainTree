package geom3d;

import j3dScene.J3DScene;

import java.awt.Color;
import java.util.Random;


public class Rectangle3d {
	public Point3d center;
	public Vector3d[] bases = new Vector3d[2];
	
	public Rectangle3d(Point3d center, Vector3d[] bases){
		this.center = center;
		this.bases = new Vector3d[]{bases[0], bases[1]};
	}
	
	public Rectangle3d(Random rand) {
		center = new Point3d(rand);
		Vector3d u = new Vector3d(rand);
		while (u.isZeroVector()) u = new Vector3d(rand);
		bases[0] = u;
		Vector3d v = new Vector3d(rand);
		while (v.isZeroVector() || v.isParallel(u)) v = new Vector3d(rand);
		Vector3d uxv = u.cross(v);
		bases[1] = uxv;
	}

	public double distance(Rectangle3d rect){
		return distance_optimized(rect);
	}

	/* 196..766 ~*/
	public double distance_optimized(Rectangle3d rect){
		Point3d[] thisCorners = getCorners();		//counter-clockwise order starting with bases[0]+bases[1]
		Point3d[] rectCorners = rect.getCorners();
		Vector3d[] thisNormals = {bases[1],bases[0].reverse(), bases[1].reverse(),bases[0]};//6HOps
		Vector3d[] rectNormals = {rect.bases[1],rect.bases[0].reverse(), rect.bases[1].reverse(),rect.bases[0]};//6HOps

		boolean[][] inVoronoi1 = genInVoronoi(thisCorners, thisNormals, rectCorners, rectNormals);//48HOps
		boolean[][] inVoronoi2 = genInVoronoi(rectCorners, rectNormals, thisCorners, thisNormals);//48HOps
		
		int[] perm = {0,2,1,3};
		for(int i: perm){
			for(int j: perm){
				if(!inVoronoi1[i][j] && !inVoronoi1[i][(j+1)%4]) continue;
				if(!inVoronoi2[j][i] && !inVoronoi2[j][(i+1)%4]) continue;
				double c = checkEdgePair(//38HOps
						thisCorners[i],thisCorners[(i+1)%4],thisNormals[i], 
						rectCorners[j],rectCorners[(j+1)%4],rectNormals[j]);
//				System.out.println("c = " + c);
				if(c>=0) {
//					System.out.println("Stopped when i = " + i + " and j = " + j);
					return c;
				}
			}
		}//38 -> 16*38 = 608HOps

		double sep1 = axisSeparation(thisCorners,rectCorners);//25HOps
		double sep2 = axisSeparation(rectCorners,thisCorners);//25HOps
		return Math.max(sep1,sep2);
	}

	/** 16*3 = 48HOps */
	private static boolean[][] genInVoronoi(Point3d[] corners1, Vector3d[] normals1, Point3d[] corners2, Vector3d[] normals2){
		boolean[][] ret = new boolean[4][4];
		
		//e_0
		ret[0][0] = corners1[0].vectorTo(corners2[0]).dot(normals1[0])>=0;
		ret[0][1] = corners1[0].vectorTo(corners2[1]).dot(normals1[0])>=0;
		ret[0][2] = corners1[0].vectorTo(corners2[2]).dot(normals1[0])>=0;
		ret[0][3] = corners1[0].vectorTo(corners2[3]).dot(normals1[0])>=0;

		//e_1
		ret[1][0] = corners1[2].vectorTo(corners2[0]).dot(normals1[1])>=0;
		ret[1][1] = corners1[2].vectorTo(corners2[1]).dot(normals1[1])>=0;
		ret[1][2] = corners1[2].vectorTo(corners2[2]).dot(normals1[1])>=0;
		ret[1][3] = corners1[2].vectorTo(corners2[3]).dot(normals1[1])>=0;

		//e_2
		ret[2][0] = corners1[2].vectorTo(corners2[0]).dot(normals1[2])>=0;
		ret[2][1] = corners1[2].vectorTo(corners2[1]).dot(normals1[2])>=0;
		ret[2][2] = corners1[2].vectorTo(corners2[2]).dot(normals1[2])>=0;
		ret[2][3] = corners1[2].vectorTo(corners2[3]).dot(normals1[2])>=0;
		
		//e_3
		ret[3][0] = corners1[0].vectorTo(corners2[0]).dot(normals1[3])>=0;
		ret[3][1] = corners1[0].vectorTo(corners2[1]).dot(normals1[3])>=0;
		ret[3][2] = corners1[0].vectorTo(corners2[2]).dot(normals1[3])>=0;
		ret[3][3] = corners1[0].vectorTo(corners2[3]).dot(normals1[3])>=0;
		return ret;
	}
	
	/** 670HOps */
	public double distance_nonoptimized(Rectangle3d rect){
		Point3d[] thisCorners = getCorners();		//counter-clockwise order starting with bases[0]+bases[1]
		Point3d[] rectCorners = rect.getCorners();
		Vector3d[] thisNormals = {bases[1],bases[0].reverse(), bases[1].reverse(),bases[0]};//6HOps
		Vector3d[] rectNormals = {rect.bases[1],rect.bases[0].reverse(), rect.bases[1].reverse(),rect.bases[0]};//6HOps

		int[] perm = {0,2,1,3};
		for(int i: perm){
			for(int j: perm){
				double c = checkEdgePair(//38HOps
						thisCorners[i],thisCorners[(i+1)%4],thisNormals[i], 
						rectCorners[j],rectCorners[(j+1)%4],rectNormals[j]);
				if(c>=0) {
					return c;
				}
			}
		}//16*38 = 608HOps

		double sep1 = axisSeparation(thisCorners,rectCorners);//25HOps
		double sep2 = axisSeparation(rectCorners,thisCorners);//25HOps
		return Math.max(sep1,sep2);
	}
	
	/** 25HOps */
	private static double axisSeparation(Point3d[] corners1, Point3d[] corners2){
		Vector3d n = corners1[1].vectorTo(corners1[0]).cross(corners1[1].vectorTo(corners1[2])).normIn();//6+7=13HOps
		boolean negatives = false;
		boolean positives = false;
		double min = Double.POSITIVE_INFINITY;
		for(Point3d c: corners2){
			Vector3d v = corners1[0].vectorTo(c);
			double dot = v.dot(n);//3HOps
			min = Math.min(min, Math.abs(dot));
			if(dot>0) 	positives = true;
			else		negatives = true;
		}//4*3=12HOps
		if(positives&&negatives) return 0;
		else return min;
	}

	/** 38HOps */
	private double checkEdgePair(Point3d p1, Point3d p2, Vector3d n1, Point3d q1, Point3d q2, Vector3d n2){
		Point3d[] minDist = closestSegmentPoint(p1,p2,q1,q2);//28HOps
		Vector3d v = minDist[0].vectorTo(minDist[1]);
		if(v.dot(n1)>0 && v.dot(n2)<0) return v.length();//3+3+4=10HOps
		return -1;
	}
	
	/** 
	 * 28HOps at most. 
	 */
	public static Point3d[] closestSegmentPoint(Point3d p1, Point3d p2, Point3d q1, Point3d q2){
		Point3d startPoint1 = p1;
		Point3d startPoint2 = q1;

		Vector3d dir1 = p1.vectorTo(p2);
		Vector3d dir2 = q1.vectorTo(q2);
		
		if(dir1.length()<0.000001 && dir2.length()<0.00001 )
			return new Point3d[]{startPoint1,startPoint2};
		if(dir1.length()<0.000001) return new Point3d[]{startPoint1,closestSegmentPoint(startPoint2, q2, startPoint1)};
		if(dir2.length()<0.000001) return new Point3d[]{closestSegmentPoint(startPoint1, p2, startPoint2),startPoint2};
		//System.out.println("len1 "+d1.length()+" .. len2 "+d2.length());
		
		Vector3d r = startPoint2.vectorTo(startPoint1);
		double a = dir1.dot(dir1);//|S1| squared       .. 3HOp
		double e = dir2.dot(dir2);//|S2| squared       .. 3HOp
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
		return new Point3d[]{c1,c2};	    
	}
	
	public static Point3d closestSegmentPoint(Point3d p11, Point3d p12, Point3d p2){
		Line3d l = new Line3d(p11, p11.vectorTo(p12));
		double t = l.projectOnto(p2);
		t = clamp(t)*p11.getDistance(p12);
		return l.getPoint(t);
	}

	private static double clamp(double s){
		if(s<0) return 0;
		if(s>1) return 1;
		return s;
	}
	
	/** Return corners of rectangle in counter-clockwise order. */
	public Point3d[] getCorners() {
		return new Point3d[]{
				center.add( bases[0]).addIn( bases[1]),
				center.subtract( bases[0]).addIn(bases[1]),
				center.subtract(bases[0]).subtractIn(bases[1]),
				center.add(bases[0]).subtractIn( bases[1])
		};
	}
	
	/*
	 * returns the plane through the rectangle
	 */
	public Plane3d getPlane() {
		return new Plane3d(bases[0].cross(bases[1]).makeUnitVector(),center);
	}
	
	public void draw(J3DScene scene, Color clr) {
		geom3d.Box3d box01 = new geom3d.Box3d(center, bases[0].cross(bases[1]).scaleToLength(0.01), bases[0], bases[1]);
		scene.addShape(box01, clr);
	}
	public void draw(J3DScene scene) { draw(scene, Color.black); }
	
	public static void main(String[] args) {
		Random rand = new Random();
		Rectangle3d rect1 = new Rectangle3d(rand);
		Rectangle3d rect2 = new Rectangle3d(rand);
		Plane3d plane1 = rect1.getPlane();
		Plane3d plane2 = rect2.getPlane();
		Line3d line =plane1.getIntersection(plane2);
		
		J3DScene scene = J3DScene.createJ3DSceneInFrame();
		scene.removeAllShapes();

		scene.setBackgroundColor(Color.GREEN);
		plane1.draw(scene);
		plane2.draw(scene);
		rect1.draw(scene);
		rect2.draw(scene, Color.blue);
		line.draw(scene);
		scene.repaint();

	}
}
