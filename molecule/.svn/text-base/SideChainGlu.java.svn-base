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

public class SideChainGlu extends SideChain {
	private Carbon   Cb;
	private Carbon   Cg;
	private Carbon   Cd;
	private Oxygen Oe1;
	private Oxygen Oe2;

	BranchGroup CbCgBond  = null;
	BranchGroup CgCdBond  = null;
	BranchGroup CdOe1Bond = null;
	BranchGroup CdOe2Bond = null;
	Shape3D CbCgBondShape = null;
	Shape3D CgCdBondShape = null;
	Shape3D CdOe1BondShape = null;
	Shape3D CdOe2BondShape = null;
	
	public SideChainGlu(AminoAcid aminoAcid) {
		super(aminoAcid);
		Cb = new Carbon();
		Cg = new Carbon();
		Cd = new Carbon();
		Oe1 = new Oxygen();
		Oe2 = new Oxygen();
	}
	
	public Carbon  getCb()           { return Cb; }
	public void    setCb(Carbon cb)  { Cb = cb;   }
	public Carbon  getCg()           { return Cg; }
	public void    setCg(Carbon cg)  { Cg = cg;   }
	public Carbon  getCd()           { return Cd; }
	public void    setCd(Carbon cd)  { Cd = cd;   }
	public Oxygen getOe1()            { return Oe1; }
	public void    setOe1(Oxygen oe1) { Oe1 = oe1;  }
	public Oxygen getOe2()            { return Oe2; }
	public void    setOe2(Oxygen oe2) { Oe2 = oe2;  }
	public BranchGroup getCbCgBond()                 { return CbCgBond; }
	public void    setCbCgBond(BranchGroup CbCgBond) { this.CbCgBond = CbCgBond; } 
	public BranchGroup getCgCdBond()                 { return CgCdBond; }
	public void    setCgCdBond(BranchGroup CgCdBond) { this.CgCdBond = CgCdBond; } 
	public BranchGroup getCdOe1Bond()                  { return CdOe1Bond; }
	public void    setCdOe1Bond(BranchGroup CdOe1Bond) { this.CdOe1Bond = CdOe1Bond; } 
	public BranchGroup getCdOe2Bond()                  { return CdOe2Bond; }
	public void    setCdOe2Bond(BranchGroup CdOe2Bond) { this.CdOe2Bond = CdOe2Bond; } 
	public Shape3D getCbCgBondShape()                      { return CbCgBondShape; }
	public void    setCbCgBondShape(Shape3D CbCgBondShape) { this.CbCgBondShape = CbCgBondShape; }
	public Shape3D getCgCdBondShape()                      { return CgCdBondShape; }
	public void    setCgCdBondShape(Shape3D CgCdBondShape) { this.CgCdBondShape = CgCdBondShape; }
	public Shape3D getCdOe1BondShape()                       { return CdOe1BondShape; }
	public void    setCdOe1BondShape(Shape3D CdOe1BondShape) { this.CdOe1BondShape = CdOe1BondShape; }
	public Shape3D getCdOe2BondShape()                       { return CdOe2BondShape; }
	public void    setCdOe2BondShape(Shape3D CdOe2BondShape) { this.CdOe2BondShape = CdOe2BondShape; }
	
	public void createBranchGroup(AminoAcid aminoAcid, Shape3D shape, ProteinViewer pV) {
	  Cb.setBranchGroup(pV.addSphere(Cb, Carbon.getAtomicRadius()/2, Colors.black));
	  Cg.setBranchGroup(pV.addSphere(Cg, Carbon.getAtomicRadius()/2, Colors.black));
	  Cd.setBranchGroup(pV.addSphere(Cd, Carbon.getAtomicRadius()/2, Colors.black));
	  Oe1.setBranchGroup(pV.addSphere(Oe1, Oxygen.getAtomicRadius()/2, Colors.red));
	  Oe2.setBranchGroup(pV.addSphere(Oe2, Oxygen.getAtomicRadius()/2, Colors.red));
	  setCbCgBond(pV.addBond(Cb, Cg, Colors.black));
	  setCbCgBondShape(shape);
	  setCgCdBond(pV.addBond(Cg, Cd, Colors.black));
	  setCgCdBondShape(shape);
	  setCdOe1Bond(pV.addBond(Cd, Oe1, Colors.black));
	  setCdOe1BondShape(shape);
	  setCdOe2Bond(pV.addBond(Cd, Oe2, Colors.black));
	  setCdOe2BondShape(shape);
	  aminoAcid.setCaCbBond(pV.addBond(aminoAcid.getCa(), Cb, Colors.black));
	  aminoAcid.setCaCbBondShape(shape);
	}
	
	public void detachBranchGroup() {
		Cb.getBranchGroup().detach();
		Cg.getBranchGroup().detach();
		Cd.getBranchGroup().detach();
		Oe1.getBranchGroup().detach();
		Oe2.getBranchGroup().detach();
		CbCgBond.detach();
		CgCdBond.detach();
		CdOe1Bond.detach();
		CdOe2Bond.detach();
		aminoAcid.getCaCbBond().detach();
	}
	public void reattachBranchGroup(TransformGroup spin) {
		spin.addChild(Cb.getBranchGroup());
		spin.addChild(Cg.getBranchGroup());
		spin.addChild(Cd.getBranchGroup());
		spin.addChild(Oe1.getBranchGroup());
		spin.addChild(Oe2.getBranchGroup());
		spin.addChild(CbCgBond);
		spin.addChild(CgCdBond);
		spin.addChild(CdOe1Bond);
		spin.addChild(CdOe2Bond);
		spin.addChild(aminoAcid.getCaCbBond());
	}

	public void draw(J3DScene scene, ChainTree cTree, int i, Carbon Ca) { 
		Point3d CaPos = Ca.getPosition();
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cb.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cg.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cd.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Oe1.getPosition().subtract(CaPos)), Oxygen.vdWRadius/2), Color.red);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Oe2.getPosition().subtract(CaPos)), Oxygen.vdWRadius/2), Color.red);
	}


	
}
