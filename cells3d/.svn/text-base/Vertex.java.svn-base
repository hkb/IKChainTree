package cells3d;
import com.sun.j3d.utils.geometry.Sphere;

public class Vertex {
	private geom3d.Point3d p;
	private Cell firstCell;
	private int vertexID;
	private Sphere sphere;
	
	public Vertex() { 
		p = null;
		firstCell = null;
		vertexID = -1;
	}
	
	public Vertex(geom3d.Point3d p, int ID) {
		this.p = p;
		firstCell = null;
		vertexID = ID;
	}
	
	public geom3d.Point3d getPoint() { return p; }
	public Cell getFirstCell() { return firstCell; }
	public int getVertexID() { return vertexID; }
	public Sphere getSphere() { return sphere; }
	public void setSphere(Sphere sphere) { this.sphere = sphere; }
	public void setFirstCell(Cell cell) { firstCell = cell; }
}

