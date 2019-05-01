package sample;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class EnemyType1 extends Object {
    private boolean alive;
    private boolean canMove;
    private int countMoveLeft;
    private int countMoveRight;

    private int MAX_MOVE = 150;
    private int MOVE_SPEED = 1;

    public EnemyType1(int startx, int starty, boolean canMove) {
        super(Type.ENEMY1);
        this.setCollisionBox(startx, starty , 38, 48, Color.INDIANRED);
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

    // Assign random motion to Enemy
    // Will move a certain amount right - once reach the counter limit, it will move to the left (and again)
    public void giveMotion (Map map) {
        if (countMoveRight < MAX_MOVE) {
            move_X (MOVE_SPEED, map);
            countMoveRight++;
        }
        if (countMoveRight == MAX_MOVE && countMoveLeft < MAX_MOVE) {
            move_X (-MOVE_SPEED, map);
            countMoveLeft++;
        }
        if (countMoveLeft == MAX_MOVE && countMoveRight == MAX_MOVE) {
            countMoveRight=0;
            countMoveLeft=0;
            MAX_MOVE = 50 + new Random ().nextInt(150);
        }
    }

    public boolean isCanMove() {
        return canMove;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }
}
