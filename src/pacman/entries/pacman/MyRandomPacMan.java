package pacman.entries.pacman;

import java.util.ArrayList;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Random;

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
public class MyRandomPacMan extends Controller<MOVE>
{
	private Controller<EnumMap<GHOST,MOVE>> controllerGhosts; 
	private ArrayList<Integer> iIndex = new ArrayList<Integer>();
	private boolean firstTime = true;
	private static int SIZE = 50;
	private ArrayList<Integer> index;
	private Container[] bestMoves;

	public MyRandomPacMan (Controller<EnumMap<GHOST,MOVE>> ghosts){
		this.controllerGhosts = ghosts;
	}

	public void computeMOVE(Game game, long timeDue, ArrayList<Integer> currActive, HashSet<Integer> used, int modality){
		Random generator = new Random(System.currentTimeMillis());
		switch(modality){
		case 0:
			index = new ArrayList<Integer>();
			for(int i = 0; i<SIZE; i++)
				index.add(currActive.get(generator.nextInt(currActive.size())));
			break;
		case 1:
			used.addAll(index);
			break;
		case 2:
			index = new ArrayList<Integer>();
			index.addAll(currActive);
			index.removeAll(used);
			break;
		case 3:			
			used.addAll(index);
			break;
		case 4:
			index = new ArrayList<Integer>();
			ArrayList<Integer> getFrom = new ArrayList<Integer>(iIndex);
			getFrom.removeAll(used);
			for(int i = 0; i<SIZE; i++)
				index.add(getFrom.get(generator.nextInt(getFrom.size())));
			break;
		case 5:
			used.addAll(index);
			break;
		case 6:
		case 7:
			index = new ArrayList<Integer>(iIndex);
			index.removeAll(used);
			break;
		}

		if(modality >= 6){
			System.out.println("TOP");
			if(used.size() + index.size() < 220)
				System.out.println("Errore");
		}

		ArrayList<Container> moves = new ArrayList<Container>();

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
				case 0:
				case 2:
				case 4:
				case 6:
					path = gameState.getApproximateNextMoveTowardsTarget(currPac, gotoPill, last, DM.PATH);
					break;
				case 1:
				case 3:
				case 5:
				case 7:
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
		}
		if(moves.isEmpty()) return;

		for(Container move : moves)
			if(move.heuristic >= bestMoves[modality].heuristic)
				bestMoves[modality] = move;
		

		if(bestMoves[modality].game.getPacmanNumberOfLivesRemaining() < game.getPacmanNumberOfLivesRemaining() && modality<7)
			computeMOVE(game, timeDue, currActive, used, modality+1);			
	}


	public MOVE getMove(Game game, long timeDue) 
	{
		if(firstTime){
			int[] index = game.getActivePillsIndices();
			for(int i = 0; i<index.length; i++)
				iIndex.add(index[i]);
			firstTime = false;
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

		ArrayList<Integer> activeIndex = new ArrayList<Integer>();
		int[] cindex = game.getActivePillsIndices();
		for(int i = 0; i<cindex.length; i++)
			activeIndex.add(cindex[i]);

		computeMOVE(game, timeDue, activeIndex, new HashSet<Integer>(), 0);

		Container toReturn = new Container();
		for(Container move : bestMoves){
			if(move.heuristic > toReturn.heuristic)
				toReturn = move;
		}

		return toReturn.next;
	}

	private class Container{
		public MOVE next = MOVE.NEUTRAL;
		public Integer heuristic = Integer.MIN_VALUE;
		public Game game;
	}
}
