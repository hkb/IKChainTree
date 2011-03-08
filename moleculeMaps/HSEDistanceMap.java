package moleculeMaps;

import geom3d.Chain3d;
import geom3d.Point3d;
import geom3d.Vector3d;
import dataStructures.DLNode;

public class HSEDistanceMap {
	private int [][] dm;
	private int n;
		
	/*
	 * creates a distance map of the specified chain. dm[i][j] contains 
	 * distance between i-th and j-th item in the chain, rounded to the 
	 * closest integer.
	 */ 
	public HSEDistanceMap(Chain3d chain) {
		n = chain.getSize();
		dm = new int[n][n];
			
		DLNode prev = chain.getFirst();
		DLNode curr = (prev != null)? prev.getNext() : null;
		DLNode next = (curr != null)? curr.getNext() : null;
		DLNode nd;
		Point3d p = (prev != null)? (Point3d)prev.getObject() : null;
		Point3d q = (curr != null)? (Point3d)curr.getObject() : null;
		Point3d r = (next != null)? (Point3d)next.getObject() : null;
		Point3d z;
		Vector3d nv;
		int i = 1;
		int j;
		int dist;
	
		while (next != null) {
			nv = Vector3d.createSum(new Vector3d(q,p), new Vector3d(q,r));
			
			nd = next;
			j = 1;
			while (nd != null) {
				if (i == j) dm[i][i] = 0;
				else {
					z = (Point3d)nd.getObject();
					dm[i][j] = (int)Math.round(q.getDistance(z));
					if (!Point3d.isBehind(z, q, nv)) dm[i][j] = -dm[i][j];
				}
				nd = nd.getNext(); 
				j++;
			}
			prev = curr; p = q;
			curr = next; q = r;
			next = next.getNext(); 
			r = (next != null)? (Point3d)next.getObject() : null;
			i++;
		}
	}
		

	public int getSize() { return n; }
	public int[][] getHSEDistanceMap() { return dm; }
		
	public int get(int i, int j) { return dm[i][j]; }
		
	public void set(int i, int j, int dist) { dm[i][j] = dist; }
		
	public int getHSEUpNumber(int i, int r) {
		int countUp = 0;
		int inc;
		for (int j = 1; j < n-1; j++) {
			inc = get(i,j);
			if ((0 < inc) && (inc < r)) countUp++;
		}
		return countUp;
	}
			
	public int getHSEDownNumber(int i, int r) {
		int countDown = 0;
		int inc;
		for (int j = 1; j < n-1; j++) {
			inc = get(i,j);
			if ((inc < 0) && (-inc < r)) countDown++;
		}
		return countDown;
	}
}
