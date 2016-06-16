package pacman;

import DecisionTree.Node;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;




public class EvaluationGene {

	public  static final int MIN_GHOST_DIST = 10;
	public static int evaluateGameState(Game game) {
		Node node = new Node();
		node.setGameState(game);
		return evaluateGameState(node, false); 
	}
	public static long a;
	public static long b;
	public static long c;
	public static long d;	

	public static int evaluateGameState(Node node, boolean IsCollided) {
		Game g = node.getGameState();
		int pacIdx = g.getPacmanCurrentNodeIndex();
		int shortestGhostDist = Integer.MAX_VALUE; int secondShortestGhostDist = Integer.MAX_VALUE;
		int ghostDist=0;
		int toReturn;

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
		if(shortestGhostDist != Integer.MAX_VALUE && shortestGhostDist !=-1 && shortestGhostDist<MIN_GHOST_DIST )
			if(secondShortestGhostDist != Integer.MAX_VALUE && secondShortestGhostDist !=-1 && secondShortestGhostDist<MIN_GHOST_DIST ) 
				ghostDist =   ((shortestGhostDist+secondShortestGhostDist)/2)*10000;
			else  //Altrimenti ci mettiamo alla distanza più corta
				ghostDist = shortestGhostDist*10000;
		else //Se non abbiamo nessuna delle due distanze allora ci mettiamo ad una distanza minima
			ghostDist = MIN_GHOST_DIST*10000;
		
		/*Preferiamo la mossa che ci porta verso la pallina (energetica o non) più vicina*/
		int[] activePillIndices = g.getActivePillsIndices();
		int[] activePowerPillIndices = g.getActivePowerPillsIndices();
		int[] pillIndices = new int[activePillIndices.length+activePowerPillIndices.length];
		System.arraycopy(activePillIndices, 0, pillIndices, 0, activePillIndices.length);
		System.arraycopy(activePowerPillIndices, 0, pillIndices, activePillIndices.length-1, activePowerPillIndices.length);
		int shortestPillDistance =  g.getShortestPathDistance(pacIdx,g.getClosestNodeIndexFromNodeIndex(pacIdx, pillIndices, DM.PATH));

		toReturn = (int) (a*g.getScore()  + b*ghostDist + c*node.getGameState().getPacmanNumberOfLivesRemaining() + d*(200 - shortestPillDistance));
	
		return toReturn;
	}

	public static MOVE getBestMove(int leftValue, int rightValue, int upValue, int downValue) {

		MOVE bestMove = MOVE.NEUTRAL;
		int bestValue = Integer.MIN_VALUE;
		if (leftValue != Integer.MIN_VALUE && leftValue > bestValue) {
			bestMove = MOVE.LEFT;
			bestValue = leftValue;
		}
		if (rightValue != Integer.MIN_VALUE && rightValue > bestValue) {
			bestMove = MOVE.RIGHT;
			bestValue = rightValue;
		}
		if (upValue != Integer.MIN_VALUE && upValue > bestValue) {
			bestMove = MOVE.UP;
			bestValue = upValue;
		}
		if (downValue != Integer.MIN_VALUE && downValue > bestValue) {
			bestMove = MOVE.DOWN;
			bestValue = downValue;
		}

		return bestMove;
	}
	
}
