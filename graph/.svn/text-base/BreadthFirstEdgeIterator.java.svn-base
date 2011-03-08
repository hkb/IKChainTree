package graph;

import java.util.HashMap;

import dataStructures.Queue;

/*
 * iterates through all edges of the graph in the breadth-first fashion
 */

public class BreadthFirstEdgeIterator implements java.util.Iterator<Edge> {

	Graph G;
	HashMap<Vertex,Integer> hash = new HashMap<Vertex,Integer>();
	Queue queue = new Queue();
	Vertex w;
	Edge edge;
	
	public BreadthFirstEdgeIterator(Graph G, Vertex v) {
		this.G = G;
		for (int i = 0; i < G.getNrVertices(); i++) hash.put(G.getVertex(i), -1);
		hash.put(v, 0);
		queue.pushSet(v.getEdges());
	}
	public boolean hasNext() { return !queue.isEmpty(); }
	
	public Edge next() {
		edge = (Edge)queue.pop();
		w = edge.to();
		if (hash.get(w) == -1) {
			hash.put(w, (hash.get(edge.getTwin().to())+1));
			queue.pushSet(w.getEdges());
		}	
		return edge;
	}

	public void remove() {}
	
	public void printDepth() {
		for (int i = 0; i < G.getNrVertices(); i++) System.out.println("Vertex " + i + " is " + hash.get(G.getVertex(i)) + " from the root");
	}
}
