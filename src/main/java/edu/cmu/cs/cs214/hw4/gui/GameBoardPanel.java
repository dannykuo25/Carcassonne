package edu.cmu.cs.cs214.hw4.gui;

import edu.cmu.cs.cs214.hw4.core.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Game GUI.
 */
public class GameBoardPanel extends JPanel implements GameChangeListener {
  private static final String IMAGE_PATH = "./src/main/resources/Tiles.png";
  private final Game game;
  private final JButton[][] squares;
  private final JLabel currentPlayerLabel;
  private final JLabel actionLabel;
  private JButton currTile;

  /**
   * Constructor for game board panel
   * @param g game core implementation
   */
  public GameBoardPanel(Game g) {
    game = g;
    g.addGameChangeListener(this);

    currentPlayerLabel = new JLabel("hello");
    currentPlayerLabel.setForeground(Color.RED);
    currentPlayerLabel.setFont(new Font("Verdana", Font.PLAIN, 18));
    actionLabel = new JLabel("action");
    squares = new JButton[GameImpl.BOARD_SIZE][GameImpl.BOARD_SIZE];
    currTile = new JButton();

    setLayout(new BorderLayout());
    add(actionLabel, BorderLayout.NORTH);
    add(createBoardPanel(), BorderLayout.CENTER);
    add(createScorePanel(), BorderLayout.EAST);
    try {
      add(createControlPanel(), BorderLayout.SOUTH);
    } catch (IOException e) {
      System.out.println("wrong tile image path");
      e.printStackTrace();
    }
  }

  // TODO: score board
  private JPanel createScorePanel() {
    JPanel pane = new JPanel();
    return pane;
  }

  private JPanel createControlPanel() throws IOException {
    JPanel pane = new JPanel();
    pane.setLayout(new GridLayout(1, 5));
    pane.add(currentPlayerLabel, BorderLayout.NORTH);
    pane.add(createInstructionLabel());
    pane.add(createPlaceTilePane());
    pane.add(createPlaceMeeplePane());
    pane.add(createNextTurnPane());
    return pane;
  }

  // Create 4 buttons representing directions
  // click -> game.placeMeeple(0-3)
  private JPanel createPlaceMeeplePane() {
    JPanel pane = new JPanel();

    JLabel label = new JLabel("Place meeple:");
    pane.add(label);

    for (Direction dir : Direction.values()) {
      JButton button = new JButton(String.valueOf(dir));
      button.addActionListener(e -> {
        game.placeMeeple(dir);
      });
      pane.add(button);
    }
    return pane;
  }

  private JPanel createNextTurnPane() {
    JPanel pane = new JPanel();

    JButton button = new JButton("Next Turn");
    button.addActionListener(e -> {
      game.nextTurn();
    });
    pane.add(button);

    return pane;
  }

  private JPanel createPlaceTilePane() throws IOException {
    JPanel pane = new JPanel();

    JLabel label = new JLabel("Current tile:");
    pane.add(label);

    String path = "./src/main/resources/Tiles.png";
    BufferedImage image  = ImageIO.read(new File(path));
    BufferedImage tileImage = image.getSubimage(270, 0, 90, 90);
    // add tile image to button, then to panel
    JButton button = currTile;
    button.setPreferredSize(new Dimension(90, 90));
    ImageIcon icon = new ImageIcon(tileImage);
    button.setIcon(icon);
    pane.add(button);

    JButton btn = new JButton("Rotate");
    btn.addActionListener(e -> {
      game.totateCurTile();
    });
    pane.add(btn);

    return pane;
  }

  private JLabel createInstructionLabel() {
    String text = "Instructions\n" +
        "1. click on grid above to place a tile (rotate if you like) \n" +
        "2. click on meeple direction to place a meeple\n" +
        "3. click on Next Turn";
    return new JLabel(convertToMultiline(text));
  }

  private String convertToMultiline(String s) {
    return "<html>" + s.replaceAll("\n", "<br>");
  }

  private JScrollPane createBoardPanel() {
    JPanel pane = new JPanel();
    pane.setPreferredSize(new Dimension( 12960,12960));
    pane.setLayout(new GridLayout(GameImpl.BOARD_SIZE, GameImpl.BOARD_SIZE));
    JScrollPane scrollPane = new JScrollPane(pane);
    pane.setAutoscrolls(true);
    scrollPane.setPreferredSize(new Dimension(800, 650));
    scrollPane.getHorizontalScrollBar().setUnitIncrement(25);
    scrollPane.getVerticalScrollBar().setUnitIncrement(25);

    for (int i = 0; i < GameImpl.BOARD_SIZE; i++) {
      for (int j = 0; j < GameImpl.BOARD_SIZE; j++) {
        squares[i][j] = new JButton();
        squares[i][j].setText(i + "," + j);
        int row = i;
        int col = j;
        squares[row][col].addActionListener(e -> {
          game.placeTile(row, col);
        });
        squares[row][col].setVisible(false);
        pane.add(squares[i][j]);
      }
    }
    Container parent = squares[GameImpl.BOARD_SIZE/2][GameImpl.BOARD_SIZE/2].getParent();
    Rectangle r = new Rectangle(90*144/2 - 500, 90*144/2 - 300, 100, 100);
    System.out.println(r.toString());
    pane.scrollRectToVisible(r);

    return scrollPane;
  }

