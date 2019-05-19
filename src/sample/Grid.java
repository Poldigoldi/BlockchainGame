package sample;
import java.util.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;


public class Grid {


  /*
      This class enables to create a level of the game using an Array od String
      A Grid object will be created when we initialise the game Map

      # Elements #
      " " -> nothing
      1,2,3 -> platform
      4 -> Block
      5 -> dynamic platform RED
      6 -> dynamic platform ORANGE
      7 -> interactive button ORANGE
      8 -> interactive button RED
      MV -> Moving Platform
      W -> Weapon
      I -> MiniGame Block
      B -> Big Door
      T -> Terminal
      D -> disappearing Platform
      R -> attackbot + solid block

      # Constructor #
      reads the array and creates Node object for every platform found, and store them in an ArrayList

  */

  private Frame platformleft = new Frame("/graphics/platformleft.png");
  private Frame platformright = new Frame("/graphics/platformright.png");
  private Frame platformmiddle = new Frame("/graphics/platformmiddle.png");
  private Frame block = new Frame("/graphics/block.png");

  private ArrayList<Platform> platforms = new ArrayList<>();
  private ArrayList<Shape> outlines = new ArrayList<>();
  private final int OBJ_WIDTH = 64;
  int width;
  int height;
  String[] map;

    private String[] map1 = new String[]{
            "4                     4",
            "4                     4",
            "4                    B4",
            "4          MV      4444",
            "4    123              4",
            "4 I    MV             4",
            "44444           DDD  T4",
            "4                  4444",
            "44     44444          4",
            "4    44          55   4",
            "4  66                 4",
            "4  4      7 8   4444444",
            "4       123123  R     4",
            "44              4    84",
            "4  W  66        4   744",
            "4               4555444",
            "4  DDD                4",
            "4            MV       4",
            "4   123  MV           4",
            "4            H        4",
            "44444444444444444444444",
    };

  private String[] map2 = new String[]{
          "44444444444444444444444",
          "4                     4",
          "4                     4",
          "4                     4",
          "4                     4",
          "4                     4",
          "4                     4",
          "4                     4",
          "4                     4",
          "4                     4",
          "4                     4",
          "44444444444444444444444"
  };

  private String[] map3 = new String[]{
          "4                    4",
          "4                    4",
          "4                    4",
          "4                    4",
          "4                    4",
          "4                    4",
          "4                    4",
          "4                    4",
          "4                    4",
          "4                    4",
          "4                    4",
          "4                    4",
          "4                    4",
          "4444444444444444444444"
  };


  /* Constructor */
  Grid(int level) {
    map = map1;
    if (level == 2) map = map2;
    width = map[0].length();
    height = map.length;
    for (int y = 0; y < height; y++) {
      int x = 0;
      for (char value : map[y].toCharArray()) {
        if (value == '4' || value == 'R') {
          Platform platform = new Platform(Type.SOLID, block);
          platform.setCollisionBox(x * OBJ_WIDTH, y * 64, OBJ_WIDTH, 64, Color.TRANSPARENT);
          platforms.add(platform);
        }
        if (value == '1' || value == '2' || value == '3') {
          Platform platform = new Platform(Type.PLATFORM, platformleft);
          if (value == '2') platform = new Platform(Type.PLATFORM, platformmiddle);
          if (value == '3') platform = new Platform(Type.PLATFORM, platformright);
          platform.setCollisionBox(x * OBJ_WIDTH, y * 64, OBJ_WIDTH, 10, Color.GREEN);
          platforms.add(platform);
        }
        if (value == '5') {
          Platform platform = new Platform(Type.PLATFORM, "orange");
          platform.setDisappear(true);
          platform.setAlive(false);
          platform.setCollisionValues(x * OBJ_WIDTH, y * 64, OBJ_WIDTH, 10);
          platform.setCollisionBox(0, 0, 0, 0, Color.ORANGE);
          Rectangle outline = new Rectangle(OBJ_WIDTH, 10, Color.ORANGE);
          outline.opacityProperty().setValue(0.25);
          outline.setX(x * OBJ_WIDTH);
          outline.setY(y * 64);
          outlines.add(outline);
          platforms.add(platform);
        }
        if (value == '6') {
          Platform platform = new Platform(Type.PLATFORM, "red");
          platform.setDisappear(true);
          platform.setAlive(false);
          platform.setCollisionValues(x * OBJ_WIDTH, y * 64, OBJ_WIDTH, 10);
          platform.setCollisionBox(0, 0, 0, 0, Color.RED);
          Rectangle outline = new Rectangle(OBJ_WIDTH, 10, Color.RED);
          outline.opacityProperty().setValue(0.25);
          outline.setX(x * OBJ_WIDTH);
          outline.setY(y * 64);
          outlines.add(outline);
          platforms.add(platform);
        }
        if (value == 'M') {
          Platform platform = new Platform(Type.PLATFORM, 400, platformleft);
          platform.setCollisionBox(x * OBJ_WIDTH, y * 64, OBJ_WIDTH, 10, Color.GREEN);
          platforms.add(platform);
        }
        if (value == 'V') {
          Platform platform = new Platform(Type.PLATFORM, 400, platformright);
          platform.setCollisionBox(x * OBJ_WIDTH, y * 64, OBJ_WIDTH, 10, Color.GREEN);
          platforms.add(platform);
        }
        if (value == 'D') {
          Platform platform = new Platform(Type.PLATFORM, true);
          platform.setAlive(true);
          platform.setCollisionBox(x * OBJ_WIDTH, y * 64, OBJ_WIDTH, 10, Color.GREEN);
          platform.setCollisionValues(x * OBJ_WIDTH, y * 64, OBJ_WIDTH, 10);
          platforms.add(platform);
        }
        x++;
      }
    }
  }

  /* ------ Getters ------- */

  public String[] map() {
    return map;
  }

  int height() {
    return height * OBJ_WIDTH;
  }

  int width() {
    return width * OBJ_WIDTH;
  }

  int getOBJ_WIDTH() {
    return OBJ_WIDTH;
  }

  ArrayList<Platform> platforms() {
    return this.platforms;
  }

  ArrayList<Shape> outlines() {
    return this.outlines;
  }
}

