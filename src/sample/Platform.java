package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;


public class Platform extends Object{
  private boolean canDisappear = false;
  private ArrayList<Integer> collisionValues = new ArrayList<>();

  Platform(Type type, Frame ... frame) {
    super(type);
    sprite = new Sprite(type, frame);
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


}