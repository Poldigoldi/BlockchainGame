package sample;


import java.util.ArrayList;

public class Luggage {
    private Item block;
    ArrayList<Item> weapons = new ArrayList<> ();

    void show() {
        /*Shows items*/
    }

    void take(Item item) {
        /*Picks up item and add it to luggage*/
        item.setAlive (false);
        this.block = item;
        System.out.println ("Player picked up:" + item.name);
    }

    void drop(Item item, double x, double y) {
        /*Drops item*/
        item.setAlive (true);
        item.drop (x, y);
        this.block = null;
        System.out.println ("Player dropped:" + item.name);
    }

    Item getblock () {
        return this.block;
    }
}
