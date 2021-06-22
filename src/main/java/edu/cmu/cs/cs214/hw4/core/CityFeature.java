package edu.cmu.cs.cs214.hw4.core;

import java.util.List;

public class CityFeature implements Feature {

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

  @Override
  public void scoreIncompleteFeature(List<Player> players) {

  }
}
