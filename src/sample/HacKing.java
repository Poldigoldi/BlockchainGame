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


    /* Motion variables */
    private int counter_moves;
    private int directionIndex; // 1 (up), 2 (down), 3 (right), 4 (left)

    private final int LIMIT_MOVES = 300;
    private final int SPEED_MOVE = 1;

    /* Defense variables */
    private int counter_shield;
    private boolean shieldState;
    private int counter_heal;
    private final int TIME_HEALING = 90;

    /* Attack variables */
    private final int ATTACK_BULLETS_AMOUNT = 4;
    private final int TIME_BETWEEN_ATTACK = 60 * 2;
    private final int TIME_BETWEEN_BULLETS = 10;
    private int counter;
    private int attack_mode; // 1 for enemy wave - 2 for missiles
    private int attack_bullet_count;

    /* Constructor */
    public HacKing (int startx, int starty) {
        super (Type.KING, 1);
        this.setCollisionBox(startx, starty , 60, 60, Color.INDIANRED);
        sprite.loadDefaultImages (new Frame("/graphics/enemy1.png"));
        this.attack_mode = 2;
        this.directionIndex = 1;
        this.shieldState = true;

        // initialize counters
        this.counter = 0;
        this.attack_bullet_count = 0;
        this.counter_heal = 0;
        this.counter_moves = 0;
        this.counter_shield = 0;

    }

    /* ------------------------- MOTION ------------------------- */

    public void move (Map map, double playerX, double playerY) {

        // TODO: check if Hacking too far from center, move back toward center
        //  - enhance his attack method to make it hard to beat
        //  - needs a special attack
        //  - allow him to regenerate life
        //

        /** This method allow the king to moves automatically around
         *
         * @logic:
         *
         *  *  Follow a cyclic pattern, goes up/down/left/right
         *  *
         *
         */
        if (isGoingOutOfBound (map.getLevel ().height(), map.getLevel ().width)) {
            directionIndex = newRandom (directionIndex);
        }
        listenerTooFarFromPlayer (playerX, playerY);

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
            directionIndex = newRandom (directionIndex);
        }
    }

    // generate a new random number different from the current given
    private int newRandom (int current) {
        int next;
        do {
            next = randomNumber (4);
        } while (next == current);
        return next;
    }

    private int randomNumber (int limit) {
        // between 1 and limit
        return new Random ().nextInt(limit) + 1;
    }

    // Returns true, if player gets too close from map bounds
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

    // If player too far from King, force king to move closer
    private void listenerTooFarFromPlayer(double x, double y) {
        if (Math.abs (getX () - x) > 500) {
            if (getX () > x) { directionIndex = 4; }
            else { directionIndex = 3; }
        }
        if (Math.abs (getY () - y) > 500) {
            if (getY () > y) { directionIndex = 1; }
            else { directionIndex = 2; }
        }
    }

    /* ------------------------- DEFENSE ------------------------ */

    void listenerDefense () {
        System.out.println (getLives ());
        heal ();
        shield ();
    }


    // If King life not full, will start a counter to gain one life
    private void heal () {
        if (! isLifeMax ()) {
            counter_heal++;
        }
        if (counter_heal == TIME_HEALING) {
            counter_heal = 0;
            winOneLive ();
        }
    }

    // If life very low, create a temporary shield around him
    // condition:
    // - can use the shield: life related but also time! otherwise impossible to kill him
    //
    private void shield() {
        if (! isLifeLow ()) {
            return;
        }
        if (shieldState) {
            // do something
        }
        nextShieldState ();
    }

    private void nextShieldState () {
        counter_shield++;
        // shield is ON
        if (shieldState && counter_shield == 120) {
            System.out.println ("> deactivating shield");
            shieldState = false;
            counter_shield = 0;
        }
        // shield is OFF
        if (!shieldState && counter_shield == 200) {
            System.out.println ("> activating shield");
            shieldState = true;
            counter_shield = 0;
        }
    }

    private boolean isLifeMax () {
        return this.getLives () == 10;
    }

    private boolean isLifeLow () {
        return this.getLives () < 3;
    }


    /* ------------------------- ATTACKS ------------------------ */


    public int getAttackMode() {
        return attack_mode;
    }

    public void nextAttackMode () {
        if (this.attack_mode > 1 && attack_bullet_count < ATTACK_BULLETS_AMOUNT) {
            attack_bullet_count++;
            return;
        }
        attack_bullet_count = 0;
        this.attack_mode = this.attack_mode % 3 + 1;
    }

    public boolean isCanAttack () {
        counter++;

        if (attack_mode == 1 && counter == TIME_BETWEEN_ATTACK) {
            counter = 0;
            return true;
        }
        if (attack_mode > 1
                && counter >= TIME_BETWEEN_ATTACK
                && (counter-TIME_BETWEEN_ATTACK) % TIME_BETWEEN_BULLETS == 0) {
            if (counter == TIME_BETWEEN_ATTACK + (ATTACK_BULLETS_AMOUNT) * TIME_BETWEEN_BULLETS) {
                counter = 0;
            }
            return true;
        }
        return false;
    }
}
