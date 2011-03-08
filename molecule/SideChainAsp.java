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

public class SideChainAsp extends SideChain {
	private Carbon  Cb;
	private Carbon  Cg;
	private Oxygen  Od1;
	private Oxygen  Od2;
	
	BranchGroup CbCgBond  = null;
	BranchGroup CgOd1Bond  = null;
	BranchGroup CgOd2Bond  = null;
	Shape3D CbCgBondShape = null;
	Shape3D CgOd1BondShape = null;
	Shape3D CgOd2BondShape = null;
	
	public SideChainAsp(AminoAcid aminoAcid) {
		super(aminoAcid);
		Cb = new Carbon();
		Cg = new Carbon();
		Od1 = new Oxygen();
		Od2 = new Oxygen();
	}
	
	public Carbon  getCb()           { return Cb; }
	public void    setCb(Carbon cb)  { Cb = cb;   }
	public Carbon  getCg()           { return Cg; }
	public void    setCg(Carbon cg)  { Cg = cg;   }
	public Oxygen  getOd1()          { return Od1; }
	public void    setOd1(Oxygen od1)  { Od1 = od1;   }
	public Oxygen  getOd2()           { return Od2; }
	public void    setOd2(Oxygen od2)  { Od2 = od2;   }
	public BranchGroup getCbCgBond()                 { return CbCgBond; }
	public void    setCbCgBond(BranchGroup CbCgBond) { this.CbCgBond = CbCgBond; } 
	public BranchGroup getCgOd1Bond()                 { return CgOd1Bond; }
	public void    setCgOd1Bond(BranchGroup CgOd1Bond) { this.CgOd1Bond = CgOd1Bond; } 
	public BranchGroup getCgOd2Bond()                 { return CgOd2Bond; }
	public void    setCgOd2Bond(BranchGroup CgOd2Bond) { this.CgOd2Bond = CgOd2Bond; } 
	public Shape3D getCbCgBondShape()                      { return CbCgBondShape; }
	public void    setCbCgBondShape(Shape3D CbCgBondShape) { this.CbCgBondShape = CbCgBondShape; }
	public Shape3D getCgOd1BondShape()                      { return CgOd1BondShape; }
	public void    setCgOd1BondShape(Shape3D CgOd1BondShape) { this.CgOd1BondShape = CgOd1BondShape; }
	public Shape3D getCgOd2BondShape()                      { return CgOd2BondShape; }
	public void    setCgOd2BondShape(Shape3D CgOd2BondShape) { this.CgOd2BondShape = CgOd2BondShape; }

	public void createBranchGroup(AminoAcid aminoAcid, Shape3D shape, ProteinViewer pV) {
		Cb.setBranchGroup(pV.addSphere( Cb,  Carbon.getAtomicRadius()/2, Colors.black));
		Cg.setBranchGroup(pV.addSphere( Cg,  Carbon.getAtomicRadius()/2, Colors.black));
		Od1.setBranchGroup(pV.addSphere(Od1, Oxygen.getAtomicRadius()/2, Colors.red));
		Od2.setBranchGroup(pV.addSphere(Od2, Oxygen.getAtomicRadius()/2, Colors.red));
		setCbCgBond(pV.addBond( Cb, Cg,  Colors.black));
		setCbCgBondShape(shape);
		setCgOd1Bond(pV.addBond(Cg, Od1, Colors.black));
		setCgOd1BondShape(shape);
		setCgOd2Bond(pV.addBond(Cg, Od2, Colors.black));
		setCgOd2BondShape(shape);
		aminoAcid.setCaCbBond(pV.addBond(aminoAcid.getCa(), Cb, Colors.black));
		aminoAcid.setCaCbBondShape(shape);
	}
	
	public void detachBranchGroup() {
		Cb.getBranchGroup().detach();
		Cg.getBranchGroup().detach();
		Od1.getBranchGroup().detach();
		Od2.getBranchGroup().detach();
		CbCgBond.detach();
		CgOd1Bond.detach();
		CgOd2Bond.detach();
		aminoAcid.getCaCbBond().detach();
	}
	
	public void reattachBranchGroup(TransformGroup spin) {
		spin.addChild(Cb.getBranchGroup());
		spin.addChild(Cg.getBranchGroup());
		spin.addChild(Od1.getBranchGroup());
		spin.addChild(Od2.getBranchGroup());
		spin.addChild(CbCgBond);
		spin.addChild(CgOd1Bond);
		spin.addChild(CgOd2Bond);
		spin.addChild(aminoAcid.getCaCbBond());
	}

	public void draw(J3DScene scene, ChainTree cTree, int i, Carbon Ca) { 
		Point3d CaPos = Ca.getPosition();
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cb.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cg.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Od1.getPosition().subtract(CaPos)), Oxygen.vdWRadius/2), Color.red);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Od2.getPosition().subtract(CaPos)), Oxygen.vdWRadius/2), Color.red);
	}

}
