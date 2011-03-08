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

public class SideChainPro extends SideChain {
	private Carbon  Cb;
	private Carbon  Cg;
	private Carbon Cd;
	
	BranchGroup CbCgBond  = null;
	BranchGroup CgCdBond  = null;
	BranchGroup CdNBond  = null;
	Shape3D CbCgBondShape = null;
	Shape3D CgCdBondShape = null;
	Shape3D CdNBondShape = null;
	
	public SideChainPro(AminoAcid aminoAcid) {
		super(aminoAcid);
		Cb = new Carbon();
		Cg = new Carbon();
		Cd = new Carbon();
	}
	
	public Carbon  getCb()           { return Cb; }
	public void    setCb(Carbon cb)  { Cb = cb;   }
	public Carbon  getCg()           { return Cg; }
	public void    setCg(Carbon cg)  { Cg = cg;   }
	public Carbon  getCd()           { return Cd; }
	public void    setCd(Carbon cd)  { Cd = cd;   }
	public BranchGroup getCbCgBond()                 { return CbCgBond; }
	public void    setCbCgBond(BranchGroup CbCgBond) { this.CbCgBond = CbCgBond; } 
	public BranchGroup getCgCdBond()                 { return CgCdBond; }
	public void    setCgCdBond(BranchGroup CgCdBond) { this.CgCdBond = CgCdBond; } 
	public Shape3D getCbCgBondShape()                      { return CbCgBondShape; }
	public void    setCbCgBondShape(Shape3D CbCgBondShape) { this.CbCgBondShape = CbCgBondShape; }
	public Shape3D getCgCdBondShape()                      { return CgCdBondShape; }
	public void    setCgCdBondShape(Shape3D CgCdBondShape) { this.CgCdBondShape = CgCdBondShape; }
	
	public void createBranchGroup(AminoAcid aminoAcid, Shape3D shape, ProteinViewer pV) {	
		Cb.setBranchGroup(pV.addSphere(Cb, Carbon.getAtomicRadius()/2, Colors.black));
		Cg.setBranchGroup(pV.addSphere(Cg, Carbon.getAtomicRadius()/2, Colors.black));
		Cd.setBranchGroup(pV.addSphere(Cd, Carbon.getAtomicRadius()/2, Colors.black));
		setCbCgBond(pV.addBond(Cb, Cg, Colors.black));
		setCbCgBondShape(shape);
		setCgCdBond(pV.addBond(Cg, Cd, Colors.black));
		setCgCdBondShape(shape);
		aminoAcid.setCaCbBond(pV.addBond(aminoAcid.getCa(), Cb, Colors.black));
		aminoAcid.setCaCbBondShape(shape);
		aminoAcid.setCdNBond(pV.addBond(Cd, aminoAcid.getN(), Colors.black));
		aminoAcid.setCdNBondShape(shape);
	}
	
	public void detachBranchGroup() {
		Cb.getBranchGroup().detach();
		Cg.getBranchGroup().detach();
		Cd.getBranchGroup().detach();
		CbCgBond.detach();		  
		CgCdBond.detach();
		aminoAcid.getCdNBond().detach();
		aminoAcid.getCaCbBond().detach();
	}
	public void reattachBranchGroup(TransformGroup spin) {
		spin.addChild(Cb.getBranchGroup());
		spin.addChild(Cg.getBranchGroup());
		spin.addChild(Cd.getBranchGroup());
		spin.addChild(CbCgBond);		  
		spin.addChild(CgCdBond);
		spin.addChild(aminoAcid.getCdNBond());
		spin.addChild(aminoAcid.getCaCbBond());
	}

	public void draw(J3DScene scene, ChainTree cTree, int i, Carbon Ca) { 
		Point3d CaPos = Ca.getPosition();
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cb.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cg.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cd.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
	}

	
}
