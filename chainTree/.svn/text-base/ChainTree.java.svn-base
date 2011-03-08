package chainTree;

import java.awt.Color;
import java.util.List;
import java.util.Random;


import matrix.RotationMatrix4x4;
import molecule.AlphaHelix;
import molecule.AminoAcid;
import molecule.BetaSheet;
import molecule.BetaStrand;
import molecule.Carbon;
import molecule.Protein;
import molecule.SideChain;
import geom3d.Point3d;
import geom3d.Segment3d;
import geom3d.Sphere3d;
import geom3d.Vector3d;
import geom3d.Line3d;
import geom3d.Cylinder3d;
import geom3d.PointSet3d;
import geom3d.Volume3d;
import dataStructures.Queue;
import dataStructures.Set;
import dataStructures.Stack;
import j3dScene.J3DScene;
import java.lang.management.*;

public class ChainTree {
	PointSet3d points;
	public CTNode root;
	public CTNode[] nodes;                  // leaves of the chain tree corresponding to bonds 
	public double[] proteinDihedralAngles;
	int[] contactVector;
	Protein protein;
	Stack logBook = new Stack();
	public double[] distances;
	boolean peptidePlanes;
	
	int nrBonds;
	int nrAtoms;
	RotationMatrix4x4 leftMatrix  = new RotationMatrix4x4();
	RotationMatrix4x4 rightMatrix = new RotationMatrix4x4();

	
	public int clashVolumeOverlapCount; 
	public List<Object[]> clashCheckedPairs = null;
	public int energyVolumeOverlapCount;
	public int primitiveOverlapCount;
	public int clashVolumeUpdateCount; 
	public List<Object[]> clashUpdatePairs = null;
	int clashCalls;
	int[] clashStatistics = new int[20];
	int[][] disjointCapsules  = new int[20][20];
	private final double PI = Math.PI;
	private final double PIPI = 2*PI;
	public final static double clashDist = 1.7;
	public final static double squaredClashDistance = clashDist*clashDist;
	public final static double extension = clashDist/2;
	public final static double squaredClashDist = clashDist*clashDist;
	public final int contactRadius = 14;
	public final int squaredContactRadius = contactRadius*contactRadius;
	public final int energyExtension = contactRadius/2;
	
	private PointSet3d oxygenAtoms = new PointSet3d();
	private PointSet3d hydrogenAtoms = new PointSet3d();
	
	public J3DScene j3dg;
	BoundingVolumeManager volumeManager = new BoundingVolumeManager(this);
	
	public int clashDetectionMode = -1;     //-1 = no clash detection
	                                        // 0 = after each single dihedral rotation
											// 1 = after all n dihedral rotations
	public  int boundingMode = 3;           // 0 = bounding volumes of 2 nested bounding volumes
	                                        // 1 = bounding volume of all bonds
											// 2 = same as 0
											// 3 = as 2 but capsules of locked nodes computed as 1

	public int volumeMode = BoundingVolumeManager.CAPSULE;
	
	public int rmsdMode = 2;                // 0 = atom positions
	                                        // 1 = atom distances
	                                        // 2 = dihedral angles
	
	public int drawCapsuleLevelLower = 1, drawCapsuleLevelUpper = 0;
	// magenta, green, red, cyan, blue, yellow
	public static Color[] colors = { new Color(255, 0, 255, 70), new Color(0, 255, 0, 70), new Color(255, 0, 0, 100),
			new Color(0, 255, 255, 100), new Color(0, 0, 255, 100), new Color(255, 255, 0, 100),
			new Color(0, 0, 100, 100), new Color(169, 169, 169, 100), new Color(255, 255, 0, 100),
			new Color(0, 255, 255, 100), new Color(0, 0, 255, 100), new Color(255, 255, 0, 100), 
			new Color(0, 255, 255, 100), new Color(0, 0, 255,100), new Color(255, 255, 0, 100) };

	public ChainTree() {}

	
	/*
	 * if peptidePlanes is TRUE, nodes corresponding to peptide planes are created
	 */
	public ChainTree(PointSet3d points, Protein protein, boolean peptidePlanes) {
		this.points = points;
		this.protein = protein;
		this.peptidePlanes = peptidePlanes;

		proteinDihedralAngles = protein.getDihedralAngles(); 
		nrAtoms = points.getSize();  // number of bonds in the backbone
		nrBonds = nrAtoms - 1;
		System.out.println(nrBonds);
		nodes = new CTNode[nrBonds];
		Queue queue = new Queue();
		int sz, sz2;
		Point3d p, q;
		if (peptidePlanes) {
			p = points.get(1);
			for (int i = 1; i < nrBonds-1; i++) {
				q = points.get(i+1);
				queue.push(nodes[i] = new CTNode(p, q, i));
				p = q;
			}
			CTNode nd;
			
			queue.push(nodes[0] = new CTNode(points.get(0), points.get(1),0));
			for (int i = 0; i < nrBonds-2; i = i+3) {
				nd = new CTNode((CTNode)queue.pop(), (CTNode)queue.pop());
				queue.push(nd = new CTNode(nd, (CTNode)queue.pop()));
			}
			queue.push(nodes[nrBonds-1] = new CTNode(points.get(nrBonds-1),points.get(nrBonds), nrBonds-1));
			sz = (nrBonds-1)/3 + 2;
		}
		else {
			p = points.get(0);
			for (int i = 0; i < nrBonds; i++) { 
				q = points.get(i+1);
				queue.push(nodes[i] = new CTNode(p, q, i));
				p = q;
			}
			sz = nrBonds;
		}
		
		while (sz > 1) {
			sz2 = sz/2;
			//			System.out.println("Queue size = " + sz + " sz2 = " + sz2);
			for (int k = 0; k < sz2; k++) queue.push(new CTNode((CTNode)queue.pop(), (CTNode)queue.pop()));
			if (2*sz2 != sz) {
				queue.push(queue.pop());
				sz2++; 
			}
			sz = sz2;
		}
		root = (CTNode)queue.pop();
		queue.clear();
	}

	
	public CTNode getRoot() { return root; }
	
	public void setRoot(CTNode root) { this.root = root; }

	public int getHeight() { return root.height; }
	/*
	 * rotates nodes so that the chain tree has leaves between node l and node r
	 * (both included) only. All these nodes are locked.
	 */
	public void regroup(int l, int r) {
		// regroup so that nodes l, l+1, ..., r are exactly covered
		CTNode nd = regroupLeft(nodes[l], r);
		nd = regroupRight(nodes[r], l);
		// rebalance regrouped subtree
		nd = newRebalanceSubtree(nd);
		// lock subtree
		lock(nd);
		
	}

	private CTNode regroupLeft(CTNode nd, int r) {
		CTNode cND = nd;	
		while (cND.high < r) {                           
			//			System.out.print("lockleft: at node "); cND.toConsole(); 
			if (cND.parent.left == cND) cND = cND.parent;
			else {
				if (cND.parent.parent.right == cND.parent) {
					//					System.out.print("lockleft: before left rotation of grand parent of node "); cND.toConsole(); 
					cND.parent.parent.leftRotation(this);
				}
				else {
					//					System.out.print("lockleft: before right rotation of grand parent of node "); cND.toConsole(); 
					cND.parent.parent.rightRotation(this);
				}
			}
		}
		return cND;
	}


	private CTNode regroupRight(CTNode nd, int l) {
		CTNode cND = nd;
		while (cND.low > l) {
			//			System.out.print("lockright: at node "); cND.toConsole();
			if (cND.parent.right == cND) cND = cND.parent;
			else {
				if (cND.parent.parent.left == cND.parent) {
					//					System.out.print("lockright: before right rotation of grand parent of node "); cND.toConsole(); 
					cND.parent.parent.rightRotation(this);
				}
				else {
					//					System.out.print("lockright: before left rotation of grand parent of node "); cND.toConsole(); 
					cND.parent.parent.leftRotation(this);
				}
			}
		}
		return cND;
	}
	
	public void lockAlphaHelices(boolean regroup) {
		int a, b;
		for (AlphaHelix alphaHelix : protein.getAlphaHelices()) {
			a = protein.getIndex(alphaHelix.getInitSeqNum());
			b = protein.getIndex(alphaHelix.getEndSeqNum());
		    System.out.println("Locking a-helix: a = " + a + ", b = " + b + " locking bonds [" + (3*a-2) + "," + (3*b-3) + "]");
		   	if (regroup) regroup(3*a-2, 3*b-3);  
		    else for (int k = 3*a-2; k <= 3*b-3; k++) nodes[k].isLocked = true; 
		}
	}

	public void lockBetaStrands(boolean regroup) {
		int a, b;
		for (BetaSheet betaSheet : protein.getBetaSheets()) {
			for (BetaStrand betaStrand : betaSheet.getBetaStrands())  {
				a = protein.getIndex(betaStrand.getInitSeqNum());
				b = protein.getIndex(betaStrand.getEndSeqNum());
				System.out.println("Locking b-strand: a = " + a + ", b = " + b + " locking bonds [" + (3*a-2) + "," + (3*b-3) + "]");
				if (regroup) regroup(3*a-2, 3*b-3);  
				else for (int k = 3*a-2; k <= 3*b-3; k++) nodes[k].isLocked = true;
			}
		}

	}
	

