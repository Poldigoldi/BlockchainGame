package sample;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.ArrayList;
import java.util.List;

//set framerate to 9999 for no looping
public class Sprite extends ImageView {
    private boolean animationactive = true;
    private List<Frame> defaultAnimation = new ArrayList<>();
    private List<Frame> defaultLeftAnimation = new ArrayList<>();
    private List<Frame> defaultRightAnimation = new ArrayList<>();
    private List<Frame> motionRightAnimation = new ArrayList<>();
    private List<Frame> motionLeftAnimation = new ArrayList<>();
    private List<Frame> fallRightAnimation = new ArrayList<>();
    private List<Frame> fallLeftAnimation = new ArrayList<>();
    private List<Frame> jumpRightAnimation = new ArrayList<>();
    private List<Frame> jumpLeftAnimation = new ArrayList<>();
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

    public Frame currentFrame(){ return currentFrame;}

    //for an animated sprite

    public void setanimation(boolean active){ animationactive = active;}

    public void loadDefaultImages(Frame... images){
        animationCycle = 0;
        frameDelay = 0;
        if(defaultAnimation.size()!=0) defaultAnimation.clear();
        for(Frame image: images)
            defaultAnimation.add(image);
    }

    public void loadDefaultRightImages(Frame... images){ for(Frame image: images) defaultRightAnimation.add(image); }
    public void loadDefaultLeftImages(Frame... images){ for(Frame image: images) defaultLeftAnimation.add(image); }

    public void loadLeftMotionImages(Frame... images){ for(Frame image: images) motionLeftAnimation.add(image); }
    public void loadRightMotionImages(Frame... images){ for(Frame image: images) motionRightAnimation.add(image); }

    public void loadfallLeftImages(Frame... images){ for(Frame image: images) fallLeftAnimation.add(image); }
    public void loadfallRightImages(Frame... images){ for(Frame image: images) fallRightAnimation.add(image); }

    public void loadjumpLeftImages(Frame... images){ for(Frame image: images) jumpLeftAnimation.add(image); }
    public void loadjumpRightImages(Frame... images){ for(Frame image: images) jumpRightAnimation.add(image); }

    //updates images
    private void updateFrame(){
        if(frameDelay > currentFrame.framerate()){
            frameDelay = 0;
            animationCycle++;
        }
        frameDelay++;
        if( currentFrame.framerate()==9999) frameDelay = 0;
    }

    public void update(boolean movingRight, boolean moving, boolean isLanded, boolean movingDown, double x, double y) {
        moveTo(x, y);
        //for non-moving animations
        if (!type.hasMovementAnimation() && defaultAnimation.size()>0 && animationactive){
            animate(defaultAnimation);
        }
        //for moving animations
        //jumping
        if (type.hasMovementAnimation()) {
            if(!movingDown) {
                if (movingRight) animate(jumpRightAnimation);
                else animate(jumpLeftAnimation);
            }
            //falling
            else if(!isLanded) {
                if (movingRight) animate(fallRightAnimation);
                else animate(fallLeftAnimation);
            }
            //default (not currently moving)
            else if(!moving) {
                if (movingRight) animate(defaultRightAnimation);
                else animate(defaultLeftAnimation);
            }
            //moving left/right
            else if(moving) {
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
