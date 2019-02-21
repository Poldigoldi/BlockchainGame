package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.HashMap;


public class Main extends Application {
    private Player player = new Player("Come");
    private Pane appRoot = new Pane();
    private Pane gameRoot = new Pane();

    private HashMap<KeyCode, Boolean> keys = new HashMap<>();


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


    public static void main(String[] args) {
        launch(args);
    }

    private void initContent() {
        Rectangle background = new Rectangle(1280, 720);
        addEntity(player.createPlayer(0,600,50));
        appRoot.getChildren().addAll(background, gameRoot);
    }

    private void addEntity(Node node) {
        gameRoot.getChildren().add(node);
    }

    private Boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }

    private void update() {
        if (isPressed(KeyCode.LEFT)) {
            System.out.println("LEFT");
        }
        if (isPressed(KeyCode.RIGHT)) {
            System.out.println("RIGHT");
        }
        if (isPressed(KeyCode.SPACE)) {
            System.out.println("JUMP");
        }
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
}
