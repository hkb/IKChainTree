package geom3d;

import java.io.*;

import dataStructures.*;
import moleculeMaps.*;
import molecule.AminoAcid;
import molecule.PDB;
import misc.*;

public class Chain3d extends DLList {
	String chainName = "";
	String sequence = new String();
	DistanceMap dm;
	HSEDistanceMap hse;
	
	public Chain3d() {}
	
	/* **********************************************************************
	 * reads x-, y- and z-coordinates of points from a specified input file *
	 * (one point per line)                                                 *
	 ********************************************************************** */
	public Chain3d(String inputFileName) {
		double x, y, z;
		try {
			FileReader rd = new FileReader(inputFileName);
			System.out.flush();
			
			StreamTokenizer st = new StreamTokenizer(rd);
			st.parseNumbers();

			int token = st.nextToken();
			while (token != StreamTokenizer.TT_EOF) {
				switch (token) {
				case StreamTokenizer.TT_NUMBER:
					x = st.nval;
					token = st.nextToken();
					y = st.nval;
					token = st.nextToken();
					z = st.nval;
					push(new Point3d(x,y,z));
					System.out.println(x + " " + y + " " + z);
					break;
				}
				token = st.nextToken();
			}
			rd.close();
		} 
		catch (IOException e) {
		}
	}

	public String getChainName() { return chainName; }
	public String getSequence() { return sequence; }
	
	/* ********************************************************************************
	 * creates a chain corresponding to the C-trace of Ca atoms in the given PDB file *
	 ******************************************************************************** */
	public void PDB2CTrace(String fileName) {
		try {
			BufferedReader br = PDB.getFileName(fileName);
			String record = PDB.getFirstRecord(br, 'A', "ATOM");
			while (record != null) {
				if (PDB.getAtomChainName(record).equals("A")) {
					if (PDB.getAtomSymbol(record).equals("CA")) {
						push(PDB.getAtomCoordinates(record));
						sequence = sequence.concat(String.valueOf(AminoAcid.translateAA31(PDB.getAtomAminoAcidName(record))));
					}
				}
				record = br.readLine();	
				if (!record.startsWith("ATOM")) record = null;
			}
			System.out.println(sequence);
		} catch (IOException e) { System.out.println("IOException error!"); e.printStackTrace(); }
	}

	/* ******************************************************************************************
	 * creates a chain corresponding to the C-trace of Ca atoms between given residue sequence *
	 * numbers in the given PDB file.                                                          *
	 ***************************************************************************************** */
	public void PDB2CTrace(String fileName, int init, int term) {
		try {
			BufferedReader br = PDB.getFileName(fileName);
			String record = PDB.getFirstRecord(br, 'A', "ATOM");
			record = PDB.getFirstAtomResidueSequenceNumber(br, record, init);
			while (record != null) {
				if (PDB.getAtomChainName(record).equals("A")) {
					if (PDB.getAtomSymbol(record).equals("CA")) {
						push(PDB.getAtomCoordinates(record));
						sequence = sequence.concat(String.valueOf(AminoAcid.translateAA31(PDB.getAtomAminoAcidName(record))));
					}
				}
				record = br.readLine();	
				if ((PDB.getResidueSequenceNumber(record) > term) || !record.startsWith("ATOM")) record = null;
			}
			System.out.println(sequence);
		} catch (IOException e) { System.out.println("IOException error!"); e.printStackTrace(); }
	}

	
	/* *******************************************************************************************
	 * creates a chain corresponding to the NCaC-trace of N, Ca and C atoms in the given PDB file *
	 ******************************************************************************************* */
	public void PDB2NCaCTrace(String fileName) {
		try {
			BufferedReader br = PDB.getFileName(fileName);
			String record = PDB.getFirstRecord(br, 'A', "ATOM");
			while (record != null) {
				if (PDB.getAtomChainName(record).equals("A")) {
					if (PDB.getAtomSymbol(record).equals("N")) {
						push(PDB.getAtomCoordinates(record));
						record = br.readLine();
						push(PDB.getAtomCoordinates(record));
						sequence = sequence.concat(String.valueOf(AminoAcid.translateAA31(PDB.getAtomAminoAcidName(record))));
						record = br.readLine();
						push(PDB.getAtomCoordinates(record));
					}
				}
				record = br.readLine();	
				if (!record.startsWith("ATOM")) record = null;
			}
			System.out.println(sequence);
		} catch (IOException e) { System.out.println("IOException error!"); e.printStackTrace(); }
	}

	/* **********************************************************************************
	 * prints dihedral angles along the chain (not defined for the first two points and *
	 * the last point).                                                                 *
	 ********************************************************************************** */
	public void printDihedralAngles() {
		DLNode nd1 = getFirst();
		DLNode nd2 = (nd1 != null)? nd1.getNext() : null;
		DLNode nd3 = (nd2 != null)? nd2.getNext() : null;
		DLNode nd4 = (nd3 != null)? nd3.getNext() : null;
		Point3d p1 = (nd1 != null)? (Point3d)nd1.getObject() : null;
		Point3d p2 = (nd1 != null)? (Point3d)nd2.getObject() : null;
		Point3d p3 = (nd1 != null)? (Point3d)nd3.getObject() : null;
		Point3d p4 = (nd1 != null)? (Point3d)nd4.getObject() : null;
		System.out.print("1: 0.000000000000000  ");
		int i = 2;
		while (nd4 != null) {
			if ((i-1) % 3 == 0) {
				System.out.println();
				System.out.print(((i+2) / 3) + ": ");
			}
			System.out.print(Functions.toRadians(Point3d.getDihedralAngle(p1, p2, p3, p4)) + "  ");
			nd1 = nd2;
			nd2 = nd3;
			nd3 = nd4;
			nd4 = nd4.getNext();
			p1 = p2;
			p2 = p3;
			p3 = p4;
			p4 = (nd4 != null)? (Point3d)nd4.getObject() : null;
			i++;
		}
		System.out.println();
	}

