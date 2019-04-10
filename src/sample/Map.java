package sample;

import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;

public class Map {

    private ArrayList<Item> blocks = new ArrayList<>();
    private Pane gameRoot = new Pane();
    Grid level = new Grid();

    void initialize () {

        ArrayList<Node> platforms = this.level.getPlatforms ();
        for (int i = 0; i < level.getPlatforms ().size(); i++){
            showEntity (platforms.get (i));
        }

        Node ItemNode = createEntity (100, 400, 64, 64, Color.SADDLEBROWN);
        Item block = new Item ("block", ItemNode);
        addItem (block);
    }

    Pane getGameRoot() {
        return this.gameRoot;
    }

    ArrayList<Item> getBlocks () {
        return this.blocks;
    }

    Grid getLevel () {
        return this.level;
    }


    void addItem (Item item) {
        /* add Item to Map */
        if (!this.blocks.contains (item)) {
            this.blocks.add (item);
        }
        showEntity (item.getItem ());
    }

    void removeItem (Item item) {
        hideEntity (item.getItem ());
    }

    Node createEntity (int x, int y, int w, int h, Color color) {
        Rectangle entity = new Rectangle(w, h);
        entity.setTranslateX(x);
        entity.setTranslateY(y);
        entity.setFill(color);
        return entity;
    }

    void showEntity(Node node) {
        gameRoot.getChildren().add(node);
    }

    void hideEntity (Node node) {
        gameRoot.getChildren().remove (node);
    }
}
