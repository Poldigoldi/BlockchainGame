package sample;

import javafx.scene.Group;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.geometry.Point2D;
import javafx.scene.transform.Rotate;

import java.nio.file.Paths;
import java.util.ArrayList;


/*Objects hold two things, a collision box, and an image.
An animated object can move left/right/up/down (when we choose), e.g. for a player that would be when we use the arrow keys
but for an enemy we would give it basic instructions to move left/right randomly. If an enemy fell off a platform
it would also fall, so we still want to apply gravity to it.

If you are making an object, e.g. player, platform, item, first declare the new object and give it an appropriate type,
then set its collision box. The collision box is the important thing, it has an x and y and tells you 'where' the object
is, the sprite just follows the collision box.
If you create a new type of object and have something specific in mind you want it to do during update, you can change
the update function to include new behaviours for that type of object.
 */
public class Object {
    //Object components
    public Shape box;
    public Sprite sprite;

    //Object variables
    boolean alive;
    public Type type;
    public int width;
    public int height;
    boolean canJump = true;
    Point2D Velocity = new Point2D(0, 0);
    boolean facingRight;
    boolean movingDown;
    boolean isMoving;
    boolean isLanded;
    private boolean spin;
    private int spincounter;
    private boolean facingRightOnPlatform;

    //sounds
    private AudioClip landSound = new AudioClip(Paths.get("src/sound/land.wav").toUri().toString());

    //choose where the object starts, if its animated, its type, and its initial image.
    public Object(Type type, Frame... defaultFrame) {
        sprite = new Sprite(type, defaultFrame);
        this.type = type;
        this.alive = true;
        this.facingRight = true;
        sprite.setRotationAxis(Rotate.Y_AXIS);
    }

    //creates a collision box, this is initially a rectangle, but in the future it could be other shapes.
    public void setCollisionBox(double x, double y, int width, int height, Color colour) {
        box = new Rectangle(width, height, colour);
        box.setTranslateX(x);
        box.setTranslateY(y);
        this.width = width;
        this.height = height;
        //**NOTE** you can change SHOWCOLLISIONBOXES to true to see collision boxes if you want.
        boolean SHOWCOLLISIONBOXES = false;
        if (SHOWCOLLISIONBOXES == false) box.setFill(Color.TRANSPARENT);
        box.opacityProperty().set(0.25);
        sprite.moveTo(box.getTranslateX(), box.getTranslateY());
    }

    public int height() {
        return height;
    }

    public int width() {
        return width;
    }

    public Sprite sprite() {
        return sprite;
    }

    public void add(Group group) {
        if (box == null) {
            box = new Rectangle(5, 5, Color.BLACK);
        }
        group.getChildren().addAll(sprite, box);
    }

    //shows or hide object on the screen
    public void setVisible(boolean visible) {
        sprite.setVisible(visible);
        box.setVisible(visible);
    }

    //the only objects you'd want to update are animated objects, e.g. anything that can fall, move around
    //this would include the player, enemies, cubes falling from the sky etc.
    void update(Map map) {
        //update Sprite position and animation
        if (spin) spin();
        sprite.update(spin, facingRight, isMoving, isLanded, movingDown, box.getTranslateX(), box.getTranslateY());
        //for Clouds
        if (type == Type.SPRAY) if (sprite.dead()) map.hideEntity(this);
        if (type == Type.LAYER3) {
            circumnavigate(0.05, map);
        }
        if (type == Type.LAYER1) {
            circumnavigate(0.2, map);
        }
        if (type.hasGravity()) {
            //for Everything else that has gravity
            applyGravity();
        }
        if (type.hasGravity() || type == Type.KING) {
            updateY(map);
            updateX(map);
            isMoving = false;
        }
    }

    private void applyGravity() {
        if (this.Velocity.getY() < 10) {
            this.Velocity = this.Velocity.add(0, 1);
        }
    }

    private void spin() {
        spincounter++;
        sprite.setRotate(sprite.getRotate() + 0.4 * (50 - spincounter));
        if (spincounter == 50) {
            spin = false;
            sprite.setRotate(0);
            spincounter = 0;
        }
    }

    private void updateY(Map map) {
        move_Y((int) this.Velocity.getY(), map);
    }

    private void updateX(Map map) {
        if (this.type != Type.PLAYER) move_X((int) this.Velocity.getX(), map);
    }


    //returns true if the move is not blocked
    boolean move_X(int value, Map map) {
        isMoving = true;
        facingRight = value > 0;
        for (Object object : map.level().platforms()) {
            //checks if player at same height as object and its solid first, then block left/right movements
            if (type == Type.BULLET && box.getBoundsInParent().intersects(object.box.getBoundsInParent())) {
                return false;
            }

            if (object.type == Type.SOLID && this.getY() > object.getY() - this.height && this.getY() < object.getY() + this.height) {
                if (facingRight && Math.abs(object.getX() - this.getX() - this.width) <= 5) return false;
                if (!facingRight && Math.abs(object.getX() + object.width() - this.getX()) <= 5) return false;
            }
        }
        for (int i = 0; i < Math.abs(value); i++) {
            this.setX(this.getX() + (facingRight ? 1 : -1));
        }
        return true;
    }


    void move_Y(int value, Map map) {
        movingDown = value > 0; // (Y=0) at the top of the frame
        for (int i = 0; i < Math.abs(value); i++) {
            /* Check for collisions between player and platforms */
            for (Platform object : map.level().platforms()) {
                if (box.getBoundsInParent().intersects(object.box.getBoundsInParent())) {
                    if (movingDown) {
                        if (this.getY() + this.height == object.getY()) {
                            if (!isLanded && (this.type == Type.PLAYER || this.type == Type.ENEMY))
                                landSound.play();
                            canJump = true;
                            isLanded = true;
                            facingRightOnPlatform = facingRight;
                            if (object.isMoving && object.isMovingRight()) {
                                move_X(2, map);
                                if (this.type == Type.PLAYER) map.moveScreenX(2, this);
                            } else if (object.isMoving && !object.isMovingRight()) {
                                move_X(-2, map);
                                if (this.type == Type.PLAYER) map.moveScreenX(-2, this);
                            }
                            facingRight = facingRightOnPlatform;
                            return;
                        }
                    } else { // Moving up
                        if (this.getY() == object.getY() + object.height() && object.type == Type.SOLID) {
                            return;
                        }
                    }
                }
            }
            /* Move ! Happens if no collision were detected */
            this.setY(this.getY() + (movingDown ? 1 : -1));
            isLanded = false;
        }
    }

    void died() {
        sprite.activateDeathAnimation();
        box.setVisible(false);
    }

    //for clouds
    private void circumnavigate(double speed, Map map) {
        box.setTranslateX(box.getTranslateX() + speed);
        if (box.getTranslateX() > map.level().width()) box.setTranslateX(-sprite.getImage().getWidth());
    }

    public static double random(double min, double max) {
        return (Math.random() * ((max - min) + 1)) + min;
    }


    //getters, setters

    void setspin(boolean spin) {
        spincounter = 0;
        this.spin = spin;
    }

    boolean getSpin() {
        return spin;
    }

    public double getX() {
        return box.getTranslateX();
    }

    public double getY() {
        return box.getTranslateY();
    }

    public void setX(double x) {
        box.setTranslateX(x);
    }

    public void setY(double y) {
        box.setTranslateY(y);
    }

    boolean isAlive() {
        return alive;
    }

    void setAlive(boolean alive) {
        this.alive = alive;
    }

    void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }

    boolean isFacingRight() {
        return facingRight;
    }
}
