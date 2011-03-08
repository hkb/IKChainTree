package cells3d;

import geom3d.*; //InputPanelPointApplet;
import geom3d.Point3d;
/*import com.artchase.geom3d.InputPanelRandomApplet;
import com.artchase.geom3d.Point3d;
import com.artchase.geom3d.SelectPointPanelApplet;
import com.artchase.geom3d.SelectCellPanelApplet;
import com.artchase.geom3d.Sphere3d;
*/
import dataStructures.Set;
import misc.*;
import gui3d.*;
import dataStructures.Stack;

import javax.vecmath.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;
import java.util.Random;
import javax.media.j3d.*;
import javax.swing.*;
import com.sun.j3d.utils.universe.*;
import java.applet.*;	
import com.sun.j3d.utils.behaviors.mouse.*;
import com.sun.j3d.utils.behaviors.keyboard.*;
import com.sun.j3d.utils.geometry.Sphere;

import java.io.*; //FileWriter;
//import java.io.IOException;
//import java.io.StreamTokenizer;


public class Viewer3d extends Applet implements ActionListener {
	private Cells3d cells;
	private Appearance ap;
//	private Shape3D shape = null;
	private CellShape3d graphicShape = null; 
	private ShapeUpdater updater = null;
	private BranchGroup root;
	private TransformGroup spin = new TransformGroup();
	
	private Vertex selectedVertex = null;
	private Color3f selectedSphereColor = null;
	private Cell selectedCell = null;
	private Color3f selectedTetrahedronColor = null;
	
	private Random randGen = new Random();
	
	private JTextField nrSpannedVerticesField;
	private JTextField nrEdgesField;
	private JTextField nrFacesField;
	private JTextField nrCellsField;
	
	private boolean circumCircleShown = false;
	private BranchGroup circumCircleBranchGroup = null;
	
	private Stack toProcess;
	private int selectedAdjCellIndex;
	private Cell selectedAdjCell = null;
	
	public Viewer3d(Cells3d cells) {
		super();
		this.cells = cells;
		cells.setViewer(this);
		toProcess = new Stack();
	}
	
	public Cells3d getCells() { return cells; }
	public int getNrSpannedVerticesField() { return Integer.parseInt(nrSpannedVerticesField.getText()); }
	public int getNrEdgesField() { return Integer.parseInt(nrEdgesField.getText()); }
	public int getNrFacesField() { return Integer.parseInt(nrFacesField.getText()); }
	public int getNrCellsField() { return Integer.parseInt(nrCellsField.getText()); }
	public void setNrSpannedVerticesField(int n) { nrSpannedVerticesField.setText(String.valueOf(n)); }
	public void setNrEdgesField(int n) { nrEdgesField.setText(String.valueOf(n)); }
	public void setNrFacesField(int n) { nrFacesField.setText(String.valueOf(n)); }
	public void setNrCellsField(int n) { nrCellsField.setText(String.valueOf(n)); }
	
	public Vertex getSelectedVertex() { return selectedVertex; }
	public Cell getSelectedCell() { return selectedCell; }
	public Sphere getSelectedSphere() { return getSelectedVertex().getSphere(); }
	public void setSelectedVertex(Vertex v) { selectedVertex = v; }
	public void setSelectedCell(Cell cell) { selectedCell = cell; }
	public void setSelectedSphere(Sphere sphere) { getSelectedVertex().setSphere(sphere); } 
	public Color3f getSelectedSphereColor() { return selectedSphereColor; }
	public void setSelectedSphereColor(Color3f color) { selectedSphereColor = color; }

