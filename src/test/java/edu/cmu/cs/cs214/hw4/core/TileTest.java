package edu.cmu.cs.cs214.hw4.core;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class TileTest {
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
  public void testTile() {
    while (!g.getIsGameOver()) {
      Tile t = g.drawPlaceableTile();
      System.out.println(t);
      if (t == null) {
        g.setIsGameOver(true);
        break;
      }
    }
    System.out.println("Finish printing all tiles");

  }

  @Test
  public void testRotate() {
    Tile t1 = g.getDeck().drawByName("D");
    for (int i = 0; i < 3; i++) t1.rotate();
    System.out.println(t1);
    Tile t2 = g.getDeck().drawByName("D");
    System.out.println(t2);
  }
}
