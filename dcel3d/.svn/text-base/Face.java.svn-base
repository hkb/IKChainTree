package dcel3d;

import geom3d.*;

public class Face {
	protected HalfEdge halfEdge;
	private Vector3d normal = null;
	
	public Face(HalfEdge halfEdge) { this.halfEdge = halfEdge; }

	public void clear(DCEL3d dcel) {
		HalfEdge e = halfEdge;
		do {
			HalfEdge e1 = getNextHalfEdge(e);
			dcel.halfEdges.delete(e);
			e = e1;
		} while (e != halfEdge);
		halfEdge = null;
		normal = null;
	}
	
	public HalfEdge getHalfEdge() { return halfEdge; }
	
	public int getNrHalfEdges () {
		int counter = 0;
		HalfEdge e = halfEdge;
		do {
			counter++;
			e = getNextHalfEdge(e);
		} while (e != halfEdge);
		return counter;
	}
	
	public Face getOppositeFace(HalfEdge e) { return e.getTwin().getFace(); }
	
	/*
	 * returns next half-edge on this face (will not work for degenerate faces with 
	 * half-edges and their twins bounding the same face.
	 */
	public HalfEdge getNextHalfEdge(HalfEdge e) { return e.getTwin().getPrev(); }
	
	public HalfEdge getPrevHalfEdge(HalfEdge e) { return e.getNext().getTwin(); }
	
	public Point3d getFacePoint() { return halfEdge.getOriginPoint(); }
	
	/*
	 * returns half-edge with given origin vertex and bounding this face
	 */
	public HalfEdge findHalfEdge(Vertex v) {
		HalfEdge e = v.getHalfEdge();
		do {
			if (e.face == this) return e; else e = e.getNext();
		} while (e != v.getHalfEdge());
		System.out.println("returning null");
		return null;
	}
	
	/*
	 * returns half-edge with given origin vertex, target vertex and bounding this face
	 */
	public HalfEdge findHalfEdge(Vertex v, Vertex w) {
		HalfEdge e = v.getHalfEdge();
		do {
			if ((e.face == this) && (e.getTarget() == w)) return e; else e = e.getNext();
		} while (e != v.getHalfEdge());
		return null;
	}

	public Vector3d getNormal() {
		if (normal == null) {
			Point3d a = halfEdge.getOriginPoint();
			Point3d b = halfEdge.getTargetPoint();
			Vector3d ab = new Vector3d(a, b);
			HalfEdge nextHalfEdge = getNextHalfEdge(halfEdge);
			Point3d c = nextHalfEdge.getTargetPoint();
			Vector3d ac = new Vector3d(a, c);
			normal = Vector3d.crossProduct(ab, ac);
			normal.normalize();				
		}
		return normal;
	}
	
	public void setHalfEdge(HalfEdge halfEdge) { this.halfEdge = halfEdge; }

	public void setNormal(Vector3d normal) { this.normal = normal; }
	
	/*
	 * returns TRUE if this face with given normal vector is seen from the point p 
	 */
	public boolean isBehind(Point3d p) { 
		return Vector3d.dotProduct(getNormal(), new Vector3d(getFacePoint(),p)) < 0.0;
	}

	/*
	 * returns TRUE if the face is triangular
	 */
	public boolean isTriangular() {
		return getNextHalfEdge(getNextHalfEdge(getNextHalfEdge(halfEdge))) == halfEdge;
	}
	
	public void toConsole() { System.out.println(toString()); }
	
	public String toString() {
		String str = "[";
		HalfEdge e = halfEdge;
		do {
			str = str  + e.getOriginPoint().toString(2) ;
			e = getNextHalfEdge(e);
		} while (e != halfEdge);
		str = str + "]";
		return str;
	}

}
