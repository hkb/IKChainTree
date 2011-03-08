package molecule;

import java.io.*;

public class PDBSelect25 {
	BufferedReader br;
	String record = null;
	String prefix = "/Volumes/Home/pawel/Documents/workspace/proteins/src/protein/";	
	
	public PDBSelect25() {
		try {
			br = new BufferedReader(new FileReader("pdb_select_25_4018"));
			br.readLine(); br.readLine(); br.readLine();
		} catch (IOException e) {	System.out.println("IOException error!"); e.printStackTrace(); }
	}
	
	public String getNextRecord() { 
		try {
			record = br.readLine();
		} catch (IOException e) {	System.out.println("IOException error!"); e.printStackTrace(); }
		return record;
	}
	
	/*
	 * Looks for the chain id of a specified PDB Select25 file.
	 */
	public static String getChainID(String filename) {
		String record = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader("pdb_select_25_4018"));
			br.readLine(); br.readLine(); br.readLine();
			while (((record = br.readLine()) != null) && !record.substring(8,12).equals(filename)) {}	
		} catch (IOException e) { System.out.println("IOException error!"); e.printStackTrace(); } 
		if (record != null) return record.substring(12,13); else return null;
	}
	
	public void betaSheetsPDBSelect25(String inFile) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(inFile));
			String record = new String();
			record = br.readLine(); record = br.readLine(); record = br.readLine();
		
//			BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
			Protein p;
			String name;
			int count = 0;
			
			while ((record = br.readLine()) != null) {
				count++;
				name = record.substring(8,12);
				System.out.print(count + ". Processing protein " + name);
				//Edited by Rasmus
				p = new Protein(name, 1, true);
				//p = new Protein(name, "Ca", true);
				p.clear(); 
				System.out.println();
			}
		} catch (IOException e) {	System.out.println("IOException error!"); e.printStackTrace(); }

	}
	
	/*
	 * reads (subset of) pdb_select25. Creates a new file within each protein represented as a 1-letter sequence
	 */
	public void longPDBSelect25(String inFile,String outFile) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(inFile));
			String record = new String();
			record = br.readLine(); record = br.readLine(); record = br.readLine();
		
			BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
			Protein p;
			String name;
			
			while ((record = br.readLine()) != null) {
				name = record.substring(8,12);
				BufferedReader brPDBFile = new BufferedReader(new FileReader(prefix + name + ".pdb"));
//				p = new Protein(name);
//				p.get1LetterCoils(bw);
//				p.clear();
				brPDBFile.close();
			}
			bw.close();
		}
		catch (IOException e) {	System.out.println("IOException error!"); e.printStackTrace(); }
	}
	
	/*
	 * reads (subset of) pdb_select25. Creates a new file with each protein represented as a sequence xyz-coordinates of its C-alpha atoms
	 */
	public void PDBSelect25ToCalphas(String inFile,String outFile) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(inFile));
			String record = new String();
			record = br.readLine(); record = br.readLine(); record = br.readLine();
		
			BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
			Protein p;
			String name;
			
			while ((record = br.readLine()) != null) {
				name = record.substring(8,12);
				bw.write("   " + name); bw.write('\n');
				BufferedReader brPDBFile = new BufferedReader(new FileReader(prefix + name + ".pdb"));
//				p = new Protein(name);
//				p.getCalphas(bw);
//				p.clear();
				brPDBFile.close();
			}
			bw.close();
		}
		catch (IOException e) {	System.out.println("IOException error!"); e.printStackTrace(); }
	}

	/*
	 * reads (subset of) pdb_select25. Creates a new file with distances between i-th and i+delta C-alpha
	 */
	public void PDBSelect25ToCalphasDist(String inFile,String outFile) {
		try {
			int[] distFreq = new int[1001];
			
			BufferedReader br = new BufferedReader(new FileReader(inFile));
			String record = new String();
			record = br.readLine(); record = br.readLine(); record = br.readLine();
		
			BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
			String name;
			
			while ((record = br.readLine()) != null) {
				name = record.substring(8,12);
				bw.write("   " + name); bw.write('\n');
				BufferedReader brPDBFile = new BufferedReader(new FileReader(prefix + name + ".pdb"));
//				p = new Protein(name);
//				p.getDistances(4,distFreq,bw);
//				p.clear();
				brPDBFile.close();
			}
			
			bw.write('\n');
			for (int i=0; i < 1000; i++) 
				if (distFreq[i] != 0) bw.write(i + ": " + distFreq[i] + '\n');

			bw.close();
		}
		catch (IOException e) {	System.out.println("IOException error!"); e.printStackTrace(); }
	}

	
	
	/*
	 * reads (subset of) pdb_select25 file and creates corresponding file with the mnemonic names of proteins
	 */
	public void shortPDBSelect25(String inFile, String outFile) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(inFile));
			BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
			String record;
			record = br.readLine();
			record = br.readLine();
			record = br.readLine();

			while ((record = br.readLine()) != null) { bw.write(record.substring(8,12)); bw.write('\n'); }
			bw.close();
		}	
		catch (IOException e) { System.out.println("IOException error!"); e.printStackTrace();  }
	}

    public void getResidueIntervals() {
    	try {
    		Protein p;
    		String name, record25;
    		PDBSelect25 pdbSelect25 = new PDBSelect25();
    		int count = 0;

    		while ((record25 = pdbSelect25.getNextRecord()) != null) {
    			name = record25.substring(8,13);
    			if (!name.equals("-----")) { 	
    				System.out.print(++count + ". " + name + ": ");
    				String fileName = name.substring(0,4);
    				String source = PDB.getFilePath(fileName);
       				br = new BufferedReader(new FileReader(source));
    				//Edited by Rasmus
       				p = new Protein(name, 1, true);
       				//p = new Protein(name, "Ca", true);
       				int size = p.emptyIntervals.getSize();
       				for (int i = 0; i < size; i = i+2)
       					System.out.print("]" + (Integer)p.emptyIntervals.get(i) + "," + (Integer)p.emptyIntervals.get(i+1) + "[ ");
       				System.out.println();
       				p.clear();
				}
			}
    	} catch (IOException e) { System.out.print(": IOException error!"); }
    }

	
	public static void main(String[] args) 
	{
		String prefix = "/Volumes/Home/pawel/Documents/MotherOfAllProjects/";	
		PDBSelect25 p = new PDBSelect25();
//		p.betaSheetsPDBSelect25(prefix + "pdb_select25");
//		p.longPDBSelect25(prefix+"test_pdb_select25.txt",prefix+"pdb_select25coil");
//		p.PDBSelect25ToCalphasDist(prefix+"test_pdb_select25.txt", prefix+"test");
		p.getResidueIntervals();
//		p.shortPDBSelect25(prefix+"pdb_select_25_4018",prefix+"test");
	}

}

