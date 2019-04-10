package sample;
import java.util.*;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;


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
  private final int platformHeight = 10;
  private final int platformWidth = 64;

  private Image platformleft = new Image("/platformleft.png");
  private ImagePattern platformleftImage = new ImagePattern(platformleft);
  private Image platformright = new Image("/platformright.png");
  private ImagePattern platformrightImage = new ImagePattern(platformright);
  private Image platformmiddle = new Image("/platformmiddle.png");
  private ImagePattern platformmiddleImage = new ImagePattern(platformmiddle);
  private Image block = new Image("/block.png");
  private ImagePattern blockImage = new ImagePattern(block);

  private ArrayList<Node> platforms = new ArrayList<> ();
  int width;
  int height;
  private String[] map = new String[]{
          "4000000000000000000004",
          "4000000000000000000004",
          "4000000000000000000004",
          "4000000000000000000004",
          "4000000001230000000004",
          "4230000000000000000004",
          "4000013000000000000004",
          "4000000001300000000004",
          "4000000000000122230004",
          "4000000001300000000004",
          "4444444444444000000004",
          "4444444444444000004444"
  };

  /* Constructor */
  Grid() {
    width = map[0].length();
    height = map.length;
    for (int i = 0; i < height; i++) {
      int j = 0;
      for (char value : map[i].toCharArray()) {
        if(value!= '0'){
          Node platform = createRectangle(i, j, platformmiddleImage, platformWidth, platformHeight);
          if (value == '1') platform = createRectangle(i, j, platformleftImage, platformWidth, platformHeight);
          if (value == '3') platform = createRectangle(i, j, platformrightImage, platformWidth, platformHeight);
          if (value == '4') platform = createRectangle(i, j, blockImage, 64, 64);
          platforms.add (platform);
        }
        j++;
      }
    }
  }

  Node createRectangle(int i, int j, ImagePattern fill, int width, int height) {
    Rectangle platform = new Rectangle(width, height);
    platform.setTranslateX(j * 64);
    platform.setTranslateY(i * 64);
    platform.setFill(fill);
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
    return this.platformHeight;
  }
}
