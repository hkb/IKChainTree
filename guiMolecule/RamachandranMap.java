package guiMolecule;

public class RamachandranMap {
	int [][] map = new int[361][361];
	int max = 0;
	
	public int get(int i, int j) { return map[i][j]; }
	public int getMax() { return max; }	
	
	public void inc(double phi, double psi) {
		int intX = new Double(phi+180).intValue(); 
		int intY = new Double(psi+180).intValue();
		map[intX][intY] = map[intX][intY] + 1;
		if (map[intX][intY] > max) max++;
	}


}
