package Game.Core;

import java.util.LinkedList;

public class Time implements Constants {

    //This is a set of dynamic arrays to record the times taken to complete
    //  operations within the chess game to check optimisation of different parts of it
    private static LinkedList<Long> possibleMoves = new LinkedList<>();
    //As the time taken for each depth will take different lengths
    //  Then each depth is recorded differently
    //  This is done using a two dimensional dynamic arrays
    private static LinkedList<LinkedList<Long>> alphaBeta = new LinkedList<>();
    private static LinkedList<Long> performMove = new LinkedList<>();
    private static LinkedList<Long> undoMove = new LinkedList<>();
    private static LinkedList<Long> boardValue = new LinkedList<>();

    //This is a set of dynamic arrays to record the times taken to
    //  generate all of the moves for each chess piece
    private static LinkedList<Long> pawn = new LinkedList<>();
    private static LinkedList<Long> queen = new LinkedList<>();
    private static LinkedList<Long> king = new LinkedList<>();
    private static LinkedList<Long> knight = new LinkedList<>();
    private static LinkedList<Long> rook = new LinkedList<>();
    private static LinkedList<Long> bishop = new LinkedList<>();

    //This is set of functions to add the operation time to the list
    public static void PossibleMoves(long time) {
        possibleMoves.add(time);
    }

    public static void AlphaBeta(long time, int depth) {
        if (alphaBeta.size() == 0) {
            for (int pos = 0; pos < depth; pos++) {
                alphaBeta.add(new LinkedList<>());
            }
        }
        alphaBeta.get(depth).add(time);
    }

    //This is a set of functions to record the time taken for each pieces to generate all moves
    public static void PerformMove(long time) {
        performMove.add(time);
    }
    public static void UndoMove(long time) {
        undoMove.add(time);
    }
    public static void BoardValue(long time) {
        boardValue.add(time);
    }
    public static void Pawn(long time) {
        pawn.add(time);
    }
    public static void Queen(long time) {
        queen.add(time);
    }
    public static void King(long time) {
        king.add(time);
    }
    public static void Knight(long time) {
        knight.add(time);
    }
    public static void Rook(long time) {
        rook.add(time);
    }
    public static void Bishop(long time) {
        bishop.add(time);
    }

    //This is a set of functions to get all of the information in the array
    //  The functions are all private so to get the data then the printTimes function needs to be called
    private static void getPossibleMoves() {
        long total = 0;
        for (long time : possibleMoves) {
            total += time;
        }
        double average = total / possibleMoves.size();
        System.out.println("Possible moves :" + average);
    }

    //Time for ABP to occur at each depth
    private static void getAlphaBeta() {
        System.out.println("Alpha Beta");

        int pos = 1;
        for (LinkedList<Long> times : alphaBeta) {
            if (alphaBeta.size() > 0) {
                long total = 0;
                for (long time : times) {
                    total += time;
                }
                double average = total / times.size();
                System.out.println("Depth " + pos + " :" + average);
                pos++;
            }
        }
    }

    //Time for Perform move to occur
    private static void getPerformMove() {
        long total = 0;
        for (long time : performMove) {
            total += time;
        }
        double average = total / performMove.size();
        System.out.println("Perform move :" + average);
    }

    //Time for undo move to occur
    private static void getUndoMove() {
        long total = 0;
        for (long time : undoMove) {
            total += time;
        }
        double average = total / undoMove.size();
        System.out.println("Undo move :" + average);
    }

    //Time for board value to occur
    private static void getBoardValue() {
        long total = 0;
        for (long time : boardValue) {
            total += time;
        }
        double average = total / boardValue.size();
        System.out.println("Board value :" + average);
    }

    //Time for pawn moves to occur
    private static void getPawn() {
        long total = 0;
        for (long time : pawn) {
            total += time;
        }
        double average = total / pawn.size();
        System.out.println("Pawn moves :" + average + ", " + pawn.size());
    }

    //Time for queen moves to occur
    private static void getQueen() {
        long total = 0;
        for (long time : queen) {
            total += time;
        }
        double average = total / queen.size();
        System.out.println("Queen moves :" + average + ", " + queen.size());
    }

    //Time for king moves to occur
    private static void getKing() {
        long total = 0;
        for (long time : king) {
            total += time;
        }
        double average = total / king.size();
        System.out.println("King moves :" + average + ", " + king.size());
    }

    //Time for  knight moves to occur
    private static void getKnight() {
        long total = 0;
        for (long time : knight) {
            total += time;
        }
        double average = total / knight.size();
        System.out.println("Knight moves :" + average + ", " + knight.size());
    }

    //Time for rook moves to occur
    private static void getRook() {
        long total = 0;
        for (long time : rook) {
            total += time;
        }
        double average = total / rook.size();
        System.out.println("Rook moves :" + average + ", " + rook.size());
    }

    //Time for bishop moves to occur
    private static void getBishop() {
        long total = 0;
        for (long time : bishop) {
            total += time;
        }
        double average = total / bishop.size();
        System.out.println("Bishop moves :" + average + ", " + bishop.size());
    }

    //This is a function to print all of the information gathered
    public static void printTime() {
        if (time) {
            System.out.println("Time taken for operations");
            getAlphaBeta();
            getPossibleMoves();
            getPerformMove();
            getUndoMove();
            getBoardValue();
            getPawn();
            getQueen();
            getKing();
            getKnight();
            getRook();
            getBishop();
        }
    }
}