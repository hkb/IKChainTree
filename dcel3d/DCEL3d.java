package dcel3d;

import dataStructures.Set;
import geom3d.*;
import graph.ConflictGraph;
import dataStructures.Stack;

public class DCEL3d {
	protected Set vertices = new Set();   // each vertex has a pointer to one of its half-edges
	protected Set halfEdges = new Set();  // each half-edge has a pointer to its face on the left
	protected Set faces = new Set();      // each face has a pointer to one of its vertices
	
/******************************************************************************************
	
    DCEL3d = Doubly Connected Edge List
  implemented by Pawel Winter, June 2008

Description: Data structure to represented surface of 3d convex hull. 
Comes with a simple 3d viewer.

Supports:
- insertion of vertices

Possible extensions:
- unbounded faces
- deletion of edges and vertices
- visualization

******************************************************************************************/

	
	
	public Set getVertices() { return vertices; }
	public int getNrVertices() { return vertices.getSize(); }
	
	public Set getHalfEdges() { return halfEdges; }
	public int getNrHalfEdges() { return halfEdges.getSize(); } 
	
	public Set getFaces() { return faces; }
	public int getNrFaces() { return faces.getSize(); }

	public DCEL3d(Point3d p0, Point3d p1, Point3d p2, Point3d p3) {
		// check orientation
		Vector3d cr = Vector3d.crossProduct(new Vector3d(p0, p2), new Vector3d(p0, p1));
		boolean ccw = Vector3d.dotProduct(cr, new Vector3d(p0, p3)) > 0.0;
		// create vertices of the dcel
		Vertex v0 = insertPoint(p0);
		Vertex v1 = ccw? insertPoint(p1) : insertPoint(p2);
		Vertex v2 = ccw? insertPoint(p2) : insertPoint(p1);
		Vertex v3 = insertPoint(p3);
		// create half-edges of the dcel
		HalfEdge v0v1 = insertSegment(v0,v1);
		HalfEdge v0v2 = insertSegment(v0,v2);
		HalfEdge v0v3 = insertSegment(v0,v3);
		HalfEdge v1v2 = insertSegment(v1,v2);
		HalfEdge v1v3 = insertSegment(v1,v3);
		HalfEdge v2v3 = insertSegment(v2,v3);
		// create faces of the dcel
		Face face021 = new Face(v0v2);
		Face face032 = new Face(v0v3);
		Face face013 = new Face(v0v1);
		Face face123 = new Face(v1v2);
		faces.insert(face021);
		faces.insert(face032);
		faces.insert(face013);
		faces.insert(face123);
		// order half-edges
		v0v1.next =      v0v2.prev      = v0v3;
		v0v2.next =      v0v3.prev      = v0v1;
		v0v3.next =      v0v1.prev      = v0v2;
		v1v2.next =      v0v1.twin.prev = v1v3;
		v1v3.next =      v1v2.prev      = v0v1.twin;
		v2v3.next =      v0v2.twin.prev = v1v2.twin;
		v0v1.twin.next = v1v3.prev      = v1v2;
		v0v2.twin.next = v1v2.twin.prev = v2v3;
		v0v3.twin.next = v2v3.twin.prev = v1v3.twin;
		v1v2.twin.next = v2v3.prev      = v0v2.twin;
		v1v3.twin.next = v0v3.twin.prev = v2v3.twin;
		v2v3.twin.next = v1v3.twin.prev = v0v3.twin;
		// assign faces to half-edges
		v0v1.face = v1v3.face      = v0v3.twin.face = face013;
		v0v2.face = v0v1.twin.face = v1v2.twin.face = face021;
		v0v3.face = v0v2.twin.face = v2v3.twin.face = face032;
		v1v2.face = v2v3.face      = v1v3.twin.face = face123;		
	}
	
	
	// not needed
	public DCEL3d(Tetrahedron tetra) {
		// create vertices of the tetrahedron
		Vertex v0 = insertPoint(tetra.getPoint(0));
		Vertex v1 = insertPoint(tetra.getPoint(1));
		Vertex v2 = insertPoint(tetra.getPoint(2));
		Vertex v3 = insertPoint(tetra.getPoint(3));
		// create half-edges of the tetrahedron
		HalfEdge v0v1 = insertSegment(v0,v1);
		HalfEdge v0v2 = insertSegment(v0,v2);
		HalfEdge v0v3 = insertSegment(v0,v3);
		HalfEdge v1v2 = insertSegment(v1,v2);
		HalfEdge v1v3 = insertSegment(v1,v3);
		HalfEdge v2v3 = insertSegment(v2,v3);
		// create faces of the tetrahedron
		Face face021 = new Face(v0v2);
		Face face032 = new Face(v0v3);
		Face face013 = new Face(v0v1);
		Face face123 = new Face(v1v2);
		faces.insert(face021);
		faces.insert(face032);
		faces.insert(face013);
		faces.insert(face123);
		// order half-edges
		v0v1.next =      v0v2.prev      = v0v3;
		v0v2.next =      v0v3.prev      = v0v1;
		v0v3.next =      v0v1.prev      = v0v2;
		v1v2.next =      v0v1.twin.prev = v1v3;
		v1v3.next =      v1v2.prev      = v0v1.twin;
		v2v3.next =      v0v2.twin.prev = v1v2.twin;
		v0v1.twin.next = v1v3.prev      = v1v2;
		v0v2.twin.next = v1v2.twin.prev = v2v3;
		v0v3.twin.next = v2v3.twin.prev = v1v3.twin;
		v1v2.twin.next = v2v3.prev      = v0v2.twin;
		v1v3.twin.next = v0v3.twin.prev = v2v3.twin;
		v2v3.twin.next = v1v3.twin.prev = v0v3.twin;
		// assign faces to half-edges
		v0v1.face = v1v3.face      = v0v3.twin.face = face013;
		v0v2.face = v0v1.twin.face = v1v2.twin.face = face021;
		v0v3.face = v0v2.twin.face = v2v3.twin.face = face032;
		v1v2.face = v2v3.face      = v1v3.twin.face = face123;			
	}
	
