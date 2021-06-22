package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Main game logic.
 */
public class GameImpl implements Game {
  public static final int BOARD_SIZE = 144;
  public static final int NUM_TILE_EDGES = 4;
  private final Deck deck;
  private final List<Player> playerList;
  private boolean isGameOver;
  private int currPlayerIndex;
  private final Board board;
  private int[] currentXY;
  private GameChangeListener listener;
  private Tile currentTile;

  /**
   * Game constructor.
   */
  public GameImpl() {
    deck = new Deck();
    isGameOver = false;
    board = new Board();
    playerList = new ArrayList<Player>();
    currPlayerIndex = 0;
  }

  /**
   * Start the game by placing the first "D" tile on the Center of the board.
   * you'll draw that specific tile out of the deck to put on the board (and then shuffle the deck).
   */
  @Override
  public void startGame() {
    // draw default tile
    currentTile = deck.drawByName("D");
    // rotate to default rotation
    for (int i = 0; i < 3; i++) currentTile = currentTile.rotate();
    // place on board object
    board.placeTileOnBoard(currentTile, BOARD_SIZE/2, BOARD_SIZE/2);
    // shuffle deck
    deck.shuffle();
    // set num of players
    setNumOfPlayers(2);
    // notify change
    notifyMoveMade(BOARD_SIZE/2, BOARD_SIZE/2);
    notifyPlayerChanged();
    // draw a new tile, and change current tile on gui
    currentTile = drawPlaceableTile();
    notifyCurTileChanged();
  }

  private void notifyCurTileChanged() {
    listener.curTileChanged(currentTile);
  }

  private void notifyMoveMade(int r, int c) {
    listener.tileChanged(r, c);
  }

  private void notifyPlayerChanged() {
    listener.currentPlayerChanged(getCurrentPlayer());
  }


  /**
   * Place a tile on x-y coordinates.
   * @param x x coordinate
   * @param y y coordinate
   * @return whether successfully place a tile or not
   */
  public boolean placeTile(int x, int y) {
    if (!board.isCellEmpty(x, y)) {
      return false;
    }
    if (!board.isPlaceableCell(currentTile, x, y)) {
      return false;
    }
    board.placeTileOnBoard(currentTile, x, y);
    currentXY = new int[] {x, y};
    notifyMoveMade(x, y);
    return true;
  }

  /**
   * Place a meeple on a segment of the current tile.
   * If village, field, roadcity, false
   * @param dir direction to put a meeple on the current tile.
   * @return success or not.
   */
  public boolean placeMeeple(Direction dir) {
    if (getCurrentPlayer().getNumMeeple() <= 0) return false;
    if (board.getTile(currentXY[0], currentXY[1]).getSegments().get(String.valueOf(dir)).getName().equals(SegmentType.Village)
        || board.getTile(currentXY[0], currentXY[1]).getSegments().get(String.valueOf(dir)).getName().equals(SegmentType.Field)
        || board.getTile(currentXY[0], currentXY[1]).getSegments().get(String.valueOf(dir)).getName().equals(SegmentType.RoadCity)
    ) {
      return false;
    }
    if (board.isOccupied(currentXY[0], currentXY[1], String.valueOf(dir))) {
      return false;
    }
    // place meeple on board
    board.placeMeepleOnBoard(String.valueOf(dir), currentXY[0], currentXY[1], currPlayerIndex);
    // remove 1 meeple from player's supply
    getCurrentPlayer().sendMeepleOut();
    // notify gui
    notifyMeepleChanged(dir);
    return true;
  }

  @Override
  public void totateCurTile() {
    currentTile = currentTile.rotate();
    notifyCurTileChanged();
  }

  @Override
  public Tile getTile(int row, int col) {
    return board.getTile(row, col);
  }

  private void notifyMeepleChanged(Direction dir) {
    System.out.println("notify meeple changed(placed)");
    listener.meepleChanged(currentXY[0], currentXY[1], dir);
  }

  /**
   * update features.
   * @param x x
   * @param y y
   */
  public void updateFeatures(int x, int y) {
    board.updateFeatures(x, y, getCurrentPlayer());
  }

  /**
   * Go to next turn by move current player index by 1.
   */
  @Override
  public void nextTurn() {
    System.out.println("clicking next turn button");
    // update current player index
    currPlayerIndex = ((currPlayerIndex + 1) % playerList.size());
    notifyPlayerChanged();
    // update current tile
    currentTile = drawPlaceableTile();
    notifyCurTileChanged();
    // check is game over
    if (currentTile == null) {
      System.out.println("game over");
    }
  }

  /**
   * At the start of a game, set the number of players.
   * @param n number of players to play this game
   */
  public void setNumOfPlayers(int n) {
    for (int i = 0; i < n; i++) {
      Player p = new Player();
      p.setId(i);
      playerList.add(p);
    }
    currPlayerIndex = 0;
  }

  /**
   * Number of players.
   * @return number of players
   */
  public int numOfPlayers() {
    return playerList.size();
  }

  /**
   * Is game over or not.
   * @return Is game over or not
   */
  public boolean getIsGameOver() {
    return isGameOver;
  }

  /**
   * is game over setter.
   * @param isOver boolean value setting whether the game is over or not.
   */
  public void setIsGameOver(boolean isOver) {
    isGameOver = isOver;
  }

  /**
   * draw a tile from deck, return null if empty.
   * @return a Tile object or null
   */
  public Tile drawPlaceableTile() {
    while (true) {
      Tile t = deck.draw();
      if (t == null) {
        currentTile = null;
        return null;
      }
      if (board.isPlaceable(t)) {
        currentTile = t;
        return t;
      }
    }
  }

  /**
   * Get number of tiles left in deck.
   * @return number of tiles left in deck
   */
  public int numOfTilesLeft() {
    return deck.getTiles().size();
  }

  /**
   * Get board (possibly not secure)
   * @return board
   */
  public Board getBoard() {
    return board;
  }

  /**
   * Get deck (not secure)
   * @return deck
   */
  public Deck getDeck() {
    return deck;
  }

  /**
   * Get current player
   * @return current player
   */
  @Override
  public Player getCurrentPlayer() {
    return playerList.get(currPlayerIndex);
  }

  @Override
  public Tile getCurrentTile() {
    return currentTile;
  }

  /**
   * Setter for current tile.
   * @param t tile object
   */
  public void setCurrentTile(Tile t) {
    currentTile = t;
  }

  public int currPlayerIndex() {
    return currPlayerIndex;
  }

  public void setCurrPlayerIndex(int currPlayerIndex) {
    this.currPlayerIndex = currPlayerIndex;
  }

  public void scoreIncompletedFeatures() {
    board.scoreIncompletedFeatures(playerList);
  }

  /**
   * Get winner with string.
   * @return String winner message with score
   */
  public String getWinner() {
    Player winner = playerList.get(0);
    for (Player p : playerList) {
      if (p.getScore() > winner.getScore()) {
        winner = p;
      }
    }
    return winner.toString() + "wins with " + winner.getScore() + "points!";
  }

  @Override
  public void addGameChangeListener(GameChangeListener listener) {
    this.listener = listener;
  }
}
