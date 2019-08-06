package structures;

import java.util.*;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 * 
 */
public class Tree {
	
	/**
	 * Root node
	 */
	TagNode root=null;
	
	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;
	
	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}
	
	/**
	 * Builds the DOM tree from input HTML file, through scanner passed
	 * in to the constructor and stored in the sc field of this object. 
	 * 
	 * The root of the tree that is built is referenced by the root field of this object.
	 */
	public void build() {
		/** COMPLETE THIS METHOD **/
		
		 
		Stack<TagNode> words	 = new Stack<TagNode>(); 
		
	//	TagNode front = null; 
		TagNode ptr = null; 
		 
		
		String currentString = ""; 
		
		while (sc.hasNextLine()) {
			currentString = sc.nextLine(); 
			
			if (currentString.substring(0,1).equals("<") && !currentString.substring(1,2).equals("/")) { // if it is opening like '<' or '>' with no '/'
				
				currentString = currentString.substring(1, currentString.length()-1); // take away the < and > 
				
				if (root == null) {
					 
					root = new TagNode(currentString, null, null); // make a new node for the root of the tree
					words.push(root); 
					ptr = root; 
				
				} else if (ptr.firstChild == null) {
					
					ptr.firstChild = new TagNode(currentString, null, null); // make a new node for the first child 
					words.push(ptr.firstChild); 
					ptr = words.peek(); 
				
				} else {
					
					ptr = ptr.firstChild; 					
					
					while (ptr.sibling != null) {
						ptr = ptr.sibling; // keep traversing through tree
					}
					
					ptr.sibling = new TagNode(currentString, null, null); // make a new node for the sibling 
					words.push(ptr.sibling);
					ptr = words.peek(); 
				}
				
				
			} else if (currentString.substring(0,1).equals("<") && currentString.substring(1,2).equals("/")) { // if it is a closing '/'
				
				ptr = words.pop(); 
				
				if (words.isEmpty() == true) {
					continue; 
				}
				ptr = words.peek(); 
			
			} else if (!currentString.substring(0,1).equals("<")) { // if it is not a tag, then it is a word
				
				if (ptr.firstChild == null) {
					ptr.firstChild = new TagNode(currentString, null, null); 
				 
				} else {
					ptr = ptr.firstChild; 
				
					while (ptr.sibling != null) {
						ptr = ptr.sibling; 
					}
					ptr.sibling = new TagNode (currentString, null, null); 
					ptr = words.peek(); 
				}
			}	
		}
	}

	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) {
		/** COMPLETE THIS METHOD **/
		replace(oldTag, newTag, root); 
	}
	// Helper method for replaceTag
	private static TagNode replace(String oldTag, String newTag, TagNode front) { //private replace(String oldTag, String newTag, TagNode front) {

		for (TagNode ptr=front; ptr != null; ptr=ptr.sibling) {
			if (front == null) {
				return front; //return; 
			}
			
			if (ptr.tag.equals(oldTag)) {
				ptr.tag = newTag; 
			} 
			if (ptr.firstChild != null) {
				replace(oldTag, newTag, ptr.firstChild);
			}
		}	
		return front; 
	}

	
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	
	// actual bolding here 
	public void boldRow(int row) {
		/** COMPLETE THIS METHOD **/
		TagNode tableNode = bold(row, root); 
		//System.out.println(tableNode.toString());
		int counter = 1; 
		
		tableNode = tableNode.firstChild; 
		
		while (counter != row) {
			tableNode = tableNode.sibling; 
			counter++; 
		}
		tableNode = tableNode.firstChild; 
		
		for (TagNode ptr = tableNode; ptr != null; ptr = ptr.sibling ) {
			if (counter == row) {
			TagNode b = new TagNode ("b", ptr.firstChild, null);
			ptr.firstChild = b; 
			}
		}
	}
	// helper for bold row // find table, while to find row, go to first child until no more siblings and add a bold tag to each one 
	private static TagNode bold(int row, TagNode root) {

		if (root == null) {
			return null;
		}
		if (root.tag.equals("table")) {
			return root; 
		}
		
		TagNode sibling = bold(row, root.sibling); 
		TagNode firstChild = bold(row, root.firstChild); 
		
		if (sibling != null) {
			return sibling; 
		} else if (firstChild != null) {
			return firstChild; 
		} else {
			return root; 
		}	
	}
	
	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li tags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	public void removeTag(String tag) {
		/** COMPLETE THIS METHOD **/
		TagNode prev = null; 
		
		if (tag.equals("em") || tag.equals("b") || tag.equals("p")) {
			deleteCategory1(tag, root, prev); 
		} else if (tag.equals("ol") || tag.equals("ul")) {
			deleteCategory2(tag, root, prev); 
		}
	}
	
	/* 
	 * Helper for removeTag Category 1
	 */
	
	private static TagNode deleteCategory1(String tag, TagNode root, TagNode previous) {
		if (root == null) {
			return root; 
		}
		for (TagNode ptr=root; ptr != null; ptr=ptr.sibling) {
			TagNode prev = previous; 
			
			
			if (ptr.tag.equals("em") || ptr.tag.equals("b") || ptr.tag.equals("p")){ // deletes the em, b and p tags
		
				if (ptr.tag.equals(tag)) { // if the pointer's tag is the target tag
					TagNode temp = new TagNode(ptr.tag,ptr.firstChild,ptr.sibling);				
					
					if (prev != null) { // if the prev is not null, do the deletion 
						//ptr = temp.firstChild; 
						//prev.firstChild = ptr; 
						ptr = ptr.firstChild;
						prev.sibling = ptr; 
						ptr.sibling = temp.sibling; 
					} else { // if the prev is null, do the deletion without prev
						ptr = ptr.firstChild;
						ptr.sibling = temp.sibling; 
					}
				}
			}
			prev = ptr; 
			if (ptr.firstChild != null) {
				deleteCategory1(tag, ptr.firstChild, prev); 
			} else {
				deleteCategory1(tag, ptr.sibling, prev); 
			}
		}
		return root; 
	}
	
	/* 
	 * Helper for removeTag Category 2
	 */
	
	private static TagNode deleteCategory2(String tag, TagNode root, TagNode previous) {
		if (root == null) {
			return root; 
		}
		for (TagNode ptr=root; ptr != null; ptr=ptr.sibling) {
			TagNode prev = previous; 
			 
			if (ptr.tag.equals("ol")) {
				
				if (ptr.tag.equals(tag)) {
					TagNode temp = new TagNode(ptr.tag,ptr.firstChild,ptr.sibling);
					
					if (prev!= null) { // if the prev is not null, do the deletion 
						ptr = temp.firstChild; 
						prev.firstChild = ptr; 
					}
					while (ptr != null) { // if the tag is an li, now we delete it 
						if (ptr.tag.equals("li")) {
							ptr.tag = "p";
						}
						if (ptr.sibling == null) {
							ptr.sibling = temp.sibling;
							break; 
						}
						ptr = ptr.sibling; 
					}
				}	
			} else if (ptr.tag.equals("ul")) { // olSubTree.firstChild = prev.firstChild, prev.firstchild = olSubTree 
				
				if (ptr.tag.equals(tag)) {
					TagNode temp = new TagNode(ptr.tag,ptr.firstChild,ptr.sibling);
					
					if (prev!= null) { // if the prev is not null, do the deletion 
						ptr = temp.firstChild; 
						prev.firstChild.sibling = ptr;  //this is what needed to be changed 
					}
					
					while (ptr != null) { // if the tag is an li, now we delete it 
						if (ptr.tag.equals("li")) {
							ptr.tag = "p";
						}
						if (ptr.sibling == null) {
							ptr.sibling = temp.sibling;
							break; 
						}
						ptr = ptr.sibling; 
					}
				} 
			}
			prev = ptr; 
			if (ptr.firstChild != null) {
				deleteCategory2(tag, ptr.firstChild, prev); 
			} else {
				deleteCategory2(tag, ptr.sibling, prev); 
			}

		}
		
		return root; 
	}
	
	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */
	public void addTag(String word, String tag) {
		/** COMPLETE THIS METHOD **/
		TagNode prev = null; 
		//add(word, tag, root, prev); 
		
		if (tag.equals("em") || tag.equals("b")) {
			root = add(word, tag, root, prev); 
		}
	}
	private static TagNode add(String word, String tag, TagNode root, TagNode previous) {
		TagNode prev = previous;
		if (root.sibling != null) { // have to check siblings first because if you go firstChild first then there could be siblings of those nodes that wont be checked
			// check siblings first 
			prev = root; 
			root.sibling = add(word, tag, root.sibling, prev); 
		}
		
		if (root.firstChild != null) {
			// now check firstChild 
			prev = root; 
			root.firstChild = add(word, tag, root.firstChild, prev); 
		}
		
		if (root.tag.contains(word)) {

			if (root.tag.equals(word)) { // if the tag is exactly the word
				TagNode temp = new TagNode (tag, root, root.sibling); 
				// now set the sibling to null to replace
				root.sibling = null; 
				// return the new node, that has the rest of the LL attached to it 
				return temp; 
			} else { // now we have to split it into different parts 
				String[] str = root.tag.split(" ");  // split it based on spaces 
				//String newString = ""; 
				 for (int i = 0; i < str.length; i++) {
					 
					 if (str[i].equals(word) && str[i] == str[0]) { // if the target is at 0 
						 TagNode temp = new TagNode (tag, root, root.sibling); 
						 TagNode target = new TagNode (str[i], null, null); 
	
						// string concatenation  
						 String combined = ""; 
						 int k = 1; 
						 while (str[k] != str[str.length-1]) {
							 k++; 
						 }
						 k++; 
						 for (int j = 1; j < k; j++) {
							 if (combined != "") {
								 combined =  combined + " " + str[j]; 
							 } else {
								 combined = str[j]; 
							 }
						 }

						 TagNode newWord = new TagNode (combined, null, null); 
						 
						 while (root.sibling != null) {
							 root = root.sibling; 
						 }
						 root.sibling = temp; 
						 temp.firstChild = target; 
						 prev.firstChild = temp; // just added it
						 temp.sibling = newWord; 
						 
						 return temp; 
						 
					 } else if (str[i].equals(word) && str[i] == str[str.length-1]) { // if target is at length-1
						 TagNode temp = new TagNode (tag, root, root.sibling); 
						 TagNode target = new TagNode (str[i], null, null); 
						 
						// string concatenation 
						 String combined = ""; 
						 int k = 0; 
						 while (str[k] != str[str.length-1]) {
							 k++; 
						 }
						 for (int j = 0; j < k; j++) {
							 if (combined != "") {
								 combined = combined + " " + str[j];
							 } else {
								 combined = str[j];
							 }
						 }
						 TagNode newWord = new TagNode (combined, null, null); 
						 
						 while (root.sibling != null) {
							 root = root.sibling; 
						 }
						 prev.firstChild = newWord; 
						 newWord.sibling = temp; 
						 temp.firstChild = target; 
						 
						 return newWord; 
					 
					 } else if (str[0].equals(word) == false && str[str.length-1].equals(word) == false && str[i].equals(word)) {
						 TagNode temp = new TagNode (tag, root, root.sibling); 
						 TagNode target = new TagNode (str[i], null, null); 
						 
						// string concatenation for front part of the String 0,i (FRONT)
						 String combined = ""; 
						 int k = 0; 
						 while (str[k] != str[i]) {
							 k++; 
						 }
						 for (int j = 0; j < k; j++) {
							 if (combined != "") {
								 combined = combined + " " + str[j];
							 } else {
								 combined = str[j];
							 }
						 }
						 TagNode front = new TagNode (combined, null, null); 
						 
						 // string concatenation for back part of string i, length-1 (REAR) 
						 String combinedRear = ""; 
						 k = i+1;  
						 while (str[k] != str[str.length-1]) {
							 k++; 
						 }
						 k++; 
						 for (int j = i+1; j < k; j++) {
							 if (combinedRear != "") {
								 combinedRear =  combinedRear + " " + str[j]; 
							 } else {
								 combinedRear = str[j]; 
							 }
						 }

						 TagNode rear = new TagNode (combinedRear, null, null); 
						 
						 // for the front 
						 prev.firstChild = front; 
						 front.sibling = temp; 
						 temp.firstChild = target;
						 
						 // for the rear
						 temp.sibling = rear; 
						 
						 return front; 
					 }
				 }
			}
		}
		return root; 
	}
	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines. 
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}
	
	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}
	
	/**
	 * Prints the DOM tree. 
	 *
	 */
	public void print() {
		print(root, 1);
	}
	
	private void print(TagNode root, int level) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			for (int i=0; i < level-1; i++) {
				System.out.print("      ");
			};
			if (root != this.root) {
				System.out.print("|---- ");
			} else {
				System.out.print("      ");
			}
			System.out.println(ptr.tag);
			if (ptr.firstChild != null) {
				print(ptr.firstChild, level+1);
			}
		}
	}
}
