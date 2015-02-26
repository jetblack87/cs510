package cs380.othello;

public class OthelloAlphaBetaPlayer_mwa29 extends OthelloMinimaxPlayer_mwa29 {

	/**
	 * The default constructor
	 */
	public OthelloAlphaBetaPlayer_mwa29() {
		super();
	}

	/**
	 * Constructs an {@link OthelloMinimaxPlayer_mwa29} using the given
	 * maxDepth;
	 * 
	 * @param maxDepth
	 *            The highest depth this player will search.
	 */
	public OthelloAlphaBetaPlayer_mwa29(int maxDepth) {
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
						state.applyMoveCloning(returnMove).score());
			} else {
				final int nextMoveScore = Helper.playerScoreValue(
						state.nextPlayerToMove,
						minValue(state.applyMoveCloning(a),
								state.nextPlayerToMove, 0, returnMoveScore,
								Integer.MAX_VALUE));
				if (nextMoveScore > returnMoveScore) {
					returnMove = a;
					returnMoveScore = nextMoveScore;
				}
			}
		}

		return returnMove;
	}

	/**
	 * The max-value portion of the minimax search
	 * 
	 * Performs alpha-beta pruning
	 * 
	 * @param state
	 *            The current state
	 * @param currentPlayer
	 *            The player that is making the move
	 * @param currentDepth
	 *            The current depth of the search
	 * @param currentMin
	 *            The current minimum value found in the preceding minValue
	 *            processing
	 * @return The maximized score of the min-values for all subsequent moves
	 */
	private int maxValue(final OthelloState state, final int currentPlayer,
			final int currentDepth, int alpha, int beta) {
		if (state.gameOver()
				|| (INFINITE != maxDepth && currentDepth > maxDepth)) {
			return state.score();
		} else {
			int v = Integer.MIN_VALUE;
			for (final OthelloMove a : state.generateMoves()) {
				v = Helper.Integer_max(v, Helper.playerScoreValue(
						currentPlayer,
						minValue(state.applyMoveCloning(a), currentPlayer,
								currentDepth + 1, alpha, beta)));

				// Perform pruning
				alpha = Helper.Integer_max(alpha, v);
				if (beta <= alpha) {
					return v;
				}
			}
			return v;
		}
	}

	/**
	 * The min-value portion of the minimax search
	 * 
	 * Performs alpha-beta pruning
	 * 
	 * @param state
	 *            The current state
	 * @param currentPlayer
	 *            The player that is making the move
	 * @param currentDepth
	 *            The current depth of the search
	 * @param currentMax
	 *            The current maximum value found in the preceding maxValue
	 *            processing
	 * @return The minimized score of the max-values for all subsequent moves
	 */
	private int minValue(final OthelloState state, final int currentPlayer,
			final int currentDepth, int alpha, int beta) {
		if (state.gameOver()
				|| (INFINITE != maxDepth && currentDepth > maxDepth)) {
			return state.score();
		} else {
			int v = Integer.MAX_VALUE;
			for (final OthelloMove a : state.generateMoves()) {
				v = Helper.Integer_min(v, Helper.playerScoreValue(
						currentPlayer,
						maxValue(state.applyMoveCloning(a), currentPlayer,
								currentDepth + 1, alpha, beta)));

				// Perform pruning
				beta = Helper.Integer_min(beta, v);
				if (beta <= alpha) {
					return v;
				}
			}
			return v;
		}
	}
}
