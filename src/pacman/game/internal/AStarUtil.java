package pacman.game.internal;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;

public class AStarUtil {
	
	public static double computeDistanceCost(Game g,int pacIdx, int targetIdx, int numberOfCornes){
		int[] jIdxs = g.getJunctionIndices();
		int[] tmpJdist = new int[jIdxs.length]; 
		int[] jIndexes =  new int[numberOfCornes];
		int distToTaget = 0;
		int[] distXfromTarget = new int[numberOfCornes];
		int sumOfDistX=0; int sumOfDistXFromTarget = 0;

		/*Find nearest corners in respect of MsPacman position*/
		for(int i=0; i<jIdxs.length;i++)
			tmpJdist[i] = g.getManhattanDistance(pacIdx, jIdxs[i]);
		//Find respective indicies
		for(int i=0;i<jIndexes.length;i++){
			jIndexes[i] = g.getClosestNodeIndexFromNodeIndex(pacIdx, jIdxs, DM.MANHATTAN);
			removeElements(jIdxs, jIndexes[i]);
		}
		Arrays.sort(tmpJdist);

		/*Find distance from MsPacman to Target*/
		distToTaget = g.getManhattanDistance(pacIdx, targetIdx);

		/*Find distances from Xi corner to target*/
		
		for(int i=0; i<numberOfCornes; i++)
			distXfromTarget[i] = g.getManhattanDistance(jIndexes[i], targetIdx);

		/*Sums of distances*/
		for(int i=0; i<numberOfCornes; i++){
			sumOfDistX+=tmpJdist[i];
			sumOfDistXFromTarget+=distXfromTarget[i];
		}


		return (sumOfDistX + sumOfDistXFromTarget - distToTaget)*1000 ;
	}

	public static double computeGhostCostI(Game g){
		int[] jIdxs = g.getJunctionIndices();
		int[] tmpJdist = new int[jIdxs.length];
		double sumOfDistances  = 0;
		
		/*Find nearest corners in respect of ghost position*/
		for(GHOST ghost : GHOST.values()){
			int ghostIdx = g.getGhostCurrentNodeIndex(ghost);
			for(int i=0; i<jIdxs.length;i++)
				tmpJdist[i] = g.getManhattanDistance(ghostIdx, jIdxs[i]);
	
			Arrays.sort(tmpJdist);
			sumOfDistances += 5000/Math.pow(tmpJdist[0] +  tmpJdist[1], 2);
			
		}
		
		return sumOfDistances;
	}
	
	public static int[] removeElements(int[] input, int deleteMe) {
		List<Integer> result = new LinkedList<Integer>();

		for(int item : input)
			if(!(deleteMe == item))
				result.add(item);

		int[] res = new int[result.size()];
		for(int i=0;i<result.size();i++)
			res[i] = result.get(i);

		return res;
	}
}
