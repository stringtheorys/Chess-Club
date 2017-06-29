package Game.Core;

import Core.Log;
import Game.Pieces.*;

//Class to contain all of the board pieces array and the location of the black and white king
public class Board {

    private Piece[][] pieces;       //Array of chess board
    //Position of white king
    private int whiteKingX;
    private int whiteKingY;
    //Position of black king
    private int blackKingX;
    private int blackKingY;

    //Constructor creates the default board state
    public Board(){
        initDefaultBoard();
    }

    //Getter function for the chess pieces
    //  Set java will return the pieces as a references then the data in the class does not
    //  need to be set again as only one instances of the array exists
    //  Meaning that whenever the move class changes the array to make a move then the references
    //  of pieces is also updated
    public Piece[][] getPieces() {
        return pieces;
    }

    //Getter and setter function for the location of the black and white kings
    public int getWhiteKingX() {
        return whiteKingX;
    }
    public int getWhiteKingY() {
        return whiteKingY;
    }
    public void setWhiteKing(int X, int Y) {
        this.whiteKingX = X;
        this.whiteKingY = Y;
    }
    public int getBlackKingX() {
        return blackKingX;
    }
    public int getBlackKingY() {
        return blackKingY;
    }
    public void setBlackKing(int X, int Y) {
        this.blackKingX = X;
        this.blackKingY = Y;
    }

    //Prints the board
    public void print(){
        System.out.println(toString());
    }

    @Override
    public String toString() {
        String output = "xy 0  1  2  3  4  5  6  7\n";
        for (int x = 0; x < 8; x++){
            output += x + " ";
            for (int y = 0; y < 8; y++){
                if (pieces[x][y] != null){
                    output += pieces[x][y].getSymbol() + (pieces[x][y].isWhite() ? "W " : "B ");
                } else {
                    output += "   ";
                }
            }
            output += "\n";
        }
        return output;
    }

    //This is a testing function that takes a board state and creates the board
    public void testBoard(String board) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                pieces[x][y] = null;
            }
        }

        String[] Board = board.split("\n");
        if (Board.length != 9) {
            throw new IllegalStateException("Board line number is not equal to 9");
        }
        for (int x = 1; x < Board.length; x++) {
            String row = Board[x];

            int y = 0;
            if (Board[x].length() < 25) {
                throw new IllegalStateException("Board line " + x + " is not equal to 25");
            }
            for (int pos = 1; pos < 25; pos+=3){
                String piece = row.substring(pos, pos + 3);
                if (! piece.equals("   ")) {
                    //Adds the piece
                    char pieceType = piece.charAt(1);
                    boolean colour = piece.charAt(2) == 'W';
                    switch(pieceType) {
                        case('P'):
                            pieces[x-1][y] = new Pawn(colour,x-1, y);
                            break;
                        case('N'):
                            pieces[x-1][y] = new Knight(colour,x-1, y);
                            break;
                        case('B'):
                            pieces[x-1][y] = new Bishop(colour,x-1, y);
                            break;
                        case('K'):
                            pieces[x-1][y] = new King(colour,x-1, y);
                            break;
                        case('Q'):
                            pieces[x-1][y] = new Queen(colour,x-1, y);
                            break;
                        case('R'):
                            pieces[x-1][y] = new Rook(colour,x-1, y);
                            break;
                        default:
                            System.out.println("Unknown type :" + pieceType);
                    }
                }
                y++;
            }
        }
        print();
    }

    //This sets the default board which is called by the constructor
    private void initDefaultBoard(){
        pieces = new Piece[8][8];
        int w = 0;
        int b = 7;

        //Pawns
        for (int x = 0; x < 8; x++){
            pieces[x][1] = new Pawn(true, x,1);      //White
            pieces[x][6] = new Pawn(false, x,6);     //Black
        }

        //Bishops
        pieces[2][w] = new Bishop(true,2, w);        //White
        pieces[5][w] = new Bishop(true,5 ,w);
        pieces[2][b] = new Bishop(false,2, b);       //Black
        pieces[5][b] = new Bishop(false,5, b);

        //Knight
        pieces[1][w] = new Knight(true,1, w);        //White
        pieces[6][w] = new Knight(true,6, w);
        pieces[1][b] = new Knight(false,1, b);
        pieces[6][b] = new Knight(false, 6, b);       //Black

        //Rook
        pieces[0][w] = new Rook(true,0, w);          //White
        pieces[7][w] = new Rook(true, 7, w);
        pieces[0][b] = new Rook(false, 0, b);         //Black
        pieces[7][b] = new Rook(false, 7, b);

        //Queen
        pieces[3][w] = new Queen(true,3, w);         //White
        pieces[3][b] = new Queen(false,3, b);        //Black

        //King
        pieces[4][w] = new King(true,4, w);          //White
        whiteKingX = 4;
        whiteKingY = w;
        pieces[4][b] = new King(false,4, b);         //Black
        blackKingX = 4;
        blackKingY = b;

        King.setBoard(this);
    }

    //This is a debugging tool that allows the piece position to be compared to the real position of the piece in the
    // array
    public void checkPiecePositions(){
        for (int X = 0; X < 8; X++) {
            for (int Y = 0; Y < 8; Y++) {
                if (pieces[X][Y] != null && (pieces[X][Y].getX() != X || pieces[X][Y].getY() != Y)){
                    Log.log(String.format("Piece position is incorrect real position (%d, %d) piece position (%d, %d)", X, Y, pieces[X][Y].getX(), pieces[X][Y].getY()));
                    Log.log("Piece type is " + pieces[X][Y].getName() + " colour is " + pieces[X][Y].getColour());
                    print();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //throw new NullPointerException("Piece position is invalid");
                }
            }
        }
    }
}