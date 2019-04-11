package sample;

import javafx.scene.layout.Pane;
import java.util.ArrayList;
import java.util.List;

public class Map {

    private ArrayList<Item> blocks = new ArrayList<>();
    private Pane gameRoot = new Pane();
    private Grid level = new Grid();

    public void initialize() {
        //Adds every platform object within the level
        for (Object platform : level.platforms()) {
            showEntity(platform);
        }
        Item block = new Item("block", 300, 300);
        addItem(block);
    }

    Pane getGameRoot() {
        return this.gameRoot;
    }

    ArrayList<Item> getBlocks () {
        return this.blocks;
    }

    Grid level() {
        return this.level;
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
        gameRoot.getChildren().add(object);
    }

    void hideEntity (Object object) {
        gameRoot.getChildren().remove (object);
    }
}
