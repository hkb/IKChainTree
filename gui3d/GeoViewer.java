package gui3d;

import geom3d.Point3d;
import geom3d.Segment3d;
import geom3d.PointSet3d;
import geom3d.Box3d;
import dataStructures.Set;

import guiMolecule.AtomAppearance;
import guiMolecule.BondAppearance;

import java.applet.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.media.j3d.*;
import javax.vecmath.*;

import misc.Colors;
import molecule.Atom;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.geometry.Box;

import com.sun.j3d.utils.universe.SimpleUniverse;

public class GeoViewer extends Applet {
	private PointSet3d points = null;
	private Set segments = null;
	private Box3d box = null;
	private TransformGroup spin = new TransformGroup();
              	public TransformGroup[] transformGroups;
              	public int transformGroupCounter = 0;
	
	public GeoViewer(PointSet3d points) {
		super();
		this.points = points;
	             	transformGroups = new TransformGroup[points.getSize()];
	}
	
//	public GeoViewer(PointSet3d points, Set segments) {
//		super();
//		this.points = points;
//		this.segments = segments;
//	}

	public GeoViewer(PointSet3d points, Box3d box) {
		super();
		this.points = points;
		this.box = box;
	}

	public TransformGroup getSpin() { return spin; }
	
	public void init() {
	    // create canvas
	    GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
	    Canvas3D cv = new Canvas3D(gc);
	    setLayout(new BorderLayout());
	    add(cv, BorderLayout.CENTER);
	    BranchGroup bg = createSceneGraph();
	    bg.compile();
	    SimpleUniverse su = new SimpleUniverse(cv);
	    su.getViewingPlatform().setNominalViewingTransform();
	    su.addBranchGraph(bg);
	 }
	
	public void addPoint(Point3d p, Color3f color) {
		Sphere sphere = new Sphere(0.06f);
		Appearance sphereAppear = new Appearance();
		sphereAppear.setColoringAttributes(new ColoringAttributes(Colors.red,ColoringAttributes.NICEST));
		sphere.setAppearance(sphereAppear);
		sphere.addChild(new Point3dShape());
		Transform3D sphereTransform = new Transform3D();
		sphereTransform.setScale(0.2);
		sphereTransform.setTranslation(new javax.vecmath.Vector3d(p.getX(), p.getY(), p.getZ())); 
		System.out.println(transformGroupCounter);
		transformGroups[transformGroupCounter]  = new TransformGroup(sphereTransform);
		transformGroups[transformGroupCounter].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		transformGroups[transformGroupCounter].addChild(sphere);
		spin.addChild(transformGroups[transformGroupCounter]);
		transformGroupCounter++;
	//	                              transformGroups[transformGroupCounter] = tg;
	}


	 public void addSegment(Point3d p0, Point3d p1, Color3f color) {
		 LineArray la = new LineArray(2, LineArray.COORDINATES | LineArray.COLOR_3 );
		 la.setCoordinate(0, new Point3f((float)p0.getX(),(float)p0.getY(), (float)p0.getZ()));  
		 la.setCoordinate(1, new Point3f((float)p1.getX(),(float)p1.getY(), (float)p1.getZ()));  
		 la.setColor(0, color);
		 la.setColor(1, color);
		 Shape3D shape = new Shape3D(la, BondAppearance.bond);
		 Transform3D segmentTransform = new Transform3D();
		 TransformGroup tg = new TransformGroup(segmentTransform);
		 tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		 tg.addChild(shape);
		 spin.addChild(tg);
	 }

	 public void addBox(Box3d box) {
		 double x = box.getXDir().getLength(); 
		 double y = box.getYDir().getLength(); 
		 double z = box.getZDir().getLength(); 
		 Appearance ap = new Appearance();
		 ap.setColoringAttributes(new ColoringAttributes(Colors.blue, ColoringAttributes.NICEST));
		 ap.setPolygonAttributes(new PolygonAttributes(PolygonAttributes.POLYGON_LINE, PolygonAttributes.CULL_NONE,0.0f));
 		 Box boxImage = new Box((float)x, (float)y, (float)z, ap);   
		 Transform3D tr = new Transform3D();
		 tr.setTranslation(new javax.vecmath.Vector3d(x, y, z));
		 TransformGroup tgBox = new TransformGroup(tr);
		 tgBox.addChild(boxImage);
		 spin.addChild(tgBox);
	 }

	 
	
	 private BranchGroup createSceneGraph() {
		 BranchGroup root = new BranchGroup();
	    
	    spin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	    spin.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	    spin.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
		spin.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		spin.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);

	    root.addChild(spin);
	    

		for (int i = 0; i < points.getSize(); i++) addPoint((Point3d)points.get(i), Colors.red);
		for (int i = 0; i < points.getSize()-1; i++) addSegment((Point3d)points.get(i), (Point3d)points.get(i+1), Colors.blue);
		if (box != null) addBox(box);

/*		double delta = 0.0;
		double inc = 0.001;
		while(delta < 10) {
			Transform3D sphereTransform = new Transform3D();
			delta = delta+inc;
			sphereTransform.setScale(0.2 + delta);		
			sphereTransform.setTranslation(new javax.vecmath.Vector3d(0.8, 0, 0)); 

		    transformGroups[0].setTransform(sphereTransform);
		    repaint();
		}
*/
		
		//key behavior
	    KeyNavigatorBehavior behavior = new KeyNavigatorBehavior(spin);
	    BoundingSphere bounds = new BoundingSphere();
	    behavior.setSchedulingBounds(bounds);
	    spin.addChild(behavior);
	    
	    //mouse-directed rotation
	    MouseRotate rotator = new MouseRotate(spin);
	    rotator.setSchedulingBounds(bounds);
	    spin.addChild(rotator);
	    
	    //mouse-based translation
	    MouseTranslate translator = new MouseTranslate(spin);
	    translator.setSchedulingBounds(bounds);
	    spin.addChild(translator);
	    
	    //mouse-directed zoom
	    MouseZoom zoom = new MouseZoom();
	    zoom.setSchedulingBounds(bounds);
	    spin.addChild(zoom);
	    
	    //light and background
	    
		 //make background green
		 Background background = new Background(Colors.green);
		 background.setApplicationBounds(bounds);
		 root.addChild(background);

	   return root;

	  }

	 public void actionPerformed(ActionEvent actionEvent) {
		 String cmd = actionEvent.getActionCommand();
	 }
	 
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	    new MainFrame(new Viewer3d(null, null), 1280, 960);
	}


}
