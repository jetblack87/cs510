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

	private final static String DEFAULT_FILE = "dat/SBP-level3.txt";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SlidingBrickProblem sbp = null;
		if (args.length > 0) {
			sbp = new SlidingBrickProblem(new File(args[0]));			
		} else {
			sbp = new SlidingBrickProblem(new File(DEFAULT_FILE));
		}
		
		SlidingBrickProblem.randomWalks(sbp.initialGameState, 10000);
	}
}
