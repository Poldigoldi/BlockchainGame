package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.HashMap;


public class Main extends Application {
    private Player player = new Player("Come");
    private Pane appRoot = new Pane();
    private Map map = new Map();

    private HashMap<KeyCode, Boolean> keys = new HashMap<>();


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        initContent();
        Scene scene = new Scene(appRoot);

        scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));

        primaryStage.setScene(scene);
        primaryStage.show();

        runGame();
    }

    private void initContent() {
        Rectangle background = new Rectangle(1280, 720);
        map.initialize ();

        Node PlayerNode = map.createEntity (60,600,50, 50, Color.DARKGREEN);
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
        this.player.applyGravity();
        this.player.updateY();

        // check for items pickup/drop
        handleItems();

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

    private Boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }
}
