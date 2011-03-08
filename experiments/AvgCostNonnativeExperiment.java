package experiments;

import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Locale;

import geom3d.PointSet3d;
import misc.Toolbox;
import molecule.Protein;
import chainTree.BoundingVolumeManager;
import chainTree.ChainTree;
import chainTree.RMSD;
import static experiments.AvgCostExperiment.C_P;
import static experiments.AvgCostExperiment.C_V;
import static experiments.AvgCostExperiment.C_U;

public class AvgCostNonnativeExperiment {
	public static final int COST_MODEL = 0;
	public static final int CPUTIME_MODEL = 1;
	public static int MODEL = COST_MODEL;
	
	public static void main(String[] args){
		Locale.setDefault(Locale.ENGLISH);
		String[] pdbs = new String[]{"1X5RA","1X0OA","1XDXA","1AKPA","1Y6DA"};
		//		String[] pdbs = new String[]{"1CTFA","1LE2A","1HTBA","1JB0A"};
		//Each row-configuration specifies the following:
		// - If the table should be generated for ...
		//   á 0: both clashing and non-clashing structures
		//   á 1: only clashing structures
		//   á 2: only non-clashing structures
		// - If peptide bonds should be grouped
		// - If secondary structures should be grouped
		// - If the tree should be rebalanced after grouping
		Object[][] rowConfigs = {
				{0,false, false,false},
				{0,true,false,false},
				{0,false,true,false},
				{0,true,true,false},
				{0,false,true,true},
				{0,true,true,true},

				{1,false, false,false},
				{1,true,false,false},
				{1,false,true,false},
				{1,true,true,false},
				{1,false,true,true},
				{1,true,true,true},

				{2,false, false,false},
				{2,true,false,false},
				{2,false,true,false},
				{2,true,true,false},
				{2,false,true,true},
				{2,true,true,true},
		};
		int prevClashStatus = -1;
		for(Object[] rc: rowConfigs){
			if((Integer)rc[0]!=prevClashStatus) {
				printStatRow(pdbs,(Integer)rc[0]);
				prevClashStatus = (Integer)rc[0];
			}
			printRow(pdbs,rc);
		}
	}

	static void printStatRow(String[] pdbs, int clashStatus){
		//Print pdb-ids
		System.out.println("\\hline");
		for(String pdb: pdbs) System.out.print(" & "+pdb);
		System.out.println(" \\\\");
		
		//Print RMSD
		for(String pdb: pdbs){
			PrintStream out = System.out;
			System.setOut(new PrintStream(new OutputStream() {public void write(int b) {/*noop*/}}));
			ChainTree cTree = restoreStructure(pdb, BoundingVolumeManager.CAPSULE, true, true,true);
			System.setOut(out);
			System.out.printf(" & %.1f\\AA",RMSD.getRMSD(cTree));
		}
		System.out.println(" \\\\");
		
		//Print ratio of clashing/non-clashing
		
		if(clashStatus==0)
			System.out.print("%");
		for(String pdb: pdbs) {
			PrintStream out = System.out;
			System.setOut(new PrintStream(new OutputStream() {public void write(int b) {/*noop*/}}));
			double[] costSample = getCost(pdb, BoundingVolumeManager.CAPSULE, 0, true,true,true);
			System.setOut(out);
			switch(clashStatus){
			case 0: System.out.print(" & "+(int)costSample[4]+"c+"+((int)costSample[5]-(int)costSample[4])+"nc");break;
			case 1: System.out.print(" & "+(int)costSample[4]+"/"+(int)costSample[5]);break;
			case 2: System.out.print(" & "+((int)costSample[5]-(int)costSample[4])+"/"+(int)costSample[5]);break;
			}
		}
		System.out.println(" \\\\");

		System.out.println("\\hline\\hline");
	}


