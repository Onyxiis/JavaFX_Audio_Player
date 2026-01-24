package com.example.audio_player;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Playlist {

    private final List<File> songs;
    private int currentIndex;

    public Playlist(){
        this.songs = new ArrayList<>();
        this.currentIndex = 0;
    }


    public void loadFromDirectory(File selectedFile){
        songs.clear();

        File directory = selectedFile.getParentFile();
        File[] files = directory.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".wav") ||
                        name.toLowerCase().endsWith(".au") ||
                        name.toLowerCase().endsWith(".aiff")
        );

        if (files != null) {
            songs.addAll(Arrays.asList(files));
        }

        currentIndex = songs.indexOf(selectedFile);
    }

    public File getCurrentFile() {
        if (isEmpty() || currentIndex < 0 || currentIndex >= songs.size()) {
            return null;
        }
        return songs.get(currentIndex);
    }

    public void next(){
        if(!isEmpty()){
            currentIndex = (currentIndex + 1) % songs.size();
        }
    }

    public void previous(){
        if(!isEmpty()){
            currentIndex = (currentIndex - 1 + songs.size()) % songs.size();
        }
    }

    public boolean hasNext(){
        return !isEmpty();
    }

    public boolean hasPrevious(){
        return !isEmpty();
    }

    public boolean isEmpty(){
        return songs.isEmpty();
    }
    public int size(){
        return songs.size();
    }

    public int getCurrentIndex(){
        return currentIndex;
    }
}
