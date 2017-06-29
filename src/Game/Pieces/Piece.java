package Game.Pieces;

import Game.Core.Board;
import Game.Core.Constants;
import Game.Core.Move;

import java.util.LinkedList;

//The class is abstract meaning that it can be instantiated on its own
public abstract class Piece implements Constants {
    //This is the type of image for the pieces
    private final String imageType = ".png";

    protected final static int KingValue = 200;
    protected final static int QueenValue = 40;
    protected final static int RookValue = 10;
    protected final static int KnightValue = 10;
    protected final static int BishopValue = 15;
    protected final static int PawnValue = 5;

    //These values for required for the piece
    protected boolean white;
    protected int x;
    protected int y;
    protected static Board board;

    //Constructor for the piece
    public Piece(boolean white, int x, int y){
        this.white = white;
        this.x = x;
        this.y = y;
    }

    public static void setBoard(Board newBoard) {
        board = newBoard;
    }
    public void setX(int X) {
        this.x = X;
    }
    public void setY(int Y) {
        this.y = Y;
    }
    public void setPosition(int X, int Y) {
        setX(X);
        setY(Y);
    }
    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }
    public boolean isWhite() {
        return this.white;
    }
    //Get the piece image location
    public String getImage(){
        return "Sprites/" + getName() + (isWhite() ? "_White" : "_Black") + imageType;
    }
    //Gets the colour of the piece
    public String getColour() {
        return (isWhite() ? "White" : "Black");
    }

    @Override
    public String toString() {
        return String.format("%s %s (%d, %d)", getName(), getColour(), x, y);
    }

    //This function checks if the piece can move to a location x, y
    protected boolean inBound(int X, int Y){
        return (X >= 0 && X < 8 && Y >= 0 && Y < 8);
    }

    //This function checks if the piece is going to be attacked
    protected boolean isAttacked(int newX, int newY){
        Piece[][] pieces = board.getPieces();
        //Loops through the board
        for (int X = 0; X < 8; X++){
            for (int Y = 0; Y < 8; Y++){
                //Checks if the piece exist and it the opponents piece
                //  and is not attacked and is not a king
                if (pieces[X][Y] != null && pieces[X][Y].isWhite() != white && !(pieces[X][Y] instanceof King)){
                    LinkedList<Move> moves = pieces[X][Y].getMoves();
                    //Iterate through the moves to check if the move attacks this position
                    for (Move move : moves){
                        if (move.getToX() == newX && move.getToY() == newY){
                            return true;
                        }
                    }
                }
            }
        }
        //Else not attacked then return false
        return false;
    }

    //Move direction function which moves in a dx and dy direction
    //  Till the location is outside the board or has collided with a piece
    protected LinkedList<Move> moveDirection(int dx, int dy){
        //Creates a list of moves
        LinkedList<Move> Moves = new LinkedList<>();
        Piece[][] pieces = board.getPieces();

        int X = x;
        int Y = y;
        //Checks that it is inbound
        while (inBound(X + dx, Y + dy)){
            //If no piece then continue
            if (pieces[X + dx][Y + dy] == null) {
                Moves.add(new Move(x, y, X + dx, Y + dy));
            } else {
                //If piece is the opponents then add move
                if (pieces[X + dx][Y + dy].isWhite() != isWhite()){
                    Moves.add(new Move(x, y, X + dx, Y + dy));
                }
                //Stop searching in direction as collide with piece
                break;
            }
            X += dx;
            Y += dy;
        }
        return Moves;
    }

    //This is an abstract function which all subclasses must implement
    public abstract LinkedList<Move> getMoves();

    public abstract char getSymbol();
    public abstract int getValue();
    public abstract String getName();
}
