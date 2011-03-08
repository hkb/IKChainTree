package molecule;

import geom3d.*;
import guiMolecule.ProteinViewer;
import dataStructures.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;

import com.sun.j3d.utils.applet.MainFrame;

/*
 * HISTORY:
 * 
 * PROTEIN originates from Greek word proteios meaning "primary" or "of first rank". 
 * This name was adapted by J�ns Berzelius in 1838 to emphasize the importance of these 
 * molecules.
 * 
 * Proteins are constituent molecules in muscle tissues, ligaments, tendons, bones, 
 * skin, hair, organs and glands.
 * 
 * Proteins are carriers (hemoglobin carries oxygen) and storages (myoglobin stores oxygen in muscle cells)
 * Proteins are triggers of physiological processes.
 * 
 * globular (compact), fibrous (extended in shape) and membrane proteins.
 * 
 * Myoglobin is highly compact consisting of 75% a-helices.
 * 
 * Average protein has several hundreds of residues. Muscle protein titin has about 27.000 residues.
 */
 
public class Protein {
	String name;
	private Set aminoAcids;           // amino acids of the protein
	int type;                         // indicates which atoms are included
	int size;                         // number of amino acids
	private Set<AlphaHelix> alphaHelices; // alpha-helices of the protein
	private Set<BetaSheet> betaSheets;    // beta-sheets of the protein
	private Set ssBonds;              	  // CYS bonds
	private char[] sequence;          	  // 1-letter sequence of the protein
	int firstAminoAcidPDBNumber;          // PDB number of the first amino acid
	Set emptyIntervals;
	Set insertions;
	private ProteinViewer viewer;
	
