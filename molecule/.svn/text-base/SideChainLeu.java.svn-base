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

public class SideChainLeu extends SideChain {
	private Carbon  Cb;
	private Carbon  Cg;
	private Carbon Cd1;
	private Carbon  Cd2;
	
	BranchGroup CbCgBond  = null;
	BranchGroup CgCd1Bond  = null;
	BranchGroup CgCd2Bond  = null;
	Shape3D CbCgBondShape = null;
	Shape3D CgCd1BondShape = null;
	Shape3D CgCd2BondShape = null;
	
	public SideChainLeu(AminoAcid aminoAcid) {
		super(aminoAcid);
		Cb = new Carbon();
		Cg = new Carbon();
		Cd1 = new Carbon();
		Cd2 = new Carbon();
	}
	
	public Carbon  getCb()           { return Cb; }
	public void    setCb(Carbon cb)  { Cb = cb;   }
	public Carbon  getCg()           { return Cg; }
	public void    setCg(Carbon cg)  { Cg = cg;   }
	public Carbon  getCd1()           { return Cd1; }
	public void    setCd1(Carbon cd1)  { Cd1 = cd1;   }
	public Carbon  getCd2()           { return Cd2; }
	public void    setCd2(Carbon cd2)  { Cd2 = cd2;   }
	public BranchGroup getCbCgBond()                 { return CbCgBond; }
	public void    setCbCgBond(BranchGroup CbCgBond) { this.CbCgBond = CbCgBond; } 
	public BranchGroup getCgCd1Bond()                 { return CgCd1Bond; }
	public void    setCgCd1Bond(BranchGroup CgCd1Bond) { this.CgCd1Bond = CgCd1Bond; } 
	public BranchGroup getCgCd2Bond()                 { return CgCd2Bond; }
	public void    setCgCd2Bond(BranchGroup CgCd2Bond) { this.CgCd2Bond = CgCd2Bond; } 
	public Shape3D getCbCgBondShape()                      { return CbCgBondShape; }
	public void    setCbCgBondShape(Shape3D CbCgBondShape) { this.CbCgBondShape = CbCgBondShape; }
	public Shape3D getCgCd1BondShape()                      { return CgCd1BondShape; }
	public void    setCgCd1BondShape(Shape3D CgCd1BondShape) { this.CgCd1BondShape = CgCd1BondShape; }
	public Shape3D getCgCd2BondShape()                      { return CgCd2BondShape; }
	public void    setCgCd2BondShape(Shape3D CgCd2BondShape) { this.CgCd2BondShape = CgCd2BondShape; }
	
	public void createBranchGroup(AminoAcid aminoAcid, Shape3D shape, ProteinViewer pV) {
		Cb.setBranchGroup(pV.addSphere(Cb, Carbon.getAtomicRadius()/2, Colors.black));
		Cg.setBranchGroup(pV.addSphere(Cg, Carbon.getAtomicRadius()/2, Colors.black));
		Cd1.setBranchGroup(pV.addSphere(Cd1, Carbon.getAtomicRadius()/2, Colors.black));
		Cd2.setBranchGroup(pV.addSphere(Cd2, Carbon.getAtomicRadius()/2, Colors.black));
		setCbCgBond(pV.addBond(Cb, Cg, Colors.black));
		setCbCgBondShape(shape);
		setCgCd1Bond(pV.addBond(Cg, Cd1, Colors.black));
		setCgCd1BondShape(shape);
		setCgCd2Bond(pV.addBond(Cg, Cd2, Colors.black));
		setCgCd2BondShape(shape);
		aminoAcid.setCaCbBond(pV.addBond(aminoAcid.getCa(), Cb, Colors.black));
		aminoAcid.setCaCbBondShape(shape);
	}
	
	public void detachBranchGroup() {
		Cb.getBranchGroup().detach();
		Cg.getBranchGroup().detach();
		Cd1.getBranchGroup().detach();
		Cd2.getBranchGroup().detach();
		CbCgBond.detach();		  
		CgCd1Bond.detach();
		CgCd2Bond.detach();
		aminoAcid.getCaCbBond().detach();
	}
	public void reattachBranchGroup(TransformGroup spin) {
		spin.addChild(Cb.getBranchGroup());
		spin.addChild(Cg.getBranchGroup());
		spin.addChild(Cd1.getBranchGroup());
		spin.addChild(Cd2.getBranchGroup());
		spin.addChild(CbCgBond);		  
		spin.addChild(CgCd1Bond);
		spin.addChild(CgCd2Bond);
		spin.addChild(aminoAcid.getCaCbBond());
	}

	public void draw(J3DScene scene, ChainTree cTree, int i, Carbon Ca) { 
		Point3d CaPos = Ca.getPosition();
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cb.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cg.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cd1.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cd2.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
	}

	
}
