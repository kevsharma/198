package hw4;

public class problem5 {

    public static void main(String[] args)
    {
        // gonna need an array for Bags
        Bag[] S = new Bag[6];
        S[0] = new Bag(14,20);
        S[1] = new Bag(15,14);
        S[2] = new Bag(7,12);
        S[3] = new Bag(5,7);
        S[4] = new Bag(4,5);
        S[5] = new Bag(2,2);

        System.out.println(maxValue(S,12)); // should give 19
        System.out.println(maxValue(S,17)); // should give 24
        System.out.println(maxValue(S,20)); // should give 27

    }

    public static int maxValue(Bag[] S, int W){
    	Bag[][] T = new Bag[S.length+1][W+1];
    	// dimension of T = (|S|+1) by (W+1), +1 to include Weight of suitcase being 0.
    	
    	// When we can initialize the first row to (0,0) tuples, we can use dp relation for first i.
    	for(int w=0; w<=W; w++) {
    		T[0][w] = new Bag(0,0); // weight, then value
    	}
    	
    	// dp relation
    	for(int i=1; i<=S.length; i++) {
    		Bag thisItem = S[i-1];
    		
    		for(int w=0; w<=W; w++) { // w is the weight going from 0 to W
    			// We will compare three things to put into this.
    			// above, thisItem.value, back
    			T[i][w] = new Bag(T[i-1][w].weight, T[i-1][w].value);
    			
    			if(w - thisItem.weight >= 0) {
    				int aboveValue = T[i-1][w].value;
        			if(T[i-1][w].weight + thisItem.weight <= w) {
        				aboveValue = aboveValue + thisItem.value;
        			}
    				int backValue = T[i-1][w-thisItem.weight].value + thisItem.value; 
        			
        			// compare the three and use whichever is larger for T[i][w]
        			if(aboveValue >= thisItem.value && aboveValue >= backValue) {
        				int x = T[i-1][w].weight;
        				if(aboveValue == T[i-1][w].value + thisItem.value) {
        					x = x + thisItem.weight;
        				}
        				T[i][w] = new Bag(x, aboveValue);
        			}
        			else if(thisItem.value >= aboveValue && thisItem.value >= backValue) {
        				T[i][w] = new Bag(thisItem.weight, thisItem.value);
        			}
        			else { // if(backValue >= aboveValue && backValue >= thisItem.value) {
        				T[i][w] = new Bag(T[i-1][w-thisItem.weight].weight + thisItem.weight, backValue);
        			}
    			}
    		}
    	}
    	
    	return T[S.length][W].value;
    }

}

class Bag{
    int weight;
    int value;

    public Bag(int weight, int value){
        this.weight = weight;
        this.value = value;
    }
}

