package sample;

import javafx.scene.image.Image;


/* An item is an object with extra methods*/
public class Item extends Object {
    final String name;
    private Boolean alive;

    Item(String name, int STARTX, int STARTY) {
        super(0, 0, true, Type.ITEM, new Image("/item1.png"));
        sprite.loadDefaultAnimation(new Image("/item1.png"), new Image("/item2.png"), new Image("/item3.png"),
                                    new Image("/item4.png"), new Image("/item5.png"), new Image("/item6.png"));
        setTranslateX(STARTX);
        setTranslateY(STARTY);
        this.name = name;
        this.alive = true;
    }


    void drop (double x, double y) {
        setTranslateX (x);
        setTranslateY (y);
    }

    Boolean isAlive() {
        return this.alive;
    }

    void setAlive(Boolean value) {
        this.alive = value;
    }
}
