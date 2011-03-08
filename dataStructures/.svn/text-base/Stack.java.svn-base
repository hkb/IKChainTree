package dataStructures;

public class Stack {
	public StackNode top;
	private int size;
	
	private class StackNode {
		protected Object obj = null;
		protected StackNode prev = null;
		
		public StackNode(Object obj, StackNode prev) { 
			this.obj = obj; 
			this.prev = prev; 
		}
	}

	public Stack() { top = null; size = 0; }
	
	public void clear() { while (!isEmpty()) pop(); }
	
	public boolean isEmpty() {return top == null; }
	
	public void push(Object obj) {
		StackNode nd = new StackNode(obj,top);
		top = nd;
		size++;
	}
	
	/*
	 * pushes all objects in the set on the stack
	 */
	public void pushSet(Set set) { for (int i = 0; i < set.getSize(); i++) push(set.get(i)); }
	
	public Object pop() {
		StackNode nd = top;
		top = top.prev;
		size--;
		return nd.obj;
	}

	public Object peek() { return top.obj; }
	
	public int getSize() { return size; }
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

