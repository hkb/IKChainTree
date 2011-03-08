package j3dScene;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;

import javax.media.j3d.*;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import matrix.Matrix4x4;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Text2D;
import com.sun.j3d.utils.universe.SimpleUniverse;

import geom3d.Capsule3d;
import geom3d.Point3d;
import geom3d.RSS3d;
import geom3d.Shape3d;
import geom3d.Sphere3d;
import geom3d.Tetrahedron;
import geom3d.Triangle3d;
import geom3d.Vector3d;

/** A graphics class for viewing scenes using Java3D. 
 * All the <code>Shape</code>-subclasses specified in the <code>edu.geom3D</code> 
 * package can be added to a <code>J3DScene</code> object and are automatically 
 * painted on a <code>Canvas3D</code> object. For 
 * instance the following code creates a scene with a cylinder and a red 
 * transparent box and adds the canvas to a frame. 
 * <pre>
 * J3DScene scene = new J3DScene();
 * scene.addShape(  new Cylinder(new Vector(1,0,0), new Vector(0.5,0.5, 0.3), 0.1f) );
 * 
 * Vector boxCorner = new Vector(-1,0,0);
 * Vector[] boxBases = {new Vector(1,0,0), new Vector(0,1,0), new Vector(0,0,1)};
 * float[] boxExtents = {0.8f, 1, 2};
 * Box box = new Box( boxCorner, boxBases, boxExtents );
 * scene.addShape( box, new Color(200,0,0,100) );
 * 
 * Canvas3D canvas = scene.getCanvas();
 * 
 * JFrame frame = new JFrame();
 * frame.setSize(400,400);
 * frame.getContentPane().add( canvas );
 * frame.setVisible(true);
 * </pre>
 * Text can be added to the scene as well and will always face the camera. 
 * 
 * The <code>repaint()</code> method must be called every time the position of 
 * shapes has changed and the canvas should be updated. The pointers 
 * to added shapes are stored, so subsequent changes in the <code>box</code> 
 * object in the above code will be visible on the canvas when <code>repaint()</code> 
 * is called. The following example shows how to animate a sphere rotating around origo.
 * <pre>
 *  J3DScene scene = new J3DScene();
 *  Sphere sphere = new Sphere( new Vector(1,0,0), 0.1f); 
 *  scene.addShape(sphere);
 *  float t = 0;
 *  while(true){
 * 		t+=0.01f;
 * 		sphere.center = new Vector(Math.cos(t), Math.sin(t), 0);
 * 		scene.repaint();
 * 		try{ Thread.sleep(30); }catch(InterruptedException exc){}
 *  }
 * </pre>
 * 
 * A static method is supplied for conveniently creating a frame containing a scene-viewer. 
 * The following example shows how to quickly create a <code>J3DScene</code> object 
 * that is shown in a frame and ready for use:
 * <pre>
 * J3DScene scene = J3DScene.createJ3DSceneInFrame();
 * scene.setAxisEnabled(true);
 * scene.addShape(  new Cylinder(new Vector(1,0,0), new Vector0,1,0), 0.1f) );
 * </pre>
 * @author R. Fonseca
 */
public class J3DScene {
	public JFrame frame;
	private Canvas3D canvas;
	private BranchGroup sceneRoot, scene;
	private CamBehavior camBehavior;
	private RebuildBehavior rebuildBehavior;
	private OrbitBehavior orbitBehavior;
	private Timer repaintTimer;

	private final BoundingSphere bounds = new BoundingSphere(new javax.vecmath.Point3d(0,0,0), 5000);
	private Background background;

	private final Map<Shape3d,BranchGroup> shapeTransforms = new HashMap<Shape3d,BranchGroup>();
	private final Map<Shape3d,Color> primitives = new HashMap<Shape3d,Color>();
	private Vector3d lightDir = new Vector3d(0,-1,-5);
	private Point3d sceneCenter = new Point3d(0,0,0);
	private final List<Shape3d> axisElements = new ArrayList<Shape3d>();


	/** Set color of background. */
	public void setBackgroundColor(Color c){
		background.setColor(c.getRed()/255f, c.getGreen()/255f, c.getBlue()/255f);
	}

	/** Removes one volume from the scene. */
	public void removeShape(Shape3d v){
		primitives.remove(v);
		BranchGroup bg = shapeTransforms.remove(v);
		scene.removeChild(bg);

	}

	/** Remove all volumes from the scene. */
	public void removeAllShapes(){
		while(!primitives.isEmpty())
			removeShape(primitives.entrySet().iterator().next().getKey());
		primitives.clear();
		shapeTransforms.clear();
	}

	/** Add a volume object. The standard color gray will be used */
	public void addShape(Shape3d v){	addShape(v,Color.gray);	}
	/** Add a volume object with a specified color */
	public void addShape(Shape3d v, Color c){	
		primitives.put(v, c);
		Node p = genPrimitive(v, c);
		scene.addChild(p);
	}
	/** Add a text-object at the specified position. */
	public void addText(String t, Point3d pos){ 
		addShape(new TextShape(t,pos), Color.GRAY); 
	}
	public void addText(String t, Point3d pos, double height){ 
		addShape(new TextShape(t,pos,height), Color.GRAY); 
	}
	public void addText(String t, Point3d pos, double height, Color c){
		addShape(new TextShape(t,pos,height), c);
	}

	/** Sets the location that the camera looks at to the center of all the shapes added 
	 * to the scene.  */
	public void centerCamera(){
		Point3d sceneCenter = new Point3d(0,0,0);
		for(Entry<Shape3d, Color> entry: primitives.entrySet())
			sceneCenter.addIn(entry.getKey().getCenter());
		sceneCenter.scale(-1f/primitives.entrySet().size());

		Transform3D transform = new Transform3D();
		transform.setTranslation(new Vector3f((float)sceneCenter.getX(), (float)sceneCenter.getY(), (float)sceneCenter.getZ()));
		TransformGroup tg = ((TransformGroup)((TransformGroup)sceneRoot.getChild(0)).getChild(0));
		tg.setTransform(transform);

	}

	/** Zooms such that the maximal distance between two objects is within the view */
	public void autoZoom(){

		double maxDist = 0;
		try{
			for(Entry<Shape3d, Color> entry: primitives.entrySet()){
				for(Entry<Shape3d, Color> entry2: primitives.entrySet()){
					double d = entry.getKey().getCenter().getDistance(entry2.getKey().getCenter());
					if(d>maxDist) maxDist=d;
				}
			}
		}catch(ConcurrentModificationException exc){
			try{ Thread.sleep(300); }catch(InterruptedException exc2){}
			autoZoom();
			return;
		}
		if(maxDist>0){
			this.camBehavior.setScale(2/(maxDist+10));
			this.repaint();
		}
	}

