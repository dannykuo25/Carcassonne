package edu.cmu.cs.cs214.hw4.core;

/**
 * Segment representation in a tile.
 * meeple == -1 means no meeple on this segment.
 * meeple == 0 ~ number of players - 1 means the playerIndex who place a meeple on this tile.
 */
public class Segment {
  private SegmentType name;
  private int meeple;

  /**
   * Constructor
   * @param name SegmentType name
   * @param meeple meeple owner id
   */
  public Segment(SegmentType name, int meeple) {
    this.name = name;
    this.meeple = meeple;
  }

  /**
   * Get meeple id
   * @return meeple owner id
   */
  public int getMeeple() {
    return meeple;
  }

  /**
   * Set meeple id
   * @param meeple meeple id
   */
  public void setMeeple(int meeple) {
    this.meeple = meeple;
  }

  /**
   * get segment type
   * @return
   */
  public SegmentType getName() {
    return name;
  }

  public void setName(SegmentType name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name + " " + meeple;
  }
}