	/* **********************************************************************************
	 * prints angles along the chain (not defined for the first and the last point).    *
	 ********************************************************************************** */
	public void printAngles() {
		DLNode nd1 = getFirst();
		DLNode nd2 = (nd1 != null)? nd1.getNext() : null;
		DLNode nd3 = (nd2 != null)? nd2.getNext() : null;
		Point3d p1 = (nd1 != null)? (Point3d)nd1.getObject() : null;
		Point3d p2 = (nd1 != null)? (Point3d)nd2.getObject() : null;
		Point3d p3 = (nd1 != null)? (Point3d)nd3.getObject() : null;
		System.out.print("1: 0.000000000000000  ");
		int i = 2;
		while (nd3 != null) {
			if ((i-1) % 3 == 0) {
				System.out.println();
				System.out.print(((i+2) / 3) + ": ");
			}
			System.out.print(Functions.toRadians(Point3d.getAngle(p1, p2, p3)) + "  ");
			nd1 = nd2;
			nd2 = nd3;
			nd3 = nd3.getNext();
			p1 = p2;
			p2 = p3;
			p3 = (nd3 != null)? (Point3d)nd3.getObject() : null;
			i++;
		}
		System.out.println();
	}


	/* **********************************************************************************
	 * prints lengths of the bonds along the chain.                                     *
	 ********************************************************************************** */
	public void printBondLengths() {
		DLNode nd1 = getFirst();
		DLNode nd2 = (nd1 != null)? nd1.getNext() : null;
		Point3d p1 = (nd1 != null)? (Point3d)nd1.getObject() : null;
		Point3d p2 = (nd1 != null)? (Point3d)nd2.getObject() : null;
		int i = 1;
		while (nd2 != null) {
			if ((i-1) % 3 == 0) {
				System.out.println();
				System.out.print(((i+2) / 3) + ": ");
			}
			System.out.print(Point3d.getDistance(p1, p2) + "  ");
			nd1 = nd2;
			nd2 = nd2.getNext();
			p1 = p2;
			p2 = (nd2 != null)? (Point3d)nd2.getObject() : null;
			i++;
		}
		System.out.println();
	}

	/* 
	 * creates a contact map of the point set
	 */
/*	public ContactMap getContactMap(double r) {
		ContactMap cm = new ContactMap(this); 
		double rr = r * r;
		DLNode u = getFirst();
		Point3d up;
		int i = 0;
		DLNode v;
		int j;
		while (u != null) {
			up = (Point3d)u.getObject();
			cm.set(i,i,true);
			v = u.getNext();
			j = i + 1;
			while (v != null) {
				cm.set(i,j,up.getSquaredDistance((Point3d)v.getObject()) <= rr);
				v = v.getNext();
				j++;
			}
			u = u.getNext();
			i++;
		}
		return cm;
	}
*/	
	public DistanceMap getDistanceMap() { return new DistanceMap(this); }

	public HSEDistanceMap getHSEDistanceMap() { return new HSEDistanceMap(this); }
	/* 
	 * creates a contact number vector of the point set
	 */
	public ContactNumber getContactNumber(double r) { 
		return new ContactNumber(this, r); 
	}

	
	/*
	 * returns the centroid point of the chain
	 */
	public Point3d getCentroid() {
		DLListIterator iter = new DLListIterator(this);
		Point3d q = new Point3d();
		while (iter.hasNext()) q.addIn((Point3d)iter.next());
		System.out.println(getSize());
		q.scale(1.0/getSize());
		return q;
	}

	/*
	 * translates the chain 
	 */
	public void translate(double x, double y, double z) {
		DLListIterator iter = new DLListIterator(this);
		while (iter.hasNext()) ((Point3d)iter.next()).translate(x ,y, z); 
	}
	
	/*
	 * returns rmsd of two equal length chains. 
	 */
	public Double rmsd(Chain3d c) {
		if (getSize() == c.getSize()) {
			DLListIterator iter = new DLListIterator(this);
			DLListIterator cIter = new DLListIterator(c);
			double dist = 0.0;
			while (iter.hasNext()) dist += ((Point3d)iter.next()).getSquaredDistance((Point3d)cIter.next());
			return  Math.sqrt(dist/getSize()); 
		}
		else return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		Chain3d chain = new Chain3d("/Users/pawel/Documents/pdb1914");
//		DistanceMap dm = chain.getDistanceMap();
//		dm.display();
		
//		ContactNumber cn = chain.getContactNumber(13);
//		cn.display();

		Chain3d chain = new Chain3d();
		
		chain.PDB2NCaCTrace("");
		chain.printBondLengths();
//
		
//		HSEDistanceMap hse = new HSEDistanceMap(chain);
//		hse.print();
	
//		ContactNumberSpectrumFrame cnsFrame = new ContactNumberSpectrumFrame(chain);
//		HSENumberSpectrum cnsFrame = new HSENumberSpectrum(chain);
	}

}
