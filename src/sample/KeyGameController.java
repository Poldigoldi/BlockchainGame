package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;


import java.net.URL;
import java.util.ResourceBundle;


public class KeyGameController implements Initializable {

    @FXML
    private Circle myCircle;

    @FXML
    private HBox wordBox;

    @FXML
    private Text instruction1;

    @FXML
    private Text instruction2;

    @FXML
    private Group help;

    @FXML
    private Pane image;


    double SPIN_VALUE = 13.8;
    int index=0;
    String alphabet ="abcdefghijklmnopqrstuvwxyz";
    int alphaIndex = 0;


   // Map wordPair = new Map()




    Coordinate circleCentre;



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        myCircle.setStroke(Color.BLACK);
        Image alphabet = new Image("/graphics/minigameimages/images.jpg",false);
        myCircle.setFill(new ImagePattern(alphabet));
        circleCentre = new Coordinate((int)myCircle.getCenterX(), (int)myCircle.getCenterY());


    }

    @FXML
    void spinWheel(KeyEvent event) {

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
            }
            else {
                System.out.print("Game over, try a new word");
            }
        }

        if (event.getCode() == KeyCode.ENTER) {
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
        }

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



    Boolean compareWord() {
        String word = getWord();
        if (word.charAt(index) == alphabet.charAt(alphaIndex)) {
            return true;
        }
        return false;
    }

    String getWord() {
        return "secret";
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

}

