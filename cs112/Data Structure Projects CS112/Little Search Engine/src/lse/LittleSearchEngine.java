package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine
{
	
	/**
	 * Below two Hashtables have the following designations:
	 * keywordsIndex: 
	 * This is a hash table of all keywords. 
	 * 
	 * The key is the actual keyword, and the associated value is an array list of all occurrences of the keyword in documents. 
	 * The array list is maintained in DESCENDING order of frequencies.
	 * 
	 * 
	 * noiseWords:
	 * The hash set of all noise words.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine()
	{
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) throws FileNotFoundException
	{
		HashMap<String, Occurrence> temp = new HashMap<String, Occurrence>();
		
		Scanner sc = new Scanner(new File(docFile));
		while(sc.hasNext())
		{
			String line = sc.nextLine().trim();
			if(line.isEmpty() || line==null)
				continue;
					
			String[] words = line.split(" ");
			for(String word : words)
			{
				String isKeyword = getKeyword(word);
				if(isKeyword != null)
				{
					// if it already exists, increment count, else make new object
					if(temp.containsKey(isKeyword))
					{
						Occurrence value = temp.get(isKeyword);
						value.frequency = value.frequency + 1;
						
						temp.put(isKeyword, value);
					}
					else
					{
						Occurrence value = new Occurrence(docFile, 1);
						temp.put(isKeyword, value);
					}
				}
			}
		}
		
		sc.close();
		return temp;
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws)
	{
		/*
		 * kws has key|value ==> String|Occurrence
		 * keywordsIndex has key|value ==> String|Arraylist<Occ>
		 */
		for(String strKey : kws.keySet())
		{
			if(!keywordsIndex.containsKey(strKey))
			{
				ArrayList<Occurrence> value = new ArrayList<Occurrence>();
				
				Occurrence temp = kws.get(strKey);
				value.add(temp); // complete the key|value pair (make an ArrayList then add Occurrence object for strKey)
				
				insertLastOccurrence(value);
				
				keywordsIndex.put(strKey, value); // map new key|value pair to master hashtable
				
			}
			else
			{
				ArrayList<Occurrence> value = keywordsIndex.get(strKey);
				
				// get Occurrence object for strKey from kws
				Occurrence temp = kws.get(strKey); 
				
				// append temp to strKey, ArrayList in keywordsIndex
				value.add(temp); 
				
				 // put appended item in correct place
				insertLastOccurrence(value);
				
				keywordsIndex.put(strKey, value);
			}
		}
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test, otherwise returns null. 
	 * 
	 * A keyword is any word that, after being stripped of any
	 * trailing punctuation(s), consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * NO OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
	 * 
	 * If a word has multiple trailing punctuation characters, they must all be stripped
	 * So "word!!" will become "word", and "word?!?!" will also become "word"
	 * 
	 * See assignment description for examples
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word)
	{
		if(word.length() <= 1)
			return null;

		word = word.toLowerCase();

		// Checks word for non alphabetic characters which are not punctuation.
		for(int i=0; i<word.length(); i++)
		{
			char ch = word.charAt(i);
			
			// '.', ',', '?', ':', ';' and '!'
			if(!Character.isLetter(ch) && (ch != '.') && (ch != ',') && (ch != '?') && (ch != ':') && (ch != ';') && (ch != '!'))
				return null;
		}
		
		for(int i=0; i<word.length(); i++)
		{
			if(!Character.isLetter(word.charAt(i)) && i<word.length()-1)
			{
				// checks if non-letter is in between two letters, otherwise removes all trailing punctuation
				if(Character.isLetter(word.charAt(i+1)))
					return null;
				else
					word = word.substring(0, i);
			}
			
			// Works when i = word.length()-1. Remove trailing punctuation.
			else if(!Character.isLetter(word.charAt(i)))
				word = word.substring(0, i);
		}
		
		// word is free of punctuation
		if(noiseWords.contains(word))
			return null;
		
		// if word has nothing remaining
		if(word.length()==0)
			return null;
		
		return word;
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs)
	{
		if(occs.size() <= 1) // We had no comparisons of middle values.
			return null;
		
		ArrayList<Integer> mids = new ArrayList<Integer>();
		Occurrence temp = occs.remove(occs.size()-1);
		
		// Note that start and end are indexes and not values. start > end since occs is descending.
		int start = 0, end = occs.size()-1, mid = 0;
		
		while(start <= end)
		{
			mid = start + ((end - start)/2);
			mids.add(mid);
			
			if(occs.get(mid).frequency == temp.frequency)
			{
				occs.add(mid, temp);
				return mids;
			}
			
			else if(occs.get(mid).frequency > temp.frequency)
				start = mid + 1;
			else
				end = mid - 1;
		}
		
		occs.add(start, temp);
		return mids;
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) throws FileNotFoundException
	{
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext())
		{
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
		
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. 
	 * 
	 * Note that a matching document will only appear once in the result. 
	 * 
	 * Ties in frequency values are broken in favor of the first keyword. 
	 * That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same 
	 * frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * See assignment description for examples
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, 
	 *         returns null or empty array list.
	 */
	public ArrayList<String> top5search(String kw1, String kw2)
	{
		kw1 = kw1.toLowerCase();
		kw2 = kw2.toLowerCase();
		
		ArrayList<String> result = new ArrayList<String>();
		
		ArrayList<Occurrence> keywordOne = keywordsIndex.get(kw1);
		ArrayList<Occurrence> keywordTwo = keywordsIndex.get(kw2);
		
		if(keywordOne == null)
			keywordOne = new ArrayList<Occurrence>();
		if(keywordTwo == null)
			keywordTwo = new ArrayList<Occurrence>();
		if(keywordOne.size()==0 && keywordTwo.size()==0) 
			return result;
		
		
		ArrayList<Occurrence> one = new ArrayList<Occurrence>();
		ArrayList<Occurrence> two = new ArrayList<Occurrence>();
		// replicate items into one and two.
		
		for(Occurrence n : keywordOne)
		{
			Occurrence temp = new Occurrence(n.document, n.frequency);
			one.add(temp);
		}
		for(Occurrence n : keywordTwo)
		{
			Occurrence temp = new Occurrence(n.document, n.frequency);
			two.add(temp);
		}
		
		while((one.size() > 0 || two.size() > 0) && result.size()< 5)
		{
			if(one.size() == 0)
			{
				String tempDoc = two.remove(0).document;
				
				if(!result.contains(tempDoc))
					result.add(tempDoc);
			}
			else if(two.size()==0)
			{
				String tempDoc = one.remove(0).document;
				
				if(!result.contains(tempDoc))
					result.add(tempDoc);
			}
			else if(one.get(0).frequency >= two.get(0).frequency)
			{
				String tempDoc = one.remove(0).document;
				
				if(!result.contains(tempDoc))
					result.add(tempDoc);
			}
			else
			{
				String tempDoc = two.remove(0).document;
				
				if(!result.contains(tempDoc))
					result.add(tempDoc);
			}
		}
		
		return result;
	}
}














