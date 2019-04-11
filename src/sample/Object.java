package sample;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.image.Image;
import javafx.geometry.Point2D;


/*Objects hold two things, a collision box, and an image.
Objects can be animated, in which case you will want to call update on them.
An animated object can move left/right/up/down (when we choose), e.g. for a player that would be when we use the keys
but for an enemy we would give it basic instructions to move left/right randomly. If an enemy fell off a platform
it would also fall, so we still want to apply gravity to it.
Platforms would be 'not animated' so they dont need to be updated.
 */
public class Object extends Group {
    //**NOTE** you can change SHOWCOLLISIONBOXES to true to see collision boxes if you want.
    private boolean SHOWCOLLISIONBOXES = true;
    public Boolean animated;
    public Shape collisionBox;
    public Sprite sprite;
    public Type type;
    public int width;
    public int height;
    public boolean CanJump = true;
    public Point2D Velocity = new Point2D(0,0);

    //choose where the object starts, if its animated, its type, and its initial image.
    public Object(int x, int y, boolean animated, Type type, Image defaultImage){
        sprite = new Sprite(defaultImage);
        this.animated = animated;
        this.type = type;
        getChildren().addAll(sprite);
        setTranslateX(x);
        setTranslateY(y);
    }

    //creates a collision box, this is initially a rectangle, but in the future it could be other shapes.
    public void setCollisionBox(int width, int height, Color colour){
        collisionBox = new Rectangle(width, height, colour);
        this.width = width;
        this.height = height;
        if (SHOWCOLLISIONBOXES == false) collisionBox.setFill(Color.TRANSPARENT);
        collisionBox.opacityProperty().set(0.3);
        getChildren().addAll(collisionBox);
    }

    public int height() {return height;}
    public int width() {return width;}

    public Sprite sprite() { return sprite;}


    //the only objects you'd want to update are animated objects, e.g. anything that can fall, move around
    //this would include the player, enemies, cubes falling from the sky etc.
    void update(Map map, int direction, boolean moving){
        applyGravity();
        updateY(map);
        sprite().update(direction, moving);
    }

    void applyGravity() {
        if (this.Velocity.getY () < 10) {
            this.Velocity = this.Velocity.add(0, 1);
        }
    }

    void updateY (Map map) {
        move_Y((int)this.Velocity.getY(), map);
    }

    public void move_X(int value, Map map) {
        Boolean movingRight = value > 0;
        for (Object object : map.level().platforms()) {
            //checks if player at same height as object and its solid first
            if(object.type == Type.SOLID && this.getTranslateY()> object.getTranslateY()-this.height
                    && this.getTranslateY()< object.getTranslateY()+object.height()){
                if(movingRight && Math.abs(object.getTranslateX() - this.getTranslateX() - this.width) < 5) return;
                if(!movingRight && Math.abs(object.getTranslateX()+object.width()-this.getTranslateX()) < 5) return;
            }
        }
        for (int i=0; i<Math.abs(value); i++) {
            setTranslateX(getTranslateX() + (movingRight ? 1 : -1));
        }
    }

    public void move_Y(int value, Map map) {
        Boolean movingDown = value > 0; // (Y=0) at the top of the frame
        for (int i=0; i<Math.abs(value); i++) {
            /* Check for collisions between player and platforms */
            for (Object object : map.level().platforms()) {
                if (getBoundsInParent().intersects (object.getBoundsInParent ())) {
                    if (movingDown) {
                        if (getTranslateY () + this.height == object.getTranslateY ()) {
                            CanJump = true;
                            return;
                        }
                    } else { // Moving up
                        if (getTranslateY () == object.getTranslateY () + object.height() && object.type == Type.SOLID) {
                            return;
                        }
                    }
                }
            }
            /* Move ! Happens if no collision were detected */
            setTranslateY (getTranslateY () + (movingDown ? 1 : -1));
        }
    }


}
