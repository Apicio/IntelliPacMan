package AbPruning;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Random;

import DecisionTree.Node;
import DecisionTree.Tree;
import pacman.Evaluation;
import pacman.Evaluation_;
import pacman.controllers.Controller;
import pacman.controllers.examples.AggressiveGhosts;
import pacman.controllers.examples.RandomGhosts;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.internal.AStar;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class MinMaxPacman extends Controller<MOVE>
{
	static int k=0;
	private MOVE myMove=MOVE.NEUTRAL;
	private int depth;
	Controller<EnumMap<GHOST,MOVE>> ghostController;
	private Random rnd=new Random();
	private MOVE[] allMoves=MOVE.values();
	private boolean canReverse;


	public MinMaxPacman(Controller<EnumMap<GHOST,MOVE>> ghostController, int depth, boolean canReverse){
		this.ghostController = ghostController;
		this.depth = depth;
		this.canReverse = canReverse;
	}

	public MOVE getMove(Game game, long timeDue) 
	{
		State head = new State();
		head.game = game;
		head.depth = 0;
		ArrayList<State> next = getNextPACMANMoves(head);
		ArrayList<State> comp = new ArrayList<State>();
		for(State child : next){
			comp.add(minimax(child, false));
		}
		double alpha = Integer.MIN_VALUE;
		for(State find : comp){
			if(alpha<find.beta){
				alpha = find.beta;
				myMove = find.pacMove;
			}
		}
		
		return myMove;
	}

	private State minimax(State node, boolean isMax) {
		if(node.depth == depth){
			if(isMax)
				node.alpha = Evaluation_.evaluateGameState(node.game);
			else 
				node.beta = Evaluation_.evaluateGameState(node.game);
			return node;
		}

		if(isMax){
			ArrayList<State> next = getNextPACMANMoves(node);
			for(State child : next){
				node.alpha = Math.max(node.alpha, minimax(child,false).beta);
				if(node.beta <= node.alpha)
					return node;
			}
			return node;
		}else{
			ArrayList<State> next = getNextGHOSTMoves(node);
			if(!(ghostController instanceof RandomGhosts)){
				for(State child : next){
					node.beta = Math.min(node.beta, minimax(child,true).alpha);
					if(node.beta <= node.alpha)
						return node;
				}
				return node;
			}else{
				for(State child : next){
					double res = 0;
					res += minimax(child,true).alpha;
					node.beta = res/next.size();
				}
				return node;
			}
		}
	}

	private ArrayList<State> getNextGHOSTMoves(State gameState){
		if(!(ghostController instanceof RandomGhosts))
			return getMINAggressiveMoves(gameState);
		else
			return getMINRandomMoves(gameState);
	}

	private ArrayList<State> getMINRandomMoves(State gameState) {
		ArrayList<State> toReturn = new ArrayList<State>();
		ArrayList<MOVE[]> moves = new ArrayList<MOVE[]>();
		for(GHOST ghost : GHOST.values()){
			int ghostIndex = gameState.game.getGhostCurrentNodeIndex(ghost);
			moves.add(gameState.game.getPossibleMoves(ghostIndex));
		}		
		for(int i = 0; i<moves.get(0).length; i++){
			for(int j = 0; j<moves.get(1).length; j++){
				for(int k = 0; k<moves.get(2).length; k++){
					for(int x = 0; x<moves.get(3).length; x++){
						Game tmpGame = gameState.game.copy();
						EnumMap<GHOST, MOVE> ghostStruct = ghostController.getMove(gameState.game,-1);
						ghostStruct.replace(GHOST.values()[0], moves.get(0)[i]);
						ghostStruct.replace(GHOST.values()[1], moves.get(1)[j]);
						ghostStruct.replace(GHOST.values()[2], moves.get(2)[k]);
						ghostStruct.replace(GHOST.values()[3], moves.get(3)[x]);
						tmpGame.advanceGame(MOVE.NEUTRAL, ghostStruct);
						State c = new State();
						c.game = tmpGame;
						c.alpha = gameState.alpha;
						c.beta = gameState.beta;
						c.depth = gameState.depth+1;
						c.pacMove = gameState.pacMove;
						toReturn.add(c);
					}
				}
			}
		}
		return toReturn;
	}

	private ArrayList<State> getMINAggressiveMoves(State gameState) {
		ArrayList<State> toReturn = new ArrayList<State>();
		EnumMap<GHOST, MOVE> ghostStruct = this.ghostController.getMove(gameState.game,-1);
		Game tmpGame = gameState.game.copy();		
		tmpGame.advanceGame(MOVE.NEUTRAL, ghostStruct);
		tmpGame.updateGhosts(ghostStruct);
		State c = new State();
		c.game = tmpGame;
		c.alpha = gameState.alpha;
		c.beta = gameState.beta;
		c.depth = gameState.depth+1;
		c.pacMove = gameState.pacMove;
		toReturn.add(c);
		return toReturn;
	}


	private ArrayList<State> getNextPACMANMoves(State gameState) {
		int currIndex = gameState.game.getPacmanCurrentNodeIndex();
		MOVE[] moves;
		if(!canReverse)
			moves = gameState.game.getPossibleMoves(currIndex, gameState.game.getPacmanLastMoveMade());
		else
			moves = gameState.game.getPossibleMoves(currIndex);
		ArrayList<State> toReturn = new ArrayList<State>();
		EnumMap<GHOST, MOVE> ghostMove = ghostController.getMove(gameState.game,-1);
		for(MOVE move : moves){
			Game tmpGame = gameState.game.copy();
			tmpGame.advanceGame(move, ghostMove);
			tmpGame.updatePacMan(move);
			State c = new State();
			c.game = tmpGame;
			if(gameState.depth == 0)
				c.pacMove = move;
			else
				c.pacMove = gameState.pacMove;
			c.alpha = gameState.alpha;
			c.beta = gameState.beta;
			c.depth = gameState.depth+1;
			toReturn.add(c);
		}
		return toReturn;
	}

	private class State{
		public Integer depth = 0;
		public Game game;
		public MOVE pacMove = MOVE.NEUTRAL;;
		public double alpha = Integer.MIN_VALUE;
		public double beta = Integer.MAX_VALUE;
	}
}