	public void naiveRebalance2(){
		for(int i=0;i<nodes.length;i++){
			CTNode n = nodes[i].parent.parent;
			while(n!=null && n.parent!=null){
				if(n.parent.left==n){
					CTNode sibling = n.parent.right;
					if(n.height<=sibling.height-2 && !groupTop(sibling)) n.parent.leftRotation(this);
					else if(n.height>=sibling.height+2 && !groupTop(n)) n.parent.rightRotation(this);
				}else{
					CTNode sibling = n.parent.left;
					if(n.height<=sibling.height-2 && !groupTop(sibling)) n.parent.rightRotation(this);
					else if(n.height>=sibling.height+2 && !groupTop(n)) n.parent.leftRotation(this);
				}
				n=n.parent;
			}
		}
	}
	
	private static boolean groupTop(CTNode n){
		return n.isLocked&&n.parent!=null&&!n.parent.isLocked;
	}
	public void naiveRebalance(){
		for(int i=0;i<nodes.length;i++){
			CTNode n = nodes[i].parent.parent;
			if(nodes[i].isLocked){
				while(n!=null && n.parent!=null && n.parent.isLocked){
					if(n.parent.left==n){
						CTNode sibling = n.parent.right;
						if(n.height<=sibling.height-2) n.parent.leftRotation(this);
						else if(n.height>=sibling.height+2) n.parent.rightRotation(this);
					}else{
						CTNode sibling = n.parent.left;
						if(n.height<=sibling.height-2) n.parent.rightRotation(this);
						else if(n.height>=sibling.height+2) n.parent.leftRotation(this);
					}
					n=n.parent;
				}
				if(n.isLocked){
					n.height=0;
					n=n.parent;
					while(n!=null){
						n.height = Math.max(n.left.height, n.right.height)+1;
						n=n.parent;
					}
				}
			}else{
				while(n!=null && n.parent!=null){
					if(n.parent.left==n){
						CTNode sibling = n.parent.right;
						if(n.height<=sibling.height-2) n.parent.leftRotation(this);
						else if(n.height>=sibling.height+2) n.parent.rightRotation(this);
					}else{
						CTNode sibling = n.parent.left;
						if(n.height<=sibling.height-2) n.parent.rightRotation(this);
						else if(n.height>=sibling.height+2) n.parent.leftRotation(this);
					}
					n=n.parent;
				}
			}
		}
	}
	
	public void lock(CTNode nd) {
		if (!nd.isLocked) {
			nd.isLocked = true;
			if (nd.left != null) {
				lock(nd.left);
				lock(nd.right);
			}
		}
	}
	
	
	public void removeLockedSubtrees(boolean peptidePlanes) { 
		CTNode nd, cnd;
		if (peptidePlanes) {
			for (int i = 1; i < root.high; i = i+3) {
				nd = nodes[i].parent.parent;
				System.out.println("removing peptide plane [" + nd.low + "," + nd.high + "]");
				nd.height=0;
				cnd = nd;
				while ((cnd = cnd.parent)!= root) cnd.height = Math.max(cnd.left.height, cnd.right.height) + 1;
			}
		}
		removeLockedSubtrees(root); 
	}
	
	public void removeLockedSubtrees(CTNode nd) {
		CTNode cnd;
		if (nd.height > 1) {
			if (nd.isLocked) {
				if (!nd.parent.isLocked) System.out.println("removing subtree [" + nd.low + "," + nd.high + "]");
				nd.height = 0;
				cnd = nd;
				while ((cnd = cnd.parent)!= root) cnd.height = Math.max(cnd.left.height, cnd.right.height) + 1;
			}
			else {
				removeLockedSubtrees(nd.left);
				removeLockedSubtrees(nd.right);
			}
		}
	}

	public void addLockedSubtrees(boolean peptidePlanes) { 
		addLockedSubtrees(root); 
		CTNode nd, cnd;
		if (peptidePlanes) {
			for (int i = 1; i < root.high; i = i+3) {
				nd = nodes[i].parent.parent;
				nd.height=2;
				cnd = nd;
				while ((cnd = cnd.parent)!= root) cnd.height = Math.max(cnd.left.height, cnd.right.height) + 1;
			}
		}
		addLockedSubtrees(root); 
	}


	public void addLockedSubtrees(CTNode nd) {
		CTNode cnd;
		if (nd.isLocked && (nd.left != null)) {
			nd.height = Math.max(nd.left.height, nd.right.height) + 1;
			cnd = nd;
			while ((cnd = cnd.parent) != null) cnd.height = Math.max(cnd.left.height, cnd.right.height) + 1;
		}
		else {
			if (nd.height != 0) {
				addLockedSubtrees(nd.left);
				addLockedSubtrees(nd.right);
			}
		}
	}
	
	public CTNode newRebalanceSubtree(CTNode nd) {
		if (nd.height > 2) {
			System.out.println("rebalancing subtree [" + nd.low + "," + nd.high + "]");
			newRebalanceSubtree(nd.left);
			newRebalanceSubtree(nd.right);
			return newRebalance(nd); 
		}
		else {
			System.out.println("height of [" + nd.low + "," + nd.high + "] is too small " + nd.height);
			return nd;
		}
	}
	
	public CTNode newRebalance(CTNode a) {
		System.out.print("rebalancing node [" + a.low + "," + a.high + "].");
		CTNode b = a.left;
		CTNode c = a.right;
		if (b.height - c.height > 1) {
			System.out.print(" Left side higher.");
			CTNode d = b.left;
			CTNode e = b.right;
			if (d.height >= e.height) {
				System.out.println(" Double rotation");
				a.rightRotation(this);
				newRebalance(a);
				return newRebalance(b);
			}
			else {
				System.out.println(" Single rotation");
				b.leftRotation(this);
				a.rightRotation(this);
				newRebalance(a);
				return newRebalance(e);
			}
		}
		if (c.height - b.height > 1) {
			System.out.print(" Right side higher.");
			CTNode d = c.left;
			CTNode e = c.right;
			if (e.height >= d.height) {
				System.out.println(" Double rotation");
				a.leftRotation(this);
				newRebalance(a);
				return newRebalance(c);
			}
			else {
				System.out.println(" Single rotation");
				c.rightRotation(this);
				a.leftRotation(this);
				newRebalance(a);
				return newRebalance(d);
			}
		}
		System.out.println(" No rebalancing was needed");
		return a;
	}

		
	
	public RotationMatrix4x4 getTransformMatrix4x4(int i, int j) {
		// find split vertex
//		CTNode split = nodes[i];
//		while (split.high < j-1) split = split.parent;
		CTNode split = root;
		boolean lookingForSplitVertex = true;
		while( split.left != null && lookingForSplitVertex){
			if (j-1 <= split.left.high) split = split.left;
			else {
				if (split.right.low <= i) split = split.right;
				else lookingForSplitVertex = false;	
			}
		}
		//		System.out.println("split vertex is [" + split.low + "," + split.high + "]");
		if ((split.low == i) && (split.high == j-1)) return split.matr;
		CTNode nd = split;
		RotationMatrix4x4 rotMatrix = new RotationMatrix4x4();
		// left subtree
		nd = nd.left;
		while (nd != null) {
			//			System.out.println("Going left: node [" + nd.low + "," + nd.high + "]");
			if (nd.low == i) { rotMatrix.multL(nd.matr); nd = null; }
			else {
				if (i <= nd.left.high) { rotMatrix.multL(nd.right.matr); nd = nd.left; }
				else nd = nd.right;
			}
		}
		// right subtree
		nd = split.right;
		while (nd != null) {
			//			System.out.println("Going right: node [" + nd.low + "," + nd.high + "]");
			if (nd.high == j-1) { rotMatrix.multR(nd.matr); nd = null; }
			else {
				if (nd.right.low <= j-1) { rotMatrix.multR(nd.left.matr); nd = nd.right; } 
				else nd = nd.left;
			}
		}
		return rotMatrix;
	}

	public PointSet3d getPoints(CTNode nd) {
		PointSet3d pointSubset = new PointSet3d();
		for (int i = nd.low; i <= nd.high+1; i++) pointSubset.insert(getPosition(nd.low, i));
		return pointSubset;
	}
	
	/* 
	 * returns the coordinates of the j-th origo in the coordinate system of the i-th origo.
	 */
	public Point3d getPosition(int i, int j) {
		if (i == j) return new Point3d(0, 0, 0);
		RotationMatrix4x4 m = getTransformMatrix4x4(i, j);
		return getPosition(m);
	}

	public Point3d getPosition(RotationMatrix4x4 m) {
		return new Point3d(m.get14(), m.get24(), m.get34());
	}
	
	
	public Point3d getPosition(int i, int j, Point3d p) {
		if (i == j) return p;
		RotationMatrix4x4 m = getTransformMatrix4x4(i, j);
		return getPosition(m, p);
	}
	
