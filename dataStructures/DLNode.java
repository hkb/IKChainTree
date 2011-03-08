package dataStructures;

public class DLNode {
	protected Object obj;
	protected DLNode prev;
	protected DLNode next;
	
	public DLNode(Object obj) { 
		this.obj = obj;
		prev = null;
		next = null; 
	}
	public DLNode(Object obj, DLNode prev, DLNode next) {
		this.obj = obj;
		this.prev = prev;
		this.next = next;
	}
	
	/*
	 * returns the object represented by the node
	 */
	public Object getObject() { return obj; }
	
	/*
	 * returns previous node of the doubly linked list
	 */
	public DLNode getPrev() { return prev; }
	
	/*
	 * return next node of the doubly linked list
	 */
	public DLNode getNext() { return next; }
	
	/*
	 * clears the node and returns its object
	 */
	public Object clear() {
		prev = null;
		next = null;
		return obj;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

