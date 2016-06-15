package pacman.entries.pacman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;

import pacman.Evaluation;
import pacman.Evaluation_;
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
	private boolean firstTime = true;


	public MyRandomPacMan2 (Controller<EnumMap<GHOST,MOVE>> ghosts){
		this.controllerGhosts = ghosts;
	}

	public MOVE computeMove(Game game, long timeDue, boolean isAll){
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
			c.heuristic = Evaluation_.evaluateGameState(gameState);
			c.game = gameState;
			moves.add(c);
		}

		Container best = new Container();
		for(Container move : moves)
			if(move.heuristic > best.heuristic)
				best = move;

		if(best.game == null)
			System.out.println("FUCK");

		if(best.game.getPacmanNumberOfLivesRemaining() < game.getPacmanNumberOfLivesRemaining()&&!isAll)
			return computeMove(game, timeDue, true);
		else
			return best.next;
	}



	public MOVE getMove(Game game, long timeDue) 
	{
		if(firstTime){
			iIndex = game.getActivePillsIndices();;
			firstTime = false;
		}

		return computeMove(game, timeDue,false);
	}

	private class Container{
		public MOVE next = MOVE.NEUTRAL;
		public Integer heuristic = Integer.MIN_VALUE;
		public Game game;
	}
}
