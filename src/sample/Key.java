package sample;

import javafx.scene.control.Button;

public class Key {
    private Button button = new Button();
    private int value;

    Key(int value) {
        this.value = value;
        button.setMinSize(50,50);
        button.setText(Integer.toString(value));
    }

    Button getButton() {
        return button;
    }

    int getValue() {
        return value;
    }
}
