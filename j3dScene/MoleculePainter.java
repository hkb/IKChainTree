package j3dScene;

import geom3d.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.media.j3d.Canvas3D;
import javax.swing.JFrame;

import jbcl.calc.structural.transformations.Rototranslation;
import jbcl.data.basic.TwoTuple;
import jbcl.data.types.Vector3D;

/** A simple interface for drawing a molecule using the J3DGraphics class. On 
 *  construction a scene is created. When <code>setupCoords</code> is called atoms 
 *  and bonds are added to the scene, and not removed again (unless <code>setupCoords</code>
 *  is called again). A bond is automatically created if the distance between two atoms is 
 *  less than the average of their Van der Waals radii plus 5%. When <code>updateCoords</code> 
 *  is called the coordinates of atoms (and hence bonds is updated).
 * 
 *  The color of each atom-type is specified in the static <code>colors</code> array and can 
 *  be changed by setting e.g.
 *  <pre>
 *  MoleculePainter.colors[MoleculePainter.O] = java.awt.Color.RED.darker();
 *  MoleculePainter.colors[MoleculePainter.N] = java.awt.Color.BLUE.darker();
 *  </pre>
 *  The <code>setupCoords</code> must be called again for any changes in the color array to 
 *  take effect. 
 * 
 * @author R. Fonseca
 */
public class MoleculePainter {
	/** Array specifying the colors of each atom type */
	public static Color[] colors = {Color.BLUE, Color.GRAY, Color.RED, Color.WHITE, Color.YELLOW};
	/** The color of covalent bonds */
	public static Color bondColor = Color.GRAY.brighter();
	/** Identifier for nitrogen */
	public static final int N = 0;
	/** Identifier for carbon */
	public static final int C = 1;
	/** Identifier for oxygen */
	public static final int O = 2;
	/** Identifier for hydrogen */
	public static final int H = 3;
	/** Identifier for sulfur */
	public static final int S = 4;

	/** The atom-radii used for generating spheres */
	public static final float[] atomRadii = {0.71f, 0.69f, 0.66f, 0.31f, 1.05f};
	/** The Van der Waals radii used for generating bonds between atoms. To change the vdwRadii for 
	 * sulfur type e.g. <code>MoleculePainter.vdwRadii[MoleculePainter.S] *= 1.05;</code> */
	public static final float[] vdwRadii 	= {1.55f, 1.7f, 1.5f, 1.2f, 1.8f};
	
	
	private final J3DScene scene = new J3DScene();
	private final List<SceneMolecule> molecules = new ArrayList<SceneMolecule>();
	

	private class SceneMolecule{
		private final static int SKIP = 1;
		private final List<double[]> coords;
		private Vector3D[] superposedCoords, allSuperposedCoords;
		private final List<Sphere3d> spheres = new ArrayList<Sphere3d>();
		private final List<Shape3d> bondShapes = new ArrayList<Shape3d>();
		@SuppressWarnings("unused")
		private final boolean cAlpha;

		SceneMolecule(List<double[]> coords, List<Integer> atomTypes, boolean dim){
			this(coords, atomTypes, dim, false);
		}
		SceneMolecule(List<double[]> coords, List<Integer> atomTypes, boolean dim, boolean cAlpha){
			this.cAlpha = cAlpha;
			this.coords = coords;
			for(int i=0;i<coords.size();i++){
				double[] c = coords.get(i);
				int t = atomTypes.get(i);
				
				Sphere3d s = new Sphere3d(new Point3d(c[0],c[1],c[2]), atomRadii[t]);
				Color col = colors[t];
				if(dim) col = new Color(colors[t].getRed(), colors[t].getGreen(), colors[t].getBlue(), 100);
				if(cAlpha) s.radius = 0.2f;
				scene.addShape(s, col);
				spheres.add(s);
			}
			
			for(int i=0;i<coords.size();i++){
				double vdwRad1 = vdwRadii[atomTypes.get(i)];
				for(int j=i+1;j<coords.size();j++){
					double vdwRad2 = vdwRadii[atomTypes.get(j)];
					
					double d = spheres.get(i).center.getDistance(spheres.get(j).center);
					if(cAlpha && d<=(3.9)){
						Cylinder3d c = new Cylinder3d(spheres.get(i).center, spheres.get(j).center, 0.2);
						Color col = bondColor;
						if(dim) col = new Color(bondColor.getRed(), bondColor.getGreen(), bondColor.getBlue(), 100);
						scene.addShape(c, col);
						bondShapes.add(c);
					}
					if(!cAlpha && d <= (vdwRad1+vdwRad2)/2){
						Cylinder3d c = new Cylinder3d(spheres.get(i).center, spheres.get(j).center, 0.2f);
						Color col = bondColor;
						if(dim) col = new Color(bondColor.getRed(), bondColor.getGreen(), bondColor.getBlue(), 100);
						scene.addShape(c, col);
						bondShapes.add(c);
					}
				}
			}
		}
		
