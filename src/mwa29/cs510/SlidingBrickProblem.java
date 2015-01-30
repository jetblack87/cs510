/**
 * CS510
 * Mark Albrecht
 */
package mwa29.cs510;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The SlidingBrickProblem class
 */
public class SlidingBrickProblem {

	/**
	 * Enumeration containing all of the directions a piece can move.
	 */
	public enum Direction {
		up(0, -1), down(0, 1), left(-1, 0), right(1, 0);

		/**
		 * The delta for x when a piece moves in this direction
		 */
		private int dx;
		/**
		 * The delta for y when a piece moves in this direction
		 */
		private int dy;

		/**
		 * Constructs a direction with the given dx and dy
		 * 
		 * @param dx
		 *            the delta for when this piece in moved
		 * @param dy
		 *            the delta for when this piece is moved
		 */
		Direction(int dx, int dy) {
			this.dx = dx;
			this.dy = dy;
		}

		/**
		 * Gets dx
		 * 
		 * @return dx
		 */
		public int getDx() {
			return dx;
		}

		/**
		 * Gets dy
		 * 
		 * @return dy
		 */
		public int getDy() {
			return dy;
		}
	}

	/**
	 * Class representing a move
	 */
	private static class GameMove {

		/**
		 * The game state before this move occurs
		 */
		private GameState beforeMove;
		/**
		 * The piece that is being moved
		 */
		private GamePiece pieceMoved;
		/**
		 * The direction that the piece is being moved
		 */
		private Direction moveDirection;

		/**
		 * The board after this move occurs
		 */
		private GameState afterMove;

