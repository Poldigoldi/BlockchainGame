package sample;

import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

public class Weapon extends Collectable {

    private final String name;
    private boolean canShoot;
    private int bullets;

    Weapon (String name, int bullets) {
        super(new Frame("/graphics/weapon.png"));
        this.setCollisionBox (200, 500, 25, 25, Color.BLUEVIOLET);
        sprite.offset (-20, -35);
        this.alive = true;
        this.canShoot = true;
        this.name = name;
        this.bullets = bullets;
        setItemType (Type.WEAPON);
        sprite.setRotationAxis(Rotate.Y_AXIS);
        new AnimationTimer() {
            int i;
            public void handle(long now) {
                i+=2;
                sprite.setRotate(i);
            }
        }.start();
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

    public void winOneBullet () {
        bullets++;
    }

    public void looseOneBullet () {
        bullets--;
    }

    public int getBullets() {
        return bullets;
    }

}
