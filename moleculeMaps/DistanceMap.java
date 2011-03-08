package moleculeMaps;

import geom3d.*;
import dataStructures.DLNode;

public class DistanceMap {
	private double [][] dm;
	private int n;
	
	
	/*
	 * creates a distance map of the specified chain. dm[i][j] is the distance between
	 * i-th and j-th item in the chain.
	 */ 
	public DistanceMap(Chain3d chain) {
		n = chain.getSize();
		dm = new double[n][n];
		DLNode u = chain.getFirst();
		Point3d up;
		int i = 0;
		DLNode v;
		int j;
		while (u != null) {
			up = (Point3d)u.getObject();
			dm[i][i] = 0.0;
			v = u.getNext();
			j = i + 1;
			while (v != null) {
				dm[i][j] = up.getDistance((Point3d)v.getObject());
				v = v.getNext();
				j++;
			}
			u = u.getNext();
			i++;
		}
	}
	
	public int getSize() { return n; }
	public double[][] getDistanceMap() { return dm; }
	
	public double get(int i, int j) { return dm[i][j]; }
	
	public void set(int i, int j, double dist) { dm[i][j] = dm[j][i] = dist; }
	
	public int getContactNumber(int i, double r) {
		int count = 0;
		for (int j = 0; j < n; j++) if (get(i,j) < r) count++;
		return count;
	}
	
	/*
	 * distances between Ca atoms are translated into probabilities using the 
	 * function suggested by Gelly et al., Protein Peeling: an approach for 
	 * splitting a 3D protein structure into compact fragments, Bioinformatics 
	 * 22 (2006) 129-133.
	 */
	public double[][] getContactProbablityMatrix() {
		double d0 = 8; 
		double delta = 1.5;
		double[][] cpm = new double[n][n];
		for (int i = 0; i < n-1; i++) {
			cpm[i][i] = 1.0;
			for (int j = i+1; j < n; j++) {
				cpm[i][j] = cpm[j][i] = 1.0/(1.0 + Math.exp((get(i,j) - d0)/delta));
			}
		}
		return cpm;
	}
}
