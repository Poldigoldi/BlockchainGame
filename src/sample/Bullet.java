package sample;


import javafx.scene.paint.Color;

public class Bullet extends Object {

    final int SPEED_MAGNITUDE = 6;
    final String DIRECTION;

    Bullet (double startx, double starty, String direction) {
        super(Type.BULLET, new Frame("/graphics/item1.png"));
        setCollisionBox (startx, starty, 20, 10, Color.BLACK);
        this.DIRECTION = direction;
    }

    public void move (Map map) {
        int speed = speedSign (DIRECTION) * SPEED_MAGNITUDE;
        if (! move_X (speed, map)) {
            setAlive (false);
            map.hideEntity (this);
        }
    }

    private int speedSign (String direction) {
        switch (direction) {
            case "LEFT": return -1;
            case "RIGHT": return 1;
        }
        return 0;
    }

}
