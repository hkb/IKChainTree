package geom3d;

public class AxesAlignedCube3d extends AxesAlignedBox3d {
	
	/*
	 * constructs minimum size axes-aligned cube containing an axes-aligned box
	 */
	public AxesAlignedCube3d(AxesAlignedBox3d box) {
		double width = box.getWidth();
		anchor = box.anchor;
		bases = new Vector3d[3];
		bases[0] = box.bases[0].createScaledToLengthVector3d(width);
		bases[1] = box.bases[1].createScaledToLengthVector3d(width);
		bases[2] = box.bases[2].createScaledToLengthVector3d(width);
	}
	public static void main(String[] args) {
		AxesAlignedBox3d box = new AxesAlignedBox3d(new PointSet3d(10));
		AxesAlignedCube3d cube = new AxesAlignedCube3d(box);
		System.out.println(cube.toString(3));
	}

}
