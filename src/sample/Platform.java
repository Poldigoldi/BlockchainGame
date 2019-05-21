package sample;

import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/*Objects of class platform provide the constructs for the player to stand on and interact with.
there are a number of different types of platforms, which are classified using an array of different constructors.
 */
public class Platform extends Object {
    private boolean disappearing = false;
    private String colour;
    private boolean timed = false;
    private ArrayList<Integer> collisionValues = new ArrayList<>();
    private boolean movingRight = true;
    private int updateCount;

    /*default platforms*/
    Platform(Type type, Frame... frame) {
        super(type);
        sprite = new Sprite(type, frame);
    }

    /*dissapearing platforms*/
    Platform(Type type, boolean timed) {
        super(type);
        this.timed = timed;
        Frame frame = new Frame("/graphics/disappear1.png");
        sprite.setImage(frame);
    }

    Platform(Type type, String colour, Frame... frame) {
        super(type);
        this.colour = colour;
        sprite = new Sprite(type, frame);
    }

    /*dynamic button activated platforms*/
    Platform(Type type, String colour) {
        super(type);
        this.colour = colour;
        alive = true;
        if (colour.equals("orange")) {
            sprite.loadDefaultImages(new Frame("/graphics/orangePlatform1.png", 100),
                    new Frame("/graphics/orangePlatform2.png"),
                    new Frame("/graphics/orangePlatform3.png"),
                    new Frame("/graphics/orangePlatform4.png", 40),
                    new Frame("/graphics/orangePlatform3.png"),
                    new Frame("/graphics/orangePlatform2.png"));
        }
        if (colour.equals("red")) {
            sprite.loadDefaultImages(new Frame("/graphics/redPlatform1.png", 100),
                    new Frame("/graphics/redPlatform2.png"),
                    new Frame("/graphics/redPlatform3.png"),
                    new Frame("/graphics/redPlatform4.png", 40),
                    new Frame("/graphics/redPlatform3.png"),
                    new Frame("/graphics/redPlatform2.png"));
        }
    }

    /*moving platforms*/
    Platform(Type type, int movement, Frame... frame) {
        super(type, frame);
        isMoving = true;
        alive = true;
        new AnimationTimer() {
            int i = 0;

            public void handle(long now) {
                if (movingRight) {
                    i += 2;
                    box.setTranslateX(getX() + 2);
                }
                if (!movingRight) {
                    i -= 2;
                    box.setTranslateX(getX() - 2);
                }
                if (i >= movement) {
                    movingRight = false;
                }
                if (i <= 0) {
                    movingRight = true;
                }
            }
        }.start();
    }

    void calculateUpdate(int updateCount) {
        this.updateCount = updateCount + 50;
    }

    /*required to store platform collision box locations which are changed or removed during play*/
    void setCollisionValues(int x, int y, int width, int height) {
        collisionValues.add(x);
        collisionValues.add(y);
        collisionValues.add(width);
        collisionValues.add(height);
    }

    void restoreCollisionBox() {
        setCollisionBox(collisionValues.get(0), collisionValues.get(1), collisionValues.get(2), collisionValues.get(3), Color.DARKORANGE);
    }

    boolean disappearing() {
        return disappearing;
    }


    void setDisappear(boolean disappear) {
        this.disappearing = disappear;
    }

    boolean canDisappear() {
        return this.disappearing;
    }

    String getColour() {
        return this.colour;
    }

    boolean isMovingRight() {
        return movingRight;
    }

    public boolean isTimed() {
        return timed;
    }

    boolean matchUpdate(int updateCount) {
        return this.updateCount == updateCount;
    }
}