package sample;

import javafx.animation.AnimationTimer;
import javafx.scene.transform.Rotate;

/*animated object with a listener in main for player interaction*/
class Life extends Collectable {
    Life() {
        setItemType(Type.HEART);
        Frame heart = new Frame("/graphics/healthcollect.png");
        sprite.setImage(heart);
        sprite().offset(-20, -40);
        sprite.setRotationAxis(Rotate.Y_AXIS);
        new AnimationTimer() {
            int i;

            public void handle(long now) {
                i += 2;
                sprite.setRotate(i);
            }
        }.start();
    }
}

