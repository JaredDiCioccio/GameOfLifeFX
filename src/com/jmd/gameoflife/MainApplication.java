package com.jmd.gameoflife;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jmd.gameoflife.gui.GameController;
import com.jmd.gameoflife.gui.NewGameController;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainApplication extends Application {
	private static final Logger logger = Logger.getLogger(MainApplication.class.getName());

	private static final String NEW_GAME_VIEW_FXML = "gui/NewGame.fxml";
	private static final String GAME_VIEW_FXML = "gui/Game.fxml";

	private Stage gameStage;
	private Stage newGameStage;
	private NewGameController newGameController;
	private GameController gameController;

	@Override
	public void start(Stage primaryStage) throws Exception {
		Group root = new Group();
		Scene scene = new Scene(root, 800, 800, Color.WHITE);
		primaryStage.setTitle("Life");
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(e -> handleExit());
		primaryStage.setResizable(false);
		this.gameStage = primaryStage;

		launchGameWindow();

	}

	private GameController launchGameWindow() {
		FXMLLoader fxmlLoader = getFxmlLoader(GAME_VIEW_FXML);

		AnchorPane pane = null;
		try {
			pane = fxmlLoader.load();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Could not load fxmlFile: " + fxmlLoader.getLocation(), e);
		}
		gameController = fxmlLoader.getController();
		gameController.setMainApplication(this);
		Scene scene = new Scene(pane);
		gameStage.setScene(scene);

		gameStage.sizeToScene();
		gameStage.show();
		return gameController;

	}

	public void handleExit() {
		logger.log(Level.INFO, "Exiting Application");
		this.gameController.quit();
		this.gameStage.close();
		Platform.exit();

	}

	public FXMLLoader getFxmlLoader(String fxmlLocation) {
		FXMLLoader loader = new FXMLLoader();
		URL location = MainApplication.class.getResource(fxmlLocation);
		loader.setLocation(location);
		return loader;
	}

	public NewGameController launchNewGameWindow() {
		gameController.pauseGame();

		FXMLLoader loader = getFxmlLoader(NEW_GAME_VIEW_FXML);

		AnchorPane pane = null;
		try {
			pane = loader.load();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Could not load fxmlFile: " + loader.getLocation(), e);
		}

		newGameController = loader.getController();
		newGameController.setMainApplication(this);
		newGameStage = new Stage();
		Scene scene = new Scene(pane);
		newGameStage.setScene(scene);
		newGameStage.sizeToScene();
		newGameStage.show();
		return newGameController;
	}

	public void cancelNewGameWindow() {
		closeNewGameWindow();
		gameController.resumeGame();
	}

	public void startNewGame(int boardSize) {
		logger.log(Level.INFO, "Starting New Game with boardSize " + boardSize);
		gameController.startNewGame(boardSize);

	}

	public static void main(String[] args) {
		MainApplication.launch(args);
	}

	public void closeNewGameWindow() {
		this.newGameStage.close();

	}
}
