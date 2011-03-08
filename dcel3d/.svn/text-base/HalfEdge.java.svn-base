package dcel3d;

import geom3d.*;

public class HalfEdge {
	protected Vertex origin;   // source vertex
	protected HalfEdge next;   // next half-edge 
	protected HalfEdge prev;   // previous half-edge
	protected HalfEdge twin;   // matching edge on the adjacent facet 
	protected Face face;
	private boolean deletionMark;
	
	public HalfEdge(Vertex a) {
		origin = a;
		deletionMark = false;
	}
	
	public void clear() {
		if (twin != null) twin.twin = null;
		origin = null;
		next = prev = twin = null;
		face = null;
	}
	
	public Vertex getOrigin() { return origin; }
	public Vertex getTarget() { return twin.origin; }
	public Point3d getOriginPoint() { return origin.getPoint(); }
	public Point3d getTargetPoint() { return twin.getOriginPoint(); }
	public HalfEdge getNext() { return next; }
	public HalfEdge getPrev() { return prev; }
	public HalfEdge getTwin() { return twin; }
	public Face getFace() { return face; }
	public boolean getDeletionMark() { return deletionMark; }
	
	public void setOrigin(Vertex origin) { this.origin = origin; } 
	public void setNext(HalfEdge next) { this.next = next; }
	public void setPrev(HalfEdge prev) { this.prev = prev; }
	public void setTwin(HalfEdge twin) { this.twin = twin; }
	public void setFace(Face face) { this.face = face; }
	public void setDeletionMark(boolean deletionMark) { this.deletionMark = deletionMark; }
	
	public String toString() { 
		String str = "[";
		Point3d p = origin.getPoint();
		str = str + p.toString() + ",";
		if (twin != null) {
			p = twin.getOrigin().getPoint();
			str = str + p.toString();
		}
		str = str + "]";
		return str;
	}
	
	public String toString(int nrDecimals) { 
		String str = "[";
		Point3d p = origin.getPoint();
		str = str + p.toString(nrDecimals) + ",";
		if (twin != null) {
			p = twin.getOrigin().getPoint();
			str = str + p.toString(nrDecimals);
		}
		str = str + "]";
		return str;

	}
}
