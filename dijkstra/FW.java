//********************************************************************
// FW.java
//

import java.io.IOException;

/**
 * A class that performs and times the Floyd-Warshall and the Dijkstra
 * algorithms to determine the all-pairs shortest path of an input
 * graph. The user must input the graph text file as a command-line
 * argument.
 * NOTE: dijkstra() uses the PriorityQueue class
 * @author Chris A. McManigal
 */
public class FW {

   /**
    * class definition
    */
   private static final int INFINITY = 999999999;
   
   
   /**
    * serves as the driver of the program;
    * performs timing calculations, and prints them to screen, along
    * with the output adjacency matrices
    * @param args an array of command-line arguments
    */
   public static void main (String[] args) {      
      Graph input_floyd = null;
      Graph input_dijkstra = null;
      Graph output = null;
      
      long time1, time2;
    
      if (args.length < 1)
         System.out.println ("Must specify graph file. Try again.");
      else {
         try
         {
            input_floyd = new Graph (args[0]);
            input_dijkstra = new Graph (args[0]); 
            output = new Graph (input_floyd.getNumV());
         }
         
         catch (IOException io)
         {
            System.out.println ("I/O error: ");
            io.printStackTrace();
            return;
         }
      }
 
      time1 = System.currentTimeMillis();
      
      // performs ASSP
      floyd (input_floyd, output);
      time2 = System.currentTimeMillis();
      
      // accounts for swapping between two arrays when even vertices
      if (output.getNumV() % 2 == 0)
         input_floyd.displayMatrix();
      else
         output.displayMatrix();      

      System.out.println ("For " + output.getNumV() + " vertices, " +
                          "Floyd-Warshall takes " + (time2 - time1) + 
                          " millisec.\n\n\n");
                         
      output.resetGraph();                  
                         
      time1 = System.currentTimeMillis();
      
      // performs SSSP for each vertex
      for (int row = 0; row < input_dijkstra.getNumV(); row++)
         dijkstra (input_dijkstra, output, row);
      time2 = System.currentTimeMillis();
      output.displayMatrix();      
      System.out.println ("\nFor " + output.getNumV() + " vertices, " +
                          "Dijkstra takes " + (time2 - time1) + 
                          " millisec.\n");    
   } 
   
   
   /**
    * performs Floyd-Warshall algorithm to determine all-pairs 
    * shortest path
    * @param input the input graph's adjacency matrix
    * @param output the output graph's adjacency matrix
    */
   public static void floyd (Graph input, Graph output) {
      Graph prev = input;
      Graph next = output;
      Graph temp = null;
      int row, col, weight;
      
      int numVerts = input.getNumV();
      
      for (int k = 0; k < numVerts; k++) {
         for (row = 0; row < numVerts; row++)
            for (col = 0; col < numVerts; col++) {
               weight = Math.min (prev.getWeight (row, col),
                                  prev.getWeight (row, k) +
                                  prev.getWeight (k, col));
               
               next.setWeight (row, col, weight);           
            }
            
         // swap arrays so only have to use two
         temp = prev;
         prev = next;
         next = temp;                       
      }  
   }  
   
   
   /**
    * performs Dijkstra's algorithm to determine single-source 
    * shortest path
    * @param input the input graph's adjacency matrix
    * @param output the output graph's adjacency matrix
    * @param source the source vertex
    */
   public static void dijkstra (Graph input, Graph output, 
                                int source) {
      int col, weight = 0;
      int u, v;
      
      int row = source;
      int numVerts = input.getNumV();
                         
      output.setWeight (row, row, 0);
      
      PriorityQueue Q = new PriorityQueue (output.getRow (row));
     
      while(Q.Empty() == 0) {
        	u = Q.Delete_root();
        	v = input.nextneighbor (u);
                      
         while(v != -1) {
            if (output.getWeight (row, v) > 
                output.getWeight (row, u) + 
                input.getWeight (u, v)) {
                 
               weight = output.getWeight (row, u) + 
                  input.getWeight (u, v);

               output.setWeight (row, v, weight);
               input.setWeight (row, v, weight);
               Q.Update (v, weight);
            }
               
            v = input.nextneighbor(u);
         }
      }
   }           
}
