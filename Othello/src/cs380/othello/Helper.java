package cs380.othello;

public class Helper {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Integer#max(int, int)
	 */
	public static int Integer_max(int one, int two) {
		if (one > two) {
			return one;
		} else {
			return two;
		}		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Integer#min(int, int)
	 */
	public static int Integer_min(int one, int two) {
		if (one < two) {
			return one;
		} else {
			return two;
		}
	}

	/**
	 * Translates the given Othello score for the given player. If it's PLAYER1,
	 * the given score is correct. If it's PLAYER2, the score is negated since
	 * he thinks that a lower score is better.
	 * 
	 * @param player
	 *            The player that is making the next move
	 * @param score
	 *            The score to translate
	 * @return the translated score
	 */
	public static int playerScoreValue(final int player, final int score) {
		if (OthelloState.PLAYER2 == player) {
			return score * -1;
		} else {
			return score;
		}
	}

}
