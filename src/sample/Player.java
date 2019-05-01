package sample;


import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.nio.file.Paths;

/*Nick made major changes to this class. A player is an Object that can JUMP and hold items */
public class Player extends Object {
    //sounds
    AudioClip jumpSound = new AudioClip(Paths.get("src/sound/jump.wav").toUri().toString());

    //variables
    private int WIDTH = 30, HEIGHT = 60;
    private String name;
    private Luggage luggage = new Luggage();
    private Stage stage;

    public Player(String name, int STARTX, int STARTY, Stage stage) {
        super(Type.PLAYER);
        setCollisionBox(STARTX, STARTY, WIDTH, HEIGHT,Color.BLUE);
        this.stage = stage;
        this.name = name;
        //since the image is size 64, but the player collision box is 30/60, some offset is required.
        sprite().offset(-(64-WIDTH)/2, -(64- HEIGHT));
    }

    Luggage getLuggage () {
        return this.luggage;
    }

    void jump() {
        if (CanJump) {
            jumpSound.play();
            this.Velocity = this.Velocity.add (0, -30);
            CanJump=false;
        }
    }

    void useItem(Item item) {
        /* Do Something with item .... */
    }


    //set up the images for walking etc.
    public void initialise() {
        sprite.loadDefaultLeftImages(
                new Frame("/graphics/defaultleft.png", 150),
                new Frame("/graphics/defaultleft2.png", 10));
        sprite.loadDefaultRightImages(
                new Frame("/graphics/defaultright.png", 150),
                new Frame("/graphics/defaultright2.png", 10));
        sprite.loadRightMotionImages(
                new Frame("/graphics/motionRight0.png"),
                new Frame("/graphics/motionRight1.png"),
                new Frame("/graphics/motionRight2.png"),
                new Frame("/graphics/motionRight3.png"),
                new Frame("/graphics/motionRight4.png"));
        sprite.loadLeftMotionImages(
                new Frame("/graphics/motionLeft0.png"),
                new Frame("/graphics/motionLeft1.png"),
                new Frame("/graphics/motionLeft2.png"),
                new Frame("/graphics/motionLeft3.png"),
                new Frame("/graphics/motionLeft4.png"));
    }

}