  @Override
  public void curTileChanged(Tile tile) {
    // re-render the currTile
    currTile.setIcon(getImageIcon(tile));
  }

  // render tile image to gui
  @Override
  public void tileChanged(int row, int col) {
    // add tile image to button, then to panel
    JButton button = squares[row][col];
    button.setPreferredSize(new Dimension(90, 90));
    ImageIcon icon = getImageIcon(game.getCurrentTile());
    button.setIcon(icon);
    button.setVisible(true);
    squares[row - 1][col].setVisible(true);
    squares[row + 1][col].setVisible(true);
    squares[row][col - 1].setVisible(true);
    squares[row][col + 1].setVisible(true);
  }

  private ImageIcon getImageIcon(Tile t) {
    BufferedImage image = null;
    try {
      image = ImageIO.read(new File(IMAGE_PATH));
    } catch (IOException e) {
      System.out.println("wrong image path");
      e.printStackTrace();
    }

    // calculate subimage location
    int[] xy = getSubImageLocation(t);

    assert image != null;
    BufferedImage tileImage = image.getSubimage(xy[0], xy[1], 90, 90);
    // get rotated image
    tileImage = rotateClockwise(tileImage, t.getRotattionTimes());
    // get meepled image
//    tileImage = withCircle(tileImage, Color.BLUE, 45, 0, 15);

    // add tile image to button, then to panel
    return new ImageIcon(tileImage);
  }

  /**
   * rotate clockwise
   * @param src BufferedImage
   * @param n rotate 90 degree n times
   * @return BufferedImage
   */
  public static BufferedImage rotateClockwise(BufferedImage src, int n) {
    int weight = src.getWidth();
    int height = src.getHeight();

    AffineTransform at = AffineTransform.getQuadrantRotateInstance(n, weight / 2.0, height / 2.0);
    AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);

    BufferedImage dest = new BufferedImage(weight, height, src.getType());
    op.filter(src, dest);
    return dest;
  }

  private int[] getSubImageLocation(Tile t) {
    int[] result = new int[] {0, 0};
    int chr = t.getName().toString().charAt(0);
    result[0] = 90 * ((chr - 65) % 6);
    result[1] = 90 * ((chr - 65) / 6);
    return result;
  }

  @Override
  public void currentPlayerChanged(Player player) {
    currentPlayerLabel.setText("Current player: " + player);
  }

  @Override
  public void scoreChanged(List<Player> players) {
    // TODO: show score changed status
  }

  @Override
  public void gameEnded(Player winner) {
    // TODO: show game ended status with who is the winner
    JFrame frame = (JFrame) SwingUtilities.getRoot(this);

    if (winner != null) {
      showDialog(frame, "Winner!", winner + " just won the game! with score " + winner.getScore());
    } else {
      showDialog(frame, "Stalemate", "The game has ended in a stalemate.");
    }

    // Append the 'start new game' command to the end of the
    // EventQueue. This is necessary because we need to wait
    // for all of the buttons to finish dispatching before
    // we reset the game's state. (If you are confused about
    // this, try calling 'game.startNewGame()' without the
    // 'invokeLater' and see what happens).
    SwingUtilities.invokeLater(game::startGame);
  }

  private static void showDialog(Component component, String title, String message) {
    JOptionPane.showMessageDialog(component, message, title, JOptionPane.INFORMATION_MESSAGE);
  }

  @Override
  public void meepleChanged(int x, int y, Direction dir) {
    Tile t = game.getTile(x, y);
    // add tile image to button, then to panel
    JButton button = squares[x][y];
    button.setPreferredSize(new Dimension(90, 90));

    BufferedImage image = null;
    try {
      image = ImageIO.read(new File(IMAGE_PATH));
    } catch (IOException e) {
      System.out.println("wrong image path");
      e.printStackTrace();
    }

    // calculate subimage location
    int[] xy = getSubImageLocation(t);

    assert image != null;
    BufferedImage tileImage = image.getSubimage(xy[0], xy[1], 90, 90);
    // get rotated image
    tileImage = rotateClockwise(tileImage, t.getRotattionTimes());
    // get meepled image
    // color
    // position
    int[] pos = getPos(dir);
    tileImage = withCircle(tileImage, Color.BLUE, pos[0], pos[1], 10);

    ImageIcon icon = new ImageIcon(tileImage);
    button.setIcon(icon);
    button.setVisible(true);
  }

  private int[] getPos(Direction dir) {
    if (dir.equals(Direction.North)) {
      return new int[] {45, 10};
    } else if (dir.equals(Direction.East)) {
      return new int[] {80, 45};
    } else if (dir.equals(Direction.South)) {
      return new int[] {45, 80};
    } else if (dir.equals(Direction.West)) {
      return new int[] {10, 45};
    } else {
      return new int[] {45, 45};
    }
  }

  /**
   * blue dot representation as meeple
   * @param src
   * @param color
   * @param x
   * @param y
   * @param radius
   * @return
   */
  public static BufferedImage withCircle(BufferedImage src, Color color, int x, int y, int radius) {
    BufferedImage dest = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());

    Graphics2D g = (Graphics2D) dest.getGraphics();
    g.drawImage(src, 0, 0, null);
    g.setColor(color);
    g.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
    g.dispose();

    return dest;
  }
}
