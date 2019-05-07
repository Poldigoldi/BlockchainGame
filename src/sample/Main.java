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
import java.util.ArrayList;
import java.util.List;


public class Main extends Application {
    //music
    String musicFile = "src/sound/song1.mp3";
    javafx.scene.media.Media musicMedia = new javafx.scene.media.Media(new File(musicFile).toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(musicMedia);
    //sounds
    AudioClip defeatSound = new AudioClip(Paths.get("src/sound/defeat.wav").toUri().toString());
    AudioClip menuSound = new AudioClip(Paths.get("src/sound/menuSong.mp3").toUri().toString());
    AudioClip instructionSound = new AudioClip(Paths.get("src/sound/instructionSong.mp3").toUri().toString());

    //global variables
    private final int WIDTH = 960 , HEIGHT = 640;
    private final int PLAYERSTARTX = 450, PLAYERSTARTY = 300;
    private final int PLAYER_START_LIVES = 2;
    private Mode mode = Mode.STARTMENU;
    private int counter;
    private boolean gameisOver;
    private boolean gameisMenu;
    private boolean gameisInstructions;
    private static boolean firstCall = true;

    private Pane appRoot = new Pane();
    private Map map = new Map();
    private Player player;
    private Scene mainScene;
    private GameOver gameOver = new GameOver(WIDTH, HEIGHT);
    private GameMenu gameMenu = new GameMenu(WIDTH, HEIGHT);
    private InstructionScreen instructionScreen = new InstructionScreen(WIDTH, HEIGHT);

    private HashMap<KeyCode, Boolean> keys = new HashMap<>();
    private List<Object> animatedObjects = new ArrayList<>();


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        mediaPlayer.play();
        mediaPlayer.setVolume(0);

        //initialise player, the 'player' is the collision box of the playerInstance
        player = new Player("Hero", PLAYERSTARTX, PLAYERSTARTY, primaryStage, PLAYER_START_LIVES);
        player.initialise();

        //initialise Scene/game
        initContent();
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

    private void initContent() {
        //initialise background
        Image back1 = new Image("/graphics/background1.png", true);
        BackgroundImage background = new BackgroundImage(back1, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT , BackgroundSize.DEFAULT);
        appRoot.setBackground(new Background(background));

        //initialise map / contents of the appRoot (i.e. the player, platforms, blocks, etc.)
        map.initialize ();
        map.showEntity(player);
        appRoot.getChildren().addAll(map.mapRoot());

        //for anything that is animated, add them here, e.g. spinning blocks, player, clouds.
        animatedObjects.add(player);
        for(Object block: map.blocks()) animatedObjects.add(block);
        for(Object object: map.objects()) animatedObjects.add(object);
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
        boolean keypressed = false;

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

        if (mode == Mode.PLATFORMGAME) {

            /*  Handles all the game events, including player motion and interaction with items  */
            if (isPressed (KeyCode.LEFT)) {
                keypressed = true;
                if (player.move_X (-5, map)) moveScreenX (-5);
            }
            if (isPressed (KeyCode.RIGHT) && keypressed == false) {
                if (player.move_X (5, map)) moveScreenX (5);
            }
            if (isPressed (KeyCode.SPACE)) {
                player.jump ();
            }

            moveScreenY ();
            ListenerEnemies ();
            ListenerItemsEvent ();
            ListenerPlayerLives ();

            for (Object object : animatedObjects) {
                object.update (map);
            }

            if (player.getLives () == 0 || isObjectOutOfBounds (player)) {
                handleGameOver ();
            }
        }
    }

    //updates the screen X based on player position
    private void moveScreenX(int movement){
        if(player.getX()>WIDTH/2+8 && player.getX()<map.level().width()-WIDTH/2-32){
            counter++;
            //this moves everything in the screen with the character
            map.mapRoot().setTranslateX(map.level().width()-player.getX() - WIDTH);
            //this is for parallax scrolling of background elements
            if(counter == 5|| counter == 10){
                for(Object object: animatedObjects) if(object.type==Type.LAYER2) object.setX(object.getX()-movement/5);
            }
            if(counter == 10){
                for(Object object: animatedObjects) if(object.type==Type.LAYER4) object.setX(object.getX()-movement/5);
                counter = 0;
            }
        }
    }

    //updates the screen Y based on player position
    private void moveScreenY(){
        if(player.getY()>HEIGHT/2+40 && player.getY()<map.level().height()-HEIGHT/2-64){
            map.mapRoot().setTranslateY(map.level().height()-player.getY() - HEIGHT);
        }

    }

    /* ---------- PLAYER ----------- */


    private void ListenerItemsEvent () {
        for (Item block : map.blocks()) {
            if ((this.player.box.getBoundsInParent()).intersects(block.box.getBoundsInParent())) {
                /* pickup item */
                if (block.isAlive() && isPressed (KeyCode.A)) {
                    player.getLuggage ().take (block);
                    map.hideEntity (block);
                    miniGameKey();
                }
            }
        }
        /* drop item */
        Item myBlock = player.getLuggage ().getblock ();
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
        for (EnemyType1 enemy: map.getEnemies ()) {
            if ( player.box.getBoundsInParent ().intersects (enemy.box.getBoundsInParent ()) ) {
                collision++;
                // Waits that player moves out from the enemy to loose another life
                if (player.isCanDie ()) {
                    System.out.println ("PLAYER LOST A LIFE");
                    player.LooseOneLive ();
                    player.setCanDie (false);
                }
            }
        }
        // if player not on any enemy
        if (collision == 0) {
            player.setCanDie (true);
        }
    }
    private boolean PlayerKillEnemy (EnemyType1 enemy) {
        /*
        if ( player.box.getBoundsInParent ().intersects (enemy.box.getBoundsInParent ()) ) {
            return true;
        }*/
        return false;
    }

    /* ----------- ENEMIES ------------ */


    private void ListenerEnemies () {
        for (EnemyType1 enemy : map.getEnemies ()) {

            // check if enemy died
            if (PlayerKillEnemy (enemy) || isObjectOutOfBounds (enemy) ) {
                enemy.setAlive (false);
                map.hideEntity (enemy);
            }
            // if enemy alive - give motion
            if (enemy.isAlive () && enemy.getCanMove ()) {
                enemy.giveMotion (map);
            }
        }
    }

    /* ----------------- GAME OVER ------------------- */

    private boolean isObjectOutOfBounds(Object object) {
        if (object.getY() > map.level().height()){
            return true;
        }
        return false;
    }

    private void handleGameOver() {
        if(!gameisOver) {
            mainScene.setRoot(gameOver.returnRoot());
            mainScene.setFill(Color.BLACK);
            defeatSound.play();
            mediaPlayer.stop();
            map.setEnemiesAlive (false);
            gameisOver = true;
        }
        if (gameOver.isStartAgain()) {
            gameisOver=false;
            mediaPlayer.play();
            player.setX(PLAYERSTARTX);
            player.setY(PLAYERSTARTY);
            player.setLives (PLAYER_START_LIVES);
            map.setEnemiesAlive (true);
            map.mapRoot().setTranslateX(map.level().width()-player.getX() - WIDTH);
            map.mapRoot().setTranslateY(map.level().height()-player.getY() - HEIGHT);
            moveScreenY();
            gameOver.setStartAgain();
            mainScene.setRoot(appRoot);
        }
    }

    private void handleMenu() {
        if(!gameisMenu) {
            mainScene.setRoot(gameMenu.returnRoot());
            mainScene.setFill(Color.BLACK);
            menuSound.play();
            mediaPlayer.stop();
            gameisMenu = true;
        }
        if (gameMenu.isStartGame()) {
            gameisMenu=false;
            mode = Mode.PLATFORMGAME;
            menuSound.stop();
            mediaPlayer.play();
            player.setX(PLAYERSTARTX);
            player.setY(PLAYERSTARTY);
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
            instructionSound.play();
            mediaPlayer.stop();
            gameisInstructions = true;
        }
        if (instructionScreen.isReturn_to_menu()) {
            gameMenu.resetInstructionPress();
            instructionSound.stop();
            gameisInstructions=false;
            mediaPlayer.play();
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
        KeyGame mini = new KeyGame();
        Group game = mini.returnRoot();
        mainScene.setRoot(game);
        mode = Mode.MINIGAME;
        // mainScene.setRoot(appRoot);
    }



    /* --------------- GETTERS AND SETTERS --------------------- */

    public void setMode(Mode mode) {
        this.mode = mode;
    }

}

