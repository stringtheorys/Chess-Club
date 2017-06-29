package Menus;

import Alerts.MessageAlert;
import Database.UserDB_interface;
import Core.Constants;
import Core.Log;
import Core.ScreenController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class PlayChessUI extends VBox implements Constants {

    //Attribute
    private int UserId = -1;
    private int AccessLevel = -1;
    private ScreenController parent;

    private Label topLabel;
    private Button localGameButton;
    private Button vsComputerButton;
    private Button viewTournamentButton;
    private Button createTournamentButton;

    private VBox profileBox;

    private Button signInButton;
    private Button registerButton;

    private Label usernameLabel;
    private Image userImage;
    private ImageView userImageView;
    private Button logOutButton;

    //Constructor
    public PlayChessUI(ScreenController parent){
        super();

        this.parent = parent;
        this.setAlignment(Pos.TOP_CENTER);

        Log.logStatus("PlayChessUI : PlayChessUICreating Play Chess screen", '+');
        initGui();
        initSignIn();
    }

    //Constructor
    public PlayChessUI(ScreenController parent, int UserId, int AccessLevel){
        this(parent);

        this.UserId = UserId;
        this.AccessLevel = AccessLevel;

        Log.logStatus("PlayChessUI : Create the User Profile on Play Chess screen", '+');

        initUserProfile();
    }

    //Creates the UI
    private void initGui(){

        topLabel = new Label("Chess");
        topLabel.setFont(extraLargeFont);
        this.getChildren().add(topLabel);
        setMargin(topLabel, mediumMargin);

        localGameButton = new Button("Play local game");
        localGameButton.setFont(mediumFont);
        localGameButton.setOnAction(event -> playLocalGameButton());
        this.getChildren().add(localGameButton);
        setMargin(localGameButton, mediumMargin);

        vsComputerButton = new Button("Play Vs Computer");
        vsComputerButton.setFont(mediumFont);
        vsComputerButton.setOnAction(event -> playVsComputerButton());
        this.getChildren().add(vsComputerButton);
        setMargin(vsComputerButton, mediumMargin);

        viewTournamentButton = new Button("View Tournaments");
        viewTournamentButton.setFont(mediumFont);
        viewTournamentButton.setOnAction(event -> viewTournamentsButton());
        this.getChildren().add(viewTournamentButton);
        setMargin(viewTournamentButton, mediumMargin);
    }

    //Show the sign page
    private void initSignIn(){
        profileBox = new VBox();
        profileBox.setAlignment(Pos.TOP_CENTER);
        signInButton = new Button("Sign In");
        signInButton.setFont(mediumFont);
        signInButton.setOnAction(event -> signInButton());
        profileBox.getChildren().add(signInButton);
        setMargin(signInButton, mediumMargin);

        registerButton = new Button("Create Account");
        registerButton.setFont(mediumFont);
        registerButton.setOnAction(event -> registerButton());
        profileBox.getChildren().add(registerButton);
        this.getChildren().add(0, profileBox);
        setMargin(profileBox, rightMargin);
    }

    //If User log in then create the user profile
    private void initUserProfile(){

        //Info = new String[] {username, photoLocation, level};
        String[] userInfo = UserDB_interface.getUserInfo(UserId);
        if (userInfo != null) {

            profileBox.getChildren().remove(signInButton);
            profileBox.getChildren().remove(registerButton);

            usernameLabel = new Label(userInfo[0]);
            usernameLabel.setFont(mediumFont);
            profileBox.getChildren().add(usernameLabel);

            userImage = new Image(getClass().getResourceAsStream("ProfilePhotos\\" + userInfo[1]));
            userImageView = new ImageView(userImage);
            userImageView.setFitHeight(100);
            userImageView.setPreserveRatio(true);
            profileBox.getChildren().add(userImageView);

            logOutButton = new Button("Log out");
            logOutButton.setFont(smallFont);
            logOutButton.setOnAction(event -> logOut());
            profileBox.getChildren().add(logOutButton);

            if (AccessLevel > 1) { //Not a student
                createTournamentButton = new Button("Create Tournament");
                createTournamentButton.setFont(mediumFont);
                createTournamentButton.setOnAction(event -> createTournamentButton());
                this.getChildren().add(createTournamentButton);
            }
        } else {
            Log.logError("initUserProfie : Play Chess load profile fail");
            MessageAlert.DataBaseConnectionAlert();
        }
    }

    //Button Methods
    private void logOut(){
        Log.log("logOut : Log out button pressed");
        parent.showPlayChess();
    }
    private void signInButton(){
        Log.log("signInButton : Sign In button pressed");
        parent.showSignIn();
    }
    private void registerButton(){
        Log.log("registerButton : Register button pressed");
        parent.showRegistration();
    }
    private void playLocalGameButton() {
        Log.log("playLocalGameButton : Play Local Chess Game button pressed");
        if (UserId > 0) {
            parent.showLocalGame(UserId, AccessLevel);
        } else {
            parent.showLocalGame();
        }
    }
    private void playVsComputerButton(){
        Log.log("playVsComputerButton : Play Vs Computer Button pressed");
        if (UserId > 0) {
            parent.showVsComputerGame(UserId, AccessLevel);
        } else {
            parent.showVsComputerGame();
        }
    }
    private void viewTournamentsButton(){
        Log.log("viewTournamentsButton : View Tournaments Button pressed");
        if (UserId > 0) {
            parent.showViewTournaments(UserId, AccessLevel);
        } else {
            parent.showViewTournaments();
        }
    }
    private void createTournamentButton(){
        Log.log("createTournamentButton : Create tournament Button pressed");
        parent.showCreateTournament(UserId, AccessLevel);
    }
}
