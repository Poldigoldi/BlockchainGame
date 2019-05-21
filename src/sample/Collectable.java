package sample;

/* interactable items that the player can use to their advantage - including guns and lives*/
class Collectable extends Object {
    private Type itemType;

    Collectable(Frame... frames) {
        super(Type.ITEM, frames);
    }

    void drop(double x, double y) {
        box.setTranslateX(x);
        box.setTranslateY(y);
    }

    Type getItemType() {
        return itemType;
    }

    void setItemType(Type itemType) {
        this.itemType = itemType;
    }
}
