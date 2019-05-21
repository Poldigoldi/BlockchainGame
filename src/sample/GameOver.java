package sample;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


class GameOver {
    private Font tinyFont, smallFont, mediumFont;
    private Group root = new Group();

    GameOver(int WIDTH, int HEIGHT) {
        initialiseFonts();
        //Labels
        Label gameOverLabel = new Label("GAME OVER");
        Label insertLabel = new Label(">>INSERT BITCOIN TO CONTINUE<<");
        Label tryagainLabel = new Label("(Press SPACE to try again)");
        gameOverLabel.setFont(mediumFont);
        gameOverLabel.setTextFill(Color.ANTIQUEWHITE);
        insertLabel.setFont(smallFont);
        tryagainLabel.setFont(tinyFont);
        tryagainLabel.setTextFill(Color.ANTIQUEWHITE);
        gameOverLabel.setTranslateX((WIDTH / 2) - 170);
        gameOverLabel.setTranslateY(150);
        insertLabel.setTranslateX(150);
        insertLabel.setTranslateY(HEIGHT / 2 + 200);
        tryagainLabel.setTranslateX(WIDTH / 2 - 150);
        tryagainLabel.setTranslateY(HEIGHT / 2 + 150);
        //Images
        Image brokenBoy = new Image("/graphics/gameover.png");
        ImageView brokenBoyView = new ImageView(brokenBoy);
        brokenBoyView.setTranslateX(WIDTH / 2 - 150);
        brokenBoyView.setTranslateY(HEIGHT / 2 - 80);
        //Root
        new AnimationTimer() {
            int i = 0;

            public void handle(long now) {
                i++;
                if (i < 50) {
                    insertLabel.setTextFill(Color.YELLOW);
                }
                if (i > 50) {
                    insertLabel.setTextFill(Color.WHITE);
                }
                if (i == 100) i = 0;
            }
        }.start();
        root.getChildren().addAll(brokenBoyView, gameOverLabel, insertLabel, tryagainLabel);
    }

    Group returnRoot() {
        return root;
    }

    private void initialiseFonts() {
        try {
            tinyFont = Font.loadFont(new FileInputStream(new File("src/graphics/Fleftex_M.ttf")), 16);
            smallFont = Font.loadFont(new FileInputStream(new File("src/graphics/Fleftex_M.ttf")), 28);
            mediumFont = Font.loadFont(new FileInputStream(new File("src/graphics/Fleftex_M.ttf")), 54);
        } catch (FileNotFoundException e) {
            tinyFont = Font.font("Verdana", 16);
            smallFont = Font.font("Verdana", 28);
            mediumFont = Font.font("Verdana", 54);
        }
    }
}

