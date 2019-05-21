package sample;

import javafx.animation.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.net.URL;
import java.util.*;


public class KeyGameController implements Initializable {
    @FXML
    private Group encryption;

    @FXML
    AnchorPane root;

    @FXML
    Label hello;

    @FXML
    Group b;

    @FXML
    Text escape;

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


    boolean isDone = true;

    BlockAnimation ba;

    @FXML
    private Button pPublicKey;

    @FXML
    private Button cPrivateKey;



    @FXML
    private Group instructionPane;



    @FXML
    Label pressEnter;

    @FXML
    private Text instructions;

    @FXML
    private Group signiture;

    @FXML
    private AnchorPane cPK, pPK;

    private int instructionString = 1;

    double origTranslateX, origTranslateY, oringinalLocationX, oringinalLocationY, saveX, saveY, newTranslateX, newTranslateY;

    boolean added = false;

    Timer timer;

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
        text();

        myCircle.setStroke(Color.BLACK);
        Image alphabet = new Image("/graphics/minigameimages/images.jpg",false);
        myCircle.setFill(new ImagePattern(alphabet));
        circleCentre = new Coordinate((int)myCircle.getCenterX(), (int)myCircle.getCenterY());


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
        System.out.println(alphaIndex);
    }
    //TODO : to checks for Hbox labels secret word




    void text() {

        ArrayList<String> messages = new ArrayList<>();

        messages.add("Ahoy Chaps! You have entered the mysterious Block!\n Let's take a look...");
        messages.add("A block in a blockchain, is simply a piece of information or message.");
        messages.add("These messages are safe and secure to send and receive.");
        messages.add("Before blockchain, sending secret messages meant using the same key to lock and unlock the message.");
        messages.add("This meant the secret key would have to be exchanged if people wanted to send secret messages... That's not secure!");
        messages.add("Blockchain solves this problem by using 2 keys! Each person has a public key and private key.");
        messages.add("These keys can be used as a pair to encrypt and decrypt messages.");
        messages.add("These keys are related: the private key is put into a machine to generate a public key.");
        messages.add("If someone wants to send you a message, they can securely send it by locking it with you public key.");
        messages.add("Then you can unlock with your private key!");
        messages.add("Clueso wants to send you a message. You must decrypt the message to use as a password for the next level.");
        messages.add("");


      /*  messages.add("This makes it extra secure as you don't need to share your private key.");



        messages.add("He has locked the message using your public key.");
        messages.add("You must unlock the message with your private key!");
        messages.add("The message will be the code for you to get to the next level!");
        messages.add("Use left and right to move, and space to select the letter");*/

        BlockAnimation ba = new BlockAnimation();
        ba.start(b, new ArrayList<>(Arrays.asList("graphics/minigameimages/animation/bridge1.png")), 1, 1);
        textAnimation(messages.get(0));



        root.addEventHandler(KeyEvent.KEY_PRESSED, e ->
        {
            if (e.getCode() == KeyCode.ENTER ) {

                pressEnter.setVisible(false);
                if (instructionString < messages.size()){
                    if (ba.getStatus() && isDone) {
                        textAnimation(messages.get(instructionString));
                        instructionString++;
                    }
                }
                else {
                    instructionPane.setVisible(false);
                }

            }

            if (e.getCode() == KeyCode.I && !instructionPane.isVisible()) {
                instructionPane.setVisible(true);
            }



        });

    }


    void animation() {
        ArrayList<String> bridge = new ArrayList<>(Arrays.asList("graphics/minigameimages/animation/bridge1.png",
                "graphics/minigameimages/animation/bridge2.png", "graphics/minigameimages/animation/bridge3.png",
                "graphics/minigameimages/animation/bridge4.png","graphics/minigameimages/animation/bridge5.png",
                "graphics/minigameimages/animation/bridge6.png","graphics/minigameimages/animation/bridge7.png",
                "graphics/minigameimages/animation/bridge8.png","graphics/minigameimages/animation/bridge9.png",
                "graphics/minigameimages/animation/bridge10.png","graphics/minigameimages/animation/bridge11.png",
                "graphics/minigameimages/animation/bridge12.png","graphics/minigameimages/animation/bridge13.png"
        ));
        ArrayList<String> message = new ArrayList<>(Arrays.asList("graphics/minigameimages/animation/message1.png",
                "graphics/minigameimages/animation/message2.png", "graphics/minigameimages/animation/message3.png",
                "graphics/minigameimages/animation/message4.png","graphics/minigameimages/animation/message5.png",
                "graphics/minigameimages/animation/message8.png"
        ));
        ArrayList<String> message2 = new ArrayList<>(Arrays.asList("graphics/minigameimages/animation/message6.png",
                "graphics/minigameimages/animation/message7.png"
        ));

        ArrayList<String> hello = new ArrayList<>(Arrays.asList("graphics/minigameimages/animation/hello1.png",
                "graphics/minigameimages/animation/hello2.png", "graphics/minigameimages/animation/hello3.png",
                "graphics/minigameimages/animation/hello4.png","graphics/minigameimages/animation/hello5.png",
                "graphics/minigameimages/animation/hello6.png","graphics/minigameimages/animation/hello7.png",
                "graphics/minigameimages/animation/hello8.png","graphics/minigameimages/animation/hello9.png",
                "graphics/minigameimages/animation/hello10.png","graphics/minigameimages/animation/hello11.png",
                "graphics/minigameimages/animation/hello12.png","graphics/minigameimages/animation/hello13.png",
                "graphics/minigameimages/animation/hello14.png","graphics/minigameimages/animation/hello15.png",
                "graphics/minigameimages/animation/hello16.png","graphics/minigameimages/animation/hello15.png",
                "graphics/minigameimages/animation/hello16.png","graphics/minigameimages/animation/hello15.png"


        ));

        ArrayList<String> spy = new ArrayList<>(Arrays.asList("graphics/minigameimages/animation/spy1.png",
                "graphics/minigameimages/animation/spy2.png", "graphics/minigameimages/animation/spy3.png",
                "graphics/minigameimages/animation/spy4.png","graphics/minigameimages/animation/spy5.png",
                "graphics/minigameimages/animation/spy6.png","graphics/minigameimages/animation/spy7.png",
                "graphics/minigameimages/animation/spy8.png","graphics/minigameimages/animation/spy9.png",
                "graphics/minigameimages/animation/spy10.png","graphics/minigameimages/animation/spy11.png",
                "graphics/minigameimages/animation/spy12.png","graphics/minigameimages/animation/spy13.png",
                "graphics/minigameimages/animation/spy14.png","graphics/minigameimages/animation/spy15.png",
                "graphics/minigameimages/animation/spy16.png","graphics/minigameimages/animation/spy17.png",
                "graphics/minigameimages/animation/spy18.png","graphics/minigameimages/animation/spy19.png",
                "graphics/minigameimages/animation/spy20.png", "graphics/minigameimages/animation/spy21.png",
                "graphics/minigameimages/animation/spy22.png", "graphics/minigameimages/animation/spy23.png"
        ));


        ArrayList<String> keypair = new ArrayList<>(Arrays.asList("graphics/minigameimages/animation/keypair1.png",
                "graphics/minigameimages/animation/keypair2.png", "graphics/minigameimages/animation/keypair3.png"
        ));
        ArrayList<String> pairlock = new ArrayList<>(Arrays.asList("graphics/minigameimages/animation/pair1.png",
                "graphics/minigameimages/animation/pair2.png", "graphics/minigameimages/animation/pair3.png"
        ));
        ArrayList<String> gen = new ArrayList<>(Arrays.asList("graphics/minigameimages/animation/gen1.png",
                "graphics/minigameimages/animation/gen2.png", "graphics/minigameimages/animation/gen3.png",
                "graphics/minigameimages/animation/gen4.png","graphics/minigameimages/animation/gen5.png",
                "graphics/minigameimages/animation/gen6.png","graphics/minigameimages/animation/gen5.png",
                "graphics/minigameimages/animation/gen7.png","graphics/minigameimages/animation/gen5.png",
                "graphics/minigameimages/animation/gen6.png","graphics/minigameimages/animation/gen5.png",
                "graphics/minigameimages/animation/gen8.png","graphics/minigameimages/animation/gen9.png",
                "graphics/minigameimages/animation/gen10.png",  "graphics/minigameimages/animation/gen11.png"
        ));
        ArrayList<String> unlock = new ArrayList<>(Arrays.asList("graphics/minigameimages/animation/keyu1.png",
                "graphics/minigameimages/animation/keyu2.png", "graphics/minigameimages/animation/keyu3.png",
                "graphics/minigameimages/animation/keyu4.png","graphics/minigameimages/animation/keyu5.png",
                "graphics/minigameimages/animation/keyu6.png","graphics/minigameimages/animation/keyu7.png"
        ));
        ArrayList<String> lock = new ArrayList<>(Arrays.asList("graphics/minigameimages/animation/keyl1.png",
                "graphics/minigameimages/animation/keyl2.png", "graphics/minigameimages/animation/keyl3.png",
                "graphics/minigameimages/animation/keyl4.png","graphics/minigameimages/animation/keyl5.png",
                "graphics/minigameimages/animation/keyl6.png","graphics/minigameimages/animation/keyl7.png"
        ));

        switch (instructionString) {

            case 1: {
                ba = new BlockAnimation();
                ba.start(b, bridge, 200, 10);
                break;
            }
            case 2: {
                ba = new BlockAnimation();
                ba.start(b, message, 200, 10);
                break;
            }
            case 3: {
                ba = new BlockAnimation();
                ba.start(b, message2, 600, 10);
                break;
            }
            case 4: {
                ba = new BlockAnimation();
                ba.start(b, hello, 200, 10);
                break;
            }
            case 5: {
                ba = new BlockAnimation();
                ba.start(b, spy, 200, 1);
                break;
            }
            case 6: {
                ba = new BlockAnimation();
                ba.start(b, keypair, 1000, 500);
                break;
            }
            case 7: {
                ba = new BlockAnimation();
                ba.start(b, pairlock, 400, 500);
                break;

            }
            case 8: {
                ba = new BlockAnimation();
                ba.start(b, gen, 400, 200);
                break;

            }
            case 9: {
                ba = new BlockAnimation();
                ba.start(b, unlock, 400, 200);
                break;
            }
            case 10: {
                ba = new BlockAnimation();
                ba.start(b, lock, 400, 200);
                break;
            }
            case 11: {

                break;

            }
            case 12: {
                ImageView i = new ImageView("graphics/minigameimages/animation/instructions.png");
                i.setFitWidth(675);
                i.setPreserveRatio(true);
                b.getChildren().clear();
                b.setTranslateX(-100);
                b.getChildren().add(i);
            }


            default: return;
        }
        isDone = true;
        pressEnter.setVisible(true);
    }

    boolean textAnimation(String s) {

        //Media sound = new Media(new File("graphics/minigameimages/keyPress.mp3").toURI().toString());
        //MediaPlayer mp = new MediaPlayer(sound);

        IntegerProperty count = new SimpleIntegerProperty(0);
        Timeline timeline = new Timeline();
        isDone = false;

        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.04), e -> {
            if (count.get() > s.length()) {
                animation();
                timeline.stop();



            } else {
                instructions.setText(s.substring(0, count.get()));
                //mp.play();
                count.set(count.get()+1);
            }

        }
        );
        timeline.getKeyFrames().add(kf);
        timeline.setCycleCount(Animation.INDEFINITE);

        timeline.play();
        return false;

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


