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

public class GameMenu {
    private int WIDTH, HEIGHT;
    private boolean startGame = false;
    private boolean instructions = false;
    private Font teenytinyFont, tinyFont, smallFont, mediumFont;
    Group root = new Group();

    public GameMenu(int WIDTH, int HEIGHT) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        initialiseFonts();

        //Labels
        Label gameInfo = new Label("Learn Blockchain the fun way!");
        gameInfo.setFont(smallFont);
        gameInfo.setTranslateX(150);
        gameInfo.setTranslateY(HEIGHT/2+220);

        Label creators = new Label("An educational game created by the Inglorious Basterds:\n\n" +
                "Nick Wilson\n" +
                "Nadia Zyborska\n" +
                "Come Tisserand\n" +
                "Harvey Benjen\n" +
                "Joe Williams\n" +
                "Leonard Goldschmidt");
        creators.setFont(teenytinyFont);
        creators.setTranslateX(5);
        creators.setTranslateY(5);

        //Buttons
        Button startGame = new Button("START");
        startGame.setFont(tinyFont);
        startGame.setTranslateX(WIDTH/2-50);
        startGame.setTranslateY(HEIGHT/2+100);

        Button instructions = new Button("INSTRUCTIONS");
        instructions.setFont(tinyFont);
        instructions.setTranslateX(WIDTH/2-80);
        instructions.setTranslateY(HEIGHT/2+150);

        //Images
        Image blockchainAdventure = new Image("/graphics/blockchainAdventure.png");
        ImageView adventureView = new ImageView(blockchainAdventure);
        adventureView.setFitWidth(WIDTH-500);
        adventureView.setFitHeight(HEIGHT-300);
        adventureView.setTranslateX(250);
        adventureView.setTranslateY(50);

        Image menuBackground = new Image("/graphics/menuBackground.png");
        ImageView backgroundView = new ImageView(menuBackground);
        backgroundView.setFitWidth(WIDTH);
        backgroundView.setFitHeight(HEIGHT);
        backgroundView.setTranslateX(0);
        backgroundView.setFitHeight(0);

        //Root
        new AnimationTimer() {
            int i = 0;
            public void handle(long now) {
                i++;
                if(i < 50){ gameInfo.setTextFill(Color.DEEPPINK); }
                if(i > 50){ gameInfo.setTextFill(Color.WHITE); }
                if(i == 100) i = 0;
            }
        }.start();
        root.getChildren().addAll(backgroundView, adventureView, creators, gameInfo, startGame, instructions);
        startGame.setOnAction(this::startPress);
        instructions.setOnAction(this::instructionPress);

    }

    public boolean isStartGame() {
        return startGame;
    }

    public void setStartAgain() {
        this.startGame = false;
    }

    public boolean isInstructionsPressed() {return instructions;}

    public void resetInstructionPress() {instructions = false;}

    public Group returnRoot() {
        return root;
    }

    public void startPress(ActionEvent e) {
        System.out.println("click");
        startGame = true;
    }

    public void instructionPress(ActionEvent e) {
        System.out.println("Instruction click");
        instructions = true;
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
