package sample;


import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Bullet extends Object {

    // GLOBAL VARIABLES
    private final int WIDHT = 20;
    private final int HEIGHT = 15;
    private final int SPEED_MAGNITUDE = 6;

    private final boolean playerFacingRight;
    private final boolean isPlayerShooting;
    private int frame = 0;
    private Label label = new Label("01010");
    private String style = "  -fx-background-color: rgba(0,0,0,0.5);  -fx-text-fill: #39ff14;";
    private Font font = Font.font("Verdana", 8);

    Bullet (double startx, double starty, boolean facingRight, boolean shooter) {
        super(Type.BULLET, new Frame("/graphics/item1.png"));
        setCollisionBox (startx, starty, WIDHT, HEIGHT, Color.BLACK);
        this.playerFacingRight = facingRight;
        this.isPlayerShooting = shooter;
        label.setStyle(style);
        label.setFont(font);
        sprite.setVisible(false);
        label.setText(randomText());
    }

    public Label label(){ return label; }

    void move(Map map) {
        label.setTranslateX(box.getTranslateX());
        label.setTranslateY(box.getTranslateY()-5);
        int speed = (playerFacingRight ? 1 : -1) * SPEED_MAGNITUDE;
        if (! move_X (speed, map)) {
           map.removeBullet(this);
        }
    }

    private String randomText(){
        String string =
                    rc() + rc() + rc() +rc() + rc() + rc() + rc() + rc() + "\n" +
                    rc() + rc() + rc() +rc() + rc() + rc() + rc() + rc() + "\n" ;

        return string;
    }


    public static String rc(){
        double x = (int)(Math.random()*((5-0)+1))+0;
        if(x==0 || x==1) return "0";
        if(x==3 || x==4) return "1";
        return " ";
    }


    public int getWIDHT() {
        return WIDHT;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }

    boolean isPlayerShooting() {
        return isPlayerShooting;
    }
}
