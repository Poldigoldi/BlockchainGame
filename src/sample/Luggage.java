package sample;


import java.util.ArrayList;

public class Luggage {
    private Block block;
    private ArrayList<Collectable> items = new ArrayList<> ();

    void show() {
        /* Shows items */
    }

    void take(Collectable item) {
        // check if its a block
        System.out.println ("taking item: " + item.getItemType ());
        if (item.getItemType () == Type.BLOCK && this.block == null) {
            item.setAlive (false);
            this.block = (Block) item;
        }
        // check if its a weapon
        if (item.getItemType () == Type.WEAPON) {
            item.setAlive (false);
            this.items.add (item);
        }
    }

    void drop(Collectable item, double x, double y) {
        /* TODO: SEE HOW DO WE DROP DIFFERENT ITEMS */
        item.setAlive (true);
        item.drop (x, y - 20);
        if (item.getItemType () == Type.BLOCK) {
            this.block = null;
        }
    }

    Block getblock () {
        return this.block;
    }

    ArrayList<Collectable> getItems () {
        return this.items;
    }
}
