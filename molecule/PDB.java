package molecule;

import gui.Histogram;
import misc.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import geom3d.*;
import guiMolecule.RamachandranMap;

public class PDB {	
	
	/*
	 * gets the input file
	 */
	public static BufferedReader getFileName(String fileName) { 
		BufferedReader br = null;
		try {
			String chainName;
			//String source = "/Volumes/Home/pawel/Documents/MotherOfAllProjects/pdb_files/";
			String source = "pdb_files/";
			if (fileName == "") {				
				System.out.print("Enter the name of PDB-file: ");
				BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
				chainName = in.readLine();
			}
			else chainName = fileName;
			source = source + chainName + ".pdb";
			System.out.println(source);
			br = new BufferedReader(new FileReader(source));
		} catch (IOException e) { System.out.println("IOException error!"); e.printStackTrace(); }
		return br;
	}

	
	/*
	 * scans to the first record of a particular record type in a given chain
	 */
	public static String getFirstRecord(BufferedReader br, char chainNam, String recordName) {
		String record = null;
		try {
			record = br.readLine();
			while ((record != null) && !record.startsWith(recordName)) record = br.readLine();
		} catch (IOException e) { System.out.println("IOException error!"); e.printStackTrace(); }
		return record;
	} 
	
	/*
	 * scans to the first ATOM record of the first model
	 */
	public static String getFirtAtomRecordFirstModel(BufferedReader br) {
		return getFirstRecord(br, 'A', "ATOM");
	}
	// methods related to a given ATOM record in the PDB file
	
	/*
	 * atom symbol of the ATOM record in the PDB file
	 */
	public static String getAtomSymbol(String record) { return record.substring(12,16).trim(); }
	
	public static String getAltLoc(String record) { return record.substring(16,17); }
	public static char getAltLocChar(String record) { return record.charAt(16); }	
	
	/*
	 * name of the amino acid of the ATOM record in the PDB file
	 */
	public static String getAtomAminoAcidName(String record) { return record.substring(17,20).trim(); }
	public static String getAtomResidueName(String record) { return getAtomAminoAcidName(record); }
	public static int getAtomResidueNumber(String record) { return Integer.parseInt(record.substring(22, 26).trim()); }

	/*
	 * chain name of the ATOM record in the PDB file
	 */
	public static String getAtomChainName(String record) { return record.substring(21, 22).trim(); }
	public static char getAtomChainChar(String record) { return record.charAt(21); }

	/*
	 * residue sequence number of the ATOM record in the PDB file
	 */
	public static int getResidueSequenceNumber(String record) { return Integer.parseInt(record.substring(22, 26).trim()); }
	
	public static String getInsertionCode(String record) { return record.substring(26, 27).trim(); }
	public static char getInsertionCodeChar(String record) { return record.charAt(26); }

	/*
	 * point with the coordinates of the atom in the ATOM record in the PDB file
	 */
	public static Point3d getAtomCoordinates(String record) {
		return new Point3d(Double.parseDouble(record.substring(31, 38).trim()), 
						   Double.parseDouble(record.substring(39, 46).trim()),
						   Double.parseDouble(record.substring(47, 54).trim()));
	}
	
	/*
	 * returns the helix class in the HELIX record in the PDB file.
	 */
	public static int getHelixClass(String record) {
		return Integer.parseInt(record.substring(39, 40).trim());
	}
	
	/*
	 * returns the index of the first amino acid in the specified helix.
	 */
	public static int getHelixInitSeqNum(String record) { return Integer.parseInt(record.substring(21, 25).trim()); }
	
	/*
	 * returns the index of the last amino acid in the specified helix.
	 */
	public static int getHelixEndSeqNum(String record) { return Integer.parseInt(record.substring(33, 37).trim());	}

	public static int getSS1SeqNum(String record) { return Integer.parseInt(record.substring(17,21).trim()); }
	public static int getSS2SeqNum(String record) { return Integer.parseInt(record.substring(31,35).trim()); }

	// methods related to the set of ATOM records 
	
