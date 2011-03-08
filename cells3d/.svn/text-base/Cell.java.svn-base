package cells3d;
import geom3d.*;

import javax.vecmath.Color3f;
import javax.media.j3d.*;

public class Cell {
	private Vertex vertex[] = new Vertex[4];
	private Cell adjCell [] = new Cell[4];
	protected boolean toBeInspectedFlag = false;
	protected int cellID;
	Shape3D tetrahedron;
	LineArray lineArray = new LineArray(12, LineArray.COORDINATES | LineArray.COLOR_3);; 
	 Point3d[] coords = new Point3d[12];
	 Color3f[] colors = new Color3f[12];

	TriangleArray triangleArray;
	BranchGroup bgTetrahedron;
	
	public Cell(Vertex c0, Vertex c1, Vertex c2, Vertex c3) {
		vertex[0] = c0;
		vertex[1] = c1;
		vertex[2] = c2;
		vertex[3] = c3;
	}

	public Cell(Vertex c0, Vertex c1, Vertex c2, Vertex c3, int ID, boolean flag) {
		vertex[0] = c0;
		vertex[1] = c1;
		vertex[2] = c2;
		vertex[3] = c3;
		cellID = ID;
		toBeInspectedFlag = flag;

	}
	
	public Cell(Vertex[] v,  int ID, boolean flag) {
		vertex = v;
		cellID = ID;
		toBeInspectedFlag = flag;
	}
	
/*	
	private void setUpLineArray() {
		
		 lineArray = new LineArray(12, LineArray.COORDINATES | LineArray.COLOR_3);
//		 Point3d[] coords = new Point3d[12];
		 coords[0] = coords[2] = coords[4] = getPoint(0); 
		 coords[1] = coords[6] = coords[8] = getPoint(1); 
		 coords[3] = coords[7] = coords[10]= getPoint(2); 
		 coords[5] = coords[9] = coords[11]= getPoint(3);
		 System.out.println(coords);
		 lineArray.setCapability(LineAttributes.ALLOW_PATTERN_READ);
		 lineArray.setCapability(LineAttributes.ALLOW_PATTERN_WRITE);
		 lineArray.setCapability(LineAttributes.ALLOW_WIDTH_READ);
		 lineArray.setCapability(LineAttributes.ALLOW_WIDTH_WRITE);
		 lineArray.setCapability(GeometryArray.ALLOW_COLOR_READ);
		 lineArray.setCapability(GeometryArray.ALLOW_COLOR_WRITE);
		 
		 lineArray.setCoordinates(0, coords);
//		 Color3f[] colors = new Color3f[12];
		 for (int i = 0; i < 12; i++) colors[i] = Colors.black;
		 lineArray.setColors(0, colors);
	}
*/	
/*
	public void clear() {
		for (int i = 0; i < 4; i++) { 
			vertex[i] = null;
			adjCell[i] = null;
			cellID = -1;
		}
		tetrahedron = null;
		lineArray = null;
		triangleArray = null;
		bgTetrahedron.detach();
	}
*/	
	public void clear(Cells3d cells) {
		Vertex v;
System.out.println("Clearing cell " + cellID);
		for (int i = 0; i < 4; i++) { 
			v = vertex[i];
			vertex[i] = null;
			
			if (v.getFirstCell() == this) v.setFirstCell(cells.findCell(v));
			adjCell[i] = null;
		}
		cellID = -1;
		toBeInspectedFlag = false;
		bgTetrahedron.detach();
		cells.getViewer().setSelectedCell(null);
		cells.getViewer().setSelectedAdjCell(null);
		cells.getCells().delete(this);
	}
	
	public Vertex getVertex(int i) { return vertex[i]; }
	public int getVertexID(int i) { return vertex[i].getVertexID(); }
	public Vertex[] getVertices() { return vertex; }
	public Point3d getPoint(int i) { return getVertex(i).getPoint(); }
	public Point3d[] getPoints() {
		Point3d[] p = new Point3d[4];
		for (int i = 0; i < 4; i++) p[i] = vertex[i].getPoint();
		System.out.println(p[0].toString(3));
		return p;
	}
	public Cell getAdjCell(int i) { return adjCell[i]; }
	public Cell getAdjCell(Vertex v) { return getAdjCell(getIndex(v)); }
	public int getCellID() { return cellID; }
	public int getAdjCellID(int indx) { return getAdjCell(indx).getCellID(); }	
	public boolean getToBeInspectedFlag() { return toBeInspectedFlag; }
	public Shape3D getTetrahedron() { return tetrahedron; }
	public LineArray getLineArray() { return lineArray; }
	public BranchGroup getBgTetrahedron() { return bgTetrahedron; }
	
