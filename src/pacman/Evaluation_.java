package pacman;

import java.util.Arrays;
import java.util.EnumMap;

import org.apache.commons.lang3.ArrayUtils;

import DecisionTree.Node;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;




public class Evaluation_ {

	public  static final int MIN_GHOST_DIST = 10;
	public static final int CROWDED_DISTANCE=30;

	public static long evaluateGameState(Game game) {
		Node node = new Node();
		node.setGameState(game);
		return evaluateGameState(node, false); 
	}

	public static Game initGame;


	public static long evaluateGameState(Node node, boolean IsCollided) {
		Game g = node.getGameState();
		int pacIdx = g.getPacmanCurrentNodeIndex();
		long shortestGhostDist = Long.MAX_VALUE; long secondShortestGhostDist = Long.MAX_VALUE;
		long ghostDist=0; 
		long toReturn; long a=1;


		/*Calcolo distanza minima dai ghost*/		
		long tmp = 0;
		for(GHOST ghost : GHOST.values()){
			if(g.getGhostLairTime(ghost) == 0 ) continue;
			
			int ghostIdx = g.getGhostCurrentNodeIndex(ghost);
			tmp = g.getShortestPathDistance(pacIdx,ghostIdx);
			if(!g.isGhostEdible(ghost)){
				if(shortestGhostDist>tmp){
					secondShortestGhostDist = shortestGhostDist;
					shortestGhostDist = tmp;
				}
			}
		}
		/* Se abbiamo una distanza più corta ed una seconda distanza più corta, facciamo la media */	
		if(shortestGhostDist != Long.MAX_VALUE && shortestGhostDist !=-1 && shortestGhostDist<MIN_GHOST_DIST )
			if(secondShortestGhostDist != Long.MAX_VALUE && secondShortestGhostDist !=-1 && secondShortestGhostDist<MIN_GHOST_DIST ) 
				ghostDist =   ((shortestGhostDist+secondShortestGhostDist)/2)*10000;
			else  //Altrimenti ci mettiamo alla distanza più corta
				ghostDist = shortestGhostDist*10000;
		else //Se non abbiamo nessuna delle due distanze allora ci mettiamo ad una distanza minima
			ghostDist = MIN_GHOST_DIST*10000;

		/*Preferiamo la mossa che ci porta verso la pallina (energetica o non) più vicina*/
		int[] pillIndices = ArrayUtils.addAll(g.getActivePillsIndices(), g.getActivePowerPillsIndices());




		/*	for(GHOST ghost : GHOST.values()){
			pacManGolosity+= g.getGhostEdibleTime(ghost);
		}*/

		//		int[] activePillIndices = g.getActivePillsIndices();
		//		int[] activePowerPillIndices = g.getActivePowerPillsIndices();	
		//		int[] pillIndices = new int[activePillIndices.length+activePowerPillIndices.length];	
		//		System.arraycopy(activePillIndices, 0, pillIndices, 0, activePillIndices.length);
		//		System.arraycopy(activePowerPillIndices, 0, pillIndices, activePillIndices.length-1, activePowerPillIndices.length);
		long shortestPillDistance =  g.getShortestPathDistance(pacIdx,g.getClosestNodeIndexFromNodeIndex(pacIdx, pillIndices, DM.PATH));
		if(g.getScore()>100000)
			a=10;
		toReturn =  50*(long)g.getScore()  + ghostDist + (long)node.getGameState().getPacmanNumberOfLivesRemaining()*100000000*a + (200 - shortestPillDistance);
		if(toReturn<0)
			System.out.println("Ovf");
		return toReturn;
	}

	private static int[] getTwoClosestGhosts(Game g, int pacIdx){
		int[] toReturn = new int[2];
		int[] tmp = new int[4]; 
		int r=0;
		/*Calcolo distanza da tutti i ghostt*/		
		int shortestGhostDist = 0; int secondShortestGhostDist =0;
		for(GHOST ghost : GHOST.values()){
			int ghostIdx = g.getGhostCurrentNodeIndex(ghost);
			if(!g.isGhostEdible(ghost))
				tmp[r] = g.getShortestPathDistance(pacIdx,ghostIdx); r++;
		}

		Arrays.sort(tmp);
		toReturn[0] = tmp[0]; toReturn[1] = tmp[1];
		return toReturn;
	}

	private static boolean isGoingTowardPacman(Game game, int pacIdx){
		int k = 0; int j=0;
		int distances[][] = new int[2][2];
		Game tmpGame = game.copy();
		int[] closestGhosts = getTwoClosestGhosts(game, pacIdx);
		for(int i=0;i<closestGhosts.length;i++){
			if(!ArrayUtils.contains(closestGhosts, -1)){
				distances[k][j] = game.getManhattanDistance(pacIdx,closestGhosts[i]);
				k++;
			}
		}
		k=0;j++;

		/*Calcolo mosse al tempo t-1*/
		EnumMap<GHOST,MOVE> moves = new EnumMap<GHOST,MOVE>(GHOST.class);
		for(GHOST g: GHOST.values()){
			moves.put(g, game.getGhostLastMoveMade(g).opposite());
		}
		tmpGame.advanceGhosts(moves);

		for(int i=0;i<closestGhosts.length;i++){
			if(!ArrayUtils.contains(closestGhosts, -1)){
				distances[k][j] = game.getManhattanDistance(pacIdx,closestGhosts[i]);
				k++;
			}
		}

		if(distances[0][0]<distances[0][1] && distances[1][0]<distances[1][1] )
			return true;
		else
			return false;
	}

	private static boolean isCrowded(Game game)
	{
		GHOST[] ghosts=GHOST.values();
		float distance=0;

		for (int i=0;i<ghosts.length-1;i++)
			for(int j=i+1;j<ghosts.length;j++)
				distance+=game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghosts[i]),game.getGhostCurrentNodeIndex(ghosts[j]));

		return (distance/6) < CROWDED_DISTANCE ? true : false;
	}

	public static MOVE getBestMove(long leftValue, long rightValue, long upValue, long downValue) {

		MOVE bestMove = MOVE.NEUTRAL;
		long bestValue = Long.MIN_VALUE;
		if (leftValue != Long.MIN_VALUE && leftValue > bestValue) {
			bestMove = MOVE.LEFT;
			bestValue = leftValue;
		}
		if (rightValue != Long.MIN_VALUE && rightValue > bestValue) {
			bestMove = MOVE.RIGHT;
			bestValue = rightValue;
		}
		if (upValue != Long.MIN_VALUE && upValue > bestValue) {
			bestMove = MOVE.UP;
			bestValue = upValue;
		}
		if (downValue != Long.MIN_VALUE && downValue > bestValue) {
			bestMove = MOVE.DOWN;
			bestValue = downValue;
		}

		return bestMove;
	}

}
