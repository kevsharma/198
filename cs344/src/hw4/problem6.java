package hw4;

public class problem6 {
	public static void main(String[] args) {
		//pseudocode("temporaremporium", "emporiumtemp");
		System.out.println("hamburger".substring(7,8));
		//pseudocode("aabcdeabc", "abcdabcdea");
	}
	
	public static void pseudocode(String S, String T) {
		// indexing starts with 0 for both arrays and Strings
		// for Strings 0 through len(String)-1
		
		int[][] table = new int[S.length()+1][T.length()+1];
		// initialization
		for(int i=0; i<table.length; i++)
			for(int j=0; j<table[0].length; j++) 
				table[i][j] = 0;
			
		// dp relation - if matches, table[i][j] += table[i-1][j-1]
		for(int i=1; i<table.length; i++) { // O(len(S)) 
			for(int j=1; j<table[0].length; j++) { // O(len(T))
				if(S.charAt(i-1) == T.charAt(j-1)) {
					table[i][j] = 1 + table[i-1][j-1]; // dp relation
				}
			}
		}
		
		// find maximum value from table, then derive the substring using that maximum value
		int longestConsec = 0;
		int indexWhereLongestEndsInS = 0;
		
		for(int i=1; i<table.length; i++) { // O(len(S)) 
			for(int j=1; j<table[0].length; j++) { // O(len(T))
				if(table[i][j] > longestConsec) {
					longestConsec = table[i][j];
					indexWhereLongestEndsInS = i-1;
				}
			}
		}
		System.out.println("Longest consecutive length: " + longestConsec);
		System.out.println("start: " + (indexWhereLongestEndsInS-longestConsec+1));
		System.out.println("end: " + indexWhereLongestEndsInS);
		String common = new String(S.substring(indexWhereLongestEndsInS-longestConsec+1, indexWhereLongestEndsInS+1));
		System.out.println("Longest Consecutive string:" + common);
	}
}

