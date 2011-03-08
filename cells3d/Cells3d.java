
package cells3d;
import dataStructures.Set;
import misc.*;

import geom3d.*;
import dataStructures.Stack;
import com.sun.j3d.utils.applet.MainFrame;

/*
 * 3d polytopes are represented by two sets:
 * - vertices
 * - cells
 */

public class Cells3d {
	private int nrVertices = 0;
	private int nrSpannedVertices = 0;
	private int freeID = 0;
	protected Set cells = new Set(0);
	protected Set vertices = new Set(0);
	private Vertex infinityVertex = new Vertex();
	private Viewer3d cellsViewer = null;
	
	public Cells3d() {}
	
	public void clear() {
		cells.clear();
		vertices.clear();
		nrVertices = 0;
		freeID = 0;
	}

	
	/*
	 * takes the first four points and creates the first cell
	 */
	public Cell firstCell() {
		Point3d p0 = getPoint(0);
		Point3d p1 = getPoint(1);
		Point3d p2 = getPoint(2);
		Point3d p3 = getPoint(3);
		// check orientation
		Vector3d cr = Vector3d.crossProduct(new Vector3d(p0, p2), new Vector3d(p0, p1));
		boolean ccw = Vector3d.dotProduct(cr, new Vector3d(p0, p3)) > 0.0;

		Vertex v0 = getVertex(0);
		Vertex v1 = ccw? getVertex(1) : getVertex(2);
		Vertex v2 = ccw? getVertex(2) : getVertex(1);
		Vertex v3 = getVertex(3);

		
		Cell cell = new Cell(v0, v1, v2, v3, freeID++, false);
		cells.insert(cell);
		v0.setFirstCell(cell);
		v1.setFirstCell(cell);
		v2.setFirstCell(cell);
		v3.setFirstCell(cell);
		nrSpannedVertices = 4;
		
		Cell adjCell3 = new Cell(infinityVertex, v0, v1, v2, freeID++, false);
		Cell adjCell2 = new Cell(infinityVertex, v0, v3, v1, freeID++, false);
		Cell adjCell1 = new Cell(infinityVertex, v0, v2, v3, freeID++, false);
		Cell adjCell0 = new Cell(infinityVertex, v1, v3, v2, freeID++, false);
		
		Cell.makeAllAdjacent(cell, adjCell0, adjCell1, adjCell2, adjCell3);
		infinityVertex.setFirstCell(adjCell0);
		cellsViewer.setNrSpannedVerticesField(4);
		cellsViewer.setNrEdgesField(6);				 
		cellsViewer.setNrFacesField(4);
		cellsViewer.setNrCellsField(1);
		return cell;
	}
	
	public int getNrVertices() { return vertices.getSize(); }
	public int getNrSpannedVertices() { return nrSpannedVertices; }
	public int getNrCells() { return cells.getSize(); }
	public void setNrVertices(int n) { nrVertices = n; }
	public Vertex getVertex(int i) {return  (Vertex)vertices.get(i); }
	public Point3d getPoint(int i) { return getVertex(i).getPoint(); }
	public Cell getCell(int i) { return (Cell)cells.get(i); }
	public Set getCells() { return cells; }
	public Set getVertices() { return vertices; }
	public Set getPoints() {
		Set points = new Set(nrVertices);
		for (int i = 0; i < nrVertices; i++) points.insert(getPoint(i));
		return points;
	}
	public void insertVertex(Vertex v) { vertices.insert(v); }
	
	public Viewer3d getViewer() { return cellsViewer; }
	public void setViewer(Viewer3d cellsViewer) { this.cellsViewer = cellsViewer; }
	
	/*
	 * adds  new point
	 */
	public Stack nextCell(int i) {
		Vertex v = getVertex(i);
		Point3d p = getPoint(i);
		Cell cell = findCell(p);
		Stack toProcess = (cell != null)? split(cell, v, false) : addExterior(v);
		nrSpannedVertices++;
		cellsViewer.setNrSpannedVerticesField(cellsViewer.getNrSpannedVerticesField() + 1);
		int size = toProcess.getSize();
		cellsViewer.setNrEdgesField(cellsViewer.getNrEdgesField() + size + 2);
		cellsViewer.setNrFacesField(cellsViewer.getNrFacesField() + size * 4);
		cellsViewer.setNrCellsField(cellsViewer.getNrCellsField() + size);
		return toProcess;
	}

