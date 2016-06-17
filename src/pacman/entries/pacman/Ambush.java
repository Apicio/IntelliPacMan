package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.internal.AStar;
import pacman.game.internal.AStarONE;
import pacman.game.internal.AStarTWO;
import pacman.game.internal.AbstractAStar;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class Ambush extends Controller<MOVE>
{
	private int getClosestGhost(Game game, int pacManIndex){
		double distance = Double.MAX_VALUE;
		int closestGhost = -1;
		for(GHOST ghost : GHOST.values()){
			int g = game.getGhostCurrentNodeIndex(ghost);
			double d = game.getShortestPathDistance(pacManIndex, g);
			if(d<distance){
				distance = d;
				closestGhost = g;
			}
		}
		return closestGhost;
	}
	private int getClosestPill(Game game, int currentNodeIndex)
	{				
		//get all active pills
		int[] activePills=game.getActivePillsIndices();	
		//get all active power pills
		int[] activePowerPills=game.getActivePowerPillsIndices();	
		//create a target array that includes all ACTIVE pills and power pills
		int[] targetNodeIndices=new int[activePills.length+activePowerPills.length];		
		for(int i=0;i<activePills.length;i++)
			targetNodeIndices[i]=activePills[i];		
		for(int i=0;i<activePowerPills.length;i++)
			targetNodeIndices[activePills.length+i]=activePowerPills[i];	
		//return the next direction once the closest target has been identified
		double distance = Double.MAX_VALUE;
		int closestPill = -1;
		for(int i = 0; i<targetNodeIndices.length; i++){
			double d = game.getShortestPathDistance(currentNodeIndex, targetNodeIndices[i]);
			if(d<distance){
				distance = d;
				closestPill = targetNodeIndices[i];
			}
		}
		return closestPill;	
	}
	private int getClosestPowerPill(Game game, int currentNodeIndex)
	{        

		//get all active power pills
		int[] activePowerPills=game.getActivePowerPillsIndices();  
		//create a target array that includes all ACTIVE pills and power pills
		int[] targetNodeIndices=new int[activePowerPills.length];    
		for(int i=0;i<activePowerPills.length;i++)
			targetNodeIndices[i]=activePowerPills[i];  
		//return the next direction once the closest target has been identified
		double distance = Double.MAX_VALUE;
		int closestPill = -1;
		for(int i = 0; i<targetNodeIndices.length; i++){
			double d = game.getShortestPathDistance(currentNodeIndex, targetNodeIndices[i]);
			if(d<distance){
				distance = d;
				closestPill = targetNodeIndices[i];
			}
		}
		return closestPill;  
	}

	/*Return the closest ghost, which is closest to the MsPacman's closest PP*/
	public int getNearestGhostToPowerPill(Game game){
		//int[] activePP = game.getActivePowerPillsIndices();
		int nearestPP = getClosestPowerPill(game, game.getPacmanCurrentNodeIndex());
		double minDistanceGhost = Double.MAX_VALUE;
		int nearestGhost = -1;
		for(GHOST ghost : GHOST.values()){
			int currGhostIdx = game.getGhostCurrentNodeIndex(ghost);
			double currDist = game.getShortestPathDistance(currGhostIdx, nearestPP);
			if(currDist<minDistanceGhost){
				minDistanceGhost = currDist;
				nearestGhost = currGhostIdx;
			}
		}
		return nearestGhost;
	}
	/*Returns the closest edible ghost with respect of MsPacman's Position*/
	public int getNearestEdibleGhost(Game game){
		int pacmanPos = game.getPacmanCurrentNodeIndex();
		double minDistance = Double.MAX_VALUE;
		int nearestEdibleGhost = -1;

		for(GHOST ghost : GHOST.values()){
			if(game.isGhostEdible(ghost)){
				int currGhostIdx = game.getGhostCurrentNodeIndex(ghost);
				double currDist = game.getShortestPathDistance(currGhostIdx,pacmanPos);
				if(currDist < minDistance){
					minDistance = currDist;
					nearestEdibleGhost = currGhostIdx;
				}

			}
		}
		return nearestEdibleGhost;
	}
	// Rule 1
	static private int NPP_THR1 = 5;
	static private int NG_THR1 = 4;
	static private int GN2NP_THR1 = 6;
	// Rule 2
	static private int NPP_THR2 = 6;
	static private int NG_THR2 = 8;
	// Rule 3
	static private int NG_THR3 = 8;
	// Rule 4
	static private int NG_THR4 = 8;
	static private int NEG_THR4 = 8;
	// Rule 5
	static private int NG_THR5 = 8;
	// Rule 6
	static private int NG_THR6 = 9;
	static private int NEG_THR6 = 8;
	// Rule 7
	static private int NG_THR7 = 9;
	
	static private int NOEDIBLEGHOST = -1;
	
	AStar one = new AStar();
	AStar two = new AStar();
	
	public MOVE getMove(Game game, long timeDue) 
	{
		int pacManIndex = game.getPacmanCurrentNodeIndex();
		int ghostIndex = getClosestGhost(game,pacManIndex);
		int pillIndex = getClosestPill(game,pacManIndex);
		int powerPillIndex = getClosestPowerPill(game,pacManIndex);
		int ghostNext2PPill = getNearestGhostToPowerPill(game);
		int edibleGhost = getNearestEdibleGhost(game);
		
		one.createGraph(game.getCurrentMaze().graph);
		two.createGraph(game.getCurrentMaze().graph);		

//		for(GHOST ghost : GHOST.values())
//			if(game.getGhostEdibleTime(ghost)==0 && game.getGhostLairTime(ghost)==0)
//				if(game.getShortestPathDistance(pacManIndex,game.getGhostCurrentNodeIndex(ghost))<MIN_DISTANCE)
//					return game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(),game.getGhostCurrentNodeIndex(ghost),DM.PATH);
//		
		// Rule 1
		if(game.getShortestPathDistance(pacManIndex, powerPillIndex) <= NPP_THR1)
			if(game.getShortestPathDistance(pacManIndex, ghostIndex) >= NG_THR1)
				if(game.getShortestPathDistance(pacManIndex, ghostNext2PPill) >= GN2NP_THR1){
					return game.getPacmanLastMoveMade().opposite();
				}

		// Rule 2
		if(game.getActivePowerPillsIndices().length != 0)
			if(game.getShortestPathDistance(pacManIndex, ghostIndex) <= NG_THR2)
				if(game.getShortestPathDistance(pacManIndex, powerPillIndex) <= NPP_THR2){
					double npp = game.getShortestPathDistance(pacManIndex, powerPillIndex);
					double gnnpp = game.getShortestPathDistance(pacManIndex, ghostNext2PPill);
					if(npp <= gnnpp){
						return two.getMoveTo(powerPillIndex, game);
					}
				}
		// Rule 3
		if(game.getActivePowerPillsIndices().length != 0)
			if(game.getShortestPathDistance(pacManIndex, ghostIndex) <= NG_THR3){
				double npp = game.getShortestPathDistance(pacManIndex, powerPillIndex);
				double gnnpp = game.getShortestPathDistance(pacManIndex, ghostNext2PPill);
				if(npp <= gnnpp){
					return one.getMoveTo(powerPillIndex, game);
				}
			}
		// Rule 4
		if(edibleGhost != NOEDIBLEGHOST)
			if(game.getShortestPathDistance(pacManIndex, ghostIndex) <= NG_THR4)
				if(game.getShortestPathDistance(pacManIndex, edibleGhost) <= NEG_THR4)
					return one.getMoveTo(edibleGhost, game);
		// Rule 5
		if(game.getShortestPathDistance(pacManIndex, ghostIndex) <= NG_THR5)
			return one.getMoveTo(pillIndex, game);
		// Rule 6
		if(edibleGhost != NOEDIBLEGHOST)
			if(game.getShortestPathDistance(pacManIndex, ghostIndex) >= NG_THR6)
				if(game.getShortestPathDistance(pacManIndex, edibleGhost) <= NEG_THR6)
					return one.getMoveTo(edibleGhost, game);			
		// Rule 7
		if(game.getShortestPathDistance(pacManIndex, ghostIndex) >= NG_THR7)
			return one.getMoveTo(pillIndex, game);

		return MOVE.NEUTRAL;
	}
}
