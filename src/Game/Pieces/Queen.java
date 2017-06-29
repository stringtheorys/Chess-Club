package Game.Pieces;

import Game.Core.Move;

import java.util.LinkedList;

//Queen class which extends the abstract piece class
public class Queen extends Piece {

    //Constructor with piece type, symbol, value and Name
    public Queen(boolean white, int x, int y) {
        super(white, x, y);
    }

    @Override
    public LinkedList<Move> getMoves() {
        //This gets the moves that the Queen can do
        LinkedList<Move> Moves = new LinkedList<>();

        //Rook moves
        Moves.addAll(moveDirection(1, 0));
        Moves.addAll(moveDirection(-1, 0));
        Moves.addAll(moveDirection(0, 1));
        Moves.addAll(moveDirection(0, -1));
        //Bishop moves
        Moves.addAll(moveDirection(1, 1));
        Moves.addAll(moveDirection(1, -1));
        Moves.addAll(moveDirection(-1, 1));
        Moves.addAll(moveDirection(-1, -1));

        return Moves;
    }

    @Override
    public char getSymbol() {
        return 'Q';
    }

    @Override
    public int getValue() {
        return QueenValue;
    }

    @Override
    public String getName() {
        return "Queen";
    }
}
