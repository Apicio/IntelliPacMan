package pacman.entries.pacman;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;

import pacman.Evaluation;
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
public class MyRandomPacMan extends Controller<MOVE>
{
	private MOVE myMove=MOVE.NEUTRAL;
	private Controller<MOVE> controllerPacman;
	private Controller<EnumMap<GHOST,MOVE>> controllerGhosts; 
	private int[] iIndex;
	private boolean firstTime = true;

	public MyRandomPacMan (Controller<MOVE> pacman, Controller<EnumMap<GHOST,MOVE>> ghosts){
		this.controllerGhosts = ghosts;
		this.controllerPacman = pacman;
	}


	public MOVE getMove(Game game, long timeDue) 
	{
		int[] index = game.getActivePillsIndices();
		if(firstTime){
			iIndex = game.getActivePillsIndices();;
			firstTime = false;
		}	
				
		
		ArrayList<Container> moves = new ArrayList<Container>();
		
		for(int i=0; i<index.length; i++){
			int k = 0;
			int gotoPill = index[i];
			Game gameState = game.copy();
			Container c = new Container();
			while(gameState.getPacmanCurrentNodeIndex() != gotoPill){
				int currPac = gameState.getPacmanCurrentNodeIndex();	
				MOVE last = gameState.getPacmanLastMoveMade() != null? gameState.getPacmanLastMoveMade() : MOVE.NEUTRAL;
				//MOVE path = gameState.getNextMoveTowardsTarget(currPac, gotoPill, DM.PATH);
				MOVE path = gameState.getApproximateNextMoveTowardsTarget(currPac, gotoPill, last, DM.PATH);
				gameState.advanceGame(path, controllerGhosts.getMove());
				if(k == 0)
					c.next = path;	
				k++;
			}
			c.heuristic = Evaluation.evaluateGameState(gameState);
			moves.add(c);
		}
		
		int heuristic = Integer.MIN_VALUE;
		for(Container move : moves){
			if(move.heuristic > heuristic){
				myMove = move.next;
				heuristic = move.heuristic;
			}
		}
		
		int soglia = 30000000;
		if(heuristic < soglia){
			System.out.println("all");
			moves = new ArrayList<Container>();		
			for(int i=0; i<iIndex.length; i++){
				int k = 0;
				int gotoPill = iIndex[i];
				Game gameState = game.copy();
				Container c = new Container();
				while(gameState.getPacmanCurrentNodeIndex() != gotoPill){
					int currPac = gameState.getPacmanCurrentNodeIndex();	
					MOVE last = gameState.getPacmanLastMoveMade() != null? gameState.getPacmanLastMoveMade() : MOVE.NEUTRAL;
					MOVE path = gameState.getNextMoveTowardsTarget(currPac, gotoPill, DM.PATH);
					//MOVE path = gameState.getApproximateNextMoveTowardsTarget(currPac, gotoPill, last, DM.PATH);
					gameState.advanceGame(path, controllerGhosts.getMove());
					if(k == 0)
						c.next = path;	
					k++;
				}
				c.heuristic = Evaluation.evaluateGameState(gameState);
				moves.add(c);
			}
			
			heuristic = Integer.MIN_VALUE;
			for(Container move : moves){
				if(move.heuristic > heuristic){
					myMove = move.next;
					heuristic = move.heuristic;
				}
			}
		}
		
		
		return myMove;
	}
	
	private class Container{
		public MOVE next;
		public Integer heuristic;
	}
}
