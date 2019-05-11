package sample;

import javafx.scene.paint.Color;

public class Weapon extends Collectable {

    final String name;

    Weapon (String name) {
        super(new Frame("/graphics/item1.png"));
        this.setCollisionBox (200, 500, 20, 20, Color.BLUEVIOLET);
        sprite.offset (-5, -10);
        this.alive = true;
        this.name = name;
        setItemType (Type.WEAPON);
    }
}
