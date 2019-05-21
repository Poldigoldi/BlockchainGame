package sample;

import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//set framerate to 9999 for no looping
public class Sprite extends ImageView {
    private boolean animationactive = true;
    private int defaultAnimationChoice = 1;
    private List<Frame> hurtAnimation = new ArrayList<>();
    private List<Frame> defaultAnimation1 = new ArrayList<>();
    private List<Frame> defaultAnimation2 = new ArrayList<>();
    private List<Frame> defaultLeftAnimation = new ArrayList<>();
    private List<Frame> defaultRightAnimation = new ArrayList<>();
    private List<Frame> motionRightAnimation = new ArrayList<>();
    private List<Frame> motionLeftAnimation = new ArrayList<>();
    private List<Frame> fallRightAnimation = new ArrayList<>();
    private List<Frame> fallLeftAnimation = new ArrayList<>();
    private List<Frame> jumpRightAnimation = new ArrayList<>();
    private List<Frame> jumpLeftAnimation = new ArrayList<>();
    private List<Frame> deathAnimation = new ArrayList<>();
    private int animationCycle;
    private Frame currentFrame;
    private int frameDelay;
    private boolean dying = false;
    private boolean dead = false;
    private int xoffset, yoffset;
    private Type type;

    void moveTo(double x, double y) {
        if (!dying) {
            setTranslateX(x + xoffset);
            setTranslateY(y + yoffset);
        }
    }

    public void offset(int x, int y) {
        xoffset = x;
        yoffset = y;
    }

    //for a non-animating sprite
    Sprite(Type type, Frame... defaultFrame) {
        this.type = type;
        if (defaultFrame.length == 0) {
            Frame noDefaultSpecified = new Frame("/graphics/noimage.png");
            setImage(noDefaultSpecified);
            currentFrame = noDefaultSpecified;
        } else {
            setImage(defaultFrame[0]);
            currentFrame = defaultFrame[0];
        }
    }

    //for an animated sprite

    void setanimation(boolean active) {
        animationactive = active;
    }

    public boolean isAnimationActive() {
        return animationactive;
    }

    void setdefaultanimationchoice(int choice) {
        if (defaultAnimationChoice != choice) {
            defaultAnimationChoice = choice;
            animationCycle = 0;
            frameDelay = 0;
        }
    }

    void loadDefaultImages(Frame... images) {
        animationCycle = 0;
        frameDelay = 0;
        if (defaultAnimation1.size() != 0) defaultAnimation1.clear();
        Collections.addAll(defaultAnimation1, images);
    }

    void loadDefault2Images(Frame... images) {
        animationCycle = 0;
        frameDelay = 0;
        if (defaultAnimation2.size() != 0) defaultAnimation2.clear();
        Collections.addAll(defaultAnimation2, images);
    }

    void loadHurtImages(Frame... images) {
        Collections.addAll(hurtAnimation, images);
    }

    void loadDeathImages(Frame... images) {
        Collections.addAll(deathAnimation, images);
    }

    void loadDefaultRightImages(Frame... images) {
        Collections.addAll(defaultRightAnimation, images);
    }

    void loadDefaultLeftImages(Frame... images) {
        Collections.addAll(defaultLeftAnimation, images);
    }

    void loadLeftMotionImages(Frame... images) {
        Collections.addAll(motionLeftAnimation, images);
    }

    void loadRightMotionImages(Frame... images) {
        Collections.addAll(motionRightAnimation, images);
    }

    void loadfallLeftImages(Frame... images) {
        Collections.addAll(fallLeftAnimation, images);
    }

    void loadfallRightImages(Frame... images) {
        Collections.addAll(fallRightAnimation, images);
    }

    void loadjumpLeftImages(Frame... images) {
        Collections.addAll(jumpLeftAnimation, images);
    }

    void loadjumpRightImages(Frame... images) {
        Collections.addAll(jumpRightAnimation, images);
    }

    //updates images
    private void updateFrame() {
        if (frameDelay > currentFrame.framerate()) {
            frameDelay = 0;
            animationCycle++;
        }
        frameDelay++;
        if (currentFrame.framerate() == 9999) frameDelay = 0;
    }

    void activateDeathAnimation() {
        if (!dying) {
            dying = true;
            animationCycle = 0;
            frameDelay = 0;
        }
    }

    public void update(boolean spinning, boolean movingRight, boolean moving, boolean isLanded, boolean movingDown, double x, double y) {
        moveTo(x, y);
        if (dying) {
            animate(deathAnimation);
            return;
        }
        if(spinning && type.hasHurtAnimation()){
            animate(hurtAnimation);
            return;
        }
        //for non-moving animations
        if (!type.hasMovementAnimation() && defaultAnimation1.size() > 0 && animationactive) {
            if (defaultAnimationChoice == 1) animate(defaultAnimation1);
            if (defaultAnimationChoice == 2) animate(defaultAnimation2);
        }
        //for moving animations
        //jumping
        if (type.hasMovementAnimation()) {
            if (!movingDown && type.hasJumpingAnimation()) {
                if (movingRight) animate(jumpRightAnimation);
                else animate(jumpLeftAnimation);
            }
            //falling
            else if (!isLanded && type.hasFallingAnimation()) {
                if (movingRight) animate(fallRightAnimation);
                else animate(fallLeftAnimation);
            }
            //default (not currently moving)
            else if (!moving) {
                if (movingRight) animate(defaultRightAnimation);
                else animate(defaultLeftAnimation);
            }
            //moving left/right
            else {
                if (movingRight) animate(motionRightAnimation);
                else animate(motionLeftAnimation);
            }
        }
    }

    private void animate(List<Frame> images) {
        updateFrame();
        if (images.size() == 0) return;
        if (dying && animationCycle == images.size() - 1) dead = true;
        if (animationCycle >= images.size()) {
            animationCycle = 0;
        }
        currentFrame = images.get(animationCycle);
        setImage(images.get(animationCycle));
    }

    boolean dead() {
        return dead;
    }
}
