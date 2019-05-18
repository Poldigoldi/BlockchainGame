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


    public int getLives () {
        return this.lives;
    }

    public void setLives (int lives) {
        this.lives = lives;
    }

    public void winOneLive () {
        this.lives++;
    }

    public void looseOneLife () {
        this.lives--;
    }

    public boolean isCanDie() {
        return canDie;
    }

    public void setCanDie(boolean canDie) {
        this.canDie = canDie;
    }
}
