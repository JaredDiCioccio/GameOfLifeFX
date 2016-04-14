package com.jmd.life.gui;

import com.jmd.life.Life;
import com.jmd.life.LifeObserver;

public class LifeConsolePrinter implements LifeObserver {

	@Override
	public void notifyObserver(Life life) {
		int[][] board = life.getBoard();
		System.out.println("Tick: " + life.getTickCount());
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board.length; col++) {
				System.out.print(board[row][col]);
			}
			System.out.println();
		}
	}
}
