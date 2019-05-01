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

public class InstructionScreen {
    private int WIDTH, HEIGHT;
    private boolean return_to_menu = false;
    private Font teenytinyFont, tinyFont, smallFont, mediumFont;
    Group root = new Group();

    public InstructionScreen(int WIDTH, int HEIGHT) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        initialiseFonts();

        //Labels
        Label header = new Label("INSTRUCTIONS:");
        header.setFont(mediumFont);
        header.setTranslateX(250);
        header.setTranslateY(HEIGHT/30);

        Label instructionsText = new Label(
                "Controls:\n\n" +
                        "Left and right arrow keys to move the robot.\n" +
                "Space to jump.\n" +
                        "Y to pick up a block.\n" +
                        "X to drop a block.\n\n" +
                        "Goal:\n\n" +
                        "Collect enough blocks to build a Blockchain bridge, which will \n" +
                        "get you over the gap. Of course, there is a catch! In order to \n" +
                        "pick up a block you need to complete a puzzle. Well, it would\n" +
                        "defeat the purpose to give you the answer, so go ahead an pick up\n" +
                        "a block and get puzzling.\n" +
                        "Oh, before I forget. Beware of the vicious enemy robots\n\n" +
                        "Let's learn some Blockchain!");
        instructionsText.setFont(tinyFont);
        instructionsText.setTranslateX(5);
        instructionsText.setTranslateY(130);

        //Buttons
        Button returnToMenu = new Button("Return to Menu");
        returnToMenu.setFont(tinyFont);
        returnToMenu.setTranslateX(WIDTH-200);
        returnToMenu.setTranslateY(HEIGHT-80);

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
                if(i < 50){ header.setTextFill(Color.DEEPPINK); }
                if(i > 50){ header.setTextFill(Color.WHITE); }
                if(i == 100) i = 0;
            }
        }.start();
        root.getChildren().addAll(backgroundView, instructionsText, header, returnToMenu);
        returnToMenu.setOnAction(this::startPress);
    }

    public Group returnRoot() {
        return root;
    }

    public boolean isReturn_to_menu() {
        return return_to_menu;
    }

    public void setReturn_to_menu() {
        this.return_to_menu = false;
    }

    public void startPress(ActionEvent e) {
        System.out.println("click");
        return_to_menu = true;
    }

    private void initialiseFonts(){
        try {
            teenytinyFont = Font.loadFont(new FileInputStream(new File("src/graphics/Fleftex_M.ttf")), 8);
            tinyFont = Font.loadFont(new FileInputStream(new File("src/graphics/Fleftex_M.ttf")), 15);
            smallFont = Font.loadFont(new FileInputStream(new File("src/graphics/Fleftex_M.ttf")), 28);
            mediumFont = Font.loadFont(new FileInputStream(new File("src/graphics/Fleftex_M.ttf")), 54);
        } catch (FileNotFoundException e) {
            teenytinyFont = Font.font("Verdana", 15);
            tinyFont = Font.font("Verdana", 15);
            smallFont = Font.font("Verdana", 28);
            mediumFont = Font.font("Verdana", 54);
        }
    }
}
