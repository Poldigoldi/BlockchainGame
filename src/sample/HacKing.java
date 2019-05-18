package sample;

import javafx.scene.paint.Color;

/** This class defines the features of the Big Boss - aka -'HacKing'
 *  The way he works:
 *
 *  He moves like a basic Enemy
 *  1st attack: sends a wave of enemy
 *  2nd attack: send missiles
 *
 */

public class HacKing extends Enemy {

    private final int MAX_LIVES = 2;
    private final int TIME_BETWEEN_ATTACK = 60 * 2;
    private int counter;
    private int attack_mode; // 1 for enemy wave - 2 for missiles

    // Constructor
    public HacKing (int startx, int starty) {
        super (startx, starty, false,  500, 50);
        this.setCollisionBox(startx, starty , 60, 60, Color.INDIANRED);
        setLives (MAX_LIVES);
        this.attack_mode = 2;
        this.counter = 0;
    }


    public int getAttackMode() {
        return attack_mode;
    }

    public void nextAttackMode () {
        this.attack_mode = this.attack_mode % 2 + 1;
    }

    public boolean isCanAttack () {
        counter++;
        if (counter == TIME_BETWEEN_ATTACK) {
            counter = 0;
            return true;
        }
        return false;
    }
}
