package geom3d;

import dataStructures.Stack;
import dataStructures.Set;
import gui3d.ComplexViewer;

import com.sun.j3d.utils.applet.MainFrame;

public class Delaunay3d {
	protected PointSet3d points;
	private Set[] simplicies;
	private Stack stack = new Stack();
	protected ComplexViewer complexViewer;

	/*
	 * create a vertex for each point
	 * add vertices to the cell complex
	 * 
	 * find a face on the boundary
	 * its twin face is placed on a stack
	 * while stack is not empty
	 *     pop a face from the stack
	 *     if the popped face is in a tetrahedron, ignore it
	 *     otherwise
	 *        find a tetrahedron with this face and empty circumscribing circle
	 *            if such tetrahedron does not exists, then the face is interior, do nothing
	 *            otherwise
	 *               add the tetrahedron to cells
	 *               for each of its other three faces
	 *                   if the twin face is in some tetrahedron, update adjacency
	 *                   otherwise
	 *                      make the twin face adjacent to the infinite face
	 *                      place the twin face on the stack
	 */
	
	public Delaunay3d() {
		points = new PointSet3d(0);
		setSimplicies(new Set[4]);
		for (int i = 0 ; i < 4; i++) getSimplicies()[i] = new Set(0);		
	}
	
	public PointSet3d getPoints() { 
		return points; 
	}
	
	public Set getSimplicies(int i) { 
		return simplicies[i]; 
	}

	public void setSimplicies(Set[] simplicies) {
		this.simplicies = simplicies;
	}

	public Set[] getSimplicies() {
		return simplicies;
	}

	public void setStack(Stack stack) {
		this.stack = stack;
	}

	public Stack getStack() {
		return stack;
	}

	 
	/*
	 * finds the triangle on the convex hull
	 */
	public Simplex first3Simplex() {
		int[] exteriorTriangle = points.getConvexHullTriangle();  
		Simplex facet = new Simplex(exteriorTriangle);
		getStack().push(facet);             															   // System.out.println("First triangle pushed on stack"); facet.toConsole(3);
		return facet;
	}
	
	/*
	 * returns exposed tetrahedron of a triangle taken from the stack (if any)
	 */
	public Simplex next4Simplex() {                                                                  System.out.println("Next triangle to be popped from the stack.");
		Simplex simplex = null;
		if (!getStack().isEmpty()) {
			Simplex facet = (Simplex)getStack().pop();             									    facet.toConsole(3);
			simplex = getSimplex(facet.getPoints()[0], facet.getPoints()[1], facet.getPoints()[2]);
			if (simplex != null) simplex = null;
			else {
				int[] pts = points.getExposedTetrahedron(facet.getPoints()[0],facet.getPoints()[1],facet.getPoints()[2]);    if (pts == null) System.out.println("Did not find exposed tetrahedron");
				if (pts != null) {
					simplex = new Simplex(pts);            										    System.out.print("Tetrahedron added to DT: "); simplex.toConsole(3);
					insert(simplex);                       										    System.out.println("New simplex inserted.");
					for (int i = 0; i < 4; i++) {     											    System.out.println("Looking for simplex [" + simplex.facets[i].getPoints()[0] + "," + simplex.facets[i].getPoints()[2] + "," + simplex.facets[i].getPoints()[1] + "]");
						Simplex simplex2 = getSimplex(simplex.facets[i].getPoints()[0],simplex.facets[i].getPoints()[2],simplex.facets[i].getPoints()[1]);
						if (simplex2 == null) {                													    System.out.println("Simplex not found.");
							simplex2 = new Simplex(simplex.facets[i].getPoints()[0],simplex.facets[i].getPoints()[2],simplex.facets[i].getPoints()[1]); 
							getStack().push(simplex2);    											    System.out.print("New triangle on the stack: "); simplex2.toConsole(3);
						}
						simplex2.twin = simplex.facets[i];
						simplex.facets[i].twin = simplex2;
					}
				}
			}
		}
		return simplex;
	}

	
	public Delaunay3d(PointSet3d points) {
		this.points = points;
		setSimplicies(new Set[4]);
		for (int i = 0 ; i < 4; i++) getSimplicies()[i] = new Set(0);
	
		first3Simplex();
		while (!getStack().isEmpty()) next4Simplex();
	}
	
	public void clear() {
		for (int i = 0; i < 4; i++) getSimplicies()[i].clear();
		points.clear();
		getStack().clear();
	}
	
	public boolean isEmpty() { return getSimplicies()[2].isEmpty(); }
	
	/*
	 * recursively creates and adds non-existing facets to the complex and then adds the simplex to the complex 
	 */
	public void insert(Simplex simplex) {
		if (simplex.dimension > 0) addFacets(simplex);
		getSimplicies()[simplex.dimension].insert(simplex);
	}

