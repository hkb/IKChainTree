package experiments;

import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.management.*;
import java.util.Locale;

import geom3d.PointSet3d;
import molecule.Protein;
import chainTree.BoundingVolumeManager;
import chainTree.ChainTree;
import chainTree.ChainTreePainter;

public class AvgCostExperiment {
	public static final int COST_MODEL = 0;
	public static final int CPUTIME_MODEL = 1;
	public static int MODEL = COST_MODEL;
	public static final double degree = Math.PI/180;

	public static void main(String[] args){
		Locale.setDefault(Locale.ENGLISH);
//		String[] pdbs = new String[]{"1Y6DA"};
		String[] pdbs = new String[]{"1X5RA","1X0OA","1XDXA","1AKPA","1Y6DA"};
//		String[] pdbs = new String[]{"1Y6DA"};
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
//				{0,false, false,false},
//				{0,true,false,false},
//				{0,false,true,false},
//				{0,true,true,false},
//				{0,false,true,true},
//				{0,true,true,true},

//				{1,false, false,false},
//				{1,true,false,false},
//				{1,false,true,false},
//				{1,true,true,false},
//				{1,false,true,true},
//				{1,true,true,true},
//
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
		
		System.out.println("\\hline");
		for(String pdb: pdbs) System.out.print(" & "+pdb);
		System.out.println(" \\\\");
		if(clashStatus==0)
			System.out.print("%");
		for(String pdb: pdbs) {
			PrintStream out = System.out;
			System.setOut(new PrintStream(new OutputStream() {public void write(int b) {/*noop*/}}));
			double[] costSample = getCost(pdb, BoundingVolumeManager.CAPSULE, 0, true,true,true);
			System.setOut(out);
//			System.out.print(" & "+(int)costSample[4]+"c+"+(int)costSample[5]+"nc="+((int)costSample[4]+(int)costSample[5]));
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
		System.out.print(" & \\multicolumn{"+pdbs.length+"}{|c|}{");
		if((Boolean)rowConfig[1]) 	System.out.print("PP grouped, ");
		else						System.out.print("PP not grouped, ");
		if((Boolean)rowConfig[2]) 	System.out.print("SS locked ");
		else						System.out.print("SS not locked ");
		if((Boolean)rowConfig[3]) 	System.out.print("and rebalanced");
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



	static double[] getCost(String pdbId, int volume, int clashStatus, boolean groupPeptide, boolean groupSS, boolean rebalance){
		if(C_V==null) setupCostsWithTransform();

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
		
		for (int i = 1; i < cTree.nodes.length-1; i++) 
			cTree.changeRotationAngle(i, 
					cTree.proteinDihedralAngles[i] -
					cTree.getDihedralAngle(i));

		//		cTree.initPaint();
		//N_V = number of BV pairs tested for overlap
		//N_P = number of primitive pairs tested
		//N_U = number of nodes that is updated
		double N_V_Sum = 0, N_P_Sum = 0, N_U_Sum = 0;
		int N_V_Count = 0, N_P_Count = 0, N_U_Count = 0;
		long totalNanos = 0;
		int clashCount = 0,count=0;
		for(int a=-20;a<20;a+=4){
			if(a==0) continue;
			
			for (int i = 0; i < cTree.nodes.length; i++) {
				if (!cTree.nodes[i].isLocked) {

					cTree.clashVolumeOverlapCount = 0;
					cTree.primitiveOverlapCount = 0;
					cTree.clashVolumeUpdateCount = 0;
					long start = System.nanoTime();  // getUserTime();
					ChainTree.warn = true;
					cTree.changeRotationAngle(i, a*degree);	//4.22ms
					ChainTree.warn = false;
					boolean clashes = cTree.isClashing(i);			//0.2ms
					long end = System.nanoTime(); // getUserTime();
					if (end == start) System.err.println("zero time");
//					System.err.println("changeRotation: "+(middle-start));
//					System.err.println("isClashing:     "+(end-middle));
					if(clashes) clashCount++;
					count++;

					if(clashStatus==0||(clashStatus==1&&clashes)||(clashStatus==2&&!clashes)){
						N_V_Sum+=cTree.clashVolumeOverlapCount;
						N_P_Sum+=cTree.primitiveOverlapCount;
						N_U_Sum+=cTree.clashVolumeUpdateCount;
						N_V_Count++;
						N_P_Count++;
						N_U_Count++;
						totalNanos+=(end-start);
//						System.err.println("Took "+(end-start)/1000+"ms");
					}
					cTree.changeRotationAngle(i, -a*degree);
				}
			}
		}
//		System.err.println("first: "+(ChainTree.times[0]/ChainTree.count));
//		System.err.println("second: "+(ChainTree.times[1]/ChainTree.count));
//		System.err.println(clashCount+" of "+count);
		double N_V = (N_V_Sum/N_V_Count);
		double N_P = (N_P_Sum/N_P_Count);
		double N_U = (N_U_Sum/N_U_Count); 

		//Calculate cost
		double T = N_V*C_V[volume] + N_P*C_P + N_U*C_U[volume];
		return new double[]{T,N_V,N_U,N_P, clashCount, count, totalNanos/(count*1000000.0)};
		
	}


	public static double[] C_V;
	public static double[] C_U;
	public static double C_P;
	static void setupCostsNoTransform(){
		C_P = 0.000041;//0.0002;//4

		C_V = new double[4];
		C_V[BoundingVolumeManager.OBB] 		= 0.00094;
		C_V[BoundingVolumeManager.RSS] 		= 0.0018;
		C_V[BoundingVolumeManager.CAPSULE] 	= 0.00058;
		C_V[BoundingVolumeManager.PSS] 		= 0.00014;

		C_U = new double[4];
		C_U[BoundingVolumeManager.OBB] 		= 0.0079;
		C_U[BoundingVolumeManager.RSS] 		= 0.0074;
		C_U[BoundingVolumeManager.CAPSULE] 	= 0.0020;
		C_U[BoundingVolumeManager.PSS] 		= 0.00016;
	}
	static void setupCostsWithTransform(){
		C_P = 0.00030;

		C_V = new double[4];
		C_V[BoundingVolumeManager.OBB] 		= 0.0022;
		C_V[BoundingVolumeManager.RSS] 		= 0.0029;
		C_V[BoundingVolumeManager.CAPSULE] 	= 0.0012;
		C_V[BoundingVolumeManager.PSS] 		= 0.00069;

		C_U = new double[4];
		C_U[BoundingVolumeManager.OBB] 		= 0.0084;
		C_U[BoundingVolumeManager.RSS] 		= 0.0082;
		C_U[BoundingVolumeManager.CAPSULE] 	= 0.0022;
		C_U[BoundingVolumeManager.PSS] 		= 0.00039;
	}
	static void setupHOPCosts(){
			C_P = 4;

			C_V = new double[4];
			C_V[BoundingVolumeManager.CAPSULE] 	= 33.0;
			C_V[BoundingVolumeManager.RSS] 		= 300;
			C_V[BoundingVolumeManager.OBB] 		= 73;
			C_V[BoundingVolumeManager.PSS] 		= 4;

			C_U = new double[4];
			C_U[BoundingVolumeManager.CAPSULE] 	= 229;
			C_U[BoundingVolumeManager.RSS] 		= 0.0081;
			C_U[BoundingVolumeManager.OBB] 		= 458;
			C_U[BoundingVolumeManager.PSS] 		= 28;
	}
	
	public static long getUserTime( ) {
	    ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
	    return bean.isCurrentThreadCpuTimeSupported()?bean.getCurrentThreadUserTime():0L;
	}
}
