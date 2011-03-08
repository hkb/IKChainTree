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

public class SideChainTrp extends SideChain {
	private Carbon   Cb;
	private Carbon   Cg;
	private Carbon   Cd1;
	private Carbon   Cd2;
	private Nitrogen Ne1;
	private Carbon   Ce2;
	private Carbon   Ce3;
	private Carbon   Cz2;
	private Carbon   Cz3;
	private Carbon   Ch2;

	BranchGroup CbCgBond    = null;
	BranchGroup CgCd1Bond   = null;
	BranchGroup CgCd2Bond   = null;
	BranchGroup Cd1Ne1Bond  = null;
	BranchGroup Ne1Ce2Bond  = null;
	BranchGroup Cd2Ce2Bond  = null;
	BranchGroup Cd2Ce3Bond  = null;
	BranchGroup Ce2Cz2Bond  = null;
	BranchGroup Ce3Cz3Bond  = null;
	BranchGroup Cz2Ch2Bond  = null;
	BranchGroup Cz3Ch2Bond  = null;

	Shape3D CbCgBondShape   = null;
	Shape3D CgCd1BondShape  = null;
	Shape3D CgCd2BondShape  = null;
	Shape3D Cd1Ne1BondShape = null;
	Shape3D Ne1Ce2BondShape = null;
	Shape3D Cd2Ce2BondShape = null;
	Shape3D Cd2Ce3BondShape  = null;
	Shape3D Ce2Cz2BondShape  = null;
	Shape3D Ce3Cz3BondShape = null;
	Shape3D Cz2Ch2BondShape = null;
	Shape3D Cz3Ch2BondShape = null;

	public SideChainTrp(AminoAcid aminoAcid) {
		super(aminoAcid);
		Cb  = new Carbon();
		Cg  = new Carbon();
		Cd1 = new Carbon();;
		Ne1 = new Nitrogen();
		Cd2 = new Carbon();
		Ce2 = new Carbon();
		Ce3 = new Carbon();
		Cz2 = new Carbon();
		Cz3 = new Carbon();
		Ch2 = new Carbon();
	}
	
