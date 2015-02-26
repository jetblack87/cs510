package cs380.othello;

public class OthelloMinimaxPlayer_mwa29 extends OthelloPlayer {

	/**
	 * Indicates infinite max depth
	 */
	public static final int INFINITE = 0;

	/**
	 * The maximum depth to recurse. Defaults to INFINITE.
	 */
	protected int maxDepth = INFINITE;

	/**
	 * The default constructor
	 */
	public OthelloMinimaxPlayer_mwa29() {
		// Default constructor
	}

	/**
	 * Constructs an {@link OthelloMinimaxPlayer_mwa29} using the given
	 * maxDepth;
	 * 
	 * @param maxDepth
	 *            The highest depth this player will search.
	 */
	public OthelloMinimaxPlayer_mwa29(final int maxDepth) {
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
				final int nextMoveScore = Helper.playerScoreValue(
						state.nextPlayerToMove,
						minValue(state.applyMoveCloning(a),
								state.nextPlayerToMove, 0));
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
	 * @param state
	 *            The current state
	 * @param currentPlayer
	 *            The player that is making the move
	 * @param currentDepth
	 *            The current depth of the search
	 * @return The maximized score of the min-values for all subsequent moves
	 */
	private int maxValue(final OthelloState state, final int currentPlayer,
			final int currentDepth) {
		if (state.gameOver()
				|| (INFINITE != maxDepth && currentDepth > maxDepth)) {
			return state.score();
		} else {
			int v = Integer.MIN_VALUE;
			for (final OthelloMove a : state.generateMoves()) {
				v = Helper.Integer_max(v, Helper.playerScoreValue(
						currentPlayer,
						minValue(state.applyMoveCloning(a), currentPlayer,
								currentDepth + 1)));
			}
			return v;
		}
	}

	/**
	 * The min-value portion of the minimax search
	 * 
	 * @param state
	 *            The current state
	 * @param currentPlayer
	 *            The player that is making the move
	 * @param currentDepth
	 *            The current depth of the search
	 * @return The minimized score of the max-values for all subsequent moves
	 */
	private int minValue(final OthelloState state, final int currentPlayer,
			final int currentDepth) {
		if (state.gameOver()
				|| (INFINITE != maxDepth && currentDepth > maxDepth)) {
			return state.score();
		} else {
			int v = Integer.MAX_VALUE;
			for (final OthelloMove a : state.generateMoves()) {
				v = Helper.Integer_min(v, Helper.playerScoreValue(
						currentPlayer,
						maxValue(state.applyMoveCloning(a), currentPlayer,
								currentDepth + 1)));
			}
			return v;
		}
	}
}
