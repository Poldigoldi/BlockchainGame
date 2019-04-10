package sample;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.Node;
import java.util.ArrayList;
import java.util.List;

public class Sprite extends ImageView {
    private Node owner;
    private Image defaultRight;
    private Image defaultLeft;
    private List<Image> motionRight = new ArrayList<>();
    private List<Image> motionLeft = new ArrayList<>();
    private int animationCycle;
    private int frameRate = 5;
    private int frameDelay;

    public Sprite(Node owner){
        this.owner = owner;
    }

    public void loadDefaultImages(Image defaultRight, Image defaultLeft){
        this.defaultRight = defaultRight;
        this.defaultLeft = defaultLeft;
    }

    public void loadLeftMotionImages(Image... images){
        for(Image image: images) motionLeft.add(image);
    }

    public void loadRightMotionImages(Image... images){
        for(Image image: images) motionRight.add(image);
    }

    public void update(int direction, boolean moving){
        if(owner!=null) {
            this.setX(owner.getTranslateX());
            this.setY(owner.getTranslateY());
        }
        if(moving == false){
            if(direction>0) setImage(defaultRight);
            else setImage(defaultLeft);
        } else animate(direction);
    }

    private void animate(int direction){
        if(frameDelay > frameRate){
            frameDelay = 0;
            animationCycle++;
        }
        frameDelay++;
        if(direction>0){
            if(animationCycle>=motionRight.size()) animationCycle = 0;
            setImage(motionRight.get(animationCycle));
        } else {
            if(animationCycle>=motionLeft.size()) animationCycle = 0;
            setImage(motionLeft.get(animationCycle));
        }
    }
}
