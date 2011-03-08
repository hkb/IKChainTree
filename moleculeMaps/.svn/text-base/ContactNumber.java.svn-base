package moleculeMaps;

import geom3d.*;
import dataStructures.DLNode;

public class ContactNumber {
	private int [] cn;
	private int n;
	double r;

	/*
	 * creates an integer vector cn such that cn[i] is the number of items at most r away from i-th item of 
	 * the chain.
	 */ 
	public ContactNumber(Chain3d chain, double r) {
		n = chain.getSize();
		cn = new int[n];
		this.r = r;
		double rr = r * r;
		DLNode u = chain.getFirst();
		Point3d up;
		int i = 0;
		DLNode v;
		int j;
		while (u != null) {
			up = (Point3d)u.getObject();
			v = u.getNext();
			j = i + 1;
			while (v != null) {
				if (up.getSquaredDistance((Point3d)v.getObject()) <= rr) {
					cn[i] = cn[i]+1;
					cn[j] = cn[j]+1;
				}
				v = v.getNext();
				j++;
			}
			u = u.getNext();
			i++;
		}
	}
	
	public int getSize() { return n; }
	
	public int get(int i) { return cn[i]; }
	
	public void set(int i, int k) { cn[i] = k; }
	
	public void display () {
		ContactNumberFrame cnFrame = new ContactNumberFrame(this);
	}
	
	
}
