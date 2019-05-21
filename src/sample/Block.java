package sample;


/* An item is an object with extra methods*/
class Block extends Collectable {
    Block(String name) {
        alive = true;
        sprite.offset(-7, -7);
        sprite.loadDefaultImages(new Frame("/graphics/item1.png"),
                new Frame("/graphics/item2.png"),
                new Frame("/graphics/item3.png"),
                new Frame("/graphics/item4.png"),
                new Frame("/graphics/item5.png"),
                new Frame("/graphics/item6.png"));
        setItemType(Type.BLOCK);
    }
}
