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
public class Object  {
    //**NOTE** you can change SHOWCOLLISIONBOXES to true to see collision boxes if you want.
    private boolean SHOWCOLLISIONBOXES = true;

    public Shape box;
    public Sprite sprite;
    public Type type;
    public int width;
    public int height;
    public boolean CanJump = true;
    public Point2D Velocity = new Point2D(0,0);
    public boolean movingRight;
    public boolean movingDown;
    public boolean isMoving;

    //choose where the object starts, if its animated, its type, and its initial image.
    public Object(Type type, Image defaultImage){
        sprite = new Sprite(defaultImage);
        this.type = type;
    }

    //creates a collision box, this is initially a rectangle, but in the future it could be other shapes.
    public void setCollisionBox(int x, int y, int width, int height, Color colour){
        box = new Rectangle(width, height, colour);
        box.setTranslateX(x);
        box.setTranslateY(y);
        this.width = width;
        this.height = height;
        if (SHOWCOLLISIONBOXES == false) box.setFill(Color.TRANSPARENT);
        box.opacityProperty().set(0.25);
        sprite.moveTo(box.getTranslateX(), box.getTranslateY());
    }

    public int height() {return height;}
    public int width() {return width;}

    public Sprite sprite() { return sprite;}

    public void add(Group group) { group.getChildren().addAll(sprite, box);}

    //the only objects you'd want to update are animated objects, e.g. anything that can fall, move around
    //this would include the player, enemies, cubes falling from the sky etc.
    void update(Map map){
        sprite.moveTo(box.getTranslateX(), box.getTranslateY());
        if(type == Type.BACKGROUND){
            box.setTranslateX(box.getTranslateX()+0.05);
            if(box.getTranslateX()>map.level().width()) box.setTranslateX(-500);
            return;
        }
        applyGravity();
        updateY(map);
        if(type == Type.PLAYER) sprite.update(movingRight, isMoving);
        if(type == Type.ITEM) sprite.update();
        isMoving = false;
    }
    void applyGravity() {
        if (this.Velocity.getY () < 10) {
            this.Velocity = this.Velocity.add(0, 1);
        }
    }

    void updateY (Map map) {
        move_Y((int)this.Velocity.getY(), map);
    }

    //returns true if the move is not blocked
    public boolean move_X(int value, Map map) {
        isMoving = true;
        movingRight = value > 0;
        for (Object object : map.level().platforms()) {
            //checks if player at same height as object and its solid first, then block left/right movements
            if(object.type == Type.SOLID
                    && box.getTranslateY()>object.box.getTranslateY()-this.height
            && box.getTranslateY()<object.box.getTranslateY()+this.height){
                if(movingRight && Math.abs(object.box.getTranslateX() - box.getTranslateX() - this.width) <= 5) return false;
                if(!movingRight && Math.abs(object.box.getTranslateX()+object.width()- box.getTranslateX()) <= 5) return false;
            }
        }
        for (int i=0; i<Math.abs(value); i++) {
            box.setTranslateX(box.getTranslateX() + (movingRight ? 1 : -1));
        }
        return true;
    }

    public void move_Y(int value, Map map) {
        movingDown = value > 0; // (Y=0) at the top of the frame
        for (int i=0; i<Math.abs(value); i++) {
            /* Check for collisions between player and platforms */
            for (Object object : map.level().platforms()) {
                if (box.getBoundsInParent().intersects(object.box.getBoundsInParent())) {
                    if (movingDown) {
                        if (box.getTranslateY () + this.height == object.box.getTranslateY ()) {
                            CanJump = true;
                            return;
                        }
                    } else { // Moving up
                        if (box.getTranslateY () == object.box.getTranslateY () + object.height() && object.type == Type.SOLID) {
                            return;
                        }
                    }
                }
            }
            /* Move ! Happens if no collision were detected */
            box.setTranslateY (box.getTranslateY () + (movingDown ? 1 : -1));
        }
    }


}
