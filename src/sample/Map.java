package sample;

import javafx.scene.Group;
import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

public class Map {

    private ArrayList<Item> items = new ArrayList<>();
    private ArrayList<Object> animatedObjects = new ArrayList<>();
    private Group mapRoot = new Group();
    private Grid level = new Grid();

    public void initialize() {
        createClouds();
        //Adds every platform object within the level
        for (Object platform : level.platforms()) {
            showEntity(platform);
        }
        Item block = new Item("block");
        block.setCollisionBox(300, 300 , 16, 16, Color.DARKRED);
        addItem(block);
    }

    public Group mapRoot() {
        return mapRoot;
    }

    ArrayList<Item> blocks() { return this.items; }

    ArrayList<Object> objects() { return this.animatedObjects; }

    public Grid level() { return level; }

    public void addItem (Item item) {
        /* add Item to Map */
        if (!items.contains (item)) { items.add (item); }
        showEntity(item);
    }

    void removeItem (Item item) { hideEntity(item); }

    void showEntity(Object object) { object.add(mapRoot); }

    void hideEntity (Object object) { mapRoot.getChildren().remove (object); }

    void addAnimatedObjects(Object... objects) {
        for(Object object: objects){
            showEntity(object);
            animatedObjects.add(object);
        }
    }

    //Nick's functions

    private void createClouds(){
        Object cloud = new Object(Type.BACKGROUND, new Image("/clouds1.png"));
        cloud.setCollisionBox(0,0, 5, 5, Color.CORAL);
        Object cloud2 = new Object(Type.BACKGROUND, new Image("/clouds2.png"));
        cloud2.setCollisionBox(400,200, 5, 5, Color.CORAL);
        Object cloud3 = new Object(Type.BACKGROUND, new Image("/clouds3.png"));
        cloud3.setCollisionBox(-300,250, 5, 5, Color.CORAL);
        addAnimatedObjects(cloud, cloud2, cloud3);
    }
}
