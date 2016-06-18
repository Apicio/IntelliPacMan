package pacman;

import org.apache.commons.lang3.ArrayUtils;
import DecisionTree.Node;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;




public class EvaluationHeuristic {

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
		if(shortestGhostDist != Integer.MAX_VALUE && shortestGhostDist !=-1 && shortestGhostDist<MIN_GHOST_DIST )
			if(secondShortestGhostDist != Integer.MAX_VALUE && secondShortestGhostDist !=-1 && secondShortestGhostDist<MIN_GHOST_DIST ) 
				ghostDist =   ((shortestGhostDist+secondShortestGhostDist)/2)*10000;
			else  //Altrimenti ci mettiamo alla distanza più corta
				ghostDist = shortestGhostDist*10000;
		else //Se non abbiamo nessuna delle due distanze allora ci mettiamo ad una distanza minima
			ghostDist = MIN_GHOST_DIST*10000;
		
		/*Preferiamo la mossa che ci porta verso la pallina (energetica o non) più vicina*/
//		int[] activePillIndices = g.getActivePillsIndices();
//		int[] activePowerPillIndices = g.getActivePowerPillsIndices();
		int[] pillIndices = ArrayUtils.addAll(g.getActivePillsIndices(), g.getActivePowerPillsIndices());	
//		int[] pillIndices = new int[activePillIndices.length+activePowerPillIndices.length];	
//		System.arraycopy(activePillIndices, 0, pillIndices, 0, activePillIndices.length);
//		System.arraycopy(activePowerPillIndices, 0, pillIndices, activePillIndices.length-1, activePowerPillIndices.length);
		
		int shortestPillDistance =  g.getShortestPathDistance(pacIdx,g.getClosestNodeIndexFromNodeIndex(pacIdx, pillIndices, DM.PATH));
		toReturn =  50*g.getScore()  + ghostDist + node.getGameState().getPacmanNumberOfLivesRemaining()*100000000 + (200 - shortestPillDistance);
	/*	
		if(50*g.getScore()<0)
			System.out.println("Ovf 1");
		if(ghostDist<0)
			System.out.println("Ovf 2");
		if(temp<0){
			System.out.println("Ovf 3");
		    System.out.println("n_live: "+node.getGameState().getPacmanNumberOfLivesRemaining());
		}
		if(shortestPillDistance<0)
			System.out.println("Ovf 4");
		*/
		if(toReturn<0)
			System.out.println("Ovf ALL");
		
		return toReturn;
	}
	
    private static boolean isCrowded(Game game)
    {
    	GHOST[] ghosts=GHOST.values();
        float distance=0;
        
        for (int i=0;i<ghosts.length-1;i++)
            for(int j=i+1;j<ghosts.length;j++)
                distance+=game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghosts[i]),game.getGhostCurrentNodeIndex(ghosts[j]));
        
        return (distance/6)<CROWDED_DISTANCE ? true : false;
    }

	public static MOVE getBestMove(long leftValue, long rightValue, long upValue, long downValue) {

		MOVE bestMove = MOVE.NEUTRAL;
		long bestValue = Integer.MIN_VALUE;
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
