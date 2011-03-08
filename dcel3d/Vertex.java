package dcel3d;

import geom3d.*;
import dataStructures.Set;

public class Vertex {
	private HalfEdge halfEdge;
	private Point3d point;
	private boolean visibleMark = false;
	protected Set visibleFaces;
	private Vertex cgVertex;
	
	public Vertex(Point3d point) {
		this.point = point;
		halfEdge = null;
	}
	
	public Point3d getPoint() { return point; }
	public HalfEdge getHalfEdge() { return halfEdge; }
	public Vertex getCGVertex() { return cgVertex; }
	
	public void setObject(Point3d point) { this.point = point; }
	
	public boolean getVisibleMark() { return visibleMark; }
	
	public int getDegree() {
		int degree = 0;
		HalfEdge e = getHalfEdge();
		do { 
			degree++;
			e = e.getNext();
		} while (e != halfEdge);
		return degree;
	}
	
	public void setHalfEdge(HalfEdge halfEdge) { this.halfEdge = halfEdge; } 
	public void setCGVertex(Vertex cgVertex) { this.cgVertex = cgVertex; }
	public void setVisibleMark(boolean visibleMark) { this.visibleMark = visibleMark; } 
}

