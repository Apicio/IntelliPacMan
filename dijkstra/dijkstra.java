// Author:	Renee L. Ramsey, W. Klostermeyer
// Algorithms: An Undergraduate Course with Programming
// Program:     dijkstra.java
// Date:	5/12/2002, 11/14/02
//
// dijkstra.java
//
// to run this program: >java dijkstra
// note the PriorityQueue class must reside in the same directory.
import java.io.*;	//for I/O
///////////////////////////////////////////////////////////////////////////
public class dijkstra
   {
    public static void main(String[] args) throws IOException
    {
	// create a graph object using adjacency matrix
     	graph G = new graph("graph.txt");

     	// If you want to use a Link List graph, uncomment this out and comment
	// out the graph object above
	// graphLinkList G = new graphLinkList("unweighted.txt","u");

	// create a randomgraph object using adjacency matrix
     	// randomgraph G = new randomgraph(20, 20, 20);


        // call dijkstra function
        dijkstra_function(G,0);


    }// end main
//--------------------------------------------------------------

// If you want to use a Linked List graph, 
// you must change the datatype to be graphLinkList
// If you want to use randomgraph, you must change datatype to be randomgraph
// You could probably re-write graphLinkList and randomgraph to return a graph object,
// but I am too lazy.

   public static void dijkstra_function(graph G, int s) throws IOException
   {                  					// s is the index of the starting vertex
   	// declare variables
	int nVerts, u, v;
	int [] dist;

        nVerts = G.vertices(); 			// get number of vertices in the graph class

        // initialize array
        dist = new int[nVerts];

        for(v=0; v<nVerts; v++) 			// initializations
        {
        	dist[v] = 99999; 				// 99999 represents infinity

        }// end for

        dist[s] = 0;

        PriorityQueue Q = new PriorityQueue(dist);  		

        while(Q.Empty() == 0) 			// if heap is not empty
        {

                u = Q.Delete_root();
                v = G.nextneighbor(u);

                while(v != -1)  			// for each neighbor of u
                {
                	if(dist[v] > dist[u] + G.edgeLength(u,v)) {
                		dist[v] = dist[u] + G.edgeLength(u,v);
				Q.Update(v, dist[v]);
                         }

                	v = G.nextneighbor(u);  	// get the next neighbor of u

                }// end while

        }// end while

        System.out.println("");    			// display the array dist
        for(int row=0; row<nVerts; row++)
      	{
      		System.out.print(dist[row] + " ");
      	  	System.out.println("");
      	}// end for

   }// end bfs_function()
//--------------------------------------------------------------
}//end class dijkstra
///////////////////////////////////////////////////////////////////////////
