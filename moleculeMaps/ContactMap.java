package moleculeMaps;

import geom3d.*;
import dataStructures.DLNode;

// Given a chain and radius r, get(i,j) returns true if i-th and j-th point in 
// the chain are at most r apart.

public class ContactMap {
	private boolean [][] cm;
	private int n;
	
	/*
	 * creates a contact map for a given chain and a given radius
	 */
	public ContactMap(Chain3d chain, double r) {
		n = chain.getSize();
		cm = new boolean[n][n];
		double rr = r * r;
		DLNode u = chain.getFirst();
		Point3d up;
		int i = 0;
		DLNode v;
		int j;
		while (u != null) {
			up = (Point3d)u.getObject();
			cm[i][i] = true;
			v = u.getNext();
			j = i + 1;
			while (v != null) {
				cm[i][j++] = up.getSquaredDistance((Point3d)v.getObject()) <= rr;
				v = v.getNext();
			}
			u = u.getNext();
			i++;
		}
	}
	
	public int getSize() { return n; }
	
	public boolean get(int i, int j) { return cm[i][j]; }
	
	public void set(int i, int j, boolean bool) { cm[i][j] = cm[j][i] = bool; }
	
	public void display() {
		ContactMapFrame cmFrame = new ContactMapFrame(this);
	}
	
	public void print() {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i == j) System.out.print('o');
				else {
					if (get(i,j)) System.out.print('#'); else System.out.print(' ');
				}
			}
			System.out.println();
		}
	}
}