	public void addFacets(Simplex simplex) {
		int pts[] = new int[simplex.dimension];
		int tmp;
		for (int i = 0; i <= simplex.dimension; i++) {
			pts = getSubArray(simplex.getPoints(), i);
			if ((simplex.dimension == 3) && 
				!Point3d.isBehind((Point3d)points.get(pts[0]), (Point3d)points.get(pts[1]), 
						          (Point3d)points.get(pts[2]), (Point3d)points.get(simplex.getPoints()[i]))) {
				tmp = pts[1];
				pts[1] = pts[2];
				pts[2] = tmp;
			}
			simplex.facets[i] = getSimplex(pts);
			if (simplex.facets[i] == null) { 
				simplex.facets[i] = new Simplex(pts);
				insert(simplex.facets[i]);
			}
		}
		if (simplex.dimension > 0) 
			for (int i = 0; i <= simplex.dimension; i++) simplex.opposite[i] = getSimplex(simplex.getPoints()[i]);
	}
	

	/*
	 * given k+1 point, 0 <= k <= 3, returns its k-simplex; if k-simplex does not exist, null is returned
	 */
	public Simplex getSimplex(int[] pts) {
		Simplex simplex;
		int dim = pts.length-1;
		int size = getSimplicies()[dim].getSize();
		for (int i = 0; i < size; i++ ) {
			simplex = (Simplex)getSimplicies()[dim].get(i);
			if (simplex.isSimplex(pts)) return simplex;
		}
		return null;
	}
	
	public Simplex getSimplex(int p0, int p1, int p2) {
		int[] pts = { p0, p1, p2 };
		return getSimplex(pts);
	}
	
	public Simplex getSimplex(int p) {
		int[] pts = { p } ;
		return getSimplex(pts);
	}

	private int[] getSubArray(int[] ind, int i) {
		int[] indx = new int[ind.length-1];
		for (int j = 0; j < i; j++) indx[j] = ind[j];
		for (int j = i+1; j < ind.length; j++) indx[j-1] = ind[j];
		return indx;
	}

	public void computeAlphaValues() {
		Simplex simplex;
		Point3d p0, p1, p2;
		int size = getSimplicies()[0].getSize();
		for (int i = 0; i < size; i++) ((Simplex)getSimplicies()[0].get(i)).setAlpha(0);
		size = getSimplicies()[1].getSize();
		for (int i = 0; i < size; i++) {
			simplex = ((Simplex)getSimplicies()[1].get(i));
			p0 = (Point3d)points.get(simplex.getPoints()[0]);
			p1 = (Point3d)points.get(simplex.getPoints()[1]);
			simplex.setAlpha(p0.getDistance(p1)/2);
//			System.out.println("k = 1, i = " + i + ": alpha = " + simplex.alpha);
		}
		size = getSimplicies()[2].getSize();
		for (int i = 0; i < size; i++) {
			simplex = ((Simplex)getSimplicies()[2].get(i));
			p0 = (Point3d)points.get(simplex.getPoints()[0]);
			p1 = (Point3d)points.get(simplex.getPoints()[1]);
			p2 = (Point3d)points.get(simplex.getPoints()[2]);
			simplex.setAlpha(Circle3d.getRadius(p0, p1, p2));
//			System.out.println("k = 2, i = " + i + ": alpha = " + simplex.alpha);
		}
		Sphere3d sphere;
		size = getSimplicies()[3].getSize();
		for (int i = 0; i < size; i++) {
			simplex = ((Simplex)getSimplicies()[3].get(i));
			sphere = new Sphere3d((Point3d)points.get(simplex.getPoints()[0]), (Point3d)points.get(simplex.getPoints()[1]), 
					              (Point3d)points.get(simplex.getPoints()[2]), (Point3d)points.get(simplex.getPoints()[3]));
			simplex.setAlpha(sphere.getRadius());
//			System.out.println("k = 3, i = " + i + ": alpha = " + simplex.alpha);
		}
	}
		
		
	public void setViewer(ComplexViewer complexViewer) {
		this.complexViewer = complexViewer;
	}
	
	public void toConsole(Set set) {
		Simplex simplex;
		int size = set.getSize();
		for (int i = 0; i < size; i++) {
			simplex = (Simplex)set.get(i);
			System.out.println(simplex.toString(3) + ", alpha = " + simplex.alpha);
		}
	}
	
	public static void main(String[] args) {

		int a = 2;
		Delaunay3d delaunay = new Delaunay3d();
		
		ComplexViewer complexViewer = new ComplexViewer(delaunay);
		new MainFrame(complexViewer, 960, 720);

	}

}	