	public void setVertex(int i, Vertex v) { vertex[i] = v; }
	public void setAdjCell(int i, Cell cell) { adjCell[i] = cell; }
	public void setToBeInspectedFlag(boolean flag) { toBeInspectedFlag = flag; }
	public void setCellID(int cellID) { this.cellID = cellID; }
	public void setTetrahedron(Shape3D tetrahedron) {this.tetrahedron = tetrahedron; }
	public void setLineArray(LineArray lineArray) { this.lineArray = lineArray; } 
	public void setTriangleArray(TriangleArray triangleArray) { this.triangleArray = triangleArray; }
	public void setBgTetrahedron(BranchGroup bgTetrahedron) { this.bgTetrahedron = bgTetrahedron; }

	/*
	 * returns TRUE if the specified vertex is a vertex of this cell
	 */
	public boolean hasVertex(Vertex v)  {
		for (int i = 0; i < 4; i++) if (getVertex(i) == v) return true; 
		return false;
	}
	
	/*
	 * returns TRUE if the specified vertices are in this cell
	 */
	public boolean hasVertices(Vertex v0, Vertex v1, Vertex v2, Vertex v3) {
		return hasVertex(v0) && hasVertex(v1) && hasVertex(v2) && hasVertex(v3);
	}

	
	/*
	 * returns TRUE if the specified point p is inside this cell
	 */
	public boolean isInside(Point3d p) {
		return Point3d.isInside(p,getPoint(0), getPoint(1), getPoint(2), getPoint(3));
	}
	
	public boolean isFaceSharing(Cell adjCell) {
		int i = 0; 
		while ((i < 4) && (getAdjCell(i) != adjCell)) i++;
		return i < 4;
	}
	
	public boolean isBoundaryEdge(Vertex y, Vertex z, Vertex x, Vertex v, Point3d p) {
//		System.out.println("y=" + y.getVertexID() + " z=" + z.getVertexID() + " x=" + x.getVertexID() + " v=" +v.getVertexID());
		Cell adjCell = getAdjCell(x);
//		System.out.println("AdjCell = " + adjCell.toString());
		Point3d[] face = adjCell.getFacePoints(0);
//		System.out.println("Face: " + face[0] + " " + face[1] + " " + face[2]);
		return Point3d.isBehind(p, face[0], face[1], face[2]); 
	}

	/*
	 * returns true if this cell and the adjacent cell form a convex polyhedron
	 */
	public boolean isConvex(int i) {
		int j = 0;
		Cell adjCell = getAdjCell(i);
		while (hasVertex(adjCell.getVertex(j))) j++;
//		System.out.println("j = " + j);
		Point3d[] face = getFacePoints(i);
		return Point3d.isInCone(getPoint(i), face, adjCell.getPoint(j));	
	}
	
	
	public boolean isInfiniteCell() { return getPoint(0) == null; }
		
	public int getIndex(Vertex v) {
		for (int i = 0; i < 4; i++) if (vertex[i] == v) return i;
		return -1;
	}
	
	/*
	 * returns i-th point on the face opposite to vertex v[q];
	 */
	public Point3d getPoint(int q, int i) { return getPoint((q+i)%4); }
	
	public int getCellIndex(Cell cell) {
		for (int i = 0; i < 4; i++) if (adjCell[i] == cell) return i;
		return -1;
	}
	
	/*
	 * returns the missing index of the cell 
	 */
	public int getFourthIndex(int x, int y, int z) {
		for (int i = 0; i < 4; i++) {
			if ((i !=x) && (i != y) && (i != z)) return i;
		}
		return -1;
	}
	
	/*
	 * returns the fourth vertex of the cell
	 */
	public Vertex getFourthVertex(Vertex x, Vertex y, Vertex z) {
		for (int i = 0; i < 4; i++) {
			Vertex v = vertex[i];
			if ((v != x) && (v != y) && (v != z)) return v;
		}
		return null;
	}
	
	public Vertex getFourthVertex(Vertex[] vertex) {
		return getFourthVertex(vertex[0], vertex[1], vertex[2]);
	}

/*
 * returns face opposite to the specified vertex i (counterclockwise)
 */
	public Point3d[] getFacePoints(int i) {
		Vertex[] vertex = getFaceVertices(i);
		Point3d[] face = new Point3d[3];
		for (int j = 0; j < 3; j++) face[j] = vertex[j].getPoint();
		return face;
	}
	
	public Vertex[] getFaceVertices(int i)
	{
		boolean swap; 
		Vertex face[] = new Vertex[3];
		int j = 0;
		for (int k = 0; k < 4; k++) if (k != i) face[j++] = getVertex(k);
		if (getPoint(i) == null) { // infinityVertex
			int k = adjCell[i].getCellIndex(this);
			swap = Point3d.isBehind(adjCell[i].getPoint(k),face[0].getPoint(), face[1].getPoint(), face[2].getPoint());
		}
		else swap = !Point3d.isBehind(getPoint(i), face[0].getPoint(), face[1].getPoint(), face[2].getPoint());
		if (swap) {
			Vertex tmp = face[0];
			face[0] = face[1];
			face[1] = tmp;			
		}
		return face;
	}
	
