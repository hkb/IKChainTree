package experiments;

import java.util.ArrayList;
import java.util.List;

import geom3d.PointSet3d;
import j3dScene.J3DScene;
import molecule.Protein;
import chainTree.BoundingVolumeManager;
import chainTree.ChainTree;
import chainTree.ChainTreeStatistics;

public class ClashExperiment {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		genTable6(BoundingVolumeManager.CAPSULE, 3);
	}

	private static void genTable5(int volume, int boundingMode){
		String[] pdbs = new String[]{"1X5RA","1X0OA","1XDXA","1AKPA","1Y6DA"};
		
		//Table 2. Total and root vol of clash capsules.
		List<int[][]> rows = new ArrayList<int[][]>();
		boolean groupSS = true;
		boolean rebalance = true;
		for(String pdb: pdbs){
			rows.add(runOnProtein(pdb,volume, boundingMode, groupSS, rebalance));
		}
		System.out.println("Table 5");
		for(int i=0;i<pdbs.length;i++){
			int[][] clashStats = rows.get(i);
			System.out.printf("%s & %d & %d\\\\ \n",
					pdbs[i],clashStats[1][0], clashStats[1][2]);
		}
	}
	private static void genTable6(int volume, int boundingMode){
		String[] pdbs = new String[]{"1X5RA","1Y6DA","1XDXA","1AKPA","1Y6DA"};
		
		//Table 2. Total and root vol of clash capsules.
		List<int[][]> rows = new ArrayList<int[][]>();
		boolean groupSS = false;
		boolean rebalance = false;
		for(String pdb: pdbs){
			rows.add(runOnProtein(pdb,volume, boundingMode, groupSS, rebalance));
		}
		System.out.println("Table 6");
		for(int i=0;i<pdbs.length;i++){
			int[][] clashStats = rows.get(i);
			System.out.printf("%s & %d & %.0f & %d & %.0f\\\\ \n",
					pdbs[i],clashStats[0][3], clashStats[0][3]*1f/clashStats[0][2],clashStats[1][3], clashStats[1][3]*1f/clashStats[1][2]);
		}
	}
	private static int[][] runOnProtein(String pdb, int volumeMode, int boundingMode, boolean groupSS, boolean rebalance){
		int[][] ret = new int[2][];
		
		Protein protein = new Protein(pdb, 2, true);
		
		PointSet3d allPoints = protein.getPointSet();
		PointSet3d points = new PointSet3d();
		for (int i = 0; i < allPoints.getSize(); i++) points.insert(allPoints.get(i));

		ChainTree cTree = new ChainTree(points, protein, true);
		cTree.boundingMode = boundingMode;
		cTree.volumeMode = volumeMode;

		for (int i = 2; i < cTree.nodes.length; i = i+3) cTree.nodes[i].isLocked = true;
		cTree.nodes[cTree.nodes.length-1].isLocked = true;
		cTree.nodes[0].isLocked = true;
		
		cTree.createBoundingVolume(cTree.root);
		cTree.createEnergyBoundingVolume(cTree.root);
		
		ret[0] = getRotationStatistics(cTree);
		
		cTree = new ChainTree(points, protein, true);
		cTree.boundingMode = boundingMode;
		cTree.volumeMode = volumeMode;
		
		
		for (int i = 2; i < cTree.nodes.length; i = i+3) cTree.nodes[i].isLocked = true;
		cTree.nodes[cTree.nodes.length-1].isLocked = true;
		cTree.nodes[0].isLocked = true;
		cTree.lockAlphaHelices(groupSS,rebalance);
		cTree.lockBetaStrands(groupSS,rebalance);
		
		cTree.createBoundingVolume(cTree.root);
		cTree.createEnergyBoundingVolume(cTree.root);
		
		ret[1] = getRotationStatistics(cTree);
//		System.out.println("Total volume of volumes is " +  cTree.getTotalCapsulesVolume()+", root volume is "+cTree.getRootCapsuleVolume());
//		cTree.createEnergyBoundingVolume(cTree.root);
		
		return ret;
	
	}
	
	public static int[] getRotationStatistics(ChainTree cTree) {
		int numberClashingStructures = 0;
		int numberNonClashingStructures = 0;
		int totalCountClashingStructures = 0;
		int totalCountNonClashingStructures = 0;
		int totalCountEnergy = 0;
		for (int i = 0; i < cTree.nodes.length; i++) {
			if (!cTree.nodes[i].isLocked) {
				for (int j = 0; j < 10; j++) {				
					cTree.changeRotationAngle(i, Math.PI/180.0);
					cTree.clashVolumeOverlapCount = 0;
					cTree.energyVolumeOverlapCount = 0;
					if (cTree.isClashing(i)) { 
						numberClashingStructures++;
						totalCountClashingStructures += cTree.clashVolumeOverlapCount;
//						System.out.println("clash, #checks: " + cTree.globalCount);
					}
					else {
						numberNonClashingStructures++;
						totalCountNonClashingStructures += cTree.clashVolumeOverlapCount;
						cTree.getCrossEnergy(i);
						totalCountEnergy += cTree.energyVolumeOverlapCount;
//						System.out.println("no clash, #checks: " + cTree.globalCount);
//						System.out.println("energy,   #checks: " + cTree.globalCountEnergy);
					}
				}
			}
			i++;//TODO: What??????????
		}
//		System.out.println("Number of clashing structures is     " + numberClashingStructures);
//		System.out.println("Number of capsule intersection tests in clashing structures i " + totalCountClashingStructures);
//		System.out.println("Number of non-clashing structures is " + numberNonClashingStructures);
//		System.out.println("Number of capsule intersection tests in non-clashing structures i " + totalCountNonClashingStructures);
//		System.out.println("Number of energy capsule intersection tests in non-clashing structures is " + totalCountEnergy);
		return new int[]{
				numberClashingStructures, 
				totalCountClashingStructures, 
				numberNonClashingStructures,
				totalCountNonClashingStructures,
				totalCountEnergy
				};
	}

}