	/*
	 * first ATOM record with a given atomName, aminoAcidName and chain.
	 */
	public static String getFirstAtomRecord(BufferedReader br, char chainName, String atomName, String aminoAcidName) {
		String record = null;
		try {
			record = br.readLine();
			// scan to the first atom of chain chainName
			while ((record != null) && !record.startsWith("ATOM")) record = br.readLine();
			while (record != null) {
				if (getAtomSymbol(record).equals(atomName) && 
					(record.charAt(21) == chainName) && 
					(getAltLoc(record).equals(" ") 	|| getAltLoc(record).equals("A")) &&
					(aminoAcidName.equals("") || getAtomAminoAcidName(record).equals(aminoAcidName))	) {
					return record;
				}
				record = br.readLine();
			}
		} catch (IOException e) { System.out.println("IOException error!"); e.printStackTrace(); }
		return record;
	}
	
	/*
	 * first ATOM record with given atomName and chainName.
	 */
	public static String getFirstAtomRecord(BufferedReader br, char chainName, String atomName) {
		return getFirstAtomRecord(br, chainName, atomName, ""); 
	}

	/*
	 * next ATOM record with a given atomName and chainName
	 */
	public static String getNextAtomRecord(BufferedReader br, char chainName, String atomName) {
		String record = null;
		boolean found = false;
		try {
			record = br.readLine();
			while ((record != null) && !found && !record.startsWith("TER")) {
				if (record.startsWith("ATOM") && getAtomSymbol(record).equals(atomName) && (record.charAt(21) == chainName) && (getAltLoc(record).equals(" ") || getAltLoc(record).equals("A"))) found = true;
				else record = br.readLine();
			}
		} catch (IOException e) { System.out.println("IOException error!"); e.printStackTrace(); }
		if (found) return record; else return null;
	}
	
	/*
	 * first ATOM record with a given residue sequence number
	 */
	public static String getFirstAtomResidueSequenceNumber(BufferedReader br, String record, int residueSequenceNumber) {
		String targetRecord = record;
		try {
			while (targetRecord.startsWith("ATOM") && 
				   (getResidueSequenceNumber(targetRecord) != residueSequenceNumber)) targetRecord = br.readLine(); 
		} catch (IOException e) { System.out.println("IOException error!"); e.printStackTrace(); }
		return targetRecord;
	}

	// methods related to a given HELIX record in the PDB file.
	
	/*
	 * sequence number of the first residue of the helix
	 */
	public static int getSequenceNumberInitialResidue(String record) {
		return Integer.parseInt(record.substring(22, 25).trim());
	}
	
	/*
	 * sequence number of the terminal residue of the helix
	 */
	public static int getSequenceNumberTerminalResidue(String record) {
		return Integer.parseInt(record.substring(34, 37).trim());
	}
	
	// methods related to the set of HELIX records 
	
	/* 
	 * first PDB-line with a HELIX record
	 */
	public static String getFirstHelixRecord(BufferedReader br, char chainName) {
		return getFirstRecord(br, chainName, "HELIX");
	}
	
	/*
	 * next PDB-line with a HELIX record
	 */
	public static String getNextHelixRecord(BufferedReader br) {
		String record = null;
		try {
			record = br.readLine();
		} catch (IOException e) { System.out.println("IOException error!"); e.printStackTrace(); }			
		if (record.startsWith("HELIX")) return record; else return null; 
	}
	
	public static char getHelixInitChainID(String record) { return record.charAt(19); }

	public static char getHelixEndChainID(String record) { return record.charAt(31); }

	// methods related to a given SHEET record
	
	
	public static int getBetaStrandNumber(String record) {
		return Integer.parseInt(record.substring(8, 10).trim());
	}

	public static int getNumberBetaStrands(String record) {
		return Integer.parseInt(record.substring(15,16).trim());
	}

	public static char getBetaStrandInitChainID(String record) {
		return record.charAt(21);
	}
	
	public static int getBetaStrandInitSeqNum(String record) {
//		System.out.println(record.substring(22, 26).trim());
		return Integer.parseInt(record.substring(22, 26).trim());	
	}

	public static char getBetaStrandEndChainID(String record) {
		return record.charAt(32);
	}
	
	public static int getBetaStrandEndSeqNum(String record) {
		return Integer.parseInt(record.substring(33, 37).trim());	
	}
	
	public static int getBetaStrandSense(String record) {
		return Integer.parseInt(record.substring(38, 40).trim());	
	}

	
	//  methods related to the set of SHEET records
	
