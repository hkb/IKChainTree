//package molecule;
//
//import geom3d.*;
//import guiMolecule.ProteinViewer;
//import dataStructures.*;
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//
//import com.sun.j3d.utils.applet.MainFrame;
//
///*
// * HISTORY:
// * 
// * PROTEIN originates from Greek word proteios meaning "primary" or "of first rank". 
// * This name was adapted by Jšns Berzelius in 1838 to empasize the importance of these 
// * molecules.
// * 
// * Proteins are constituent molecules in muscle tissues, ligaments, tendons, bones, 
// * skin, hair, organs and glands.
// * 
// * Proteins are carriers (hemoglobin crries oxygen) and storages (myoglobin stores oxygen in muscle cells)
// * Proteins are triggers of physiological processes.
// * 
// * globular (compact), fibrous (extended in shape) and membrane proteins.
// * 
// * Myoglobin is highly compact consisting of 75% a-helices.
// * 
// * Average protein has several hundreds of residues. Muscle protein titin has about 27.000 residues.
// */
//
//public class ProteinBak {
//	private int n;                      // number of amino acids
//	String type = null;
//	private AminoAcid firstAminoAcid;   // first amino acid
//	private int firstAminoAcidNumber;   // numbering in PDB does not always start with 1.
//	private AminoAcid lastAminoAcid;    // last amino acid
//	private Set alphaHelices;
//	private Set betaSheets;
//	private char[] sequence;
//	private ProteinViewer viewer;
//	
//	/*
//	 * reads a pdb-file and creates a protein. 
//	 * 
//	 * Parameter proteinName consists of the 4-letters protein name possibly followed by 1-letter chain identification.
//	 * If chain identification is not specified, 'A' is assumed
//	 * 
//	 * Parameter type specifies which atoms are to be included. More specifically
//	 * 
//	 * type = "Ca" -   only Ca atoms are included
//	 * type = "NCaC" - only backbone atoms N, Ca and C are included
//	 * type = "NCaCO" - only backbone atams N, Ca, C, O are included
//	 * type = "NCaCOH" - all backbone atoms with hydrogen atoms are included
//	 * 
//	 * Side chains are so far ignored.
//	 * 
//	 * If parameter position is true, coordinates of atoms are fetched from the pdb file
//	 */
//	public ProteinBak(String proteinName, String type, boolean position) {
//		try {
//			this.type = type;
//			String fileName = proteinName.substring(0,4);
//			char chainName = (proteinName.length() == 4)? 'A' : proteinName.charAt(4);
//			String source = PDB.getFilePath(fileName);
//			String record = null;
//			String atomSymbol = null;
//			alphaHelices = new Set();
//			betaSheets = new Set();
//			BufferedReader br = new BufferedReader(new FileReader(source));
//			record = new String();
//			n = 0;
//			AminoAcid aa = null;
//			record = br.readLine();
//			while ((record != null) && !record.startsWith("ENDMDL")) {
//				if (record.startsWith("ATOM")) {
//					if (record.charAt(21) == chainName) {
//						atomSymbol = PDB.getAtomSymbol(record);
//						if (atomSymbol.equals("N")) {
//							aa = new AminoAcid(PDB.getAtomAminoAcidName(record), type);
//							if (firstAminoAcid == null) {
//								firstAminoAcidNumber = PDB.getAtomResidueNumber(record);
//								firstAminoAcid = lastAminoAcid = aa;
//							}
//							else {
//								aa.setPrev(lastAminoAcid);
//								lastAminoAcid.setNext(aa);
//								lastAminoAcid = aa;
//							}
//							aa.setName(PDB.getAtomAminoAcidName(record));
//							System.out.println(aa.getName());
//							if (position && aa.hasCore()) aa.getCore().getN().setPosition(PDB.getAtomCoordinates(record));
//							n++;
//						}
//						else {
//							if (atomSymbol.equals("CA")) {
//								if (position) {
//									if (aa.hasCore()) aa.getCore().getCa().setPosition(PDB.getAtomCoordinates(record));
//									else aa.setPosition(PDB.getAtomCoordinates(record));
//								}
//							}
//							else {
//								if (atomSymbol.equals("C")) {
//									if (position && aa.hasCore()) aa.getCore().getC().setPosition(PDB.getAtomCoordinates(record));
//								}
//								else {
//									if (atomSymbol.equals("O")) {
//										if (position && aa.hasCore() && aa.getCore().hasO()) aa.getCore().getO().setPosition(PDB.getAtomCoordinates(record));
//									}
//								}
//							}
//						}
//					}
//				}
//				if (record.startsWith("HELIX")) {
//					if (PDB.getHelixClass(record) == 1) {
//						int initSeqNum = PDB.getHelixInitSeqNum(record) - firstAminoAcidNumber + 1;
//						int endSeqNum = PDB.getHelixEndSeqNum(record) - firstAminoAcidNumber + 1;
//						AlphaHelix alphaHelix = new AlphaHelix(this, initSeqNum, endSeqNum);
//						alphaHelices.insert(alphaHelix);
//					}
//				} 
//				if (record.startsWith("SHEET")) {
//					if (PDB.getStrandNumber(record) == 1) {
//						BetaSheet betaSheet = new BetaSheet(this);
//						int initSeqNum = PDB.getBetaStrandInitSeqNum(record) - firstAminoAcidNumber + 1;
//						int endSeqNum = PDB.getBetaStrandEndSeqNum(record) - firstAminoAcidNumber + 1;
//						int sense = PDB.getBetaStrandSense(record);
//						betaSheet.betaStrands.insert(new BetaStrand(this, initSeqNum, endSeqNum, sense));
//						betaSheets.insert(betaSheet);
//					}
//				}
//				record = br.readLine();
//			}
//		} catch (IOException e) { System.out.println("IOException error!"); e.printStackTrace(); }
//	}
//
//	/*
//	 * returns number of amino acid
//	 */
//	public int getSize() { return n; }
//	
//	/* 
//	 * returns number of atoms
//	 */
//	public int getNumberAtoms() {
//		int count = 0;
//		Core core = null;
//		AminoAcid aminoAcid = firstAminoAcid;
//		while (aminoAcid != null) {
//			if (!aminoAcid.hasCore()) count++;
//			else {
//				core = aminoAcid.getCore();
//				count = count + 3;
//				if (core.hasO()) count++;
//				if (core.hasH()) count = count + 2;
//			}
//			aminoAcid = aminoAcid.getNextAminoAcid();
//		}
//		return count;
//	}
//	
//	public int getNumberAlphaHelices() { if (alphaHelices == null) return 0; else return alphaHelices.getSize(); }
//	
//	public int getNumberBetaSheets() { if (betaSheets == null) return 0; else return betaSheets.getSize(); }
//
//	public String getType() { return type; }
//	public AminoAcid getFirstAminoAcid() { return firstAminoAcid; }
//
//	public char[] getSequence() { 
//		if (sequence == null) {
//			sequence = new char[n];
//			AminoAcid aa = firstAminoAcid;
//			int i = 0;
//			while (aa != null) {
//				sequence[i++] = AminoAcid.translateAA31(aa.getName());
//				aa = aa.getNextAminoAcid();
//			}
//		}
//		return sequence; 
//	}
//	
//	public void setSequence(char[] sequence) { this.sequence = sequence; }
//
//	/*
//	 * appends given amino acid to the end of the protein
//	 */
//	public void append(AminoAcid aminoAcid) {
//		if (n == 0) firstAminoAcid = aminoAcid;
//		else {
//			AminoAcid prevAminoAcid = lastAminoAcid;
//			prevAminoAcid.setNext(aminoAcid);
//			aminoAcid.setPrev(prevAminoAcid);
//		}
//		lastAminoAcid = aminoAcid;
//		n = n++;	
//	}
//	
//	public void appendChain(Chain3d chain) {
//		DLNode nd = chain.getFirst();
//		while (nd != null) {
//			append(new AminoAcid("", "Ca", (Point3d)nd.getObject()));
//			nd = nd.getNext();
//		}
//	}
//	
//	public void setHSEVectors() {
//		Point3d p;
//		AminoAcid prevAminoAcid = firstAminoAcid;
//		AminoAcid aminoAcid = (prevAminoAcid != null)? prevAminoAcid.getNextAminoAcid() : null;
//		AminoAcid nextAminoAcid = (aminoAcid != null)? aminoAcid.getNextAminoAcid() : null;
//		while (nextAminoAcid != null) {
//			p = aminoAcid.getPosition();
//			aminoAcid.setHSEVector(Vector3d.createSum(new Vector3d(p, prevAminoAcid.getPosition()), new Vector3d(p, nextAminoAcid.getPosition())));
//			prevAminoAcid = aminoAcid;
//			aminoAcid = nextAminoAcid;
//			nextAminoAcid = aminoAcid.getNextAminoAcid();
//		}
//	}
//	
//	
//	public void setLinearCa(double d) {
//		int i = 0;
//		AminoAcid aminoAcid = firstAminoAcid;
//		while (aminoAcid != null) {
//			if (!aminoAcid.hasCore()) aminoAcid.setPosition(i*d, 0.0, 0.0);
//			aminoAcid = aminoAcid.getNextAminoAcid();
//			i++;
//		}
//	}
//	
//	public void print() {
//		Core core = null;
//		AminoAcid aminoAcid = firstAminoAcid;
//		while (aminoAcid != null) {
//			if (!aminoAcid.hasCore()) System.out.println(aminoAcid.getName() + " - Ca: " + aminoAcid.getPosition().toString(3));
//			else {
//				core = aminoAcid.getCore();
//				System.out.println(aminoAcid.getName() + " - N : " + core.getN().getPosition());
//				System.out.println("    - Ca: " + core.getCa().getPosition());
//				System.out.println("    - C : " + core.getC().getPosition());
//				if (core.hasO()) System.out.println("    - O : " + core.getO().getPosition());
//				if (core.hasH()) {
//					System.out.println("    - HN: " + core.getHN().getPosition());
//					System.out.println("    - HCa:" + core.getHCa().getPosition());
//				}
//			}
//			aminoAcid = aminoAcid.getNextAminoAcid();
//		}
//	}
//	
//	/*
//	 * returns the coordinates of atoms, one atom per line
//	 */
//	public PointSet3d getPointSet() {
//		PointSet3d points = new PointSet3d();
//		Core core = null;
//		AminoAcid aminoAcid = firstAminoAcid;
//		while (aminoAcid != null) {
//			if (type.equals("Ca") || !aminoAcid.hasCore()) points.insert(aminoAcid.getPosition());
//			else {
//				core = aminoAcid.getCore();
//				points.insert(core.getN().getPosition());
//				points.insert(core.getCa().getPosition());
//				points.insert(core.getC().getPosition());
//				if (core.hasO()) points.insert(core.getO().getPosition());
//				if (core.hasH()) {
//					points.insert(core.getHN().getPosition());
//					points.insert(core.getHCa().getPosition());
//				}
//			}
//			aminoAcid = aminoAcid.getNextAminoAcid();
//		}
//		return points;
//	}
//
//	/*
//	 * prints protein sequence (1-letter symbols)
//	 */
//	public void printSymbols() {
//		System.out.println(String.copyValueOf(getSequence()));
//	}
//	
//	/*
//	 * prints the coordinates of atoms, one atom per line
//	 */
//	public void printCoordinates() {
//		Point3d p;
//		Core core = null;
//		AminoAcid aminoAcid = firstAminoAcid;
//		while (aminoAcid != null) {
//			if (!aminoAcid.hasCore()) {
//				p = aminoAcid.getPosition();
//				System.out.println(p.getX() + " " + p.getY() + " " + p.getZ());
//			}
//			else {
//				core = aminoAcid.getCore();
//				p = core.getN().getPosition();
//				System.out.println(p.getX() + " " + p.getY() + " " + p.getZ());
//				p = core.getCa().getPosition();
//				System.out.println(p.getX() + " " + p.getY() + " " + p.getZ());
//				p = core.getC().getPosition();
//				System.out.println(p.getX() + " " + p.getY() + " " + p.getZ());
//				if (core.hasO()) {
//					p = core.getO().getPosition();
//					System.out.println(p.getX() + " " + p.getY() + " " + p.getZ());
//				}
//				if (core.hasH()) {
//					p = core.getHN().getPosition();
//					System.out.println(p.getX() + " " + p.getY() + " " + p.getZ());
//					p = core.getHCa().getPosition();
//					System.out.println(p.getX() + " " + p.getY() + " " + p.getZ());
//				}
//			}
//			aminoAcid = aminoAcid.getNextAminoAcid();
//		}
//	}
//
//	public void printAlphaHelicesIntervals() {
//		String str = "";
//		int size = alphaHelices.getSize();
//		for (int i = 0; i < size; i++) str = str + ((AlphaHelix)alphaHelices.get(i)).toString();
//		System.out.println("Alpha helices, intervals: " + str);
//	}
//
//	public void printAlphaHelicesSymbols() {
//		String str = "";
//		int size = alphaHelices.getSize();
//		for (int i = 0; i < size; i++) str = str + "[" + ((AlphaHelix)alphaHelices.get(i)).getSymbols() + "]";
//		System.out.println("Alpha helices, symbols: " + str);
//	}
//
//	/*
//	 * prints gap sizes (number of amino acids) between consecutive alpha helices
//	 */
//	public void printAlphaHelicalGapLengths() {
//		AlphaHelix alphaHelix;
//		if (!alphaHelices.isEmpty()) {
//			AlphaHelix alphaHelixPrev = (AlphaHelix)alphaHelices.get(0);
//			int size = alphaHelices.getSize();
//			System.out.print("Lenghts of alpha helical gaps: ");
//			for (int i = 1; i < size; i++) {
//				alphaHelix = (AlphaHelix)alphaHelices.get(i);
//				System.out.print(alphaHelix.getInitSeqNum() - alphaHelixPrev.getEndSeqNum() - 1 + " ");
//				alphaHelixPrev = alphaHelix;
//			}
//			System.out.println();
//		}
//	}
//	
//	/*
//	 * prints phi angles. Core is required
//	 */
//	public void printPhiAngles() {
//		AminoAcid aminoAcid = firstAminoAcid;
//		AminoAcid next = (aminoAcid != null)? aminoAcid.getNextAminoAcid() : null;
//		while (next != null) {
//			System.out.println(aminoAcid.getName() + ": " + next.getCore().getPhiAngle());
//			aminoAcid = next;
//			next = aminoAcid.getNextAminoAcid();
//		}
//	}
//
//	public void printPsiAngles() {
//		AminoAcid aminoAcid = firstAminoAcid;
//		AminoAcid next = (aminoAcid != null)? aminoAcid.getNextAminoAcid() : null;
//		while (next != null) {
//			System.out.println(aminoAcid.getName() + ": " + aminoAcid.getCore().getPsiAngle());
//			aminoAcid = next;
//			next = aminoAcid.getNextAminoAcid();
//		}
//	}
//
//	public void printOmegaAngles() {
//		AminoAcid aminoAcid = firstAminoAcid;
//		AminoAcid next = (aminoAcid != null)? aminoAcid.getNextAminoAcid() : null;
//		while (next != null) {
//			System.out.println(aminoAcid.getName() + ": " + aminoAcid.getCore().getOmegaAngle());
//			aminoAcid = next;
//			next = aminoAcid.getNextAminoAcid();
//		}
//	}
//	
//	public void printDihedralAngles() {
//		Core core = null;
//		int count = 0;
//		AminoAcid aminoAcid = firstAminoAcid;
//		while (aminoAcid != null) {
//			count++;
//			core = aminoAcid.getCore();
//			System.out.println(count + ". " + aminoAcid.getName() + ": " + "(" + core.getPhiAngle() +", " + core.getPsiAngle() + ", " + core.getOmegaAngle() + ")");
//			aminoAcid = aminoAcid.getNextAminoAcid();
//		}
//	}
//	
//	public void printDistances() {
//		Core core = null;
//		int count = 0;
//		AminoAcid aminoAcid = firstAminoAcid;
//		while (aminoAcid != null) {
//			count++;
//			if (aminoAcid.hasCore()) {
//				core = aminoAcid.getCore();
//				System.out.println(count + ". " + aminoAcid.getName() + ": " + core.getNCaDistance() + ", " + core.getCaCDistance() + ", " + core.getCNDistance());
//			}
//			else aminoAcid.getCaCaDistance();
//			aminoAcid = aminoAcid.getNextAminoAcid();
//		}
//	}
//	public void setViewer(ProteinViewer viewer) { this.viewer = viewer; }
//
//	
//	/**
//	 * @param args
//	 */
//	
//
//	public static void main(String[] args) {
////		Protein protein = new Protein("1A6X","NCaC",true);
////		System.out.println(protein.getNumberAtoms());
////		protein.printCoordinates();
////		protein.setLinearCa(3.8);
////		protein.setSequence(PDB.getSequence("2CRO", 'A'));
//		ProteinViewer viewer = new ProteinViewer();
//		new MainFrame(viewer, 640, 480);
///*		System.out.println("Number alpha helices is " + protein.getNumberAlphaHelices());
//		System.out.println("Number beta sheets is "   + protein.getNumberBetaSheets());
//		protein.printSymbols();
//		protein.printAlphaHelicesIntervals();
//		protein.printAlphaHelicesSymbols();
//		protein.printAlphaHelicalGapLengths();
//*/	}
//}
