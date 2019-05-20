package sample;

import javafx.scene.paint.Color;

import java.util.Random;

/** This class defines the features of the Big Boss - aka -'HacKing'
 *  The way he works:
 *
 * @motion:
 *
 *
 * @attack:
 *  1st attack: sends a wave of enemy
 *  2nd attack: send a serie of bullets
 *
 */

public class HacKing extends Person {


    // Motion variables
    private int counter_moves;
    private int directionIndex; // 1 (up), 2 (down), 3 (right), 4 (left)

    private final int LIMIT_MOVES = 300;
    private final int SPEED_MOVE = 1;

    // Attack variables
    private final int ATTACK_BULLETS_AMOUNT = 4;
    private final int TIME_BETWEEN_ATTACK = 60 * 4;
    private final int TIME_BETWEEN_BULLETS = 10;
    private int counter;
    private int attack_mode; // 1 for enemy wave - 2 for missiles
    private int attack_bullet_count;

    // Constructor
    public HacKing (int startx, int starty) {
        super (Type.KING, 6);
        this.setCollisionBox(startx, starty , 60, 60, Color.INDIANRED);
        sprite.loadDefaultImages (new Frame("/graphics/enemy1.png"));
        this.attack_mode = 2;
        this.counter = 0;
        this.attack_bullet_count = 0;
        this.counter_moves = 0;
        this.directionIndex = 1;

    }

    /* ------------------------- MOTION ------------------------- */

    public void move (Map map) {
        /** This method allow the king to moves automatically around
         *
         * @logic:
         *
         *  *  Follow a cyclic pattern, goes up/down/left/right
         *  *
         *
         */
        System.out.println (counter_moves);
        if (isGoingOutOfBound (map.getLevel ().height(), map.getLevel ().width)) {
            System.out.println ("Stopped from going outbound direction " + directionIndex );
            directionIndex = newRandom (directionIndex);
        }

        switch (directionIndex) {
            case 1: // Up
                move_Y (-SPEED_MOVE, map);
                break;
            case 2: // Down
                move_Y (SPEED_MOVE, map);
                break;
            case 3: // Right
                move_X (SPEED_MOVE, map);
                break;
            case 4: // Left
                move_X (-SPEED_MOVE, map);
                break;
        }
        nextDirection ();
    }

    private void nextDirection () {
        counter_moves++;
        if (counter_moves >= randomNumber (LIMIT_MOVES) + 100 ) {
            counter_moves = 0;
            System.out.println ("> old direction! " + directionIndex );
            directionIndex = newRandom (directionIndex);
            System.out.println ("new direction! " + directionIndex );
        }
    }

    private int newRandom (int current) {
        int next;
        do {
            next = randomNumber (4);
        } while (next == current);
        return next;
    }

    private int randomNumber (int limit) {
        // between 1 and 4
        return new Random ().nextInt(limit) + 1;
    }

    private boolean isGoingOutOfBound (int mapHeight, int mapWidth) {
        int OFFSET = 150;
        if (   (directionIndex == 1 && getY () <= OFFSET)
            || (directionIndex == 2 && getY () >= mapHeight - OFFSET)
            || (directionIndex == 3 && getX () >= mapWidth - OFFSET)
            || (directionIndex == 4 && getX () <= OFFSET) ) {
            return true;
        }
        return false;
    }

    /* ------------------------- ATTACKS ------------------------ */


    public int getAttackMode() {
        return attack_mode;
    }

    public void nextAttackMode () {
        if (this.attack_mode == 2 && attack_bullet_count < ATTACK_BULLETS_AMOUNT) {
            attack_bullet_count++;
            return;
        }
        attack_bullet_count = 0;
        this.attack_mode = this.attack_mode % 2 + 1;
    }

    public boolean isCanAttack () {
        counter++;

        switch (attack_mode) {
            case 1:
                if (counter == TIME_BETWEEN_ATTACK) {
                    counter = 0;
                    return true;
                }
            case 2:
                // keep shooting bullets before switching to enemy wave
                if (counter >= TIME_BETWEEN_ATTACK && (counter-TIME_BETWEEN_ATTACK) % TIME_BETWEEN_BULLETS == 0) {
                    if (counter == TIME_BETWEEN_ATTACK + (ATTACK_BULLETS_AMOUNT) * TIME_BETWEEN_BULLETS) {
                        counter = 0;
                    }
                    return true;
                }
        }
        return false;
    }
}
