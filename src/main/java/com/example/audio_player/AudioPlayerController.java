package com.example.audio_player;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioPlayerController {

    private final Main ui;
    private final Playlist playlist;
    private final Timeline progressTimeline;

    private Clip clip;
    private AudioInputStream audioStream;
    private FloatControl volumeControl;

    public AudioPlayerController(Main ui){
        this.ui = ui;
        this.playlist = new Playlist();

        this.progressTimeline = new Timeline(
                new KeyFrame(Duration.millis(100), e -> updateProgress())
        );
        this.progressTimeline.setCycleCount(Timeline.INDEFINITE);
    }

    public void loadSongDialog(Stage stage){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Audio File");
        fileChooser.getExtensionFilters().add( new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.au", "*.aiif")
    );

        File musicFolder = new File("src/main/resources/music");
        if (!musicFolder.exists()){
            musicFolder = new File("music");
        }
        if (musicFolder.exists() && musicFolder.isDirectory()){
            fileChooser.setInitialDirectory(musicFolder);
        }

        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null){
            playlist.loadFromDirectory(selectedFile);
            loadCurrentSong();
        }
    }

    private void loadCurrentSong() {
        File currentFile = playlist.getCurrentFile();
        if (currentFile == null) return;

        try {
            cleanup();

            audioStream = AudioSystem.getAudioInputStream(currentFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);

            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            String displayName = removeFileExtension(currentFile.getName());
            ui.updateSongLabel(displayName);
            ui.updateTimeLabel("0:00 / " + TimeFormatter.format(clip.getMicrosecondLength()));

            ui.setButtonsEnabled(true);

            setVolume(ui.getCurrentVolume());

            play();

            ui.showAlert("Succes", "Song loaded successfully", Alert.AlertType.INFORMATION);

        } catch (UnsupportedAudioFileException e) {
            ui.showAlert("Error", "Unsupported audio file format", Alert.AlertType.ERROR);
        } catch (IOException e) {
            ui.showAlert("Error", "Could not read file", Alert.AlertType.ERROR);
        } catch (LineUnavailableException e) {
            ui.showAlert("Error", "Audio line unavailable", Alert.AlertType.ERROR);
        }
    }

        private String removeFileExtension(String fileName) {
            int lastDot = fileName.lastIndexOf('.');
            if (lastDot > 0) {
                return fileName.substring(0, lastDot);
            }
            return fileName;
        }

        public void play(){
            if(clip != null){
                clip.start();
                progressTimeline.play();
            }
        }

        public void pause(){
            if(clip != null && clip.isRunning()){
                clip.stop();
                progressTimeline.pause();
            }
        }

        public void stop(){
            if(clip != null) {
                clip.stop();
                clip.setMicrosecondPosition(0);
                progressTimeline.pause();
                updateProgress();
            }
        }
        public void nextSong(){
            if(playlist.hasNext()){
                playlist.next();
                loadCurrentSong();
            }
        }
        public void previousSong() {
            if (playlist.hasPrevious()) {
                playlist.previous();
                loadCurrentSong();
            }
        }

    public void setVolume(int percent) {
        if (volumeControl != null) {
            float min = volumeControl.getMinimum();
            float max = volumeControl.getMaximum();
            float range = max - min;
            float gain = min + (range * percent / 100.0f);
            volumeControl.setValue(gain);
        }
    }


        public void reset() {
            if (clip != null) {
                clip.setMicrosecondPosition(0);
                updateProgress();
            }
        }

        private void updateProgress() {
            if (clip != null) {
                long current = clip.getMicrosecondPosition();
                long total = clip.getMicrosecondLength();

                double progress = (double) current / total;
                ui.updateProgress(progress);

                String timeDisplay = TimeFormatter.format(current) + " / " + TimeFormatter.format(total);
                ui.updateTimeLabel(timeDisplay);

                // Auto-stop when song finishes
                if (current >= total) {
                    stop();
                }
            }
        }
        public void cleanup() {
            if (progressTimeline != null) {
                progressTimeline.stop();
            }
            if (clip != null) {
                clip.stop();
                clip.close();
            }
            if (audioStream != null) {
                try {
                    audioStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


