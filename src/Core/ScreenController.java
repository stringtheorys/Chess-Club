package Core;

import Game.UI.ChessGame;
import Menus.*;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

//This is the controller for the main screen with the box on the screen
public class ScreenController extends HBox {

    //Attributes
    private VBox currentScreen;
    private Driver parent;

    //Constructor
    public ScreenController(Driver parent){
        super();

        this.parent = parent;
        this.setAlignment(Pos.CENTER);
        showPlayChess();
    }

    //This removes the current screen
    //  It recursively remove the current screen till children size is equals to zero
    private void removeCurrentScreen(){
        Log.log("removeCurrentScreen : Removing the current screen");
        if (getChildren().size() == 1) {
            this.getChildren().remove(0);
            //removeCurrentScreen();
        }
    }

    //Set the size of the screen to menu size
    public void setNormalSize(){
        parent.setNormalSize();
    }

    //Shows the play chess menu screen
    public void showPlayChess(int... UserInfo){
        removeCurrentScreen();
        //Checks if a user has logged in as data is added to the argument UserInfo
        if (UserInfo.length == 0) {
            currentScreen = new PlayChessUI(this);
            Log.logStatus("showPlayerChess : Showing play chess screen", '+');
        } else {
            currentScreen = new PlayChessUI(this, UserInfo[0], UserInfo[1]);
            Log.logStatus("showPlayerChess : Showing play chess screen with userId: " + UserInfo[0] + ", " +
                    "AccessLevel: " +
                    UserInfo[1], '+');
        }
        this.getChildren().add(currentScreen);
    }

    //Shows the sign in menu
    public void showSignIn(){
        removeCurrentScreen();
        currentScreen = new SignInUI(this);
        Log.logStatus("showSignIn : Showing sign in screen", '+');
        this.getChildren().add(currentScreen);
    }

    //Shows the registration menu
    public void showRegistration(){
        removeCurrentScreen();
        currentScreen = new RegisterUI(this);
        Log.logStatus("showRegistration : Showing registration screen", '+');
        this.getChildren().add(currentScreen);
    }

    //Shows the chess game screen
    //  The arguments are used to allow the gamemode type and userinfo to change
    private void showGame(int gameMode, String type, int... UserInfo){
        removeCurrentScreen();
        if (UserInfo.length == 0) {
            currentScreen = new ChessGame(this, gameMode);
        } else {
            currentScreen = new ChessGame(this, gameMode, UserInfo[0], UserInfo[1]);
        }
        Log.logStatus("showGame : Showing " + type + " chess game", '+');
        parent.setChessGameSize();
        this.getChildren().add(currentScreen);
    }

    //These function are for the chess mode types
    public void showLocalGame(int... UserInfo){
        showGame(1, "Local", UserInfo);
    }
    public void showOnlineGame(int... UserInfo){
        showGame(3, "Online", UserInfo);
    }
    public void showVsComputerGame(int... UserInfo){
        showGame(2, "Computer", UserInfo);
    }

    //This shows the tournament screen
    public void showViewTournaments(int... UserInfo){
        removeCurrentScreen();
        if (UserInfo.length == 0) {
            currentScreen = new ViewTournamentsUI(this);
            Log.logStatus("showViewTournament : Showing view tournament", '+');
        } else {
            currentScreen = new ViewTournamentsUI(this, UserInfo[0], UserInfo[1]);
            Log.logStatus("showViewTournament : Showing view tournament with userId: " + UserInfo[0] + ", " +
                    "AccessLevel: " +
                    UserInfo[1], '+');
        }
        this.getChildren().add(currentScreen);
    }

    //This shows the Create a tournament screen
    public void showCreateTournament(int UserId, int AccessLevel){
        removeCurrentScreen();
        currentScreen = new CreateTournamentUI(this, UserId, AccessLevel);
        Log.logStatus("showCreateTournament : Showing create tournament", '+');
        this.getChildren().add(currentScreen);
    }
}