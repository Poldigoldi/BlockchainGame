package sample;

import javafx.scene.Group;
import java.util.ArrayList;
import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public class Map {

    private ArrayList<Object> animatedObjects = new ArrayList<>();
    private ArrayList<Collectable> items = new ArrayList<>();
    private ArrayList<AttackBot> attackbots = new ArrayList<> ();
    private ArrayList<Person> enemies = new ArrayList<> ();
    private HacKing king;
    private Group mapRoot = new Group();
    private Grid level;
    private int currentLevel;
    private Object terminal, bigdoor;
    private ArrayList<HelpPopUp> helpers = new ArrayList<>();
    private int WIDTH, HEIGHT;
    private ArrayList<PlatformButton> buttons = new ArrayList<>();

    public Map(int level, int WIDTH, int HEIGTH){
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        if(level==1){
            initialiseLevel1();
        }
        if(level==2){
            initialiseLevel2();
        }
        if (level==3) {
            initialiseLevel3();
        }
    }

    private void addToGrid(){
        for(int y = 0; y< level.height; y++){
            for(int x = 0; x < level.width; x++){
                if(level.map()[y].charAt(x) == 'T') addTerminal(x, y);
                if(level.map()[y].charAt(x) == 'B') addBigDoor(x, y);
                if(level.map()[y].charAt(x) == 'I') addBlock(x, y);
                if(level.map()[y].charAt(x) == 'W') addWeapon(x, y);
                if(level.map()[y].charAt(x) == 'o') addButton(x, y, level.map()[y].charAt(x));
                if(level.map()[y].charAt(x) == 'r') addButton(x, y, level.map()[y].charAt(x));
                if(level.map()[y].charAt(x) == 'H') addLife(x, y);
                if(level.map()[y].charAt(x) == '.') addGrass(x, y);
                if(level.map()[y].charAt(x) == '#') addAttackBot(x, y);
            }
        }
    }

    //ORDERING IS IMPORTANT!
    private void initialiseLevel1() {
        level = new Grid(1);
        currentLevel = 1;
        createLayer3Clouds();
        createLayer1Clouds();
        /*
        createLayer4Mountains();

        createLayer2Mountains();

        */
        createEnemies();
        addAnimatedPlatforms();
        generalInitialiser();
        addToGrid();
        addHelper(16, 15, "Jump on the Button to make platforms disappear!", false);
        addHelper(0, 15, "Welcome to our world! Come find me dotted around the map for hints and tips.\n" +
                "Use 'A','W',D' to move", true);
        addHelper(22, 4, "Press E to interact with the terminal!\nHave you found the password yet?", false);
        addHelper(0,4, "Walk into the block to start the Mini Game. \n" +
                "Complete it and you'll find a key password.", true);
    }

    private void initialiseLevel2(){
        level = new Grid(2);
        currentLevel = 2;
        createLayer3Clouds();
        addAnimatedPlatforms();
        generalInitialiser();
        addToGrid ();
        addEnemy (3);
        addHelper(0,9,"ARGH! An evil HacKing is trying to steal your key!\n" +
                "You need to defeat him to get to the next level.", true);
    }

    private void initialiseLevel3() {
        level = new Grid(3);
        currentLevel = 3;
        createLayer3Clouds();
        addAnimatedPlatforms();
        generalInitialiser();
        addToGrid ();
    }

    private void generalInitialiser(){
        for(Shape outline : level.outlines()) {
            mapRoot.getChildren().add(outline);
        }
        //Adds every platform object within the level
        for (Platform platform : level.platforms()) {
            showEntity(platform);
        }

    }

    void addGrass(int x, int y){
            Object grass = new Object(Type.ABSTRACT, new Frame("/graphics/grass1.png"));
            grass.setCollisionBox(x * 64, y * 64, 0, 0, Color.TRANSPARENT);
            showEntity(grass);
            level.bringtofront().add(grass);
        }

    void addItem(Collectable item) {
        /* add Block to Map */
        items.add (item);
        animatedObjects.add(item);
        showEntity(item);
    }

    void addPlayer(Object player, int startx, int starty){
        showEntity(player);
        player.setX(startx);
        player.setY(starty);
        animatedObjects.add(player);
    }

    void removeBullet(Bullet bullet){
        bullet.setAlive(false);
        mapRoot.getChildren ().remove (bullet.label());
        hideEntity(bullet);
    }

    void removePlayer(Object player){
        hideEntity(player);
    }

    private void showEntity(Object object) {
        object.add(mapRoot);
    }

    void hideEntity(Object object) {
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
        Person enemy;
        switch (typeId){
            case 1:  // Enemy stay more at bottom of map - on left side
                enemy = new Enemy (200, 100, true, 800, 100);
                break;
            case 2: // Enemy stay more at top of map - anywhere
                enemy = new Enemy (600, 500, true, 800, 10);
                break;
            case 3:
                enemy = new HacKing (700, 350);
                this.king = (HacKing) enemy;
                break;
            default: // Enemy will be between bottom and middle - anywhere
                enemy = new Enemy (1300, 500, true, 800, 50);
                break;
        }
        addAnimatedObjects(enemy);
        enemies.add (enemy);
    }

    void addRandomEnemy() {
        // randomise position and behaviour of enemy created
        int rand = new Random ().nextInt(3);
        addEnemy (rand);
    }

    public void addAttackBot(int x, int y){
        AttackBot attackBot = new AttackBot(Type.ATTACKBOT, new Frame("/graphics/attackbot1.png"));
        attackBot.setCollisionBox(x*64, y*64, 50, 50, Color.PURPLE);
        attackBot.sprite.toFront();
        addAnimatedObjects(attackBot);
        mapRoot.getChildren().add(attackBot.laser());
        attackbots.add(attackBot);
    }

    private void addHelper(int x, int y, String string, Boolean faceRight){
        HelpPopUp popup = new HelpPopUp(x, y, string, faceRight);
        helpers.add(popup);
        addAnimatedObjects(popup.helper());
    }

    private void addTerminal(int x, int y){
        Object terminal = new Object(Type.DOODAD);
        terminal.setCollisionBox(x*64, y*64, 50, 50, Color.YELLOW);
        terminal.sprite.loadDefaultImages(new Frame("/graphics/terminal1.png"),
                new Frame("/graphics/terminal2.png"),
                new Frame("/graphics/terminal3.png"));
        addAnimatedObjects(terminal);
        this.terminal = terminal;
    }

    private void addBigDoor(int x, int y){
        Object bigdoor = new Object(Type.DOODAD, new Frame("/graphics/bigdoor1.png"));
        bigdoor.setCollisionBox(x*64, y*64, 50, 50, Color.YELLOW);
        bigdoor.sprite.offset(-45, -85);
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
        addAnimatedObjects(bigdoor);
        this.bigdoor = bigdoor;
    }

    private void addBlock(int x, int y){
        Block block = new Block ("block");
        block.setCollisionBox(x*64, y*64 , 16, 16, Color.DARKRED);
        addItem(block);
    }

    private void addWeapon (int x, int y) {
        Weapon weapon1 = new Weapon ("CyberGun XS-4678", 10000000);
        weapon1.setCollisionBox (x*64, y*64, 25, 25, Color.BLUEVIOLET);
        addItem (weapon1);
    }

    private void addButton (int x, int y, char value) {
        if (value == 'o') {
            PlatformButton button = new PlatformButton(Type.PLATFORMBUTTON, "orange", new Frame("/graphics/buttonUp.png"), new Frame("/graphics/buttonDown.png"));
            button.setCollisionBox(x * 64 + 11, y * 64 + 40, 40, 5, Color.ORANGE);
            buttons.add(button);
            showEntity(button);
        }
        if (value == 'r') {
            PlatformButton button = new PlatformButton(Type.PLATFORMBUTTON, "red", new Frame("/graphics/buttonUpRed.png"), new Frame("/graphics/buttonDownRed.png"));
            button.setCollisionBox(x * 64 + 11, y * 64 + 40, 40, 5, Color.RED);
            buttons.add(button);
            showEntity(button);
        }
    }
    private void addLife (int x, int y) {
        Life life = new Life ("life");
        life.setCollisionBox(x*64, y*64 , 16, 16, Color.DARKRED);
        addItem(life);
    }














    //RANGE

    boolean inRangeOfTerminal(double playerx, double playery){
        if(terminal == null) return false;
        double distance = Math.sqrt(Math.pow((playerx - terminal.box.getTranslateX()), 2) + Math.pow((playery - terminal.box.getTranslateY()), 2));
        if(distance < 60) return true;
        return false;
    }

    boolean inRangeOfBigDoor(double playerx, double playery){
        if(bigdoor == null) return false;
        double distance = Math.sqrt(Math.pow((playerx - bigdoor.box.getTranslateX()), 2) + Math.pow((playery - bigdoor.box.getTranslateY()), 2));
        if(distance < 10) return true;
        return false;
    }


    private void addAnimatedPlatforms(){
      for(Platform platform : level.platforms()){
        if (platform.getColour() != null || platform.isMoving || platform.isTimed()){
          animatedObjects.add(platform);
        }
      }
    }

//Nick's functions for making the background graphics

    private void createLayer3Clouds(){
        Object cloud = new Object(Type.LAYER3, new Frame("/graphics/clouds1.png"));
        cloud.setCollisionBox(random(0 ,level.width()),random(0, (level.height()*0.8)), 5, 5, Color.CORAL);
        Object cloud2 = new Object(Type.LAYER3, new Frame("/graphics/clouds2.png"));
        cloud2.setCollisionBox(random(0 ,level.width()),random(0, (level.height()*0.8)), 5, 5, Color.CORAL);
        Object cloud3 = new Object(Type.LAYER3, new Frame("/graphics/clouds3.png"));
        cloud3.setCollisionBox(random(0 ,level.width()),random(0, (level.height()*0.8)), 5, 5, Color.CORAL);
        addAnimatedObjects(cloud, cloud2, cloud3);
    }


    private void createLayer1Clouds(){
        Frame[] L2Clouds = {
                new Frame("/graphics/smallcloud1.png"),    new Frame("/graphics/smallcloud2.png"),
                new Frame("/graphics/smallcloud3.png"),    new Frame("/graphics/smallcloud4.png"),
                new Frame("/graphics/smallcloud5.png"),    new Frame("/graphics/smallcloud6.png"),
                new Frame("/graphics/smallcloud7.png"),    new Frame("/graphics/smallcloud8.png"),
                new Frame("/graphics/smallcloud9.png"),    new Frame("/graphics/smallcloud10.png"),
                new Frame("/graphics/smallcloud3.png"),    new Frame("/graphics/smallcloud4.png"),
                new Frame("/graphics/smallcloud11.png")};
        for(Frame cloudImage: L2Clouds){
            Object cloud = new Object(Type.LAYER1, cloudImage);
            cloud.setCollisionBox(random(0 ,level.width()),random(0, (level.height()*0.8)), 5, 5, Color.YELLOWGREEN);
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

    Group mapRoot() {
        return mapRoot;
    }

    Grid level() { return level; }

    Object bigdoor(){ return bigdoor;}

    ArrayList<HelpPopUp> helpers(){ return helpers;}

    public ArrayList<AttackBot> attackbots(){return attackbots;}

    public ArrayList<Object> animatedObjects() {
        return animatedObjects;
    }

    public ArrayList<Person> getEnemies () {
        return enemies;
    }

    ArrayList<Collectable> getItems() {
        return items;
    }

    ArrayList<PlatformButton> buttons() {
        return this.buttons;
    }

    Grid getLevel() {
        return this.level;
    }

    int getCurrentLevel() {
        return currentLevel;
    }

    public void setButton(String colour) {
        for(PlatformButton button : buttons){
            if(!button.getColour().equals(colour)){
                button.buttonUp();
            }
            if(button.getColour().equals(colour)){
                button.buttonDown();
            }
        }
    }

    HacKing getKing() {
        return king;
    }

    public void setKing(HacKing king) {
        this.king = king;
    }
    /*** SCREEN MOVEMENT
     *
     *
     */

    //updates the screen X based on player position
    private int counter;
    void moveScreenX(int movement, Object player) {
        if (player.getX() > WIDTH/2 + 5 && player.getX() < level().width() - WIDTH / 2) {
            mapRoot().setTranslateX(-player.getX()+0.5*WIDTH);
            counter++;
            //this moves everything in the screen with the character

            //this is for parallax scrolling of background elements
            if (counter == 5 || counter == 10) {
                for (Object object : animatedObjects)
                    if (object.type == Type.LAYER2) object.setX(object.getX() - 0.2*movement);
            }
            if (counter == 10) {
                for (Object object : animatedObjects)
                    if (object.type == Type.LAYER4) object.setX(object.getX() - 0.2*movement);
                counter = 0;
            }
        }
    }
}