	public Point3d getPosition(RotationMatrix4x4 m, Point3d p) {
		return new Point3d(m.get11()*p.x + m.get12()*p.y + m.get13()*p.z + m.get14(),
				           m.get21()*p.x + m.get22()*p.y + m.get23()*p.z + m.get24(),
				           m.get31()*p.x + m.get32()*p.y + m.get33()*p.z + m.get34()); 
	}

	public Point3d getPosition(RotationMatrix4x4 m, double x, double y, double z) {
		return new Point3d(m.get11()*x + m.get12()*y + m.get13()*z + m.get14(),
				           m.get21()*x + m.get22()*y + m.get23()*z + m.get24(),
				           m.get31()*x + m.get32()*y + m.get33()*z + m.get34()); 
	}

	public Vector3d getDirection(int i, int j, Vector3d v) {
		if (i == j) return v;
		RotationMatrix4x4 m = getTransformMatrix4x4(i, j);
		return getDirection(m, v);
	}

	public Vector3d getDirection(RotationMatrix4x4 m, Vector3d v) {
		return new Vector3d(m.get11()*v.x + m.get12()*v.y + m.get13()*v.z,
				            m.get21()*v.x + m.get22()*v.y + m.get23()*v.z,
				            m.get31()*v.x + m.get32()*v.y + m.get33()*v.z); 
	}

	/* 
	 * returns the coordinates of the j-th origo in the coordinate system of the 0-th origo
	 */
	public Point3d getPosition(int j)  { return getPosition(0, j);	}

	/*
	 * returns the j-th origo coordinates of p in the coordinate system of the 0-th origo 
	 */
	public Point3d getPosition(int j, Point3d p) { return getPosition(0, j, p);	}

	/* Ikke l�ngere n�dvendig --Rasmus
	 * public Vector  getPosition(int j, Vector  v) {
		Point3d p = getPosition(j, new Point3d(v.x(), v.y(), v.z()));
		return new Vector(p.getX(), p.getY(), p.getZ()); 
	}*/

	public double getDihedralAngle(int i) {
		if ((i < 1) && (i >= nodes.length-1 )) return 0.0;
		else {
			Point3d p = new Point3d();
			Point3d q = getPosition(i-1,i);
			Point3d r = getPosition(i-1,i+1);
			Point3d s = getPosition(i-1,i+2);
			Vector3d pq = new Vector3d(p,q);
			Vector3d qr = new Vector3d(q,r);
			Vector3d rs = new Vector3d(r,s);
			return Vector3d.getDihedralAngle(pq, qr, rs);
		}
	}
	
	/*
	 * computes dihedral angles of bonds (first and last bond excluded)
	 */
	public double[] getDihedralAngles() {
		double[] dihedralAngles = new double[nodes.length];
		dihedralAngles[0] = 0.0;
		Point3d q, r , s;
		Vector3d pq, qr, rs;
		q = getPosition(0);
		r = getPosition(1);
		s = getPosition(2);
		qr = new Vector3d(q, r);
		rs = new Vector3d(r, s);
		for (int i = 1; i < nodes.length-1; i++) {
			q = r;
			r = s;
			pq = qr;
			qr = rs;
			s = getPosition(i+2);
			rs = new Vector3d(r, s);
			dihedralAngles[i] = Vector3d.getDihedralAngle(pq, qr, rs);
		}
		dihedralAngles[nodes.length-1] = 0.0;
		return dihedralAngles;
	}

	public void printDihedralAngles() {
		double[] dihedralAngles = getDihedralAngles();
		for (int i = 0; i < nodes.length; i++) System.out.println(i + ". dihedral angle = " + dihedralAngles[i]);
	}

	public void printDihedralAnglesDifferences() {
		double[] dihedralAngles = getDihedralAngles();
		for (int i = 0; i <= nodes.length; i++) 
			System.out.println(i + ". dihedral angle difference = " + (dihedralAngles[i]-proteinDihedralAngles[i]));
	}

	public double getDihedralAnglesRMSD(double[] targetDihedralAngles, int size) {
		double[] dihedralAngles = getDihedralAngles();
		double sum = 0.0;
		double diff;
		for (int i = 1; i < size; i++) {
			if (!nodes[i].isLocked) {
				diff = getAngleDiff(dihedralAngles[i], targetDihedralAngles[i]);
				sum += diff*diff;
			}
		}
		return Math.sqrt(sum/size);
	}

	public double getDihedralAnglesRMSD(int size) {
		return getDihedralAnglesRMSD(proteinDihedralAngles, size);
	}

	
	/*
	 * returns RMSD between positions of atoms in native configuration and current configuration
	 */
	public double getRMSD() {
		double sum = 0;
		RotationMatrix4x4 mi = new RotationMatrix4x4();
		for (int i = 1; i <= nodes.length; i++) {
			mi.multR(nodes[i-1].matr);
			sum += points.get(i).getSquaredDistance(getPosition(mi));
		}
		return Math.sqrt(sum/(nodes.length+1));
	}
	
	/*
	 * returns matrix with distances between all pairs of atoms.
	 */
//	public double[][] getDistanceMatrix() {
//		Point3d p;
//		int n = points.getSize();
//		double[][] sqm = new double[n][n];
//		for (int i = 0; i < n; i++) {
//			sqm[i][i] = 0.0;
//			p = points.get(i);
//			for (int j = i+1; j < n; j++) sqm[i][j] = sqm[j][i] = p.getDistance(points.get(j));
//		}
//		return sqm;
//	}

	public void getDistanceMatrix() {
		Point3d p;
		int k = 0;
		distances = new double[nrAtoms*(nrAtoms-1)/2];
		for (int i = 0; i < nrAtoms-1; i++) {
			p = points.get(i);
			for (int j = i+1; j < nrAtoms; j++) distances[k++] = p.getDistance(points.get(j));
		}
	}

	public double getDistance(int i, int j) {
//		System.out.print("(i,j) = (" + i +"," + j + "), " );
		if (i < j) {
//			System.out.println(i*(2*nrAtoms - i - 1)/2 + j - i - 1);
			return distances[i*(2*nrAtoms - i - 1)/2 + j - i - 1];
		}
		if (i > j) {
//			System.out.println(j*(2*nrAtoms - j - 1)/2 + i - j - 1);
			return distances[j*(2*nrAtoms - j - 1)/2 + i - j - 1];
		}
		return 0.0;
	}
	/*
	 * returns matrix with squared distances between all pairs of atoms.
	 */
	public double[] getSquaredDistanceMatrix() {
		Point3d p;
		double[] sqm = new double[nrAtoms];
		int k = 0;
		for (int i = 0; i < nrAtoms-1; i++) {
			p = points.get(i);
			for (int j = i+1; j < nrAtoms; j++) sqm[k++] = p.getSquaredDistance(points.get(j));
		}
		return sqm;
	}
	
	public double getPairwiseDistanceRMSD(int size) {
		double sum = 0;
		double dNC;
		RotationMatrix4x4 mj = new RotationMatrix4x4();
//		long startUserTime = getUserTime();
//		long startCpuTime = getCpuTime();
//		long startTaskTime = System.nanoTime( );


		for (int i = 0; i < size-1; i++) {
			mj.reset(); 
			for (int j = i+1 ; j < size; j++) {
				mj.multR(nodes[j-1].matr);
				dNC = getDistance(i,j) - getPosition(mj).getDistance();
				sum += dNC*dNC;
			}
		}
//		long userTime = getUserTime() - startUserTime;
//		long cpuTime = getCpuTime() - startCpuTime;
//		long taskTime  = System.nanoTime( ) - startTaskTime;
//		System.out.println(size + " getPairwiseDistanceRMSD - User time " + startUserTime + " " + userTime);
//		System.out.println(size + " getPairwiseDistanceRMSD - Cpu  time " + startCpuTime + " " + cpuTime);
//		System.out.println(size + " getPairwiseDistanceRMSD - Task  time " + startTaskTime + " " + taskTime);
		return Math.sqrt(2*sum/(size*size - size));
	}

	
//	public double getPairwiseDistanceRMSD(int size) {
//		double sum = 0;
//		double dNC;
//		int step, k;
//		for (int i = 0; i < size-1; i++) {
//			step = 3;
//			k = i + 1;
//			while ((k < size-1) && nodes[k].isLocked) k++;  // go to next unlocked bond
//			step = step + k - 1;
//			for (int j = step ; j < size; j++) {
//				dNC = distances[i][j] - getPosition(i,j).getDistance();  // this is very expensive
//				sum += dNC*dNC;
//			}
//		}
//		return Math.sqrt(2*sum/(size*size - size));
//	}

	
	public double getPairwiseDistanceRMSD() { return getPairwiseDistanceRMSD(points.getSize()); }
	
