package Game.Players;

import Core.Log;
import Game.Core.Board;
import Game.Core.Constants;
import Game.Core.Move;
import Game.Pieces.Piece;

import java.util.LinkedList;
import java.util.Random;

///This is the AI player which uses the alpha beta pruning algorithm
public class AlphaBetaPruning implements Constants {

    //This are constant values
    // Infinity is just a large values which the boards values can't be larger than
    private final int INFINITY = 1000;
    private final Random rnd = new Random();

    private final double nano = Math.pow(10, 9);
    //Node counter just counts the number of board states evaluated
    private int totalNodeCounter = 0;
    private int endNodeCount = 0;
    private int prunedNodeCounter = 0;
    //This stores the best moves
    private LinkedList<Move> bestMove = new LinkedList<>();
    ///This is a references to the game board so does not need to be updated
    private Board board;
    //This is the max node depth
    private int maxDepth = 4;

    //The constructor gets a pointer for the board
    public AlphaBetaPruning(Board board){
        this.board = board;
        //setNodeEvalTime();
    }

    public Move getAiMove(boolean white) {
        return getAiMove(white, maxDepth);
    }

    //To get the ai move then a player needs to be known
    public Move getAiMove(boolean white, int depth){
        //Set the max depth
        maxDepth = depth;
        //Reset the node counter
        totalNodeCounter = 0;
        endNodeCount = 0;
        prunedNodeCounter = 0;
        //Clears the best moves
        bestMove.clear();
        //This gets the max depth for the algorithm to search
        //maxDepth = getMaxDepth(white);
        Log.logStatus("Max Depth :" + maxDepth, '+');

        //Measures the time for the algorithm to run
        long start = System.nanoTime();
        //Runs the main algorithm
        search(white, maxDepth, -INFINITY, INFINITY);
        long end = System.nanoTime();

        //Logs the time taken for the nodes to be evaluated
        Log.log(String.format("Nodes evaluated : %d in %f s, End nodes : %d, Pruning : %d",
                totalNodeCounter, (end - start) / nano, endNodeCount, prunedNodeCounter));

        //Returns a rnd moves from the best moves
        return bestMove.get(rnd.nextInt(bestMove.size()));
    }

    //Alpha is the min value
    //Beta is the max value
    public int search(boolean white, int depth, int alpha, int beta) {
        //Check if the algorithm has run for the depth
        if (depth == 0) {
            endNodeCount++;
            //This calculates the value of the board state
            return boardVal(white);
        }

        //These are the variables that both players are going to use
        int bestValue = -INFINITY;         //Best possible move in this state
        //Calculated now so they would not need to be recalculated
        boolean nextPlayer = !white;       //The next player
        int nextDepth = depth - 1;         //The next depth
        int negBeta = -beta;               //The negative version of beta

        //list of moves that could be done at this board state
        LinkedList<Move> possibleMoves = possibleMoves(white);

        //A for each loop to iterate through the list of moves
        MoveIteration : for (Move move : possibleMoves) {
            totalNodeCounter++;
            //Performs the move on the board
            move.perform(board);
            //Recursively runs the Negamax algorithm
            int value = search(nextPlayer, nextDepth, negBeta, -alpha);
            //Undoes the board state
            move.undo(board);

            //Checks if the move is first move depth and equal to the current value
            if (value == bestValue && depth == maxDepth) {
                //Adds the move to a list of best moves
                bestMove.add(move);
                //Check if the board value is greater than the best board value
            } else if (value > bestValue) {
                //Replaces the best value
                bestValue = value;
                //Check if move in on the first move depth
                if (depth == maxDepth) {
                    //Clears the set of possible moves as a better move is found
                    bestMove.clear();
                    bestMove.add(move);
                }
                //Checks if the value is greater than alpha
                if (value > alpha) {
                    //Replaces alpha
                    alpha = value;
                    //Checks if the alpha value is now greater than the beta value
                    if (alpha >= beta) {
                        prunedNodeCounter++;
                        //Breaks the for loop as alpha >= beta
                        break MoveIteration;
                    }
                }
            }
        }
        return bestValue;
    }

    //This function evaluates the board state once it is in a terminal state
    public int boardVal(boolean white){
        //Gets the board pieces
        Piece[][] pieces = board.getPieces();
        int value = 0;
        //Loops through the board adding the value of the pieces to value
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (pieces[x][y] != null) {
                    value += ((pieces[x][y].isWhite() == white) ? 1 : -1) * pieces[x][y].getValue();
                }
            }
        }
        return value;
    }

    //Gets all of the possible moves that could be made by player
    public LinkedList<Move> possibleMoves(boolean white){
        board.checkPiecePositions();
        //Gets the board pieces
        Piece[][] pieces = board.getPieces();
        LinkedList<Move> moves = new LinkedList<>();
        //Loop through the board
        for (int x = 0; x < 8; x++){
            for (int y = 0; y < 8; y++){
                //If the piece is of the team argument white
                // then add all of the piece's moves to moves
                if (pieces[x][y] != null && pieces[x][y].isWhite() == white){
                    //Piece will return a linkedList of moves
                    moves.addAll(pieces[x][y].getMoves());
                }
            }
        }
        //moves = orderMoves(moves, pieces);
        //Returns the moves
        return moves;
    }
}