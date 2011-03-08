package geom3d;

import matrix.Matrix3x3;


public class ParametricPlane3d {
	public Point3d p;
	public Vector3d n, v1, v2;
	protected Matrix3x3 projInv;
	
	/** 
	 * 8 + 31 = 39HOps
	 */
	public ParametricPlane3d(Point3d p, Vector3d v1, Vector3d v2){
		this.p = p;
		this.n = v1.cross(v2).normIn();
		this.v1 = v1;
		this.v2 = v2;
		this.projInv = Matrix3x3.createColumnMatrix(v1, v2, n).inverse();
	}
	
	/** Projects the point v onto this plane and returns the parameters of 
	 * the projected point (scaling of v1, of v2 and finally the distance or 
	 * scaling along n). 9HOps
	 */
	public double[] projectPoint(Point3d v){
		Vector3d x = p.vectorTo(v);
		projInv.applyToIn(x);
		return new double[]{x.x, x.y, x.z};
	}
	
	/** Returns a parameter setting for the line describing the 
	 * intersection with this plane. Assumes there's a point 
	 * intersection.
	 */
	public double intersectionParameter(Line3d l){
		//From http://www.thepolygoners.com/tutorials/lineplane/lineplane.html
		return n.dot(l.p.vectorTo(p))/n.dot(l.dir);
	}
	
	public Point3d getP(){
		return p;
	}
	public Point3d getP(double[] pars){
		return p.add(v1.multiply(pars[0])).addIn(v2.multiply(pars[1]));
	}
}
