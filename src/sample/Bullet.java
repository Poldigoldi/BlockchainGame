package sample;


import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Bullet extends Object {
    int frame = 0;
    final int SPEED_MAGNITUDE = 6;
    final boolean playerFacingRight;
    Label label = new Label("01010");
    private String style = "  -fx-background-color: rgba(0,0,0,0.5);  -fx-text-fill: #39ff14;";
    Font font = Font.font("Verdana", 8);

    Bullet (double startx, double starty, boolean facingRight) {
        super(Type.BULLET, new Frame("/graphics/item1.png"));
        setCollisionBox (startx, starty, 20, 15, Color.BLACK);
        this.playerFacingRight = facingRight;
        label.setStyle(style);
        label.setFont(font);
        sprite.setVisible(false);
    }

    public Label label(){ return label; }

    public void move (Map map) {
        frame++;
        label.setTranslateX(box.getTranslateX());
        label.setTranslateY(box.getTranslateY()-5);
        int speed = (playerFacingRight ? 1 : -1) * SPEED_MAGNITUDE;
        if(frame == 5){
            frame = 0;
            if(speed > 0) label.setText(randomText(true));
            else label.setText(randomText(false));
        }
        if (! move_X (speed, map)) {
           map.removeBullet(this);
        }
    }

    private String randomText(boolean goingRight){
        String string =
                    rc() + rc() + rc() +rc() + rc() + rc() + rc() + rc() + "\n" +
                    rc() + rc() + rc() +rc() + rc() + rc() + rc() + rc() + "\n" +
                    rc() + rc() + rc() +rc() + rc() + rc() + rc() + rc() + "\n";

        return string;
    }


    public static String rc(){
        double x = (int)(Math.random()*((5-0)+1))+0;
        if(x==0 || x==1 || x ==2) return "0";
        if(x==3 || x==4) return "1";
        return " ";
    }
}
