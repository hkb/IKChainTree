package graph;

import dataStructures.Set;
import java.util.Random;

/* *****************************************************************************************

                                 Graph
                  implemented by Pawel Winter, May 2008

Graphs represented as a set of vertices. Each vertex has a set of incident edges.

Supports:
- insertion of vertices
- insertion of edges (vertices must exist)
- deletion of vertices
- deletion of edges

Possible extensions:
- unbounded faces
- visualization

******************************************************************************************/

public class Graph {
	protected Set<Vertex> vertices;
	
	/*
	 * creates empty graph
	 */
	public Graph() { vertices = new Set<Vertex>(); }
		
	/*
	 * creates a graph with n vertices and with the expected edge density p. 
	 * Edges have integer lengths uniformly distributed in the interval 
	 * between LOW (included) and HIGH (excluded)
	 */
	public Graph(int n, double p, int low, int high) {
		Random randGen = new Random();
		vertices = new Set<Vertex>(); 
		for (int i = 0; i < n; i++) insertVertex(new Integer(i));
		for (int i = 0; i < n-1; i++) 
			for (int j =i+1; j < n; j++) 
				if (randGen.nextDouble() <= p) insertEdge(i,j,new Integer(randGen.nextInt(high-low) + low));
	}
	
	public Vertex getVertex(int i) { return vertices.get(i); }
	
	public Set<Vertex> getVertices() { return vertices; }
	
	/*
	 * set of edges
	 */
	public Set<Edge> getEdges() {
		Set<Edge> edges = new Set<Edge>();
		for (int i = 0; i < vertices.getSize(); i++) edges.append(getVertex(i).getEdges());
		return edges;
	}
	
	/*
	 * TRUE if vertices u and v are connected by an edge
	 */
	public boolean areAdjacent(Vertex u, Vertex v) {
		int uDegree = u.edges.getSize();
		Edge e;
		for (int i = 0; i < uDegree; i++) {
			e = u.edges.get(i);
			if (e.to() == v) return true;
		}
		return false;
	}
	
	/*
	 * TRUE if the object is represented by a vertex in the graph
	 */
	public boolean isVertex(Object object) { return vertices.isMember(object); }
	
	/*
	 * index of the vertex
	 */
	public int findIndex(Vertex v) {
		for (int i = 0; i < vertices.getSize(); i++) if (vertices.get(i) == v) return i;
		throw new IllegalArgumentException("vertex does not exist");
	}
	
	/*
	 * index of the vertex representing the object
	 */
	public int findIndex(Object object) { return vertices.findIndex(object); }

	/*
	 * vertex representing the object
	 */
	public Vertex findVertex(Object object) {
		for (int i = 0; i < vertices.getSize(); i++) if (getVertex(i).getObject() == object) return getVertex(i);
		return null;
	}
	
	/* 
	 * i-th vertex
	 */
	public Vertex findVertex(int i) { return getVertex(i); }
	
	
	public Object findObject(Vertex v) { return v.object; }
	public Object findObject(int i) { return getVertex(i).object; }
	
	/*
	 * return vertex of degree k
	*/
	
	public Vertex findVertexOfDegree(int k) { return findVertexOfDegree(k, 0); }
	public Vertex findVertexOfDegree(int k, int startIndx) {
		for (int i = startIndx; i < vertices.getSize(); i++) if (getVertex(i).getDegree() == k) return getVertex(i);
		return null;
		
	}
	
	/*
	 * inserts new vertex into the graph
	 */
	public Vertex insertVertex(Object data) { 
		Vertex v = new Vertex(data);
		vertices.insert(v);
		return v;
	}
	public Vertex insertVertex() { return insertVertex(null); }
	
	public void delete(Vertex v) { deleteIndex(findIndex(v)); }
	
	/*
	 * deletes a vertex with given index from the graph
	 */
	public void deleteIndex(int i) {
		Vertex v = getVertex(i);
		Edge e;
		while (!v.edges.isEmpty()) {
			e = v.edges.getLast();
			deleteEdge(e.to(),v);
		}
		vertices.deleteIndex(i);
	}
	
	public void deleteLast() { deleteIndex(vertices.getSize()-1); }
	
	/*
	 * returns the index of the edge from vertex u to vertex v 
	 */
	public int findEdgeIndex(Vertex u, Vertex v) {
		Set<Edge> uEdges = u.edges;
		for (int i = 0; i < uEdges.getSize(); i++) {
			if (uEdges.get(i).to() == v) return i;
		}
		throw new IllegalArgumentException("such edge does not exist");	
	}
	
	/*
	 * returns the edge from vertex u to vertex v
	 */
	public Edge findEdge(Vertex u, Vertex v) {
		Set<Edge> uEdges = u.edges;
		Edge uv;
		for (int i = 0; i < uEdges.getSize(); i++) {
			uv = uEdges.get(i);
			if (uv.to() == v) return uv;
		}
		return null;
	}
	
	/*
	 * inserts an edge into a graph
	 */
	public void insertEdge(Vertex v, Vertex w) { insertEdge(v,w,null); }
	
	/*
	 * inserts edge with some data into a graph
	 */
	public void insertEdge(Vertex v, Vertex w, Object object) { 
		Edge e = new Edge(w,object); 
		Edge f = new Edge(v,object);
		v.edges.insert(e);
		w.edges.insert(f);
		e.setTwin(f);
		f.setTwin(e);
	}
	
	public void insertEdge(int i, int j, Object object) { 
		insertEdge(findVertex(i), findVertex(j), object);
	}
	
	public void deleteEdge(Vertex u, Vertex v) {
		u.edges.deleteIndex(findEdgeIndex(u,v));
		v.edges.deleteIndex(findEdgeIndex(v,u));
	}
	
	/*
	 * returns number of vertices
	 */
	public int getNrVertices() { return vertices.getSize(); }

	/*
	 * returns number of arcs in the graph
	 */
	public int getNrArcs() {
		int size = vertices.getSize();
		int count = 0;
		for (int i = 0; i < size; i++) count = count + (vertices.get(i)).edges.getSize();
		return count;
	}
	
	/*
	 * displays graph info; vertices and edges must represent integer objects
	 */
	public void toConsole() {
		Vertex u, v;
		Edge uv;
		int size = vertices.getSize();
		System.out.println("The graph has " + vertices.getSize() + " vertices and " + getNrArcs() + " edges.");
		for (int i = 0; i < size; i++) {
			u = vertices.get(i);
			System.out.print(" from vertex " + (Integer)u.object + " to: ");
			for (int j = 0; j < u.edges.getSize(); j++) {
				uv = u.edges.get(j);
				v = uv.to();
				System.out.print((Integer)v.object + ":" + (Integer)uv.object +  ", ");			
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		Graph G = new Graph(10, 0.5, 1, 10);
		G.toConsole();
		BreadthFirstEdgeIterator iter = new BreadthFirstEdgeIterator(G, G.getVertex(0));
//		DepthFirstEdgeIterator iterDepth = new DepthFirstEdgeIterator(G, G.getVertex(0));
		while (iter.hasNext()) iter.next();
		iter.printDepth();
	}
}