	public Cell getSelectedAdjCell() { return selectedAdjCell; }
	public void setSelectedAdjCell(Cell adjCell) { selectedAdjCell = adjCell; }
	public int getSelectedAdjCellIndex() { return selectedAdjCellIndex; }
	public void setSelectedAdjCellIndex(int index) { selectedAdjCellIndex = index; }
	public Shape3D getSelectedTetrahedron() { return selectedCell.getTetrahedron(); }
	public void setSelectedTetrahedron(Shape3D selectedTetrahedron) { selectedCell.setTetrahedron(selectedTetrahedron); } 
	public Color3f getSelectedTetrahedronColor() { return selectedTetrahedronColor; }
	public void setSelectedTetrahedronColor(Color3f color) { selectedTetrahedronColor = color; }

	
	public void init() {
		// create panels
		setLayout(new BorderLayout());
		add(setUpEastPanel(),  BorderLayout.EAST);
		add(setUpSouthPanel(), BorderLayout.SOUTH);
		
		// create canvas
	    GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
	    Canvas3D cv = new Canvas3D(gc);
	    add(cv, BorderLayout.CENTER);
	    root = createSceneGraph();
	    
	    // create universe
	    SimpleUniverse su = new SimpleUniverse(cv);
	    su.getViewingPlatform().setNominalViewingTransform();
	    su.addBranchGraph(root);
	 }

	private JPanel setUpEastPanel() {
		JPanel p = new JPanel();               // east panel consists of one column with 5 rows
		p.setLayout(new GridLayout(5,1));
		p.add(new JLabel("Running Mode"));              // 1. row
		JButton button = new JButton("Test");
		p.add(button);									// 2. row
		button.addActionListener(this);
		button = new JButton("File");
		p.add(button);                                  // 3. row
		button.addActionListener(this);
		button = new JButton("PDB");
		p.add(button);                                  // 4. row
		button.addActionListener(this);
		p.add(setUpStatisticsPanel());                 //  5. row
		return p;
	}

	
	private JPanel setUpStatisticsPanel() {
		JPanel statistics = new JPanel();

		statistics.setLayout(new GridLayout(5,2));
		statistics.add(new JLabel("STATISTICS",SwingConstants.CENTER));

		JPanel nrSpannedVertices = new JPanel();
		nrSpannedVertices.setLayout(new GridLayout(1,2));
	
		JLabel nrSpannedVerticesLabel = new JLabel("vertices");
		nrSpannedVerticesField = new JTextField("0");
		nrSpannedVerticesField.setHorizontalAlignment(JTextField.RIGHT);
		nrSpannedVertices.add(nrSpannedVerticesLabel);
		nrSpannedVertices.add(nrSpannedVerticesField);

		statistics.add(nrSpannedVertices);
	
		JPanel nrEdges = new JPanel();
		nrEdges.setLayout(new GridLayout(1,2));

		JLabel nrEdgesLabel = new JLabel("edges");
		nrEdgesField = new JTextField("0");
		nrEdgesField.setHorizontalAlignment(JTextField.RIGHT);
		nrEdges.add(nrEdgesLabel);
		nrEdges.add(nrEdgesField);					
		
		statistics.add(nrEdges);
	
		JPanel nrFaces = new JPanel();
		nrFaces.setLayout(new GridLayout(1,2));

		JLabel nrFacesLabel = new JLabel("faces");
		nrFacesField = new JTextField("0");
		nrFacesField.setHorizontalAlignment(JTextField.RIGHT);
		nrFaces.add(nrFacesLabel);
		nrFaces.add(nrFacesField);					
		
		statistics.add(nrFaces);

		JPanel nrCells = new JPanel();
		nrCells.setLayout(new GridLayout(1,2));

		JLabel nrCellsLabel = new JLabel("cells");
		nrCellsField = new JTextField("0");
		nrCellsField.setHorizontalAlignment(JTextField.RIGHT);
		nrCells.add(nrCellsLabel);
		nrCells.add(nrCellsField);					
		
		statistics.add(nrCells);
		return statistics;
	}
	
	private JPanel setUpSouthPanel() {
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(1,10,10,5));
		JPanel addPointPanel = new JPanel();
		addPointPanel.setLayout(new GridLayout(3,1));
		JLabel addPointLabel = new JLabel("Add Point", SwingConstants.CENTER);
		addPointPanel.add(addPointLabel);
		JButton button = new JButton("Console");
		button.addActionListener(this);
		addPointPanel.add(button);
		button = new JButton("Random Point");
		button.addActionListener(this);
		addPointPanel.add(button);
		p.add(addPointPanel);