	/*
	 * returns the contribution to pairwise distance RMSD by points separated by the k-th bond.
	 */
	public double getPairwiseSquaredDistancsSum(int k) {
		double sum = 0;
		int size = points.getSize();
		double dNC;
		for (int i = 0; i < k; i++) 
			for (int j = k+1 ; j < size; j++) {
				dNC = getDistance(i,j) - getPosition(i,j).getDistance();
				sum += dNC*dNC;
			}
		return sum;
	}

	public double getAngleDiff(double a, double b) {
		double aa = a;
		double bb = b;
		while (aa > PI) aa -= PIPI;
		while (aa <= -PI) aa += PIPI;
		while (bb > PI) bb -= PIPI;
		while (bb <= -PI) bb += PIPI;
		if (aa*bb >= 0.0) return Math.abs(aa-bb);
		else {
			double sum = Math.abs(aa) + Math.abs(bb);
			if (sum <= PI) return sum; else return PIPI - sum;
		}
	}
	
	/*
	 * returns the sum of the volumes of all capsules
	 */
	public double getTotalCapsulesVolume() {
		double volume = 0.0;
		Stack stack = new Stack();
		CTNode nd;
		stack.push(root);
		while (!stack.isEmpty()) {
			nd = (CTNode)stack.pop();
			volume += nd.volume.volume();
			if (nd.right != null) stack.push(nd.right);
			if (nd.left  != null) stack.push(nd.left);
		}
		return volume;
	}
	
	public double getRootCapsuleVolume(){
		return root.volume.volume();
	}
	
	
	/*
	 * computes two cylinders, one with the axis through the extreme leaves and the other through
	 * the midpoint of the segment between extreme leaves and the middle point. 
	 */
	public void getCylinderAxis(CTNode nd) {
		Point3d p = new Point3d(0, 0, 0);
		Point3d q = getPosition(nd.low, nd.high);
		Vector3d dir = new Vector3d(q);
		Line3d line = new Line3d(dir);
		double tMin = 0, tMax = 1, t; 
		Point3d pMin = p, pMax = q;
		double maxSquaredDist = 0, dist;
		for (int k = nd.low+1; k < nd.high-1; k++) {
			Point3d r = getPosition(nd.low, k);
			t = Vector3d.dotProduct(line.getDir(), new Vector3d(r, p));
			if (t < tMin) { tMin = t; pMin = r; } 
			else { 
				if (t > tMax) { tMax = t; pMax = r; } 
			}
			dist = line.getSquaredDistance(r);
			if (dist > maxSquaredDist) maxSquaredDist = dist;
		}
		Cylinder3d cylinder1 = new Cylinder3d(Math.sqrt(maxSquaredDist), pMin, new Segment3d(pMin, pMax));

		p = new Point3d(q.getX()/2, q.getY()/2, q.getZ()/2);
		int m = (nd.high - nd.low + 1)/2;
		q = getPosition(nd.low, m);
		dir = new Vector3d(p,q);
		line = new Line3d(p,dir);
		tMin = 0; tMax = 1;
		pMin = p; pMax = q;
		maxSquaredDist = q.getSquaredDistance(p);
		for (int k = nd.low+1; k < nd.high-1; k++) {
			if (k != m)  {
				Point3d r = getPosition(nd.low, k);
				t = Vector3d.dotProduct(line.getDir(), new Vector3d(r, p));
				if (t < tMin) { tMin = t; pMin = r; } 
				else { 
					if (t > tMax) { tMax = t; pMax = r; } 
				}
				dist = line.getSquaredDistance(r);
				if (dist > maxSquaredDist) maxSquaredDist = dist;
			}
		}
		Cylinder3d cylinder2 = new Cylinder3d(Math.sqrt(maxSquaredDist), pMin, new Segment3d(pMin, pMax));
	}

	/*
	 * when the dihedral  angle of i-th bond is changed by the angle a,
	 * the rotation matrix associated with this bond as well as all rotation 
	 * matrices involving it are updated.
	 */
	public void changeRotationAngle(int i, double a) {
		CTNode nd = nodes[i];
		RotationMatrix4x4 matr = nodes[i].matr;
		nd.a -= a;
		double s = nd.s = Math.sin(nd.a);
		double c = nd.c = Math.cos(nd.a);
		double d = nd.d = 1 - nd.c;
		double wxyd = nd.wxy*d, wxzd = nd.wxz*d, wyzd = nd.wyz*d;
		double wxs = nd.wx*s, wys = nd.wy*s, wzs = nd.wz*s;
		matr.set11(nd.wxx*d + c);
		matr.set12(wxyd + wzs);
		matr.set13(wxzd - wys);
		matr.set21(wxyd - wzs);
		matr.set22(nd.wyy*d + c);
		matr.set23(wyzd + wxs);
		matr.set31(wxzd + wys);
		matr.set32(wyzd - wxs);
		matr.set33(nd.wzz*d + c);

		double a14 = matr.get11()*matr.get14() + matr.get12()*matr.get24() + matr.get13()*matr.get34();
		double a24 = matr.get21()*matr.get14() + matr.get22()*matr.get24() + matr.get23()*matr.get34();
		matr.set34(matr.get31()*matr.get14() + matr.get32()*matr.get24() + matr.get33()*matr.get34());
		matr.set14(a14);
		matr.set24(a24);
		nodes[i].matr = matr;

		// rotation matrices between nodes[i] and the root are updated
		while ((nd = nd.parent) != null) {
			// old rotation matrix and capsule are saved in case the rotation is rejected.
			logBook.push(nd.matr);
			logBook.push(nd.volume);
			logBook.push(nd);
			nd.matr.copy(nd.left.matr);
			nd.matr.multR(nd.right.matr);
		}
		// bounding volumes between nodes[i] and the root are updated
		nd = nodes[i];
		while ((nd.parent != null) && (nd == nd.parent.right)) nd = nd.parent;
		while ((nd = nd.parent) != null) updateBoundingVolume(nd); //Takes 4ms total
	}
	
	/*
	 * generates an array of identical dihedral angles
	 */
	public void setFixedDihedralAngles(double dihedralAngle) {
		double angle;
		for (int i = 1; i < nodes.length-1; i++) 	{
			if (!nodes[i].isLocked) {
				angle = getDihedralAngle(i);
				changeRotationAngle(i, dihedralAngle - angle);
				logBook.clear();
			}
		}
	}

	/*
	 * generates an array of random dihedral angles
	 */
	public void setRandomDihedralAngles() {
		double angle, dihedralAngle;
		Random rand = new Random();
		for (int i = 1; i < nodes.length-1; i++) 	{
			if (!nodes[i].isLocked) {
				dihedralAngle = PI*(2.0*rand.nextDouble() - 1);
				angle = getDihedralAngle(i);
				changeRotationAngle(i, dihedralAngle - angle);
				logBook.clear();
			}
		}
	}


	/*
	 * generates random dihedral angles associated with the leaves
	 */
	public void randomDihedralAngles() {
		Random rand = new Random();
		for (int i = 0; i < nodes.length; i++) 	{
			if (!nodes[i].isLocked) {
				changeRotationAngle(i, 2.0*rand.nextDouble()*PI);
				logBook.clear();
			}
		}
	}

	/*
	 * returns true if a rotation around the i-th bond resulted in a clashing structure
	 */
	public boolean isClashing(int i) {
		CTNode nd = nodes[i], ndP, ndS;
		Set<CTNode> leftSack  = new Set<CTNode>(getHeight());
		Set<CTNode> rightSack = new Set<CTNode>(getHeight());
		leftMatrix.copy(nd.matr);
		rightMatrix.reset();
		while (nd != root) {
			ndP = nd.parent;
			if (ndP.left == nd){
				ndS = ndP.right;
				if (isClashing(i, leftSack, ndS, rightMatrix)) return true;
				rightSack.insert(ndS);
				rightMatrix.multR(ndS.matr);
			}
			else {
				ndS = ndP.left;
				if (isClashing(i, ndS, rightSack, leftMatrix)) return true;
				leftSack.insert(ndS);
				leftMatrix.multL(ndS.matr);
			}
			nd = ndP;
		}
		return false;
	}

//	public boolean isClashing(int i) {
//	CTNode nd = nodes[i], ndP;
//	Set<CTNode> leftSack  = new Set<CTNode>(100);
//	Set<CTNode> rightSack = new Set<CTNode>(100);
//	while (nd != root) {
//		ndP = nd.parent;
//		if (ndP.left == nd) rightSack.insert(ndP.right);
//		else leftSack.insert(ndP.left);
//		nd = ndP;
//	}
//	leftSack.reverse();
//	rightSack.reverse();
//	return isClashing(leftSack, rightSack);
//}

	
//	public boolean isClashing(int i) {
//		CTNode nd = nodes[i]; 
//		CTNode ndP = nd.parent;
//		CTNode left;
//		CTNode right;
//		while (ndP != null) {
//			left = ndP.left;
//			right = ndP.right;
//			if ((left != null) && (right != null) && areClashing(left, right)) return true;
//			nd = ndP;
//			ndP = nd.parent;
//		}
//		return false;
//	}

	
	
