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

public class SideChainThr extends SideChain {
	private Carbon Cb;
	private Oxygen Og1;
	private Carbon Cg2;
//	private Oxygen Oxt;   // not standard, found in f.ex. 1A0A
	
	BranchGroup CbOg1Bond  = null;
	BranchGroup CbCg2Bond  = null;
	Shape3D CbOg1BondShape = null;
	Shape3D CbCg2BondShape = null;
	
	public SideChainThr(AminoAcid aminoAcid) {
		super(aminoAcid);
		Cb = new Carbon();
		Og1 = new Oxygen();
		Cg2 = new Carbon();
//		Oxt = new Oxygen();
	}
	
	public Carbon  getCb()            { return Cb; }
	public void    setCb(Carbon cb)   { Cb = cb;   }
	public Oxygen  getOg1()           { return Og1; }
	public void    setOg1(Oxygen og1) { Og1 = og1;   }
	public Carbon  getCg2()           { return Cg2; }
	public void    setCg2(Carbon cg2) { Cg2 = cg2;   }
//	public Oxygen  getOxt()           { return Oxt; }
//	public void    setOxt(Oxygen oxt) { Oxt = oxt;   }
	public BranchGroup getCbOg1Bond()                        { return CbOg1Bond; }
	public void    setCbOg1Bond(BranchGroup CbOg1Bond)       { this.CbOg1Bond = CbOg1Bond; } 
	public BranchGroup getCbCg2Bond()                        { return CbCg2Bond; }
	public void    setCbCg2Bond(BranchGroup CbCg2Bond)       { this.CbCg2Bond = CbCg2Bond; } 
	public Shape3D getCbOg1BondShape()                       { return CbOg1BondShape; }
	public void    setCbOg1BondShape(Shape3D CbOg1BondShape) { this.CbOg1BondShape = CbOg1BondShape; }
	public Shape3D getCbCg2BondShape()                       { return CbCg2BondShape; }
	public void    setCbCg2BondShape(Shape3D CbCg2BondShape) { this.CbCg2BondShape = CbCg2BondShape; }

	public void createBranchGroup(AminoAcid aminoAcid, Shape3D shape, ProteinViewer pV) {	
		Cb.setBranchGroup(pV.addSphere(Cb, Carbon.getAtomicRadius()/2, Colors.black));
		Og1.setBranchGroup(pV.addSphere(Og1, Oxygen.getAtomicRadius()/2, Colors.red));
		Cg2.setBranchGroup(pV.addSphere(Cg2, Carbon.getAtomicRadius()/2, Colors.black));
//		Oxt.setBranchGroup(pV.addSphere(Oxt, Oxygen.getAtomicRadius()/2, Colors.red));
		setCbOg1Bond(pV.addBond(Cb, Og1, Colors.black));
		setCbOg1BondShape(shape);
		setCbCg2Bond(pV.addBond(Cb, Cg2, Colors.black));
		setCbCg2BondShape(shape);
		aminoAcid.setCaCbBond(pV.addBond(aminoAcid.getCa(), Cb, Colors.black));
		aminoAcid.setCaCbBondShape(shape);
	}
	
	public void detachBranchGroup() {
		Cb.getBranchGroup().detach();
		Og1.getBranchGroup().detach();
		Cg2.getBranchGroup().detach();
//		Oxt.getBranchGroup().detach();
		CbOg1Bond.detach();		  
		CbCg2Bond.detach();
		aminoAcid.getCaCbBond().detach();
	}

	public void reattach(TransformGroup spin) {
		spin.addChild(Cb.getBranchGroup());
		spin.addChild(Og1.getBranchGroup());
		spin.addChild(Cg2.getBranchGroup());
//		spin.addChild(Oxt.getBranchGroup());
		spin.addChild(CbOg1Bond);		  
		spin.addChild(CbCg2Bond);
		spin.addChild(aminoAcid.getCaCbBond());

	}
	
	public void draw(J3DScene scene, ChainTree cTree, int i, Carbon Ca) { 
		Point3d CaPos = Ca.getPosition();
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cb.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Og1.getPosition().subtract(CaPos)), Oxygen.vdWRadius/2), Color.red);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cg2.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
	}

	
}
