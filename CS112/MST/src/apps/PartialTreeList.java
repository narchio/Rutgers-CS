package apps;

import java.util.Iterator;
import java.util.NoSuchElementException;

import structures.Vertex;


public class PartialTreeList implements Iterable<PartialTree> {
    
	/**
	 * Inner class - to build the partial tree circular linked list 
	 * 
	 */
	public static class Node {
		/**
		 * Partial tree
		 */
		public PartialTree tree;
		
		/**
		 * Next node in linked list
		 */
		public Node next;
		
		/**
		 * Initializes this node by setting the tree part to the given tree,
		 * and setting next part to null
		 * 
		 * @param tree Partial tree
		 */
		public Node(PartialTree tree) {
			this.tree = tree;
			next = null;
		}
	}

	/**
	 * Pointer to last node of the circular linked list
	 */
	private Node rear;
	
	/**
	 * Number of nodes in the CLL
	 */
	private int size;
	
	/**
	 * Initializes this list to empty
	 */
    public PartialTreeList() {
    	rear = null;
    	size = 0;
    }

    /**
     * Adds a new tree to the end of the list
     * 
     * @param tree Tree to be added to the end of the list
     */
    public void append(PartialTree tree) {
    	Node ptr = new Node(tree);
    	if (rear == null) {
    		ptr.next = ptr;
    	} else {
    		ptr.next = rear.next;
    		rear.next = ptr;
    	}
    	rear = ptr;
    	size++;
    }

    /**
     * Removes the tree that is at the front of the list.
     * 
     * @return The tree that is removed from the front
     * @throws NoSuchElementException If the list is empty
     */
    public PartialTree remove() 
    throws NoSuchElementException {
    		
    	////	/* COMPLETE THIS METHOD *//////
    	
    		PartialTree answer = null; 
    		
    		// if the size is 0 || 1 
    		if (rear == null || rear == rear.next) {	
    			// throw exception if there is 
    			if (size == 0) {
    				throw new NoSuchElementException(); 
    			}
    			// if the rear is not null, then it is the rear.next (so size == 1)
    			if (rear != null) {
    				answer = rear.tree; 
    			}
    			rear = null; 
    			size--; 
    			return answer; 
    		
    		} else {
    			// if the size is greater than 1 
    			answer = rear.next.tree; 
    			rear.next = rear.next.next; 
    			size--; 
    			return answer; 
    		}
    }

    /**
     * Removes the tree in this list that contains a given vertex.
     * 
     * @param vertex Vertex whose tree is to be removed
     * @return The tree that is removed
     * @throws NoSuchElementException If there is no matching tree
     */
    public PartialTree removeTreeContaining(Vertex vertex) 
    throws NoSuchElementException {
    	////	/* COMPLETE THIS METHOD */////
    	Node ptr = rear.next; 
    	Node prev = rear; 
    	
    	if (size == 0) { // if there are no elements, then throw exception 
    		throw new NoSuchElementException(); 
   
    	} else {
    		// this is the first 1 run through
    		if (ptr.tree.getRoot().equals(vertex.getRoot())) {
				
				if (size == 1) { // check if there is only 1 node
					rear = null; 
					size--; 
					return ptr.tree; 
					
				} else if (vertex.getRoot().equals(rear.tree.getRoot())) { // comparing the two vertices 
					prev.next = rear.next; // or should it be rear.next?
					rear = prev; 
					size--; 
					return ptr.tree; 
				
				} else { // if there is more than 1 node and it is not equal to the tree's rear
					prev.next = ptr.next; 
					size--; 
					return ptr.tree; 
				}
			}
			prev = ptr; 
    			ptr = ptr.next; 
			

    		while (ptr != rear.next) { // now if it is not equal to length of 1, then go through till the list is finished
    			if (ptr.tree.getRoot().equals(vertex.getRoot())) {
    				
    				if (size == 1) { // check if there is only 1 node
    					rear = null; 
    					size--; 
    					return ptr.tree; 
    					
    				} else if (vertex.getRoot().equals(rear.tree.getRoot())) { // comparing the two vertices 
    					prev.next = rear.next; // or should it be rear.next?
    					rear = prev; 
    					size--; 
    					return ptr.tree; 
    				
    				} else { // if there is more than 1 node and it is not equal to the tree's rear
    					prev.next = ptr.next; 
    					size--; 
    					return ptr.tree; 
    				}
    			}
    			prev = ptr; 
    			ptr = ptr.next; 
    			
    			
    			// now, after the second iteration, check if the condition of the while loop is met, and if so, throw the exception
    			if (ptr == rear.next) { // if there are no matching trees, throw exception
    				throw new NoSuchElementException(); 
    			}
    		}	
    	}
    		return ptr.tree;
    }
    
    /**
     * Gives the number of trees in this list
     * 
     * @return Number of trees
     */
    public int size() {
    	return size;
    }
    
    /**
     * Returns an Iterator that can be used to step through the trees in this list.
     * The iterator does NOT support remove.
     * 
     * @return Iterator for this list
     */
    public Iterator<PartialTree> iterator() {
    	return new PartialTreeListIterator(this);
    }
    
    private class PartialTreeListIterator implements Iterator<PartialTree> {
    	
    	private PartialTreeList.Node ptr;
    	private int rest;
    	
    	public PartialTreeListIterator(PartialTreeList target) {
    		rest = target.size;
    		ptr = rest > 0 ? target.rear.next : null;
    	}
    	
    	public PartialTree next() 
    	throws NoSuchElementException {
    		if (rest <= 0) {
    			throw new NoSuchElementException();
    		}
    		PartialTree ret = ptr.tree;
    		ptr = ptr.next;
    		rest--;
    		return ret;
    	}
    	
    	public boolean hasNext() {
    		return rest != 0;
    	}
    	
    	public void remove() 
    	throws UnsupportedOperationException {
    		throw new UnsupportedOperationException();
    	}
    	
    }
}


