package Game.Pieces;

import Game.Core.Move;

import java.util.LinkedList;

//King class which extends the abstract piece class
public class King extends Piece {

    //Constructor with piece type, symbol, value and Name
    public King(boolean white, int x, int y) {
        super(white, x, y);
    }

    //This gets the moves that the king can do
    @Override
    public LinkedList<Move> getMoves() {
        //LinkedList for moves
        LinkedList<Move> moves = new LinkedList<>();

        //List of change in x and y position
        int[] xPos = new int[]{-1,0,1,-1,1,-1,0,1};
        int[] yPos = new int[]{1,1,1,0,0,-1,-1,-1};

        int X = x;
        int Y = y;

        Piece[][] pieces = board.getPieces();

        //Loop through the moves
        for (int pos = 0; pos < xPos.length; pos++){
            if (inBound(X + xPos[pos], Y + yPos[pos])){
                //Check if the move is empty or other team
                if (pieces[X + xPos[pos]][Y + yPos[pos]] == null || pieces[X + xPos[pos]][Y + yPos[pos]].isWhite() != white) {
                    //King is not allowed to move if it can be attacked
                    if (! isAttacked(X + xPos[pos], Y + yPos[pos])) {
                        moves.add(new Move(X, Y, X + xPos[pos], Y + yPos[pos]));
                    }

                }
            }
        }

        //Castling
        int row = (isWhite() ? 0 : 7);
        //Check the king is in original position
        if (X == 4 && Y == row) {
            //Short
            //Checks that the piece is of type Rook and is piece is of the same piece
            if (pieces[7][row] != null && pieces[7][row] instanceof Rook && pieces[7][row].isWhite()) {
                //Check that the pieces between are the empty
                if (pieces[5][row] == null && pieces[6][row] == null) {
                    //Checks that the new king position is not attacked
                    if (!isAttacked(6, row)) {
                        moves.add(new Move(4, row, 6, row));
                    }
                }
            }
            //Long
            if (pieces[0][row] != null && pieces[0][row] instanceof Rook && pieces[0][row].isWhite()) {
                if (pieces[2][row] == null && pieces[3][row] == null){
                    if (!isAttacked(2, row)){
                        moves.add(new Move(4, row,2, row));
                    }
                }
            }
        }

        assert X == x;
        assert Y == y;
        return moves;
    }

    @Override
    public char getSymbol() {
        return 'K';
    }
    @Override
    public int getValue() {
        return KingValue;
    }
    @Override
    public String getName() {
        return "King";
    }
}
