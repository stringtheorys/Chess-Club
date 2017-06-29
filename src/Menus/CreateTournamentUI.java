package Menus;

import Alerts.MessageAlert;
import Core.Constants;
import Core.Log;
import Core.ScreenController;
import Database.TournamentDB_interface;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class CreateTournamentUI extends VBox implements Constants {

    //Attributes
    private ScreenController parent;
    private int AdminId;
    private int AccessLevel;
    private ArrayList<PlayerButton> playerButtons;

    private Button menuButton;
    private Label topLabel;
    private HBox topBox;

    //Tournament name and size
    private Label tournamentNameLabel;
    private TextField tournamentNameTextField;
    private Label tournamentSizeLabel;
    private ComboBox<String> tournamentSizeCombo;

    //Player box
    private Label tournamentPlayersLabel;
    private HBox playerBox;
    private VBox leftPlayerBox;
    private VBox rightPlayerBox;
    private HBox playerInfoBox;
    private Label playerNameLabel;
    private TextField playerNameTextField;
    private Label playerSeedLabel;
    private TextField playerSeedTextField;
    private HBox bottomBox;

    //Buttons
    private Button addPlayerButton;
    private Button createTournamentButton;

    //Constructor
    public CreateTournamentUI(ScreenController parent, int AdminId, int AccessLevel){
        super();
        this.parent = parent;
        this.AdminId = AdminId;
        this.AccessLevel = AccessLevel;

        Log.logStatus("Creating the UI for Create Tournament", '+');
        initGui();

        playerButtons = new ArrayList<>();
    }

    //Creates the UI
    private void initGui(){

        menuButton = new Button("Menu");
        menuButton.setFont(mediumFont);
        menuButton.setOnAction(event -> menu());
        this.getChildren().add(menuButton);
        setMargin(menuButton, rightMargin);

        //Top labels
        topLabel = new Label("Create Tournament");
        topLabel.setFont(extraLargeFont);
        this.getChildren().add(topLabel);
        setMargin(topLabel, largeMargin);

        //Tournament information
        topBox = new HBox();
        tournamentNameLabel = new Label("Tournament Name :");
        tournamentNameLabel.setFont(mediumFont);
        topBox.getChildren().add(tournamentNameLabel);
        tournamentNameTextField = new TextField();
        tournamentNameTextField.setMinWidth(textFieldLength);
        topBox.getChildren().add(tournamentNameTextField);
        HBox.setMargin(tournamentNameTextField, smallRightMargin);
        tournamentSizeLabel = new Label("Player Number :");
        tournamentSizeLabel.setFont(mediumFont);
        topBox.getChildren().add(tournamentSizeLabel);
        tournamentSizeCombo = new ComboBox<>();
        tournamentSizeCombo.getItems().addAll(
                    "2","4","8","16","32"
        );
        tournamentSizeCombo.setValue(tournamentSizeCombo.getItems().get(0));
        topBox.getChildren().add(tournamentSizeCombo);
        this.getChildren().add(topBox);
        setMargin(topBox, mediumMargin);

        //Tournament player information
        tournamentPlayersLabel = new Label("Players");
        tournamentPlayersLabel.setFont(mediumFont);
        this.getChildren().add(tournamentPlayersLabel);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPrefSize(200, 200);
        playerBox = new HBox();
        playerBox.setAlignment(Pos.TOP_CENTER);
        playerBox.setBorder(blackBorder);
        leftPlayerBox = new VBox();
        playerBox.getChildren().add(leftPlayerBox);
        HBox.setMargin(leftPlayerBox, smallRightMargin);
        rightPlayerBox = new VBox();
        playerBox.getChildren().add(rightPlayerBox);
        scrollPane.setContent(playerBox);
        this.getChildren().add(scrollPane);
        setMargin(scrollPane, mediumMargin);

        //Button information
        playerInfoBox = new HBox();
        playerNameLabel = new Label("Player Name :");
        playerNameLabel.setFont(mediumFont);
        playerInfoBox.getChildren().add(playerNameLabel);
        playerNameTextField = new TextField();
        playerNameTextField.setPrefWidth(textFieldLength);
        playerNameTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.TAB)){
                playerSeedTextField.requestFocus();
            }
        });
        playerInfoBox.getChildren().add(playerNameTextField);
        HBox.setMargin(playerNameTextField, smallRightMargin);
        playerSeedLabel = new Label("Player Seed :");
        playerSeedLabel.setFont(mediumFont);
        playerInfoBox.getChildren().add(playerSeedLabel);
        playerSeedTextField = new TextField();
        playerSeedTextField.setPrefWidth(textFieldLength);
        playerSeedTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)){
                createPlayer();
            }
        });
        playerInfoBox.getChildren().add(playerSeedTextField);
        this.getChildren().add(playerInfoBox);
        setMargin(playerInfoBox, mediumMargin);

        bottomBox = new HBox();

        //Buttons
        addPlayerButton = new Button("Add player");
        addPlayerButton.setFont(mediumFont);
        addPlayerButton.setOnAction(event -> createPlayer());
        bottomBox.getChildren().add(addPlayerButton);
        bottomBox.setMargin(addPlayerButton, smallRightMargin);

        createTournamentButton = new Button("Create tournament");
        createTournamentButton.setFont(mediumFont);
        createTournamentButton.setOnAction(event -> createTournament());
        bottomBox.getChildren().add(createTournamentButton);
        bottomBox.setMargin(createTournamentButton, smallRightMargin);

        this.getChildren().add(bottomBox);
    }

    //Change menu
    private void menu(){
        Log.log("Menu button pressed");
        parent.showPlayChess(AdminId, AccessLevel);
    }

    //Create a player, add to list of players
    private void createPlayer(){
        String playerName;
        String playerSeed;
        int tournamentSize = Integer.parseInt(tournamentSizeCombo.getValue());
        if (playerButtons.size() == tournamentSize){
            MessageAlert.MaxPlayerNum();
        } else if (playerButtons.size() < tournamentSize){
            playerName = playerNameTextField.getText();
            playerSeed = playerSeedTextField.getText();
            if (playerNameTextField.getText().equals("")){
                MessageAlert.MissingInput("Player Name");
            } else if (playerSeedTextField.getText().equals("")){
                addPlayer(playerName, 0);
            } else {
                try {
                    int PlayerSeed = Integer.parseInt(playerSeed);
                    addPlayer(playerName, PlayerSeed);
                } catch(NumberFormatException ex) {
                    MessageAlert.IncorrectInput("Player seed");
                }
            }
        } else {
            MessageAlert.MaxTournamentSize();
        }
    }

    //Create the tournament
    private void createTournament(){
        String tournamentName = tournamentNameTextField.getText();
        int tournamentSize = Integer.parseInt(tournamentSizeCombo.getValue());
        if (tournamentName.equals("")) {
            MessageAlert.TournamentName();
        } else if (tournamentSize != playerButtons.size()) {
            MessageAlert.MorePlayersNeeded();
        } else {
            String[] playerNameInfo = new String[tournamentSize];
            int pos = 0;
            for (PlayerButton playerButton : playerButtons){
                playerNameInfo[pos] = playerButton.getPlayerName();
                pos++;
            }
            int result = TournamentDB_interface.createTournament(AdminId, tournamentName, playerNameInfo);
            if (result == -1){
                MessageAlert.DataBaseConnectionAlert();
            } else if (result == 0){
                Log.logStatus("Error on create tournament", '-');
            } else {
                Log.logStatus("Create tournament complete", '+');
                parent.showPlayChess(AdminId, AccessLevel);
            }
        }

    }

    //Add the player
    private void addPlayer(String playerName, int playerSeed){
        PlayerButton playerButton = new PlayerButton(this, playerName, playerSeed);

        int insertPos = 0;
        for (PlayerButton button : playerButtons){
            if (button.getPlayerSeed() < playerSeed){
                break;
            } else {
                insertPos ++;
            }
        }
        for (int pos = insertPos; pos < playerButtons.size(); pos++){
            if (pos % 2 == 0){
                leftPlayerBox.getChildren().remove(playerButtons.get(pos));
            } else {
                rightPlayerBox.getChildren().remove(playerButtons.get(pos));
            }
        }
        playerButtons.add(insertPos, playerButton);
        for (int pos = insertPos; pos < playerButtons.size(); pos++){
            if (pos % 2 == 0){
                leftPlayerBox.getChildren().add(playerButtons.get(pos));
            } else {
                rightPlayerBox.getChildren().add(playerButtons.get(pos));
            }
        }
    }

    //Delete the player
    private void deletePlayer(PlayerButton playerButton){
        int buttonPos = playerButtons.indexOf(playerButton);

        for (int pos = buttonPos; pos < playerButtons.size(); pos ++){
            if (pos % 2 == 0){
                leftPlayerBox.getChildren().remove(playerButtons.get(pos));
            } else {
                rightPlayerBox.getChildren().remove(playerButtons.get(pos));
            }
        }
        playerButtons.remove(playerButton);
        for (int pos = buttonPos; pos < playerButtons.size(); pos ++){
            if (pos % 2 == 0){
                leftPlayerBox.getChildren().add(playerButtons.get(pos));
            } else {
                rightPlayerBox.getChildren().add(playerButtons.get(pos));
            }
        }
    }

    //Player button class
    private class PlayerButton extends HBox {
        //Attributes
        private Label playerNameLabel;
        private Label playerSeedLabel;
        private Button deletePlayerButton;
        private Image deleteImage;
        private ImageView deleteImageView;

        private String playerName;
        private int playerSeed;
        private CreateTournamentUI parent;

        //Constructor
        public PlayerButton(CreateTournamentUI parent, String playerName, int playerSeed){
            this.parent = parent;
            this.playerName = playerName;
            this.playerSeed = playerSeed;

            this.setBorder(blackBorder);
            this.initGui();
        }

        //Create the UI
        private void initGui(){
            playerNameLabel = new Label(this.playerName);
            playerNameLabel.setFont(mediumFont);
            this.getChildren().add(playerNameLabel);
            setMargin(playerNameLabel, smallRightMargin);

            playerSeedLabel = new Label(Integer.toString(this.playerSeed));
            playerSeedLabel.setFont(mediumFont);
            this.getChildren().add(playerSeedLabel);
            setMargin(playerSeedLabel, smallRightMargin);

            deletePlayerButton = new Button();

            deleteImage = new Image(getClass().getResourceAsStream("closeIcon.png"));
            deleteImageView = new ImageView(deleteImage);
            deleteImageView.setFitHeight(20);
            deleteImageView.setPreserveRatio(true);

            deletePlayerButton.setGraphic(deleteImageView);
            deletePlayerButton.setOnAction(event -> deletePlayer());
            this.getChildren().add(deletePlayerButton);
        }

        //Delete the player function
        private void deletePlayer(){
            Log.log("Delete player :" + playerName);
            parent.deletePlayer(this);
        }

        //Gets information
        public int getPlayerSeed(){
            return this.playerSeed;
        }
        public String getPlayerName() {
            return this.playerName;
        }
    }
}
