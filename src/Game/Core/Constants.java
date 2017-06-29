package Game.Core;

import javafx.scene.paint.Color;

public interface Constants {

    //This is for debug to record the time taken to complete an operation
    boolean time = false;

    //Tile size
    int tileHeight = 80;
    int tileWidth = 80;

    //Tile image size
    int imageHeight = 60;
    int imageWidth = 60;

    //The tile colour options
    Color attackColour = Color.CRIMSON;
    Color moveColor = Color.AQUAMARINE;
    Color emptyMove = Color.WHEAT;
}
