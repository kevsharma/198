package hw4;

import java.util.List;
import java.util.ArrayList;

public class problem4 {

	public static void main(String[] args) {
		int[] S = {2,3,5,7,13,15,19};
		for(int i=0; i<=30; i++) {
			System.out.println(i + ": " + Pseudocode(S,i));
		}
	}

	public static List<Integer> Pseudocode(int[] S, int B){
		/*
		 * Used for dp Relation
		 * two arrays needed to ensure an item from S is only used once in a subset summing to a value
		 */
		boolean[] previous = new boolean[B+1];
		boolean[] current = new boolean[B+1]; 
		int[] storage = new int[B+1]; // used to backtrack for the actual subset
		
		// initialization setup
		for(int i=0; i<=B; i++) {
			previous[i] = current[i] = false;
			storage[i] = -1;
		}
		previous[0] = true;
		
		for(int setItem : S) {
			for(int i=0; i<=B; i++) {
				if(i >= setItem) {
					current[i] = previous[i] || previous[i-setItem];
					if(!previous[i] && previous[i-setItem]) {
						// don't overwrite previous, if unnecessary
						//because then you'll look like you have repeats without actually having repeats
						storage[i] = i-setItem;
					}
				}
				else {
					current[i] = previous[i];
				}
			}
			
			// put contents of current into previous and reset current
			// doesn't overwrite the initial 0 set to true
			for(int b=1; b<=B; b++) {
				previous[b] = current[b];
				current[b] = false;
			}
		}
		
		// 'tacking back' using storage and 'j' to retrieve actual subset summing to B
		List<Integer> result = new ArrayList<>();
		
		int j = storage[B];
		int temp = B;
		while(j!=-1) {
			result.add(0, temp-j);
			temp = j;
			j = storage[temp];
		}
		
		return result;
	}
}