	/*
	 * returns the first cell containing specified vertex.
	 */
	public Cell findCell(Vertex v) {
		Cell cell;
		int size = cells.getSize();
		for (int i = 0; i < size; i++) {
			cell = (Cell)cells.get(i);
			if (cell.hasVertex(v)) return cell;
		}
		return null;
	}
	
	public Cell findCell(int cellID) {
		Cell cell;
		int size = cells.getSize();
		for (int i = 0; i < size; i++) {
			cell = (Cell)cells.get(i);
			if (cell.getCellID() == cellID) return cell;
		}
		return null;
	}
	
	/*
	 * returns the cell containing a specified point. null is returned if the point is outside of
	 * all cells.
	 */
	private Cell findCell(Point3d p) {
		Cell cell;
		int size = cells.getSize();
		for (int i = 0; i < size; i++) {
			cell = (Cell)cells.get(i);
			if (cell.isInside(p)) return cell; 
		}
		return null;
	}
	
	/*
	 * returns the cell with specified vertices 
	 */
	public Cell findCell(Vertex v0, Vertex v1, Vertex v2, Vertex v3) {
		Cell cell;
		int size = cells.getSize();
		for (int i = 0; i < size; i++) {
			cell = (Cell)cells.get(i);
			if (cell.hasVertices(v0, v1, v2, v3)) return cell;
		}
		return null;
	}
	
	/*
	 * returns TRUE if the cell with specified vertices exists.
	 */
	public boolean isCell(Vertex v0, Vertex v1, Vertex v2, Vertex v3) {
		return findCell(v0, v1, v2, v3) != null;
	}
	
	/*
	 * performs a 2-3 flip between 2 adjacent cells sharing face v0,v1,v2. Cell cell has
	 * in addition vertex w while cell adjCell has vertex u. Union of these two cells 
	 * must be convex and the circle circumscribing one of the cells must contain fourth
	 * vertex of the other cell.
	 */
	private void flip23(Cell cell, Cell adjCell, Vertex w, Vertex u, Vertex[] faceVertices, Stack toProcess) {
System.out.println("Making 2-3 flip");
		Vertex v0 = faceVertices[0];
		Vertex v1 = faceVertices[1];
		Vertex v2 = faceVertices[2];
		Cell c1 = new Cell(w, u, v0, v1, freeID++, false);
		Cell c2 = new Cell(w, u, v1, v2, freeID++, false);
		Cell c3 = new Cell(w, u, v2, v0, freeID++, false);
		c1.makeAdjacent(c2);
		c1.makeAdjacent(c3);
		c1.makeAdjacent(cell.getAdjCell(v2));
		c1.makeAdjacent(adjCell.getAdjCell(v2));
		c2.makeAdjacent(c3);
		c2.makeAdjacent(cell.getAdjCell(v0));
		c2.makeAdjacent(adjCell.getAdjCell(v0));
		c3.makeAdjacent(cell.getAdjCell(v1));
		c3.makeAdjacent(adjCell.getAdjCell(v1));
		cells.insert(c1);
		cellsViewer.addTetrahedron(c1, Colors.yellow);
		cells.insert(c2);
		cellsViewer.addTetrahedron(c2, Colors.yellow);
		cells.insert(c3);
		cellsViewer.addTetrahedron(c3, Colors.yellow);
		toProcess.push(c1);
		toProcess.push(c2);
		toProcess.push(c3);
		System.out.println("Clearing " + cell.toString());
		cell.clear(this);
		System.out.println("Clearing mate " + adjCell.toString());
		adjCell.clear(this);
	}
	
