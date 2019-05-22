package sample;

import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.nio.file.Paths;

class AttackBot extends Object {
    private int counter;
    private Polygon laser = new Polygon();
    private AudioClip lockonSound = new AudioClip(Paths.get("src/sound/lockon.wav").toUri().toString());
    private AudioClip lasershootSound = new AudioClip(Paths.get("src/sound/lasershoot.wav").toUri().toString());
    private boolean hasLockedOn = false;
    private boolean hasFired = false;

    /*Set up images for robots*/
    AttackBot(Type type, Frame... frame) {
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


    /*This tracks the laser that locks on and fires at player if in range*/
    boolean moveLaser(double playerx, double playery) {
        double distance = Math.sqrt(Math.pow((playerx - box.getTranslateX()), 2) + Math.pow((playery - box.getTranslateY()), 2));
        counter++;
        //reset

        if (counter > 400) {
            counter = 0;
        }
        if (distance < 300 || counter > 300) {
            laser.setVisible(true);
            if (!hasLockedOn) {
                lockonSound.play();
                hasLockedOn = true;
            }
            //move if not attacking
            if (counter < 300) {
                int laserXOffset;
                if (facingRight) laserXOffset = 55;
                else laserXOffset = 20;
                double angle1 = getAngle(getX() + laserXOffset, getY() + 28, playerx - 10, playery + 25);
                double xstep1 = Math.cos(Math.toRadians(-angle1));
                double ystep1 = Math.sin(Math.toRadians(-angle1 + 180));
                double pointX1 = (getX() + laserXOffset) + (2000 * xstep1);
                double pointY1 = getY() + 28 + (2000 * ystep1);
                double angle2 = getAngle(getX() + laserXOffset, getY() + 28, playerx + 15, playery + 35);
                double xstep2 = Math.cos(Math.toRadians(-angle2));
                double ystep2 = Math.sin(Math.toRadians(-angle2 + 180));
                double pointX2 = (getX() + laserXOffset) + (2000 * xstep2);
                double pointY2 = getY() + 28 + (2000 * ystep2);
                laser.getPoints().clear();
                laser.getPoints().addAll(getX() + laserXOffset + 12, getY() + 26,
                        getX() + laserXOffset, getY() + 28,
                        pointX1, pointY1,
                        pointX2, pointY2);
            }
            //before locking
            if (counter < 200) {
                laser.setFill(new Color(1, 0, 0, 0.7));
            }
            //locking on
            if (counter > 200 && counter < 300) {
                if (counter % 5 == 0) {
                    laser.setFill(new Color(1, 0.9, 0.9, 0.7));
                } else laser.setFill(new Color(1, 0.5, 0.5, 1));
            }
            //firing
            if (counter > 300 && counter < 320) {
                if (!hasFired) {
                    lasershootSound.play();
                }
                hasFired = true;
                laser.setFill(new Color(1, 0.9, 0.9, 1));
                return true;
            }
            if (counter > 320) {
                laser.setVisible(false);
            }
        } else {
            laser.setVisible(false);
            hasLockedOn = false;
            hasFired = false;
            counter = 0;
        }
        return false;
    }


    int counter() {
        return counter;
    }

    private static double getAngle(double x1, double y1, double x2, double y2) {
        //angle = angle + Math.ceil( -angle / 360 ) * 360;
        return Math.atan2(y2 - y1, x2 - x1) * 180 / Math.PI;
    }

    Polygon laser() {
        return laser;
    }
}