	static void printRow(String[] pdbs, Object[] rowConfig){
		//		System.out.printf("ClashStatus: %d. Group peptide bonds: %b. Group SS: %b. Rebalance after grouping: %b\n",rowConfig[0],rowConfig[1],rowConfig[2],rowConfig[3]);
		System.out.print(" & \\multicolumn{"+pdbs.length+"}{|c|}{");
		if((Boolean)rowConfig[1]) 	System.out.print("PP grouped, ");
		else						System.out.print("PP not grouped, ");
		if((Boolean)rowConfig[2]) 	System.out.print("SS locked ");
		else						System.out.print("SS not locked ");
		if((Boolean)rowConfig[3]) 	System.out.print("and grouped");
		else						System.out.print("");
		System.out.println("}  \\\\ \\hline %Clashstatus: "+rowConfig[0]);
		for(int vol = 0; vol<=3;vol+=1){
			double avgN_V = 0, avgN_U = 0, avgN_P = 0;
			System.out.printf("%s",volumeName(vol));
			for(String pdb: pdbs){
				PrintStream out = System.out;
				System.setOut(new PrintStream(new OutputStream() {public void write(int b) {/*noop*/}}));
				double[] cost = getCost(pdb, vol, (Integer)rowConfig[0], (Boolean)rowConfig[1], (Boolean)rowConfig[2], (Boolean)rowConfig[3] );
				avgN_V+=cost[1];
				avgN_U+=cost[2];
				avgN_P+=cost[3];
				System.setOut(out);
				switch(MODEL){
				case COST_MODEL: 	System.out.printf(" & %10.3f", cost[0]); break;
				case CPUTIME_MODEL:		System.out.printf(" & %10.3f", cost[6]); break;
				}
			}
			System.out.printf(" \\\\\t\t%% <N_V> = %d, <N_U> = %d, <N_P> = %d",
					(int)(avgN_V/pdbs.length),(int)(avgN_U/pdbs.length),(int)(avgN_P/pdbs.length));
			System.out.println();
		}
		System.out.println("\\hline");
	}
	static String volumeName(int v){
		switch(v){
		case BoundingVolumeManager.OBB: return "OBB";
		case BoundingVolumeManager.RSS: return "RSS";
		case BoundingVolumeManager.CAPSULE: return "LSS";
		case BoundingVolumeManager.PSS: return "PSS"; 
		}
		return "Unknown";
	}



	static void storeStructure(String pdbId){
		ChainTree cTree = MinimizeRMSDExperiment.minimizeRMSD(false, pdbId, BoundingVolumeManager.CAPSULE, true, true, true);

		StringBuilder dihedrals = new StringBuilder();
		for (int i = 1; i < cTree.nodes.length-1; i++) {
			dihedrals.append(cTree.getDihedralAngle(i));
			dihedrals.append('\n');
		}
		String file = "NonNative_"+pdbId+".txt";
		Toolbox.writeToFile(dihedrals.toString(), file, false);
		//		cTree.initPaint();
		//		if(true) throw new Error("STOP");
	}

	static ChainTree restoreStructure(String pdbId, int volume, boolean groupPeptide, boolean groupSS, boolean rebalance){
//		String file = "NonNative_"+pdbId+".txt";
//		String fContents = Toolbox.readFromFile(file);
//		if(fContents==null) return null;

		Protein protein = new Protein(pdbId, 2, true);

		PointSet3d allPoints = protein.getPointSet();
		PointSet3d points = new PointSet3d();
		for (int i = 0; i < allPoints.getSize(); i++) points.insert(allPoints.get(i));

		ChainTree cTree = new ChainTree(points, protein, groupPeptide);
		cTree.boundingMode = 3;
		cTree.volumeMode = volume;

		for (int i = 2; i < cTree.nodes.length; i = i+3) cTree.nodes[i].isLocked = true;
		cTree.nodes[cTree.nodes.length-1].isLocked = true;
		cTree.nodes[0].isLocked = true;
		if (groupSS) {
			cTree.lockAlphaHelices(true);
			cTree.lockBetaStrands(true);
		}
		
		if (rebalance) {
			cTree.removeLockedSubtrees(groupPeptide);
			cTree.newRebalanceSubtree(cTree.root);
			cTree.addLockedSubtrees(groupPeptide);
		}



		
		cTree.createBoundingVolume(cTree.root);
		cTree.createEnergyBoundingVolume(cTree.root);
//		cTree.restoreHeights();

		cTree.setFixedDihedralAngles(Math.PI);
		
//		double[] dihedrals = new double[cTree.nodes.length-2];
//		String[] dihedralStrings = fContents.split("\n");
//		if(dihedrals.length!=dihedralStrings.length) throw new Error("File contents does not match structure");
//		for(int c=0;c<dihedrals.length;c++) dihedrals[c] = Double.parseDouble(dihedralStrings[c]);

//		for (int i = 1; i < cTree.nodes.length-1; i++) 
//			cTree.changeRotationAngle(i, dihedrals[i-1] - cTree.getDihedralAngle(i));
		//		cTree.setRandomDihedralAngles()
		return cTree;
	}

