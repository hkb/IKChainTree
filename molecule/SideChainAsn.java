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

public class SideChainAsn extends SideChain {
	private Carbon  Cb;
	private Carbon  Cg;
	private Oxygen  Od1;
	private Nitrogen  Nd2;
	
	BranchGroup CbCgBond  = null;
	BranchGroup CgOd1Bond  = null;
	BranchGroup CgNd2Bond  = null;
	Shape3D CbCgBondShape = null;
	Shape3D CgOd1BondShape = null;
	Shape3D CgNd2BondShape = null;
	
	public SideChainAsn(AminoAcid aminoAcid) {
		super(aminoAcid);
		Cb = new Carbon();
		Cg = new Carbon();
		Od1 = new Oxygen();
		Nd2 = new Nitrogen();
	}
	
	public Carbon  getCb()           { return Cb; }
	public void    setCb(Carbon cb)  { Cb = cb;   }
	public Carbon  getCg()           { return Cg; }
	public void    setCg(Carbon cg)  { Cg = cg;   }
	public Oxygen  getOd1()          { return Od1; }
	public void    setOd1(Oxygen od1)  { Od1 = od1;   }
	public Nitrogen  getNd2()           { return Nd2; }
	public void    setNd2(Nitrogen nd2)  { Nd2 = nd2;   }
	public BranchGroup getCbCgBond()                 { return CbCgBond; }
	public void    setCbCgBond(BranchGroup CbCgBond) { this.CbCgBond = CbCgBond; } 
	public BranchGroup getCgOd1Bond()                 { return CgOd1Bond; }
	public void    setCgOd1Bond(BranchGroup CgOd1Bond) { this.CgOd1Bond = CgOd1Bond; } 
	public BranchGroup getCgNd2Bond()                 { return CgNd2Bond; }
	public void    setCgNd2Bond(BranchGroup CgNd2Bond) { this.CgNd2Bond = CgNd2Bond; } 
	public Shape3D getCbCgBondShape()                      { return CbCgBondShape; }
	public void    setCbCgBondShape(Shape3D CbCgBondShape) { this.CbCgBondShape = CbCgBondShape; }
	public Shape3D getCgOd1BondShape()                      { return CgOd1BondShape; }
	public void    setCgOd1BondShape(Shape3D CgOd1BondShape) { this.CgOd1BondShape = CgOd1BondShape; }
	public Shape3D getCgNd2BondShape()                      { return CgNd2BondShape; }
	public void    setCgNd2BondShape(Shape3D CgOd2BondShape) { this.CgNd2BondShape = CgOd2BondShape; }

	public void createBranchGroup(AminoAcid aminoAcid, Shape3D shape, ProteinViewer pV) {	
		Cb.setBranchGroup(pV.addSphere(Cb, Carbon.getAtomicRadius()/2, Colors.black));
		Cg.setBranchGroup(pV.addSphere(Cg, Carbon.getAtomicRadius()/2, Colors.black));
		Od1.setBranchGroup(pV.addSphere(Od1, Oxygen.getAtomicRadius()/2, Colors.red));
		Nd2.setBranchGroup(pV.addSphere(Nd2, Nitrogen.getAtomicRadius()/2, Colors.blue));
		setCbCgBond(pV.addBond(Cb, Cg, Colors.black));
		setCbCgBondShape(shape);
		setCgOd1Bond(pV.addBond(Cg, Od1, Colors.black));
		setCgOd1BondShape(shape);
		setCgNd2Bond(pV.addBond(Cg, Nd2, Colors.black));
		setCgNd2BondShape(shape);
		aminoAcid.setCaCbBond(pV.addBond(aminoAcid.getCa(), Cb, Colors.black));
		aminoAcid.setCaCbBondShape(shape);
	}	
	
	public void detachBranchGroup() {
		Cb.getBranchGroup().detach();
		Cg.getBranchGroup().detach();
		Od1.getBranchGroup().detach();
		Nd2.getBranchGroup().detach();
		CbCgBond.detach();		  
		CgOd1Bond.detach();
		CgNd2Bond.detach();
		aminoAcid.getCaCbBond().detach();
	}

	public void reattachBranchGroup(TransformGroup spin) {
		spin.addChild(Cb.getBranchGroup());
		spin.addChild(Cg.getBranchGroup());
		spin.addChild(Od1.getBranchGroup());
		spin.addChild(Nd2.getBranchGroup());
		spin.addChild(CbCgBond);		  
		spin.addChild(CgOd1Bond);
		spin.addChild(CgNd2Bond);
		spin.addChild(aminoAcid.getCaCbBond());
	}

	public void draw(J3DScene scene, ChainTree cTree, int i, Carbon Ca) { 
		Point3d CaPos = Ca.getPosition();
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cb.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cg.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Od1.getPosition().subtract(CaPos)), Oxygen.vdWRadius/2), Color.red);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Nd2.getPosition().subtract(CaPos)), Nitrogen.vdWRadius/2), Color.blue);
	}

	
}
