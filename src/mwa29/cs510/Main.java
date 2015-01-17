/**
 * CS510
 * Mark Albrecht
 */
package mwa29.cs510;

import java.io.File;

/**
 * The main entry into the program
 */
public class Main {

	/**
	 * @param args
	 *            the command line argument to the application
	 */
	public static void main(String[] args) {
		for (int i = 0; i < args.length - 1; i++) {
			if (args[i].equals("-file")) {
				filePath = args[i + 1];
			} else if (args[i].equals("-maxMoves")) {
				maxMoves = Integer.parseInt(args[i + 1]);
			}
		}

		SlidingBrickProblem sbp = new SlidingBrickProblem(new File(filePath));
		SlidingBrickProblem.randomWalks(sbp.initialGameState, maxMoves);
	}

	private static String filePath = "dat/SBP-level0.txt";
	private static int maxMoves = 100;
}
