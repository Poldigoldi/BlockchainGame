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
  private String colour;
  private boolean pressed = false;
  private Frame buttonUp = new Frame("/graphics/buttonUp.png");
  private Frame buttonUpRed = new Frame("/graphics/buttonUpRed.png");
  private Frame buttonDown = new Frame("/graphics/buttonDown.png");


  PlatformButton(Type type, String colour) {
    super(type);
    this.colour = colour;
    if (colour.equals("red")) {sprite.setImage(buttonUpRed);}
    else { sprite.setImage(buttonUp);}
    sprite().offset(-11, -40);
  }





  void setPressed(boolean pressed) {
    this.pressed = pressed;
  }

  void buttonDown() {
    sprite.setImage(buttonDown);
  }

  void buttonUp() {
    sprite.setImage(buttonUp);
  }

  boolean isPressed(){
    return this.pressed;
  }

  public String getColour() {
    return this.colour;
  }

}