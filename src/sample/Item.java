package sample;

import javafx.scene.Node;

public class Item {
    final String name;
    private Node item;
    private Boolean alive;

    Item(String name, Node entity) {

        this.name = name;
        this.alive = true;
        this.item = entity;
    }

    Node getItem () {
        return this.item;
    }

    void drop (double x, double y) {
        this.item.setTranslateX (x);
        this.item.setTranslateY (y);
    }

    Boolean isAlive() {
        return this.alive;
    }

    void setAlive(Boolean value) {
        this.alive = value;
    }
}
