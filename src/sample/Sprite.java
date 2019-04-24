package sample;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.ArrayList;
import java.util.List;

public class Sprite extends ImageView {
    private List<Frame> defaultAnimation = new ArrayList<>();
    private List<Frame> defaultLeftAnimation = new ArrayList<>();
    private List<Frame> defaultRightAnimation = new ArrayList<>();
    private List<Frame> motionRightAnimation = new ArrayList<>();
    private List<Frame> motionLeftAnimation = new ArrayList<>();
    private int animationCycle;
    private Frame currentFrame;
    private int frameDelay;
    private int xoffset, yoffset;
    private Type type;

    public void moveTo(double x, double y){
        setTranslateX(x + xoffset);
        setTranslateY(y + yoffset);
    }

    public void offset(int x, int y){
        xoffset = x;
        yoffset = y;
    }

    //for a non-animating sprite
    public Sprite(Type type, Frame... defaultFrame){
        this.type = type;
        if(defaultFrame.length == 0) {
            Frame noDefaultSpecified = new Frame("/graphics/noimage.png");
            setImage(noDefaultSpecified);
            currentFrame = noDefaultSpecified;
        }
        else {
            setImage(defaultFrame[0]);
            currentFrame = defaultFrame[0];
        }
    }



    //for an animated sprite

    public void loadDefaultRightImages(Frame... images){ for(Frame image: images) defaultRightAnimation.add(image); }

    public void loadDefaultLeftImages(Frame... images){ for(Frame image: images) defaultLeftAnimation.add(image); }

    public void loadDefaultImages(Frame... images){
        for(Frame image: images)
            defaultAnimation.add(image);
    }

    public void loadLeftMotionImages(Frame... images){ for(Frame image: images) motionLeftAnimation.add(image); }

    public void loadRightMotionImages(Frame... images){ for(Frame image: images) motionRightAnimation.add(image); }

    //updates images
    private void updateFrame(){
        if(frameDelay > currentFrame.framerate()){
            frameDelay = 0;
            animationCycle++;
        }
        frameDelay++;
    }

    public void update(boolean movingRight, boolean moving, double x, double y) {
        moveTo(x, y);
        if (!type.hasMovementAnimation() && defaultAnimation.size()>0){
            animate(defaultAnimation);
        }
        if (type.hasMovementAnimation()) {
            if(!moving) {
                if (movingRight) animate(defaultRightAnimation);
                else animate(defaultLeftAnimation);
            }
            if(moving) {
                if (movingRight) animate(motionRightAnimation);
                else animate(motionLeftAnimation);
            }
        }
    }

    private void animate(List<Frame> images){
        updateFrame();
        if(images.size()==0) return;
        if(animationCycle>=images.size()) animationCycle = 0;
        currentFrame = images.get(animationCycle);
        setImage(images.get(animationCycle));
    }
}
