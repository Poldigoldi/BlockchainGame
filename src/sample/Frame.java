package sample;

import javafx.scene.image.Image;

public class Frame extends Image {
    private int DEFAULTFRAMERATE = 5;
    private int framerate = DEFAULTFRAMERATE;

    public Frame(String imageURL, int framerate) {
        super(imageURL);
        this.framerate = framerate;
    }

    public Frame(String imageURL) {
        super(imageURL);
    }

    int framerate() {
        return framerate;
    }
}