	public ConflictGraph getConflictGraph(PointSet3d points) {
		Face face;
		Point3d p;
		graph.Vertex vA, vB;
		int nrPoints;
		ConflictGraph cg = new ConflictGraph();
		int nrFaces = faces.getSize();
		for (int j = 0; j < nrFaces; j++) {
			// creates B-vertex of given face
			face = (Face)faces.get(j);
			vB = cg.insertVertexB(face);
			nrPoints = points.getSize();
			for (int i = 4; i < nrPoints; i++) {
				// checks if a point can see the face, if necessary creates an A-vertex 
				// representing the point and connects it with the B-vertex of the face
				p = (Point3d)points.get(i);
				if (face.isBehind(p)) { 
					vA = cg.findVertexA(p); 
					if (vA == null) vA = cg.insertVertexA(points.get(i));
					cg.insertEdge(vA, vB); 
//					System.out.println("Face " + face.toString() + " is seen from " + p.toString(2));
				}
			}
		}
		return cg;
	}

	/*
	 * returns the boundary visible from a given vertex
	 */
	public Set getVisibilityBoundary(graph.Vertex vA) {
		markVisibleHalfEdges(vA);
		Set boundary = new Set();
		HalfEdge e = getFirstBoundaryHalfEdge(vA);
//		System.out.println("First boundary half-edge: " + e.toString(2));
		boundary.insert(e);
		HalfEdge f = getNextBoundaryHalfEdge(vA, e);
		while (f != e) {
//			System.out.println("Next boundary half-edge: " + f.toString(2));
			boundary.insert(f);
			f = getNextBoundaryHalfEdge(vA, f);
		}
		return boundary;
	}
	
