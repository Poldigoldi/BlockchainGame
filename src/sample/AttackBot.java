package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class AttackBot extends Object {
    int counter;
    private Line laser = new Line();
    int laserXOffset;
    private boolean laserActive;
    int laserdistance = 2000;

    public AttackBot(Type type, Frame ... frame) {
        super(type);
        sprite.loadDefaultImages(new Frame("graphics/attackbot1.png", 8),
                new Frame("/graphics/attackbot2.png", 9),
                new Frame("/graphics/attackbot3.png", 10),
                new Frame("/graphics/attackbot4.png", 11),
                new Frame("/graphics/attackbot5.png", 10),
                new Frame("/graphics/attackbot4.png", 9),
                new Frame("/graphics/attackbot3.png", 8),
                new Frame("/graphics/attackbot2.png", 7));
        sprite.loadDefault2Images(new Frame("/graphics/attackbotright1.png", 8),
                new Frame("/graphics/attackbotright2.png", 9),
                new Frame("/graphics/attackbotright3.png", 10),
                new Frame("/graphics/attackbotright4.png", 11),
                new Frame("/graphics/attackbotright5.png", 10),
                new Frame("/graphics/attackbotright4.png", 9),
                new Frame("/graphics/attackbotright3.png", 8),
                new Frame("/graphics/attackbotright2.png", 7));
    }


    public boolean moveLaser(double playerx, double playery){
        double distance = Math.sqrt(Math.pow((playerx - box.getTranslateX()), 2) + Math.pow((playery - box.getTranslateY()), 2));
        counter++;
        //reset
        if(counter>400){
            counter = 0;
        }
        if(distance < 300) {
            laser.setVisible(true);
                //move if not attacking

                if(counter<300) {
                    if (facingRight) laserXOffset = 55;
                    else laserXOffset = 20;
                    laser.setStartX(getX() + laserXOffset);
                    laser.setStartY(getY() + 28);

                    laser.setStrokeWidth(5);
                    laser.setFill(Color.RED);
                    double angle = getAngle(getX() + laserXOffset, getY() + 28,playerx, playery);
                    double pointX = (getX() + laserXOffset)  + (500 * Math.cos(angle));
                    double pointY = getY() + 28 + (500 * Math.sin(angle));
                    laser.setEndX(pointX);
                    laser.setEndY(pointY);
//                    double angle1 = getAngle( getX() + laserXOffset, getY() + 28, playerx - 20.0,playery + 64);

//                        double pointX1 = (getX() + laserXOffset) - (500 * Math.cos(angle1));
//                        double pointY1 = getY() + 28 - (500 * Math.sin(angle1));
//                    double angle2 = getAngle(getX() + laserXOffset +12 , getY() + 26, playerx + 20.0,playery + 64);
//                         double pointX2 = getX() + laserXOffset + 12 - 500 * Math.cos(angle2);
//                         double pointY2 = getY() + 26 + 500 * Math.sin(angle2);
//
//                    laser.getPoints().addAll(new Double[]{
//                            getX() + laserXOffset + 12, getY() + 26,
//                            getX() + laserXOffset, getY() + 28,
//                            pointX1, pointY1});

//                    System.out.println(pointX2 + " " + pointY2);

                }
                //before locking
                if(counter<200){
                    laser.setFill(new Color(1, 0, 0, 0.7));
                }
                //locking on
                if(counter>200 && counter<300){
                    if(counter % 5 == 0) laser.setFill(new Color(1, 0.9, 0.9, 0.7));
                    else laser.setFill(new Color(1, 0.5, 0.5, 1));
                }
                //firing
                if(counter>300 && counter<320){
                    laser.setFill(new Color(1, 0.9, 0.9, 1));
                    if(distance<350){
                        counter = 0;
                        return true;
                    }
                }
                if(counter>320){
                    laser.setVisible(false);
                }
            }
        else{
            laser.setVisible(false);
            counter = 0;
        }
        return false;
        }



    public int counter(){ return counter;}

    public static double getAngle(double x1, double y1, double x2, double y2)
    {

           double angle = (Math.atan2(x1 - x2, y1 - y2));
        //angle = angle + Math.ceil( -angle / 360 ) * 360;
        return angle;
    }


    public Line laser(){ return laser;}
}
