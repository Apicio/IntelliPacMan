package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class FMyPacMan extends Controller<MOVE>
{
	public enum States{EAT, EVADE, CHASE}
	private static final int THRESHOLD=10;
	public String s;
	
	public MOVE getMove(Game game, long timeDue) 
	{
		int current=game.getPacmanCurrentNodeIndex();
		s = StateMachine(game, timeDue, current);
		switch(s){
			case "EAT":
				System.out.println("EAT");
				return Eat(game, timeDue, current);
			case "EVADE" :
				System.out.println("EVADE");
				return Evade(game, timeDue, current);
			case "CHASE" :
				System.out.println("CHASE");
				return Chase(game, timeDue, current);
		}
		return MOVE.NEUTRAL;
	}
	
	String StateMachine(Game game, long timeDue,int current){
		
		if(!NearGhost(game, timeDue, current) && !PowerPill(game, timeDue, current)){//If NO ghost near and NO Power Pill Evade
			return States.EAT.toString();
		}
		if(NearGhost(game, timeDue, current) && PowerPill(game, timeDue, current)){//If ghost near and Power Pill ON Chase 
			return States.CHASE.toString();
		}
		return States.EVADE.toString();//If NO ghost near EAT
	}
	
	MOVE Eat(Game game, long timeDue, int current){
		int next;
		int[] activePills=game.getActivePillsIndices();
		int[] activePowerPills=game.getActivePowerPillsIndices();
		int[] targetNodeIndices=new int[activePills.length+activePowerPills.length];
		
		for(int i=0;i<activePills.length;i++)
			targetNodeIndices[i]=activePills[i];
		
		for(int i=0;i<activePowerPills.length;i++)
			targetNodeIndices[activePills.length+i]=activePowerPills[i];
		next = game.getClosestNodeIndexFromNodeIndex(current,targetNodeIndices,DM.PATH);
		return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(),next,DM.PATH);	
	}
	
	MOVE Evade(Game game, long timeDue, int current){
		int distance = 0;
		for(GHOST ghost : GHOST.values()){
			if(game.getGhostEdibleTime(ghost)==0 && game.getGhostLairTime(ghost)==0){
				distance = game.getShortestPathDistance(current,game.getGhostCurrentNodeIndex(ghost));
				if(distance <THRESHOLD){
					return game.getNextMoveAwayFromTarget(current,game.getGhostCurrentNodeIndex(ghost),DM.PATH);
				}
			}	
		}
		return Eat(game, timeDue, current);
	}
	
	MOVE Chase(Game game, long timeDue, int current){
		int distance;
		int minDistance=Integer.MAX_VALUE;
		GHOST minGhost=null;
		
		for(GHOST ghost : GHOST.values()){
			if(game.getGhostEdibleTime(ghost)>80)
			{
				distance=game.getShortestPathDistance(current,game.getGhostCurrentNodeIndex(ghost));
				minGhost = Distance(minGhost,minDistance,distance, ghost);
			}
		}
		if(minGhost!=null){
			return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(),game.getGhostCurrentNodeIndex(minGhost),DM.PATH);
		}
		return Eat(game, timeDue, current);
	}
	
	GHOST Distance(GHOST myghost,int minDistance, int distance, GHOST ghost){
		if(distance<minDistance)
		{
			minDistance=distance;
			myghost=ghost;
		}
		return myghost;
	}
	
	boolean PowerPill(Game game, long timeDue, int current){
		for(GHOST ghost : GHOST.values()){
			if(game.getGhostEdibleTime(ghost)>0)
				return true;
		}
		return false;
	}
	
	boolean NearGhost(Game game, long timeDue, int current){
		int distance;
		for(GHOST ghost : GHOST.values()){
			distance = game.getShortestPathDistance(current,game.getGhostCurrentNodeIndex(ghost)); 
			if(distance < THRESHOLD){
				return true;
			}
		}
		return false;
	}
}