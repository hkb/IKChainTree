package sorting;

import geom3d.*;

public class SortToolSimplexAlpha implements SortTool {
	public int compare(Object p1, Object p2) {
		if ((p1 instanceof Simplex) && (p2 instanceof Simplex)) {
			double alpha1 = ((Simplex)p1).getAlpha();
			double alpha2 = ((Simplex)p2).getAlpha();
			if (alpha1 < alpha2) return COMP_LESS;
			else {
				if (alpha1 > alpha2) return COMP_GRTR; else return COMP_EQUAL;
			}
		}
		else throw SortTool.err1;
	}

}
