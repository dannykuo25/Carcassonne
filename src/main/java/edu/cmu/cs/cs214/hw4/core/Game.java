package edu.cmu.cs.cs214.hw4.core;

/**
 * The game interface is used by the GUI to report GUI related events to
 * the core implementation.
 */
public interface Game {
  /**
   * Register a game change listener to be notified of game change events.
   *
   * @param listener The listener to be notified of game change events.
   */
  void addGameChangeListener(GameChangeListener listener);

  /**
   * Start a game.
   */
  void startGame();

  /**
   * Get the player that currently has the turn.
   *
   * @return The player that currently has the turn.
   */
  Player getCurrentPlayer();

  /**
   * Get current tile to render on gui.
   * @return current tile
   */
  Tile getCurrentTile();

  /**
   * Attempts to place the current player's decision tile on a square in the game
   * grid. If true is returned, then the move is valid and the change is made;
   * otherwise, the move is invaid and nothing is changed.
   *
   * @param row The row of the current player's move.
   *
   * @param col The column of the current player's move.
   *
   * @throws IllegalStateException if the game has not yet been started.
   *
   * @return true if the move was valid and made, false otherwise false can
   *         mean that the coordinates were outside of the board or occupied
   *         already.
   */
  boolean placeTile(int row, int col);

  /**
   * Go to the next turn.
   */
  void nextTurn();

  /**
   * Attempts to place the current player's meeple on a diection of a tile in the game
   * grid. If true is returned, then the move is valid and the change is made;
   * otherwise, the move is invaid and nothing is changed.
   *
   * @param dir direction enum
   * @return true if the move was valid and made, false otherwise false can
   *         mean that the place has occupied by other meeples already, or
   *         the SegmentType of that direction of that tile is village,
   *         roadCity, or field.
   */
  boolean placeMeeple(Direction dir);

  /**
   * Rotate current tile.
   */
  void totateCurTile();


  /**
   * Get tile by row and col
   * @param row row
   * @param col col
   * @return Tile
   */
  Tile getTile(int row, int col);
}
