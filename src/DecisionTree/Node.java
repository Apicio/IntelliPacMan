


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
	private int a = Integer.MIN_VALUE;
	private int b = Integer.MAX_VALUE;
	private int utility;
	public int getUtility() {
		return utility;
	}

	public void setUtility(int utility) {
		this.utility = utility;
	}
	private boolean isMax;
	private double probability;
	

	public Node() {
		this(MOVE.NEUTRAL, null);
	}
	
	public Node(MOVE move, Node predecessor) {
		this.move = move;
		this.predecessor = predecessor;
	}
	
	public Node(MOVE move, Node predecessor, boolean isMax) {
		this.move = move;
		this.predecessor = predecessor;
		this.isMax = isMax;
		this.neighbors = new ArrayList<Node>();
	}
	
	public ArrayList<Node> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(ArrayList<Node> neighbors) {
		this.neighbors = neighbors;
	}
	
	public void addNeighbor(Node node){
		this.neighbors.add(node);
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
	public int getA(){
		return a;
	}
	public int getB(){
		return b;
	}
	public void setA(int v){
		a = v;
	}
	public void setB(int v){
		b = v;
	}

	public boolean isMax() {
		return isMax;
	}

	public void setMax(boolean isMax) {
		this.isMax = isMax;
	}
	public double getProbability() {
		return probability;
	}
	public void setProbability(double probability) {
		this.probability = probability;
	}
}
