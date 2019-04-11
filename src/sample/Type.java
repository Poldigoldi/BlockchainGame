package sample;

public enum Type {
    SOLID, PLAYER, PLATFORM, DEFAULT, ITEM
}

//Platform means you can jump up through it, but its still solid when you land on it
//Solid means it is completely solid and nothing can pass through it
//Item will eventually mean can be walked through i guess, may be redundant if we do a 'NON-SOLID' type.
//This could be useful later on for doing other things with objects if we've given them types.