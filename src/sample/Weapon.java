package sample;

import javafx.scene.paint.Color;

public class Weapon extends Collectable {

    private final String name;
    private boolean canShoot;
    private int bullets;

    Weapon (String name, int bullets) {
        super(new Frame("/graphics/item1.png"));
        this.setCollisionBox (200, 500, 20, 20, Color.BLUEVIOLET);
        sprite.offset (-5, -10);
        this.alive = true;
        this.canShoot = true;
        this.name = name;
        this.bullets = bullets;
        setItemType (Type.WEAPON);
    }

    public String getName() {
        return name;
    }

    public boolean isCanShoot() {
        return canShoot;
    }

    public void setCanShoot(boolean canShoot) {
        this.canShoot = canShoot;
    }

    public void looseOneBullet () {
        bullets--;
    }

    public void getOneBullet () {
        bullets++;
    }

    public int getBullets() {
        return bullets;
    }
}
