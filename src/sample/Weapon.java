package sample;

import javafx.animation.AnimationTimer;
import javafx.scene.transform.Rotate;

public class Weapon extends Collectable {

    private final String name;
    private boolean canShoot;
    private int bullets;
    private int bulletShot;

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


    String getName() {
        return name;
    }

    private boolean isMaxShootReached () {
        return bulletShot >= MAX_BULLET_SHOT;
    }

    // Player shooting frequency capped to a certain level
    boolean isCanShoot() {
        if (canShoot) {
            if (! isMaxShootReached ()) {
                return true;
            }
        }
        return false;
    }

    void setCanShoot(boolean canShoot) {
        this.canShoot = canShoot;
    }

    public void winOneBullet() {
        bullets++;
    }

    void looseOneBullet() {
        bullets--;
        bulletShot += 12;
    }

    int getBullets() {
        return bullets;
    }

    void doesntShoot (){
        if (bulletShot > 0) {
            bulletShot--;
        }
    }
}
