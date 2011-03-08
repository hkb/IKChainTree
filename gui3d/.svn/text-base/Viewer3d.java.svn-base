package gui3d;
import javax.vecmath.*;
import javax.vecmath.Vector3d;
import java.awt.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.universe.*;
import java.applet.*;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.mouse.*;
import com.sun.j3d.utils.behaviors.keyboard.*;
import com.sun.j3d.utils.geometry.Sphere;
import geom3d.*;
import geom3d.Point3d;

public class Viewer3d extends Applet {
	private dcelShape3d shape;
	private PointSet3d points;
	
	public Viewer3d(dcelShape3d shape, PointSet3d points) {
		super();
		this.shape = shape;
		this.points = points;
	}
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

	 private BranchGroup createSceneGraph() {
	    BranchGroup root = new BranchGroup();
	    
	    TransformGroup spin = new TransformGroup();
	    spin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	    spin.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	    root.addChild(spin);
	    
	    //rotating object
	    Appearance ap = new Appearance();
	    ap.setMaterial(new Material());
	    ap.setPolygonAttributes(new PolygonAttributes(PolygonAttributes.POLYGON_LINE, PolygonAttributes.CULL_NONE,0));
	    ap.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.NONE,0.0f));
	    shape.setAppearance(ap);
	    Transform3D tr = new Transform3D();
	    tr.setScale(1.0);
	    TransformGroup tg = new TransformGroup(tr);
	    spin.addChild(tg);
	    tg.addChild(shape);
	    
	    
	    
	    
	    // Create a simple shape leaf node, add it to the scene graph.
	    Appearance textAppear = new Appearance();
	    ColoringAttributes textColor = new ColoringAttributes();
	    textColor.setColor(1.0f, 0.0f, 0.0f);
	    textAppear.setColoringAttributes(textColor);
	    textAppear.setMaterial(new Material());
	    
	    Font3D font3D = new Font3D(new Font("Helvetica", Font.PLAIN, 1),
	        new FontExtrusion());
	    

		int size = points.getSize();
		for (int i = 0; i < size; i++) {
			Point3d p = (Point3d)points.get(i);
			Sphere sphere = new Sphere(0.003f);
			Appearance sphereAppear = new Appearance();
			sphereAppear.setColoringAttributes(new ColoringAttributes(new Color3f(0.0f,0.0f,1.0f),ColoringAttributes.NICEST));
			sphere.setAppearance(sphereAppear);
			sphere.addChild(new Point3dShape());
			Transform3D sphereTransform = new Transform3D();
//			sphereTransform.setScale(0.2);
			sphereTransform.setTranslation(new Vector3d(p.getX(), p.getY(), p.getZ()));
			tg = new TransformGroup(sphereTransform);
			spin.addChild(tg);
			tg.addChild(sphere);
			
			Text3D label = new Text3D(font3D, new String(String.valueOf(i)));
		    label.setAlignment(Text3D.ALIGN_CENTER);
		    Shape3D textShape = new Shape3D();
		    textShape.setGeometry(label);
		    textShape.setAppearance(textAppear);
		    Transform3D labelTransform = new Transform3D();
		    labelTransform.setScale(0.05);
			labelTransform.setTranslation(new Vector3d(p.getX()+0.02, p.getY()+0.02, p.getZ()+0.02));
			tg = new TransformGroup(labelTransform);
			
//		    spin.addChild(tg);
//		    tg.addChild(textShape);

		}
		// line pattern dot
		size = points.getSize();
	    Point3f[] dotPts = new Point3f[2*size-2];
	    Point3d p;
	    for (int i=0; i< size-1; i++) {
	    	p = (Point3d)points.get(i);
	    	dotPts[2*i] = new Point3f(new Float(p.getX()),new Float(p.getY()),new Float(p.getZ()));
	    	p = (Point3d)points.get(i+1);
	    	dotPts[2*i+1] = new Point3f(new Float(p.getX()),new Float(p.getY()),new Float(p.getZ()));
	    }
	    LineArray dot = new LineArray(2*size-2, LineArray.COORDINATES);
	    dot.setCoordinates(0, dotPts);
	    LineAttributes dotLa = new LineAttributes();
	    dotLa.setLineWidth(2.0f);
//	    dotLa.setLinePattern(LineAttributes.PATTERN_DOT);
	    Appearance dotApp = new Appearance();
	    dotApp.setLineAttributes(dotLa);
//	    Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
	    ColoringAttributes ca = new ColoringAttributes(new Color3f(0.0f,0.0f,1.0f),
	            ColoringAttributes.SHADE_FLAT);
	    dotApp.setColoringAttributes(ca);
	    Shape3D dotShape = new Shape3D(dot, dotApp);
	    TransformGroup objTrans = new TransformGroup();
	    spin.addChild(objTrans);

	    // Add the primitives to the scene
	    objTrans.addChild(dotShape);
	    

	    
	    
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
	    Background background = new Background(1.0f, 1.0f, 0.0f);
	    background.setApplicationBounds(bounds);
	    root.addChild(background);
	    AmbientLight light = new AmbientLight(true, new Color3f(Color.red));
	    light.setInfluencingBounds(bounds);
//	    root.addChild(light);
	    PointLight ptlight = new PointLight(new Color3f(Color.red), new Point3f(0f,0f,-5f), new Point3f(1f,0f,0f));
	    ptlight.setInfluencingBounds(bounds);
	    root.addChild(ptlight);
	    PointLight ptlight2 = new PointLight(new Color3f(Color.orange), new Point3f(-2f,2f,2f), new Point3f(1f,0f,0f));
	    ptlight2.setInfluencingBounds(bounds);
//	    root.addChild(ptlight2);
	    return root;
	  }

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		dcelShape3d shape = null;
	    new MainFrame(new Viewer3d(null, null), 1280, 960);
	}

}
