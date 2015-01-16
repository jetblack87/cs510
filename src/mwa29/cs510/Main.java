/**
 * 
 */
package mwa29.cs510;

import java.io.File;

/**
 * @author mark
 *
 */
public class Main {

	private final static String DEFAULT_FILE = "dat/SBP-level0.txt";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length > 0) {
			SlidingBrickProblem sbp = new SlidingBrickProblem(new File(args[0]));			
		} else {
			SlidingBrickProblem sbp = new SlidingBrickProblem(new File(DEFAULT_FILE));

			SlidingBrickProblem.randomWalks(sbp.initialGameState, 100);
			
			
		}
	}

}
