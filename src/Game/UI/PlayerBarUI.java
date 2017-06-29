package Game.UI;

import Core.Constants;
import Core.Driver;
import Database.UserDB_interface;
import Game.Players.AlphaBetaPruning;
import Menus.PlayChessUI;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class PlayerBarUI extends VBox implements Constants {

    //Attributes
    ChessGame parent;
    private Label topLabel;
    private Label playerNameLabel;
    private Image playerImage;
    private ImageView playerImageView;
    private Button menuButton;

    //Constructor
    public PlayerBarUI(ChessGame parent){
        this.parent = parent;

        initGui();
    }

    //Constructor
    public PlayerBarUI(ChessGame parent, int playerId){
        this(parent);

        //Username, PhotoLocation
        String[] playerInfo = UserDB_interface.getUserInfo(playerId);
        playerNameLabel.setText(playerInfo[0]);

        playerImage = new Image(PlayChessUI.class.getResourceAsStream("Sprites/"+playerInfo[1]),
                60, 60, true, true);
        playerImageView.setImage(playerImage);
    }

    //Constructor
    public PlayerBarUI(ChessGame parent, boolean white){
        this(parent);

        playerImage = new Image(AlphaBetaPruning.class.getResourceAsStream("Terminator.jpg"),
                60, 60, true, true);
        playerImageView.setImage(playerImage);

        playerNameLabel.setText("Vs Computer");
    }

    //Create the UI
    private void initGui(){

        topLabel = new Label("Player Info");
        topLabel.setFont(mediumFont);
        this.getChildren().add(topLabel);
        setMargin(topLabel, mediumMargin);

        playerNameLabel = new Label("Local Game");
        playerNameLabel.setFont(mediumFont);
        this.getChildren().add(playerNameLabel);
        setMargin(playerNameLabel, smallMargin);

        playerImage = new Image(Driver.class.getResourceAsStream("ChessIcon.png"),
                60, 60, true, true);
        playerImageView = new ImageView(playerImage);
        this.getChildren().add(playerImageView);
        setMargin(playerImageView, mediumMargin);

        menuButton = new Button("Menu");
        menuButton.setFont(mediumFont);
        menuButton.setOnAction(event -> menu());
        this.getChildren().add(menuButton);
    }

    //Show the play chess menu
    private void menu(){
        parent.showPlayChess();
    }
}
