package molecule;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

public class PDBStatistics {
	
	public static void getNumberProteinsWithEmptyIntervals() {
		Hashtable<Integer,Integer> emptyIntervalsFrequencies = new Hashtable<Integer,Integer>();
		for (int i = 1; i <= 30; i++) emptyIntervalsFrequencies.put(i,0);
		String name, record;
		Protein p;
		PDBSelect25 pdbSelect25 = new PDBSelect25();
		int count = 0;
		int totalCount = 0;
		int nrEmptyIntervals = 0;
		while ((record = pdbSelect25.getNextRecord()) != null) {
			name = record.substring(8,13);
			if (!name.equals("-----")) { 	
				p = new Protein(name,1,true); 
				count++;
				totalCount = totalCount + p.getEmptyIntervals().getSize();
				if (!p.getEmptyIntervals().isEmpty()) nrEmptyIntervals++;
				System.out.println(count + ": " + nrEmptyIntervals);
				p.getEmptyIntervalsFrequencies(emptyIntervalsFrequencies);
				p.clear();
			}
		}
		System.out.println("Number of proteins with empty intervals is: " + nrEmptyIntervals +
				           " out of " + count + " proteins. This is " + 100.0*nrEmptyIntervals/count + "%");
		System.out.println("Total number of empty intervals is " + totalCount);
		System.out.println(emptyIntervalsFrequencies.toString());

	}
	
	/*
	 * frequencies of SS-bonds in PDBSelect_25 proteins
	 */
	public static void getSSFrequencies() {
		Hashtable<Integer,Integer> ssBondFrequencies = new Hashtable<Integer,Integer>();
		for (int i = 1; i <= 25; i++) ssBondFrequencies.put(i,0);
		String name, record;
		Protein p;
		PDBSelect25 pdbSelect25 = new PDBSelect25();
		int count = 0;
		int totalCount = 0;
		int nrSSBonds = 0;
		int nrProteinsWithSSBonds = 0;
		while ((record = pdbSelect25.getNextRecord()) != null) {
			name = record.substring(8,13);
			if (!name.equals("-----")) { 	
				p = new Protein(name,1,true); 
				count++;
				if (!p.getSSBonds().isEmpty()) {
					nrProteinsWithSSBonds++;
					nrSSBonds = p.getSSBonds().getSize()/2;
					totalCount = totalCount + nrSSBonds;
					System.out.println(count  + ". " + name + ": " +  + nrSSBonds);
					ssBondFrequencies.put(nrSSBonds, ssBondFrequencies.get(nrSSBonds) + 1);
				}
				p.clear();
			}
		}
		System.out.println("Number of proteins with SS-bonds is: " + nrProteinsWithSSBonds +
				           " out of " + count + " proteins. This is " + 1.0*totalCount/nrProteinsWithSSBonds + "SS-bonds on average");
		System.out.println("Total number of SS-bonds is " + totalCount);
		lengthsToPercents(ssBondFrequencies,nrProteinsWithSSBonds);
		System.out.println(ssBondFrequencies.toString());
	}

	
	/*
	 * number of parallel and antiparallel strands across selected pdb files 
	 */ 
	public static void getStrandDistribution() {
		String name, record;
		Protein protein;
		PDBSelect25 pdbSelect25 = new PDBSelect25();
		int count = 0;
		int countParallelStrands = 0;
		int countAntiParallelStrands = 0;
		int countCloseAntiParallelStrands = 0;
		while ((record = pdbSelect25.getNextRecord()) != null) {
			name = record.substring(8,13);
			if (!name.equals("-----")) { 	
				System.out.print(++count + ". " + name + ": ");
				protein = new Protein(name,1,true); 
				System.out.println(protein.getSequence());
				protein.printBetaSheetsSymbols();
				countParallelStrands += protein.getNumberParallelStrands();
				countAntiParallelStrands += protein.getNumberAntiParallelStrands();
				countCloseAntiParallelStrands += protein.getNumberAntiParallelStrands(4);
				protein.clear();
			}
		}
		System.out.println("Number of parallel strands: " + countParallelStrands);
		System.out.println("Number of antiparallel strands: " + countAntiParallelStrands);
		System.out.println("Number of close antiparallel strands: " + countCloseAntiParallelStrands);
		
	}
	public static void getSheetSizeDistribution() {
		String name, record;
		Protein protein;
		PDBSelect25 pdbSelect25 = new PDBSelect25();
		int[] sheetSizes = new int[9];
		while ((record = pdbSelect25.getNextRecord()) != null) {
			name = record.substring(8,13);
			if (!name.equals("-----")) { 	
				//System.out.print(++count + ". " + name + ": ");
				protein = new Protein(name,1,true); 
				//System.out.println(protein.getSequence());
				//protein.printBetaSheetsSymbols();
				for(int s=0;s<protein.getBetaSheets().getSize();s++){
					BetaSheet sheet = protein.getBetaSheet(s);
					sheetSizes[Math.min(sheet.getSize(),sheetSizes.length-1)]++;
				}
				
				protein.clear();
			}
		}
		
		System.out.println("Beta sheet size distribution");
		for(int s=0;s<sheetSizes.length;s++){
			System.out.println( (s)+" "+sheetSizes[s]);
		}
	}
	
