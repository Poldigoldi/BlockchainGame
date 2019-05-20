package sample;

class Person extends Object {

    private int lives;
    private boolean canDie;

    // Constructor
    Person(Type type, int START_LIVES) {
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

    void winOneLive() {
        this.lives++;
    }

    void looseOneLife() {
        if (this.type == Type.ENEMY) {
            this.setspin(true);
            this.lives--;
        }
        if (this.type == Type.PLAYER || this.type == Type.KING) {
            if (!getSpin()) {
                this.lives--;
                this.setspin(true);
            }
        }
    }

    void addLife() {
        this.lives++;
    }

    boolean isCanDie() {
        return canDie;
    }

    void setCanDie(boolean canDie) {
        this.canDie = canDie;
    }
}
