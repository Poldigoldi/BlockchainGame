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

      E -> Enemy will spawn here

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

  private ArrayList<Object> bringtofront = new ArrayList<> ();
  private ArrayList<Platform> platforms = new ArrayList<> ();
  private ArrayList<Shape> outlines = new ArrayList<>();
  private final int OBJ_WIDTH = 64;
  int width;
  int height;
  String [] map;

  private String[] map1 = new String[]{
          "4                                            2",
          "4                                            3",
          "£                                            4",
          "4                                            4",
          "4    E   <->                                 7",
          "A   <-->    485                 65           4",
          "4                                   DDD      4",
          "4  W                            W       448!£4",
          "2        !      OOO     RRR    <->           4",
          "1.......11.. o       r            E     E   B4",
          "000000000000000     273     23432443£444444544",
  };

    private String[] map2 = new String[]{
            "7                     8",
            "4                     4",
            "4                     4",
            "8          MV        B4",
            "4    <->           4463",
            "4 I    MV             2",
            "44444           DDD  T4",
            "6      .....       222£",
            "44     [@@@]          3",
            "4    £!         DDD   2",
            "4  RR                 1",
            "5               [@@@@]1",
            "4       <---->  8     5",
            "£               #    r7",
            "2  W  RR        4   o45",
            "3               3OOO33£",
            "!8 DDD                3",
            "2   ...      MV       2",
            "2   <->           DD  7",
            "1.....     H   ......11",
            "00000000000000000000000",
    };

  private String[] map3 = new String[]{
          "4                            4",
          "4                            4",
          "4                            4",
          "4         56668              4",
          "4         4  H7              4",
          "1           675              £",
          "3          D         H       4",
          "2oH..     D        OOO   .H.B2",
          "2<-->   MV   MV          [@@]1",
          "1      D         MV          4",
          "28RR6     OOO  H             7",
          "5  O6 <>      <>          . W5",
          "6HRW6    ....      MV     4#6£",
          "3<-->    <-->   DD    RR     3",
          "2            ...    <->  D   2",
          "2  H<->  ....<->..r       D  7",
          "1r D     [@@@@@@@@]        .H1",
          "000    0000000000000 DD DD 000"
  };

  private String[] map4 = new String[]{
          "4         3          4",
          "4         2          4",
          "4         2          4",
          "4T        4         B4",
          "855 D     4D      D554",
          "3    D    3    D   D £",
          "2     OO  7  D     D #",
          "# o D     2      D   4",
          "4<->      2D         #",
          "4   D  RR #    D     4",
          "2       r 3D     D   4",
          "4  o   OR 3      D   2",
          "# ROO         D      4",
          "4......r........     4",
          "[@@@@@@@@@@@@@@]     4",
          "!                DD  #",
          "#          DDDDD     2",
          "4  D  D  D DDDDD    o4",
          "4D                  -4",
          "4  D      #          #",
          "4D                R  4",
          "#               R    4",
          "6   OO  D         R r4",
          "4          OO       -4",
          "5             MV     7",
          "#                MV  4",
          "4             MV     8",
          "4  DDDDDDDDDD        #",
          "4444                 4"
  };

  private String[] map5 = new String[]{
          "                       ",
          "                       ",
          "      DDD     DDD      ",
          "       4       4       ",
          "           4           ",
          "           4           ",
          "     4     4     4     ",
          "      4         4      ",
          "       444444444       ",
          "                       ",
          "44444444444444444444444",
  };

  /* Constructor */
  Grid(int level) {
    map = map1;
    if (level == 2) map = map2;
    else if (level == 3) map = map3;
    else if (level == 4) map = map4;
    else if (level == 5) map = map5;
    width = map[0].length();
    height = map.length;
    for (int y = 0; y < height; y++) {
      int x = 0;
      for (char value : map[y].toCharArray()) {
        //SOLID BLOCKS AND VARIANTS
        if (value == '4' || value == '#') {
          Frame block = new Frame("/graphics/block.png");
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
        Frame platformleft = new Frame("/graphics/platformleft.png");
        Frame platformright = new Frame("/graphics/platformright.png");
        if (value == '<' || value == '-' || value == '>') {
          Platform platform = new Platform(Type.PLATFORM, platformleft);
          Frame platformmiddle = new Frame("/graphics/platformmiddle.png");
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
