package molecule;

import j3dScene.J3DScene;

import java.awt.Color;

import geom3d.Point3d;
import geom3d.Sphere3d;
import guiMolecule.ProteinViewer;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;

import chainTree.ChainTree;

import misc.Colors;

public class SideChainCys extends SideChain {
	private Carbon  Cb;
	private Sulphur  Sg;
	
	BranchGroup CbSgBond  = null;
	Shape3D CbSgBondShape = null;

	public SideChainCys(AminoAcid aminoAcid) {
		super(aminoAcid);
		Cb = new Carbon();
		Sg = new Sulphur();
	}
	
	
	public Carbon  getCb()           { return Cb; }
	public void    setCb(Carbon cb)  { Cb = cb;   }
	public Sulphur getSg()           { return Sg; }
	public void    setSg(Sulphur sg) { Sg = sg;   }
	public BranchGroup getCbSgBond()                 { return CbSgBond; }
	public void    setCbSgBond(BranchGroup CbSgBond) { this.CbSgBond = CbSgBond; } 
	public Shape3D getCbSgBondShape()                      { return CbSgBondShape; }
	public void    setCbSgBondShape(Shape3D CbSgBondShape) { this.CbSgBondShape = CbSgBondShape; }

	
	public void createBranchGroup(AminoAcid aminoAcid, Shape3D shape, ProteinViewer pV) {
	  Cb.setBranchGroup(pV.addSphere(Cb, Carbon.getAtomicRadius()/2, Colors.black));
	  Sg.setBranchGroup(pV.addSphere(Sg, Sulphur.getAtomicRadius()/2, Colors.yellow));
	  setCbSgBond(pV.addBond(Cb, Sg, Colors.black));
	  setCbSgBondShape(shape);
	  aminoAcid.setCaCbBond(pV.addBond(aminoAcid.getCa(), Cb, Colors.black));
	  aminoAcid.setCaCbBondShape(shape);
	}
	
	public void detachBranchGroup() {
		Cb.getBranchGroup().detach();
		Sg.getBranchGroup().detach();
		aminoAcid.getCaCbBond().detach();
		CbSgBond.detach();
	}

	public void reattachBranchGroup(TransformGroup spin) {
		spin.addChild(Cb.getBranchGroup());
		spin.addChild(Sg.getBranchGroup());
		spin.addChild(aminoAcid.getCaCbBond());
		spin.addChild(CbSgBond);
	}
	
	public void draw(J3DScene scene, ChainTree cTree, int i, Carbon Ca) { 
		Point3d CaPos = Ca.getPosition();
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cb.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Sg.getPosition().subtract(CaPos)), Sulphur.vdWRadius/2), Color.yellow);
	}


}