	public Point3d[] getFace(Vertex v) { return getFacePoints(getIndex(v)); }
	
	public Point3d[] getCommonFace(Cell adjCell) {
		int i = 0; 
		while ((i < 4) && (getAdjCell(i) != adjCell)) i++;
		if (i < 4) return getFacePoints(i); else return null;
	}
	
	public Vertex[] getCommonEdge(Cell adjCell, Cell thirdCell, Vertex[] faceVertices) {
		Vertex w; Vertex u;
		Vertex v[] = new Vertex[3];
		for (int i = 0; i < 2; i++) {
			w = faceVertices[i];
			if (adjCell.hasVertex(w) && thirdCell.hasVertex(w)) {
				for (int j = i+1; j < 3; j++) {
					u = faceVertices[j];
					if (adjCell.hasVertex(u) && thirdCell.hasVertex(u)) {
						v[0] = w;
						v[1] = u;
					}
				}
			}
			else v[2] = w;
		}
		return v;
	}
	
	public static Cell getCommonAdjCell(Cell cell1, Cell cell2) {
		Cell cell; 
		Cell commonCell = null;
		int i = 0;
		while ((i < 4) && (commonCell == null)) {
			cell = cell1.getAdjCell(i);
			if (cell.getCellID() != -1) {
				int j = 0;
				while ((j < 4) && (commonCell == null)) {
					if (cell == cell2.getAdjCell(j)) commonCell = cell; else j++;
				}
			}
			i++;
		}
		return commonCell;
	}
	/*
	 * makes the specified cell and this cell adjacent if they share a face
	 */
	public void makeAdjacent(Cell cell) {
		Vertex vA, vB;
		int i = 0;
		while (i < 4) {
			vA = getVertex(i);
			if (!cell.hasVertex(vA)) break; else i++;
		}
//		System.out.println("i is " + i);
		if (i != 4) {
			int j = 0;
			while (j < 4) {
				vB = cell.getVertex(j);
				if (!hasVertex(vB)) break; else j++;
			}
//			System.out.println("j is " + j);
			if (j != 4) {
				setAdjCell(i,cell);
				cell.setAdjCell(j,this);
			}
		}
	}
	
	static public void makeAllAdjacent(Cell c0, Cell c1, Cell c2, Cell c3) {
		c0.makeAdjacent(c1);
		c0.makeAdjacent(c2);
		c0.makeAdjacent(c3);
		c1.makeAdjacent(c2);
		c1.makeAdjacent(c3);
		c2.makeAdjacent(c3);
	}
	
	static public void makeAllAdjacent(Cell c0, Cell c1, Cell c2, Cell c3, Cell c4) {
		c0.makeAdjacent(c1);
		c0.makeAdjacent(c2);
		c0.makeAdjacent(c3);
		c0.makeAdjacent(c4);
		c1.makeAdjacent(c2);
		c1.makeAdjacent(c3);
		c1.makeAdjacent(c4);
		c2.makeAdjacent(c3);
		c2.makeAdjacent(c4);
		c3.makeAdjacent(c4);
	}

	public int findAdjCellIndex(Cell cell) {
		for (int i = 0; i < 4; i++) {
			if (adjCell[i] == cell) return i;
		}
		return -1;
	}
	
	public void changeAppearance(int lineWidth, int linePattern, Color3f color) {
		//Shape3D tetrahedron = getTetrahedron();
		Appearance ap = getTetrahedron().getAppearance();

		LineAttributes la = new LineAttributes();
		la.setLinePattern(LineAttributes.PATTERN_SOLID);
		la.setLineWidth(lineWidth);
		ap.setLineAttributes(la);
		
		 Color3f[] colors = new Color3f[12];
		 for (int i = 0; i < 12; i++) colors[i] = color;
		 lineArray.setColors(0, colors);

		getTetrahedron().setAppearance(ap);
	}
	
	public String toString() { return toString(" "); }
	public String toString(String offset) {
		String str = offset + "Cell " + cellID + ": [" + vertex[0].getVertexID() + "," +
				vertex[1].getVertexID() + "," + vertex[2].getVertexID() + "," +
				vertex[3].getVertexID() + "]";
		return str;
	}
	public String toStringFace(int id) { return toStringFace(" ", id); }
	public String toStringFace(String offset, int id) {
		String str = offset + "Face " + id + " of cell " + cellID + ": [";
		int k = 0;
		for (int i = 0; i < 4; i++)  {
			if (i != id) { 
				str = str + vertex[i].getVertexID();
				k++;
				if (k < 3) str = str + ","; else str = str + "]";
			}
		}
		return str;
	}
	
	
}

