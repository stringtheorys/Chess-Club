package Game.Pieces;

import Game.Core.Move;

import java.util.LinkedList;

//Knight class which extends the abstract piece class
public class Knight extends Piece {

    //Constructor with piece type, symbol, value and Name
    public Knight(boolean white, int x, int y) {
        super(white, x, y);
    }

    //This gets the moves that the Knight can do
    @Override
    public LinkedList<Move> getMoves() {
        LinkedList<Move> moves = new LinkedList<>();
        Piece[][] pieces = board.getPieces();

        //This are the change in x's and y's
        int[] xPos = new int[]{1,2,-1,-2,1,2,-1,-2};
        int[] yPos = new int[]{2,1,2,1,-2,-1,-2,-1};

        int X = x;
        int Y = y;

        //Loop through the moves
        for (int pos = 0; pos < xPos.length; pos++){
            if (inBound(X + xPos[pos], Y + yPos[pos])){
                //Check if the move is empty or other team
                if (pieces[X + xPos[pos]][Y + yPos[pos]] == null || pieces[X + xPos[pos]][Y + yPos[pos]].isWhite() != white) {
                    moves.add(new Move(X, Y, X + xPos[pos], Y + yPos[pos]));
                }
            }
        }

        return moves;
    }

    @Override
    public char getSymbol() {
        return 'N';
    }
    @Override
    public int getValue() {
        return KnightValue;
    }
    @Override
    public String getName() {
        return "Knight";
    }
}