	private static void setAminoAcidFrequencies(Hashtable frequencies) {
		frequencies.put('A', 0); frequencies.put('R', 0); frequencies.put('N', 0); frequencies.put('D', 0);
		frequencies.put('C', 0); frequencies.put('E', 0); frequencies.put('Q', 0); frequencies.put('G', 0);
		frequencies.put('H', 0); frequencies.put('I', 0); frequencies.put('L', 0); frequencies.put('K', 0);
		frequencies.put('M', 0); frequencies.put('F', 0); frequencies.put('P', 0); frequencies.put('S', 0);
		frequencies.put('T', 0); frequencies.put('W', 0); frequencies.put('Y', 0); frequencies.put('V', 0);
		frequencies.put('m', 0); frequencies.put('e', 0); frequencies.put('a', 0); frequencies.put('c', 0); 
		frequencies.put('p', 0); frequencies.put('x', 0); frequencies.put('?', 0); 
	}
	
	private static void setLengthFrequencies(Hashtable<Integer,Integer> frequencies) {
		frequencies.put(1, 0);  frequencies.put(2, 0);  frequencies.put(3, 0);  frequencies.put(4, 0);
		frequencies.put(5, 0);  frequencies.put(6, 0);  frequencies.put(7, 0);  frequencies.put(8, 0);
		frequencies.put(9, 0);  frequencies.put(10, 0); frequencies.put(11, 0); frequencies.put(12, 0);
		frequencies.put(13, 0); frequencies.put(14, 0); frequencies.put(15, 0); frequencies.put(16, 0);
		frequencies.put(17, 0); frequencies.put(18, 0); frequencies.put(19, 0); frequencies.put(20, 0);
		frequencies.put(21, 0); frequencies.put(22, 0); frequencies.put(23, 0); frequencies.put(24, 0);
		frequencies.put(25, 0); 
	}

	
	private static void toPercents(Hashtable frequencies, int total) {
		frequencies.put('A', 10000*(Integer)frequencies.get('A')/total);
		frequencies.put('C', 10000*(Integer)frequencies.get('C')/total);
		frequencies.put('D', 10000*(Integer)frequencies.get('D')/total);
		frequencies.put('E', 10000*(Integer)frequencies.get('E')/total);
		frequencies.put('F', 10000*(Integer)frequencies.get('F')/total);
		frequencies.put('G', 10000*(Integer)frequencies.get('G')/total);
		frequencies.put('H', 10000*(Integer)frequencies.get('H')/total);
		frequencies.put('I', 10000*(Integer)frequencies.get('I')/total);
		frequencies.put('K', 10000*(Integer)frequencies.get('K')/total);
		frequencies.put('L', 10000*(Integer)frequencies.get('L')/total);
		frequencies.put('M', 10000*(Integer)frequencies.get('M')/total);
		frequencies.put('N', 10000*(Integer)frequencies.get('N')/total);
		frequencies.put('P', 10000*(Integer)frequencies.get('P')/total);
		frequencies.put('Q', 10000*(Integer)frequencies.get('Q')/total);
		frequencies.put('R', 10000*(Integer)frequencies.get('R')/total);
		frequencies.put('S', 10000*(Integer)frequencies.get('S')/total);
		frequencies.put('T', 10000*(Integer)frequencies.get('T')/total);
		frequencies.put('V', 10000*(Integer)frequencies.get('V')/total);
		frequencies.put('W', 10000*(Integer)frequencies.get('W')/total);
		frequencies.put('Y', 10000*(Integer)frequencies.get('Y')/total);
	}

