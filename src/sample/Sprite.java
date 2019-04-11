package sample;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.ArrayList;
import java.util.List;

public class Sprite extends ImageView {
    private Image defaultImage;
    private Image defaultRight;
    private Image defaultLeft;
    private List<Image> defaultAnimation = new ArrayList<>();
    private List<Image> motionRight = new ArrayList<>();
    private List<Image> motionLeft = new ArrayList<>();
    private int animationCycle;
    private int frameRate = 5;
    private int frameDelay;

    public void offset(int x, int y){
        setTranslateX(x);
        setTranslateY(y);
    }

    //for a non-animating sprite
    public Sprite(Image defaultImage){
        this.defaultImage = defaultImage;
        setImage(this.defaultImage);
    }

    //for an animated sprite

    public void loadDefaultImages(Image defaultRight, Image defaultLeft){
        this.defaultRight = defaultRight;
        this.defaultLeft = defaultLeft;
    }

    public void loadDefaultAnimation(Image... images){ for(Image image: images) defaultAnimation.add(image); }

    public void loadLeftMotionImages(Image... images){
        for(Image image: images) motionLeft.add(image);
    }

    public void loadRightMotionImages(Image... images){
        for(Image image: images) motionRight.add(image);
    }

    //updates images
    private void updateFrame(){
        if(frameDelay > frameRate){
            frameDelay = 0;
            animationCycle++;
        }
        frameDelay++;
    }

    public void update(){
        updateFrame();
        if(animationCycle>=defaultAnimation.size()) animationCycle = 0;
        setImage(defaultAnimation.get(animationCycle));
    }

    public void update(int direction, boolean moving){
        if(moving == false){
            if(direction>0) setImage(defaultRight);
            else setImage(defaultLeft);
        } else animate(direction);
    }

    private void animate(int direction){
        updateFrame();
        if(direction>0){
            if(animationCycle>=motionRight.size()) animationCycle = 0;
            setImage(motionRight.get(animationCycle));
        } else {
            if(animationCycle>=motionLeft.size()) animationCycle = 0;
            setImage(motionLeft.get(animationCycle));
        }
    }
}
