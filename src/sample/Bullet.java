package sample;


import javafx.scene.paint.Color;

public class Bullet extends Object {

    final int SPEED_MAGNITUDE = 6;
    final boolean playerFacingRight;

    Bullet (double startx, double starty, boolean facingRight) {
        super(Type.BULLET, new Frame("/graphics/item1.png"));
        setCollisionBox (startx, starty, 20, 10, Color.BLACK);
        this.playerFacingRight = facingRight;
    }

    public void move (Map map) {
        int speed = (playerFacingRight ? 1 : -1) * SPEED_MAGNITUDE;
        if (! move_X (speed, map)) {
            setAlive (false);
            map.hideEntity (this);
        }
    }
}
