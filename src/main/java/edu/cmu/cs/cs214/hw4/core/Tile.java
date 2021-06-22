package edu.cmu.cs.cs214.hw4.core;

import java.util.HashMap;
import java.util.Map;

public class Tile {
  private Enum<TileName> name;
  private Map<String, Segment> segments;
  private boolean shield;
  private int rotattionTimes;


  /**
   * Default constructor
   */
  public Tile() {
    rotattionTimes = 0;
  }
  /**
   * constructor for tile.
   * @param t Tile object
   */
  public Tile(Tile t) {
    this.name = t.name;
    this.segments = new HashMap<>();
    for (Map.Entry<String, Segment> entry : t.segments.entrySet()) {
      this.segments.put(entry.getKey(), entry.getValue());
    }
    this.shield = t.shield;
    this.rotattionTimes = t.rotattionTimes;
  }
  /**
   * Tile constructor.
   * @param name name of tile
   * @param segments a JSONItem object to represent direction and segment relationship
   * @param shield whether this tile contains shield or not
   */
  public Tile(String name, JSONConfigReader.JSONItem segments, boolean shield) {
    this.name =  TileName.valueOf(name);
    this.shield = shield;
    this.segments = new HashMap<String, Segment>();
    this.segments.put("North", new Segment(SegmentType.valueOf(segments.North), -1));
    this.segments.put("East", new Segment(SegmentType.valueOf(segments.East), -1));
    this.segments.put("South", new Segment(SegmentType.valueOf(segments.South), -1));
    this.segments.put("West", new Segment(SegmentType.valueOf(segments.West), -1));
    this.segments.put("Center", new Segment(SegmentType.valueOf(segments.Center), -1));
    this.rotattionTimes = 0;
  }

  /**
   * See if this tile has shield.
   * @return boolean isShield
   */
  public boolean isShield() {
    return shield;
  }

  public Enum<TileName> getName() {
    return name;
  }

  /**
   * Still shallow copying
   * @return segment map
   */
  public Map<String, Segment> getSegments() {
    Map<String, Segment> copySegment = new HashMap<>();
    copySegment.put("North", segments.get("North"));
    copySegment.put("East", segments.get("East"));
    copySegment.put("South", segments.get("South"));
    copySegment.put("West", segments.get("West"));
    copySegment.put("Center", segments.get("Center"));
    return copySegment;
  }

  public SegmentType getSegmentType(String dir) {
    return segments.get(dir).getName();
  }
  /**
   * Rotate clockwise.
   * @return rotated tile instance
   */
  public Tile rotate() {
    rotattionTimes = (rotattionTimes + 1) % 4;
    Segment n = segments.get("North");
    segments.put("North", segments.get("West"));
    segments.put("West", segments.get("South"));
    segments.put("South", segments.get("East"));
    segments.put("East", n);
    return this;
  }
  @Override
  public String toString() {
    return String.format("Tile %s, N: %s, E: %s, S: %s, W: %s, C: %s",
        name,
        segments.get("North"),
        segments.get("East"),
        segments.get("South"),
        segments.get("West"),
        segments.get("Center")
    );
  }

  /**
   * get rotation times, 0,1,2,3
   * @return
   */
  public int getRotattionTimes() {
    return rotattionTimes;
  }

  /**
   * set rotation times
   * @param rotattionTimes int range from 0 - 3
   */
  public void setRotattionTimes(int rotattionTimes) {
    this.rotattionTimes = rotattionTimes;
  }
}
