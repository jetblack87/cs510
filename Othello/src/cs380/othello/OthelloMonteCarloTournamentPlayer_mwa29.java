package cs380.othello;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OthelloMonteCarloTournamentPlayer_mwa29 extends OthelloPlayer {

	/**
	 * The total time in milliseconds to spend on each move
	 * 
	 * Defaults to 100
	 */
	private int maxTime = 100;

	private List<OthelloNode> foo;

	public OthelloMonteCarloTournamentPlayer_mwa29() {
	}

	public OthelloMonteCarloTournamentPlayer_mwa29(int maxTime) {
		this.maxTime = maxTime;
	}

	/**
	 * 
	 * its parent, its children, the action that led to this state, the number
	 * of times this node has been visited and the average score found so far
	 * for this node.
	 */
	private static class OthelloNode {
		private OthelloState state;
		private OthelloNode parent;
		private List<OthelloNode> children;
		private OthelloMove action;
		private int g;
		private int score;

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof OthelloNode) {
				OthelloNode that = (OthelloNode) obj;
				return (null != this.state && null != that.state && stateEquals(
						this.state, that.state));
			}
			return false;
		}
	}

	private static boolean stateEquals(OthelloState one, OthelloState two) {
		for (int i = 0; i < one.boardSize; i++) {
			for (int j = 0; j < one.boardSize; j++) {
				if (one.board[i][j] != two.board[i][j]) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public OthelloMove getMove(OthelloState state) {
		foo = new ArrayList<OthelloNode>();
		OthelloNode root = createNode(state, null, null);
		long startTime = System.currentTimeMillis();
		while (System.currentTimeMillis() - startTime < maxTime) {
			OthelloNode node = treePolicy(root);
			if (node != null) {
				OthelloNode node2 = defaultPolicy(node);
				node2.score = score(node2);
				backup(node, node2.score);
			}
		}
		return bestChild(root).action;
	}

	/**
	 * just creates a game tree node from the given board. As explained in
	 * class, each node in the game tree stores (at least): its parent, its
	 * children, the action that led to this state, the number of times this
	 * node has been visited and the average score found so far for this node.
	 * 
	 * @param state
	 *            the state
	 * @return the best child
	 */
	private OthelloNode createNode(OthelloState state, OthelloMove action,
			OthelloNode parent) {
		OthelloNode node = new OthelloNode();
		node.state = state;
		node.parent = parent;
		node.action = action;
		node.children = new ArrayList<OthelloNode>();
		return node;
	}

	/**
	 * if the next player to move in node is PLAYER1, it returns the child with
	 * the maximum average score, if the next player to move in the node is
	 * PLAYER2, then it returns the child with the minimum average score.
	 * 
	 * @param root
	 * @return
	 */
	private OthelloNode bestChild(OthelloNode node) {
		final OthelloState state = node.state;
		OthelloMove bestMove = null;
		int returnMoveScore = 0;
		switch (state.nextPlayerToMove) {
		case OthelloState.PLAYER1:
			returnMoveScore = Integer.MIN_VALUE;
			for (OthelloNode child : node.children) {
				if (child.score > returnMoveScore) {
					bestMove = child.action;
					returnMoveScore = child.score;
				}
			}
			break;
		case OthelloState.PLAYER2:
			returnMoveScore = Integer.MAX_VALUE;
			for (OthelloNode child : node.children) {
				if (child.score < returnMoveScore) {
					bestMove = child.action;
					returnMoveScore = child.score;
				}
			}
			break;
		}
		OthelloNode returnNode = createNode(state.applyMoveCloning(bestMove),
				bestMove, node);
		return returnNode;
	}

	/**
	 * this function does the following:
	 * 
	 * If 'node' still has any children that are not in the tree, then it
	 * generates one of those children ('newnode'), it adds 'newnode' as a child
	 * to 'node', and returns 'newnode'.
	 * 
	 * If 'node' is a terminal node (no actions can be performed). Then it
	 * returns "node"
	 * 
	 * If 'node' is not a terminal but all its children are in the tree, then:
	 * 90% of the times "nodetmp = bestChild(node)", and 10% of the times
	 * "nodetmp = [a child of node at random]" (if you are curious, this is
	 * called an epsilon-greedy strategy). Then, the function returns
	 * "treePolicy(nodetmp)"
	 * 
	 * @param root
	 * @return
	 */
	private OthelloNode treePolicy(OthelloNode node) {
		OthelloNode returnNode = null;
		List<OthelloMove> moves = node.state.generateMoves();

		if (moves.isEmpty()) {
			return node;
		}

		for (OthelloMove move : moves) {
			OthelloState moveState = node.state.applyMoveCloning(move);
			OthelloNode newNode = createNode(moveState, move, node);
			if (!isNodeInTree(newNode)) {
				node.children.add(newNode);
				foo.add(newNode);
				return newNode;
			}
		}

		Random rand = new Random();
		boolean ninetyPercent = (rand.nextInt(10) % 10 != 0);
		OthelloNode nodetmp = null;
		if (ninetyPercent) {
			nodetmp = bestChild(node);
		} else {
			OthelloMove randMove = moves.get(rand.nextInt(moves.size()));
			nodetmp = createNode(node.state.applyMoveCloning(randMove),
					randMove, node);
		}

		returnNode = treePolicy(nodetmp);

		return returnNode;
	}

	/**
	 * Determined whether the given node is in the tree.
	 * 
	 * @param node
	 *            the node to check
	 * @return true if the node is in the tree
	 */
	private boolean isNodeInTree(OthelloNode node) {
		return foo.contains(node);
	}

	/**
	 * this function just uses the random agent to select actions at random for
	 * each player, until the game is over, and returns the final state of the
	 * game.
	 * 
	 * @param node
	 * @return
	 */
	private OthelloNode defaultPolicy(OthelloNode node) {
		OthelloRandomPlayer player = new OthelloRandomPlayer();
		OthelloNode currNode = node;
		OthelloMove move = null;
		do {
			move = player.getMove(currNode.state);
			currNode = createNode(currNode.state.applyMoveCloning(move), move,
					currNode);
		} while (null != move);
		return currNode;
	}

	/**
	 * returns the score of the game (you can use the built-in score function in
	 * the Othello package, you do NOT need to use the complex evaluation
	 * function you created for the previous assignment)
	 * 
	 * @param node
	 *            the node to whose score is to be returned
	 * @return the score of the game
	 */
	private int score(OthelloNode node) {
		return node.state.score();
	}

	/**
	 * increments in 1 the number of times "node" has been visited, and updates
	 * the average score in "node" with the value "score". If "node" has a
	 * parent, then it calls "backup(node.parent,score)".
	 * 
	 * @param node
	 * @param score
	 */
	private void backup(OthelloNode node, int score) {
		node.g++;
		node.score = score;
		if (null != node.parent) {
			backup(node.parent, score);
		}
	}
}