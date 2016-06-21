package Deep;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.EnumMap;
//import java.util.PriorityQueue;
//
//import pacman.game.Game;
//import pacman.game.Constants.MOVE;
//
///*
// * This class is used to compute the shortest path for the ghosts: as these may not reverse, one cannot use 
// * a simple look-up table. Instead, we use the pre-computed shortest path distances as an admissable
// * heuristic. Although AStar needs to be run every time a path is to be found, it is very quick and does
// * not expand too many nodes beyond those on the optimal path.
// */
//public class AStar
//{
//	 N[] graph;
//	
//	public void createGraph(Node[] nodes)
//	{
//		graph=new N[nodes.length];
//		
//		//create graph
//		for(int i=0;i<nodes.length;i++)
//			graph[i]=new N(nodes[i].nodeIndex);
//		
//		//add neighbours
//		for(int i=0;i<nodes.length;i++)
//		{	
//			EnumMap<MOVE,Integer> neighbours=nodes[i].neighbourhood;
//			MOVE[] moves=MOVE.values();
//			
//			for(int j=0;j<moves.length;j++)
//				if(neighbours.containsKey(moves[j]))
//					graph[i].adj.add(new E(graph[neighbours.get(moves[j])],moves[j],1));	
//		}
//	}
//	
//	
//	
//	public synchronized int[] computePathsAStar(int s, int t, MOVE lastMoveMade, Game game)
//    {	
//		N start=graph[s];
//		N target=graph[t];
//		
//        PriorityQueue<N> open = new PriorityQueue<N>();
//        ArrayList<N> closed = new ArrayList<N>();
//
//        start.g = 0;
//        start.h = game.getShortestPathDistance(start.index, target.index);
//
//        start.reached=lastMoveMade;
//        
//        open.add(start);
//
//        while(!open.isEmpty())
//        {
//            N currentNode = open.poll();
//            closed.add(currentNode);
//            
//            if (currentNode.isEqual(target))
//                break;
//
//            for(E next : currentNode.adj)
//            {
//            	if(next.move!=currentNode.reached.opposite())
//            	{
//	                double currentDistance = next.cost;
//	
//	                if (!open.contains(next.node) && !closed.contains(next.node))
//	                {
//	                    next.node.g = currentDistance + currentNode.g;
//	                    next.node.h = game.getShortestPathDistance(next.node.index, target.index);
//	                    
//	                    next.node.parent = currentNode;
//	                    
//	                    next.node.reached=next.move;
//	
//	                    open.add(next.node);
//	                }
//	                else if (currentDistance + currentNode.g < next.node.g)
//	                {
//	                    next.node.g = currentDistance + currentNode.g;
//	                    next.node.parent = currentNode;
//	                    
//	                    next.node.reached=next.move;
//	
//	                    if (open.contains(next.node))
//	                        open.remove(next.node);
//	
//	                    if (closed.contains(next.node))
//	                        closed.remove(next.node);
//	
//	                    open.add(next.node);
//	                }
//	            }
//            }
//        }
//
//        return extractPath(target);
//    }
//	
//	public synchronized int[] computePathsAStar(int s, int t, Game game)
//    {	
//		return computePathsAStar(s, t, MOVE.NEUTRAL, game);
//    }
//
//    protected synchronized int[] extractPath(N target)
//    {
//    	ArrayList<Integer> route = new ArrayList<Integer>();
//        N current = target;
//        route.add(current.index);
//
//        while (current.parent != null)
//        {
//            route.add(current.parent.index);
//            current = current.parent;
//        }
//        
//        Collections.reverse(route);
//
//        int[] routeArray=new int[route.size()];
//        
//        for(int i=0;i<routeArray.length;i++)
//        	routeArray[i]=route.get(i);
//        
//        return routeArray;
//    }
//    
//    public void resetGraph()
//    {
//    	for(N node : graph)
//    	{
//    		node.g=0;
//    		node.h=0;
//    		node.parent=null;
//    		node.reached=null;
//    	}
//    }
//}
//
//class N implements Comparable<N>
//{
//    public N parent;
//    public double g, h;
//    public boolean visited = false;
//    public ArrayList<E> adj;
//    public int index;
//    public MOVE reached=null;
//
//    public N(int index)
//    {
//        adj = new ArrayList<E>();
//        this.index=index;
//    }
//
//    public N(double g, double h)
//    {
//        this.g = g;
//        this.h = h;
//    }
//
//    public boolean isEqual(N another)
//    {
//        return index == another.index;
//    }
//
//    public String toString()
//    {
//        return ""+index;
//    }
//
//	public int compareTo(N another)
//	{
//      if ((g + h) < (another.g + another.h))
//    	  return -1;
//      else  if ((g + h) > (another.g + another.h))
//    	  return 1;
//		
//		return 0;
//	}
//}
//
//class E
//{
//	public N node;
//	public MOVE move;
//	public double cost;
//	
//	public E(N node,MOVE move,double cost)
//	{
//		this.node=node;
//		this.move=move;
//		this.cost=cost;
//	}
//}

import pacman.game.Game;

import java.util.Arrays;

import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.internal.AbstractAStar;

