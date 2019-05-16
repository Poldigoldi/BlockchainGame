package sample;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class KeyPad {
    private GridPane keyPad = new GridPane();
    private Key one, two, three, four, five, six, seven, eight, nine, zero;
    private Button enter, clear, exit;
    private TextField display;
    private Group root = new Group();
    private boolean isCodeCorrect = false;


    void initialise() {
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

        /*Buttons*/
        enter = new Button("ENTER");
        enter.setMinSize(50,50);
        clear = new Button("C");
        clear.setMinSize(50,50);
        exit = new Button("EXIT");
        exit.setMinSize(50,50);


        /*Add buttons to keyPad*/
        keyPad.add(display, 3,0);
        keyPad.add(seven.getButton(),0,0);
        keyPad.add(eight.getButton(),1,0);
        keyPad.add(nine.getButton(),2,0);
        keyPad.add(four.getButton(),0,1);
        keyPad.add(five.getButton(),1,1);
        keyPad.add(six.getButton(),2,1);
        keyPad.add(one.getButton(),0,2);
        keyPad.add(two.getButton(),1,2);
        keyPad.add(three.getButton(), 2,2);
        keyPad.add(zero.getButton(),1,3);
        keyPad.add(enter, 2,3);
        keyPad.add(clear, 0,3);
        keyPad.add(exit,3,3);

        keyPad.setPadding(new Insets(10));
        keyPad.setHgap(10);
        keyPad.setVgap(10);

        root.getChildren().add(keyPad);

    }

    public Group getRoot() {
        return root;
    }

    public String getDisplayText() {
        return display.getText();
    }

    public void setDisplayText(String displayText) {
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

    public Button getExit() {
        return exit;
    }

    public boolean isCodeCorrect() {
        return isCodeCorrect;
    }

    public void setCodeCorrect(boolean codeCorrect) {
        isCodeCorrect = codeCorrect;
    }
}
