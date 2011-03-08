package molecule;

import geom3d.*;
import com.sun.j3d.utils.geometry.Sphere;



/*
 * The are 20 naturally occurring amino acids. 
 * 
 * In their isolated states, amino acids are white crystalline solids with high melting and boiling points. This is 
 * very atypical for organic molecules. Amino acids are building blocks of proteins which are generally viewed as organic.
 * 
 * Following amino acids are hydrophobic (nonpolar): valine, leucine, isoleucine, methionine, phenylalaine.
 * Following amino acids are hydrophylic (polar): asparagine, glutamic acid, glutamine, histidine, lysine, arginine, aspartic acid.
 * Following amino acids are neutral (on the nonpolar side): glycine, alanine, tryptophan, cysteine, proline.
 * Following amino acids are neutral (on the polar side): serine, threonine, tyrosine. 
 *
 * Following amino acids have positively charged side chains (have amine group in their side chains): arginine, histidine, lysine
 * Following amino acids have negatively charged side chains (have carboxy group in their side chains): asparic acid, glutamic acid.
 * The remaining amino acids have neutrally charged side chains.
 * 
 * Humans can synthetize 11 of then. The remaining 9 essential amino acids must be ingested.
 * Essential amino acids are: histidine, isoleucine, leucine, lysine, methionine, phenylalanine,
 * threonine, tryptophan, and valine.
 */

