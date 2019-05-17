package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Random;


public class Main extends Application {
    //music
    private String gameSong = "src/sound/song1.mp3";
    private javafx.scene.media.Media gameMedia = new javafx.scene.media.Media(new File(gameSong).toURI().toString());
    private MediaPlayer gameMediaPlayer = new MediaPlayer(gameMedia);

    private String menuSong = "src/sound/menuSong.mp3";
    private javafx.scene.media.Media menuMedia = new javafx.scene.media.Media(new File(menuSong).toURI().toString());
    private MediaPlayer menuMediaPlayer = new MediaPlayer(menuMedia);

    //sounds
    private AudioClip defeatSound = new AudioClip(Paths.get("src/sound/defeat.wav").toUri().toString());

    //global variables
    private final int WIDTH = 960 , HEIGHT = 640;
    private final int PLAYERSTARTX = 450, PLAYERSTARTY = 300;
    private final int PLAYER_START_LIVES = 4;
    private final int TIME_LIMIT = 60 * 20 * 1; // 20 seconds
    private Mode mode = Mode.STARTMENU;
    private int counter;
    private int timeCounter = 0;
    private boolean gameisOver;
    private boolean gameisMenu;
    private boolean gameisInstructions;
    private KeyPad keyPad = new KeyPad(WIDTH, HEIGHT);

    private Pane appRoot = new Pane();
    private Map map;
    private Scene mainScene;
    private GameOver gameOver = new GameOver(WIDTH, HEIGHT);
    private GameMenu gameMenu = new GameMenu(WIDTH, HEIGHT);
    private InstructionScreen instructionScreen = new InstructionScreen(WIDTH, HEIGHT);

