package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;

public class Drag extends Application {
    Rectangle dragObject;
    Rectangle targetObject;
    double clickX, clickY;
    double objectX, objectY;

    @Override
    public void start(Stage primaryStage) throws Exception{
        //initialise drag object
        dragObject = new Rectangle(50, 50, 100, 50);
        dragObject.setFill(Color.RED);
        dragObject.setCursor(Cursor.HAND);
        dragObject.setOnMousePressed(MousePressEventHandler);
        dragObject.setOnMouseDragged(MouseDraggedEventHandler);
        //initialise target object
        targetObject = new Rectangle(150, 150, 100, 50);
        targetObject.setFill(Color.GREEN);

        Group root = new Group();
        root.getChildren().addAll(dragObject);
        //set the stage
        primaryStage.setTitle("Dragging");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }




    EventHandler<MouseEvent> MousePressEventHandler =
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    //get x and y distance of click from object origin
                    clickX = e.getSceneX();
                    clickY = e.getSceneY();
                    objectX = ((Rectangle)(e.getSource())).getTranslateX();
                    objectY = ((Rectangle)(e.getSource())).getTranslateY();
                }
            };

    EventHandler<MouseEvent> MouseDraggedEventHandler =
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    double offsetX = e.getSceneX() - clickX;
                    double offsetY = e.getSceneY() - clickY;
                    double newX = objectX + offsetX;
                    double newY = objectY + offsetY;
                    ((Rectangle)(e.getSource())).setTranslateX(newX);
                    ((Rectangle)(e.getSource())).setTranslateY(newY);
                }
            };




    public static void main(String[] args) {
        launch(args);
    }
}
