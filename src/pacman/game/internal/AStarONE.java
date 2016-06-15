package pacman.game.internal;

import java.util.ArrayList;
import java.util.PriorityQueue;

import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

/*
 * This class is used to compute the shortest path for the ghosts: as these may not reverse, one cannot use 
 * a simple look-up table. Instead, we use the pre-computed shortest path distances as an admissable
 * heuristic. Although AStar needs to be run every time a path is to be found, it is very quick and does
 * not expand too many nodes beyond those on the optimal path.
 * 
 */
public class AStarONE extends AbstractAStar
{

	@Override
	public double computeCost() {
		return 0;
	}
	
}
