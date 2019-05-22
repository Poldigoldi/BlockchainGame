package sample;

public class Tower extends Person {

    private int waitCount;

    private final int TIME_BETEWEEN_BULLET = 200;

    public Tower(boolean facing) {
        super(Type.TOWER, 1);
        this.facingRight = facing;
        sprite.loadDefaultImages(new Frame("graphics/tower.png"));
        this.waitCount = 0;
    }

    boolean isCanShoot () {
        waitCount++;
        if (waitCount == TIME_BETEWEEN_BULLET) {
            waitCount = 0;
            return true;
        }
        return false;
    }
}
