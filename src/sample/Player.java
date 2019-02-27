package sample;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Player {
    private String name;
    private Point2D Velocity;
    private boolean CanJump;
    private Luggage luggage;
    private Node Player;


    Player(String name) {
        this.name = name;
        this.Velocity = new Point2D(0,0);
        this.CanJump = true;
        this.luggage = new Luggage();
    }

    Node getPlayer() {
        return this.Player;
    }
    Luggage getLuggage () {
        return this.luggage;
    }

    void move_X(int value) {
        Boolean movingRight = value > 0;

        for (int i=0; i<Math.abs(value); i++) {
            Player.setTranslateX(Player.getTranslateX() + (movingRight ? 1 : -1));
        }
        // TODO: consider collisions with map elements
    }

    void move_Y(int value) {
        Boolean movingDown = value > 0; // (Y=0) at the top of the frame

        for (int i=0; i<Math.abs(value); i++) {
            if (movingDown && Player.getTranslateY () < 620) {
                Player.setTranslateY (Player.getTranslateY () + 1);
            }
            if (!movingDown && Player.getTranslateY () > 40) {
                Player.setTranslateY (Player.getTranslateY () - 1);
            }
            if (Player.getTranslateY () == 620) {
                CanJump = true;
            }
        }
        /* TODO 1) consider collisions with map elements
           TODO 2) Change the way CanJump=true, so that allowed when collide with a platform
        */
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

    void updateY () {
        move_Y((int)this.Velocity.getY());
    }

    void useItem(Item item) {

    }

    Node createPlayer(Node entity) {
        entity.getProperties().put("velocity", Velocity);
        entity.getProperties().put("canJump", CanJump);
        entity.getProperties().put("alive", true);
        this.Player = entity;
        return entity;
    }
}
