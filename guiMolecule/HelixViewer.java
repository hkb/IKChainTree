package guiMolecule;

import molecule.*;
import geom3d.*;
import misc.*;
import java.applet.*;	
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.Enumeration;

import javax.media.j3d.*;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;

import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class HelixViewer extends Applet implements ActionListener {
	private AlphaHelix helix;
	private Protein protein;
	private BranchGroup root;
	private TransformGroup spin = new TransformGroup();

	

	public HelixViewer() {
		super();
		AlphaHelix helix = null;
	}
	
	
	public void init() {
		setLayout(new BorderLayout());
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(1,1));
		add(p, BorderLayout.SOUTH);
		JButton button = new JButton("Run");
		p.add(button);
		button.addActionListener(this);

		// create canvas
	    GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
	    Canvas3D cv = new Canvas3D(gc);
	    add(cv, BorderLayout.CENTER);
	    root = createSceneGraph();
	    
	    SimpleUniverse su = new SimpleUniverse(cv);
	    su.getViewingPlatform().setNominalViewingTransform();
	    su.addBranchGraph(root);
	}
	private void createSpinTG() {
		spin = new TransformGroup();
		spin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		spin.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	    spin.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
		spin.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		spin.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
	}
	
	 private BranchGroup createSceneGraph() {
		 BranchGroup root = new BranchGroup();
		 root.setCapability(BranchGroup.ALLOW_DETACH);
		 createSpinTG();
		 root.addChild(spin);
		 SimpleBehavior myRotationBehavior = new SimpleBehavior(spin);
		 myRotationBehavior.setSchedulingBounds(new BoundingSphere());
		 root.addChild(myRotationBehavior);
		 
		 KeyNavigatorBehavior behavior = new KeyNavigatorBehavior(spin);
		 BoundingSphere bounds = new BoundingSphere();
		 behavior.setSchedulingBounds(bounds);
		 root.addChild(behavior);
		 
		 //mouse-directed rotation
		 MouseRotate rotator = new MouseRotate(spin);
		 rotator.setSchedulingBounds(bounds);
		 root.addChild(rotator);  
		 
		 //mouse-based translation
		 MouseTranslate translator = new MouseTranslate(spin);
		 translator.setSchedulingBounds(bounds);
		 root.addChild(translator);
		 
		 //mouse-directed zoom
		 MouseZoom zoom = new MouseZoom();
		 zoom.setSchedulingBounds(bounds);
		 root.addChild(zoom);
		 
		 //make background blue
		 Background background = new Background(Colors.blue);
		 background.setApplicationBounds(bounds);
		 root.addChild(background);
		 
		 // light
		 AmbientLight light = new AmbientLight(true, new Color3f(Color.red));
		 light.setInfluencingBounds(bounds);
		 root.addChild(light);
		 PointLight ptlight = new PointLight(new Color3f(Color.red), new Point3f(0f,0f,-5f), new Point3f(1f,0f,0f));
		 ptlight.setInfluencingBounds(bounds);
		 root.addChild(ptlight);
		 PointLight ptlight2 = new PointLight(new Color3f(Color.orange), new Point3f(-2f,2f,2f), new Point3f(1f,0f,0f));
		 ptlight2.setInfluencingBounds(bounds);
	     root.addChild(ptlight2);
	     
	     // let Java3d perform optimization on this scene graph
	     root.compile();
		 return root;
	 }
	 
	 public void addSphere(Point3d p, double radius, Color3f color) {
		 BranchGroup bgSphere = new BranchGroup();
		 Appearance ap = new Appearance();
		 ap.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_READ);
		 ap.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
		 ap.setColoringAttributes(new ColoringAttributes(color, ColoringAttributes.NICEST));
		 Sphere sphere = new Sphere(new Float(radius),ap);
		 sphere.getShape().setCapability(Shape3D.ALLOW_APPEARANCE_READ);
		 sphere.getShape().setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
		 Transform3D tr = new Transform3D();
		 tr.setTranslation(new javax.vecmath.Vector3d(p.getX(), p.getY(), p.getZ()));
		 TransformGroup tgSphere = new TransformGroup(tr);
		 tgSphere.addChild(sphere);
		 bgSphere.addChild(tgSphere);
		 spin.addChild(bgSphere);
	 }
	 
	 public void actionPerformed(ActionEvent actionEvent) {
		 /*//Outcommented .. there were errors 
		 String cmd = actionEvent.getActionCommand();
		 if ("Run".equals(cmd)) {
			 helix = new AlphaHelix(10,"NCaC");
			 helix.setViewer(this);
			 helix.setPositions();
			 AminoAcid aminoAcid = helix.getFirstAminoAcid();
			 while (aminoAcid != null) {
				addSphere(aminoAcid.getPosition(), Carbon.getAtomicRadius(), Colors.red);
				if (aminoAcid.hasCore())
				{
					Core core = aminoAcid.getCore();
					addSphere(core.getN().getPosition(), Nitrogen.getAtomicRadius(), Colors.black);
					addSphere(core.getC().getPosition(), Carbon.getAtomicRadius(), Colors.magenta);
					if (core.hasO()) {
						addSphere(core.getO().getPosition(), 0.1f, Colors.white);
						if (core.hasH()) {
							addSphere(core.getHN().getPosition(), 0.05f, Colors.gray);
							addSphere(core.getHCa().getPosition(), 0.05f, Colors.gray);
						}
					}
				}
				aminoAcid = aminoAcid.getNext();
			 }
			 System.out.println(Functions.toRadians(Vector3d.getDihedralAngle(new Vector3d(-1,1,0), new Vector3d(0,0,1), new Vector3d(0,1,0))));
			 helix.printDihedralAngles();
		 }
		 */
	 }
	 
	 public class SimpleBehavior extends Behavior {
		 private TransformGroup targetTG;
		 private Transform3D rotation = new Transform3D();

		 private double angle = 0.0;
		 
		 SimpleBehavior(TransformGroup targetTG) { 
			 this.targetTG = targetTG; 
		 }
		 
		 public void initialize() {
			 this.wakeupOn(new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED));
		 }
		 
		 
		 public void processStimulus(Enumeration criteria) {
			 WakeupCriterion wakeupInstance;
			 while (criteria.hasMoreElements()) {
				 wakeupInstance = (WakeupCriterion)criteria.nextElement();
				 if (wakeupInstance instanceof WakeupOnAWTEvent) {
					 AWTEvent[] evt = ((WakeupOnAWTEvent)wakeupInstance).getAWTEvent();
					 int key = ((KeyEvent)evt[0]).getKeyCode();
					 if (key == KeyEvent.VK_P) {
					 }
					 if (key == KeyEvent.VK_N) {
//						 updater = new ShapeUpdater();
//						 graphicShape.updateData(updater);
					 }
					 if (key == KeyEvent.VK_X) {
						 angle += 0.1;
						 rotation.rotX(angle);
						 targetTG.setTransform(rotation);
					 }
					 if (key == KeyEvent.VK_Y) {
						 angle += 0.1;
						 rotation.rotY(angle);
						 targetTG.setTransform(rotation);
					 }
					 if (key == KeyEvent.VK_Z) {
						 angle += 0.1;
						 rotation.rotZ(angle);
						 targetTG.setTransform(rotation);
					 }
				 }
			 }
			 this.wakeupOn(new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED));
		 }
	 }
}
