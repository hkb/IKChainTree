package geom3d;

import graph.Edge;
import graph.Graph;
import graph.Vertex;
import j3dScene.J3DScene;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import molecule.Protein;

import sorting.SortToolSegment3d;
import sorting.SorterQuick;

import dataStructures.DS;
import dataStructures.DSNode;
import dataStructures.Set;

import Alphashape.Filtration;
import Delaunay3D.Delaunay3D;

public class SMT3D {

	public SMT3D() {}

	public static void main(String[] args) {
	
//		int n = 200;
//		PointSet3d points = new PointSet3d(n,2);
//		double smtCylinderRadius = 0.01;

		
		Protein protein = new Protein("1Y6DA", 2, true);
		PointSet3d points = protein.getPointSetBackboneShell();
		for (int i = 0; i < points.getSize(); i++) points.get(i).scale(0.05);
		points.toConsole();
		int n = points.getSize();
		double smtCylinderRadius = 0.01;
		PointSet3d extended = new PointSet3d(points);
		
		HashMap<Point3d, Integer> index = new HashMap<Point3d, Integer>(); // relates points to their indexes

		HashMap<Datastructures.Point, Point3d> hash = new HashMap<Datastructures.Point, Point3d>(); //relates points used in Delaunay3d to points used in PointSet3d
		ArrayList<Datastructures.Point> pointlist = new ArrayList<Datastructures.Point>();
		for (int i = 0; i < n; i++) {
			Point3d p = points.get(i);
			Datastructures.Point q = new Datastructures.Point(p.x, p.y, p.z);
			hash.put(q, p);
			index.put(p, i);
			pointlist.add(q);
		}

		Delaunay3D Del3D = new Delaunay3D();
		Del3D.setPoints(pointlist);
		Del3D.computeFirstTetrahedron(5000);
		Del3D.compute();

		//Del3D.printNumberOfTetrahedraGenerated();

		Filtration f = new Filtration();
		f.compute(Del3D);
		//		f.getTriangles();

		
		
		double alpha = 0.55;
		J3DScene scene = J3DScene.createJ3DSceneInFrame();
		ArrayList<Datastructures.Tetrahedron> oldTetrahedra = f.getTetrahedra();
		ArrayList<Datastructures.Triangle> triangles = f.getTriangles();
		ArrayList<Double> mstLengths4 = new ArrayList<Double>();
		ArrayList<Double> fstLengths4 = new ArrayList<Double>();
		ArrayList<Set<Segment3d>> smts4 = new ArrayList<Set<Segment3d>>();
		ArrayList<Tetrahedron> tetras = new ArrayList<Tetrahedron>();

		ArrayList<Double> mstLengths3 = new ArrayList<Double>();
		ArrayList<Double> fstLengths3 = new ArrayList<Double>();
		ArrayList<Set<Segment3d>> smts3 = new ArrayList<Set<Segment3d>>();
		ArrayList<Triangle3d> trgles = new ArrayList<Triangle3d>();
		
		Point3d p0, p1, p2, p3, s012;
		Segment3d sgm;
		Set<Segment3d> mst, tmpSMT;
		Tetrahedron tetra;
		Datastructures.Tetrahedron oldTetra;
		Triangle3d triangle;
		int i0, i1, i2, i3;
		double smtLength, mstLength, tmp;
		
		//Computes SMTs for tetrahedra
		for (int i = 0; i < oldTetrahedra.size(); i++) {
			oldTetra = oldTetrahedra.get(i);
			if (oldTetra.getInterior().getLeft() < alpha) {
				System.out.println("Tetrahedron-interior " + oldTetra.getInterior());
				p0 = hash.get(oldTetra.getPoint(0)); 
				p1 = hash.get(oldTetra.getPoint(1));
				p2 = hash.get(oldTetra.getPoint(2));
				p3 = hash.get(oldTetra.getPoint(3));
				tetra = new Tetrahedron(p0,p1,p2,p3);
				System.out.println("radius: " + tetra.getCircumSphere().radius);
				scene.addShape(tetra,new Color(140,0,0,90));
				
				Set<Segment3d> smt = tetra.getSMT(scene, false);
				if (smt.getSize() == 5) {
					smtLength = 0.0;
					for (int j = 0; j < smt.getSize(); j++) smtLength += ((Segment3d)smt.get(j)).getLength();
//					System.out.println("Length of the SMT is " + smtLength);
					for (int j = 0; j < smt.getSize(); j++) {
						sgm = (Segment3d)smt.get(j);
						scene.addShape(new geom3d.Cylinder3d(sgm.a, sgm.b, 0.001), Color.YELLOW);	
					}
					mst = tetra.getMST();
					mstLength = 0.0;
					for (int j = 0; j < 3; j++) mstLength += ((Segment3d)mst.get(j)).getLength();
//					System.out.println("Length of the MST is " + mstLength);
					tetras.add(tetra);
					mstLengths4.add(mstLength);
					fstLengths4.add(smtLength);
					smts4.add(smt);
//					System.out.println("Ratio is " + ratios.get(i));
					int j = tetras.size()-1;
					while ((j > 0) && ((mstLengths4.get(j)/fstLengths4.get(j)) > (mstLengths4.get(j-1)/fstLengths4.get(j-1)))) {
						tmp = mstLengths4.get(j);
						mstLengths4.set(j, mstLengths4.get(j-1));
						mstLengths4.set(j-1, tmp);
						tmp = fstLengths4.get(j);
						fstLengths4.set(j, fstLengths4.get(j-1));
						fstLengths4.set(j-1, tmp);
						Tetrahedron tmpTetra = tetras.get(j);
						tetras.set(j, tetras.get(j-1));
						tetras.set(j-1, tmpTetra);
						tmpSMT = smts4.get(j);
						smts4.set(j, smts4.get(j-1));
						smts4.set(j-1, tmpSMT);
						j--;
					}
				}
			}
		}
		
		// Computes SMTs for triangles
		for (int i = 0; i < triangles.size(); i++) {
			Datastructures.Triangle oldTriangle = triangles.get(i);
			if ((oldTriangle.getRegular().getLeft() != Double.NaN) && (
					oldTriangle.getRegular().getLeft() < alpha)) {
				System.out.println("Triangle-singular " + oldTriangle.getSingular());
				System.out.println("Triangle-regular " + oldTriangle.getRegular());
				System.out.println("Triangle-interior " + oldTriangle.getInterior());
				p0 = hash.get(oldTriangle.getPoint(0));
				p1 = hash.get(oldTriangle.getPoint(1));
				p2 = hash.get(oldTriangle.getPoint(2));
				triangle = new Triangle3d(p0,p1,p2);
				s012 = Point3d.getSteinerPoint(p0, p1, p2);
				if (s012 != null) {
					Set<Segment3d> smt = new Set<Segment3d>();
					smt.insert(new Segment3d(s012, p0));
					smt.insert(new Segment3d(s012, p1));
					smt.insert(new Segment3d(s012, p2));
					smtLength = 0.0;
					for (int j = 0; j < 3; j++) smtLength += ((Segment3d)smt.get(j)).getLength();
//					for (int j = 0; j < smt.getSize(); j++) {
//						sgm = (Segment3d)smt.get(j);
//						scene.addShape(new geom3d.Cylinder3d(sgm.a, sgm.b, 0.001), Color.YELLOW);	
//					}
					mst = triangle.getMST();
					mstLength = 0.0;
					for (int j = 0; j < 2; j++) mstLength += ((Segment3d)mst.get(j)).getLength();
//					System.out.println("Length of the MST is " + mstLength);
					trgles.add(triangle);
					mstLengths3.add(mstLength);
					fstLengths3.add(smtLength);
					smts3.add(smt);
//					System.out.println("Ratio is " + ratios.get(i));
					int j = trgles.size()-1;
					while ((j > 0) && ((mstLengths3.get(j)/fstLengths3.get(j)) > (mstLengths3.get(j-1)/fstLengths3.get(j-1)))) {
						tmp = mstLengths3.get(j);
						mstLengths3.set(j, mstLengths3.get(j-1));
						mstLengths3.set(j-1, tmp);
						tmp = fstLengths3.get(j);
						fstLengths3.set(j, fstLengths3.get(j-1));
						fstLengths3.set(j-1, tmp);
						Triangle3d tmpTriangle = trgles.get(j);
						trgles.set(j, trgles.get(j-1));
						trgles.set(j-1, tmpTriangle);
						tmpSMT = smts3.get(j);
						smts3.set(j, smts3.get(j-1));
						smts3.set(j-1, tmpSMT);
						j--;
					}
				}
			}
		}
		
//		for (int j = 0; j < ratios.size(); j++) System.out.println(j + ". ratio = " + ratios.get(j));	
		
		mst = points.getMST();
		SorterQuick sorter = new SorterQuick();
		SortToolSegment3d tool = new SortToolSegment3d();
		sorter.Sort(mst, tool, true);
		double length = 0.0;
		for (int i = 0; i < mst.getSize(); i ++) length += mst.get(i).getLength();
		
		smtLength = 0.0;

		// scans tetrahedra (sorted by for example non-increasing Steiner ratio or by non-increasing difference
		// of its MST and SMT
		DS ds = new DS();
		ArrayList<DSNode> nodes = new ArrayList<DSNode>();
		for (int i = 0; i < n; i++) nodes.add(ds.makeSet(i));
		int nrDisjointSets = n;
		Set<Segment3d> covSet;
		Segment3d sgm0, sgm1;
		int covSize;
		int j4 = 0;
		int j3 = 0;
		int j2 = 0;
		while ((nrDisjointSets != 1) && (j4 < tetras.size())) {
			tetra = tetras.get(j4);
			i0 = index.get(tetra.points[0]);
			i1 = index.get(tetra.points[1]);
			i2 = index.get(tetra.points[2]);
			i3 = index.get(tetra.points[3]);

			// attempts to add the smt of the tetrahedron if its corners are still in different components 
			if ((ds.find(nodes.get(i0)) != ds.find(nodes.get(i1))) &&
				(ds.find(nodes.get(i0)) != ds.find(nodes.get(i2))) &&
				(ds.find(nodes.get(i0)) != ds.find(nodes.get(i3))) &&
				(ds.find(nodes.get(i1)) != ds.find(nodes.get(i2))) &&
				(ds.find(nodes.get(i1)) != ds.find(nodes.get(i3))) &&
				(ds.find(nodes.get(i2)) != ds.find(nodes.get(i3)))) {
				covSet = tetra.coveringSegments(mst);
				covSize = covSet.getSize();
				// adds the smt of the tetrahedron of three of its edges are covered by the MST of all terminals
				if ( covSize == 3) {				
					Set<Segment3d> smt = smts4.get(j4);
					sgm = (Segment3d)smt.get(4);
					extended.insert(sgm.a);
					extended.insert(sgm.b);
					smtLength += fstLengths4.get(j4);
					ds.union(ds.find(nodes.get(i0)), ds.find(nodes.get(i1))); 
					ds.union(ds.find(nodes.get(i0)), ds.find(nodes.get(i2)));
					ds.union(ds.find(nodes.get(i0)), ds.find(nodes.get(i3)));
					nrDisjointSets = nrDisjointSets - 3;
				}
				else {
					// adds the smt of the tetrahedron if two of its edges are not incident and covered by the MST of all terminals
					if (covSize == 2) {
						sgm0 = mst.get(0);
						sgm1 = mst.get(1);
						if ((sgm0.a != sgm1.a) && (sgm0.a != sgm1.b) && (sgm0.b != sgm1.a) && (sgm0.b != sgm1.b)) {
							Set<Segment3d> smt = smts4.get(j4);
							sgm = (Segment3d)smt.get(4);
							extended.insert(sgm.a);
							extended.insert(sgm.b);
							smtLength += fstLengths4.get(j4);
							ds.union(ds.find(nodes.get(i0)), ds.find(nodes.get(i1))); 
							ds.union(ds.find(nodes.get(i0)), ds.find(nodes.get(i2)));
							ds.union(ds.find(nodes.get(i0)), ds.find(nodes.get(i3)));
							nrDisjointSets = nrDisjointSets - 3;

						}
						
					}
				}
			}
			j4++;
		}
		while ((nrDisjointSets != 1) && (j3 < trgles.size())) {
			triangle = trgles.get(j3);
			i0 = index.get(triangle.p0);
			i1 = index.get(triangle.p1);
			i2 = index.get(triangle.p2);
			if ((ds.find(nodes.get(i0)) != ds.find(nodes.get(i1))) &&
				(ds.find(nodes.get(i0)) != ds.find(nodes.get(i2))) &&
				(ds.find(nodes.get(i1)) != ds.find(nodes.get(i2)))) {
				if (triangle.coveringSegments(mst).getSize() == 2) {				
					Set<Segment3d> smt = smts3.get(j3);
//					for (int k = 0; k < 3; k++) {
//						sgm = (Segment3d)smt.get(k);
//						scene.addShape(new geom3d.Cylinder3d(sgm.a, sgm.b, smtCylinderRadius), Color.MAGENTA);	
//					}
//					System.out.println("FST of triangle " + i0 + ", " + i1 + ", " + i2);
					sgm = (Segment3d)smt.get(0);
					extended.insert(sgm.a);
					smtLength += fstLengths3.get(j3);
					ds.union(ds.find(nodes.get(i0)), ds.find(nodes.get(i1)));
					ds.union(ds.find(nodes.get(i0)), ds.find(nodes.get(i2)));
					nrDisjointSets = nrDisjointSets-2;
				}
			}
			j3++;
		}
		while ((nrDisjointSets != 1) && (j2 < mst.getSize())) {
			sgm = mst.get(j2); 
			i0 = index.get(sgm.a);
			i1 = index.get(sgm.b);
			if (ds.find(nodes.get(i0)) != ds.find(nodes.get(i1))) {
				smtLength += sgm.getLength();					
//				scene.addShape(new geom3d.Cylinder3d(sgm.a, sgm.b, smtCylinderRadius), Color.CYAN);	
				ds.union(ds.find(nodes.get(i0)), ds.find(nodes.get(i1)));
				nrDisjointSets--;
			}
			j2++;			
		}
			
		
		mst = extended.getMST();
		double extendedLength = 0.0;
		for (int i = 0; i < mst.getSize(); i++) {
			sgm = mst.get(i);
			extendedLength += sgm.getLength();
//			scene.addShape(new geom3d.Cylinder3d(sgm.a, sgm.b, smtCylinderRadius), Color.BLUE);
		}
			
		Graph mstGraph = new Graph();
		for (int i = 0; i < extended.getSize(); i++) mstGraph.insertVertex(extended.get(i));
		for (int i = 0; i < mst.getSize(); i++) {
			sgm = mst.get(i);
			mstGraph.insertEdge(extended.getClosestIndex(sgm.a), extended.getClosestIndex(sgm.b), sgm.getLength());
		}
		
		// removing pendant Steiner points
		Vertex v;
		while ((v = mstGraph.findVertexOfDegree(1, points.getSize())) != null) {
			mstGraph.delete(v);
			extended.delete((Point3d)v.getObject());
			System.out.println("Steiner point of degree 1 deleted");
		}

		// removing Steiner points of degree 2
		Vertex u, w;
		Point3d pu, pv, pw;
//		int i = points.getSize();
		while ((v = mstGraph.findVertexOfDegree(2, points.getSize())) != null) {
			u = v.getAdjacentVertex(0);
			w = v.getAdjacentVertex(1);
			mstGraph.insertEdge(u, w, ((Point3d)u.getObject()).getDistance((Point3d)w.getObject()));
//				mstGraph.deleteEdge(u, v);
//				mstGraph.deleteEdge(w, v);
			mstGraph.delete(v);
			extended.delete((Point3d)v.getObject());
			System.out.println("Steiner point of degree 2 replaced by an edge to adjacent vertices");
		}

		// dealing with terminals of degree 2 or more where its incident edges make less than 120 degrees
		Point3d ps;
		Vertex s;
		int i = 0;
		int degree;
		int nrTerminals = points.getSize();
		Set<Vertex> adjVertices;
		PointSet3d adjPoints = new PointSet3d();
		PointSet3d pair;
		while (i < extended.getSize()) {
			v = mstGraph.getVertex(i);
			adjVertices = v.getAdjacentVertices();
			degree = adjVertices.getSize();
			adjPoints.clear();
			for (int j = 0; j < degree; j++) adjPoints.insert((Point3d)adjVertices.get(j).getObject());
			pv = (Point3d)v.getObject();
			while (((i < nrTerminals) && (degree >= 2)) || ((i >= nrTerminals) && (degree >= 4))) {
				pair = adjPoints.getSharpestWedge(pv);
				pu = pair.get(0);
				pw = pair.get(1);
				if (Point3d.isSteinerAngle(pu, pv, pw)) {
					u = mstGraph.findVertex(pu);
					w = mstGraph.findVertex(pw);
					mstGraph.deleteEdge(v, u);
					mstGraph.deleteEdge(v, w);
					ps = Point3d.getSteinerPoint(pu, pv, pw);
					extended.insert(ps);
					s = mstGraph.insertVertex(ps);
					mstGraph.insertEdge(s, u, pu.getDistance(ps));
					mstGraph.insertEdge(s, v, pv.getDistance(ps));
					mstGraph.insertEdge(s, w, pw.getDistance(ps));
					adjVertices.delete(u); adjPoints.delete(pu);
					adjVertices.delete(w); adjPoints.delete(pw);
					adjVertices.insert(s); adjPoints.insert(ps);
					degree--;
					System.out.println("Steiner point connecting 3 points making a sharp angle added");
				}
				else degree = -1;
			}
			i++;
		}

		double improvedExtendedLength = 0.0;
		Set<Edge> edges;
		for (int i11 = 0; i11 < mstGraph.getNrVertices(); i11++) {
			u = mstGraph.getVertex(i11);
			pu = (Point3d)u.getObject();
			edges = u.getEdges();
			for (int j = 0; j < edges.getSize(); j++) {
				v = (edges.get(j)).to();
				pv = (Point3d)v.getObject();
				if (pu.dominates(pv)) {
					improvedExtendedLength += pu.getDistance(pv);
					scene.addShape(new geom3d.Cylinder3d(pu, pv, smtCylinderRadius), Color.BLUE);
				}
			}
		}
			
		extended.draw(scene, points.getSize(), extended.getSize()-1, Color.YELLOW);
		points.draw(scene, 0, points.getSize()-1, Color.RED);
		Point3d p, textPoint;
		for (int i11 = 0; i11 < points.getSize(); i11++)  {
			p = points.get(i11);
			textPoint = new Point3d(p.x + 0.01, p.y + 0.01, p.z + 0.01);
			scene.addText((new Integer(i11)).toString(), textPoint, 0.04);
		}

		System.out.println("Length of the MST of the original points " + length);
		System.out.println("Length of the Steiner tree found is " + smtLength);
		System.out.println("Length of the MST of original points and Steiner points is " + extendedLength);
		System.out.println("Length of the improved MST of original points and Steiner points is " + improvedExtendedLength);
	}
}
