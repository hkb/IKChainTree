package experiments;

import j3dScene.J3DScene;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import geom3d.PointSet3d;
import misc.Toolbox;
import molecule.Protein;
import chainTree.BoundingVolumeManager;
import chainTree.ChainTree;
import chainTree.RMSD;

public class MinimizeRMSDExperiment {

	public static void main(String[] args){
		Locale.setDefault(Locale.ENGLISH);
		//Each row-configuration specifies the following:
		// - If peptide bonds should be grouped
		// - If secondary structures should be grouped
		// - If the tree should be rebalanced after grouping
		boolean[][] chaintreeConfigs = {
				{false, false,false},
				//				{true,false,false},
				//				{false,true,false},
				//				{true,true,false},
				//				{false, false,true},//Makes no sense to rebalance when SS is not grouped
				//				{true,false,true},//Makes no sense to rebalance when SS is not grouped
				//				{false,true,true},
				{true,true,true},
		};
		for(boolean[] rc: chaintreeConfigs){
			minimizeRMSD(rc);
		}
	}
	static void minimizeRMSD(boolean[] chaintreeConfig){
		//		String[] pdbs = new String[]{"1X5RA","1X0OA","1XDXA","1AKPA","1Y6DA"};
		String[] pdbs = new String[]{"1X5RA"};
		System.out.printf("Group peptide bonds: %b. Group SS: %b. Rebalance after grouping: %b\n",chaintreeConfig[0],chaintreeConfig[1],chaintreeConfig[2]);
		for(int vol = 0; vol<=3;vol+=1){
			System.out.printf("%s\n",volumeName(vol));
			for(String pdb: pdbs){
				minimizeRMSD(true,pdb, vol, chaintreeConfig[0], chaintreeConfig[1], chaintreeConfig[2]);
			}
		}
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


	static ChainTree minimizeRMSD(boolean log, String pdbId, int volume, boolean groupPeptide, boolean groupSS, boolean rebalance){
		double[] distances;

		if(C_V==null) setupCosts();
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
		cTree.lockAlphaHelices(groupSS);
		cTree.lockBetaStrands(groupSS);

		if (rebalance) {
			cTree.removeLockedSubtrees(groupPeptide);
			cTree.newRebalanceSubtree(cTree.root);
			cTree.addLockedSubtrees(groupPeptide);
		}

		
		cTree.createBoundingVolume(cTree.root);
		cTree.createEnergyBoundingVolume(cTree.root);

		
// THIS FOR-LOOP WAS CORRECTED BY PW
		for (int i = 1; i < cTree.nodes.length-1; i++) {
			if (cTree.nodes[i].isLocked) { // there is no reason to rotate by 0
//				cTree.changeRotationAngle(i, cTree.getDihedralAngle(i)- cTree.proteinDihedralAngles[i]);
//				System.out.println(i + ": " + cTree.getDihedralAngle(i));
			}
			else {
				cTree.changeRotationAngle(i, -cTree.getDihedralAngle(i));
//				cTree.changeRotationAngle(i, cTree.getDihedralAngle(i)- Math.PI);   // why -Math.PI ?
//				System.out.println(i + ": " + cTree.getDihedralAngle(i));
			}
		}
		cTree.j3dg = J3DScene.createJ3DSceneInFrame();
		cTree.initPaint();

		
		String logFileName = "MinRMSD_"+pdbId+"_"+volumeName(volume);
		if(groupPeptide) logFileName+="_groupPeptide";
		if(groupSS) logFileName+="_groupSS";
		if(rebalance) logFileName+="_rebalance";
		logFileName+=".txt";

		StringBuilder sb = new StringBuilder();
		int it = 0, iit = 0, itSinceImprove=0;
		double avgCost = 0;
		int avgOver = 10, avgCount = 0;

		int size = cTree.nodes.length;
		cTree.getDistanceMatrix();
//		System.out.println(); System.out.println(cTree.distances[0]); System.out.println(cTree.distances[1]);
		double rmsd = cTree.getPairwiseDistanceRMSD();
//		double rmsd = RMSD.getRMSD(cTree);//cTree.getDihedralAnglesRMSD(size);

		//Start MC minimization of RMSD
		int b, N_V, N_P, N_U;
		double angle, cost, newRmsd;
		boolean clashing;
		while(itSinceImprove<200){
			it++;
//			System.out.println(it);

			//Choose a bond: b
			b = (int)(Math.random()*size);
			while(cTree.nodes[b].isLocked) b = (int)(Math.random()*size);
//			System.out.println("rotating bond " + b);
			
			//Rotate it randomly
			angle = (Math.random()-0.5) * 15 * (Math.PI/180);
			cTree.clashVolumeOverlapCount = 0;
			cTree.primitiveOverlapCount = 0;
			cTree.clashVolumeUpdateCount = 0;
			cTree.changeRotationAngle(b, angle);
			clashing = cTree.isClashing(b);
//			clashing = false;
			N_V = cTree.clashVolumeOverlapCount;
			N_P = cTree.primitiveOverlapCount;
			N_U = cTree.clashVolumeUpdateCount;
			cost = N_V*C_V[volume]+N_U*C_U[volume]+N_P*C_P;
			
//	new rmsd is only needed when new structure is not clashing - huge saving
//			newRmsd = RMSD.getRMSD(cTree);
//			newRmsd = cTree.getPairwiseDistanceRMSD(); // replaced by PW; problems with RMSD.getRSMD(cTree)

//			if(log){
//				//if(it%100==0){//log
//				if(avgCount==avgOver){
//					sb.append(String.format("%d it: %d iit: %d rmsd: %.3f cost: %.3f clashing: %d\n",
//							//						System.currentTimeMillis(),it,iit,rmsd,cost,clashing?1:0,newRmsd<rmsd?1:0));
//							System.currentTimeMillis(),it,iit,rmsd,avgCost/avgOver,clashing?1:0));
//
//					avgCount=0;
//					avgCost=0;
//				}
//				avgCost+=cost;
//				avgCount++;
//			}

			if(it%1000==0){//Write to console
				System.err.printf("%s %s %s %d it: %d iit: %d rmsd: %.3f cost: %.3f clashing: %d\n",
						pdbId,volumeName(volume),now("hh:mm:ss:SSSS"),System.currentTimeMillis(),it,iit,rmsd,cost,clashing?1:0);
			}

			
			if(log && it%10000==0){//Write log to file
				Toolbox.writeToFile(sb.toString(),logFileName,false);
			}

			if(clashing){
//				System.out.println("clashing rotation");
				cTree.changeRotationAngle(b, -angle);
				itSinceImprove++;
			}else{
				newRmsd = cTree.getPairwiseDistanceRMSD();
				//Check for improvement
				if(newRmsd<rmsd){
					iit++;//Improving iteration
					rmsd = newRmsd;
					itSinceImprove = 0;
				}else{
					cTree.changeRotationAngle(b, -angle);
					itSinceImprove++;
				}
			}
		}
		//		cTree.initPaint();
		if(log){
			Toolbox.writeToFile(sb.toString(),logFileName,false);
			System.out.println("Simulation done. Wrote log to "+logFileName);
		}
		return cTree;
	}

	public static String now(String dateFormat) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(cal.getTime());

	}

	public static double[] C_V;
	public static double[] C_U;
	public static double C_P;
	static void setupCosts(){
		C_P = 0.0002;//4

		C_V = new double[3];
		C_V[BoundingVolumeManager.CAPSULE] 	= 0.00075;//31.0;
		C_V[BoundingVolumeManager.RSS] 		= 0.0035;//20.0;
		C_V[BoundingVolumeManager.OBB] 		= 0.0025;//117.0;

		C_U = new double[3];
		C_U[BoundingVolumeManager.CAPSULE] 	= 0.0045;//100.0;
		C_U[BoundingVolumeManager.RSS] 		= 0.020;//200.0;
		C_U[BoundingVolumeManager.OBB] 		= 0.020;//200.0;

	}
}
