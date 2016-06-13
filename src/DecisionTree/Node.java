


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DecisionTree;

import java.util.ArrayList;
import pacman.game.Constants.MOVE;
import pacman.game.Game;


/**
 *
 * @author Marmik
 */

public class Node {
	
	private Game gameState;
	private ArrayList<Node> neighbors;
	private Node predecessor;
	private MOVE move;
	private boolean visited;
	private int depth;

	public Node() {
		this(MOVE.NEUTRAL, null);
	}
	
	public Node(MOVE move, Node predecessor) {
		this.move = move;
		this.predecessor = predecessor;
	}
	
	public ArrayList<Node> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(ArrayList<Node> neighbors) {
		this.neighbors = neighbors;
	}

	public Node getPredecessor() {
		return predecessor;
	}

	public void setPredecessor(Node predecessor) {
		this.predecessor = predecessor;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public MOVE getMove() {
		return move;
	}

	public void setMove(MOVE move) {
		this.move = move;
	}
	
	public Game getGameState() {
		return gameState;
	}
	
	public void setGameState(Game gameState) {
		this.gameState = gameState;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}
}
