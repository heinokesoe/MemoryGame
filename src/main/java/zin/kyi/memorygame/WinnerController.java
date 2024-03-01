package zin.kyi.memorygame;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class WinnerController {
  @FXML
  private Label winnerName;

  public void displayWinnerName(String winner) {
    winnerName.setText(winner);
  }
  public WinnerController() {
    SoundEffect sound = new SoundEffect();
    sound.playWinningSound();
  }
}
