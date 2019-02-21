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


    public Player(String name) {
        this.name = name;
        this.Velocity = new Point2D(0,0);
        this.CanJump = true;
        this.luggage = new Luggage();
    }

    void move_X(int value) {

    }

    void move_Y(int value) {

    }

    void jump() {

    }

    void useItem(Item item) {

    }

    Node createPlayer(int x, int y, int w) {
        Rectangle entity = new Rectangle(w, w);
        entity.setTranslateX(x);
        entity.setTranslateY(y);
        entity.setFill(Color.RED);
        entity.getProperties().put("velocity", Velocity);
        entity.getProperties().put("canJump", CanJump);
        entity.getProperties().put("alive", true);

        return entity;
    }

}
