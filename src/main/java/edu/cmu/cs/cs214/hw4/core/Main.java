package edu.cmu.cs.cs214.hw4.core;

import edu.cmu.cs.cs214.hw4.gui.GameBoardPanel;

import javax.swing.*;
import java.util.List;
import java.util.Scanner;

/**
 * Main function to execute the game.
 */
public class Main {
  public static final int NUM_PLAYERS = 2;

  /**
   * Main function to execute the game by command line.
   * @param args command line argument
   */
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      createAndShowGameBoard();
    });
  }

  private static void createAndShowGameBoard() {
    JFrame frame = new JFrame("Carcassonne game");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    Game game = new GameImpl();

    GameBoardPanel gamePanel = new GameBoardPanel(game);
    gamePanel.setOpaque(true);
    frame.setContentPane(gamePanel);
    game.startGame();

    // Display the window.
    frame.pack();
    frame.setVisible(true);
  }

}
