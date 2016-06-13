/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DecisionTree;
import java.util.ArrayList;

import pacman.game.Constants.MOVE;
/**
 *
 * @author Marmik
 */

// creates a tree for easy BFS/DFS/Iterative Deepening traversals
public class Tree {
	

	private Node headNode;
	
	public Tree(int depth) {
		headNode = new Node();
		
		ArrayList<Node> currentDepthNodes = new ArrayList<Node>();
		ArrayList<Node> nextDepthNodes = new ArrayList<Node>();
		currentDepthNodes.add(headNode);
		headNode.setDepth(0);
		for (int i = 0; i < depth; i++) {
			for (Node node : currentDepthNodes) {
				Node left = new Node(MOVE.LEFT, node);
				Node right = new Node(MOVE.RIGHT, node);
				Node up = new Node(MOVE.UP, node);
				Node down = new Node(MOVE.DOWN, node);
				
				left.setDepth(node.getDepth()+1);
				right.setDepth(node.getDepth()+1);
				up.setDepth(node.getDepth()+1);
				down.setDepth(node.getDepth()+1);
				
				nextDepthNodes.add(left);
				nextDepthNodes.add(right);
				nextDepthNodes.add(up);
				nextDepthNodes.add(down);
				
				ArrayList<Node> neighbors = new ArrayList<Node>(4);
				neighbors.add(left);
				neighbors.add(right);
				neighbors.add(up);
				neighbors.add(down);
				node.setNeighbors(neighbors);
			}
			
			currentDepthNodes = nextDepthNodes;
			nextDepthNodes = new ArrayList<Node>();
		}
	}
	
	public Node getHeadNode() {
		return headNode;
	}
}