	private static void lengthsToPercents(Hashtable frequencies, int total) {
		for (int i = 1; i < 26; i++) frequencies.put(i, 10000*(Integer)frequencies.get(i)/total);
	}
	
	
	/*
	 * frequencies of amino acids across all proteins in pdb select25
	 */
	public static void getAminoAcidFrequencies() {
		try {
			String name;
			int count = 0;
			int total = 0;
			Protein p;
			BufferedReader br = new BufferedReader(new FileReader("pdb_select_25_4018"));
			br.readLine(); br.readLine(); br.readLine();
			String record = new String();
			Hashtable aminoAcidFrequencies = new Hashtable();
			setAminoAcidFrequencies(aminoAcidFrequencies);
			while ((record = br.readLine()) != null) {
				name = record.substring(8,13);
				if (!name.equals("-----")) {
					System.out.print(++count + ". " + name + ": ");
					p = new Protein(name,1,true);
					total += p.getSize();
					p.printSymbols();
					p.getAminoAcidFrequencies(aminoAcidFrequencies);
					p.clear();
				}
			}
			toPercents(aminoAcidFrequencies, total);

			System.out.println(aminoAcidFrequencies.toString());
			System.out.println("Total number of amino acids is " + total);
		} catch (IOException e) {	System.out.println("IOException error!"); e.printStackTrace(); }

	}
	
