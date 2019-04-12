package sample;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;

public class Map {

    private ArrayList<Item> blocks = new ArrayList<>();
    private Group mapRoot = new Group();
    private Grid level = new Grid();

    public void initialize() {
        //Adds every platform object within the level
        for (Object platform : level.platforms()) {
            showEntity(platform);
        }
        Item block = new Item("block", 300, 300);
        block.setCollisionBox(16, 16, Color.TURQUOISE);
        addItem(block);
    }

    public Group getMapRoot() {
        return mapRoot;
    }

    ArrayList<Item> blocks() {
        return this.blocks;
    }

    public Grid level() {
        return level;
    }

    public void addItem (Item item) {
        /* add Item to Map */
        if (!blocks.contains (item)) { blocks.add (item); }
        showEntity(item);
    }

    void removeItem (Item item) {
        hideEntity(item);
    }

    void showEntity(Object object) {
        mapRoot.getChildren().add(object);
    }

    void hideEntity (Object object) {
        mapRoot.getChildren().remove (object);
    }
}