	private boolean parallelProjection = false;
	/** Enables and disables parallel projection (as opposed to perspective projection). */
	public void setParallelProjection(boolean enabled) {
		if(enabled && !parallelProjection){
			canvas.getView().setProjectionPolicy(View.PARALLEL_PROJECTION);
		}
		if(!enabled && parallelProjection){
			canvas.getView().setProjectionPolicy(View.PERSPECTIVE_PROJECTION);
		}
		parallelProjection = enabled;
	}


	private class RotThread extends Thread {
		boolean stop = false;
		public void run() {
			stop = false;
			while(!stop){
				camBehavior.rotate(0.01f);
				try {Thread.sleep(40);} catch (InterruptedException e) {	}
			}
		}
	}
	private RotThread rotThread;
	/** Toggles rotation */
	public void toggleRotation(){
		//Thread t = new RotThread();
		//t.start();
		if(rotThread!=null && rotThread.isAlive()){
			rotThread.stop = true;
		}else{
			rotThread = new RotThread();
			rotThread.start();
		}
	}


	private void updateTransforms(Shape3d v){
		if(v instanceof geom3d.Sphere3d) updateSphereTransforms((Sphere3d)v);
		if(v instanceof geom3d.Cylinder3d) updateCylinderTransforms((geom3d.Cylinder3d)v);
		if(v instanceof geom3d.Box3d) updateBoxTransforms((geom3d.Box3d)v);
		if(v instanceof RSS3d) updateRSSTransforms((RSS3d)v);
		if(v instanceof geom3d.Cone3d) updateConeTransforms((geom3d.Cone3d)v);
		if(v instanceof geom3d.Capsule3d) updateCapsuleTransforms((geom3d.Capsule3d)v);
		if(v instanceof geom3d.Triangle3d) updateTriangleTransforms((geom3d.Triangle3d)v);
		if(v instanceof geom3d.Tetrahedron) updateTetrahedronTransforms((geom3d.Tetrahedron)v);
		if(v instanceof TextShape) updateTextTransforms((TextShape)v);
	}
	private void updateSphereTransforms(geom3d.Sphere3d s){
		TransformGroup tg = (TransformGroup)shapeTransforms.get(s).getChild(0);

		Transform3D trans = new Transform3D();
		trans.setTranslation(toJ3DVec(s.getCenter()));
		trans.setScale(s.getRadius());

		tg.setTransform(trans);
	}
	private void updateCylinderTransforms(geom3d.Cylinder3d c){

		Transform3D trans = new Transform3D();
		Vector3d v1 = new Vector3d(0,1.0001,0);
		Vector3d v2 = c.getSegment().getAToB();

		if(v2.getLength()>0.000001 && v1.angle(v2)>0.00001 && v1.angle(v2)<Math.PI-0.00001){ 
			Vector3d v = Vector3d.crossProduct(v1, v2);
			v.scaleToLength(1);
			Matrix4x4 m4 = Matrix4x4.createRotationMatrix(v1.angle(v2), v);
			trans.set(m4.getCoordArray());
		}
		trans.setScale(new Vector3d(c.getRadius(), v2.length(), c.getRadius()));
		trans.setTranslation(c.getSegment().getMidPoint().toVector());

		((TransformGroup)shapeTransforms.get(c).getChild(0)).setTransform(trans);
	}
	private void updateConeTransforms(geom3d.Cone3d c){

		Transform3D trans = new Transform3D();
		Vector3d v1 = new Vector3d(0,1,0);
		Vector3d v2 = c.p1.vectorTo(c.p2);

		if(v2.length()>0.000001 && v1.angle(v2)>0.00001){ 
			//Matrix m = Matrix.createRotationMatrix(v1.angle(v2), v1.cross(v2).normIn());
			//trans.set(m.getCoordArray());
			Vector3d v = Vector3d.crossProduct(v1, v2);
			v.scaleToLength(1);
			Matrix4x4 m4 = Matrix4x4.createRotationMatrix(v1.angle(v2), v);
			trans.set(m4.getCoordArray());
		}
		trans.setScale(new Vector3d(c.rad, v2.length(), c.rad));
		trans.setTranslation(c.getCenter().toVector());

		((TransformGroup)shapeTransforms.get(c).getChild(0)).setTransform(trans);
	}
	private void updateTextTransforms(TextShape t){
		Transform3D transform = new Transform3D();
		transform.setTranslation(toJ3DVec(t.pos));
		transform.setScale(4*t.height);

		((TransformGroup)shapeTransforms.get(t).getChild(0)).setTransform(transform);
	}
	private void updateBoxTransforms(geom3d.Box3d b){
		Transform3D transform = new Transform3D();
		if(b.getXDir().cross(b.getYDir()).dot(b.getZDir())<0){
			Matrix4x4 m = Matrix4x4.createColumnMatrix(
					b.getXDir(), 
					b.getZDir(), 
					b.getYDir()  );
			transform.set(m.getCoordArray());
			//transform.setScale(new Vector3d(b.getXDir().getLength(), b.getZDir().getLength(), b.getYDir().getLength()));
			transform.setTranslation(toJ3DVec(b.getAnchor()));
		}else{
			Matrix4x4 m = Matrix4x4.createColumnMatrix(
					b.getXDir(), 
					b.getYDir(), 
					b.getZDir()  );
			transform.set(m.getCoordArray());
			//transform.setScale(new Vector3d(b.getXDir().getLength(), b.getYDir().getLength(), b.getZDir().getLength()));
			transform.setTranslation(toJ3DVec(b.getAnchor()));
		}

		//transform.setTranslation(toJ3DVec(b.p));
		((TransformGroup)shapeTransforms.get(b).getChild(0)).setTransform(transform);
	}
	private void updateRSSTransforms(RSS3d r){
//		if(true) return;v
		Transform3D trans = new Transform3D();
		Vector3d v2 = r.rectangle.bases[0];
		Vector3d v3 = r.rectangle.bases[1];
		double width = v2.length()*2;
		double height = v3.length()*2;
		double radius = r.radius;
		Matrix4x4 m = Matrix4x4.createColumnMatrix(v2.norm(),v3.norm(),v2.cross(v3));
		
		trans.set(m.getCoordArray());
		trans.setScale(new Vector3d(width, height, radius));
		trans.setTranslation(toJ3DVec(r.getCenter()));

		((TransformGroup)shapeTransforms.get(r).getChild(0)).setTransform(trans);
		TransformGroup tg = ((TransformGroup)shapeTransforms.get(r).getChild(0));

		BranchGroup bg = (BranchGroup)tg.getChild(0);

		
		trans = new Transform3D();
		trans.setScale(new Vector3d(radius/width,radius/height,1));
		trans.setTranslation(new Vector3d(0.5,0.5,0));
		((TransformGroup)bg.getChild(0)).setTransform(trans);

		trans = new Transform3D();
		trans.rotZ(Math.PI/2);
		trans.setTranslation(new Vector3d(-0.5,0.5,0));
		trans.setScale(new Vector3d(radius/height,radius/width,1));
		((TransformGroup)bg.getChild(1)).setTransform(trans);
		
		trans = new Transform3D();
		trans.rotZ(Math.PI);
		trans.setTranslation(new Vector3d(-0.5,-0.5,0));
		trans.setScale(new Vector3d(radius/width,radius/height,1));
		((TransformGroup)bg.getChild(2)).setTransform(trans);

		trans = new Transform3D();
		trans.rotZ(3*Math.PI/2);
		trans.setTranslation(new Vector3d(0.5,-0.5,0));
		trans.setScale(new Vector3d(radius/height,radius/width,1));
		((TransformGroup)bg.getChild(3)).setTransform(trans);
		
		//Half-cylinders
		trans = new Transform3D();
//		trans.rotZ(3*Math.PI/2);
		trans.setTranslation(new Vector3d(0.5,0,0));
		trans.setScale(new Vector3d(radius/width,1,1));
		((TransformGroup)bg.getChild(4)).setTransform(trans);

		trans = new Transform3D();
		trans.rotZ(Math.PI/2);
		trans.setTranslation(new Vector3d(0,0.5,0));
		trans.setScale(new Vector3d(radius/height,1,1));
		((TransformGroup)bg.getChild(5)).setTransform(trans);

		trans = new Transform3D();
		trans.rotZ(Math.PI);
		trans.setTranslation(new Vector3d(-0.5,0,0));
		trans.setScale(new Vector3d(radius/width,1,1));
		((TransformGroup)bg.getChild(6)).setTransform(trans);

		trans = new Transform3D();
		trans.rotZ(3*Math.PI/2);
		trans.setTranslation(new Vector3d(0,-0.5,0));
		trans.setScale(new Vector3d(radius/height,1,1));
		((TransformGroup)bg.getChild(7)).setTransform(trans);
	}
	private void updateTriangleTransforms(Triangle3d t){
		Transform3D transform = new Transform3D();
		Matrix4x4 m = Matrix4x4.createColumnMatrix(t.p0.vectorTo(t.p1), t.p1.vectorTo(t.p2), t.getNormal());

		transform.set(m.getCoordArray());
		transform.setTranslation(toJ3DVec(t.p1));

		((TransformGroup)shapeTransforms.get(t).getChild(0)).setTransform(transform);
	}
	private void updateTetrahedronTransforms(Tetrahedron t){
		Transform3D transform = new Transform3D();
		Matrix4x4 m = Matrix4x4.createColumnMatrix(
				t.getPoint(0).vectorTo(t.getPoint(1)), 
				t.getPoint(0).vectorTo(t.getPoint(2)), 
				t.getPoint(0).vectorTo(t.getPoint(3)));
		transform.set(m.getCoordArray());
		transform.setTranslation(toJ3DVec(t.getPoint(0)));

		((TransformGroup)shapeTransforms.get(t).getChild(0)).setTransform(transform);
	}
	private void updateCapsuleTransforms(Capsule3d c){
		Transform3D trans = new Transform3D();
		Vector3d v1 = new Vector3d(0,1,0);
		Vector3d v2 = c.segment.getAToB();
		if(v1.length()>0 && v2.length()>0 && v1.angle(v2)>0.00001){ 
			Matrix4x4 m = Matrix4x4.createRotationMatrix(v1.angle(v2), v1.cross(v2).scaleToLength(1));
			trans.set(m.getCoordArray());
		}
		trans.setScale(new Vector3d(c.rad, v2.length(), c.rad));
		trans.setTranslation(toJ3DVec(c.segment.getMidPoint()));


		((TransformGroup)shapeTransforms.get(c).getChild(0)).setTransform(trans);
		TransformGroup tg = ((TransformGroup)shapeTransforms.get(c).getChild(0));

		BranchGroup bg = (BranchGroup)tg.getChild(0);

		trans = new Transform3D();
		trans.setScale(new Vector3d(1,c.rad/v2.length(),1));
		trans.setTranslation(new Vector3d(0,0.5,0));
		((TransformGroup)bg.getChild(0)).setTransform(trans);

		trans = new Transform3D();
		trans.rotX(Math.PI);
		trans.setTranslation(new Vector3d(0,-0.5,0));
		trans.setScale(new Vector3d(1,c.rad/v2.length(),1));
		((TransformGroup)bg.getChild(1)).setTransform(trans);
	}


