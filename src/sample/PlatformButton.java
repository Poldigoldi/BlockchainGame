package sample;

import javafx.scene.paint.Color;

 /*
      This class creates user intractable button objects
      they have a value of if pressed or not
      this means that when they are pressed an event can occur

      # Constructor #
     implements attributes previously described in the object class
  */

public class PlatformButton extends Object{
  private boolean pressed = false;


  PlatformButton(Type type, Frame ... frame) {
    super(type);
    sprite = new Sprite(type, frame);
  }

  void setPressed(boolean pressed) {
    this.pressed = pressed;
  }

  boolean isPressed(){
    return this.pressed;
  }

}