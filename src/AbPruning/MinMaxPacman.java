package AbPruning;

import java.util.ArrayList;
import java.util.EnumMap;

import DecisionTree.Node;
import DecisionTree.Tree;
import pacman.Evaluation;
import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class MinMaxPacman extends Controller<MOVE>
{
	private MOVE myMove=MOVE.NEUTRAL;
	static private int DEPTH = 5;
	Controller<EnumMap<GHOST,MOVE>> ghostController;
	
	public MinMaxPacman(Controller<EnumMap<GHOST,MOVE>> ghostController){
		this.ghostController = ghostController;
	}

	public MOVE getMove(Game game, long timeDue) 
	{
		abTree tree = new abTree(game);
		mmComputeTree(tree.getHeadNode(),true);
		minimax(tree.getHeadNode());
		return tree.getHeadNode().getMove();
	}


	private void minimax(Node headNode) {
		ArrayList<Node> neighbors = headNode.getNeighbors();
		if(neighbors == null || neighbors.size() == 0){
			return;
		}
		// TODO
		
			
		
	}

	private void mmComputeTree(Node node, boolean isMax) {
		Game gameState = node.getGameState().copy();
		ArrayList<Container> nextStates = new ArrayList<Container>();
		if(isMax)
			nextStates = getNextPACMANMoves(gameState);
		else
			nextStates = getNextGHOSTMoves(gameState);

		for(Container g : nextStates){
			Node child = new Node(g.pacMove, node, isMax);
			child.setDepth(node.getDepth()+1);
			child.setGameState(g.game);
			child.setProbability(g.probability);
			node.addNeighbor(child);
		}

		if(node.getDepth() < DEPTH)
			for(Node n : node.getNeighbors())
				mmComputeTree(n, !isMax);
		else{
			node.setUtility(Evaluation.evaluateGameState(node.getGameState()));
		}
	}



	private ArrayList<Container> getNextGHOSTMoves(Game gameState) {
		ArrayList<Container> toReturn = new ArrayList<Container>();
		ArrayList<MOVE[]> moves = new ArrayList<MOVE[]>();
		for(GHOST ghost : GHOST.values()){
			int ghostIndex = gameState.getGhostCurrentNodeIndex(ghost);
			moves.add(gameState.getPossibleMoves(ghostIndex));
		}
		
		for(int i = 0; i<moves.get(0).length; i++){
			for(int j = 0; j<moves.get(1).length; j++){
				for(int k = 0; k<moves.get(2).length; k++){
					for(int x = 0; x<moves.get(3).length; x++){
						Game tmpGame = gameState.copy();
						EnumMap<GHOST, MOVE> ghostStruct = ghostController.getMove(gameState,-1);
						ghostStruct.replace(GHOST.values()[0], moves.get(0)[i]);
						ghostStruct.replace(GHOST.values()[1], moves.get(1)[j]);
						ghostStruct.replace(GHOST.values()[2], moves.get(2)[k]);
						ghostStruct.replace(GHOST.values()[3], moves.get(3)[x]);
						tmpGame.advanceGame(MOVE.NEUTRAL, ghostStruct);
						Container c = new Container();
						c.game = tmpGame;
						c.probability = 1/(moves.get(0).length*moves.get(1).length*moves.get(2).length*moves.get(3).length);
						toReturn.add(c);
					}
				}
			}
		}
		return toReturn;
	}


	private ArrayList<Container> getNextPACMANMoves(Game gameState) {
		int currIndex = gameState.getPacmanCurrentNodeIndex();
		MOVE[] moves = gameState.getPossibleMoves(currIndex);
		ArrayList<Container> toReturn = new ArrayList<Container>();
		EnumMap<GHOST, MOVE> ghostMove = ghostController.getMove(gameState,-1);
		for(MOVE move : moves){
			Game tmpGame = gameState.copy();
			tmpGame.advanceGame(move, ghostMove);
			Container c = new Container();
			c.game = tmpGame;
			c.pacMove = move;
			toReturn.add(c);
		}
		return toReturn;
	}

	boolean isValidMove(MOVE[] validMoves, MOVE move) {
		for (MOVE validMove : validMoves) {
			if (move == validMove) return true;
		}
		return false;
	}
	
	private class Container{
		public Game game;
		public double probability;
		public MOVE pacMove;
		
		public Container(){
			this.pacMove = MOVE.NEUTRAL;
			this.probability = 1;
		}
	}
}
