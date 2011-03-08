package molecule;
import java.util.Hashtable;

import dataStructures.Set;

public class BetaSheet {
	private Protein protein;
	private String sheetId;
	private Set<BetaStrand> betaStrands;

	public BetaSheet(Protein protein) {
		this.protein = protein;
		setBetaStrands(new Set<BetaStrand>());
	}

	public int getSize() { return betaStrands.getSize(); }
	
	public void setSheetID(String sheetId) { this.sheetId = sheetId; }
	
	public String getSheetId() { return sheetId; }
	
	public void setBetaStrands(Set<BetaStrand> betaStrands) { this.betaStrands = betaStrands; }


	/*
	 * sets the secondary classification of all amino acids in this b-sheet to 'E'
	 */	
	public void setSecClass() {
		BetaStrand betaStrand;
		for (int i = 0; i < getNumberBetaStrands(); i++) {
			betaStrand = betaStrands.get(i);
			betaStrand.setSecClass();
		}
	}

	
	public int getNumberBetaStrands() { return getSize(); }
	
	public int getNumberParallelStrands() {
		int count = 0;
		BetaStrand betaStrand;
		int size = getNumberBetaStrands();
		for (int i = 0; i < size; i++) {
			betaStrand = betaStrands.get(i);
			if (betaStrand.getSense() == 1) count++;			
		}
		return count;
	}
	
	/*
	 * return number of antiparallel strands
	 */
	public int getNumberAntiParallelStrands() {
		int count = 0;
		BetaStrand betaStrand;
		int size = getNumberBetaStrands();
		for (int i = 0; i < size; i++) {
			betaStrand = betaStrands.get(i);
			if (betaStrand.getSense() == -1) count++;			
		}
		return count;
	}
	
	/*
	 * return number of antiparallel strands separated by at most gapSize residues.
	 */
	public int getNumberAntiParallelStrands(int gapSize) {
		int count = 0;
		BetaStrand betaStrand = null;
		BetaStrand prevBetaStrand = null;
		int size = getNumberBetaStrands();
		for (int i = 0; i < size; i++) {
			prevBetaStrand = betaStrand;
			betaStrand = betaStrands.get(i);
			if ((betaStrand.getSense() == -1) && 
				(Math.abs(protein.getIndex(betaStrand.getInitSeqNum()) - protein.getIndex(prevBetaStrand.getEndSeqNum())) <= gapSize+1 ))  count++;			
		}
		return count;
	}
	
	public Set<BetaStrand> getBetaStrands() { return betaStrands; }
	
	public BetaStrand getBetaStrand(int i) { return betaStrands.get(i); }
	
	public void getAminoAcidFrequenciesInBetaStrands(Hashtable aminoAcidFrequencies) {
		char[] charString;
		BetaStrand betaStrand;
		int size = betaStrands.getSize();
		for (int i = 0; i < size; i++) {
			betaStrand = betaStrands.get(i);
			charString  = betaStrand.getSymbols().toCharArray();
			for (int j = 0; j < charString.length; j++) {
				aminoAcidFrequencies.put(charString[j], (Integer)aminoAcidFrequencies.get(charString[j]) + 1);
			}

		}
	}
	

	
	
	/*
	 * prints gap sizes (number of amino acids) between consecutive beta strands
	 */
	public void printBetaStrandGapLengths() {
		BetaStrand betaStrand;
		BetaStrand betaStrandPrev = betaStrands.get(0);
		int size = getSize();
		System.out.print("Lenghts of b-strand gaps: ");
		for (int i = 1; i < size; i++) {
			betaStrand = betaStrands.get(i);
			System.out.print(betaStrand.getInitSeqNum() - betaStrandPrev.getEndSeqNum() - 1 + " ");
			betaStrandPrev = betaStrand;
		}
		System.out.print(", ");
	}
	
	/*
	 * prints a beta strand preceded by its orientation w.r.t. predecessor strand
	 */
	public void printBetaStrandSymbols() {
		BetaStrand betaStrand;
		int size = getSize();
		for (int j = 0; j < size; j++) {
			betaStrand = getBetaStrand(j);
			System.out.print(betaStrand.getSense() + "[" + betaStrand.getSymbols() + "]");
		}
		System.out.print(", ");
	}
	
	public void printBetaStrandIntervals() {
		BetaStrand betaStrand;
		int size = getSize();
		System.out.print("[");
		for (int i = 0; i < size; i++) {
			betaStrand = betaStrands.get(i);
			System.out.print(betaStrand.getSense() + "[" + betaStrand.getInitSeqNum() + "," + betaStrand.getEndSeqNum() + "]");
		}
		System.out.println("]");
		
	}
}