	/*
	 * frequencies of amino acids in helicies across all selected pdb-files.
	 */
	public static void getAminoAcidFrequenciesInAlphaHelices() {
		try {
			String name;
			int count = 0;
			int countProteinsWithHelices = 0;
			int countHelices = 0;
			int countResiduesInHelices = 0;
			int lng;
			Protein p;
			AlphaHelix alphaHelix;
			BufferedReader br = new BufferedReader(new FileReader("pdb_select_25_4018"));
			br.readLine(); br.readLine(); br.readLine();
			String record = new String();
			Hashtable aminoAcidFrequencies = new Hashtable();
			setAminoAcidFrequencies(aminoAcidFrequencies);
			Hashtable<Integer,Integer> lengthFrequencies = new Hashtable<Integer,Integer>();
			setLengthFrequencies(lengthFrequencies);
			while ((record = br.readLine()) != null) {
				name = record.substring(8,13);
				if (!name.equals("-----")) {
					System.out.print(++count + ". " + name + ": ");
					p = new Protein(name,1,true);
					if (!p.getAlphaHelices().isEmpty())  {
						countProteinsWithHelices++;
						countHelices = countHelices + p.getAlphaHelices().getSize();
						for (int i = 0; i < p.getNumberAlphaHelices(); i++) {
							alphaHelix = (AlphaHelix)p.getAlphaHelices().get(i);
							lng = alphaHelix.getSize();
							countResiduesInHelices = countResiduesInHelices + lng;
							if (lng > 24) lng = 25;
							lengthFrequencies.put(lng, lengthFrequencies.get(lng)+1);
						}
						p.printAlphaHelicesSymbols();	
						p.getAminoAcidFrequenciesInAlphaHelices(aminoAcidFrequencies);
					}
					else System.out.println();
					p.clear();
				}
			}
			
			toPercents(aminoAcidFrequencies,countResiduesInHelices);
			System.out.println(aminoAcidFrequencies.toString());
			lengthsToPercents(lengthFrequencies,countHelices);
			System.out.println(lengthFrequencies.toString());

			System.out.println("Total number of proteins is " + count);
			System.out.println("Total number of proteins with alpha helices is " + countProteinsWithHelices);
			System.out.println("Total number of alpha helices is " + countHelices);
			System.out.println("Total number of residues in alpha helices is " + countResiduesInHelices);
		} catch (IOException e) {	System.out.println("IOException error!"); e.printStackTrace(); }
	}

	
	/*
	 * frequencies of amino acids in strands across all selected pdb-files in select25.
	 */
	public static void getAminoAcidFrequenciesInBetaStrands() {
		try {
			String name;
			int count = 0;
			int countProteinsWithBetaSheets = 0;
			int countBetaSheets = 0;
			int countBetaStrands = 0;
			int countResiduesInBetaStrands = 0;
			int countParallelStrandPairs = 0;
			int countAntiParallelStrandPairs = 0;
			int lng = 0;
			int[][] lengthPairsParallel = new int[17][17];
			int[][] lengthPairsAntiParallel = new int[17][17];
			Integer [] mateBack;
			Integer [] mateForw;
			int[][] pairFreq = new int[21][21];
			BetaSheet betaSheet;
			BetaStrand betaStrand, prevBetaStrand;
			Protein p;
			BufferedReader br = new BufferedReader(new FileReader("pdb_select_25_4018"));
			br.readLine(); br.readLine(); br.readLine();
			String record = new String();
			Hashtable aminoAcidFrequencies = new Hashtable();
			setAminoAcidFrequencies(aminoAcidFrequencies);
			Hashtable<Integer,Integer> lengthFrequencies = new Hashtable<Integer,Integer>();
			setLengthFrequencies(lengthFrequencies);
			while ((record = br.readLine()) != null) {
				name = record.substring(8,13);
				if (!name.equals("-----")) {
					System.out.print(++count + ". " + name + ": ");
					p = new Protein(name,1,true);
					if (!p.getBetaSheets().isEmpty()) {
						countProteinsWithBetaSheets++;
						countBetaSheets = countBetaSheets + p.getBetaSheets().getSize();
						for (int i = 0; i < p.getNumberBetaSheets(); i++) {
							betaSheet = (BetaSheet)p.getBetaSheet(i);
							countBetaStrands = countBetaStrands + betaSheet.getNumberBetaStrands();
							prevBetaStrand = null;
							for (int j = 0; j < betaSheet.getNumberBetaStrands(); j++) {
								betaStrand = (BetaStrand)betaSheet.getBetaStrand(j);
								countResiduesInBetaStrands = countResiduesInBetaStrands + betaStrand.getSize();
								if ((betaStrand.getSense() != 0) && (betaStrand.getSize() == 5) && (prevBetaStrand.getSize() == 5)) {
									mateBack = betaStrand.getPairing(prevBetaStrand);
/*									System.out.print("[");
									for (int k = 0; k < mateBack.length; k++) System.out.print(mateBack[k] + " ");
									System.out.print("]");
*/									mateForw = prevBetaStrand.getPairing(betaStrand);
/*									System.out.print("[");
									for (int k = 0; k < mateForw.length; k++) System.out.print(mateForw[k] + " ");
									System.out.print("]");
*/									for (int k = 0; k < mateForw.length; k++) {
										if (mateForw[k] != null) {
											if (mateBack[mateForw[k]].intValue() == k) { 
												System.out.print(prevBetaStrand.getSymbol(k)); 
												System.out.print(betaStrand.getSymbol(mateForw[k]) + " ");
												pairFreq[AminoAcid.toInt(prevBetaStrand.getSymbol(k))][AminoAcid.toInt(betaStrand.getSymbol(mateForw[k]))]++;
											}
										}
									}
								}
								// computes number of pair lengths of paired beta strands
								if (betaStrand.getSense() == 1) {
									countParallelStrandPairs++;
									if (prevBetaStrand.getSize() < 16) {
										if (betaStrand.getSize() < 16) lengthPairsParallel[prevBetaStrand.getSize()][betaStrand.getSize()]++;
										else lengthPairsParallel[prevBetaStrand.getSize()][16]++;
									}
									else {
										if (betaStrand.getSize() < 16) lengthPairsParallel[16][betaStrand.getSize()]++;
										else lengthPairsParallel[16][16]++;
									}
								}
								else {
									if (betaStrand.getSense() == -1) {
										countAntiParallelStrandPairs++;
										if (prevBetaStrand.getSize() < 16) {
											if (betaStrand.getSize() < 16) lengthPairsAntiParallel[prevBetaStrand.getSize()][betaStrand.getSize()]++;
											else lengthPairsAntiParallel[prevBetaStrand.getSize()][16]++;
										}
										else {
											if (betaStrand.getSize() < 16) lengthPairsAntiParallel[16][betaStrand.getSize()]++;
											else lengthPairsAntiParallel[16][16]++;
										}
									}
								}
								lng = betaStrand.getSize();
								if (lng > 24) lng = 25;
								lengthFrequencies.put(lng, lengthFrequencies.get(lng)+1);
								prevBetaStrand = betaStrand;
							}
						}
					}
					p.printBetaSheetsSymbols();
					p.getAminoAcidFrequenciesInBetaStrands(aminoAcidFrequencies);
					p.clear();
				}
			}
			toPercents(aminoAcidFrequencies,countResiduesInBetaStrands);
			System.out.println(aminoAcidFrequencies.toString());
			lengthsToPercents(lengthFrequencies,countBetaStrands);
			System.out.println(lengthFrequencies.toString());
			
			System.out.println("Total number of proteins is " + count);
			System.out.println("Total number of proteins with beta sheets is " + countProteinsWithBetaSheets);
			System.out.println("Total number of beta sheets is " + countBetaSheets);
			System.out.println("Total number of beta strands is " + countBetaStrands);
			System.out.println("Total number of residues in beta strands is " + countResiduesInBetaStrands);
			System.out.println("Total number of parallel pairs of strands is " + countParallelStrandPairs);
			System.out.println("Total number of antiparallel pairs of strands is " + countAntiParallelStrandPairs);

			System.out.println("Frequencies of length pairs of strands; parallel and antiarallel");
			for (int i = 1; i < 17; i++) {
				System.out.print(i + ": " + (lengthPairsParallel[i][i]+lengthPairsAntiParallel[i][i]) + " ");
				for (int j = i+1; j < 17; j++) System.out.print((lengthPairsParallel[i][j]+lengthPairsParallel[j][i]+lengthPairsAntiParallel[i][j]+lengthPairsAntiParallel[j][i]) + " ");
				System.out.println();
			}
			System.out.println(); System.out.println();

			System.out.println("Frequencies of length pairs of parallel strands");
			for (int i = 1; i < 17; i++) {
				System.out.print(i + ": " + lengthPairsParallel[i][i] + " ");
				for (int j = i+1; j < 17; j++) System.out.print((lengthPairsParallel[i][j]+lengthPairsParallel[j][i]) + " ");
				System.out.println();
			}
			System.out.println(); System.out.println();
			
			System.out.println("Frequencies of length pairs of antiparallel strands");			
			for (int i = 1; i < 17; i++) {
				System.out.print(i + ": " + lengthPairsAntiParallel[i][i] + " ");
				for (int j = i+1; j < 17; j++) System.out.print((lengthPairsAntiParallel[i][j]+lengthPairsAntiParallel[j][i]) + " ");
				System.out.println();
			}
			for (int i = 0; i < 21; i++) {
				System.out.print(AminoAcid.toChar(i) + " & ");				
				for (int j = 0; j < i; j++) System.out.print((pairFreq[i][j]+pairFreq[j][i]) + " & ");
				System.out.print(pairFreq[i][i] + " & ");
				for (int j = i+1; j < 21; j++) System.out.print((pairFreq[i][j]+pairFreq[j][i]) + " & ");
				System.out.println("hline");
			}
		} catch (IOException e) {	System.out.println("IOException error!"); e.printStackTrace(); }
	}
	
