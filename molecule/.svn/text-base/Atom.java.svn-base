package molecule;
import geom3d.*;
import guiMolecule.AtomAppearance;
//import com.sun.j3d.utils.geometry.Sphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;

import com.sun.j3d.utils.geometry.Sphere;

public class Atom {
	private Point3d         position;
	protected static double atomicRadius;
	public static double vdWRadius;
	private BranchGroup     atomBranchGroup = null;
	private BranchGroup     vdWBranchGroup  = null;
	
	public Point3d       getPosition()       { return position; }	
	public static double getAtomicRadius()   { return atomicRadius; }
	public static double getVdWRadius()      { return vdWRadius; }
	public BranchGroup 	 getAtomBranchGroup()    { return atomBranchGroup; }
	public BranchGroup   getBranchGroup()    { return getAtomBranchGroup(); }
	public BranchGroup   getVdWBranchGroup() { return vdWBranchGroup; }
	
	public void setPosition(Point3d p) {
		if (position == null) position = new Point3d(p); else position.set(p);
	}
	
	public void setPosition(double x, double y, double z) {
		if (position == null) position = new Point3d(x, y, z); else position.set(x, y, z);
	}

	public void setAtomBranchGroup(BranchGroup atomBranchGroup)   { this.atomBranchGroup = atomBranchGroup; }
	public void setBranchGroup(BranchGroup atomBranchGroup)   { setAtomBranchGroup(atomBranchGroup); }

	public void setVdWBranchGroup(BranchGroup vdWBranchGroup) { this.vdWBranchGroup  = vdWBranchGroup; }
	
	
	 public void createBranchGroups(AtomAppearance ap) {
		 atomBranchGroup = new BranchGroup();
		 atomBranchGroup.setCapability(BranchGroup.ALLOW_DETACH);
		 atomBranchGroup.setCapability(BranchGroup.ALLOW_CHILDREN_READ);

		 Sphere atomSphere = new Sphere((float)atomicRadius, ap);
		 atomSphere.getShape().setCapability(Shape3D.ALLOW_APPEARANCE_READ);
		 atomSphere.getShape().setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);

		 Transform3D atomTr = new Transform3D();
		 atomTr.setTranslation(new javax.vecmath.Vector3d(position.getX(), position.getY(), position.getZ()));
		 TransformGroup atomTg = new TransformGroup(atomTr);
		 atomTg.addChild(atomSphere);
		 atomBranchGroup.addChild(atomTg);

		 vdWBranchGroup = new BranchGroup();
		 vdWBranchGroup.setCapability(BranchGroup.ALLOW_DETACH);
		 vdWBranchGroup.setCapability(BranchGroup.ALLOW_CHILDREN_READ);

		 Sphere vdWSphere = new Sphere((float)vdWRadius, ap);
		 vdWSphere.getShape().setCapability(Shape3D.ALLOW_APPEARANCE_READ);
		 vdWSphere.getShape().setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);

		 Transform3D vdWTr = new Transform3D();
		 vdWTr.setTranslation(new javax.vecmath.Vector3d(position.getX(), position.getY(), position.getZ()));
		 TransformGroup vdWTg = new TransformGroup(vdWTr);
		 vdWTg.addChild(vdWSphere);
		 vdWBranchGroup.addChild(vdWTg);
	 }
	 
	 public void addBranchGroups(AtomAppearance ap, TransformGroup spin) {
		 createBranchGroups(ap);
		 spin.addChild(atomBranchGroup);
	 }


}
