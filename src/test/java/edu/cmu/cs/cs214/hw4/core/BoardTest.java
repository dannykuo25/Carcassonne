package edu.cmu.cs.cs214.hw4.core;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class BoardTest {
  private GameImpl g;

  @Before
  public void setUp() {
    g = new GameImpl();
    g.addGameChangeListener(new GameChangeListener() {
      @Override
      public void curTileChanged(Tile tile) {

      }

      @Override
      public void tileChanged(int row, int col) {

      }

      @Override
      public void currentPlayerChanged(Player player) {

      }

      @Override
      public void scoreChanged(List<Player> players) {

      }

      @Override
      public void gameEnded(Player winner) {

      }

      @Override
      public void meepleChanged(int x, int y, Direction dir) {

      }
    });
    g.startGame();
  }

  @Test
  public void testBoard() {
    System.out.println(g.getBoard().toString());
    while (!g.getIsGameOver()) {
      Tile t = g.drawPlaceableTile();
      System.out.println(t);
      if (t == null) {
        g.setIsGameOver(true);
        break;
      }
      // input
      List<List<Integer>> cells = g.getBoard().placeableCells(t);
      g.placeTile(cells.get(0).get(0), cells.get(0).get(1));
      if (g.getCurrentPlayer().getNumMeeple() > 0) {
        g.placeMeeple(Direction.North);
      }
      g.nextTurn();
    }
    g.scoreIncompletedFeatures();
    g.getWinner();

    System.out.println(g.getBoard().toString());
    System.out.println("Finish testing board");
  }

}
