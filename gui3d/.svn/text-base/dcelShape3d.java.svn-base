package gui3d;

import javax.vecmath.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.geometry.*;
import dcel3d.*;

public class dcelShape3d extends Shape3D {
	public dcelShape3d(DCEL3d dcel) {
		GeometryInfo gi = new GeometryInfo(GeometryInfo.POLYGON_ARRAY);
		int nrVertices = dcel.getNrVertices();
		Point3d [] points = new Point3d[nrVertices];
		for (int i = 0; i < nrVertices; i++) {
			geom3d.Point3d p = ((Vertex)(dcel.getVertices().get(i))).getPoint();
			points[i] = new javax.vecmath.Point3d(p.getX(), p.getY(), p.getZ());
		}
		int nrFaces = dcel.getNrFaces();
		int nrHalfEdges = dcel.getNrHalfEdges();
		int [] indices = new int[nrHalfEdges];
		int [] stripCounts = new int[nrFaces];
		int counter = 0;
		for (int i = 0; i < nrFaces; i++) {
			Face face = (Face)(dcel.getFaces().get(i));
			stripCounts[i] = face.getNrHalfEdges();
			HalfEdge e = face.getHalfEdge();
			HalfEdge f = e;
			do { 
				indices[counter++] = dcel.getVertices().findIndex(f.getOrigin());
				f = face.getNextHalfEdge(f);
			} while (f != e);
		}
		gi.setCoordinates(points);
		gi.setCoordinateIndices(indices);
		gi.setStripCounts(stripCounts);
		NormalGenerator ng = new NormalGenerator();
		ng.generateNormals(gi);
		this.setGeometry(gi.getGeometryArray());
	}

}
