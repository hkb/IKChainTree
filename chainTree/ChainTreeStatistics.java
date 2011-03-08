package chainTree;

import dataStructures.Stack;

public class ChainTreeStatistics {

	public static void getHeightDistribution(ChainTree chainTree) {
		int[] heights = new int[chainTree.nodes.length];
		CTNode nd;
		Stack stack = new Stack();
		stack.push(chainTree.getRoot());
		while (!stack.isEmpty()) {
			nd = (CTNode)stack.pop();
			heights[nd.height]++;
			if (!nd.isLeaf()) {
				if (nd.right != null) stack.push(nd.right);
				if (nd.left  != null) stack.push(nd.left);
			}
		}
		System.out.print("heights of the chain tree nodes: ");
		for (int i = 0; i <= chainTree.getRoot().height; i++) System.out.print(i + ":" + heights[i] + ", ");
		System.out.println();
	}
	
	
	public static void getDepthDistribution(ChainTree chainTree) {
		Integer d;
		int c = 0;
		CTNode nd = chainTree.getRoot();
		int h = nd.height;
		int[] depth = new int[h+1];
		int[] depthLeaf = new int[h+1];
		Stack stack = new Stack();
		stack.push(nd);
		stack.push(0);
		while (!stack.isEmpty()) {
			d = (Integer)stack.pop();
			nd = (CTNode)stack.pop();
			depth[d]++;
			if (nd.isLeaf()) depthLeaf[d]++;
			else {
				if (nd.right != null) { stack.push(nd.right); stack.push(d+1); }
				if (nd.left  != null) { stack.push(nd.left);  stack.push(d+1); }
			}
		}
		System.out.print("depths of the chain tree nodes: ");
		for (int i = 0; i <= h; i++) System.out.print(i + ":" + depth[i] + ", ");
		System.out.println();
		System.out.print("depths of the chain tree leaves: ");
		for (int i = 0; i <= h; i++) {
			System.out.print(i + ":" + depthLeaf[i] + ", ");
			c = c + depthLeaf[i]*i;
		}
		System.out.println();
		System.out.println("Cost of the tree is " + c);
	}
	
	public static void getBoundingVolumesStatistics(ChainTree chainTree) {
		CTNode nd = chainTree.getRoot();
		int h = nd.height;
		int hNodes[] = new int[h+1];
		double accVolume[] = new double[h+1];
		Stack stack = new Stack();
		stack.push(chainTree.getRoot());
		while (!stack.isEmpty()) {
			nd = (CTNode)stack.pop();
			hNodes[nd.height]++;
			accVolume[nd.height] += nd.volume.volume()/(nd.high-nd.low+1);
			if (nd.right != null) stack.push(nd.right); 
			if (nd.left  != null) stack.push(nd.left); 
		}
		System.out.println("Average volume pr. binding");
		for (int i = 0; i <= h; i++) {
			System.out.println("height = " + i + ": " + (accVolume[i]/hNodes[i]));
		}
	}
	
	/*
	 * each unlocked bound is ratated by 1, 2, 3, ..., 10 degrees. Number of clashing and non-clashing structures is 
	 * determined. For each clashing and non-clashing structure, the number of clash tests is determined and added to the
	 * total count of clash tests
	 */
	public static void getRotationStatistics(ChainTree cTree) {
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
						System.out.println("clash, #checks: " + cTree.clashVolumeOverlapCount);
					}
					else {
						numberNonClashingStructures++;
						totalCountNonClashingStructures += cTree.clashVolumeOverlapCount;
						cTree.getCrossEnergy(i);
						totalCountEnergy += cTree.energyVolumeOverlapCount;
						System.out.println("no clash, #checks: " + cTree.clashVolumeOverlapCount);
						System.out.println("energy,   #checks: " + cTree.energyVolumeOverlapCount);
					}
				}
			}
			i++;
		}
		System.out.println("Number of clashing structures is     " + numberClashingStructures);
		System.out.println("Number of capsule intersection tests in clashing structures i " + totalCountClashingStructures);
		System.out.println("Number of non-clashing structures is " + numberNonClashingStructures);
		System.out.println("Number of capsule intersection tests in non-clashing structures i " + totalCountNonClashingStructures);
		System.out.println("Number of energy capsule intersection tests in non-clashing structures is " + totalCountEnergy);
	}
}
