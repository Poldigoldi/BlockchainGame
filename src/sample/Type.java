package sample;

public enum Type {
    SOLID, PLAYER, PLATFORM, DEFAULT, ITEM, LAYER3, LAYER2, LAYER1, LAYER4
}

//Platform means you can jump up through it, but its still solid when you land on it
//Solid means it is completely solid and nothing can pass through it
//Item will eventually mean can be walked through i guess, may be redundant if we do a 'NON-SOLID' type.
//This could be useful later on for doing other things with objects if we've given them types.

/*
PLAYER: Nothing unique so far.
PLATFORM: Other objects can move up through platforms, but not down.
ITEM: Has no collision but still has gravity.
SOLID: Objects are unable to X or Y collide with these type of objects.
LAYER4: Sits at the back and does nothing
LAYER3: These objects do not collide and move VERY SLOWLY in the background.
LAYER2: These objects do not collide and move SLOWLY in the background.
LAYER1: These move slowly as the player moves.
 */