	/*
	 * returns potential beta hairpins: 2 antiparallel beta-strands separated by at most 5 residues 
	 */
	public static void lookForBetaHairpins(int gapSize) {
		try {
			byte[] classification;
			int lastIndex;
			int count = 0;
			int nrPotentialBetaHairpins = 0;
			int nrProteinsWithPotentialBetaHairpins = 0;
			boolean hasBetaHairpins;
			String name;
			Protein p;
			BufferedReader br = new BufferedReader(new FileReader("pdb_select_25_4018"));
			br.readLine(); br.readLine(); br.readLine();
			String record = new String();
			while ((record = br.readLine()) != null) {
				name = record.substring(8,13);
				if (!name.equals("-----")) {
					p = new Protein(name,1,true);
					count++;
					hasBetaHairpins = false;
					lastIndex = -1;
					classification = p.getClassificationArray();
					for (int i = 0; i < classification.length; i++) {
						if ((classification[i] >= 4) && (classification[i] <= 6)) {
							if ((i != 0) && (classification[i-1] != classification[i])) {
								if ((lastIndex >= 0) && (i - lastIndex <= gapSize+1)) {
									nrPotentialBetaHairpins++;
									if (!hasBetaHairpins) {
										System.out.print(count + ". " + name + "[0," + (p.getSize()-1) + "]: [");
										for (int j = 0; j < p.getSize(); j++) System.out.print((j - 10*(j/10)));
										System.out.println("]");
										System.out.print(count + ". " + name + "[0," + (p.getSize()-1) + "]: [");
										p.printSymbols();
										hasBetaHairpins = true;
										nrPotentialBetaHairpins++;
										nrProteinsWithPotentialBetaHairpins++;
									}
									System.out.print(count + ". " + name + "[0," + (p.getSize()-1) + "]: ");
									System.out.println("Potential beta-hairpin between beta-strand ending at " + lastIndex + " and beginning at " + i);
								}
							}
						}
						if ((i < classification.length-1) && (classification[i] >= 4) && (classification[i] <= 6) && 
							(classification[i] != classification[i+1])) lastIndex = i;
					}
					p.clear();
				}
			}
			System.out.println("Number of potential hairpins is " + nrPotentialBetaHairpins);
			System.out.println("Number of proteins with potential beta hairpins is " + nrProteinsWithPotentialBetaHairpins);
		} catch (IOException e) {	System.out.println("IOException error!"); e.printStackTrace(); }
	}
	
