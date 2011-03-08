package sorting;

import geom3d.Point3d;

public class SortToolPoint3dXYZ implements SortTool {
	public int compare(Object p1, Object p2) {
		if ((p1 instanceof Point3d) && (p2 instanceof Point3d)) {
			double a1 = ((Point3d)p1).getX();
			double a2 = ((Point3d)p2).getX();
			if (a1 < a2) return COMP_LESS;
			else { 
				if (a1 > a2) return COMP_GRTR; 
				else {
					a1 = ((Point3d)p1).getY();
					a2 = ((Point3d)p2).getY();
					if (a1 < a2) return COMP_LESS;
					else { 
						if (a1 > a2) return COMP_GRTR; 
						else {
							a1 = ((Point3d)p1).getZ();
							a2 = ((Point3d)p2).getZ();
							if (a1 < a2) return COMP_LESS;
							else {
								if (a1 > a2) return COMP_GRTR; else return COMP_EQUAL;
							}
						}
					}
				}
			}
		}
		else throw SortTool.err1;
	}
}

