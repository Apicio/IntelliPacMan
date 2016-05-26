// Author:	Renee L. Ramsey, William F. Klostermeyer
// Algorithms: An Undergraduate Course with Programming
// Program:     LinkList Class
// Date:	4/23/2002
//
// graphLinkList.java
// Graph LinkList class which has data and methods to work with a weighted or unweighted graph.
// Note: 	Unlike LinkListClass, this only generates undirected, unweighted graphs
//                 (though easy enough to change to generated others)
//		File should exist in same directory as program.
// Notes: 	The graph class takes two ints and a long as parameters:
//              first is number of vertices
//              second is edge probability (0-100) indicates the
//                    probability that an edge exists
//              third is the long seed for the random number generator
//                    use -1 to have the system clock used
// To run this program: > Call the class from another program.
//			  Example:  graph a = new randomgraphLinkList(12, 50, -1);
import java.io.*;	//for I/O
import java.util.Random;
///////////////////////////////////////////////////////////////////////////
public class randomgraphLinkList
{
   public int current_edge_weight;    		// used in next_neighbor
   private Link[] aGraph;				// Linked List to represent a graph
   private int nVerts;					// number of vertices
   private int x=0;			        	// row pointer
   private Link[] next;                         // array to track next neighbor

  public randomgraphLinkList(int a, int prob, long theseed) 
   {
      double b;			// declare & initialize variables
      int i, j, n;

      // more initializations
      n = a;					// initialize n to number of cols/rows
      nVerts = n;				// initialize number of vertices
      aGraph = new Link[nVerts];  		// make lists for each vertex
      next = new Link[nVerts];

      for(int e=0; e<nVerts; e++) 	
      {
       	aGraph[e] = null;                               // set to null
      }// end for

      for(i=0; i < nVerts; i++)				// initialize next neighbor
           next[i]=null;

      Random generator = new Random();
      Random generator2 = new Random(theseed);
      Random mygenerator; 

      if (theseed == -1)
        mygenerator = generator;
      else
        mygenerator = generator2;

      for(i=0; i < nVerts; i++) {
         for(j=0; j < nVerts; j++) {
              b = mygenerator.nextDouble() * 100;
              if (b <= prob) {
                     insertLink(i, j, 1);    // 1 is default edge weight
                     insertLink(j, i, 1);    // make it undirected                                    
              }
         }
      }

   }// end constructor


//------------------------------------------------------------------------
   public void insertLink(int x, int y, int w)		// insert a link
   {

     Link node = new Link(y,w);				// create a new link
     node.next = aGraph[x];   				// set node.next equal to array
     aGraph[x] = node;                                // set array equal to node

   }// end method insertLink()
// -------------------------------------------------------------
   public void display()                               	// display the Link List Graph
   {
    System.out.println("");

    for(int row=0; row<nVerts; row++)			// loop through the vertices
      {
        Link node = aGraph[row];			// set node

      	while(node != null)				// while node is not null
        {
      	  System.out.print(node.vertexnum + ":" + node.edgeweight + " "); // display it
      	  node = node.next;  				// set node to the next vertex

        }// end while

        System.out.println("");
      }// end for

   }// end method display()
//------------------------------------------------------------------------
   public int vertices()
   {

      return nVerts;					// return the number of vertices

   }// end method vertices()
//------------------------------------------------------------------------
   public int edgeLength(int row, int vertex)
   {
       Link node = aGraph[row];  			// set node

       while(node.vertexnum != vertex && node != null)  	// while node is not null and not equal to vertex
       {
          node = node.next; 				// set node equal to the next node

       }// end while

     	if(node == null)				// if node is null
     		return -1; 				// return a -1
     	else
     		return node.edgeweight;			// return the edge weight

   }// end method edgeLength()
//------------------------------------------------------------------------

   public int nextneighbor(int v)
   {
      if (next[v] == null)
         next[v] = aGraph[v];   		// reset to first neighbor in list
      else
         next[v] = next[v].next;

      if (next[v] == null) {
            current_edge_weight = -1;
            return -1; 				// -1 flags no more neighbors
         }
      else {
            current_edge_weight = next[v].edgeweight;
            return next[v].vertexnum;
           }

   }// end method nextneighbor()

//---------------------------------------------------------------------------
   public void resetnext()
   {
      for (int i=0; i < nVerts; i++)			// reset the array next to all -1's
         next[i] = null;

   }// end method resetnext()


}// end class graphLinkList
///////////////////////////////////////////////////////////////////////////
class Link
{
   public int vertexnum;           	  		// data item
   public int edgeweight;		  		// weight
   public Link next;              			// next link in list

//---------------------------------------------------------------------------
  public Link(int dd, int ww) 			// constructor
  {
      vertexnum = dd;       				// data item
      edgeweight = ww;                        	// weight

   }// end constructor

}// end class Link
////////////////////////////////////////////////////////////////////////////





