package structures;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class BST<T extends Comparable<T>>{

    private BSTNode<T> root; // root of our BST 
    private int size; // # elements in our BST

    public BST(){
        root = null;
        size = 0;
    }

    // Throws illegalArgExcept if item is a duplicate.
    public void insert(T item) throws IllegalArgumentException{
        BSTNode<T> ptr = root; 
        BSTNode<T> prev = null;
        int c = 0;

        while(ptr != null){
            c = item.compareTo(ptr.data);
            if(c == 0)
                throw new IllegalArgumentException("Duplicate entry found");
            
            prev = ptr;
            ptr = c < 0 ? ptr.left : ptr.right;
        }

        // We have found a position for item.
        BSTNode<T> toInsert = new BSTNode<T>(item);

        // If prev is null, then root was null.
        if(prev == null){
            root = toInsert;
            size++;
        }

        // Otherwise insert in correct subtree.
        else{
            if(c < 0){
                prev.left = toInsert;
            }
            else{
                prev.right = toInsert;
            }
        }
    }


	public T delete(T key) throws NoSuchElementException {
        // search and locate
        // x tracks, p is lagging parent
		BSTNode<T> x = root, p = null;   
		int c;
		while (x != null) {
			c = key.compareTo(x.data);
			if (c == 0) {
				break;
			}
			p = x;
			x = c < 0 ? x.left : x.right;
        }
        // Should the key not be in the BST, then we throw an exception.
		if (x == null) throw new NoSuchElementException();
        
        // Note that at this point, x contains the key and p is its parent.

        T toReturn = null;
        
    	if (x.right != null && x.left != null) {
			// y is the inorder predecessor of x.
			BSTNode<T> y = x.left;  // left turn first
			p = x;
			while (y.right != null) {  // keep turning right until dead end
				p = y;
				y = y.right;
            }
            
            /*
            * Store x.data, then put y.data into x.data and set x to y so that bottom cases execute and remove x.
            */
            toReturn = x.data;
			x.data = y.data; 
			x = y;  
		}

		if (p == null) {  // no parent
			root = x.left == null ? x.right : x.left;  // works for both case 1 and case 2
			size--;
			return toReturn;
		}

		if (p.right == x) {
			p.right = x.right == null ? x.left : x.right;
		}
		else {
			p.left = x.right == null ? x.left : x.right;
		}

		size--;
		return toReturn;
	}

    /*
    * Returns null if not found.
    */
    public T search(T key){
        
        BSTNode<T> ptr = root;
        while(ptr != null){
            if(ptr.data.compareTo(key) == 0){
                return ptr.data;
            }
                // key is 3, ptr.data is 19. 3.compareTO(19) < 0, then we go left.
            ptr = key.compareTo(ptr.data) < 0 ? ptr.left : ptr.right;
        }

        return null;
    }

    private void inorderTraversal(BSTNode<T> root, ArrayList<T> list){
        if(root == null)
            return;

        // Go all the way to the left.
        inorderTraversal(root.left, list);

        // When you traverse back up from the Null, add that element to list
        list.add(root.data);

        // Go all the way to the right.
        inorderTraversal(root.right, list);
    }

    public ArrayList<T> sort(){
        ArrayList<T> sortedElements = new ArrayList<>();
        inorderTraversal(root, sortedElements);
        return sortedElements;
    }
}