	/*
	 * reads a pdb-file and creates a protein. 
	 * 
	 * Parameter proteinName consists of the 4-letters protein name possibly followed by 1-letter chain identification.
	 * If chain identification is not specified, 'A' is assumed
	 * 
	 * Parameter type specifies which atoms are to be included. More specifically
	 * type = 0: gets everything from the PDB file (currently without hydrogen atoms
	 * 
	 * type = 1: "Ca" -   only Ca atoms are included
	 * type = 2: "NCaC" - only backbone atoms N, Ca and C are included
	 * type = 3: "NCaCO" - only backbone atoms N, Ca, C, O are included
	 * type = 4: "NCaCOS" - all backbone atoms and side chain
	 * 
	 * Options below are not supported yet
	 * 
	 * type = "NCaCOH" - all backbone atoms with hydrogen atoms are included
	 * type = "NCaCOHS"
	 * type = "NCaCoHSH"
	 * 
	 * 
	 * 
	 * If parameter position is true, coordinates of atoms are fetched from the pdb file
	 */
	public Protein(String proteinName, int typeX, boolean position) {
		try {
			name              = proteinName;
			type              = typeX;
			String fileName   = proteinName.substring(0,4);
			char chainName    = (proteinName.length() == 4)? 'A' : proteinName.charAt(4);
			String source     = PDB.getFilePath(fileName);
			String record     = null;
			String atomSymbol = null;
			emptyIntervals    = new Set();
			insertions        = new Set();
			aminoAcids        = new Set();
			alphaHelices      = new Set();
			betaSheets        = new Set();
			ssBonds           = new Set();
			
			BufferedReader br = new BufferedReader(new FileReader(source));
			record = new String();
			AminoAcid aa = null;
			AminoAcid lastAA = null;
			boolean CAOnly = false; // true if pdb file contains only CA atoms 
			record = br.readLine();
			while ((record != null) && !record.startsWith("ENDMDL")) {       
//				if (record.startsWith("ATOM") || record.startsWith("HETATM")) {      earlier version
				if (record.startsWith("ATOM")) {  
					if ((record.charAt(21) == chainName) && 
						((PDB.getAltLocChar(record) == ' ') || (PDB.getAltLocChar(record) == 'A')) &&
						(PDB.getInsertionCodeChar(record) == ' ')) {            
						atomSymbol = PDB.getAtomSymbol(record);
						if (atomSymbol.equals("N")) {
							if ((typeX == 0) && (type < 2)) type = 2;
							if (type > 1) {
								lastAA = aa;
								aa = new AminoAcid(PDB.getAtomAminoAcidName(record), type);
								aa.setName(PDB.getAtomAminoAcidName(record));
								aa.setPDBNumber(PDB.getResidueSequenceNumber(record));
								aa.setPrev(lastAA);
								if (lastAA != null) lastAA.setNext(aa);
								if ((lastAA != null) && (lastAA.getPDBNumber() != aa.getPDBNumber() -1)) {
									emptyIntervals.insert(lastAA.getPDBNumber()+1);
									emptyIntervals.insert(aa.getPDBNumber()-1);
								}
								if (getAminoAcids().isEmpty()) {
									firstAminoAcidPDBNumber = PDB.getAtomResidueNumber(record);
								}
								aminoAcids.insert(aa);
								if (position && aa.getCore().hasC()) aa.getCore().getN().setPosition(PDB.getAtomCoordinates(record));
							}
						}
						else {
							if (atomSymbol.equals("CA")) {
								if ((typeX == 0) && (type < 1)) type = 1;
								if (CAOnly || getAminoAcids().isEmpty()) {       // only CA atoms in the PDB file
									lastAA = aa;
									aa = new AminoAcid(PDB.getAtomAminoAcidName(record), type);
									aa.setName(PDB.getAtomAminoAcidName(record));
									aa.setPDBNumber(PDB.getResidueSequenceNumber(record));
									aa.setPrev(lastAA);
									if (lastAA != null) lastAA.setNext(aa);
									if ((lastAA != null) && (lastAA.getPDBNumber() != aa.getPDBNumber() - 1)) {
										emptyIntervals.insert(lastAA.getPDBNumber()+1);
										emptyIntervals.insert(aa.getPDBNumber()-1);
									}
									if (getAminoAcids().isEmpty()) {
										firstAminoAcidPDBNumber = PDB.getAtomResidueNumber(record);
										CAOnly = true;
									}
									aminoAcids.insert(aa);
								}
								if (position) {
									if (aa.hasCore()) aa.getCa().setPosition(PDB.getAtomCoordinates(record));
									else aa.setPosition(PDB.getAtomCoordinates(record));
								}
							}
							else {
							if (atomSymbol.equals("C") && (aa != null)) {
								if (position && aa.getCore().hasC()) aa.getCore().getC().setPosition(PDB.getAtomCoordinates(record));
							}
							else {
							if (atomSymbol.equals("O") && (aa != null)) {
								if ((typeX == 0) && (type < 3)) type = 3;
								if ((type > 2) && position) {
									aa.getCore().setO(new Oxygen());
									aa.getCore().getO().setPosition(PDB.getAtomCoordinates(record));
								}
							}
							else {
							if (atomSymbol.equals("CB") && (aa != null)) {
								if ((typeX == 0) && (type < 4)) type = 4;
								if (type > 3) 
									switch (aa.getSymbol()) {
									case 'A':   // ALA - Alaine
										SideChainAla sideChainAla = new SideChainAla(aa);
										aa.setSideChain(sideChainAla);
										if (position) sideChainAla.getCb().setPosition(PDB.getAtomCoordinates(record));
										break;
									case 'C':   // CYS - Cysteine
										SideChainCys sideChainCys = new SideChainCys(aa);
										aa.setSideChain(sideChainCys);
										if (position) sideChainCys.getCb().setPosition(PDB.getAtomCoordinates(record));
										break;
									case 'D':   // ASP - Aspartic acid
										SideChainAsp sideChainAsp = new SideChainAsp(aa);
										aa.setSideChain(sideChainAsp);
										if (position) sideChainAsp.getCb().setPosition(PDB.getAtomCoordinates(record));
										break;
									case 'E':   // GLU - Glutamic Acid
										SideChainGlu sideChainGlu = new SideChainGlu(aa);
										aa.setSideChain(sideChainGlu);
										if (position) sideChainGlu.getCb().setPosition(PDB.getAtomCoordinates(record));
										break;
									case 'F':   // PHE - Phenylalanine
										SideChainPhe sideChainPhe = new SideChainPhe(aa);
										aa.setSideChain(sideChainPhe);
										if (position) sideChainPhe.getCb().setPosition(PDB.getAtomCoordinates(record));
										break;
									case 'H':   // HIS - Histidine
										SideChainHis sideChainHis = new SideChainHis(aa);
										aa.setSideChain(sideChainHis);
										if (position) sideChainHis.getCb().setPosition(PDB.getAtomCoordinates(record));
										break;
									case 'I':   // ILE - Isoleucine
										SideChainIle sideChainIle = new SideChainIle(aa);
										aa.setSideChain(sideChainIle);
										if (position) sideChainIle.getCb().setPosition(PDB.getAtomCoordinates(record));
										break;
									case 'K':   // LYS - Lysine
										SideChainLys sideChainLys = new SideChainLys(aa);
										aa.setSideChain(sideChainLys);
										if (position) sideChainLys.getCb().setPosition(PDB.getAtomCoordinates(record));
										break;
									case 'L':   // LEU - Leucine
										SideChainLeu sideChainLeu = new SideChainLeu(aa);
										aa.setSideChain(sideChainLeu);
										if (position) sideChainLeu.getCb().setPosition(PDB.getAtomCoordinates(record));
										break;
									case 'M':   // MET - Methionine 
										SideChainMet sideChainMet = new SideChainMet(aa);
										aa.setSideChain(sideChainMet);
										if (position) sideChainMet.getCb().setPosition(PDB.getAtomCoordinates(record));
										break;
									case 'N':   // ASN - Asparagine
										SideChainAsn sideChainAsn = new SideChainAsn(aa);
										aa.setSideChain(sideChainAsn);
										if (position) sideChainAsn.getCb().setPosition(PDB.getAtomCoordinates(record));
										break;
									case 'P':   // PRO - Proline
										SideChainPro sideChainPro = new SideChainPro(aa);
										aa.setSideChain(sideChainPro);
										if (position) sideChainPro.getCb().setPosition(PDB.getAtomCoordinates(record));
										break;
									case 'Q':   // GLN - Glutamine
										SideChainGln sideChainGln = new SideChainGln(aa);
										aa.setSideChain(sideChainGln);
										if (position) sideChainGln.getCb().setPosition(PDB.getAtomCoordinates(record));
										break;
									case 'R':   // ARG - Arginine 
										SideChainArg sideChainArg = new SideChainArg(aa);
										aa.setSideChain(sideChainArg);
										if (position) sideChainArg.getCb().setPosition(PDB.getAtomCoordinates(record));
										break;
									case 'S':   // ARG - Arginine 
										SideChainSer sideChainSer = new SideChainSer(aa);
										aa.setSideChain(sideChainSer);
										if (position) sideChainSer.getCb().setPosition(PDB.getAtomCoordinates(record));
										break;				
									case 'T':   // THR - Threonine
										SideChainThr sideChainThr = new SideChainThr(aa);
										aa.setSideChain(sideChainThr);
										if (position) sideChainThr.getCb().setPosition(PDB.getAtomCoordinates(record));
										break;
									case 'Y':   // TYR - Tyrosine
										SideChainTyr sideChainTyr = new SideChainTyr(aa);
										aa.setSideChain(sideChainTyr);
										if (position) sideChainTyr.getCb().setPosition(PDB.getAtomCoordinates(record));
										break;
									case 'V':   // VAL - Valine
										SideChainVal sideChainVal = new SideChainVal(aa);
										aa.setSideChain(sideChainVal);
										if (position) sideChainVal.getCb().setPosition(PDB.getAtomCoordinates(record));
										break;
									case 'W':   // TRP - Tryptophan
										SideChainTrp sideChainTrp = new SideChainTrp(aa);
										aa.setSideChain(sideChainTrp);
										if (position) sideChainTrp.getCb().setPosition(PDB.getAtomCoordinates(record));
										break;
									}
								}
							else {
							if (atomSymbol.equals("CG") && (aa != null) && (aa.hasSideChain() && (type > 3) && position)) 
								switch (aa.getSymbol()) {
								case 'D': ((SideChainAsp)aa.getSideChain()).getCg().setPosition(PDB.getAtomCoordinates(record)); break;
								case 'E': ((SideChainGlu)aa.getSideChain()).getCg().setPosition(PDB.getAtomCoordinates(record)); break;
								case 'F': ((SideChainPhe)aa.getSideChain()).getCg().setPosition(PDB.getAtomCoordinates(record)); break;
								case 'H': ((SideChainHis)aa.getSideChain()).getCg().setPosition(PDB.getAtomCoordinates(record)); break;
								case 'K': ((SideChainLys)aa.getSideChain()).getCg().setPosition(PDB.getAtomCoordinates(record)); break;
								case 'L': ((SideChainLeu)aa.getSideChain()).getCg().setPosition(PDB.getAtomCoordinates(record)); break;
								case 'M': ((SideChainMet)aa.getSideChain()).getCg().setPosition(PDB.getAtomCoordinates(record)); break;
								case 'N': ((SideChainAsn)aa.getSideChain()).getCg().setPosition(PDB.getAtomCoordinates(record)); break;
								case 'P': ((SideChainPro)aa.getSideChain()).getCg().setPosition(PDB.getAtomCoordinates(record)); break;
								case 'Q': ((SideChainGln)aa.getSideChain()).getCg().setPosition(PDB.getAtomCoordinates(record)); break;
								case 'R': ((SideChainArg)aa.getSideChain()).getCg().setPosition(PDB.getAtomCoordinates(record)); break;
								case 'Y': ((SideChainTyr)aa.getSideChain()).getCg().setPosition(PDB.getAtomCoordinates(record)); break;
								case 'W': ((SideChainTrp)aa.getSideChain()).getCg().setPosition(PDB.getAtomCoordinates(record)); break;
								}
							else {
							if (atomSymbol.equals("CG1") && (aa != null) && (aa.hasSideChain() && (type > 3) && position)) 
								switch (aa.getSymbol()) {
								case 'I': ((SideChainIle)aa.getSideChain()).getCg1().setPosition(PDB.getAtomCoordinates(record)); break;
								case 'V': ((SideChainVal)aa.getSideChain()).getCg1().setPosition(PDB.getAtomCoordinates(record));	break;
								}
							else {
							if (atomSymbol.equals("CG2") && (aa != null) && (aa.hasSideChain() && (type > 3) && position)) 
								switch (aa.getSymbol()) {
								case 'I': ((SideChainIle)aa.getSideChain()).getCg2().setPosition(PDB.getAtomCoordinates(record));	break;
								case 'T': ((SideChainThr)aa.getSideChain()).getCg2().setPosition(PDB.getAtomCoordinates(record));	break;
								case 'V': ((SideChainVal)aa.getSideChain()).getCg2().setPosition(PDB.getAtomCoordinates(record));	break;
								}
							else {
							if (atomSymbol.equals("CD")  && (aa != null) && (aa.hasSideChain() && (type > 3) && position)) 
								switch (aa.getSymbol()) {
								case 'E': ((SideChainGlu)aa.getSideChain()).getCd().setPosition(PDB.getAtomCoordinates(record));	break;
								case 'K': ((SideChainLys)aa.getSideChain()).getCd().setPosition(PDB.getAtomCoordinates(record)); break;
								case 'P': ((SideChainPro)aa.getSideChain()).getCd().setPosition(PDB.getAtomCoordinates(record));	break;
								case 'Q': ((SideChainGln)aa.getSideChain()).getCd().setPosition(PDB.getAtomCoordinates(record));	break;
								case 'R': ((SideChainArg)aa.getSideChain()).getCd().setPosition(PDB.getAtomCoordinates(record));	break;
								}
							else {
							if (atomSymbol.equals("CD1") && (aa != null) && (aa.hasSideChain() && (type > 3) && position)) 
								switch (aa.getSymbol()) {
								case 'F': ((SideChainPhe)aa.getSideChain()).getCd1().setPosition(PDB.getAtomCoordinates(record));	break;
								case 'I': ((SideChainIle)aa.getSideChain()).getCd1().setPosition(PDB.getAtomCoordinates(record));	break;
								case 'L': ((SideChainLeu)aa.getSideChain()).getCd1().setPosition(PDB.getAtomCoordinates(record));	break;
								case 'Y': ((SideChainTyr)aa.getSideChain()).getCd1().setPosition(PDB.getAtomCoordinates(record));	break;
								case 'W': ((SideChainTrp)aa.getSideChain()).getCd1().setPosition(PDB.getAtomCoordinates(record));	break;
								}
							else {
							if (atomSymbol.equals("CD2") && (aa != null) && (aa.hasSideChain() && (type > 3) && position)) 
								switch (aa.getSymbol()) {
								case 'F': ((SideChainPhe)aa.getSideChain()).getCd2().setPosition(PDB.getAtomCoordinates(record));	break;
								case 'H': ((SideChainHis)aa.getSideChain()).getCd2().setPosition(PDB.getAtomCoordinates(record));	break;
								case 'L': ((SideChainLeu)aa.getSideChain()).getCd2().setPosition(PDB.getAtomCoordinates(record));	break;
								case 'Y': ((SideChainTyr)aa.getSideChain()).getCd2().setPosition(PDB.getAtomCoordinates(record));	break;
								case 'W': ((SideChainTrp)aa.getSideChain()).getCd2().setPosition(PDB.getAtomCoordinates(record));	break;
								}
							else {
							if (atomSymbol.equals("CE") && (aa != null) && (aa.hasSideChain() && (type > 3) && position)) 
								switch (aa.getSymbol()) {
								case 'K': ((SideChainLys)aa.getSideChain()).getCe().setPosition(PDB.getAtomCoordinates(record));	break;
								case 'M': ((SideChainMet)aa.getSideChain()).getCe().setPosition(PDB.getAtomCoordinates(record));	break;
								}
							else {
							if (atomSymbol.equals("CE1") && (aa != null) && (aa.hasSideChain() && (type > 3) && position)) 
								switch (aa.getSymbol()) {
								case 'F': ((SideChainPhe)aa.getSideChain()).getCe1().setPosition(PDB.getAtomCoordinates(record));	break;
								case 'H': ((SideChainHis)aa.getSideChain()).getCe1().setPosition(PDB.getAtomCoordinates(record));	break;
								case 'Y': ((SideChainTyr)aa.getSideChain()).getCe1().setPosition(PDB.getAtomCoordinates(record));	break;
								}
							else {
							if (atomSymbol.equals("CE2") && ( aa != null) && (aa.hasSideChain() && (type > 3) && position))
								switch (aa.getSymbol()) {
								case 'F': ((SideChainPhe)aa.getSideChain()).getCe2().setPosition(PDB.getAtomCoordinates(record));	break;
								case 'Y': ((SideChainTyr)aa.getSideChain()).getCe2().setPosition(PDB.getAtomCoordinates(record));	break;
								case 'W': ((SideChainTrp)aa.getSideChain()).getCe2().setPosition(PDB.getAtomCoordinates(record));	break;			
								}
							else {
							if (atomSymbol.equals("CE3") && ( aa != null) && (aa.hasSideChain() && (type > 3) && position))
								switch (aa.getSymbol()) {
								case 'W': ((SideChainTrp)aa.getSideChain()).getCe3().setPosition(PDB.getAtomCoordinates(record));	break;			
								}
							else {
							if (atomSymbol.equals("CZ") && (aa != null) && (aa.hasSideChain() && (type > 3) && position)) 
								switch (aa.getSymbol()) {
								case 'F': ((SideChainPhe)aa.getSideChain()).getCz().setPosition(PDB.getAtomCoordinates(record)); break;
								case 'R': ((SideChainArg)aa.getSideChain()).getCz().setPosition(PDB.getAtomCoordinates(record));	break;
								case 'Y': ((SideChainTyr)aa.getSideChain()).getCz().setPosition(PDB.getAtomCoordinates(record));	break;
								}								
							else {
							if (atomSymbol.equals("CZ2") && ( aa != null) && (aa.hasSideChain() && (type > 3) && position))
								switch (aa.getSymbol()) {
								case 'W': ((SideChainTrp)aa.getSideChain()).getCz2().setPosition(PDB.getAtomCoordinates(record)); break;			
								}
							else {
							if (atomSymbol.equals("CZ3") && ( aa != null) && (aa.hasSideChain() && (type > 3) && position))
								switch (aa.getSymbol()) {
								case 'W': ((SideChainTrp)aa.getSideChain()).getCz3().setPosition(PDB.getAtomCoordinates(record));	break;			
								}
							else {
							if (atomSymbol.equals("CH2") && ( aa != null) && (aa.hasSideChain() && (type > 3) && position))
								switch (aa.getSymbol()) {
								case 'W': ((SideChainTrp)aa.getSideChain()).getCh2().setPosition(PDB.getAtomCoordinates(record));	break;			
								}
							else {
							if (atomSymbol.equals("ND1") && (aa != null) && (aa.hasSideChain() && (type > 3) && position)) 
								switch (aa.getSymbol()) {
								case 'H': ((SideChainHis)aa.getSideChain()).getNd1().setPosition(PDB.getAtomCoordinates(record));	break;
								}				
							else {
							if (atomSymbol.equals("ND2") && (aa != null) && (aa.hasSideChain() && (type > 3) && position)) 
								switch (aa.getSymbol()) {
								case 'N': ((SideChainAsn)aa.getSideChain()).getNd2().setPosition(PDB.getAtomCoordinates(record)); break;
								}				
							else {	
							if (atomSymbol.equals("NE") && (aa != null) && (aa.hasSideChain() && (type > 3) && position)) 
								switch (aa.getSymbol()) {
								case 'R': ((SideChainArg)aa.getSideChain()).getNe().setPosition(PDB.getAtomCoordinates(record));	break;
								}
							else {
							if (atomSymbol.equals("NE1") && ( aa != null) && (aa.hasSideChain() && (type > 3) && position))
								switch (aa.getSymbol()) {
								case 'W': ((SideChainTrp)aa.getSideChain()).getNe1().setPosition(PDB.getAtomCoordinates(record));	break;			
								}
							else {
							if (atomSymbol.equals("NE2") && (aa != null) && (aa.hasSideChain() && (type > 3) && position)) 
								switch (aa.getSymbol()) {
								case 'H': ((SideChainHis)aa.getSideChain()).getNe2().setPosition(PDB.getAtomCoordinates(record)); break;
								case 'Q': ((SideChainGln)aa.getSideChain()).getNe2().setPosition(PDB.getAtomCoordinates(record)); break;
								}				
							else {	
							if (atomSymbol.equals("NH1") && (aa != null) && (aa.hasSideChain() && (type > 3) && position)) 
								switch (aa.getSymbol()) {
								case 'R': ((SideChainArg)aa.getSideChain()).getNh1().setPosition(PDB.getAtomCoordinates(record));	break;
								}				
							else {
							if (atomSymbol.equals("NH2") && (aa != null) && (aa.hasSideChain() && (type > 3) && position)) 
								switch (aa.getSymbol()) {
								case 'R': ((SideChainArg)aa.getSideChain()).getNh2().setPosition(PDB.getAtomCoordinates(record)); break;
								}								
							else {
							if (atomSymbol.equals("NZ") && (aa != null) && (aa.hasSideChain() && (type > 3) && position)) 
								switch (aa.getSymbol()) {
								case 'K': ((SideChainLys)aa.getSideChain()).getNz().setPosition(PDB.getAtomCoordinates(record));	break;
								}
							else {
							if (atomSymbol.equals("OG") && (aa != null) && (aa.hasSideChain() && (type > 3) && position)) 
								switch (aa.getSymbol()) {
								case 'S': ((SideChainSer)aa.getSideChain()).getOg().setPosition(PDB.getAtomCoordinates(record)); break;
								}
							else {
							if (atomSymbol.equals("OG1") && (aa != null) && (aa.hasSideChain() && (type > 3) && position)) 
								switch (aa.getSymbol()) {
								case 'T': ((SideChainThr)aa.getSideChain()).getOg1().setPosition(PDB.getAtomCoordinates(record));	break;
								}
							else {
							if (atomSymbol.equals("OD1") && (aa != null) && (aa.hasSideChain() && (type > 3) && position)) 
								switch (aa.getSymbol()) {
								case 'D': ((SideChainAsp)aa.getSideChain()).getOd1().setPosition(PDB.getAtomCoordinates(record));	break;
								case 'N': ((SideChainAsn)aa.getSideChain()).getOd1().setPosition(PDB.getAtomCoordinates(record));	break;
								}
							else {
							if (atomSymbol.equals("OD2") && (aa != null) && (aa.hasSideChain() && (type > 3) && position)) 
								switch (aa.getSymbol()) {
								case 'D': ((SideChainAsp)aa.getSideChain()).getOd2().setPosition(PDB.getAtomCoordinates(record));	break;
								}
							else {
							if (atomSymbol.equals("OE1") && (aa != null) && (aa.hasSideChain() && (type > 3) && position)) 
								switch (aa.getSymbol()) {
								case 'E': ((SideChainGlu)aa.getSideChain()).getOe1().setPosition(PDB.getAtomCoordinates(record));	break;
								case 'Q': ((SideChainGln)aa.getSideChain()).getOe1().setPosition(PDB.getAtomCoordinates(record));
									break;
								}
							else {
							if (atomSymbol.equals("OE2") && (aa != null) && (aa.hasSideChain() && (type > 3) && position)) 
								switch (aa.getSymbol()) {
								case 'E': ((SideChainGlu)aa.getSideChain()).getOe2().setPosition(PDB.getAtomCoordinates(record));	break;
								}
							else {
							if (atomSymbol.equals("OH") && (aa != null) && (aa.hasSideChain() && (type > 3) && position)) 
								switch (aa.getSymbol()) {
								case 'Y': ((SideChainTyr)aa.getSideChain()).getOh().setPosition(PDB.getAtomCoordinates(record));	break;
								}
							else {
							if (atomSymbol.equals("SG") && (aa != null) && (aa.hasSideChain() && (type > 3) && position)) 
								switch (aa.getSymbol()) {
								case 'C': ((SideChainCys)aa.getSideChain()).getSg().setPosition(PDB.getAtomCoordinates(record));	break;
								}
							else {
							if (atomSymbol.equals("SD") && (aa != null) && (aa.hasSideChain() && (type > 3) && position)) 
								switch (aa.getSymbol()) {
								case 'M': ((SideChainMet)aa.getSideChain()).getSd().setPosition(PDB.getAtomCoordinates(record));	break;
								}
							}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}
				}
				if (record.startsWith("HELIX")) {
					if (PDB.getHelixInitChainID(record) == chainName) {						
						int initSeqNum = PDB.getHelixInitSeqNum(record);
						int endSeqNum = PDB.getHelixEndSeqNum(record);
						AlphaHelix alphaHelix = new AlphaHelix(this, initSeqNum, endSeqNum);
						alphaHelix.setType(PDB.getHelixClass(record));
						alphaHelices.insert(alphaHelix);
					}
				}
				if (record.startsWith("SHEET")) {
					if ((PDB.getBetaStrandInitChainID(record) == chainName) && (PDB.getBetaStrandInitChainID(record) == chainName)) {			
//						System.out.println("testing " + PDB.getBetaStrandNumber(record) + " " + record);
						if (PDB.getBetaStrandNumber(record) == 1) {       // first beta strand in the sheet
							BetaSheet betaSheet = new BetaSheet(this);
							betaSheet.setSheetID(PDB.getBetaSheetId(record));
							int initSeqNum = PDB.getBetaStrandInitSeqNum(record);
							int endSeqNum = PDB.getBetaStrandEndSeqNum(record);
							int sense = PDB.getBetaStrandSense(record);
							betaSheet.getBetaStrands().insert(new BetaStrand(this, initSeqNum, endSeqNum, sense));
							betaSheets.insert(betaSheet);
						}
						else {
							if (!betaSheets.isEmpty()) {
								BetaSheet betaSheet = (BetaSheet)betaSheets.getLast();
								if (betaSheet.getSheetId().equals(PDB.getBetaSheetId(record))) {
									int initSeqNum = PDB.getBetaStrandInitSeqNum(record);
									int endSeqNum = PDB.getBetaStrandEndSeqNum(record);
									int sense = PDB.getBetaStrandSense(record);
									BetaStrand betaStrand = new BetaStrand(this, initSeqNum, endSeqNum, sense);
									betaSheet.getBetaStrands().insert(betaStrand);	
//									System.out.println(betaStrand.getDistances(betaStrandLast));
								}
							}
						}
					}
				}
				if (record.startsWith("SSBOND") && (PDB.getSS1BondChainId(record) == chainName) && (PDB.getSS2BondChainId(record) == chainName)) { 
					ssBonds.insert(PDB.getSS1SeqNum(record));
					ssBonds.insert(PDB.getSS2SeqNum(record));
				}
				record = br.readLine();
			}
			
		} catch (IOException e) { System.out.print(": IOException error!"); 
//				e.printStackTrace(); 
		}
		size = aminoAcids.getSize();
		setSecClass();
	}

	public void clear() {
		aminoAcids.clear();
		alphaHelices.clear();
		betaSheets.clear();
	}
	
	public String getName() { return name; }
	public void setAminoAcids(Set aminoAcids) { this.aminoAcids = aminoAcids; }

	public Set getAminoAcids() { return aminoAcids; }
	
	/*
	 * returns the i-th amino acid in the protein
	 */
	public AminoAcid getAminoAcid(int i)  { return (AminoAcid)aminoAcids.get(i); }
	
	/*
	 * returns the position of the i-th Ca atom
	 */
	public Point3d getCaPosition(int i) { return getAminoAcid(i).getPosition(); }
	
	/*
	 * returns the set of points corresponding to Ca positions.
	 */
	public PointSet3d getCaPositions() {
		int size = getSize();
		PointSet3d caPositions = new PointSet3d();
		for (int i = 0; i < size; i++) caPositions.insert(getCaPosition(i));
		return caPositions;
	}
	
	/*
	 * returns the last amino acid in the protein
	 */
	public AminoAcid getLastAminoAcid() { return (AminoAcid)aminoAcids.getLast(); }
	
	/*
	 * returns the index of amino acid with specified PDB amino acid number
	 */
	public int getIndex(int pdbNumber) {
//		System.out.println("testing...." + pdbNumber + ", " + getFirstAminoAcidPDBNumber());
		int position = pdbNumber - getFirstAminoAcidPDBNumber();
		int i = 0;
		int size = emptyIntervals.getSize();
		while ((i < size) && (pdbNumber > (Integer)emptyIntervals.get(i))) {
			position = position - (Integer)emptyIntervals.get(i+1) + (Integer)emptyIntervals.get(i) - 1 - 1; 
			i = i + 2;
		}
		return position;
	}
	
	public Set getEmptyIntervals() { return emptyIntervals; }
	
	public int getEmptyInterval(int i) { return (Integer)emptyIntervals.get(i); }
	
	public int getNumberEmptyIntervals() { return emptyIntervals.getSize()/2; }
	
	public Set<AlphaHelix> getAlphaHelices() { return alphaHelices; }
	
	public AlphaHelix getAlphaHelix(int i) { return alphaHelices.get(i); }
	
	public Set<BetaSheet> getBetaSheets() { return betaSheets; }
	
	public BetaSheet getBetaSheet(int i) { return betaSheets.get(i); }
 
	public int getFirstAminoAcidPDBNumber() { return firstAminoAcidPDBNumber; }
	
	public Set getSSBonds() { return ssBonds; }
	
	public int getSSBond(int i) { return (Integer)ssBonds.get(i); }
	
	/*
	 * returns number of amino acids
	 */
	public int getSize() { return size; }
	
	/* 
	 * returns number of atoms
	 */
	public int getNumberAtoms() {
		int count = 0;
		Core core = null;
		int size = getAminoAcids().getSize();
		for (int i = 0; i < size; i++) {
			AminoAcid aminoAcid = getAminoAcid(i);
			if (!aminoAcid.hasCore()) count++;
			else {
				core = aminoAcid.getCore();
				count = count + 3;
				if (core.hasO()) count++;
				if (core.hasH()) count = count + 2;
			}
		}
		return count;
	}
	
	public int getNumberAminoAcids() { return getAminoAcids().getSize(); }
	
	public int getNumberAlphaHelices() { if (alphaHelices == null) return 0; else return alphaHelices.getSize(); }
	
	public int getNumberBetaSheets() { if (betaSheets == null) return 0; else return betaSheets.getSize(); }
	
	public int getNumberParallelStrands() {
		int count = 0;
		int size = betaSheets.getSize();
		for (int i = 0; i < size; i++) count = count + getBetaSheet(i).getNumberParallelStrands();
		return count;
	}

	public int getNumberAntiParallelStrands() {
		int count = 0;
		int size = betaSheets.getSize();
		for (int i = 0; i < size; i++) count = count + getBetaSheet(i).getNumberAntiParallelStrands();
		return count;
	}
	
	public int getNumberAntiParallelStrands(int gapSize) {
		int count = 0;
		int size = betaSheets.getSize();
		for (int i = 0; i < size; i++) count = count + getBetaSheet(i).getNumberAntiParallelStrands(gapSize);
		return count;
	}

	
	public int getNumberSSBonds() { return ssBonds.getSize()/2; }
	
	public void getEmptyIntervalsFrequencies(Hashtable<Integer,Integer> emptyIntervalsFrequencies) {
		int diff;
		int size = emptyIntervals.getSize();
		for (int i = 0; i < size; i = i+2) {
			diff = (Integer)emptyIntervals.get(i+1) - (Integer)emptyIntervals.get(i) + 1;
			if (diff > 29) diff = 30;
			System.out.println(diff);
			emptyIntervalsFrequencies.put(diff, (Integer)emptyIntervalsFrequencies.get(diff) + 1);
		}
	}
	
	public void getAminoAcidFrequencies(Hashtable aminoAcidFrequencies) {
		for (int i = 0; i < sequence.length; i++) 
			aminoAcidFrequencies.put(sequence[i], (Integer)aminoAcidFrequencies.get(sequence[i]) + 1);
	}

	public void getAminoAcidFrequenciesInAlphaHelices(Hashtable aminoAcidFrequencies) {
		char[] charString;
		AlphaHelix alphaHelix;
		int size = alphaHelices.getSize();
		for (int i = 0; i < size; i++) {
			alphaHelix = getAlphaHelix(i);
			charString  = alphaHelix.getSymbols().toCharArray();
			for (int j = 0; j < charString.length; j++) 
				aminoAcidFrequencies.put(charString[j], (Integer)aminoAcidFrequencies.get(charString[j]) + 1);
		}
	}


	public void getAminoAcidFrequenciesInBetaStrands(Hashtable aminoAcidFrequencies) {
		int size = betaSheets.getSize();
		for (int i = 0; i < size; i++) getBetaSheet(i).getAminoAcidFrequenciesInBetaStrands(aminoAcidFrequencies);
	}
	
	
	public int getType() { return type; }

	/*
	 * returns amino acid sequence as an array of 1-letter characters
	 */
	public char[] getSequence() { 
		if (sequence == null) {
			int size = getSize();
			sequence = new char[size];
			for (int i = 0; i < size; i++) sequence[i] = AminoAcid.translateAA31(getAminoAcid(i).getName());
		}
		return sequence; 
	}
	
	public void setSequence(char[] sequence) { this.sequence = sequence; }

	/*
	 * sets secondary classification of amino acid in a-helices and b-sheets
	 */
	public void setSecClass() {
		for (int i = 0; i < getNumberAlphaHelices(); i++) (alphaHelices.get(i)).setSecClass();
		for (int i = 0; i < getNumberBetaSheets(); i++) (betaSheets.get(i)).setSecClass();
	}
	
	public SideChain getSideChain(int i) { return this.getAminoAcid(i).getSideChain(); }
	
	/*
	 * appends given amino acid to the end of the protein
	 */
	public void append(AminoAcid aminoAcid) { getAminoAcids().insert(aminoAcid); }
	
	public void appendChain(Chain3d chain) {
		DLNode nd = chain.getFirst();
		while (nd != null) {
			append(new AminoAcid("", 1, (Point3d)nd.getObject()));
			nd = nd.getNext();
		}
	}
	
	public void setHSEVectors() {
		Point3d p;
		AminoAcid prevAminoAcid = getAminoAcid(0);
		AminoAcid aminoAcid = getAminoAcid(1);
		AminoAcid nextAminoAcid;
		int size = getSize();
		for (int i = 2; i < size; i++) {
			nextAminoAcid = getAminoAcid(i);
			p = aminoAcid.getPosition();
			aminoAcid.setHSEVector(Vector3d.createSum(new Vector3d(p, prevAminoAcid.getPosition()), new Vector3d(p, nextAminoAcid.getPosition())));
			prevAminoAcid = aminoAcid;
			aminoAcid = nextAminoAcid;
		}
	}
	
	
	public void setLinearCa(double d) {
		int size = getSize();
		AminoAcid aminoAcid;
		for (int i = 0; i < size; i++) {
			aminoAcid = getAminoAcid(i);
			if (!aminoAcid.hasCore()) aminoAcid.setPosition(i*d, 0.0, 0.0);
		}
	}
	
	public void print() {
		Core core = null;
		int size = getSize();
		AminoAcid aminoAcid;
		for (int i = 0; i < size; i++) {
			aminoAcid = getAminoAcid(i);
			if (!aminoAcid.hasCore()) System.out.println(aminoAcid.getName() + " - Ca: " + aminoAcid.getPosition().toString(3));
			else {
				core = aminoAcid.getCore();
				if (type != 1) System.out.println(aminoAcid.getName() + " - N : " + core.getN().getPosition());
				System.out.println("    - Ca: " + core.getCa().getPosition());
				if (type != 1) System.out.println("    - C : " + core.getC().getPosition());
				if (core.hasO()) System.out.println("    - O : " + core.getO().getPosition());
				if (core.hasH()) {
					System.out.println("    - HN: " + core.getHN().getPosition());
					System.out.println("    - HCa:" + core.getHCa().getPosition());
				}
			}
		}
	}
	
	/*
	 * returns the coordinates of atoms, one atom per line
	 */
	public PointSet3d getPointSet() {
		int size = getSize();
		PointSet3d points = new PointSet3d();
		Core core = null;
		AminoAcid aminoAcid;
		for (int i = 0; i < size; i++) {
			aminoAcid = getAminoAcid(i);
			if ((type == 1) || !aminoAcid.hasCore()) points.insert(aminoAcid.getPosition());
			else {
				core = aminoAcid.getCore();
				points.insert(core.getN().getPosition());
				points.insert(core.getCa().getPosition());
				points.insert(core.getC().getPosition());
				if (core.hasO()) points.insert(core.getO().getPosition());
				if (core.hasH()) {
					points.insert(core.getHN().getPosition());
					points.insert(core.getHCa().getPosition());
				}
			}
		}
		return points;
	}

	public PointSet3d getPointSetBackbone() {
		int size = getSize();
		PointSet3d points = new PointSet3d();
		Core core = null;
		AminoAcid aminoAcid;
		for (int i = 0; i < size; i++) {
			aminoAcid = getAminoAcid(i);
			if ((type == 1) || !aminoAcid.hasCore()) points.insert(aminoAcid.getPosition());
			else {
				core = aminoAcid.getCore();
				points.insert(core.getN().getPosition());
				points.insert(core.getCa().getPosition());
				points.insert(core.getC().getPosition());
			}
		}
		return points;
	}
	
	
	/*
	 * returns the coordinates of atoms, one atom per line. 
	 * Disregards Ca atoms
	 */
	public PointSet3d getPointSetBackboneMinusCa() {
		int size = getSize();
		PointSet3d points = new PointSet3d();
		Core core = null;
		AminoAcid aminoAcid;
		for (int i = 0; i < size; i++) {
			aminoAcid = getAminoAcid(i);
			if ((type == 1) || !aminoAcid.hasCore()) points.insert(aminoAcid.getPosition());
			else {
				core = aminoAcid.getCore();
				points.insert(core.getN().getPosition());
//				points.insert(core.getCa().getPosition());
				points.insert(core.getC().getPosition());
				if (core.hasO()) points.insert(core.getO().getPosition());
				if (core.hasH()) {
					points.insert(core.getHN().getPosition());
					points.insert(core.getHCa().getPosition());
				}
			}
		}
		return points;
	}

	public PointSet3d getPointSetBackboneShell() {
		int size = getNumberAminoAcids();
		PointSet3d points = new PointSet3d();
		AminoAcid aminoAcid = getAminoAcid(0);
		AminoAcid aminoAcidNext;
		Point3d N, Ca, C = null;
		N = aminoAcid.getN().getPosition();
		points.insert(N);
		Vector3d NCa = null, CCa, CaC, NC, CN, CaN, CaCb, CO, NH;
		Ca = aminoAcid.getCa().getPosition(); 
		CaN = new Vector3d(Ca, N);
		for (int i = 1; i < size-1; i++) {
			aminoAcidNext = getAminoAcid(i);
			NCa = CaN.reverse();
			C = aminoAcid.getC().getPosition(); 
			CCa = new Vector3d(C, Ca);
			if (aminoAcid.getSymbol() == 'G') CaCb = NCa.add(CCa).scaleToLength(1.01);
			else CaCb = NCa.add(CCa).scaleToLength(1.54);
			points.insert(Ca);
			points.insert(Ca.add(CaCb));
			
			CaC = CCa.reverse();
			N = aminoAcidNext.getN().getPosition(); 		
			NC  = new Vector3d(N,C);
			CO = CaC.add(NC).scaleToLength(1.23);
			points.insert(C);
			points.insert(C.add(CO));
			
			CN  = NC.reverse();
			Ca = aminoAcidNext.getCa().getPosition();
			CaN = new Vector3d(Ca, N);
			NH = CN.add(CaN).scaleToLength(1.01);
			points.insert(N);
			points.insert(N.add(NH));
			
			aminoAcid = aminoAcidNext;
			NCa = CaN.reverse();
		}
		C = aminoAcid.getC().getPosition();
		CCa = new Vector3d(C, Ca);
		CaCb = NCa.add(CCa).scaleToLength(1.54);
		points.insert(Ca);
		points.insert(Ca.add(CaCb));
		
		points.insert(C);
		return points;
	}
	
	public byte[] getClassificationArray() {
		byte[] classification = new byte[getSize()];
		byte typ = 0;
		AlphaHelix alphaHelix;
		for (int i = 0; i < getNumberAlphaHelices(); i++) {
			alphaHelix = getAlphaHelix(i);
			if (alphaHelix.getType() == 1) typ = 1;
			else {
				if (alphaHelix.getType() == 5) typ = 2; else typ = 3;
			}
			for (int j = getIndex(alphaHelix.getInitSeqNum()); j <= getIndex(alphaHelix.getEndSeqNum()); j++) classification[j] = typ;
		}
		BetaSheet betaSheet;
		BetaStrand betaStrand;
		for (int i = 0; i < getNumberBetaSheets(); i++) {
			betaSheet = getBetaSheet(i);
			for (int j = 0; j < betaSheet.getNumberBetaStrands(); j++) {
				betaStrand = betaSheet.getBetaStrand(j);
				if (betaStrand.getSense() == 0) typ = 4;
				else {
					if (betaStrand.getSense() == 1) typ = 5;
					else {
						if (betaStrand.getSense() == -1) typ = 6; else typ = 7;
					}
				}
				for (int k = getIndex(betaStrand.getInitSeqNum()); k <= getIndex(betaStrand.getEndSeqNum()); k++) classification[k] = typ;
			}
		}
		for (int i = 0; i < getNumberSSBonds(); i++) {
			classification[getIndex(getSSBond(2*i))] = 8;
			classification[getIndex(getSSBond(2*i+1))] = 8;
		}						
		return classification;
	}
	
	public void printClassificationArray() {
		byte[] classification = getClassificationArray();
		for (int i = 0; i < classification.length; i++) System.out.print(classification[i]);
		System.out.println("]");

	}
	
	/*
	 * checks distances between Ca atoms
	 */
	public boolean hasBreak(int maxDist) {
		boolean breakInChain = false;
		int size = getSize();
		AminoAcid aminoAcid = getAminoAcid(0);
		AminoAcid nextAminoAcid;
		for (int i = 1; i < size; i++) {
			nextAminoAcid = getAminoAcid(i);
//			System.out.println(aminoAcid.getCaCaDistance(nextAminoAcid));
			if (aminoAcid.getCaCaDistance(nextAminoAcid) > maxDist) {
				System.out.print("Break after amino acid " + aminoAcid.getPDBNumber());
				if (nextAminoAcid == getLastAminoAcid()) System.out.print("; at the end of protein.");
				else {
					if (aminoAcid.getPDBNumber() + 1 != nextAminoAcid.getPDBNumber()) System.out.print("; at empty interval.");
					else breakInChain = true;
				}
				System.out.println();
			}
			aminoAcid = nextAminoAcid;
		}
		return breakInChain;
	}
	
	public boolean hasInsertions() { return !insertions.isEmpty(); }
	
	
	/*
	 * prints protein sequence (1-letter symbols)
	 */
	public void printSymbols() { System.out.println(String.copyValueOf(getSequence())); }
	
	/*
	 * prints the coordinates of atoms, one atom per line
	 */
	public void printCoordinates() {
		int size = getSize();
		Core core = null;
		AminoAcid aminoAcid;
		for (int i = 0; i < size; i++) {
			aminoAcid = getAminoAcid(i);
			if (!aminoAcid.hasCore()) System.out.println(aminoAcid.getPosition().toString());
			else {
				core = aminoAcid.getCore();
				System.out.println(core.getN().getPosition().toString());
				System.out.println(core.getCa().getPosition().toString());
				System.out.println(core.getC().getPosition().toString());
				if (core.hasO()) System.out.println(core.getO().getPosition().toString());
				if (core.hasH()) {
					System.out.println(core.getHN().getPosition().toString());
					System.out.println(core.getHCa().getPosition().toString());
				}
			}
		}
	}

	public void printAlphaHelicesIntervals() {
		String str = "";
		int size = alphaHelices.getSize();
		for (int i = 0; i < size; i++) str = str + getAlphaHelix(i).toString();
		System.out.println("Alpha helices, intervals: " + str);
	}

	public void printAlphaHelicesSymbols() {
		String str = "";
		int size = alphaHelices.getSize();
		for (int i = 0; i < size; i++) str = str + "[" + getAlphaHelix(i).getSymbols() + "]";
		System.out.println("Alpha helices, symbols: " + str);
	}

	/*
	 * prints gap sizes (number of amino acids) between consecutive alpha helices
	 */
	public void printAlphaHelicalGapLengths() {
		AlphaHelix alphaHelix;
		if (!alphaHelices.isEmpty()) {
			AlphaHelix alphaHelixPrev = getAlphaHelix(0);
			int size = alphaHelices.getSize();
			System.out.print("Lenghts of alpha helical gaps: ");
			for (int i = 1; i < size; i++) {
				alphaHelix = getAlphaHelix(i);
				System.out.print(alphaHelix.getInitSeqNum() - alphaHelixPrev.getEndSeqNum() - 1 + " ");
				alphaHelixPrev = alphaHelix;
			}
			System.out.println();
		}
	}
	
	public void printBetaSheetsIntervals() {
		int size = betaSheets.getSize();
		for (int i = 0; i < size; i++) {
			BetaSheet betaSheet = getBetaSheet(i);
			System.out.print("b-sheet, intervals: ");
			betaSheet.printBetaStrandIntervals();
		}
	}


	/*
	 * prints beta sheet sequence. Each strand is surounded by [] and preceded by its orientation w.r.t. predecessor strand
	 */
	public void printBetaSheetsSymbols() {
		BetaSheet betaSheet;
		int size = betaSheets.getSize();
		for (int i = 0; i < size; i++) {
			betaSheet = getBetaSheet(i);
			System.out.print("b-sheet, symbols: ");
			betaSheet.printBetaStrandSymbols();
		}
		System.out.println();
	}
	
	public void printBetaSheetsGapLengths() {
		BetaSheet betaSheet;
		int size = betaSheets.getSize();
		for (int i = 0; i < size; i++) {
			betaSheet = getBetaSheet(i);
			betaSheet.printBetaStrandGapLengths();
		}
		System.out.println();
	}
	
	/*
	 * prints phi angles. Core is required
	 */
	public void printPhiAngles() {
		int size = getSize();
		AminoAcid aminoAcid = getAminoAcid(0);
		AminoAcid nextAminoAcid;
		for (int i = 1; i < size; i++) {
			nextAminoAcid = getAminoAcid(i);
			System.out.println(aminoAcid.getName() + ": " + nextAminoAcid.getCore().getPhiAngle());
			aminoAcid = nextAminoAcid;

		}
	}

	public void printPsiAngles() {
		int size = getSize()-1;
		AminoAcid aminoAcid = getAminoAcid(0);
		AminoAcid nextAminoAcid;
		for (int i = 1; i < size; i++) {
			nextAminoAcid = getAminoAcid(i);
			System.out.println(aminoAcid.getName() + ": " + nextAminoAcid.getCore().getPsiAngle());
			aminoAcid = nextAminoAcid;
		}
	}

	public void printOmegaAngles() {
		int size = getSize()-1;                    // number of peptide bonds
		AminoAcid aminoAcid = getAminoAcid(0);
		AminoAcid nextAminoAcid;
		for (int i = 0; i < size; i++) {
			nextAminoAcid = getAminoAcid(i);
			System.out.println((i+1) + "-" + (i+2) + ": "  + aminoAcid.getName() + "-" + nextAminoAcid.getName() + ": " + nextAminoAcid.getCore().getOmegaAngle());
			aminoAcid = nextAminoAcid;

		}
	}
/*
	public void printOmegaAngles() {
		int size = getSize()-1;                    // number of peptide bonds
		AminoAcid aminoAcid = getAminoAcid(0);
		for (int i = 0; i < size; i++) {
			aminoAcid = getAminoAcid(i);
			System.out.println(i + ". " + aminoAcid.getName() + ": " + aminoAcid.getCore().getOmegaAngle());
		}
	}
*/

	/*
	 * return vector of dihedral angles of the bonds 
	 */
	public double[] getDihedralAngles() {
		int size = getSize();
		double[] dihedralAngles = new double[3*size];
		Core core = null;
		AminoAcid aminoAcid;
		for (int i = 0; i < size; i++) {
			aminoAcid = getAminoAcid(i);
			core = aminoAcid.getCore();
			dihedralAngles[3*i] = core.getPhiAngle();
			dihedralAngles[3*i+1] = core.getPsiAngle();
			if (i != size-1) dihedralAngles[3*i+2] = core.getOmegaAngle();
		}
		return dihedralAngles;
	}

	
	/*
	 * prints dihedral angles of the bonds 
	 */
	public void printDihedralAngles() {
		int size = getSize();
		Core core = null;
		int count = 0;
		AminoAcid aminoAcid;
		for (int i = 0; i < size; i++) {
			aminoAcid = getAminoAcid(i);
			core = aminoAcid.getCore();
			System.out.println(++count + ". " + aminoAcid.getName() + ": " + "(" + core.getPhiAngle() +", " + core.getPsiAngle() + ", " + core.getOmegaAngle() + ")");
		}
	}
	
	public void printDistances() {
		int size = getSize() - 1;
		Core core = null;
		int count = 0;
		AminoAcid aminoAcid;
		for (int i = 0; i < size; i++) {
			aminoAcid = getAminoAcid(i);
			System.out.print(++count + ". " + aminoAcid.getName() + ": ");
			if (aminoAcid.hasCore()) {
				core = aminoAcid.getCore();
				System.out.println(core.getNCaDistance() + ", " + core.getCaCDistance() + ", " + core.getCNDistance());
			}
			else System.out.println(aminoAcid.getCaCaDistance());
		}
	}

	public void setViewer(ProteinViewer viewer) { this.viewer = viewer; }
	
	/*
	 * writes 1-letter amino acids of the protein.
	 */
	public void get1LetterAminoAcids(BufferedWriter bw) {
		try {
			bw.write(getSequence() + " " + '\n');
		} catch (IOException e) { System.out.println("IOException error!"); e.printStackTrace(); }
	}

	

	/*
	 * writes 1-letter amino acids of the coil of a protein - one coil subsequence per line.
	 */
/*	public void get1LetterCoils(BufferedWriter bw) {
		try {
			int i = 1;
			while (i <= n) {
				if (secondaryStructure[i] == 'C') bw.write(getAminoAcidType(i++));
				else {
					if (i > 1) bw.write(" " + '\n');
					i++;
					while ((i <= n) && (secondaryStructure[i] != 'C')) i++;
				}	
			}			
		} catch (IOException e) { System.out.println("IOException error!"); e.printStackTrace(); }
	}
*/


	

	/*
	 * writes amino acids (3 letters) into a specified file.  
	 */
/*	public void getAminoAcids(BufferedWriter bw) {
		try {
			for (int i=1; i <=n; i++) bw.write(i + ": " + getAminoAcidSecondaryClass(i) + " " + getAminoAcidType(i) + " " + atom[i].toString() + '\n');
		} catch (IOException e) { System.out.println("IOException error!"); e.printStackTrace(); }
	}
*/
	

	/*
	 * writes coordinates of C-alpha atoms into a specified file.
	 */
/*	public void getCalphas(BufferedWriter bw) {
		try {
			for (int i=1; i<=m; i++) 
				if (getAtomType(i).equals("CA")) bw.write(atom[i].toString() + '\n'); 
		} catch (IOException e) { System.out.println("IOException error!"); e.printStackTrace(); }
	}
*/


	

	/*
	 * writes coordinates of C-alpha atoms into a specified file.
	 */
/*	public void getNumberedCalphas(BufferedWriter bw) {
		try {
			for (int i=1; i<=m; i++) 
				if (getAtomType(i).equals("CA")) bw.write(i + ": " + atom[i].toString() + '\n'); 
		} catch (IOException e) { System.out.println("IOException error!"); e.printStackTrace(); }
	}
*/

	
	/**
	 * @param args
	 */
	

	public static void main(String[] args) {
//		Protein protein = new Protein("1A6X", 2, true);
//		protein.printDihedralAngles();
//		System.out.println(protein.getNumberAtoms());
//		protein.printCoordinates();
//		protein.setLinearCa(3.8);
//		protein.setSequence(PDB.getSequence("2CRO", 'A'));
		ProteinViewer viewer = new ProteinViewer();
		new MainFrame(viewer, 1280, 960);
/*		System.out.println("Number alpha helices is " + protein.getNumberAlphaHelices());
		System.out.println("Number beta sheets is "   + protein.getNumberBetaSheets());
		protein.printSymbols();
		protein.printAlphaHelicesIntervals();
		protein.printAlphaHelicesSymbols();
		protein.printAlphaHelicalGapLengths();
*/	}


}
