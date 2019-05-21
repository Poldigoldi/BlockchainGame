package sample;

public enum Type {
    SPRAY, SOLID, PLAYER, PLATFORM, PLATFORMBUTTON, ITEM, HEART, WEAPON, BULLET, BLOCK, LAYER1, LAYER2, LAYER3, LAYER4, ENEMY, DOODAD, ABSTRACT, KING, ATTACKBOT, TOWER;

    //used in update to see whether to apply falling
    public boolean hasGravity() {
        if (this == PLAYER) return true;
        if (this == ITEM) return true;
        return this == ENEMY;
    }

    //used in update to see whether to look for the last movement for animations (e.g. left or right?)
    public boolean hasMovementAnimation() {
        if (this == PLAYER) return true;
        if (this == ENEMY) return true;
        return this == KING;
    }

    public boolean hasFallingAnimation() {
        if (this == PLAYER) return true;
        return this == ENEMY;
    }


    public boolean hasHurtAnimation() {
        if (this == PLAYER) return true;
        return this == ENEMY;
    }


    public boolean hasJumpingAnimation() {
        if (this == PLAYER) return true;
        return this == ENEMY;
    }

}

//Platform means you can jump up through it, but its still solid when you land on it
//Solid means it is completely solid and nothing can pass through it
//Block will eventually mean can be walked through i guess, may be redundant if we do a 'NON-SOLID' type.
//This could be useful later on for doing other things with objects if we've given them types.

/*
PLAYER: Nothing unique so far.
ENEMY: will add stuff.
PLATFORM: Other objects can move up through platforms, but not down.
ITEM: Has no collision but still has gravity.
SOLID: Objects are unable to X or Y collide with these type of objects.
LAYER4: Sits at the back and does nothing (Distant mountains)
LAYER3: These objects do not collide and move VERY SLOWLY in the background. (Far away clouds)
LAYER2: These move slowly as the player moves. (Buildings in background)
LAYER2: These objects do not collide and move SLOWLY in the background. (Clouds in front of buildings)
 */