	private Node genTriangle(Triangle3d t, Color color){
		Appearance app = new Appearance(); 

		Material mat = new Material();
		mat.setDiffuseColor(color.getRed()/255f,color.getGreen()/255f,color.getBlue()/255f);

		app.setMaterial(mat);

		if(color.getAlpha()<255){
			TransparencyAttributes ta = new TransparencyAttributes();
			ta.setTransparencyMode(TransparencyAttributes.NICEST);
			ta.setTransparency(1-color.getAlpha()/255f);
			app.setTransparencyAttributes(ta);
		}

		PolygonAttributes pa = new PolygonAttributes();
		pa.setCullFace(PolygonAttributes.CULL_NONE);
		pa.setBackFaceNormalFlip(true);
		app.setPolygonAttributes(pa);

		Triangle3D tri = new Triangle3D(1, app);

		TransformGroup tg = new TransformGroup();
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg.addChild(tri);
		//shapeTransforms.put(t, tg);

		BranchGroup ret = new BranchGroup();
		ret.addChild(tg);
		ret.setCapability(BranchGroup.ALLOW_DETACH);
		ret.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		ret.compile();
		shapeTransforms.put(t, ret);
		updateTriangleTransforms(t);
		return ret;
	}
	private Node genTetrahedron(Tetrahedron t, Color color){
		Appearance app = new Appearance(); 

		Material mat = new Material();
		mat.setDiffuseColor(color.getRed()/255f,color.getGreen()/255f,color.getBlue()/255f);
		app.setMaterial(mat);

		if(color.getAlpha()<255){
			TransparencyAttributes ta = new TransparencyAttributes();
			ta.setTransparencyMode(TransparencyAttributes.NICEST);
			ta.setTransparency(1-color.getAlpha()/255f);
			app.setTransparencyAttributes(ta);
		}

		PolygonAttributes pa = new PolygonAttributes();
		pa.setCullFace(PolygonAttributes.CULL_NONE);
		pa.setBackFaceNormalFlip(true);
		app.setPolygonAttributes(pa);

		List<Triangle3d> tList = new LinkedList<Triangle3d>();
		tList.add(new Triangle3d(new Point3d(0,0,0),new Point3d(1,0,0),new Point3d(0,1,0)));//tList.add(new Triangle(t.p1,t.p2,t.p3));
		tList.add(new Triangle3d(new Point3d(0,0,0),new Point3d(0,0,1),new Point3d(1,0,0)));//tList.add(new Triangle(t.p1,t.p4,t.p2));
		tList.add(new Triangle3d(new Point3d(0,1,0),new Point3d(0,0,0),new Point3d(0,0,1)));//tList.add(new Triangle(t.p3,t.p1,t.p4));
		tList.add(new Triangle3d(new Point3d(0,1,0),new Point3d(1,0,0),new Point3d(0,0,1)));//tList.add(new Triangle(t.p3,t.p2,t.p4));
		TriangleSet3D tset = new TriangleSet3D(tList, app);


		TransformGroup tg = new TransformGroup();
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg.addChild(tset);
		//shapeTransforms.put(t, tg);

		BranchGroup ret = new BranchGroup();
		ret.addChild(tg);
		ret.setCapability(BranchGroup.ALLOW_DETACH);
		ret.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		ret.compile();
		shapeTransforms.put(t, ret);
		updateTetrahedronTransforms(t);
		return ret;
	}
	private Node genCapsule(geom3d.Capsule3d c, Color color){
		Appearance app = new Appearance(); 

		Material mat = new Material();
		mat.setDiffuseColor(color.getRed()/255f,color.getGreen()/255f,color.getBlue()/255f);
		app.setMaterial(mat);

		if(color.getAlpha()<255){
			TransparencyAttributes ta = new TransparencyAttributes();
			ta.setTransparencyMode(TransparencyAttributes.NICEST);
			ta.setTransparency(1-color.getAlpha()/255f);
			app.setTransparencyAttributes(ta);
		}

		BranchGroup capsGroup = new BranchGroup();
		capsGroup.setCapability(BranchGroup.ALLOW_CHILDREN_READ);

		//First hemisphere
		Transform3D trans = new Transform3D();
		trans.setTranslation(new Vector3d(0,0.5,0));
		TransformGroup tg  = new TransformGroup(trans);
		tg.addChild(new Hemisphere3D(1, app));
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		capsGroup.addChild(tg);

		//Second hemisphere
		trans = new Transform3D();
		trans.rotX(Math.PI);
		trans.setTranslation(new Vector3d(0,-0.5, 0));
		tg = new TransformGroup(trans);
		tg.addChild(new Hemisphere3D(1,app));
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		capsGroup.addChild(tg);

		capsGroup.addChild(new HollowCylinder3D(1,1,app));


		TransformGroup tg1 = new TransformGroup();
		tg1.addChild(capsGroup);
		tg1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg1.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
		//shapeTransforms.put(c, tg1);

		BranchGroup ret = new BranchGroup();
		ret.addChild(tg1);
		ret.setCapability(BranchGroup.ALLOW_DETACH);
		ret.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		shapeTransforms.put(c, ret);

		updateCapsuleTransforms(c);
		return ret;
	}
	private Node genCylinder(geom3d.Cylinder3d c, Color color){
		Appearance app = new Appearance(); 

		Material mat = new Material();
		mat.setDiffuseColor(color.getRed()/255f,color.getGreen()/255f,color.getBlue()/255f);
		app.setMaterial(mat);

		if(color.getAlpha()<255){
			TransparencyAttributes ta = new TransparencyAttributes();
			ta.setTransparencyMode(TransparencyAttributes.NICEST);
			ta.setTransparency(1-color.getAlpha()/255f);
			app.setTransparencyAttributes(ta);
		}

		//Cylinder cyl = new Cylinder(c.rad, c.p1.distance(c.p2), app);
		Cylinder cyl = new Cylinder(1, 1,Cylinder.GENERATE_NORMALS, 32, 1, app);



		TransformGroup tg1 = new TransformGroup();
		tg1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg1.addChild(cyl);
		//shapeTransforms.put(c, tg1);

		BranchGroup ret = new BranchGroup();
		ret.addChild(tg1);
		ret.setCapability(BranchGroup.ALLOW_DETACH);
		ret.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		shapeTransforms.put(c, ret);
		updateCylinderTransforms(c);
		return ret;
	}
	private Node genCone(geom3d.Cone3d c, Color color){
		Appearance app = new Appearance(); 

		Material mat = new Material();
		mat.setDiffuseColor(color.getRed()/255f,color.getGreen()/255f,color.getBlue()/255f);
		app.setMaterial(mat);

		if(color.getAlpha()<255){
			TransparencyAttributes ta = new TransparencyAttributes();
			ta.setTransparencyMode(TransparencyAttributes.NICEST);
			ta.setTransparency(1-color.getAlpha()/255f);
			app.setTransparencyAttributes(ta);
		}

		//Cylinder cyl = new Cylinder(c.rad, c.p1.distance(c.p2), app);
		Cone cyl = new Cone(1, 1, app);



		TransformGroup tg1 = new TransformGroup();
		tg1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg1.addChild(cyl);
		//shapeTransforms.put(c, tg1);

		BranchGroup ret = new BranchGroup();
		ret.addChild(tg1);
		ret.setCapability(BranchGroup.ALLOW_DETACH);
		ret.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		ret.compile();
		shapeTransforms.put(c, ret);
		updateConeTransforms(c);
		return ret;
	}
	private Node genSphere(geom3d.Sphere3d s, Color color){
		Appearance app = new Appearance(); 

		Material mat = new Material();
		mat.setDiffuseColor(color.getRed()/255f,color.getGreen()/255f,color.getBlue()/255f);
		mat.setShininess(100f);
		mat.setSpecularColor(1, 0,0);
		app.setMaterial(mat);

		if(color.getAlpha()<255){
			TransparencyAttributes ta = new TransparencyAttributes();
			ta.setTransparencyMode(TransparencyAttributes.NICEST);
			ta.setTransparency(1-color.getAlpha()/255f);
			app.setTransparencyAttributes(ta);
		}

		com.sun.j3d.utils.geometry.Sphere sphere = 
			new com.sun.j3d.utils.geometry.Sphere(
					1,
					com.sun.j3d.utils.geometry.Sphere.GENERATE_NORMALS, 
					32,
					app);

		/*Transform3D trans = new Transform3D();
		trans.setTranslation(toJ3DVec(s.center));
		trans.setScale(s.radius);*/

		//TransformGroup tg1 = new TransformGroup(trans);
		TransformGroup tg1 = new TransformGroup();
		tg1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg1.addChild(sphere);
		//shapeTransforms.put(s, tg1);

		BranchGroup ret = new BranchGroup();
		ret.addChild(tg1);
		ret.setCapability(BranchGroup.ALLOW_DETACH);
		ret.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		shapeTransforms.put(s, ret);
		updateSphereTransforms(s);
		return ret;
	}
	private Node genBox(geom3d.Box3d b, Color color) {
		Appearance app = new Appearance(); 

		Material mat = new Material();
		mat.setDiffuseColor(color.getRed()/255f,color.getGreen()/255f,color.getBlue()/255f);
		app.setMaterial(mat);

		if(color.getAlpha()<255){
			TransparencyAttributes ta = new TransparencyAttributes();
			ta.setTransparencyMode(TransparencyAttributes.NICEST);
			ta.setTransparency(1-color.getAlpha()/255f);
			app.setTransparencyAttributes(ta);
		}

		//com.sun.j3d.utils.geometry.Box box = new com.sun.j3d.utils.geometry.Box(v.extents[0]/2,v.extents[1]/2,v.extents[2]/2, app);
		com.sun.j3d.utils.geometry.Box box = new com.sun.j3d.utils.geometry.Box(1,1,1, app);


		TransformGroup tg = new TransformGroup();
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg.addChild(box);
		//shapeTransforms.put(b, tg);

		BranchGroup ret = new BranchGroup();
		ret.addChild(tg);
		ret.setCapability(BranchGroup.ALLOW_DETACH);
		ret.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		shapeTransforms.put(b, ret);
		updateBoxTransforms(b);
		return ret;
	}
	private Node genRSS(RSS3d r, Color color){
		Appearance app = new Appearance(); 

		Material mat = new Material();
		mat.setDiffuseColor(color.getRed()/255f,color.getGreen()/255f,color.getBlue()/255f);
		app.setMaterial(mat);

		if(color.getAlpha()<255){
			TransparencyAttributes ta = new TransparencyAttributes();
			ta.setTransparencyMode(TransparencyAttributes.NICEST);
			ta.setTransparency(1-color.getAlpha()/255f);
			app.setTransparencyAttributes(ta);
		}		
		BranchGroup capsGroup = new BranchGroup();
		capsGroup.setCapability(BranchGroup.ALLOW_CHILDREN_READ);

		//Quarterspheres
		Transform3D trans = new Transform3D();
		trans.setTranslation(new Vector3d(0.5,0.5,0));
		TransformGroup tg  = new TransformGroup(trans);
		tg.addChild(new QuarterSphere3D(1, app));
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		capsGroup.addChild(tg);

		trans = new Transform3D();
		trans.rotZ(Math.PI/2);
		trans.setTranslation(new Vector3d(-0.5,0.5, 0));
		tg = new TransformGroup(trans);
		tg.addChild(new QuarterSphere3D(1,app));
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		capsGroup.addChild(tg);
		
		trans = new Transform3D();
		trans.rotZ(Math.PI);
		trans.setTranslation(new Vector3d(-0.5,-0.5, 0));
		tg = new TransformGroup(trans);
		tg.addChild(new QuarterSphere3D(1,app));
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		capsGroup.addChild(tg);

		trans = new Transform3D();
		trans.rotZ(3*Math.PI/2);
		trans.setTranslation(new Vector3d(0.5,-0.5, 0));
		tg = new TransformGroup(trans);
		tg.addChild(new QuarterSphere3D(1,app));
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		capsGroup.addChild(tg);

		//Half-cylinders
		trans = new Transform3D();
		//trans.rotZ(3*Math.PI/2);
//		trans.setTranslation(new Vector3d(0.5,0, 0));
		tg = new TransformGroup(trans);
		tg.addChild(new HalfCylinder3D(1,1,app));
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		capsGroup.addChild(tg);

		trans = new Transform3D();
		tg = new TransformGroup(trans);
		tg.addChild(new HalfCylinder3D(1,1,app));
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		capsGroup.addChild(tg);

		trans = new Transform3D();
		tg = new TransformGroup(trans);
		tg.addChild(new HalfCylinder3D(1,1,app));
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		capsGroup.addChild(tg);

		trans = new Transform3D();
		tg = new TransformGroup(trans);
		tg.addChild(new HalfCylinder3D(1,1,app));
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		capsGroup.addChild(tg);
		
		//Bounding planes
		trans = new Transform3D();
		trans.setTranslation(new Vector3d(0,0,1));
		tg = new TransformGroup(trans);
		tg.addChild(new Rectangle3D(1,1,app));
		capsGroup.addChild(tg);

		trans = new Transform3D();
		trans.rotX(Math.PI);
		trans.setTranslation(new Vector3d(0,0,-1));
		tg = new TransformGroup(trans);
		tg.addChild(new Rectangle3D(1,1,app));
		capsGroup.addChild(tg);

		
		
		TransformGroup tg1 = new TransformGroup();
		tg1.addChild(capsGroup);
		tg1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg1.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
		//shapeTransforms.put(c, tg1);

		BranchGroup ret = new BranchGroup();
		ret.addChild(tg1);
		ret.setCapability(BranchGroup.ALLOW_DETACH);
		ret.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		shapeTransforms.put(r, ret);

		updateRSSTransforms(r);
		return ret;
	}
	private Node genPrimitive(Shape3d v, Color c){
		if(		v instanceof geom3d.Cylinder3d)		return genCylinder((geom3d.Cylinder3d)v, c);
		else if(v instanceof geom3d.Cone3d)			return genCone((geom3d.Cone3d)v, c);
		else if(v instanceof geom3d.Box3d)			return genBox((geom3d.Box3d)v, c);
		else if(v instanceof geom3d.RSS3d)			return genRSS((RSS3d)v, c);
		else if(v instanceof geom3d.Sphere3d)		return genSphere((geom3d.Sphere3d)v, c);
		else if(v instanceof geom3d.Capsule3d)		return genCapsule((geom3d.Capsule3d)v, c);
		else if(v instanceof geom3d.Triangle3d)		return genTriangle((geom3d.Triangle3d)v, c);
		else if(v instanceof geom3d.Tetrahedron)	return genTetrahedron((geom3d.Tetrahedron)v, c);
		else if(v instanceof TextShape)		return genText((TextShape)v, c);
		else{ System.err.println("Warning: unknown primitive: "+v.getClass().getName()); return null; }
	}
	private Node genText(TextShape t, Color c){
		//Color c = new Color(255-c.getRed(), 255-c.getGreen(), 255-c.getBlue());
		Shape3D text3D = new Text2D(t.text, new Color3f(c),"Arial",48, Font.BOLD );

		TransformGroup subTg = new TransformGroup();
		subTg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		Billboard billboard = new Billboard(subTg);
		billboard.setSchedulingBounds( bounds );
		subTg.addChild( billboard );

		TransformGroup tg = new TransformGroup();
		tg.addChild(subTg);
		subTg.addChild(text3D);
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		//shapeTransforms.put(t, tg);

		BranchGroup ret = new BranchGroup();
		ret.addChild(tg);
		ret.setCapability(BranchGroup.ALLOW_DETACH);
		ret.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		shapeTransforms.put(t, ret);
		updateTextTransforms(t);
		return ret;
	}
	private Node genBackground() {
		background = new Background(new Color3f(Color.white)); 
		background.setCapability(Background.ALLOW_COLOR_WRITE);
		background.setCapability(Background.ALLOW_COLOR_READ);
		background.setApplicationBounds(bounds);
		return background;
	}
	private Node genLight(){
		Color3f light1Color = new Color3f(1f, 1f, 1f);
		Vector3f light1Direction = toJ3DVec(lightDir);
		DirectionalLight light1  = new DirectionalLight(light1Color, light1Direction);
		light1.setInfluencingBounds(bounds);
		return light1;
	}
	private Node genLight2(){
		Color3f light1Color = new Color3f(1f, 1f, 1f);
		Vector3f light1Direction = new Vector3f(0,-5f,5f);
		DirectionalLight light1  = new DirectionalLight(light1Color, light1Direction);
		light1.setInfluencingBounds(bounds);
		return light1;
	}