	/*
	 * performs 3-2 flip between 3 cells that share an edge v0v1 and pairwise share a face.
	 * Union of cell and adjCell is not convex and thirdCells fills the gap making the 
	 * union convex.
	 */
	private void flip32(Cell cell, Cell adjCell, Cell thirdCell, Vertex w, Vertex u, Vertex[] face, Stack toProcess) {
System.out.println("Making 3-2 flip");
		Vertex v0 = face[0];
		Vertex v1 = face[1];
		Vertex v2 = face[2];
		System.out.println("flip32, cell1: " + cell.toString());
		System.out.println("flip32, cell2: " + adjCell.toString());
		System.out.println("flip32, cell3: " + thirdCell.toString());
		System.out.println("flip32, v2: " + v2.getVertexID());

		Cell c1 = new Cell(v0, v2, u, w, freeID++, false);
		Cell c2 = new Cell(v1, v2, w, u, freeID++, false);
		c1.makeAdjacent(c2);
		c1.makeAdjacent(cell.getAdjCell(v1));       System.out.println("flip32: " + cell.getAdjCell(v1));
		c1.makeAdjacent(adjCell.getAdjCell(v1));    System.out.println("flip32: " + adjCell.getAdjCell(v1));
		c1.makeAdjacent(thirdCell.getAdjCell(v1));  System.out.println("flip32: " + thirdCell.getAdjCell(v1));
		c2.makeAdjacent(cell.getAdjCell(v0));       System.out.println("flip32: " + cell.getAdjCell(v0));
		c2.makeAdjacent(adjCell.getAdjCell(v0));    System.out.println("flip32: " + adjCell.getAdjCell(v0));
		c2.makeAdjacent(thirdCell.getAdjCell(v0));  System.out.println("flip32: " + thirdCell.getAdjCell(v0));
		cells.insert(c1);
		cellsViewer.addTetrahedron(c1, Colors.yellow);
		cells.insert(c2);
		cellsViewer.addTetrahedron(c2, Colors.yellow);
		toProcess.push(c1);
		toProcess.push(c2);
		cell.clear(this);
		adjCell.clear(this);
		thirdCell.clear(this);
	}

	
	private Stack split(Cell cell, Vertex w, boolean flag) {
//		System.out.println("Splitting cell " + cell.toString());
		Cell c[] = new  Cell[4];
		Vertex[] v = cell.getVertices();
		c[0] = new Cell(w, v[1], v[2], v[3], freeID++, flag);
		c[1] = new Cell(w, v[0], v[2], v[3], freeID++, flag);
		c[2] = new Cell(w, v[0], v[3], v[1], freeID++, flag);
		c[3] = new Cell(w, v[0], v[1], v[2], freeID++, flag);
		v[0].setFirstCell(c[1]);
		w.setFirstCell(c[0]);


		
		for (int i = 0; i < 4; i++) {
			c[i].makeAdjacent(cell.getAdjCell(i));
			cells.insert(c[i]);
		}
		cell.clear(this);

		Cell.makeAllAdjacent(c[0], c[1], c[2], c[3]);
		
		Stack toProcess = new Stack();
		for (int i = 0; i < 4; i++) {
			cellsViewer.addTetrahedron(c[i], Colors.yellow);
			toProcess.push(c[i]);
		}
		cellsViewer.setNrFacesField(cellsViewer.getNrFacesField() - 4);
		cellsViewer.setNrCellsField(cellsViewer.getNrCellsField() - 1);

		return toProcess;
	}
	
