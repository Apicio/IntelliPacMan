package pacman.entries.pacman;

import java.util.ArrayList;

import java.util.EnumMap;

import pacman.EvaluationHeuristic;
import pacman.controllers.Controller;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.internal.AStar;
import pacman.game.internal.AStarONE;
import pacman.game.internal.AStarTWO;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class MyAStar extends Controller<MOVE>
{
	private Controller<EnumMap<GHOST,MOVE>> controllerGhosts; 
	private int[] index;
	private AStar a;

	public MyAStar (Controller<EnumMap<GHOST,MOVE>> ghosts){
		this.controllerGhosts = ghosts;
	}

	public MOVE computeMOVE(Game game, boolean isAll){
		//Random generator = new Random(System.currentTimeMillis());
		//		index = new ArrayList<Integer>();
		//		ArrayList<Integer> allIndex = new ArrayList<Integer>(iIndex);
		//		while(index.size() != SIZE){
		//			int from = generator.nextInt(2);
		//			if(from<1){
		//				int v = currActive.remove(generator.nextInt(currActive.size()));
		//				boolean isGhostIndex = false;
		//				for(GHOST g : GHOST.values()){
		//					if(game.getGhostCurrentNodeIndex(g) == v){
		//						isGhostIndex = true;
		//					}
		//				}
		//				if(!isGhostIndex)
		//					index.add(v);
		//			}
		//			if(from>=1){
		//				int v = allIndex.remove(generator.nextInt(allIndex.size()));
		//				boolean isGhostIndex = false;
		//				for(GHOST g : GHOST.values()){
		//					if(game.getGhostCurrentNodeIndex(g) == v){
		//						isGhostIndex = true;
		//					}
		//				}
		//				if(!isGhostIndex)
		//					index.add(v);
		//			}
		//		}
		//		for(GHOST g : GHOST.values()){
		//			if(game.isGhostEdible(g))
		//				index.add(game.getGhostCurrentNodeIndex(g));
		//		}
		ArrayList<Container> moves = new ArrayList<Container>();

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
		index = game.getPillIndices();
		a = new AStar();
		a.createGraph(game.getCurrentMaze().graph);
		return computeMOVE(game, false);
	}

	private class Container{
		public MOVE next = MOVE.NEUTRAL;
		public Long heuristic = Long.MIN_VALUE;
		public Game game;
	}
}
