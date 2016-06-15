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
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 * 
 */
public class MinMaxPacman extends Controller<MOVE>
{
	static int k=0;
	private MOVE myMove=MOVE.NEUTRAL;
	static private int DEPTH = 10;
	Controller<EnumMap<GHOST,MOVE>> ghostController;
	private Random rnd=new Random();
	private MOVE[] allMoves=MOVE.values();


	public MinMaxPacman(Controller<EnumMap<GHOST,MOVE>> ghostController){
		this.ghostController = ghostController;
	}

	public MOVE getMove(Game game, long timeDue) 
	{
		//abTree tree = new abTree(game);
		//mmComputeTree(tree.getHeadNode(),true);
		State head = new State();
		head.game = game;
		head.depth = 0;
		ArrayList<State> next = getNextPACMANMoves(head);
		ArrayList<State> comp = new ArrayList<State>();
		for(State child : next){
			comp.add(minimax(child, false));
		}
		Integer alpha = Integer.MIN_VALUE;
		for(State find : comp){
			if(alpha<find.beta){
				alpha = find.beta;
				myMove = find.pacMove;
			}
		}
		ArrayList<MOVE[]> moves = new ArrayList<MOVE[]>();
		for(GHOST ghost : GHOST.values()){
			int ghostIndex = game.getGhostCurrentNodeIndex(ghost);
			moves.add(game.getPossibleMoves(ghostIndex));
		}		

			return myMove;
	}

	//	private State minimax(State node, boolean isMax) {
	//		if(node.depth == DEPTH){
	//			node.utility = Evaluation_.evaluateGameState(node.game);
	//			System.out.println(isMax);
	//			return node;
	//		}
	//		
	//		if(isMax){
	//			node.utility = Integer.MIN_VALUE;
	//			ArrayList<State> next = getNextPACMANMoves(node);
	//			for(State child : next){
	//				System.out.println(child.pacMove);
	//				node.utility = Math.max(node.utility, minimax(child,false).utility);
	//				if(node.utility >= node.beta)
	//					return node;
	//				node.alpha = Math.max(node.alpha, node.utility);
	//			}
	//			return node;
	//		}else{
	//			node.utility = Integer.MAX_VALUE;
	//			ArrayList<State> next = getNextGHOSTMoves(node);
	//			for(State child : next){
	//				node.utility = Math.min(node.utility, minimax(child,true).utility);
	//				if(node.utility <= node.alpha)
	//					return node;
	//				node.beta = Math.min(node.beta, node.utility);
	//			}
	//			return node;	
	//		}
	//	}
	private State minimax(State node, boolean isMax) {
		if(node.depth == DEPTH){
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
			for(State child : next){
				node.beta = Math.min(node.beta, minimax(child,true).alpha);
				if(node.beta <= node.alpha)
					return node;
			}
			return node;	
		}
	}
	//	private void mmComputeTree(Node node, boolean isMax) {
	//		Game gameState = node.getGameState().copy();
	//		ArrayList<State> nextStates = new ArrayList<State>();
	//		if(isMax)
	//			nextStates = getNextPACMANMoves(gameState);
	//		else
	//			nextStates = getNextGHOSTMoves(gameState);
	//
	//		for(State g : nextStates){
	//			Node child = new Node(g.pacMove, node, isMax);
	//			child.setDepth(node.getDepth()+1);
	//			child.setGameState(g.game);
	//			child.setProbability(g.probability);
	//			node.addNeighbor(child);
	//		}
	//
	//		if(node.getDepth() < DEPTH)
	//			for(Node n : node.getNeighbors())
	//				mmComputeTree(n, !isMax);
	//		else{
	//			node.setUtility(Evaluation.evaluateGameState(node.getGameState()));
	//		}
	//	}



	//	private ArrayList<State> getNextGHOSTMoves(State gameState) {
	//		ArrayList<State> toReturn = new ArrayList<State>();
	//		ArrayList<MOVE[]> moves = new ArrayList<MOVE[]>();
	//		for(GHOST ghost : GHOST.values()){
	//			int ghostIndex = gameState.game.getGhostCurrentNodeIndex(ghost);
	//			moves.add(gameState.game.getPossibleMoves(ghostIndex));
	//		}		
	//		
	//		
	//		for(int i = 0; i<moves.get(0).length; i++){
	//			for(int j = 0; j<moves.get(1).length; j++){
	//				for(int k = 0; k<moves.get(2).length; k++){
	//					for(int x = 0; x<moves.get(3).length; x++){
	//						Game tmpGame = gameState.game.copy();
	//						EnumMap<GHOST, MOVE> ghostStruct = ghostController.getMove(gameState.game,-1);
	//						ghostStruct.replace(GHOST.values()[0], moves.get(0)[i]);
	//						ghostStruct.replace(GHOST.values()[1], moves.get(1)[j]);
	//						ghostStruct.replace(GHOST.values()[2], moves.get(2)[k]);
	//						ghostStruct.replace(GHOST.values()[3], moves.get(3)[x]);
	//						tmpGame.advanceGame(MOVE.NEUTRAL, ghostStruct);
	//						State c = new State();
	//						c.game = tmpGame;
	//						c.alpha = gameState.alpha;
	//						c.beta = gameState.beta;
	//						c.depth = gameState.depth+1;
	//						c.pacMove = gameState.pacMove;
	//					//	c.probability = 1/(moves.get(0).length*moves.get(1).length*moves.get(2).length*moves.get(3).length);
	//						toReturn.add(c);
	//					}
	//				}
	//			}
	//		}
	//		System.out.println(toReturn.size());
	//		return toReturn;
	//	}

	private ArrayList<State> getNextGHOSTMoves(State gameState) {
		ArrayList<State> toReturn = new ArrayList<State>();
		EnumMap<GHOST, MOVE> ghostStruct = this.ghostController.getMove(gameState.game,-1);
		Game tmpGame = gameState.game.copy();
		tmpGame.advanceGame(MOVE.NEUTRAL, ghostStruct);
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
		MOVE[] moves = gameState.game.getPossibleMoves(currIndex);
		ArrayList<State> toReturn = new ArrayList<State>();
		EnumMap<GHOST, MOVE> ghostMove = ghostController.getMove(gameState.game,-1);
		for(MOVE move : moves){
			Game tmpGame = gameState.game.copy();
			tmpGame.advanceGame(move, ghostMove);
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
		public double probability = 1;
		public MOVE pacMove = MOVE.NEUTRAL;;
		public Integer alpha = Integer.MIN_VALUE;
		public Integer beta = Integer.MAX_VALUE;
		public Integer utility;
	}
	/*	
	private class Container{
		public Game game;
		public double probability;
		public MOVE pacMove;

		public Container(){
			this.pacMove = MOVE.NEUTRAL;
			this.probability = 1;
		}
	} */
}
