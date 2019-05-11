package sample;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class EnemyType1 extends Object {

    /*
    TODO: 1) allow enemy to attack (either rockets/contact)
          2) reduce collision box size
     */
    private boolean canMove;
    private int countMoveLeft;
    private int countMoveRight;
    private int limitMoves = 400;

    // Global variables
    private final int OFFSET = 5;
    private final int MOVE_SPEED = 1;
    private final double JUMP_SPEED_Y = 29;
    private final double JUMP_SPEED_X = 2.8;
    private final int JUMP_PROBA_RANGE;
    private final int MIN_MOVE = 200;
    private final int MAX_MOVES_AMPLITUDE;

    public EnemyType1(int startx, int starty, boolean canMove, int moveAmplitude, int jumpProbRange) {
        super(Type.ENEMY1);
        this.setCollisionBox(startx, starty , 38, 48, Color.INDIANRED);
        this.canMove = canMove;
        this.alive = true;
        this.countMoveLeft=0;
        this.countMoveRight=0;
        this.MAX_MOVES_AMPLITUDE = moveAmplitude;
        this.JUMP_PROBA_RANGE = jumpProbRange;

        sprite.loadDefaultLeftImages(new Frame("/graphics/enemy1.png"), new Frame("/graphics/enemy1.png"));
        sprite.loadDefaultRightImages((new Frame("/graphics/enemy1.png")),(new Frame("/graphics/enemy1.png")));
        sprite.loadLeftMotionImages((new Frame("/graphics/enemy1.png")));
        sprite.loadRightMotionImages((new Frame("/graphics/enemy1.png")));
        sprite.offset(-5, -16);
    }

    public void giveMotion (Map map) {
        /**
         This method defines the motion logic of the enemy

         @rules:
         * At start, if cannot fall, move RIGHT
         * Once reach the 'moves limit' in one direction, change to other direction
         * Once has reach limit for both LEFT and RIGHT, reset counter and generate a new random 'moves limit'
         * if can fall, and gap too big to be jumped - turn back
         * if can fall, and platform/ground after - 1/2 chance to jump!
         * if finds a block constrain, jump over!

         **/
        // reset horizontal speed after a jump
        if (isLanded ) {
            this.Velocity = new Point2D (0, this.Velocity.getY ());
        }
        // Enemy goes RIGHT if he is allowed
        if (countMoveRight < limitMoves) {
            if (isGapRightTooBig (map)) {
                countMoveRight = limitMoves;
                return;
            }
            if ( (CanJumpGapRight (map) && decideToJump (map, "RIGHT"))
                || CanJumpBlockRight (map)) {
                jump (JUMP_SPEED_X);
                return;
            }
            move_X (MOVE_SPEED, map);
            countMoveRight++;
        }
        // Enemy goes LEFT if he is allowed
        if (countMoveRight == limitMoves && countMoveLeft < limitMoves) {
            if (isGapLeftTooBig (map)) {
                countMoveLeft = limitMoves;
                return;
            }
            if ( (CanJumpGapLeft (map) && decideToJump (map, "LEFT"))
                    || CanJumpBlockLeft (map)) {
                jump (-JUMP_SPEED_X);
                return;
            }
            move_X (-MOVE_SPEED, map);
            countMoveLeft++;
        }
        // Reset directions counters & generate new 'moves limit'
        if (countMoveLeft == limitMoves && countMoveRight == limitMoves) {
            countMoveRight=0;
            countMoveLeft=0;
            limitMoves = MIN_MOVE + new Random ().nextInt(MAX_MOVES_AMPLITUDE);
        }
    }

    /* -------------------- PRIVATE METHODS ------------------------- */

    private void jump (double value) {
        if (CanJump) {
            this.Velocity = this.Velocity.add (value, -JUMP_SPEED_Y);
            CanJump=false;
        }
    }

    private boolean isGapRightTooBig (Map map) {
        /**
         @output: 'true' if there is a big gap that the enemy cannot jump without dying
         **/
        for (Object platform : map.getLevel ().platforms ()) {
            if ( (platform.getY () >= (getY () + height) )
                && platform.getX () > getX ()
                && platform.getX () < getX () + width + 2*platform.width) {
                return false;
            }
        }
      //  System.out.println ("GAP RIGHT TOO BIG");
        return true;
    }
    private boolean isGapLeftTooBig (Map map) {
        /**
         @output: 'true' if there is a big gap that the enemy cannot jump without dying
         **/
        for (Object platform : map.getLevel ().platforms ()) {
            if ( (platform.getY () >= (getY () + height) )
                    && platform.getX () < getX ()
                    && platform.getX () > getX () - width - 2*platform.width) {
                return false;
            }
        }
      //  System.out.println ("GAP LEFT TOO BIG");
        return true;
    }

    private boolean CanJumpGapRight (Map map) {
        /**
         @output: Return 'true' if satisfies the following conditions:

          * if a platform/block is above/below enemy (condition 1)
          * if enemy is not at start RIGHT of the map (condition 2)
          * if this platform/block is on the RIGHT
         and gap between is 1-2 platform length (condition 3, 4 and 5)

         **/
        for (Object platform : map.getLevel ().platforms ()) {
            // case 1: enemy has no gap on his RIGHT
            if (    platform.getX() >= getX()
                    && platform.getX () < getX () + 2 * platform.width
                    && platform.getY () >= getY() + height){
                return false;
            }
            // case 2: enemy has a gap on his RIGHT, small enough to jump
            if (    platform.getY () >= getY () - 2 * platform.height
                    && platform.getX () < map.getLevel ().width() - platform.width - OFFSET
                    && getX () < platform.getX() - platform.width
                    && getX () > platform.getX () - 2 * platform.width) {
          //      System.out.println ("CAN JUMP GAP RIGHT");
                return true;
            }
        }
        return false;
    }

    private boolean CanJumpGapLeft(Map map) {
        /**
         @output: Return 'true' if satisfies the following conditions:

          * if a platform/block is above/below enemy (condition 1)
          * if enemy is not at start LEFT of the map (condition 2)
          * if this platform/block is on the LEFT
            and gap between is 1-2 platform length (condition 3, 4 and 5)

         **/
        for (Object platform : map.getLevel ().platforms ()) {
            // case 1: enemy has no gap on his LEFT
            if (platform.getX () <= getX ()
                && platform.getX () >= getX () - 2 * platform.width
                    && platform.getY () >= getY ()) {
                return false;
            }
            // case 2: enemy has a gap on his LEFT, small enough to jump
            if (    platform.getY () >= getY () - 2 * platform.height
                    && platform.getX () > platform.width + OFFSET
                    && getX () < platform.getX () + platform.width * 3
                    && getX () > platform.getX () + platform.width * 2) {
         //       System.out.println ("CAN JUMP GAP LEFT");
                return true;
            }
        }
        return false;
    }

    private boolean CanJumpBlockRight (Map map) {
        /**
         @output 'true' if satisfies the following conditions:

          * if the enemy hasn't reached end RIGHT of the map (condition 1)
          * if a block is on LEFT, next to him (condition 2 and 3)
          * if the block is just above him (condition 4 and 5)
         */
        for (Object platform : map.getLevel ().platforms ()) {
            if (    getX () + width < map.getLevel ().width() - platform.width - OFFSET
                    && (platform.getX () > getX() + width)
                    && (platform.getX () <= getX() + width + OFFSET)
                    && (platform.getY() + platform.height == getY() + height) ){
                //System.out.println ("CAN JUMP RIGHT");
                return true;
            }
        }
        return false;
    }

    private boolean CanJumpBlockLeft(Map map) {
        /**
         @output 'true' if satisfies the following conditions:

          * if the enemy hasn't reached start LEFT of the map (condition 1)
          * if a block is on LEFT, next to him (condition 2 and 3)
          * if the block is just above him (condition 4 and 5)
         */
        for (Object platform : map.getLevel ().platforms ()) {


            if (    getX () > platform.width + OFFSET
                    && (getX () >= platform.getX())
                    && (getX () <= platform.getX() + platform.width + OFFSET)
                    && (platform.getY() + platform.height == getY() + height) ){
        //        System.out.println ("CAN JUMP LEFT");
                return true;
            }
        }
        return false;
    }

    private boolean decideToJump (Map map, String side) {
        /**
            If the enemy can jump to a new platform - here has 1/45 chance to actually do it
            Because this function is called 60 times per second. It gives more chance to the enemy to actually jump
            @output: 'true' if rand = 1, 'false' if rand = 0
         **/
        int rand = new Random ().nextInt(JUMP_PROBA_RANGE);
        if (rand != 1) {
            for (Object p : map.getLevel ().platforms ()) {
                // only jump if there is a platform so he doesn't die
                if (p.getY () > getY () + height) {
                    if (   (side == "RIGHT" && (getX () > p.getX () - width) && (getX () <  p.getX () + width))
                        || (side == "LEFT" && getX () > p.getX () + p.width - width && (getX () < p.getX () + p.width + width))   ){
                //        System.out.println ("SAFE TO ABORT JUMP " + side);
                        return false;
                    }
                }
            }
      //      System.out.println ("WASN'T SAFE TO NOT JUMP " + side);
        }
        return true;
    }

    /* -------------------------- GETTERS & SETTERS -------------------------- */

    public boolean getCanMove() {
        return canMove;
    }


}
