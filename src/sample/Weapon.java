package sample;

import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

public class Weapon extends Collectable {

    private final String name;
    private boolean canShoot;
    private boolean maxReached;
    private int bullets;
    private boolean mustWait;
    private int bulletshot;
    private int waitCount;

    private final int MAX_BULLET_SHOT = 30;
    private final int TIME_WAIT = 50;

    Weapon (String name, int bullets) {
        super(new Frame("/graphics/weapon.png"));
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
        this.waitCount = 0;
        this.bulletshot = 0;
        this.mustWait = false;
        this.maxReached = false;
    }


    String getName() {
        return name;
    }

    private boolean isMaxShootReached () {
        return bulletshot >= MAX_BULLET_SHOT;
    }

    boolean isCanShoot() {
        if (canShoot) {
            mustWait = true;
        }
        System.out.println (bulletshot);
        if (mustWait) {
            mustWait = false;
            if (! isMaxShootReached ()) {
                System.out.println ("---------");
                maxReached = false;
                return true;
            }
            System.out.println ("MAX REACHED");
            this.maxReached = true;
            if (maxReached) {
                waitCount++;
            }
        }
        return false;
    }

    void setCanShoot(boolean canShoot) {
        this.canShoot = canShoot;
    }

    public void winOneBullet () {
        bullets++;
    }

    void looseOneBullet() {
        bullets--;
        bulletshot += 8;
    }

    int getBullets() {
        return bullets;
    }

    void doesntShoot (){
        if (bulletshot > 0) {
            bulletshot--;
        }
    }
}