	public Carbon   getCb()              { return Cb;  }
	public void     setCb(Carbon cb)     { Cb = cb;    }
	public Carbon   getCg()              { return Cg;  }
	public void     setCg(Carbon cg)     { Cg = cg;    }
	public Carbon   getCd1()             { return Cd1; }
	public void     setCd1(Carbon cd1)   { Cd1 = cd1;  }
	public Nitrogen getNe1()             { return Ne1; }
	public void     setNe1(Nitrogen ne1) { Ne1 = ne1;  }
	public Carbon   getCd2()             { return Cd2; }
	public void     setCd2(Carbon cd2)   { Cd2 = cd2;  }
	public Carbon   getCe2()             { return Ce2; }
	public void     setCe2(Carbon ce2)   { Ce2 = ce2;  }
	public Carbon   getCe3()             { return Ce3; }
	public void     setCe3(Carbon ce3)   { Ce3 = ce3;  }
	public Carbon   getCz2()             { return Cz2; }
	public void     setCz2(Carbon cz2)   { Cz2 = cz2;  }
	public Carbon   getCz3()             { return Cz3; }
	public void     setCz3(Carbon cz3)   { Cz3 = cz3;  }
	public Carbon   getCh2()             { return Ch2; }
	public void     setCh2(Carbon ch2)   { Ch2 = ch2;  }

	
	public BranchGroup getCbCgBond()                     { return CbCgBond; }
	public void    setCbCgBond(BranchGroup CbCgBond)     { this.CbCgBond = CbCgBond; } 
	public BranchGroup getCgCd1Bond()                    { return CgCd1Bond; }
	public void    setCgCd1Bond(BranchGroup CgCd1Bond)   { this.CgCd1Bond = CgCd1Bond; } 
	public BranchGroup getCgCd2Bond()                    { return CgCd2Bond; }
	public void    setCgCd2Bond(BranchGroup CgCd2Bond)   { this.CgCd2Bond = CgCd2Bond; } 
	public BranchGroup getCd1Ne1Bond()                   { return Cd1Ne1Bond; }
	public void    setCd1Ne1Bond(BranchGroup Cd1Ne1Bond) { this.Cd1Ne1Bond = Cd1Ne1Bond; } 
	public BranchGroup getNe1Ce2Bond()                   { return Ne1Ce2Bond; }
	public void    setNe1Ce2Bond(BranchGroup Ne1Ce2Bond) { this.Ne1Ce2Bond = Ne1Ce2Bond; } 
	public BranchGroup getCd2Ce2Bond()                   { return Cd2Ce2Bond; }
	public void    setCd2Ce2Bond(BranchGroup Cd2Ce2Bond) { this.Cd2Ce2Bond = Cd2Ce2Bond; } 
	public BranchGroup getCd2Ce3Bond()                   { return Cd2Ce3Bond; }
	public void    setCd2Ce3Bond(BranchGroup Cd2Ce3Bond) { this.Cd2Ce3Bond = Cd2Ce3Bond; } 
	public BranchGroup getCe2Cz2Bond()                   { return Ce2Cz2Bond; }
	public void    setCe2Cz2Bond(BranchGroup Ce2Cz2Bond) { this.Ce2Cz2Bond = Ce2Cz2Bond; } 
	public BranchGroup getCe3Cz3Bond()                   { return Ce3Cz3Bond; }
	public void    setCe3Cz3Bond(BranchGroup Ce3Cz3Bond) { this.Ce3Cz3Bond = Ce3Cz3Bond; } 
	public BranchGroup getCz2Ch2Bond()                   { return Cz2Ch2Bond; }
	public void    setCz2Ch2Bond(BranchGroup Cz2Ch2Bond) { this.Cz2Ch2Bond = Cz2Ch2Bond; } 
	public BranchGroup getCz3Ch2Bond()                   { return Cz3Ch2Bond; }
	public void    setCz3Ch2Bond(BranchGroup Cz3Ch2Bond) { this.Cz3Ch2Bond = Cz3Ch2Bond; } 

	
	public Shape3D getCbCgBondShape()                          { return CbCgBondShape; }
	public void    setCbCgBondShape(Shape3D CbCgBondShape)     { this.CbCgBondShape = CbCgBondShape; } 
	public Shape3D getCgCd1BondShape()                    	   { return CgCd1BondShape; }
	public void    setCgCd1BondShape(Shape3D CgCd1BondShape)   { this.CgCd1BondShape = CgCd1BondShape; } 
	public Shape3D getCgCd2BondShape()                         { return CgCd2BondShape; }
	public void    setCgCd2BondShape(Shape3D CgCd2BondShape)   { this.CgCd2BondShape = CgCd2BondShape; } 
	public Shape3D getCd1Ne1BondShape()                        { return Cd1Ne1BondShape; }
	public void    setCd1Ne1BondShape(Shape3D Cd1Ne1BondShape) { this.Cd1Ne1BondShape = Cd1Ne1BondShape; } 
	public Shape3D getNe1Ce2BondShape()                        { return Ne1Ce2BondShape; }
	public void    setNe1Ce2BondShape(Shape3D Ne1Ce2BondShape) { this.Ne1Ce2BondShape = Ne1Ce2BondShape; } 
	public Shape3D getCd2Ce2BondShape()                        { return Cd2Ce2BondShape; }
	public void    setCd2Ce2BondShape(Shape3D Cd2Ce2BondShape) { this.Cd2Ce2BondShape = Cd2Ce2BondShape; } 
	public Shape3D getCd2Ce3BondShape()                        { return Cd2Ce3BondShape; }
	public void    setCd2Ce3BondShape(Shape3D Cd2Ce3BondShape) { this.Cd2Ce3BondShape = Cd2Ce3BondShape; } 
	public Shape3D getCe2Cz2BondShape()                        { return Ce2Cz2BondShape; }
	public void    setCe2Cz2BondShape(Shape3D Ce2Cz2BondShape) { this.Ce2Cz2BondShape = Ce2Cz2BondShape; } 
	public Shape3D getCe3Cz3BondShape()                        { return Ce3Cz3BondShape; }
	public void    setCe3Cz3BondShape(Shape3D Ce3Cz3BondShape) { this.Ce3Cz3BondShape = Ce3Cz3BondShape; } 
	public Shape3D getCz2Ch2BondShape()                        { return Cz2Ch2BondShape; }
	public void    setCz2Ch2BondShape(Shape3D Cz2Ch2BondShape) { this.Cz2Ch2BondShape = Cz2Ch2BondShape; } 
	public Shape3D getCz3Ch2BondShape()                        { return Cz3Ch2BondShape; }
	public void    setCz3Ch2BondShape(Shape3D Cz3Ch2BondShape) { this.Cz3Ch2BondShape = Cz3Ch2BondShape; } 
	
