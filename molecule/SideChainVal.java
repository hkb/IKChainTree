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

public class SideChainVal extends SideChain {
	private Carbon Cb;
	private Carbon Cg1;
	private Carbon Cg2;
	
	BranchGroup CbCg1Bond  = null;
	BranchGroup CbCg2Bond  = null;
	Shape3D CbCg1BondShape = null;
	Shape3D CbCg2BondShape = null;
	
	public SideChainVal(AminoAcid aminoAcid) {
		super(aminoAcid);
		Cb = new Carbon();
		Cg1 = new Carbon();
		Cg2 = new Carbon();
	}
	
	public Carbon  getCb()            { return Cb; }
	public void    setCb(Carbon cb)   { Cb = cb;   }
	public Carbon  getCg1()           { return Cg1; }
	public void    setCg1(Carbon cg1) { Cg1 = cg1;   }
	public Carbon  getCg2()           { return Cg2; }
	public void    setCg2(Carbon cg2) { Cg2 = cg2;   }
	
	
	public BranchGroup getCbCg1Bond()                        { return CbCg1Bond; }
	public void    setCbCg1Bond(BranchGroup CbCg1Bond)       { this.CbCg1Bond = CbCg1Bond; } 
	public BranchGroup getCbCg2Bond()                        { return CbCg2Bond; }
	public void    setCbCg2Bond(BranchGroup CbCg2Bond)       { this.CbCg2Bond = CbCg2Bond; } 
	public Shape3D getCbCg1BondShape()                       { return CbCg1BondShape; }
	public void    setCbCg1BondShape(Shape3D CbCg1BondShape) { this.CbCg1BondShape = CbCg1BondShape; }
	public Shape3D getCbCg2BondShape()                       { return CbCg2BondShape; }
	public void    setCbCg2BondShape(Shape3D CbCg2BondShape) { this.CbCg2BondShape = CbCg2BondShape; }
	
	public void createBranchGroup(AminoAcid aminoAcid, Shape3D shape, ProteinViewer pV) {	
		Cb.setBranchGroup(pV.addSphere(Cb, Carbon.getAtomicRadius()/2, Colors.black));
		Cg1.setBranchGroup(pV.addSphere(Cg1, Carbon.getAtomicRadius()/2, Colors.black));
		Cg2.setBranchGroup(pV.addSphere(Cg2, Carbon.getAtomicRadius()/2, Colors.black));
		setCbCg1Bond(pV.addBond(Cb, Cg1, Colors.black));
		setCbCg1BondShape(shape);
		setCbCg2Bond(pV.addBond(Cb, Cg2, Colors.black));
		setCbCg2BondShape(shape);
		aminoAcid.setCaCbBond(pV.addBond(aminoAcid.getCa(), Cb, Colors.black));
		aminoAcid.setCaCbBondShape(shape);
	}
	
	public void detachBranchGroup() {
		Cb.getBranchGroup().detach();
		Cg1.getBranchGroup().detach();
		Cg2.getBranchGroup().detach();
		CbCg1Bond.detach();		  
		CbCg2Bond.detach();
		aminoAcid.getCaCbBond().detach();
	}
	public void reattachBranchGroup(TransformGroup spin) {
		spin.addChild(Cb.getBranchGroup());
		spin.addChild(Cg1.getBranchGroup());
		spin.addChild(Cg2.getBranchGroup());
		spin.addChild(CbCg1Bond);		  
		spin.addChild(CbCg2Bond);
		spin.addChild(aminoAcid.getCaCbBond());
	}

//	public ChainTree createChainTree() {
		
//	}
	
	public void draw(J3DScene scene, ChainTree cTree, int i, Carbon Ca) { 
		Point3d CaPos = Ca.getPosition();
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cb.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cg1.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cg2.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
	}

	
}
