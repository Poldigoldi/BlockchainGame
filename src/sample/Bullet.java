package sample;


import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/*Sets up simple Bullet when fired*/
public class Bullet extends Object {

    private final boolean playerFacingRight;
    private final boolean isPlayerShooting;
    private Label label = new Label("01010");

    /*Creates bullet and start position is set by object position*/
    Bullet(double startx, double starty, boolean facingRight, boolean shooter) {
        super(Type.BULLET, new Frame("/graphics/item1.png"));
        // GLOBAL VARIABLES
        int WIDHT = 20;
        int HEIGHT = 15;
        setCollisionBox(startx, starty, WIDHT, HEIGHT, Color.BLACK);
        this.playerFacingRight = facingRight;
        this.isPlayerShooting = shooter;
        String style = "  -fx-background-color: rgba(0,0,0,0.5);  -fx-text-fill: #39ff14;";
        label.setStyle(style);
        Font font = Font.font("Verdana", 8);
        label.setFont(font);
        sprite.setVisible(false);
        label.setText(randomText());
    }

    public Label label() {
        return label;
    }

    /*Moves bullet along map*/
    void move(Map map) {
        if (isAlive()) {
            label.setTranslateX(box.getTranslateX());
            label.setTranslateY(box.getTranslateY() - 5);
            int SPEED_MAGNITUDE = 6;
            int speed = (playerFacingRight ? 1 : -1) * SPEED_MAGNITUDE;
            if (!move_X(speed, map)) {
                setAlive(false);
                map.removeBullet(this);
            }
        }
    }

    /*Sets up random binary text that appears on the bullet*/
    private String randomText() {
        String text =
                rc() + rc() + rc() + rc() + rc() + rc() + rc() + rc() + "\n" +
                        rc() + rc() + rc() + rc() + rc() + rc() + rc() + rc() + "\n";
        return text;
    }

    public static String rc() {
        double x = (int) (Math.random() * ((5 - 0) + 1));
        if (x == 0 || x == 1) return "0";
        if (x == 3 || x == 4) return "1";
        return " ";
    }

    boolean isPlayerShooting() {
        return isPlayerShooting;
    }
}
