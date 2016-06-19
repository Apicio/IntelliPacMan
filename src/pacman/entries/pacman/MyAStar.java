package pacman.entries.pacman;

import java.util.ArrayList;
import java.util.EnumMap;
import org.apache.commons.lang3.ArrayUtils;
import pacman.controllers.Controller;
import pacman.game.Constants.DM;
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
	public  static final int MIN_GHOST_DIST = 10;
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
			}
			c.heuristic = evaluateGameState(gameState);
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
		public Long heuristic = Long.MIN_VALUE;
		public Game game;
	}


	public static long evaluateGameState(Game g) {
		int pacIdx = g.getPacmanCurrentNodeIndex();
		int shortestGhostDist = Integer.MAX_VALUE; int secondShortestGhostDist = Integer.MAX_VALUE;
		int ghostDist=0;
		long toReturn;

		/*Calcolo distanza minima dai ghost*/		
		int tmp = 0;
		for(GHOST ghost : GHOST.values()){
			int ghostIdx = g.getGhostCurrentNodeIndex(ghost);
			if(!g.isGhostEdible(ghost)){
				tmp = g.getShortestPathDistance(pacIdx,ghostIdx);
				if(shortestGhostDist>tmp){
					secondShortestGhostDist = shortestGhostDist;
					shortestGhostDist = tmp;
				}
			}
		}

		/* Se abbiamo una distanza più corta ed una seconda distanza più corta, facciamo la media */	
		if(shortestGhostDist != Integer.MAX_VALUE && shortestGhostDist !=-1 && shortestGhostDist<MIN_GHOST_DIST)
			if(secondShortestGhostDist != Integer.MAX_VALUE && secondShortestGhostDist !=-1 && secondShortestGhostDist<MIN_GHOST_DIST ) 
				ghostDist =   ((shortestGhostDist+secondShortestGhostDist)/2)*10000;
			else  //Altrimenti ci mettiamo alla distanza più corta
				ghostDist = shortestGhostDist*10000;
		else //Se non abbiamo nessuna delle due distanze allora ci mettiamo ad una distanza minima
			ghostDist = MIN_GHOST_DIST*10000;

		/*Preferiamo la mossa che ci porta verso la pallina (energetica o non) più vicina*/
		int[] pillIndices = ArrayUtils.addAll(g.getActivePillsIndices(), g.getActivePowerPillsIndices());	
		int shortestPillDistance =  g.getShortestPathDistance(pacIdx,g.getClosestNodeIndexFromNodeIndex(pacIdx, pillIndices, DM.PATH));

		/*Se Ms Pacman ha perso tutte le vite tranne una, allora preferiamo i percorsi che non ci protano a GameOver*/
		toReturn =  50*g.getScore()+ ghostDist + g.getPacmanNumberOfLivesRemaining()*100000000 + (200 - shortestPillDistance);
		return toReturn;
	}
}
