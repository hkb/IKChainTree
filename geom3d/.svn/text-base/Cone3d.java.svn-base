package geom3d;

/**
 * A cone class. The cone is represented by the endpoints and a radius. 
 * @author R. Fonseca
 */
public class Cone3d implements Volume3d{
	public Point3d p1, p2;
	public float rad;

	public Cone3d(Point3d p1, Point3d p2, float r){
		this.p1 = p1;
		this.p2 = p2;
		this.rad = r;
	}


	public boolean overlaps(Volume3d vol) {
		throw new Error("Cone.overlaps(..) not implemented");
	}
	
	public boolean contains(Point3d p){
		throw new Error("Cone.containts(..) not implemented");
	}


	public double volume() {
		return Math.PI*rad*rad*p1.getDistance(p2)/3f;
	}

	public Cone3d clone(){ throw new Error("Not implemented"); }

	public Point3d getCenter() {
		return p1.clone().addIn(p1.vectorTo(p2).multiplyIn(0.5));
	}

	public boolean overlaps(Shape3d s) { throw new Error("Not implemented");	}
}
