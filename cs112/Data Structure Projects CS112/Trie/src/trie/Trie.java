package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords)
	{
		TrieNode root = new TrieNode(null, null, null);
		if(allWords.length == 0)
			return root;
		
		// initialize first child
		root.firstChild = new TrieNode(new Indexes(0, (short)(0),(short)(allWords[0].length()-1)), null, null);
		
		for(int i=1; i<allWords.length; i++)
		{
			TrieNode ptr = root.firstChild, prev = root;
			String word1 = "", word2 = allWords[i];
			
			/*
			 * Level 1 Traversal : Left to Right (siblings)
			 * Finds a node that contains a matching prefix to allWords[i] and sets prev and ptr to that node.
			 * If it doesn't find such a node, then prev = right most node in level 1 and ptr = prev.sibling;
			 */
			while(ptr != null)
			{
				word1 = allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex, ptr.substr.endIndex + 1);
				
				if(commonPrefix(word1, word2).length() != 0) // Found matching prefix
				{
					prev = ptr;
					break;
				}
				prev = ptr;
				ptr = ptr.sibling;
			}

			
			if(ptr == null) // No match found in Level 1 for allWords[i]. prev = right most node in level 1. ptr = prev.sibling = null.
				prev.sibling = new TrieNode(new Indexes(i, (short)(0),(short)(allWords[i].length()-1)), null, null);
				
			else // Match found, prev and ptr both node where there is a common prefix.
			{
				String commonPref = commonPrefix(word1,word2);
				
				// If that node has no children, then this works as intended.
				if(prev.firstChild == null)
				{
					// #1 Change prev's indexes to appropriate prefix
					// #2 Change firstChild to whatever comes after prev's index change
					// #3 Add a sibling with appropriate indexes
					
					// #1
					prev.substr.endIndex = (short)(commonPref.length() - 1);
					
					// #2
					int wordIndex = prev.substr.wordIndex;
					Indexes temp = new Indexes(wordIndex, (short)commonPref.length(),(short)(allWords[wordIndex].length()-1));
					prev.firstChild = new TrieNode(temp, null, null);
					
					// #3
					Indexes temp2 = new Indexes(i,(short)commonPref.length(), (short)(allWords[i].length()-1));
					prev.firstChild.sibling = new TrieNode(temp2, null, null);
				}
				
				// If prev and ptr do have children (there may be more common prefix after we shave common prefix.)
				// prev and ptr being matching node in level 1
				else
				{
					
					// we have common pref, lets check if it only partially matches with *prev = ptr* node
					// i.e. if prev = be but word contains b(!e), then we need to change upper
					if(word1.length()>commonPref.length() && word2.length()>commonPref.length() 
							&& commonPref.length()!=0)
					{
						/*
						 * #1 Change prev index
						 * #2 Create new child which holds rest of prefix
						 * #3 Attach older child to #2 node
						 * #4 Insert new node as sibling of created subtree, i.e prev.firstChild.sibling.
						 */
						
						// #1
						ptr.substr.endIndex = (short)((int)ptr.substr.endIndex - commonPref.length());
						
						// #2
						ptr = prev.firstChild; // Store existing
						
						Indexes temp = new Indexes(prev.substr.wordIndex, (short)(prev.substr.startIndex + commonPref.length()),
								(short)(prev.substr.endIndex + commonPref.length()));
						
						// #3
						prev.firstChild = new TrieNode(temp,ptr, null);
						
						// #4
						Indexes t = new Indexes(i, (short)(prev.substr.startIndex + commonPref.length()),
								(short)(allWords[i].length()-1));
					
						prev.firstChild.sibling = new TrieNode(t, null, null); // add the new word
						
						continue;
					}
					
					TrieNode prev2 = ptr;
					ptr = prev.firstChild;
					word2 = word2.substring((prev.substr.endIndex - prev.substr.startIndex)+1); 
					
					// Before this we need to change prev's indexes
					while(ptr!=null) 
					{
						// Change word1 to ptr and change word2 to (-prev)word2
						word1 = allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex, ptr.substr.endIndex + 1);
						commonPref = commonPrefix(word1,word2); 
														
						if(commonPref.length()!=0 && ptr.firstChild!=null)
						{
							prev = ptr;
							prev2 = ptr;
							ptr = ptr.firstChild;
							
							// Change word2 to (-prev)word2
							word2 = word2.substring((prev.substr.endIndex - prev.substr.startIndex)+1); 
							break; // ptr = ptr.sibling must not be reached
							
						}
						
						else if(commonPref.length() != 0) // Executed if they have some prefix in common
						{
							// #1	Change ptr nodes' indexes endIndex to make it prefix				
							ptr.substr.endIndex = (short)(ptr.substr.startIndex + commonPref.length() - 1);
							
							// #2	Add remainder of ptr nodes' substring first.
							int wordIndex = ptr.substr.wordIndex;
							Indexes temp = new Indexes(wordIndex, (short)(ptr.substr.startIndex +commonPref.length()), (short)(allWords[wordIndex].length()-1));
							ptr.firstChild = new TrieNode(temp, null, null);
							
							// #3	Then attach (-prev)word2
							Indexes temp2 = new Indexes(i,(short)(ptr.substr.startIndex +commonPref.length()), (short)(allWords[i].length()-1));
							ptr.firstChild.sibling = new TrieNode(temp2, null, null);
							
							break; // ptr = ptr.sibling must not be reached
						}
						prev2 = ptr;
						ptr = ptr.sibling;
					}
					
					// This is only reached if ptr became null and we had no matches.
					if(ptr == null) 
					{
						Indexes temp = new Indexes(i, (short)(prev.substr.endIndex - prev.substr.startIndex + 1),
								(short)(allWords[i].length()-1));
					
						prev2.sibling = new TrieNode(temp, null, null);
					}
					
				}
			}
		}
		return root;
	}
	
	private static String commonPrefix(String word1, String word2)
	{
		// word1 is the Parent node
		// word2 is the node to be inserted
		int count = 0;
		while(count<Math.min(word1.length(), word2.length()))
		{
			if(word1.charAt(count) == word2.charAt(count))
				count++;
			else
				break;
		}
		
		return new String(word1.substring(0,count));
	}
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root, String[] allWords, String prefix)
	{
		ArrayList<TrieNode> result = new ArrayList<>();
		if(root.firstChild == null)
			return null;
		
		TrieNode ptr = root.firstChild;

		// Print the entire tree if prefix is empty, thus it matches with everything
		// Do not move beyond the if.
		if(prefix.length() == 0)
		{
			grabLeaves(ptr, result);
			
			// You must return null if arraylist is empty, you cannot return empty arrayList
			if(result.size()==0)
				return null;
			
			return result;
		}
			
		String currentWord = allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex, ptr.substr.endIndex + 1);
		String commonPrefix = commonPrefix(currentWord, prefix);
		// Find which node matches with Prefix to the max
		while(prefix.length()> 0 && ptr!= null)
		{
			currentWord = allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex, ptr.substr.endIndex + 1);
			commonPrefix = commonPrefix(currentWord, prefix);
			
			if(commonPrefix.length() > 0) // If they have some prefix in common
			{
				if((currentWord.length() > commonPrefix.length()) && (prefix.length() > commonPrefix.length()))
					return null;
				
				// Otherwise
				prefix = prefix.substring(commonPrefix.length());
				
				if(prefix.length()>0)
					ptr = ptr.firstChild;
				else
					break;
			}
			else
				ptr = ptr.sibling;
		}
		
		if(ptr!= null)
		{
			TrieNode temp = ptr.sibling;
			ptr.sibling = null;
			grabLeaves(ptr, result);
			ptr.sibling = temp;
		}
		
		// You must return null if arrayList is empty, you cannot return empty arrayList
		if(result.size()==0)
			return null;
		
		return result;
	}
	
	
	private static void grabLeaves(TrieNode ptr, ArrayList<TrieNode> result)
	{
		if(ptr == null)
			return;
		
		grabLeaves(ptr.firstChild, result);
		if(ptr.firstChild == null)
			result.add(ptr);
		grabLeaves(ptr.sibling, result);
	}
	
	public static void print(TrieNode root, String[] allWords){
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 
}
