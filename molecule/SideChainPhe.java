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

public class SideChainPhe extends SideChain {
	private Carbon   Cb;
	private Carbon   Cg;
	private Carbon   Cd1;
	private Carbon   Cd2;
	private Carbon   Ce1;
	private Carbon   Ce2;
	private Carbon   Cz;

	BranchGroup CbCgBond   = null;
	BranchGroup CgCd1Bond  = null;
	BranchGroup CgCd2Bond  = null;
	BranchGroup Cd1Ce1Bond = null;
	BranchGroup Cd2Ce2Bond = null;
	BranchGroup Ce1CzBond  = null;
	BranchGroup Ce2CzBond  = null;

	Shape3D CbCgBondShape   = null;
	Shape3D CgCd1BondShape  = null;
	Shape3D CgCd2BondShape  = null;
	Shape3D Cd1Ce1BondShape = null;
	Shape3D Cd2Ce2BondShape = null;
	Shape3D Ce1CzBondShape  = null;
	Shape3D Ce2CzBondShape = null;

	public SideChainPhe(AminoAcid aminoAcid) {
		super(aminoAcid);
		Cb  = new Carbon();
		Cg  = new Carbon();
		Cd1 = new Carbon();;
		Cd2 = new Carbon();
		Ce1 = new Carbon();
		Ce2 = new Carbon();
		Cz  = new Carbon();
	}
	
	public Carbon   getCb()              { return Cb;  }
	public void     setCb(Carbon cb)     { Cb = cb;    }
	public Carbon   getCg()              { return Cg;  }
	public void     setCg(Carbon cg)     { Cg = cg;    }
	public Carbon   getCd1()             { return Cd1; }
	public void     setCd1(Carbon cd1)   { Cd1 = cd1;  }
	public Carbon   getCd2()             { return Cd2; }
	public void     setCd2(Carbon cd2)   { Cd2 = cd2;  }
	public Carbon   getCe1()             { return Ce1; }
	public void     setCe1(Carbon ce1)   { Ce1 = ce1;  }
	public Carbon   getCe2()             { return Ce2; }
	public void     setCe2(Carbon ce2)   { Ce2 = ce2;  }
	public Carbon   getCz()              { return Cz;  }
	public void     setCz(Carbon cz)     { Cz = cz;    }
	
	public BranchGroup getCbCgBond()                     { return CbCgBond; }
	public void    setCbCgBond(BranchGroup CbCgBond)     { this.CbCgBond = CbCgBond; } 
	public BranchGroup getCgCd1Bond()                    { return CgCd1Bond; }
	public void    setCgCd1Bond(BranchGroup CgCd1Bond)   { this.CgCd1Bond = CgCd1Bond; } 
	public BranchGroup getCgCd2Bond()                    { return CgCd2Bond; }
	public void    setCgCd2Bond(BranchGroup CgCd2Bond)   { this.CgCd2Bond = CgCd2Bond; } 
	public BranchGroup getCd1Ce1Bond()                   { return Cd1Ce1Bond; }
	public void    setCd1Ce1Bond(BranchGroup Cd1Ce1Bond) { this.Cd1Ce1Bond = Cd1Ce1Bond; } 
	public BranchGroup getCd2Ce2Bond()                   { return Cd2Ce2Bond; }
	public void    setCd2Ce2Bond(BranchGroup Cd2Ce2Bond) { this.Cd2Ce2Bond = Cd2Ce2Bond; } 
	public BranchGroup getCe1CzBond()                    { return Ce1CzBond; }
	public void    setCe1CzBond(BranchGroup Ce1CzBond)   { this.Ce1CzBond = Ce1CzBond; } 
	public BranchGroup getCe2CzBond()                    { return Ce2CzBond; }
	public void    setCe2CzBond(BranchGroup Ce2CzBond)   { this.Ce2CzBond = Ce2CzBond; } 

	
	public Shape3D getCbCgBondShape()                          { return CbCgBondShape; }
	public void    setCbCgBondShape(Shape3D CbCgBondShape)     { this.CbCgBondShape = CbCgBondShape; } 
	public Shape3D getCgCd1BondShape()                    	   { return CgCd1BondShape; }
	public void    setCgCd1BondShape(Shape3D CgCd1BondShape)   { this.CgCd1BondShape = CgCd1BondShape; } 
	public Shape3D getCgCd2BondShape()                         { return CgCd2BondShape; }
	public void    setCgCd2BondShape(Shape3D CgCd2BondShape)   { this.CgCd2BondShape = CgCd2BondShape; } 
	public Shape3D getCd1Ce1BondShape()                        { return Cd1Ce1BondShape; }
	public void    setCd1Ce1BondShape(Shape3D Cd1Ce1BondShape) { this.Cd1Ce1BondShape = Cd1Ce1BondShape; } 
	public Shape3D getCd2Ce2BondShape()                        { return Cd2Ce2BondShape; }
	public void    setCd2Ce2BondShape(Shape3D Cd2Ce2BondShape) { this.Cd2Ce2BondShape = Cd2Ce2BondShape; } 
	public Shape3D getCe1CzBondShape()                         { return Ce1CzBondShape; }
	public void    setCe1CzBondShape(Shape3D Ce1CzBondShape)   { this.Ce1CzBondShape = Ce1CzBondShape; } 
	public Shape3D getCe2CzBondShape()                         { return Ce2CzBondShape; }
	public void    setCe2CzBondShape(Shape3D Ce2CzBondShape)   { this.Ce2CzBondShape = Ce2CzBondShape; } 

