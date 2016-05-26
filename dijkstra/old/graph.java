// Author:	Renee L. Ramsey, William F. Klostermeyer
// Algorithms: An Undergraduate Course with Programming
// Program:     Graph Class
// Date:	2/23/2002
//
// graph.java
// Graph class which has data and methods to work with a graph.
// Note: 	The graph class takes the filename as an input parameter.
//		File should exist in same directory as program.
// File format: The first line of the file is an integer n (number of vertices).
// 		Then there will be n lines each with n integers on each line
//              with a space separating each integer 
//              (each integer is edge weight).
//              Must be no spaces or carriage return following last value 
//              in file.
//
// to run this program: > Call the class from another program.
//			  Example:  graph a = new graph("graph.txt");

import java.io.*;	//for I/O
///////////////////////////////////////////////////////////////////////////
public class graph
{
   public  int current_edge_weight;                     // used in
							//    next_neighbor
   private int[][] M;					// adjacency matrix to represent a graph
   private int n;					// number of cols/rows
   private int nVerts;					// number of vertices
   private int x;					// row pointer
   private int y;                                       // column pointer
   private int[] next;                                  // array to track next neighbor

   public graph(String sFileName) throws IOException	// constructor
   {

      int a=0, c=0, b=0, CR=13;				// declare & initialize variables
      boolean EOF = false;
      FileInputStream input = null;
      char ch=0;
      int i;

      try
      {
         input = new FileInputStream(sFileName);	// initialize input and open the filename
      }

      catch (FileNotFoundException e) {                	// catch all kinds of errors with the file
      System.out.println("Could not open file " + sFileName + " , exiting...");
      System.exit(0); }

      catch(IOException e) {
      System.out.println("Error opening file" + sFileName + " , exiting...");
      System.exit(0); }

      catch (Exception e) {
      System.out.println("Unexpected error: " + e + " , exiting...");
      System.exit(0); }

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
      n = a;						// initialize n to number of cols/rows
      x = 0; 						// initialize number of rows to zero
      y = 0;                                            // initialize number of columns to zero
      M = new int[n][n];             			// initialize 2D array to all zeros
      nVerts = n;					// initialize number of vertices
      next = new int[n];                  		// next neighbor for each vertex

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
                 insertVertex(c,x,y);                   // insert the vertex into the graph
                 x++;  					// increment row
                 y = 0;					// reset column
                 c = 0;                              	// reset c
                 break;

              case 32 :   				// white space
                 ch = (char) c;                      	// convert byte to a character
                 c = Character.getNumericValue(ch);     // convert character to an integer
                 insertVertex(c,x,y);                   // insert the vertex into the graph
                 y++;					// increment column
                 c = 0;     				// reset c
                 break;

              case -1 :   				// EOF
                 ch = (char) c;                      	// convert byte to a character
                 c = Character.getNumericValue(ch);     // convert character to an integer
                 insertVertex(c,x,y);                   // insert the vertex into the graph
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

      input.close();				// close the input file

      for(i=0; i < nVerts; i++)			// initialize next neighbor
           next[i]=-1;

   }// end constructor
//------------------------------------------------------------------------
   public void insertVertex(int a, int x, int y)	// insert a vertex
   {
      if(x == y)					// if M[i][i]
      {
         if(a != 0)                                     // if value if not zero, display error and exit
         {
            System.out.println("Cannot initialize graph, M[i][i] must be zero!  Exiting...");
            System.exit(0);
         }// end if
      }// end outer if

      M[x][y] = a;					// insert vertex into matrix M

   }// end method insertVertex()
//------------------------------------------------------------------------
   public void display()
   {
    System.out.println("");    				// display the array
    for(int row=0; row<n; row++)
      {
      	for(int col=0; col<n; col++)
      	  System.out.print(M[row][col] + " ");
      	  System.out.println("");
      }// end for
   }// end method display()
//------------------------------------------------------------------------
   public int vertices()
   {
      return nVerts;					// return the number of vertices

   }// end method vertices()
//------------------------------------------------------------------------
   public int edgeLength(int a, int b)
   {
      return M[a][b];					// return the edge length

   }// end method edgeLength()
//------------------------------------------------------------------------
   public int nextneighbor(int v)
   {

      next[v] = next[v] + 1; 				// initialize next[v] to the next neighbor

      if(next[v] < nVerts)
      {
      	while(M[v][next[v]] == 0 && next[v] < nVerts)
      	{
         next[v] = next[v] + 1;                         // initialize next[v] to the next neighbor

         if(next[v] == nVerts)
         	break;
      	}// end while

     }// end if

      if(next[v] >= nVerts)
      {
         next[v]=-1;                                    // reset to -1
         current_edge_weight = -1;
      }
      else current_edge_weight = M[v][next[v]];

      return next[v];      				// return next neighbor of v to be processed

   }// end method nextneighbor
//---------------------------------------------------------------------------
   public void resetnext()
   {
      for (int i=0; i < nVerts; i++)	// reset the array next to all -1's
         next[i] = -1;

   }// end method resetnext()

}// end class graph
////////////////////////////////////////////////////////////////////////////


