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

public class SideChainLys extends SideChain {
	private Carbon  Cb;
	private Carbon  Cg;
	private Carbon Cd;
	private Carbon  Ce;
	private Nitrogen Nz;
	
	BranchGroup CbCgBond  = null;
	BranchGroup CgCdBond  = null;
	BranchGroup CdCeBond  = null;
	BranchGroup CeNzBond  = null;
	Shape3D CbCgBondShape = null;
	Shape3D CgCdBondShape = null;
	Shape3D CdCeBondShape = null;
	Shape3D CeNzBondShape = null;
	
	public SideChainLys(AminoAcid aminoAcid) {
		super(aminoAcid);
		Cb = new Carbon();
		Cg = new Carbon();
		Cd = new Carbon();
		Ce = new Carbon();
		Nz = new Nitrogen();
	}
	
	
	public Carbon  getCb()           { return Cb; }
	public void    setCb(Carbon cb)  { Cb = cb;   }
	public Carbon  getCg()           { return Cg; }
	public void    setCg(Carbon cg)  { Cg = cg;   }
	public Carbon  getCd()           { return Cd; }
	public void    setCd(Carbon cd)  { Cd = cd;   }
	public Carbon  getCe()           { return Ce; }
	public void    setCe(Carbon ce)  { Ce = ce;   }
	public Nitrogen getNz()          { return Nz; }
	public void    setNz(Nitrogen nz){ Nz = nz;   }
	public BranchGroup getCbCgBond()                 { return CbCgBond; }
	public void    setCbCgBond(BranchGroup CbCgBond) { this.CbCgBond = CbCgBond; } 
	public BranchGroup getCgCdBond()                 { return CgCdBond; }
	public void    setCgCdBond(BranchGroup CgCdBond) { this.CgCdBond = CgCdBond; } 
	public BranchGroup getCdCeBond()                 { return CdCeBond; }
	public void    setCdCeBond(BranchGroup CdCeBond) { this.CdCeBond = CdCeBond; } 
	public BranchGroup getCeNzBond()                 { return CeNzBond; }
	public void    setCeNzBond(BranchGroup CeNzBond) { this.CeNzBond = CeNzBond; } 
	public Shape3D getCbCgBondShape()                      { return CbCgBondShape; }
	public void    setCbCgBondShape(Shape3D CbCgBondShape) { this.CbCgBondShape = CbCgBondShape; }
	public Shape3D getCgCdBondShape()                      { return CgCdBondShape; }
	public void    setCgCdBondShape(Shape3D CgCdBondShape) { this.CgCdBondShape = CgCdBondShape; }
	public Shape3D getCdCeBondShape()                      { return CdCeBondShape; }
	public void    setCdCeBondShape(Shape3D CdCeBondShape) { this.CdCeBondShape = CdCeBondShape; }
	public Shape3D getCeNzBondShape()                      { return CeNzBondShape; }
	public void    setCeNzBondShape(Shape3D CeNzBondShape) { this.CeNzBondShape = CeNzBondShape; }

	public void createBranchGroup(AminoAcid aminoAcid, Shape3D shape, ProteinViewer pV) {
		Cb.setBranchGroup(pV.addSphere(Cb, Carbon.getAtomicRadius()/2, Colors.black));
		Cg.setBranchGroup(pV.addSphere(Cg, Carbon.getAtomicRadius()/2, Colors.black));
		Cd.setBranchGroup(pV.addSphere(Cd, Carbon.getAtomicRadius()/2, Colors.black));
		Ce.setBranchGroup(pV.addSphere(Ce, Carbon.getAtomicRadius()/2, Colors.black));
		Nz.setBranchGroup(pV.addSphere(Nz, Nitrogen.getAtomicRadius()/2, Colors.blue));
		setCbCgBond(pV.addBond(Cb, Cg, Colors.black));
		setCbCgBondShape(shape);
		setCgCdBond(pV.addBond(Cg, Cd, Colors.black));
		setCgCdBondShape(shape);
		setCdCeBond(pV.addBond(Cd, Ce, Colors.black));
		setCdCeBondShape(shape);
		setCeNzBond(pV.addBond(Ce, Nz, Colors.black));
		setCeNzBondShape(shape);
		aminoAcid.setCaCbBond(pV.addBond(aminoAcid.getCa(), Cb, Colors.black));
		aminoAcid.setCaCbBondShape(shape);
	}
	
	public void detachBranchGroup() {
		Cb.getBranchGroup().detach();
		Cg.getBranchGroup().detach();
		Cd.getBranchGroup().detach();
		Ce.getBranchGroup().detach();
		Nz.getBranchGroup().detach();
		CbCgBond.detach();		  
		CgCdBond.detach();
		CdCeBond.detach();
		CeNzBond.detach();
		aminoAcid.getCaCbBond().detach();
	}
	public void reattachBranchGroup(TransformGroup spin) {
		spin.addChild(Cb.getBranchGroup());
		spin.addChild(Cg.getBranchGroup());
		spin.addChild(Cd.getBranchGroup());
		spin.addChild(Ce.getBranchGroup());
		spin.addChild(Nz.getBranchGroup());
		spin.addChild(CbCgBond);		  
		spin.addChild(CgCdBond);
		spin.addChild(CdCeBond);
		spin.addChild(CeNzBond);
		spin.addChild(aminoAcid.getCaCbBond());
	}

	public void draw(J3DScene scene, ChainTree cTree, int i, Carbon Ca) { 
		Point3d CaPos = Ca.getPosition();
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cb.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cg.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cd.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Ce.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Nz.getPosition().subtract(CaPos)), Nitrogen.vdWRadius/2), Color.blue);
	}

}
