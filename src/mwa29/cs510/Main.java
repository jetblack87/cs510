/**
 * CS510
 * Mark Albrecht
 */
package mwa29.cs510;

import java.io.File;
import java.time.Duration;
import java.time.Instant;

/**
 * The main entry into the program
 */
public class Main {

	private enum SolveType {
		RANDOM_WALK, BREADTH_FIRST, DEPTH_FIRST, ITERATIVE_DEPTH_FIRST;
	}

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
			} else if (args[i].equals("-solveType")) {
				solveType = SolveType.valueOf((args[i + 1]));
			}
		}

		Instant start = Instant.now();
		SlidingBrickProblem sbp = new SlidingBrickProblem(new File(filePath));
		switch (solveType) {
		case RANDOM_WALK:
			SlidingBrickProblem.randomWalks(sbp.initialGameState, maxMoves);
			break;
		case BREADTH_FIRST:
			SlidingBrickProblem.breadthFirst(sbp.initialGameState);
			break;
		case DEPTH_FIRST:
			SlidingBrickProblem.depthFirstRecurse(sbp.initialGameState, 0);
			break;
		case ITERATIVE_DEPTH_FIRST:
			for (int i = 1; i < maxMoves; i++) {
				if (SlidingBrickProblem.depthFirstRecurse(sbp.initialGameState,
						i)) {
					break;
				}
			}
			break;
		}
		Instant stop = Instant.now();
		System.out.println("Duration: " + Duration.between(start, stop));
	}

	private static String filePath = "dat/hw1/SBP-level0.txt";
	private static int maxMoves = 100;
	private static SolveType solveType = SolveType.RANDOM_WALK;
}
