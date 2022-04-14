package hw4;

public class problem2 {
	
	public static void main(String[] args) {
		System.out.println(MinBills(269));
	}
	
	// our big number will be like 3000. So no coin permutation should exceed 3000
	public static Tuple MinBills(int N) {
		// Dynamic programming
		int[] T = new int[N+1];
		T[0] = 0;

		int j = 3000;

		for(int amt=1; amt<=N; amt++) {
			if(amt-50 >= 0) {
				j = Math.min(T[amt-1], T[amt-6]);
				j = Math.min(j, T[amt-27]);
				j = Math.min(j, T[amt-38]);
				j = Math.min(j, T[amt-50]);
			}
			else if(amt-38 >= 0) {
				j = Math.min(T[amt-1], T[amt-6]);
				j = Math.min(j, T[amt-27]);
				j = Math.min(j, T[amt-38]);
			}
			else if(amt-27 >= 0) {
				j = Math.min(T[amt-1], T[amt-6]);
				j = Math.min(j, T[amt-27]);
			}
			else if(amt-6 >= 0) {
				j = Math.min(T[amt-1], T[amt-6]);
			}
			else {
				j = T[amt-1];
			}
			
			T[amt] = 1 + j;
		}
		
		// Return
		Tuple t = new Tuple(T[N]);
		int amt = N;
		for(int i=0; i<t.A.length; i++) {			
			if(amt-50 >= 0) {
				j = Math.min(T[amt-1], T[amt-6]);
				j = Math.min(j, T[amt-27]);
				j = Math.min(j, T[amt-38]);
				j = Math.min(j, T[amt-50]);
				
				// actually populating
				
				if(j == T[amt-1]) {
			    	t.A[i] = 1;
			    	amt -= 1;
			    }else if(j == T[amt-6]) {
			    	t.A[i] = 6;
			    	amt -= 6;
			    }else if(j == T[amt-27]) {
			    	t.A[i] = 27;
			    	amt -= 27;
			    }else if(j == T[amt-38]) {
			    	t.A[i] = 38;
			    	amt -= 38;
			    }else if(j == T[amt-50]) {
			    	t.A[i] = 50;
			    	amt -= 50;
			    }
			}
			else if(amt-38 >= 0) {
				j = Math.min(T[amt-1], T[amt-6]);
				j = Math.min(j, T[amt-27]);
				j = Math.min(j, T[amt-38]);
				// actually populating
				
				if(j == T[amt-1]) {
			    	t.A[i] = 1;
			    	amt -= 1;
			    }else if(j == T[amt-6]) {
			    	t.A[i] = 6;
			    	amt -= 6;
			    }else if(j == T[amt-27]) {
			    	t.A[i] = 27;
			    	amt -= 27;
			    }else if(j == T[amt-38]) {
			    	t.A[i] = 38;
			    	amt -= 38;
			    }
			}
			else if(amt-27 >= 0) {
				j = Math.min(T[amt-1], T[amt-6]);
				j = Math.min(j, T[amt-27]);
				if(j == T[amt-1]) {
			    	t.A[i] = 1;
			    	amt -= 1;
			    }else if(j == T[amt-6]) {
			    	t.A[i] = 6;
			    	amt -= 6;
			    }else if(j == T[amt-27]) {
			    	t.A[i] = 27;
			    	amt -= 27;
			    }
			}
			else if(amt-6 >= 0) {
				j = Math.min(T[amt-1], T[amt-6]);
				if(j == T[amt-1]) {
			    	t.A[i] = 1;
			    	amt -= 1;
			    }else if(j == T[amt-6]) {
			    	t.A[i] = 6;
			    	amt -= 6;
			    }
			}
			else {
				j = T[amt-1];
				if(j == T[amt-1]) {
			    	t.A[i] = 1;
			    	amt -= 1;
			    }
			}
		}
		
		return t;
	}
}


class Tuple{
	int minBills;
	int[] A;
	
	public Tuple(int mB) {
		minBills = mB;
		A = new int[minBills];
	}
	
	public String toString() {
		String str = new String("It took " + minBills + " bills.");
		str +=" ->  ";
		for(int integer : A)
			str += integer + ",  ";
		
		return str;
	}
}















