package graph;

/*
 * Edge class contains edge data as well as two vertices of the edge.
 */
public class Edge {
	protected Object object;
	protected Vertex s;
	protected Vertex t;
	protected Edge twin;

	public Edge(Vertex t, Object object) {
		this.s = null;
		this.t = t;
		this.object = object;
		twin = null;
	}
	
	public Edge getTwin() { return twin; }
	
	public void setTwin(Edge twin) {
		this.twin = twin;
	}
	
	public Vertex to() { return t; }
	public Vertex from() { return s; }
}

