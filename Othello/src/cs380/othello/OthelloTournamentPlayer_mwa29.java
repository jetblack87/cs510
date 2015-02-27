package cs380.othello;

public class OthelloTournamentPlayer_mwa29 extends OthelloMinimaxPlayer_mwa29 {

	/**
	 * The total time in milliseconds to spend on each move
	 * 
	 * Defaults to 100
	 */
	private int maxTime = 100;

	/**
	 * The default constructor
	 */
	public OthelloTournamentPlayer_mwa29() {
		super();
	}

	/**
	 * Constructs an {@link OthelloMinimaxPlayer_mwa29} using the given maxTime;
	 * 
	 * @param maxTime
	 *            The total time in milliseconds to spend on each move
	 */
	public OthelloTournamentPlayer_mwa29(int maxTime) {
		this.maxTime = maxTime;
	}

	@Override
	public OthelloMove getMove(OthelloState state) {
		return minimaxDecision(state, System.currentTimeMillis());
	}

	/**
	 * The minimax decision routine that takes startTime
	 * 
	 * @param state
	 *            the starting state
	 * @param startTime
	 *            the starting time in milliseconds
	 * @return the move to make
	 */
	protected OthelloMove minimaxDecision(final OthelloState state,
			final long startTime) {
		OthelloMove returnMove = null;
		int returnMoveScore = Integer.MIN_VALUE;

		for (final OthelloMove a : state.generateMoves()) {
			if (null == returnMove) {
				returnMove = a;
				returnMoveScore = Helper.playerScoreValue(
						state.nextPlayerToMove,
						state.applyMoveCloning(returnMove).score());
			} else {
				final int nextMoveScore = alphabeta(state, startTime,
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
	 * Performs minimax using tournament processing
	 * 
	 * @param state
	 *            the current state
	 * @param startTime
	 *            the time the algorithm started
	 * @param alpha
	 *            the alpha variable
	 * @param beta
	 *            the beta variable
	 * @param maximizingPlayer
	 *            whether this is being called for the maximizing player
	 * @return the heuristic score
	 */
	private int alphabeta(OthelloState state, long startTime, int alpha,
			int beta, boolean maximizingPlayer) {
		if (System.currentTimeMillis() - startTime > maxTime
				|| state.gameOver()) {
			return state.score();
		}
		if (maximizingPlayer) {
			int v = Integer.MIN_VALUE;
			for (final OthelloMove a : state.generateMoves()) {
				v = Helper.Integer_max(
						v,
						alphabeta(state.applyMoveCloning(a), startTime - 1,
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
						alphabeta(state.applyMoveCloning(a), startTime - 1,
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
