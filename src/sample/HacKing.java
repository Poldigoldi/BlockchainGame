package sample;

import javafx.scene.paint.Color;

/** This class defines the features of the Big Boss - aka -'HacKing'
 *  The way he works:
 *
 *  He moves like a basic Enemy
 *
 *
 *
 */

public class HacKing extends Enemy {

    // Constructor
    public HacKing (int startx, int starty) {
        super (startx, starty, false,  500, 50);
        this.setCollisionBox(startx, starty , 60, 60, Color.INDIANRED);
    }


}
