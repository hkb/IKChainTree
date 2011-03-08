package molecule;

import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.LineArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;
import javax.media.j3d.*;
import geom3d.*;
import guiMolecule.HelixViewer;
import com.sun.j3d.utils.applet.MainFrame;

/* 
 * Alpa helix was first described by Pauling (1951).
 * 
 * 
 * Over 30% of all residues of globular proteins are found in helices. In globular proteins 
 * alpha helices vary considerably in length, ranging from 4 to over 40 residues. The average
 * length is around 10-11 residues. This is approx. 17Å, corresponding to 3 turns.
 * 
 * The most common location for an alpha helix in proteins is along the outside of the
 * protein, with one side of the helix facing the solution and the other side facing 
 * the hydrophobic interior of the protein.
 * 
 * The regular alpha helix has 3.6 residues per turn with each residue offset from the 
 * preceding residue by 1.5Å. The pitch of the alpha helix is 5.4Å (3.6 * 1.5Å)
 * 
 * Hydrogen bonds occur between the backbone carbonyl oxygen (acceptor) of one residue 
 * and the amide hydrogen (donor) of a residue for ahead in the polypeptide chain. 
 * The hydrogen bonds are 2.86Å long from oxygen to nitrogen atoms, linear and lie 
 * (in regular helix) parallel to the helical axis. In real alpha helices deviations 
 * from ideal geometry of hydrogen bonds do occur.
 * 
 * Ideal dihedral angles in the geometrically pure alpha helices are phi = -57.8 and 
 * psi = -47.0. The average dihedral angles phi = -64 +/- 7,  and psi = -41 +/- 7. 
 * Solovyev and Sindylov quote Schultz and Shirmer (1979) that phi = psi = -60.
 * 
 * On average the amino end of the alpha helix is positive whilst the carboxyl end is 
 * negative.
 * 
 * The first NH groups and the last CO groups will normally lack hydrogen bonds. 
 * Short helices therefore have distorted conformations. Dipoles formed by these two groups 
 * point along the helical axis.
 * 
 * Side chains project outward into solution.
 * 
 * Ala, Glu, Leu and Met are often found in alpha helices. Pro, Gly, Ser, Thr, Val occur 
 * relatively rarely. Pro mainly occurs in the first turn of an alpha helix. It creates
 * sterical problems in alpha helical conformations
 * 
 * Alpha helices that are observed in proteins are almost always right-handed. This is 
 * because all amino acids are L-handed and as a consequence, their residues would have
 * to project inwards in left-handed helices. Short regions of left-handed alpha helices 
 * (3-5 residues) occur only occasionally.

*/
public class AlphaHelix {

	private final double residuesPerTurn = 3.6;   //  Ångstrom
	private final double translationPerResidue = 1.5;   // Ångstrom
	private final double pitch = residuesPerTurn * translationPerResidue;  // 5.4 Ångstrom
	private final int phi = -57;
	private final int psi = -47;
	
	private Protein protein;
	private int n;
	private int initSeqNum;
	private int endSeqNum;
	private int type;
	private Segment3d helicalAxis;
	
	private HelixViewer viewer;
	/*
	 * creates alpha helix with n residues. 
	 * type = "C": only Ca are created.
	 * type = "NCaC" : cores with N, Ca and C are created.
	 * type = "NCaCO" : cores with N, Ca, C and O are created.
	 * type = "NCaCOH" : cores with N, Ca, C, O and all H are created. 
	 */
	public AlphaHelix(Protein protein, int initSeqNum, int endSeqNum) {
		this.protein = protein;
		this.initSeqNum = initSeqNum;
		this.endSeqNum = endSeqNum;
		this.n = protein.getIndex(endSeqNum) - protein.getIndex(initSeqNum)+1;
	}
	
	public int getSize() { return n; }
	
	/*
	 * returns the index in the protein of the first amino acid
	 */
	public int getInitSeqNum() { return initSeqNum; }
	
	/*
	 * returns the index in the protein of the last amino acid
	 */
	public int getEndSeqNum() { return endSeqNum; }
	
	public String getSymbols() { 
	//	System.out.println("Helix begins at " + initSeqNum + ", ends at " + endSeqNum + ", has length " + (protein.getIndex(endSeqNum)-protein.getIndex(initSeqNum)+1));
	//	System.out.println(protein.getSequence()[protein.getIndex(initSeqNum)]);
		return String.copyValueOf(protein.getSequence(), protein.getIndex(initSeqNum), protein.getIndex(endSeqNum)-protein.getIndex(initSeqNum)+1); 
	}
	
