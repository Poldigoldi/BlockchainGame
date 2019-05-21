package sample;

import javafx.scene.Group;
import javafx.scene.paint.Color;

import java.util.ArrayList;


/*Creates health bar for boss*/
class BossHealthBar {
    private Group group = new Group();
    private ArrayList<Object> healthpointList = new ArrayList<>();
    private Object bossframe;
    private int bosshealth;
    private Frame tophealth = new Frame("/graphics/bosshealth1.png");
    private Frame middlehealth = new Frame("/graphics/bosshealth2.png");
    private Frame bottomhealth = new Frame("/graphics/bosshealth3.png");

    /*Initialises healthbar with number of lives*/
    BossHealthBar(int bosshealth) {
        this.bosshealth = bosshealth;
        bossframe = new Object(Type.ABSTRACT, new Frame("/graphics/hackkingframe.png"));
        for (int i = 0; i < this.bosshealth; i++) {
            Frame frame = middlehealth;
            if (i == 0) frame = tophealth;
            if (i == this.bosshealth - 1) frame = bottomhealth;
            Object healthpoint = new Object(Type.ABSTRACT, frame);
            healthpoint.setCollisionBox(52, 101 + i * 33, 0, 0, Color.RED);
            healthpointList.add(healthpoint);
            healthpoint.sprite.offset(100, 200);
            healthpoint.add(group);
        }
        bossframe.add(group);
    }

    void updatePosition(double x, double y) {
        for (int i = 0; i < bosshealth; i++) {
            healthpointList.get(i).box.setTranslateX(healthpointList.get(i).box.getTranslateX() + 20);
            healthpointList.get(i).box.setTranslateY(healthpointList.get(i).box.getTranslateX() + i * 20);
        }
    }

    void updateAppearance(int bosshealth) {
        for (int i = 1; i < this.bosshealth; i++) {
            if (i < bosshealth) healthpointList.get(i).sprite.setOpacity(1);
            else healthpointList.get(i).sprite.setOpacity(0.1);
        }
        if (bosshealth < 1) {
            group.setVisible(false);
        }
    }


    Group group() {
        return group;
    }

    ArrayList<Object> healthpointList() {
        return healthpointList;
    }
}