	public static void checkForBreaks() {
		try {
			int count = 0;
			int nrBreaks = 0;
			String name;
			Protein p;
			BufferedReader br = new BufferedReader(new FileReader("pdb_select_25_4018"));
			br.readLine(); br.readLine(); br.readLine();
			String record = new String();
			while ((record = br.readLine()) != null) {
				name = record.substring(8,13);
				if (!name.equals("-----")) {
					p = new Protein(name,1,true);
					System.out.println(++count + ". " + name);
					if (p.hasBreak(12)) nrBreaks++;
					p.clear();
				}
			}
			System.out.println("Number of breaks is " + nrBreaks);
		} catch (IOException e) {	System.out.println("IOException error!"); e.printStackTrace(); }
	}
	
	public static void checkForInsertions() {
		try {
			int count = 0;
			int nrInsertions = 0;
			String name;
			Protein p;
			BufferedReader br = new BufferedReader(new FileReader("pdb_select_25_4018"));
			br.readLine(); br.readLine(); br.readLine();
			String record = new String();
			while ((record = br.readLine()) != null) {
				name = record.substring(8,13);
				if (!name.equals("-----")) {
					p = new Protein(name,1,true);
					System.out.println(++count + ". " + name);
					if (p.hasInsertions()) nrInsertions++;
					p.clear();
				}
			}
			System.out.println("Number of insertions is " + nrInsertions);
		} catch (IOException e) {	System.out.println("IOException error!"); e.printStackTrace(); }
	}

