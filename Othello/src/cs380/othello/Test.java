package cs380.othello;

/**
 *
 * @author santi
 */
public class Test {

	public static void main(String args[]) {
		// Create the game state with the initial position for an 8x8 board:
		OthelloState state = new OthelloState(8);
		OthelloPlayer playerone = new OthelloRandomPlayer();
		OthelloPlayer playertwo = new OthelloRandomPlayer();

		for (int i = 0; i < args.length - 1; i++) {
			if (args[i].equals("-boardsize")) {
				state = new OthelloState(Integer.parseInt(args[i + 1]));
			} else if (args[i].equals("-playerone")) {
				playerone = getPlayer(args[i + 1]);
			} else if (args[i].equals("-playertwo")) {
				playerone = getPlayer(args[i + 1]);
			}
		}

		OthelloPlayer players[] = { playerone, playertwo };

		do {
			// Display the current state in the console:
			System.out.println("\nCurrent state, "
					+ OthelloState.PLAYER_NAMES[state.nextPlayerToMove]
					+ " to move:");
			System.out.print(state);

			// Get the move from the player:
			OthelloMove move = players[state.nextPlayerToMove].getMove(state);
			System.out.println(move);
			state = state.applyMoveCloning(move);
		} while (!state.gameOver());

		// Show the result of the game:
		System.out.println("\nFinal state with score: " + state.score());
		System.out.println(state);
	}

	/**
	 * Returns an {@link OthelloPlayer} object based on the given playerString
	 * 
	 * @param playerString
	 *            the argument for this player
	 * @return the new {@link OthelloPlayer} object based on the given
	 *         playerString
	 */
	private static OthelloPlayer getPlayer(String playerString) {
		String splitString[] = playerString.split(":");
		String playerType = splitString[0];
		int maxDepth = Integer.MIN_VALUE;
		if (splitString.length > 1) {
			maxDepth = Integer.parseInt(splitString[1]);
		}
		if (playerType.equals("random")) {
			return new OthelloRandomPlayer();
		} else if (playerType.equals("minimax")) {
			if (maxDepth > Integer.MIN_VALUE) {
				return new OthelloMinimaxPlayer_mwa29(maxDepth);
			} else {
				return new OthelloMinimaxPlayer_mwa29();
			}
		} else {
			throw new RuntimeException("Unknown type supplied: " + playerType);
		}
	}
}
