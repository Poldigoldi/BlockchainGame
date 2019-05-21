package sample;

import javafx.scene.Group;
import javafx.scene.paint.Color;

import java.util.ArrayList;

class InfoBar {
    private Group infoBarGroup = new Group();
    private ArrayList<Object> infoBarList = new ArrayList<>();
    private Object holder;
    private Object power1;
    private Object healthpoint1;
    private Object healthpoint2;
    private Object healthpoint3;
    private Object healthpoint4;

    InfoBar() {
        holder = new Object(Type.ABSTRACT, new Frame("/graphics/infobar.png"));
        power1 = new Object(Type.ABSTRACT);
        healthpoint1 = new Object(Type.ABSTRACT);
        healthpoint2 = new Object(Type.ABSTRACT);
        healthpoint3 = new Object(Type.ABSTRACT);
        healthpoint4 = new Object(Type.ABSTRACT);
        holder.setCollisionBox(0, 0, 0, 0, Color.RED);
        loadWeapon();
        loadHealthPoint(healthpoint1, 170);
        loadHealthPoint(healthpoint2, 200);
        loadHealthPoint(healthpoint3, 230);
        loadHealthPoint(healthpoint4, 260);
        infoBarGroup.getChildren().addAll(holder.box, power1.box, healthpoint1.box, healthpoint2.box, healthpoint3.box, healthpoint4.box);
        infoBarList.add(holder);
        infoBarList.add(power1);
        infoBarList.add(healthpoint1);
        infoBarList.add(healthpoint2);
        infoBarList.add(healthpoint3);
        infoBarList.add(healthpoint4);
    }

    private void loadHealthPoint(Object healthpoint, int xoffset) {
        healthpoint.setCollisionBox(0, 0, 0, 0, Color.RED);
        healthpoint.sprite.offset(xoffset, 2);
        healthpoint.sprite.loadDefaultImages(new Frame("/graphics/healthfill1.png", 8),
                new Frame("/graphics/healthfill2.png", 8),
                new Frame("/graphics/healthfill3.png", 8),
                new Frame("/graphics/healthfill4.png", 8),
                new Frame("/graphics/healthfill5.png", 9999));
        healthpoint.sprite.loadDefault2Images(new Frame("/graphics/healthempty1.png", 8),
                new Frame("/graphics/healthempty2.png", 8),
                new Frame("/graphics/healthempty3.png", 8),
                new Frame("/graphics/healthempty4.png", 8),
                new Frame("/graphics/healthempty4.png", 8),
                new Frame("/graphics/healthempty6.png", 9999));
    }

    private void loadWeapon() {
        power1.setCollisionBox(0, 0, 0, 0, Color.RED);
        power1.sprite.offset(190, 32);
        power1.sprite.loadDefaultImages(new Frame("/graphics/weaponempty1.png", 9999));
        power1.sprite.loadDefault2Images(new Frame("/graphics/weaponfill1.png", 8),
                new Frame("/graphics/weaponfill2.png", 8),
                new Frame("/graphics/weaponfill3.png", 8),
                new Frame("/graphics/weaponfill4.png", 8),
                new Frame("/graphics/weaponfill5.png", 8),
                new Frame("/graphics/weaponfill6.png", 8),
                new Frame("/graphics/weaponfill7.png", 9999));
    }


    void positionBar(double x, double y) {
        for (Object object : infoBarList) {
            object.box.setTranslateX(x);
            object.box.setTranslateY(y);
            object.sprite.toFront();
        }
    }

    void updateWeapon(boolean hasWeapon) {
        if (hasWeapon) power1.sprite.setdefaultanimationchoice(2);
        else {
            power1.sprite.setdefaultanimationchoice(1);
        }
    }


    void updateHealthFill(int lives) {
        if (lives > 3) healthpoint4.sprite.setdefaultanimationchoice(1);
        if (lives > 2) healthpoint3.sprite.setdefaultanimationchoice(1);
        if (lives > 1) healthpoint2.sprite.setdefaultanimationchoice(1);
    }

    void updateHealthEmpty(int lives) {
        if (lives < 4) healthpoint4.sprite.setdefaultanimationchoice(2);
        if (lives < 3) healthpoint3.sprite.setdefaultanimationchoice(2);
        if (lives < 2) healthpoint2.sprite.setdefaultanimationchoice(2);
    }

    Group infoBarGroup() {
        return infoBarGroup;
    }

    ArrayList<Object> infoBarList() {
        return infoBarList;
    }
}
