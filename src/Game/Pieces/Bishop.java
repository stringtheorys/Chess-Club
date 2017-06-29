package Game.Pieces;

import Game.Core.Move;

import java.util.LinkedList;

//Bishop class which extends the abstract piece class
public class Bishop extends Piece {

    //Constructor with piece type, symbol, value and Name
    public Bishop(boolean white, int x, int y) {
        super(white, x, y);
    }

    //This gets the moves that the bishop can do
    @Override
    public LinkedList<Move> getMoves() {
        LinkedList<Move> Moves = new LinkedList<>();

        //This uses the Piece function to move
        //  As the bishop moves in 4 direction
        //  The moveDirection function returns a linked list
        Moves.addAll(moveDirection(1, 1));        //Diagonal top right
        Moves.addAll(moveDirection(1, -1));       //Diagonal bottom right
        Moves.addAll(moveDirection(-1, 1));       //Diagonal top left
        Moves.addAll(moveDirection(-1, -1));      //Diagonal top right

        return Moves;
    }

    @Override
    public char getSymbol() {
        return 'B';
    }
    @Override
    public int getValue() {
        return BishopValue;
    }
    @Override
    public String getName() {
        return "Bishop";
    }
}
