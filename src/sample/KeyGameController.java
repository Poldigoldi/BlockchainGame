package sample;

import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;


import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.ResourceBundle;


public class KeyGameController implements Initializable {



        @FXML
        private Group encryption;

        @FXML
    Label hello;

        @FXML
        private Circle myCircle;

        @FXML
        private HBox wordBox;

        @FXML
        private Button cPublicKey;

        @FXML
        private Rectangle signitureBounds;

        @FXML
        private Button pPrivateKey;

        @FXML
        private Button pPublicKey;

        @FXML
        private Button cPrivateKey;

        @FXML
        private Group verifyText;

        @FXML
        private Group verify;

        @FXML
        private AnchorPane cPK1;

        @FXML
        private ImageView tickOrCross;

        @FXML
        private Group signiture;

        @FXML
        private AnchorPane cPK;

        double origTranslateX, origTranslateY, oringinalLocationX, oringinalLocationY, saveX, saveY, newTranslateX, newTranslateY;

        boolean added = false;

        private Button insideButton;

    private String word;

    {
        word = getWord();
    }


    private int index=0;
    private int alphaIndex = 0;



    private Coordinate circleCentre;



    @Override
    public void initialize(URL url, ResourceBundle rb) {

        /*
        myCircle.setStroke(Color.BLACK);
        Image alphabet = new Image("/graphics/minigameimages/images.jpg",false);
        myCircle.setFill(new ImagePattern(alphabet));
        circleCentre = new Coordinate((int)myCircle.getCenterX(), (int)myCircle.getCenterY());*/

        verify();
    }

    @FXML
    void spinWheel(KeyEvent event) {

        double SPIN_VALUE = 13.8;
        if (event.getCode() == KeyCode.LEFT) {

            if (alphaIndex == 1) {
                myCircle.setRotate(0);
            }
            else {
                myCircle.setRotate(myCircle.getRotate() + SPIN_VALUE);
            }

            if (alphaIndex == 0) {
                alphaIndex = 25;
            }
            else {
                alphaIndex--;
            }

        }
        if (event.getCode() == KeyCode.RIGHT) {

            if (alphaIndex == 25) {
                alphaIndex = 0;
                myCircle.setRotate(0);
            }
            else {
                if (myCircle.getRotate() > SPIN_VALUE) {
                    myCircle.setRotate(myCircle.getRotate() - SPIN_VALUE);
                }
                else {
                    myCircle.setRotate(myCircle.getRotate() + 360 - SPIN_VALUE);
                }

                alphaIndex++;
            }
        }

        if (event.getCode() == KeyCode.SPACE) {
            if (compareWord()) {
                wordBox.getChildren().get(index).setVisible(true);
                index++;
                if (index == word.length()) {
                   verify();
                }
            }
            else {
                System.out.print("Game over, try a new word");
            }
        }

     /*   if (event.getCode() == KeyCode.ENTER) {
            if (instruction1.isVisible()){
                instruction1.setVisible(false);
                image.setVisible(true);
            }
            else if (image.isVisible()) {
                image.setVisible(false);
                instruction2.setVisible(true);
            }
            else if (instruction2.isVisible()){
                help.setVisible(false);
            }
            else {
                help.setVisible(true);
            }
        }*/

        System.out.println(alphaIndex);
      /*  if (event.getEventType() == KeyEvent.KEY_PRESSED) {

            if (isPressed(KeyCode.LEFT)) {

                System.out.print(myCircle.getRotate());
                System.out.println("press occured!");

                myCircle.setRotate(myCircle.getRotate() - (360 / 26));

            }
        }*/
    }

    //TODO : to checks for Hbox labels secret word

    String getRandomWord(int i) {
       return new ArrayList<>(Arrays.asList(
                "HASH", "PARTY", "BROWNS", "ARE", "TASTY"
        )).get(i);

   }





    Boolean compareWord() {

        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        if (word.charAt(index) == alphabet.charAt(alphaIndex)) {
            return true;
        }
        return false;
    }

    String getWord() {

        return "secret";
    }


    void verify() {

        if (signiture.isVisible()) {
            signiture.setVisible(false);
            verify.setVisible(true);
            cPrivateKey.setTranslateX(0);
            cPrivateKey.setTranslateY(0);

        }

        if (encryption.isVisible()){
            encryption.setVisible(false);
            signiture.setVisible(true);
        }


        pPrivateKey.setCursor(Cursor.HAND);
        cPrivateKey.setCursor(Cursor.HAND);
        cPublicKey.setCursor(Cursor.HAND);
        pPublicKey.setCursor(Cursor.HAND);


        pPrivateKey.setOnMousePressed(buttonOnMousePressedEventHandler);
        pPrivateKey.setOnMouseDragged(buttonOnMouseDraggedEventHandler);
        pPrivateKey.setOnMouseReleased(buttonOnMouseReleasedEventHandler);

        cPrivateKey.setOnMousePressed(buttonOnMousePressedEventHandler);
        cPrivateKey.setOnMouseDragged(buttonOnMouseDraggedEventHandler);
        cPrivateKey.setOnMouseReleased(buttonOnMouseReleasedEventHandler);

        pPublicKey.setOnMousePressed(buttonOnMousePressedEventHandler);
        pPublicKey.setOnMouseDragged(buttonOnMouseDraggedEventHandler);
        pPublicKey.setOnMouseReleased(buttonOnMouseReleasedEventHandler);

        cPublicKey.setOnMousePressed(buttonOnMousePressedEventHandler);
        cPublicKey.setOnMouseDragged(buttonOnMouseDraggedEventHandler);
        cPublicKey.setOnMouseReleased(buttonOnMouseReleasedEventHandler);
    }

