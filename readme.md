# 🎵 JavaFX Audio Player

A lightweight, feature-rich audio player built using Java and JavaFX. This application allows users to load audio files, automatically generates playlists from the file directory, and provides standard playback controls including volume and seek functionality.

## ✨ Features

- **Playback Controls:** Play, Pause, Stop, and Reset functionality.
- **Playlist Navigation:** Next (⏭) and Previous (⏮) buttons with circular navigation.
- **Smart Loading:** Automatically detects all supported audio files in the selected directory to create a playlist.
- **Volume Control:** Real-time volume slider.
- **Progress Tracking:** Dynamic progress bar and time display.
- **Supported Formats:** `.wav`, `.au`, `.aiff`.

## 📂 Project Structure

The project follows a standard Maven architecture. Audio files are stored in the resources directory to ensure they travel with the project.

```text
Audio_Player
├── src
│   └── main
│       ├── java
│       │   └── com.example.audio_player
│       │       └── Main.java  <-- Source Code
│       └── resources
│           ├── com.example.audio_player
│           │   └── hello-view.fxml
│           └── music          <-- Place your .wav files here
│               ├── Song1.wav
│               └── Song2.wav
└── pom.xml
```

🚀 How to Run
Because this project uses the JavaFX Maven Plugin, the most reliable way to run the application is via Maven.

Option 1: Using IntelliJ IDEA (Recommended)
Open the project in IntelliJ IDEA.

Open the Maven sidebar on the right side of the IDE.

Navigate to Audio_Player -> Plugins -> javafx.

Double-click on javafx:run.

Option 2: Using Terminal
If you have Maven installed on your command line, you can run:

Bash
mvn clean javafx:run

Note: If you try to run the Main.java file directly using the standard "Run" button in IntelliJ, you may encounter module path errors. Please use the Maven plugin method described above.

🎧 Adding Your Own Music
To add songs so they appear in the project by default:

Navigate to src/main/resources/music.

Paste your .wav files into this folder.

When the application launches, click Load Song, select the music folder, and pick a track. The player will automatically load all other tracks in that folder into the playlist.

🛠 Technologies Used
Java 17+

JavaFX 17+

Maven (Dependency Management)
