package sample;

import javafx.scene.Group;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class BossHealthBar {
    private Group group = new Group();
    private ArrayList<Object> healthpointList = new ArrayList<>();
    private Object bossframe;
    private int health;
    Frame tophealth =  new Frame("/graphics/bosshealth1.png");
    Frame middlehealth =  new Frame("/graphics/bosshealth2.png");
    Frame bottomhealth =  new Frame("/graphics/bosshealth3.png");

    public BossHealthBar(int bosshealth) {
        health = bosshealth;
        bossframe = new Object(Type.ABSTRACT, new Frame("/graphics/hackkingframe.png"));
        for(int i = 0; i < health; i++) {
            Frame frame = middlehealth;
            if(i == 0 ) frame = tophealth;
            if(i == health-1) frame = bottomhealth;
            Object healthpoint = new Object(Type.ABSTRACT, frame);
            healthpoint.setCollisionBox(60, 100+i*28, 0, 0, Color.RED);
            healthpointList.add(healthpoint);
            healthpoint.sprite.offset(100, 200);
            healthpoint.add(group);
        }
        bossframe.add(group);
    }

    public void updatePosition(double x, double y){
        for(int i = 0; i < health; i++) {
            healthpointList.get(i).box.setTranslateX(healthpointList.get(i).box.getTranslateX()+20);
            healthpointList.get(i).box.setTranslateY(healthpointList.get(i).box.getTranslateX()+i*20);
        }
    }

    public void updateAppearance(int bosshealth){
        for(int i = 0; i < health; i++) {
            if(i<bosshealth-1) healthpointList.get(bosshealth-1).sprite.setOpacity(1);
            else healthpointList.get(bosshealth-1).sprite.setOpacity(0.1);
        }
    }



    Group group() {
        return group;
    }

    ArrayList<Object> healthpointList() {
        return healthpointList;
    }
}