    boolean checkBounds(int sceneX, int sceneY) {

        int acceptionBound = 10;

     //   if (signiture.isVisible()) {

           if((sceneX > (int)signitureBounds.getLayoutX()-acceptionBound  &&
               sceneX < (int)signitureBounds.getLayoutX()+acceptionBound) ||
              (sceneY > (int)signitureBounds.getLayoutY()-acceptionBound  &&
               sceneY < (int)signitureBounds.getLayoutY()+acceptionBound)) {
               return true;
           }
           return false;

      //  }
     //   return false;

   /*     boolean checkBounds(int sceneX, int sceneY, int boundsX, int boundsY) {

            int acceptionBound = 10;

            if((sceneX > (int)signitureBounds.getLayoutX()-acceptionBound  &&
                    sceneX < (int)signitureBounds.getLayoutX()+acceptionBound) ||
                    (sceneY > (int)signitureBounds.getLayoutY()-acceptionBound  &&
                            sceneY < (int)signitureBounds.getLayoutY()+acceptionBound)) {
                return true;
            }
            return false;*/
    }




    @FXML
    void checker(Button source) {

        if (source.equals(cPrivateKey)) {
            insideButton = null;
            cPK.setStyle("-fx-background-color: green");
            Timeline t = new Timeline();
            t.setOnFinished(e->verify());
            t.setDelay(new Duration(900));
            t.play();

        }
        else {
            added = true;
            cPK.setStyle("-fx-background-color: red");
        }

    }


    EventHandler<MouseEvent> buttonOnMousePressedEventHandler =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    if(!encryption.isVisible()) {
                        oringinalLocationX = t.getSceneX();
                        oringinalLocationY = t.getSceneY();
                        saveX = ((Button)(t.getSource())).getLayoutX();
                        saveY= ((Button)(t.getSource())).getLayoutY();
                        origTranslateX = ((Button)(t.getSource())).getTranslateX();
                        origTranslateY = ((Button)(t.getSource())).getTranslateY();
                    }
                }
            };

    EventHandler<MouseEvent> buttonOnMouseDraggedEventHandler =
            new EventHandler<MouseEvent>()  {

                @Override
                public void handle(MouseEvent t) {
                    if (!encryption.isVisible()) {
                        double offsetX = t.getSceneX() - oringinalLocationX;
                        double offsetY = t.getSceneY() - oringinalLocationY;
                        newTranslateX = origTranslateX + offsetX;
                        newTranslateY = origTranslateY + offsetY;

                        ((Button)(t.getSource())).setTranslateX(newTranslateX);
                        ((Button)(t.getSource())).setTranslateY(newTranslateY);
                    }

                }
            };


    EventHandler<MouseEvent> buttonOnMouseReleasedEventHandler =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {

                    int placeX = (int)(saveX + ((Button)(t.getSource())).getTranslateX());
                    int placeY = (int)((saveY + ((Button)(t.getSource())).getTranslateY()));

                    if (checkBounds(placeX,placeY) && insideButton == null){
                        System.out.println("if");
                        ((Button)(t.getSource())).setTranslateX(407-saveX);
                        ((Button)(t.getSource())).setTranslateY(280-saveY);
                        insideButton = (Button)(t.getSource());
                        checker((Button)(t.getSource()));
                    }
                    else if (checkBounds(placeX,placeY) && insideButton != null) {
                        System.out.println("else if");
                        ((Button)(t.getSource())).setTranslateX(0);
                        ((Button)(t.getSource())).setTranslateY(0);
                    }
                    else {
                        System.out.println("else   : " + signitureBounds.getLayoutX());
                        insideButton = null;
                        ((Button)(t.getSource())).setTranslateX(0);
                        ((Button)(t.getSource())).setTranslateY(0);
                        cPK.setStyle("-fx-background-color: #1fffec");
                    }
                }
            };
}
















 /*   @FXML
    void spinWheel(MouseEvent event) {

        if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {

            Coordinate placeHolder = new Coordinate((int)event.getX(), (int)event.getY());
            startAngle = getAngle(circleCentre, placeHolder);

            System.out.println("press occured!");

            Coordinate currentPoint = new Coordinate((int)event.getX(), (int)event.getY());
            double currentAngle = getAngle(circleCentre, currentPoint);
            myCircle.setRotate(currentAngle-startAngle);


        }

    }

    int getAngle(Coordinate origin, Coordinate other) {

        int angle;
        int dy = other.getY() - origin.getY();
        int dx = other.getX() - origin.getX();


        if (dx == 0) {
             angle = (int)(dy >= 0? PI/2: -PI/2);
        }
        else {
            angle = (int)Math.atan(dy/dx);
            if (dx < 0) {
                angle += PI;
            }
        }
        if (angle <0) {
            angle += 2*PI;
        }

        return angle;

    }*/


/* A block is a peice of information.
   Contains a time stamp -> created bloch HASH
   has hash of previous block.
 */
