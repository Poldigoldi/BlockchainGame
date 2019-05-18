package sample;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;


public class Platform extends Object {
  private boolean canDisappear = false;
  private String colour;
  private ArrayList<Integer> collisionValues = new ArrayList<>();

  Platform(Type type, Frame ... frame) {
    super(type);
    sprite = new Sprite(type, frame);
  }

  Platform(Type type,String colour, Frame ... frame) {
    super(type);
    this.colour = colour;
    sprite = new Sprite(type, frame);
  }

  Platform (Type type, String colour ) {
    super(type);
    this.colour = colour;
    alive = true;
    sprite.loadDefaultImages(new Frame("/graphics/orangePlatform1.png", 100),
            new Frame("/graphics/orangePlatform2.png"),
            new Frame("/graphics/orangePlatform3.png"),
            new Frame("/graphics/orangePlatform4.png", 40),
            new Frame("/graphics/orangePlatform3.png"),
            new Frame("/graphics/orangePlatform2.png"));
  }

  public Platform(Type type, int movement, double currentX, double currentY, int obj_width, Frame... frame) {
    super(type, frame);
    isMoving = true;
    alive = true;
      setCollisionBox(currentX * obj_width, currentY * 64, obj_width, 10, Color.GREEN);
      new AnimationTimer() {
          boolean direction = true;
          int i = 0;
          public void handle(long now) {
              if (direction) {
                  i += 2;
              }
              if (!direction) {
                  i -= 2;
              }
              if (i >= movement) {
                  direction = false;
              }
              if (i <= 0) {
                  direction = true;
              }
              double newX = currentX + i;
              box.setTranslateX(newX);
          }
      }.start();
  }


  public void setCollisionValues(int x, int y, int width, int height) {
    if (canDisappear == true) {
      collisionValues.add(x);
      collisionValues.add(y);
      collisionValues.add(width);
      collisionValues.add(height);
    }
  }


  public void restoreCollisionBox() {
    setCollisionBox(collisionValues.get(0), collisionValues.get(1), collisionValues.get(2), collisionValues.get(3), Color.DARKORANGE);
  }


  public void setDisappear(boolean disappear) {
    this.canDisappear = disappear;
  }

  public boolean canDisappear() {
    return this.canDisappear;
  }

  public String getColour() {
    return this.colour;
  }



}