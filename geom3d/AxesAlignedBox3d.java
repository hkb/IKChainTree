package geom3d;

public class AxesAlignedBox3d  extends Box3d {

	public AxesAlignedBox3d() {}
	/**
	 * Constructs minimum size axes-aligned box containing given set of points.
	 */
	public AxesAlignedBox3d(PointSet3d points) {
		double minX = points.getExtremeLeft().getX();
		double maxX = points.getExtremeRight().getX();
		double minY = points.getExtremeBottom().getY();
		double maxY = points.getExtremeTop().getY();
		double minZ = points.getExtremeBack().getZ();
		double maxZ = points.getExtremeFront().getZ();
		anchor = new Point3d(minX + (maxX-minX)/2, minY + (maxY - minY)/2, minZ + (maxZ - minZ)/2);
		Vector3d xdir = new Vector3d((maxX - minX)/2, 0, 0);
		Vector3d ydir = new Vector3d(0, (maxY - minY)/2, 0);
		Vector3d zdir = new Vector3d(0, 0, (maxZ - minZ)/2);
		bases = new Vector3d[]{xdir, ydir, zdir};
	}
	
	/*
	 * returns true if axes-aligned box intersects or touches this axis-aligned box
	 */
	public boolean isIntersected(AxesAlignedBox3d box) {
		/*
		if ((anchor.x - xdir.x > box.anchor.x + box.xdir.x) || (anchor.x + xdir.x < box.anchor.x - box.xdir.x)) return false;
		if ((anchor.y - ydir.y > box.anchor.y + box.ydir.y) || (anchor.y + ydir.y < box.anchor.y - box.ydir.y)) return false;
		if ((anchor.z - zdir.z > box.anchor.z + box.zdir.z) || (anchor.z + zdir.z < box.anchor.z - box.zdir.z)) return false;
		return true;
		 */
		throw new Error("Unimplemented (Box3d changed. Update this method)");
	}
	
	public String toString(int dec) {
		return "Axes-aligned box, anchor   = " + anchor.toString(dec) + '\n' +
		   	   "                  x-vector = " + bases[0].toString(dec) + '\n' +
		   	   "                  y-vector = " + bases[1].toString(dec) + '\n' +
		   	   "                  z-vector = " + bases[2].toString(dec);
	}
	
	public static void main(String[] args) {
		AxesAlignedBox3d box = new AxesAlignedBox3d(new PointSet3d(10));
		System.out.println(box.toString(3));
	}
}
