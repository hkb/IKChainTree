package experiments;

import java.util.ArrayList;
import java.util.List;

import geom3d.PointSet3d;
import molecule.Protein;
import chainTree.BoundingVolumeManager;
import chainTree.ChainTree;

public class VolumeExperiment {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		genTable("Table 2", BoundingVolumeManager.CAPSULE, 0);
//		genTable("Table 3", BoundingVolumeManager.CAPSULE, 1);
//		genTable("Table 4", BoundingVolumeManager.CAPSULE, 3);
	}

	private static void genTable(String name, int volume, int boundingMode){
		String[] pdbs = new String[]{"1X5RA","1X0OA","1XDXA","1AKPA","1Y6DA"};
		
		//Table 2. Total and root vol of clash capsules.
		List<double[]> rows = new ArrayList<double[]>();
		boolean groupSS = true;
		boolean rebalance = true;
		for(String pdb: pdbs){
			rows.add(runOnProtein(pdb,volume, boundingMode, groupSS, rebalance));
		}
		System.out.println(name);
		for(int i=0;i<pdbs.length;i++){
			double[] vols = rows.get(i);
			System.out.printf("%s & %.0f & %.0f & %.0f & %.0f\\\\\n",pdbs[i],vols[0], vols[2], vols[1], vols[3]);
		}
	}
	private static double[] runOnProtein(String pdb, int volumeMode, int boundingMode, boolean groupSS, boolean rebalance){
		double[] ret = new double[4];
		
		Protein protein = new Protein(pdb, 2, true);
		
		PointSet3d allPoints = protein.getPointSet();
		PointSet3d points = new PointSet3d();
		for (int i = 0; i < allPoints.getSize(); i++) points.insert(allPoints.get(i));

		ChainTree cTree = new ChainTree(points, protein, true);
		cTree.boundingMode = boundingMode;
		cTree.volumeMode = volumeMode;
		
		cTree.createBoundingVolume(cTree.root);
		ret[0] = cTree.getTotalCapsulesVolume();
		ret[1] = cTree.getRootCapsuleVolume();
		
		cTree = new ChainTree(points, protein, true);
		cTree.boundingMode = boundingMode;
		cTree.volumeMode = volumeMode;
		
		
		for (int i = 2; i < cTree.nodes.length; i = i+3) cTree.nodes[i].isLocked = true;
		cTree.nodes[cTree.nodes.length-1].isLocked = true;
		cTree.nodes[0].isLocked = true;
		cTree.lockAlphaHelices(groupSS, rebalance);
		cTree.lockBetaStrands(groupSS, rebalance);
		
		cTree.createBoundingVolume(cTree.root);
//		System.out.println("Total volume of volumes is " +  cTree.getTotalCapsulesVolume()+", root volume is "+cTree.getRootCapsuleVolume());
//		cTree.createEnergyBoundingVolume(cTree.root);
		ret[2] = cTree.getTotalCapsulesVolume(); 
		ret[3] = cTree.getRootCapsuleVolume();
		
		return ret;
	
	}

}