	private boolean axisEnabled = false;
	/** Enables or disables xyz-axis from the origo */
	public void setAxisEnabled(boolean axisEnabled){
		if(axisEnabled && axisElements.isEmpty()){
			float rad = 0.015f;
			axisElements.add(new geom3d.Cylinder3d(new Point3d(0,0,0),new Point3d(1-2*rad,0,0), rad));
			axisElements.add(new geom3d.Cylinder3d(new Point3d(0,0,0),new Point3d(0,1-2*rad,0), rad));
			axisElements.add(new geom3d.Cylinder3d(new Point3d(0,0,0),new Point3d(0,0,1-2*rad), rad));

			axisElements.add(new geom3d.Cone3d(new Point3d(1-2*rad,0,0),new Point3d(1,0,0), 2*rad));
			axisElements.add(new geom3d.Cone3d(new Point3d(0,0,1-2*rad),new Point3d(0,0,1), 2*rad));
			axisElements.add(new geom3d.Cone3d(new Point3d(0,1-2*rad,0),new Point3d(0,1,0), 2*rad));

			axisElements.add(new TextShape("x", new Point3d(1,0,0)));
			axisElements.add(new TextShape("y", new Point3d(0,1,0)));
			axisElements.add(new TextShape("z", new Point3d(0,0,1)));
		}
		if(axisEnabled && !this.axisEnabled){
			for(Shape3d s: axisElements) addShape(s, Color.GRAY);
		}
		if(!axisEnabled && this.axisEnabled){
			for(Shape3d s: axisElements) removeShape(s);
		}
		this.axisEnabled = axisEnabled;
	}


