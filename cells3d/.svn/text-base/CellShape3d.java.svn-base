package cells3d;
import javax.media.j3d.*;
import javax.vecmath.Color3f;

public class CellShape3d extends IndexedTriangleArray {
	
	public CellShape3d(Cells3d cells, int maxNrCells) {
		super(4*maxNrCells, GeometryArray.COORDINATES | GeometryArray.COLOR_3,12*maxNrCells);
		int maxNrVertices = 4*maxNrCells;
		int nrCells = cells.getNrCells();
		System.out.println("Number of cells is: " + nrCells);
		
		int[] coords = new int[12*maxNrCells];
		int nrVertices = cells.getNrVertices();
		double[] coordinates = new double[3*maxNrVertices];
		Color3f[] colors = new Color3f[maxNrVertices];
		for (int i = 0; i < nrVertices; i++) {
			Vertex v = cells.getVertex(i);
			geom3d.Point3d p = v.getPoint();
			coordinates[3*i] = p.getX();
			coordinates[3*i+1] = p.getY();
			coordinates[3*i+2] = p.getZ();
			colors[i] = new Color3f(1,0,0);
		}
		for (int i = nrVertices; i < maxNrVertices; i++) {
			coordinates[3*i] = 0.0;
			coordinates[3*i+1] = 0.0;
			coordinates[3*i+2] = 0.0;
			colors[i] = new Color3f(1,0,0);
		}

		setCoordinates(0,coordinates);
		setColors(0,colors);
		
		Cell cell;
		int j = 0;
		for (int i = 0; i < nrCells; i++) {
			cell = cells.getCell(i);
			coords[j]   = cell.getVertexID(1);
			coords[j+1] = cell.getVertexID(3);
			coords[j+2] = cell.getVertexID(2);
			coords[j+3] = cell.getVertexID(0);
			coords[j+4] = cell.getVertexID(2);
			coords[j+5] = cell.getVertexID(3);
			coords[j+6] = cell.getVertexID(0);
			coords[j+7] = cell.getVertexID(3);
			coords[j+8] = cell.getVertexID(1);
			coords[j+9] = cell.getVertexID(0);
			coords[j+10]= cell.getVertexID(1);
			coords[j+11]= cell.getVertexID(2);
			for (int k = j; k < j+12; k++) System.out.print(coords[k] + " ");
			System.out.println();
			j = j + 12;				
		}
		setCoordinateIndices(0, coords);
	}
}