    private HashMap<KeyCode, Boolean> keys = new HashMap<>();
    private Player player = new Player("Hero", PLAYERSTARTX, PLAYERSTARTY, PLAYER_START_LIVES);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        menuMediaPlayer.play();
        menuMediaPlayer.setVolume(20);
        //initialise Scene/game
        initContent(1);
        mainScene = new Scene(appRoot, WIDTH, HEIGHT);
        mainScene.setFill(Color.BLACK);
        mainScene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        mainScene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
        primaryStage.setScene(mainScene);
        mainScene.setRoot(gameMenu.returnRoot());
        gameisMenu = true;
        handleMenu();
        primaryStage.show();
        runGame(primaryStage);
    }

    private void initContent(int level) {
        //initialise background
        Image back1 = new Image("/graphics/background1.png", true);
        BackgroundImage background = new BackgroundImage(back1, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT , BackgroundSize.DEFAULT);
        appRoot.setBackground(new Background(background));

        //initialise map / contents of the appRoot (i.e. the player, platforms, blocks, etc.)
        map = new Map(level); //initialises level based on level number input
        appRoot.getChildren().addAll(map.mapRoot());
        //for anything that is animated, add them here, e.g. spinning blocks, player, clouds.
        map.addPlayer(player);
    }

    private void runGame(Stage stage) {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update(stage);
            }
        };
        timer.start();
    }

    //boolean keypressed stops people holding both left and right down at same time
    private void update(Stage stage) {
        if (gameMediaPlayer.getStatus() != MediaPlayer.Status.PLAYING && mode == Mode.PLATFORMGAME) {
            gameMediaPlayer.play();
        }

        if (mode == Mode.MINIGAME) {
            if (isPressed (KeyCode.ESCAPE)) {
                mode = Mode.PLATFORMGAME;
                mainScene.setRoot (appRoot);
            }
        }
        if (mode == Mode.STARTMENU) {
            if (gameMenu.isStartGame ()) {
                handleMenu ();
            }
            if (gameMenu.isInstructionsPressed ()) {
                handleInstructions ();
            }
        }

        if (mode == Mode.GAMEOVER) {
            gameOver();
        }

        if (mode == Mode.KEYPAD) {
            handleKeyPad();
        }

        if (keyPad.isCodeCorrect()) {
            changeLevel(2);
            keyPad.setCodeCorrect(false);
        }

        /*Opens keyPad if in the right position and action button is pressed "e"*/
        if (map.getCurrentLevel() == 1 && player.getX() == 1310 && player.getY() == 644 && isPressed(KeyCode.E)) {
            openKeyPad();
        }

        if (mode == Mode.PLATFORMGAME) {
            /*  Handles all the game events, including player motion and interaction with items  */
            if (isPressed (KeyCode.A)) {
                player.setFacingRight (false);
                if (player.move_X (-5, map))moveScreenX (-5);
            }
            else if (isPressed (KeyCode.D)){
                player.setFacingRight (true);
                if (player.move_X (5, map)) moveScreenX (5);
            }
            if (isPressed (KeyCode.W)) {
                player.jump ();
            }

            moveScreenY ();
            ListenerEnemies ();
            ListenerItemsEvent ();
            ListenerPlayerLives ();
            ListenerPlayerUseWeapon ();
            ListenerButtons();
            UpdateAnimatedObjects ();
            ListenerGameOver ();
            ListenerTimeBeforeNewEnemyWave ();
        }
    }

    /*Changes the level based on level number. New levels can be created in Grid class*/
    private void changeLevel(int level) {
        map.removePlayer(player);
        appRoot.getChildren().clear();
        initContent(level);
        player.setX(PLAYERSTARTX);
        player.setY(PLAYERSTARTY);
        map.mapRoot().setTranslateX(map.level().width()-player.getX() - WIDTH);
        map.mapRoot().setTranslateY(map.level().height()-player.getY() - HEIGHT);
    }

    /*Creates the keyPad and changes game mode*/
    private void openKeyPad() {
        keyPad = new KeyPad(WIDTH, HEIGHT);
        Group keyPadRoot = new Group();
        keyPad.initialise();
        keyPadRoot.getChildren().add(keyPad.getRoot());
        mainScene.setRoot(keyPadRoot);
        mode = Mode.KEYPAD;
    }

    /*Handles all button clicks on the keyPad*/
    private void handleKeyPad() {
        keyPad.getOne().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(Integer.toString(keyPad.getOne().getValue()))));
        keyPad.getTwo().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(Integer.toString(keyPad.getTwo().getValue()))));
        keyPad.getThree().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(Integer.toString(keyPad.getThree().getValue()))));
        keyPad.getFour().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(Integer.toString(keyPad.getFour().getValue()))));
        keyPad.getFive().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(Integer.toString(keyPad.getFive().getValue()))));
        keyPad.getSix().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(Integer.toString(keyPad.getSix().getValue()))));
        keyPad.getSeven().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(Integer.toString(keyPad.getSeven().getValue()))));
        keyPad.getEight().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(Integer.toString(keyPad.getEight().getValue()))));
        keyPad.getNine().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(Integer.toString(keyPad.getNine().getValue()))));
        keyPad.getZero().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(Integer.toString(keyPad.getZero().getValue()))));
        keyPad.getClear().setOnAction(event -> keyPad.setDisplayText(""));
        keyPad.getExit().setOnAction(event -> {
            mainScene.setRoot(appRoot);
            mode = Mode.PLATFORMGAME;
        });
        keyPad.getEnter().setOnAction(event -> {
            if (keyPad.getDisplayText().equals("1234")) {
                mainScene.setRoot(appRoot);
                keyPad.setCodeCorrect(true);
                mode = Mode.PLATFORMGAME;
            } else {
                keyPad.setDisplayText("WRONG CODE!");
            }
        });
    }

    //updates the screen X based on player position
    private void moveScreenX(int movement){
        if(player.getX()>WIDTH/2+8 && player.getX()<map.level().width()-WIDTH/2-32){
            counter++;
            //this moves everything in the screen with the character
            map.mapRoot().setTranslateX(map.level().width()-player.getX() - WIDTH);
            //this is for parallax scrolling of background elements
            if(counter == 5|| counter == 10){
                for(Object object: map.animatedObjects()) if(object.type==Type.LAYER2) object.setX(object.getX()-movement/5);
            }
            if(counter == 10){
                for(Object object: map.animatedObjects()) if(object.type==Type.LAYER4) object.setX(object.getX()-movement/5);
                counter = 0;
            }
        }
    }

    //updates the screen Y based on player position
    private void moveScreenY(){

        if(player.getY()>HEIGHT/2+40 && player.getY()<map.level().height()-HEIGHT/2-64){
            if((player.getY()>HEIGHT/2 - 70) && (player.getY()<HEIGHT/2 + 70)) {
            }
               else{ map.mapRoot().setTranslateY(map.level().height()-player.getY() - HEIGHT);}
            }
        }

    private void UpdateAnimatedObjects () {
        for (Object object : map.animatedObjects()) {
            object.update (map);
        }
    }

    /* ---------- PLAYER ----------- */

    private void ListenerItemsEvent () {
        for (Collectable item : map.getItems ()) {
            if ((this.player.box.getBoundsInParent()).intersects(item.box.getBoundsInParent())) {
                /* pickup item */
                if (item.isAlive() && isPressed (KeyCode.E)) {
                    player.getLuggage ().take (item);
                    map.hideEntity (item);
                    if (item.getItemType () == Type.BLOCK) {
                        miniGameKey();
                    }
                }
            }
        }
        /* drop item  TODO: see how to drop WEAPONS - do we still want to drop blocks ? */
        Block myBlock = player.getLuggage ().getblock ();
        if (myBlock != null) {
            if (! myBlock.isAlive ()) {
                if (isPressed (KeyCode.Z)) {
                    double new_X = player.getX();
                    double new_Y = player.getY() ;
                    player.getLuggage ().drop (myBlock, new_X, new_Y);
                    map.addItem(myBlock);
                }
            }
        }
    }

    private void ListenerPlayerLives () {
        int collision=0;
        for (Enemy enemy: map.getEnemies ()) {
            if (enemy.isAlive ()) {
                if ( player.box.getBoundsInParent ().intersects (enemy.box.getBoundsInParent ()) ) {
                    collision++;
                    // Waits that player moves out from the enemy to loose another life
                    if (player.isCanDie ()) {
                        player.LooseOneLive ();
                        player.setCanDie (false);
                    }
                }
            }
        }
        // if player not on any enemy
        if (collision == 0) {
            player.setCanDie (true);
        }
    }

    private void ListenerPlayerUseWeapon () {
        if (player.hasWeapon ()) {

            // If player allowed to use weapon and has bullets left
            if (isPressed (KeyCode.SPACE) && player.canUseWeapon ()) {
                Bullet bullet = new Bullet (player.getX () + 5, player.getY () + player.height/4, player.isFacingRight ());
                map.animatedObjects().add(bullet);
                map.addAnimatedObjects (bullet);
                player.getLuggage ().getWeapon ().looseOneBullet ();
                player.getLuggage ().getWeapon ().setCanShoot (false);
            }
            // User can only shoot 1 bullet when press key (un-press & press again to shoot)
            if (!isPressed (KeyCode.SPACE)) {
                player.getLuggage ().getWeapon ().setCanShoot (true);
            }

            // moves existing bullets
            for (Object object : map.animatedObjects()) {
                if (object instanceof Bullet && object.isAlive ()) {
                    ((Bullet) object).move (map);

                    // check if bullet collide with any enemy
                    // TODO: remove bullets & enemy who are dead from animatedObjects array
                    for (Enemy enemy : map.getEnemies ()) {
                        if (enemy.isAlive ()
                            && enemy.box.getBoundsInParent ().intersects (object.box.getBoundsInParent ()) ) {
                            enemy.setAlive (false);
                            object.setAlive (false);
                            map.hideEntity (enemy);
                            map.hideEntity (object);
                        }
                    }
                }
            }
        }
    }

    /* ----------- ENEMIES ------------ */

    private void ListenerEnemies () {
        for (Enemy enemy : map.getEnemies ()) {

            // check if enemy died
            if (isObjectOutOfBounds (enemy) ) {
                enemy.setAlive (false);
            }
            // if enemy alive - give motion
            if (enemy.isAlive () && enemy.getCanMove ()) {
                enemy.giveMotion (map);
            }
        }
    }
    // Sends a new wave of enemy to the game every so often
    // New enemies are sent 1 by 1 (every second)
    // will keep going until there is more 10 enemies on the map
    private void ListenerTimeBeforeNewEnemyWave () {
        timeCounter++;
        int WAVE_SIZE = 3;
        if (map.getEnemies ().size () < 10) {
            if (timeCounter >= TIME_LIMIT && timeCounter % 60 == 0) addRandomEnemy ();
            if (timeCounter > TIME_LIMIT + WAVE_SIZE * 60) timeCounter = 0;
        }
    }

    private void addRandomEnemy () {
        // randomise position and behaviour of enemy created
        int rand = new Random ().nextInt(3);
        map.addEnemy (rand);
        Enemy newEnemy = map.getEnemies ().get (map.getEnemies ().size () - 1); //retrieve last enemy added
        map.animatedObjects().add (newEnemy);
    }

    /* ----------- BUTTONS ------------ */
    private void ListenerButtons () {
        for (PlatformButton button : map.getLevel().buttons()) {
            if (!(this.player.box.getBoundsInParent()).intersects(button.box.getBoundsInParent())) {
                button.setPressed(false);
            }
            if (!button.isPressed() && (this.player.box.getBoundsInParent()).intersects(button.box.getBoundsInParent()) && player.movingDown && !player.isLanded) {
                    for(Platform platform : map.getLevel().platforms()) {
                        if(platform.canDisappear() && platform.isAlive() && platform.getColour().equals(button.getColour())) {
                            platform.setAlive(false);
                            platform.setCollisionBox(0, 0, 0, 0, Color.DARKORANGE);
                            map.setButton(button.getColour());
                    }
                        else if(platform.canDisappear() && !platform.isAlive() && !platform.getColour().equals(button.getColour())) {
                            platform.restoreCollisionBox();
                            platform.setAlive(true);
                            map.setButton(button.getColour());

                        }
                    }
                    button.setPressed(true);
            }
        }
    }

    /* ----------------- GAME OVER ------------------- */



    private boolean isObjectOutOfBounds(Object object) {
        if (object.getY() > map.level().height()
            || object.getX () >= map.level ().width() - map.level ().getOBJ_WIDTH ()
            || object.getX () < map.level ().getOBJ_WIDTH ()){
            return true;
        }
        return false;
    }

    private void ListenerGameOver() {
        if (player.getLives () > 0 && !isObjectOutOfBounds (player)) {
            return;
        }
        if(!gameisOver) { //This get called when you're playing and then you DIE!
            mainScene.setRoot(gameOver.returnRoot());
            defeatSound.play();
            mode = Mode.GAMEOVER;
        }
    }

    private void gameOver() {
        mainScene.setFill(Color.BLACK);
        map.setEnemiesAlive(false);
        gameisOver = true;

        if (gameOver.isStartAgain()) {
            mainScene.setRoot(appRoot);
            gameisOver = false;
            player.setX(PLAYERSTARTX);
            player.setY(PLAYERSTARTY);
            player.setLives(PLAYER_START_LIVES);
            map.setEnemiesAlive(true);
            map.resetPlatforms();
            map.resetButtons();
            keys.clear(); /**added to prevent input from previous game being called on reset**/
            mainScene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
            mainScene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
            map.mapRoot().setTranslateX(map.level().width() - player.getX() - WIDTH);
            map.mapRoot().setTranslateY(map.level().height() - player.getY() - HEIGHT);
            moveScreenY();
            gameOver.setStartAgain();
            mode = Mode.PLATFORMGAME;
        }
    }

    private void handleMenu() {
        if(!gameisMenu) {
            mainScene.setRoot(gameMenu.returnRoot());
            mainScene.setFill(Color.BLACK);
            gameisMenu = true;
        }
        if (gameMenu.isStartGame()) {
            gameisMenu=false;
            mode = Mode.PLATFORMGAME;
            menuMediaPlayer.stop();
            player.setX(PLAYERSTARTX);
            player.setY(PLAYERSTARTY);
            timeCounter = 0;
            map.mapRoot().setTranslateX(map.level().width()-player.getX() - WIDTH);
            map.mapRoot().setTranslateY(map.level().height()-player.getY() - HEIGHT);
            moveScreenY();
            gameMenu.setStartAgain();
            mainScene.setRoot(appRoot);
        }
    }

    private void handleInstructions() {
        if(!gameisInstructions) {
            mainScene.setRoot(instructionScreen.returnRoot());
            mainScene.setFill(Color.BLACK);
            gameisInstructions = true;
        }
        if (instructionScreen.isReturn_to_menu()) {
            gameMenu.resetInstructionPress();
            gameisInstructions=false;
            instructionScreen.setReturn_to_menu();
            mainScene.setRoot(gameMenu.returnRoot());
        }
    }

    private Boolean isPressed(KeyCode key) {
        return keys.getOrDefault (key, false);
    }
    /* ----------------- MINI GAME ------------------- */

    private void miniGameKey() {
        /* Mini games activated once player collects a block on the map */
        try {
            KeyGame mini = new KeyGame ();

            AnchorPane game = mini.returnRoot ();
            mainScene.setRoot (game);
            mainScene.getRoot ().requestFocus ();
            mode = Mode.MINIGAME;

        } catch (Exception e) {
            e.printStackTrace ();

        }
    }
}

