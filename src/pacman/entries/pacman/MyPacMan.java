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
public class MyPacMan extends Controller<MOVE>
{
	private MOVE myMove=MOVE.NEUTRAL;
	
	
	public MOVE getMove(Game game, long timeDue) 
	{
		int current = game.getPacmanCurrentNodeIndex();
		int move = current;
		int[] moves = game.getNeighbouringNodes(current);
		int max_distance = Integer.MIN_VALUE;
		System.out.println(moves.length);
		
		for(GHOST ghost : GHOST.values()){
				for(int i=0; i<moves.length; i++){
					int dist = game.getShortestPathDistance(moves[i],game.getGhostCurrentNodeIndex(ghost));
					if(dist>max_distance){
						move = moves[i];
						max_distance = dist;
					}
				}
				myMove = game.getMoveToMakeToReachDirectNeighbour(current, move);	
		}
		return myMove;
	}
}