	/* 
	 * first PDB-line with a SHEET record
	 */
	public static String getFirstSheetRecord(BufferedReader br, char chainName) {
		return getFirstRecord(br, chainName, "SHEET");
	}
	
	public static String getBetaSheetId(String record) {
		return record.substring(11,14).trim();
	}
	
	/*
	 * next PDB-line with a SHEET record
	 */
	public static String getNextSheetRecord(BufferedReader br) {
		String record = null;
		try {
			record = br.readLine();
		} catch (IOException e) { System.out.println("IOException error!"); e.printStackTrace(); }			
		if (record.startsWith("SHEET")) return record; else return null; 
	}

	public static char getSS1BondChainId(String record) { return record.charAt(15); } 
	public static char getSS2BondChainId(String record) { return record.charAt(29); } 
	
	/*
	 * returns the path to the pdb file
	 */
	public static String getFilePath(String fileName) {
//		String source = "/Volumes/Home/pawel/Documents/workspace/proteins/src/protein/";
//		String source = "/Volumes/Home/pawel/Documents/MotherOfAllProjects/pdb_files/";
		String source = "pdb_files/";

		try {
			if (fileName == "") {				
				System.out.print("Enter the name of PDB-file: ");
				BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
				source = source + in.readLine() + ".pdb";
			}
			else source = source + fileName + ".pdb";
		} catch (IOException e) { System.out.println("IOException error!"); e.printStackTrace(); }
		
		if(!new File(source).exists() && new File("pdb_files_Lotan/"+fileName+".pdb").exists())
			source = "pdb_files_Lotan/"+fileName+".pdb";
		
		return source;
	}
	
