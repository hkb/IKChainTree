package graph;

import dataStructures.Set;
/*
 * Vertex class contains vertex data and a list of incident edges
 */
public class Vertex {
	protected Object object;
	protected Set<Edge> edges;

	public Vertex(Object object) {
		this.object = object;
		edges = new Set<Edge>();
	}
	
	public Object getObject() { return object; }
	
	public int getDegree() { return edges.getSize(); }
	
	public Set<Edge> getEdges() { return edges; } 
	
	public Vertex getAdjacentVertex(int i) { return (edges.get(i)).to(); }
	
	public Set<Vertex> getAdjacentVertices() {
		Set<Vertex> adjVertices = new Set<Vertex>();
		for (int i = 0; i < edges.getSize(); i++) adjVertices.insert(edges.get(i).to());
		return adjVertices;
	}
	
	public boolean isIsolated() { return edges.isEmpty(); }
	
	public boolean hasOneEdge() { return edges.getSize() == 1; }
}

