// Author:	Renee L. Ramsey, William F. Klostermeyer
// Algorithms: An Undergraduate Course with Programming
// Program:     LinkList Class
// Date:	4/23/2002
//
// graphLinkList.java
// Graph LinkList class which has data and methods to work with a weighted or unweighted graph.
// Note: 	The Graph LinkList class takes the filename and whether the graph is weighted ("W") or
//		unweighted ("u") as the input parameters.  Unweighted graphs will have edge weights of 1.
//		File should exist in same directory as program.
// File format: The first line of the file is a byte n (number of vertices).
// 		    Then there will be n lines:
//              For unweighted graph, a line contains ints separated by
//                  spaces, the ints on line "i" are th neighbors of
//                  vertex i
//              For weighted graph, a line contains pairs of ints a,b
//                  (pairs are separated by spaces), where on line "i"
//                  a pair a, b means vertex i is adjacent to vertex b
//                  by edge ia (arc i->a) of weight b
// to run this program: > Call the class from another program.
//			  Example:  graphLinkList b = new graphLinkList("unweighted.txt","u");
import java.io.*;	//for I/O
///////////////////////////////////////////////////////////////////////////
public class graphLinkList
{
   public int current_edge_weight;    		// used in next_neighbor
   private Link[] aGraph;			// Linked List to represent a graph
   private int nVerts;				// number of vertices
   private int x=0;			        // row pointer
   private Link[] next;                         // array to track next neighbor

  public graphLinkList(String sFileName,String sGraphType) throws IOException	// constructor
  {

      int a=0, c=0, b=0, CR=13, y=0, iWeight=0, cChar=0;  // declare & initialize variables
      boolean EOF = false;
      FileInputStream input = null;
      char ch=0;
      int i;

      try
      {
         input = new FileInputStream(sFileName);	// initialize input and open the filename..
      }                                                 // ..provided in the first command line arg

      catch (FileNotFoundException e) {                 // catch all kinds of errors with the file
      System.out.println("Could not open file " + sFileName + " , exiting...");
      System.exit(0); }

      catch(IOException e) {
      System.out.println("Error opening file" + sFileName + " , exiting...");
      System.exit(0); }

      catch (Exception e) {
      System.out.println("Unexpected error: " + e + " , exiting...");
      System.exit(0); }

      if(sGraphType == "u")				// if it's an unweighted graph
      	iWeight = 1;                                    // all edge weights are 1

      while(a != CR)
      {
         try
         {
            a = input.read();				// read the next byte in the file

            if(a == -1)					// if there's nothing in the file, we reached EOF..
            {                                           // ..display error message and exit
               System.out.println("Encountered EOF, exiting...");
               System.exit(0);

            }
            else if(a != CR) 				// if a is not equal to carriage return
               b += a;                                  // add byte to variable b
         }

      catch(IOException e) {   				// catch all errors when reading the file
      System.out.println("Done Reading: " + e + " , exiting...");
      EOF = true; }

      catch (Exception e) {
      System.out.println("Unexpected error: " + e + " , exiting...");
      System.exit(0); }

      }// end while

      ch = (char) b;					// convert byte b to a char
      a = Character.getNumericValue(ch);             	// convert ch to an integer

      // more initializations

      nVerts = a;					// initialize number of vertices
      aGraph = new Link[nVerts];  			// make lists for each vertex
      next = new Link[nVerts];

      for(int e=0; e<nVerts; e++) 			// loop through array
      {
       	aGraph[e] = null;                               // set to null
      }// end for

      while(!EOF)					// while not EOF
      {
         try
         {
            a = input.read();				// read the next byte in the file

            switch(a)
            {
              case 13 :          			// carriage return
                 ch = (char) c;                      	// convert byte to a character
                 c = Character.getNumericValue(ch);     // convert character to an integer 		                                                        // 1 cuz it's unweighted

                 if(sGraphType == "w" && y == 0)
                 {
                    cChar = c;                          // save integer in a variable
                    y = 1;       			// set y to 1
                 }
                 else if(sGraphType == "w" && y == 1)
                 {
                    insertLink(x,cChar,c);        	// insert link for the weighted graph
                    y = 0;                              // reset y to zero
                 }
                 else
                 	insertLink(x,c,iWeight);        // insert link

                 x++;  					// increment row
                 c = 0;                              	// reset c
                 break;

              case 32 :   				// white space
                 ch = (char) c;                      	// convert byte to a character
                 c = Character.getNumericValue(ch);     // convert character to an integer

                 if(sGraphType == "w" && y == 0)
                 {
                    cChar = c;                          // save integer in a variable
                    y = 1;       			// set y to 1
                 }
                 else if(sGraphType == "w" && y == 1)
                 {
                    insertLink(x,cChar,c);        	// insert link for the weighted graph
                    y = 0;                              // reset y to zero
                 }
                 else
                 	insertLink(x,c,iWeight);        // insert link

                 c = 0;     				// reset c
                 break;

              case -1 :   				// EOF
                 ch = (char) c;                      	// convert byte to a character
                 c = Character.getNumericValue(ch);     // convert character to an integer

                 if(sGraphType == "w" && y == 0)
                 {
                    cChar = c;                          // save integer in a variable
                    y = 1;       			// set y to 1
                 }
                 else if(sGraphType == "w" && y == 1)
                 {
                    insertLink(x,cChar,c);        	// insert link for the weighted graph
                    y = 0;                              // reset y to zero
                 }
                 else
                 	insertLink(x,c,iWeight);        // insert link

                 EOF = true;                           	// set EOF
                 break;

              case 10 : 				// Line feed
                 break;                                 // don't do anything

              default :
                 c += a;                                // add byte to variable c
                 break;

            }// end switch

         }// end try

         catch(IOException e) {                         // catch all errors when reading the file
         System.out.println("Done Reading: " + e + " , exiting...");
         EOF = true; }

         catch (Exception e) {
         System.out.println("Unexpected error: " + e + " , exiting...");
         System.exit(0); }


      }// end while

      input.close();					// close the input file

      for(i=0; i < nVerts; i++)				// initialize next neighbor
           next[i]=null;

   }// end constructor

//------------------------------------------------------------------------
   public void insertLink(int x, int y, int w)		// insert a link
   {

     Link node = new Link(y,w);				// create a new link
     node.next = aGraph[x];   				// set node.next equal to array
     aGraph[x] = node;                                  // set array equal to node

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





