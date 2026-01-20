package com.example.audio_player;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.sql.Time;


public class Main extends Application {

    private Clip clip;
    private AudioInputStream audioStream;
    private FloatControl volumeControl;

    private Label songLabel;
    private Label timeLabel;
    private Button playButton;
    private Button pauseButton;
    private Button stopButton;
    private Button resetButton;
    private Slider volumeSlider;
    private ProgressBar progressBar;
    private Timeline timeline;


    @Override
    public void start(Stage primaryStage){

        songLabel = new Label("No song loaded");
        songLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        timeLabel = new Label("0:00 / 0:00");
        timeLabel.setStyle("-fx-font-size: 12px");

        VBox topSection = new VBox(10, songLabel, timeLabel);
        topSection.setAlignment(Pos.CENTER);
        topSection.setPadding(new Insets(20));


        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(400);

        playButton = new Button("▶ Play");
        pauseButton = new Button("⏸ Pause");
        stopButton = new Button("⏹ Stop");
        resetButton = new Button("🔄 Reset");
        Button loadButton = new Button("📁 Load Song");

        playButton.setStyle("-fx-font-size: 14px; -fx-padding: 10 20");
        pauseButton.setStyle("-fx-font-size: 14px; -fx-padding: 10 20");
        stopButton.setStyle("-fx-font-size: 14px; -fx-padding: 10 20");
        resetButton.setStyle("-fx-font-size: 14px; -fx-padding: 10 20");
        loadButton.setStyle("-fx-font-size: 14px; -fx-padding: 10 20");

        playButton.setDisable(true);
        pauseButton.setDisable(true);
        stopButton.setDisable(true);
        resetButton.setDisable(true);

        playButton.setOnAction(e -> playSong());
        pauseButton.setOnAction(e -> pauseSong());
        stopButton.setOnAction(e -> stopSong());
        resetButton.setOnAction(e -> resetSong());
        loadButton.setOnAction(e -> loadSong(primaryStage));

        HBox controlButtons = new HBox(10, playButton, pauseButton, stopButton, resetButton);
        controlButtons.setAlignment(Pos.CENTER);

        Label volumeLabel = new Label("🔊 Volume");
        volumeSlider = new Slider(0, 100, 50);
        volumeSlider.setShowTickLabels(true);
        volumeSlider.setShowTickMarks(true);
        volumeSlider.setMajorTickUnit(25);
        volumeSlider.setPrefWidth(300);

        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            setVolume(newVal.intValue());
        });

        VBox volumeSection = new VBox(5, volumeLabel, volumeSlider);
        volumeSection.setAlignment(Pos.CENTER);

        VBox mainLayout = new VBox(20,
                topSection,
                progressBar,
                controlButtons,
                volumeSection,
                loadButton
        );
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(30));
        mainLayout.setStyle("-fx-background-color: #ecf0f1");

        Scene scene = new Scene (mainLayout, 500, 450);
        primaryStage.setTitle("Audio Player");
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(e -> cleanup());

        timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> updateProgress()));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    private void loadSong(Stage stage){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Audio File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Audio Files" , "*.wav", "*.au", "*.aiff")
        );
        File file = fileChooser.showOpenDialog(stage);

        if(file != null){
            try{
                cleanup();

                audioStream = AudioSystem.getAudioInputStream(file);
                clip = AudioSystem.getClip();
                clip.open(audioStream);

                volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

                songLabel.setText(file.getName());
                timeLabel.setText("0:00 / " + formatTime(clip.getMicrosecondLength()));


                playButton.setDisable(false);
                pauseButton.setDisable(false);
                stopButton.setDisable(false);
                resetButton.setDisable(false);

                setVolume((int) volumeSlider.getValue());


                showAlert("Success", "Song loaded successfully!", Alert.AlertType.INFORMATION);

            } catch (UnsupportedAudioFileException e){
                showAlert("Error", "Unsupported audio file format", Alert.AlertType.ERROR);
            } catch (IOException e){
                showAlert("Error", "Could not read file", Alert.AlertType.ERROR);
            } catch (LineUnavailableException e){
                showAlert("Error", "Audio line unavaible", Alert.AlertType.ERROR);
            }
        }
    }



    private void playSong(){
        if(clip != null){
            clip.start();
            timeline.play();
        }
    }

    private void pauseSong(){
        if(clip != null && clip.isRunning()){
            clip.stop();
            timeline.pause();
        }
    }

    private void stopSong(){
        if(clip != null) {
            clip.stop();
            clip.setMicrosecondPosition(0);
            timeline.pause();
            updateProgress();
        }
    }
    private void resetSong(){
        if(clip != null){
            clip.setMicrosecondPosition(0);
            updateProgress();
        }
    }

    private void setVolume(int percent){
        if(volumeControl != null){
            float min = volumeControl.getMinimum();
            float max = volumeControl.getMaximum();
            float range = max - min;
            float gain = min + (range * percent / 100.0f);
            volumeControl.setValue(gain);
        }
    }

    private void updateProgress(){
        if(clip != null){
            long current = clip.getMicrosecondPosition();
            long total = clip.getMicrosecondLength();

            double progress = (double) current / total;
            progressBar.setProgress(progress);

            timeLabel.setText(formatTime(current) + " / " + formatTime(total));

        }
    }

    private String formatTime(long microseconds){
        long seconds = microseconds / 1_000_000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    private void showAlert(String title, String message, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void cleanup(){
        if (timeline != null) {
            timeline.stop();
        }
        if (clip != null) {
            clip.close();
        }
        if (audioStream != null){
            try{
                audioStream.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        launch(args);
    }
}
