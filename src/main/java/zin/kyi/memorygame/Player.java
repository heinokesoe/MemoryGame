package zin.kyi.memorygame;

public class Player {
  private String name;
  private int score;

  public Player(String name) {
    this.name = name;
    this.score = 0;
  }

  public String getName() {
    return name;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getScore() {
    return score;
  }

  public void incrementScore() {
    score++;
  }

}