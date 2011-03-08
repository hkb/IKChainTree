package molecule;

import j3dScene.J3DScene;

import java.awt.Color;

import geom3d.Point3d;
import geom3d.Sphere3d;
import guiMolecule.AtomAppearance;
import guiMolecule.ProteinViewer;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;

import chainTree.ChainTree;

import misc.Colors;

public class SideChainArg extends SideChain {
	private Carbon Cb;
	private Carbon Cg;
	private Carbon Cd;
	private Nitrogen Ne;
	private Carbon Cz;
	private Nitrogen Nh1;
	private Nitrogen Nh2;

	private Atom[] atoms = new Atom[7];
	private BranchGroup[] bonds = new BranchGroup[6];
	private Shape3D[] shapes = new Shape3D[6];
	
	public SideChainArg(AminoAcid aminoAcid) {
		super(aminoAcid);
		
		Cb = new Carbon();
		Cg = new Carbon();
		Cd = new Carbon();;
		Ne = new Nitrogen();
		Cz = new Carbon();
		Nh1 = new Nitrogen();
		Nh2  = new Nitrogen();

		atoms[0] = new Carbon();
		atoms[1] = new Carbon();
		atoms[2] = new Carbon();
		atoms[3] = new Nitrogen();
		atoms[4] = new Carbon();
		atoms[5] = new Nitrogen();
		atoms[6] = new Nitrogen();
	}
	
	public Carbon   getCb()  { return Cb; }   public void setCb(Carbon cb)     { Cb = cb;   }
	public Carbon   getCg()  { return Cg; }   public void setCg(Carbon cg)     { Cg = cg;   }
	public Carbon   getCd()  { return Cd; }   public void setCd(Carbon cd)     { Cd = cd;   }
	public Nitrogen getNe()  { return Ne; }   public void setNe(Nitrogen ne)   { Ne = ne;   }
	public Carbon   getCz()  { return Cz; }   public void setCz(Carbon cz)     { Cz = cz;   }
	public Nitrogen getNh1() { return Nh1; }  public void setNh1(Nitrogen nh1) { Nh1 = nh1;  }
	public Nitrogen getNh2() { return Nh2; }  public void setNh2(Nitrogen nh2) { Nh2 = nh2;  }

	public BranchGroup getCbCgBond()  { return bonds[0]; } public void setCbCgBond(BranchGroup  CbCgBond)  { bonds[0] = CbCgBond;  } 
	public BranchGroup getCgCdBond()  { return bonds[1]; } public void setCgCdBond(BranchGroup  CgCdBond)  { bonds[1] = CgCdBond;  } 
	public BranchGroup getCdNeBond()  { return bonds[2]; } public void setCdNeBond(BranchGroup  CdNeBond)  { bonds[2] = CdNeBond;  } 
	public BranchGroup getNeCzBond()  { return bonds[3]; } public void setNeCzBond(BranchGroup  NeCzBond)  { bonds[3] = NeCzBond;  } 
	public BranchGroup getCzNh1Bond() { return bonds[4]; } public void setCzNh1Bond(BranchGroup CzNh1Bond) { bonds[4] = CzNh1Bond; } 
	public BranchGroup getCzNh2Bond() { return bonds[5]; } public void setCzNh2Bond(BranchGroup CzNh2Bond) { bonds[5] = CzNh2Bond; } 

