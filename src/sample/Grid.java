package sample;
import java.util.*;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;


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

  private Image platformleft = new Image("/platformleft.png");
  private Image platformright = new Image("/platformright.png");
  private Image platformmiddle = new Image("/platformmiddle.png");
  private Image block = new Image("/block.png");

  private ArrayList<Object> platforms = new ArrayList<> ();
  int width;
  int height;
  private String[] map = new String[]{
          "4000000000000000000004",
          "4000000000000000000004",
          "4000000000000000000004",
          "4000000000000000000004",
          "4000000001230000000004",
          "4230000000000440000004",
          "4000013000000000000004",
          "4000000001300000000004",
          "4000000000000122230004",
          "4000040001300000000004",
          "4444444444444000000004",
          "4444444444444000004444"
  };

  /* Constructor */
  Grid() {
    width = map[0].length();
    height = map.length;
    for (int y = 0; y < height; y++) {
      int x = 0;
      for (char value : map[y].toCharArray()) {
          if(value == '4') {
            Object platform = new Object(x*64, y*64,  false,  Type.SOLID, block);
            platform.setCollisionBox(64, 64, Color.RED);
            platforms.add(platform);
          }
          if(value == '1' || value == '2' || value == '3'){
            Object platform = new Object(x*64, y*64, false,  Type.PLATFORM, platformleft);
            if (value == '2') platform = new Object(x*64, y*64, false, Type.PLATFORM, platformmiddle);
            if (value == '3') platform = new Object(x*64, y*64,  false,  Type.PLATFORM, platformright);
            platform.setCollisionBox(64, 10, Color.GREEN);
            platforms.add(platform);
        }
        x++;
      }
    }
  }

   /* ------ Getters ------- */

  int getHeight(){
    return height;
  }

  int getWidth(){
    return width;
  }

  ArrayList<Object> platforms() {
    return this.platforms;
  }

}
