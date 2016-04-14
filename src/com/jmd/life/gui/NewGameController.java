package com.jmd.life.gui;

import java.net.URL;
import java.util.ResourceBundle;

import com.jmd.life.Life;
import com.jmd.life.MainApplication;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

public class NewGameController implements Initializable {
	@FXML
	private RadioButton radioButtonSmall;
	@FXML
	private ToggleGroup radioGroup;
	@FXML
	private RadioButton radioButtonMedium;
	@FXML
	private RadioButton radioButtonLarge;
	@FXML
	private RadioButton radioButtonCustom;
	@FXML
	private TextField textFieldCustom;
	@FXML
	private Button buttonStart;
	@FXML
	private Button buttonCancel;
	private MainApplication mainApplication;

	@FXML
	public void handleStartButton(ActionEvent event) {
		Toggle selectedToggle = radioGroup.getSelectedToggle();
		if (selectedToggle == radioButtonSmall) {
			mainApplication.startNewGame(Life.SMALL_SIZE);
		} else if (selectedToggle == radioButtonMedium) {
			mainApplication.startNewGame(Life.MEDIUM_SIZE);
		} else if (selectedToggle == radioButtonLarge) {
			mainApplication.startNewGame(Life.LARGE_SIZE);
		} else if (selectedToggle == radioButtonCustom) {
			mainApplication.startNewGame(Integer.parseInt(textFieldCustom.getText()));
		} else {
			// TODO something went wrong
		}
		mainApplication.closeNewGameWindow();
	}

	@FXML
	public void handleCancelButton(ActionEvent event) {
		mainApplication.cancelNewGameWindow();

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		radioButtonCustom.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				textFieldCustom.setDisable(!newValue);
			}
		});

		textFieldCustom.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.matches("\\d*")) {
					int value = Integer.parseInt(newValue);
					value = value <= 100 ? value : 100;
					textFieldCustom.setText(Integer.toString(value));
				} else {
					textFieldCustom.setText(oldValue);
				}
			}
		});
		
		radioButtonSmall.selectedProperty().set(true);
	}

	public void setMainApplication(MainApplication mainApplication) {
		this.mainApplication = mainApplication;
	}

}
