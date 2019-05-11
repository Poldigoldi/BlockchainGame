package sample;


import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class Bullet extends Object {

    int SPEED = 3;

    Bullet (double startx, double starty) {
        super(Type.BULLET, new Frame("/graphics/item1.png"));
        setCollisionBox (startx, starty, 20, 5, Color.BLACK);
    }

    public void shoot (Map map) {
        move_X (SPEED, map);
    }

    public void stop () {
        Velocity = new Point2D (0, 0);
    }
}
