package structures;

public class BSTNode<T extends Comparable<T>> {
    T data;
    BSTNode<T> left, right;

    public BSTNode(T data){
        this.data = data;
        this.left = null;
        this.right = null;
    }

    public BSTNode(T data, BSTNode<T> left, BSTNode<T> right){
        this.data = data;
        this.left = left;
        this.right = right;
    }
}
