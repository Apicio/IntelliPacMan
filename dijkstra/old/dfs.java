// Author:	Renee L. Ramsey
// Algorithms: An Undergraduate Course with Programming
// Program:     dfs.java
// Date:	5/12/2002
//
// dfs.java
//
// to run this program: >java dfs
import java.io.*;	//for I/O
///////////////////////////////////////////////////////////////////////////
public class dfs
{
    public static void main(String[] args) throws IOException
    {
	// create a graph object using adjacency matrix
     	graph G = new graph("graph.txt");

        // call dfsGraph class
        dfsGraph A = new dfsGraph(G);


    }// end main
}//end class dfs
//--------------------------------------------------------------
class dfsGraph
{
   private graph G;
   private int nVerts, time;				// number of vertices, time
   private int [] color;
   private int [] pie;

   public dfsGraph(graph G)
   {
   	// declare variables
	int u;
	int WHITE=0;

        nVerts = G.vertices(); 				// get number of vertices in the graph class

        // initialize arrays
        color = new int[nVerts];
        pie = new int[nVerts];

        for(u=0; u<nVerts; u++)
        {
        	color[u] = WHITE;;
        	pie[u] = -1;

        }// end for

        time = 0;

        for(u=0; u<nVerts; u++)
        {
        	if(color[u] == WHITE)
                	DFS_Visit(u);

        }// end for

    }// end bfs_function()

//--------------------------------------------------------------
   public void DFS_Visit(int u)
   {
   	// declare variables
   	int WHITE=0, GRAY=1, BLACK=2, v;
   	int [] d;
	int [] f;

	// initialize arrays
        d = new int[nVerts];
        f = new int[nVerts];

	color[u] = GRAY;  				// white vertex u has just been discovered

	d[u] = time + 1;

        v = G.nextneighbor(u);

        while(v != -1)
        {
        	if(color[v] == WHITE)
                {
                	pie[v] = u;
                	DFS_Visit(v);

                }// end if

                v = G.nextneighbor(u);

        }// end while

        color[u] = BLACK;					// blackened u, it is finished

        f[u] = time + 1;

        System.out.println("");    				// display the array d
    	for(int row=0; row<nVerts; row++)
      	{
      		System.out.print(d[row] + " ");
      	  	System.out.println("");
      	}// end for

   }// end dfsGraph()
//--------------------------------------------------------------
}//end class dfs
///////////////////////////////////////////////////////////////////////////