//public class AminoAcid {
//	private Core      	core      = null;
//	private SideChain 	sideChain = null;
//	private String    	name      = null;                // 3-letter name
//	private Integer 	pdbNumber = null;
//	private char 		symbol;                       // 1-letter symbol
//	private Point3d 	position  = null;
//	private Sphere 		sphere;
//	private boolean 	essential;
//	private AminoAcid 	next      = null;
//	private AminoAcid 	prev      = null;
//	private Vector3d 	hseVector = null;
//	private int 		contactRadius = 0;
//	private Integer 	contactNumber = null;
//	private int 		hseRadius     = 0;
//	private Integer 	hseNumberUp   = null;
//	private Integer 	hseNumberDown = null;
//	private final static char[] aaTable = {'A', 'R', 'N', 'D', 'C', 'E', 'Q', 'G', 'H', 'I', 'L', 'K', 'M', 'F', 'P', 'S', 'T', 'W', 'Y', 'V', '?'};
//
//	/*
//	 * creates amino acid positioned in the origo.
//	 */
//	public AminoAcid(String name, String type) { 
//		this.name = name;
//		position = new Point3d(); 
//		if (type != "Ca") core = new Core(this, type);
//	}
//
//	/*
//	 * creates amino acid positioned at the given point p.
//	 */
//	public AminoAcid(String name, String type, Point3d p) { 
//		this.name = name;
//		position = p; 
//		if (type != "Ca") core = new Core(this, type);
//	}
//
//	
//	public String    getName()          { return name; }
//	public Integer   getPDBNumber()     { return pdbNumber; }
//	public Point3d   getPosition()      { return hasCore()? core.getCa().getPosition() : position; }
//	public Core      getCore()          { return core; }
//	public SideChain getSideChain()     { return sideChain; }
//	public AminoAcid getNextAminoAcid() { return next; }
//	public AminoAcid getPrevAminoAcid() { return prev; }
//	public Vector3d  getHSEVector()     { return hseVector; }
//	public Sphere    getSphere()        { return sphere; }
//	
//	public void setName(String name)              { this.name = name; }
//	public void setPDBNumber(Integer pdbNumber)   { this.pdbNumber = pdbNumber; }
//   	public void setPosition(Point3d position)              { this.position = position; }
//	public void setPosition(double x, double y, double z)  { this.position = new Point3d(x, y, z); }
//	public void setCore(Core core)                { this.core = core; }
//	public void setSideChain(SideChain sideChain) { this.sideChain = sideChain; }
//	public void setNext(AminoAcid next)           { this.next = next; }
//	public void setPrev(AminoAcid prev)           { this.prev = prev; }
//	public void setHSEVector(Vector3d hseVector)  { this.hseVector = hseVector; }
//	public void setSphere(Sphere sphere)          { this.sphere = sphere; }
//	
//	public boolean hasCore() { return core != null; }
//	
//	/*
//	 * distance between two consecutive Ca atoms 
//	 */
//	public double getCaCaDistance() {
//		AminoAcid aminoAcid = getNextAminoAcid();
//		if (aminoAcid != null) return getCaCaDistance(aminoAcid); 
//		return 0.0;
//	}
//	
//	/*
//	 * distance to Ca of another amino acid
//	 */
//	public double getCaCaDistance(AminoAcid aminoAcid) { return getPosition().getDistance(aminoAcid.getPosition()); }
//
//	
//	
//	/*
//	 * returns the contact number of this amino acid for the specified radius r
//	 */
//	public int getContactNumber(int r) {
//		if ((contactRadius != r) || (contactNumber == null)) computeContactNumber(r);
//		return contactNumber.intValue();
//	}
//	
//	/*
//	 * computes the contact number of this amino acid for the specified radius r unless
//	 * it was computed earlier for this particular radius
//	 */
//	public void computeContactNumber(int r) {
//		int rr = r*r;
//		Point3d p = (hasCore())? getCore().getCa().getPosition() : getPosition();
//		Point3d q;
//		Integer contactNumber = 0;
//		AminoAcid aminoAcid = getPrevAminoAcid();
//		while (aminoAcid != null) {
//			q = (aminoAcid.hasCore())? aminoAcid.getCore().getCa().getPosition() : aminoAcid.getPosition();
//			if (p.getSquaredDistance(q) <= rr) contactNumber++;
//			aminoAcid = aminoAcid.getPrevAminoAcid();
//		}
//		aminoAcid = getNextAminoAcid();
//		while (aminoAcid != null) {
//			q = (aminoAcid.hasCore())? aminoAcid.getCore().getCa().getPosition() : aminoAcid.getPosition();
//			if (p.getSquaredDistance(q) <= rr) contactNumber++;
//			aminoAcid = aminoAcid.getNextAminoAcid();
//		}
//		contactRadius = r;
//	}
//	
//	
//	/*
//	 * returns up HSE number of this amino acid for the specified radius r
//	 */
//	public int getHSENumberUp(int r) {
//		if ((hseRadius != r) || (hseNumberUp == null)) computeHSENumbers(r);
//		return hseNumberUp.intValue();
//	}
//
//	/*
//	 * returns down HSE number of this amino acid for the specified radius r
//	 */
//	public int getHSENumberDown(int r) {
//		if ((hseRadius != r) || (hseNumberDown == null)) computeHSENumbers(r);
//		return hseNumberDown.intValue();
//	}
//
//	
//	/*
//	 * computes up and down HSE numbers of this amino acid for the specified radius r unless
//	 * they were computed earlier for this particular radius
//	 */
//	public void computeHSENumbers(int r) {
//		int rr = r*r;
//		Point3d p = (hasCore())? getCore().getCa().getPosition() : getPosition();
//		Point3d q;
//		Vector3d v = getHSEVector();
//		AminoAcid aminoAcid = getPrevAminoAcid();
//		while (aminoAcid != null) {
//			q = (aminoAcid.hasCore())? aminoAcid.getCore().getCa().getPosition() : aminoAcid.getPosition();
//			if (p.getSquaredDistance(q) <= rr) {
//				if (Point3d.isBehind(q, p, v)) hseNumberUp++; else hseNumberDown++;
//			}
//			aminoAcid = aminoAcid.getPrevAminoAcid();
//		}
//		aminoAcid = getNextAminoAcid();
//		while (aminoAcid != null) {
//			q = (aminoAcid.hasCore())? aminoAcid.getCore().getCa().getPosition() : aminoAcid.getPosition();
//			if (p.getSquaredDistance(q) <= rr) {
//				if (Point3d.isBehind(q, p, v)) hseNumberUp++; else hseNumberDown++;
//			}
//			aminoAcid = aminoAcid.getNextAminoAcid();
//		}	
//	}
//	
//	public int getSurfaceArea() { return sideChain.getSurfaceArea(); }
//	
//	/*
//	 * translates 3-letters amino acid names to 1-letter names
//	 */
//	public static char translateAA31(String str) {
//		if (str.equals("ALA") ) return 'A';
//		if (str.equals("ARG") ) return 'R';
//		if (str.equals("ASN") ) return 'N';
//		if (str.equals("ASP") ) return 'D';
//		if (str.equals("CYS") ) return 'C';
//		if (str.equals("GLU") ) return 'E';
//		if (str.equals("GLN") ) return 'Q';
//		if (str.equals("GLY") ) return 'G';
//		if (str.equals("HIS") ) return 'H';
//		if (str.equals("ILE") ) return 'I';
//		if (str.equals("LEU") ) return 'L';
//		if (str.equals("LYS") ) return 'K';
//		if (str.equals("MET") ) return 'M';
//		if (str.equals("PHE") ) return 'F';
//		if (str.equals("PRO") ) return 'P';
//		if (str.equals("SER") ) return 'S';
//		if (str.equals("THR") ) return 'T';
//		if (str.equals("TRP") ) return 'W';
//		if (str.equals("TYR") ) return 'Y';
//		if (str.equals("VAL") ) return 'V';
//		if (str.equals("MSE") ) return 'm'; // amino acid?
//		if (str.equals("PCA") ) return 'e'; 
//		if (str.equals("ACE") ) return 'a'; // acetyl group
//		if (str.equals("CXM") ) return 'c';
//		if (str.equals("PYR") ) return 'p';
//		if (str.equals("EXC") )	return 'x';
//		return '?';
//	}
//
//	/*
//	 * translates 1-letter amino acid names to 3-letters names
//	 */
//	public static String translateAA13(char c) {
//		if (c == 'A') return "ALA";
//		if (c == 'R') return "ARG";
//		if (c == 'N') return "ASN";
//		if (c == 'D') return "ASP";
//		if (c == 'C') return "CYS";
//		if (c == 'E') return "GLU";
//		if (c == 'Q') return "GLN";
//		if (c == 'G') return "GLY";
//		if (c == 'H') return "HIS";
//		if (c == 'I') return "ILE";
//		if (c == 'L') return "LEU";
//		if (c == 'K') return "LYS";
//		if (c == 'M') return "MET";
//		if (c == 'F') return "PHE";
//		if (c == 'P') return "PRO";
//		if (c == 'S') return "SER";
//		if (c == 'T') return "THR";
//		if (c == 'W') return "TRP";
//		if (c == 'Y') return "TYR";
//		if (c == 'V') return "VAL";
//		if (c == 'm') return "MSE";  // amino acid
//		return "???";
//	}
//
//	/*
//	 * translates 1-letter amino acid names to 3-letters names
//	 */
//	public static int toInt(char c) {
//		if (c == 'A') return 0;
//		if (c == 'R') return 1;
//		if (c == 'N') return 2;
//		if (c == 'D') return 3;
//		if (c == 'C') return 4;
//		if (c == 'E') return 5;
//		if (c == 'Q') return 6;
//		if (c == 'G') return 7;
//		if (c == 'H') return 8;
//		if (c == 'I') return 9;
//		if (c == 'L') return 10;
//		if (c == 'K') return 11;
//		if (c == 'M') return 12;
//		if (c == 'F') return 13;
//		if (c == 'P') return 14;
//		if (c == 'S') return 15;
//		if (c == 'T') return 16;
//		if (c == 'W') return 17;
//		if (c == 'Y') return 18;
//		if (c == 'V') return 19;
//		if (c == 'm') return 20;  // amino acid
//		return 20;
//	}
//
//	/*
//	 * translates 3-letters amino acid names to 1-letter names
//	 */
//	public static char toChar(int i) { return aaTable[i]; }
//
//
//
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//
//	}
//
//}
