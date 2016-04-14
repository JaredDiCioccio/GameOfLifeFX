package com.jmd.life;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Life implements LifeObservable {

	/**
	 * OFFSETS format is {row offset, column offset}
	 */

	private static int[][] OFFSETS = { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 },
			{ 1, 1 } };

	public static final int SMALL_SIZE = 20;
	public static final int MEDIUM_SIZE = 40;
	public static final int LARGE_SIZE = 60;

	private int[][] board;
	private int tickCount;

	public enum GameType {
		DIES_AT_EDGE, OVERLAPS;
	}

	private GameType gameType;

	private Set<LifeObserver> observers;

	public Life(int boardWidthHeight, int randomLivingCells, GameType gameType) {
		this.board = new int[boardWidthHeight][boardWidthHeight];
		this.tickCount = 0;
		for (int i = 0; i < randomLivingCells; i++) {
			int row = randInt(0, boardWidthHeight - 1);
			int col = randInt(0, boardWidthHeight - 1);
			
			board[row][col] = 1;
		}
		this.gameType = gameType;
		this.observers = new HashSet<LifeObserver>();
		notifyObservers();		
	}

	private void notifyObservers() {
		observers.forEach(o -> o.notifyObserver(this));
	}

	public Life(int boardWidthHeight, int randomLivingCells) {
		this(boardWidthHeight, randomLivingCells, GameType.OVERLAPS);
	}

	public Life(int boardWidthHeight) {
		this(boardWidthHeight, 0);
	}

	public int getTickCount() {
		return tickCount;
	}

	public void tick() {
		tickCount++;
		int[][] tempBoard = new int[board.length][board.length];

		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board.length; col++) {
				tempBoard[row][col] = mutateCellAt(row, col);
			}
		}

		board = tempBoard;
		notifyObservers();
	}

	private int mutateCellAt(int row, int col) {
		if (tileIsAliveAt(row, col)) {
			return mutateLivingCellAt(row, col);
		} else {
			return mutateDeadCellAt(row, col);
		}
	}

	/**
	 * 
	 * @param row
	 * @param col
	 * @return 1 if the cell gives birth, 0 otherwise
	 */
	private int mutateDeadCellAt(int row, int col) {
		if (numberOfLivingNeighborsOf(row, col) == 3) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * 
	 * @param row
	 * @param col
	 * @return 0 if the cell dies, 1 if it lives
	 */
	private int mutateLivingCellAt(int row, int col) {
		int numberOfNeighbors = numberOfLivingNeighborsOf(row, col);
		if (numberOfNeighbors < 2 || numberOfNeighbors > 3) {
			return 0;
		} else {
			return 1;
		}
	}

	private int numberOfLivingNeighborsOf(int row, int col) {
		int neighbors = 0;
		for (int[] offset : OFFSETS) {
			if (tileIsAliveAt(row + offset[0], col + offset[1])) {
				neighbors++;
			}
		}
		return neighbors;
	}

	private boolean tileIsAliveAt(int row, int col) {

		if (gameType == GameType.OVERLAPS) {
			row = row < 0 ? board.length - 1 : row;
			col = col < 0 ? board.length - 1 : col;
			row = row >= board.length ? 0 : row;
			col = col >= board.length ? 0 : col;

			return board[row][col] == 1;
		} else {
			return (row >= 0 && row < board.length && col >= 0 && col < board.length) ? board[row][col] == 1 : false;
		}

	}

	public int[][] getBoard() {
		return this.board;
	}

	/**
	 * Returns a psuedo-random number between min and max, inclusive. The
	 * difference between min and max can be at most
	 * <code>Integer.MAX_VALUE - 1</code>.
	 *
	 * @param min
	 *            Minimim value
	 * @param max
	 *            Maximim value. Must be greater than min.
	 * @return Integer between min and max, inclusive.
	 * @see java.util.Random#nextInt(int)
	 */
	public static int randInt(int min, int max) {

		// Usually this can be a field rather than a method variable
		Random rand = new Random();
		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

	@Override
	public void addObserver(LifeObserver observer) {
		observers.add(observer);

	}

	@Override
	public void stopObserving(LifeObserver observer) {
		observers.remove(observer);
	}

	public void toggleCellAt(int row, int col) {
		int oldValue = board[row][col];
		board[row][col] = oldValue == 1 ? 0 : 1;
		notifyObservers();
	}

	public void setCellAlive(int row, int col) {
		board[row][col] = 1;
		notifyObservers();
	}

	public void setCellDead(int row, int col) {
		board[row][col] = 0;
		notifyObservers();
	}

}
