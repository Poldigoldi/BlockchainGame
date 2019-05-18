package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class KeyPad {
    private GridPane keyPad = new GridPane();
    private Key one, two, three, four, five, six, seven, eight, nine, zero;
    private Button enter, clear, exit;
    private TextField display;
    private Group root = new Group();
    private boolean isCodeCorrect = false;
    private int WIDTH, HEIGHT;
    private Font font;
    private String style = "-fx-background-color: #000000;  -fx-text-fill: #39ff14; -fx-opacity: 1;";

    {
        try {
            font = Font.loadFont(new FileInputStream(new File("src/graphics/Fleftex_M.ttf")), 16);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    KeyPad(int WIDTH, int HEIGHT) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
    }

    void initialise() {
        Image background = new Image("/graphics/neo.png");
        ImageView backgroundView = new ImageView(background);
        backgroundView.setFitWidth(WIDTH/4);
        backgroundView.setFitHeight(HEIGHT/4);
        backgroundView.setTranslateX(WIDTH/2);
        backgroundView.setTranslateY(HEIGHT/2-100);

        /*Selection keys*/
        one = new Key(1);
        two = new Key(2);
        three = new Key(3);
        four = new Key(4);
        five = new Key(5);
        six = new Key(6);
        seven = new Key(7);
        eight = new Key(8);
        nine = new Key(9);
        zero = new Key(0);

        display = new TextField("");
        display.setMinSize(200,50);
        display.setFont(font);
        display.setStyle(style);

        /*Buttons*/
        enter = new Button("ENTER");
        enter.setMinSize(50,50);
        clear = new Button("C");
        clear.setMinSize(50,50);
        exit = new Button("EXIT");
        exit.setMinSize(50,50);
        enter.setFont(font);
        enter.setStyle(style);
        clear.setFont(font);
        clear.setStyle(style);
        exit.setFont(font);
        exit.setStyle(style);


        /*Add buttons to keyPad*/
        keyPad.add(display, 0,0,3,1);
        keyPad.add(seven.getButton(),0,1);
        keyPad.add(eight.getButton(),1,1);
        keyPad.add(nine.getButton(),2,1);
        keyPad.add(four.getButton(),0,2);
        keyPad.add(five.getButton(),1,2);
        keyPad.add(six.getButton(),2,2);
        keyPad.add(one.getButton(),0,3);
        keyPad.add(two.getButton(),1,3);
        keyPad.add(three.getButton(), 2,3);
        keyPad.add(zero.getButton(),1,4);
        keyPad.add(enter, 2,4);
        keyPad.add(clear, 0,4);
        keyPad.add(exit,3,4);

        keyPad.setPadding(new Insets(10));
        keyPad.setHgap(10);
        keyPad.setVgap(10);

        root.getChildren().addAll(backgroundView, keyPad);

    }

    Group getRoot() {
        return root;
    }
    String getDisplayText() {
        return display.getText();
    }
    void setDisplayText(String displayText) {
        this.display.setText(displayText);
    }

    Key getOne() {
        return one;
    }
    Key getTwo() {
        return two;
    }
    Key getThree() {
        return three;
    }
    Key getFour() {
        return four;
    }
    Key getFive() {
        return five;
    }
    Key getSix() {
        return six;
    }
    Key getSeven() {
        return seven;
    }
    Key getEight() {
        return eight;
    }
    Key getNine() {
        return nine;
    }
    Key getZero() {
        return zero;
    }
    Button getEnter() {
        return enter;
    }
    Button getClear() {
        return clear;
    }

    Button getExit() {
        return exit;
    }
    boolean isCodeCorrect() {
        return isCodeCorrect;
    }
    void setCodeCorrect(boolean codeCorrect) {
        isCodeCorrect = codeCorrect;
    }
}
