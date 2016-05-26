// Author:	Renee L. Ramsey
// Algorithms: An Undergraduate Course with Programming
// Program:     bfs.java
// Date:	5/12/2002
//
// bfs.java
//
// to run this program: >java bfs
// note the graph class must reside in the same directory.
import java.io.*;	//for I/O
///////////////////////////////////////////////////////////////////////////
public class bfs
{
    public static void main(String[] args) throws IOException
    {
	// create a graph object using adjacency matrix
     	graph G = new graph("graph.txt");

	// If you want to use a Link List graph, uncomment this out and comment
	// out the graph object above
	//graphLinkList G = new graphLinkList("unweighted.txt","u");

        // call BFS function
        bfs_function(G,0);


    }// end main
//--------------------------------------------------------------
// If you want to use a Link List graph, you must change the datatype to be graphLinkList
   public static void bfs_function(graph G, int s)	// s is the index of the starting vertex
   {
   	// declare variables
	int nVerts, u, v, head, tail;
	int [] color;
	int [] d;
	int [] pie;
	int [] Q;
	int WHITE=0, GRAY=1, BLACK=2;

	nVerts = G.vertices(); 				// get number of vertices in the graph class
	head = 0;					// initialize head
	tail = 1;					// initialize tail

        // initialize arrays
        color = new int[nVerts];
        d = new int[nVerts];
        pie = new int[nVerts];
        Q = new int[nVerts];

        for(u=0; u<nVerts && u!=s; u++) 		// initialization
        {
        	color[u] = WHITE;
        	d[u] = -1;
        	pie[u] = -1;

        }// end for

        color[s] = GRAY;
        d[s] = 0;
        pie[s] = -1;
        Q[0] = s;

        while(head != tail) 			// if head and tail are not equal, it's not empty
        {
        	u = Q[head];			// delete the head
                v = G.nextneighbor(u);

                while(v != -1)  		// for each neighbor of u
                {
                	if(color[v] == WHITE) 	// if color v is white
                	{
                	 	color[v] = GRAY; // set color to gray
                	 	d[v] = d[u] + 1;
                	 	pie[v] = u;
                	 	Q[tail] = v; 	// put it at the end
                	 	tail++;         // increment the tail

                	}// end if
                          // System.out.println("v : " + v);
                	v = G.nextneighbor(u);  // get the next neighbor of u

                }// end while

                color[u] = BLACK; 		// set color u to black
                head++;				// delete u from Q

        }// end while

        System.out.println("");    				// display the array d
    	for(int row=0; row<nVerts; row++)
      	{
      		System.out.print(d[row] + " ");
      	  	System.out.println("");
      	}// end for

   }// end bfs_function()
//--------------------------------------------------------------
}//end class test
///////////////////////////////////////////////////////////////////////////