/*        if (signiture.isVisible()) {
            signiture.setVisible(false);
            cPrivateKey.setTranslateX(0);
            cPrivateKey.setTranslateY(0);

        }*/

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

        int acceptionBound = 15;


        if ((sceneX > (int)signitureBounds.getLayoutX()-acceptionBound  &&
                sceneX < (int)signitureBounds.getLayoutX()+acceptionBound) ||
                (sceneY > (int)signitureBounds.getLayoutY()-acceptionBound  &&
                        sceneY < (int)signitureBounds.getLayoutY()+acceptionBound)) {
            return true;
        }
        return false;


    }

    @FXML
    void checker(Button source) {

        if (source.equals(pPublicKey) && signiture.isVisible()) {
            insideButton = null;
            cPK.setStyle("-fx-background-color: green");
            escape.setVisible(true);

        }
        else if (source.equals(pPublicKey) && signiture.isVisible()) {
            pPK.setStyle("-fx-background-color: green");

        }
        else {
            added = true;
            cPK.setStyle("-fx-background-color: red");
            pPK.setStyle("-fx-background-color: red");
        }

    }

    @FXML
    void changeText() {



    }


    EventHandler<MouseEvent> buttonOnMousePressedEventHandler =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    if(!encryption.isVisible()) {
                        oringinalLocationX = t.getSceneX();
                        oringinalLocationY = t.getSceneY();
                        saveX = ((Button)(t.getSource())).getLayoutX();
                        saveY = ((Button)(t.getSource())).getLayoutY();
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
