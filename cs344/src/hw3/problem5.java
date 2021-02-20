package hw3;

import java.util.Arrays;
import java.util.HashMap;

public class problem5 {
	public static void main(String[] args) {
		int[] A = {3,2,7,3,5,3,5,7,2};
		System.out.println("During a call to our procedure,");
		System.out.println("Our dictionary D, looks as follows for every iteration:\n");
		int[] B = problem3(A,4);
		
		System.out.println("\nContents of B after call: " + Arrays.toString(B));
	}
	
	public static int[] problem3(int[] A, int k) {
		int n = A.length;
		int[] B = new int[n-k+1];

		HashMap<Integer, Integer> D = new HashMap<>();
		
		int distinct = 0;
		int indexB = 0;

		for(int i=0; i<n; i++) {
			System.out.println("Beginning of iteration i="+i+":\t"+ D.toString());
			if(i>= k) {
				B[indexB] = distinct;
				++indexB;
				
				Integer value = D.get(A[i-k]);
				if(value == 1) {
					D.remove(A[i-k]);
					--distinct;
				}
				else {
					D.put(A[i-k], value-1);
				}
			}
			
			// increment #distinct (count) if it doesn't exist in dict
			Integer value = D.get(A[i]);
			if(value != null) {
				D.put(A[i], value+1);
			}
			else
			{
				++distinct;
				D.put(A[i], 1);
			}
		}
		System.out.println("Loop terminated, dictionary:\t"+ D.toString());
		
		B[indexB] = distinct;
		return B;
	}
	
	
	// Runs in O(n)
	public static Tuple extraCredit(int[] A) {
		int n = A.length;
		
		int runningTotal = 0;
		HashMap<Integer, Integer> D = new HashMap<>();
		
		for(int x=0; x<n; ++x) {
			runningTotal += A[x];
			if(runningTotal == 100)
				return new Tuple(0, x);
			
			int complement = runningTotal - 100;
			if(D.get(complement) != null)
				return new Tuple(D.get(complement)+1, x);
			
			D.put(runningTotal, x);
		}
		
		return new Tuple(-1,-1);
	}

	public static int problem2(int[] A) {
		int longest = 1;
		HashMap<Integer, Integer> p2 = new HashMap<>();

		for(int i=0; i<A.length; i++) {
			Integer v = p2.get(A[i] -1);
			if(v != null) {
				p2.put(A[i], v+1);
				if(v+1 > longest) {
					longest = v+1;
				}
			}
			else {
				p2.put(A[i], 1);
			}
		}
		return longest;
	}
	
}



class Tuple{
	public int i;
	public int j;
	public Tuple(int i, int j){
		this.i = i;
		this.j = j;
	}
	
	public String toString() {
		return "(i,j) = (" + i + "," + j+")";
	}
}
