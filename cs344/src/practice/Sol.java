package practice;

public class Sol {
	public static void main(String[] args) {
		int[] A = {2,1,3,6,3,3};
		Integer majEle = majority(A, 0, A.length-1);
		System.out.print("Length of Array: " + A.length);
		System.out.println("\nMajority Element constitutes " + ((int) Math.ceil((A.length)*2.0/3.0)) + " elements");
		
		System.out.println("\nMajority Element: "+majEle);
		int count;
		if(majEle != null) {
			count = 0;
			for(int a : A) {
				if (a == majEle)
					++count;
			}
			
			System.out.println(majEle + " appears " + count + " number of times.");
		}
	}
	
	public static Integer majority(int[] A, int lo, int hi) {
		// base case
		if(A.length == 0)
			return null;
		
		if(lo == hi)
			return A[lo];
		
		
		// divide step
		
		int mid = lo + (hi-lo)/2;
		Integer left = majority(A, lo, mid);
		Integer right = majority(A, mid+1, hi);
		
		// combine step
		
		int majorityQuantifier = (int) Math.ceil((hi-lo+1)*2.0/3.0);
		
		if(left != null) { // check whether value returned by left call forms majority
			int leftCount = 0;
			for(int i=lo; i<=hi; i++)
				if(A[i] == left)
					++leftCount;
			
			if(leftCount >= majorityQuantifier)
				return left;
		}
		
		
		// check whether value returned by the right call forms majority
		if(right != null) {
			int rightCount = 0;
			for(int i=lo; i<=hi; i++)
				if(A[i] == right)
					++rightCount;
			
			if(rightCount >= majorityQuantifier)
				return right;
		}
		
		// if neither 
		return null;
	}
}

