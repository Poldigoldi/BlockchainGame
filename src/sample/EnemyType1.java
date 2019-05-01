package sample;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class EnemyType1 extends Object {
    private boolean alive;

    public EnemyType1(int startx, int starty) {
        super(Type.ENEMY1);
        this.setCollisionBox(startx, starty , 38, 48, Color.INDIANRED);
        alive = true;
        sprite.loadDefaultLeftImages(new Frame("/graphics/enemy1.png"),
                new Frame("/graphics/enemy1.png"));
        sprite.loadDefaultRightImages((new Frame("/graphics/enemy1.png")),(new Frame("/graphics/enemy1.png")));
        sprite.loadLeftMotionImages((new Frame("/graphics/enemy1.png")));
        sprite.loadRightMotionImages((new Frame("/graphics/enemy1.png")));
        sprite.offset(-5, -16);
    }
}