	private void initialBuild(){
		sceneRoot = new BranchGroup();
		sceneRoot.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		TransformGroup tgroup = new TransformGroup();
		sceneRoot.addChild(tgroup);
		tgroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tgroup.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		tgroup.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		tgroup.setCapability(TransformGroup.ALLOW_CHILDREN_READ);


		camBehavior = new CamBehavior(tgroup);
		camBehavior.setSchedulingBounds(bounds);
		sceneRoot.addChild(camBehavior);

		//rebuildBehavior = new RebuildBehavior(tgroup);
		rebuildBehavior = new RebuildBehavior();
		rebuildBehavior.setSchedulingBounds(bounds);
		sceneRoot.addChild(rebuildBehavior);


		//BranchGroup scene = buildScene();
		//BranchGroup scene = new BranchGroup();

		Transform3D transform = new Transform3D();
		transform.setTranslation(toJ3DVec(sceneCenter.toVector().createReverseVector3d()));
		TransformGroup tg = new TransformGroup(transform);
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		scene = new BranchGroup();
		//scene.setCapability(BranchGroup.ALLOW_DETACH);
		scene.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		scene.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);

		for(Entry<Shape3d, Color> entry: primitives.entrySet())
			scene.addChild(genPrimitive(entry.getKey(), entry.getValue()));
		//for(TextPrimitive tp: texts) movedScene.addChild(genTextPrimitive(tp));
		scene.addChild(genLight());
		scene.addChild(genLight2());
		scene.addChild(genBackground());
		//if(paintAxis) scene.addChild(genAxis());
		tg.addChild(scene);
		//scene.addChild(tg);
		tgroup.addChild(tg);


