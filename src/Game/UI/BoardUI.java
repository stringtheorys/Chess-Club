package Game.UI;

import Core.Log;
import Game.Core.Board;
import Game.Core.Move;
import Game.Pieces.Piece;
import Game.Players.AlphaBetaPruning;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.LinkedList;

//This is the main class to control the chess board ui
public class BoardUI extends VBox {

    //Attributes
    private ChessGame parent;
    private GridPane board;
    private TileUI[][] tiles;
    private Board chessBoard;
    private boolean white;
    private int gameMode = 2;
    private AlphaBetaPruning abp;

    //This is the current selected x position
    private int selectX = -1;
    private int selectY = -1;

    //Constructor
    public BoardUI(ChessGame parent, int gameMode){
        this.gameMode = gameMode;
        this.parent = parent;
        initGui();
    }

    //Creates the gui
    private void initGui(){
        white = true;
        chessBoard = new Board();
        abp = new AlphaBetaPruning(chessBoard);
        board = new GridPane();
        tiles = new TileUI[8][8];
        Piece[][] pieces = chessBoard.getPieces();

        //Creates the tiles on the board
        for (int x = 0; x < 8; x++){
            for (int y = 0; y < 8; y++){
                TileUI tile = new TileUI(this, x, y);
                tiles[x][y] = tile;
                board.add(tile, x, y);
                if (pieces[x][y] != null){
                    tiles[x][y].addPiece(pieces[x][y]);
                }
            }
        }
        this.getChildren().add(board);
    }

    //Makes the move
    public void makeMove(int x, int y){
        //Gets the pieces
        Piece[][] pieces = chessBoard.getPieces();
        //Removes the piece image from the tile on the gui
        tiles[selectX][selectY].removePiece();
        tiles[x][y].removePiece();
        //Makes the move
        Move move = new Move(selectX, selectY, x, y);
        move.perform(chessBoard);
        //Replace the image with the new one
        tiles[x][y].addPiece(pieces[x][y]);
        //If castle occurs then update the screen with the rook position
        if (move.hasCastle()) {
            Log.log("Move castle");
            tiles[move.getCastleFromX()][move.getCastleFromY()].removePiece();
            tiles[move.getCastleToX()][move.getCastleToY()].removePiece();
            tiles[move.getCastleToX()][move.getCastleToY()].addPiece(pieces[move.getCastleToX()][move.getCastleToY()]);
        }
        //Adds move to moveUI
        parent.addPlayerMove(white, selectX, selectY, x, y);
        unhighlightAll();
        nextTurn();
    }

    //Show moves for the user once clicked on tile
    public void showMoves(int x, int y){
        //If in local game or vs computer and white player
        if (gameMode == 1 || (gameMode == 2 && white)) {
            //If clicked on the selected tile then unhighlight
            if (selectX == x && selectY == y) {
                unhighlightAll();
            } else {
                Piece[][] pieces = chessBoard.getPieces();
                unhighlightAll();
                //If the tile has a piece and is the same as current player
                if (pieces[x][y] != null && pieces[x][y].isWhite() == white) {
                    selectX = x;
                    selectY = y;
                    LinkedList<Move> moves = pieces[x][y].getMoves();
                    //Iterate through all the moves
                    for (Move move : moves) {
                        //Highlight the position based if empty piece in position
                        if (pieces[move.getToX()][move.getToY()] == null) {
                            tiles[move.getToX()][move.getToY()].move();
                        } else {
                            tiles[move.getToX()][move.getToY()].attack();
                        }
                    }
                } else {
                    //If no piece then unhighlight
                    unhighlightAll();
                }
            }
        }
    }

    //Change all of the tiles backgrounds to default
    private void unhighlightAll(){
        for (int x = 0; x < 8; x++){
            for (int y = 0; y < 8; y++){
                tiles[x][y].unHighlight();
            }
        }
        selectX = -1;
        selectY = -1;
    }

    //The next turn
    private void nextTurn(){
        //Next player
        white = !white;
        //Checks if in checkmate
        if (isCheckmate(white)){
            endGame();
        } else if (gameMode == 1){                          //Local game

        } else if (gameMode == 2 && white == false){        // Ai game move
            Log.log("Starting ai move");
            //Gets the move to the alpha beta pruning
            Move aiMove = abp.getAiMove(white);
            //Sets the select position to the move
            selectX = aiMove.getFromX();
            selectY = aiMove.getFromY();
            //Makes the move
            makeMove(aiMove.getToX(), aiMove.getToY());
        }
    }

    //This check is there is checkmate
    private boolean isCheckmate(boolean white){
        Piece[][] pieces =  chessBoard.getPieces();

        //Else not attacked then return false
        return false;
    }

    private void endGame() {
        Log.log("Game ends");
    }

}
