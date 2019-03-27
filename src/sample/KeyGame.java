package sample;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import static javafx.application.Application.launch;


public class KeyGame extends Application{

    Scene s;
    Circle image_Red, image_Green, image_Blue;
    double orgSceneX, orgSceneY;
    double orgTranslateX, orgTranslateY;
    boolean won = false;

    public Group returnRoot(){

        Group root = new Group();

        image_Red = new Circle(50.0f, Color.RED);
        image_Red.setCursor(Cursor.HAND);
        image_Red.setOnMousePressed(circleOnMousePressedEventHandler);
        image_Red.setOnMouseDragged(circleOnMouseDraggedEventHandler);

        image_Green = new Circle(50.0f, Color.GREEN);
        image_Green.setCursor(Cursor.MOVE);
        image_Green.setCenterX(150);
        image_Green.setCenterY(150);
        image_Green.setOnMousePressed(circleOnMousePressedEventHandler);
        image_Green.setOnMouseDragged(circleOnMouseDraggedEventHandler);

        image_Blue = new Circle(50.0f, Color.BLUE);
        image_Blue.setCursor(Cursor.HAND);
        image_Blue.setTranslateX(300);
        image_Blue.setTranslateY(100);
        image_Blue.setOnMousePressed(circleOnMousePressedEventHandler);
        image_Blue.setOnMouseDragged(circleOnMouseDraggedEventHandler);


        root.getChildren().addAll(image_Red, image_Green, image_Blue);

        return root;

    }


    public static void main(String[] args) {
        launch(args);

    }

    public void start(Stage primaryStage) throws Exception{

        Group root = returnRoot();
        Pane pane = new Pane();

        s = new Scene(root, 400,350);

        primaryStage.setResizable(false);
        primaryStage.setScene(s);

        primaryStage.setTitle("drag-n-drop");
        primaryStage.show();

        KeyGame mini = new KeyGame();
        Group game = mini.returnRoot();

        s.setRoot(game);

        if (won) {
            s.setRoot(pane);
        }



    }

    boolean iswon() {

        BooleanProperty t;
        t = new SimpleBooleanProperty();

        if (this.circles() == 0) {
            return true;

       }

        return false;

   }

   int circles() {

       int distSq = (int) ((image_Red.getCenterX() - image_Blue.getCenterX()) * (image_Red.getCenterX() - image_Blue.getCenterX())
                         + (image_Red.getCenterY() - image_Blue.getCenterY()) * (image_Red.getCenterY() - image_Blue.getCenterY()));

       int radSum = (int)((image_Red.getRadius() + image_Blue.getRadius()) * (image_Red.getRadius() + image_Blue.getRadius()));

       System.out.println(distSq + radSum);

       if (distSq == radSum)
           return -1;
       else if (distSq > radSum)
           return 1;
       else
           return 0;

   }


    EventHandler<MouseEvent> circleOnMousePressedEventHandler  =
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    orgSceneX = event.getSceneX();
                    orgSceneY = event.getSceneY();
                    orgTranslateX = ((Circle)event.getSource()).getTranslateX();
                    orgTranslateY = ((Circle)event.getSource()).getTranslateY();

                }
            };

    EventHandler<MouseEvent> circleOnMouseDraggedEventHandler =
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {

                    double offsetX = event.getSceneX() - orgSceneX;

                    double offsetY = event.getSceneY() - orgSceneY;
                    double newTranslateX = orgTranslateX + offsetX;
                    double newTranslateY = orgTranslateY + offsetY;

                    ((Circle)(event.getSource())).setTranslateX(newTranslateX);
                    ((Circle)(event.getSource())).setTranslateY(newTranslateY);

                    won = iswon();
                    System.out.println(won);
                }
            };
}

