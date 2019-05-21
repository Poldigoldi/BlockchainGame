package sample;

public class Tower extends Person {

    private boolean canShoot;
    private int waitCount;

    private final int TIME_BETWEEN_SHOOT = 10;
    private final int TIME_BETEWEEN_SERIE = 200;

    public Tower(boolean facing) {
        super(Type.TOWER, 1);
        this.facingRight = facing;
        sprite.loadDefaultImages(new Frame("graphics/attackbot1.png"));
        this.canShoot = false;
        this.waitCount = 0;
    }

    boolean isCanShoot () {
        waitCount++;
        if (waitCount == TIME_BETEWEEN_SERIE) {
            waitCount = 0;
            return true;
        }
        return false;
    }
}
