package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Game board
 */
public class Board {
  private Tile[][] tiles;
  private List<Feature> features;

  /**
   * Board constructor.
   */
  public Board() {
    tiles = new Tile[GameImpl.BOARD_SIZE][GameImpl.BOARD_SIZE];
    features = new ArrayList<>();
  }

  /**
   * Place tile on board.
   * @param t Tile to place
   * @param x x coordinate
   * @param y y coordinate
   */
  public void placeTileOnBoard(Tile t, int x, int y) {
    tiles[x][y] = new Tile(t);
  }

  /**
   * Check if this grid is not occupied by any tile.
   * @param x x coordinate.
   * @param y y coordinate.
   * @return empty grid or not
   */
  public boolean isCellEmpty(int x, int y) {
    return tiles[x][y] == null;
  }

  /**
   * Get neighbor tiles from coordinate x and y.
   * @param x x coordinate
   * @param y y coordinate
   * @return an array of tiles, [North, East, South, West]
   */
  public Tile[] neighborTiles(int x, int y) {
    Tile[] neighbors = new Tile[4];
    if (x > 0 && tiles[x - 1][y] != null) {
      neighbors[0] = new Tile(tiles[x - 1][y]);
    }
    if (y < tiles[0].length - 1 && tiles[x][y + 1] != null) {
      neighbors[1] = new Tile(tiles[x][y + 1]);
    }
    if (x < tiles.length - 1 && tiles[x + 1][y] != null) {
      neighbors[2] = new Tile(tiles[x + 1][y]);
    }
    if (y > 0 && tiles[x][y - 1] != null) {
      neighbors[3] = new Tile(tiles[x][y - 1]);
    }
    return neighbors;
  }