	public void flipAttempt(Stack toProcess) {
		Cell cell = (Cell)toProcess.pop();
		if (cell.cellID != -1) {
			Cell adjCell;
System.out.println("Attempt to flip, cellID: " + cell.getCellID());
System.out.println("Attempt to flip: " + cell.toString());
			Vertex u, w;
			Point3d p;
			Vertex[] faceVertices;
			Point3d[] points = cell.getPoints();
			Sphere3d sphere = new Sphere3d(points[0],points[1],points[2],points[3]);
			Point3d center = sphere.getCenter();
//			cellsViewer.addSphere(center, sphere.getRadius(), Colors.green);
			int i = 0;
			while (i < 4) {
				w = cell.getVertex(i);
				adjCell = cell.getAdjCell(i);
				if (!adjCell.isInfiniteCell()) {
System.out.println("Flip mate " + adjCell.toString() + " through " + cell.toStringFace("", i));
					faceVertices = cell.getFaceVertices(i);
					u = adjCell.getFourthVertex(faceVertices);
					p = u.getPoint();
					if (p.getSquaredDistance(center) < sphere.getSquaredRadius()) {
						if (cell.isConvex(i)) { 
System.out.println("Cells " + cell.getCellID() + " and " + adjCell.getCellID() + " about to be 2-3 flipped.");
							flip23(cell, adjCell, w, u, faceVertices, toProcess);
							i = 3;
toConsole("Cells just after flipping");
						}
						else {
							Cell thirdCell = Cell.getCommonAdjCell(cell, adjCell);
							if (thirdCell != null) {
System.out.println(thirdCell.toString());
								Vertex[] face = cell.getCommonEdge(adjCell, thirdCell, faceVertices);
								if (!thirdCell.isInfiniteCell()) {
System.out.println("Cells " + cell.getCellID() + " and " + adjCell.getCellID() + " about to be 3-2 flipped.");
									flip32(cell, adjCell, thirdCell, w, u, face, toProcess);
									i = 3;
toConsole("Cells just after flipping");
								}
							}
						}
					}
				}
				i++;
			}			
		}
	}
	
	/*
	 * returns infinite cell containing point p 
	 */
	private Cell getFirstVisibleCell(Point3d p) {
		Cell cell, adjCell, inspectedCell;
		Point3d face[];

//		System.out.println("Looking for the first face visible from p (represented by its unique infinite cell)");
		Stack toInspect = new Stack();
		Stack inspected = new Stack();
		cell = infinityVertex.getFirstCell();
		cell.setToBeInspectedFlag(true);
		toInspect.push(cell);
		while (!toInspect.isEmpty()) {
			cell = (Cell)toInspect.pop();
			inspected.push(cell);
//			System.out.println("Inspecting cell: " + cell.toString());
			int indx = cell.getIndex(infinityVertex);
			face = cell.getFacePoints(indx);
//			System.out.println(" Index of infinite vertex is: " + indx);
//			System.out.println(" Face opposite infinite vertex: " + face[0] + " " + face[1] + " " + face[2]);
			if (Point3d.isBehind(p, face[0], face[1], face[2]))  {
//				System.out.println("First infinite cell found: " + cell.toString());
				while (!toInspect.isEmpty()) {
					adjCell = (Cell)toInspect.pop();
					adjCell.setToBeInspectedFlag(false);
				}
			}
			else {
				for (int i = 1; i < 4; i++) {
					adjCell = cell.getAdjCell(i);
					if (!adjCell.getToBeInspectedFlag()) {
						adjCell.setToBeInspectedFlag(true);
						toInspect.push(adjCell);
//						System.out.println("Adjacent infinite cell pushed on stack: " + adjCell.toString());
					}					
				}
			}
		}
		while (!inspected.isEmpty()) {
			inspectedCell = (Cell)inspected.pop();
			inspectedCell.setToBeInspectedFlag(false);
		}
		return cell;
	}
	
	
	private void createVertexCells(Vertex v, Set boundary) {
		Cell cell;
		v.setFirstCell((Cell)boundary.get(0));
		int size = boundary.getSize();
		for (int i = 0; i < size; i++) {
			cell = (Cell)boundary.get(i);
			cell.setVertex(0, v);
			cell.setToBeInspectedFlag(false);
			cells.insert(cell);
			cellsViewer.addTetrahedron(cell, Colors.yellow);
System.out.println(" Infinity vertex replaced by the new vertex: " + cell.toString());
//			Cell adjCell = cell.getAdjCell(0);
//			System.out.println(adjCell.toString());
/*			if (cell.isConvex(0)) {
				sphere = new Sphere3d(v.getPoint(), cell.getPoint(1), cell.getPoint(2), cell.getPoint(3));
				center = sphere.getCenter();
				p = adjCell.getFourthVertex(cell.getVertex(1), cell.getVertex(2), cell.getVertex(3)).getPoint();
				if (p.getSquaredDistance(center) < sphere.getSquaredRadius())
					System.out.println("Cells " + cell.getCellID() + " and " + adjCell.getCellID() + " can be flipped.");
			}
*/		}
	}