		//tgroup.addChild(scene);
		//scene.compile();
		sceneRoot.compile();
	}

	public J3DScene(){
		this.initialBuild();
	}

	/** Get the canvas that displays the scene. If this method is called 
	 * several times the same <code>Canvas3D</code> object will be returned 
	 * every time.*/
	public Canvas3D getCanvas(){
		if(canvas==null){
			//initialBuild();

			GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
			canvas = new Canvas3D(config);
			SimpleUniverse universe = new SimpleUniverse(canvas);
			//universe.getViewer().getView().setProjectionPolicy(View.PARALLEL_PROJECTION);
			universe.addBranchGraph(sceneRoot);
			universe.getViewingPlatform().setNominalViewingTransform();

			CamListener cl = new CamListener();
			//canvas.addMouseListener(cl);
			//canvas.addMouseMotionListener(cl);

			orbitBehavior = new OrbitBehavior(canvas,
					OrbitBehavior.PROPORTIONAL_ZOOM | OrbitBehavior.REVERSE_ROTATE
					| OrbitBehavior.REVERSE_TRANSLATE );
			//orbitBehavior.setZoomFactor(1);
			orbitBehavior.setSchedulingBounds(bounds);    
			universe.getViewingPlatform().setViewPlatformBehavior(orbitBehavior);

			canvas.addKeyListener(cl);
			canvas.addMouseWheelListener(cl);
		}

		return canvas;
	}

	/** Repaint the canvas. If the scene has been changed in any way the 
	 * scene displayer will update the view when <code>repaint()</code> is called 
	 * and no sooner. If the scene is repeatedly changed, and repaint repeatedly 
	 * called the viewer will show an animation. */
	public void repaint(){
		rebuildBehavior.rebuild();
	}

	/** Repaint the canvas repeatedly every <code>millisecondDelay</code> milliseconds. */
	public void repaintRepeatedly(long millisecondDelay){
		if(repaintTimer!=null){
			repaintTimer.cancel();
		}else{
			repaintTimer = new Timer();
		}
		class RepaintTask extends TimerTask{
			public void run() {
				repaint();
			}
		}
		repaintTimer.schedule(new RepaintTask(), 1, millisecondDelay);
	}

	private static Vector3f toJ3DVec(Vector3d v){ return new Vector3f((float)v.x, (float)v.y, (float)v.z ); }
	private static Vector3f toJ3DVec(Point3d v){ return new Vector3f((float)v.x, (float)v.y, (float)v.z ); }

	private class CamListener extends MouseAdapter implements MouseMotionListener, MouseWheelListener, KeyListener{
		private boolean shiftPressed = false;
		private Point lastPoint = null;
		private long lastTime = System.currentTimeMillis();
		public void mousePressed(MouseEvent e) {	lastPoint = e.getPoint();		}
		public void mouseReleased(MouseEvent e) {	lastPoint = null; }
		public void mouseClicked(MouseEvent e){
			rebuildBehavior.rebuild();
		}

		public void mouseDragged(MouseEvent e) {
			if(lastPoint==null) {
				lastPoint = e.getPoint();
				lastTime = System.currentTimeMillis();
				return;
			}
			Point point = e.getPoint();
			float dX = point.x-lastPoint.x;
			float dY = point.y-lastPoint.y;
			float damper = Math.max(10, (float)(System.currentTimeMillis()-lastTime))*10f;

			if(shiftPressed){
				camBehavior.rotate(dX*(float)Math.PI/damper);
			}else{
				Vector3d delta = new Vector3d(dX, -dY, 0).createScaledVector3d(1/damper);
				camBehavior.translate(delta);
			}
			lastPoint = point;
			lastTime = System.currentTimeMillis();

		}

		public void mouseWheelMoved(MouseWheelEvent e){
			float damper = Math.max(10, (float)(System.currentTimeMillis()-lastTime))*10f;
			camBehavior.scale(e.getWheelRotation()/damper);
			//orbitBehavior.setZoomFactor(orbitBehavior.getZoomFactor()+e.getWheelRotation());
			//System.out.println(orbitBehavior.getZoomFactor());
			lastTime = System.currentTimeMillis();
		}

		public void mouseMoved(MouseEvent e) {}
		public void keyPressed(KeyEvent e) {
			if( e.getKeyCode()==KeyEvent.VK_SHIFT )	shiftPressed = true;

			if(e.getKeyCode()==KeyEvent.VK_DOWN && shiftPressed){
				float damper = Math.max(10, (float)(System.currentTimeMillis()-lastTime));
				camBehavior.scale(10f/damper);
				lastTime = System.currentTimeMillis();
			}
			if(e.getKeyCode()==KeyEvent.VK_UP && shiftPressed){
				float damper = Math.max(10, (float)(System.currentTimeMillis()-lastTime));
				camBehavior.scale(-10f/damper);
				lastTime = System.currentTimeMillis();
			}
			if(e.getKeyCode()==KeyEvent.VK_LEFT && shiftPressed){
				camBehavior.rotate(0.1f);
			}
			if(e.getKeyCode()==KeyEvent.VK_RIGHT && shiftPressed){
				camBehavior.rotate(-0.1f);
			}

			if(e.getKeyCode()==KeyEvent.VK_UP && !shiftPressed){
				camBehavior.translate(new Vector3d(0,-0.1,0));
			}
			if(e.getKeyCode()==KeyEvent.VK_DOWN && !shiftPressed){
				camBehavior.translate(new Vector3d(0,0.1,0));
			}
			if(e.getKeyCode()==KeyEvent.VK_LEFT && !shiftPressed){
				camBehavior.translate(new Vector3d(0.1,0,0));
			}
			if(e.getKeyCode()==KeyEvent.VK_RIGHT && !shiftPressed){
				camBehavior.translate(new Vector3d(-0.1,0,0));
			}
			if(e.getKeyCode()==KeyEvent.VK_S){
				j3dScene.Toolbox.writeJPEGFile("J3DScene.jpg", canvas);
				System.out.println("Stored view to J3DScene.jpg");
			}
			if(e.getKeyCode()==KeyEvent.VK_E){
				j3dScene.Toolbox.writeEPSFile("J3DScene.eps", canvas);
				System.out.println("Stored view to J3DScene.eps");
			}
			if(e.getKeyCode()==KeyEvent.VK_C){
				J3DScene.this.centerCamera();
			}
			if(e.getKeyCode()==KeyEvent.VK_Z){
				J3DScene.this.autoZoom();
			}
			if(e.getKeyCode()==KeyEvent.VK_R){
				J3DScene.this.toggleRotation();
			}
			if(e.getKeyCode()==KeyEvent.VK_P){
				J3DScene.this.setParallelProjection(!parallelProjection);
			}
			if(e.getKeyCode()==KeyEvent.VK_A){
				J3DScene.this.setAxisEnabled(!axisEnabled);
			}
		}
		public void keyReleased(KeyEvent e) {
			if( e.getKeyCode()==KeyEvent.VK_SHIFT ) shiftPressed = false;
		}
		public void keyTyped(KeyEvent e) {}

	}

	private static class CamBehavior extends Behavior {

		private TransformGroup transformGroup;
		private Transform3D trans = new Transform3D();
		private WakeupCriterion criterion;
		private double yAngle = 0.0f;
		private Vector3f translation = new Vector3f(0,0,0);
		private double scale = 1f;



		private final int ROTATE = 1;

		// create a new RotateBehavior
		CamBehavior(TransformGroup tg) {	transformGroup = tg;	}

		// initialize behavior to wakeup on a behavior post with id = ROTATE
		public void initialize() {
			criterion = new WakeupOnBehaviorPost(this, ROTATE);
			wakeupOn(criterion);
		}

		// processStimulus to rotate the cube
		@SuppressWarnings("unchecked")
		public void processStimulus(Enumeration criteria) {
			trans.rotY(yAngle);
			trans.setTranslation(translation);
			trans.setScale(scale);
			transformGroup.setTransform(trans);
			wakeupOn(criterion);
			//System.out.println("Scale "+scale);
		}

		// when the mouse is clicked, postId for the behavior
		void rotate(float dY) {
			yAngle+=dY;
			postId(ROTATE);
		}
		void translate(Vector3d delta){
			translation.add(new Vector3f((float)delta.getX(), (float)delta.getY(), (float)delta.getZ()));
			postId(ROTATE);
		}
		void scale(double s){
			scale-=s;
			if(scale<=0.001) scale=0.001f;
			postId(ROTATE);
		}
		void setScale(double s){
			scale=s;
			if(scale<=0.001) scale=0.001f;
			postId(ROTATE);
		}
	}
	private class RebuildBehavior extends Behavior {
		private boolean rebuilding = false;
		//private TransformGroup tgroup;
		private WakeupCriterion criterion;

		private final int REBUILD = 5;

		// initialize behavior to wakeup on a behavior post with id = ROTATE
		public void initialize() {
			criterion = new WakeupOnBehaviorPost(this, REBUILD);
			wakeupOn(criterion);
		}

		@SuppressWarnings("unchecked")
		public void processStimulus(Enumeration criteria) {
			try{
				for(Entry<Shape3d, BranchGroup> entry: shapeTransforms.entrySet()){
					updateTransforms(entry.getKey());
				}
			}catch(ConcurrentModificationException exc){
				return;
			}
			wakeupOn(criterion);
			rebuilding = false;
		}

		// when the mouse is clicked, postId for the behavior
		synchronized void rebuild() {
			if(rebuilding) return;
			rebuilding = true;
			postId(REBUILD);
		}
	}

	public static void main(String[] args) {
		J3DScene j3ds = createJ3DSceneInFrame();
		j3ds.addShape(new geom3d.Cylinder3d(new Point3d(0,1,0), new Point3d(0,1,1), 0.1), new Color(20, 20, 200, 200));
		geom3d.Box3d box = new geom3d.Box3d(
				new Point3d(-0.5,0.5,-0.5),
				new Vector3d(-1,-1,0).createScaledToLengthVector3d(0.5),
				new Vector3d(-1,1,0).createScaledToLengthVector3d(0.5),
				new Vector3d(0,0,-1).createScaledToLengthVector3d(0.5) );
		j3ds.addShape(box, Color.GREEN.darker());

		j3ds.setAxisEnabled(true);

		j3ds.addShape(new geom3d.Capsule3d(new Point3d(1,1,0), new Point3d(3,0,0), 0.1), new Color(20,200,20, 100));
		geom3d.Cylinder3d cyl = new geom3d.Cylinder3d(new Point3d(0.4,0,0.1), new Point3d(0.4,0.5,0), 0.1);
		j3ds.addShape(cyl, Color.RED.darker().darker());
		Sphere3d s = new Sphere3d(new Point3d(-1,-0.2,0), 0.3f);
		j3ds.addShape(s, Color.MAGENTA);
		Tetrahedron tetr = new Tetrahedron(new Point3d(0,0,1), new Point3d(-0.5,0,1), new Point3d(-0.5,0.5,1), new Point3d(-0.25,0.25, 1.25));
		j3ds.addShape(tetr, new Color(50,50,255));
		j3ds.addShape(new Triangle3d(new Point3d(0.2f, -0.2f, 0.1f), new Point3d(0.8, -0.8, 0.1), new Point3d(1,-0.3, 0.1)), new Color(150,50,255));
		j3ds.addShape(new RSS3d(new Point3d(0.5,0,1),new Vector3d[]{new Vector3d(0.2,0.2,0),new Vector3d(-0.1,0.1,0)},0.1), Color.MAGENTA);
		
		j3ds.centerCamera();
		j3ds.autoZoom();
	}


	/** Create a frame containing a canvas, display it and return the  
	 * J3DScene object shown in the frame. */
	public static J3DScene createJ3DSceneInFrame() {
		JFrame f = new JFrame("J3DScene-viewer");
		f.setSize(1200,800);
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		
		J3DScene j3ds = new J3DScene();
		j3ds.frame = f;
		Canvas3D canvas = j3ds.getCanvas();

		JMenuBar menubar = new JMenuBar();
		f.setJMenuBar(menubar);
		JMenu view = new JMenu("View");
		menubar.add(view);

		JMenuItem item;
		view.add(item = new JMenuItem("Export JPG"));
		class ExportActionListener implements ActionListener{
			J3DScene j3ds;
			ExportActionListener(J3DScene j3ds){ this.j3ds = j3ds; }
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				ExampleFileFilter filter = new ExampleFileFilter(new String[]{"jpg","jpeg"}, "JPG images");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showSaveDialog(j3ds.canvas);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					j3dScene.Toolbox.writeJPEGFile(chooser.getSelectedFile().getAbsolutePath(), j3ds.canvas);
				}	
			}	
		}
		item.addActionListener(new ExportActionListener(j3ds));
		view.add(item = new JMenuItem("Export EPS"));
		class ExportEPSActionListener implements ActionListener{
			J3DScene j3ds;
			ExportEPSActionListener(J3DScene j3ds){ this.j3ds = j3ds; }
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				ExampleFileFilter filter = new ExampleFileFilter(new String[]{"eps"}, "Encapsulated Postscript images");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showSaveDialog(j3ds.canvas);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					j3dScene.Toolbox.writeEPSFile(chooser.getSelectedFile().getAbsolutePath(), j3ds.canvas);
				}	
			}
		}
		item.addActionListener(new ExportEPSActionListener(j3ds));
		view.addSeparator();

		view.add(item = new JMenuItem("Auto-zoom (z)"));
		class AutozoomActionListener implements ActionListener{
			J3DScene j3ds;
			AutozoomActionListener(J3DScene j3ds){ this.j3ds = j3ds; }
			public void actionPerformed(ActionEvent e) {
				j3ds.autoZoom();
			}
		}
		item.addActionListener(new AutozoomActionListener(j3ds));

		view.add(item = new JMenuItem("Center view (c)"));
		class CenterActionListener implements ActionListener{
			J3DScene j3ds;
			CenterActionListener(J3DScene j3ds){ this.j3ds = j3ds; }
			public void actionPerformed(ActionEvent e) {
				j3ds.centerCamera();
			}
		}
		item.addActionListener(new CenterActionListener(j3ds));

		view.add(item = new JMenuItem("Toggle rotation (r)"));
		class RotateActionListener implements ActionListener{
			J3DScene j3ds;
			RotateActionListener(J3DScene j3ds){ this.j3ds = j3ds; }
			public void actionPerformed(ActionEvent e) {
				j3ds.toggleRotation();
			}
		}
		item.addActionListener(new RotateActionListener(j3ds));


		view.add(item = new JMenuItem("Toggle parallel projection (p)"));
		class ParallelActionListener implements ActionListener{
			J3DScene j3ds;
			ParallelActionListener(J3DScene j3ds){ this.j3ds = j3ds; }
			public void actionPerformed(ActionEvent e) {
				j3ds.setParallelProjection(!j3ds.parallelProjection);
			}
		}
		item.addActionListener(new ParallelActionListener(j3ds));

		f.getContentPane().add(canvas);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		return j3ds;
	}

}
