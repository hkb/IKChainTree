package graph;

import dataStructures.*;

/*
 * iterates through all edges of the graph
 */

public class EdgeIterator implements java.util.Iterator<Edge> {
	private SetIterator vertexIterator = null;
	private Vertex currentVertex;
	private SetIterator currentEdgeIterator = null;

	public EdgeIterator(Graph G) {
		vertexIterator = new SetIterator(G.vertices);
		if (G.vertices.isEmpty()) currentVertex = null;
		else {
			currentVertex = (Vertex)vertexIterator.next();
			if (currentVertex != null) currentEdgeIterator = new SetIterator(currentVertex.getEdges());
		}
	}
	public boolean hasNext() { 
		if (currentEdgeIterator.hasNext()) return true; 
		else {
			if (vertexIterator.hasNext()) {
				currentVertex = (Vertex)vertexIterator.next();
				currentEdgeIterator = new SetIterator(currentVertex.getEdges());
				return currentEdgeIterator.hasNext();
			} 
			else return false;
		}
	}
	public Edge next() { return (Edge)currentEdgeIterator.next(); }
	public void remove() {}
}

