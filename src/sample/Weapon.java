package sample;

import javafx.animation.AnimationTimer;
import javafx.scene.transform.Rotate;

public class Weapon extends Collectable {

    private final String name;
    private boolean canShoot;
    private int bullets;
    private int bulletShot;

    /* Defines maximum shooting level before player capped */
    private final int MAX_BULLET_SHOT = 30;

    Weapon(String name, int bullets) {
        super(new Frame("/graphics/weapon.png"));
        sprite.offset(-20, -35);
        this.alive = true;
        this.canShoot = true;
        this.name = name;
        this.bullets = bullets;
        setItemType(Type.WEAPON);
        sprite.setRotationAxis(Rotate.Y_AXIS);
        new AnimationTimer() {
            int i;

            public void handle(long now) {
                i += 2;
                sprite.setRotate(i);
            }
        }.start();
        this.bulletShot = 0;
    }

    void doesntShoot() {
        if (bulletShot > 0) {
            bulletShot--;
        }
    }

    private boolean isMaxShootReached() {
        return bulletShot >= MAX_BULLET_SHOT;
    }

    // Player shooting frequency capped to a certain level
    boolean isCanShoot() {
        return canShoot && !isMaxShootReached();
    }

    void setCanShoot(boolean canShoot) {
        this.canShoot = canShoot;
    }

    void winOneBullet() {
        bullets++;
    }

    void looseOneBullet() {
        bullets--;
        bulletShot += 12;
    }

    int getBullets() {
        return bullets;
    }


    String getName() {
        return name;
    }
}
