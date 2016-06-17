package pacman.entries.pacman;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Random;
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
public class MyRandomPacMan extends Controller<MOVE>
{
	private Controller<EnumMap<GHOST,MOVE>> controllerGhosts; 
	private ArrayList<Integer> iIndex = new ArrayList<Integer>();
	private static int SIZE = 55;
	private ArrayList<Integer> index;
	private Container[] bestMoves;
	private int level = -1;
	private ArrayList<Container> moves;
	private int modality = -1;

	public MyRandomPacMan (Controller<EnumMap<GHOST,MOVE>> ghosts){
		this.controllerGhosts = ghosts;
		this.moves = new ArrayList<Container>();
	}

	public void computeMOVE(Game game, ArrayList<Integer> curr, int modality){
		Random generator = new Random(System.currentTimeMillis());
		switch(modality){
		case 0:
			index = new ArrayList<Integer>();
			for(int i = 0; i<SIZE && curr.size() !=0; i++)
				index.add(curr.remove(generator.nextInt(curr.size())));
			break;
		case 1:
			break;
		case 2:
			index = new ArrayList<Integer>();
			for(int i = 0; i<SIZE && curr.size() !=0; i++)
				index.add(curr.remove(generator.nextInt(curr.size())));
			break;
		case 3:			
			break;
		case 4:
			index = new ArrayList<Integer>();
			for(int i = 0; i<SIZE && curr.size() !=0; i++)
				index.add(curr.remove(generator.nextInt(curr.size())));
			break;
		case 5:
			break;
		case 6:
			index = new ArrayList<Integer>();
			for(int i = 0; i<SIZE && curr.size() !=0; i++)
				index.add(curr.remove(generator.nextInt(curr.size())));
			break;
		case 7:
			break;
		default:
			return;
		} 
		
		ArrayList<Container> localMoves = new ArrayList<Container>();

		for(int i=0; i<index.size(); i++){
			int k = 0;
			int gotoPill = index.get(i);
			Game gameState = game.copy();
			Container c = new Container();
			while(gameState.getPacmanCurrentNodeIndex() != gotoPill){
				int currPac = gameState.getPacmanCurrentNodeIndex();	
				MOVE last = gameState.getPacmanLastMoveMade() != null? gameState.getPacmanLastMoveMade() : MOVE.NEUTRAL;
				MOVE path = null;
				switch(modality){
				case 0: case 2: case 4: case 6:
					path = gameState.getApproximateNextMoveTowardsTarget(currPac, gotoPill, last, DM.PATH);
					break;
				case 1: case 3: case 5: case 7:
					path = gameState.getNextMoveTowardsTarget(currPac, gotoPill, DM.PATH);
					break;		
				}
				gameState.advanceGame(path, controllerGhosts.getMove());
				if(k == 0)
					c.next = path;	
				k++;
			}
			c.heuristic = Evaluation_.evaluateGameState(gameState);
			c.game = gameState;
			moves.add(c);
			localMoves.add(c);
		}

		for(Container move : localMoves)
			if(move.heuristic >= bestMoves[modality].heuristic)
				bestMoves[modality] = move;


		if(bestMoves[modality].game.getPacmanNumberOfLivesRemaining() < game.getPacmanNumberOfLivesRemaining())
			computeMOVE(game, curr, modality+1);			
	}


	public MOVE getMove(Game game, long timeDue) 
	{
		if(level != game.getCurrentLevel()){
			int[] index = game.getActivePillsIndices();
			for(int i = 0; i<index.length; i++)
				iIndex.add(index[i]);
			level = game.getCurrentLevel();
			System.out.println("NextLevel!!");
		}

		bestMoves = new Container[8];
		bestMoves[0] = new Container();
		bestMoves[1] = new Container();
		bestMoves[2] = new Container();
		bestMoves[3] = new Container();
		bestMoves[4] = new Container();
		bestMoves[5] = new Container();
		bestMoves[6] = new Container();
		bestMoves[7] = new Container();

		ArrayList<Integer> copyIndex = new ArrayList<Integer>(iIndex);
		moves.clear();
		modality = 0;
		computeMOVE(game, copyIndex, modality);
		Container best = new Container();

		if(modality < 8){
			for(Container m0 : moves)
				if(best.heuristic < m0.heuristic)
					best = m0;
			return best.next;
		}

		for(Container m1 : moves)
			if(game.getPacmanNumberOfLivesRemaining() != m1.game.getPacmanNumberOfLivesRemaining())
				moves.remove(m1);			
		for(Container m2 : moves)
			if(m2.heuristic > best.heuristic)
				best = m2;	

		return best.next;
	}

	private class Container{
		public MOVE next = MOVE.NEUTRAL;
		public Integer heuristic = Integer.MIN_VALUE;
		public Game game;
	}
}
