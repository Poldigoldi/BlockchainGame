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
      " "     == nothing
      <    -    >   == platform left, middle, right
      .       == grass
      0       == Dirt/grass
      1       == dirt/Grass meeting Block
      2       == Viny block
      3       == mildly viny block
      4       == Block
      5       == Block variant, panel fall off and small tree
      6       == Block variant, flower
      7       == Block variant, panel and vines
      8       == Block variant, heavily cracked
      !, £ -> tree variants of Block
      [   @   ]       == left / middle /right mixed grass and metal blocks

      R -> dynamic platform RED
      r -> interactive button RED
      O -> dynamic platform ORANGE
      o -> interactive button ORANGE

      MV -> Moving Platform

      W -> Weapon
      I -> MiniGame Block
      B -> Big Door
      T -> Terminal
      D -> disappearing Platform
      # -> attackbot + solid block
      H -> LIFE collectible

      # Constructor #
      reads the array and creates Node object for every platform found, and store them in an ArrayList

                if(level.map()[y].charAt(x) == 'T') addTerminal(x, y);
                if(level.map()[y].charAt(x) == 'B') addBigDoor(x, y);
                if(level.map()[y].charAt(x) == 'I') addBlock(x, y);
                if(level.map()[y].charAt(x) == 'W') addWeapon(x, y);
                if(level.map()[y].charAt(x) == '7') addButton(x, y, '7');
                if(level.map()[y].charAt(x) == '8') addButton(x, y, '8');
                if(level.map()[y].charAt(x) == 'H') addLife(x, y);
                if(level.map()[y].charAt(x) == 'R') addAttackBot(x, y);
                if(level.map()[y].charAt(x) == '.') addGrass(x, y);
  */

  private Frame platformleft = new Frame("/graphics/platformleft.png");
  private Frame platformright = new Frame("/graphics/platformright.png");
  private Frame platformmiddle = new Frame("/graphics/platformmiddle.png");
  private Frame block = new Frame("/graphics/block.png");

  private ArrayList<Object> bringtofront = new ArrayList<> ();
  private ArrayList<Platform> platforms = new ArrayList<> ();
  private ArrayList<Shape> outlines = new ArrayList<>();
  private final int OBJ_WIDTH = 64;
  int width;
  int height;
  String [] map;

    private String[] map1 = new String[]{
            "4                     4",
            "4                     4",
            "4                    B4",
            "8          MV      4464",
            "4    <->              3",
            "4 I    MV             2",
            "44444           DDD  T4",
            "6      .....       444£",
            "44     [@@@]          3",
            "4    £!         DDD   2",
            "4  RR                 1",
            "5               [@@@@@@",
            "4       <---->  8     4",
            "£               4    r7",
            "4  W4 RR        4   o45",
            "3               3OOO33£",
            "!4 DDD                3",
            "2   ...      MV       2",
            "2   <->    #          7",
            "1.....     H   ......11",
            "00000000000000000000000",
    };

  private String[] map2 = new String[]{
          "444444#44444444444444444444444",
          "4                            4",
          "4.H..                    ...B4",
          "356!8   MV   DD          [@@]4",
          "4                  MV        4",
          "4         1#   H             3",
          "4     13      13      56  .  4",
          "4 W      ....             4#64",
          "4434     1223         RR     4",
          "4             .5    <>   D   4",
          "4   123  ....123..r       D  4",
          "4  D     4444000000        .H4",
          "444   1114444444444  D D D 664"
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
        //SOLID BLOCKS AND VARIANTS
        if (value == '4' || value == '#') {
          Platform platform = new Platform(Type.SOLID, block);
          platform.setCollisionBox(x * OBJ_WIDTH, y * 64, OBJ_WIDTH, 64, Color.TRANSPARENT);
          platforms.add(platform);
        }
        if (value == '0') {
          Platform platform = new Platform(Type.SOLID, new Frame("/graphics/dirt1.png"));
          platform.setCollisionBox(x * OBJ_WIDTH, y * 64, OBJ_WIDTH, 64, Color.TRANSPARENT);
          platforms.add(platform);
        }
        if (value == '1') {
          Platform platform = new Platform(Type.SOLID, new Frame("/graphics/block2.png"));
          platform.setCollisionBox(x * OBJ_WIDTH, y * 64, OBJ_WIDTH, 64, Color.TRANSPARENT);
          platform.sprite.setTranslateX(platform.getX()-3);
          bringtofront.add(platform);
          platforms.add(platform);
        }
        if (value == '2') {
          Platform platform = new Platform(Type.SOLID, new Frame("/graphics/block3.png"));
          platform.setCollisionBox(x * OBJ_WIDTH, y * 64, OBJ_WIDTH, 64, Color.TRANSPARENT);
          platform.sprite.setTranslateY(platform.getY()-4);
          platforms.add(platform);
        }
        if (value == '3') {
          Platform platform = new Platform(Type.SOLID, new Frame("/graphics/block4.png"));
          platform.setCollisionBox(x * OBJ_WIDTH, y * 64, OBJ_WIDTH, 64, Color.TRANSPARENT);
          platforms.add(platform);
        }
        if (value == '5') {
          Platform platform = new Platform(Type.SOLID, new Frame("/graphics/block5.png"));
          platform.setCollisionBox(x * OBJ_WIDTH, y * 64, OBJ_WIDTH, 64, Color.TRANSPARENT);
          platforms.add(platform);
        }
        if (value == '6') {
          Platform platform = new Platform(Type.SOLID, new Frame("/graphics/block6.png"));
          platform.setCollisionBox(x * OBJ_WIDTH, y * 64, OBJ_WIDTH, 64, Color.TRANSPARENT);
          platforms.add(platform);
        }
        if (value == '7') {
          Platform platform = new Platform(Type.SOLID, new Frame("/graphics/block7.png"));
          platform.setCollisionBox(x * OBJ_WIDTH, y * 64, OBJ_WIDTH, 64, Color.TRANSPARENT);
          platforms.add(platform);
        }
        if (value == '8') {
          Platform platform = new Platform(Type.SOLID, new Frame("/graphics/block8.png"));
          platform.setCollisionBox(x * OBJ_WIDTH, y * 64, OBJ_WIDTH, 64, Color.TRANSPARENT);
          platforms.add(platform);
        }
        //left Grass/Block
        if (value == '[') {
          Platform platform = new Platform(Type.SOLID, new Frame("/graphics/grassblock2.png"));
          platform.setCollisionBox(x * OBJ_WIDTH, y * 64, OBJ_WIDTH, 64, Color.TRANSPARENT);
          platform.sprite.setTranslateY(platform.getY()-11);
          platform.sprite.setTranslateX(platform.getX()-2);
          platforms.add(platform);
        }
        //middle Grass/Block
        if (value == '@') {
          Platform platform = new Platform(Type.SOLID, new Frame("/graphics/grassblock1.png"));
          platform.setCollisionBox(x * OBJ_WIDTH, y * 64, OBJ_WIDTH, 64, Color.TRANSPARENT);
          platform.sprite.setTranslateY(platform.getY()-12);
          platforms.add(platform);
        }
        //right Grass/Block
        if (value == ']') {
          Platform platform = new Platform(Type.SOLID, new Frame("/graphics/grassblock3.png"));
          platform.setCollisionBox(x * OBJ_WIDTH, y * 64, OBJ_WIDTH, 64, Color.TRANSPARENT);
          platform.sprite.setTranslateY(platform.getY()-11);
          platforms.add(platform);
        }
        if (value == '!') {
          Platform platform = new Platform(Type.SOLID, new Frame("/graphics/solidtree1.png"));
          platform.setCollisionBox(x * OBJ_WIDTH, y * 64, OBJ_WIDTH, 64, Color.TRANSPARENT);
          platform.sprite.setTranslateY(platform.getY()-20);
          platforms.add(platform);
          bringtofront.add(platform);
        }
        if (value == '£') {
          Platform platform = new Platform(Type.SOLID, new Frame("/graphics/solidtree2.png"));
          platform.setCollisionBox(x * OBJ_WIDTH, y * 64, OBJ_WIDTH, 64, Color.TRANSPARENT);
          platform.sprite.setTranslateY(platform.getY()-58);
          platforms.add(platform);
          bringtofront.add(platform);
        }




        //NON SOLID BLOCKS
        if (value == '<' || value == '-' || value == '>') {
          Platform platform = new Platform(Type.PLATFORM, platformleft);
          if (value == '-') platform = new Platform(Type.PLATFORM, platformmiddle);
          if (value == '>') platform = new Platform(Type.PLATFORM, platformright);
          platform.setCollisionBox(x * OBJ_WIDTH, y * 64, OBJ_WIDTH, 10, Color.GREEN);
          platforms.add(platform);
        }
        if (value == 'O') {
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
        if (value == 'R') {
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

  ArrayList<Object> bringtofront() {
    return bringtofront;
  }


}
