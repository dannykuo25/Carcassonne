package edu.cmu.cs.cs214.hw4.core;

import java.util.List;
import java.util.Map;


public class MonasteryFeature implements Feature {
  private int numOfTiles;
  private List<List<Integer>> openings;
  private Player p;
  private Board board;
  private int[] position;


  public MonasteryFeature(Player currentPlayer, Board board, int x, int y) {
    this.p = currentPlayer;
    this.board = board;
    this.position = new int[] {x, y};
  }

  @Override
  public boolean placeMeeple(Player p) {
    return false;
  }

  @Override
  public int scoreFeature() {
    return 0;
  }

  @Override
  public boolean updateFeature() {
    return false;
  }

  @Override
  public boolean combine() {
    return false;
  }

  /**
   * Go over near-by 8 tiles, add points to
   * @param players
   */
  @Override
  public void scoreIncompleteFeature(List<Player> players) {
    numOfTiles = getNumOfTiles();
    p.setScore(p.getScore() + numOfTiles);
  }

  private int getNumOfTiles() {
    int result = 0;
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (board.getTiles()[position[0] - 1 + i][position[1] - 1 + j] != null) {
          result++;
        }
      }
    }
    return result;
  }
}
