package sample;

import javafx.scene.image.Image;


/* An item is an object with extra methods*/
public class Item extends Object {
    final String name;
    private Boolean alive;

    Item(String name) {
        super(Type.ITEM);
        this.name = name;
        alive = true;
        sprite.offset(-7, -7);
        sprite.loadDefaultImages(new Frame("/graphics/item1.png"),
                                    new Frame("/graphics/item2.png"),
                                    new Frame("/graphics/item3.png"),
                                    new Frame("/graphics/item4.png"),
                                    new Frame("/graphics/item5.png"),
                                    new Frame("/graphics/item6.png"));
    }

    void drop (double x, double y) {
        box.setTranslateX (x);
        box.setTranslateY (y);
    }

    Boolean isAlive() {
        return this.alive;
    }

    void setAlive(Boolean value) {
        this.alive = value;
    }
}
