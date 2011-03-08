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
import java.io.File;

import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;

import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class ProteinViewer extends Applet implements ActionListener {
	private Protein        protein;
	private BranchGroup    root;
	private TransformGroup spin = new TransformGroup();
	private Shape3D        globalShape;
	private SimpleUniverse su;
	private JFileChooser   fileChooser = null;
	
	
	private ProteinViewerMenu pvm;
	private Canvas3D cv;
	
	private JButton button;
	
	JMenuItem showHideAlpha;
	JMenuItem showHideBeta;
	JMenuItem showHideCoil;
	String isViewing = "";
	String isShowing = "";
	String model     = "Atoms";
	boolean isShowingHelices = false;
	boolean isShowingSheets  = false;
	boolean secondaryStructuresShown;

	public String getModel() { return model; }
	public TransformGroup getSpin() { return spin; }

	public ProteinViewer() {
		super();
		protein = null;
		secondaryStructuresShown = false;
	}
	
	
	public void init() {
	
		setLayout(new BorderLayout());
		
		// create menu
		pvm = new ProteinViewerMenu(this);
		add(pvm.getMenuBar(), BorderLayout.NORTH);

		// create canvas
	    GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
	    cv = new Canvas3D(gc);
	    add(cv, BorderLayout.CENTER);
	    root = createSceneGraph();
	    
	    
	    fileChooser = new JFileChooser("/Volumes/Home/pawel/Documents/MotherOfAllProjects/pdb_files/");
	    fileChooser.addChoosableFileFilter(new PDBFileFilter());

	    su = new SimpleUniverse(cv);
	    su.getViewingPlatform().setNominalViewingTransform();
	    su.addBranchGraph(root);
	 
	}
	
	private void clearMenu() {
		pvm.setJMenuItemLabel(showHideAlpha, "HIDE a-Helices");
		pvm.setJMenuItemLabel(showHideBeta, "HIDE b-Sheets");
		pvm.setJMenuItemLabel(showHideCoil, "HIDE Coil");
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
		 root = new BranchGroup();
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
		 
//		 Alpha rotationAlpha = new Alpha(-1, 10000);
//		 RotationInterpolator rotator1 = new RotationInterpolator(rotationAlpha, spin);
//		 rotator1.setSchedulingBounds(bounds);
//		 root.addChild(rotator1);

		 //make background green
		 Background background = new Background(Colors.green);
		 background.setApplicationBounds(bounds);
		 root.addChild(background);
		 
		 // light
		 AmbientLight light = new AmbientLight(true, new Color3f(Color.red));
		 light.setInfluencingBounds(bounds);
//		 root.addChild(light);
		 PointLight ptlight = new PointLight(new Color3f(Color.red), new Point3f(0f,0f,-5f), new Point3f(1f,0f,0f));
		 ptlight.setInfluencingBounds(bounds);
//		 root.addChild(ptlight);
		 PointLight ptlight2 = new PointLight(new Color3f(Color.orange), new Point3f(-2f,2f,2f), new Point3f(1f,0f,0f));
		 ptlight2.setInfluencingBounds(bounds);
//	     root.addChild(ptlight2);
	     
	     // let Java3d perform optimization on this scene graph
	     root.compile();
		 return root;
	 }
	 
	 public BranchGroup createSphere(Atom atom, double radius, AtomAppearance ap) {
		 BranchGroup bgSphere = new BranchGroup();
		 bgSphere.setCapability(BranchGroup.ALLOW_DETACH);
		 bgSphere.setCapability(BranchGroup.ALLOW_CHILDREN_READ);

		 Sphere sphere = new Sphere((float)radius, ap); 
		 sphere.getShape().setCapability(Shape3D.ALLOW_APPEARANCE_READ);
		 sphere.getShape().setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);

		 Transform3D tr = new Transform3D();
		 Point3d p = atom.getPosition();
		 tr.setTranslation(new javax.vecmath.Vector3d(p.getX(), p.getY(), p.getZ()));
		 TransformGroup tgSphere = new TransformGroup(tr);
		 tgSphere.addChild(sphere);
		 bgSphere.addChild(tgSphere);
		 return bgSphere;
	 }

	 public BranchGroup addSphere(Atom atom, double radius, AtomAppearance ap) {
		 BranchGroup bgSphere = createSphere(atom, radius, ap);
		 spin.addChild(bgSphere);
		 return bgSphere;
	 }
	 
	 //to be removed when sideChains are updated
	 public BranchGroup addSphere(Atom atom, double radius, Color3f color) {
		 BranchGroup bgSphere = new BranchGroup();
		 bgSphere.setCapability(BranchGroup.ALLOW_DETACH);
		 bgSphere.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		 Sphere sphere = null;
		 if (color.equals(Colors.gray)) sphere = new Sphere((float)Atom.getAtomicRadius(), AtomAppearance.alphaCarbon); 
		 else {
			 if (color.equals(Colors.black)) sphere = new Sphere((float)Atom.getAtomicRadius(), AtomAppearance.carbon); 
			 else {
				 if (color.equals(Colors.blue)) sphere = new Sphere((float)Atom.getAtomicRadius(), AtomAppearance.nitrogen); 
				 else {
					 if (color.equals(Colors.red)) sphere = new Sphere((float)Atom.getAtomicRadius(), AtomAppearance.oxygen); 
					 else {
						 if (color.equals(Colors.yellow)) sphere = new Sphere((float)Atom.getAtomicRadius(), AtomAppearance.sulphur); 
						 else {
							 if (color.equals(Colors.white)) sphere = new Sphere((float)Atom.getAtomicRadius(), AtomAppearance.hydrogen); 
						 }
					 }
				 }
			 }
		 }
		 sphere.getShape().setCapability(Shape3D.ALLOW_APPEARANCE_READ);
		 sphere.getShape().setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);

		 Transform3D tr = new Transform3D();
		 Point3d p = atom.getPosition();
		 tr.setTranslation(new javax.vecmath.Vector3d(p.getX(), p.getY(), p.getZ()));
		 TransformGroup tgSphere = new TransformGroup(tr);
		 tgSphere.addChild(sphere);
		 bgSphere.addChild(tgSphere);
		 spin.addChild(bgSphere);
		 return bgSphere;
	 }
	 
	 public BranchGroup addBond(Atom atomP, Atom atomQ, Color3f color) {
		 Point3d p = atomP.getPosition();
		 Point3d q = atomQ.getPosition();
		 BranchGroup bgBond = new BranchGroup();
		 bgBond.setCapability(BranchGroup.ALLOW_DETACH);
		 bgBond.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		 bgBond.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		 LineArray la = new LineArray(2, GeometryArray.COORDINATES | GeometryArray.COLOR_3 );
		 Point3f[] coords = new Point3f[2];
		 coords[0] = new Point3f((float)(p.getX()), (float)(p.getY()), (float)(p.getZ()));
		 coords[1] = new Point3f((float)(q.getX()), (float)(q.getY()), (float)(q.getZ()));
		 Color3f[] colors = new Color3f[2];
		 colors[0] = colors[1] = color;
		 la.setCoordinates(0, coords);
		 la.setColors(0, colors);
		 globalShape = new Shape3D(la,BondAppearance.bond);
		 la.setCapability(GeometryArray.ALLOW_COLOR_WRITE);
		 globalShape.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
		 globalShape.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
		 bgBond.addChild(globalShape);
		 spin.addChild(bgBond);
		 return bgBond;
	 }
	 
	 public void actionPerformed(ActionEvent actionEvent) {
		 AminoAcid aminoAcid;
		 Core core;
		 AlphaHelix alphaHelix;
		 BetaSheet betaSheet;
		 BetaStrand betaStrand;
		 
		 String cmd = actionEvent.getActionCommand();
		 System.out.println(cmd);
		 if ("Load".equals(cmd)) {
			 
			 // cleans up before next protein is loaded
			 if (protein != null) {
				 spin.removeAllChildren();
				 clearMenu();
				 protein.clear();
				 cv.repaint();
			 }
			 
			 // gets the PDB-file to be loaded
			 int returnVal = fileChooser.showOpenDialog(cv);
			 File selectedFile = (returnVal == JFileChooser.APPROVE_OPTION)? fileChooser.getSelectedFile() : null;

			 // gets from PDBSelect25 name of the chain to loaded 
			 String fileName = selectedFile.getName().substring(0,4);
			 fileName = fileName + PDBSelect25.getChainID(fileName);
			 System.out.println(fileName);
			 
			 // loads the selected protein
			 int type = 0;
			 protein = new Protein(fileName, type, true);
			 protein.setViewer(this);
			 System.out.println("Number alpha helices is " + protein.getNumberAlphaHelices());
			 System.out.println("Number beta sheets is "   + protein.getNumberBetaSheets());
			 protein.printSymbols();
			 this.setName(protein.getName());
			 
			 protein.printAlphaHelicesIntervals();
			 protein.printAlphaHelicesSymbols();
			 protein.printAlphaHelicalGapLengths();
			 protein.printBetaSheetsIntervals();
			 protein.printBetaSheetsSymbols();
			 protein.printBetaSheetsGapLengths();
			 
//			 protein.getBetaSheet(0).getBetaStrand(0).printDistances(protein.getBetaSheet(0).getBetaStrand(1));
			 
			 // displays loaded protein
			 type = protein.getType();
			 for (int i = 0; i < protein.getSize(); i++) {
				 aminoAcid = protein.getAminoAcid(i);
//				 System.out.println(i + ". " + aminoAcid.getName());
				 core = aminoAcid.getCore();	
				 if (aminoAcid.hasNext()) { 
					 aminoAcid.setPeptideBond(addBond(aminoAcid.getC(), aminoAcid.getNext().getN(), Colors.black));
					 aminoAcid.setPeptideBondShape(globalShape);
					 aminoAcid.getPeptideBond().detach();
					 
					 aminoAcid.setCaCaBond(addBond(aminoAcid.getCa(), aminoAcid.getNext().getCa(), Colors.black));
					 aminoAcid.setCaCaBondShape(globalShape);
					 aminoAcid.getCaCaBond().detach();
				 }
				 core.getCa().addBranchGroups(AtomAppearance.alphaCarbon, spin);
				 if (type > 1) {
					 core.createBranchGroup(aminoAcid, globalShape, this);
					 if (type > 2) { 
						 if (core.hasO()) {
							 core.getO().addBranchGroups(AtomAppearance.oxygen, spin);
							 core.setCOBond(addBond(core.getC(), core.getO(), Colors.black));
							 core.setCOBondShape(globalShape);
						 }
					 }
					 if (aminoAcid.hasNext()) spin.addChild(aminoAcid.getPeptideBond());
				 }
				 else {
					 if (aminoAcid.hasNext()) spin.addChild(aminoAcid.getCaCaBond());
				 }
				 isViewing = "Side Chains";
				 if (aminoAcid.hasSideChain() && (type == 4)) {
					 switch (aminoAcid.getSymbol()) {
					 case 'A': ((SideChainAla)aminoAcid.getSideChain()).createBranchGroup(aminoAcid,globalShape,this); break;
					 case 'C': ((SideChainCys)aminoAcid.getSideChain()).createBranchGroup(aminoAcid,globalShape,this); break;
					 case 'D': ((SideChainAsp)aminoAcid.getSideChain()).createBranchGroup(aminoAcid,globalShape,this); break;
					 case 'E': ((SideChainGlu)aminoAcid.getSideChain()).createBranchGroup(aminoAcid,globalShape,this); break;
					 case 'F': ((SideChainPhe)aminoAcid.getSideChain()).createBranchGroup(aminoAcid,globalShape,this); break;
					 case 'G': ((SideChainGly)aminoAcid.getSideChain()).createBranchGroup(aminoAcid,globalShape,this); break;
					 case 'H': ((SideChainHis)aminoAcid.getSideChain()).createBranchGroup(aminoAcid,globalShape,this); break;
					 case 'I': ((SideChainIle)aminoAcid.getSideChain()).createBranchGroup(aminoAcid,globalShape,this); break;
					 case 'K': ((SideChainLys)aminoAcid.getSideChain()).createBranchGroup(aminoAcid,globalShape,this); break;
					 case 'L': ((SideChainLeu)aminoAcid.getSideChain()).createBranchGroup(aminoAcid,globalShape,this); break;
					 case 'M': ((SideChainMet)aminoAcid.getSideChain()).createBranchGroup(aminoAcid,globalShape,this); break;
					 case 'N': ((SideChainAsn)aminoAcid.getSideChain()).createBranchGroup(aminoAcid,globalShape,this); break;
					 case 'P': ((SideChainPro)aminoAcid.getSideChain()).createBranchGroup(aminoAcid,globalShape,this); break;
					 case 'Q': ((SideChainGln)aminoAcid.getSideChain()).createBranchGroup(aminoAcid,globalShape,this); break;
					 case 'R': ((SideChainArg)aminoAcid.getSideChain()).createBranchGroup(aminoAcid,globalShape,this); break;
					 case 'S': ((SideChainSer)aminoAcid.getSideChain()).createBranchGroup(aminoAcid,globalShape,this); break;
					 case 'T': ((SideChainThr)aminoAcid.getSideChain()).createBranchGroup(aminoAcid,globalShape,this); break;
					 case 'V': ((SideChainVal)aminoAcid.getSideChain()).createBranchGroup(aminoAcid,globalShape,this); break;
					 case 'Y': ((SideChainTyr)aminoAcid.getSideChain()).createBranchGroup(aminoAcid,globalShape,this); break;
					 case 'W': ((SideChainTrp)aminoAcid.getSideChain()).createBranchGroup(aminoAcid,globalShape,this); break;	 
					 }
				}
				 
			 }
			 System.out.println(Functions.toRadians(Vector3d.getDihedralAngle(new Vector3d(-1,1,0), new Vector3d(0,0,1), new Vector3d(0,1,0))));	
		 }
		 if ("Clear".equals(cmd)) {
			 spin.removeAllChildren();
			 if (protein != null) protein.clear();
		 }
		 if ("Exit".equals(cmd)) {
			 spin.removeAllChildren();
			 if (protein != null) protein.clear();
			 destroy();
		 }
		 
		 if ("vdW".equals(cmd)) {
			 for (int i= 0; i < protein.getSize(); i++) {
				 aminoAcid = protein.getAminoAcid(i);
				 if (aminoAcid.isVisible()) {
					 aminoAcid.getCa().getAtomBranchGroup().detach();
					 spin.addChild(aminoAcid.getCa().getVdWBranchGroup());
					 if (aminoAcid.hasCore() && !isViewing.equals("CaTrace")) {
						 core = aminoAcid.getCore();
						 if (core.hasN()) {
							 core.getN().getAtomBranchGroup().detach();
							 spin.addChild(core.getN().getVdWBranchGroup());
						 }
						 if (core.hasC()) {
							 core.getC().getAtomBranchGroup().detach();
							 spin.addChild(core.getC().getVdWBranchGroup());
						 }
						 if (core.hasO() && (isViewing.equals("Oxygen") || isViewing.equals("Side Chains"))) {
							 core.getO().getAtomBranchGroup().detach();
							 spin.addChild(core.getO().getVdWBranchGroup());
						 }
					 }
				 }
			 }
			 model = "vdW";
		 }
		 if ("Atoms".equals(cmd)) {
			 for (int i= 0; i < protein.getSize(); i++) {
				 aminoAcid = protein.getAminoAcid(i);
				 if (aminoAcid.isVisible()) {
					 aminoAcid.getCa().getVdWBranchGroup().detach();
					 spin.addChild(aminoAcid.getCa().getAtomBranchGroup());
					 if (aminoAcid.hasCore()) {
						 core = aminoAcid.getCore();
						 if (core.hasN()) {
							 core.getN().getVdWBranchGroup().detach();
							 spin.addChild(core.getN().getAtomBranchGroup());
						 }
						 if (core.hasC()) {
							 core.getC().getVdWBranchGroup().detach();
							 spin.addChild(core.getC().getAtomBranchGroup());
						 }
						 if (core.hasO() && (isViewing.equals("Oxygen") || isViewing.equals("Side Chains"))) {
							 core.getO().getVdWBranchGroup().detach();
							 spin.addChild(core.getO().getAtomBranchGroup());
						 }
					 }
				 }
			 }
			 model = "Atoms";
		 }
		 
		 if ("CaTrace".equals(cmd)) {
			 if (isViewing.equals("Backbone")) {                         // Backbone view to be replaced by Ca-trace view
				 for (int i = 0; i < protein.getSize(); i++)  {
					 aminoAcid = protein.getAminoAcid(i);
					 if (aminoAcid.isVisible()) {
						 aminoAcid.getCore().detachBranchGroup();
						 if (aminoAcid.hasNext()) {
							 aminoAcid.getPeptideBond().detach();
							 spin.addChild(aminoAcid.getCaCaBond());
						 }
					 }
				 }
				 isViewing = "CaTrace";
			 }
			 if (isViewing.equals("Oxygen")) {                              // Oxygen view to be replaced be Ca-trace
				 for (int i = 0; i < protein.getSize(); i++)  {
					 aminoAcid = protein.getAminoAcid(i);
					 if (aminoAcid.isVisible()) {
						 aminoAcid.getCore().detachBranchGroup();
						 if (aminoAcid.hasNext()) { 
							 aminoAcid.getPeptideBond().detach();
							 spin.addChild(aminoAcid.getCaCaBond());							 
						 }
						 aminoAcid.getO().getAtomBranchGroup().detach();
						 aminoAcid.getO().getVdWBranchGroup().detach();
						 aminoAcid.getCore().getCOBond().detach();
					 }
				 }
				 isViewing = "CaTrace";
			 }
			 if (isViewing.equals("Side Chains")) {                           // Side Chains view to be replaced be Ca-trace
				 for (int i = 0; i < protein.getSize(); i++)  {
					 aminoAcid = protein.getAminoAcid(i);
					 if (aminoAcid.isVisible()) {
						 aminoAcid.getCore().detachBranchGroup();
						 if (aminoAcid.hasNext()) {
							 aminoAcid.getPeptideBond().detach();
							 spin.addChild(aminoAcid.getCaCaBond());
						 	}
						 aminoAcid.getO().getAtomBranchGroup().detach();
						 aminoAcid.getO().getVdWBranchGroup().detach();
						 aminoAcid.getCore().getCOBond().detach();
						 if (aminoAcid.getSymbol() != 'G') aminoAcid.getSideChain().detachBranchGroup();
					 }
				 }
				 isViewing = "CaTrace";
			 }
		 }
		 if ("Backbone".equals(cmd)) {                                       // Ca-trace view is replaced by backbone view
			 if (isViewing.equals("CaTrace") && (protein.getType() > 1)) {
				 for (int i = 0; i < protein.getSize(); i++) {
					 aminoAcid = protein.getAminoAcid(i);
					 if (aminoAcid.isVisible()) {
						 if (aminoAcid.hasNext()) {
							 aminoAcid.getCaCaBond().detach();
							 spin.addChild(aminoAcid.getPeptideBond());
						 }
						 aminoAcid.getCore().reattach(spin, this);
					 }
				 }
				 isViewing = "Backbone";
			 }
			 if (isViewing.equals("Oxygen")) {                               // Oxygen view is replaced by backbone view
				 for (int i = 0; i < protein.getSize(); i++) {
					 aminoAcid = protein.getAminoAcid(i);
					 if (aminoAcid.isVisible()) {
						 aminoAcid.getO().getAtomBranchGroup().detach();
						 aminoAcid.getO().getVdWBranchGroup().detach();
						 aminoAcid.getCore().getCOBond().detach();	
					 }
				 }		
				 isViewing = "Backbone";
			 }
			 if (isViewing.equals("Side Chains")) {                           // Side Chains view to be replaced by backbone view
				 for (int i = 0; i < protein.getSize(); i++)  {
					 aminoAcid = protein.getAminoAcid(i);
					 if (aminoAcid.isVisible()) {
						 aminoAcid.getO().getAtomBranchGroup().detach();
						 aminoAcid.getO().getVdWBranchGroup().detach();
						 aminoAcid.getCore().getCOBond().detach();
						 if (aminoAcid.getSymbol() != 'G') aminoAcid.getSideChain().detachBranchGroup();
					 }
				 }
				 isViewing = "Backbone";
			 }
		 }
		 if ("Oxygen".equals(cmd)) {  
			 if (isViewing.equals("CaTrace") && (protein.getType() > 2)) {      // Ca-trace view is replaced by oxygen view
				 for (int i = 0; i < protein.getSize(); i++) {
					 aminoAcid = protein.getAminoAcid(i);
					 if (aminoAcid.isVisible()) {
						 if (model.equals("Atoms")) spin.addChild(aminoAcid.getO().getAtomBranchGroup());
						 else spin.addChild(aminoAcid.getO().getVdWBranchGroup());
						 spin.addChild(aminoAcid.getCore().getCOBond());
						 if (aminoAcid.hasNext()) {
							 aminoAcid.getCaCaBond().detach();
							 spin.addChild(aminoAcid.getPeptideBond());
						 }
						 aminoAcid.getCore().reattach(spin, this);
					 }
				 }	
				 isViewing = "Oxygen";
			 }

			 if (isViewing.equals("Backbone") && (protein.getType() > 2)) {      // Backbone view is replaced by oxygen view
				 for (int i = 0; i < protein.getSize(); i++) {
					 aminoAcid = protein.getAminoAcid(i);
					 if (aminoAcid.isVisible()) {
						 if (model.equals("Atoms")) spin.addChild(aminoAcid.getO().getAtomBranchGroup());
						 else spin.addChild(aminoAcid.getO().getVdWBranchGroup());
						 spin.addChild(aminoAcid.getCore().getCOBond());
					 }
				 }
				 isViewing = "Oxygen";
			 }
			 if (isViewing.equals("Side Chains")) {                           // Side Chains view is replaced by oxygen view
				 for (int i = 0; i < protein.getSize(); i++)  {
					 aminoAcid = protein.getAminoAcid(i);
					 if (aminoAcid.isVisible()) {
						 if (aminoAcid.getSymbol() != 'G') aminoAcid.getSideChain().detachBranchGroup();
					 }
				 }
				 isViewing = "Oxygen";
			 }

		 }
		 if ("Side Chains".equals(cmd)) {
			 if (isViewing.equals("CaTrace") && (protein.getType() > 3)) {      // Ca-trace view is replaced by Side Chain view
				 for (int i = 0; i < protein.getSize(); i++) {
					 aminoAcid = protein.getAminoAcid(i);
//					 System.out.println(i + ". " + aminoAcid.getName());
					 if (aminoAcid.isVisible()) {
						 if (model.equals("Atoms")) spin.addChild(aminoAcid.getO().getAtomBranchGroup());
						 else spin.addChild(aminoAcid.getO().getVdWBranchGroup());
						 spin.addChild(aminoAcid.getCore().getCOBond());
						 if (aminoAcid.hasNext()) {
							 aminoAcid.getCaCaBond().detach();
							 spin.addChild(aminoAcid.getPeptideBond());
						 }
						 aminoAcid.getCore().reattach(spin, this);
						 if (aminoAcid.getSymbol() != 'G') aminoAcid.getSideChain().reattachBranchGroup(spin);
					 }
				 }	
				 isViewing = "Side Chains";
			 }
			 if (isViewing.equals("Backbone") && (protein.getType() > 3)) {      // Backbone view is replaced by Side Chain view
				 for (int i = 0; i < protein.getSize(); i++) {
					 aminoAcid = protein.getAminoAcid(i);
					 if (aminoAcid.isVisible()) {
						 if (model.equals("Atoms")) spin.addChild(aminoAcid.getO().getAtomBranchGroup());
						 else spin.addChild(aminoAcid.getO().getVdWBranchGroup());
						 spin.addChild(aminoAcid.getCore().getCOBond());
						 if (aminoAcid.getSymbol() != 'G') aminoAcid.getSideChain().reattachBranchGroup(spin);
					 }
				 }	
				 isViewing = "Side Chains";
			 }
			 if (isViewing.equals("Oxygen") && (protein.getType() > 3)) {      // Oxygen view is replaced by Side Chain view
				 for (int i = 0; i < protein.getSize(); i++) {
					 aminoAcid = protein.getAminoAcid(i);
					 if (aminoAcid.isVisible()) {
						 if (aminoAcid.getSymbol() != 'G') aminoAcid.getSideChain().reattachBranchGroup(spin);
					 }
				 }	
				 isViewing = "Side Chains";

			 }

		 }
		 if ("a-highlight".equals(cmd)) {
			 Color3f color;
			 if (isShowingHelices) color = Colors.black; else color = Colors.red;
			 int sizeH = protein.getNumberAlphaHelices();
			 for (int i = 0; i < sizeH; i++) protein.getAlphaHelix(i).changeColor(color);
			 isShowingHelices = !isShowingHelices;
		 }
		 if ("b-highlight".equals(cmd)) {
			 Color3f color;
			 if (isShowingSheets) color = Colors.black; else color = Colors.magenta;
			 int sizeB = protein.getNumberBetaSheets();
			 for (int i = 0; i < sizeB; i++) {
				 betaSheet = protein.getBetaSheet(i);
				 int bSize = betaSheet.getBetaStrands().getSize();
				 for (int j = 0; j < bSize; j++) {
					 betaStrand = betaSheet.getBetaStrand(j);
					 betaStrand.changeColor(color);
				}
			 }
			 isShowingSheets = !isShowingSheets;
		 }
		 if ("a-Helices".equals(cmd)) {
			 int sizeH = protein.getNumberAlphaHelices();
			 Color color = pvm.getAlphaMenuItem().getBackground();
			 for (int i = 0; i < sizeH; i++) {
				 alphaHelix = protein.getAlphaHelix(i);
				 int k = protein.getIndex(alphaHelix.getInitSeqNum());
				 int l = protein.getIndex(alphaHelix.getEndSeqNum());
				 if (color.equals(Color.red)) {
					 for (int j = k; j < l; j++) protein.getAminoAcid(j).reattach(spin, isViewing, this); 
					 pvm.getAlphaMenuItem().setBackground(Color.white);				 
				 }
				 else {
					 for (int j = k; j < l; j++) protein.getAminoAcid(j).detach(isViewing);
					 pvm.getAlphaMenuItem().setBackground(Color.red);
				 }
			 }
		 }
		 if ("b-Sheets".equals(cmd)) {
			 int sizeB = protein.getNumberBetaSheets();
			 Color color = pvm.getBetaMenuItem().getBackground();
			 for (int i = 0; i < sizeB; i++) {
				 betaSheet = protein.getBetaSheet(i);
				 int sizeS = betaSheet.getNumberBetaStrands();
				 for (int j = 0; j < sizeS; j++) {
					 betaStrand = betaSheet.getBetaStrand(j);
					 int k = protein.getIndex(betaStrand.getInitSeqNum());
					 int l = protein.getIndex(betaStrand.getEndSeqNum());
					 if (color.equals(Color.red)) {	
						 for (int m = k; m < l; m++) protein.getAminoAcid(m).reattach(spin, isViewing, this); 
						 pvm.getBetaMenuItem().setBackground(Color.white);					 
					 }
					 else {
						 for (int m = k; m < l; m++) protein.getAminoAcid(m).detach(isViewing);
						 pvm.getBetaMenuItem().setBackground(Color.red);	
					 } 
				 }
			 }
		 }
		 if ("Coil".equals(cmd)) {
			 Color color = pvm.getCoilMenuItem().getBackground();

			 if (color.equals(Color.red)) {	
				 for (int i = 0; i < protein.getSize(); i++) {
					 aminoAcid = (AminoAcid)protein.getAminoAcid(i);
					 if (aminoAcid.getSecClass() == 'C') aminoAcid.reattach(spin, isViewing, this);
				 }
				 pvm.getCoilMenuItem().setBackground(Color.white);					 
			 }
			 else {
				 for (int i = 0; i < protein.getSize(); i++) {
					 aminoAcid = (AminoAcid)protein.getAminoAcid(i);
					 if (aminoAcid.getSecClass() == 'C') aminoAcid.detach(isViewing);
				 }
				 pvm.getCoilMenuItem().setBackground(Color.red);		
			 }
		 }
		 
		 
		 
		 
		 if ("Test".equals(cmd)) {
			 Carbon c = new Carbon();
			 c.setPosition(0.0, 0.0, -5.0);
			 Nitrogen n = new Nitrogen();
			 n.setPosition(5.0, 1.0, -10.0);
			 addBond(c, n, Colors.black);
			 Carbon c2 = new Carbon();
			 c2.setPosition(2.1, 3.2, -2);
			 Nitrogen n2 = new Nitrogen();
			 n2.setPosition(-3, 4, -1);
			 addBond(c2, n2, Colors.blue);
		 }
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

