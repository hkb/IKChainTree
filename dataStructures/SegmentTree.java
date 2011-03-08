package dataStructures;

import chainTree.CTNode;

public class SegmentTree {
	Node root;
	
	private class Node {
		Node ndLeft, ndRight, ndParent;
		Object object;
	
		public Node(Node ndLeft, Node ndRight) {
			this.ndLeft = ndLeft;
			this.ndRight = ndRight;
			this.ndLeft.ndParent = this;
			this.ndRight.ndParent = this;
		}
	}
	public SegmentTree(Object[] objects) {
		
		Queue queue = new Queue();
		for (int i = 0; i < objects.length; i++) queue.push(objects[i]);
	
		int sz = objects.length, sz2;
		Node ndLeft, ndRight;
		while (sz > 1) {
			sz2 = sz/2;
			System.out.println("Queue size = " + sz + " sz2 = " + sz2);
			for (int k = 0; k < sz2; k++) {
				ndLeft = (Node)queue.pop();
				ndRight = (Node)queue.pop();
				queue.push(new Node(ndLeft, ndRight)); 
			}
			if (2*sz2 != sz) {
				queue.push(queue.pop());
				sz2++; 
			}
			sz = sz2;
		}
		root = (Node)queue.pop();
		queue.clear();
	}	

}