	private boolean isClashing(int i, Set<CTNode> leftSack, CTNode ndR, RotationMatrix4x4 rightMatrix) {
		RotationMatrix4x4 leftMatrix = new RotationMatrix4x4(nodes[i].matr);
		for (CTNode ndL : leftSack) {
			leftMatrix.multL(ndL.matr);
			RotationMatrix4x4 m = new RotationMatrix4x4(leftMatrix);
			m.multR(rightMatrix);
			if (areClashing(m, ndL, ndR)) return true;
		}
		return false;
	}


	private boolean isClashing(int i, CTNode ndL, Set<CTNode> rightSack, RotationMatrix4x4 leftMatrix) {
		RotationMatrix4x4 rightMatrix = new RotationMatrix4x4();
		for (CTNode ndR : rightSack) {
			rightMatrix.multR(ndR.matr);
			RotationMatrix4x4 m = new RotationMatrix4x4(rightMatrix);
			m.multL(leftMatrix);
			if (areClashing(m, ndL, ndR)) return true;
		}
		return false;
	}

	private boolean isClashing(Set<CTNode> leftSack, Set<CTNode> rightSack) {
		for (CTNode ndL : leftSack) 
			for (CTNode ndR : rightSack) if (areClashing(ndL, ndR)) return true;
		return false;
	}

	
	/*
	 * creates energy bounding volumes of the subtree rooted at node nd
	 */
	public void createEnergyBoundingVolume(CTNode nd) {
//		if (!nd.isLeaf()) {
		if(!(nd.right==null || nd.left==null)){
			createEnergyBoundingVolume(nd.left);
			createEnergyBoundingVolume(nd.right);
			updateEnergyBoundingVolume(nd);
		}
		else //nd.energyCapsule = new Capsule3d(new Point3d(0, 0, 0), new Point3d(nd.getPQ()), energyExtension);
			nd.energyVolume = volumeManager.createLeafVolume(nd, energyExtension);
	}

	/*
	 * recomputes bounding volume of node nd
	 */
	public void updateEnergyBoundingVolume(CTNode nd) {
		switch(boundingMode){
		case 0://capsule of two capsules
			Volume3d rightVol = volumeManager.transform(nd.low, nd.right.low, nd.right.energyVolume);
			nd.energyVolume = volumeManager.createEnclosingVolume(nd.left.energyVolume, rightVol);
			break;
		case 1://volume of all bonds
			nd.energyVolume = volumeManager.createEnclosingExtendedVolume(getPoints(nd), energyExtension);
			break;
		case 2://same as 0
			rightVol = volumeManager.transform(nd.low, nd.right.low, nd.right.energyVolume);
			nd.energyVolume = volumeManager.createEnclosingVolume(nd.left.energyVolume, rightVol);
			break;
		case 3: 
			if (nd.isLocked || (nd.height <= 2)) {
				nd.energyVolume = volumeManager.createEnclosingExtendedVolume(getPoints(nd),energyExtension);
			}else {
				Volume3d right = volumeManager.transform(nd.low, nd.right.low, nd.right.energyVolume);
				nd.energyVolume = volumeManager.createEnclosingVolume(nd.left.energyVolume, right);
			}
			break;
		}
	}

	
	/*
	 * return energy contribution across the specified bond
	 */
	public double getCrossEnergy(int i) {
		//		System.out.println("energy across bond " + i);
		CTNode nd = nodes[i];
		Set leftSack = new Set();
		Set rightSack = new Set();
		leftSack.insert(nd);
		rightSack.insert(nd);
		double energy = getCrossEnergy(nd,nd);
		while (nd != root) {
			if (nd.parent.right == nd) {
				leftSack.insert(nd.parent.left);
				energy = energy + getCrossEnergy(nd.parent.left, rightSack);
			}
			else {
				rightSack.insert(nd.parent.right);
				energy = energy + getCrossEnergy(leftSack, nd.parent.right);
			}
			nd = nd.parent;
		}
		return energy;
	}

	private double getCrossEnergy(Set leftSack, CTNode nd) {
		if (nd.low %3 == 1) {
			//			System.out.println("energy across leftSack and [" + nd.low + "," + nd.high + "]");
			double energy = 0;
			for (int i = 0; i < leftSack.getSize(); i++) energy += getCrossEnergy((CTNode)leftSack.get(i), nd);
			return energy;
		}
		else return 0;
	}

	private double getCrossEnergy(CTNode nd, Set rightSack) {
		if (nd.low % 3 == 1) {
			//			System.out.println("energy across [" + nd.low + "," + nd.high + "] and rightSack");
			double energy = 0;
			for (int i = 0; i < rightSack.getSize(); i++) energy += getCrossEnergy(nd, (CTNode)rightSack.get(i));
			return energy;
		}
		else return 0;
	}

	private double getCrossEnergy(CTNode left, CTNode right) {
		//		System.out.println("energy across [" + left.low + "," + left.high + "] and [" + right.low + "," + right.high + "]");
		if (left.isLeaf()) {
			if (left.low % 3 != 1) return 0;
			else {
				if (right.isLeaf()) {
					if (right.high % 3 != 1) return 0;
					else {
						Point3d p = getPosition(left.low, right.high + 1);
						if (p.getSquaredDistance() <= squaredContactRadius) return 1; else return 0;
					}
				}
				if (right.energyVolume == null) System.out.println("error");
				Volume3d rotatedVolume = volumeManager.transform(left.low, right.low, right.energyVolume);
//				Capsule3d rotatedCapsule = new Capsule3d(getPosition(left.low, 
//																	 right.low, 
//																	 right.energyCapsule.segment.getA()), 
//						                                 getPosition(left.low, 
//						                                		 	 right.low, 
//						                                		 	 right.energyCapsule.segment.getB()), 
//						                                 right.energyCapsule.rad);
// here we merely need to check if Point3d(0.0.0) is contained in the rotatedCapsule
//				if (rotatedVolume.contains(getPosition(left.low))) return getCrossEnergy(left, right.left) + getCrossEnergy(left, right.right); else return 0;
				if (rotatedVolume.overlaps(left.energyVolume)) return getCrossEnergy(left, right.left) + getCrossEnergy(left, right.right); else return 0;
			}
		}
		if (right.isLeaf()) {
			if (right.high %3 != 1) return 0;
			else {
				//Point3d rightPoint = getPosition(left.low, right.low);
				//if (left.energyCapsule.contains(rightPoint)) return getCrossEnergy(left.left, right) + getCrossEnergy(left.right, right); else return 0;
				Volume3d rotatedVolume = volumeManager.transform(left.low, right.low, right.energyVolume);
				if (rotatedVolume.overlaps(left.energyVolume)) return getCrossEnergy(left.left, right) + getCrossEnergy(left.right, right); else return 0;
			}
		}
		energyVolumeOverlapCount++;
//		if (left.energyCapsule.overlaps(right.energyCapsule)) return getCrossEnergy(left.left, right) + getCrossEnergy(left.right, right); else return 0;
		Volume3d rotatedVolume = volumeManager.transform(left.low, right.low, right.energyVolume);
		if (left.energyVolume.overlaps(rotatedVolume)) return getCrossEnergy(left.left, right) + getCrossEnergy(left.right, right); else return 0;
		
	}


	/*
	 * returns contact number of a specified Ca-atom (note that i modulo 3 must be 1)
	 */
	public int getContactNumber(int i) {
		if (i %3 == 1) {
			int contactNumber = 0;
			CTNode nd = null; 
			nd = nodes[i-1];
			while ((nd != root) && (nd.parent.right == nd)) {
				contactNumber = contactNumber + getContactNumber(nd.parent.left, i);	
				nd = nd.parent;
			}

			nd = nodes[i];
			while ((nd != root) && (nd.parent.left == nd)) {
				contactNumber = contactNumber + getContactNumber(i, nd.parent.right);	
				nd = nd.parent;
			}

			while (nd != root) {
				if (nd.parent.right == nd) 	contactNumber = contactNumber + getContactNumber(nd.parent.left, i);	
				else contactNumber = contactNumber + getContactNumber(i, nd.parent.right);	
				nd = nd.parent;
			}
			return contactNumber;
		}
		else return  -1;
	}

	private int getContactNumber(CTNode nd, int i) {
		//		System.out.println("left side: computing contact number for [" + nd.low + "," + nd.high + "] and " + i);
		if (nd.isLeaf()) {
			if (nd.low % 3 != 1) return 0;
			else {
				Point3d p = getPosition(nd.low, i);
				//				System.out.println(p.getSquaredDistance());
				int contactNumber = (p.getSquaredDistance() <= squaredContactRadius)? 1 : 0;
				//				if (contactNumber == 1) System.out.println("atoms " + (nd.low) + " and " + i + " are in contact");
				return contactNumber;
			}
		}
		return getContactNumber(nd.left, i) + getContactNumber(nd.right, i);
	}

