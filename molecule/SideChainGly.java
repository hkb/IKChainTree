package molecule;

import j3dScene.J3DScene;

import java.awt.Color;

import geom3d.Point3d;
import geom3d.Sphere3d;
import guiMolecule.ProteinViewer;

import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;

import chainTree.ChainTree;



public class SideChainGly extends SideChain {
	public SideChainGly(AminoAcid aminoAcid) {
		super(aminoAcid);
	}
	
	
	public void createBranchGroup(AminoAcid aminoAcid, Shape3D shape, ProteinViewer pV) {	
	}
	
	public void detachBranchGroup() {
	}
	public void reattachBranchGroup(TransformGroup spin) {
	}
	public void draw(J3DScene scene, ChainTree cTree, int i, Carbon Ca) { }


}
