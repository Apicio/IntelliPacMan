package AbPruning;

import java.util.ArrayList;

import DecisionTree.Node;
import pacman.game.Game;

public class abTree {
	
private Node headNode;

	public abTree(Game game) {
		headNode = new Node();
		headNode.setGameState(game);
		headNode.setDepth(0);
		headNode.setNeighbors(new ArrayList<Node>());
	}
	
	public Node getHeadNode() {
		return headNode;
	}

}
