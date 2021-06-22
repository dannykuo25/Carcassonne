package edu.cmu.cs.cs214.hw4.core;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;


public class JSONConfigReader {
  public class JSONItem {
    public String North;
    public String South;
    public String East;
    public String West;
    public String Center;
  }
  public class JSONTiles {
    public JSONTile[] tiles;
  }
  public class JSONTile {
    public String name;
    public JSONItem segments;
    public boolean shield;
    public int amount;
  }

  public static JSONTiles parse(String configFile) {
    Gson gson = new Gson();
    try (Reader reader = new FileReader(new File(configFile))) {
      JSONTiles result = gson.fromJson(reader, JSONTiles.class);
      return result;
    } catch (IOException e) {
      throw new IllegalArgumentException("Error when reading file: " + configFile, e);
    }
  }

  public static void main(String[] args) {
    JSONTiles result = parse("src/main/resources/config.json");
    for (int i = 0; i < 9; i++) {
      System.out.println(result.tiles[i].name);
      System.out.println(result.tiles[i].segments.North);
      System.out.println(result.tiles[i].segments.South);
      System.out.println(result.tiles[i].shield);
      System.out.println(result.tiles[i].amount);
    }
  }
}
