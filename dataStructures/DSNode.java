package dataStructures;

public class DSNode {
	protected Object object;
	protected DSNode parent;
	protected int rank; 
	
	public DSNode(Object object) {
		this.object = object;
		parent = this;
		rank = 0;
	}
}
