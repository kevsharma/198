package practice;


class Item{
	int value; 
	boolean isRed; 
	
	public Item(int value, boolean isRed) {
		this.value = value;
		this.isRed = isRed;
	}
	
	public String toString() {
		return "" + value;
	}
}

public class dp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Item[] S = new Item[5];
		// declared items, initialize them using constructor
		S[0] = new Item(2, false);
		S[1] = new Item(3, true);
		S[2] = new Item(6, false);
		S[3] = new Item(8, true);
		S[4] = new Item(10, false);
		
		System.out.println(algo(S, 8));
	}

	public static int algo(Item[] S, int B) {

		int n = S.length; 
		
		// initializtion 
		
		int[][] T = new int[n][B+1];			
		for(int col=0; col<B+1; col++)
			T[0][col] = 101;
		
		T[0][0] = 0;
		if(S[0].value <= B)
			T[0][S[0].value] = S[0].isRed ? 1 : 0;
		
		for(int i=0; i<n; i++)
		{	for(int j=0; j<B+1; j++)
				System.out.print(T[i][j] + "\t");
			System.out.println("");
		}
		System.out.println("\nInitialization done, moving onto DP: \n\n\n");
		
		
		
		// dp relation
		for(int i=1; i<n; i++)
		{
				int total = S[i].isRed ? 1 : 0;
			
			for(int j=0; j<B+1; j++) {
				T[i][j] = T[i-1][j];
				if(j-S[i].value < 0) continue;
				T[i][j] = Math.min(T[i-1][j], T[i-1][(j-S[i].value)]+ total);
			}
			
			
			System.out.println("i = " + i + "; S[i] = " + S[i].value);
			for(int r=0; r<n; r++)
			{	for(int c=0; c<B+1; c++)
					System.out.print(T[r][c] + "\t");
				System.out.println("");
			}
			System.out.println("\n\n");
		}
		
		return T[n-1][B]; //<= 100;
	}
	
}


