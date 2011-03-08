package dataStructures;


public class DLListIterator implements java.util.Iterator<Object> {

	private DLList lst;
	private DLNode current;
	
	public DLListIterator(DLList lst) {
		this.lst = lst;
		current = null;
	}
	
	public boolean hasNext() { 
		if (lst.isEmpty()) return false; else return current != lst.last; 
	}
	
	public Object next() {
		if (hasNext()) {
			if (current == null) current = lst.first; else current = current.next;
			return current.obj;
		}
		else return null; 
	}
	
	public void remove() {}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