	public void createBranchGroup(AminoAcid aminoAcid, Shape3D shape, ProteinViewer pV) {
	  Cb.setBranchGroup(pV.addSphere(Cb, Carbon.getAtomicRadius()/2, Colors.black));
	  Cg.setBranchGroup(pV.addSphere(Cg, Carbon.getAtomicRadius()/2, Colors.black));
	  Cd1.setBranchGroup(pV.addSphere(Cd1, Carbon.getAtomicRadius()/2, Colors.black));
	  Cd2.setBranchGroup(pV.addSphere(Cd2, Carbon.getAtomicRadius()/2, Colors.black));
	  Ce1.setBranchGroup(pV.addSphere(Ce1, Carbon.getAtomicRadius()/2, Colors.black));
	  Ce2.setBranchGroup(pV.addSphere(Ce2, Carbon.getAtomicRadius()/2, Colors.black));
	  Cz.setBranchGroup(pV.addSphere(Cz,   Carbon.getAtomicRadius()/2, Colors.black));
	  setCbCgBond(pV.addBond(Cb, Cg, Colors.black));		  
	  setCbCgBondShape(shape);
	  setCgCd1Bond(pV.addBond(Cg, Cd1, Colors.black));
	  setCgCd1BondShape(shape);
	  setCgCd2Bond(pV.addBond(Cg, Cd2, Colors.black));
	  setCgCd2BondShape(shape);
	  setCd1Ce1Bond(pV.addBond(Cd1, Ce1, Colors.black));
	  setCd1Ce1BondShape(shape);
	  setCd2Ce2Bond(pV.addBond(Cd2, Ce2, Colors.black));
	  setCd2Ce2BondShape(shape);
	  setCe1CzBond(pV.addBond(Ce1, Cz, Colors.black));
	  setCe1CzBondShape(shape);
	  setCe2CzBond(pV.addBond(Ce2, Cz, Colors.black));
	  setCe2CzBondShape(shape);
	  aminoAcid.setCaCbBond(pV.addBond(aminoAcid.getCa(), Cb, Colors.black));
	  aminoAcid.setCaCbBondShape(shape);
	}
	
	public void detachBranchGroup() {
		Cb.getBranchGroup().detach();
		Cg.getBranchGroup().detach();
		Cd1.getBranchGroup().detach();
		Cd2.getBranchGroup().detach();
		Ce1.getBranchGroup().detach();
		Ce2.getBranchGroup().detach();
		Cz.getBranchGroup().detach();
		CbCgBond.detach();		  
		CgCd1Bond.detach();
		CgCd2Bond.detach();
		Cd1Ce1Bond.detach();
		Cd2Ce2Bond.detach();
		Ce1CzBond.detach();
		Ce2CzBond.detach();
		aminoAcid.getCaCbBond().detach();
	}
	public void reattachBranchGroup(TransformGroup spin) {
		spin.addChild(Cb.getBranchGroup());
		spin.addChild(Cg.getBranchGroup());
		spin.addChild(Cd1.getBranchGroup());
		spin.addChild(Cd2.getBranchGroup());
		spin.addChild(Ce1.getBranchGroup());
		spin.addChild(Ce2.getBranchGroup());
		spin.addChild(Cz.getBranchGroup());
		spin.addChild(CbCgBond);		  
		spin.addChild(CgCd1Bond);
		spin.addChild(CgCd2Bond);
		spin.addChild(Cd1Ce1Bond);
		spin.addChild(Cd2Ce2Bond);
		spin.addChild(Ce1CzBond);
		spin.addChild(Ce2CzBond);
		spin.addChild(aminoAcid.getCaCbBond());
	}


	public void draw(J3DScene scene, ChainTree cTree, int i, Carbon Ca) { 
		Point3d CaPos = Ca.getPosition();
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cb.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cg.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cd1.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cd2.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Ce1.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Ce2.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cz.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
	}

	
}
