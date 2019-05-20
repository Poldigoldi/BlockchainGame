package sample;

 /*
      This class creates user intractable button objects
      they have a value of if pressed or not
      this means that when they are pressed an event can occur

      # Constructor #
     implements attributes previously described in the object class
  */

class PlatformButton extends Object {
    private String colour;
    private boolean pressed = false;
    private Frame buttonUp;
    private Frame buttonDown;


    PlatformButton(Type type, String colour, Frame up, Frame down) {
        super(type);
        buttonUp = up;
        buttonDown = down;
        this.colour = colour;
        sprite.setImage(buttonUp);
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

    boolean isPressed() {
        return this.pressed;
    }

    String getColour() {
        return this.colour;
    }
}