package sample;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;


public class KeyGame extends Application {
    Scene s;
    AnchorPane root = new AnchorPane();

    KeyGame(){
        Rectangle sweeper = new Rectangle(115, 75, 0, 530);
        sweeper.setFill(Color.BLACK);
        root.getChildren().addAll(sweeper);
        sweep(sweeper).play();
    }

    AnchorPane returnRoot() throws Exception{
        root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        return root;
    }


    public static void main(String[] args) {
        launch(args);

    }

    private Timeline sweep(Rectangle rectangle) {
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(rectangle.widthProperty(), 0.0f )),
                new KeyFrame(new Duration(1000),
                        new KeyValue(rectangle.widthProperty(), 750.0f )));
        return timeline;
    }

    public void start(Stage primaryStage) throws Exception{
        s = new Scene(root, 960,640);
        s.setFill(Color.BLACK);
        primaryStage.setResizable(false);
        primaryStage.setScene(s);

        primaryStage.setTitle("drag-n-drop");
        primaryStage.show();

        KeyGame mini = new KeyGame();
        AnchorPane game = mini.returnRoot();

        s.setRoot(game);
        s.getRoot().requestFocus();

    }
}

