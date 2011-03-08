package geom3d;

import com.sun.j3d.utils.geometry.Sphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TriangleArray;

public class Simplex {
	protected int dimension;      // dimension of the simplex: 0 - point, 1 - segment, 2 - triangle, 3 - tetrahedron.
	private int[] points;       // corners of the simplex.
	protected Simplex[] facets;   // facets of the simplex opposite of the i-th point.
	protected Simplex[] opposite; // 0-simplex opposite to the i-th facet - not relevant for 0-simplicies.
	protected Simplex twin;       // simplices with the same points but with opposite orientation - only relevant for 2-simplicies.
	protected Delaunay3d complex;
	double alpha;
	
	private Shape3D tetrahedron;
	private Shape3D triangle;
	private Sphere sphere;
	private TriangleArray triangleArray;
	private BranchGroup bgTetrahedron;

	
	public Simplex(int[] points) {
		dimension = points.length - 1;
		this.setPoints(new int[dimension+1]);
		for (int i = 0; i <= dimension; i++) this.getPoints()[i] = points[i];
	    facets = new Simplex[4];
	    opposite = new Simplex[4];
	    System.out.println("New simplex: " + toString(3));
	}
	
	public Simplex(int p) {
		dimension = 0;
		setPoints(new int[1]);
		getPoints()[0] = p;
	}
	
	public Simplex(int p0, int p1, int p2) {
		dimension = 2;
		setPoints(new int[3]);
		getPoints()[0] = p0; 
		getPoints()[1] = p1;
		getPoints()[2] = p2;
	}
	
	public double getAlpha() { return alpha; }
	public Shape3D getTetrahedron() { return tetrahedron; }
	
	public void setAlpha(double alpha) { this.alpha = alpha; }
	
	public void clear() {
		for (int i = 0; i <= dimension; i++) {
			facets[i] = null;
			opposite[i] = null;
			twin = null;
		}
		
	}
	
	public boolean isSimplex(int[] pts) {
		int size = pts.length;
		if (size < 4) return isSimplex(pts, size);
		else {
			boolean cont = true, found;
			int i = 0, j;
			while (cont && (i < 4)) {
				 j = 0;
				 found = false;
				 while (!found && (j < 4)) found = (getPoints()[i] == pts[j++]);
				 cont = found;
				 i++;
			}
			return cont;
		}
	}
	
	public boolean isSimplex(int[] pts, int k) {
		boolean cont = true, same = true;
		int i = 0, j;
		while (cont && (i < k)) {
			j = 0;
			same = true;
			while (same && (j < k)) {
				same = getPoints()[j] == pts[(i+j)%k];
				if (same) j++; else j = k;
			}
			cont = !same;
			i++;
		}
		return same;
	}
	
	public String toString(int dec) {
		String str = "[";
		for (int i = 0; i < dimension; i++) str = str + getPoints()[i] + ",";
		return str + getPoints()[dimension] + "]";
	}
	
	public void toConsole(int dec) { System.out.println(toString(dec)); }

	public void setSphere(Sphere sphere) {
		this.sphere = sphere;
	}

	public Sphere getSphere() {
		return sphere;
	}

	public void setPoints(int[] points) {
		this.points = points;
	}

	public int[] getPoints() {
		return points;
	}

	public void setTetrahedron(Shape3D tetrahedron) {
		this.tetrahedron = tetrahedron;
	}

	public void setTriangleArray(TriangleArray triangleArray) {
		this.triangleArray = triangleArray;
	}

	public TriangleArray getTriangleArray() {
		return triangleArray;
	}

	public void setBgTetrahedron(BranchGroup bgTetrahedron) {
		this.bgTetrahedron = bgTetrahedron;
	}

	public BranchGroup getBgTetrahedron() {
		return bgTetrahedron;
	}

	public void setTriangle(Shape3D triangle) {
		this.triangle = triangle;
	}

	public Shape3D getTriangle() {
		return triangle;
	}
}
