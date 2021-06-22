package edu.cmu.cs.cs214.hw4.core;

/**
 * Player class contains a player's information, which
 * includes number(id), score, and number of meeples.
 */
public class Player {
  private int id;
  private int score;
  private int numMeeple;

  /**
   * Player constructor.
   */
  public Player() {
    score = 0;
    numMeeple = 7;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  /**
   * Remove 1 meeple from player's supply.
   */
  public void sendMeepleOut() {
    numMeeple--;
  }
  public int getNumMeeple() {
    return numMeeple;
  }

  public void setNumMeeple(int numMeeple) {
    this.numMeeple = numMeeple;
  }

  @Override
  public String toString() {
    return "Player " + id;
  }
}
