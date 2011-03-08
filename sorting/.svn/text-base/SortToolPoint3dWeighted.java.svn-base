package sorting;

import geom3d.*;

public class SortToolPoint3dWeighted implements SortTool {
	public int compare(Object p1, Object p2) {
		if ((p1 instanceof Point3dWeighted) && (p2 instanceof Point3dWeighted)) {
			double w1 = ((Point3dWeighted)p1).getWeight();
			double w2 = ((Point3dWeighted)p2).getWeight();
			if (w1 < w2) return COMP_LESS;
			if (w1 > w2) return COMP_GRTR; 
			return COMP_EQUAL; 
		}
		else throw SortTool.err1;
	}
}