		JPanel selectPanel = new JPanel();
		selectPanel.setLayout(new GridLayout(3,1));
		JLabel selectPanelLabel = new JLabel("Select", SwingConstants.CENTER);
		selectPanel.add(selectPanelLabel);
		button = new JButton("Point");
		button.setActionCommand("Select_Point");
		button.addActionListener(this);
		selectPanel.add(button);
		button = new JButton("Cell");
		button.setActionCommand("Select_Cell");
		button.addActionListener(this);
		selectPanel.add(button);
		p.add(selectPanel);

		JPanel selectedPointPanel = new JPanel();
		selectedPointPanel.setLayout(new GridLayout(3,1));
		JLabel selectedPointLabel = new JLabel("Point Selected", SwingConstants.CENTER);
		selectedPointPanel.add(selectedPointLabel);
		button = new JButton("Move");
		button.setActionCommand("Selected_Point_Move");
		button.addActionListener(this);
		selectedPointPanel.add(button);
		p.add(selectedPointPanel);

		JPanel selectedCellPanel = new JPanel();
		selectedCellPanel.setLayout(new GridLayout(4,1));
		JLabel selectedCellLabel = new JLabel("Cell Selected", SwingConstants.CENTER);
		selectedCellPanel.add(selectedCellLabel);
		button = new JButton("Adjacent Cell");
		button.setActionCommand("Selected_Cell_Adjacent");
		button.addActionListener(this);
		selectedCellPanel.add(button);
		button = new JButton("Circumcircle");
		button.setActionCommand("Selected_Cell_Circumcircle");
		button.addActionListener(this);
		selectedCellPanel.add(button);
		p.add(selectedCellPanel);

		button = new JButton("Next Cell");
		p.add(button);
		button.addActionListener(this);

		button = new JButton("Random");
		p.add(button);
		button.addActionListener(this);

		button = new JButton("Clear");
		p.add(button);
		button.addActionListener(this);

		button = new JButton("Save");
		p.add(button);
		button.addActionListener(this);

		button = new JButton("Load");
		p.add(button);
		button.addActionListener(this);
		return p;
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

/*		 
		 ap = new Appearance();
		 ap.setCapability(Appearance.ALLOW_POINT_ATTRIBUTES_WRITE);
		 ap.setCapability(Appearance.ALLOW_LINE_ATTRIBUTES_WRITE);
		 ap.setCapability(Appearance.ALLOW_POLYGON_ATTRIBUTES_WRITE);
		 ap.setCapability(Appearance.ALLOW_RENDERING_ATTRIBUTES_WRITE);
		 ap.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
		 ap.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
		 ap.setMaterial(new Material());
		 ap.setPolygonAttributes(new PolygonAttributes(PolygonAttributes.POLYGON_LINE, PolygonAttributes.CULL_NONE,0));
		 ap.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.NONE,0.0f));

		 graphicShape = new cellShape3d(cells,199);
		 shape = new Shape3D(graphicShape, ap);
		 graphicShape.setCapability(GeometryArray.ALLOW_REF_DATA_READ);
		 graphicShape.setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);
		 graphicShape.setCapability(IndexedTriangleArray.ALLOW_COLOR_READ);
		 graphicShape.setCapability(IndexedTriangleArray.ALLOW_COLOR_WRITE);
		 graphicShape.setCapability(GeometryArray.ALLOW_COORDINATE_READ);
		 graphicShape.setCapability(GeometryArray.ALLOW_COORDINATE_WRITE);

*/		 
/*		 Transform3D tr = new Transform3D();
		 tr.setScale(0.25);
		 TransformGroup tg = new TransformGroup(); //tr);
		 spin.addChild(tg);
		 tg.addChild(shape);
*/		 
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
	 
