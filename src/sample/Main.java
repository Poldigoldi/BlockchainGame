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

import java.util.ArrayList;
import java.util.HashMap;


public class Main extends Application {
    private Player player = new Player("Come");
    private Pane appRoot = new Pane();
    private Pane gameRoot = new Pane();

    private HashMap<KeyCode, Boolean> keys = new HashMap<>();
    private ArrayList<Item> blocks = new ArrayList<>();


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
        Item block = new Item ("block");
        Node ItemNode = createEntity (300, 620, 50, 50, Color.SADDLEBROWN);
        block.createItem (ItemNode);
        addItem (block);

        Node PlayerNode = createEntity (60,600,50, 50, Color.DARKGREEN);
        showEntity(this.player.createPlayer(PlayerNode));

        showEntity(createEntity(0, 670, 1280, 720, Color.DARKGRAY));
        appRoot.getChildren().addAll(background, gameRoot);

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
        for (Item block : blocks) {
            if ((this.player.getPlayer().getBoundsInParent()).intersects(block.getItem().getBoundsInParent())) {
                /* pickup item */
                if (block.isAlive ()) {
                    if (isPressed (KeyCode.A)) {
                        this.player.getLuggage ().take (block);
                        removeItem (block);
                    }
                }
            }
            /* drop item */
            if (! block.isAlive ()) {
                if (isPressed (KeyCode.Z)) {
                    double new_X = this.player.getPlayer().getTranslateX();
                    double new_Y = this.player.getPlayer().getTranslateY() ;
                    this.player.getLuggage ().drop (block, new_X, new_Y);
                    addItem (block);
                }
            }
        }
    }

    private void addItem (Item item) {
        /* add Item to Map */
        if (!this.blocks.contains (item)) {
            this.blocks.add (item);
        }
        showEntity (item.getItem ());
    }


    private void removeItem (Item item) {
        hideEntity (item.getItem ());
    }

    private Node createEntity (int x, int y, int w, int h, Color color) {
        Rectangle entity = new Rectangle(w, h);
        entity.setTranslateX(x);
        entity.setTranslateY(y);
        entity.setFill(color);
        return entity;
    }

    private void showEntity(Node node) {
        gameRoot.getChildren().add(node);
    }

    private void hideEntity (Node node) {
        gameRoot.getChildren().remove (node);
    }

    private Boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }
}
