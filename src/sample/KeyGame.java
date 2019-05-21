package sample;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

import static java.awt.Color.BLACK;
import static javafx.application.Application.launch;


public class KeyGame extends Application {
    Scene s;
    AnchorPane root = new AnchorPane();
    FXMLLoader loader = new FXMLLoader();

    KeyGame(){
        Rectangle sweeper = new Rectangle(115, 75, 0, 530);
        sweeper.setFill(Color.BLACK);
        root.getChildren().addAll(sweeper);
        sweep(sweeper).play();
    }

    @FXML
    AnchorPane returnRoot() throws Exception{

        root = loader.load(getClass().getResource("sample.fxml"));
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