	/*
	 * marks every half-edges on every visible face. 
	 * Half-edges on the boundary have their twins not marked
	 */
	public void markVisibleHalfEdges(graph.Vertex vA) {
		Face face;
		HalfEdge e;
		graph.Vertex vB;
		int degree = vA.getEdges().getSize();
		for (int i = 0; i < degree; i++) {
			vB = vA.getAdjacentVertex(i);
			face = (Face)vB.getObject();
			e = face.getHalfEdge();
			do {
				e.setDeletionMark(true);
				e = face.getNextHalfEdge(e);
			} while (e != face.getHalfEdge());
		}
	}
	/*
	 * returns a boundary half-edges by going through half-edges of all visible faces
	 * until encountering a half-edge with unmarked twin.
	 */
	private HalfEdge getFirstBoundaryHalfEdge(graph.Vertex vA) {
		Face face;
		HalfEdge e;
		int degree = vA.getDegree();
		for (int i = 0; i < degree; i++) {
			graph.Vertex vB = vA.getAdjacentVertex(i);
			face = (Face)vB.getObject();
			e = face.getHalfEdge();
			do {
				if (!e.getTwin().getDeletionMark()) return e;
				e = face.getNextHalfEdge(e);
			} while (e != face.getHalfEdge());
		}
		System.out.println("Something wrong; no boundary half-edge");
		return null; // this would be an error
	}
	
	/*
	 * returns next ccw boundary half-edge given boundary edge e. 
	 */
	private HalfEdge getNextBoundaryHalfEdge(graph.Vertex vA, HalfEdge e) {
		HalfEdge f = e.getTwin();
		do  f = f.getPrev(); while (!f.getDeletionMark() || f.getTwin().getDeletionMark());
		return f;
	}
	
	public void boundaryToConsole(String str, Set boundary) {
		HalfEdge e;
		System.out.print(str);
		int size = boundary.getSize();
		for (int i = 0; i < size; i++) {
			e = (HalfEdge)boundary.get(i);
			System.out.print(e.toString(2) + " ");
		}
		System.out.println();

	}
	
	/*
	 * updates next and prev pointers of boundary half-edges so that fully visible 
	 * half-edges with origins on this boundary are omitted.
	 */
	public void cleanUpBoundary(graph.Vertex vA, Set boundary, Stack cleanUpStack) {
		graph.Vertex vB;
		Face face;
		HalfEdge e, f;
		int size = vA.getEdges().getSize();
		for (int i = 0; i < size; i++) {
			vB = vA.getAdjacentVertex(i);
			face = (Face)vB.getObject();
			e = face.getHalfEdge();
			do {
//				System.out.println("Checking half-edges for deletion " + e.toString(2));
				if (e.getDeletionMark() && e.getTwin().getDeletionMark()) { 
					cleanUpStack.push(e);
//					System.out.println("Half-edge to be deleted " + e.toString(2));
				}
				e = face.getNextHalfEdge(e);
			} while (e != face.getHalfEdge());
			faces.delete(face);
		}

		size = boundary.getSize();
		f = (HalfEdge)boundary.getLast();
		for (int i = 0; i < size; i++) {
			e = (HalfEdge)boundary.get(i);
			e.setNext(f.getTwin());
			f.getTwin().setPrev(e);
			e.getOrigin().setHalfEdge(e);
			f = e;
		}	
	}
	
	/*
	 * removes half-edges, vertices and faces visible from the added point.
	 */
	public void cleanUp(graph.Vertex vA, Stack cleanUpStack) {
		HalfEdge e;
		Vertex v;
		while (!cleanUpStack.isEmpty()) {
			e = (HalfEdge)cleanUpStack.pop();
			v = e.getOrigin();
			if (v.getHalfEdge() == e) {
				v.setHalfEdge(null);
				v.setObject(null);
				vertices.delete(v);
			}
			e.setOrigin(null);
			e.setNext(null);
			e.setPrev(null);
			e.setTwin(null);
			e.setFace(null);
			halfEdges.delete(e);
		}
	}
	
