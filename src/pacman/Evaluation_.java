package pacman;

import DecisionTree.Node;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;



public class Evaluation_ {

	public static int evaluateGameState(Node node, boolean IsCollided) {
		int c1 = 0;
		int c2 = 0;
		int c3 = 0;
		int score = 0;
		int ghostDist = 0;
		
		if(node.getDepth() >= 20)
			c1 = 1;
		if( !node.getGameState().gameOver() )
			c2 = 2;
		if( !IsCollided )
			c3 = 3;
		
		if( IsCollided )
			System.out.println(IsCollided);
		
		score = node.getGameState().getScore();
		
		int tmp = 0;
		ghostDist = Integer.MAX_VALUE;
		for(GHOST ghost : GHOST.values()){
			tmp = node.getGameState().getShortestPathDistance(node.getGameState().getPacmanCurrentNodeIndex(),node.getGameState().getGhostCurrentNodeIndex(ghost));
			if(ghostDist>tmp)
				ghostDist = tmp;
		}
		//System.out.println(score);
		return score + 100*(c1+c2+c3) + 100*ghostDist;

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
