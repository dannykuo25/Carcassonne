package edu.cmu.cs.cs214.hw4.core;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Deck class to store a list of tiles on the board.
 */
public class Deck {
  private List<Tile> tiles;

  /**
   * parser.
   * @param configFile config file.
   * @return JSONTiles format
   */
  public static JSONConfigReader.JSONTiles parse(String configFile) {
    Gson gson = new Gson();
    try (Reader reader = new FileReader(new File(configFile))) {
      JSONConfigReader.JSONTiles result = gson.fromJson(reader, JSONConfigReader.JSONTiles.class);
      return result;
    } catch (IOException e) {
      throw new IllegalArgumentException("Error when reading file: " + configFile, e);
    }
  }

  /**
   * Deck constructor.
   */
  public Deck() {
    tiles = new ArrayList<>();
    // initialize deck
    JSONConfigReader.JSONTiles res = parse("src/main/resources/config.json");
    for (int i = 0; i < res.tiles.length; i++) {
      int amount = res.tiles[i].amount;
      for (int j = 0; j < amount; j++) {
        Tile t = new Tile(res.tiles[i].name, res.tiles[i].segments, res.tiles[i].shield);
        tiles.add(t);
      }
    }
  }
//  public void shuffle() {}

  /**
   * Getter for tiles
   * @return list of tiles(defensive copy)
   */
  public List<Tile> getTiles() {
    List<Tile> result = new ArrayList<>();
    for (Tile t : tiles) {
      result.add(new Tile(t));
    }
    return result;
  }
  /**
   * Draw a tile from deck. If no more tiles to draw, return null.
   * @return A Tile object. Return null if deck is empty
   */
  public Tile draw() {
    if (tiles.size() == 0) {
      return null;
    }
    return tiles.remove(tiles.size() - 1);
  }

  /**
   * Draw a tile by name.
   * @param tileName tile name string
   * @return a Tile object.
   */
  public Tile drawByName(String tileName) {
    if (tiles.size() == 0) {
      return null;
    }
    for (int i = 0; i < tiles.size(); i++) {
      if (tiles.get(i).getName() == TileName.valueOf(tileName)) {
        Tile result = new Tile(tiles.get(i));
        tiles.remove(i);
        return result;
      }
    }
    return null;
  }

  /**
   * Shuffle deck.
   */
  public void shuffle() {
    Collections.shuffle(tiles);
  }
}
