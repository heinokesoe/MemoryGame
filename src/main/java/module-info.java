module zin.kyi.memorygame {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.desktop;
  requires javafx.media;


  opens zin.kyi.memorygame to javafx.fxml;
  exports zin.kyi.memorygame;
}