	private int getContactNumber(int i, CTNode nd) {
		//		System.out.println("right side: computing contact number for " + i + " and [" + nd.low + "," + nd.high + "]");
		if (nd.isLeaf()) {
			if (nd.high % 3 != 1) return 0;
			else {
				Point3d p = getPosition(i, nd.high);
				//				System.out.println(p.getSquaredDistance());
				int contactNumber = (p.getSquaredDistance() <= squaredContactRadius)? 1 : 0;
				//				if (contactNumber == 1) System.out.println("atoms " + i + " and " + nd.high + " are in contact");
				return contactNumber;
			}
		}
		return getContactNumber(i, nd.left) + getContactNumber(i, nd.right);
	}


	public boolean insideBoundingVolume(CTNode subtree, CTNode leaf) { return true; }

	public boolean intersectingBoundingVolumes(CTNode leftSubtree, CTNode righSubtree) { return true; }

	public void showRotationMatrices() { root.showRotationMatrices(); }

	
	/*
	 * entire chain tree is written out
	 */
	public void toConsole() { toConsole(0); }

	/*
	 * chain tree with nodes of height at least k are written out
	 */
	public void toConsole(int k) {
		CTNode nd;
		Stack stack = new Stack();
		stack.push(root);
		while (!stack.isEmpty()) {
			nd = (CTNode)stack.pop();
			nd.toConsole();
			if (nd.isInternal() && (nd.height > k)) {
				stack.push(nd.right);
				stack.push(nd.left);
			}			
		}
	}

	/*
	 * chain tree with nodes within [i,j] and height at least k are written out
	 */
	public void toConsole(int i, int j, int k) {
		CTNode nd;
		Stack stack = new Stack();
		stack.push(root);
		while (!stack.isEmpty()) {
			nd = (CTNode)stack.pop();
			if (((nd.low <= i) && (i <= nd.high)) || ((nd.low <= j) && (j <= nd.high)) || ((i < nd.low) && (nd.high < j))) nd.toConsole();
			if ((nd.right != null) && (nd.right.height >= k)) stack.push(nd.right);
			if ((nd.left  != null) && (nd.left.height  >= k)) stack.push(nd.left);
		}
	}

	public void toConsole(CTNode nd) { toConsole(nd, 0); }
	
	public void toConsole(CTNode nd, int k) {
		CTNode cnd;
		Stack stack = new Stack();
		stack.push(nd);
		while (!stack.isEmpty()) {
			cnd = (CTNode)stack.pop();
			cnd.toConsole();
			if ((cnd.right != null) && (cnd.right.height >= k)) stack.push(cnd.right);
			if ((cnd.left != null) && (cnd.left.height >= k)) stack.push(cnd.left);
		}
	}
	
	public boolean areClashing(CTNode ndL, CTNode ndR) {
		if (ndL.low + 3 < ndR.high) {
			RotationMatrix4x4 m = getTransformMatrix4x4(ndL.low, ndR.low);
			return areClashing(m, ndL, ndR);
		}
		return false;
	}
	
	public boolean areClashing(RotationMatrix4x4 m, CTNode ndL, CTNode ndR) {
		if (ndL.low + 3 < ndR.high) {
			Volume3d rotatedVol = volumeManager.transform(m, ndR.volume);
			
// what was the purpose of this?
//			clashVolumeOverlapCount++;
//			if (clashCheckedPairs != null) {
//				clashCheckedPairs.add(new Object[]{ndL.volume.clone(),rotatedVol.clone(),ndR.volume, ndL.low, ndR.low, volumeManager});
//			}
			clashVolumeOverlapCount++;          
			if (ndL.volume.overlaps(rotatedVol)) {
				if (ndL.left == null) {                         
					if (ndR.left == null){                                      // ndL and ndR are leaves     // do we really need this in case of capsules (especially for 2 pairs of CaC bonds)
						Point3d p3 = getPosition(m);
						if (p3.getSquaredDistance()   <= squaredClashDistance) { primitiveOverlapCount++;    return true; }
						Point3d p4 = getPosition(m, ndR.matr.get14(), ndR.matr.get24(), ndR.matr.get34());
						if (p4.getSquaredDistance()   <= squaredClashDistance) { primitiveOverlapCount += 2; return true; }
						Point3d p2 = new Point3d(ndL.matr.get14(), ndL.matr.get24(), ndL.matr.get34());
						if (p2.getSquaredDistance(p3) <= squaredClashDistance) { primitiveOverlapCount += 3; return true; }
						primitiveOverlapCount += 4;
						return p2.getSquaredDistance(p4) <= squaredClashDistance;
					}
					if (areClashing(m, ndL, ndR.left)) return true;          // ndL is leaf, ndR is interior
					RotationMatrix4x4 m1 = new RotationMatrix4x4(m);
					m1.multR(ndR.left.matr);
					return areClashing(m1, ndL, ndR.right);
				}
				if (ndR.left == null)                                        // ndL is interior, ndR is leaf 
					return (areClashing(m, ndL.left, ndR) ? true : areClashing(ndL.right, ndR));
				
				if (ndL.volume.volume() > ndR.volume.volume())               // ndL and ndR are interior
					return (areClashing(m, ndL.left, ndR) ? true : areClashing(ndL.right, ndR));
				
				if (areClashing(m, ndL, ndR.left)) return true;
				RotationMatrix4x4 m1 = new RotationMatrix4x4(m);
				m1.multR(ndR.left.matr);
				return areClashing(m1, ndL, ndR.right);
			}
			disjointCapsules[ndL.height][ndR.height]++;
			return false;
		}
		return false;
	}

	/*	public boolean areClashing(CTNode ndL, CTNode ndR) {
		if (ndL.isLeaf()) {
			if (ndR.isLeaf()) {

			}
			else {
				Cylinder3d cylinderR = getPosition(ndR.cylinder);
				Segment3d sgmR = new Segment3d(cylinderR.getAnchor(), cylinderR.getDir());

			}
		}
		else {
			Cylinder3d cylinderL = getPosition(ndL.cylinder);
			Segment3d sgmL = new Segment3d(cylinderL.getAnchor(), cylinderL.getDir());
			if (ndR.isLeaf()) {

			}
			else {
				Cylinder3d cylinderR = getPosition(ndR.cylinder);
				Segment3d sgmR = new Segment3d(cylinderR.getAnchor(), cylinderR.getDir());
				if (sgmL.getSquaredDistance(sgmR) > squaredClashDist) return false;
			}
		}
	}
	 */	

	/*
	 * creates bounding volumes of the subtree rooted at node nd
	 */
	public void createBoundingVolume(CTNode nd) {
//		if (!nd.isLeaf()) {
//		if(!(nd.left==null || nd.right==null)){
		if ((nd.left != null) && (nd.right != null)) {
			createBoundingVolume(nd.left);
			createBoundingVolume(nd.right);
			updateBoundingVolume(nd);
		}
		else nd.volume = volumeManager.createLeafVolume(nd, extension);
	}

	/*
	 * recomputes bounding volume of node nd
	 */
	public void updateBoundingVolume(CTNode nd) {
		clashVolumeUpdateCount++;
		PointSet3d exPoints;
		Vector3d CaC, NC, CO, NH, CN, CaN;
		Point3d H, O;
		switch(boundingMode){
		case 0://volume of two children
//			Volume3d rightVol = volumeManager.transform(nd.low, nd.right.low, nd.right.volume);
			Volume3d rightVol = volumeManager.transform(nd.left.matr, nd.right.volume);
			nd.volume = volumeManager.createEnclosingVolume(nd.left.volume, rightVol);
			break;
		case 1://volume of all bonds
			nd.volume = volumeManager.createEnclosingExtendedVolume(getPoints(nd), extension);
			break;
		case 2://same as 0
//			rightVol = volumeManager.transform(nd.low, nd.right.low, nd.right.volume);
			rightVol = volumeManager.transform(nd.left.matr, nd.right.volume);
			nd.volume = volumeManager.createEnclosingVolume(nd.left.volume, rightVol);
			break;
		case 3://Optimized .. volume of bonds for locked subtrees, volume of children otherwise
			if (nd.isLocked || (nd.height <= 2 && peptidePlanes)) {
//				if (nd.height == 2) {                   // added to check if peptide planes with NH and CO bonds overlap
//					exPoints = new PointSet3d(getPoints(nd));
//					CaC = new Vector3d(exPoints.get(0), exPoints.get(1));
//					NC  = new Vector3d(exPoints.get(2), exPoints.get(1));
//					CO = CaC.add(NC).scaleToLength(1.23);
//					O = exPoints.get(1).add(CO);
//					exPoints.insert(O);
//					oxygenAtoms.insert(getPosition(nd.low+1).add(CO));
//					CN = NC.reverse();
//					CaN = new Vector3d(exPoints.get(3), exPoints.get(2));
//					NH = CN.add(CaN).scaleToLength(1.05);
//					H = exPoints.get(2).add(NH);
//					exPoints.insert(H);
//					hydrogenAtoms.insert(getPosition(nd.low+2).add(NH));
//					nd.volume = volumeManager.createEnclosingExtendedVolume(exPoints, extension);
//
//				}
//				else
				nd.volume = volumeManager.createEnclosingExtendedVolume(getPoints(nd), extension);
//				if(warn && nd.height==0 && (nd.left!=null&&nd.right!=null)){
//					throw new Error("stop");
//				}
			}
			else {
//				Volume3d rightVolume = volumeManager.transform(nd.low, nd.right.low, nd.right.volume);
				Volume3d rightVolume = volumeManager.transform(nd.left.matr, nd.right.volume);
				nd.volume = volumeManager.createEnclosingVolume(nd.left.volume, rightVolume);

// what is the purpose of this if statement?
//				if(clashUpdatePairs!=null){
//					clashUpdatePairs.add(new Object[]{nd.left.volume.clone(), rightVolume.clone(), nd.right.volume.clone(), nd.low, nd.right.low, volumeManager});
//				}
			}
			break;
		}
	}
	public static boolean warn = false;

