package Game.Pieces;

import Game.Core.Move;

import java.util.LinkedList;

//Pawn class which extends the abstract piece class
public class Pawn extends Piece {

    //Constructor with piece type, symbol, value and Name
    public Pawn(boolean white, int x, int y) {
        super(white, x, y);
    }

    @Override
    public LinkedList<Move> getMoves() {
        //This gets the moves that the Pawn can do
        LinkedList<Move> moves = new LinkedList<>();
        Piece[][] pieces = board.getPieces();

        int newY = y + (isWhite() ? 1 : -1);
        int Y = y;
        int X = x;

        //Forward move
        if (inBound(X, newY) && pieces[X][newY] == null) {
            moves.add(new Move(X, Y, X, newY));
            //Bonus move
            if (isWhite()) {
                if (Y == 1 && pieces[X][3] == null) {
                    moves.add(new Move(X, Y, X, 3));
                }
            } else {
                if (Y == 6 && pieces[X][4] == null) {
                    moves.add(new Move(X, Y, X , 4));
                }
            }
        }
        //Left attack
        if (inBound(X - 1, newY) && pieces[X - 1][newY] != null && pieces[X - 1][newY].isWhite() != isWhite()) {
            moves.add(new Move(X, Y, X - 1, newY));
        }
        //Right attack
        if (inBound(X + 1, newY) && pieces[X + 1][newY] != null && pieces[X + 1][newY].isWhite() != isWhite()) {
            moves.add(new Move(X, Y, X + 1, newY));
        }
        return moves;
    }

    @Override
    public char getSymbol() {
        return 'P';
    }
    @Override
    public int getValue() {
        return PawnValue;
    }
    @Override
    public String getName() {
        return "Pawn";
    }
}
