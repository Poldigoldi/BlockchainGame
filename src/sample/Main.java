package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    private Player player;
    private Pane appRoot = new Pane();
    private Map map = new Map();
    private Scene mainScene;
    private GameOver gameOver = new GameOver();
    private int playerDirection = 1;
    private HashMap<KeyCode, Boolean> keys = new HashMap<>();


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        player = new Player("Come", 200, 200, primaryStage);
        player.initialise();
        initContent();
        mainScene = new Scene(appRoot, 1280, 720);
        mainScene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        mainScene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
        primaryStage.setScene(mainScene);
        primaryStage.show();


        runGame();
    }

    private void initContent() {
        Image back1 = new Image("/background1.png", true);
        BackgroundImage background = new BackgroundImage(back1, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT , BackgroundSize.DEFAULT);
        appRoot.setBackground(new Background(background));
        map.initialize ();
        map.showEntity(player);
        appRoot.getChildren().addAll(map.getGameRoot ());

    }
    private void runGame() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        timer.start();
    }

    private void update() {
        boolean playerMoving = false;
        /*  Handles all the game events, including player motion and interaction with items  */
        // check for Keys
        if (isPressed(KeyCode.LEFT)) {
            player.move_X (-5, map);
            playerDirection = -1;
            playerMoving = true;
        }
        if (isPressed(KeyCode.RIGHT)) {
            player.move_X (5, map);
            playerDirection = 1;
            playerMoving = true;
        }
        if (isPressed(KeyCode.SPACE)) {
            player.jump();
        }
        if (isPressed(KeyCode.ESCAPE)) {
            mainScene.setRoot(appRoot);
        }
        player.sprite().setImage(new Image("/motionLeft1.png"));
        player.update(map, playerDirection, playerMoving);
        map.getBlocks().get(0).sprite.update(); //updates the item animation, this method will change
        // check for items pickup/drop
        handleItems();
        if (isPlayerOutOfBounds()) {
          //  handleGameOver();
        }
    }

    /* ---------- Sub methods ----------- */

    private void handleItems () {
        for (Item block : map.getBlocks ()) {
            if ((this.player.getBoundsInParent()).intersects(block.getBoundsInParent())) {
                /* pickup item */
                if (block.isAlive ()) {
                    if (isPressed (KeyCode.A)) {
                        this.player.getLuggage ().take (block);
                        map.removeItem (block);
                        miniGameKey();
                    }
                }
            }
        }
        /* drop item */
        Item myBlock = this.player.getLuggage ().getblock ();
        if (myBlock != null) {
            if (! myBlock.isAlive ()) {
                if (isPressed (KeyCode.Z)) {
                    double new_X = this.player.getTranslateX();
                    double new_Y = this.player.getTranslateY() ;
                    this.player.getLuggage ().drop (myBlock, new_X, new_Y);
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