public class MyAStarHeuristic extends AbstractAStar{
	private static final int MIN_GHOST_DIST = 10;
	private Game game;
	private int blinky;
	private int[] nBlinky;
	private int inky;
	private int[] nInky;
	private int pinky;
	private int[] nPinky;
	private int sue;
	private int[] nSue;
	
	public MyAStarHeuristic(Game g){
		super();
		this.game = g;
		this.blinky = game.getGhostCurrentNodeIndex(GHOST.BLINKY);
		this.inky = game.getGhostCurrentNodeIndex(GHOST.INKY);
		this.pinky = game.getGhostCurrentNodeIndex(GHOST.PINKY);
		this.sue = game.getGhostCurrentNodeIndex(GHOST.SUE);
//		this.nBlinky = game.getNeighbouringNodes(blinky);
//		this.nInky = game.getNeighbouringNodes(inky);
//		this.nPinky = game.getNeighbouringNodes(pinky);
//		this.nSue = game.getNeighbouringNodes(sue);
	}

	@Override
	public double computeHeuristic(Game game, int start, int target) {
		return game.getShortestPathDistance(start, target);
	}

	@Override
	public double computeCost(Game game, int index, int index2) {
		return 0;
	}
	
	public MOVE getMoveTo(int target, Game game){
		int currPac = game.getPacmanCurrentNodeIndex();
		int [] path = computePathsAStar(currPac, target, game.getPacmanLastMoveMade(), game);
		return game.getMoveToMakeToReachDirectNeighbour(currPac, path[1]);
	}

	@Override
	public double addictionalCost(int index, Game game, double dist) {
		int cost = 1000000;

		/* Lair Avoid */
		if(game.isLiarIndex(index))
			return cost+6000000-dist;
		
		/* Ghost Avoid */
		if(game.getShortestPathDistance(index,blinky)<MIN_GHOST_DIST && !game.isGhostEdible(GHOST.BLINKY) && index == blinky)
			return cost+6000000-dist;
		if(game.getShortestPathDistance(index,inky)<MIN_GHOST_DIST && !game.isGhostEdible(GHOST.INKY) && index == inky)
			return cost+6000000-dist;
		if(game.getShortestPathDistance(index,pinky)<MIN_GHOST_DIST && !game.isGhostEdible(GHOST.PINKY) && index == pinky)
			return cost+6000000-dist;
		if(game.getShortestPathDistance(index,sue)<MIN_GHOST_DIST && !game.isGhostEdible(GHOST.SUE) && index == sue)
			return cost+6000000-dist;
		
		/* Near index Avoid */
//		if(game.getShortestPathDistance(index,blinky)<MIN_GHOST_DIST && !game.isGhostEdible(GHOST.BLINKY) && isNear(index,nBlinky))
//			return cost+6000000-dist;
//		if(game.getShortestPathDistance(index,inky)<MIN_GHOST_DIST && !game.isGhostEdible(GHOST.INKY) && isNear(index,nInky))
//			return cost+6000000-dist;
//		if(game.getShortestPathDistance(index,pinky)<MIN_GHOST_DIST && !game.isGhostEdible(GHOST.PINKY) && isNear(index,nPinky))
//			return cost+6000000-dist;
//		if(game.getShortestPathDistance(index,sue)<MIN_GHOST_DIST && !game.isGhostEdible(GHOST.SUE) && isNear(index,nSue))
//			return cost+6000000-dist;
		
		/*Calcolo distanza minima dai ghost*/	
		
		int tmp = 0;
		int ghostDist=0; 
		int shortestGhostDist = Integer.MAX_VALUE; 
		int secondShortestGhostDist = Integer.MAX_VALUE;
		
		for(GHOST ghost : GHOST.values()){
			int ghostIdx = game.getGhostCurrentNodeIndex(ghost);
			if(!game.isGhostEdible(ghost)){
				tmp = computeDistancePath(index,ghostIdx); 
				if(shortestGhostDist>tmp){
					secondShortestGhostDist = shortestGhostDist;
					shortestGhostDist = tmp;
				}
			}
		}
		
		/* Se abbiamo una distanza più corta ed una seconda distanza più corta, facciamo la media */	
		if(shortestGhostDist != Integer.MAX_VALUE && shortestGhostDist !=-1 && shortestGhostDist<MIN_GHOST_DIST )
			if(secondShortestGhostDist != Integer.MAX_VALUE && secondShortestGhostDist !=-1 && secondShortestGhostDist<MIN_GHOST_DIST ) 
				ghostDist =   ((shortestGhostDist+secondShortestGhostDist)/2)*10000;
			else  //Altrimenti ci mettiamo alla distanza più corta
				ghostDist = shortestGhostDist*10000;
		else //Se non abbiamo nessuna delle due distanze allora ci mettiamo ad una distanza minima
			ghostDist = MIN_GHOST_DIST*10000;	
		return cost-ghostDist-dist;
	}

	private int computeDistancePath(int index, int ghostIdx) {
		int dist = game.getShortestPathDistance(index,ghostIdx);
		int[] i = game.getLiarIndex();
		if(dist == -1 && i.length !=0){
			dist = 0;		
			for(int ii : i)
				dist = dist + game.getShortestPathDistance(index,ii);
			dist = dist/i.length;
		}
		return dist;
	}

//	private boolean isNear(int index, int[] ghost) {
//		for(int i : ghost)
//			return i == index;
//		return false;
//	}
}