	private Stack addExterior(Vertex v) {
System.out.println("Adding exterior vertex " + v.getVertexID());
		Point3d p =(Point3d)v.getPoint();
		Cell cell, adjCell;
		Set visible = new Set(0);
		Set boundary = new Set(0);
		Cell firstBoundaryCell = null;
		int iIndx=-1;
		int jIndx = -1; 
		Vertex xVertex = null;
		Vertex yVertex = null;


		cell = getFirstVisibleCell(p);
		
		Vertex faceVertex[] = new Vertex[3]; // needed for testing
		Point3d face[] = new Point3d[3];
System.out.println(" Looking for all visible faces");
		Stack toInspect = new Stack();
		Stack toProcess = new Stack();
		toInspect.push(cell);
		cell.setToBeInspectedFlag(true);
		while (!toInspect.isEmpty()) {
			cell = (Cell)toInspect.pop();
System.out.println("  Inspecting " + cell.toString());
			for (int i = 1; i < 4; i++) {
				adjCell = cell.getAdjCell(i);
System.out.println("   i = " + i + ": Adjacent " + adjCell);
				if (!adjCell.getToBeInspectedFlag()) {
					face = adjCell.getFacePoints(0);
					faceVertex = adjCell.getFaceVertices(0);
System.out.print("    " + adjCell.toStringFace("", 0));
					if (Point3d.isBehind(p, face[0], face[1], face[2])) {
						adjCell.setToBeInspectedFlag(true);
						toInspect.push(adjCell);
System.out.println(" visible, pushed on stack.");
					}
					else {
						boundary.insert(cell);
						firstBoundaryCell = cell;
						iIndx = (i+1)%4; if (iIndx == 0) iIndx++;
						jIndx = (iIndx+1)%4; if (jIndx == 0) jIndx++;
						xVertex = cell.getVertex(iIndx);
						yVertex = cell.getVertex(jIndx);
System.out.println(" is unvisible, boundary face.");
					}
				}
			}
			visible.insert(cell);
			toProcess.push(cell);
System.out.println(" Visible " + cell.toString());
		}

		createVertexCells(v, visible);
	
		// Find first boundary face
		// make it adjacent to the opposite (invisible) face
		
System.out.println(" Adding new infinite cells");
		
		cell = firstBoundaryCell; 
System.out.println("First cell: " + cell.toString());
		Vertex firstVertex = xVertex;
		Vertex zVertex = cell.getFourthVertex(xVertex, yVertex, v);
		Cell invisibleCell = cell.getAdjCell(zVertex); 
System.out.println("First invisible " + invisibleCell.toString());
		Cell newCell = new Cell(infinityVertex, xVertex, yVertex, v, freeID++, false); 
System.out.println("First new " + newCell.toString());
		infinityVertex.setFirstCell(newCell);
		newCell.makeAdjacent(cell);
		newCell.makeAdjacent(invisibleCell);
		Cell firstNewCell = newCell;
		do {
			Cell prevNewCell = newCell;
			Cell nextCell = cell.getAdjCell(xVertex);
			while (nextCell.hasVertex(v)) {
				xVertex = zVertex;
				zVertex = nextCell.getFourthVertex(zVertex, yVertex, v);
				cell = nextCell;
				nextCell = cell.getAdjCell(xVertex);
			}
			xVertex = yVertex;
			yVertex = zVertex;
			zVertex = cell.getFourthVertex(xVertex, yVertex, v);
			invisibleCell = nextCell;
			newCell = new Cell(infinityVertex, xVertex, yVertex, v, freeID++, false); 
System.out.println("First new " + newCell.toString());
			newCell.makeAdjacent(cell);
			newCell.makeAdjacent(invisibleCell);
			newCell.makeAdjacent(prevNewCell);
		} while (yVertex != firstVertex);
		firstNewCell.makeAdjacent(newCell);
		
		toConsole("Cells just before attempting flipping");
		return toProcess;
//		while (!toProcess.isEmpty()) flipAttempt(toProcess);

	}

