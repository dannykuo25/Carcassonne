package edu.cmu.cs.cs214.hw4.core;

import java.util.List;

/**
 * Feature Interface to represent features on the board.
 */
public interface Feature {
  /**
   * Place meeple on this feature.
   * @param p player
   * @return true if place meeple is successful
   */
  public boolean placeMeeple(Player p);

  /**
   * Score this feature to players.
   * @return score to players
   */
  public int scoreFeature();

  /**
   * Combine 2 features into 1 feature.
   * It requires one of openings in both 2 features are the same as the placing tile position in this round.
   * Update player -> meeple mapping
   * update num of tiles
   * update opening
   * @return whether combine is successful or not
   */
  public boolean updateFeature();


  public boolean combine();

  public void scoreIncompleteFeature(List<Player> players);

//  public incompleteScore(Player[] players);
}
