package sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Luggage {
    ArrayList blocks = new ArrayList ();

    void show() {
        /*Shows items*/
    }

    void take(Item item) {
        /*Picks up item and add it to luggage*/
        item.setAlive (false);
        this.blocks.add(item);
        System.out.println ("Player picked up:" + item.name);
    }

    void drop(Item item, double x, double y) {
        /*Drops item*/
        item.setAlive (true);
        item.drop (x, y);
        this.blocks.remove(item);
        System.out.println ("Player dropped:" + item.name);
    }
}