	public void toConsole(String str) {
		System.out.println(str);
		int size = cells.getSize();
		for (int i = 0; i < size; i++)  {
			Cell cell = (Cell)cells.get(i);
			System.out.println(cell.toString());
			for (int j = 0; j < 4; j++) System.out.println(cell.getAdjCell(j).toString(" Neighbor cell no. " + j + ": "));
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
/*		int size = 5;
		PointSet3d points = new PointSet3d(0);
		
		points.insert(new Point3d(0.881, 0.758, 0.031));
		points.insert(new Point3d(0.349, 0.946, 0.107));
		points.insert(new Point3d(0.038, 0.541, 0.837));
		points.insert(new Point3d(0.755, 0.409, 0.196));
		points.insert(new Point3d(0.541, 0.691, 0.530));
		for (int i = 0; i < size; i++) 
			System.out.println(((Point3d)points.get(i)).toString(3));

		Cells3d cells = new Cells3d((Point3d)points.get(0), (Point3d)points.get(1), (Point3d)points.get(2), (Point3d)points.get(3));
		cells.toConsole("4 points");
		cells.insert((Point3d)points.get(4));
		cells.toConsole("5 points");		
*/
/*		PointSet3d points = new PointSet3d("/Users/pawel/Documents/pdb1914");
		int size = points.getSize();
*/
/*		int size = 10;
		PointSet3d points = new PointSet3d(size);
		for (int i = 0; i < size; i++) { System.out.println(((Point3d)points.get(i)).toString(3)); }
		Point3d p0 = (Point3d)points.get(0);
		Point3d p1 = (Point3d)points.get(1);
		Point3d p2 = (Point3d)points.get(2);
		Point3d p3 = (Point3d)points.get(3);
		Cells3d cells = new Cells3d(p0, p1, p2, p3);
		for (int i = 4; i < size; i++) cells.insert((Point3d)points.get(i));
*/		
	

//		Point3d p0 = new Point3d(0.0, 0.2, 0.0);
//		Point3d p1 = new Point3d(1.0, 0.0, 0.16);
//		Point3d p2 = new Point3d(0.0, 1.0, 0.11);
//		Point3d p3 = new Point3d(0.01, 0.13, 2.0);
//		Point3d p4 = new Point3d(1.02, 1.03, 1.24);
/*		Point3d p5 = new Point3d(1.05, 0.06, 1.06);
		Point3d p6 = new Point3d(0.06, 1.07, 1.08);
		Point3d p7 = new Point3d(1.09, 1.10, 1.11);
		Point3d p8 = new Point3d(0.512, 0.513, 0.414);
		Point3d p9 = new Point3d(0.317, 0.716, 0.615);
		Point3d p10 = new Point3d(2.317, 1.716, 0.615);
		Point3d p11 = new Point3d(0.318, 2.518, 1.418);
*/		
//		Cells3d cells = new Cells3d(p0, p1, p2, p3);
		Cells3d cells = new Cells3d();
		Viewer3d cellsViewer = new Viewer3d(cells);
//		cells.toConsole("4 points");
//		cellsViewer.addSphere(p0, 0.02);
//		cellsViewer.addSphere(p1, 0.02);
//		cellsViewer.addSphere(p2, 0.02);
//		cellsViewer.addSphere(p3, 0.02);	
//		cellsViewer.addTetrahedron(p0, p1, p2, p3, new Color3f(1,0,0));
//		cellsViewer.addSphere(p4, 0.02);
//		cells.insert(p4);
//		cells.toConsole("5 points");		
//		cells.insert(p5);
//		cells.toConsole("6 points");
//		cells.insert(p6);
//		cells.toConsole("7 points");
//		cells.insert(p7);
//		cells.toConsole("8 points");
//		cells.insert(p8);
//		cells.insert(p9);
//		cells.insert(p10);
//		cells.insert(p11);

		new MainFrame(cellsViewer, 1280, 960);


	}

}
