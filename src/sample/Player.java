package sample;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Player {
    private String name;
    private Point2D Velocity;
    private boolean CanJump;
    private Luggage luggage;
    private Node Player;
    final int height;
    private Sprite sprite;

    Player(String name, int height) {
        this.name = name;
        this.Velocity = new Point2D(0,0);
        this.CanJump = true;
        this.luggage = new Luggage();
        this.height = height;
    }

    Node getPlayer() {
        return this.Player;
    }

    Sprite getSprite() { return this.sprite;  }

    Luggage getLuggage () {
        return this.luggage;
    }

    void move_X(int value) {
        Boolean movingRight = value > 0;
        for (int i=0; i<Math.abs(value); i++) {
            Player.setTranslateX(Player.getTranslateX() + (movingRight ? 1 : -1));
        }
    }

    void move_Y(int value, Map GameMap) {
        Boolean movingDown = value > 0; // (Y=0) at the top of the frame

        for (int i=0; i<Math.abs(value); i++) {

            /* Check for collisions between player and platforms */
            for (Node platform : GameMap.getLevel ().getPlatforms ()) {
                if (this.Player.getBoundsInParent ().intersects (platform.getBoundsInParent ())) {
                    int PlatformHeight = GameMap.getLevel ().getPlatformHeight ();

                    if (movingDown) {
                        if (this.Player.getTranslateY () + this.height == platform.getTranslateY ()) {
                            this.Player.setTranslateY (this.Player.getTranslateY () - 1);
                            CanJump = true;
                            return;
                        }
                    } else { /* Moving up */
                        if (this.Player.getTranslateY () == platform.getTranslateY () + PlatformHeight) {
                            return;
                        }
                    }
                }
            }
            /* Move ! Happens if no collision were detected */
            this.Player.setTranslateY (this.Player.getTranslateY () + (movingDown ? 1 : -1));
        }
    }

    void jump() {
        if (CanJump) {
            this.Velocity = this.Velocity.add (0, -30);
            CanJump=false;
        }
    }

    void applyGravity() {
        if (this.Velocity.getY () < 10) {
            this.Velocity = this.Velocity.add(0, 1);
        }
    }

    void updateY (Map GameMap) {
        move_Y((int)this.Velocity.getY(), GameMap);
    }

    void useItem(Item item) {
        /* Do Something with item .... */
    }

    Node createPlayer(Node entity) {
        entity.getProperties().put("velocity", Velocity);
        entity.getProperties().put("canJump", CanJump);
        entity.getProperties().put("alive", true);
        this.Player = entity;
        sprite = new Sprite(Player);
        sprite.loadDefaultImages(new Image("/defaultright.png"), new Image("/defaultleft.png"));
        sprite.loadRightMotionImages(new Image("/motionRight0.png"), new Image("/motionRight1.png"),
                new Image("/motionRight2.png"), new Image("/motionRight3.png"), new Image("/motionRight4.png"));
        sprite.loadLeftMotionImages(new Image("/motionLeft0.png"), new Image("/motionLeft1.png"),
                new Image("/motionLeft2.png"), new Image("/motionLeft3.png"), new Image("/motionLeft4.png"));
        return entity;
    }

}
