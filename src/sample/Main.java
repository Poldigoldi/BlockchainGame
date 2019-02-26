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
    private Pane gameRoot = new Pane();

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
        addEntity(this.player.createPlayer(60,600,50));
        appRoot.getChildren().addAll(background, gameRoot);
        Rectangle ground = new Rectangle (1280, 60);
        ground.setTranslateX(0);
        ground.setTranslateY(670);
        ground.setFill(Color.DARKGRAY);
        addEntity (ground);
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
    }

    private void addEntity(Node node) {
        gameRoot.getChildren().add(node);
    }

    private Boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }
}
