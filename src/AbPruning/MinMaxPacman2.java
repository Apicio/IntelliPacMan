package AbPruning;
import java.util.EnumMap;
import java.util.Random;
import pacman.EvaluationHeuristic;
import pacman.controllers.Controller;
import pacman.controllers.examples.RandomGhosts;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class MinMaxPacman2 extends Controller<MOVE>
{
	static int k=0;
	private MOVE myMove=MOVE.NEUTRAL;
	private int depth;
	private Controller<EnumMap<GHOST,MOVE>> ghostController;
	private boolean canReverse;
	private Random rnd = new Random(System.currentTimeMillis());


	public MinMaxPacman2(Controller<EnumMap<GHOST,MOVE>> ghostController, int depth, boolean canReverse){
		this.ghostController = ghostController;
		this.depth = depth;
		this.canReverse = canReverse;
	}

	public MOVE getMove(Game game, long timeDue) 
	{
		State head = new State();
		head.game = game;
		head.depth = 0;
		State[] next = getNextPACMANMoves(head);
		State[] comp = new State[next.length];
		int l = 0;
		for(State child : next){
			comp[l] = minimax(child, false);
			l++;
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
		if(node.depth == depth || node.game.getActivePillsIndices().length == 0){
			if(isMax)
				node.alpha = EvaluationHeuristic.evaluateGameState(node.game);
			else 
				node.beta = EvaluationHeuristic.evaluateGameState(node.game);
			return node;
		}

		if(isMax){
			State[] next = getNextPACMANMoves(node);
			for(State child : next){
				/* Propago le caratteristiche */
				child.alpha = node.alpha;
				child.beta = node.beta;
				child.depth = node.depth+1;
				node.alpha = Math.max(node.alpha, minimax(child,false).beta);
				if(node.beta <= node.alpha)
					return node;
			}
			return node;
		}else{
			State[] next = getNextGHOSTMoves(node);
			if(!(ghostController instanceof RandomGhosts)){
				for(State child : next){
					/* Propago le caratteristiche */
					child.alpha = node.alpha;
					child.beta = node.beta;
					child.depth = node.depth+1;	
					node.beta = Math.min(node.beta, minimax(child,true).alpha);
					if(node.beta <= node.alpha)
						return node;
				}
				return node;
			}else{
				double res = 0;
				for(State child : next)
					res += minimax(child,true).alpha;
				node.beta = res/next.length;
				return node;
			}
		}
	}

	private State[] getNextGHOSTMoves(State gameState){
		if(!(ghostController instanceof RandomGhosts))
			return getMINAggressiveMoves(gameState);
		else
			return getMINRandomMoves(gameState);
	}

	private State[] getMINRandomMoves(State gameState) {
		MOVE[][] moves = new MOVE[4][];
		int y = 0;
		for(GHOST ghost : GHOST.values()){
			moves[y] = gameState.game.getPossibleMoves(gameState.game.getGhostCurrentNodeIndex(ghost));
			y++;
		}
		
		State[] toReturn = new State[y-1];
		int l = 0;
		for(int i = 0; i<moves[0].length; i++){
			for(int j = 0; j<moves[1].length; j++){
				for(int k = 0; k<moves[2].length; k++){
					for(int x = 0; x<moves[3].length; x++){
						Game tmpGame = gameState.game.copy();
						EnumMap<GHOST, MOVE> ghostMoves = new EnumMap<GHOST, MOVE>(GHOST.class);						
						ghostMoves.put(GHOST.values()[0], moves[0][i]);
						ghostMoves.put(GHOST.values()[1], moves[1][j]);
						ghostMoves.put(GHOST.values()[2], moves[2][k]);
						ghostMoves.put(GHOST.values()[3], moves[3][x]);
						tmpGame.advanceGame(MOVE.NEUTRAL, ghostMoves);
						State c = new State();
						c.game = tmpGame;
						c.alpha = gameState.alpha;
						c.beta = gameState.beta;
						c.depth = gameState.depth+1;
						c.pacMove = gameState.pacMove;
						toReturn[l] = c;
						l++;
					}
				}
			}
		}
		return toReturn;
	}

	private State[] getMINAggressiveMoves(State gameState) {
		EnumMap<GHOST, MOVE> ghostStruct = this.ghostController.getMove(gameState.game,-1);
		Game tmpGame = gameState.game.copy();		
		if(!canReverse)
			tmpGame.advanceGame(MOVE.NEUTRAL, ghostStruct);
		else
			tmpGame.advanceGhosts(ghostStruct);
		
		State[] toReturn = new State[1];
		State c = new State();
		c.pacMove = gameState.pacMove;
		c.game = tmpGame;
		toReturn[0] = c;
		return toReturn;
	}


	private State[] getNextPACMANMoves(State gameState) {
		int currIndex = gameState.game.getPacmanCurrentNodeIndex();
		MOVE[] moves;
		if(!canReverse)
			moves = gameState.game.getPossibleMoves(currIndex, gameState.game.getPacmanLastMoveMade());
		else
			moves = gameState.game.getPossibleMoves(currIndex);
		shuffleArray(moves);
		State[] toReturn = new State[moves.length];
		EnumMap<GHOST, MOVE> ghostMoves = new EnumMap<GHOST, MOVE>(GHOST.class);	
		int l = 0;
		for(MOVE move : moves){
			Game tmpGame = gameState.game.copy();
			if(!canReverse)
				tmpGame.advanceGame(move, ghostMoves);
			else
				tmpGame.advancePacMan(move);
			State c = new State();
			c.game = tmpGame;
			if(gameState.depth == 0)
				c.pacMove = move;
			else
				c.pacMove = gameState.pacMove;
			toReturn[l] = c;
			l++;
		}
		return toReturn;
	}
	
	public void shuffleArray(MOVE[] ar)
	  {
	    // If running on Java 6 or older, use `new Random()` on RHS here
	    if(ar.length <2) return;
	    for (int i = ar.length - 1; i > 0; i--)
	    {
	      int index = rnd.nextInt(i + 1);
	      // Simple swap
	      MOVE a = ar[index];
	      ar[index] = ar[i];
	      ar[i] = a;
	    }
	  }

	private class State{
		public Integer depth = 0;
		public Game game;
		public MOVE pacMove = MOVE.NEUTRAL;;
		public double alpha = Integer.MIN_VALUE;
		public double beta = Integer.MAX_VALUE;
	}
}
