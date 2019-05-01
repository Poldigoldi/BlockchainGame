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

    //global variables
    private int WIDTH = 960 , HEIGHT = 640;
    private int PLAYERSTARTX = 450, PLAYERSTARTY = 300;
    private double playerY = PLAYERSTARTY;
    private Mode mode = Mode.PLATFORMGAME;
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
        mediaPlayer.setVolume(20);

        //initialise player, the 'player' is the collision box of the playerInstance
        player = new Player("Come", PLAYERSTARTX, PLAYERSTARTY, primaryStage);
        player.initialise();

        //initialise Scene/game
        initContent();
        mainScene = new Scene(appRoot, WIDTH, HEIGHT);
        mainScene.setFill(Color.BLACK);
        mainScene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        mainScene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
        primaryStage.setScene(mainScene);
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
        if(mode == Mode.PLATFORMGAME) {
            /*  Handles all the game events, including player motion and interaction with items  */
            if (isPressed(KeyCode.LEFT)) {
                keypressed = true;
                if (player.move_X(-5, map)) moveScreenX(-5);
            }
            if (isPressed(KeyCode.RIGHT) && keypressed == false) {
                if (player.move_X(5, map)) moveScreenX(5);
            }
            if (isPressed(KeyCode.SPACE)) {
                player.jump();
            }
            if (isPressed(KeyCode.ESCAPE)) {
                mainScene.setRoot(appRoot);
            }
            moveScreenY();
            for (Object object : animatedObjects) {
                object.update(map);
            }
            handleItems();
            if (isPlayerOutOfBounds()) { handleGameOver(); }
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

    /* ---------- Sub methods ----------- */

    private void handleItems () {
        for (Item block : map.blocks()) {
            if ((this.player.box.getBoundsInParent()).intersects(block.box.getBoundsInParent())) {
                /* pickup item */
                if (block.isAlive() && isPressed (KeyCode.A)) {
                    player.getLuggage ().take (block);
                    map.removeItem (block);
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

    private void miniGameKey() {
        /* Mini games activated once player collects a block on the map */
        try {
            KeyGame mini = new KeyGame();

            AnchorPane game = mini.returnRoot();
            mainScene.setRoot(game);
            mainScene.getRoot().requestFocus();

        } catch (Exception e) {
            e.printStackTrace();

        }

       // mainScene.setRoot(appRoot);
    }

    private boolean isPlayerOutOfBounds() {
        if (player.getY() > map.level().height()){
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
            gameisOver = true;
        }
        if (gameOver.isStartAgain()) {
            gameisOver=false;
            mediaPlayer.play();
            player.setX(PLAYERSTARTX);
            player.setY(PLAYERSTARTY);
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
            defeatSound.play();
            mediaPlayer.stop();
            gameisMenu = true;
        }
        if (gameMenu.isStartGame()) {
            gameisMenu=false;
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
            defeatSound.play();
            mediaPlayer.stop();
            gameisInstructions = true;
        }
        if (instructionScreen.isReturn_to_menu()) {
            gameisInstructions=false;
            mediaPlayer.play();
            player.setX(PLAYERSTARTX);
            player.setY(PLAYERSTARTY);
            map.mapRoot().setTranslateX(map.level().width()-player.getX() - WIDTH);
            map.mapRoot().setTranslateY(map.level().height()-player.getY() - HEIGHT);
            moveScreenY();
            instructionScreen.setReturn_to_menu();
            mainScene.setRoot(appRoot);
        }
    }

    private Boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }


}

