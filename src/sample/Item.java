package sample;

import javafx.scene.image.Image;


/* An item is an object with extra methods*/
public class Item extends Object {
    final String name;
    private Boolean alive;

    Item(String name) {
        super(Type.ITEM, new Image("/graphics/item1.png"));
        this.name = name;
        alive = true;
        sprite.offset(-7, -7);
        sprite.loadDefaultAnimation(new Image("/graphics/item1.png"),
                                    new Image("/graphics/item2.png"),
                                    new Image("/graphics/item3.png"),
                                    new Image("/graphics/item4.png"),
                                    new Image("/graphics/item5.png"),
                                    new Image("/graphics/item6.png"));
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
