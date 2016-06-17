package pacman.game.internal;

import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

/*
 * This class is used to compute the shortest path for the ghosts: as these may not reverse, one cannot use 
 * a simple look-up table. Instead, we use the pre-computed shortest path distances as an admissable
 * heuristic. Although AStar needs to be run every time a path is to be found, it is very quick and does
 * not expand too many nodes beyond those on the optimal path.
 */
public class AStarTWO extends AbstractAStar
{
	private static final int NUMJUNCTION = 10;
	@Override
	public double computeCost(Game game, int start, int target) {
		return AStarUtil.computeDistanceCost(game, start, target, NUMJUNCTION);
	}

	@Override
	public double computeHeuristic(Game game, int start, int target) {
		return game.getShortestPathDistance(start, target);
	}
	
	public MOVE getMoveTo(int powerPillIndex, Game game){
		int currPac = game.getPacmanCurrentNodeIndex();
		int [] path = computePathsAStar(currPac, powerPillIndex, game.getPacmanLastMoveMade(), game);
		return game.getMoveToMakeToReachDirectNeighbour(currPac, path[1]);
		
	}

	@Override
	public double addictionalCost(int index, Game game) {
		double cost = 0;
		if(!game.isJunction(index))
			return cost;
		
		cost +=500;
//		for(GHOST g : GHOST.values())
//			if(game.getGhostCurrentNodeIndex(g) == index){
//				cost +=1000000;
//				if(game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(g), DM.PATH) < 100)
//					cost +=6000000;
//			}
			//System.out.println(cost);
		return cost;
	}

}