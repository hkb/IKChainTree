package chainTree;

import geom3d.PointSet3d;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

import molecule.Protein;

public class ChainTreeExplorer extends JFrame{
	private final ChainTree cTree;
	
	public ChainTreeExplorer(ChainTree cTree){
		super("ChainTree explorer");
		super.setSize(500,600);
		this.cTree = cTree;
		setup();
		super.setVisible(true);
	}
	
	void setup(){
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("ChainTree");
		top.add(setup(cTree.root));
		JTree tree = new JTree(top);
		this.getContentPane().add(new JScrollPane(tree));
	}
	
	CTNodeWrapper setup(CTNode n){
		CTNodeWrapper ret = new CTNodeWrapper(n);
		if(n.left!=null) ret.add(setup(n.left));
		if(n.right!=null) ret.add(setup(n.right));
		return ret;
	}
	class CTNodeWrapper extends DefaultMutableTreeNode{
		final CTNode nd;
		CTNodeWrapper(CTNode nd){
			super(nd);
			this.nd = nd;
		}
		
		public String toString(){
			return String.format("[%d,%d,height:%d%s]",nd.low,nd.high,nd.height,nd.isLocked?",locked":"");
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String pdbId = "1X5RA";
		int volume = BoundingVolumeManager.CAPSULE;
		boolean groupPeptide = true;
		boolean groupSS = true;
		boolean rebalance = false;
		
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
		cTree.lockAlphaHelices(groupSS, rebalance);
		cTree.lockBetaStrands(groupSS,rebalance);

		cTree.createBoundingVolume(cTree.root);
		
		new ChainTreeExplorer(cTree);
	}

}
