package chainTree;

import java.awt.Color;
import java.util.Random;

import javax.swing.JFrame;

import jbcl.calc.structural.Crmsd;
import jbcl.calc.structural.transformations.Rototranslation;
import jbcl.data.basic.TwoTuple;
import jbcl.data.types.Vector3D;

import matrix.RotationMatrix4x4;
import molecule.AlphaHelix;
import molecule.BetaSheet;
import molecule.BetaStrand;
import molecule.Protein;
import geom3d.Capsule3d;
import geom3d.Point3d;
import geom3d.Segment3d;
import geom3d.Sphere3d;
import geom3d.Vector3d;
import geom3d.Line3d;
import geom3d.Cylinder3d;
import geom3d.PointSet3d;
import dataStructures.Queue;
import dataStructures.Set;
import dataStructures.Stack;
import j3dScene.J3DScene;
import misc.Constants;

 

public class ChainTreeTest extends ChainTree {
//	PointSet3d points;
//	CTNode root;
//	CTNode[] nodes;                  // leaves of the chain tree corresponding to bonds 
////	double[] nativeDihedralAngles;
//	int[] nativeContactVector;
//	int[] contactVector;
//	Protein protein;
//	Stack logBook = new Stack();
//	double[][] distances;           // distances between atoms in the native structure, -1 if unknown
//	int activeBound;
//	
//	int clashCalls;
//	int[] clashStatistics = new int[20];
//	int[][] disjointCapsules  = new int[20][20];
//	private final double PIPI = 2*Constants.pi;
//	public final double clashDist = 1.7;
//	public final double squaredClashDistance = clashDist*clashDist;
//	public final double extension = clashDist/2;
//	public final double squaredClashDist = clashDist*clashDist;
//	public final int contactRadius = 14;
//	public final int squaredContactRadius = contactRadius*contactRadius;
//	
//	private J3DScene j3dg;
//	
//	private final int clashDetectionMode = -1;    //-1 = no clash detection
//												 // 0 = after each single dihedral rotation
//										         // 1 = after all n dihedral rotations
//	private  int boundingMode = 0;               // 0 = bounding volumes of 2 nested bounding volumes
//	                                             // 1 = bounding volume of all points
//
//	public final int drawCapsuleLevelLower = 3, drawCapsuleLevelUpper = 3;
//	// magenta, green, red, cyan, blue, yellow
//	private Color[] colors = { new Color(255, 0, 255, 70), new Color(0, 255, 0, 70), new Color(255, 0, 0, 70),
//			new Color(0, 255, 255, 70), new Color(0, 0, 255, 70), new Color(255, 255, 0, 70),
//			new Color(0, 255, 255, 70), new Color(0, 0, 255, 70), new Color(255, 255, 0, 70),
//			new Color(0, 255, 255, 70), new Color(0, 0, 255, 70), new Color(255, 255, 0, 70), 
//			new Color(0, 255, 255, 70), new Color(0, 0, 255, 70), new Color(255, 255, 0, 70) };
//
//
//	public ChainTreeTest(PointSet3d points, Protein protein) {
//		super();
//		this.points = points;
//		this.protein = protein;
//
//		proteinDihedralAngles = protein.getDihedralAngles(); 
//		int n = points.getSize()-1;  // number of bonds in the backbone
//		System.out.println("n = " + n + ", " + (int)(n+1)/3);
//		nodes = new CTNode[n];
//		nativeContactVector = new int[(n+4)/3];
//		contactVector = new int[(n+4)/3];
//		Queue queue = new Queue();
//
//		for (int i = 0; i < n; i++) queue.push(nodes[i] = new CTNode(points,i));
//
//		int sz = n, sz2;
//		while (sz > 1) {
//			sz2 = sz/2;
//			//			System.out.println("Queue size = " + sz + " sz2 = " + sz2);
//			for (int k = 0; k < sz2; k++) queue.push(new CTNode((CTNode)queue.pop(), (CTNode)queue.pop()));
//			if (2*sz2 != sz) {
//				queue.push(queue.pop());
//				sz2++; 
//			}
//			sz = sz2;
//		}
//		root = (CTNode)queue.pop();
//		queue.clear();
//	}
//
//	private void setupNativeViewer(){
//		J3DScene scene = J3DScene.createJ3DSceneInFrame();
//		scene.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//		scene.frame.setLocation(new java.awt.Point(600,0));
//
//		class NativeUpdater implements Runnable{
//			private final J3DScene scene;
//			NativeUpdater(J3DScene scene){
//				this.scene = scene;
//			}
//			public void run(){
//				while(scene.frame.isVisible()){
//					
//					PointSet3d nativeCoords = new PointSet3d();
//					PointSet3d strucCoords = new PointSet3d();
//
//					for(int i=0;i<Math.min(activeBound-1,points.getSize());i++){
//						nativeCoords.insert(points.get(i));
//						strucCoords.insert(getPosition(i));
//					}
//
//					Vector3D[][] jbclCoords = RMSD.getJBCLCoords(strucCoords, nativeCoords);
//					TwoTuple<Double, Rototranslation> optTrans = 
//						Crmsd.optimalCrmsdTransformation(jbclCoords[0], jbclCoords[1]);
//					optTrans.second.transform(jbclCoords[1]);
//					Vector3D prev = null, nativePrev = null;
//					scene.removeAllShapes();
//					for(int a=0;a<jbclCoords[0].length;a++){
//						
//						if(prev!=null && nativePrev!=null){
//							scene.addShape(new Capsule3d(
//									new Point3d(prev.x, prev.y, prev.z), 
//									new Point3d(jbclCoords[0][a].x,jbclCoords[0][a].y,jbclCoords[0][a].z), 
//									0.17));
//							scene.addShape(new Capsule3d(
//									new Point3d(nativePrev.x, nativePrev.y, nativePrev.z), 
//									new Point3d(jbclCoords[1][a].x,jbclCoords[1][a].y,jbclCoords[1][a].z), 
//									0.17), new Color(100,100,100,50));
//						}
//						prev = jbclCoords[0][a];
//						nativePrev = jbclCoords[1][a];
//					}
//					scene.centerCamera();
//					scene.autoZoom();
//					try{Thread.sleep(2000);}catch(InterruptedException exc){}
//				}
//			}
//		}
//
//		Thread nativeThread = new Thread(new NativeUpdater(scene));
//		nativeThread.start();
//	}
//
//	/*
//	 * rotates nodes so that the chain tree has leaves between node l and node r
//	 * (both included) only. All these nodes are locked.
//	 */
//	public void lock(int l, int r) {
//		lockLeft(nodes[l], r);
//		CTNode nd = lockRight(nodes[r], l);
//		nd.isLocked = true;
//		for (int i = l; i <= r; i++) {
//			nd = nodes[i];
//			while (!nd.isLocked) {
//				nd.isLocked = true;
//				nd = nd.parent;
//			}
//		}
//	}
//
//	private CTNode lockLeft(CTNode nd, int r) {
//		CTNode cND = nd;	
//		while (cND.high < r) {                           
//			//			System.out.print("lockleft: at node "); cND.toConsole(); 
//			if (cND.parent.left == cND) cND = cND.parent;
//			else {
//				if (cND.parent.parent.right == cND.parent) {
//					//					System.out.print("lockleft: before left rotation of grand parent of node "); cND.toConsole(); 
//					cND.parent.parent.leftRotation(this);
//				}
//				else {
//					//					System.out.print("lockleft: before right rotation of grand parent of node "); cND.toConsole(); 
//					cND.parent.parent.rightRotation(this);
//				}
//			}
//		}
//		return cND;
//	}
//
//
//	private CTNode lockRight(CTNode nd, int l) {
//		CTNode cND = nd;
//		while (cND.low > l) {
//			//			System.out.print("lockright: at node "); cND.toConsole();
//			if (cND.parent.right == cND) cND = cND.parent;
//			else {
//				if (cND.parent.parent.left == cND.parent) {
//					//					System.out.print("lockright: before right rotation of grand parent of node "); cND.toConsole(); 
//					cND.parent.parent.rightRotation(this);
//				}
//				else {
//					//					System.out.print("lockright: before left rotation of grand parent of node "); cND.toConsole(); 
//					cND.parent.parent.leftRotation(this);
//				}
//			}
//		}
//		return cND;
//	}
//
//	public void lockAlphaHelices() {
//		int a, b;
//		AlphaHelix alphaHelix;
//		Set alphaHelices = protein.getAlphaHelices();
//		for (int i = 0; i < alphaHelices.getSize(); i++) {
//			alphaHelix = (AlphaHelix)alphaHelices.get(i);
//			a = protein.getIndex(alphaHelix.getInitSeqNum());
//			b = protein.getIndex(alphaHelix.getEndSeqNum());
//		    System.out.println("Locking a-helix: a = " + a + ", b = " + b + " locking bonds [" + (3*(a-1)) + "," + (3*b-2) + "]");
//			lock(3*(a-1), 3*b-2);    // chain tree is rebalanced
//			//			for (int k = 3*(a-1); k <= 3*b-2; k++) nodes[k].isLocked = true;   //chain tree is not rebalanced
//		}
//
//	}
//
//	public void lockBetaStrands() {
//		int a, b;
//		BetaSheet betaSheet;
//		Set betaSheets = protein.getBetaSheets();
//		BetaStrand betaStrand;
//		for (int i = 0; i < betaSheets.getSize(); i++) {
//			betaSheet = (BetaSheet)betaSheets.get(i);
//			for (int j = 0; j < betaSheet.getSize(); j++) {
//				betaStrand = (BetaStrand)betaSheet.getBetaStrand(j);
//				a = protein.getIndex(betaStrand.getInitSeqNum());
//				b = protein.getIndex(betaStrand.getEndSeqNum());
//				System.out.println("Locking b-strand: a = " + a + ", b = " + b + " locking bonds [" + (3*(a-1)) + "," + (3*b-2) + "]");
//				lock(3*(a-1), 3*b-2);  // chain tree is rotated
//				//				for (int k = 3*(a-1); k <= 3*b-2; k++) nodes[k].isLocked = true;   //chain tree is not rotated
//
//			}
//		}
//
//	}
//
//	public RotationMatrix4x4 getTransformMatrix4x4(int i, int j) {
//		// find split vertex
//		CTNode split = root;
//		boolean lookingForSplitVertex = true;
//		while (split.isInternal() && lookingForSplitVertex) {
//			if (j-1 <= split.left.high) split = split.left;
//			else {
//				if (split.right.low <= i) split = split.right;
//				else lookingForSplitVertex = false;	
//			}
//		}
//		//		System.out.println("split vertex is [" + split.low + "," + split.high + "]");
//		// 
//		if ((split.low == i) && (split.high == j-1)) return split.matr;
//		CTNode nd = split;
//		RotationMatrix4x4 m = new RotationMatrix4x4(1,0,0,0, 0,1,0,0, 0,0,1,0);
//		// left subtree
//		nd = nd.left;
//		while (nd != null) {
//			//			System.out.println("Going left: node [" + nd.low + "," + nd.high + "]");
//			if (nd.low == i) { m.multL(nd.matr); nd = null; }
//			else {
//				if (i <= nd.left.high) { m.multL(nd.right.matr); nd = nd.left; }
//				else nd = nd.right;
//			}
//		}
//		// right subtree
//		nd = split.right;
//		while (nd != null) {
//			//			System.out.println("Going right: node [" + nd.low + "," + nd.high + "]");
//			if (nd.high == j-1) { m.multR(nd.matr); nd = null; }
//			else {
//				if (nd.right.low <= j-1) { m.multR(nd.left.matr); nd = nd.right; } 
//				else nd = nd.left;
//			}
//		}
//		return m;
//	}
//
//	public PointSet3d getPoints(CTNode nd) {
//		PointSet3d pointSubset = new PointSet3d();
//		for (int i = nd.low; i <= nd.high+1; i++) pointSubset.insert(getPosition(nd.low, i));
//		return pointSubset;
//	}
//	
//	/* 
//	 * returns the coordinates of the j-th origo in the coordinate system of the i-th origo.
//	 */
//	public Point3d getPosition(int i, int j) {
//		if (i == j) return new Point3d(0, 0, 0);
//		RotationMatrix4x4 m = getTransformMatrix4x4(i, j);
//		return new Point3d(m.get14(), m.get24(), m.get34());
//	}
//
//	public Point3d getPosition(int i, int j, Point3d p) {
//		if (i == j) return p;
//		RotationMatrix4x4 m = getTransformMatrix4x4(i, j);
//		return new Point3d(m.get11()*p.getX() + m.get12()*p.getY() + m.get13()*p.getZ() + m.get14(),
//				m.get21()*p.getX() + m.get22()*p.getY() + m.get23()*p.getZ() + m.get24(),
//				m.get31()*p.getX() + m.get32()*p.getY() + m.get33()*p.getZ() + m.get34()); 
//	}
//	/* Ikke l¾ngere n¿dvendig --Rasmus
//	 * public Point3d getPosition(int i, int j, Vector  v) {
//		Point3d p = getPosition(i, j, new Point3d(v.x(), v.y(), v.z()));
//		return new Vector(p.getX(), p.getY(), p.getZ()); 
//	}*/
//
//
//	/* 
//	 * returns the coordinates of j-th origo in the coordinate system of 0-th origo
//	 */
//	public Point3d getPosition(int j)  { return getPosition(0, j);	}
//
//	public Point3d getPosition(int j, Point3d p) { return getPosition(0, j, p);	}
//
//	/* Ikke l¾ngere n¿dvendig --Rasmus
//	 * public Vector  getPosition(int j, Vector  v) {
//		Point3d p = getPosition(j, new Point3d(v.x(), v.y(), v.z()));
//		return new Vector(p.getX(), p.getY(), p.getZ()); 
//	}*/
//
//	public double getDihedralAngle(int i) {
//		if ((i < 1) && (i >= nodes.length-1 )) return 0.0;
//		else {
//			Point3d p = new Point3d(0,0,0);
//			Point3d q = getPosition(i-1,i);
//			Point3d r = getPosition(i-1,i+1);
//			Point3d s = getPosition(i-1,i+2);
//			Vector3d pq = new Vector3d(p,q);
//			Vector3d qr = new Vector3d(q,r);
//			Vector3d rs = new Vector3d(r,s);
//			return Vector3d.getDihedralAngle(pq, qr, rs);
//		}
//	}
//	
//	/*
//	 * computes dihedral angles of bonds (first and last bond excluded)
//	 */
//	public double[] getDihedralAngles() {
//		double[] dihedralAngles = new double[nodes.length];
//		dihedralAngles[0] = 0.0;
//		Point3d p, q, r , s;
//		Vector3d pq, qr, rs;
//		q = getPosition(0);
//		r = getPosition(1);
//		s = getPosition(2);
//		qr = new Vector3d(q, r);
//		rs = new Vector3d(r, s);
//		for (int i = 1; i < nodes.length-1; i++) {
//			p = q;
//			q = r;
//			r = s;
//			pq = qr;
//			qr = rs;
//			s = getPosition(i+2);
//			rs = new Vector3d(r, s);
//			dihedralAngles[i] = Vector3d.getDihedralAngle(pq, qr, rs);
//		}
//		dihedralAngles[nodes.length-1] = 0.0;
//		return dihedralAngles;
//	}
//
//	public void printDihedralAngles() {
//		double[] dihedralAngles = getDihedralAngles();
//		for (int i = 0; i < nodes.length; i++) System.out.println(i + ". dihedral angle = " + dihedralAngles[i]);
//	}
//
//	public void printDihedralAnglesDifferences() {
//		double[] dihedralAngles = getDihedralAngles();
//		for (int i = 0; i <= nodes.length; i++) 
//			System.out.println(i + ". dihedral angle difference = " + (dihedralAngles[i]-proteinDihedralAngles[i]));
//	}
//
//	public double getDihedralAnglesRMSD(double[] targetDihedralAngles, int size) {
//		double[] dihedralAngles = getDihedralAngles();
//		double sum = 0.0;
//		double diff;
//		for (int i = 1; i < size; i++) {
//			if (!nodes[i].isLocked) {
//				diff = getAngleDiff(dihedralAngles[i], targetDihedralAngles[i]);
//				sum += diff*diff;
//			}
//		}
//		return Math.sqrt(sum)/size;
//	}
//
//	public double getDihedralAnglesRMSD(int size) {
//		return getDihedralAnglesRMSD(proteinDihedralAngles, size);
//	}
//
//	
//	/*
//	 * returns RMSD between positions of atoms in native configuration and current configuration
//	 */
//	public double getRMSD() {
//		double sum = 0;
//		for (int i = 1; i <= nodes.length; i++) sum += points.get(i).getSquaredDistance(getPosition(i));
//		return Math.sqrt(sum/(nodes.length+1));
//	}
//	
//	/*
//	 * returns matrix with native distances between all pairs of atoms.
//	 */
//	public double[][] getDistanceMatrix() {
//		Point3d p;
//		int n = points.getSize();
//		double[][] sqm = new double[n][n];
//		for (int i = 0; i < n; i++) {
//			sqm[i][i] = 0;
//			p = points.get(i);
//			for (int j = i+1; j < n; j++) sqm[i][j] = sqm[j][i] = p.getDistance(points.get(j));
//		}
//		return sqm;
//	}
//
//	public double[][] getDistanceSubmatrix() {
//		Point3d p;
//		int n = points.getSize();
//		double[][] sqm = new double[n][n];
//		for (int i = 0; i < n; i++) {
//			for (int j = i+1; j < n; j++) sqm[i][j] = sqm [j][i] = -1; 
//			sqm[i][i] = 0;
//			p = points.get(i);
//			if (i < n-1) sqm[i][i+1] = sqm[i+1][i] = p.getDistance(points.get(i+1));		
//		    if (i < n-2) sqm[i][i+2] = sqm[i+2][i] = p.getDistance(points.get(i+2));		
//		}
//		int a, b;
//		AlphaHelix alphaHelix;
//		Set alphaHelices = protein.getAlphaHelices();
//		for (int i = 0; i < alphaHelices.getSize(); i++) {
//			alphaHelix = (AlphaHelix)alphaHelices.get(i);
//			a = protein.getIndex(alphaHelix.getInitSeqNum());
//			b = protein.getIndex(alphaHelix.getEndSeqNum());
//			for (int j = 3*(a-1); j < 3*b-1; j++) {
//				p = points.get(j);
//				for (int k = j+1; k < 3*b-1; k++) sqm[j][k] = sqm[k][j] = p.getDistance(points.get(k));
//			}
//		}
//		BetaSheet betaSheet;
//		Set betaSheets = protein.getBetaSheets();
//		BetaStrand betaStrandJ, betaStrandK;
//		int aj, bj, ak, bk;
//		for (int i1 = 0; i1 < betaSheets.getSize(); i1++) {
//			betaSheet = (BetaSheet)betaSheets.get(i1);
//			for (int j = 0; j < betaSheet.getSize(); j++) {
//				betaStrandJ = (BetaStrand)betaSheet.getBetaStrand(j);
//				aj = protein.getIndex(betaStrandJ.getInitSeqNum());
//				bj = protein.getIndex(betaStrandJ.getEndSeqNum());
//				for (int k = j; k < betaSheet.getSize(); k++) {
//					betaStrandK = (BetaStrand)betaSheet.getBetaStrand(k);
//					ak = protein.getIndex(betaStrandK.getInitSeqNum());
//					bk = protein.getIndex(betaStrandK.getEndSeqNum());
//					for (int mj = 3*(aj-1); mj < 3*bj-1; mj++) {
//						p = points.get(mj);
//						for (int mk = 3*(ak-1); mk < 3*bk-1; mk++) sqm[mj][mk] = sqm[mk][mj] = p.getDistance(points.get(mk));
//					}
//				}
//			}
//		}
//		return sqm;
//	}
//	
//	/*
//	 * returns matrix with squared distances between all pairs of atoms.
//	 */
//	public double[][] getSquaredDistanceMatrix() {
//		Point3d p;
//		int n = points.getSize();
//		double[][] sqm = new double[n][n];
//		for (int i = 0; i < n; i++) {
//			sqm[i][i] = 0;
//			p = points.get(i);
//			for (int j = i+1; j < n; j++) sqm[i][j] = sqm[j][i] = p.getSquaredDistance(points.get(j));
//		}
//		return sqm;
//	}
//	
//	public double getPairwiseSubDistanceRMSD(int size) {
//		double sum1 = 0;
//		int count = 0;
//		double dNC1;
//		int step, k;
//		for (int i = 0; i < size-1; i++) {
//			step = 3;
//			k = i + 1;
//			while ((k < size-1) && nodes[k].isLocked) k++;
//			step = step + k - i - 1;
//			for (int j = i+step ; j < size; j++) {
//				if (distances[i][j] > 0) {
//					count++;
//					dNC1 = distances[i][j] - getPosition(i,j).getDistance();
//					sum1 += dNC1*dNC1;
//				}
//			}
//		}
//		if (count == 0) return 0.0;
//		return Math.sqrt(2*sum1/count);
//	}
//
//	
//	public double getPairwiseDistanceRMSD(int size) {
//		double sum = 0;
//		double dNC;
//		int step, k;
//		for (int i = 0; i < size-1; i++) {
//			step = 3;
//			k = i + 1;
//			while ((k < size-1) && nodes[k].isLocked) k++;
//			step = step + k - i - 1;
//			for (int j = i+step ; j < size; j++) {
//				dNC = distances[i][j] - getPosition(i,j).getDistance();
//				sum += dNC*dNC;
//			}
//		}
//		return Math.sqrt(2*sum/(size*size - size));
//	}
//	
//	public double getPairwiseDistanceRMSD() { return getPairwiseDistanceRMSD(points.getSize()); }
//	
//	/*
//	 * returns the contribution to pairwise distance RMSD by points separated by the k-th bond.
//	 */
//	public double getPairwiseSquaredDistancsSum(int k) {
//		double sum = 0;
//		int size = points.getSize();
//		double dNC;
//		for (int i = 0; i < k; i++) 
//			for (int j = k+1 ; j < size; j++) {
//				dNC = distances[i][j] - getPosition(i,j).getDistance();
//				sum += dNC*dNC;
//			}
//		return sum;
//	}
//
//	public double getAngleDiff(double a, double b) {
//		double aa = a;
//		double bb = b;
//		while (aa > Constants.pi) aa -= PIPI;
//		while (aa <= -Constants.pi) aa += PIPI;
//		while (bb > Constants.pi) bb -= PIPI;
//		while (bb <= -Constants.pi) bb += PIPI;
//		if (aa*bb >= 0.0) return Math.abs(aa-bb);
//		else {
//			double sum = Math.abs(aa) + Math.abs(bb);
//			if (sum <= Constants.pi) return sum; else return PIPI - sum;
//		}
//	}
//	
//	/*
//	 * returns the sum of the volumes of all capsules
//	 */
//	public double getTotalCapsulesVolume() {
//		double volume = 0.0;
//		Stack stack = new Stack();
//		CTNode nd;
//		stack.push(root);
//		while (!stack.isEmpty()) {
//			nd = (CTNode)stack.pop();
//			volume += nd.volume.volume();
//			if (nd.right != null) stack.push(nd.right);
//			if (nd.left  != null) stack.push(nd.left);
//		}
//		return volume;
//	}
//	
//	
//	/*
//	 * computes two cylinders, one with the axis through the extreme leaves and the other through
//	 * the midpoint of the segment between extreme leaves and the middle point. 
//	 */
//	public void getCylinderAxis(CTNode nd) {
//		Point3d p = new Point3d(0, 0, 0);
//		Point3d q = getPosition(nd.low, nd.high);
//		Vector3d dir = new Vector3d(q);
//		Line3d line = new Line3d(dir);
//		double tMin = 0, tMax = 1, t; 
//		Point3d pMin = p, pMax = q;
//		double maxSquaredDist = 0, dist;
//		for (int k = nd.low+1; k < nd.high-1; k++) {
//			Point3d r = getPosition(nd.low, k);
//			t = Vector3d.dotProduct(line.getDir(), new Vector3d(r, p));
//			if (t < tMin) { tMin = t; pMin = r; } 
//			else { 
//				if (t > tMax) { tMax = t; pMax = r; } 
//			}
//			dist = line.getSquaredDistance(r);
//			if (dist > maxSquaredDist) maxSquaredDist = dist;
//		}
//		Cylinder3d cylinder1 = new Cylinder3d(Math.sqrt(maxSquaredDist), pMin, new Segment3d(pMin, pMax));
//
//		p = new Point3d(q.getX()/2, q.getY()/2, q.getZ()/2);
//		int m = (nd.high - nd.low + 1)/2;
//		q = getPosition(nd.low, m);
//		dir = new Vector3d(p,q);
//		line = new Line3d(p,dir);
//		tMin = 0; tMax = 1;
//		pMin = p; pMax = q;
//		maxSquaredDist = q.getSquaredDistance(p);
//		for (int k = nd.low+1; k < nd.high-1; k++) {
//			if (k != m)  {
//				Point3d r = getPosition(nd.low, k);
//				t = Vector3d.dotProduct(line.getDir(), new Vector3d(r, p));
//				if (t < tMin) { tMin = t; pMin = r; } 
//				else { 
//					if (t > tMax) { tMax = t; pMax = r; } 
//				}
//				dist = line.getSquaredDistance(r);
//				if (dist > maxSquaredDist) maxSquaredDist = dist;
//			}
//		}
//		Cylinder3d cylinder2 = new Cylinder3d(Math.sqrt(maxSquaredDist), pMin, new Segment3d(pMin, pMax));
//	}
//
//	/*
//	 * when the dihedral  angle of i-th bond is changed by the angle a,
//	 * the rotation matrix associated with this bond as well as all rotation 
//	 * matrices involving it are updated.
//	 */
//	public void changeRotationAngle(int i, double a) {
//		CTNode nd = nodes[i];
//		RotationMatrix4x4 matr = nodes[i].matr;
//		nd.a -= a;
//		double s = nd.s = Math.sin(nd.a);
//		double c = nd.c = Math.cos(nd.a);
//		double d = nd.d = 1 - nd.c;
//		double wxyd = nd.wxy*d, wxzd = nd.wxz*d, wyzd = nd.wyz*d;
//		double wxs = nd.wx*s, wys = nd.wy*s, wzs = nd.wz*s;
//		matr.set11(nd.wxx*d + c);
//		matr.set12(wxyd + wzs);
//		matr.set13(wxzd - wys);
//		matr.set21(wxyd - wzs);
//		matr.set22(nd.wyy*d + c);
//		matr.set23(wyzd + wxs);
//		matr.set31(wxzd + wys);
//		matr.set32(wyzd - wxs);
//		matr.set33(nd.wzz*d + c);
//
//		double a14 = matr.get11()*matr.get14() + matr.get12()*matr.get24() + matr.get13()*matr.get34();
//		double a24 = matr.get21()*matr.get14() + matr.get22()*matr.get24() + matr.get23()*matr.get34();
//		matr.set34(matr.get31()*matr.get14() + matr.get32()*matr.get24() + matr.get33()*matr.get34());
//		matr.set14(a14);
//		matr.set24(a24);
//		nodes[i].matr = matr;
//
//		// rotation matrices between nodes[i] and the root are updated
//		while ((nd = nd.parent) != null) {
//			logBook.push(new RotationMatrix4x4(nd.matr)); 
//			logBook.push(nd.volume);
//			logBook.push(nd);
//			nd.matr = new RotationMatrix4x4(nd.left.matr);
//			nd.matr.multR(nd.right.matr);
//		}
//		
//		// bounding volumes between nodes[i]Êand the root are updated
//		nd = nodes[i];
//		while ((nd.parent != null) && (nd == nd.parent.right)) nd = nd.parent;
//		while ((nd = nd.parent) != null) updateBoundingVolume(nd);	
////		System.out.println(i + " updated");
//	}
//
//	/*
//	 * generates an array of identical dihedral angles
//	 */
//	public void setFixedDihedralAngles(double dihedralAngle) {
//		for (int i = 1; i < nodes.length-1; i++) 	{
//			if (!nodes[i].isLocked) {
//				changeRotationAngle(i, dihedralAngle - getDihedralAngle(i));
//				logBook.clear();
//			}
//		}
//	}
//
//	/*
//	 * generates an array of random dihedral angles
//	 */
//	public void setRandomDihedralAngles() {
//		Random rand = new Random();
//		for (int i = 1; i < nodes.length-1; i++) 	{
//			if (!nodes[i].isLocked) {
//				changeRotationAngle(i, Constants.pi*(2.0*rand.nextDouble() - 1) - getDihedralAngle(i));
//				logBook.clear();
//			}
//		}
//	}
//
//	/*
//	 * generates random dihedral angles associated with the leaves
//	 */
//	public void randomDihedralAngles() {
//		Random rand = new Random();
//		for (int i = 0; i < nodes.length; i++) 	{
//			if (!nodes[i].isLocked) {
//				changeRotationAngle(i, 2.0*rand.nextDouble()*Constants.pi);
//				logBook.clear();
//			}
//		}
//	}
//
//	/*
//	 * returns true if a rotation around the i-th bond resulted in a clashing structure
//	 */
//	public boolean isClashing(int i) {
//		CTNode nd = nodes[i], ndP;
//		Set<CTNode> leftSack  = new Set<CTNode>(nd);
//		Set<CTNode> rightSack = new Set<CTNode>(nd);
//		while (nd != root) {
//			//			System.out.println("Clash check at node [" + nd.low + "," + nd.high + "]" );
//			ndP = nd.parent;
//			if (ndP.left == nd) {
//				if (isClashing(leftSack, ndP.right)) return true;
//				rightSack.insert(ndP.right);
//			}
//			else {
//				if (isClashing(ndP.left, rightSack)) return true;
//				leftSack.insert(ndP.left);
//			}
//			nd = ndP;
//		}
//		return false;
//	}
//
//	private boolean isClashing(Set<CTNode> leftSack, CTNode ndR) {
//		CTNode ndL;
//		for (int i = 0; i < leftSack.getSize(); i++) {
//			ndL = (CTNode)leftSack.get(i);		
//			if (areClashing(ndL, ndR)) return true;
//		}
//		return false;
//	}
//
//	private boolean isClashing(CTNode ndL, Set<CTNode> rightSack) {
//		CTNode ndR;
//		for (int i = 0; i < rightSack.getSize(); i++) {
//			ndR = (CTNode)rightSack.get(i);			
//			if (areClashing(ndL, ndR)) return true;
//		}
//		return false;
//	}
//
//	/*
//	 * return energy contribution across the specified bond
//	 */
//	public double getCrossEnergy(int i) {
//		//		System.out.println("energy across bond " + i);
//		CTNode nd = nodes[i];
//		Set leftSack = new Set();
//		Set rightSack = new Set();
//		leftSack.insert(nd);
//		rightSack.insert(nd);
//		double energy = getCrossEnergy(nd,nd);
//		while (nd != root) {
//			if (nd.parent.right == nd) {
//				leftSack.insert(nd.parent.left);
//				energy = energy + getCrossEnergy(nd.parent.left, rightSack);
//			}
//			else {
//				rightSack.insert(nd.parent.right);
//				energy = energy + getCrossEnergy(leftSack, nd.parent.right);
//			}
//			nd = nd.parent;
//		}
//		return energy;
//	}
//
//	private double getCrossEnergy(Set leftSack, CTNode nd) {
//		if (nd.low %3 == 1) {
//			//			System.out.println("energy across leftSack and [" + nd.low + "," + nd.high + "]");
//			double energy = 0;
//			for (int i = 0; i < leftSack.getSize(); i++) energy += getCrossEnergy((CTNode)leftSack.get(i), nd);
//			return energy;
//		}
//		else return 0;
//	}
//
//	private double getCrossEnergy(CTNode nd, Set rightSack) {
//		if (nd.low % 3 == 1) {
//			//			System.out.println("energy across [" + nd.low + "," + nd.high + "] and rightSack");
//			double energy = 0;
//			for (int i = 0; i < rightSack.getSize(); i++) energy += getCrossEnergy(nd, (CTNode)rightSack.get(i));
//			return energy;
//		}
//		else return 0;
//	}
//
//	private double getCrossEnergy(CTNode left, CTNode right) {
//		//		System.out.println("energy across [" + left.low + "," + left.high + "] and [" + right.low + "," + right.high + "]");
//		if (left.isLeaf()) {
//			if (left.low % 3 != 1) return 0;
//			else {
//				if (right.isLeaf()) {
//					if (right.high % 3 != 1) return 0;
//					else {
//						Point3d p = getPosition(left.low, right.high + 1);
//						if (p.getSquaredDistance() <= squaredContactRadius) return 1; else return 0;
//					}
//				}
//				if (insideBoundingVolume(right,left)) return getCrossEnergy(left, right.left) + getCrossEnergy(left, right.right); else return 0;
//			}
//		}
//		if (right.isLeaf()) {
//			if (right.high %3 != 1) return 0;
//			else {
//				if (insideBoundingVolume(left, right)) return getCrossEnergy(left.left, right) + getCrossEnergy(left.right, right); else return 0;
//			}
//		}
//		if (intersectingBoundingVolumes(left, right)) return getCrossEnergy(left.left, right) + getCrossEnergy(left.right, right); else return 0;
//	}
//
//
//	/*
//	 * returns contact number of a specified Ca-atom (note that i modulo 3 must be 1)
//	 */
//	public int getContactNumber(int i) { return getContactNumber(i, nodes.length+1); }
//	
//	/* 
//	 * returns contact number of a specified Ca-atom among the first bound atoms
//	 */
//	public int getContactNumber(int i, int bound) {
//		if (i %3 != 1) return -1;  // not a Ca-atom
//		int contactNumber = 0;
//		CTNode nd = null; 
//		nd = nodes[i-1];
//		while (nd != root) {
//			if (nd.parent.right == nd) contactNumber += getContactNumber(nd.parent.left, i, bound);	
//			else contactNumber += getContactNumber(i,nd.parent.right, bound);
//			nd = nd.parent;
//		}
//		return contactNumber;
//	}
//
//	/*
//	 * returns contact number of a specified Ca-atom with its prefix subchain 
//	 */
//	private int getContactNumber(CTNode nd, int i, int bound) {
//		if (nd.isLeaf()) {
//			if (nd.low % 3 != 1) return 0;
//			return  (getPosition(nd.low, i).getSquaredDistance() <= squaredContactRadius)? 1 : 0;
//		}
//		return getContactNumber(nd.left, i, bound) + getContactNumber(nd.right, i, bound);
//	}
//
//	/*
//	 * returns contact number of a specified Ca-atom with the subchain between i and bound.
//	 */
//	private int getContactNumber(int i, CTNode nd, int bound) {
//		if (bound < nd.low) return 0;
//		if (nd.isLeaf()) {
//			if (nd.high % 3 != 1) return 0;
//			return (getPosition(i, nd.high).getSquaredDistance() <= squaredContactRadius)? 1 : 0;
//		}
//		return getContactNumber(i, nd.left, bound) + getContactNumber(i, nd.right, bound);
//	}
//
//	public void getContactVector(int[] CNVector, int activeBound) {
//		for (int i = 1; i < activeBound; i = i+3) CNVector[(i-1)/3] = getContactNumber(i, activeBound);
//	}
//	
//	public void updateContactVector(int[] CNVector, int activeBound) {
//		int count = 0;
//		Point3d p = getPosition(activeBound-1);
//		for (int i = 1; i < activeBound-3; i = i+3) {
//			if (p.getSquaredDistance(getPosition(i)) <= squaredContactRadius) {
//				CNVector[(i-1)/3]++;
//				count++;
//			}
//		}
//		CNVector[(activeBound-1)/3] = count;
//	}
//
//	public boolean insideBoundingVolume(CTNode subtree, CTNode leaf) { return true; }
//
//	public boolean intersectingBoundingVolumes(CTNode leftSubtree, CTNode righSubtree) { return true; }
//
//	public void showRotationMatrices() { root.showRotationMatrices(); }
//
//
//	/*
//	 * chain tree with nodes of height at least k are written out
//	 */
//	public void toConsole(int k) {
//		CTNode nd;
//		Stack stack = new Stack();
//		stack.push(root);
//		while (!stack.isEmpty()) {
//			nd = (CTNode)stack.pop();
//			nd.toConsole();
//			if (nd.isInternal() && (nd.height > k)) {
//				stack.push(nd.right);
//				stack.push(nd.left);
//			}			
//		}
//	}
//
//	public void toConsole(int i, int j, int k) {
//		CTNode nd;
//		Stack stack = new Stack();
//		stack.push(root);
//		while (!stack.isEmpty()) {
//			nd = (CTNode)stack.pop();
//			if (((i <= nd.high) && (nd.high < j)) || ((i < nd.low) && (nd.low <= j))) nd.toConsole();
//			if ((nd.right.height >= k) && (nd.right.low <= j)) stack.push(nd.right);
//			if ((nd.left.height >= k) && (i <= nd.left.high)) stack.push(nd.left);
//		}
//	}
//
//	public boolean areClashing(CTNode ndL, CTNode ndR) {
//		//		System.out.println("left: " + ndL.toString() + " right: " + ndR.toString());
//		if (ndL.low + 3 < ndR.high) {
//			Capsule3d rotatedCapsule = new Capsule3d(getPosition(ndL.low, ndR.low, ndR.capsule.segment.getA()), 
//					getPosition(ndL.low, ndR.low, ndR.capsule.segment.getB()), ndR.capsule.rad);
//			if (ndL.capsule.overlaps(rotatedCapsule)) {
//				if (ndL.isLeaf()) {
//					if (ndR.isLeaf()) {
//						Point3d p1 = new Point3d(0,0,0);
//						Point3d p2 = getPosition(ndL.low, ndL.high+1);
//						Point3d p3 = getPosition(ndL.low, ndR.low);
//						Point3d p4 = getPosition(ndL.low, ndR.high+1);
//						return 	p1.getSquaredDistance(p3)<=squaredClashDistance ||
//						p1.getSquaredDistance(p4)<=squaredClashDistance ||
//						p2.getSquaredDistance(p3)<=squaredClashDistance ||
//						p2.getSquaredDistance(p4)<=squaredClashDistance;
//
//						/*return 	points.get(ndL.low).getSquaredDistance(points.get(ndR.high+1)) <= squaredClashDistance || 
//								points.get(ndL.low).getSquaredDistance(points.get(ndR.low)) <= squaredClashDistance ||
//								points.get(ndL.high+1).getSquaredDistance(points.get(ndR.high+1)) <= squaredClashDistance ||
//								points.get(ndL.high+1).getSquaredDistance(points.get(ndR.low)) <= squaredClashDistance;
//						 */
//					}
//					else return areClashing(ndL, ndR.left) || areClashing(ndL, ndR.right);
//				}
//				else {
//					if (ndR.isLeaf()) return areClashing(ndL.left, ndR) || areClashing(ndL.right, ndR);
//					else {
//						if (ndL.height > ndR.height) return areClashing(ndL.left,ndR) || areClashing(ndL.right, ndR);
//						else return areClashing(ndL, ndR.left) || areClashing(ndL, ndR.right);
//					}
//				}
//			}
//			else {
//				disjointCapsules[ndL.height][ndR.height]++;
//				return false;
//			}
//		}
//		else return false;
//	}
//
//	/*	public boolean areClashing(CTNode ndL, CTNode ndR) {
//		if (ndL.isLeaf()) {
//			if (ndR.isLeaf()) {
//
//			}
//			else {
//				Cylinder3d cylinderR = getPosition(ndR.cylinder);
//				Segment3d sgmR = new Segment3d(cylinderR.getAnchor(), cylinderR.getDir());
//
//			}
//		}
//		else {
//			Cylinder3d cylinderL = getPosition(ndL.cylinder);
//			Segment3d sgmL = new Segment3d(cylinderL.getAnchor(), cylinderL.getDir());
//			if (ndR.isLeaf()) {
//
//			}
//			else {
//				Cylinder3d cylinderR = getPosition(ndR.cylinder);
//				Segment3d sgmR = new Segment3d(cylinderR.getAnchor(), cylinderR.getDir());
//				if (sgmL.getSquaredDistance(sgmR) > squaredClashDist) return false;
//			}
//		}
//	}
//	 */	
//
//	/*
//	 * creates bounding volumes of the sutree rooted at node nd
//	 */
//	public void createBoundingVolume(CTNode nd) {
//		if (!nd.isLeaf()) {
//			createBoundingVolume(nd.left);
//			createBoundingVolume(nd.right);
//			updateBoundingVolume(nd);
//		}
//		else nd.capsule = new Capsule3d(new Point3d(0, 0, 0), new Point3d(nd.getPQ()), extension); 
//	}
//
//	/*
//	 * recomputes bounding volume of node nd
//	 */
//	public void updateBoundingVolume(CTNode nd) {
//		if (boundingMode == 0) {
//			PointSet3d pointSubset = new PointSet3d();		
//			pointSubset.insert(nd.left.capsule.segment.getA());
//			pointSubset.insert(nd.left.capsule.segment.getB());
//			pointSubset.insert(getPosition(nd.low, nd.right.low, nd.right.capsule.segment.getA()));
//			pointSubset.insert(getPosition(nd.low, nd.right.low, nd.right.capsule.segment.getB()));
//			nd.capsule = Capsule3d.createBoundingCapsule_CovarianceFit(pointSubset);	
//			if (nd.left.capsule.rad < nd.right.capsule.rad) nd.capsule.rad += nd.right.capsule.rad; else nd.capsule.rad += nd.left.capsule.rad;
//		}
//		else {
//			nd.capsule = Capsule3d.createBoundingCapsule_CovarianceFit(getPoints(nd));
//			nd.capsule.rad += extension;
//		}
//	}
//
//
//	public void clashTest() {
//		//		int i1;
//		//		Random rand = new Random();
//
//		//		while (true) {		
//		//			i1 = rand.nextInt(nodes.length);
//		//			while (nodes[i1].isLocked) i1 = rand.nextInt(nodes.length);
//		if (areClashing(root.left, root.right)) System.out.println("clash detected");
//		else System.out.println("no clash detected");
//		//		}
//	}
//
//	
//	public void restore() {
//		CTNode nd;
//		while (!logBook.isEmpty()) {
//			nd = (CTNode)logBook.pop();
//			if (!nd.isLeaf()) {
//				nd.capsule = (Capsule3d)logBook.pop();
//				nd.matr = (RotationMatrix4x4)logBook.pop();
//			}
//			else {
//				nd.matr = (RotationMatrix4x4)logBook.pop();
//				nd.d = (Double)logBook.pop();
//				nd.c = (Double)logBook.pop();
//				nd.s = (Double)logBook.pop();
//				nd.a = (Double)logBook.pop();
//			}
//		}
//	}
//
//	/*
//	 * keeps rotating forever around one bond at a time
//	 */
//	public void rotate(ChainTreeTest nativeCTree) {
//		distances = nativeCTree.getDistanceSubmatrix();
//		double bestRMSD = 0.0;  // = getPairwiseSubDistanceRMSD(points.getSize()-1);
//		double rmsd, dihRMSD = 0.0;
//		double treshold = 0.05;
//		double sum2, bestSum2 = 0; 
//		double dNC2;
//		int count2;
//		CTNode nd;
//		Random rand = new Random();
//		int n, count = 0, diff;
//		int size = 3;                            // maximum number of bonds that can be rotated in each iteration
//		boolean clashDetected;
//		int[] bond = new int[size];
//		activeBound = 11;  //nodes.length;       // last bond that can be rotated
//		int nrIter = activeBound*1000;
//		int optCounter = 0, nrOptIter = 2000;
//		boolean optRSMD = false;
//		while (nodes[activeBound].isLocked) activeBound++;
//		getContactVector(contactVector, activeBound);
//		nativeCTree.getContactVector(nativeContactVector, activeBound);
//		repaint(activeBound);
//		double angle;
//		while (true) {
//			dihRMSD = getDihedralAnglesRMSD(activeBound-1);
//			activeBound += 3;                                                     // chain is extended
//			if (activeBound > nodes.length) activeBound = nodes.length;
//			System.out.println("Chain extended to " + activeBound + " aminoacids");
//			sum2 = 0; count2 = 0;
//			for (int i = 1; i < activeBound; i = i+3) {
//				count2++;
//				contactVector[(i-1)/3] = getContactNumber(i, activeBound);					
//				nativeContactVector[(i-1)/3] = nativeCTree.getContactNumber(i, activeBound);
//				diff = nativeContactVector[(i-1)/3] - contactVector[(i-1)/3];
//				sum2 += diff*diff;
//				System.out.print((contactVector[(i-1)/3]-nativeContactVector[(i-1)/3]) + ",");
//			}
//			System.out.println();
//			bestRMSD = getPairwiseSubDistanceRMSD(activeBound);
//			bestSum2 = Math.sqrt(sum2/count2);
//			System.out.println("RMSD is " + bestRMSD + ", contact RMSD is " + bestSum2);
//			nrIter += 20000;
//
//			while (count < nrIter) {
//				n = rand.nextInt(size) + 1;
//				clashDetected = false;
//				for (int i = 0; i < n; i++) {
//					bond[i] = rand.nextInt(activeBound); while (nodes[bond[i]].isLocked) bond[i] = rand.nextInt(activeBound);
//					nd = nodes[bond[i]];
//					logBook.push(nd.a); logBook.push(nd.s); logBook.push(nd.c); logBook.push(nd.d);
//					logBook.push(new RotationMatrix4x4(nd.matr)); logBook.push(nd);
//					angle = Constants.pi*(2*rand.nextDouble()-1)/2;				
//					changeRotationAngle(bond[i], angle);
//					// detection of clashes is done after each dihedral rotation - clashDetectionMode = 0
//					if ((clashDetectionMode == 0) && isClashing(bond[i])) { 
//						restore();
//						clashDetected = true;
//						i = n;
//					}
//				}
//				// detection of clashes is done after all n dihedral rotation - clashDetectionMode = 1
//				if (clashDetectionMode == 1) {
//					for (int i = n - 1; i >= 0; i--) {
//						if (isClashing(bond[i])) {
//							restore();
//							clashDetected = true;
//							i = n;
//						}
//					}
//				}
//				if (!clashDetected) {
//					rmsd = getPairwiseSubDistanceRMSD(activeBound);		
//					sum2 = 0; count2 = 0;
//					for (int i = 0; i < (activeBound-1)/3; i++) {
//						count2++;
//						dNC2 = getContactNumber(3*i+1, activeBound)-nativeContactVector[i];
//						sum2 += dNC2*dNC2;
//					}
//					sum2 = Math.sqrt(sum2/count2);
//
//					if (sum2 < bestSum2) {
//						bestSum2 = sum2;
//						bestRMSD = rmsd;
//						double cRMSD = RMSD.getRMSD(this, activeBound);
//						System.out.println(count + ". dist rmsd = " + misc.Functions.toString(bestRMSD,4) + 
//								", CONT RMSD = " + misc.Functions.toString(bestSum2, 4) +
//								", n = " + n + ", 1. bond = " + bond[0] + " active bond = " + activeBound + 
//								" nr. iter. = " + nrIter + " dihRMSD = " + misc.Functions.toString(dihRMSD, 4) +
//								" superposRMSD = " + misc.Functions.toString(cRMSD, 4));
//						logBook.clear();
//						for (int i = 1; i < activeBound; i = i+3) {
//							contactVector[(i-1)/3] = getContactNumber(i, activeBound);					
//							System.out.print((contactVector[(i-1)/3]-nativeContactVector[(i-1)/3]) + ",");
//						}
//						System.out.println();
//						repaint(activeBound);
//						if (rmsd > 0) {
//							optRSMD = true;
//							optCounter = 0;
//						}
//					}
//					else {
//						if (optRSMD) {
//							if ((rmsd < bestRMSD) && (bestSum2 > 0.5)) { //&& (sum2 == bestSum2)) { //(Math.abs(sum2-bestSum2)/sum2 < treshold))  {
//								bestRMSD = rmsd;
//								bestSum2 = sum2;
//								double cRMSD = RMSD.getRMSD(this, activeBound);
//								System.out.println(count + ". DIST RMSD = " + misc.Functions.toString(bestRMSD,4) + 
//										", cont rmsd = " + misc.Functions.toString(bestSum2, 4) +
//										", n = " + n + ", 1. bond = " + bond[0] + " active bond = " + activeBound + 
//										" nr. iter. = " + nrIter + " dihRMSD = " + misc.Functions.toString(dihRMSD, 4) +
//										" superposRMSD = " + misc.Functions.toString(cRMSD, 4));
//								logBook.clear();
//								for (int i = 1; i < activeBound; i = i+3) {
//									contactVector[(i-1)/3] = getContactNumber(i, activeBound);					
////									System.out.print((contactVector[(i-1)/3]-nativeContactVector[(i-1)/3]) + ",");
//								}
////								System.out.println();
//
//								repaint(activeBound);
//							}
//							else restore();
//							optCounter++;
//							if (optCounter == nrOptIter) optRSMD = false;
//						}
//						else restore();
//					}
//				}
//				count++;
//			}
//		}
//	}
//
//	public void initPaint() {
//		initPaint(0, nodes.length);
//	}
//	
//	public void initPaint(int low, int high) {
//		j3dg.addShape(new Sphere3d(new Point3d(0, 0, 0), 0.2f), java.awt.Color.blue);
//	}
//
//
//	public void repaint(int activeBound) {
//		Point3d q;
//		Point3d p = new Point3d(0,0,0);
//		CTNode nd;
//		Color clr;
//		for (int i = 1; i <= activeBound; i++) {
//			if (i%3 == 0) clr = Color.blue;
//			else {
//				if (i%3 == 1) clr = Color.darkGray; else clr = Color.lightGray; 
//			}
//			q = getPosition(i);
//			nd = nodes[i-1];
//			if (nd.sphere == null) {
//				nd.sphere = new Sphere3d(q, 0.2f);
//				nd.cylinder = new Cylinder3d(p, q, 0.2f);
//				j3dg.addShape(nd.sphere, clr);
//				if (nd.isLocked) {
//					if ((i % 3 == 0) && !nodes[i-2].isLocked) j3dg.addShape(nd.cylinder,Color.pink);
//					else j3dg.addShape(nd.cylinder, Color.red); 
//				}
//				else j3dg.addShape(nd.cylinder);
//			}
//			else {
//				nd.sphere.center = q;
//				nd.cylinder.getSegment().setA(p);
//				nd.cylinder.getSegment().setB(q);
//			}
//			p = q;
//		}
//
//		
//		Stack stack = new Stack();
//		stack.push(root);
//		while (!stack.isEmpty()) {
//			nd = (CTNode)stack.pop();
//			if ((nd.height <= drawCapsuleLevelUpper) && (nd.height>=drawCapsuleLevelLower )) {
//				if (nd.high <= activeBound) {
//					if (nd.drawCapsule == null) {
//						nd.drawCapsule = new Capsule3d(getPosition(0, nd.low, nd.capsule.segment.getA()), 
//								                       getPosition(0, nd.low, nd.capsule.segment.getB()), 
//								                       nd.capsule.rad);
//						j3dg.addShape(nd.drawCapsule, colors[nd.height]);
//						// if a node has height > drawCapsuleLevelLower, capsules of both children are drawn no matter what their heights are.
//						if ((nd.left != null) && (nd.height > drawCapsuleLevelLower) && (nd.left.height < drawCapsuleLevelLower)) {
//							nd.left.drawCapsule = new Capsule3d(getPosition(0, nd.left.low, nd.left.capsule.segment.getA()), 
//							    								getPosition(0, nd.left.low, nd.left.capsule.segment.getB()), 
//								     							nd.left.capsule.rad);
//							j3dg.addShape(nd.left.drawCapsule, colors[nd.height]);
//						}
//						if ((nd.right != null) && (nd.height > drawCapsuleLevelLower) && (nd.right.height < drawCapsuleLevelLower)) {
//							nd.right.drawCapsule = new Capsule3d(getPosition(0, nd.right.low, nd.right.capsule.segment.getA()), 
//							     								 getPosition(0, nd.right.low, nd.right.capsule.segment.getB()), 
//								     		  					 nd.right.capsule.rad);
//							j3dg.addShape(nd.right.drawCapsule, colors[nd.height]);
//						}
//					}
//					else {
//						nd.drawCapsule.segment.setA( getPosition(nd.low, nd.capsule.segment.getA()) );
//						nd.drawCapsule.segment.setB( getPosition(nd.low, nd.capsule.segment.getB()) );
//						nd.drawCapsule.rad = nd.capsule.rad;
//					}
//				}	
//			}
//			if (!nd.isLeaf()) {
//				stack.push(nd.left);
//				stack.push(nd.right);
//			}
//		}
//		j3dg.repaint();
//	}
//
//	public static void main(String[] args) {
////		Protein protein = new Protein("1CTF", 2, true);
////		Protein protein = new Protein("1A0A", 2, true);
//		Protein protein = new Protein("1A23", 2, true);
////		Protein protein = new Protein("1A56", 2, true);
////		Protein protein = new Protein("1A63", 2, true);
//
////		Protein protein = new Protein("1A6X", 2, true);
////		Protein protein = new Protein("1CTJ", 2,true);
//		PointSet3d allPoints = protein.getPointSet();
//		PointSet3d points = new PointSet3d();
//		for (int i = 0; i < allPoints.getSize(); i++) points.insert(allPoints.get(i));
////		for (int i = 0; i < 40; i++) points.insert(allPoints.get(i));
//
//		ChainTreeTest nativeCTree = new ChainTreeTest(points, protein);
//		
//		ChainTreeTest cTree = new ChainTreeTest(points, protein);
//		for (int i = 2; i < cTree.nodes.length; i = i+3) cTree.nodes[i].isLocked = true;
//		cTree.nodes[cTree.nodes.length-1].isLocked = true;
//		cTree.nodes[0].isLocked = true;
////		cTree.lockAlphaHelices();
////		cTree.lockBetaStrands();
//		
//		
//		cTree.j3dg = J3DScene.createJ3DSceneInFrame();
//
//		cTree.createBoundingVolume(cTree.root);
//		System.out.println("Total volume of capsules is " +  cTree.getTotalCapsulesVolume());
//		// all dihedral angles, apart from locked bonds, are set to PI
//		cTree.setFixedDihedralAngles(Math.PI);
//		System.out.println("Total volume of capsules is " +  cTree.getTotalCapsulesVolume());
//
//		for (int i = 1; i < cTree.nodes.length+1; i = i + 3) {
//			cTree.contactVector[(i-1)/3] = cTree.getContactNumber(i);
//			cTree.nativeContactVector[(i-1)/3] = nativeCTree.getContactNumber(i);
//			System.out.print("contact number at " + i  + " is " + cTree.getContactNumber(i));
//			System.out.println(", native contact number is " + nativeCTree.getContactNumber(i));
//			
//		}
//
//
//		cTree.initPaint();
////		cTree.setupNativeViewer();
//		
//		cTree.rotate(nativeCTree);
//	
//		/*		ChainTree cTree1 = new ChainTree(points, protein, 0);
//		for (int i = 2; i < cTree1.nodes.length; i = i+3) cTree1.nodes[i].isLocked = true;
//		cTree1.nodes[cTree1.nodes.length-1].isLocked = true;
//		cTree1.nodes[0].isLocked = true;
////		cTree1.lockAlphaHelices();
////		cTree1.lockBetaStrands();
//		
//		J3DScene j3dg1 = J3DScene.createJ3DSceneInFrame();
//		System.out.println("got here");
//		
//		// all dihedral angles, apart from locked bonds, are set to PI
//		cTree1.setFixedDihedralAngles(Math.PI);
//		cTree1.initPaint(j3dg1);
//		cTree1.createBoundingVolume(cTree1.root, cTree1.extension, j3dg1);
//		cTree1.rotate(j3dg1);
//
//		
//		for (int i = 0; i < 20; i++) {
//			for (int j = 0; j < 20 ; j++) {
//				System.out.print((cTree.disjointCapsules[i][j] - cTree1.disjointCapsules[i][j]) + " ");
//			}
//			System.out.println();
//		}
//*/
//		//		for (int i = 1; i <= protein.getSize(); i++) System.out.print(cTree.getContactNumber(3*i-2) + " ");
//
////		for (int i = 0; i < 20; i++) System.out.println(i + ". " + cTree.clashStatistics[i]);
//		//		System.out.println(cTree.getCrossEnergy(55));
//
//
//		//		J3DScene j3dg = J3DScene.createJ3DSceneInFrame();
//		//		j3dg.startRotating();
//		//		cTree.initPaint(j3dg);
//		//		cTree.repaint(j3dg);
//
//		/*		
//		int i1, i2, i3, i4;
//		double angle1, angle2, angle3, angle4;
//		int energy;
//		int minEnergy = 999999999, maxEnergy = 0;
//		Random rand = new Random();
//		double crossEnergy1, crossEnergy2, crossEnergy3, crossEnergy4, newCrossEnergy1, newCrossEnergy2, newCrossEnergy3, newCrossEnergy4;
//
//		while (true) {		
//			i1 = rand.nextInt(n);
//			while (cTree.nodes[i1].isLocked) i1 = rand.nextInt(n);
////			i2 = rand.nextInt(n);
////			while ((cTree.nodes[i2].isLocked) || (i2 == i1)) i2 = rand.nextInt(n);
////			i3 = rand.nextInt(n);
////			while ((cTree.nodes[i3].isLocked) || (i3 == i1) || (i3 == i2)) i3 = rand.nextInt(n);
////			i4 = rand.nextInt(n);
////			while ((cTree.nodes[i4].isLocked) || (i4 == i1) || (i4 == i2) || (i4 == i3)) i4 = rand.nextInt(n);
////			crossEnergy1 = cTree.getCrossEnergy(i1);
//			angle1 = rand.nextDouble()/50 + 0.01;
//			cTree.changeRotationAngle(i1, angle1);
////			newCrossEnergy1 = cTree.getCrossEnergy(i1);
////			crossEnergy2 = cTree.getCrossEnergy(i2);
////			angle2 = rand.nextDouble()/50 + 0.01;
////			cTree.changeRotationAngle(i2, angle2);
////			newCrossEnergy2 = cTree.getCrossEnergy(i2);
////			crossEnergy3 = cTree.getCrossEnergy(i3);
////			angle3 = rand.nextDouble()/50 + 0.01;
////			cTree.changeRotationAngle(i3, angle3);
////			newCrossEnergy3 = cTree.getCrossEnergy(i3);				
////			crossEnergy4 = cTree.getCrossEnergy(i4);
////			angle4 = rand.nextDouble()/50 + 0.01;
////			cTree.changeRotationAngle(i4, angle4);
////			newCrossEnergy4 = cTree.getCrossEnergy(i4);				
////			energy = 0;
//			for (int i = 1; i <= protein.getSize(); i++) energy = energy + cTree.getContactNumber(3*i-2);
//			if (energy > minEnergy) {
////			if (maxEnergy >= energy) {
////			if (crossEnergy1 + crossEnergy2 + crossEnergy3 <= newCrossEnergy1 + newCrossEnergy2 + newCrossEnergy3) {
////				cTree.changeRotationAngle(i4, -angle4);
////				cTree.changeRotationAngle(i3, -angle3);
////				cTree.changeRotationAngle(i2, -angle2);
//				cTree.changeRotationAngle(i1, -angle1);
//			}
//			else {
//				minEnergy = energy;
////				maxEnergy = energy;
////				System.out.println(i1 + "," + i2 + "," + i3 + ": energy down by " + (crossEnergy1+crossEnergy2+crossEnergy3 - newCrossEnergy1-newCrossEnergy2-newCrossEnergy3));
//				System.out.println("total energy is " + energy);
//				cTree.repaint(j3dg);
//				try { Thread.sleep(20); } catch (InterruptedException e) { e.printStackTrace(); }
//			}
//		}
//		 */
//	}

}