	/*
	 * Inserts new vertex and connects it to the vertices of the visible boundary.
	 * Does not update conflict graph
	 */
	public Vertex insertFaces(graph.Vertex vA, Set boundary) {
		Vertex v, w;
		Vertex u = new Vertex((Point3d)vA.getObject());
		vertices.insert(u);
		int size = boundary.getSize();
		HalfEdge eHalfEdges[] = new HalfEdge[size];
		HalfEdge fHalfEdges[] = new HalfEdge[size];	
		HalfEdge gHalfEdges[] = new HalfEdge[size];
		HalfEdge hHalfEdges[] = new HalfEdge[size];
		Face newFaces[] = new Face[size];
		
		for (int i = 0; i < size; i++) {
			eHalfEdges[i] = (HalfEdge)boundary.get(i);
			eHalfEdges[i].setDeletionMark(false);
			v = eHalfEdges[i].getOrigin();
			w = eHalfEdges[i].getTarget();
			fHalfEdges[i] = eHalfEdges[i].getTwin();
			gHalfEdges[i] = new HalfEdge(w); halfEdges.insert(gHalfEdges[i]);
			hHalfEdges[i] = new HalfEdge(u); halfEdges.insert(hHalfEdges[i]);
			v.setHalfEdge(eHalfEdges[i]);
			w.setHalfEdge(gHalfEdges[i]);
		}
		
		u.setHalfEdge(hHalfEdges[0]);
		
		for (int i = 0; i < size; i++) {
			newFaces[i] = new Face(eHalfEdges[i]); faces.insert(newFaces[i]);
			// eHalfEdges[i].setTwin() remains unchanged
			// fHalfEdges[i].setTwin() remains unchanged
			gHalfEdges[i].setTwin(hHalfEdges[(i+1) % size]);
			hHalfEdges[i].setTwin(gHalfEdges[(i-1+size) % size]);
			
			// eHalfEdges[i].setPrev() remains unchanged
			eHalfEdges[i].setNext(hHalfEdges[i].getTwin());
			fHalfEdges[i].setPrev(gHalfEdges[i]);
			// fHalfEdges[i].setNext() remains unchanged
			gHalfEdges[i].setPrev(eHalfEdges[(i+1) % size]);
			gHalfEdges[i].setNext(fHalfEdges[i]);
			hHalfEdges[i].setPrev(hHalfEdges[(i-1+size) % size]);
			hHalfEdges[i].setNext(hHalfEdges[(i+1) % size]);
			
			eHalfEdges[i].setFace(newFaces[i]);
			// fHalEdges[i].setFace() remains unchanged
			gHalfEdges[i].setFace(newFaces[i]);
			hHalfEdges[i].setFace(newFaces[i]);	
		}	
		return u;
	}
	
	public Vertex insertPoint(Point3d p) {
		Vertex v = new Vertex(p);
		vertices.insert(v);
		return v;
	}


	public HalfEdge insertSegment(Vertex a, Vertex b) {
		HalfEdge ab = new HalfEdge(a);
		HalfEdge ba = new HalfEdge(b);
		ab.twin = ba;
		ba.twin = ab;
		halfEdges.insert(ab);
		halfEdges.insert(ba);
		a.setHalfEdge(ab);
		b.setHalfEdge(ba);
		return ab;
	}
	
	public void toConsole() { toConsole(""); }
	public void toConsole(String str) {
		Face face;
		System.out.println();
		if (str != "") System.out.print(str);
		int size = faces.getSize();
		System.out.println("DCEL has " + size + " faces");
		for (int i = 0; i < size; i++) {
			face = (Face)faces.get(i);
			System.out.println(" Face " + i + ": " + face.toString());
		}
		System.out.println();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

