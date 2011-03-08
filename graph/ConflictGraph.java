package graph;

import geom3d.*;
import dcel3d.Face;
import dcel3d.HalfEdge;

public class ConflictGraph extends BipartiteGraph {
	
	public Vertex getVertexA(int i) { return (Vertex)getVerticesA().get(i); }
	
	public void update(Vertex vA, dcel3d.Vertex v) {
		Face face;
		Vertex vB, vC;
		Point3d pC;
		int sizeA = getVerticesA().getSize();
		HalfEdge e = v.getHalfEdge();
		do {
			face = e.getFace();
			System.out.println("adding new face to conflict graph: " + face.toString());
			vB = insertVertexB(e.getFace());
			for (int j = 0; j < sizeA; j++) {
				vC = (Vertex)getVerticesA().get(j);
				if (vC != vA) {
					pC = (Point3d)vC.getObject(); 
					System.out.println("testing if " + pC.toString(2) + " can see the face");
					if (face.isBehind(pC)) {
						insertEdge(vB,vC);
						System.out.println("edge added to the conflict graph");
					}
				}
			}
			e = e.getNext();
		} while (e != v.getHalfEdge());
		
		int degreeA = vA.getEdges().getSize();
		for (int i = 0; i < degreeA; i++) { 
			Edge eAB = (Edge)vA.getEdges().get(0);
			vB = eAB.to();
			deleteB(vB); 
		}
		deleteA(vA);
	}
	
	
	public void toConsole() { toConsole(""); }
	public void toConsole(String str) {
		Vertex vA, vB;
		Point3d pA;
		Edge e;
		int degree;
		int size = getVerticesA().getSize();
		System.out.println(str);
		System.out.println("Conflict graph has " + size + " points:");
		for (int i = 0; i < size; i++) {
			vA = (Vertex)getVerticesA().get(i);
			degree = vA.getDegree();
			pA = (Point3d)vA.getObject();
			System.out.print(" Point " + i + " : " + pA.toString(2) + " has degree " + degree + ": ");
			for (int j = 0; j < degree; j++) {
				e = (Edge)vA.getEdges().get(j);
				System.out.print(getVerticesB().findIndex(e.to()) + ", ");
			}
			System.out.println();
		}
		
		size = getVerticesB().getSize();
		System.out.println("Conflict graph has " + size + " faces:");

		for (int j = 0; j < size; j++) {
			vB = (Vertex)getVerticesB().get(j);
			degree = vB.getDegree();
			System.out.print(" Face: " + j + ": " + ((Face)vB.getObject()).toString() + " has degree " + degree + ": ");
			for (int i = 0; i < degree; i++) {
				e = (Edge)vB.getEdges().get(i);
				System.out.print(getVerticesA().findIndex(e.to()) + ", ");
			}
			System.out.println();
		}
		System.out.println();
	}

}
