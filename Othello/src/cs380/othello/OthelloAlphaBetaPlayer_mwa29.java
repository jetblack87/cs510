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
				final int nextMoveScore = alphabeta(state, maxDepth,
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
	private int alphabeta(OthelloState state, int currentDepth, int alpha,
			int beta, boolean maximizingPlayer) {
		if (currentDepth == 0 || state.gameOver()) {
			return state.score();
		}
		if (maximizingPlayer) {
			int v = Integer.MIN_VALUE;
			for (final OthelloMove a : state.generateMoves()) {
				v = Helper.Integer_max(
						v,
						alphabeta(state.applyMoveCloning(a), currentDepth - 1,
								alpha, beta, false));
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
						alphabeta(state.applyMoveCloning(a), currentDepth - 1,
								alpha, beta, true));
				beta = Helper.Integer_min(beta, v);
				if (beta <= alpha) {
					break;
				}
			}

			return v;
		}
	}
}
