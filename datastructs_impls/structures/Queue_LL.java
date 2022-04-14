package structures;

import java.util.NoSuchElementException;

public class Queue_LL<T>{
    
    private Node<T> rear;
    private int size;

    public Queue_LL(){
        rear = null;
        size = 0;
    }

    // O(1) time enqueue.
    public void enqueue(T item){
        Node<T> toEnqueue = new Node<T>(item, null);
        
        // if rear is null, then this item is first.
        if(rear == null){
            toEnqueue.next = toEnqueue;
        }
        else{
            // rear was not null, hence rear.next exists.
            toEnqueue.next = rear.next;
            rear.next = toEnqueue;
        }

        rear = toEnqueue;
        size++;
    }

    // O(1) time dequeue
    public T dequeue() throws NoSuchElementException{
        // Using an arraylist, we had O(n) time dequeue.
        // Using a CLL we have O(1) time dequeue.

        if(rear == null)
            throw new NoSuchElementException("Queue is empty");
        
        T toDequeue = rear.next.data;
        size--;

        if(rear.next.data == rear.data){
            rear = null;    
        }
        else{
            rear.next = rear.next.next;
        }

        return toDequeue;
    }

    public T peek() throws NoSuchElementException{
        if (rear == null) {
			throw new NoSuchElementException("queue is empty");
        }
        
		return rear.next.data;
    }

    public boolean isEmpty(){
        return size==0;
    }

    public int size(){
        return size;
    }
}
