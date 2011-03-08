package graph;

import java.util.HashMap;

import dataStructures.Stack;

/*
 * iterates through all edges of the graph in the depth-first fashion
 */

public class DepthFirstEdgeIterator implements java.util.Iterator<Edge> {
	
	Graph G;
	HashMap<Vertex,Integer> hash = new HashMap<Vertex,Integer>();
	Stack stack = new Stack();
	Vertex w;
	Edge edge;
	
	public DepthFirstEdgeIterator(Graph G, Vertex v) {
		this.G = G;
		for (int i = 0; i < G.getNrVertices(); i++) hash.put(G.getVertex(i), -1);
		hash.put(v, 0);
		stack.pushSet(v.getEdges());
	}
		
	public boolean hasNext() { return !stack.isEmpty(); }
		
	public Edge next() {
		edge = (Edge)stack.pop();
		w = edge.to();
		if (hash.get(w) == -1) {
			hash.put(w, (hash.get(edge.getTwin().to())+1));
			stack.pushSet(w.getEdges());
		}	
		return edge;
	}

	public void remove() {}
		
	}

