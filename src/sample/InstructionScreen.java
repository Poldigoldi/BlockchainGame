package sample;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

class InstructionScreen {
    private Font tinyFont;
    private Font mediumFont;
    private Group root = new Group();
    private Button returnButton = new Button("Return to Menu");

    InstructionScreen(int WIDTH, int HEIGHT) {
        initialiseFonts();

        //Labels
        Label header = new Label("INSTRUCTIONS:");
        header.setFont(mediumFont);
        header.setTranslateX(250);
        header.setTranslateY(HEIGHT / 30);

        Label instructionsText = new Label(
                "Controls:\n\n" +
                        "'A' 'W' 'D'  to move the robot.\n" +
                        "'Space' to shoot.\n" +
                        "'E' to interact\n\n" +
                        "Pick up items by walking into them.\n" +
                        "\n\n" +
                        "Goal:\n\n" +
                        "Find a secret code to open the door to the next level.\n" +
                        "I won't tell you where to find it. You'll have to figure that out yourself.\n" +
                        "There are buttons you can jump on, which do things...\n" +
                        "Oh, before I forget. Beware of the vicious enemy robots...\n\n" +
                        "Let's learn some Blockchain!");
        instructionsText.setFont(tinyFont);
        instructionsText.setTranslateX(5);
        instructionsText.setTranslateY(130);

        //Buttons
        returnButton.setFont(tinyFont);
        returnButton.setStyle("-fx-background-color: #000000;  -fx-text-fill: #39ff14; -fx-opacity: 1;");
        returnButton.setTranslateX(WIDTH - 200);
        returnButton.setTranslateY(HEIGHT - 80);

        //Images
        Image instructionsBackground = new Image("/graphics/menuBackground.png");
        ImageView backgroundView = new ImageView(instructionsBackground);
        backgroundView.setFitWidth(WIDTH);
        backgroundView.setFitHeight(HEIGHT);
        backgroundView.setTranslateX(0);
        backgroundView.setFitHeight(0);

        //Root
        new AnimationTimer() {
            int i = 0;

            public void handle(long now) {
                i++;
                if (i < 50) {
                    header.setTextFill(Color.DEEPPINK);
                }
                if (i > 50) {
                    header.setTextFill(Color.WHITE);
                }
                if (i == 100) i = 0;
            }
        }.start();
        root.getChildren().addAll(instructionsText, header, returnButton);
        returnButton.setOnAction(this::returnPress);
    }

    Group returnRoot() {
        return root;
    }

    Button returnButton() {
        return returnButton;
    }

    private void returnPress(ActionEvent e) {
    }

    private void initialiseFonts() {
        try {
            tinyFont = Font.loadFont(new FileInputStream(new File("src/graphics/Fleftex_M.ttf")), 15);
            mediumFont = Font.loadFont(new FileInputStream(new File("src/graphics/Fleftex_M.ttf")), 54);
        } catch (FileNotFoundException e) {
            tinyFont = Font.font("Verdana", 15);
            mediumFont = Font.font("Verdana", 54);
        }
    }
}