	public void createBranchGroup(AminoAcid aminoAcid, Shape3D shape, ProteinViewer pV) {	
		Cb.setBranchGroup(pV.addSphere(Cb, Carbon.getAtomicRadius()/2, Colors.black));
		Cg.setBranchGroup(pV.addSphere(Cg, Carbon.getAtomicRadius()/2, Colors.black));
		Cd1.setBranchGroup(pV.addSphere(Cd1, Carbon.getAtomicRadius()/2, Colors.black));
		Cd2.setBranchGroup(pV.addSphere(Cd2, Carbon.getAtomicRadius()/2, Colors.black));
		Ne1.setBranchGroup(pV.addSphere(Ne1, Nitrogen.getAtomicRadius()/2, Colors.blue));
		Ce2.setBranchGroup(pV.addSphere(Ce2, Carbon.getAtomicRadius()/2, Colors.black));
		Ce3.setBranchGroup(pV.addSphere(Ce3, Carbon.getAtomicRadius()/2, Colors.black));
		Cz2.setBranchGroup(pV.addSphere(Cz2, Carbon.getAtomicRadius()/2, Colors.black));
		Cz3.setBranchGroup(pV.addSphere(Cz3, Carbon.getAtomicRadius()/2, Colors.black));
		Ch2.setBranchGroup(pV.addSphere(Ch2, Carbon.getAtomicRadius()/2, Colors.black));
		setCbCgBond(pV.addBond(Cb, Cg, Colors.black));
		setCbCgBondShape(shape);
		setCgCd1Bond(pV.addBond(Cg, Cd1, Colors.black));
		setCgCd1BondShape(shape);
		setCgCd2Bond(pV.addBond(Cg, Cd2, Colors.black));
		setCgCd2BondShape(shape);
		setNe1Ce2Bond(pV.addBond(Ne1, Ce2, Colors.black));
		setNe1Ce2BondShape(shape);
		setCd2Ce2Bond(pV.addBond(Cd2, Ce2, Colors.black));
		setCd2Ce2BondShape(shape);
		setCd2Ce3Bond(pV.addBond(Cd2, Ce3, Colors.black));
		setCd2Ce3BondShape(shape);
		setCe2Cz2Bond(pV.addBond(Ce2, Cz2, Colors.black));
		setCe2Cz2BondShape(shape);
		setCe3Cz3Bond(pV.addBond(Ce3, Cz3, Colors.black));
		setCe3Cz3BondShape(shape);
		setCz2Ch2Bond(pV.addBond(Cz2, Ch2, Colors.black));
		setCz2Ch2BondShape(shape);
		setCz3Ch2Bond(pV.addBond(Cz3, Ch2, Colors.black));
		setCz3Ch2BondShape(shape);
		aminoAcid.setCaCbBond(pV.addBond(aminoAcid.getCa(), Cb, Colors.black));
		aminoAcid.setCaCbBondShape(shape);
	}
	
	public void detachBranchGroup() {
		Cb.getBranchGroup().detach();
		Cg.getBranchGroup().detach();
		Cd1.getBranchGroup().detach();
		Cd2.getBranchGroup().detach();
		Ne1.getBranchGroup().detach();
		Ce2.getBranchGroup().detach();
		Ce3.getBranchGroup().detach();
		Cz2.getBranchGroup().detach();
		Cz3.getBranchGroup().detach();
		Ch2.getBranchGroup().detach();
		CbCgBond.detach();		  
		CgCd1Bond.detach();
		CgCd2Bond.detach();
		Ne1Ce2Bond.detach();
		Cd2Ce2Bond.detach();
		Cd2Ce3Bond.detach();
		Ce2Cz2Bond.detach();
		Ce3Cz3Bond.detach();
		Cz2Ch2Bond.detach();
		Cz3Ch2Bond.detach();
		aminoAcid.getCaCbBond().detach();
	}
	public void reattachBranchGroup(TransformGroup spin) {
		spin.addChild(Cb.getBranchGroup());
		spin.addChild(Cg.getBranchGroup());
		spin.addChild(Cd1.getBranchGroup());
		spin.addChild(Cd2.getBranchGroup());
		spin.addChild(Ne1.getBranchGroup());
		spin.addChild(Ce2.getBranchGroup());
		spin.addChild(Ce3.getBranchGroup());
		spin.addChild(Cz2.getBranchGroup());
		spin.addChild(Cz3.getBranchGroup());
		spin.addChild(Ch2.getBranchGroup());
		spin.addChild(CbCgBond);		  
		spin.addChild(CgCd1Bond);
		spin.addChild(CgCd2Bond);
		spin.addChild(Ne1Ce2Bond);
		spin.addChild(Cd2Ce2Bond);
		spin.addChild(Cd2Ce3Bond);
		spin.addChild(Ce2Cz2Bond);
		spin.addChild(Ce3Cz3Bond);
		spin.addChild(Cz2Ch2Bond);
		spin.addChild(Cz3Ch2Bond);
		spin.addChild(aminoAcid.getCaCbBond());
	}

	public void draw(J3DScene scene, ChainTree cTree, int i, Carbon Ca) { 
		Point3d CaPos = Ca.getPosition();
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cb.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cg.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cd1.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cd2.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Ne1.getPosition().subtract(CaPos)), Nitrogen.vdWRadius/2), Color.blue);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Ce2.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Ce3.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cz2.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cz3.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Ch2.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
	}

	
}
