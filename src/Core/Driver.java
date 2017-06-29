package Core;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

//This is the main class which runs and starts the gui
public class Driver extends Application implements Constants {

    //Attributes in creation
    private Stage window;
    private Scene mainScene;
    private ScreenController screenController;

    //Main function called at the start of the program which launches the gui and then runs the start function
    public static void main(String... args){
        try {
            launch(Driver.class, args);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    //Start function which runs to create the gui
    @Override
    public void start(Stage primaryStage) {
        //Main window
        window = primaryStage;

        //The screen controller that controls what is on the screen
        screenController = new ScreenController(this);

        //The scene is what is on the scene
        mainScene = new Scene(screenController);

        //Sets the window values
        window.setScene(mainScene);
        window.setTitle("Chess Club");
        setNormalSize();
        window.getIcons().add(new Image(getClass().getResourceAsStream("ChessIcon.png")));

        //Starts the log class
        Log.startFile();
        Log.logStatus("start : Driver opening window", '+');

        //Shows the window
        window.show();
    }

    @Override
    public void stop() {
        Log.log("stop : Application is closed");
        System.exit(0);
    }

    //Changes the window size to the chess game size
    public void setChessGameSize(){
        window.setHeight(chessHeight);
        window.setWidth(chessWidth);
    }
    //Changes the window size to the menu size
    public void setNormalSize(){
        window.setHeight(menuHeight);
        window.setWidth(menuWidth);
    }
}
