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

public class SideChainHis extends SideChain {
	private Carbon   Cb;
	private Carbon   Cg;
	private Nitrogen Nd1;
	private Carbon   Cd2;
	private Carbon   Ce1;
	private Nitrogen Ne2;

	BranchGroup CbCgBond    = null;
	BranchGroup CgNd1Bond   = null;
	BranchGroup CgCd2Bond   = null;
	BranchGroup Nd1Ce1Bond  = null;
	BranchGroup Cd2Ne2Bond  = null;
	BranchGroup Ce1Ne2Bond  = null;
	Shape3D CbCgBondShape   = null;
	Shape3D CgNd1BondShape  = null;
	Shape3D CgCd2BondShape  = null;
	Shape3D Nd1Ce1BondShape = null;
	Shape3D Cd2Ne2BondShape = null;
	Shape3D Ce1Ne2BondShape = null;
	
	public SideChainHis(AminoAcid aminoAcid) {
		super(aminoAcid);
		Cb  = new Carbon();
		Cg  = new Carbon();
		Nd1 = new Nitrogen();
		Cd2 = new Carbon();
		Ce1 = new Carbon();
		Ne2 = new Nitrogen();
	}
	
	public Carbon   getCb()              { return Cb;  }
	public void     setCb(Carbon cb)     { Cb = cb;    }
	public Carbon   getCg()              { return Cg;  }
	public void     setCg(Carbon cg)     { Cg = cg;    }
	public Nitrogen getNd1()             { return Nd1; }
	public void     setNd1(Nitrogen nd1) { Nd1 = nd1;  }
	public Carbon   getCd2()             { return Cd2; }
	public void     setCd2(Carbon cd2)   { Cd2 = cd2;  }
	public Carbon   getCe1()             { return Ce1; }
	public void     setCe1(Carbon ce1)   { Ce1 = ce1;  }
	public Nitrogen getNe2()             { return Ne2; }
	public void     setNe2(Nitrogen ne2) { Ne2 = ne2;  }
	public BranchGroup getCbCgBond()                     { return CbCgBond; }
	public void    setCbCgBond(BranchGroup CbCgBond)     { this.CbCgBond = CbCgBond; } 
	public BranchGroup getCgNd1Bond()                    { return CgNd1Bond; }
	public void    setCgNd1Bond(BranchGroup CgNd1Bond)   { this.CgNd1Bond = CgNd1Bond; } 
	public BranchGroup getCgCd2Bond()                    { return CgCd2Bond; }
	public void    setCgCd2Bond(BranchGroup CgCd2Bond)   { this.CgCd2Bond = CgCd2Bond; } 
	public BranchGroup getNd1Ce1Bond()                   { return Nd1Ce1Bond; }
	public void    setNd1Ce1Bond(BranchGroup Nd1Ce1Bond) { this.Nd1Ce1Bond = Nd1Ce1Bond; } 
	public BranchGroup getCd2Ne2Bond()                   { return Cd2Ne2Bond; }
	public void    setCd2Ne2Bond(BranchGroup Cd2Ne2Bond) { this.Cd2Ne2Bond = Cd2Ne2Bond; } 
	public BranchGroup getCe1Ne2Bond()                   { return Ce1Ne2Bond; }
	public void    setCe1Ne2Bond(BranchGroup Ce1Ne2Bond) { this.Ce1Ne2Bond = Ce1Ne2Bond; } 
	public Shape3D getCbCgBondShape()                          { return CbCgBondShape; }
	public void    setCbCgBondShape(Shape3D CbCgBondShape)     { this.CbCgBondShape = CbCgBondShape; }
	public Shape3D getCgNd1BondShape()                         { return CgNd1BondShape; }
	public void    setCgNd1BondShape(Shape3D CgNd1BondShape)   { this.CgNd1BondShape = CgNd1BondShape; }
	public Shape3D getCgCd2BondShape()                         { return CgCd2BondShape; }
	public void    setCgCd2BondShape(Shape3D CgCd2BondShape)   { this.CgCd2BondShape = CgCd2BondShape; }
	public Shape3D getNd1Ce1BondShape()                        { return Nd1Ce1BondShape; }
	public void    setNd1Ce1BondShape(Shape3D Nd1Ce1BondShape) { this.Nd1Ce1BondShape = Nd1Ce1BondShape; }
	public Shape3D getCd2Ne2BondShape()                        { return Cd2Ne2BondShape; }
	public void    setCd2Ne2BondShape(Shape3D Cd2Ne2BondShape) { this.Cd2Ne2BondShape = Cd2Ne2BondShape; }
	public Shape3D getCe1Ne2BondShape()                        { return Ce1Ne2BondShape; }
	public void    setCe1Ne2BondShape(Shape3D Ce1Ne2BondShape) { this.Ce1Ne2BondShape = Ce1Ne2BondShape; }

