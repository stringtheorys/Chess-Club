package Menus;

import Core.Constants;
import Core.Log;
import Database.TournamentDB_interface;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class TournamentUI extends VBox implements Constants {

    //Attibutes
    private int MatchMargin = 50;

    private String tournamentName;
    private int tournamentId;
    private int playerNum;
    private int Rounds;

    private Label topLabel;
    private HBox tournamentRounds;

    private int UserId = -1;
    private ViewTournamentsUI parent;
    private boolean isAdmin;

    private TournamentMatchButton[][] matchButtons;

    //Constructor
    public TournamentUI(ViewTournamentsUI parent, int UserId, int tournamentId){

        this.parent = parent;
        this.UserId = UserId;

        this.tournamentId = tournamentId;
        initGui();
    }

    //Creates the UI
    private void initGui(){

        //TournamentInfo = new String[]{Name, Size};
        //Gets the tournament information with the tournament name, player num and the admin id
        String[] tournamentInfo = TournamentDB_interface.getTournamentInfo(tournamentId);
        if (tournamentInfo != null){
            //Checks if the user is the admin
            isAdmin = (UserId == Integer.parseInt(tournamentInfo[2]));

            //Gets the tournament general information
            tournamentName = tournamentInfo[0];
            //Number of players in the tournament
            playerNum = Integer.parseInt(tournamentInfo[1]);
            //The number of players is reduced by half each time so the number of rounds is log 2, player num
            Rounds = (int) (Math.log(playerNum) / Math.log(2));

            //Create the label for the tournament name
            topLabel = new Label(tournamentName);
            topLabel.setFont(mediumFont);
            this.getChildren().add(topLabel);

            //Which is the box for all of the rounds, which is horizontal as rounds goes left to right
            tournamentRounds = new HBox();
            tournamentRounds.setAlignment(Pos.CENTER);

            //This is the number of matches on the first round
            int numRoundMatches = playerNum / 2;

            //This is a list of match buttons, as the number of buttons will be max
            matchButtons = new TournamentMatchButton[Rounds][playerNum];

            //For each round then get all of the matches
            for (int Round = 0; Round < Rounds; Round++){

                //Creates a vertical box for the matches
                VBox RoundBox = new VBox();
                RoundBox.setAlignment(Pos.CENTER);

                //For each round match for the match
                for (int Pos = 0; Pos < numRoundMatches; Pos++){
                    //MatchInfo = new String[]{player1Name, player2Name, winner};
                    //Gets the match information
                    String[] matchInfo = TournamentDB_interface.getMatchInfo(tournamentId, Round, Pos);

                    //Match button
                    TournamentMatchButton matchButton;

                    //Checks if the match info is null, then no match or error
                    if (matchInfo == null){
                        matchButton = new TournamentMatchButton(this, isAdmin, Pos, Round);
                    } else {

                        //Gets the match information with the player names and winner
                        String player1Name = matchInfo[0];
                        String player2Name = matchInfo[1];
                        int winner = Integer.parseInt(matchInfo[2]);

                        //Creates the match button
                        if (winner == 0){
                            matchButton = new TournamentMatchButton(this, isAdmin, Pos, Round, player1Name, player2Name);
                        } else {
                            matchButton = new TournamentMatchButton(this, isAdmin, Pos, Round, player1Name, player2Name, winner);
                        }
                    }

                    //Adds the match to list to list of buttons
                    matchButtons[Round][Pos] = matchButton;
                    //Adds the match button
                    RoundBox.getChildren().add(matchButton);

                    //Sets the margins of the button
                    setMargin(matchButton, tournamentMargin(numRoundMatches, Pos, MatchMargin));
                }

                //Once the round is done then add round to tournament
                tournamentRounds.getChildren().add(RoundBox);

                //Reduces the number of round matches in half
                numRoundMatches /= 2;
                //Doubles the match margin as half as many matches
                MatchMargin *= 2;
            }
            //Adds the rounds to box
            this.getChildren().add(tournamentRounds);
        }
    }

    //Gets the tournament margin
    private Insets tournamentMargin(int roundsize, int pos, int matchMargin){
        if (pos == roundsize-1){
            return new Insets(0,0,0,0);
        } else {
            return new Insets(0,0, matchMargin,0);
        }
    }

    //Updates the tournament button
    private void updateButton(String winnerName, int match_round, int match_pos, int winner) {
        //Player 1 win = true; Player 2 win = false;
        if (match_round == Rounds-1) {
            TournamentDB_interface.updateMatch(tournamentId, winnerName, match_round, match_pos, winner, false);
        } else {
            TournamentDB_interface.updateMatch(tournamentId, winnerName, match_round, match_pos, winner, true);
            int newPos = match_pos / 2;
            int newRound = match_round + 1;

            if (match_pos % 2 == 0) {
                matchButtons[newRound][newPos].updatePlayer1(winnerName);
            } else {
                matchButtons[newRound][newPos].updatePlayer2(winnerName);
            }
        }
    }

    //Tournament button class
    private class TournamentMatchButton extends VBox implements Constants {
        //Attributes
        private Button topButton;
        private Button bottomButton;
        private int winner = 0;
        private String player1Name = "";
        private String player2Name = "";

        private TournamentUI parent;
        private boolean admin = false;
        private int match_pos = 0;
        private int match_round = 0;

        private final static int ButtonWidth = 150;

        //Constructor
        public TournamentMatchButton(TournamentUI parent, boolean admin, int match_pos, int match_round){
            this.parent = parent;
            this.admin = admin;

            this.match_pos = match_pos;
            this.match_round = match_round;

            topButton = new Button(this.player1Name);
            topButton.setFont(mediumFont);
            topButton.setPrefWidth(ButtonWidth);
            topButton.setBorder(blackBorder);
            topButton.setOnMousePressed(event -> buttonClick(1));
            this.getChildren().add(topButton);
            bottomButton = new Button(this.player2Name);
            bottomButton.setPrefWidth(ButtonWidth);
            bottomButton.setFont(mediumFont);
            bottomButton.setBorder(blackBorder);
            bottomButton.setOnMousePressed(event -> buttonClick(2));
            this.getChildren().add(bottomButton);
        }
        //Constructor
        public TournamentMatchButton(TournamentUI parent, boolean admin, int match_pos, int match_round, String
                player1Name, String player2Name){
            this(parent, admin, match_pos, match_round);

            this.player1Name = player1Name;
            this.player2Name = player2Name;

            topButton.setText(player1Name);
            bottomButton.setText(player2Name);
        }
        //Constructor
        public TournamentMatchButton(TournamentUI parent, boolean admin, int match_pos, int match_round, String
                player1Name, String player2Name, int winner) {
            this(parent, admin, match_pos, match_round, player1Name, player2Name);

            this.winner = winner;

            if (winner == 1) {
                topButton.setBackground(redBackground);
            } else if (winner == 2) {
                bottomButton.setBackground(redBackground);
            } else {
                Log.log("Error TournamentMatchButton winner not 1 or 2");
            }
        }

        //On button click
        private void buttonClick(int winner){
            if (admin) {
                if (!(player1Name.equals("") && player2Name.equals(""))) {
                    if (this.winner == 0) {
                        this.winner = winner;

                        if (winner == 1) {
                            topButton.setBackground(redBackground);
                            parent.updateButton(player1Name, match_round, match_pos, 1);
                        } else if (winner == 2) {
                            bottomButton.setBackground(redBackground);
                            parent.updateButton(player2Name, match_round, match_pos, 2);
                        } else {
                            Log.log("Error TournamentMatchButton winner not 1 or 2");
                        }
                    }
                }
            }
        }

        //Update the top player
        public void updatePlayer1(String playerName) {
            player1Name = playerName;
            topButton.setText(player1Name);
        }
        //Update the down player
        public void updatePlayer2(String playerName) {
            player2Name = playerName;
            bottomButton.setText(player2Name);
        }
    }
}
