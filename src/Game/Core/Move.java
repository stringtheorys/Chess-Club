package Game.Core;

import Core.Log;
import Game.Pieces.King;
import Game.Pieces.Pawn;
import Game.Pieces.Piece;
import Game.Pieces.Queen;

//This class is uses to describe a move by a piece
public class Move {

    //Stores the coordinate of the move
    private int fromX;
    private int fromY;
    private int toX;
    private int toY;

    //When the move occurs then a piece can be taken
    private Piece pieceTaken = null;

    //If pawn promotion occurs
    private boolean pawnPromo = false;

    //If castling then the extra move
    private Move rook;

    //Constructor
    public Move(int fromX, int fromY, int toX, int toY){
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
    }

    //Getter functions for each of the positions
    public int getFromX(){
        return this.fromX;
    }
    public int getFromY() {
        return this.fromY;
    }
    public int getToX() {
        return this.toX;
    }
    public int getToY() {
        return this.toY;
    }

    public boolean hasCastle() {
        return rook != null;
    }
    public int getCastleFromX() {
        return hasCastle() ? rook.getFromX() : -1;
    }
    public int getCastleFromY() {
        return hasCastle() ? rook.getFromY() : -1;
    }
    public int getCastleToX() {
        return hasCastle() ? rook.getToX() : -1;
    }
    public int getCastleToY() {
        return hasCastle() ? rook.getToY() : -1;
    }

    //This performs the move on the board
    public void perform(Board board){
        Piece[][] pieces = board.getPieces();


        pieceTaken = pieces[toX][toY];

        //Move the piece
        pieces[toX][toY] = pieces[fromX][fromY];
        pieces[toX][toY].setPosition(toX, toY);

        //Null the old position
        pieces[fromX][fromY] = null;

        //Piece which was moved
        Piece movedPiece = pieces[fromX][fromY];
        //Pawn promotion
        //If piece is a pawn
        if (movedPiece instanceof Pawn){
            //If on the back rows
            if (toY == 0 || toY == 7){
                pieces[toX][toY] = new Queen(movedPiece.isWhite(),toX,toY);
            }
        }

        //Castling If piece is a king
        else if (movedPiece instanceof King){
            if (movedPiece.isWhite()){
                board.setWhiteKing(toX, toY);
            } else {
                board.setBlackKing(toX, toY);
            }
            if (fromY == 0 || fromY == 7) {
                if (fromX == 4 && toX == 6) {
                    rook = new Move(7, fromY, 5, fromY); //Short castle
                } else if (fromX == 4 && toX == 2) {
                    rook = new Move(0, fromY, 3, fromY); //Long castle
                }
            }
            //This performs the rooks move as well
            if (rook != null) {
                rook.perform(board);
            }
        }
    }

    //This is the undo function called by the alpha beta pruning class to reverse a move
    public void undo(Board board){
        Piece[][] pieces = board.getPieces();

        pieces[fromX][fromY] = pieces[toX][toY];
        pieces[fromX][fromY].setPosition(fromX, fromY);
        pieces[toX][toY] = pieceTaken;

        Piece movedPiece = pieces[fromX][fromY];
        //Checks if the move is by the King then change King position
        if (movedPiece instanceof King){
            if (movedPiece.isWhite()){
                board.setWhiteKing(fromX, fromY);
            } else {
                board.setBlackKing(fromX, fromY);
            }

            //If the king is moved then the rook may have moved
            if (rook != null){
                rook.undo(board);
                rook = null;
            }
        }

        //If pawn promotion then replace piece as pawn
        if (pawnPromo){
            pieces[fromX][fromY] = new Pawn(movedPiece.isWhite(), fromX, fromY);
            pawnPromo = false;
        }
        pieceTaken = null;
    }

    public void print() {
        Log.log(toString());
    }
    @Override
    public String toString() {
        return String.format("Move from (%s, %s), to (%s, %s)", fromX, fromY, toX, toY);
    }
}