	public int getType() { return type; }
	public void setType(int type) { this.type = type; }
	
	/*
	 * positions atoms in a perfect alpha helix along the helical axis overlaping with the z-axis.
	 * If neither cores nor sidechains are defined, only C-alpha atoms are positioned.
	 * If core is specified, all defined core atoms are positioned. 
	 * placed in the origo.
	 */
	public void setPositions() {
		double rad = 6.0;
		AminoAcid aminoAcid;
		Core core;
		double a = Math.PI/1.8; 
		double b = a/3.0;
		int size = protein.getSize();
		for (int i = 0; i < size; i++) {
			aminoAcid = protein.getAminoAcid(i);
			if (i%9 == 0) aminoAcid.setPosition(new Point3d(rad*Math.cos(a*i), 0.0, 1.5*i+0.5));
			else aminoAcid.setPosition(new Point3d(rad*Math.cos(a*i), rad*Math.sin(a*i), 1.5*i+0.5));
			if (aminoAcid.hasCore()) {
				core = aminoAcid.getCore();
				core.getN().setPosition(new Point3d(rad*Math.cos(a*i-b), rad*Math.sin(a*i-b), 1.5*i-0.5));
				core.getCa().setPosition(aminoAcid.getPosition());
				core.getC().setPosition(new Point3d(rad*Math.cos(a*i+b), rad*Math.sin(a*i+b), 1.5*i+0.5));
			}
		}
	}
	
	/*
	 * sets the secondary classification of all amino acids in this a-helix to 'H'
	 */
	public void setSecClass() {
		for (int j = protein.getIndex(getInitSeqNum()); j < protein.getIndex(getEndSeqNum()); j++) protein.getAminoAcid(j).setSecClass('H');
	}
	
	
	public void printSymbols() { System.out.print("[" + getSymbols() + "]"); }
	
	public void printPhiAngles() {
		int size = protein.getSize();
		for (int i = 1; i < size; i++) System.out.println(protein.getAminoAcid(i).getCore().getPhiAngle());
	}

	public void printPsiAngles() {
		int size = protein.getSize() - 1;
		for (int i = 0; i < size; i++) System.out.println(protein.getAminoAcid(i).getCore().getPsiAngle());
	}

	public void printOmegaAngles() {
		int size = protein.getSize();
		for (int i = 0; i < size-1; i++) System.out.println(protein.getAminoAcid(i).getCore().getOmegaAngle());
	}
	
	public void printDihedralAngles() {
		Core core;
		int size = protein.getSize();
		for (int i = 0; i < size; i++) {
		 	core = protein.getAminoAcid(i).getCore();
			System.out.println("(" + core.getPhiAngle() +", " + core.getPsiAngle() + ", " + core.getOmegaAngle() + ")");
		} 
	}  
	
	private void recolorBond(Shape3D shape, Color3f color) {
		LineArray la = (LineArray)shape.getGeometry();
		la.setColor(0,color);
		la.setColor(1,color);
	}
	
	public void changeColor(Color3f color) {
		AminoAcid aminoAcid;
		for (int j = protein.getIndex(getInitSeqNum()); j < protein.getIndex(getEndSeqNum()); j++) { 
			aminoAcid = protein.getAminoAcid(j);
			if (aminoAcid.hasNext()) {
				recolorBond(aminoAcid.getPeptideBondShape(), color);
				recolorBond(aminoAcid.getCaCaBondShape(), color);
			}
			if (aminoAcid.getCore().hasN()) {
				recolorBond(aminoAcid.getCore().getNCaBondShape(), color);
				recolorBond(aminoAcid.getCore().getCaCBondShape(), color);
				if (aminoAcid.getCore().hasO()) recolorBond(aminoAcid.getCore().getCOBondShape(), color);
			}
		}
	}

	
	public void setViewer(HelixViewer viewer) { this.viewer = viewer; }
	
	public String toString() { return "[" + initSeqNum + "," + endSeqNum + "]"; }
	
	public static void main(String[] args) {
		HelixViewer viewer = new HelixViewer();
		new MainFrame(viewer, 640, 480);
	}
}
