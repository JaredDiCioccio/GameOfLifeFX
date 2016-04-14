package com.jmd.life.gui;

import java.net.URL;
import java.util.ResourceBundle;

import com.jmd.life.Life;
import com.jmd.life.LifeObserver;
import com.jmd.life.LifeThread;
import com.jmd.life.MainApplication;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class GameController implements Initializable, LifeObserver {
	@FXML
	private MenuItem exitMenuItem;
	@FXML
	private MenuItem newGameMenuItem;
	@FXML
	private MenuItem pauseGameMenuItem;
	@FXML
	private MenuItem resumeGameMenuItem;
	@FXML
	private MenuItem resetGameMenuItem;
	@FXML
	private ToggleButton pauseButton;
	@FXML
	private Button stepButton;
	@FXML
	private Label tickLabel;
	@FXML
	private Canvas canvas;
	@FXML
	private Slider slider;

	private GraphicsContext gc;
	private Life life;
	private LifeThread lifeThread;
	private MainApplication mainApplication;
	private LifeConsolePrinter lifeConsolePrinter;

	// Event Listener on MenuItem[#exitMenuItem].onAction
	@FXML
	public void handleExit(ActionEvent event) {
		mainApplication.handleExit(null);
	}

	// Event Listener on MenuItem[#newGameMenuItem].onAction
	@FXML
	public void handleNewGame(ActionEvent event) {
		pauseGame();
		mainApplication.launchNewGameWindow();

	}

	public void pauseGame() {
		if (lifeThread != null) {
			System.out.println("Pausing Game");
			lifeThread.setPaused(true);
		} else {
			System.out.println("No game to pause");
		}
	}

	@FXML
	public void handlePauseGame(ActionEvent event) {
		pauseButton.selectedProperty().set(true);
		pauseGame();
	}

	// Event Listener on MenuItem[#resumeGameMenuItem].onAction
	@FXML
	public void handleResumeGame(ActionEvent event) {
		resumeGame();
	}

	public void resumeGame() {
		if (lifeThread != null) {
			System.out.println("Resuming Game");
			lifeThread.setPaused(false);
		} else {
			System.out.println("No game to resume");
		}

	}

	@FXML
	public void handleResetGame(ActionEvent event) {
		lifeThread.setPaused(true);
		life = new Life(life.getBoard().length);
		life.addObserver(this);
		lifeThread = null;
		lifeThread = new LifeThread(life);
		lifeThread.start();
	}

	@FXML
	public void handlePause(ActionEvent event) {
		lifeThread.setPaused(true);
	}

	@FXML
	public void handleStep(ActionEvent event) {
		life.tick();
	}

	public void startNewGame(int boardSize) {
		life = new Life(boardSize, (int) ((boardSize * boardSize) / 2));
		life.addObserver(this);
		// LifeConsolePrinter p = new LifeConsolePrinter();
		// life.addObserver(p);

		if (lifeThread != null) {
			lifeThread.quit();
		}

		lifeThread = new LifeThread(life);
		lifeThread.start();
		slider.setValue(lifeThread.getTickDelayInMillis());
	}

	public void setMainApplication(MainApplication mainApplication) {
		this.mainApplication = mainApplication;

	}

	@Override
	public void notifyObserver(Life life) {
		redraw(life);
	}

	private void redraw(Life life) {

		int[][] board = life.getBoard();
		gc = canvas.getGraphicsContext2D();

		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board.length; col++) {

				double w = canvas.getWidth() / life.getBoard().length;
				double h = canvas.getHeight() / life.getBoard().length;
				double x = w * col;
				double y = h * row;
				if (board[row][col] == 1) {
					gc.fillRect(x, y, w, h);
				}
			}
		}

		Platform.runLater(() -> {
			tickLabel.setText(Integer.toString(life.getTickCount()));
		});
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		gc = canvas.getGraphicsContext2D();
		gc.setFill(Color.BLACK);

		pauseButton.selectedProperty().addListener((observable, newValue, oldValue) -> {
			System.out.println("Toggle is now set to " + newValue);
			if (newValue) {
				resumeGame();
				pauseButton.setText("Pause");

			} else {
				pauseGame();
				pauseButton.setText("Resume");
			}
		});

		canvas.setOnMouseClicked(e -> {
			if (life != null && canvas != null) {
				handleMouseEvent(e);
			}

		});
		canvas.setOnMouseDragged(e -> {

			if (life != null && canvas != null) {
				handleMouseEvent(e);
			}

		});

		slider.valueProperty().addListener((o, nv, ov) -> {
			if (lifeThread != null) {
				lifeThread.setTickDelay(nv.longValue());
			}
		});
		slider.setValue(LifeThread.DEFAULT_TICK_DELAY);

	}

	private void handleMouseEvent(MouseEvent e) {
		double width = canvas.getWidth() / life.getBoard().length;
		double height = canvas.getHeight() / life.getBoard().length;
		int row = (int) (e.getY() / height);
		int col = (int) (e.getX() / width);
		System.out.format("Clicked at [%f,%f] which is position [%d, %d]\n", e.getX(), e.getY(), row, col);
		if (e.getButton() == MouseButton.PRIMARY) {
			life.setCellAlive(row, col);
		} else if (e.getButton() == MouseButton.SECONDARY) {
			life.setCellDead(row, col);
		}
	}

	public void quit() {

		if (lifeThread != null) {
			lifeThread.quit();
		}
		lifeThread = null;
		if (life != null) {
			life = null;
		}
		if (lifeConsolePrinter != null) {
			lifeConsolePrinter = null;
		}
	}
}