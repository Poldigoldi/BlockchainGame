package sample;
import java.util.*;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class Grid {

  /*
      This class enables to create a level of the game using an Array od String
      A Grid object will be created when we initialise the game Map

      # Elements #
      0 -> nothing
      1 -> platform

      # Constructor #
      reads the array and creates Node object for every platform found, and store them in an ArrayList

  */
  private final int PlatformHeight = 10;
  private final int PlatfromWidth = 60;

  private ArrayList<Node> platforms = new ArrayList<> ();
  int width;
  int height;
  private String[] map = new String[]{
          "0000000000000000000000",
          "0000000000000000000000",
          "0000000000000000000000",
          "0000000000000000000000",
          "0000000001110000000000",
          "1110000000000000000000",
          "0000011000000000000000",
          "0000000001100000000000",
          "0000000000000111000000",
          "0000000001100000000000",
          "0000011100000000000000",
          "1111100000000000000000"
  };

  /* Constructor */
  Grid() {
    width = map[0].length();
    height = map.length;
    for (int i = 0; i < height; i++) {
      int j = 0;
      for (char value : map[i].toCharArray()) {
        if (value == '1')  {
          Node platform = createRectangle(i, j, Color.GHOSTWHITE);
          platforms.add (platform);
        }
        j++;
      }
    }
  }

  Node createRectangle(int i, int j, Color colour) {
    Rectangle platform = new Rectangle(PlatfromWidth, PlatformHeight);
    platform.setTranslateX(j * 60);
    platform.setTranslateY(i * 60);
    platform.setFill(colour);
    return platform;
  }

   /* ------ Getters ------- */

  int getHeight(){
    return height;
  }

  int getWidth(){
    return width;
  }

  ArrayList<Node> getPlatforms () {
    return this.platforms;
  }

  int getPlatformHeight () {
    return this.PlatformHeight;
  }
}
