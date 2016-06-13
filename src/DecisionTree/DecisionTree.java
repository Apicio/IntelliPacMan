package DecisionTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import pacman.Evaluation;
import pacman.controllers.Controller;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class DecisionTree extends Controller<MOVE>
{
	private MOVE myMove = MOVE.NEUTRAL;
	static private int DEPTH = 5;
	EnumMap<GHOST, MOVE> ghostMoves = new EnumMap<GHOST, MOVE>(GHOST.class);

	public void recursiveMove(Game game, Node head){
		ArrayList<Node> n = head.getNeighbors();
		ArrayList<MOVE> moves = new ArrayList<MOVE>(Arrays.asList(head.getGameState().getPossibleMoves(head.getGameState().getPacmanCurrentNodeIndex())));
		for(Node node : n){
			if(node.getGameState() != null)
				if(moves.contains(node.getMove())){
					Game gameState = game.copy();
					gameState.advanceGame(node.getMove(), ghostMoves);
					node.setGameState(gameState);
					recursiveMove(gameState, node);
				}
		}
	}

	public boolean isCollided(Node node){
		int pacIndex = node.getGameState().getPacmanCurrentNodeIndex();
		for(GHOST ghost : GHOST.values())
			return node.getGameState().getGhostCurrentNodeIndex(ghost) == pacIndex;
		return false;
	}

	public int dfsSearch(Node node){
		Game gameState = node.getPredecessor().getGameState().copy();
		if (!isValidMove(gameState.getPossibleMoves(gameState.getPacmanCurrentNodeIndex()), node.getMove()))
			return Integer.MIN_VALUE;
		
		gameState.advanceGame(node.getMove(), ghostMoves);
		node.setGameState(gameState);
		
		ArrayList<Node> neighbors = node.getNeighbors();
		if (neighbors == null || node.getGameState().gameOver() || isCollided(node)) 
			return Evaluation.evaluateGameState(node); // end of branch return heuristic

		int bestValue = Integer.MIN_VALUE;
		for (Node neighbor : neighbors) {
			int value = dfsSearch(neighbor);
			if (value > bestValue)
				bestValue = value;
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
		this.ghostMoves.put(GHOST.BLINKY, game.getGhostLastMoveMade(GHOST.BLINKY));
		this.ghostMoves.put(GHOST.INKY, game.getGhostLastMoveMade(GHOST.INKY));
		this.ghostMoves.put(GHOST.PINKY, game.getGhostLastMoveMade(GHOST.PINKY));
		this.ghostMoves.put(GHOST.SUE, game.getGhostLastMoveMade(GHOST.SUE));

		Tree tree = new Tree(DEPTH);
		tree.getHeadNode().setGameState(game);
		ArrayList<Node> neighbors = tree.getHeadNode().getNeighbors();
		
		int leftValue = dfsSearch(neighbors.get(0));
		int rightValue = dfsSearch(neighbors.get(1));
		int upValue = dfsSearch(neighbors.get(2));
		int downValue = dfsSearch(neighbors.get(3));
		
		return Evaluation.getBestMove(leftValue, rightValue, upValue, downValue);
		
		
//		recursiveMove(game,tree.getHeadNode());
//
//		
//
//		int heuristic = Integer.MIN_VALUE;
//		for(Node node : neighbors)
//			if(node.getGameState() != null){
//				int value = dfsSearch(node);
//				if(value > heuristic){
//					heuristic = value;
//					myMove = node.getMove();
//				}
//			}
//		return myMove;
	}
}