	public void clashTest() {
		if (areClashing(root.left, root.right)) System.out.println("clash detected");
		else System.out.println("no clash detected");
	}

	public void restore() {
		CTNode nd;
		while (!logBook.isEmpty()) {
			nd = (CTNode)logBook.pop();
			if (!nd.isLeaf()) {
				nd.volume = (Volume3d)logBook.pop();
				nd.matr = (RotationMatrix4x4)logBook.pop();
			}
			else {
				nd.matr = (RotationMatrix4x4)logBook.pop();
				nd.d = (Double)logBook.pop();
				nd.c = (Double)logBook.pop();
				nd.s = (Double)logBook.pop();
				nd.a = (Double)logBook.pop();
			}
		}
	}

	/*
	 * keeps rotating forever around one bond at a time
	 */
	public void rotate() {
		double bestRMSD = 99999999.9;
		if (rmsdMode == 1) { 
			getDistanceMatrix();
			bestRMSD = getPairwiseDistanceRMSD();
		}
		if (rmsdMode == 2) bestRMSD = getDihedralAnglesRMSD(nodes.length);
		double rmsd, dihRMSD = 0;
		CTNode nd;
		Random rand = new Random(15);
		int n, i, count = 0;
		int size = 1;                            // maximum number of bonds that can be rotated in each iteration
		if (rmsdMode == 1) size = 3;
		boolean clashDetected;
		int[] bond = new int[size];
		double angle;
		while (true) {
			count++;
			n = rand.nextInt(size) + 1;
			clashDetected = false;
			for (i = 0; i < n; i++) {
				bond[i] = rand.nextInt(nodes.length); while (nodes[bond[i]].isLocked) bond[i] = rand.nextInt(nodes.length);
				nd = nodes[bond[i]];
				logBook.push(nd.a); logBook.push(nd.s); logBook.push(nd.c); logBook.push(nd.d);
				logBook.push(new RotationMatrix4x4(nd.matr)); logBook.push(nd);
				angle = PI*(2*rand.nextDouble()-1);				
				if (rmsdMode == 2) changeRotationAngle(bond[i], angle/18);
				else changeRotationAngle(bond[i], angle);
				// detection of clashes is done after each dihedral rotation - clashDetectionMode = 0
				if (clashDetectionMode == 0) {
					long startTaskTime = System.nanoTime( );
					clashDetected = isClashing(bond[i]);
					long taskTime  = System.nanoTime( ) - startTaskTime;
					System.out.println("isClashing - Task  time " + startTaskTime + " " + taskTime);
					if (clashDetected) { restore(); i = n; }
				}
			}
			// detection of clashes is done after all n dihedral rotation - clashDetectionMode = 1
			if (clashDetectionMode == 1) {
				for (i = n - 1; i >= 0; i--) {
					if (isClashing(bond[i])) {
						restore();
						clashDetected = true;
						i = n;
					}
				}
			}
			if (!clashDetected) {
				if (rmsdMode == 2) rmsd = getDihedralAnglesRMSD(nodes.length);
				else rmsd = getPairwiseDistanceRMSD();
				if (rmsd < bestRMSD)  {
					bestRMSD = rmsd;
					System.out.println(count + ". rmsd = " + bestRMSD + " improving rotation, n = " + n + ", 1. bond = " + bond[0] + 
							           " dihedral RMSD = " + dihRMSD);
					logBook.clear();
					repaint();
				}
				else restore();
			}
		}
	}

	public void initPaint() {
		if(j3dg==null) j3dg = J3DScene.createJ3DSceneInFrame();
		
		initPaint(0, nodes.length);
//		initPaint(0, 3);
	}
	public void initPaint(int low, int high) {
		j3dg.removeAllShapes();
		j3dg.setBackgroundColor(Color.white);
		CTNode nd;
		Point3d q; 
		Point3d p = new Point3d();
		j3dg.addShape(new Sphere3d(new Point3d(p), 0.775), colors[4]);
		RotationMatrix4x4 m = new RotationMatrix4x4();
		
		Color clr;
//		String symb = null;
		Point3d textPoint;
		for (int i = 1; i <= nodes.length; i++) {
			if (i%3 == 0) clr = colors[4];
			else {
				if (i%3 == 1) { 
					clr = colors[6];
//					symb = protein.getAminoAcid((i-1)/3).getName();
				}
				else clr = colors[7]; 
			}
			nd = nodes[i-1];
			m.multR(nd.matr); q = getPosition(m);
			if (i%3 == 1) {
				textPoint = new Point3d(p.x + 0.2, p.y + 0.2, p.z + 0.2);
//				j3dg.addText(symb, textPoint, 0.4);
			}
			nd.sphere = new Sphere3d(new Point3d(q), extension);
			nd.cylinder = new Cylinder3d(new Point3d(p), new Point3d(q), 0.4f);
			j3dg.addShape(nd.sphere, clr);
			if (nd.isLocked) {
				if (i % 3 == 0) j3dg.addShape(nd.cylinder,Color.cyan);
				else j3dg.addShape(nd.cylinder, Color.cyan); 
			}
			else j3dg.addShape(nd.cylinder);
			p = q;
		}
		
		AminoAcid aminoAcid;
		SideChain sideChain;
		Carbon Ca;
		char symbol;
		for (int i = 0; i < protein.getNumberAminoAcids(); i++) {
			aminoAcid = protein.getAminoAcid(i);
			Ca = aminoAcid.getCa();
			sideChain = aminoAcid.getSideChain();
			System.out.print(aminoAcid.getSymbol());
			symbol = aminoAcid.getSymbol();
			if ((symbol == 'A') || (symbol == 'C') || (symbol == 'D') || (symbol == 'E') || (symbol == 'F') ||
				(symbol == 'H') || (symbol == 'I') || (symbol == 'K') || (symbol == 'L') || (symbol == 'M') || 
				(symbol == 'N') || (symbol == 'P') || (symbol == 'R') || (symbol == 'S') || (symbol == 'Y') ||
				(symbol == 'T') || (symbol == 'V') || (symbol == 'W') || (symbol == 'Q'))  {
//				sideChain.draw(j3dg, this, 3*i+1, Ca);
			}
			
		}
		
//		Point3d z;
//		for (int i = 0; i < oxygenAtoms.getSize(); i++) {
//			z = oxygenAtoms.get(i); 
//			j3dg.addShape(new Sphere3d(new Point3d(z), 0.775), Color.red);
//		}
//			for (int i = 0; i < hydrogenAtoms.getSize(); i++) 
//			j3dg.addShape(new Sphere3d(new Point3d(hydrogenAtoms.get(i)), 0.6), Color.lightGray);
		
		Stack stack = new Stack();
		stack.push(root);
		while (!stack.isEmpty()) {
			nd = (CTNode)stack.pop();
			if (  ((nd.height <= drawCapsuleLevelUpper) && (nd.height>=drawCapsuleLevelLower))  )  {
//				if (  ((nd.height <= drawCapsuleLevelUpper) && (nd.height>=drawCapsuleLevelLower)) ||
//						((nd.parent!=null) && (nd.parent.height <= drawCapsuleLevelUpper) && (nd.parent.height>=drawCapsuleLevelLower)) )  {
				if ((low <= nd.low) && (nd.high <= high)) {
					nd.drawVolume = volumeManager.transform(0, nd.low, nd.volume); 

					j3dg.addShape(nd.drawVolume, colors[nd.height]);
					// if a node hase height > drawCapsuleLevelLower, capsules of both children are drawn no matter what their heights are.
					/*if ((nd.left != null) && (nd.height > drawCapsuleLevelLower) && (nd.left.height < drawCapsuleLevelLower)) {
						nd.left.drawCapsule = new Capsule3d(getPosition(0, nd.left.low, nd.left.capsule.segment.getA()), 
															getPosition(0, nd.left.low, nd.left.capsule.segment.getB()), 
															nd.left.capsule.rad);
						j3dg.addShape(nd.left.drawCapsule, colors[nd.height]);
					}
					if ((nd.right != null) && (nd.height > drawCapsuleLevelLower) && (nd.right.height < drawCapsuleLevelLower)) {
						nd.right.drawCapsule = new Capsule3d(getPosition(0, nd.right.low, nd.right.capsule.segment.getA()), 
															 getPosition(0, nd.right.low, nd.right.capsule.segment.getB()), 
										  					 nd.right.capsule.rad);
						j3dg.addShape(nd.right.drawCapsule, colors[nd.height]);
					}*/
				}
			}
			if (!nd.isLeaf()) {
				stack.push(nd.left);
				stack.push(nd.right);
			}
		}
	}


