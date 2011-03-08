package graph;

import dataStructures.Set;

public class BipartiteGraph  extends Graph {
	private Set verticesB = new Set();
	
	public Set getVerticesA() { return vertices; }
	
	public Set getVerticesB() { return verticesB; }
	
	public boolean hasVerticesA() { return !vertices.isEmpty(); }
	public boolean hasVerticesB() { return !verticesB.isEmpty(); }
	
	public boolean isVertexA(Object object) { return getVerticesA().isMember(object); }
	public boolean isVertexB(Object object) { return getVerticesB().isMember(object); } 
	
	/*
	 * returns A-vertex representing specified object 
	 */
	public Vertex findVertexA(Object object) { return findVertex(object); }

	/*
	 * returns B-vertex representing specified object
	 */
	public Vertex findVertexB(Object object) {
		for (int i = 0; i < verticesB.getSize(); i++) {
			Vertex v = (Vertex)verticesB.get(i);
			if (v.getObject() == object) return v;
		}
		return null;
	}

	
	public Vertex insertVertexA(Object object) { 
		Vertex v = new Vertex(object);
		vertices.insert(v);
		return v;
	}
	public Vertex insertVertexB(Object object) { 
		Vertex v = new Vertex(object);
		verticesB.insert(v);
		return v;
	}

	public void deleteIndexA(int i) { deleteIndex(i); }
	
	public void deleteIndexB(int i) {
		Vertex v = (Vertex)verticesB.get(i);
		while (!v.edges.isEmpty()) { 
			Edge e = (Edge)v.edges.getLast();
			deleteEdge(e.to(),v);	
		}
		verticesB.deleteIndex(i);
	}

	public void deleteA(Vertex v) { delete(v); }
	public void deleteB(Vertex v) { 
		int ind = verticesB.findIndex(v); 
		deleteIndexB(ind); 
	}
	
	public void deleteLastA() { deleteIndexA(vertices.getSize()-1); }
	public void deleteLastB() { deleteIndexB(vertices.getSize()-1); }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

