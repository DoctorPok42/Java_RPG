package Class.Music;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class Music {
    MediaPlayer mediaPlayer;
    private double volume;

    public Music(String path, double volume) {
        Media media = new Media(new File(path).toURI().toString());
        this.mediaPlayer = new MediaPlayer(media);
        this.volume = volume;
    }

    public void play() {
        this.mediaPlayer.setVolume(this.volume);
        this.mediaPlayer.play();
        this.mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }

    public void stop() {
        this.mediaPlayer.stop();
    }

    public void pause() {
        this.mediaPlayer.pause();
    }

    public void setVolume(double volume) {
        this.volume = volume;
        this.mediaPlayer.setVolume(volume);
    }

    public double getVolume() {
        return this.volume;
    }

    public boolean isPlaying() {
        return this.mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }
}
