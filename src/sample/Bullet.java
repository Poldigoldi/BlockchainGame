package sample;


import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class Bullet extends Object {

    final int SPEED_MAGNITUDE = 6;
    final boolean playerFacingRight;
    Label label = new Label("010101");

    Bullet (double startx, double starty, boolean facingRight) {
        super(Type.BULLET, new Frame("/graphics/item1.png"));
        setCollisionBox (startx, starty, 20, 10, Color.BLACK);
        this.playerFacingRight = facingRight;
    }

    public Label label(){ return label; }

    public void move (Map map) {
        label.setTranslateX(box.getTranslateX());
        label.setTranslateY(box.getTranslateY());
        int speed = (playerFacingRight ? 1 : -1) * SPEED_MAGNITUDE;
        if (! move_X (speed, map)) {
            setAlive (false);
            map.hideEntity (this);
        }
    }

}
