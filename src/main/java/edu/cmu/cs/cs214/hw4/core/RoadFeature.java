package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Road feature has 3 methods.
 */
public class RoadFeature implements Feature {
  private int numOfTiles;
  private List<List<Integer>> openings;
  private Map<Player, Integer> map;

  /**
   * Constructor.
   * @param openXY x y coordinates.
   */
  public RoadFeature(int[] openXY) {
    numOfTiles = 1;
    openings = new ArrayList<>();
    List<Integer> xy = new ArrayList<>();
    xy.add(openXY[0], openXY[1]);
    openings.add(xy);
    map = new HashMap<>();
  }

  @Override
  public boolean placeMeeple(Player p) {
    if (map.size() > 0) {
      return false;
    }
    if (!map.containsKey(p)) {
      map.put(p, 1);
    } else {
      map.put(p, map.get(p) + 1);
    }
    return true;
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

  @Override
  public void scoreIncompleteFeature(List<Player> players) {

  }


//  a list of  (x y  coordinate of a tile)
//  segments
//  1. openings
//  2. number of tiles
//  3. map of meeple
}