	public static void showSecondaryStructureIntervals() {
		try {
			AlphaHelix alphaHelix;  
			BetaSheet betaSheet;     
			BetaStrand betaStrand; 
			int ssBond;      
			int emptyIntervaIndex = 0; int emptyIntervalMaxIndex;
			int count = 0;
			int maxSize = 0;
			byte[] classification;
			String name;
			Protein p;
			BufferedReader br = new BufferedReader(new FileReader("pdb_select_25_4018"));
			br.readLine(); br.readLine(); br.readLine();
			String record = new String();
			while ((record = br.readLine()) != null) {
				name = record.substring(8,13);
				if (!name.equals("-----")) {
					p = new Protein(name,1,true);
					if (maxSize < p.getSize()) maxSize = p.getSize();
					System.out.print(++count + ". " + name + "[0," + (p.getSize()-1) + "]: ");
					for (int i = 0; i < p.getNumberAlphaHelices(); i++) {
						alphaHelix = p.getAlphaHelix(i);
						System.out.print("H" + alphaHelix.getType() + "[" + p.getIndex(alphaHelix.getInitSeqNum()) + "," + p.getIndex(alphaHelix.getEndSeqNum()) + "] ");
					}
					for (int i = 0; i < p.getNumberBetaSheets(); i++) {
						betaSheet = p.getBetaSheet(i);
						for (int j = 0; j < betaSheet.getNumberBetaStrands(); j++) {
							betaStrand = betaSheet.getBetaStrand(j);
							System.out.print("B" + betaStrand.getSense() + "[" + p.getIndex(betaStrand.getInitSeqNum()) + "," + p.getIndex(betaStrand.getEndSeqNum()) + "] ");
						}
					}
					for (int i = 0; i < p.getNumberSSBonds(); i++) {
						System.out.print("S1[" + p.getIndex(p.getSSBond(2*i)) + "," + p.getIndex(p.getSSBond(2*i)) + "] ");
						System.out.print("S1[" + p.getIndex(p.getSSBond(2*i+1)) + "," + p.getIndex(p.getSSBond(2*i+1)) + "] ");
					}						
					for (int i = 0; i < p.getNumberEmptyIntervals(); i++) 
						System.out.print("I1[" + p.getIndex(p.getEmptyInterval(2*i)) + "," + p.getIndex(p.getEmptyInterval(2*i+1)) + "] ");
					System.out.println();

				
					System.out.print(count + ". " + name + "[0," + (p.getSize()-1) + "]: [");
					for (int i = 0; i < p.getSize(); i++) System.out.print((i - 10*(i/10)));
					System.out.println("]");
					
					System.out.print(count + ". " + name + "[0," + (p.getSize()-1) + "]: [");
					classification = p.getClassificationArray();
					p.printClassificationArray();
					
					System.out.print(count + ". " + name + "[0," + (p.getSize()-1) + "]: [");
					p.printSymbols();

					p.clear();
				}
			}
			System.out.println("Max size is " + maxSize);
		} catch (IOException e) {	System.out.println("IOException error!"); e.printStackTrace(); }
	}
	
	
	/*
	 * frequencies of amino acids in strands across all selected pdb-files in select25.
	 */
	public static void statisticsForChainTrees() {
		try {
			String name;
			int count = 0;
			int countProteinsWithBetaStrands = 0;
			int countProteinsWithAlphaHelices = 0;
			int countProteinsWithAlphaHelicesAndBetaStrands = 0;
			int countNoAlphaHelicesNorBetaStrands = 0;
			int countHelices = 0;
			int countStrands = 0;
			int countAminoAcids = 0;
			int countAminoAcidsInStrandsThisProtein = 0;
			int countAminoAcidsInHelicesThisProtein = 0;
			int countAminoAcidsInHelices = 0;
			int countAminoAcidsInStrands = 0;
			int lengthFrequencies[] = new int[71];
			int ssFrequencies[] = new int[11];

			
			
			
			BetaSheet betaSheet;
			Protein p;
			BufferedReader br = new BufferedReader(new FileReader("pdb_select_25_4018"));
			br.readLine(); br.readLine(); br.readLine();
			String record = new String();
			while ((record = br.readLine()) != null) {
				name = record.substring(8,13);
				if (!name.equals("-----")) {
					p = new Protein(name,1,true);
					System.out.println(++count + ". " + name + ": " + p.getSize());
					
					// updates proteins length frequencies
					
					int lng = p.getSize()/10;
					if (lng > 69) lng = 70;
					lengthFrequencies[lng]++;

					
					// counts the number of proteins with helices, strands, both and none
					
					if (!p.getBetaSheets().isEmpty()) {
						if (!p.getAlphaHelices().isEmpty()) countProteinsWithAlphaHelicesAndBetaStrands++;
						else countProteinsWithBetaStrands++;
					}
					else
						if (!p.getAlphaHelices().isEmpty()) countProteinsWithAlphaHelices++;
						else countNoAlphaHelicesNorBetaStrands++;
					
					// counts the number of helices and strand in proteins
					
					
					if (!p.getAlphaHelices().isEmpty()) { 
						countHelices += p.getNumberAlphaHelices();
					}

					if (!p.getBetaSheets().isEmpty()) {
						for (int i = 0; i < p.getNumberBetaSheets(); i++) {
							betaSheet = p.getBetaSheet(i);
							countStrands += betaSheet.getNumberBetaStrands();
						}
					}
					
					// counts the number af amino acids in proteins, in helices and in strannd
					
					countAminoAcidsInHelicesThisProtein = 0;
					countAminoAcids += p.getSize();
					for (int i = 0; i < p.getNumberAlphaHelices(); i++) {
						System.out.println("helix length: " + ((AlphaHelix)p.getAlphaHelices().get(i)).getSize());
						countAminoAcidsInHelicesThisProtein += ((AlphaHelix)p.getAlphaHelices().get(i)).getSize();
					}
					countAminoAcidsInHelices += countAminoAcidsInHelicesThisProtein;	
					
					countAminoAcidsInStrandsThisProtein = 0;
					for (int i = 0; i < p.getNumberBetaSheets(); i++) {
						betaSheet = p.getBetaSheet(i);
						for (int j = 0; j < betaSheet.getNumberBetaStrands(); j++) {
							System.out.println("strand length: " + ((BetaStrand)betaSheet.getBetaStrands().get(j)).getSize());
							countAminoAcidsInStrandsThisProtein += ((BetaStrand)betaSheet.getBetaStrands().get(j)).getSize();
						}
						countAminoAcidsInStrands += countAminoAcidsInStrandsThisProtein;
					}
//					System.out.println(countAminoAcidsInHelicesThisProtein);
//					System.out.println(countAminoAcidsInStrandsThisProtein);					
					ssFrequencies[(10*(countAminoAcidsInHelicesThisProtein+countAminoAcidsInStrandsThisProtein))/p.getSize()]++;
			
					if ((p.getSize() >= 110) && (p.getSize() < 120) && ((10*(countAminoAcidsInHelicesThisProtein+countAminoAcidsInStrandsThisProtein))/p.getSize() == 5)) break;
				}
			}
			
			System.out.println("Total number of proteins is " + count);
			System.out.println("Total number of proteins with helices is " + countProteinsWithAlphaHelices);
			System.out.println("Total number of proteins with beta strands is " + countProteinsWithBetaStrands);
			System.out.println("Total number of proteins with  helices and beta strands is " + countProteinsWithAlphaHelicesAndBetaStrands);

			System.out.println("Total number of helices is " + countHelices);
			System.out.println("Total number of strands is " + countStrands);
			
			System.out.println("Total number of amino acid " + countAminoAcids);
			System.out.println("Total number of amino acid in helices " + countAminoAcidsInHelices);
			System.out.println("Total number of amino acid in strands " + countAminoAcidsInStrands);
			

			System.out.println("Length frequencies of proteins");
			for (int i = 0; i < 41; i++) System.out.println(i + ": " + lengthFrequencies[i] + " ");
			
			System.out.println("Portion of helices and strands in proteins (in %)");
			for (int i = 0; i < 10; i++) System.out.println(i + ": " + ssFrequencies[i] + " ");

			
		} catch (IOException e) {	System.out.println("IOException error!"); e.printStackTrace(); System.err.flush(); System.out.flush();}
		catch(ArithmeticException e){
			e.printStackTrace();
		}
	}

	
	public static void main(String[] args) {
//		PDBStatistics.getNumberProteinsWithEmptyIntervals();
		PDBStatistics.getStrandDistribution();
//		PDBStatistics.getAminoAcidFrequencies();
//		PDBStatistics.getAminoAcidFrequenciesInAlphaHelices();
//		PDBStatistics.getAminoAcidFrequenciesInBetaStrands();
//		PDBStatistics.getSSFrequencies();
//		PDBStatistics.checkForBreaks();
//		PDBStatistics.checkForInsertions();
//		PDBStatistics.showSecondaryStructureIntervals();
//		PDBStatistics.lookForBetaHairpins(2);
//		PDBStatistics.statisticsForChainTrees();
	}

	
}
