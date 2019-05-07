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
    }

    void drop(Item item, double x, double y) {
        /*Drops item*/
        item.setAlive (true);
        item.drop (x, y - 20);
        this.block = null;
    }

    Item getblock () {
        return this.block;
    }
}
