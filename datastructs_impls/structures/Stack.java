package structures;

import java.util.NoSuchElementException;

public class Stack<T> {
    private int size;
    Node<T> front;

    public Stack(){
        front = null;
        size = 0;
    }

    public void push(T data){
        front = new Node<T>(data, front);
        ++size;
    }

    public T pop() throws NoSuchElementException{
        if(front == null){
            throw new NoSuchElementException("Stack is empty.");
        }

        T item = front.data;
        front = front.next;
        --size;

        return item;
    }

    public T peek() throws NoSuchElementException{
        if(front == null){
            throw new NoSuchElementException("Stack is empty.");
        }

        return front.data;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public int size(){
        return size;
    }

}