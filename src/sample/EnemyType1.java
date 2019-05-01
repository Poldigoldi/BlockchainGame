package sample;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class EnemyType1 extends Object {
    private boolean alive;
    private boolean canMove;
    private int countMoveLeft;
    private int countMoveRight;

    private final int HEIGHT = 48;

    private final int MOVE_SPEED = 1;
    private final int JUMP_SPEED_X = 2;
    private final int MIN_MOVE = 50;
    private int MAX_MOVE = 400;

    public EnemyType1(int startx, int starty, boolean canMove) {
        super(Type.ENEMY1);
        this.setCollisionBox(startx, starty , 38, HEIGHT, Color.INDIANRED);
        this.canMove = canMove;
        this.alive = true;
        this.countMoveLeft=0;
        this.countMoveRight=0;
        sprite.loadDefaultLeftImages(new Frame("/graphics/enemy1.png"),
                new Frame("/graphics/enemy1.png"));
        sprite.loadDefaultRightImages((new Frame("/graphics/enemy1.png")),(new Frame("/graphics/enemy1.png")));
        sprite.loadLeftMotionImages((new Frame("/graphics/enemy1.png")));
        sprite.loadRightMotionImages((new Frame("/graphics/enemy1.png")));
        sprite.offset(-5, -16);
    }


    public void giveMotion (Map map) {
        /** rules:
         // - if cannot fall, generate random nb to move left/right
         // - if can fall, and no ground after - turn back
         // - if can fall, and platform/ground after - has 1/8 chance to jump
         // - if finds a constrain, try to jump over **/

        // reset horizontal speed after a jump
        if (isLanded ) {
            this.Velocity = new Point2D (0, this.Velocity.getY ());
        }
        // Enemy now goes RIGHT if he is allowed
        if (countMoveRight < MAX_MOVE) {
            if (isGapRightTooBig (map)) {
                countMoveRight = MAX_MOVE;
                return;
            }
            if ( (CanJumpGapRight (map) && decideToJump ())
                || CanJumpBlockRight (map)) {
                jump (JUMP_SPEED_X);
                return;
            }
            move_X (MOVE_SPEED, map);
            countMoveRight++;
        }
        // Enemy now goes LEFT if he is allowed
        if (countMoveRight == MAX_MOVE && countMoveLeft < MAX_MOVE) {
            if (isGapLeftTooBig (map)) {
                countMoveLeft = MAX_MOVE;
                return;
            }
            if ( (CanJumpGapLeft (map) && decideToJump ())
                    || CanJumpBlockLeft (map)) {
                jump (-JUMP_SPEED_X);
                return;
            }
            move_X (-MOVE_SPEED, map);
            countMoveLeft++;
        }
        // Reset counter
        if (countMoveLeft == MAX_MOVE && countMoveRight == MAX_MOVE) {
            countMoveRight=0;
            countMoveLeft=0;
            MAX_MOVE = MIN_MOVE + new Random ().nextInt(700);
        }
    }
    private void jump (int value) {
        if (CanJump) {
            this.Velocity = this.Velocity.add (value, -27);
            CanJump=false;
        }
    }

    private boolean isGapRightTooBig (Map map) {
        for (Object platform : map.getLevel ().platforms ()) {

            if ( (platform.getY () >= (getY () + height) )
                && platform.getX () > getX ()
                && platform.getX () < getX () + width + 2*platform.width) {
                return false;
            }
        }
        System.out.println ("GAP RIGHT TOO BIG");
        return true;
    }
    private boolean isGapLeftTooBig (Map map) {
        for (Object platform : map.getLevel ().platforms ()) {
            if ( (platform.getY () >= (getY () + height) )
                    && platform.getX () < getX ()
                    && platform.getX () > getX () - width - 2*platform.width) {
                return false;
            }
        }
        System.out.println ("GAP LEFT TOO BIG");
        return true;
    }

    private boolean CanJumpGapRight (Map map) {
        for (Object platform : map.getLevel ().platforms ()) {
            if (platform.getX () == getX () + width
                    || platform.getY () >= getY () + width) {
                return false;
            }

            if (Math.abs (platform.getY () - getY ()) <= platform.width
                    && getX () + width < map.getLevel ().width() - platform.width - 10
                    && platform.getX () < getX () + width + platform.width + 10
                    && platform.getX () > getX () + width + 10) {
                System.out.println ("CAN JUMP GAP RIGHT");
                return true;
            }
        }
        return false;
    }

    private boolean CanJumpGapLeft(Map map) {
        for (Object platform : map.getLevel ().platforms ()) {
            if (platform.getX () == getX () + width
                || platform.getY () >= getY () + width) {
                return false;
            }

            if (Math.abs (platform.getY () - getY ()) < platform.width
                    && getX () > platform.width + 10
                    && getX () < platform.getX () + platform.width * 2
                    && getX () > platform.getX () + platform.width) {
                System.out.println ("CAN JUMP GAP LEFT");
                return true;
            }
        }
        return false;
    }

    private boolean CanJumpBlockRight (Map map) {
        for (Object platform : map.getLevel ().platforms ()) {

            if (    getX () + width < map.getLevel ().width() - platform.width - 10
                    && (platform.getX () > getX() + width) && (platform.getX () <= getX() + width + 5)
                    && (platform.getY() < getY()) && (getY() - platform.getY()) <= platform.height*1.5){
                System.out.println ("CAN JUMP RIGHT");
                return true;
            }
        }
        return false;
    }

    private boolean CanJumpBlockLeft(Map map) {
        for (Object platform : map.getLevel ().platforms ()) {
            if (    getX () > platform.width + 10 &&
                    (getX () - platform.getX() - platform.width) > 0 && (getX () - platform.getX() - platform.width) <= 5 &&
                    (platform.getY() < getY()) && (getY() - platform.getY()) <= platform.height*1.5){
                System.out.println ("CAN JUMP LEFT");
                return true;
            }
        }
        return false;
    }

    public boolean decideToJump () {
        int rand = new Random ().nextInt(2);
        if (rand == 1) {
            System.out.println ("DECIDE TO JUMP THE GAP: ");
            return true;
        }
        return false;
    }


    /* ------------ GETTERS & SETTERS -------------- */

    public boolean isCanMove() {
        return canMove;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }
}
