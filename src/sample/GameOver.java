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


public class GameOver {
    private int WIDTH, HEIGHT;
    private boolean startAgain = false;
    private Font smallFont, mediumFont;
    Group root = new Group();

    public GameOver(int WIDTH, int HEIGHT) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        initialiseFonts();
        //Labels
        Label gameOverLabel = new Label("GAME OVER");
        Label insertLabel = new Label(">>INSERT BITCOIN TO CONTINUE<<");
        gameOverLabel.setFont(mediumFont);
        gameOverLabel.setTextFill(Color.ANTIQUEWHITE);
        insertLabel.setFont(smallFont);
        gameOverLabel.setTranslateX((WIDTH/2)-170);
        gameOverLabel.setTranslateY(150);
        insertLabel.setTranslateX(150);
        insertLabel.setTranslateY(HEIGHT/2+200);
        //Buttons
        Button tryAgain = new Button("TRY AGAIN?");
        tryAgain.setTranslateX(WIDTH/2-50);
        tryAgain.setTranslateY(HEIGHT/2+150);
        //Images
        Image brokenBoy = new Image("/graphics/gameover.png");
        ImageView brokenBoyView = new ImageView(brokenBoy);
        brokenBoyView.setTranslateX(WIDTH/2 - 150);
        brokenBoyView.setTranslateY(HEIGHT/2-80);
        //Root
        new AnimationTimer() {
            int i = 0;
            public void handle(long now) {
                i++;
                if(i < 50){ insertLabel.setTextFill(Color.YELLOW); }
                if(i > 50){ insertLabel.setTextFill(Color.WHITE); }
                if(i == 100) i = 0;
            }
        }.start();
        root.getChildren().addAll(brokenBoyView, gameOverLabel, insertLabel, tryAgain);
        tryAgain.setOnAction(this::tryAgainPress);
    }

    public boolean isStartAgain() {
        return startAgain;
    }

    public void setStartAgain() {
        this.startAgain = false;
    }




    public Group returnRoot() {
        return root;
    }

    public void tryAgainPress(ActionEvent e) {
        startAgain = true;
    }

    private void initialiseFonts(){
            try {
                smallFont = Font.loadFont(new FileInputStream(new File("src/graphics/Fleftex_M.ttf")), 28);
                mediumFont = Font.loadFont(new FileInputStream(new File("src/graphics/Fleftex_M.ttf")), 54);
            } catch (FileNotFoundException e) {
                smallFont = Font.font("Verdana", 28);
                mediumFont = Font.font("Verdana", 54);
            }
        }
    }

