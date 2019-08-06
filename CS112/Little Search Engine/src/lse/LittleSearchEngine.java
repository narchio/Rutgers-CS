package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
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
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
		/** COMPLETE THIS METHOD **/
		
		// make the keyword hash table 
		// call the get keyword method 
		int freq = 0; 
		String target = ""; 
		
	//	@SuppressWarnings("resource") // is this necessary?
		Scanner scan = new Scanner(new File(docFile)); // scan the document file (or do System.In)?
		
		HashMap<String, Occurrence> loaded = new HashMap<String, Occurrence>(); 
		
		if (docFile == null) {
			throw new FileNotFoundException(); 
		}
		
		while (scan.hasNext()) { // if there is another token 
			
			target = scan.next(); 
			
			if (getKeyword(target) != null) { // if it is a keyword
				target = getKeyword(target); 
				if (loaded.containsKey(target)) { // if it is not the first occurrence 
					freq = loaded.get(target).frequency;
					freq++; 
					Occurrence occur = new Occurrence(docFile, freq); 
					loaded.put(target, occur); 
				} else { // if it is the first occurrence 
					freq = 1; 
					Occurrence occur = new Occurrence(docFile, freq); 
					loaded.put(target, occur); 
				}
			}
		}
	//	System.out.println(loaded);
		return loaded; 
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
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		/** COMPLETE THIS METHOD **/
		//ArrayList<Occurrence> temp = new ArrayList<Occurrence>(); 
	//	System.out.println("this is kws: "+ kws);
	//	System.out.println("this is keyword index: " + keywordsIndex);
		
		ArrayList<Occurrence> temp = new ArrayList<Occurrence>();
		
		for (String target : kws.keySet()) {
			
			Occurrence t = kws.get(target); 
			
			if (keywordsIndex.containsKey(target)) { // if it is already in the master 
				
				temp = keywordsIndex.get(target); 
				temp.add(kws.get(target)); 
				
				insertLastOccurrence(temp); 
				keywordsIndex.put(target, temp); 
			} else { // if it is the first one
				ArrayList<Occurrence> temp2 = new ArrayList<Occurrence>();
				temp2.add(t); 
				keywordsIndex.put(target, temp2); 
			}
			
		}
	System.out.println("this is kws at the end: "+ kws);
	System.out.println("this is keyword index at the end: " + keywordsIndex);
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation, consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		/** COMPLETE THIS METHOD **/	
		
		// check if it is a noisword (noiseword.contains(word))
		
		word = word.toLowerCase(); 
		
		if (noiseWords.contains(word)) {
			return null; 
		} else {
			String newWord = "";  

			for (int i = 0; i < word.length(); i++) { // word.length()-1? i--? && i > ??
				if (Character.isLetter(word.charAt(i)) == true) { // if it is a letter
					newWord = newWord + word.substring(i, i+1); 
					newWord = newWord.toLowerCase(); 
				} else if (word.substring(i,word.length()-1).contains("!") || word.substring(i,word.length()-1).contains("-") ||
						word.substring(i,word.length()-1).contains("&") || word.substring(i,word.length()-1).contains("?") ||
						word.substring(i,word.length()-1).contains(";") || word.substring(i,word.length()-1).contains(",") ||
						word.substring(i,word.length()-1).contains(".") || word.substring(i,word.length()-1).contains(":") || 
						word.substring(i,word.length()-1).contains("}") || word.substring(i,word.length()-1).contains("{") ||
						word.substring(i,word.length()-1).contains("'")) { // if it is inside (contains)
					
					return null; 

				}else { // if punctuation at the end
					continue; 
				}
			}				
			return newWord; 
		}
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
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		/** COMPLETE THIS METHOD **/

		ArrayList<Integer> intPoints = new ArrayList<Integer>(); 
		
		// Values for the Binary Search
		int beg = 0; // first index
		int end = occs.size()-2; //last index
		int m = 0; 
		
		// this value is the value of the last index of the occs that needs to be removed in order to search/sort 
		// until the end where it is added back with the new value
		// the value to be stored and compared to:
		int target = occs.get(occs.size()-1).frequency; // target is the last occurrence in the occs Array list's frequency  

	
		
		// first check of the search, if the arrayList has more than 1 items 
		if (occs.size() == 0 || occs.size() == 1) {
			return null; 
		} else { // if it has more than 1 items, do the binary search
		
			while (end >= beg) { // iterate until the first index is equal to the last index 

				m = (beg+end)/2; // find the mid point by taking the average of the end points 
				
				int mPoint = occs.get(m).frequency; // mPoint is the frequency of the mid point
				
				
				intPoints.add(m); 

				if (mPoint == target) { // if it is equal to target
					break; 
				} else if (mPoint > target) { // if it is greater than target
					beg = m + 1; 
					if (end <= m) {
						m = m+1; 
					}
				} else if (mPoint < target) { // if it is less than target
					end = m-1;  
				}
			}
		}
		
		// add the final mid value
		intPoints.add(m);  
		// remove/save the target
		Occurrence temporaryVal = occs.remove(occs.size()-1); 
		// now do the actual insert last item
		occs.add(intPoints.get(intPoints.size()-1), temporaryVal); 
		
		return intPoints;
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
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
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
	 * document. Result set is arranged in descending order of document frequencies. (Note that a
	 * matching document will only appear once in the result.) Ties in frequency values are broken
	 * in favor of the first keyword. (That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2
	 * also with the same frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword // kw2 is what they meant
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, returns null.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		/** COMPLETE THIS METHOD **/
		// if the master contains it then take the first 5 occurrences 
		// but have to do for kw1 and kw2

		ArrayList<String> resultSet = new ArrayList<String>(); 

		//	System.out.println(keywordsIndex);

		ArrayList<Occurrence> list1 = keywordsIndex.get(kw1); 
		//System.out.println(keywordsIndex.get(kw1));
		ArrayList<Occurrence> list2 = keywordsIndex.get(kw2); 
		//System.out.println(keywordsIndex.get(kw2));
		ArrayList<Occurrence> temp = new ArrayList<Occurrence>(); 

		if (!keywordsIndex.containsKey(kw1) && keywordsIndex.containsKey(kw2)) { // if kw1 DNE, addAll kw2
			temp.addAll(keywordsIndex.get(kw2)); 
		} else if (!keywordsIndex.containsKey(kw2) && keywordsIndex.containsKey(kw1)) { // if kw2 DNE, addAll kw1
			temp.addAll(keywordsIndex.get(kw1)); 
		} else if (keywordsIndex.containsKey(kw1) && keywordsIndex.containsKey(kw2)) {

			//	list1.addAll(keywordsIndex.get(kw1)); 
			//	list2.addAll(keywordsIndex.get(kw2)); 

			Occurrence ptr = list1.get(0); 
			Occurrence ptr2 = list2.get(0); 
			int i = 0; // for ptr
			int k = 0; // for ptr2

			if (list1.size() == list2.size()) { // if the size of the two are equal

				while (ptr != null) { // increment through the two lists, add all to temp and remove ptr and ptr2 duplicates
					while (ptr2 != null) {
						if (ptr.frequency > ptr2.frequency) { 
							temp.add(ptr); 
							i++; 
							if (i < list1.size()) {
								ptr = list1.get(i); 
							} else {
								ptr = null; 
							}
							//ptr = list1.get(i);
							//counter++; 
						} else if (ptr.frequency < ptr2.frequency) {
							temp.add(ptr2); 
							k++; 
							if (k < list2.size()) {
								ptr2 = list2.get(k); 
							} else {
								ptr2 = null; 
							}

						} else if (ptr.frequency == ptr2.frequency) { // takes away the duplicate of ptr and ptr2 by only adding ptr
							temp.add(ptr); 
							i++;
							k++;
							if (i < list1.size()) { 
								ptr = list1.get(i); 	
							} else {
								ptr = null; 
							}

							if (k < list2.size()) {	
								ptr2 = list2.get(k);
							} else {
								ptr2 = null; 
							}
						} 
						// if ptr == null && ptr2 != null
						if (ptr2 != null && ptr == null) {
							temp.add(ptr2); 
							k++; 
							if (k < list2.size()) {
								ptr2 = list2.get(k); 
							} else {
								ptr2 = null; 
							}
						}
					}

					// if ptr2 == null && ptr != null
					if (ptr!= null && ptr2 == null) {
						temp.add(ptr); 
						i++; 
						if (i < list1.size()) {
							ptr = list1.get(i); 
						} else {
							ptr = null; 
						}
					}
				}
			} else if (list1.size() > list2.size()) { // if list 1 is longer than list 2 
				while (ptr != null) {
					while (ptr2 != null) {
						if (ptr.frequency > ptr2.frequency) { 
							temp.add(ptr); 
							i++; 
							if (i < list1.size()) {
								ptr = list1.get(i); 
							} else {
								ptr = null; 
							}
							//ptr = list1.get(i);
							//counter++; 
						} else if (ptr.frequency < ptr2.frequency) {
							temp.add(ptr2); 
							k++; 
							if (k < list2.size()) {
								ptr2 = list2.get(k); 
							} else {
								ptr2 = null; 
							}

						} else if (ptr.frequency == ptr2.frequency) { // takes away the duplicate of ptr and ptr2 by only adding ptr
							temp.add(ptr); 
							i++;
							k++;
							if (i < list1.size()) { 
								ptr = list1.get(i); 	
							} else {
								ptr = null; 
							}

							if (k < list2.size()) {	
								ptr2 = list2.get(k);
							} else {
								ptr2 = null; 
							}
						}
						// if ptr == null && ptr2 != null
						if (ptr2 != null && ptr == null) {
							temp.add(ptr2); 
							k++; 
							if (k < list2.size()) {
								ptr2 = list2.get(k); 
							} else {
								ptr2 = null; 
							}
						}
					}
					// if ptr2 == null && ptr != null
					if (ptr!= null && ptr2 == null) {
						temp.add(ptr); 
						i++; 
						if (i < list1.size()) {
							ptr = list1.get(i); 
						} else {
							ptr = null; 
						}
					}
				}
			} else if (list1.size() < list2.size()) { // if list 2 is longer than list 1 
				while (ptr != null) { // increment through the two lists, add all to temp and remove ptr and ptr2 duplicates
					while (ptr2 != null) {
						if (ptr.frequency > ptr2.frequency) { 
							temp.add(ptr); 
							i++; 
							if (i < list1.size()) {
								ptr = list1.get(i); 
							} else {
								ptr = null; 
							}
							//ptr = list1.get(i);
							//counter++; 
						} else if (ptr.frequency < ptr2.frequency) {
							temp.add(ptr2); 
							k++; 
							if (k < list2.size()) {
								ptr2 = list2.get(k); 
							} else {
								ptr2 = null; 
							}

						} else if (ptr.frequency == ptr2.frequency) { // takes away the duplicate of ptr and ptr2 by only adding ptr
							temp.add(ptr); 
							i++;
							k++;
							if (i < list1.size()) { 
								ptr = list1.get(i); 	
							} else {
								ptr = null; 
							}

							if (k < list2.size()) {	
								ptr2 = list2.get(k);
							} else {
								ptr2 = null; 
							}
						}
						// if ptr == null && ptr2 != null
						if (ptr2 != null && ptr == null) {
							temp.add(ptr2); 
							k++; 
							if (k < list2.size()) {
								ptr2 = list2.get(k); 
							} else {
								ptr2 = null; 
							}
						}
					}
					// if ptr2 == null && ptr != null
					if (ptr!= null && ptr2 == null) {
						temp.add(ptr); 
						i++; 
						if (i < list1.size()) {
							ptr = list1.get(i); 
						} else {
							ptr = null; 
						}
					}
				}

			}
		} // end of the && 
		
		// HOWEVER, could still be duplicates here, so have to remove them 
			ArrayList<Occurrence> remDupList = new ArrayList<Occurrence>(); 
			int j = 0; 
			while (temp.size() != 0) {
			
				Occurrence tempPtr = temp.get(j); 
				if (temp.size() == 1) {
					remDupList.add(tempPtr);
					temp.remove(j); 
				} else {
					if (temp.get(j).frequency == temp.get(j+1).frequency) {
						remDupList.add(tempPtr); 
					} else {
						remDupList.add(tempPtr); 
					}
					temp.remove(j); 
				}
			}
			
		// now, do while loop to get the first 5 documents from temp and put them into resultSet
			int count = 0;
			while (count < 5) {
				if (resultSet.contains(remDupList.get(count).document) && remDupList.get(count+1).document != remDupList.get(count).document ) {
					if (remDupList.size() == 1 || remDupList.size() == 2) { // if size is 1, and it is already in, just increment count 
						count++; 
					} else {
						resultSet.add(remDupList.get(count+1).document); 
						count++;	
					}	 
				
				} else {
						resultSet.add(remDupList.get(count).document); 
					count++;
				} 
			}
		return resultSet; 
	}
}
