package sample;

public class Person extends Object {

    private int lives;
    private boolean canDie;

    // Constructor
    Person (Type type, int START_LIVES) {
        super(type);
        this.canDie = true;
        this.lives = START_LIVES;
    }


    int getLives() {
        return this.lives;
    }

    void setLives(int lives) {
        this.lives = lives;
    }

    public void winOneLive () {
        this.lives++;
    }

    void looseOneLife() {
        if(this.type==type.ENEMY || this.type == Type.KING) {
            this.setspin(true);
            this.lives--;
        }
        if(this.type==type.PLAYER) {
            if (getSpin() == false) {
                this.lives--;
                this.setspin(true);
            }
        }
    }
    void addLife() {
        this.lives ++;
    }

    boolean isCanDie() {
        return canDie;
    }

    void setCanDie(boolean canDie) {
        this.canDie = canDie;
    }
}
