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
      MV -> Moving Platform

      # Constructor #
      reads the array and creates Node object for every platform found, and store them in an ArrayList

  */

  private Frame platformleft = new Frame("/graphics/platformleft.png");
  private Frame platformright = new Frame("/graphics/platformright.png");
  private Frame platformmiddle = new Frame("/graphics/platformmiddle.png");
  private Frame block = new Frame("/graphics/block.png");

  private Frame buttonUp = new Frame("/graphics/buttonUp.png");
  private Frame buttonUpRed = new Frame("/graphics/buttonUpRed.png");
  private Frame buttonDown = new Frame("/graphics/buttonDown.png");

  private ArrayList<Platform> platforms = new ArrayList<> ();
  private ArrayList<PlatformButton> buttons = new ArrayList<>();
  private ArrayList<Shape> outlines = new ArrayList<>();
  private final int OBJ_WIDTH = 64;
  int width;
  int height;
  String [] map;

  private String[] map1 = new String[]{
          "4000000000000000000004",
          "4000000000000000000004",
          "4000000006660000800004",
          "4000000000000001223004",
          "4070000012300000000004",
          "444440000000005550004",
          "4000013000001300800004",
          "4000000001300001223004",
          "4000000000001300000004",
          "4444444400400000130004",
          "4000000066600000000004",
          "400004400000040#004444",
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
  Grid(int level) {
    map = map1;
    if(level == 2) map = map2;
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
        if (value == 'M' || value == 'V') {
          Platform platform = new Platform(Type.PLATFORM, 400, x, y, OBJ_WIDTH, platformleft);
          if (value == 'V') platform = new Platform(Type.PLATFORM, 400, x+OBJ_WIDTH, y, OBJ_WIDTH, platformright);
          platforms.add(platform);
        }
        if (value == '5'){
         Platform platform = new Platform(Type.PLATFORM,"orange");
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
        if (value == '6') {
          Platform platform = new Platform(Type.PLATFORM, "red", platformright);
          platform.setCollisionBox(x * OBJ_WIDTH, y * 64, OBJ_WIDTH, 10, Color.RED);
          platform.setDisappear(true);
          platform.setCollisionValues(x * OBJ_WIDTH, y * 64, OBJ_WIDTH, 10);
          Rectangle outline = new Rectangle(OBJ_WIDTH, 10, Color.RED);
          outline.opacityProperty().setValue(0.25);
          outline.setX(x * OBJ_WIDTH);
          outline.setY(y * 64);
          outlines.add(outline);
          platforms.add(platform);
        }
          if (value == '7') {
            PlatformButton button = new PlatformButton(Type.PLATFORMBUTTON, "orange", buttonUp, buttonDown);
            button.setCollisionBox(x * 64 + 11, y * 64 + 40, 40, 5, Color.RED);
            buttons.add(button);
          }
          if (value == '8') {
            PlatformButton button = new PlatformButton(Type.PLATFORMBUTTON, "red", buttonUpRed, buttonDown);
            button.setCollisionBox(x * 64 + 11, y * 64 + 40, 40, 5, Color.RED);
            buttons.add(button);
          }
        x++;
      }
    }
  }

   /* ------ Getters ------- */

  public String[] map(){return map;}

  int height(){
    return height*OBJ_WIDTH;
  }

  int width(){
    return width*OBJ_WIDTH;
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

  ArrayList<PlatformButton> buttons() {
    return this.buttons;
  }

}
