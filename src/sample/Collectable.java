package sample;

public class Collectable extends Object {

    /* This parameter specifies that the player can only have once this object  */

    private boolean unique;
    private Type itemType;

    Collectable (Frame ... frames ) {
        super (Type.ITEM, frames);
        this.unique = false;
    }

    void drop (double x, double y) {
        box.setTranslateX (x);
        box.setTranslateY (y);
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public Type getItemType() {
        return itemType;
    }

    public void setItemType(Type itemType) {
        this.itemType = itemType;
    }
}
