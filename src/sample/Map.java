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
        createLayer4Mountains();
        createLayer3Clouds();
        createLayer2Mountains();
        createLayer1Clouds();
        createEnemies();
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

    //Enemies
    private void createEnemies(){
        EnemyType1 enemy1 = new EnemyType1(400, 200);
        addAnimatedObjects(enemy1);
    }


    //Nick's functions for making the background graphics

    private void createLayer3Clouds(){
        Object cloud = new Object(Type.LAYER3, new Frame("/graphics/clouds1.png"));
        cloud.setCollisionBox(0,0, 5, 5, Color.CORAL);
        Object cloud2 = new Object(Type.LAYER3, new Frame("/graphics/clouds2.png"));
        cloud2.setCollisionBox(400,200, 5, 5, Color.CORAL);
        Object cloud3 = new Object(Type.LAYER3, new Frame("/graphics/clouds3.png"));
        cloud3.setCollisionBox(-300,250, 5, 5, Color.CORAL);
        addAnimatedObjects(cloud, cloud2, cloud3);
    }


    private void createLayer1Clouds(){
        Frame[] L2Clouds = {
                new Frame("/graphics/smallcloud1.png"),    new Frame("/graphics/smallcloud2.png"),
                new Frame("/graphics/smallcloud3.png"),    new Frame("/graphics/smallcloud4.png"),
                new Frame("/graphics/smallcloud5.png"),    new Frame("/graphics/smallcloud6.png"),
                new Frame("/graphics/smallcloud7.png"),    new Frame("/graphics/smallcloud8.png"),
                new Frame("/graphics/smallcloud9.png"),    new Frame("/graphics/smallcloud10.png"),
                new Frame("/graphics/smallcloud11.png")};
        for(Frame cloudImage: L2Clouds){
            Object cloud = new Object(Type.LAYER1, cloudImage);
            cloud.setCollisionBox(random(0 ,level.width()),random(0, (level.height()/2)), 5, 5, Color.YELLOWGREEN);
            addAnimatedObjects(cloud);
        }
    }

    private void createLayer2Mountains(){
        Object mountain = new Object(Type.LAYER2, new Frame("/graphics/mountain1.png"));
        mountain.setCollisionBox(0, level.height()-mountain.sprite().getImage().getHeight(), 5, 5, Color.SILVER);
        Object mountain2 = new Object(Type.LAYER2, new Frame("/graphics/mountain2.png"));
        mountain2.setCollisionBox(700, level.height()-600, 5, 5, Color.SILVER);
        Object mountain3 = new Object(Type.LAYER2, new Frame("/graphics/mountain3.png"));
        mountain3.setCollisionBox(1000, level.height()-400, 5, 5, Color.SILVER);
        addAnimatedObjects(mountain, mountain2, mountain3);
    }

    private void createLayer4Mountains(){
        Object mountain = new Object(Type.LAYER4, new Frame("/graphics/distant1.png"));
        mountain.setCollisionBox(200, level.height()-300, 5, 5, Color.SILVER);
        Object mountain2 = new Object(Type.LAYER4, new Frame("/graphics/distant2.png"));
        mountain2.setCollisionBox(850, 450, 5, 5, Color.SILVER);
        Object mountain3 = new Object(Type.LAYER4, new Frame("/graphics/distant2.png"));
        mountain3.setCollisionBox(100, 300, 5, 5, Color.SILVER);
        addAnimatedObjects(mountain, mountain2, mountain3);
    }



    public static double random(double min, double max){ return (Math.random()*((max-min)+1))+min; }
}