		/**
		 * Constructs a GameMove with the given params
		 * 
		 * @param beforeMove
		 *            the game state before this move
		 * @param pieceMove
		 *            the game piece that is being moved
		 * @param moveDirection
		 *            the direction that this piece is moving
		 */
		public GameMove(GameState beforeMove, GamePiece pieceMove,
				Direction moveDirection) {
			this.beforeMove = beforeMove;
			this.pieceMoved = pieceMove;
			this.moveDirection = moveDirection;
			this.afterMove = beforeMove.cloneGameState();
			this.afterMove.setMove(this);

			// Check to see if this is a legal move:
			final int height = beforeMove.getHeight();
			final int width = beforeMove.getWidth();
			final int[][] gameBoard = beforeMove.getGameBoard();

			// When moving a piece up or left, traverse board from top left to
			// bottom right
			switch (moveDirection) {
			case up:
			case left:
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						checkAndApplyMove(pieceMove, moveDirection, gameBoard,
								y, x);
					}
				}
				break;
			// When moving a piece down or right, traverse board from bottom
			// right to top left
			case down:
			case right:
				for (int y = height - 1; y >= 0; y--) {
					for (int x = width - 1; x >= 0; x--) {
						checkAndApplyMove(pieceMove, moveDirection, gameBoard,
								y, x);
					}
				}
				break;
			}
		}

		/**
		 * Helper method for applying the move
		 * 
		 * @param pieceMove
		 *            the piece being moved
		 * @param moveDirection
		 *            the direction that the piece is moving
		 * @param gameBoard
		 *            the game board
		 * @param y
		 *            the x value
		 * @param x
		 *            the y value
		 */
		private void checkAndApplyMove(GamePiece pieceMove,
				Direction moveDirection, final int[][] gameBoard, int y, int x) {
			if (pieceMove.getPieceNumber() == gameBoard[x][y]) {
				final int dx = moveDirection.getDx();
				final int dy = moveDirection.getDy();
				if (EMPTY == gameBoard[x + dx][y + dy]
						|| pieceMove.getPieceNumber() == gameBoard[x + dx][y
								+ dy] // This check may not be needed
						|| (GOAL == gameBoard[x + dx][y + dy] && pieceMove
								.getPieceNumber() == MASTER)) {
					// Make the move
					afterMove.gameBoard[x + dx][y + dy] = pieceMove.pieceNumber;
					afterMove.gameBoard[x][y] = EMPTY;

				} else {
					throw new SlidingBrickProblemRuntimeException(
							String.format(
									"Illegal move: moving piece '%d' '%s' to '%d','%d' - '%d'",
									pieceMove.getPieceNumber(),
									moveDirection.toString(), x + dx, y + dy,
									gameBoard[x + dx][y + dy]));
				}
			}
		}

		/**
		 * Gets the {@link GameState} after this move occurs
		 * 
		 * @return the {@link GameState} after this move occurs
		 */
		public GameState getAfterMove() {
			return afterMove;
		}

		/**
		 * Gets the {@link GameState} before this move occurs
		 * 
		 * @return the {@link GameState} before this move occurs
		 */
		public GameState getBeforeMove() {
			return beforeMove;
		}

		/**
		 * Gets the move direction
		 * 
		 * @return the move direction
		 */
		public Direction getMoveDirection() {
			return moveDirection;
		}

		/**
		 * Gets the {@link GamePiece} that is being moved
		 * 
		 * @return the {@link GamePiece} that is being moved
		 */
		public GamePiece getPieceMoved() {
			return pieceMoved;
		}

		/**
		 * Gets a {@link String} that shows the move in a nice format
		 * 
		 * @return a {@link String} that shows the move in a nice format
		 */
		public String prettyToString() {
			return String.format("(%d,%s)", pieceMoved.getPieceNumber(),
					moveDirection.toString());
		}

		@Override
		public String toString() {
			return "GameMove [beforeMove=" + beforeMove + ", pieceMoved="
					+ pieceMoved + ", moveDirection=" + moveDirection
					+ ", afterMove=" + afterMove + "]";
		}
	}

	/**
	 * Class representing a game piece
	 */
	private static class GamePiece {
		/**
		 * Checks to see if this given piece is a game piece (and not empty or a
		 * wall)
		 * 
		 * @param piece
		 *            the piece to test
		 * @return true if it's a game piece, false otherwise.
		 */
		public static boolean isGamePieceInt(int piece) {
			return (EMPTY != piece && WALL != piece);
		}

		/**
		 * The number of this piece
		 */
		private int pieceNumber;

		/**
		 * Constructs a {@link GamePiece} using the given piece number
		 * 
		 * @param pieceNumber
		 *            the number of this piece
		 */
		public GamePiece(int pieceNumber) {
			super();
			this.pieceNumber = pieceNumber;
		}

		/**
		 * Gets the piece number
		 * 
		 * @return the piece number
		 */
		public int getPieceNumber() {
			return pieceNumber;
		}

		/**
		 * Sets the piece number
		 * 
		 * @param pieceNumber
		 *            the new piece number
		 */
		public void setPieceNumber(int pieceNumber) {
			this.pieceNumber = pieceNumber;
		}
	}

	/**
	 * Class representing the game state
	 */
	private static class GameState {

		/**
		 * The game board, duh!
		 */
		private int[][] gameBoard;

		/**
		 * The highest piece number - set during construction and used during
		 * piece looping
		 */
		private int maxPiece = Integer.MIN_VALUE;

		/**
		 * The move that made this state (null if it doesn't exist)
		 */
		private GameMove move = null;

		/**
		 * Constructs a {@link GameState} using the given {@link File}
		 * 
		 * @param gameFile
		 *            the file to load
		 */
		private GameState(File gameFile) {
			loadGameState(gameFile);

			setMaxPiece();
		}

		/**
		 * Gets the move
		 * 
		 * @return the move
		 */
		public GameMove getMove() {
			return move;
		}

		/**
		 * Sets the move
		 * 
		 * @param gameMove
		 *            the move {@link GameMove}
		 */
		public void setMove(GameMove gameMove) {
			this.move = gameMove;
		}

		/**
		 * Copy constructor
		 * 
		 * @param gameState
		 *            the {@link GameState} to copy
		 */
		public GameState(GameState gameState) {
			this.move = gameState.getMove();
			final int width = gameState.getWidth();
			final int height = gameState.getHeight();
			gameBoard = new int[width][height];
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					gameBoard[x][y] = gameState.gameBoard[x][y];
				}
			}

			setMaxPiece();
		}

		/**
		 * Applies the given {@link GameMove} to this {@link GameState}
		 * 
		 * @param gameMove
		 *            the {@link GameMove} to apply
		 */
		public void applyMove(GameMove gameMove) {
			gameBoard = gameMove.afterMove.gameBoard;
		}

		/**
		 * Applies the given {@link GameMove} to a cloned {@link GameState} and
		 * returns the result
		 * 
		 * @param gameMove
		 *            the {@link GameMove} to apply
		 * @return a clone of the resulting {@link GameState}
		 */
		public GameState applyMoveCloning(GameMove gameMove) {
			return gameMove.afterMove.cloneGameState();
		}

		/**
		 * Clones this {@link GameState}
		 * 
		 * @return a clone of this {@link GameState}
		 */
		public GameState cloneGameState() {
			return new GameState(this);
		}

		/**
		 * Compares this {@link GameState} against the given {@link GameState}
		 * 
		 * @param gameState
		 *            the {@link GameState} to compare against
		 * @return true if they're the same, false if they're not
		 */
		public boolean compare(GameState gameState) {
			boolean equal = true;

			if (this.getHeight() == gameState.getHeight()
					&& this.getWidth() == gameState.getWidth()) {
				final int width = this.getWidth();
				final int height = this.getHeight();
				for (int y = 0; y < height && equal; y++) {
					for (int x = 0; x < width && equal; x++) {
						if (gameBoard[x][y] != gameState.gameBoard[x][y]) {
							equal = false;
						}
					}
				}
			}
			return equal;
		}

		@Override
		public boolean equals(Object that) {
			if (!(that instanceof GameState)) {
				return false;
			}
			return compare((GameState) that);
		}

		/**
		 * Returns all the moves that can be done in a board (that is the union
		 * of the moves that each individual piece can perform).
		 * 
		 * @return All possible moves for all the pieces.
		 */
		public List<GameMove> getAllMoves() {
			List<GameMove> moveList = new ArrayList<GameMove>();
			// Start with the master and move on up
			for (int i = MASTER; i <= maxPiece; i++) {
				debug("Getting all moves for piece: " + i);
				moveList.addAll(getAllMoves(new GamePiece(i)));
			}
			return moveList;
		}

		/**
		 * Returns all possible moves for the given {@link GamePiece}
		 * 
		 * @param gamePiece
		 *            the {@link GamePiece} to move
		 * @return all possible moves for the given {@link GamePiece}
		 */
		public List<GameMove> getAllMoves(GamePiece gamePiece) {
			List<GameMove> moveList = new ArrayList<GameMove>();
			for (Direction direction : Direction.values()) {
				try {
					GameMove gameMove = new GameMove(this, gamePiece, direction);
					debug("Adding move: " + gameMove);
					moveList.add(gameMove);
				} catch (SlidingBrickProblemRuntimeException e) {
					debug("Illegal move will be ignored: " + e.getMessage());
				}
			}
			return moveList;
		}

		/**
		 * Gets the game board representation
		 * 
		 * @return the game board representation
		 */
		private int[][] getGameBoard() {
			return gameBoard;
		}

		/**
		 * Gets the height of the game board
		 * 
		 * @return the height of the game board
		 */
		private int getHeight() {
			if (null != gameBoard && gameBoard.length > 0) {
				return gameBoard[0].length;
			} else {
				return 0;
			}
		}

		/**
		 * Gets the width of the game board
		 * 
		 * @return the width of the game board
		 */
		private int getWidth() {
			if (null != gameBoard) {
				return gameBoard.length;
			} else {
				return 0;
			}
		}

		/**
		 * Checks to see if this {@link GameState} is solved
		 * 
		 * @return true if this {@link GameState} is solved, false if not
		 */
		public boolean isSolved() {
			final int height = this.getHeight();
			final int width = this.getWidth();
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (GOAL == gameBoard[x][y]) {
						return false;
					}
				}
			}
			return true;
		}

		/**
		 * Loads the game state from the given {@link File}, updating the width,
		 * height and gameBoard
		 * 
		 * @param gameFile
		 *            the file to load
		 */
		private void loadGameState(File gameFile) {
			try {
				final Scanner fileScanner = new Scanner(gameFile);
				final String dimensionsLine = fileScanner.nextLine();
				Pattern pattern = Pattern.compile("(\\d+),(\\d+),");
				Matcher matcher = pattern.matcher(dimensionsLine);
				if (matcher.matches() && matcher.groupCount() == 2) {
					final int width = Integer.parseInt(matcher.group(1));
					final int height = Integer.parseInt(matcher.group(2));
					debug(String
							.format("Creating gameBoard with width='%d' and height='%d'",
									width, height));
					gameBoard = new int[width][height];
				} else {
					fileScanner.close();
					throw new SlidingBrickProblemRuntimeException(
							"Dimensions line is incorrect: " + dimensionsLine);
				}

				final int width = this.getWidth();
				final int height = this.getHeight();
				for (int y = 0; y < height; y++) {
					final String currentLine = fileScanner.nextLine();
					List<String> element = Arrays
							.asList(currentLine.split(","));
					for (int x = 0; x < width; x++) {
						gameBoard[x][y] = Integer.parseInt(element.get(x));
					}
				}

				fileScanner.close();
			} catch (FileNotFoundException e) {
				throw new SlidingBrickProblemRuntimeException(
						"Failed to find file: " + gameFile.getPath(), e);
			}
		}

		/**
		 * Normalizes this {@link GameState} Taken from homework 1 instructions
		 */
		public void normalize() {
			int nextIdx = MASTER + 1;
			final int height = this.getHeight();
			final int width = this.getWidth();
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (gameBoard[x][y] == nextIdx) {
						nextIdx++;
					} else if (gameBoard[x][y] > nextIdx) {
						swapIdx(nextIdx, gameBoard[x][y]);
						nextIdx++;
					}
				}
			}
		}

		/**
		 * Prints the {@link GameState} to stdout
		 */
		public void outputGameState() {
			if (null == gameBoard) {
				throw new SlidingBrickProblemRuntimeException(
						"Cannot print board because it hasn't been initialized");
			}

			final int width = this.getWidth();
			final int height = this.getHeight();
			System.out.println(String.format("%d,%d,", width, height));

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					System.out.print(gameBoard[x][y] + ",");
				}
				System.out.println("");
			}
		}

		/**
		 * Sets the maxPiece variable to the highest number on the board
		 */
		private void setMaxPiece() {
			final int width = this.getWidth();
			final int height = this.getHeight();
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (GamePiece.isGamePieceInt(gameBoard[x][y])
							&& gameBoard[x][y] > maxPiece) {
						maxPiece = gameBoard[x][y];
					}
				}
			}
		}

		/**
		 * Swaps the indexes - used to normalize {@link GameState}
		 * 
		 * @param idx1
		 *            first index to swap
		 * @param idx2
		 *            second index to swap
		 */
		private void swapIdx(int idx1, int idx2) {
			final int height = this.getHeight();
			final int width = this.getWidth();
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (gameBoard[x][y] == idx1) {
						gameBoard[x][y] = idx2;
					} else if (gameBoard[x][y] == idx2) {
						gameBoard[x][y] = idx1;
					}
				}
			}

		}
	}

	/**
	 * Helper function to print messages when in DEBUG mode
	 * 
	 * @param message
	 *            the message to print
	 */
	private static void debug(Object message) {
		if (DEBUG) {
			System.out.println("[DEBUG] " + message.toString());
		}
	}

	/**
	 * Tries to solve the puzzle by randomly executing available moves
	 * 
	 * @param gameState
	 *            the {@link GameState} to walk
	 * @param N
	 *            the total number of turns to try
	 */
	public static void randomWalks(GameState gameState, int N) {
		Random random = new Random();
		boolean solved = false;
		GameState currentState = gameState.cloneGameState();

		gameState.outputGameState();

		int moveCount = 0;
		for (moveCount = 0; moveCount < N && !solved; moveCount++) {
			// 1) generate all the moves that can be generated in the board
			List<GameMove> allMoves = currentState.getAllMoves();

			debug("move count " + allMoves.size());

			// 2) select one at random
			int randomIndex = random.nextInt(allMoves.size());
			GameMove randomMove = allMoves.get(randomIndex);

			// 3) execute it
			currentState = currentState.applyMoveCloning(randomMove);

			// 4) normalize the resulting game state
			currentState.normalize();

			// 5) if we have reached the goal, or if we have executed N
			// moves, stop; otherwise, go back to 1.
			if (currentState.isSolved()) {
				solved = true;
			}

			System.out.println(String.format("\n%s\n",
					randomMove.prettyToString()));
			currentState.outputGameState();
		}

		if (solved) {
			System.out.println(String.format(
					"\nPuzzle was solved on move '%d'\n", moveCount + 1));
		} else {
			System.out.println(String.format(
					"\nThe puzzle could not be solved under '%d' moves'\n", N));
		}

	}

	/**
	 * Performs a breadth-first search strategy to solve the puzzle
	 * 
	 * @param gameState
	 *            the {@link GameState} to solve
	 */
	public static void breadthFirst(GameState gameState) {
		int nodesExplored = 0;
		boolean found = false;
		LinkedList<GameState> Q = new LinkedList<GameState>();
		LinkedList<GameState> V = new LinkedList<GameState>();
		Q.add(gameState);
		V.add(gameState);

		while (!Q.isEmpty()) {
			GameState currState = Q.removeLast();
			nodesExplored++;
			if (currState.isSolved()) {
				found = true;

				LinkedList<GameMove> successPath = new LinkedList<GameMove>();
				GameMove move = currState.getMove();
				while (null != move) {
					successPath.push(move);
					move = move.beforeMove.getMove();
				}

				for (GameMove currMove : successPath) {
					System.out.println(currMove.prettyToString());
				}

				currState.outputGameState();
				System.out.println("Number of nodes explored: " + nodesExplored);
				System.out.println("Solution length: " + successPath.size());
				break;
			}
			for (GameMove move : currState.getAllMoves()) {
				GameState nextState = currState.applyMoveCloning(move);
				GameState normalized = nextState.cloneGameState();
				normalized.normalize();
				if (!V.contains(normalized)) {
					V.add(normalized);
					Q.push(nextState);
				}
			}
		}

		if (!found) {
			System.err.println("No successful paths found");
		}
	}

	/**
	 * Recursive implementation of depth first search
	 * @param gameState the initial {@link GameState}
	 * @param maxDepth the deepest search (0 can be used for infinite)
	 * @return true if the puzzle was solved, false otherwise
	 */
	public static boolean depthFirstRecurse(GameState gameState, int maxDepth) {
		final List<GameState> discovered = new LinkedList<GameState>();
		GameState success = depthFirstRecurseAux(gameState, maxDepth, 0,
				discovered);

		if (null != success) {
			LinkedList<GameMove> successPath = new LinkedList<GameMove>();
			GameMove move = success.getMove();
			while (null != move) {
				successPath.push(move);
				move = move.beforeMove.getMove();
			}

			for (GameMove currMove : successPath) {
				System.out.println(currMove.prettyToString());
			}

			success.outputGameState();
			
			System.out.println("Number of nodes explored: " + nodesExplored);
			System.out.println("Solution length: " + successPath.size());
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Auxiliary method for depthFirstRecurse
	 * @param gameState the initial {@link GameState}
	 * @param maxDepth the deepest search (0 can be used for infinite)
	 * @param currDepth the current depth
	 * @param discovered the list of discovered states
	 * @return the solved state
	 */
	private static int nodesExplored;
	private static GameState depthFirstRecurseAux(final GameState gameState,
			final int maxDepth, final int currDepth,
			final List<GameState> discovered) {
		nodesExplored++;
		if (gameState.isSolved()) {
			return gameState;
		}
		if (maxDepth != 0 && currDepth > maxDepth) {
			return null;
		}
		for (GameMove move : gameState.getAllMoves()) {
			GameState next = gameState.applyMoveCloning(move);
			GameState normalized = next.cloneGameState();
			normalized.normalize();
			if (!discovered.contains(normalized)) {
				discovered.add(normalized);
				GameState success = depthFirstRecurseAux(next, maxDepth,
						currDepth + 1, discovered);
				if (null != success) {
					return success;
				}
			}
		}
		return null;
	}

	/**
	 * The initial {@link GameState} for this puzzle
	 */
	GameState initialGameState;

	/**
	 * A 'goal' piece
	 */
	private static final int GOAL = -1;

	/**
	 * An 'empty' piece
	 */
	private static final int EMPTY = 0;

	/**
	 * A 'wall' piece
	 */
	private static final int WALL = 1;

	/**
	 * The 'master' brick
	 */
	private static final int MASTER = 2;

	/**
	 * True if we wanted to print debug statements
	 */
	private static final boolean DEBUG = false;

	/**
	 * Constructs this {@link SlidingBrickProblem} with the given {@link File}
	 * 
	 * @param gameFile
	 *            the file to load
	 */
	public SlidingBrickProblem(File gameFile) {
		initialGameState = new GameState(gameFile);
	}
}
