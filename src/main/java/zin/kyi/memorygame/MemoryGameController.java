package zin.kyi.memorygame;

import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class MemoryGameController implements Initializable {

  @FXML
  private AnchorPane mainPane;
  @FXML
  private FlowPane imagesFlowPane;
  @FXML
  private Label player1Name;
  @FXML
  private Label player2Name;
  @FXML
  private Label player1Score;
  @FXML
  private Label player2Score;
  @FXML
  private ImageView player1Turn;
  @FXML
  private ImageView player2Turn;
  @FXML
  private ImageView playAgain;
  @FXML
  private ImageView exit;
  private int count = 0;
  private ArrayList<MemoryCard> cardsInGame;
  private MemoryCard firstCard, secondCard;
  private SoundEffect sound;
  private boolean isPlayingAgain = false;
  private boolean isPlayer1 = true;
  public static String winner;
  private Player player1;
  private Player player2;
  private int firstCardIndex;
  private int secondCardIndex;

  @FXML
  void playAgain() {
    firstCard=null;
    secondCard =null;

    DeckOfCards deck = new DeckOfCards();
    deck.shuffle();
    cardsInGame = new ArrayList<>();

    for (int i =0; i<imagesFlowPane.getChildren().size()/2; i++)
    {
      Card cardDealt = deck.dealTopCard();
      cardsInGame.add(new MemoryCard(cardDealt.getSuit(),cardDealt.getFaceName()));
      cardsInGame.add(new MemoryCard(cardDealt.getSuit(),cardDealt.getFaceName()));
    }
    Collections.shuffle(cardsInGame);
    isPlayingAgain = true;
    flipAllCards();
    player2Turn.setVisible(false);

  }

  @FXML
  void exit() {
    System.exit(0);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initializeImageView();
    player1Name.setText(player1.getName());
    player2Name.setText(player2.getName());
    playAgain();
  }

  /**
   * This will add a number to each ImageView and set the image to be the back of a Card
   */
  private void initializeImageView()
  {
    for (int i=0; i<imagesFlowPane.getChildren().size();i++)
    {
      //"cast" the Node to be of type ImageView
      ImageView imageView = (ImageView) imagesFlowPane.getChildren().get(i);
      imageView.setImage(new Image(Card.class.getResourceAsStream("images/back_of_card.png")));
      imageView.setUserData(i);

      //register a click listener
      imageView.setOnMouseClicked(event -> {
        sound.playSound();
        flipCard((int) imageView.getUserData());
      });
    }
  }

  /**
   * This will show the back of all cards that are not matched
   */
  private void flipAllCards()
  {
    for (int i=0; i<Main.cardCount;i++)
    {
      ImageView imageView = (ImageView) imagesFlowPane.getChildren().get(i);
      MemoryCard card = cardsInGame.get(i);

      if (card.isMatched())
        imageView.setVisible(false);
      else if (isPlayingAgain) {
        imageView.setVisible(true);
        imageView.setImage(card.getBackOfCardImage());
        player1.setScore(0);
        player2.setScore(0);
        updateLabels();
      } else
        imageView.setImage(card.getBackOfCardImage());

    }
  }

  private void flipCard(int indexOfCard)
  {
    ++count;
    ImageView imageView = (ImageView) imagesFlowPane.getChildren().get(indexOfCard);
    RotateTransition rotator = createRotator(imageView);
    if (firstCard==null)
    {
      firstCard = cardsInGame.get(indexOfCard);
      firstCardIndex = indexOfCard;
      PauseTransition ptChangeCardFace = changeCardFace(imageView,firstCard.getImage());
      ParallelTransition parallelTransition = new ParallelTransition(rotator, ptChangeCardFace);
      parallelTransition.play();
    }
    else if (secondCard==null)
    {
      secondCard = cardsInGame.get(indexOfCard);
      secondCardIndex = indexOfCard;
      PauseTransition ptChangeCardFace = changeCardFace(imageView,secondCard.getImage());
      ParallelTransition parallelTransition = new ParallelTransition(rotator, ptChangeCardFace);
      parallelTransition.play();
      new java.util.Timer().schedule(
          new java.util.TimerTask() {
            @Override
            public void run() {
              flipAllCards();
            }
          },
          1500
      );
      checkForMatch();
      updateLabels();
    }
    isPlayingAgain = false;
    if (count == 2) {
      isPlayer1 = !isPlayer1;
      if (isPlayer1) {
        player1Turn.setVisible(true);
        player2Turn.setVisible(false);
      } else {
        player1Turn.setVisible(false);
        player2Turn.setVisible(true);
      }
      count = 0;
    }
  }
  private RotateTransition createRotator(ImageView card) {
    RotateTransition rotator = new RotateTransition(Duration.millis(1000), card);
    rotator.setAxis(Rotate.Y_AXIS);
    rotator.setFromAngle(0);
    rotator.setToAngle(180);
    rotator.setInterpolator(Interpolator.LINEAR);
    rotator.setCycleCount(1);

    return rotator;
  }
  private PauseTransition changeCardFace(ImageView card, Image image) {
    PauseTransition pause = new PauseTransition(Duration.millis(500));

    pause.setOnFinished(
        e -> {
          card.setImage(image);
        });

    return pause;
  }

  private void updateLabels()
  {
    player1Score.setText(Integer.toString(player1.getScore()));
    player2Score.setText(Integer.toString(player2.getScore()));
  }

  public MemoryGameController() {
    player1 = new Player(Main.player1Name);
    if (player1.getName().isEmpty())
      player1.setName("Player 1");
    player2 = new Player(Main.player2Name);
    if (player2.getName().isEmpty())
      player2.setName("Player 2");
    sound = new SoundEffect();
  }

  private void checkForMatch()
  {
    if (firstCardIndex != secondCardIndex && firstCard.isSameCard(secondCard)) {
      firstCard.setMatched(true);
      secondCard.setMatched(true);
      if (isPlayer1) {
        player1.incrementScore();
        player1Turn.setVisible(false);
        player2Turn.setVisible(true);
      } else {
        player2.incrementScore();
        player1Turn.setVisible(true);
        player2Turn.setVisible(false);
      }
    }
    firstCard=null;
    secondCard=null;
    showWinner();
    }
    private void showWinner() {
      if (player1.getScore()+player2.getScore() == Main.cardCount/2) {
        winner = (player1.getScore() > player2.getScore()) ? player1.getName() : player2.getName();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Winner.fxml"));
        Parent root = null;
        try {
          root = loader.load();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
        WinnerController winnerController = loader.getController();
        winnerController.displayWinnerName(winner);
        Stage stage = (Stage) mainPane.getScene().getWindow();;
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Winner");
        stage.show();
      }
  }

}