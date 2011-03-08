package experiments;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import j3dScene.J3DScene;
import geom3d.PointSet3d;
import geom3d.Shape3d;
import chainTree.ChainTree;
import molecule.Protein;

/**
 * Folds a protein while showing the target structure.
 * @author hkb
 *
 */
public class FoldProtein {
	public static void main(String[] args){
		/*
		 * Setup
		 */
		String pdbId = "1PUX"; // specify the id of the protein to fold -- 1PUX good for IK
		int targetOpacity = 20; // should the target be displayed
		double errorTolerance = 0.01; // the error tolerance
		
		/*
		 * Experiment
		 */
		// create the protein
		Protein protein = new Protein(pdbId, 2, true);
			
		// setup protein to simulate
		ChainTree cTree = setupChainTree(protein);

		cTree.j3dg.autoZoom();
		cTree.j3dg.setWindowTitle(pdbId + " - simulation");
		
		// copy target into the scene of the chain tree
		ChainTree target = setupChainTree(protein);
		target.j3dg.frame.dispose();

		Map<Shape3d,Color> targetShapes = target.j3dg.getAllShapes();
		for (Entry<Shape3d, Color> value : targetShapes.entrySet()) {
			Shape3d shape = value.getKey();
			Color color = value.getValue();

			cTree.j3dg.addShape(shape, new Color(color.getRed(), color.getGreen(), color.getBlue(), targetOpacity));
		}
		
		// rum simulation
		obfuscateProtein(cTree);
		foldProtein(cTree, errorTolerance);
	}

	
	/**
	 * Sets up a chain tree for the given protein.
	 * @param protein
	 * @return
	 */
	private static ChainTree setupChainTree(Protein protein) {
		/*
		 * Create chain tree.
		 */
		PointSet3d allPoints = protein.getPointSet();
		PointSet3d points = new PointSet3d();
		for (int i = 0; i < allPoints.getSize(); i++) points.insert(allPoints.get(i));

		ChainTree cTree = new ChainTree(points, protein, true);
		
		/*
		 * Lock peptide planes and chain endpoints.
		 */
		for (int i = 2; i < cTree.nodes.length; i = i+3) cTree.nodes[i].isLocked = true;
		cTree.nodes[cTree.nodes.length-1].isLocked = true;
		cTree.nodes[0].isLocked = true;
		
		/*
		 * Lock secondary structures.
		 */
		cTree.lockAlphaHelices(true);
		cTree.lockBetaStrands(true);
		
		cTree.removeLockedSubtrees(true);
		cTree.newRebalanceSubtree(cTree.root);
		cTree.addLockedSubtrees(true);
		
		/*
		 * Compute three bounding volume and energy.
		 */
		cTree.createBoundingVolume(cTree.root);
		cTree.createEnergyBoundingVolume(cTree.root);

		/*
		 * Pre-compute distance matrix. <-- silly but avoids error
		 */
		cTree.getDistanceMatrix();
		
		/*
		 * Setup 3D display properties.
		 */
		cTree.j3dg = J3DScene.createJ3DSceneInFrame();
		cTree.initPaint();

		return cTree;
	}
	
	/**
	 * Obfuscates a protein (stretches it).
	 * @param cTree
	 * @return
	 */
	private static void obfuscateProtein(ChainTree cTree) {
		for (int i = 1; i < cTree.nodes.length-1; i++) {
			if (!cTree.nodes[i].isLocked) { 
				// if bound is not locked then obfuscate
				cTree.changeRotationAngle(i, -cTree.getDihedralAngle(i));
			}
		}
	}
	
	/**
	 * Performs the folding of a protein.
	 * @param cTree
	 * @return
	 */
	private static void foldProtein(ChainTree cTree, double errorTolerance) {
		double rmsd = cTree.getPairwiseDistanceRMSD(); // the root mean square distance from the actual protein structure.
		double rmsdUpperBound = rmsd; // upper bound
		int iterationsSinceLastImprovement = 0; // the number of iterations since the last improvement of the protein structure
		errorTolerance++; // trick to go from semantic value to a computational more efficient value
		int backboneLength = cTree.nodes.length; // length of the protein backbone
		int bondToRotate; // the bond to rotate at a given simulation step

		// stats
		long validStructures = 0; // total number of valid but rejected structures
		long invalidStructures = 0; // total number of invalid structures
		long improvements = 0; // total number of structure improvements
		
		long experimentStart = System.currentTimeMillis(); // the time when the experiment started

		
		// Detect rotatable bonds.
		ArrayList<Integer> rotatableBonds = new ArrayList<Integer>();
		for (int i=0; i < backboneLength; i++) if (!cTree.nodes[i].isLocked) rotatableBonds.add(i);
		int rotatableBondsLength = rotatableBonds.size();
		
		/*
		 * Perform folding.
		 */
		while (iterationsSinceLastImprovement < rotatableBondsLength) {
			iterationsSinceLastImprovement++;

			// find random, non locked, bond to rotate
			bondToRotate = rotatableBonds.get((int)(Math.random()*rotatableBondsLength));
			
			
			// find random angle to rotate by
			double angle = (Math.random()-0.5) * 15 * (Math.PI/180);
			
			// rotate bond
			cTree.changeRotationAngle(bondToRotate, angle);

			
			// check if rotation improved the protein structure
			double tmpRmsd = cTree.getPairwiseDistanceRMSD();
			
			if (!cTree.isClashing(bondToRotate) && tmpRmsd < rmsd && tmpRmsd < rmsdUpperBound) {
				rmsd = tmpRmsd * errorTolerance; // store new rmsd value
				iterationsSinceLastImprovement = 0; // reset since we have just improved the structure
				validStructures++;

				if (rmsd < rmsdUpperBound) {
					rmsdUpperBound = rmsd;
					
					improvements++;
				
					//display results
					System.out.println("Time: "+((System.currentTimeMillis()-experimentStart)/1000.0)+"\tRMSD: " + (Math.round(rmsd*1000000000.0)/1000000000.0) + "\tValid: " + validStructures + "\tInvalid: " + invalidStructures + "\tImprovements: " + improvements + "\t Success: " + ((float)improvements/(validStructures+invalidStructures)));
					cTree.repaint();
				}
			} else {
				// if the rotation did not result in an improvement the undo it
				cTree.changeRotationAngle(bondToRotate, -angle);
				invalidStructures++;
			}
			
			
			if (iterationsSinceLastImprovement > 100) {
				System.err.println("Over 100 unsuccessfull iterations!");
			}
		}
		
		System.out.print("End of simulation!");
	}
}
