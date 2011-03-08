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


public class SideChainSer extends SideChain {
	private Carbon  Cb;
	private Oxygen  Og;
	
	BranchGroup CbOgBond  = null;
	Shape3D CbOgBondShape = null;

	public SideChainSer(AminoAcid aminoAcid) {
		super(aminoAcid);
		Cb = new Carbon();
		Og = new Oxygen();
	}
	
	
	public Carbon  getCb()           { return Cb; }
	public void    setCb(Carbon cb)  { Cb = cb;   }
	public Oxygen  getOg()           { return Og; }
	public void    setOg(Oxygen og)   { Og = og;   }
	public BranchGroup getCbOgBond()                 { return CbOgBond; }
	public void    setCbOgBond(BranchGroup CbOgBond) { this.CbOgBond = CbOgBond; } 
	public Shape3D getCbOgBondShape()                      { return CbOgBondShape; }
	public void    setCbOgBondShape(Shape3D CbOgBondShape) { this.CbOgBondShape = CbOgBondShape; }
	
	public void createBranchGroup(AminoAcid aminoAcid, Shape3D shape, ProteinViewer pV) {	
		Cb.setBranchGroup(pV.addSphere(Cb, Carbon.getAtomicRadius()/2, Colors.black));
		Og.setBranchGroup(pV.addSphere(Og, Oxygen.getAtomicRadius()/2, Colors.red));
		setCbOgBond(pV.addBond(Cb, Og, Colors.black));
		setCbOgBondShape(shape);
		aminoAcid.setCaCbBond(pV.addBond(aminoAcid.getCa(), Cb, Colors.black));
		aminoAcid.setCaCbBondShape(shape);
	}
	
	public void detachBranchGroup() {
		Cb.getBranchGroup().detach();
		Og.getBranchGroup().detach();
		CbOgBond.detach();		  
		aminoAcid.getCaCbBond().detach();
	}
	
	public void reattachBranchGroup(TransformGroup spin) {
		spin.addChild(Cb.getBranchGroup());
		spin.addChild(Og.getBranchGroup());
		spin.addChild(CbOgBond);	  
		spin.addChild(aminoAcid.getCaCbBond());
	}

	public void draw(J3DScene scene, ChainTree cTree, int i, Carbon Ca) { 
		Point3d CaPos = Ca.getPosition();
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cb.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Og.getPosition().subtract(CaPos)), Oxygen.vdWRadius/2), Color.red);
	}

}

