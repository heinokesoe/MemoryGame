package zin.kyi.memorygame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;

public class Main extends Application {
  private TextInputDialog dialog;
  public static String player1Name;
  public static String player2Name;
  public static String level;
  public static int cardCount;
  private Optional<String> result;
  @Override
  public void start(Stage stage) throws IOException {
    dialog = new TextInputDialog();
    dialog.setHeaderText("Enter player 1 name");
    dialog.setContentText("Name: ");
    result =  dialog.showAndWait();
    player1Name = result.get();
    dialog.setHeaderText("Enter player 2 name");
    dialog.setContentText("Name: ");
    dialog.getEditor().clear();
    result = dialog.showAndWait();
    player2Name = result.get();
    dialog.setHeaderText("Choose the level");
    dialog.setContentText("1. Easy\n2. Normal\n3. Hard");
    dialog.getEditor().clear();
    result = dialog.showAndWait();
    level = result.orElse("2");
    switch (level) {
      case "1":
        level = "Easy.fxml";
        cardCount = 14;
        break;
      case "3":
        level = "Hard.fxml";
        cardCount = 30;
        break;
      default:
        level = "Normal.fxml";
        cardCount = 22;
    }
    System.out.println(player1Name);
    System.out.println(player2Name);
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(level));
    Scene scene = new Scene(fxmlLoader.load());
    stage.setTitle("Memory Game");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}