package mwa29.cs510;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SlidingBrickProblem {

	public enum Direction {
		up(0, -1), down(0, 1), left(-1, 0), right(1, 0);

		private int dx;
		private int dy;

		Direction(int dx, int dy) {
			this.dx = dx;
			this.dy = dy;
		}

		public int getDx() {
			return dx;
		}

		public int getDy() {
			return dy;
		}
	}

	private static class GameMove {

		private GameState beforeMove;
		private GamePiece pieceMoved;
		private Direction moveDirection;

		private GameState afterMove;

		public GameMove(GameState beforeMove, GamePiece pieceMove,
				Direction moveDirection) {
			this.beforeMove = beforeMove;
			this.pieceMoved = pieceMove;
			this.moveDirection = moveDirection;
			this.afterMove = beforeMove.cloneGameState();

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

		public GameState getAfterMove() {
			return afterMove;
		}

		public GameState getBeforeMove() {
			return beforeMove;
		}

		public Direction getMoveDirection() {
			return moveDirection;
		}

		public GamePiece getPieceMoved() {
			return pieceMoved;
		}

		@Override
		public String toString() {
			return "GameMove [beforeMove=" + beforeMove + ", pieceMoved="
					+ pieceMoved + ", moveDirection=" + moveDirection
					+ ", afterMove=" + afterMove + "]";
		}

		public String prettyToString() {
			return String.format("(%d,%s)", pieceMoved.getPieceNumber(),
					moveDirection.toString());
		}
	}

	private static class GamePiece {
		private int pieceNumber;

		public GamePiece(int pieceNumber) {
			super();
			this.pieceNumber = pieceNumber;
		}

		public int getPieceNumber() {
			return pieceNumber;
		}

		public void setPieceNumber(int pieceNumber) {
			this.pieceNumber = pieceNumber;
		}

		public static boolean isGamePieceInt(int piece) {
			return (EMPTY != piece && WALL != piece);
		}
	}

	private static class GameState {

		private int[][] gameBoard;

		private int maxPiece = Integer.MIN_VALUE;

		private GameState(File gameFile) {
			loadGameState(gameFile);

			setMaxPiece();
		}

		public GameState(GameState gameState) {
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

		public void setMaxPiece() {
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

		public void applyMove(GameMove gameMove) {
			gameBoard = gameMove.afterMove.gameBoard;
		}

		public GameState applyMoveCloning(GameMove gameMove) {
			return gameMove.afterMove.cloneGameState();
		}

		public GameState cloneGameState() {
			return new GameState(this);
		}

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

		public int[][] getGameBoard() {
			return gameBoard;
		}

		private int getHeight() {
			if (null != gameBoard && gameBoard.length > 0) {
				return gameBoard[0].length;
			} else {
				return 0;
			}
		}

		private int getWidth() {
			if (null != gameBoard) {
				return gameBoard.length;
			} else {
				return 0;
			}
		}

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
	}

	private static void debug(Object message) {
		if (DEBUG) {
			System.out.println("[DEBUG] " + message.toString());
		}
	}

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

	GameState initialGameState;

	private static final int GOAL = -1;

	private static final int EMPTY = 0;

	private static final int WALL = 1;

	private static final int MASTER = 2;

	private static final boolean DEBUG = false;

	public SlidingBrickProblem(File gameFile) {
		initialGameState = new GameState(gameFile);
	}
}
