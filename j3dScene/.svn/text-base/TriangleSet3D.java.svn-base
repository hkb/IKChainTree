package j3dScene;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.media.j3d.*;

import geom3d.Point3d;
import geom3d.Triangle3d;
import geom3d.Vector3d;

/** 
 * @author R.Fonseca
 */
public class TriangleSet3D extends Shape3D {

	public TriangleSet3D(Collection<Triangle3d> triangles, Appearance app) {
		super();

		
		List<Point3d> verts = new LinkedList<Point3d>();
		List<Vector3d> normals = new LinkedList<Vector3d>();

		for(Triangle3d t: triangles){
			Vector3d n = Vector3d.crossProduct(t.p0.vectorTo(t.p1),t.p1.vectorTo(t.p2));
			if(n.getLength()>0.000001){
				verts.add(t.p0.clone());
				verts.add(t.p1.clone());
				verts.add(t.p2.clone());
				normals.add(n);
				normals.add(n);
				normals.add(n);
			}
		}

		int i=0;
		float[] vertArr = new float[verts.size()*3];
		for(Point3d v: verts){ vertArr[i++] = (float)v.getX(); vertArr[i++] = (float)v.getY(); vertArr[i++] = (float)v.getZ(); }
		i=0;
		float[] normArr = new float[normals.size()*3];
		for(Vector3d v: normals){ normArr[i++] = (float)v.getX(); normArr[i++] = (float)v.getY(); normArr[i++] = (float)v.getZ(); }

		TriangleArray caps = new TriangleArray(vertArr.length/3, TriangleArray.COORDINATES | TriangleArray.NORMALS);

		caps.setCoordinates(0, vertArr);
		caps.setNormals(0, normArr);

		caps.setCapability(Geometry.ALLOW_INTERSECT);
		setGeometry(caps);
		if(app==null)
			setAppearance(new Appearance());
		else
			setAppearance(app);
	}



}