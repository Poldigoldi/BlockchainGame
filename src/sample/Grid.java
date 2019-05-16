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
      0 -> nothing
      1,2,3,4 -> platform
      5 -> dynamic platform
      6 -> interactive button

      # Constructor #
      reads the array and creates Node object for every platform found, and store them in an ArrayList

  */

  private Frame platformleft = new Frame("/graphics/platformleft.png");
  private Frame platformright = new Frame("/graphics/platformright.png");
  private Frame platformmiddle = new Frame("/graphics/platformmiddle.png");
  private Frame block = new Frame("/graphics/block.png");

  private ArrayList<Platform> platforms = new ArrayList<> ();
  private ArrayList<PlatformButton> buttons = new ArrayList<>();
  private ArrayList<Shape> outlines = new ArrayList<>();
  private final int OBJ_WIDTH = 64;
  int width;
  int height;
  private String[] map1 = new String[]{
          "4000000000000000000004",
          "4000000000000000000004",
          "4000000000000000000004",
          "4000000000000001223004",
          "4060000012300000000004",
          "4222300000000005550004",
          "4000013000001300000004",
          "4000000001300001223004",
          "4000000000001300000004",
          "4444444400400000004004",
          "4000000044400000000004",
          "4000044000000400004444",
          "4000444400040044000004",
          "4444444444400000444000"
  };

  private String[] map2 = new String[]{
          "4000000000000000000004",
          "4000000000000000000004",
          "4000000000000000000004",
          "4000000000000000000004",
          "4000000000000000000004",
          "4000000000000000000004",
          "4000000000000000000004",
          "4000000000000000000004",
          "4000000000000000000004",
          "4000000000000000000004",
          "4000000000000000000004",
          "4000000000000000000004",
          "4000000000000000000004",
          "4444444444444444444444"
  };


  /* Constructor */
  Grid(int leveNumber) {
    String[] map = map1;
    if (leveNumber == 2) {
      map = map2;
    }
    width = map[0].length();
    height = map.length;
    for (int y = 0; y < height; y++) {
      int x = 0;
      for (char value : map[y].toCharArray()) {
        if (value == '4') {
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
        if (value == '5'){
         Platform platform = new Platform(Type.PLATFORM, platformright);
          platform.setCollisionBox(x * OBJ_WIDTH, y * 64, OBJ_WIDTH, 10, Color.DARKORANGE);
          platform.setDisappear(true);
          platform.setCollisionValues(x * OBJ_WIDTH, y * 64, OBJ_WIDTH, 10);
          Rectangle outline = new Rectangle(OBJ_WIDTH, 10, Color.DARKORANGE);
          outline.opacityProperty().setValue(0.25);
          outline.setX(x * OBJ_WIDTH);
          outline.setY(y * 64);
          outlines.add(outline);
          platforms.add(platform);
        }
        if (value == '6'){
          PlatformButton button = new PlatformButton(Type.PLATFORMBUTTON);
          button.setCollisionBox(x * 64 + 11, y * 64 + 40,40, 5, Color.RED);
          buttons.add(button);
        }
        x++;
      }
    }
  }

   /* ------ Getters ------- */

  int height(){
    return height*64;
  }

  int width(){
    return width*OBJ_WIDTH;
  }

  public int getOBJ_WIDTH() {
    return OBJ_WIDTH;
  }

  ArrayList<Platform> platforms() {
    return this.platforms;
  }

  ArrayList<Shape> outlines() {
    return this.outlines;
  }

  ArrayList<PlatformButton> buttons() {
    return this.buttons;
  }

}
