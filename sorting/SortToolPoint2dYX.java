package sorting;

import geom2d.*;

public class SortToolPoint2dYX implements SortTool {
	public int compare(Object p1, Object p2) {
		if ((p1 instanceof Point2d) && (p2 instanceof Point2d)) {
			double y1 = ((Point2d)p1).getY();
			double y2 = ((Point2d)p2).getY();
			if (y1 < y2) return COMP_LESS;
			else { 
				if (y1 > y2) return COMP_GRTR; 
				else {
					double x1 = ((Point2d)p1).getX();
					double x2 = ((Point2d)p2).getX();
					if (x1 < x2) return COMP_LESS;
					else { 
						if (x1 > x2) return COMP_GRTR; else return COMP_EQUAL; 
					}
				}
			}
		}
		else throw SortTool.err1;
	}
}

