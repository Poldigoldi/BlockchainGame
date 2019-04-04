package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.HashMap;


public class Main extends Application {
    private Player player = new Player("Come", 40);
    private Pane appRoot = new Pane();
    private Map map = new Map();
    private Scene mainScene;
    private GameOver gameOver = new GameOver();

    private HashMap<KeyCode, Boolean> keys = new HashMap<>();


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        initContent();
        mainScene = new Scene(appRoot);

        mainScene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        mainScene.setOnKeyReleased(event -> keys.put(event.getCode(), false));

        primaryStage.setScene(mainScene);
        primaryStage.show();


        runGame();
    }

    private void initContent() {
        Rectangle background = new Rectangle(1280, 720, Color.INDIGO); // create a background
        map.initialize ();

        Node PlayerNode = map.createEntity (100,400,40, 40, Color.DARKGREEN);
        map.showEntity(this.player.createPlayer(PlayerNode));

        appRoot.getChildren().addAll(background, map.getGameRoot ());

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
        /*  Handles all the game events,
            including player motion and interaction with items
        */

        // check for Keys
        if (isPressed(KeyCode.LEFT)) {
            this.player.move_X (-5);
        }
        if (isPressed(KeyCode.RIGHT)) {
            this.player.move_X (5);
        }
        if (isPressed(KeyCode.SPACE)) {
            this.player.jump();
        }
        if (isPressed(KeyCode.ESCAPE)) {
            mainScene.setRoot(appRoot);
        }
        this.player.applyGravity();
        this.player.updateY(map);

        // check for items pickup/drop
        handleItems();
        if (isPlayerOutOfBounds()) {
            handleGameOver();
        }
    }

    /* ---------- Sub methods ----------- */

    private void handleItems () {
        for (Item block : map.getBlocks ()) {
            if ((this.player.getPlayer().getBoundsInParent()).intersects(block.getItem().getBoundsInParent())) {
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
                    double new_X = this.player.getPlayer().getTranslateX();
                    double new_Y = this.player.getPlayer().getTranslateY() ;
                    this.player.getLuggage ().drop (myBlock, new_X, new_Y);
                    map.addItem (myBlock);
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
        if (player.getPlayer().getTranslateX() > appRoot.getWidth() ||
                player.getPlayer().getTranslateX() < 0 ||
                player.getPlayer().getTranslateY() > appRoot.getHeight()) {
            return true;
        }
        return false;
    }

    private void handleGameOver() {
        GridPane gameOverScene = gameOver.returnRoot();
        mainScene.setRoot(gameOverScene);

        if (gameOver.isStartAgain()) {
            player.getPlayer().setTranslateX(100);
            player.getPlayer().setTranslateY(400);
            gameOver.setStartAgain();
            mainScene.setRoot(appRoot);
        }
    }

    private Boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }
}
