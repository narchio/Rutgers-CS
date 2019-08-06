package apps;

import structures.*;
import java.util.ArrayList;

public class MST {
	
	/**
	 * Initializes the algorithm by building single-vertex partial trees
	 * 
	 * @param graph Graph for which the MST is to be found
	 * @return The initial partial tree list
	 */
	public static PartialTreeList initialize(Graph graph) {

		/////* COMPLETE THIS METHOD*/////
		
		// Create an empty list L of partial trees
		PartialTreeList L = new PartialTreeList(); 
		
		// Separately for each vertex v in the graph:
		 
		for (int k = 0; k < graph.vertices.length; k++) { // iterate through the list of vertices 
			
			// 1. Create a partial tree T containing only v.
			Vertex v = graph.vertices[k]; 
			PartialTree T = new PartialTree(graph.vertices[k]); 
			
			// 2. Mark v as belonging to T (this will be implemented in a particular way in the code).
			v.parent = T.getRoot();  
			
			// 3. Create a priority queue (heap) P and associate it with T.
			MinHeap<PartialTree.Arc> P = new MinHeap<PartialTree.Arc>(); // this is how T relates to the heap
			
			while (v.neighbors != null) { // iterates through v's neighbors 
				// 4. Insert all of the arcs (edges) connected to v into P. The lower the weight on an arc, the higher its priority.
				PartialTree.Arc temp = new PartialTree.Arc(graph.vertices[k], v.neighbors.vertex, v.neighbors.weight); 
				// now put the edges into the MinHeap
				P.insert(temp);
				// increment the neighbors
				v.neighbors = v.neighbors.next; 
			}
			
			// 5. Add the partial tree T to the list L
			T.getArcs().merge(P);
			// now, finally add it to list L 
			L.append(T);	
		}
		return L;
	}

	/**
	 * Executes the algorithm on a graph, starting with the initial partial tree list
	 * 
	 * @param ptlist Initial partial tree list
	 * @return Array list of all arcs that are in the MST - sequence of arcs is irrelevant
	 */
	public static ArrayList<PartialTree.Arc> execute(PartialTreeList ptlist) {
		
	////	/* COMPLETE THIS METHOD */////
		
		ArrayList<PartialTree.Arc> answer = new ArrayList<PartialTree.Arc>(); 
		
		while (ptlist.size() > 1) { // while the size is not 1 or 0
			//Remove the first partial tree PTX from L (ptList). Let PQX be PTX's priority queue.
				//T1. Vertices: A  P1: (A C 1), (A D 3), (A B 4)
			PartialTree temp = ptlist.remove(); 
			
			//Remove the highest-priority arc from PQX. Say this arc is α. Let v1 and v2 be the 
			//two vertices connected by α, where v1 belongs to PTX.
				//α: (A C 1)    v1: A   v2: C
			PartialTree.Arc tempArc = temp.getArcs().deleteMin(); 
			 
			//If v2 also belongs to PTX, go back to Step 4 and pick the next highest priority arc, otherwise continue to the next step.
				//C belongs to a different partial tree (T3) than A, continue to the next step.
			Vertex ver2 = tempArc.v2;
			
			//Report α - this is a component of the minimum spanning tree.
				//(A C 1) is a component of the MST
			while (temp.getArcs().isEmpty() == false) {
				
				//Find the partial tree PTY to which v2 belongs. Remove PTY from the partial tree list L. Let PQY be PTY's priority queue.
					//T3. Vertices: C  PQ: (C A 1), (C E 2), (C D 4), (C B 5)
					if (temp.getRoot().equals(ver2.getRoot())) {
						// delete the tree from the temp
						tempArc = temp.getArcs().deleteMin(); 
						// update the vertex
						ver2 = tempArc.v2; 
					} else {
						// if it is not equal, have to get out of loop
						break; 
					}
			}	
 	
	/*Combine PTX and PTY. This includes merging the priority queues PQX and PQY into a single priority queue. 
	 * Append the resulting tree to the end of L.
 		T2. Vertices: B    PQ: (B D 3), (B A 4), (B C 5)
 		T4. Vertices: D    PQ: (D E 1), (D A 3), (D B 3), (D C 4)
 		T5. Vertices: E    PQ: (E D 1), (E C 2)
		T13. Vertices: A C  PQ: (C A 1), (C E 2), (A D 3), (A B 4), (C D 4), (C B 5)
	*/
		// this is PTY
		PartialTree combined = ptlist.removeTreeContaining(ver2); 
		
		// now, merge the two 
		temp.merge(combined); 
		// append L 
		ptlist.append(temp);
		// add arcs to answer 
		answer.add(tempArc); 
	  	}	
	//If there is more than one tree in L, go to Step 3. ----- handled by the check in the while loop
		return answer;
	}
}
