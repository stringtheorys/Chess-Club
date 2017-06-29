package Game.UI;

import Core.Constants;
import Core.Log;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MovesUI extends VBox implements Constants {

    //Attributes
    private ChessGame parent;

    private Label topLabel;
    private VBox playerMoves;
    private Label player1Label;
    private Label player2Label;
    private ScrollPane player1MoveScroll;
    private ScrollPane player2MoveScroll;
    private VBox player1Moves;
    private VBox player2Moves;

    //Constructor
    public MovesUI(ChessGame parent){
        super();

        this.parent = parent;
        setBorder(blackBorder);

        topLabel = new Label("Player Moves");
        topLabel.setFont(mediumFont);
        this.getChildren().add(topLabel);

        playerMoves = new VBox();

        //Player 1 moves
        player1MoveScroll = new ScrollPane();
        player1Moves = new VBox();
        player1Label = new Label("Player 1");
        player1Label.setFont(smallFont);
        player1Moves.getChildren().add(player1Label);
        setMargin(player1Label, mediumMargin);
        player1MoveScroll.setContent(player1Moves);
        playerMoves.getChildren().add(player1MoveScroll);
        //Player 2 moves
        player2MoveScroll = new ScrollPane();
        player2Moves = new VBox();
        player2Label = new Label("Player 2");
        player2Label.setFont(smallFont);
        player2Moves.getChildren().add(player2Label);
        setMargin(player2Label, mediumMargin);
        player2MoveScroll.setContent(player2Moves);
        playerMoves.getChildren().add(player2MoveScroll);

        this.getChildren().add(playerMoves);
    }

    //Add player moves that adds the move to the player moves lists
    public void addPlayerMove(boolean white, int x1, int y1, int x2, int y2){
        String move = String.format("(%d, %d), (%d, %d)", x1, y1, x2, y2);
        Label moveLabel = new Label(move);
        moveLabel.setFont(mediumFont);
        if (white) {
            player1Moves.getChildren().add(moveLabel);
            setMargin(moveLabel, smallMargin);
        } else {
            player2Moves.getChildren().add(moveLabel);
            setMargin(moveLabel, smallMargin);
        }
        Log.log("Player move added to the moveUI :" + move);
    }
}
