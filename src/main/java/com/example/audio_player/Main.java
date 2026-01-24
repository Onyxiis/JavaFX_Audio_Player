package com.example.audio_player;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
        mainLayout.setStyle("-fx-background-color: #ecf0f1");

        Scene scene = new Scene(mainLayout, 600, 450);
        primaryStage.setTitle("Audio Player");
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(e -> controller.cleanup());
    }

    private VBox createTopSection() {
        songLabel = new Label("No song loaded");
        songLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        timeLabel = new Label("0:00 / 0:00");
        timeLabel.setStyle("-fx-font-size: 12px");

        VBox topSection = new VBox(10, songLabel, timeLabel);
        topSection.setAlignment(Pos.CENTER);
        topSection.setPadding(new Insets(20));

        return topSection;
    }

    private ProgressBar createProgressBar() {
        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(400);
        return progressBar;
    }

    private HBox createControlButtons(Stage stage) {
        prevButton = createStyledButton("⏮ Prev");
        playButton = createStyledButton("▶ Play");
        pauseButton = createStyledButton("⏸ Pause");
        stopButton = createStyledButton("⏹ Stop");
        resetButton = createStyledButton("🔄 Reset");
        nextButton = createStyledButton("⏭ Next");

        setButtonsEnabled(false);

        prevButton.setOnAction(e -> controller.previousSong());
        playButton.setOnAction(e -> controller.play());
        pauseButton.setOnAction(e -> controller.pause());
        stopButton.setOnAction(e -> controller.stop());
        resetButton.setOnAction(e -> controller.reset());
        nextButton.setOnAction(e -> controller.nextSong());

        HBox controlButtons = new HBox(10, prevButton, playButton, pauseButton,
                stopButton, resetButton, nextButton);
        controlButtons.setAlignment(Pos.CENTER);

        return controlButtons;
    }

    private VBox createVolumeSection() {
        Label volumeLabel = new Label("🔊 Volume");

        volumeSlider = new Slider(0, 100, 50);
        volumeSlider.setShowTickLabels(true);
        volumeSlider.setShowTickMarks(true);
        volumeSlider.setMajorTickUnit(25);
        volumeSlider.setPrefWidth(300);

        volumeSlider.valueProperty().addListener((obs,
                                                  oldVal,
                                                  newVal) ->
                controller.setVolume(newVal.intValue())
        );

        VBox volumeSection = new VBox(5, volumeLabel, volumeSlider);
        volumeSection.setAlignment(Pos.CENTER);

        return volumeSection;
    }

    private Button createLoadButton(Stage stage) {
        Button loadButton = createStyledButton("📁 Load Song");
        loadButton.setOnAction(e -> controller.loadSongDialog(stage));
        return loadButton;
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-font-size: 14px; -fx-padding: 10 20");
        return button;
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
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
