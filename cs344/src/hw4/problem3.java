package hw4;

public class problem3 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(Pseudocode(5,3));
	}

	public static int Pseudocode(int n, int k) {
		int[] T = new int[n+1];
		T[0] = 1; // initialization
		
		for(int i=1; i<=n; i++) {
			for(int j=1; j<=k; j++) {
				if(j<=i) {
					// dp relation
					T[i] = T[i] + T[i-j];
				}
			}
		}
		
		return T[n];
	}
}
