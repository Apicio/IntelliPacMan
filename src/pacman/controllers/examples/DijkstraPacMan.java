package pacman.controllers.examples;

import pacman.controllers.Controller;
import pacman.game.Game;

import static pacman.game.Constants.*;

/*
 * The Class NearestPillPacMan.
 */
public class DijkstraPacMan extends Controller<MOVE>
{	
	
	/* (non-Javadoc)
	 * @see pacman.controllers.Controller#getMove(pacman.game.Game, long)
	 */
	public MOVE getMove(Game game,long timeDue)
	{		
		int currentNodeIndex=game.getPacmanCurrentNodeIndex();
		
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
		return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(),game.getClosestNodeIndexFromNodeIndex(currentNodeIndex,targetNodeIndices,DM.PATH),DM.PATH);	
		
		// La direzione ottima è determinata come intersezione tra la direzione per cui si ha la pillola più vicina e la direzione che 
		// massimizza la distanza tra i fantasmi, nel caso in cui l'intersezione sia vuota si da priorità alla lontananza dai fantasmi.
		
		// Resta da aggiungere solo una regola per gestire l'utilizzo delle power pills
	}
}