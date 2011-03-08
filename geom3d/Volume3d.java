package geom3d;

/**
 * An interface for bounding volumes. 
 * @author Ras
 */
public interface Volume3d extends Shape3d{
	
	public boolean overlaps(Volume3d vol);
	public double volume();
	public Volume3d clone();
}