	public Shape3D getCbCgBondShape()  { return shapes[0]; } public void setCbCgBondShape(Shape3D  CbCgBondShape)  { shapes[0] = CbCgBondShape;  }
	public Shape3D getCgCdBondShape()  { return shapes[1]; } public void setCgCdBondShape(Shape3D  CgCdBondShape)  { shapes[1] = CgCdBondShape;  }
	public Shape3D getCdNeBondShape()  { return shapes[2]; } public void setCdNeBondShape(Shape3D  CdNeBondShape)  { shapes[2] = CdNeBondShape;  }
	public Shape3D getNeCzBondShape()  { return shapes[3]; } public void setNeCzBondShape(Shape3D  NeCzBondShape)  { shapes[3] = NeCzBondShape;  }
	public Shape3D getCzNh1BondShape() { return shapes[4]; } public void setCzNh1BondShape(Shape3D CzNh1BondShape) { shapes[4] = CzNh1BondShape; }
	public Shape3D getCzNh2BondShape() { return shapes[5]; } public void setCzNh2BondShape(Shape3D CzNh2BondShape) { shapes[5] = CzNh2BondShape; }
	
	public void createBranchGroup(AminoAcid aminoAcid, Shape3D shape, ProteinViewer pV) {	
		atoms[0].addBranchGroups(AtomAppearance.carbon,   pV.getSpin());
		atoms[1].addBranchGroups(AtomAppearance.carbon,   pV.getSpin());
		atoms[2].addBranchGroups(AtomAppearance.carbon,   pV.getSpin());
		atoms[3].addBranchGroups(AtomAppearance.nitrogen, pV.getSpin());
		atoms[4].addBranchGroups(AtomAppearance.carbon,   pV.getSpin());
		atoms[5].addBranchGroups(AtomAppearance.nitrogen, pV.getSpin());
		atoms[6].addBranchGroups(AtomAppearance.nitrogen, pV.getSpin());

		setCbCgBond(pV.addBond(getCb(),  getCg(),  Colors.black)); setCbCgBondShape(shape);
		setCgCdBond(pV.addBond(getCg(),  getCd(),  Colors.black)); setCgCdBondShape(shape);
		setCdNeBond(pV.addBond(getCd(),  getNe(),  Colors.black)); setCdNeBondShape(shape);
		setNeCzBond(pV.addBond(getNe(),  getCz(),  Colors.black)); setNeCzBondShape(shape);
		setCzNh1Bond(pV.addBond(getCz(), getNh1(), Colors.black)); setCzNh1BondShape(shape);
		setCzNh2Bond(pV.addBond(getCz(), getNh2(), Colors.black)); setCzNh2BondShape(shape);

		aminoAcid.setCaCbBond(pV.addBond(aminoAcid.getCa(), getCb(), Colors.black));
		aminoAcid.setCaCbBondShape(shape);
	}
	
	public void detachBranchGroup() {
		for (int i = 0; i < 7; i++) atoms[i].getAtomBranchGroup().detach();
		for (int i = 0; i < 7; i++)	atoms[i].getVdWBranchGroup().detach();
		for (int i = 0; i < 6; i++) bonds[i].detach();		  
		aminoAcid.getCaCbBond().detach();
	}

	public void reattachBranchGroup(TransformGroup spin, ProteinViewer pV) {
		if (pV.getModel().equals("Atoms")) for (int i = 0; i < 7; i++) spin.addChild(atoms[i].getAtomBranchGroup());
		else for (int i = 0; i < 7; i++) spin.addChild(atoms[i].getVdWBranchGroup());
		for (int i = 0; i < 6; i++) spin.addChild(bonds[i]);		  
		spin.addChild(aminoAcid.getCaCbBond());
	}

	public void draw(J3DScene scene, ChainTree cTree, int i, Carbon Ca) { 
		Point3d CaPos = Ca.getPosition();
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cb.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cg.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cd.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Ne.getPosition().subtract(CaPos)), Nitrogen.vdWRadius/2), Color.blue);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Cz.getPosition().subtract(CaPos)), Carbon.vdWRadius/2), Color.black);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Nh1.getPosition().subtract(CaPos)), Nitrogen.vdWRadius/2), Color.blue);
		scene.addShape(new Sphere3d(cTree.getPosition(i, Nh2.getPosition().subtract(CaPos)), Nitrogen.vdWRadius/2), Color.blue);
	}
	
	
}
