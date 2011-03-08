package chainTree;

import geom3d.*;
import matrix.RotationMatrix4x4;

public class CTNode {
	protected double a, s, c, d;
	protected Vector3d u, v, pq;
	protected double wxx, wxy, wxz, wyy, wyz, wzz, wx, wy, wz;
	protected RotationMatrix4x4 matr;
	protected int height; 		// height of the node
	protected int low, high;
	protected CTNode left = null, right = null, parent = null;
	public boolean isLocked; 
	protected Cylinder3d cylinder;
	protected Sphere3d sphere;
	protected Volume3d volume, energyVolume, drawVolume;
	//protected Capsule3d energyCapsule;
		
	public CTNode(Point3d p, Point3d q, int i) {
		s = d = a = 0; c = 1;
		pq = new Vector3d(p,q);
		Vector3d w = Vector3d.createUnitVector(p,q);
		wx = w.x; wy = w.y; wz = w.z;
		wxx = wx*wx; wxy = wx*wy; wxz = wx*wz; wyy = wy*wy; wyz = wy*wz; wzz = wz*wz;
		matr = new RotationMatrix4x4(1, 0, 0, pq.x, 0, 1, 0, pq.y, 0, 0, 1, pq.z);
		left = right = null;
		low = high = i;
		height = 0;
		isLocked = false;
	}
	
	public CTNode(CTNode l, CTNode r) {
		left = l;
		right = r;
		left.parent = right.parent = this;
		matr = new RotationMatrix4x4(left.matr,right.matr);
		low = left.low;
		high = right.high;
		height = left.height+1;
		if (height <= right.height) height = right.height + 1;
		isLocked = false;
	}
	
	public Vector3d getPQ() { return pq; }
	public Vector3d getU() { return u; }
	public double getA()   { return a; }     public void setA(double a) { this.a = a; }

	public boolean isLeaf() { return height == 0; }
	public boolean isInternal() { return !isLeaf(); }
	
	public int getDepth(){
		int d=0;
		CTNode n = this;
		while(n!=null){ d++; n = n.parent; }
		return d;
	}

	static class NodeException extends RuntimeException{
		public CTNode n;
		NodeException(CTNode n){
			super();
			this.n = n;
		}
	}
	public CTNode rightRotation(ChainTree cTree) {
		CTNode b = left;
		CTNode d = b.right;		
		
		b.high = high;
		if(d==null){throw new NodeException(this); }
		low = d.low;    
		high = right.high; 
		
		RotationMatrix4x4 tempMatr = b.matr;
		b.matr = matr;
		RotationMatrix4x4.mult(d.matr,right.matr, tempMatr);
		matr = tempMatr;
		
		b.right = this;
		b.parent = parent;
		if (parent == null) cTree.setRoot(b);
		else {
			if (parent.left == this) parent.left = b; else parent.right = b;
		}
		left = d;
		parent = b;
		d.parent = this;	
		
		height = left.height + 1;
		if (right.height > left.height) height = right.height + 1;
		
		b.height = b.left.height + 1;
		if (b.right.height > b.left.height) b.height = b.right.height + 1;
		
		CTNode nd; int h;
		nd = b.parent;
		while (nd != null) {
			h = nd.left.height + 1;
			if (nd.right.height > nd.left.height) h = nd.right.height + 1;
			if (h != nd.height) { nd.height = h; nd = nd.parent; } else nd = null;
		}
		return b;
	}
	
	public CTNode leftRotation(ChainTree cTree) {
		CTNode a = right;
		CTNode d = a.left;
		
		a.low = low;   
		low = left.low;
		high = d.high;
		
		RotationMatrix4x4 tempMatr = a.matr;
		a.matr = matr;
		RotationMatrix4x4.mult(left.matr, d.matr, tempMatr);
		matr = tempMatr;
		
		a.left = this;
		a.parent = parent;
		if (parent == null) cTree.setRoot(a);
		else {
			if (parent.left == this) parent.left = a; else parent.right = a;
		}
		right = d;
		parent = a;
		d.parent = this;
		
		height = left.height + 1;
		if (right.height > left.height) height = right.height + 1; 
		
		a.height = a.left.height + 1;
		if (a.right.height > a.left.height) a.height = a.right.height + 1;
		
		CTNode nd; int h;
		nd = a.parent;
		while (nd != null) {
			h = nd.left.height + 1;
			if (nd.right.height > nd.left.height) h = nd.right.height + 1;
			if (h != nd.height) { nd.height = h; nd = nd.parent; } else nd = null;
		}
		return a;
	}
	
	public void showRotationMatrices() {
		System.out.println("[" + low + "," + high + "]");
		matr.toConsole(3);
		if (left != null) left.showRotationMatrices();
		if (right != null) right.showRotationMatrices();
	}
	
	public String toString() { 
		String str = "";
		for (int i = 0; i < height; i++) str += ".";
		str = str +"[" + String.valueOf(low) + "," + String.valueOf(high) + "]"; 
		return str;
	}

	
	public void toConsole() { 
		for (int i = 0; i < height; i++) System.out.print('.');
		System.out.println("[" + low + "," + high + "]"); }
}
