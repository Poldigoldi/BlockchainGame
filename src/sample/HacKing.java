package sample;

import javafx.scene.paint.Color;

/** This class defines the features of the Big Boss - aka -'HacKing'
 *  The way he works:
 *
 *  He moves like a basic Enemy
 *  1st attack: sends a wave of enemy
 *  2nd attack: send missiles
 *
 */

public class HacKing extends Person {

    private final int ATTACK_BULLETS_AMOUT = 2;
    private final int TIME_BETWEEN_ATTACK = 60 * 5;
    private final int TIME_BETWEEN_BULLETS = 10;
    private int counter;
    private int attack_mode; // 1 for enemy wave - 2 for missiles
    private int attack_bullet_count;

    // Constructor
    public HacKing (int startx, int starty) {
        super (Type.ENEMY, 6);
        this.setCollisionBox(startx, starty , 60, 60, Color.INDIANRED);
        sprite.loadDefaultLeftImages(new Frame("/graphics/enemy1.png"), new Frame("/graphics/enemy1.png"));
        this.attack_mode = 2;
        this.counter = 0;
        this.attack_bullet_count = 0;
    }


    public int getAttackMode() {
        return attack_mode;
    }

    public void nextAttackMode () {
        if (this.attack_mode == 2 && attack_bullet_count < ATTACK_BULLETS_AMOUT) {
            attack_bullet_count++;
            return;
        }
        attack_bullet_count = 0;
        this.attack_mode = this.attack_mode % 2 + 1;
    }

    public boolean isCanAttack () {
        counter++;

        switch (attack_mode) {
            case 1:
                if (counter == TIME_BETWEEN_ATTACK) {
                    counter = 0;
                    return true;
                }
            case 2:
                if (counter >= TIME_BETWEEN_ATTACK
                        && (counter-TIME_BETWEEN_ATTACK) % TIME_BETWEEN_BULLETS == 0) {

                    if (counter == TIME_BETWEEN_ATTACK + (ATTACK_BULLETS_AMOUT) * TIME_BETWEEN_BULLETS) {
                        counter = 0;
                    }
                    return true;
                }
        }
        return false;
    }
}
