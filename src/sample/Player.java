package sample;


import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/*Nick made major changes to this class. A player is an Object that can JUMP and hold items */
public class Player extends Object {
    private int width = 34, height = 60;
    private String name;
    private Luggage luggage = new Luggage();
    private Stage stage;

    public Player(String name, int STARTX, int STARTY, Stage stage) {
        super(0, 0, true, Type.PLAYER, new Image("/defaultRight.png"));
        this.stage = stage;
        setTranslateX(STARTX);
        setTranslateY(STARTY);
        this.name = name;
        setCollisionBox(width,height,Color.PINK);
        sprite().offset(-(64-width)/2, -(64-height));
    }

    Luggage getLuggage () {
        return this.luggage;
    }

    void jump() {
        if (CanJump) {
            this.Velocity = this.Velocity.add (0, -30);
            CanJump=false;
        }
    }

    void useItem(Item item) {
        /* Do Something with item .... */
    }


    //set up the images for walking etc.
    public void initialise() {
        sprite.loadDefaultImages(new Image("/defaultright.png"), new Image("/defaultleft.png"));
        sprite.loadRightMotionImages(new Image("/motionRight0.png"), new Image("/motionRight1.png"),
                new Image("/motionRight2.png"), new Image("/motionRight3.png"), new Image("/motionRight4.png"));
        sprite.loadLeftMotionImages(new Image("/motionLeft0.png"), new Image("/motionLeft1.png"),
                new Image("/motionLeft2.png"), new Image("/motionLeft3.png"), new Image("/motionLeft4.png"));
    }

}
