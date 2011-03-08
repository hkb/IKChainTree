package molecule;

import java.awt.Color;

import j3dScene.J3DScene;
import geom3d.Point3d;
import geom3d.Sphere3d;
import guiMolecule.AtomAppearance;
import guiMolecule.ProteinViewer;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.Shape3D;

import chainTree.ChainTree;
import misc.Colors;

public class SideChainAla extends SideChain {
	private Carbon  Cb;
	
	
	public SideChainAla(AminoAcid aminoAcid) {
		super(aminoAcid);
		Cb = new Carbon();
	}
		
	public Carbon  getCb()           { return Cb; }
	public void    setCb(Carbon cb)  { Cb = cb;   }
	
	public void createBranchGroup(AminoAcid aminoAcid, Shape3D shape, ProteinViewer pV) {
		Cb.addBranchGroups(AtomAppearance.carbon, pV.getSpin());
		aminoAcid.setCaCbBond(pV.addBond(aminoAcid.getCa(), Cb, Colors.black));
		aminoAcid.setCaCbBondShape(shape);
	}
	
	public void detachBranchGroup() {
		Cb.getAtomBranchGroup().detach();
		Cb.getVdWBranchGroup().detach();
		aminoAcid.getCaCbBond().detach();
	}
	
	public void reattachBranchGroup(TransformGroup spin, ProteinViewer pV) {
		if (pV.getModel().equals("Atoms")) spin.addChild(Cb.getAtomBranchGroup()); else spin.addChild(Cb.getVdWBranchGroup());
		spin.addChild(aminoAcid.getCaCbBond());

	}

	public void draw(J3DScene scene, ChainTree cTree, int i, Carbon Ca) { 
		Point3d CaPos = Ca.getPosition();
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cb.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
	}

}
