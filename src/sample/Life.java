package sample;

import javafx.animation.AnimationTimer;
import javafx.scene.transform.Rotate;

public class Life extends Collectable {
  private Frame heart = new Frame("/graphics/healthcollect.png");
  private String name;

  Life(String name) {
    setItemType (Type.HEART);
    this.name = name;
    sprite.setImage(heart);
    sprite().offset(-20, -40);
    sprite.setRotationAxis(Rotate.Y_AXIS);
    new AnimationTimer() {
      int i;
      public void handle(long now) {
        i+=2;
        sprite.setRotate(i);
      }
    }.start();
  }
}

