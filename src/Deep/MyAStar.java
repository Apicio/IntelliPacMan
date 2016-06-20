package Deep;

import java.util.ArrayList;
import java.util.EnumMap;
import pacman.EvaluationHeuristic;
import pacman.controllers.Controller;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.internal.MyAStarHeuristic;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class MyAStar extends Controller<MOVE>
{
	private Controller<EnumMap<GHOST,MOVE>> controllerGhosts; 
	private MyAStarHeuristic a = new MyAStarHeuristic();;
	private int[] iIndex;
	private ArrayList<Container> moves = new ArrayList<Container>();;

	public MyAStar (Controller<EnumMap<GHOST,MOVE>> ghosts){
		this.controllerGhosts = ghosts;
	}

	public MOVE computeMOVE(Game game, boolean isAll){
		int[] index = isAll? iIndex : game.getActivePillsIndices();

		for(int i=0; i<index.length; i++){
			int gotoPill = index[i];
			Game gameState = game.copy();
			Container c = new Container();
			int[] fullPath;
			if(isAll)
				fullPath = a.computePathsAStar(gameState.getPacmanCurrentNodeIndex(), gotoPill, gameState);
			else
				fullPath = a.computePathsAStar(gameState.getPacmanCurrentNodeIndex(), gotoPill, gameState.getPacmanLastMoveMade(), gameState);
			MOVE[] fullMovePath = new MOVE[fullPath.length-1];
			for(int o=1; o<fullPath.length; o++){
				fullMovePath[o-1] = game.getMoveToMakeToReachDirectNeighbour(fullPath[o-1], fullPath[o]);
				gameState.advanceGame(fullMovePath[o-1], controllerGhosts.getMove());
				c.next = o==1? fullMovePath[0] : c.next;
				if(gameState.wasPacManEaten())
					break;
			}
			c.heuristic = EvaluationHeuristic.evaluateGameState(gameState);
			c.game = gameState;
			moves.add(c);
		}

		Container best = new Container();
		for(Container move : moves)
			if(move.heuristic >= best.heuristic)
				best = move;

		if(best.game.getPacmanNumberOfLivesRemaining()<game.getPacmanNumberOfLivesRemaining() && !isAll)
			return computeMOVE(game, !isAll);
		else
			return best.next;			
	}


	public MOVE getMove(Game game, long timeDue) 
	{
		if(game.isLvLup()){
			iIndex = game.getPillIndices();		
			System.out.println("LvL UP!!");
		}
		a.createGraph(game.getCurrentMaze().graph);
		moves.clear();
		return computeMOVE(game, false);
	}

	private class Container{
		public MOVE next = MOVE.NEUTRAL;
		public Integer heuristic = Integer.MIN_VALUE;
		public Game game;
	}
}
