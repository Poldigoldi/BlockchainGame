package sample;


import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;

import java.nio.file.Paths;

/*Nick made major changes to this class. A player is an Object that can JUMP and hold items */
public class Player extends Person {
    //sounds
    AudioClip jumpSound = new AudioClip(Paths.get("src/sound/jump.wav").toUri().toString());

    //variables
    private int WIDTH = 30, HEIGHT = 60;
    private boolean canDie;
    private String name;
    private Luggage luggage;
    private String facing;

    Player(String name, int STARTX, int STARTY, int START_LIVES) {
        super(Type.PLAYER, START_LIVES);
        setCollisionBox(STARTX, STARTY, WIDTH, HEIGHT,Color.BLUE);
        this.canDie = true;
        this.name = name;
        this.luggage = new Luggage();
        this.facing = "RIGHT";
        initialise();
        //since the image is size 64, but the player collision box is 30/60, some offset is required.
        sprite().offset(-(64-WIDTH)/2, -(64- HEIGHT));
    }


    void jump() {
        if (canJump) {
            jumpSound.play();
            this.Velocity = this.Velocity.add (0, -30);
            canJump =false;
        }
    }

    boolean hasWeapon() {
        return luggage.getWeapon () != null;
    }

    boolean canUseWeapon() {
        if (luggage.getWeapon ().isCanShoot () && luggage.getWeapon ().getBullets () > 0) {
            return true;
        }
        return false;
    }

    //set up the images for walking etc.
    private void initialise() {
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
        sprite.loadfallLeftImages(
                new Frame("/graphics/main_char_fallLeft1.png", 10),
                new Frame("/graphics/main_char_fallLeft2.png", 10));
        sprite.loadfallRightImages(
                new Frame("/graphics/main_char_fallRight1.png", 10),
                new Frame("/graphics/main_char_fallRight2.png", 10));
        sprite.loadjumpLeftImages(
                new Frame("/graphics/main_char_jumpLeft1.png", 10));
        sprite.loadjumpRightImages(
                new Frame("/graphics/main_char_jumpRight1.png", 10));

    }

    /* ------------ GETTERS & SETTERS -------------- */


    Luggage getLuggage () {
        return this.luggage;
    }

    public String getFacing() {
        return facing;
    }

    public void setFacing(String facing) {
        this.facing = facing;
    }
}
