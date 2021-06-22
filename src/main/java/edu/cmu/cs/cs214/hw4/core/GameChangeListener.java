package edu.cmu.cs.cs214.hw4.core;

import java.util.List;

/**
 * Listener for change in game state.
 */
public interface GameChangeListener {

  /**
   * Called when the current tile changed.
   * @param tile The new current tile.
   */
  void curTileChanged(Tile tile);

  /**
   * Called when any tile on the board changes. This
   * includes changes to initialize a fresh board.
   *
   * @param row The row of the updated cell on the board.
   * @param col The column of the updated cell on the board.
   */
  void tileChanged(int row, int col);

  /**
   * Called when the current player changed
   *
   * @param player The new current player.
   */
  void currentPlayerChanged(Player player);

  /**
   * Called when the score changed.
   * @param players all players
   */
  void scoreChanged(List<Player> players);

  /**
   * Called when the game ends, announcing the winner (or null on a tie).
   *
   * @param winner The winner of the game, or null on a tie.
   */
  void gameEnded(Player winner);

  /**
   * Called when the meeple changed.
   * @param dir direction
   * @param x x.
   * @param y y.
   */
  void meepleChanged(int x, int y, Direction dir);
}
