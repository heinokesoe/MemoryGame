package zin.kyi.memorygame;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;

public class SoundEffect {
  public SoundEffect() {
  }
  public void playSound() {
    URL flipResource = getClass().getResource("flip.wav");
    Media flipMedia = new Media(flipResource.toString());
    MediaPlayer flipPlayer = new MediaPlayer(flipMedia);
    flipPlayer.play();
  }

  public void playWinningSound() {
    URL resource = getClass().getResource("clap.mp3");
    Media media = new Media(resource.toString());
    MediaPlayer mediaPlayer = new MediaPlayer((media));
    mediaPlayer.setOnEndOfMedia(new Runnable() {
      public void run() {
        mediaPlayer.seek(Duration.ZERO);
      }
    });
    mediaPlayer.play();
  }
}