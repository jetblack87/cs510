package cs380.othello;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class OthelloAlphaBetaPlusPlayer_mwa29 extends
		OthelloMinimaxPlayer_mwa29 {

	/**
	 * The default constructor
	 */
	public OthelloAlphaBetaPlusPlayer_mwa29() {
		super();
	}

	/**
	 * Constructs an {@link OthelloMinimaxPlayer_mwa29} using the given
	 * maxDepth;
	 * 
	 * @param maxDepth
	 *            The highest depth this player will search.
	 */
	public OthelloAlphaBetaPlusPlayer_mwa29(int maxDepth) {
		super(maxDepth);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cs380.othello.OthelloMinimaxPlayer_mwa29#minimaxDecision(cs380.othello
	 * .OthelloState)
	 */
	@Override
	protected OthelloMove minimaxDecision(final OthelloState state) {
		OthelloMove returnMove = null;
		int returnMoveScore = Integer.MIN_VALUE;

		for (final OthelloMove a : state.generateMoves()) {
			if (null == returnMove) {
				returnMove = a;
				returnMoveScore = Helper.playerScoreValue(
						state.nextPlayerToMove,
						evaluation(state.applyMoveCloning(returnMove),
								state.otherPlayer(state.nextPlayerToMove)));
			} else {
				final int nextMoveScore = alphabeta(state,
						state.otherPlayer(state.nextPlayerToMove), maxDepth,
						Integer.MIN_VALUE, Integer.MAX_VALUE, false);

				if (nextMoveScore > returnMoveScore) {
					returnMove = a;
					returnMoveScore = nextMoveScore;
				}
			}
		}

		return returnMove;
	}

	/**
	 * Performs minimax using alpha-beta pruning
	 * 
	 * Based on the pseudocode on the Alpha-Beta Pruning Wikipedia page:
	 * https://en.wikipedia.org/wiki/Alpha%E2%80%93beta_pruning
	 * 
	 * @param state
	 *            the current state
	 * @param currentPlayer
	 *            the current player
	 * @param currentDepth
	 *            the current depth
	 * @param alpha
	 *            the alpha variable
	 * @param beta
	 *            the beta variable
	 * @param maximizingPlayer
	 *            whether this is being called for the maximizing player
	 * @return the heuristic score
	 */
	private int alphabeta(OthelloState state, int currentPlayer,
			int currentDepth, int alpha, int beta, boolean maximizingPlayer) {
		if (currentDepth == 0 || state.gameOver()) {
			return evaluation(state, currentPlayer);
		}
		if (maximizingPlayer) {
			int v = Integer.MIN_VALUE;
			for (final OthelloMove a : state.generateMoves()) {
				v = Helper.Integer_max(
						v,
						alphabeta(state.applyMoveCloning(a), currentPlayer,
								currentDepth - 1, alpha, beta, false));
				alpha = Helper.Integer_max(alpha, v);
				if (beta <= alpha) {
					break;
				}
			}
			return v;
		} else {
			int v = Integer.MAX_VALUE;
			for (final OthelloMove a : state.generateMoves()) {
				v = Helper.Integer_min(
						v,
						alphabeta(state.applyMoveCloning(a), currentPlayer,
								currentDepth - 1, alpha, beta, true));
				beta = Helper.Integer_min(beta, v);
				if (beta <= alpha) {
					break;
				}
			}

			return v;
		}
	}

	/**
	 *
	 * Evaluation based on http://radagast.se/othello/Help/strategy.html
	 *
	 * Things to account for: Number of stable discs (including wedged discs:
	 * against the edge and already flanked by other player) Few unstable C or
	 * X-squares (C-squares are the squares at a2, a7, b1, b8, g1, g8, h2, and
	 * h7. X-squares are the squares at b2, b7, g2, and g7. These squares should
	 * only be occupied with care.) Mobility: Number of subsequent moves Small
	 * number of frontier discs Gain of tempo Parity Unbalanced edges Stoner
	 * traps
	 * 
	 * @param state
	 *            the state to evaluate
	 * @param currentPlayer
	 *            the current player
	 * @return the heuristic number based on this evaluation
	 */
	private int evaluation(OthelloState state, int currentPlayer) {
		int evalue = 0;
		int otherPlayer = state.otherPlayer(currentPlayer);

		evalue += numberOfStableDiscs(state, currentPlayer);
		evalue -= numberOfStableDiscs(state, otherPlayer);
		evalue -= numberOfCAndXSquares(state, currentPlayer);
		evalue += numberOfCAndXSquares(state, currentPlayer);
		evalue += mobility(state, currentPlayer);
		evalue -= numberOfFrontierDiscs(state, currentPlayer);
		evalue += tempo(state, currentPlayer);
		evalue += parity(state, currentPlayer);
		evalue += unbalancedEdged(state, currentPlayer);
		evalue += stonerTraps(state, currentPlayer);

		return evalue;
	}

	/**
	 * (C-squares are the squares at a2, a7, b1, b8, g1, g8, h2, and h7.
	 * X-squares are the squares at b2, b7, g2, and g7. These squares should
	 * only be occupied with care.)
	 * 
	 * @param state
	 *            the current state
	 * @param currentPlayer
	 *            the current player
	 * @return the number of c C-squares and X-squares occupied by currentPlayer
	 */
	private int numberOfCAndXSquares(OthelloState state, int currentPlayer) {
		int board[][] = state.board;
		int boardSize = state.boardSize;

		List<Point> cSquares = new ArrayList<Point>();
		cSquares.add(new Point(0, 1)); // a2
		cSquares.add(new Point(0, boardSize - 2)); // a7
		cSquares.add(new Point(1, 0)); // b1
		cSquares.add(new Point(1, boardSize - 1)); // b8
		cSquares.add(new Point(boardSize - 2, 0)); // g1
		cSquares.add(new Point(boardSize - 2, boardSize - 1)); // g8
		cSquares.add(new Point(boardSize - 1, 1)); // h2
		cSquares.add(new Point(boardSize - 1, boardSize - 2)); // h7

		List<Point> xSquares = new ArrayList<Point>();
		xSquares.add(new Point(1, 1)); // b2
		xSquares.add(new Point(1, boardSize - 2)); // b7
		xSquares.add(new Point(boardSize - 2, 1)); // g2
		xSquares.add(new Point(boardSize - 2, boardSize - 2)); // g7

		int count = 0;

		for (Point p : cSquares) {
			if (board[p.x][p.y] == currentPlayer) {
				count++;
			}
		}
		for (Point p : xSquares) {
			if (board[p.x][p.y] == currentPlayer) {
				count++;
			}
		}

		return 0;
	}

	private int numberOfStableDiscs(OthelloState state, int player) {
		int count = 0;
		int board[][] = state.board;
		int boardSize = state.boardSize;
		for (int j = 0; j < boardSize; j++) {
			for (int i = 0; i < boardSize; i++) {
				if (player == board[i][j] && isStable(board, boardSize, i, j)) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * Returns true if this position is stable.
	 * 
	 * @param board
	 *            the game board
	 * @param boardSize
	 *            the size of the game board
	 * @param i
	 *            the i axis
	 * @param j
	 *            the j axis
	 * @return true if this position is stable
	 */
	private boolean isStable(int[][] board, int boardSize, int i, int j) {
		if (isCorner(board, boardSize, i, j)) {
			return true;
		} else if (isWedged(board, boardSize, i, j)) {
			return true;
		} else {
			// TODO: Addition cases where a disc is stable
		}
		return false;
	}

	/**
	 * Returns true if this position is wedged.
	 * 
	 * @param board
	 *            the game board
	 * @param boardSize
	 *            the size of the game board
	 * @param i
	 *            the i axis
	 * @param j
	 *            the j axis
	 * @return true if this position is wedged
	 */
	private boolean isWedged(int[][] board, int boardSize, int i, int j) {
		if (isCorner(board, boardSize, i, j)) {
			return false; // cannot be wedged if it's a corner
		}
		final int currentPlayer = board[i][j];
		final int otherPlayer = OthelloState.PLAYER1 == currentPlayer ? OthelloState.PLAYER2
				: OthelloState.PLAYER1;
		final Edge edge = isEdge(board, boardSize, i, j);
		switch (edge) {
		case NOT_EDGE:
			return false;
		case NORTH:
		case SOUTH:
			if (otherPlayer == board[i - 1][j]
					&& otherPlayer == board[i + 1][j]) {
				return true;
			} else {
				return false;
			}
		case EAST:
		case WEST:
			if (otherPlayer == board[i][j - 1]
					&& otherPlayer == board[i][j + 1]) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * Enum used to indicate if a position is an edge
	 */
	enum Edge {
		NOT_EDGE, NORTH, EAST, SOUTH, WEST
	};

	/**
	 * Returns true if this position is an edge.
	 * 
	 * @param board
	 *            the game board
	 * @param boardSize
	 *            the size of the game board
	 * @param i
	 *            the i axis
	 * @param j
	 *            the j axis
	 * @return true if this position is an edge
	 */
	private Edge isEdge(int[][] board, int boardSize, int i, int j) {
		if (0 == i) {
			return Edge.WEST;
		} else if (boardSize - 1 == i) {
			return Edge.EAST;
		} else if (0 == j) {
			return Edge.NORTH;
		} else if (boardSize - 1 == j) {
			return Edge.SOUTH;
		} else {
			return Edge.NOT_EDGE;
		}
	}

	/**
	 * Returns true if this position is a corner.
	 * 
	 * @param board
	 *            the game board
	 * @param boardSize
	 *            the size of the game board
	 * @param i
	 *            the i axis
	 * @param j
	 *            the j axis
	 * @return true if this position is a corner
	 */
	private boolean isCorner(int[][] board, int boardSize, int i, int j) {
		if (0 == i || boardSize - 1 == i) {
			if (0 == j || boardSize - 1 == j) {
				return true;
			}
		} else if (0 == j || boardSize - 1 == j) {
			if (0 == i || boardSize - 1 == i) {
				return true;
			}
		}
		return false;
	}

	/**
	 * The number of available moves for the current player
	 * 
	 * @param state
	 *            the current state
	 * @param currentPlayer
	 *            the current player
	 * @return the number of available moves for the current player
	 */
	private int mobility(OthelloState state, int currentPlayer) {
		return state.generateMoves(currentPlayer).size();
	}

	private int numberOfFrontierDiscs(OthelloState state, int currentPlayer) {
		// TODO Auto-generated method stub
		return 0;
	}

	private int tempo(OthelloState state, int currentPlayer) {
		// TODO Auto-generated method stub
		return 0;
	}

	private int parity(OthelloState state, int currentPlayer) {
		// TODO Auto-generated method stub
		return 0;
	}

	private int unbalancedEdged(OthelloState state, int currentPlayer) {
		// TODO Auto-generated method stub
		return 0;
	}

	private int stonerTraps(OthelloState state, int currentPlayer) {
		// TODO Auto-generated method stub
		return 0;
	}
}
