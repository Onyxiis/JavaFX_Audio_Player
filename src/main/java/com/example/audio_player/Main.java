package com.example.audio_player;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class Main extends Application {

    private AudioPlayerController controller;



    private Label songLabel;
    private Label timeLabel;
    private Button playButton, pauseButton, stopButton, resetButton;
    private Button prevButton, nextButton;
    private Slider volumeSlider;
    private ProgressBar progressBar;



    @Override
    public void start(Stage primaryStage){

        controller = new AudioPlayerController(this);

        VBox mainLayout = new VBox(20,
                createTopSection(),
                createProgressBar(),
                createControlButtons(primaryStage),
                createVolumeSection(),
                createLoadButton(primaryStage)
        );

        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(30));

        mainLayout.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #1a0b2e, #16213e);"
        );

        Scene scene = new Scene(mainLayout, 600, 450);

        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.setTitle("Audio Player");
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(e -> controller.cleanup());
    }

    private VBox createTopSection() {
        songLabel = new Label("No song loaded");
        songLabel.setStyle(
                "-fx-font-size: 20px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: white;"
        );

        timeLabel = new Label("0:00 / 0:00");
        timeLabel.setStyle(
                "-fx-font-size: 14px; " +
                        "-fx-text-fill: #b4a5d4;"
        );

        VBox topSection = new VBox(10, songLabel, timeLabel);
        topSection.setAlignment(Pos.CENTER);
        topSection.setPadding(new Insets(20));
        topSection.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.08); " +
                        "-fx-background-radius: 20; " +
                        "-fx-border-color: rgba(255, 255, 255, 0.1); " +
                        "-fx-border-radius: 20; " +
                        "-fx-border-width: 1;"
        );

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.3));
        shadow.setRadius(15);
        topSection.setEffect(shadow);

        return topSection;
    }

    private ProgressBar createProgressBar() {
        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(350);
        progressBar.setPrefHeight(8);
        progressBar.setStyle(
                "-fx-accent: linear-gradient(to right, #667eea, #764ba2); " +
                        "-fx-background-color: rgba(255, 255, 255, 0.1); " +
                        "-fx-background-radius: 10;"
        );

        VBox progressSection = new VBox(progressBar);
        progressSection.setAlignment(Pos.CENTER);
        return progressBar;
    }

    private HBox createControlButtons(Stage stage) {
        prevButton = createCircularButton("⏮");
        playButton = createCircularButton("▶");
        pauseButton = createCircularButton("⏸");
        stopButton = createCircularButton("⏹");
        resetButton = createCircularButton("🔄");
        nextButton = createCircularButton("⏭");

        playButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #667eea, #764ba2); " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 24px; " +
                        "-fx-background-radius: 35; " +
                        "-fx-min-width: 70px; " +
                        "-fx-min-height: 70px; " +
                        "-fx-cursor: hand;"
        );

        setButtonsEnabled(false);

        prevButton.setOnAction(e -> controller.previousSong());
        playButton.setOnAction(e -> controller.play());
        pauseButton.setOnAction(e -> controller.pause());
        stopButton.setOnAction(e -> controller.stop());
        resetButton.setOnAction(e -> controller.reset());
        nextButton.setOnAction(e -> controller.nextSong());

        HBox controlRow1 = new HBox(15, prevButton, playButton, nextButton);
        controlRow1.setAlignment(Pos.CENTER);

        HBox controlRow2 = new HBox(15, pauseButton, stopButton, resetButton);
        controlRow2.setAlignment(Pos.CENTER);

        VBox controls = new VBox(15, controlRow1, controlRow2);
        controls.setAlignment(Pos.CENTER);

        HBox wrapper = new HBox(controls);
        wrapper.setAlignment(Pos.CENTER);

        return wrapper;
    }

    private Button createCircularButton(String text) {
        Button button = new Button(text);
        button.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.1); " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 18px; " +
                        "-fx-background-radius: 30; " +
                        "-fx-min-width: 60px; " +
                        "-fx-min-height: 60px; " +
                        "-fx-border-color: rgba(255, 255, 255, 0.2); " +
                        "-fx-border-radius: 30; " +
                        "-fx-border-width: 1; " +
                        "-fx-cursor: hand;"
        );

        button.setOnMouseEntered(e ->
                button.setStyle(
                        "-fx-background-color: rgba(255, 255, 255, 0.2); " +
                                "-fx-text-fill: white; " +
                                "-fx-font-size: 18px; " +
                                "-fx-background-radius: 30; " +
                                "-fx-min-width: 60px; " +
                                "-fx-min-height: 60px; " +
                                "-fx-border-color: rgba(255, 255, 255, 0.3); " +
                                "-fx-border-radius: 30; " +
                                "-fx-border-width: 1; " +
                                "-fx-cursor: hand;"
                )
        );

        button.setOnMouseExited(e ->
                button.setStyle(
                        "-fx-background-color: rgba(255, 255, 255, 0.1); " +
                                "-fx-text-fill: white; " +
                                "-fx-font-size: 18px; " +
                                "-fx-background-radius: 30; " +
                                "-fx-min-width: 60px; " +
                                "-fx-min-height: 60px; " +
                                "-fx-border-color: rgba(255, 255, 255, 0.2); " +
                                "-fx-border-radius: 30; " +
                                "-fx-border-width: 1; " +
                                "-fx-cursor: hand;"
                )
        );

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.3));
        shadow.setRadius(10);
        button.setEffect(shadow);

        return button;
    }

    private VBox createVolumeSection() {
        Label volumeLabel = new Label("🔊 Volume");

        volumeLabel.setStyle(
                "-fx-font-size: 14px; " +
                        "-fx-text-fill: #b4a5d4; " +
                        "-fx-font-weight: bold;"
        );
        volumeSlider = new Slider(0, 100, 50);
        volumeSlider.setPrefWidth(300);
        volumeSlider.setStyle(
                "-fx-control-inner-background: rgba(255, 255, 255, 0.1);"
        );

        volumeSlider.valueProperty().addListener((obs,
                                                  oldVal,
                                                  newVal) ->
                controller.setVolume(newVal.intValue())
        );

        VBox volumeSection = new VBox(5, volumeLabel, volumeSlider);
        volumeSection.setAlignment(Pos.CENTER);
        volumeSection.setPadding(new Insets(20));

        volumeSection.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.05); " +
                        "-fx-background-radius: 15; " +
                        "-fx-border-color: rgba(255, 255, 255, 0.1); " +
                        "-fx-border-radius: 15; " +
                        "-fx-border-width: 1;"
        );
        return volumeSection;
    }


    private Button createLoadButton(Stage stage) {
        Button loadButton = new Button("📁 Load Song");
        loadButton.setStyle(
                "-fx-background-color: linear-gradient(to right, #667eea, #764ba2); " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 15 40; " +
                        "-fx-background-radius: 25; " +
                        "-fx-cursor: hand;"
        );


        loadButton.setOnMouseEntered(e ->
                loadButton.setStyle(
                        "-fx-background-color: linear-gradient(to right, #7c8ef5, #8a5db8); " +
                                "-fx-text-fill: white; " +
                                "-fx-font-size: 16px; " +
                                "-fx-font-weight: bold; " +
                                "-fx-padding: 15 40; " +
                                "-fx-background-radius: 25; " +
                                "-fx-cursor: hand;"
                )
        );

        loadButton.setOnMouseExited(e ->
                loadButton.setStyle(
                        "-fx-background-color: linear-gradient(to right, #667eea, #764ba2); " +
                                "-fx-text-fill: white; " +
                                "-fx-font-size: 16px; " +
                                "-fx-font-weight: bold; " +
                                "-fx-padding: 15 40; " +
                                "-fx-background-radius: 25; " +
                                "-fx-cursor: hand;"
                )
        );

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(102, 126, 234, 0.4));
        shadow.setRadius(15);
        loadButton.setEffect(shadow);

        loadButton.setOnAction(e -> controller.loadSongDialog(stage));

        return loadButton;
    }

    public void updateSongLabel(String songName) {
        songLabel.setText(songName);
    }

    public void updateTimeLabel(String time) {
        timeLabel.setText(time);
    }

    public void updateProgress(double progress) {
        progressBar.setProgress(progress);
    }

    public void setButtonsEnabled(boolean enabled) {
        playButton.setDisable(!enabled);
        pauseButton.setDisable(!enabled);
        stopButton.setDisable(!enabled);
        resetButton.setDisable(!enabled);
        prevButton.setDisable(!enabled);
        nextButton.setDisable(!enabled);
    }

    public int getCurrentVolume() {
        return (int) volumeSlider.getValue();
    }

    public void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle(
                "-fx-background-color: #1a0b2e; " +
                        "-fx-text-fill: white;"
        );

        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