	static double[] getCost(String pdbId, int volume, int clashStatus, boolean groupPeptide, boolean groupSS, boolean rebalance){
		if(C_V==null) AvgCostExperiment.setupCostsWithTransform();

		Protein protein = new Protein(pdbId, 2, true);

		PointSet3d allPoints = protein.getPointSet();
		PointSet3d points = new PointSet3d();
		for (int i = 0; i < allPoints.getSize(); i++) points.insert(allPoints.get(i));

		ChainTree cTree = restoreStructure(pdbId, volume, groupPeptide, groupSS, rebalance);
		if(cTree==null) {
			storeStructure(pdbId);
			cTree = restoreStructure(pdbId, volume, groupPeptide, groupSS, rebalance);
		}

		//		cTree.initPaint();
		//		if(true) throw new Error("stop");

		//N_V = number of BV pairs tested for overlap
		//N_P = number of primitive pairs tested
		//N_U = number of nodes that is updated
		double N_V_Sum = 0, N_P_Sum = 0, N_U_Sum = 0;
		int N_V_Count = 0, N_P_Count = 0, N_U_Count = 0;
		long totalNanos = 0;
		int clashCount = 0,count=0;
		int sinceLastImprovement = 0;
//		for(int a=-20;a<20;a+=4){
		while (sinceLastImprovement < 100) {
			int a = 4;
			if(a==0) continue;

			for (int i = 0; i < cTree.nodes.length; i++) {
				if (!cTree.nodes[i].isLocked) {

					cTree.clashVolumeOverlapCount = 0;
					cTree.primitiveOverlapCount = 0;
					cTree.clashVolumeUpdateCount = 0;
					long start = getUserTime();//System.nanoTime();
					cTree.changeRotationAngle(i, a*Math.PI/180.0);

					boolean clashes = cTree.isClashing(i);
					long end = getUserTime();//System.nanoTime();
					
					if(clashes) clashCount++;
					count++;

					if(clashStatus==0||(clashStatus==1&&clashes)||(clashStatus==2&&!clashes)){
						totalNanos+=(end-start);
						N_V_Sum+=cTree.clashVolumeOverlapCount;
						N_P_Sum+=cTree.primitiveOverlapCount;
						N_U_Sum+=cTree.clashVolumeUpdateCount;
						N_V_Count++;
						N_P_Count++;
						N_U_Count++;
					}
//					cTree.changeRotationAngle(i, -a*Math.PI/180.0);
					if ((clashStatus != 0) && clashes) {
						cTree.changeRotationAngle(i, -a*Math.PI/180.0);
						sinceLastImprovement = 0;
					}
					else sinceLastImprovement++;
				}
			}
		}

		//		System.err.println(clashCount+" of "+count);
		double N_V = (N_V_Sum/N_V_Count);
		double N_P = (N_P_Sum/N_P_Count);
		double N_U = (N_U_Sum/N_U_Count); 

		//Calculate cost
		double T = N_V*C_V[volume] + N_P*C_P + N_U*C_U[volume];
		return new double[]{T,N_V,N_U,N_P,clashCount, count, totalNanos/(count*1000000.0)};
	}


	public static long getUserTime( ) {
	    ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
	    return bean.isCurrentThreadCpuTimeSupported()?bean.getCurrentThreadUserTime():0L;
	}

}
