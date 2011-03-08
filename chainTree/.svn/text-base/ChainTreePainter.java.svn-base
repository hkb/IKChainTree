package chainTree;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import geom3d.PointSet3d;
import molecule.Protein;
import chainTree.BinaryTreePainter.BinNode;

public class ChainTreePainter {

	public static void main(String[] args) {
		String[] pdbs = new String[]{"1X5RA","1X0OA","1XDXA","1AKPA","1Y6DA"};
//<<<<<<< .mine
//		ChainTree cTree= buildSampleChainTree(pdbs[4],BoundingVolumeManager.RSS,true, true, true);
//		BinaryTreePainter btp = ChainTreePainter.displayChainTree(cTree);
//=======
		ChainTree cTree= buildSampleChainTree(pdbs[4],BoundingVolumeManager.CAPSULE,true,true,true);
		ChainTreePainter.displayChainTree(cTree);
//>>>>>>> .r555
	}
	
	private static ChainTree buildSampleChainTree(String pdbId, int volume, boolean groupPeptide, boolean groupSS, boolean rebalance){
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
		cTree.lockAlphaHelices(groupSS, false);
		cTree.lockBetaStrands(groupSS,false);
		if(rebalance) cTree.naiveRebalance2();
//		try{
//		}catch(CTNode.NodeException exc){
//			CTNode n = exc.n;
//			while(n.parent!=null) {
//				System.out.print(n.parent.left==n?"left,":"right:");
//				n = n.parent;
//			}
//			System.out.println();
//		}
		cTree.createBoundingVolume(cTree.root);
//		cTree.restoreHeights();
		
		for (int i = 1; i < cTree.nodes.length-1; i++) 
			cTree.changeRotationAngle(i, 
					cTree.proteinDihedralAngles[i] -
					cTree.getDihedralAngle(i));

		return cTree;
	}
	
	public static BinaryTreePainter displayChainTree(ChainTree cTree){
		CTPaintNode root = new CTPaintNode(cTree.root, cTree);
		BinaryTreePainter btp = new BinaryTreePainter(root);
		btp.getContentPane().add(new ButtonPanel(cTree,btp,root),"South");
		btp.setVisible(false);
		btp.setVisible(true);
		return btp;
	}

	public static BinaryTreePainter displayChainSubtree(CTNode nd, ChainTree cTree) {
		CTPaintNode ctNode = new CTPaintNode(nd, cTree);
		BinaryTreePainter btp = new BinaryTreePainter(ctNode);
		btp.getContentPane().add(new ButtonPanel(cTree,btp,ctNode),"South");
		btp.setVisible(false);
		btp.setVisible(true);
		return btp;
	}
	
	static class ButtonPanel extends JPanel{
		ChainTree ct;
		BinaryTreePainter btp;
		CTPaintNode root;
		ButtonPanel(ChainTree _cTree, BinaryTreePainter _btp, CTPaintNode root){
			super();
			this.ct = _cTree;
			this.btp = _btp;
			this.root = root;
			JButton b;
			b=new JButton("Toggle 3D");
			b.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					ct.drawCapsuleLevelLower=-1;
					ct.drawCapsuleLevelUpper=-1;
					ct.initPaint();
				}});
			add(b);

			b = new JButton("Left-rotate");
			b.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					List<CTPaintNode> toggled = ((CTPaintNode)ButtonPanel.this.btp.root.n).getToggled();
					if(toggled.size()==1){
						toggled.get(0).n.leftRotation(ct);
						btp.setRoot(new CTPaintNode(ct.root,ct));
						btp.repaint();
					}
				}
			});
			add(b);
			b = new JButton("Right-rotate");
			b.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					List<CTPaintNode> toggled = ((CTPaintNode)ButtonPanel.this.btp.root.n).getToggled();
					if(toggled.size()==1){
						toggled.get(0).n.rightRotation(ct);
						btp.setRoot(new CTPaintNode(ct.root,ct));
						btp.repaint();
					}
				}
			});
			add(b);
		}
	}
	
	static class CTPaintNode implements BinaryTreePainter.BinNode{
		CTNode n;
		ChainTree ct;
		CTPaintNode left = null, right = null;
		boolean isToggled = false;
		
		CTPaintNode(CTNode n, ChainTree ct){
			this.n = n;
			this.ct = ct;
			if (n.height > 0) {
				left = new CTPaintNode(n.left,ct);
				right = new CTPaintNode(n.right,ct);
			}
//			if(n.left!=null) left = new CTPaintNode(n.left,ct);
//			if(n.right!=null) right = new CTPaintNode(n.right,ct);
		}
		List<CTPaintNode> getToggled(){
			List<CTPaintNode> toggled = new ArrayList<CTPaintNode>();
			collectToggled(toggled);
			return toggled;
		}
		void collectToggled(List<CTPaintNode> list){
			if(isToggled) list.add(this);
			if(left!=null) left.collectToggled(list);
			if(right!=null) right.collectToggled(list);
		}
		
		public BinNode getLeft() { return left;	}
		public BinNode getRight() { return right; }
		public String toString(){
			return n.height+"";
//			if(n.getDepth()<5) return "["+n.low+","+n.high+"]"; else return "";
//			return String.format("%.1f",n.volume.volume()); 
		}
		public Color getLegColor() {
			return n.isLocked?Color.RED:Color.BLACK;
		}
		public Color getNodeColor() {
			return isToggled?Color.BLUE:new Color(0,0,0,0);
		}
		public void click() {
			isToggled = !isToggled;
			if(ct.j3dg==null) return;
			if(isToggled){
				n.drawVolume = ct.volumeManager.transform(0, n.low, n.volume);
				ct.j3dg.addShape(n.drawVolume,ct.colors[n.height]);
				System.out.println(n.drawVolume);
			}else{
				ct.j3dg.removeShape(n.drawVolume);
				System.out.println(n.drawVolume);
			}
		}
	}
}
