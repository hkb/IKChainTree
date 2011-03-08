package molecule;

import geom3d.*;
import misc.Functions;
import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.LineArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;

/*
 * b-strands can be onsidered a very extended left helix which turns 60 per two residues.
 */
public class BetaStrand {
	private int n;
	private Protein protein;
	private int initSeqNum;
	private int endSeqNum;
	private int sense;
	
	public BetaStrand(Protein protein, int initSeqNum, int endSeqNum, int sense) {
		this.protein = protein;
		this.initSeqNum = initSeqNum;
		this.endSeqNum = endSeqNum;
		this.sense = sense;
		n = protein.getIndex(endSeqNum) - protein.getIndex(initSeqNum) + 1;
}
	
	public Protein getProtein() { return protein; }
	public int getSize() { return n; }
	public int getInitSeqNum() { return initSeqNum; }	
	public int getEndSeqNum() { return endSeqNum; }
	public int getSense() { return sense; }
	
	public String getSymbols() { 
//		System.out.println(protein.getSequence());
//		System.out.println("Init: " + protein.getIndex(initSeqNum)  + ", End: " + protein.getIndex(endSeqNum));
		return String.copyValueOf(protein.getSequence(), protein.getIndex(initSeqNum), n); 
	} 
	
	public char getSymbol(int i) { return getSymbols().charAt(i); }
	 
	/*
	 * sets the secondary classification of all amino acids in this b-strand to 'E'
	 */
	public void setSecClass() {
		for (int j = protein.getIndex(getInitSeqNum()); j < protein.getIndex(getEndSeqNum()); j++) protein.getAminoAcid(j).setSecClass('E');
	}

	
	/*
	 * returns distances between Ca atoms in this and another beta strand. 
	 * This is not good enough to identify hydrogen bonds.
	 */
	public double[][] getDistances(BetaStrand betaStrand) {
		Point3d ci, cj;
		int m = betaStrand.getSize();
		int offset = protein.getFirstAminoAcidPDBNumber();
		double[][] distMatrix = new double[n][m];
		for (int i = 0; i < n; i++) {
			ci = protein.getAminoAcid(protein.getIndex(initSeqNum + i)).getPosition();
			for (int j = 0; j < m; j++) {
				cj = protein.getAminoAcid(protein.getIndex(betaStrand.getInitSeqNum() + j)).getPosition();
				distMatrix[i][j] = ci.getDistance(cj);
			}
		}
		return distMatrix;
	}
	
	/*
	 * for each amino acid in the shorter beta strand, returns the index of the closest amino acid in the longer beta strand 
	 */
	public Integer [] getPairing(BetaStrand betaStrand) {
		Point3d pA, pB;
		Integer [] mate = new Integer [getSize()];
		double dist, minDist;
		int indxAInit = protein.getIndex(initSeqNum);
		int indxAEnd = protein.getIndex(endSeqNum);
		int indxBInit = protein.getIndex(betaStrand.getInitSeqNum());
		int indxBEnd = protein.getIndex(betaStrand.getEndSeqNum());
		for (int i = 0; i < indxAEnd - indxAInit+1; i++) {
			pA = protein.getCaPosition(indxAInit + i);
			minDist = 999999.9;
			for (int j = 0; j < indxBEnd - indxBInit+1; j++) {
				pB = protein.getCaPosition(indxBInit + j);
				dist = pA.getSquaredDistance(pB);
				if (dist < minDist) {
					minDist = dist;
					mate[i] = j;
				}
			}
			if (minDist > 36) mate[i] = null;
//			System.out.print(Functions.toString(minDist,0) + " ");
		}
		return mate;
	}
	
	/*
	 * prints distances between Ca atoms in this and another beta strand
	 */
	public void printDistances(BetaStrand betaStrand) {
		double[][] distMatrix = getDistances(betaStrand);
		int m = betaStrand.getSize();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) System.out.print(Functions.toString(distMatrix[i][j],2) + " ");
			System.out.println();
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
			if (aminoAcid.getPrev() != null) {
				recolorBond(aminoAcid.getPrev().getPeptideBondShape(), color);
				recolorBond(aminoAcid.getPrev().getCaCaBondShape(), color);
			}
			if (aminoAcid.getCore().hasN()) {
				recolorBond(aminoAcid.getCore().getNCaBondShape(), color);
				recolorBond(aminoAcid.getCore().getCaCBondShape(), color);
				if (aminoAcid.getCore().hasO()) recolorBond(aminoAcid.getCore().getCOBondShape(), color);
			}
		}
	}


}
