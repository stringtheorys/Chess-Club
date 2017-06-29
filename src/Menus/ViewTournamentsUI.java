package Menus;

import Alerts.MessageAlert;
import Core.Constants;
import Core.Log;
import Core.ScreenController;
import Database.DB_interface;
import Database.TournamentDB_interface;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.LinkedList;

public class ViewTournamentsUI extends VBox implements Constants {

    //Attributes
    private ScreenController parent;
    private int UserId = -1;
    private int AccessLevel = -1;

    private HBox mainBox;
    private TournamentUI currentTournament;
    private ScrollPane tournamentListScroll;
    private VBox tournamentListBox;
    private Label topLabel;
    private LinkedList<TournamentButton> tournamentButtons;
    private Button createTournamentButton;
    private Button menuButton;

    //Constructor
    public ViewTournamentsUI(ScreenController parent){
        if (DB_interface.test_DBConnection()) {
            this.parent = parent;
            initGui();
        } else {
            MessageAlert.DataBaseConnectionAlert();
            parent.showPlayChess();
        }
    }
    //Constructor
    public ViewTournamentsUI(ScreenController parent, int UserId, int AccessLevel){
        if (DB_interface.test_DBConnection()) {
            this.parent = parent;
            this.UserId = UserId;
            this.AccessLevel = AccessLevel;
            initGui();

            if (AccessLevel > 1){
                createTournamentButton = new Button("Create Tournament");
                createTournamentButton.setFont(mediumFont);
                createTournamentButton.setOnAction(event -> createTournamentButton());
                tournamentListBox.getChildren().add(createTournamentButton);
            }
        } else {
            MessageAlert.DataBaseConnectionAlert();
            parent.showPlayChess();
        }
    }

    //Create the tournament buttons
    private void createTournamentButton(){
        Log.log("CreateTournamentButton : changing to create tournament screen");
        parent.showCreateTournament(UserId, AccessLevel);
    }

    //Create the UI
    private void initGui(){
        mainBox = new HBox();
        this.setAlignment(Pos.CENTER);
        mainBox.setSpacing(30);

        tournamentListScroll = new ScrollPane();
        tournamentListScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        tournamentListScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        tournamentListBox = new VBox();
        topLabel = new Label("View Tournaments");
        topLabel.setFont(mediumFont);
        tournamentListBox.getChildren().add(topLabel);

        LinkedList<String[]> tournaments = TournamentDB_interface.getTournaments();
        if (tournaments == null){
            if (UserId != 0) {
                parent.showPlayChess(UserId, AccessLevel);
            } else {
                parent.showPlayChess();
            }
        } else {
            tournamentButtons = new LinkedList<>();
            for (String[] info : tournaments) {
                TournamentButton tournamentButton = new TournamentButton(this, info[1], Integer.parseInt(info[0]));
                tournamentButtons.add(tournamentButton);
                tournamentListBox.getChildren().add(tournamentButton);
                setMargin(tournamentButton, mediumMargin);
            }
        }
        menuButton = new Button("Menu");
        menuButton.setFont(mediumFont);
        menuButton.setOnAction(event -> menu());
        tournamentListBox.getChildren().add(menuButton);
        tournamentListBox.setMargin(menuButton, mediumMargin);
        tournamentListScroll.setContent(tournamentListBox);
        mainBox.getChildren().add(tournamentListScroll);
        this.getChildren().add(mainBox);

        if (tournaments != null && tournaments.size() > 0) {
            showTournament(Integer.parseInt(tournaments.get(0)[0]));
        }
    }

    //Show the menu page
    private void menu(){
        if (UserId > 0){
            parent.showPlayChess(UserId, AccessLevel);
        } else {
            parent.showPlayChess();
        }
    }
    //Show the tournament
    private void showTournament(int TournamentId){
        if (currentTournament != null){
            mainBox.getChildren().remove(currentTournament);
        }
        Log.log("showTournament : TournamentId = " + Integer.toString(TournamentId));
        currentTournament = new TournamentUI(this, UserId, TournamentId);
        mainBox.getChildren().add(1, currentTournament);
    }

    //Tournament button class
    private class TournamentButton extends Button {

        public TournamentButton(ViewTournamentsUI parent, String tournamentName, int tournamentId){
            setText(tournamentName);
            setFont(mediumFont);
            setOnMousePressed(event -> parent.showTournament(tournamentId));
        }
    }
}
