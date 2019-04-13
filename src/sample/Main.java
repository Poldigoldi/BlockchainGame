package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Shape;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;


public class Main extends Application {
    //global variables
    private int WIDTH = 960 , HEIGHT = 640;
    private int PLAYERSTARTX = 450, PLAYERSTARTY = 300;
    private double playerY = PLAYERSTARTY;
    private Mode mode = Mode.PLATFORMGAME;

    private Pane appRoot = new Pane();
    private Map map = new Map();
    private Player playerInstance;
    private Shape player;
    private Scene mainScene;
    private GameOver gameOver = new GameOver();

    private HashMap<KeyCode, Boolean> keys = new HashMap<>();
    private List<Object> animatedObjects = new ArrayList<>();


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //initialise player, the 'player' is the collision box of the playerInstance
        playerInstance = new Player("Come", PLAYERSTARTX, PLAYERSTARTY, primaryStage);
        playerInstance.initialise();
        player = playerInstance.box;

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
        Image back1 = new Image("/background1.png", true);
        BackgroundImage background = new BackgroundImage(back1, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT , BackgroundSize.DEFAULT);
        appRoot.setBackground(new Background(background));

        //initialise map / contents of the appRoot (i.e. the player, platforms, blocks, etc.)
        map.initialize ();
        map.showEntity(playerInstance);
        appRoot.getChildren().addAll(map.mapRoot());

        //for anything that is animated, add them here, e.g. spinning blocks, player, clouds.
        animatedObjects.add(playerInstance);
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

    private void update(Stage stage) {
        if(mode == Mode.PLATFORMGAME) {
            /*  Handles all the game events, including player motion and interaction with items  */
            if (isPressed(KeyCode.LEFT)) {
                if (playerInstance.move_X(-5, map)) moveScreenX(-5);
            }
            if (isPressed(KeyCode.RIGHT)) {
                if (playerInstance.move_X(5, map)) moveScreenX(5);
            }
            if (isPressed(KeyCode.SPACE)) {
                playerInstance.jump();
            }
            if (isPressed(KeyCode.ESCAPE)) {
                mainScene.setRoot(appRoot);
            }
            moveScreenY();
            for (Object object : animatedObjects) {
                object.update(map);
            }
            handleItems();
            if (isPlayerOutOfBounds()) {/*handleGameOver(); */ }
        }
    }

    //updates the screen X based on player position
    private void moveScreenX(int movement){
        if(player.getTranslateX()>WIDTH/2+10 && player.getTranslateX()<map.level().width()-WIDTH/2-10){
            map.mapRoot().setTranslateX(map.mapRoot().getTranslateX()+(-movement));
        }
    }
    //updates the screen Y based on player position
    private void moveScreenY(){
        double Ydifference;
        if(player.getTranslateY()>HEIGHT/2+30 && player.getTranslateY()<map.level().height()-HEIGHT/2-30){
            if(player.getTranslateY() != playerY){
                Ydifference = playerY -  player.getTranslateY();
                playerY = player.getTranslateY();
                map.mapRoot().setTranslateY(map.mapRoot().getTranslateY()+Ydifference);
            }
        }
    }

    /* ---------- Sub methods ----------- */

    private void handleItems () {
        for (Item block : map.blocks()) {
            if ((this.player.getBoundsInParent()).intersects(block.box.getBoundsInParent())) {
                /* pickup item */
                if (block.isAlive() && isPressed (KeyCode.A)) {
                    this.playerInstance.getLuggage ().take (block);
                    map.removeItem (block);
                    miniGameKey();
                }
            }
        }
        /* drop item */
        Item myBlock = this.playerInstance.getLuggage ().getblock ();
        if (myBlock != null) {
            if (! myBlock.isAlive ()) {
                if (isPressed (KeyCode.Z)) {
                    double new_X = this.player.getTranslateX();
                    double new_Y = this.player.getTranslateY() ;
                    this.playerInstance.getLuggage ().drop (myBlock, new_X, new_Y);
                    map.addItem(myBlock);
                }
            }
        }
    }

    private void miniGameKey() {
        /* Mini games activated once player collects a block on the map */
        KeyGame mini = new KeyGame();
        Group game = mini.returnRoot();
        mainScene.setRoot(game);
       // mainScene.setRoot(appRoot);
    }

    private boolean isPlayerOutOfBounds() {
        if (player.getTranslateX() > appRoot.getWidth() ||
               // player.getPlayer().getTranslateX() < 0 ||
                player.getTranslateY() > appRoot.getHeight()) {
            return true;
        }
        return false;
    }

    private void handleGameOver() {
        GridPane gameOverScene = gameOver.returnRoot();
        mainScene.setRoot(gameOverScene);

        if (gameOver.isStartAgain()) {
            player.setTranslateX(100);
            player.setTranslateY(400);
            gameOver.setStartAgain();
            mainScene.setRoot(appRoot);
        }
    }

    private Boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }
}