	public void repaint() {
		Point3d q;
		Point3d p = new Point3d(0,0,0);
		RotationMatrix4x4 m = new RotationMatrix4x4();
		CTNode nd;
		for (int i = 1; i <= nodes.length; i++) {
			nd = nodes[i-1];
			m.multR(nd.matr); 
			q = getPosition(m);
			nd.sphere.center = new Point3d(q.x, q.y, q.z);
			nd.cylinder.getSegment().setA(new Point3d(p.x, p.y, p.z));
			nd.cylinder.getSegment().setB(new Point3d(q.x, q.y, q.z));
			p = q;
		}	
		Stack stack = new Stack();
		stack.push(root);
		while (!stack.isEmpty()) {
			nd = (CTNode)stack.pop();
				if (nd.drawVolume!=null) {
					volumeManager.updateVolumePosition(
							nd.drawVolume, 
							volumeManager.transform(0, nd.low, nd.volume)
					);
				}
			if (!nd.isLeaf()) {
				stack.push(nd.right);
				stack.push(nd.left);
			}
		}
		j3dg.repaint();
		//		j3dg.centerCamera();
	}
	
	/** Get CPU time in nanoseconds. */
	public long getCpuTime( ) {
	    ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
	    return bean.isCurrentThreadCpuTimeSupported( ) ?
	        bean.getCurrentThreadCpuTime( ) : 0L;
	}
	 
	/** Get user time in nanoseconds. */

	public long getUserTime( ) {
	    ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
	    return bean.isCurrentThreadCpuTimeSupported( ) ?
	        bean.getCurrentThreadUserTime( ) : 0L;
	}

	
	public static void main(String[] args) {
		String str= "1X5RA";   // "1X0OA" "1X5RA" "1XDXA" "1AKPA" "1Y6DA" "1CTF" "1A0A" "1A6X" "1CTJ" "1JBOA"
		Protein protein = new Protein(str, 0, true);
		PointSet3d allPoints = protein.getPointSetBackbone();
		PointSet3d points = new PointSet3d();
		for (int i = 0; i < allPoints.getSize(); i++) points.insert(allPoints.get(i));
//		for (int i = 0; i < 111; i++) points.insert(allPoints.get(i));

		ChainTree cTree = new ChainTree(points, protein, true);
		
// 		locks peptide bonds		
		for (int i = 2; i < cTree.nodes.length; i = i+3) cTree.nodes[i].isLocked = true;

// 		locks the first and last bond		
		cTree.nodes[cTree.nodes.length-1].isLocked = true;
		cTree.nodes[0].isLocked = true;
		
//      locks secondary structures		
		cTree.lockAlphaHelices(true);
		cTree.lockBetaStrands(true);
//		ChainTreePainter.displayChainTree(cTree);
		
//		removes secondary structure subtrees		
		cTree.removeLockedSubtrees(cTree.peptidePlanes);

//      rebalances
		cTree.newRebalanceSubtree(cTree.root);
		cTree.addLockedSubtrees(cTree.peptidePlanes);
//		ChainTreePainter.displayChainTree(cTree);
		
		
		cTree.j3dg = J3DScene.createJ3DSceneInFrame();
		cTree.createBoundingVolume(cTree.root);

//		System.out.println("Total volume of volumes is " +  cTree.getTotalCapsulesVolume()+", root volume is "+cTree.getRootCapsuleVolume());
//		ChainTreeStatistics.getBoundingVolumesStatistics(cTree);		
//		cTree.createEnergyBoundingVolume(cTree.root);

//		ChainTreeStatistics.getRotationStatistics(cTree);
		
		// all dihedral angles, apart from locked bonds, are set to PI
		cTree.setFixedDihedralAngles(Math.PI);
//		System.out.println("Total volume of capsules is " +  cTree.getTotalCapsulesVolume());

		cTree.initPaint();
		cTree.rotate();
	
		/*		ChainTree cTree1 = new ChainTree(points, protein, 0);
		for (int i = 2; i < cTree1.nodes.length; i = i+3) cTree1.nodes[i].isLocked = true;
		cTree1.nodes[cTree1.nodes.length-1].isLocked = true;
		cTree1.nodes[0].isLocked = true;
//		cTree1.lockAlphaHelices();
//		cTree1.lockBetaStrands();
		
		J3DScene j3dg1 = J3DScene.createJ3DSceneInFrame();
		
		// all dihedral angles, apart from locked bonds, are set to PI
		cTree1.setFixedDihedralAngles(Math.PI);
		cTree1.initPaint(j3dg1);
		cTree1.createBoundingVolume(cTree1.root, cTree1.extension, j3dg1);
		cTree1.rotate(j3dg1);

		
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20 ; j++) {
				System.out.print((cTree.disjointCapsules[i][j] - cTree1.disjointCapsules[i][j]) + " ");
			}
			System.out.println();
		}
*/
		//		for (int i = 1; i <= protein.getSize(); i++) System.out.print(cTree.getContactNumber(3*i-2) + " ");

//		for (int i = 0; i < 20; i++) System.out.println(i + ". " + cTree.clashStatistics[i]);
		//		System.out.println(cTree.getCrossEnergy(55));


		//		J3DScene j3dg = J3DScene.createJ3DSceneInFrame();
		//		j3dg.startRotating();
		//		cTree.initPaint(j3dg);
		//		cTree.repaint(j3dg);

		/*		
		int i1, i2, i3, i4;
		double angle1, angle2, angle3, angle4;
		int energy;
		int minEnergy = 999999999, maxEnergy = 0;
		Random rand = new Random();
		double crossEnergy1, crossEnergy2, crossEnergy3, crossEnergy4, newCrossEnergy1, newCrossEnergy2, newCrossEnergy3, newCrossEnergy4;

		while (true) {		
			i1 = rand.nextInt(n);
			while (cTree.nodes[i1].isLocked) i1 = rand.nextInt(n);
//			i2 = rand.nextInt(n);
//			while ((cTree.nodes[i2].isLocked) || (i2 == i1)) i2 = rand.nextInt(n);
//			i3 = rand.nextInt(n);
//			while ((cTree.nodes[i3].isLocked) || (i3 == i1) || (i3 == i2)) i3 = rand.nextInt(n);
//			i4 = rand.nextInt(n);
//			while ((cTree.nodes[i4].isLocked) || (i4 == i1) || (i4 == i2) || (i4 == i3)) i4 = rand.nextInt(n);
//			crossEnergy1 = cTree.getCrossEnergy(i1);
			angle1 = rand.nextDouble()/50 + 0.01;
			cTree.changeRotationAngle(i1, angle1);
//			newCrossEnergy1 = cTree.getCrossEnergy(i1);
//			crossEnergy2 = cTree.getCrossEnergy(i2);
//			angle2 = rand.nextDouble()/50 + 0.01;
//			cTree.changeRotationAngle(i2, angle2);
//			newCrossEnergy2 = cTree.getCrossEnergy(i2);
//			crossEnergy3 = cTree.getCrossEnergy(i3);
//			angle3 = rand.nextDouble()/50 + 0.01;
//			cTree.changeRotationAngle(i3, angle3);
//			newCrossEnergy3 = cTree.getCrossEnergy(i3);				
//			crossEnergy4 = cTree.getCrossEnergy(i4);
//			angle4 = rand.nextDouble()/50 + 0.01;
//			cTree.changeRotationAngle(i4, angle4);
//			newCrossEnergy4 = cTree.getCrossEnergy(i4);				
//			energy = 0;
			for (int i = 1; i <= protein.getSize(); i++) energy = energy + cTree.getContactNumber(3*i-2);
			if (energy > minEnergy) {
//			if (maxEnergy >= energy) {
//			if (crossEnergy1 + crossEnergy2 + crossEnergy3 <= newCrossEnergy1 + newCrossEnergy2 + newCrossEnergy3) {
//				cTree.changeRotationAngle(i4, -angle4);
//				cTree.changeRotationAngle(i3, -angle3);
//				cTree.changeRotationAngle(i2, -angle2);
				cTree.changeRotationAngle(i1, -angle1);
			}
			else {
				minEnergy = energy;
//				maxEnergy = energy;
//				System.out.println(i1 + "," + i2 + "," + i3 + ": energy down by " + (crossEnergy1+crossEnergy2+crossEnergy3 - newCrossEnergy1-newCrossEnergy2-newCrossEnergy3));
				System.out.println("total energy is " + energy);
				cTree.repaint(j3dg);
				try { Thread.sleep(20); } catch (InterruptedException e) { e.printStackTrace(); }
			}
		}
		 */
	}

}
