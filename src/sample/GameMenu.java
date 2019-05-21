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
import javafx.scene.transform.Rotate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;


/*Geme Menu Winow that appears at the start of the game*/
class GameMenu {
    private Font teenytinyFont;
    private Font tinyFont;
    private Font smallFont;
    private Group root = new Group();
    private Button startGame = new Button("START");
    private Button instructions = new Button("INSTRUCTIONS");
    private int WIDTH, HEIGHT;
    private ArrayList<Object> clouds = new ArrayList<>();

    GameMenu(int WIDTH, int HEIGHT) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        initialiseFonts();

        //Labels
        Label gameInfo = new Label("Learn Blockchain the fun way!");
        gameInfo.setFont(smallFont);
        gameInfo.setTranslateX(WIDTH / 2 - 300);
        gameInfo.setTranslateY(HEIGHT / 2 + 220);

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
        startGame.setFont(tinyFont);
        startGame.setStyle("-fx-background-color: #000000;  -fx-text-fill: #39ff14; -fx-opacity: 1;");
        startGame.setTranslateX(WIDTH / 2 - 50);
        startGame.setTranslateY(HEIGHT / 2 + 100);

        instructions.setFont(tinyFont);
        instructions.setStyle("-fx-background-color: #000000;  -fx-text-fill: #39ff14; -fx-opacity: 1;");
        instructions.setTranslateX(WIDTH / 2 - 80);
        instructions.setTranslateY(HEIGHT / 2 + 150);

        //Images
        Image blockchainAdventure = new Image("/graphics/blockchainAdventure.png");
        ImageView adventureView = new ImageView(blockchainAdventure);
        adventureView.setFitWidth(WIDTH - 500);
        adventureView.setFitHeight(HEIGHT - 300);
        adventureView.setTranslateX(250);
        adventureView.setTranslateY(50);
        ImageView sampleBackground = new ImageView("/graphics/background3.png");
        sampleBackground.setTranslateX((WIDTH * 0.5) - (580 * 0.5));
        sampleBackground.setRotationAxis(Rotate.Y_AXIS);
        ImageView menuBackground = new ImageView("/graphics/title.png");
        menuBackground.setTranslateX(-(1700 - WIDTH) * 0.5);
        menuBackground.setTranslateY(-200);

        createLayer3Clouds();
        root.getChildren().add(sampleBackground);
        createLayer1Clouds();
        root.getChildren().add(menuBackground);
        //Root
        new AnimationTimer() {
            int i = 0;

            public void handle(long now) {
                i += 0.1;
                for (Object cloud : clouds) {
                    circumnavigate(cloud);
                }
                if (i < 50) {
                    gameInfo.setTextFill(Color.DEEPPINK);
                }
                if (i > 50) {
                    gameInfo.setTextFill(Color.WHITE);
                }
                if (i == 100) i = 0;
            }
        }.start();
        new AnimationTimer() {
            double i;

            public void handle(long now) {
                // i += 0.3;
                sampleBackground.setRotate(i);
            }
        }.start();
        root.getChildren().addAll(creators, startGame, instructions);
    }

    Button startGame() {
        return startGame;
    }

    Button instructions() {
        return instructions;
    }

    Group returnRoot() {
        return root;
    }

    private void initialiseFonts() {
        try {
            teenytinyFont = Font.loadFont(new FileInputStream(new File("src/graphics/Fleftex_M.ttf")), 8);
            tinyFont = Font.loadFont(new FileInputStream(new File("src/graphics/Fleftex_M.ttf")), 15);
            smallFont = Font.loadFont(new FileInputStream(new File("src/graphics/Fleftex_M.ttf")), 28);
        } catch (FileNotFoundException e) {
            teenytinyFont = Font.font("Verdana", 15);
            tinyFont = Font.font("Verdana", 15);
            smallFont = Font.font("Verdana", 28);
        }
    }

    private void createLayer3Clouds() {
        Object cloud = new Object(Type.LAYER3, new Frame("/graphics/clouds1.png"));
        cloud.setCollisionBox(random(0, WIDTH), random(0, (HEIGHT * 0.8)), 5, 5, Color.CORAL);
        Object cloud3 = new Object(Type.LAYER3, new Frame("/graphics/clouds3.png"));
        cloud3.setCollisionBox(random(0, WIDTH), random(0, (HEIGHT * 0.8)), 5, 5, Color.CORAL);
        root.getChildren().addAll(cloud.box, cloud.sprite());
        root.getChildren().addAll(cloud3.box, cloud3.sprite());
        clouds.add(cloud3);
        clouds.add(cloud);
    }


    private void createLayer1Clouds() {
        Frame[] L2Clouds = {
                new Frame("/graphics/smallcloud1.png"), new Frame("/graphics/smallcloud2.png"),
                new Frame("/graphics/smallcloud3.png"), new Frame("/graphics/smallcloud4.png"),
                new Frame("/graphics/smallcloud5.png"), new Frame("/graphics/smallcloud6.png"),
                new Frame("/graphics/smallcloud7.png"), new Frame("/graphics/smallcloud8.png"),
                new Frame("/graphics/smallcloud11.png")};
        for (Frame cloudImage : L2Clouds) {
            Object cloud = new Object(Type.LAYER1, cloudImage);
            cloud.setCollisionBox(random(0, WIDTH), random(0, (HEIGHT * 0.8)), 5, 5, Color.YELLOWGREEN);
            root.getChildren().addAll(cloud.box, cloud.sprite());
            clouds.add(cloud);
        }

    }

    private void circumnavigate(Object object) {
        double speed;
        if (object.type == Type.LAYER1) {
            speed = 0.2;
        } else speed = 0.05;
        object.sprite.setTranslateX(object.sprite.getTranslateX() + speed);
        if (object.sprite.getTranslateX() > WIDTH) object.sprite.setTranslateX(-object.sprite.getImage().getWidth());
    }

    public static double random(double min, double max) {
        return (Math.random() * ((max - min) + 1)) + min;
    }
}
