package DecisionTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.LinkedList;

import org.apache.commons.lang3.ArrayUtils;

import pacman.EvaluationHeuristic;
import pacman.controllers.Controller;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class GreedyRec extends Controller<MOVE>
{
	static private int DEPTH = 12;
	Controller<EnumMap<GHOST,MOVE>> ghostController ;

	public GreedyRec(Controller<EnumMap<GHOST,MOVE>> ghostController){
		this.ghostController=ghostController;
	}

	public ScoreMove dfsSearch(Game g, int life, int depth){
		Game gameState = g.copy();
		depth++;
		if (depth == DEPTH || g.gameOver()|| g.getPacmanNumberOfLivesRemaining() < life){
			ScoreMove scoreMove = new ScoreMove();
			scoreMove.score=EvaluationHeuristic.evaluateGameState(g);
			return scoreMove;
		}

		MOVE[] moves = gameState.getPossibleMoves(gameState.getPacmanCurrentNodeIndex());
		EnumMap<GHOST, MOVE> ghostStruct = ghostController.getMove(gameState,-1);
		ScoreMove bestValue = new ScoreMove();
		for(int i=0; i<moves.length; i++){
			Game gameStateIex = gameState.copy();
			gameStateIex.advanceGame(moves[i], ghostStruct);
			ScoreMove scoreMove = dfsSearch(gameStateIex,life,depth);
			if (scoreMove.score > bestValue.score){
			    bestValue.score=scoreMove.score;
				bestValue.move=moves[i];
			}
		}

		return bestValue;	
	}

	boolean isValidMove(MOVE[] validMoves, MOVE move) {
		for (MOVE validMove : validMoves) {
			if (move == validMove) return true;
		}
		return false;
	}


	public MOVE getMove(Game game, long timeDue) 
	{	
		ScoreMove bestValue = dfsSearch(game, game.getPacmanNumberOfLivesRemaining(), 0);
		return bestValue.move;
	}

	private int mean(LinkedList<Integer> l){
		int acc=0;
		for(Integer i : l){
			acc+=i;
		}
		System.out.println("masn");
		return acc/l.size();

	}
	private class ScoreMove{
		public int score = Integer.MIN_VALUE;
		public MOVE move = MOVE.NEUTRAL;


	}
}
