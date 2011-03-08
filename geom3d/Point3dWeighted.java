package geom3d;

public class Point3dWeighted extends Point3d {
	
	protected double weight;
	protected int ref; 
	
	public Point3dWeighted(Point3d p, double weight) {
		super(p);
		this.weight = weight;
	}
	
	public Point3d getPoint3d() {return (Point3d)this; }
	public double getWeight() { return weight; }
	
	public void setWeight(double weight) { this.weight = weight; }
}
