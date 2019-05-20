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
    private int SPEED_MOVE = 1;

    /* Defense variables */
    private int max_lives;
    private boolean shieldState;
    private int counter_shield;
    private int counter_heal;
    private final int MAX_LIFE_DECREMENT = 2;
    private final int TIME_HEALING = 90;

    /* Attack variables */
    private final int ATTACK_BULLETS_AMOUNT = 4;
    private final int TIME_BETWEEN_ATTACK = 60 * 2;
    private final int TIME_BETWEEN_BULLETS = 10;
    private int shooting_frequency = 2;
    private int counter;
    private int attack_mode; // 1 for enemy wave - 2/3 for missiles
    private int attack_bullet_count;

    /* Constructor */
    public HacKing (int startx, int starty) {
        super (Type.KING, 10);
        this.setCollisionBox(startx, starty , 80, 60, Color.INDIANRED);
        sprite.loadDefaultRightImages(
                new Frame("/graphics/hackkingright1.png"));
        sprite.loadDefaultLeftImages(
                new Frame("/graphics/hackkingleft1.png"));
                sprite.loadRightMotionImages(
                new Frame("/graphics/hackkingright1.png"),
                new Frame("/graphics/hackkingright2.png"),
                new Frame("/graphics/hackkingright3.png"),
                new Frame("/graphics/hackkingright4.png"),
                new Frame("/graphics/hackkingright5.png"));
        sprite.loadLeftMotionImages(
                new Frame("/graphics/hackkingleft1.png"),
                new Frame("/graphics/hackkingleft2.png"),
                new Frame("/graphics/hackkingleft3.png"),
                new Frame("/graphics/hackkingleft4.png"),
                new Frame("/graphics/hackkingleft5.png"));
        sprite.offset(-60, -80);
        this.attack_mode = 2;
        this.directionIndex = 1;
        this.shieldState = true;
        this.max_lives = 10;

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
        //  - needs a special attack

        /** This method allow the king to moves automatically around
         *
         * @logic:
         *
         *  *  Follow a random motion pattern
         *  *  Moves for a random amount in a same direction,
         *  *  Can either go: up/down/left/right
         *  *  If player too far, he will prioritise to get closer
         *  *  If gets too close from bounds, changes direction
         */
        increaseSpeed();
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

    private void increaseSpeed () {
        if (getLives () <= 4) SPEED_MOVE = 3;
        else  if (getLives () <= 10) SPEED_MOVE = 2;
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
        if (Math.abs (getY () - y) > 200) {
            if (getY () > y) { directionIndex = 1; }
            else { directionIndex = 2; }
        }
        if (Math.abs (getX () - x) > 200) {
            if (getX () > x) { directionIndex = 4; }
            else { directionIndex = 3; }
        }
    }

    /* ------------------------- DEFENSE ------------------------ */

    void listenerDefense () {
        heal ();
        shield ();
    }

    // If King life not full, will start a counter to gain one life
    private void heal () {
       // System.out.println (getLives ());
        if (! isLifeMax ()) {
            counter_heal++;
        }
        if (counter_heal == TIME_HEALING) {
            counter_heal = 0;
            winOneLive ();
            if (getLives () == max_lives && max_lives >= 2 * MAX_LIFE_DECREMENT) {
                max_lives -= MAX_LIFE_DECREMENT;
            }
        }
    }

    // If life very low, create a temporary shield around him
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
            shieldState = false;
            counter_shield = 0;
        }
        // shield is OFF
        if (!shieldState && counter_shield == 200) {
            shieldState = true;
            counter_shield = 0;
        }
    }

    private boolean isLifeMax () {
        return this.getLives () >= max_lives;
    }

    private boolean isLifeLow () {
        return this.getLives () <= MAX_LIFE_DECREMENT;
    }


    /* ------------------------- ATTACKS ------------------------ */

    private void increaseShootingFreq (){
        if (getLives () <= 4 ) shooting_frequency = 5;
        else if (getLives () <= 7) shooting_frequency = 4;
    }

    public int getAttackMode() {
        return attack_mode;
    }

    public void nextAttackMode () {
        increaseShootingFreq();
        if (this.attack_mode > 1 && attack_bullet_count < ATTACK_BULLETS_AMOUNT) {
            attack_bullet_count++;
            return;
        }
        attack_bullet_count = 0;
        this.attack_mode = this.attack_mode % (shooting_frequency + 1) + 1;
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