	 public void actionPerformed(ActionEvent actionEvent) {
		 String cmd = actionEvent.getActionCommand();
		 if ("Console".equals(cmd)) {                   // opens a window to read the coordinates of a point that is added to the point set.
			 //new InputPanelPointApplet(this);
		 }
		 else if ("Select_Point".equals(cmd)) {         // opens a window with the list of points. One is to be selected.
			 new SelectPointPanelApplet(this);
		 }
		 else if ("Select_Cell".equals(cmd)) {          // opens a window with the list of cells. One is to be selected.
			new SelectCellPanelApplet(this); 
		 }
		 else if ("Random Point".equals(cmd)) {         // adds random point to the point set
			 geom3d.Point3d pointToAdd = new geom3d.Point3d(randGen);
			 int size = getCells().getVertices().getSize();
			 Vertex v = new Vertex(pointToAdd, size);
			 getCells().getVertices().insert(v);
			 addSphere(v, 0.02f, Colors.red);
			 getCells().setNrVertices(size+1);
		 }
		 else if ("Selected_Point_Move".equals(cmd)) {
///			 new SelectedPointMovePanel(this);
		 }
		 else if ("Selected_Cell_Adjacent".equals(cmd)) {
			 if (selectedCell != null) {
				 System.out.println("selectedCell is not null");
				 if (selectedAdjCell != null) {
					 System.out.println("adjacentCellShown is TRUE");
					 selectedAdjCell.changeAppearance(1, LineAttributes.PATTERN_SOLID, Colors.yellow);
					 selectedAdjCell = null;
				 }
				 System.out.println("adjacentCellShown is FALSE");
				 if (cells.getNrCells() > 0) {
					 System.out.println("Number of cells is " + cells.getNrCells());
					 int nrIter = 0;
					 do {                                        
						 nrIter++;
						 selectedAdjCellIndex = (++selectedAdjCellIndex)%4;
						 System.out.println("selectedAdjCellIndex = " + selectedAdjCellIndex);
						 selectedAdjCell = selectedCell.getAdjCell(selectedAdjCellIndex);
					 } while (selectedAdjCell.isInfiniteCell() && (nrIter < 4));
					 if (!selectedAdjCell.isInfiniteCell()) {         // adjacent cell found
						 System.out.println("i am here displaying cell " + selectedAdjCellIndex);
						 selectedAdjCell.changeAppearance(5, LineAttributes.PATTERN_SOLID, Colors.magenta);
					 } else selectedAdjCell = null;                   // all adjacent cells are infinite
				 }
			 }
		 }
		 else if ("Selected_Cell_Circumcircle".equals(cmd)) {
			 if (circumCircleShown) {
				 circumCircleBranchGroup.detach();
				 circumCircleShown = false;
			 }
			 else {
				 geom3d.Point3d[] points = selectedCell.getPoints();
				 Sphere3d sphere = new Sphere3d(points[0],points[1],points[2],points[3]);
				 geom3d.Point3d center = sphere.getCenter();
				 circumCircleBranchGroup = addSphere(center, sphere.getRadius(), Colors.green);
				 circumCircleShown = true;
			 }
		 }
		 else if ("Random".equals(cmd)) {
			 cells.clear();
			 spin.removeAllChildren();
			 setNrSpannedVerticesField(0);
			 setNrEdgesField(0);
			 setNrFacesField(0);
			 setNrCellsField(0);
			 //Open window to get the number of random points to be generated.
			 new InputPanelRandomApplet(this);
		 }
		 else if ("Clear".equals(cmd)) {
			 cells.clear();
			 spin.removeAllChildren();
			 setNrSpannedVerticesField(0);
			 setNrEdgesField(0);
			 setNrFacesField(0);
			 setNrCellsField(0);
			 selectedCell = null;
			 selectedAdjCell = null;
		 }
		 else if ("Next Cell".equals(cmd)) {
			 if ((cells.getNrCells() == 0) && (cells.getNrVertices() >= 4)) {
				 cells.firstCell();
				 addTetrahedron(cells.getCell(0), Colors.yellow);				 
			 }
			 else {
				 if (toProcess.isEmpty()) {
					 int i = cells.getNrSpannedVertices();
					 if (i < cells.getNrVertices()) toProcess = cells.nextCell(i);
				 }
				 else cells.flipAttempt(toProcess);
			 }
		 }
		 else if ("Save".equals(cmd)) {
			 try {
				 BufferedWriter bw = new BufferedWriter(new FileWriter("data"));
				 PrintWriter out = new PrintWriter(bw);
				 int size = cells.getNrVertices();
				 Point3d p;
				 for (int i = 0; i < size; i++) {
					 p = ((Vertex)cells.getVertices().get(i)).getPoint();
					 out.print(p.getX() + " " + p.getY() + " " + p.getZ() + '\n'); 
				 }
				 out.close();
			 }
			 catch (IOException e) { System.out.println("IOException error!"); e.printStackTrace(); }
		 }
		 else if ("Load".equals(cmd)) {
			 PointSet3d points = new PointSet3d("data");
			 int size  = points.getSize();
			 Point3d point;
			 for (int i = 0; i < size; i++) {
				 point = (Point3d)points.get(i);
				 Vertex v = new Vertex(point, i);
				 getCells().getVertices().insert(v);
				 addSphere(v, 0.02f, Colors.red);
			}
			getCells().setNrVertices(size);
			System.out.println("Size: " + size); }
		 else if ("PDB".equals(cmd)) {
			 cells.clear();
			 spin.removeAllChildren();
			 setNrSpannedVerticesField(0);
			 setNrEdgesField(0);
			 setNrFacesField(0);
			 setNrCellsField(0);
			 //Open window to get the name of the pdb-file.
			 new InputPanelPDBApplet(this);
		 }
		 else if ("Point".equals(cmd)) {
			 ap.setPolygonAttributes(new PolygonAttributes(PolygonAttributes.POLYGON_POINT, PolygonAttributes.CULL_BACK, 0));
			 ap.setPointAttributes(new PointAttributes(5, false)); }
		 else if ("Line".equals(cmd)) {
			 ap.setPolygonAttributes(new PolygonAttributes(PolygonAttributes.POLYGON_LINE, PolygonAttributes.CULL_BACK, 0));
			 ap.setLineAttributes(new LineAttributes(2, LineAttributes.PATTERN_DASH,false)); }
		 else if ("Polygon".equals(cmd)) {
			 ap.setPolygonAttributes(new PolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_BACK, 0)); }
		 else if ("Gouraud".equals(cmd)) {
			 ColoringAttributes ca = new ColoringAttributes();
			 ca.setShadeModel(ColoringAttributes.SHADE_GOURAUD);
			 ap.setColoringAttributes(ca);
			 ap.setMaterial(null);
			 RenderingAttributes ra = new RenderingAttributes();
			 ra.setIgnoreVertexColors(false);
			 ap.setRenderingAttributes(ra); }
		 else if ("Lighting".equals(cmd)) {
			 ap.setMaterial(new Material());
			 RenderingAttributes ra = new RenderingAttributes();
			 ra.setIgnoreVertexColors(true);
			 ap.setRenderingAttributes(ra); }
	 }
	 
	 public void addSphere(Vertex v, double radius, Color3f color) {
		 geom3d.Point3d p = v.getPoint();
		 BranchGroup bgSphere = new BranchGroup();
		 bgSphere.setCapability(BranchGroup.ALLOW_DETACH);
		 Appearance ap = new Appearance();
		 ap.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_READ);
		 ap.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
		 ap.setColoringAttributes(new ColoringAttributes(color, ColoringAttributes.NICEST));
		 Sphere sphere = new Sphere(new Float(radius),ap);
		 sphere.getShape().setCapability(Shape3D.ALLOW_APPEARANCE_READ);
		 sphere.getShape().setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
		 v.setSphere(sphere);
		 Transform3D tr = new Transform3D();
		 tr.setTranslation(new javax.vecmath.Vector3d(p.getX(), p.getY(), p.getZ()));
		 TransformGroup tgSphere = new TransformGroup(tr);
		 tgSphere.addChild(sphere);
		 bgSphere.addChild(tgSphere);
		 spin.addChild(bgSphere);
	 }

	 public BranchGroup addSphere(geom3d.Point3d p, double radius, Color3f color) {
		 BranchGroup bgSphere = new BranchGroup();
		 bgSphere.setCapability(BranchGroup.ALLOW_DETACH);
		 Appearance ap = new Appearance();
		 ap.setColoringAttributes(new ColoringAttributes(color, ColoringAttributes.NICEST));
		 ap.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.NICEST, 0.5f));
		 Sphere sphere = new Sphere(new Float(radius),ap);
		 Transform3D tr = new Transform3D();
		 tr.setTranslation(new javax.vecmath.Vector3d(p.getX(), p.getY(), p.getZ()));
		 TransformGroup tgSphere = new TransformGroup(tr);
		 tgSphere.addChild(sphere);
		 bgSphere.addChild(tgSphere);
		 spin.addChild(bgSphere);
		 return bgSphere;
	 }

	 
	 public BranchGroup addTetrahedron(Cell cell, Color3f color) {

		 Point3d p0 = cell.getPoint(0);
		 Point3d p1 = cell.getPoint(1);
		 Point3d p2 = cell.getPoint(2);
		 Point3d p3 = cell.getPoint(3);
		
		 BranchGroup bgTetrahedron = new BranchGroup();
		 bgTetrahedron.setCapability(BranchGroup.ALLOW_DETACH);

		 javax.vecmath.Point3d[] coords = new javax.vecmath.Point3d[12];
		 LineArray la = new LineArray(12, LineArray.COORDINATES | LineArray.COLOR_3);
		 
		 coords[0] = coords[2] = coords[4] = new javax.vecmath.Point3d(p0.getX(), p0.getY(), p0.getZ());
		 coords[1] = coords[6] = coords[8] = new javax.vecmath.Point3d(p1.getX(), p1.getY(), p1.getZ());
		 coords[3] = coords[7] = coords[10]= new javax.vecmath.Point3d(p2.getX(), p2.getY(), p2.getZ());
		 coords[5] = coords[9] = coords[11]= new javax.vecmath.Point3d(p3.getX(), p3.getY(), p3.getZ());

		 la.setCoordinates(0, coords);
		 Color3f[] colors = new Color3f[12];
		 for (int i = 0; i < 12; i++) colors[i] = Colors.black;
		 la.setColors(0, colors);
		 
		 javax.vecmath.Point3d[] coordsTr = new javax.vecmath.Point3d[24];
		 TriangleArray tr = new TriangleArray(24, TriangleArray.COORDINATES | TriangleArray.COLOR_3);
		 coordsTr[0] = coordsTr[3] = coordsTr[6] = new javax.vecmath.Point3d(p0.getX(), p0.getY(), p0.getZ());
		 coordsTr[1] = coordsTr[4] = coordsTr[9] = new javax.vecmath.Point3d(p1.getX(), p1.getY(), p1.getZ());
		 coordsTr[2] = coordsTr[7] = coordsTr[10]= new javax.vecmath.Point3d(p2.getX(), p2.getY(), p2.getZ());
		 coordsTr[5] = coordsTr[8] = coordsTr[11]= new javax.vecmath.Point3d(p3.getX(), p3.getY(), p3.getZ());

		 coordsTr[12] = coordsTr[15] = coordsTr[18] = new javax.vecmath.Point3d(p0.getX(), p0.getY(), p0.getZ());
		 coordsTr[14] = coordsTr[17] = coordsTr[21] = new javax.vecmath.Point3d(p1.getX(), p1.getY(), p1.getZ());
		 coordsTr[13] = coordsTr[20] = coordsTr[23]= new javax.vecmath.Point3d(p2.getX(), p2.getY(), p2.getZ());
		 coordsTr[16] = coordsTr[19] = coordsTr[22]= new javax.vecmath.Point3d(p3.getX(), p3.getY(), p3.getZ());

		 tr.setCoordinates(0, coordsTr);
		 Color3f[] colorsTr = new Color3f[24];
		 for (int i = 0; i < 24; i++) colorsTr[i] = Colors.green;
		 tr.setColors(0, colorsTr);
		 
		 Appearance ap = new Appearance();
		 ap.setCapability(Appearance.ALLOW_LINE_ATTRIBUTES_READ);
		 ap.setCapability(Appearance.ALLOW_LINE_ATTRIBUTES_WRITE);
		 ap.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_READ);
		 ap.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
