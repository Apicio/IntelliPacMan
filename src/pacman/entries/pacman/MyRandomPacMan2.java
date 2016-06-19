package pacman.entries.pacman;
import java.util.ArrayList;
import java.util.EnumMap;

import pacman.EvaluationHeuristic;
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
public class MyRandomPacMan2 extends Controller<MOVE>
{
	private Controller<EnumMap<GHOST,MOVE>> controllerGhosts; 
	private int[] iIndex;

	public MyRandomPacMan2 (Controller<EnumMap<GHOST,MOVE>> ghosts){
		this.controllerGhosts = ghosts;
	}

	public MOVE computeMove(Game game, boolean isAll){
		int[] index = isAll? iIndex : game.getActivePillsIndices();
		ArrayList<Container> moves = new ArrayList<Container>();

		for(int i=0; i<index.length; i++){
			int k = 0;
			int gotoPill = index[i];
			Game gameState = game.copy();
			Container c = new Container();
			while(gameState.getPacmanCurrentNodeIndex() != gotoPill){
				int currPac = gameState.getPacmanCurrentNodeIndex();	
				MOVE last = gameState.getPacmanLastMoveMade() != null? gameState.getPacmanLastMoveMade() : MOVE.NEUTRAL;
				MOVE path = isAll? gameState.getNextMoveTowardsTarget(currPac, gotoPill, DM.PATH) : gameState.getApproximateNextMoveTowardsTarget(currPac, gotoPill, last, DM.PATH);
				gameState.advanceGame(path, controllerGhosts.getMove());
				if(k == 0)
					c.next = path;	
				k++;
			}
			c.heuristic = EvaluationHeuristic.evaluateGameState(gameState);
			c.game = gameState;
			moves.add(c);
		}

		Container best = new Container();
		for(Container move : moves)
			if(move.heuristic > best.heuristic)
				best = move;

		if(best.game.getPacmanNumberOfLivesRemaining() < game.getPacmanNumberOfLivesRemaining() && !isAll)
			return computeMove(game, true);
		else
			return best.next;
	}



	public MOVE getMove(Game game, long timeDue) 
	{
		iIndex = game.getPillIndices();
		return computeMove(game, false);
	}

	private class Container{
		public MOVE next = MOVE.NEUTRAL;
		public long heuristic = Long.MIN_VALUE;
		public Game game;
	}
}
