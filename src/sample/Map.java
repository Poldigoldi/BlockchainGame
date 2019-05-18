package sample;

import javafx.scene.Group;
import java.util.ArrayList;
import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public class Map {

    private ArrayList<Object> animatedObjects = new ArrayList<>();
    private ArrayList<Collectable> items = new ArrayList<>();
    private ArrayList<Enemy> enemies = new ArrayList<> ();
    private HacKing king;
    private Group mapRoot = new Group();
    private Grid level;
    private int currentLevel;
    private Object terminal, bigdoor;


    public Map(int level){
        if(level==1){
            initialiseLevel1();
        }
        if(level==2){
            initialiseLevel2();
        }
    }

    //ORDERING IS IMPORTANT!
    public void initialiseLevel1() {
        level = new Grid(1);
        currentLevel = 1;
        createLayer4Mountains();
        createLayer3Clouds();
        createLayer2Mountains();
        createLayer1Clouds();
        createDoodads();
        createEnemies();
        addAnimatedPlatforms();
        createCollectableObjects ();
        generalInitialiser();
    }

    public void initialiseLevel2(){
        level = new Grid(2);
        currentLevel = 2;
        createLayer3Clouds();
        generalInitialiser();
        addEnemy (3);
    }

    public void generalInitialiser(){
        for(Shape outline : level.outlines()) {
            mapRoot.getChildren().add(outline);
        }
        //Adds every platform object within the level
        for (Platform platform : level.platforms()) {
            showEntity(platform);
        }
        for (PlatformButton button : level.buttons()) {
            showEntity(button);
        }
    }

    public void addItem (Collectable item) {
        /* add Block to Map */
        items.add (item);
        animatedObjects.add(item);
        showEntity(item);
    }

    public void addPlayer(Object player, int startx, int starty){
        showEntity(player);
        player.setX(startx);
        player.setY(starty);
        animatedObjects.add(player);
    }

    public void removeBullet(Bullet bullet){
        bullet.setAlive(false);
        mapRoot.getChildren ().remove (bullet.label());
        hideEntity(bullet);
    }

    public void removePlayer(Object player){
        hideEntity(player);
    }

    public void showEntity(Object object) {
        object.add(mapRoot);
    }

    public void hideEntity(Object object) {
        mapRoot.getChildren ().remove (object.sprite);
        mapRoot.getChildren ().remove (object.box);
    }


    void addAnimatedObjects(Object... objects) {
        for(Object object: objects){
            showEntity(object);
            animatedObjects.add(object);
        }
    }

    //Enemies
    private void createEnemies () {
        addEnemy (0);
        addEnemy (1);
        addEnemy (2);
    }
    public void addEnemy (int typeId){
        Enemy enemy;
        switch (typeId){
            case 1:  // Enemy stay more at bottom of map - on left side
                enemy = new Enemy (200, 100, true, 800, 100);
                break;
            case 2: // Enemy stay more at top of map - anywhere
                enemy = new Enemy (600, 500, true, 800, 10);
                break;
            case 3:
                enemy = new HacKing (200, 500);
                king = (HacKing)enemy;
                break;
            default: // Enemy will be between bottom and middle - anywhere
                enemy = new Enemy (1300, 500, true, 800, 50);
                break;
        }
        addAnimatedObjects(enemy);
        enemies.add (enemy);
    }

    public void addRandomEnemy() {
        // randomise position and behaviour of enemy created
        int rand = new Random ().nextInt(3);
        addEnemy (rand);
    }

    private void createDoodads(){
        Object terminal = new Object(Type.DOODAD);
        terminal.setCollisionBox(15*64, 11*64, 50, 50, Color.YELLOW);
        terminal.sprite.loadDefaultImages(new Frame("/graphics/terminal1.png"),
                new Frame("/graphics/terminal2.png"),
                new Frame("/graphics/terminal3.png"));
        Object bigdoor = new Object(Type.DOODAD, new Frame("/graphics/bigdoor1.png"));
        bigdoor.setCollisionBox(20*64-19, 9*64+61, 50, 50, Color.YELLOW);
        bigdoor.sprite.offset(-40, -80);
        bigdoor.sprite.loadDefaultImages(new Frame("/graphics/bigdoor1.png", 100),
                new Frame("/graphics/bigdoor2.png"),
                new Frame("/graphics/bigdoor3.png"),
                new Frame("/graphics/bigdoor4.png"),
                new Frame("/graphics/bigdoor5.png"),
                new Frame("/graphics/bigdoor6.png"),
                new Frame("/graphics/bigdoor7.png"),
                new Frame("/graphics/bigdoor8.png"),
                new Frame("/graphics/bigdoor9.png"),
                new Frame("/graphics/bigdoor10.png"),
                new Frame("/graphics/bigdoor11.png", 9999));
        bigdoor.sprite.setanimation(false);
        addAnimatedObjects(terminal, bigdoor);
        this.terminal = terminal;
        this.bigdoor = bigdoor;
    }

    public boolean inRangeOfTerminal(double playerx, double playery){
        if(terminal == null) return false;
        double distance = Math.sqrt(Math.pow((playerx - terminal.box.getTranslateX()), 2) + Math.pow((playery - terminal.box.getTranslateY()), 2));
        if(distance < 60) return true;
        return false;
    }

    public boolean inRangeOfBigDoor(double playerx, double playery){
        if(bigdoor == null) return false;
        double distance = Math.sqrt(Math.pow((playerx - bigdoor.box.getTranslateX()), 2) + Math.pow((playery - bigdoor.box.getTranslateY()), 2));
        if(distance < 10) return true;
        return false;
    }

    public Object bigdoor(){ return bigdoor;}


    // Weapons & blocks to collect
    private void createCollectableObjects () {
        // Add block to pick-up
        Block block = new Block ("block");
        block.setCollisionBox(300, 300 , 16, 16, Color.DARKRED);
        addItem(block);

        // Add weapons to pick-up
        Weapon weapon1 = new Weapon ("CyberGun XS-4678", 100);
        addItem (weapon1);
    }

    private void addAnimatedPlatforms(){
      for(Platform platform : level.platforms()){
        if (platform.getColour() != null){
          animatedObjects.add(platform);
        }
      }
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

    /* ----------------- GETTERS & SETTERS --------------- */

    public Group mapRoot() {
        return mapRoot;
    }


    public Grid level() { return level; }


    public ArrayList<Object> animatedObjects() {
        return animatedObjects;
    }

    public ArrayList<Enemy> getEnemies () {
        return enemies;
    }
    public ArrayList<Collectable> getItems() {
        return items;
    }

    public Grid getLevel () {
        return this.level;
    }

    public void setEnemiesAlive (boolean state) {
        for (Enemy enemy : this.enemies) {
            enemy.setAlive (state);
        }
    }

    public void resetPlatforms(){
        for (Platform platform : level.platforms()) {
            if (platform.canDisappear() && !platform.isAlive()){
                platform.restoreCollisionBox();
                platform.setAlive(true);
                platform.setVisible(true);
            }
        }
    }

    public void resetButtons() {
      for (PlatformButton button : level.buttons()) {
        button.buttonUp();
      }
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setButton(String colour) {
        for(PlatformButton button : level.buttons()){
            if(!button.getColour().equals(colour)){
                button.buttonUp();
            }
            if(button.getColour().equals(colour)){
                button.buttonDown();
            }
        }
    }

    public HacKing getKing() {
        return king;
    }

    public void setKing(HacKing king) {
        this.king = king;
    }
}
