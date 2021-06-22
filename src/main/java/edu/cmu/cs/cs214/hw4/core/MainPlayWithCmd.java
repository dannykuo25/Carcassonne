package edu.cmu.cs.cs214.hw4.core;

import java.util.List;
import java.util.Scanner;

/**
 * Main function to execute the game.
 */
public class MainPlayWithCmd {
  public static final int NUM_PLAYERS = 4;

  /**
   * Main function to execute the game by command line.
   * @param args command line argument
   */
  public static void main(String[] args) {
    GameImpl g = new GameImpl();
    // input
    g.setNumOfPlayers(NUM_PLAYERS);

    g.startGame();
    System.out.println("number of players: " + NUM_PLAYERS);
    while (!g.getIsGameOver()) {
      Tile t = null;
      while (true) {
        Scanner obj = new Scanner(System.in);
        System.out.println("enter tile name:");
        String tileName = obj.nextLine();
        t = g.getDeck().drawByName(tileName);
        g.setCurrentTile(t);
        Scanner discard = new Scanner(System.in);
        System.out.println("Discard? y/n");
        String discardAns = discard.nextLine();
        if (discardAns.equals("y")) {
          continue;
        } else {
          break;
        }
      }
      if (t == null) {
        break;
      }
      System.out.println(t);


      // input
      List<List<Integer>> cells = g.getBoard().placeableCells(t);
      System.out.println("Placeable cells:" + cells);

      Scanner xy = new Scanner(System.in);
      System.out.println("enter x y coordinates: (enter q to exit)");
      String xyAns = xy.nextLine();
      if (xyAns.equals("q")) break;
      String[] splited = xyAns.split(" ");

      g.placeTile(Integer.parseInt(splited[0]), Integer.parseInt(splited[1]));
      System.out.println(g.getBoard().tileList());

      // optionally place a meeple
      if (g.getCurrentPlayer().getNumMeeple() > 0) {
        g.placeMeeple(Direction.North);
      }
      // score completed features automatically

      g.nextTurn();
    }
    System.out.println(g.getBoard().toString());
    System.out.println("Good Game!");
  }
}
