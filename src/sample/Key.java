package sample;

import javafx.scene.control.Button;
import javafx.scene.text.Font;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

class Key {
    private Button button = new Button();
    private String value;

    Key(String value) {
        this.value = value;
        button.setMinSize(50, 50);
        button.setText(value);
        try {
            button.setFont(Font.loadFont(new FileInputStream(new File("src/graphics/Fleftex_M.ttf")), 16));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        button.setStyle("-fx-background-color: #000000;  -fx-text-fill: #39ff14; -fx-opacity: 1;");
    }

    Button getButton() {
        return button;
    }

    String getValue() {
        return value;
    }
}