	public static char[] getSequence(String fileName, char chainName) {
		char[] sequence = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String record = br.readLine();
			while ((record != null) && (!record.startsWith("ATOM") || (record.charAt(11) != chainName))) record = br.readLine(); 
			int i = 0;
			while ((record != null) && record.startsWith("ATOM")) {
				if (getAtomSymbol(record).equals("CA")) {
					sequence[i] = AminoAcid.translateAA31(getAtomAminoAcidName(record));
					i++;
				}
				record = br.readLine();
			}
		} catch (IOException e) { System.out.println("IOException error!"); e.printStackTrace(); }
		return sequence;
	}

	/*
	 * returns the number of amino acids as specified in the pdb file in DBREF line
	 */
	public static int getNumberAminoAcids(String name, char chainName) {
		int count = 0;
		try {
			String fileName = "/Volumes/Home/pawel/pdbselect/" + name + ".pdb";			
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			getFirstAtomRecord(br, chainName, "CA");
			count = 1;
			while (getNextAtomRecord(br, chainName, "CA") != null) count++;
		} catch (IOException e) { System.out.println("IOException error!"); e.printStackTrace(); }
		return count;
	}

	
	/*
	 * computes phi dihedral angles for all amino acids of given type and adDs them to the histogram
	 */
	public static void getPhiAngles(int number, String name, char chainName, String aminoAcidName, Histogram histogram) {
		Point3d p1 = null;
		Point3d p2 = null;
		Point3d p3 = null;
		Point3d p4 = null;
		String record1 = null;
		String record2 = null;
		String record3 = null;
		String record4 = null;
		int count = 0;
		try {
			String fileName = "/Volumes/Home/pawel/pdbselect/" + name + ".pdb";			
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			record1 = getFirstAtomRecord(br, chainName, "C");
			if (record1 != null) record2 = getNextAtomRecord(br, chainName, "N"); else record2 = null;
			if (record2 != null) record3 = getNextAtomRecord(br, chainName, "CA"); else record3 = null;
			if (record3 != null) record4 = getNextAtomRecord(br, chainName, "C"); else record4 = null;
			while (record4 != null) {
				if (getAtomAminoAcidName(record2).equals(aminoAcidName)) {
					p1 = getAtomCoordinates(record1);
					p2 = getAtomCoordinates(record2);
					p3 = getAtomCoordinates(record3);
					p4 = getAtomCoordinates(record4);
					count++;
					histogram.inc(Functions.toRadians(Point3d.getDihedralAngle(p1, p2, p3, p4)));
				}
				record1 = record4;
				record2 = getNextAtomRecord(br, chainName, "N");
				if (record2 != null) record3 = getNextAtomRecord(br, chainName, "CA"); else record3 = null;
				if (record3 != null) record4 = getNextAtomRecord(br, chainName, "C"); else record4 = null;
			}
		} catch (IOException e) { System.out.println("IOException error!"); e.printStackTrace(); }
	}

	
	/*
	 * computes psi dihedral angles for all amino acids of given type and ads them to the histogram
	 */
	public static void getPsiAngles(int number, String name, char chainName, String aminoAcidName, Histogram histogram) {
		Point3d p1 = null;
		Point3d p2 = null;
		Point3d p3 = null;
		Point3d p4 = null;
		String record1 = null;
		String record2 = null;
		String record3 = null;
		String record4 = null;
		int count = 0;
		try {
			String fileName = "/Volumes/Home/pawel/pdbselect/" + name + ".pdb";			
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			record1 = getFirstAtomRecord(br, chainName, "N");
			if (record1 != null) record2 = getNextAtomRecord(br, chainName, "CA"); else record2 = null;
			if (record2 != null) record3 = getNextAtomRecord(br, chainName, "C"); else record3 = null;
			if (record3 != null) record4 = getNextAtomRecord(br, chainName, "N"); else record4 = null;
			while (record4 != null) {
				if (getAtomAminoAcidName(record1).equals(aminoAcidName)) {
					p1 = getAtomCoordinates(record1);
					p2 = getAtomCoordinates(record2);
					p3 = getAtomCoordinates(record3);
					p4 = getAtomCoordinates(record4);
					count++;
					histogram.inc(Functions.toRadians(Point3d.getDihedralAngle(p1, p2, p3, p4)));
				}
				record1 = record4;
				record2 = getNextAtomRecord(br, chainName, "CA");
				if (record2 != null) record3 = getNextAtomRecord(br, chainName, "C"); else record3 = null;
				if (record3 != null) record4 = getNextAtomRecord(br, chainName, "N"); else record4 = null;
			}
		} catch (IOException e) { System.out.println("IOException error!"); e.printStackTrace(); }
	}

	/*
	 * computes phi dihedral angles for all amino acids of given type and ads them to the histogram
	 */
	public static void getPhiPsiAngles(int number, String name, char chainName, String aminoAcidName, RamachandranMap rm) {
		Point3d p1 = null;
		Point3d p2 = null;
		Point3d p3 = null;
		Point3d p4 = null;
		Point3d p5 = null;
		String record1 = null;
		String record2 = null;
		String record3 = null;
		String record4 = null;
		String record5 = null;
		int count = 0;
		try {
			String fileName = "/Volumes/Home/pawel/pdbselect/" + name + ".pdb";			
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			record1 = getFirstAtomRecord(br, chainName, "C");
			System.out.println(record1);
			if (record1 != null) record2 = getNextAtomRecord(br, chainName, "N"); else record2 = null;
			if (record2 != null) record3 = getNextAtomRecord(br, chainName, "CA"); else record3 = null;
			if (record3 != null) record4 = getNextAtomRecord(br, chainName, "C"); else record4 = null;
			if (record4 != null) record5 = getNextAtomRecord(br, chainName, "N"); else record5 = null;
			while (record5 != null) {
				if (getAtomAminoAcidName(record3).equals(aminoAcidName)) {
					p1 = getAtomCoordinates(record1);
					p2 = getAtomCoordinates(record2);
					p3 = getAtomCoordinates(record3);
					p4 = getAtomCoordinates(record4);
					p5 = getAtomCoordinates(record5);
					count++;
					rm.inc(Math.round(Functions.toRadians(Point3d.getDihedralAngle(p1, p2, p3, p4))), 
						   Math.round(Functions.toRadians(Point3d.getDihedralAngle(p2, p3, p4, p5))));
				}
				record1 = record4;
				record2 = record5;
				record3 = getNextAtomRecord(br, chainName, "CA");
				record4 = getNextAtomRecord(br, chainName, "C");
				record5 = getNextAtomRecord(br, chainName, "N");
			}
		} catch (IOException e) { System.out.println("IOException error!"); e.printStackTrace(); }
	}

	
	public static double[] getCaDistances(int number, String name, char chainName) {
		Point3d p = null;
		Point3d q = null;
		int count = 0;
		String fileName;
		String record = null;
		String aminoAcidName = null;
		double dist;
		int size = getNumberAminoAcids(name, chainName)-1;
		double distances[] = new double[size];
		try {
			fileName = "/Volumes/Home/pawel/pdbselect/" + name + ".pdb";			
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			record = getFirstAtomRecord(br, chainName, "CA");
			if (record != null) q = getAtomCoordinates(record);
			while (count != size) {
				p = q;
				record = getNextAtomRecord(br, chainName, "CA");
				if (record != null) {
					q = getAtomCoordinates(record);
					distances[count] = p.getDistance(q);
					aminoAcidName = getAtomAminoAcidName(record);
					dist = distances[count];
					if ( (dist < 2.7) || (dist > 4.2)) {
						System.out.println(number + ". " + name + ", " + count + ": " + Functions.toString(dist) + " " + aminoAcidName);
					}
					count++;
				}
			}
		} catch (IOException e) { System.out.println("IOException error!"); e.printStackTrace(); }
		return distances;
	}
	
	public static void getXXXPROYYYCaDistances(int number, String name, char chainName, String aminoAcidName, Histogram histogram) {
		Point3d p1 = null;
		Point3d p2 = null;
		Point3d p3 = null;
		String fileName;
		String record1 = null;
		String record2 = null;
		String record3 = null;
		int count = 2;
		try {
			fileName = "/Volumes/Home/pawel/pdbselect/" + name + ".pdb";			
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			record1 = getFirstAtomRecord(br, chainName, "CA");
			if (record1 != null) record2 = getNextAtomRecord(br, chainName, "CA");
			if (record2 != null) record3 = getNextAtomRecord(br, chainName, "CA");
			while (record3 != null) {
				if (getAtomAminoAcidName(record2).equals(aminoAcidName)) {
					p1 = getAtomCoordinates(record1);
					p2 = getAtomCoordinates(record2);
					p3 = getAtomCoordinates(record3);
					histogram.inc(p1.getDistance(p2));
					histogram.inc(p2.getDistance(p3));					
//					System.out.print(number + ". " + name + ", " + count + ": ");
//					System.out.print(getAtomAminoAcidName(record1) + "-PRO "+ Functions.toString(p1.getDistance(p2)));
//					System.out.println(" PRO-" + getAtomAminoAcidName(record3) + " " + Functions.toString(p2.getDistance(p3)));
				}
				record1 = record2;
				record2 = record3;
				record3 = getNextAtomRecord(br, chainName, "CA");
				count++;
			}
		} catch (IOException e) { System.out.println("IOException error!"); e.printStackTrace(); }
	}

	
	/*
	 * returns a string as long as the number of amino acids indicating which kind of secondary structure it belongs:
	 * 
	 * H - helix
	 * S - sheet
	 * C - coil
	 */
	public static String getSecondaryStructure(String proteinName, int size) {
		String fileName = proteinName.substring(0,4);
		char chainName = (proteinName.length() == 4)? 'A' : proteinName.charAt(4);
		char[] str = new char[size];
		for (int i = 0; i < size; i++) str[i] ='-';
		String source = getFilePath(fileName);
		try {
			BufferedReader br = new BufferedReader(new FileReader(source));
			String record = new String();
			int a, b;
			while ((record = br.readLine()) != null) {
//				System.out.println(record.substring(0,6) + " " + record.substring(19,20).trim());
				if (record.startsWith("HELIX") && (record.charAt(19) == chainName)) {
					 {
						 a = Integer.parseInt(record.substring(22,25).trim());
						 b = Integer.parseInt(record.substring(34,37).trim());
//						 System.out.println("Helix starts at " + a + " and ends at " + b);
						 for (int i = a; i < b; i++) str[i] = 'H';
					 }
				}
				else
					if (record.startsWith("SHEET") && (record.charAt(21) == chainName)) {
						 a = Integer.parseInt(record.substring(23,27).trim());
						 b = Integer.parseInt(record.substring(34,37).trim());
//						 System.out.println("Strand starts at " + a + " and ends at " + b);
						 for (int i = a; i < b; i++) str[i] = 'S';
					}
			}
			
		} catch (IOException e) { System.out.println("IOException error!"); e.printStackTrace(); }
		return String.copyValueOf(str);
	}
	
}