//		 ap.setCapability(Appearance.ALLOW_MATERIAL_READ);
//		 ap.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
		 ap.setLineAttributes(new LineAttributes(2, LineAttributes.PATTERN_SOLID, false));
		 ap.setMaterial(new Material());
		 ap.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.SCREEN_DOOR, 0.5f));
		 
//		 LineArray la = cell.getLineArray();
		 Shape3D shape = new Shape3D(la,ap);
		 Shape3D shapeTr = new Shape3D(tr,ap);

		 shape.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
		 shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
		 cell.setTetrahedron(shape);
//		 cell.setLineArray(la);
		 cell.setTriangleArray(tr);
		 cell.setBgTetrahedron(bgTetrahedron);

		 TransformGroup tgTetrahedron = new TransformGroup();
		 tgTetrahedron.addChild(shape);
		 tgTetrahedron.addChild(shapeTr);

		 bgTetrahedron.addChild(tgTetrahedron);
		 spin.addChild(bgTetrahedron);
		 return bgTetrahedron;
	 }
	
	 public void addSpikes(geom3d.Point3d p0, 
				geom3d.Point3d p1, 
				geom3d.Point3d p2, 
				geom3d.Point3d p3,
				Color3f color) {
		 BranchGroup bgTetrahedron = new BranchGroup();
		 bgTetrahedron.setCapability(BranchGroup.ALLOW_DETACH);

		 javax.vecmath.Point3d[] coords = new javax.vecmath.Point3d[6];
		 LineArray la = new LineArray(8, LineArray.COORDINATES | LineArray.COLOR_3);
		 coords[0] = coords[2] = coords[4] = new javax.vecmath.Point3d(p0.getX(), p0.getY(), p0.getZ());
		 coords[1] = new javax.vecmath.Point3d(p1.getX(), p1.getY(), p1.getZ());
		 coords[3] = new javax.vecmath.Point3d(p2.getX(), p2.getY(), p2.getZ());
		 coords[5] = new javax.vecmath.Point3d(p3.getX(), p3.getY(), p3.getZ());
		 la.setCoordinates(0, coords);
		 la.setColor(0, color);

		 Appearance ap = new Appearance();
		 ap.setLineAttributes(new LineAttributes(2, LineAttributes.PATTERN_SOLID, false));
		 ap.setMaterial(new Material());

		 Shape3D shape = new Shape3D(la,ap);
		 TransformGroup tgTetrahedron = new TransformGroup();
		 tgTetrahedron.addChild(shape);

		 bgTetrahedron.addChild(tgTetrahedron);
		 spin.addChild(bgTetrahedron);
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
/*						 if (((KeyEvent)evt[0]).isAltDown()) targetTG.removeChild(bgSphere);
						 else {
							 com.artchase.geom3d.Point3d p4 = new com.artchase.geom3d.Point3d(1.02, 1.03, 0.24);
							 Sphere sphere = new Sphere(0.02f); 
							 Transform3D tr = new Transform3D();
							 tr.setTranslation(new Vector3d(p4.getX(), p4.getY(), p4.getZ()));
							 TransformGroup tgSphere = new TransformGroup(tr);
							 tgSphere.addChild(sphere);
							 bgSphere.addChild(tgSphere);
							 targetTG.addChild(bgSphere);
						 }
*/
					 }
					 if (key == KeyEvent.VK_N) {
						 updater = new ShapeUpdater();
						 graphicShape.updateData(updater);
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
	 
	 class ShapeUpdater implements GeometryUpdater {
		 public void updateData(Geometry geometry) {
			 Point3d vert = new Point3d();
//			 ((IndexedTriangleArray)geometry).getCoordinates(0,vert);
//			 int n = vert.length/3;
//			 ((IndexedTriangleArray)geometry).getCoordinate(0,vert);//RASMUS COMMENTED HERE
			 vert.scale(1.2);
//			 ((IndexedTriangleArray)geometry).setCoordinate(0, vert);//RASMUS COMMENTED HERE
//			 Color3f color = null;
			 Color3f red = new Color3f(1.0f, 0.0f, 0.0f);
			 Color3f green = new Color3f(0.0f, 1.0f, 0.0f);
			 ((IndexedTriangleArray)geometry).setColor(0,red);
			 ((IndexedTriangleArray)geometry).setColor(1,green);
			 ((IndexedTriangleArray)geometry).setColor(2,green);

/*			 ((GeometryArray)geometry).getColors(0, colors);
			 colors[0] = new Color3f(1,0,0);
			 colors[1] = new Color3f(0,1,0);
			 colors[2] = new Color3f(0,0,1);
*/

//			 setColors(colors);
		 }
	 }

	 
}

