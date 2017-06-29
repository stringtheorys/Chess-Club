package Database;

import Alerts.MessageAlert;
import Core.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public abstract class TournamentDB_interface extends DB_interface {

    /*
        -1 : Error Occurred
         0 : TournamentId not found
         1 : Tournament Created
     */
    public static int createTournament(int AdminId, String tournamentName, String[] playerNames) {
        //Inserts into the tournament information table with the tournament name, admin id and player number
        String sql = sqlBuilder("INSERT INTO Tournament_Info (TournamentName, AdminId, PlayerNum) VALUES ('?', ?, ?)", tournamentName, AdminId, playerNames.length);
        executeUpdate(sql);

        //Gets the tournament id from the tournament just created
        int tournamentId;
        sql = sqlBuilder("SELECT TournamentId FROM Tournament_Info WHERE TournamentName = '?'", tournamentName);
        ResultSet rs = executeQuery(sql);
        if (rs == null) {
            Log.log("CreateTournament : No result set returned");
            return -1;
        }

        //Tries to add all of the player into matches for the first round
        try {
            if (rs.next()){
                tournamentId = rs.getInt(1);

                int numPlayers = playerNames.length;
                for (int pos = 0; pos < numPlayers / 2; pos++){
                    //TournamentId, Player1Name, Player2Name, Outcome, Round, MatchPosition
                    sql = sqlBuilder("INSERT INTO Tournament_Matches VALUES (?, '?', '?', ?, ?, ?)", tournamentId, playerNames[pos], playerNames[numPlayers-pos-1], 3, 1, pos);
                    executeUpdate(sql);
                }
                //If successful then return 1
                return 1;
            } else {
                return 0;
            }
        } catch (SQLException e) {
            Log.log("CreateTournament : Exception >" + e.getMessage());
            return -1;
        } finally {
            try {
                disconnect_DB();
            } catch (SQLException e) {
                Log.log("CreateTournament : Exception >" + e.getMessage());
            }
        }
    }

    public static LinkedList<String[]> getTournaments(){
        String sql = sqlBuilder("SELECT TournamentId, TournamentName FROM Tournament_Info");
        ResultSet rs = executeQuery(sql);
        if (rs == null) {
            Log.log("getTournament : No result set returned");
            return null;
        }

        LinkedList<String[]> tournamentsInfo = new LinkedList<>();
        try {
            while (rs.next()){
                String id = Integer.toString(rs.getInt(1));
                String name = rs.getString(2);
                tournamentsInfo.add(new String[]{id, name});
            }
        } catch (SQLException e) {
            Log.log("getTournaments : Exception >" + e.getMessage());
        } finally {
            try {
                disconnect_DB();
            } catch (SQLException e) {
                Log.log("getTournaments : Exception >" + e.getMessage());
            }
        }

        return tournamentsInfo;
    }


    public static String[] getTournamentInfo(int TournamentId){
        String sql = sqlBuilder("SELECT TournamentName, PlayerNum, AdminId FROM Tournament_Info WHERE TournamentId = ?", TournamentId);
        ResultSet rs = executeQuery(sql);
        if (rs == null) {
            Log.log("getTournamentInfo : No result set returned");
            return null;
        }

        try {
            if (rs.next()){
                String TournamentName = rs.getString(1);
                String playerNum = Integer.toString(rs.getInt(2));
                String adminId = Integer.toString(rs.getInt(3));
                return new String[]{TournamentName, playerNum, adminId};
            } else {
                return null;
            }
        } catch (SQLException e) {
            Log.log("getTournamentInfo : Exception >" + e.getMessage());
            return null;
        } finally {
            try {
                disconnect_DB();
            } catch (SQLException e) {
                Log.log("getTournamentInfo : Exception >" + e.getMessage());
            }
        }
    }

    public static String[] getMatchInfo(int tournamentId, int Round, int pos){
        String sql = String.format("SELECT * FROM Tournament_Matches WHERE (TournamentId = %d) and (Round = %d) and (Match_pos = %d)", tournamentId, Round, pos);

        ResultSet rs = executeQuery(sql);
        if (rs == null) {
            Log.log("getMatchInfo : No result set returned");
            return null;
        }

        try {
            if (rs.next()){
                String[] Information = new String[3];
                Information[0] = rs.getString(2);
                Information[1] = rs.getString(3);
                Information[2] = Integer.toString(rs.getInt(4));
                return Information;
            } else {
                return null;
            }
        } catch (SQLException e) {
            Log.log("getMatchInfo : Exception >" + e.getMessage());
            return null;
        } finally {
            try {
                disconnect_DB();
            } catch (SQLException e) {
                Log.log("getMatchInfo : Exception >" + e.getMessage());
            }
        }
    }

    public static void updateMatch(int tournamentId, String winnerName, int match_round, int match_pos, int winner, boolean endTournament) {

        //Update the outcome of the current match
        String sql = sqlBuilder("UPDATE Tournament_Matches SET Outcome = ? WHERE TournamentId = ? and Round = ? and Match_pos = ?", tournamentId, winner, match_round, match_pos);
        executeUpdate(sql);

        //If the end of tournament then there are no extra tournament matches to update
        //So the next tournament needed to be updated
        if (endTournament == false) {
            //Gets the next rounds information
            int nextRound = match_round + 1;
            int nextPos = match_pos / 2;
            //Gets the players from the next round match
            sql = sqlBuilder("SELECT Player1Name, Player2Name FROM Tournament_Matches WHERE TournamentId = ? and Round = ? and Match_pos = ?", tournamentId, nextRound, nextPos);
            ResultSet rs = executeQuery(sql);
            if (rs == null) {
                MessageAlert.DataBaseConnectionAlert();
            } else {
                try {
                    //If the result set returns information then a match already exists and so only needs updating
                    // else a new match needs to be inserted into the table
                    if (rs.next()) {
                        //Update current match
                        if (match_pos % 2 == 0) {
                            sql = sqlBuilder("UPDATE Tournament_Matches SET Player2Name = '?' WHERE TournamentId = ? and Round = ? and Match_pos = ?", winnerName, tournamentId, nextRound, nextPos);
                        } else {
                            sql = sqlBuilder("UPDATE Tournament_Matches SET Player1Name = '?' WHERE TournamentId = ? and Round = ? and Match_pos = ?", winnerName, tournamentId, nextRound, nextPos);
                        }
                    } else {
                        //Insert current match
                        if (match_pos % 2 == 0) {
                            sql = sqlBuilder("INSERT INTO Tournament_Matches (TournamentId, Player1Name, Player2Name, Outcome, Round, Match_Pos) VALUES (?, '?', '', 0, ?, ?)", tournamentId, winnerName, nextRound, nextPos);
                        } else {
                            sql = sqlBuilder("INSERT INTO Tournament_Matches (TournamentId, Player1Name, Player2Name, Outcome, Round, Match_Pos) VALUES (?, '', '?', 0, ?, ?)", tournamentId, winnerName, nextRound, nextPos);
                        }
                    }
                    //Update the database
                    executeUpdate(sql);
                } catch (SQLException e) {
                    Log.log("updateMatch : Exception >" + e.getMessage());
                } finally {
                    try {
                        //Disconnect from the database
                        disconnect_DB();
                    } catch (SQLException e) {
                        Log.log("updateMatch : Exception >" + e.getMessage());
                    }
                }
            }
        }
    }
}
