package Game.UI;

import Core.ScreenController;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ChessGame extends VBox {

    //Attributes
    private ScreenController parent;

    private HBox game;
    private VBox sideBar;

    private MovesUI movesUI;
    private PlayerBarUI playerBarUI;
    private BoardUI chessBoardUI;

    private int ChessMode;
    private int UserId;
    private int AccessLevel;

    //Constructor
    public ChessGame(ScreenController parent, int ChessMode) {
        super();

        this.ChessMode = ChessMode;
        this.parent = parent;

        initGui();
    }
    //Constructor with user information
    public ChessGame(ScreenController parent, int ChessMode, int UserId, int AccessLevel){
        super();

        this.UserId = UserId;
        this.AccessLevel = AccessLevel;

        this.ChessMode = ChessMode;
        this.parent = parent;

        initGui();
    }

    //Changes the screen to play chess
    public void showPlayChess(){
        parent.setNormalSize();
        if (UserId > 0) {
            parent.showPlayChess(UserId, AccessLevel);
        } else {
            parent.showPlayChess();
        }
    }

    //Create the screen
    private void initGui(){

        game = new HBox();
        chessBoardUI = new BoardUI(this, ChessMode);
        game.getChildren().add(chessBoardUI);
        sideBar = new VBox();
        movesUI = new MovesUI(this);

        //Create the player bar based on the game mode
        if (ChessMode == 2) {
            playerBarUI = new PlayerBarUI(this, true);
        } else if (UserId > 0) {
            playerBarUI = new PlayerBarUI(this, UserId);
        } else {
            playerBarUI = new PlayerBarUI(this);
        }
        sideBar.getChildren().add(playerBarUI);
        sideBar.getChildren().add(movesUI);
        game.getChildren().add(sideBar);
        this.getChildren().add(game);
    }

    //Adds the player move to the player move UI
    public void addPlayerMove(boolean white, int x1, int y1, int x2, int y2){
        movesUI.addPlayerMove(white, x1, y1, x2, y2);
    }
}
