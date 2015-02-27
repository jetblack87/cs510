package cs380.othello;

public class OthelloMinimaxPlayer_mwa29 extends OthelloPlayer {

	/**
	 * Indicates infinite max depth
	 */
	public static final int INFINITE = -1;

	/**
	 * The maximum depth to recurse. Defaults to INFINITE.
	 */
	protected int maxDepth = INFINITE;

	/**
	 * The default constructor
	 */
	public OthelloMinimaxPlayer_mwa29() {
	}

	/**
	 * Constructs an {@link OthelloMinimaxPlayer_mwa29} using the given
	 * maxDepth;
	 * 
	 * @param maxDepth
	 *            The highest depth this player will search.
	 */
	public OthelloMinimaxPlayer_mwa29(final int maxDepth) {
		this();
		this.maxDepth = maxDepth;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cs380.othello.OthelloPlayer#getMove(cs380.othello.OthelloState)
	 */
	@Override
	public OthelloMove getMove(final OthelloState state) {
		return minimaxDecision(state);
	}

	/**
	 * The main entrance to the minimax search.
	 * 
	 * @param state
	 *            The initial state
	 * @return The move to take
	 */
	protected OthelloMove minimaxDecision(final OthelloState state) {
		OthelloMove returnMove = null;
		int returnMoveScore = Integer.MIN_VALUE;

		for (final OthelloMove a : state.generateMoves()) {
			if (null == returnMove) {
				returnMove = a;
				returnMoveScore = Helper.playerScoreValue(
						state.nextPlayerToMove,
						state.applyMoveCloning(returnMove).score());
			} else {
				final int nextMoveScore = minimax(state, maxDepth, false);
				if (nextMoveScore > returnMoveScore) {
					returnMove = a;
					returnMoveScore = nextMoveScore;
				}
			}
		}

		return returnMove;
	}

	/**
	 * Performs minimax algorithm
	 *
	 * Based on the pseudocode on the minimax Wikipedia page:
	 * https://en.wikipedia.org/wiki/Minimax
	 * 
	 * @param state
	 *            the current state
	 * @param currentDepth
	 *            the current depth
	 * @param maximizingPlayer
	 *            whether this is being called for the maximizing player
	 * @return the heuristic score
	 */
	private int minimax(OthelloState state, int currentDepth,
			boolean maximizingPlayer) {
		if (currentDepth == 0 || state.gameOver()) {
			return state.score();
		}
		if (maximizingPlayer) {
			int bestValue = Integer.MIN_VALUE;
			for (final OthelloMove a : state.generateMoves()) {
				int val = minimax(state.applyMoveCloning(a), currentDepth - 1,
						false);
				bestValue = Helper.Integer_max(bestValue, val);
			}
			return bestValue;
		} else {
			int bestValue = Integer.MAX_VALUE;
			for (final OthelloMove a : state.generateMoves()) {
				int val = minimax(state.applyMoveCloning(a), currentDepth - 1,
						true);
				bestValue = Helper.Integer_min(bestValue, val);
			}
			return bestValue;
		}
	}
}
