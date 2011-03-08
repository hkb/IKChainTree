package j3dScene;

import geom3d.Point3d;
import geom3d.PointSet3d;
import geom3d.Shape3d;


public class TextShape implements Shape3d {
	public String text;
	public Point3d pos;
	public double height;
	public TextShape(String t, Point3d p){
		this(t,p,0.1f);
	}
	public TextShape(String t, Point3d p, double height){
		text = t;
		pos = p;
		this.height = height;
	}
	public Point3d getCenter() {
		return pos.clone();
	}
	public Shape3d createBoundingShape(Shape3d s1, Shape3d s2) {
		// TODO Auto-generated method stub
		return null;
	}
	public Shape3d createBoundingShape(PointSet3d points) {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean overlaps(Shape3d s) {
		// TODO Auto-generated method stub
		return false;
	}
	public double volume() {
		// TODO Auto-generated method stub
		return 0;
	}

}