		void updateCoords(){
			
			if(superposedCoords==null){
				int n = 0;	for(int c=0;c<coords.size();c+=SKIP) n++;
				
				superposedCoords = new Vector3D[n];
				allSuperposedCoords = new Vector3D[coords.size()];
			}
			for(int c=0;c<coords.size();c+=SKIP) 
				superposedCoords[c] = new Vector3D(coords.get(c)[0],coords.get(c)[1],coords.get(c)[2]);
			for(int c=0;c<coords.size();c+=1)
				allSuperposedCoords[c] = new Vector3D(coords.get(c)[0],coords.get(c)[1],coords.get(c)[2]);
			
			if(molecules.get(0)!=this && molecules.get(0).superposedCoords.length==superposedCoords.length){
				Vector3D[] coords1 = molecules.get(0).superposedCoords;
				Vector3D[] coords2 = superposedCoords;
				TwoTuple<Double, Rototranslation> rmsdTrans = 
					jbcl.calc.structural.Crmsd.optimalCrmsdTransformation(coords1, coords2);
				rmsdTrans.second.transform(allSuperposedCoords);
			}
			
			for(int c=0;c<coords.size(); c++){
				spheres.get(c).center.setX((float)allSuperposedCoords[c].x);
				spheres.get(c).center.setY((float)allSuperposedCoords[c].y);
				spheres.get(c).center.setZ((float)allSuperposedCoords[c].z);
			}
		}
	}
	
	
	/** Adds a molecule with the specified atoms to the painter. Bonds are automatically generated based 
	 * on the values in the vdwRadii field. Each entry in the <code>coords</code>-list must be a 
	 * <code>double</code>-array of length 3, and the length of <code>types</code> must
	 * be at least as long as that of <code>coords</code>. It is not unspecified what happens if 
	 * the atom-types are either less than 0 or greater than 4. 
	 */
	public void addMolecule(List<double[]> coords, List<Integer> atomTypes ){
		assert coords.size() == atomTypes.size();
		SceneMolecule sm = new SceneMolecule(coords, atomTypes, molecules.size()!=0);
		molecules.add(sm);
		sm.updateCoords();
		scene.repaint();
		scene.centerCamera();
	}
	
	public void addCAlphaProtein(List<double[]> coords){
		List<Integer> atomTypes = new ArrayList<Integer>();
		for(int r=0;r<coords.size();r++) atomTypes.add(C);
		SceneMolecule sm = new SceneMolecule(coords, atomTypes, molecules.size()!=0, true);
		molecules.add(sm);
		sm.updateCoords();
		scene.repaint();
		scene.centerCamera();
	}
	
	/** Centers the view on the elements in the scene. */
	public void centerView(){ scene.centerCamera(); }
	/** Zooms to view the entire molecule. */
	public void autoZoom(){ scene.autoZoom(); }
	
	
	/**
	 * Updates the coordinates of atoms from the lists stored when calling <code>addMolecule</code>
	 */
	public void updateCoords(){
		for(SceneMolecule sm: molecules) sm.updateCoords();
		scene.repaint();
	}
	
	public void removeAllMolecules(){
		scene.removeAllShapes();
		molecules.clear();
	}
	
	/** Get the Java3D canvas component of the scene. This component can be added to 
	 * swing or awt containers. */
	public Canvas3D getCanvas(){
		return scene.getCanvas();
	}
	
	public void startRotating(){
		scene.toggleRotation();
	}
	
	/** Creates a visible frame with a Java3D canvas linked to a <code>MoleculePainter</code> and 
	 * return the moleculepainter. 
	 */
	public static MoleculePainter createPainterInFrame(){
		
		MoleculePainter mp = new MoleculePainter();
		
		JFrame f = new JFrame("Molecule painter");
		f.setSize(900, 700);
		f.getContentPane().add(mp.scene.getCanvas());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		
		return mp;
	}
	
	public static void main(String[] args){
		MoleculePainter mp = createPainterInFrame();
		List<double[]> coords = new ArrayList<double[]>();
		List<Integer> types = new ArrayList<Integer>();
		coords.add(new double[]{0,0,0});	types.add(C);
		coords.add(new double[]{1.7,0,0});	types.add(C);
		coords.add(new double[]{2.7,0,0});	types.add(C);
		coords.add(new double[]{3.7,0,0});	types.add(C);
		coords.add(new double[]{4.7,0,0});	types.add(C);
		coords.add(new double[]{1.7,1,0});	types.add(N);
		coords.add(new double[]{3.7,-1,0});	types.add(O);
		
		mp.addMolecule(coords, types);

		
		List<double[]> cs2 = new ArrayList<double[]>();
		List<Integer> types2 = new ArrayList<Integer>();
		cs2.add(new double[]{0,5,0}); types2.add(C);
		cs2.add(new double[]{1.6,5,0.1}); types2.add(C);
		cs2.add(new double[]{2.7,5,0.1}); types2.add(C);
		cs2.add(new double[]{3.7,5,0.1}); types2.add(C);
		cs2.add(new double[]{4.9,5,0.2}); types2.add(C);
		cs2.add(new double[]{1.5,4,0.2}); types2.add(N);
		cs2.add(new double[]{3.8,6.1,0.2}); types2.add(O);
		mp.addMolecule(cs2, types2);
		while(true){
			cs2.get((int)(Math.random()*cs2.size()))[(int)(Math.random()*2)] += 
				Math.random()*0.02-0.01;
			//coords.get(1)[0]+=0.1;
			mp.updateCoords();
			mp.centerView();
			//mp.autoZoom();
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
