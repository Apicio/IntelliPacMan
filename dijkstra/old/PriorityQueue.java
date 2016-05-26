// Author:	Renee L. Ramsey, William F. Klostermeyer
// Algorithms: An Undergraduate Course with Programming
// Program:     PriorityQueue Class
// Date:	4/27/2002, 11/14/02
//
// PriorityQueue.java
//		Let A be PriorityQueue with n nodes
//		stored in Array of items A[0..n-1]
//		left child of A[i] stored at A[2i]
//		right child of A[i] stored at A[2i+1]
// Note: 	The PriorityQueue class takes an array as an input parameter.
// to run this program: > Call the class from another program.
//			  Example:  PriorityQueue a = new PriorityQueue(array);
import java.io.*;	//for I/O
///////////////////////////////////////////////////////////////////////////
public class PriorityQueue
{
   class item
   {
      int index;				// vertex index
      int value;				// distance

      public item()
      {

         index = 0;			// initializations
         value = 0;

      }

   }// end class item


   public item [] A;			// array A used as a heap
   public int n;				// number of nodes

   public PriorityQueue(int B[]) throws IOException	// constructor
   {
 	A = new item[B.length];			// allocate array A of items

	n = B.length;				// get the number of nodes

	for(int z=0; z<n; z++)
	{
             A[z] = new item();
             A[z].index = z;
             A[z].value = B[z];

	}// end for

	for(int i=n/2-1; i>=0; i--) 		// ignore leaves
	{
   		Heapify(i);				// call Heapify method

	}// end for

   }// end constructor

//------------------------------------------------------------------------
   public void Heapify(int i)  // utility routine to percolate down from index i
   {
	int left, r, min, tmp;			// declare variables

	left = 2 * i + 1;     			// left child
	r = 2 * i + 2;       			// right child

  	if(left < n && A[1].value < A[i].value)	// find smallest child, if any less than A[i]
     		min = left;             	// save index of smaller child
  	else
  	   	min = i;

  	if(r < n && A[r].value < A[min].value)
     		min = r;           		// save index of smaller child

  	if(min != i)	 			// swap and percolate, if necessary
  	{
      		tmp = A[i].value;      		// exchange values at two indices
      		A[i].value = A[min].value;
      		A[min].value = tmp;
      		tmp = A[i].index;      		// exchange values at two indices
      		A[i].index = A[min].index;
      		A[min].index = tmp;
      		Heapify(min); 			// call Heapify

     	}// end if

   }// end method Heapify

//------------------------------------------------------------------------
   public void Insert(int key) 			// insert new node with key value = key
   {                                            // Parent of node i is at node i/2 ... assume i/2 = floor(i/2)

        int i;						// declare variables

	n++;							//increment node
	i = n;                                  	// i equals node

	while(i > 1 && A[i/2].value > key)     	// percolate up
        {
   		A[i].value = A[i/2].value;
   		i = i/2;

	}// end while

	A[i].value = key;			

   }// end method Insert()

//------------------------------------------------------------------------
   public int Delete_root()			// remove node with minimum value
   {
	int min;				

	if(n < 1) 				
	{
		System.out.println("error");	// display error
		return -1; 				// return -1
	}
	else
	{
   		min = A[0].index;
   		A[0].value = A[n-1].value;    // replace root with last element in heap
   		A[0].index = A[n-1].index;    // replace root with last element in heap
   		n--;            			// reduce heap size
   		Heapify(0);  			// percolate new root downwards
   		return min;  			// return min

	}// end if

   }// end method Delete_root()

//------------------------------------------------------------------------
   public void Update(int i, int new_key)  // decrease value of key at index i
   {
	int tmp;					// declare variables

	A[i].value = new_key;   			// assuming this is less than old key value
	while(i > 1 && A[i/2].value > new_key)  	// percolate up
  	{
  		tmp = A[i].value;   			// swap A[i] and A[i/2]
  		A[i].value = A[i/2].value;
  		A[i/1].value = tmp;
  		tmp = A[i].index;   			// swap A[i] and A[i/2]
  		A[i].index = A[i/2].index;
  		A[i/1].index = tmp;
  		i = i/2;               		

  	}// end while

   }// end method Update()

   //------------------------------------------------------------------------
   public int Empty()  				// return if heap is empty or not
   {
      if(n == 0) 					// if it's empty
      	return 1;
      else
      	return 0;    			// not empty

   }// end method Update()

   //------------------------------------------------------------------------
}// end PriorityQueue class
////////////////////////////////////////////////////////////////////////////

