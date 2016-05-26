// Author:	Renee L. Ramsey
// Algorithms: An Undergraduate Course with Programming
// Program:     dfsWrapper.java
// Date:	5/12/2002
//
// dfsWrapper.java
//
// to run this program: >java dfsWrapper
// note: the graph class must reside in the same directory.
// 	Because this is a recursive program, Java prefers that DFS be a method in
//	the graph class.  Howeverm we present independently for sake of exposition.
import java.io.*;	//for I/O
///////////////////////////////////////////////////////////////////////////
public class dfsTest
{
    public static int time;
    public static int nVerts;				// number of vertices
    public static int [] color;
    public static int [] pie;
    public static int [] d;				// discovery time
    public static int [] f;                             // finish time

    public static void main(String[] args) throws IOException
    {

	// create a graph object using adjacency matrix
     	graph G = new graph("graph.txt");

	// If you want to use a Link List graph, uncomment this out and comment
	// out the graph object above
	//graphLinkList G = new graphLinkList("unweighted.txt","u");

   	// declare variables
	int u;
	int WHITE=0;

        nVerts = G.vertices(); 			// get number of vertices in the graph class

        // initialize arrays
        color = new int[nVerts];
        pie = new int[nVerts];
        d = new int[nVerts];
        f = new int[nVerts];

        for(u=0; u<nVerts; u++) 		// initializations
        {
        	color[u] = WHITE;;
        	pie[u] = -1;

        }// end for

        time = 0;

        for(u=0; u<nVerts; u++)
        {
        	if(color[u] == WHITE)
                	DFS_Visit(G,u);

        }// end for

         System.out.println("");    		// display the array d
    	for(int row=0; row<nVerts; row++)
      	{
      		System.out.print(d[row] + " ");
      	  	System.out.println("");
      	}// end for

    }// end dfs method

//--------------------------------------------------------------
// If you want to use a Link List graph, you must change the datatype to be graphLinkList
   public static void DFS_Visit(graph G,int u)
   {
   	// declare variables
   	int WHITE=0, GRAY=1, BLACK=2, v;

	color[u] = GRAY;  			// white vertex u has just been discovered

	d[u] = time;
	time++;

        v = G.nextneighbor(u); 			// get the next neighbor of u

        while(v != -1)
        {
        	if(color[v] == WHITE)
                {
                	pie[v] = u;
                	DFS_Visit(G,v);

                }// end if

                v = G.nextneighbor(u);		// get the next neighbor of u

        }// end while

        color[u] = BLACK;			// blackened u, it is finished

        f[u] = time;
        time++;


   }// end ()
//--------------------------------------------------------------
}//end class dfsWrapper
///////////////////////////////////////////////////////////////////////////
