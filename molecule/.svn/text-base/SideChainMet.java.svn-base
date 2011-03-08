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


public class SideChainMet extends SideChain {
	private Carbon  Cb;
	private Carbon  Cg;
	private Sulphur Sd;
	private Carbon  Ce;
	
	BranchGroup CbCgBond  = null;
	BranchGroup CgSdBond  = null;
	BranchGroup SdCeBond  = null;
	Shape3D CbCgBondShape = null;
	Shape3D CgSdBondShape = null;
	Shape3D SdCeBondShape = null;

	public SideChainMet(AminoAcid aminoAcid) {
		super(aminoAcid);
		Cb = new Carbon();
		Cg = new Carbon();
		Sd = new Sulphur();
		Ce = new Carbon();
	}
	
	
	public Carbon  getCb()           { return Cb; }
	public void    setCb(Carbon cb)  { Cb = cb;   }
	public Carbon  getCg()           { return Cg; }
	public void    setCg(Carbon cg)  { Cg = cg;   }
	public Sulphur getSd()           { return Sd; }
	public void    setSd(Sulphur sd) { Sd = sd;   }
	public Carbon  getCe()           { return Ce; }
	public void    setCe(Carbon ce)  { Ce = ce;   }
	public BranchGroup getCbCgBond()                 { return CbCgBond; }
	public void    setCbCgBond(BranchGroup CbCgBond) { this.CbCgBond = CbCgBond; } 
	public BranchGroup getCgSdBond()                 { return CgSdBond; }
	public void    setCgSdBond(BranchGroup CgSdBond) { this.CgSdBond = CgSdBond; } 
	public BranchGroup getSdCeBond()                 { return SdCeBond; }
	public void    setSdCeBond(BranchGroup SdCeBond) { this.SdCeBond = SdCeBond; } 
	public Shape3D getCbCgBondShape()                      { return CbCgBondShape; }
	public void    setCbCgBondShape(Shape3D CbCgBondShape) { this.CbCgBondShape = CbCgBondShape; }
	public Shape3D getCgSdBondShape()                      { return CgSdBondShape; }
	public void    setCgSdBondShape(Shape3D CgSdBondShape) { this.CgSdBondShape = CgSdBondShape; }
	public Shape3D getSdCeBondShape()                      { return SdCeBondShape; }
	public void    setSdCeBondShape(Shape3D SdCeBondShape) { this.SdCeBondShape = SdCeBondShape; }
	
	public void createBranchGroup(AminoAcid aminoAcid, Shape3D shape, ProteinViewer pV) {
		Cb.setBranchGroup(pV.addSphere(Cb, Carbon.getAtomicRadius()/2, Colors.black));
		Cg.setBranchGroup(pV.addSphere(Cg, Carbon.getAtomicRadius()/2, Colors.black));
		Sd.setBranchGroup(pV.addSphere(Sd, Sulphur.getAtomicRadius()/2, Colors.yellow));
		Ce.setBranchGroup(pV.addSphere(Ce, Carbon.getAtomicRadius()/2, Colors.black));
		setCbCgBond(pV.addBond(Cb, Cg, Colors.black));
		setCbCgBondShape(shape);
		setCgSdBond(pV.addBond(Cg, Sd, Colors.black));
		setCgSdBondShape(shape);
		setSdCeBond(pV.addBond(Sd, Ce, Colors.black));
		setSdCeBondShape(shape);
		aminoAcid.setCaCbBond(pV.addBond(aminoAcid.getCa(), Cb, Colors.black));
		aminoAcid.setCaCbBondShape(shape);
	}
	
	public void detachBranchGroup() {
		Cb.getBranchGroup().detach();
		Cg.getBranchGroup().detach();
		Sd.getBranchGroup().detach();
		Ce.getBranchGroup().detach();
		CbCgBond.detach();		  
		CgSdBond.detach();
		SdCeBond.detach();
		aminoAcid.getCaCbBond().detach();
	}
	public void reattachBranchGroup(TransformGroup spin) {
		spin.addChild(Cb.getBranchGroup());
		spin.addChild(Cg.getBranchGroup());
		spin.addChild(Sd.getBranchGroup());
		spin.addChild(Ce.getBranchGroup());
		spin.addChild(CbCgBond);		  
		spin.addChild(CgSdBond);
		spin.addChild(SdCeBond);
		spin.addChild(aminoAcid.getCaCbBond());
	}

	public void draw(J3DScene scene, ChainTree cTree, int i, Carbon Ca) { 
		Point3d CaPos = Ca.getPosition();
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cb.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cg.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Sd.getPosition().subtract(CaPos)), Sulphur.vdWRadius/2), Color.yellow);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Ce.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);

	}

}
