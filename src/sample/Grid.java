package sample;
import java.util.*;
import javafx.scene.paint.Color;


public class Grid {

  /*
      This class enables to create a level of the game using an Array od String
      A Grid object will be created when we initialise the game Map

      # Elements #
      0 -> nothing
      1,2,3,4 -> platform

      # Constructor #
      reads the array and creates Node object for every platform found, and store them in an ArrayList

  */

  private Frame platformleft = new Frame("/graphics/platformleft.png");
  private Frame platformright = new Frame("/graphics/platformright.png");
  private Frame platformmiddle = new Frame("/graphics/platformmiddle.png");
  private Frame block = new Frame("/graphics/block.png");

  private ArrayList<Object> platforms = new ArrayList<> ();
  private final int OBJ_WIDTH = 64;
  int width;
  int height;
  private String[] map = new String[]{
          "4000000000000000000004",
          "4000000000000000000004",
          "4000000000000000000004",
          "4000000000000001223004",
          "4000000012300000000004",
          "4222300000000001300004",
          "4000013000001300000004",
          "4000000001300001223004",
          "4000000000001300000004",
          "4444444400400000004004",
          "4000000044400000000004",
          "4000044000000400004444",
          "4000444400040044000004",
          "4444444444400000444000"
  };

  /* Constructor */
  Grid() {
    width = map[0].length();
    height = map.length;
    for (int y = 0; y < height; y++) {
      int x = 0;
      for (char value : map[y].toCharArray()) {
          if(value == '4') {
            Object platform = new Object(  Type.SOLID, block);
            platform.setCollisionBox(x*OBJ_WIDTH, y*64,OBJ_WIDTH, 64, Color.TRANSPARENT);
            platforms.add(platform);
          }
          if(value == '1' || value == '2' || value == '3'){
            Object platform = new Object(  Type.PLATFORM, platformleft);
            if (value == '2') platform = new Object(  Type.PLATFORM, platformmiddle);
            if (value == '3') platform = new Object(  Type.PLATFORM, platformright);
            platform.setCollisionBox(x*OBJ_WIDTH, y*64,OBJ_WIDTH, 10, Color.GREEN);
            platforms.add(platform);
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

  ArrayList<Object> platforms() {
    return this.platforms;
  }

}
