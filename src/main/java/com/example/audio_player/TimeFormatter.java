package com.example.audio_player;

public class TimeFormatter {

    public static String format(long microseconds){
        long seconds = microseconds / 1_000_000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    private TimeFormatter(){
        throw new AssertionError("Utility class should not be instantiated");
    }
}
