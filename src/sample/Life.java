package sample;

public class Life extends Collectable {
  private Frame heart = new Frame("/graphics/item1.png");
  private String name;

  Life(String name) {
    setItemType (Type.HEART);
    this.name = name;
    sprite.setImage(heart);
    sprite().offset(-7, -7);
  }
}