  /**
   * Check if this tile can be placed at least 1 place on the board.
   * @param t Tile
   * @return true if is valid, false otherwise
   */
  public boolean isPlaceable(Tile t) {
    for (int i = 0; i < tiles.length; i++) {
      for (int j = 0; j < tiles[0].length; j++) {
        boolean result = isPlaceableCell(t, i, j);
        if (result) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Is a tile can place in this cell.
   * @param t tile
   * @param x x coordinate
   * @param y y coordinate
   * @return true or false
   */
  public boolean isPlaceableCell(Tile t, int x, int y) {
    Tile[] neighbors = neighborTiles(x, y);
    // no neighbors
    if (neighbors[0] == null && neighbors[1] == null && neighbors[2] == null && neighbors[3] == null) {
      return false;
    }
    // at least 1 matching segment
    if ((neighbors[0] == null || neighbors[0].getSegments().get("South").getName().equals(t.getSegments().get("North").getName()))
        && (neighbors[1] == null || neighbors[1].getSegments().get("West").getName().equals(t.getSegments().get("East").getName()))
        && (neighbors[2] == null || neighbors[2].getSegments().get("North").getName().equals(t.getSegments().get("South").getName()))
        && (neighbors[3] == null || neighbors[3].getSegments().get("East").getName().equals(t.getSegments().get("West").getName()))
    ) {
      return true;
    }
    return false;
  }

  /**
   * Get placeable cells that enable a tile to place onto.
   * @param t Tile
   * @return 2d x-y coordinates array
   */
  public List<List<Integer>> placeableCells(Tile t) {
    List<List<Integer>> result = new ArrayList<>();
    for (int i = 0; i < tiles.length; i++) {
      for (int j = 0; j < tiles[0].length; j++) {
        if (isPlaceableCell(t, i, j)) {
          List<Integer> cell = new ArrayList<>();
          cell.add(i);
          cell.add(j);
          result.add(cell);
        }
      }
    }
    return result;
  }

  /**
   * TODO: update feature.
   * Go over features list, in every feature openings, if matches x-y, then is updateCandidates.
   * call update method in every Feature implementation.
   * 4 directions:
   * neighbor: update existing feature
   * no neighbot: add feature
   * Center is monastery: add feature
   * @param x x
   * @param y y
   * @param currentPlayer current player
   */
  public void updateFeatures(int x, int y, Player currentPlayer) {
    Tile t = tiles[x][y];
    // add new feature
    // Center
    if (t.getSegments().get("Center").getName().equals(SegmentType.Monastery)) {
      if (t.getSegments().get("Center").getMeeple() == 1) {
        MonasteryFeature mf = new MonasteryFeature(currentPlayer, this, x, y);
        features.add(mf);
      }
    }
    boolean[] newMark = new boolean[4];
    String[] dirs = {"North", "East", "South", "West"};
    Arrays.fill(newMark, true);
    Tile[] neighbors = neighborTiles(x, y); // up right down left
    for (int i = 0; i < neighbors.length; i++) {
      if (neighbors[i] == null) continue;
      newMark[i] = false;
      // if equal to Center, connect to more segment and set additional segment mark to false
      if (t.getSegmentType(dirs[i]).equals(t.getSegmentType("Center"))) {
        List<Integer> linkingDirs = new ArrayList<>();
        for (int j = 0; j < 4; j++) {
          if (t.getSegmentType(dirs[i]).equals(t.getSegmentType(dirs[j]))) {
            linkingDirs.add(j);
            newMark[j] = false;
          }
        }
      }
      // update this neighbor's feature with connecting segments
    }
    // add new features in 4 dirs if newMark == true
    for (int i = 0; i < 4; i++) {
      if (!newMark[i]) {
          if (t.getSegmentType(dirs[i]).equals(SegmentType.Road)) {
            int[] openXY = getOpenXY(x, y, dirs[i]);
            Feature newFeature = new RoadFeature(openXY);
            features.add(newFeature);
          } else if (t.getSegmentType(dirs[i]).equals(SegmentType.City)) {
            Feature newFeature = new CityFeature();
            features.add(newFeature);
          }
      }
    }
    // update existing feature
    // combine multiple features
  }

  private int[] getOpenXY(int x, int y, String dir) {
    int[] result = new int[2];
    result[0] = x;
    result[1] = y;
    switch (dir) {
      case "North":
        result[0] = x - 1;
        break;
      case "East":
        result[1] = y + 1;
        break;
      case "South":
        result[0] = x + 1;
        break;
      case "West":
        result[1] = y - 1;
        break;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < tiles.length; i++) {
      for (int j = 0; j < tiles[0].length; j++) {
        if (tiles[i][j] == null) {
          result.append("- ");
        } else {
          result.append(tiles[i][j].getName()).append(" ");
        }
      }
      result.append("\n");
    }
    return result.toString();
  }

  /**
   * All tiles representation on the board.
   * @return All tiles representation on the board.
   */
  public String tileList() {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < tiles.length; i++) {
      for (int j = 0; j < tiles[0].length; j++) {
        if (tiles[i][j] != null) {
          result.append(tiles[i][j]);
          result.append("\n");
        }
      }
    }
    return result.toString();
  }

  /**
   * Get tile by xy.
   * @param x x
   * @param y y
   * @return tile object on the board
   */
  public Tile getTile(int x, int y) {
    return tiles[x][y];
  }

  /**
   * Get tiles
   * @return tiles
   */
  public Tile[][] getTiles() {
    return tiles;
  }

  /**
   * Place meeple on board by updating tile segment meeple status with player's index(id).
   * @param direction 5 directions string
   * @param x x
   * @param y y
   * @param playerId player id
   */
  public void placeMeepleOnBoard(String direction, int x, int y, int playerId) {
    tiles[x][y].getSegments().get(direction).setMeeple(playerId);
  }

  /**
   * Check if the segment is occupied or not.
   * @param x x
   * @param y u
   * @param direction string direction (North, South, West, East).
   * @return boolean occupied or not
   */
  public boolean isOccupied(int x, int y, String direction) {
    return helper(x, y, direction, direction, false);
  }

  private boolean helper(int x, int y, String dir, String prevDir, boolean isGoingOut) {
    String[] dirs = {"North", "East", "South", "West"};
    Tile t = tiles[x][y];
    if (t == null) return false;
    if (t.getSegments().get(dir).getMeeple() > 0) return true;
    boolean hasMeepleInsideTile = false;
    boolean hasMeepleOutsideTile = false;
    if (
        ( (t.getSegmentType("Center").equals(SegmentType.RoadCity) &&
            (t.getSegmentType(dir).equals(SegmentType.Road) || t.getSegmentType(dir).equals(SegmentType.City)) ) ||
            t.getSegmentType("Center").equals(t.getSegmentType(dir)) ) &&
            !isGoingOut) {
      for (String s : dirs) {
        if (s.equals(dir)) continue;
        if (t.getSegmentType(dir).equals(t.getSegmentType(s))) {
          hasMeepleInsideTile = helper(x, y, s, prevDir, true);
        }
      }
    }
    if (dir.equals("North") && !prevDir.equals("South")) {
      hasMeepleOutsideTile = helper(x - 1, y, "South", "North", false);
    } else if (dir.equals("East") && !prevDir.equals("West")) {
      hasMeepleOutsideTile = helper(x, y + 1, "West", "East", false);
    } else if (dir.equals("South") && !prevDir.equals("North")) {
      hasMeepleOutsideTile = helper(x + 1, y, "North", "South", false);
    } else if (dir.equals("West") && !prevDir.equals("East")) {
      hasMeepleOutsideTile = helper(x, y - 1, "East", "West", false);
    }
    return hasMeepleInsideTile || hasMeepleOutsideTile;
  }


  public void scoreIncompletedFeatures(List<Player> players) {
    for (Feature f : features) {
      f.scoreIncompleteFeature(players);
    }
  }

  public boolean isCellValid(Tile currentTile, int x, int y) {
    return (x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length &&
            isCellEmpty(x, y) &&
            isPlaceableCell(currentTile, x, y)
    );
  }
}
