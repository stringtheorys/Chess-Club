package Game.Pieces;

import Game.Core.Move;

import java.util.LinkedList;

//Rook class which extends the abstract piece class
public class Rook extends Piece {

    //Constructor with piece type, symbol, value and Name
    public Rook(boolean white, int x, int y) {
        super(white, x, y);
    }

    //Returns a linkedlist with the moves
    @Override
    public LinkedList<Move> getMoves() {
        //This gets the moves that the rook can do
        LinkedList<Move> Moves = new LinkedList<>();

        //The moves in direction
        Moves.addAll(moveDirection(1, 0));
        Moves.addAll(moveDirection(-1, 0));
        Moves.addAll(moveDirection(0, 1));
        Moves.addAll(moveDirection(0, -1));

        return Moves;
    }

    @Override
    public char getSymbol() {
        return 'R';
    }
    @Override
    public int getValue() {
        return RookValue;
    }
    @Override
    public String getName() {
        return "Rook";
    }
}
