package sorting;

import geom3d.*;

public class SortToolSegment3d implements SortTool {
	public int compare(Object p1, Object p2) {
		if ((p1 instanceof Segment3d) && (p2 instanceof Segment3d)) {
			double w1 = ((Segment3d)p1).getSquaredLength();
			double w2 = ((Segment3d)p2).getSquaredLength();
			if (w1 < w2) return COMP_LESS;
			if (w1 > w2) return COMP_GRTR; 
			return COMP_EQUAL; 
		}
		else throw SortTool.err1;
	}
}

