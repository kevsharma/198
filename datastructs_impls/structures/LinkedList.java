package structures;

import java.util.NoSuchElementException;

public class LinkedList<T>{
    private int size;
    private boolean isEmpty;
    private Node<T> front;

    public LinkedList(T data){
        front = new Node(data, null);
        size = 1;
    }

    public LinkedList(){
        size = 0;
    }

    private class Node<T>{
        T data;
        Node<T> next;

        public Node(T data, Node<T> next){
            this.data = data;
            this.next = next;
        }

        public Node(T data){
            this.data = data;
            this.next = null;
        }

        public boolean equals(Object o){
            if(!(o instanceof Node))
                return false;

            Node<T> other = (Node<T>) o;
            if(data == other.data && next == other.next){
                return true;
            }

            return false;
        }
    }

    public void insert_to_front(T data){
        size++;
        front = new Node<T>(data, front);
    }

    public void delete_front() throws NoSuchElementException{
        if(front==null)
            throw new NoSuchElementException("LinkedList is empty.");
        
        front = front.next;
        size--;
    }

    public void print_values(){
        /*
        *   Although this has been implemented here,
        *   it is worth nothing that we should instead
        *   use an iterator design pattern to traverse.

        *   Ideally create a new class called LinkedListIterator
        *   extends Iterator and code the next() and hasNext() methods.
        */

        Node<T> ptr = front;
        while(ptr!=null){
            System.out.print(ptr.data + " ==> ");
            ptr = ptr.next;
        }

        System.out.println("NULL");
    }
    
    private Node<T> head(){
        return front;
    }

    public boolean equals(Object o){
        if(!(o instanceof LinkedList)){
            return false;
        }

        LinkedList<T> other = (LinkedList<T>) o;
        
        Node<T> ptr = this.head();
        Node<T> other_ptr = other.head();

        while(ptr!=null && other_ptr!=null){
            if(!(ptr.equals(other_ptr))){
                return false;
            }

            ptr = ptr.next;
            other_ptr = other_ptr.next;
        }

        return true;
    }

    public boolean isEmpty(){
        return isEmpty;
    }
    
    public int size(){
        return size;
    }
}