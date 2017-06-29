package Game.UI;

import Core.Log;
import Game.Core.Constants;
import Game.Pieces.Piece;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

//This is the button object in the chess board
public class TileUI extends Button implements Constants {

    //Attributes
    private boolean move;
    private int x;
    private int y;
    private BoardUI parent;

    //Constructor
    public TileUI(BoardUI parent, int x, int y){
        this.parent = parent;
        this.x = x;
        this.y = y;

        move = false;

        initGui();
    }

    //Creates the button gui
    private void initGui(){

        this.setMinHeight(tileHeight);
        this.setMaxHeight(tileHeight);
        this.setMinWidth(tileWidth);
        this.setMaxWidth(tileWidth);

        this.setAlignment(Pos.CENTER);

        this.setBackground(new Background(new BackgroundFill(
                emptyMove, CornerRadii.EMPTY, Insets.EMPTY
        )));

        this.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2)
        )));

        this.setOnMousePressed(event -> buttonClick());
    }

    //Button click function
    private void buttonClick(){
        //Prints button information
        if (move){
            Log.log(String.format("Tile (%d, %d) is clicked, make move", x, y));
            parent.makeMove(x, y);
        } else {
            Log.log(String.format("Tile (%d, %d) is clicked, show move", x, y));
            parent.showMoves(x, y);
        }
    }

    //Sets the background to red as can be attacked
    public void attack(){
        this.setBackground(new Background(new BackgroundFill(
                attackColour, CornerRadii.EMPTY, Insets.EMPTY
        )));
        move = true;
    }
    //Sets the background to blue as can be moved to
    public void move(){
        this.setBackground(new Background(new BackgroundFill(
                moveColor, CornerRadii.EMPTY, Insets.EMPTY
        )));
        move = true;
    }
    //Sets the background back to default colour
    public void unHighlight(){
        this.setBackground(new Background(new BackgroundFill(
                emptyMove, CornerRadii.EMPTY, Insets.EMPTY
        )));
        move = false;
    }

    //Removes the background of the piece from the button
    public void removePiece(){
        Log.log(String.format("Removing piece at (%d, %d)", x, y));
        setGraphic(null);
    }
    //Adds the piece background to the tile
    public void addPiece(Piece piece){
        if (piece != null) {
            Log.log(String.format("Add piece at (%d, %d) piece type : %s, %s", x, y, piece.getName(), piece.getColour()));
            String imageLocation = piece.getImage();
            Image image = new Image(Piece.class.getResource(imageLocation).toString(),
                    imageWidth, imageHeight, true, true);
            ImageView imageView = new ImageView(image);
            setGraphic(imageView);
        } else {
            throw new IllegalStateException("The piece is null");
        }
    }
}