	public void createBranchGroup(AminoAcid aminoAcid, Shape3D shape, ProteinViewer pV) {
	  Cb.setBranchGroup(pV.addSphere(Cb, Carbon.getAtomicRadius()/2, Colors.black));
	  Cg.setBranchGroup(pV.addSphere(Cg, Carbon.getAtomicRadius()/2, Colors.black));
	  Nd1.setBranchGroup(pV.addSphere(Nd1, Nitrogen.getAtomicRadius()/2, Colors.blue));
	  Cd2.setBranchGroup(pV.addSphere(Cd2, Carbon.getAtomicRadius()/2, Colors.black));
	  Ce1.setBranchGroup(pV.addSphere(Ce1, Carbon.getAtomicRadius()/2, Colors.black));
	  Ne2.setBranchGroup(pV.addSphere(Ne2, Nitrogen.getAtomicRadius()/2, Colors.blue));
	  setCbCgBond(pV.addBond(Cb, Cg, Colors.black));
	  setCbCgBondShape(shape);
	  setCgNd1Bond(pV.addBond(Cg, Nd1, Colors.black));
	  setCgNd1BondShape(shape);
	  setCgCd2Bond(pV.addBond(Cg, Cd2, Colors.black));
	  setCgCd2BondShape(shape);
	  setNd1Ce1Bond(pV.addBond(Nd1, Ce1, Colors.black));
	  setNd1Ce1BondShape(shape);
	  setCd2Ne2Bond(pV.addBond(Cd2, Ne2, Colors.black));
	  setCd2Ne2BondShape(shape);
	  setCe1Ne2Bond(pV.addBond(Ce1, Ne2, Colors.black));
	  setCe1Ne2BondShape(shape);
	  aminoAcid.setCaCbBond(pV.addBond(aminoAcid.getCa(), Cb, Colors.black));
	  aminoAcid.setCaCbBondShape(shape);
	}
	
	public void detachBranchGroup() {
		Cb.getBranchGroup().detach();
		Cg.getBranchGroup().detach();
		Nd1.getBranchGroup().detach();
		Cd2.getBranchGroup().detach();
		Ce1.getBranchGroup().detach();
		Ne2.getBranchGroup().detach();
		CbCgBond.detach();		  
		CgNd1Bond.detach();
		CgCd2Bond.detach();
		Nd1Ce1Bond.detach();
		Cd2Ne2Bond.detach();
		Ce1Ne2Bond.detach();
		aminoAcid.getCaCbBond().detach();
	}

	public void reattachBranchGroup(TransformGroup spin) {
		spin.addChild(Cb.getBranchGroup());
		spin.addChild(Cg.getBranchGroup());
		spin.addChild(Nd1.getBranchGroup());
		spin.addChild(Cd2.getBranchGroup());
		spin.addChild(Ce1.getBranchGroup());
		spin.addChild(Ne2.getBranchGroup());
		spin.addChild(CbCgBond);		  
		spin.addChild(CgNd1Bond);
		spin.addChild(CgCd2Bond);
		spin.addChild(Nd1Ce1Bond);
		spin.addChild(Cd2Ne2Bond);
		spin.addChild(Ce1Ne2Bond);
		spin.addChild(aminoAcid.getCaCbBond());
	}

	public void draw(J3DScene scene, ChainTree cTree, int i, Carbon Ca) { 
		Point3d CaPos = Ca.getPosition();
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cb.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cg.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Nd1.getPosition().subtract(CaPos)), Nitrogen.vdWRadius/2), Color.blue);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cd2.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Ce1.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Ne2.getPosition().subtract(CaPos)), Nitrogen.vdWRadius/2), Color.blue);
	}

	
}
