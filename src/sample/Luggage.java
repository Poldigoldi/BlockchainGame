package sample;


public class Luggage {
    private Block block;
    private Weapon weapon;


    Luggage () {
        this.block = null;
        this.weapon = null;
    }

    void take(Collectable item) {
        // check if its a block
        System.out.println ("taking item: " + item.getItemType ());
        if (item.getItemType () == Type.BLOCK && this.block == null) {
            item.setAlive (false);
            this.block = (Block) item;
        }
        // check if its a weapon
        if (item.getItemType () == Type.WEAPON && this.weapon == null) {
            item.setAlive (false);
            this.weapon = (Weapon) item;
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

    Weapon getWeapon () {
        return this.weapon;
    }
}
