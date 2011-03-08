package chainTree;


import java.util.ArrayList;
import java.util.Collection;

import geom3d.PointSet3d;
import j3dScene.J3DScene;
import molecule.Protein;

/**
 * An Adjustable ChainTree is a data structure with locked secondary structures.
 * @author hkb
 */
public class AdjustableChainTree extends ChainTree {

	/**
	 * Creates a new adjustable chain tree from a protein.
	 * 
	 * @param protein The protein to create the adjustable chain tree from.
	 * @param scene A 3D scene to render the protein on. 
	 */
	public AdjustableChainTree(Protein protein, J3DScene scene) {
		super(extractProteinPoints(protein), protein, true);

		/*
		 * Lock peptide planes and chain endpoints.
		 */
		for (int i = 2; i < this.nodes.length; i = i+3) this.nodes[i].isLocked = true;
		this.nodes[this.nodes.length-1].isLocked = true;
		this.nodes[0].isLocked = true;
		
		/*
		 * Lock secondary structures.
		 */
		this.lockAlphaHelices(true);
		this.lockBetaStrands(true);
		
		this.removeLockedSubtrees(true);
		this.newRebalanceSubtree(this.root);
		this.addLockedSubtrees(true);
		
		/*
		 * Compute three bounding volume and energy.
		 */
		this.createBoundingVolume(this.root);
		this.createEnergyBoundingVolume(this.root);

		/*
		 * Pre-compute distance matrix. <-- silly but avoids error
		 */
		this.getDistanceMatrix();
		
		/*
		 * Setup 3D display properties if specified.
		 */
		if (scene != null) {
			this.j3dg = scene;
		    this.initPaint();
		    
		    this.j3dg.autoZoom();
		}
	}
	
	/**
	 * Creates a new adjustable chain tree from a protein.
	 * 
	 * @param protein The protein to create the adjustable chain tree from.
	 */
	public AdjustableChainTree(Protein protein) {
		this(protein, null);
	}
	
	/**
	 * Creates a new adjustable chain tree from the proteins PDB id.
	 * 
	 * @param pdbId The id of the protein.
	 * @param scene A 3D scene to render the protein on. 
	 */
	public AdjustableChainTree(String pdbId, J3DScene scene) {
		this(new Protein(pdbId, 2, true), scene);
	}
	
	/**
	 * Creates a new adjustable chain tree from the proteins PDB id.
	 * 
	 * @param pdbId The id of the protein.
	 */
	public AdjustableChainTree(String pdbId) {
		this(pdbId, null);
	}
	
	
	
	/* ----------------- PUBLIC METHODS ---------------- */
	
	/**
	 * Unfolds a folded protein to some unfolded state.
	 */
	public void unfold() {
		for (int i = 1, j = this.nodes.length-1; i < j; i++) {
			if (!this.nodes[i].isLocked) { 
				// if bound is not locked then obfuscate
				this.changeRotationAngle(i, -this.getDihedralAngle(i));
			}
		}
	}
	
	/**
	 * Returns the indices of the bonds that are not locked.
	 * @return A collection of indices of the bonds that are not locked.
	 */
    public Collection<Integer> rotateableBonds() {
		ArrayList<Integer> rotatableBonds = new ArrayList<Integer>();
		
		for (int i = 0, j = this.nodes.length; i < j; i++) { 
			if (!this.nodes[i].isLocked) {
				rotatableBonds.add(i);
			}
		}
		
    	return rotatableBonds;
    }

    
    
	/* ----------------- PRIVATE METHODS ---------------- */
    
	/**
	 * Returns a set of the points in the protein.
	 * 
	 * THIS IS NECCESERY ONLY BECAUSE JAVA REQUIRES THE super CALL TO
	 * BE THE FIRST IN THE CONSTRUCTOR!
	 * 
	 * @param protein The protein to calculate the points for.
	 * @return The points of the protein.
	 */
	private static PointSet3d extractProteinPoints(Protein protein) {
		PointSet3d allPoints = protein.getPointSet();
		PointSet3d points = new PointSet3d();
		for (int i = 0; i < allPoints.getSize(); i++) points.insert(allPoints.get(i));
		return points;
	}
	
}
