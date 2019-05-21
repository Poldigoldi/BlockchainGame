package sample;

import javafx.scene.paint.Color;

class HelpPopUp {
    //   private Rectangle popUp = new Rectangle(10, 10, Color.GREEN);

    private Object helper;
    private String string;

    HelpPopUp(int x, int y, String string, boolean faceRight) {
        helper = new Object(Type.ABSTRACT);
        helper.setCollisionBox(x * 64, y * 64, 50, 50, Color.CADETBLUE);
        if (!faceRight) {
            helper.sprite.offset(-36, 0);
            helper.sprite.loadDefaultImages(new Frame("/graphics/helper1.png", 15),
                    new Frame("/graphics/helper2.png", 10),
                    new Frame("/graphics/helper3.png", 8),
                    new Frame("/graphics/helper4.png", 7),
                    new Frame("/graphics/helper5.png", 8),
                    new Frame("/graphics/helper6.png", 10),
                    new Frame("/graphics/helper7.png", 15),
                    new Frame("/graphics/helper8.png", 10));
            helper.sprite.loadDefault2Images(new Frame("/graphics/helperoff1.png", 5),
                    new Frame("/graphics/helperoff2.png", 5),
                    new Frame("/graphics/helperoff3.png", 5));
        }
        if (faceRight) {
            helper.sprite.offset(36, 0);
            helper.sprite.loadDefaultImages(new Frame("/graphics/helperright1.png", 15),
                    new Frame("/graphics/helperright2.png", 10),
                    new Frame("/graphics/helperright3.png", 8),
                    new Frame("/graphics/helperright4.png", 7),
                    new Frame("/graphics/helperright5.png", 8),
                    new Frame("/graphics/helperright6.png", 10),
                    new Frame("/graphics/helperright7.png", 15),
                    new Frame("/graphics/helperright8.png", 10));
            helper.sprite.loadDefault2Images(new Frame("/graphics/helperoffright1.png", 5),
                    new Frame("/graphics/helperoffright2.png", 5),
                    new Frame("/graphics/helperoffright3.png", 5));
        }
        this.string = string;
    }

    public String string() {
        return string;
    }

    Object helper() {
        return helper;
    }


    boolean inRange(double playerx, double playery) {
        double distance = Math.sqrt(Math.pow((playerx - helper.box.getTranslateX()), 2) + Math.pow((playery - helper.box.getTranslateY()), 2));
        if (distance < 300) {
            helper.sprite.setdefaultanimationchoice(1);
            return true;
        }
        helper.sprite.setdefaultanimationchoice(2);
        return false;
    }
}
